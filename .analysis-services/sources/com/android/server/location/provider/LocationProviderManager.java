package com.android.server.location.provider;

/* JADX INFO: loaded from: classes2.dex */
public class LocationProviderManager extends com.android.server.location.listeners.ListenerMultiplexer<java.lang.Object, com.android.server.location.provider.LocationProviderManager.LocationTransport, com.android.server.location.provider.LocationProviderManager.Registration, android.location.provider.ProviderRequest> implements com.android.server.location.provider.AbstractLocationProvider.Listener {
    private static final float FASTEST_INTERVAL_JITTER_PERCENTAGE = 0.1f;
    private static final long MAX_CURRENT_LOCATION_AGE_MS = 30000;
    private static final int MAX_FASTEST_INTERVAL_JITTER_MS = 30000;
    private static final long MAX_GET_CURRENT_LOCATION_TIMEOUT_MS = 30000;
    private static final long MAX_HIGH_POWER_INTERVAL_MS = 300000;
    private static final long MIN_COARSE_INTERVAL_MS = 600000;
    private static final long MIN_REQUEST_DELAY_MS = 30000;
    private static final int STATE_STARTED = 0;
    private static final int STATE_STOPPED = 2;
    private static final int STATE_STOPPING = 1;
    private static final long TEMPORARY_APP_ALLOWLIST_DURATION_MS = 10000;
    private static final java.lang.String TEST_PROVIDER = "test_provider";
    private static final java.lang.String WAKELOCK_TAG = "*location*";
    private static final long WAKELOCK_TIMEOUT_MS = 30000;
    private final com.android.server.location.injector.SettingsHelper.GlobalSettingChangedListener mAdasPackageAllowlistChangedListener;
    protected final com.android.server.location.injector.AlarmHelper mAlarmHelper;
    private final android.location.altitude.AltitudeConverter mAltitudeConverter;
    private final com.android.server.location.injector.AppForegroundHelper.AppForegroundListener mAppForegroundChangedListener;
    protected final com.android.server.location.injector.AppForegroundHelper mAppForegroundHelper;
    protected final com.android.server.location.injector.AppOpsHelper mAppOpsHelper;
    private final com.android.server.location.injector.SettingsHelper.GlobalSettingChangedListener mBackgroundThrottleIntervalChangedListener;
    private final com.android.server.location.injector.SettingsHelper.GlobalSettingChangedListener mBackgroundThrottlePackageWhitelistChangedListener;
    protected final android.content.Context mContext;
    private android.app.AlarmManager.OnAlarmListener mDelayedRegister;
    protected final com.android.server.location.injector.EmergencyHelper mEmergencyHelper;
    private final com.android.server.location.injector.EmergencyHelper.EmergencyStateChangedListener mEmergencyStateChangedListener;
    private final android.util.SparseBooleanArray mEnabled;
    private final java.util.ArrayList<android.location.LocationManagerInternal.ProviderEnabledListener> mEnabledListeners;
    private final com.android.server.location.injector.SettingsHelper.GlobalSettingChangedListener mIgnoreSettingsPackageWhitelistChangedListener;
    private volatile boolean mIsAltitudeConverterIdle;
    private final android.util.SparseArray<com.android.server.location.provider.LocationProviderManager.LastLocation> mLastLocations;
    private final com.android.server.location.injector.SettingsHelper.UserSettingChangedListener mLocationEnabledChangedListener;
    protected final com.android.server.location.fudger.LocationFudger mLocationFudger;
    protected final android.location.LocationManagerInternal mLocationManagerInternal;
    private final com.android.server.location.injector.SettingsHelper.UserSettingChangedListener mLocationPackageBlacklistChangedListener;
    protected final com.android.server.location.injector.LocationPermissionsHelper mLocationPermissionsHelper;
    private final com.android.server.location.injector.LocationPermissionsHelper.LocationPermissionsListener mLocationPermissionsListener;
    private final com.android.server.location.injector.LocationPowerSaveModeHelper.LocationPowerSaveModeChangedListener mLocationPowerSaveModeChangedListener;
    protected final com.android.server.location.injector.LocationPowerSaveModeHelper mLocationPowerSaveModeHelper;
    private com.android.server.location.provider.ILocationProviderManagerWrapper mLocationProviderManagerWrapper;
    protected final com.android.server.location.settings.LocationSettings mLocationSettings;
    protected final com.android.server.location.injector.LocationUsageLogger mLocationUsageLogger;
    private final com.android.server.location.settings.LocationSettings.LocationUserSettingsListener mLocationUserSettingsListener;
    protected final java.lang.String mName;
    private final com.android.server.location.injector.PackageResetHelper mPackageResetHelper;
    private final com.android.server.location.injector.PackageResetHelper.Responder mPackageResetResponder;
    private final com.android.server.location.provider.PassiveLocationProviderManager mPassiveManager;
    protected final com.android.server.location.provider.MockableLocationProvider mProvider;
    private final java.util.concurrent.CopyOnWriteArrayList<android.location.provider.IProviderRequestListener> mProviderRequestListeners;
    private final java.util.Collection<java.lang.String> mRequiredPermissions;
    private final com.android.server.location.injector.ScreenInteractiveHelper.ScreenInteractiveChangedListener mScreenInteractiveChangedListener;
    protected final com.android.server.location.injector.ScreenInteractiveHelper mScreenInteractiveHelper;
    protected final com.android.server.location.injector.SettingsHelper mSettingsHelper;
    private int mState;
    private com.android.server.location.provider.LocationProviderManager.StateChangedListener mStateChangedListener;
    private final com.android.server.location.injector.UserInfoHelper.UserListener mUserChangedListener;
    protected final com.android.server.location.injector.UserInfoHelper mUserHelper;
    private static com.android.server.location.interfaces.IOplusLBSMainClass mOplusLbsClass = null;
    private static com.android.server.location.interfaces.ILocationFreezeProc mLocationFreeze = null;

    protected interface LocationTransport {
        void deliverOnFlushComplete(int i) throws java.lang.Exception;

        void deliverOnLocationChanged(android.location.LocationResult locationResult, android.os.IRemoteCallback iRemoteCallback) throws java.lang.Exception;
    }

    protected interface ProviderTransport {
        void deliverOnProviderEnabledChanged(java.lang.String str, boolean z) throws java.lang.Exception;
    }

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    private @interface State {
    }

    public interface StateChangedListener {
        void onStateChanged(java.lang.String str, com.android.server.location.provider.AbstractLocationProvider.State state, com.android.server.location.provider.AbstractLocationProvider.State state2);
    }

    @Override // com.android.server.location.listeners.ListenerMultiplexer
    protected /* bridge */ /* synthetic */ android.location.provider.ProviderRequest mergeRegistrations(java.util.Collection collection) {
        return mergeRegistrations((java.util.Collection<com.android.server.location.provider.LocationProviderManager.Registration>) collection);
    }

    @Override // com.android.server.location.listeners.ListenerMultiplexer
    protected /* bridge */ /* synthetic */ boolean registerWithService(android.location.provider.ProviderRequest providerRequest, java.util.Collection collection) {
        return registerWithService2(providerRequest, (java.util.Collection<com.android.server.location.provider.LocationProviderManager.Registration>) collection);
    }

    @Override // com.android.server.location.listeners.ListenerMultiplexer
    protected /* bridge */ /* synthetic */ boolean reregisterWithService(android.location.provider.ProviderRequest providerRequest, android.location.provider.ProviderRequest providerRequest2, java.util.Collection collection) {
        return reregisterWithService2(providerRequest, providerRequest2, (java.util.Collection<com.android.server.location.provider.LocationProviderManager.Registration>) collection);
    }

    protected static final class LocationListenerTransport implements com.android.server.location.provider.LocationProviderManager.LocationTransport, com.android.server.location.provider.LocationProviderManager.ProviderTransport {
        private final android.location.ILocationListener mListener;

        LocationListenerTransport(android.location.ILocationListener listener) {
            this.mListener = (android.location.ILocationListener) java.util.Objects.requireNonNull(listener);
        }

        @Override // com.android.server.location.provider.LocationProviderManager.LocationTransport
        public void deliverOnLocationChanged(android.location.LocationResult locationResult, android.os.IRemoteCallback onCompleteCallback) throws android.os.RemoteException {
            try {
                this.mListener.onLocationChanged(locationResult.asList(), onCompleteCallback);
            } catch (java.lang.RuntimeException e) {
                e.printStackTrace();
            }
        }

        @Override // com.android.server.location.provider.LocationProviderManager.LocationTransport
        public void deliverOnFlushComplete(int requestCode) throws android.os.RemoteException {
            try {
                this.mListener.onFlushComplete(requestCode);
            } catch (java.lang.RuntimeException e) {
                e.printStackTrace();
            }
        }

        @Override // com.android.server.location.provider.LocationProviderManager.ProviderTransport
        public void deliverOnProviderEnabledChanged(java.lang.String provider, boolean enabled) throws android.os.RemoteException {
            try {
                this.mListener.onProviderEnabledChanged(provider, enabled);
            } catch (java.lang.RuntimeException e) {
                e.printStackTrace();
            }
        }
    }

    protected static final class LocationPendingIntentTransport implements com.android.server.location.provider.LocationProviderManager.LocationTransport, com.android.server.location.provider.LocationProviderManager.ProviderTransport {
        private final android.content.Context mContext;
        private final android.app.PendingIntent mPendingIntent;

        public LocationPendingIntentTransport(android.content.Context context, android.app.PendingIntent pendingIntent) {
            this.mContext = context;
            this.mPendingIntent = pendingIntent;
        }

        @Override // com.android.server.location.provider.LocationProviderManager.LocationTransport
        public void deliverOnLocationChanged(android.location.LocationResult locationResult, final android.os.IRemoteCallback onCompleteCallback) throws android.app.PendingIntent.CanceledException {
            android.app.BroadcastOptions options = android.app.BroadcastOptions.makeBasic();
            options.setDontSendToRestrictedApps(true);
            options.setTemporaryAppAllowlist(10000L, 0, com.android.internal.util.FrameworkStatsLog.APP_BACKGROUND_RESTRICTIONS_INFO__EXEMPTION_REASON__REASON_LOCATION_PROVIDER, "");
            android.content.Intent intent = new android.content.Intent().putExtra("location", locationResult.getLastLocation());
            if (locationResult.size() > 1) {
                intent.putExtra("locations", (android.os.Parcelable[]) locationResult.asList().toArray(new android.location.Location[0]));
            }
            java.lang.Runnable callback = null;
            if (onCompleteCallback != null) {
                callback = new java.lang.Runnable() { // from class: com.android.server.location.provider.LocationProviderManager$LocationPendingIntentTransport$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        com.android.server.location.provider.LocationProviderManager.LocationPendingIntentTransport.lambda$deliverOnLocationChanged$0(onCompleteCallback);
                    }
                };
            }
            com.android.server.location.provider.LocationProviderManager.PendingIntentSender.send(this.mPendingIntent, this.mContext, intent, callback, options.toBundle());
        }

        static /* synthetic */ void lambda$deliverOnLocationChanged$0(android.os.IRemoteCallback onCompleteCallback) {
            try {
                onCompleteCallback.sendResult((android.os.Bundle) null);
            } catch (android.os.RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }

        @Override // com.android.server.location.provider.LocationProviderManager.LocationTransport
        public void deliverOnFlushComplete(int requestCode) throws android.app.PendingIntent.CanceledException {
            android.app.BroadcastOptions options = android.app.BroadcastOptions.makeBasic();
            options.setDontSendToRestrictedApps(true);
            options.setPendingIntentBackgroundActivityLaunchAllowed(false);
            this.mPendingIntent.send(this.mContext, 0, new android.content.Intent().putExtra("flushComplete", requestCode), null, null, null, options.toBundle());
        }

        @Override // com.android.server.location.provider.LocationProviderManager.ProviderTransport
        public void deliverOnProviderEnabledChanged(java.lang.String provider, boolean enabled) throws android.app.PendingIntent.CanceledException {
            android.app.BroadcastOptions options = android.app.BroadcastOptions.makeBasic();
            options.setDontSendToRestrictedApps(true);
            this.mPendingIntent.send(this.mContext, 0, new android.content.Intent().putExtra("providerEnabled", enabled), null, null, null, options.toBundle());
        }
    }

    protected static final class GetCurrentLocationTransport implements com.android.server.location.provider.LocationProviderManager.LocationTransport {
        private final android.location.ILocationCallback mCallback;

        GetCurrentLocationTransport(android.location.ILocationCallback callback) {
            this.mCallback = (android.location.ILocationCallback) java.util.Objects.requireNonNull(callback);
        }

        @Override // com.android.server.location.provider.LocationProviderManager.LocationTransport
        public void deliverOnLocationChanged(android.location.LocationResult locationResult, android.os.IRemoteCallback onCompleteCallback) throws android.os.RemoteException {
            com.android.internal.util.Preconditions.checkState(onCompleteCallback == null);
            try {
                if (locationResult != null) {
                    this.mCallback.onLocation(locationResult.getLastLocation());
                } else {
                    this.mCallback.onLocation((android.location.Location) null);
                }
            } catch (java.lang.RuntimeException e) {
                e.printStackTrace();
            }
        }

        @Override // com.android.server.location.provider.LocationProviderManager.LocationTransport
        public void deliverOnFlushComplete(int requestCode) {
        }
    }

    protected abstract class Registration extends com.android.server.location.listeners.RemovableListenerRegistration<java.lang.Object, com.android.server.location.provider.LocationProviderManager.LocationTransport> {
        private final android.location.LocationRequest mBaseRequest;
        private boolean mBypassPermitted;
        private boolean mForeground;
        private final android.location.util.identity.CallerIdentity mIdentity;
        private boolean mIsUsingHighPower;
        private android.location.Location mLastLocation;
        private final int mPermissionLevel;
        private boolean mPermitted;
        private android.location.LocationRequest mProviderLocationRequest;

        /* JADX INFO: Access modifiers changed from: package-private */
        public abstract com.android.internal.listeners.ListenerExecutor.ListenerOperation<com.android.server.location.provider.LocationProviderManager.LocationTransport> acceptLocationChange(android.location.LocationResult locationResult);

        protected Registration(android.location.LocationRequest request, android.location.util.identity.CallerIdentity identity, java.util.concurrent.Executor executor, com.android.server.location.provider.LocationProviderManager.LocationTransport transport, int permissionLevel) {
            super(executor, transport);
            this.mLastLocation = null;
            com.android.internal.util.Preconditions.checkArgument(identity.getListenerId() != null);
            com.android.internal.util.Preconditions.checkArgument(permissionLevel > 0);
            com.android.internal.util.Preconditions.checkArgument(!request.getWorkSource().isEmpty());
            this.mBaseRequest = (android.location.LocationRequest) java.util.Objects.requireNonNull(request);
            this.mIdentity = (android.location.util.identity.CallerIdentity) java.util.Objects.requireNonNull(identity);
            this.mPermissionLevel = permissionLevel;
            this.mProviderLocationRequest = request;
        }

        public final android.location.util.identity.CallerIdentity getIdentity() {
            return this.mIdentity;
        }

        public final android.location.LocationRequest getRequest() {
            android.location.LocationRequest locationRequest;
            synchronized (com.android.server.location.provider.LocationProviderManager.this.mMultiplexerLock) {
                locationRequest = this.mProviderLocationRequest;
            }
            return locationRequest;
        }

        @Override // com.android.server.location.listeners.RemovableListenerRegistration
        protected void onRegister() {
            super.onRegister();
            android.util.Log.d(com.android.server.location.LocationManagerService.TAG, com.android.server.location.provider.LocationProviderManager.this.mName + " provider added registration from " + getIdentity() + " -> " + getRequest() + " permissionLevel: " + this.mPermissionLevel + " permitted: " + com.android.server.location.provider.LocationProviderManager.this.mLocationPermissionsHelper.hasLocationPermissions(this.mPermissionLevel, getIdentity()));
            com.android.server.location.eventlog.LocationEventLog.EVENT_LOG.logProviderClientRegistered(com.android.server.location.provider.LocationProviderManager.this.mName, getIdentity(), this.mBaseRequest);
            onLocationPermissionsChanged();
            onBypassLocationPermissionsChanged(com.android.server.location.provider.LocationProviderManager.this.mEmergencyHelper.isInEmergency(0L));
            this.mForeground = com.android.server.location.provider.LocationProviderManager.this.mAppForegroundHelper.isAppForeground(getIdentity().getUid());
            if (com.android.server.location.provider.LocationProviderManager.mOplusLbsClass != null) {
                com.android.server.location.provider.LocationProviderManager.mOplusLbsClass.startRequesting(getIdentity(), com.android.server.location.provider.LocationProviderManager.this.mName, getRequest(), this.mForeground, "" + getKey().hashCode());
            }
            this.mProviderLocationRequest = calculateProviderLocationRequest();
            this.mIsUsingHighPower = isUsingHighPower();
            if (this.mForeground) {
                com.android.server.location.eventlog.LocationEventLog.EVENT_LOG.logProviderClientForeground(com.android.server.location.provider.LocationProviderManager.this.mName, getIdentity());
            }
        }

        @Override // com.android.server.location.listeners.RemovableListenerRegistration, com.android.server.location.listeners.ListenerRegistration
        protected void onUnregister() {
            com.android.server.location.eventlog.LocationEventLog.EVENT_LOG.logProviderClientUnregistered(com.android.server.location.provider.LocationProviderManager.this.mName, getIdentity());
            if (com.android.server.location.provider.LocationProviderManager.mOplusLbsClass != null) {
                com.android.server.location.provider.LocationProviderManager.mOplusLbsClass.stopRequesting(getIdentity(), com.android.server.location.provider.LocationProviderManager.this.mName, getRequest(), "" + getKey().hashCode());
            }
            android.util.Log.d(com.android.server.location.LocationManagerService.TAG, com.android.server.location.provider.LocationProviderManager.this.mName + " provider removed registration from " + getIdentity());
            super.onUnregister();
        }

        @Override // com.android.server.location.listeners.ListenerRegistration
        protected void onActive() {
            com.android.server.location.eventlog.LocationEventLog.EVENT_LOG.logProviderClientActive(com.android.server.location.provider.LocationProviderManager.this.mName, getIdentity());
            if (!getRequest().isHiddenFromAppOps()) {
                com.android.server.location.provider.LocationProviderManager.this.mAppOpsHelper.startOpNoThrow(41, getIdentity());
            }
            onHighPowerUsageChanged();
        }

        @Override // com.android.server.location.listeners.ListenerRegistration
        protected void onInactive() {
            onHighPowerUsageChanged();
            if (!getRequest().isHiddenFromAppOps()) {
                com.android.server.location.provider.LocationProviderManager.this.mAppOpsHelper.finishOp(41, getIdentity());
            }
            com.android.server.location.eventlog.LocationEventLog.EVENT_LOG.logProviderClientInactive(com.android.server.location.provider.LocationProviderManager.this.mName, getIdentity());
        }

        final void setLastDeliveredLocation(android.location.Location location) {
            this.mLastLocation = location;
        }

        public final android.location.Location getLastDeliveredLocation() {
            android.location.Location location;
            synchronized (com.android.server.location.provider.LocationProviderManager.this.mMultiplexerLock) {
                location = this.mLastLocation;
            }
            return location;
        }

        public int getPermissionLevel() {
            int i;
            synchronized (com.android.server.location.provider.LocationProviderManager.this.mMultiplexerLock) {
                i = this.mPermissionLevel;
            }
            return i;
        }

        public final boolean isForeground() {
            boolean z;
            synchronized (com.android.server.location.provider.LocationProviderManager.this.mMultiplexerLock) {
                z = this.mForeground;
            }
            return z;
        }

        public final boolean isPermitted() {
            boolean z;
            synchronized (com.android.server.location.provider.LocationProviderManager.this.mMultiplexerLock) {
                z = this.mPermitted || this.mBypassPermitted;
            }
            return z;
        }

        public final boolean isOnlyBypassPermitted() {
            boolean z;
            synchronized (com.android.server.location.provider.LocationProviderManager.this.mMultiplexerLock) {
                z = this.mBypassPermitted && !this.mPermitted;
            }
            return z;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$flush$1(final int requestCode) {
            executeOperation(new com.android.internal.listeners.ListenerExecutor.ListenerOperation() { // from class: com.android.server.location.provider.LocationProviderManager$Registration$$ExternalSyntheticLambda0
                public final void operate(java.lang.Object obj) throws java.lang.Exception {
                    ((com.android.server.location.provider.LocationProviderManager.LocationTransport) obj).deliverOnFlushComplete(requestCode);
                }
            });
        }

        public final void flush(final int requestCode) {
            com.android.server.location.provider.LocationProviderManager.this.mProvider.getController().flush(new java.lang.Runnable() { // from class: com.android.server.location.provider.LocationProviderManager$Registration$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$flush$1(requestCode);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.server.location.listeners.RemovableListenerRegistration
        public final com.android.server.location.listeners.ListenerMultiplexer<java.lang.Object, ? super com.android.server.location.provider.LocationProviderManager.LocationTransport, ?, ?> getOwner() {
            return com.android.server.location.provider.LocationProviderManager.this;
        }

        final boolean onProviderPropertiesChanged() {
            synchronized (com.android.server.location.provider.LocationProviderManager.this.mMultiplexerLock) {
                onHighPowerUsageChanged();
            }
            return false;
        }

        private void onHighPowerUsageChanged() {
            boolean isUsingHighPower = isUsingHighPower();
            if (isUsingHighPower != this.mIsUsingHighPower) {
                this.mIsUsingHighPower = isUsingHighPower;
                if (!getRequest().isHiddenFromAppOps()) {
                    if (this.mIsUsingHighPower) {
                        com.android.server.location.provider.LocationProviderManager.this.mAppOpsHelper.startOpNoThrow(42, getIdentity());
                    } else {
                        com.android.server.location.provider.LocationProviderManager.this.mAppOpsHelper.finishOp(42, getIdentity());
                    }
                }
            }
        }

        private boolean isUsingHighPower() {
            android.location.provider.ProviderProperties properties = com.android.server.location.provider.LocationProviderManager.this.getProperties();
            return properties != null && isActive() && getRequest().getIntervalMillis() < 300000 && properties.getPowerUsage() == 3;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public final boolean onLocationPermissionsChanged(java.lang.String packageName) {
            synchronized (com.android.server.location.provider.LocationProviderManager.this.mMultiplexerLock) {
                if (packageName != null) {
                    if (!getIdentity().getPackageName().equals(packageName)) {
                        return false;
                    }
                }
                return onLocationPermissionsChanged();
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public final boolean onLocationPermissionsChanged(int uid) {
            synchronized (com.android.server.location.provider.LocationProviderManager.this.mMultiplexerLock) {
                if (getIdentity().getUid() != uid) {
                    return false;
                }
                return onLocationPermissionsChanged();
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public boolean onBypassLocationPermissionsChanged(boolean isInEmergency) {
            synchronized (com.android.server.location.provider.LocationProviderManager.this.mMultiplexerLock) {
                boolean bypassPermitted = android.location.flags.Flags.enableLocationBypass() && isInEmergency && com.android.server.location.provider.LocationProviderManager.this.mContext.checkPermission("android.permission.LOCATION_BYPASS", this.mIdentity.getPid(), this.mIdentity.getUid()) == 0;
                if (this.mBypassPermitted == bypassPermitted) {
                    return false;
                }
                if (com.android.server.location.LocationManagerService.D) {
                    android.util.Log.v(com.android.server.location.LocationManagerService.TAG, com.android.server.location.provider.LocationProviderManager.this.mName + " provider package " + getIdentity().getPackageName() + " bypass permitted = " + bypassPermitted);
                }
                this.mBypassPermitted = bypassPermitted;
                return true;
            }
        }

        private boolean onLocationPermissionsChanged() {
            boolean permitted = com.android.server.location.provider.LocationProviderManager.this.mLocationPermissionsHelper.hasLocationPermissions(this.mPermissionLevel, getIdentity());
            if (permitted != this.mPermitted) {
                if (com.android.server.location.LocationManagerService.D) {
                    android.util.Log.v(com.android.server.location.LocationManagerService.TAG, com.android.server.location.provider.LocationProviderManager.this.mName + " provider package " + getIdentity().getPackageName() + " permitted = " + permitted);
                }
                this.mPermitted = permitted;
                if (this.mPermitted) {
                    com.android.server.location.eventlog.LocationEventLog.EVENT_LOG.logProviderClientPermitted(com.android.server.location.provider.LocationProviderManager.this.mName, getIdentity());
                    return true;
                }
                com.android.server.location.eventlog.LocationEventLog.EVENT_LOG.logProviderClientUnpermitted(com.android.server.location.provider.LocationProviderManager.this.mName, getIdentity());
                return true;
            }
            return false;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public final boolean onAdasGnssLocationEnabledChanged(int userId) {
            synchronized (com.android.server.location.provider.LocationProviderManager.this.mMultiplexerLock) {
                if (getIdentity().getUserId() != userId) {
                    return false;
                }
                return onProviderLocationRequestChanged();
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public final boolean onForegroundChanged(int uid, boolean foreground) {
            synchronized (com.android.server.location.provider.LocationProviderManager.this.mMultiplexerLock) {
                if (getIdentity().getUid() != uid || foreground == this.mForeground) {
                    return false;
                }
                android.util.Log.v(com.android.server.location.LocationManagerService.TAG, com.android.server.location.provider.LocationProviderManager.this.mName + " provider uid " + uid + " foreground = " + foreground);
                this.mForeground = foreground;
                if (com.android.server.location.provider.LocationProviderManager.mOplusLbsClass != null) {
                    com.android.server.location.provider.LocationProviderManager.mOplusLbsClass.updateForeground(getIdentity().getPackageName(), com.android.server.location.provider.LocationProviderManager.this.mName, this.mForeground);
                }
                if (this.mForeground) {
                    com.android.server.location.eventlog.LocationEventLog.EVENT_LOG.logProviderClientForeground(com.android.server.location.provider.LocationProviderManager.this.mName, getIdentity());
                } else {
                    com.android.server.location.eventlog.LocationEventLog.EVENT_LOG.logProviderClientBackground(com.android.server.location.provider.LocationProviderManager.this.mName, getIdentity());
                }
                return onProviderLocationRequestChanged() || com.android.server.location.provider.LocationProviderManager.this.mLocationPowerSaveModeHelper.getLocationPowerSaveMode() == 3;
            }
        }

        final boolean onProviderLocationRequestChanged() {
            synchronized (com.android.server.location.provider.LocationProviderManager.this.mMultiplexerLock) {
                android.location.LocationRequest newRequest = calculateProviderLocationRequest();
                if (this.mProviderLocationRequest.equals(newRequest)) {
                    return false;
                }
                android.location.LocationRequest oldRequest = this.mProviderLocationRequest;
                this.mProviderLocationRequest = newRequest;
                onHighPowerUsageChanged();
                com.android.server.location.provider.LocationProviderManager.this.updateService();
                return oldRequest.isBypass() != newRequest.isBypass();
            }
        }

        private android.location.LocationRequest calculateProviderLocationRequest() {
            android.location.LocationRequest.Builder builder = new android.location.LocationRequest.Builder(this.mBaseRequest);
            if (this.mPermissionLevel < 2) {
                builder.setQuality(104);
                if (this.mBaseRequest.getIntervalMillis() < 600000) {
                    builder.setIntervalMillis(600000L);
                }
                if (this.mBaseRequest.getMinUpdateIntervalMillis() < 600000) {
                    builder.setMinUpdateIntervalMillis(600000L);
                }
            }
            boolean locationSettingsIgnored = this.mBaseRequest.isLocationSettingsIgnored();
            if (locationSettingsIgnored) {
                if (!com.android.server.location.provider.LocationProviderManager.this.mSettingsHelper.getIgnoreSettingsAllowlist().contains(getIdentity().getPackageName(), getIdentity().getAttributionTag()) && !com.android.server.location.provider.LocationProviderManager.this.mLocationManagerInternal.isProvider((java.lang.String) null, getIdentity())) {
                    locationSettingsIgnored = false;
                }
                builder.setLocationSettingsIgnored(locationSettingsIgnored);
            }
            boolean adasGnssBypass = this.mBaseRequest.isAdasGnssBypass();
            if (adasGnssBypass) {
                if (!com.android.server.am.IOplusSceneManager.APP_SCENE_GPS.equals(com.android.server.location.provider.LocationProviderManager.this.mName)) {
                    android.util.Log.e(com.android.server.location.LocationManagerService.TAG, "adas gnss bypass request received in non-gps provider");
                    adasGnssBypass = false;
                } else if (!com.android.server.location.provider.LocationProviderManager.this.mUserHelper.isCurrentUserId(getIdentity().getUserId()) || !com.android.server.location.provider.LocationProviderManager.this.mLocationSettings.getUserSettings(getIdentity().getUserId()).isAdasGnssLocationEnabled() || !com.android.server.location.provider.LocationProviderManager.this.mSettingsHelper.getAdasAllowlist().contains(getIdentity().getPackageName(), getIdentity().getAttributionTag())) {
                    adasGnssBypass = false;
                }
                builder.setAdasGnssBypass(adasGnssBypass);
            }
            if (!locationSettingsIgnored && !isThrottlingExempt()) {
                if (!this.mForeground) {
                    builder.setIntervalMillis(java.lang.Math.max(this.mBaseRequest.getIntervalMillis(), com.android.server.location.provider.LocationProviderManager.this.mSettingsHelper.getBackgroundThrottleIntervalMs()));
                }
                if (com.android.server.am.IOplusSceneManager.APP_SCENE_GPS.equals(com.android.server.location.provider.LocationProviderManager.this.mName) && com.android.server.location.provider.LocationProviderManager.mOplusLbsClass.checkRequestBlocked(com.android.server.location.provider.LocationProviderManager.this.mName, getIdentity().getPackageName())) {
                    builder.setIntervalMillis(Long.MAX_VALUE).setMinUpdateIntervalMillis(Long.MAX_VALUE);
                }
            }
            if ("fused".equals(com.android.server.location.provider.LocationProviderManager.this.mName) && com.android.server.location.provider.LocationProviderManager.mOplusLbsClass.isFlpReqLimited(getIdentity().getPackageName())) {
                builder.setQuality(104);
            }
            return builder.build();
        }

        private boolean isThrottlingExempt() {
            if (com.android.server.location.provider.LocationProviderManager.this.mSettingsHelper.getBackgroundThrottlePackageWhitelist().contains(getIdentity().getPackageName())) {
                return true;
            }
            return com.android.server.location.provider.LocationProviderManager.this.mLocationManagerInternal.isProvider((java.lang.String) null, getIdentity());
        }

        @Override // com.android.server.location.listeners.ListenerRegistration
        public java.lang.String toString() {
            java.lang.StringBuilder builder = new java.lang.StringBuilder();
            builder.append(getIdentity());
            android.util.ArraySet<java.lang.String> flags = new android.util.ArraySet<>(2);
            if (!isForeground()) {
                flags.add("bg");
            }
            if (!isPermitted()) {
                flags.add("na");
            }
            if (!flags.isEmpty()) {
                builder.append(" ").append(flags);
            }
            if (this.mPermissionLevel == 1) {
                builder.append(" (COARSE)");
            }
            builder.append(" ").append(getRequest());
            return builder.toString();
        }
    }

    protected abstract class LocationRegistration extends com.android.server.location.provider.LocationProviderManager.Registration implements android.app.AlarmManager.OnAlarmListener, android.location.LocationManagerInternal.ProviderEnabledListener {
        private long mExpirationRealtimeMs;
        private int mNumLocationsDelivered;
        private volatile com.android.server.location.provider.LocationProviderManager.ProviderTransport mProviderTransport;
        final android.os.PowerManager.WakeLock mWakeLock;
        final com.android.server.location.provider.LocationProviderManager.ExternalWakeLockReleaser mWakeLockReleaser;

        protected abstract void onProviderOperationFailure(com.android.internal.listeners.ListenerExecutor.ListenerOperation<com.android.server.location.provider.LocationProviderManager.ProviderTransport> listenerOperation, java.lang.Exception exc);

        protected <TTransport extends com.android.server.location.provider.LocationProviderManager.LocationTransport & com.android.server.location.provider.LocationProviderManager.ProviderTransport> LocationRegistration(android.location.LocationRequest request, android.location.util.identity.CallerIdentity identity, java.util.concurrent.Executor executor, TTransport transport, int permissionLevel) {
            super(request, identity, executor, transport, permissionLevel);
            this.mNumLocationsDelivered = 0;
            this.mExpirationRealtimeMs = Long.MAX_VALUE;
            this.mProviderTransport = transport;
            this.mWakeLock = ((android.os.PowerManager) java.util.Objects.requireNonNull((android.os.PowerManager) com.android.server.location.provider.LocationProviderManager.this.mContext.getSystemService(android.os.PowerManager.class))).newWakeLock(1, com.android.server.location.provider.LocationProviderManager.WAKELOCK_TAG);
            this.mWakeLock.setReferenceCounted(true);
            this.mWakeLock.setWorkSource(request.getWorkSource());
            this.mWakeLockReleaser = new com.android.server.location.provider.LocationProviderManager.ExternalWakeLockReleaser(identity, this.mWakeLock);
        }

        @Override // com.android.server.location.listeners.ListenerRegistration
        protected void onListenerUnregister() {
            this.mProviderTransport = null;
        }

        @Override // com.android.server.location.provider.LocationProviderManager.Registration, com.android.server.location.listeners.RemovableListenerRegistration
        protected void onRegister() {
            super.onRegister();
            long registerTimeMs = android.os.SystemClock.elapsedRealtime();
            this.mExpirationRealtimeMs = getRequest().getExpirationRealtimeMs(registerTimeMs);
            if (this.mExpirationRealtimeMs <= registerTimeMs) {
                onAlarm();
            } else if (this.mExpirationRealtimeMs < Long.MAX_VALUE) {
                com.android.server.location.provider.LocationProviderManager.this.mAlarmHelper.setDelayedAlarm(this.mExpirationRealtimeMs - registerTimeMs, this, null);
            }
            com.android.server.location.provider.LocationProviderManager.this.addEnabledListener(this);
            int userId = getIdentity().getUserId();
            if (!com.android.server.location.provider.LocationProviderManager.this.isEnabled(userId)) {
                onProviderEnabledChanged(com.android.server.location.provider.LocationProviderManager.this.mName, userId, false);
            }
        }

        @Override // com.android.server.location.provider.LocationProviderManager.Registration, com.android.server.location.listeners.RemovableListenerRegistration, com.android.server.location.listeners.ListenerRegistration
        protected void onUnregister() {
            com.android.server.location.provider.LocationProviderManager.this.removeEnabledListener(this);
            if (this.mExpirationRealtimeMs < Long.MAX_VALUE) {
                com.android.server.location.provider.LocationProviderManager.this.mAlarmHelper.cancel(this);
            }
            super.onUnregister();
        }

        @Override // com.android.server.location.provider.LocationProviderManager.Registration, com.android.server.location.listeners.ListenerRegistration
        protected void onActive() {
            android.location.Location lastLocation;
            super.onActive();
            if (android.app.compat.CompatChanges.isChangeEnabled(73144566L, getIdentity().getUid())) {
                long maxLocationAgeMs = getRequest().getIntervalMillis();
                android.location.Location lastDeliveredLocation = getLastDeliveredLocation();
                if (lastDeliveredLocation != null) {
                    maxLocationAgeMs = java.lang.Math.min(maxLocationAgeMs, lastDeliveredLocation.getElapsedRealtimeAgeMillis() - 1);
                }
                if (maxLocationAgeMs <= 30000 || (lastLocation = com.android.server.location.provider.LocationProviderManager.this.getLastLocationUnsafe(getIdentity().getUserId(), getPermissionLevel(), getRequest().isBypass(), maxLocationAgeMs)) == null) {
                    return;
                }
                executeOperation(acceptLocationChange(android.location.LocationResult.wrap(new android.location.Location[]{lastLocation})));
            }
        }

        @Override // android.app.AlarmManager.OnAlarmListener
        public void onAlarm() {
            if (com.android.server.location.LocationManagerService.D) {
                android.util.Log.d(com.android.server.location.LocationManagerService.TAG, com.android.server.location.provider.LocationProviderManager.this.mName + " provider registration " + getIdentity() + " expired at " + android.util.TimeUtils.formatRealtime(this.mExpirationRealtimeMs));
            }
            synchronized (com.android.server.location.provider.LocationProviderManager.this.mMultiplexerLock) {
                this.mExpirationRealtimeMs = Long.MAX_VALUE;
                remove();
            }
        }

        @Override // com.android.server.location.provider.LocationProviderManager.Registration
        com.android.internal.listeners.ListenerExecutor.ListenerOperation<com.android.server.location.provider.LocationProviderManager.LocationTransport> acceptLocationChange(android.location.LocationResult fineLocationResult) {
            int op;
            if ((com.android.server.location.provider.LocationProviderManager.mOplusLbsClass != null && !com.android.server.location.provider.LocationProviderManager.mOplusLbsClass.shouldReportFlpAsGps(fineLocationResult.getLastLocation(), getIdentity().getPackageName())) || !com.android.server.location.provider.LocationProviderManager.mOplusLbsClass.shouldReportPnetLocationAsGps(fineLocationResult.getLastLocation(), getIdentity().getPackageName())) {
                return null;
            }
            if (android.os.SystemClock.elapsedRealtime() >= this.mExpirationRealtimeMs) {
                if (com.android.server.location.LocationManagerService.D) {
                    android.util.Log.d(com.android.server.location.LocationManagerService.TAG, com.android.server.location.provider.LocationProviderManager.this.mName + " provider registration " + getIdentity() + " expired at " + android.util.TimeUtils.formatRealtime(this.mExpirationRealtimeMs));
                }
                remove();
                return null;
            }
            android.location.LocationResult permittedLocationResult = (android.location.LocationResult) java.util.Objects.requireNonNull(com.android.server.location.provider.LocationProviderManager.this.getPermittedLocationResult(fineLocationResult, getPermissionLevel()));
            android.location.LocationResult locationResult = permittedLocationResult.filter(new java.util.function.Predicate<android.location.Location>() { // from class: com.android.server.location.provider.LocationProviderManager.LocationRegistration.1
                private android.location.Location mPreviousLocation;

                {
                    this.mPreviousLocation = com.android.server.location.provider.LocationProviderManager.LocationRegistration.this.getLastDeliveredLocation();
                }

                @Override // java.util.function.Predicate
                public boolean test(android.location.Location location) {
                    if (java.lang.Double.isNaN(location.getLatitude()) || location.getLatitude() < -90.0d || location.getLatitude() > 90.0d || java.lang.Double.isNaN(location.getLongitude()) || location.getLongitude() < -180.0d || location.getLongitude() > 180.0d) {
                        android.util.Log.e(com.android.server.location.LocationManagerService.TAG, com.android.server.location.provider.LocationProviderManager.this.mName + " provider registration " + com.android.server.location.provider.LocationProviderManager.LocationRegistration.this.getIdentity() + " dropped delivery - invalid latitude or longitude.");
                        return false;
                    }
                    if (this.mPreviousLocation != null) {
                        long deltaMs = location.getElapsedRealtimeMillis() - this.mPreviousLocation.getElapsedRealtimeMillis();
                        long maxJitterMs = java.lang.Math.min((long) (com.android.server.location.provider.LocationProviderManager.LocationRegistration.this.getRequest().getIntervalMillis() * com.android.server.location.provider.LocationProviderManager.FASTEST_INTERVAL_JITTER_PERCENTAGE), 30000L);
                        if (deltaMs < com.android.server.location.provider.LocationProviderManager.LocationRegistration.this.getRequest().getMinUpdateIntervalMillis() - maxJitterMs) {
                            if (com.android.server.location.LocationManagerService.D) {
                                android.util.Log.v(com.android.server.location.LocationManagerService.TAG, com.android.server.location.provider.LocationProviderManager.this.mName + " provider registration " + com.android.server.location.provider.LocationProviderManager.LocationRegistration.this.getIdentity() + " dropped delivery - too fast (deltaMs=" + deltaMs + ").");
                            }
                            return false;
                        }
                        double smallestDisplacementM = com.android.server.location.provider.LocationProviderManager.LocationRegistration.this.getRequest().getMinUpdateDistanceMeters();
                        if (smallestDisplacementM > 0.0d && location.distanceTo(this.mPreviousLocation) <= smallestDisplacementM) {
                            if (com.android.server.location.LocationManagerService.D) {
                                android.util.Log.v(com.android.server.location.LocationManagerService.TAG, com.android.server.location.provider.LocationProviderManager.this.mName + " provider registration " + com.android.server.location.provider.LocationProviderManager.LocationRegistration.this.getIdentity() + " dropped delivery - too close");
                            }
                            return false;
                        }
                    }
                    this.mPreviousLocation = location;
                    return true;
                }
            });
            if (locationResult == null) {
                return null;
            }
            if (android.location.flags.Flags.enableLocationBypass() && isOnlyBypassPermitted()) {
                op = 147;
            } else {
                op = com.android.server.location.LocationPermissions.asAppOp(getPermissionLevel());
            }
            if (!com.android.server.location.provider.LocationProviderManager.this.mAppOpsHelper.noteOpNoThrow(op, getIdentity())) {
                if (com.android.server.location.LocationManagerService.D) {
                    android.util.Log.w(com.android.server.location.LocationManagerService.TAG, com.android.server.location.provider.LocationProviderManager.this.mName + " provider registration " + getIdentity() + " noteOp denied");
                }
                return null;
            }
            boolean useWakeLock = getRequest().getIntervalMillis() != Long.MAX_VALUE;
            return new com.android.server.location.provider.LocationProviderManager.LocationRegistration.AnonymousClass2(locationResult, useWakeLock);
        }

        /* JADX INFO: renamed from: com.android.server.location.provider.LocationProviderManager$LocationRegistration$2, reason: invalid class name */
        class AnonymousClass2 implements com.android.internal.listeners.ListenerExecutor.ListenerOperation<com.android.server.location.provider.LocationProviderManager.LocationTransport> {
            final /* synthetic */ android.location.LocationResult val$locationResult;
            final /* synthetic */ boolean val$useWakeLock;

            AnonymousClass2(android.location.LocationResult locationResult, boolean z) {
                this.val$locationResult = locationResult;
                this.val$useWakeLock = z;
            }

            public void onPreExecute() {
                com.android.server.location.provider.LocationProviderManager.LocationRegistration.this.setLastDeliveredLocation(this.val$locationResult.getLastLocation());
                if (this.val$useWakeLock) {
                    com.android.server.location.provider.LocationProviderManager.LocationRegistration.this.mWakeLock.acquire(30000L);
                }
            }

            public void operate(com.android.server.location.provider.LocationProviderManager.LocationTransport listener) throws java.lang.Exception {
                android.location.LocationResult deliverLocationResult;
                if (com.android.server.location.provider.LocationProviderManager.LocationRegistration.this.getIdentity().getPid() == android.os.Process.myPid()) {
                    deliverLocationResult = this.val$locationResult.deepCopy();
                } else {
                    deliverLocationResult = this.val$locationResult;
                }
                listener.deliverOnLocationChanged(deliverLocationResult, this.val$useWakeLock ? com.android.server.location.provider.LocationProviderManager.LocationRegistration.this.mWakeLockReleaser : null);
                if (com.android.server.location.provider.LocationProviderManager.mOplusLbsClass != null) {
                    try {
                        com.android.server.location.provider.LocationProviderManager.mOplusLbsClass.deliverLocation(com.android.server.location.provider.LocationProviderManager.LocationRegistration.this.getIdentity(), com.android.server.location.provider.LocationProviderManager.this.mName, "" + com.android.server.location.provider.LocationProviderManager.LocationRegistration.this.getKey().hashCode());
                        com.android.server.location.provider.LocationProviderManager.mOplusLbsClass.deliverLocationForSnapshot(com.android.server.location.provider.LocationProviderManager.LocationRegistration.this.getIdentity(), com.android.server.location.provider.LocationProviderManager.this.mName, "" + com.android.server.location.provider.LocationProviderManager.LocationRegistration.this.getKey().hashCode(), deliverLocationResult);
                    } catch (java.lang.NullPointerException e) {
                        android.util.Log.e(com.android.server.location.LocationManagerService.TAG, "deliverLocation, NullPointerException fail " + e);
                    }
                }
                com.android.server.location.eventlog.LocationEventLog.EVENT_LOG.logProviderDeliveredLocations(com.android.server.location.provider.LocationProviderManager.this.mName, this.val$locationResult.size(), com.android.server.location.provider.LocationProviderManager.LocationRegistration.this.getIdentity());
            }

            public void onPostExecute(boolean success) {
                if (!success && this.val$useWakeLock) {
                    try {
                        com.android.server.location.provider.LocationProviderManager.LocationRegistration.this.mWakeLock.release();
                    } catch (java.lang.RuntimeException e) {
                        if (e.getClass() == java.lang.RuntimeException.class) {
                            android.util.Log.e(com.android.server.location.LocationManagerService.TAG, "wakelock over-released by " + e);
                        } else {
                            com.android.server.FgThread.getExecutor().execute(new java.lang.Runnable() { // from class: com.android.server.location.provider.LocationProviderManager$LocationRegistration$2$$ExternalSyntheticLambda0
                                @Override // java.lang.Runnable
                                public final void run() {
                                    com.android.server.location.provider.LocationProviderManager.LocationRegistration.AnonymousClass2.lambda$onPostExecute$0(e);
                                }
                            });
                            throw e;
                        }
                    }
                }
                if (success) {
                    if (com.android.server.location.provider.LocationProviderManager.mOplusLbsClass != null) {
                        com.android.server.location.provider.LocationProviderManager.mOplusLbsClass.checkLocationHasChanged(com.android.server.location.provider.LocationProviderManager.this.mName, com.android.server.location.provider.LocationProviderManager.LocationRegistration.this.getIdentity().getPackageName(), 0);
                    }
                    com.android.server.location.provider.LocationProviderManager.LocationRegistration locationRegistration = com.android.server.location.provider.LocationProviderManager.LocationRegistration.this;
                    int i = locationRegistration.mNumLocationsDelivered + 1;
                    locationRegistration.mNumLocationsDelivered = i;
                    boolean remove = i >= com.android.server.location.provider.LocationProviderManager.LocationRegistration.this.getRequest().getMaxUpdates();
                    if (remove) {
                        if (com.android.server.location.LocationManagerService.D) {
                            android.util.Log.d(com.android.server.location.LocationManagerService.TAG, com.android.server.location.provider.LocationProviderManager.this.mName + " provider registration " + com.android.server.location.provider.LocationProviderManager.LocationRegistration.this.getIdentity() + " finished after " + com.android.server.location.provider.LocationProviderManager.LocationRegistration.this.mNumLocationsDelivered + " updates");
                        }
                        com.android.server.location.provider.LocationProviderManager.LocationRegistration.this.remove();
                    }
                }
            }

            static /* synthetic */ void lambda$onPostExecute$0(java.lang.RuntimeException e) {
                throw new java.lang.AssertionError(e);
            }
        }

        public void onProviderEnabledChanged(java.lang.String provider, int userId, final boolean enabled) {
            com.android.internal.util.Preconditions.checkState(com.android.server.location.provider.LocationProviderManager.this.mName.equals(provider));
            android.util.Log.d(com.android.server.location.LocationManagerService.TAG, "onProviderEnabledChanged name: " + com.android.server.location.provider.LocationProviderManager.this.mName + " enabled: " + enabled);
            if (userId != getIdentity().getUserId()) {
                return;
            }
            executeSafely(getExecutor(), new java.util.function.Supplier() { // from class: com.android.server.location.provider.LocationProviderManager$LocationRegistration$$ExternalSyntheticLambda0
                @Override // java.util.function.Supplier
                public final java.lang.Object get() {
                    return this.f$0.lambda$onProviderEnabledChanged$0();
                }
            }, new com.android.internal.listeners.ListenerExecutor.ListenerOperation() { // from class: com.android.server.location.provider.LocationProviderManager$LocationRegistration$$ExternalSyntheticLambda1
                public final void operate(java.lang.Object obj) throws java.lang.Exception {
                    this.f$0.lambda$onProviderEnabledChanged$1(enabled, (com.android.server.location.provider.LocationProviderManager.ProviderTransport) obj);
                }
            }, new com.android.internal.listeners.ListenerExecutor.FailureCallback() { // from class: com.android.server.location.provider.LocationProviderManager$LocationRegistration$$ExternalSyntheticLambda2
                public final void onFailure(com.android.internal.listeners.ListenerExecutor.ListenerOperation listenerOperation, java.lang.Exception exc) {
                    this.f$0.onProviderOperationFailure(listenerOperation, exc);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ com.android.server.location.provider.LocationProviderManager.ProviderTransport lambda$onProviderEnabledChanged$0() {
            return this.mProviderTransport;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onProviderEnabledChanged$1(boolean enabled, com.android.server.location.provider.LocationProviderManager.ProviderTransport listener) throws java.lang.Exception {
            listener.deliverOnProviderEnabledChanged(com.android.server.location.provider.LocationProviderManager.this.mName, enabled);
        }
    }

    protected final class LocationListenerRegistration extends com.android.server.location.provider.LocationProviderManager.LocationRegistration implements android.os.IBinder.DeathRecipient {
        LocationListenerRegistration(android.location.LocationRequest request, android.location.util.identity.CallerIdentity identity, com.android.server.location.provider.LocationProviderManager.LocationListenerTransport transport, int permissionLevel) {
            super(request, identity, identity.isMyProcess() ? com.android.server.FgThread.getExecutor() : com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, transport, permissionLevel);
        }

        @Override // com.android.server.location.provider.LocationProviderManager.LocationRegistration, com.android.server.location.provider.LocationProviderManager.Registration, com.android.server.location.listeners.RemovableListenerRegistration
        protected void onRegister() {
            super.onRegister();
            try {
                ((android.os.IBinder) getKey()).linkToDeath(this, 0);
            } catch (android.os.RemoteException e) {
                remove();
            }
        }

        @Override // com.android.server.location.provider.LocationProviderManager.LocationRegistration, com.android.server.location.provider.LocationProviderManager.Registration, com.android.server.location.listeners.RemovableListenerRegistration, com.android.server.location.listeners.ListenerRegistration
        protected void onUnregister() {
            try {
                ((android.os.IBinder) getKey()).unlinkToDeath(this, 0);
            } catch (java.util.NoSuchElementException e) {
                android.util.Log.w(getTag(), "failed to unregister binder death listener", e);
            }
            super.onUnregister();
        }

        @Override // com.android.server.location.provider.LocationProviderManager.LocationRegistration
        protected void onProviderOperationFailure(com.android.internal.listeners.ListenerExecutor.ListenerOperation<com.android.server.location.provider.LocationProviderManager.ProviderTransport> operation, java.lang.Exception exception) {
            onTransportFailure(exception);
        }

        @Override // com.android.server.location.listeners.ListenerRegistration
        public void onOperationFailure(com.android.internal.listeners.ListenerExecutor.ListenerOperation<com.android.server.location.provider.LocationProviderManager.LocationTransport> operation, java.lang.Exception exception) {
            onTransportFailure(exception);
        }

        private void onTransportFailure(java.lang.Exception e) {
            if (e instanceof android.os.RemoteException) {
                android.util.Log.w(com.android.server.location.LocationManagerService.TAG, com.android.server.location.provider.LocationProviderManager.this.mName + " provider registration " + getIdentity() + " removed", e);
                remove();
                return;
            }
            throw new java.lang.AssertionError(e);
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            try {
                if (com.android.server.location.LocationManagerService.D) {
                    android.util.Log.d(com.android.server.location.LocationManagerService.TAG, com.android.server.location.provider.LocationProviderManager.this.mName + " provider registration " + getIdentity() + " died");
                }
                if (com.android.server.location.provider.LocationProviderManager.mLocationFreeze != null) {
                    try {
                        com.android.server.location.provider.LocationProviderManager.mLocationFreeze.onBinderDied(getKey(), getIdentity().getUid(), getIdentity().getPackageName());
                    } catch (java.lang.NullPointerException e) {
                        android.util.Log.d(com.android.server.location.LocationManagerService.TAG, "onBinderDied getKey is null!");
                    }
                }
                remove();
            } catch (java.lang.RuntimeException e2) {
                throw new java.lang.AssertionError(e2);
            }
        }
    }

    protected final class LocationPendingIntentRegistration extends com.android.server.location.provider.LocationProviderManager.LocationRegistration implements android.app.PendingIntent.CancelListener {
        LocationPendingIntentRegistration(android.location.LocationRequest request, android.location.util.identity.CallerIdentity identity, com.android.server.location.provider.LocationProviderManager.LocationPendingIntentTransport transport, int permissionLevel) {
            super(request, identity, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, transport, permissionLevel);
        }

        @Override // com.android.server.location.provider.LocationProviderManager.LocationRegistration, com.android.server.location.provider.LocationProviderManager.Registration, com.android.server.location.listeners.RemovableListenerRegistration
        protected void onRegister() {
            super.onRegister();
            if (!((android.app.PendingIntent) getKey()).addCancelListener(com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this)) {
                remove();
            }
        }

        @Override // com.android.server.location.provider.LocationProviderManager.LocationRegistration, com.android.server.location.provider.LocationProviderManager.Registration, com.android.server.location.listeners.RemovableListenerRegistration, com.android.server.location.listeners.ListenerRegistration
        protected void onUnregister() {
            ((android.app.PendingIntent) getKey()).removeCancelListener(this);
            super.onUnregister();
        }

        @Override // com.android.server.location.provider.LocationProviderManager.LocationRegistration
        protected void onProviderOperationFailure(com.android.internal.listeners.ListenerExecutor.ListenerOperation<com.android.server.location.provider.LocationProviderManager.ProviderTransport> operation, java.lang.Exception exception) {
            onTransportFailure(exception);
        }

        @Override // com.android.server.location.listeners.ListenerRegistration
        public void onOperationFailure(com.android.internal.listeners.ListenerExecutor.ListenerOperation<com.android.server.location.provider.LocationProviderManager.LocationTransport> operation, java.lang.Exception exception) {
            onTransportFailure(exception);
        }

        private void onTransportFailure(java.lang.Exception e) {
            if (e instanceof android.app.PendingIntent.CanceledException) {
                android.util.Log.w(com.android.server.location.LocationManagerService.TAG, com.android.server.location.provider.LocationProviderManager.this.mName + " provider registration " + getIdentity() + " removed", e);
                remove();
                return;
            }
            throw new java.lang.AssertionError(e);
        }

        public void onCanceled(android.app.PendingIntent intent) {
            if (com.android.server.location.LocationManagerService.D) {
                android.util.Log.d(com.android.server.location.LocationManagerService.TAG, com.android.server.location.provider.LocationProviderManager.this.mName + " provider registration " + getIdentity() + " canceled");
            }
            if (com.android.server.location.provider.LocationProviderManager.mLocationFreeze != null && intent != null) {
                com.android.server.location.provider.LocationProviderManager.mLocationFreeze.onBinderDied(intent, getIdentity().getUid(), getIdentity().getPackageName());
            }
            remove();
        }
    }

    protected final class GetCurrentLocationListenerRegistration extends com.android.server.location.provider.LocationProviderManager.Registration implements android.os.IBinder.DeathRecipient, android.app.AlarmManager.OnAlarmListener {
        private long mExpirationRealtimeMs;

        GetCurrentLocationListenerRegistration(android.location.LocationRequest request, android.location.util.identity.CallerIdentity identity, com.android.server.location.provider.LocationProviderManager.LocationTransport transport, int permissionLevel) {
            super(request, identity, identity.isMyProcess() ? com.android.server.FgThread.getExecutor() : com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, transport, permissionLevel);
            this.mExpirationRealtimeMs = Long.MAX_VALUE;
        }

        @Override // com.android.server.location.provider.LocationProviderManager.Registration, com.android.server.location.listeners.RemovableListenerRegistration
        protected void onRegister() {
            super.onRegister();
            try {
                ((android.os.IBinder) getKey()).linkToDeath(this, 0);
            } catch (android.os.RemoteException e) {
                remove();
            }
            long registerTimeMs = android.os.SystemClock.elapsedRealtime();
            this.mExpirationRealtimeMs = getRequest().getExpirationRealtimeMs(registerTimeMs);
            if (this.mExpirationRealtimeMs <= registerTimeMs) {
                onAlarm();
            } else if (this.mExpirationRealtimeMs < Long.MAX_VALUE) {
                com.android.server.location.provider.LocationProviderManager.this.mAlarmHelper.setDelayedAlarm(this.mExpirationRealtimeMs - registerTimeMs, this, null);
            }
        }

        @Override // com.android.server.location.provider.LocationProviderManager.Registration, com.android.server.location.listeners.RemovableListenerRegistration, com.android.server.location.listeners.ListenerRegistration
        protected void onUnregister() {
            if (this.mExpirationRealtimeMs < Long.MAX_VALUE) {
                com.android.server.location.provider.LocationProviderManager.this.mAlarmHelper.cancel(this);
            }
            try {
                ((android.os.IBinder) getKey()).unlinkToDeath(this, 0);
            } catch (java.util.NoSuchElementException e) {
                android.util.Log.w(getTag(), "failed to unregister binder death listener", e);
            }
            super.onUnregister();
        }

        @Override // com.android.server.location.provider.LocationProviderManager.Registration, com.android.server.location.listeners.ListenerRegistration
        protected void onActive() {
            super.onActive();
            android.location.Location lastLocation = com.android.server.location.provider.LocationProviderManager.this.getLastLocationUnsafe(getIdentity().getUserId(), getPermissionLevel(), getRequest().isBypass(), 30000L);
            if (lastLocation != null) {
                executeOperation(acceptLocationChange(android.location.LocationResult.wrap(new android.location.Location[]{lastLocation})));
            }
        }

        @Override // com.android.server.location.provider.LocationProviderManager.Registration, com.android.server.location.listeners.ListenerRegistration
        protected void onInactive() {
            executeOperation(acceptLocationChange(null));
            super.onInactive();
        }

        void deliverNull() {
            executeOperation(acceptLocationChange(null));
        }

        @Override // android.app.AlarmManager.OnAlarmListener
        public void onAlarm() {
            if (com.android.server.location.LocationManagerService.D) {
                android.util.Log.d(com.android.server.location.LocationManagerService.TAG, com.android.server.location.provider.LocationProviderManager.this.mName + " provider registration " + getIdentity() + " expired at " + android.util.TimeUtils.formatRealtime(this.mExpirationRealtimeMs));
            }
            synchronized (com.android.server.location.provider.LocationProviderManager.this.mMultiplexerLock) {
                this.mExpirationRealtimeMs = Long.MAX_VALUE;
                executeOperation(acceptLocationChange(null));
            }
        }

        @Override // com.android.server.location.provider.LocationProviderManager.Registration
        com.android.internal.listeners.ListenerExecutor.ListenerOperation<com.android.server.location.provider.LocationProviderManager.LocationTransport> acceptLocationChange(android.location.LocationResult fineLocationResult) {
            int op;
            if (android.os.SystemClock.elapsedRealtime() >= this.mExpirationRealtimeMs) {
                if (com.android.server.location.LocationManagerService.D) {
                    android.util.Log.d(com.android.server.location.LocationManagerService.TAG, com.android.server.location.provider.LocationProviderManager.this.mName + " provider registration " + getIdentity() + " expired at " + android.util.TimeUtils.formatRealtime(this.mExpirationRealtimeMs));
                }
                fineLocationResult = null;
            }
            if (fineLocationResult != null) {
                if (android.location.flags.Flags.enableLocationBypass() && isOnlyBypassPermitted()) {
                    op = 147;
                } else {
                    op = com.android.server.location.LocationPermissions.asAppOp(getPermissionLevel());
                }
                if (!com.android.server.location.provider.LocationProviderManager.this.mAppOpsHelper.noteOpNoThrow(op, getIdentity())) {
                    if (com.android.server.location.LocationManagerService.D) {
                        android.util.Log.w(com.android.server.location.LocationManagerService.TAG, "noteOp denied for " + getIdentity());
                    }
                    fineLocationResult = null;
                }
            }
            if (fineLocationResult != null) {
                fineLocationResult = fineLocationResult.asLastLocationResult();
            }
            final android.location.LocationResult locationResult = com.android.server.location.provider.LocationProviderManager.this.getPermittedLocationResult(fineLocationResult, getPermissionLevel());
            return new com.android.internal.listeners.ListenerExecutor.ListenerOperation<com.android.server.location.provider.LocationProviderManager.LocationTransport>() { // from class: com.android.server.location.provider.LocationProviderManager.GetCurrentLocationListenerRegistration.1
                public void operate(com.android.server.location.provider.LocationProviderManager.LocationTransport listener) throws java.lang.Exception {
                    android.location.LocationResult deliverLocationResult;
                    if (com.android.server.location.provider.LocationProviderManager.GetCurrentLocationListenerRegistration.this.getIdentity().getPid() == android.os.Process.myPid() && locationResult != null) {
                        deliverLocationResult = locationResult.deepCopy();
                    } else {
                        deliverLocationResult = locationResult;
                    }
                    listener.deliverOnLocationChanged(deliverLocationResult, null);
                    com.android.server.location.eventlog.LocationEventLog.EVENT_LOG.logProviderDeliveredLocations(com.android.server.location.provider.LocationProviderManager.this.mName, locationResult != null ? locationResult.size() : 0, com.android.server.location.provider.LocationProviderManager.GetCurrentLocationListenerRegistration.this.getIdentity());
                }

                public void onPostExecute(boolean success) {
                    if (success) {
                        com.android.server.location.provider.LocationProviderManager.GetCurrentLocationListenerRegistration.this.remove();
                    }
                }
            };
        }

        @Override // com.android.server.location.listeners.ListenerRegistration
        public void onOperationFailure(com.android.internal.listeners.ListenerExecutor.ListenerOperation<com.android.server.location.provider.LocationProviderManager.LocationTransport> operation, java.lang.Exception e) {
            if (e instanceof android.os.RemoteException) {
                android.util.Log.w(com.android.server.location.LocationManagerService.TAG, com.android.server.location.provider.LocationProviderManager.this.mName + " provider registration " + getIdentity() + " removed", e);
                remove();
                return;
            }
            throw new java.lang.AssertionError(e);
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            try {
                if (com.android.server.location.LocationManagerService.D) {
                    android.util.Log.d(com.android.server.location.LocationManagerService.TAG, com.android.server.location.provider.LocationProviderManager.this.mName + " provider registration " + getIdentity() + " died");
                }
                remove();
            } catch (java.lang.RuntimeException e) {
                throw new java.lang.AssertionError(e);
            }
        }
    }

    public LocationProviderManager(android.content.Context context, com.android.server.location.injector.Injector injector, java.lang.String name, com.android.server.location.provider.PassiveLocationProviderManager passiveManager) {
        this(context, injector, name, passiveManager, java.util.Collections.emptyList());
    }

    public LocationProviderManager(android.content.Context context, com.android.server.location.injector.Injector injector, java.lang.String name, com.android.server.location.provider.PassiveLocationProviderManager passiveManager, java.util.Collection<java.lang.String> requiredPermissions) {
        this.mUserChangedListener = new com.android.server.location.injector.UserInfoHelper.UserListener() { // from class: com.android.server.location.provider.LocationProviderManager$$ExternalSyntheticLambda5
            @Override // com.android.server.location.injector.UserInfoHelper.UserListener
            public final void onUserChanged(int i, int i2) {
                this.f$0.onUserChanged(i, i2);
            }
        };
        this.mLocationUserSettingsListener = new com.android.server.location.settings.LocationSettings.LocationUserSettingsListener() { // from class: com.android.server.location.provider.LocationProviderManager$$ExternalSyntheticLambda8
            @Override // com.android.server.location.settings.LocationSettings.LocationUserSettingsListener
            public final void onLocationUserSettingsChanged(int i, com.android.server.location.settings.LocationUserSettings locationUserSettings, com.android.server.location.settings.LocationUserSettings locationUserSettings2) {
                this.f$0.onLocationUserSettingsChanged(i, locationUserSettings, locationUserSettings2);
            }
        };
        this.mLocationEnabledChangedListener = new com.android.server.location.injector.SettingsHelper.UserSettingChangedListener() { // from class: com.android.server.location.provider.LocationProviderManager$$ExternalSyntheticLambda9
            @Override // com.android.server.location.injector.SettingsHelper.UserSettingChangedListener
            public final void onSettingChanged(int i) {
                this.f$0.onLocationEnabledChanged(i);
            }
        };
        this.mBackgroundThrottlePackageWhitelistChangedListener = new com.android.server.location.injector.SettingsHelper.GlobalSettingChangedListener() { // from class: com.android.server.location.provider.LocationProviderManager$$ExternalSyntheticLambda10
            @Override // com.android.server.location.injector.SettingsHelper.GlobalSettingChangedListener
            public final void onSettingChanged() {
                this.f$0.onBackgroundThrottlePackageWhitelistChanged();
            }
        };
        this.mLocationPackageBlacklistChangedListener = new com.android.server.location.injector.SettingsHelper.UserSettingChangedListener() { // from class: com.android.server.location.provider.LocationProviderManager$$ExternalSyntheticLambda11
            @Override // com.android.server.location.injector.SettingsHelper.UserSettingChangedListener
            public final void onSettingChanged(int i) {
                this.f$0.onLocationPackageBlacklistChanged(i);
            }
        };
        this.mLocationPermissionsListener = new com.android.server.location.injector.LocationPermissionsHelper.LocationPermissionsListener() { // from class: com.android.server.location.provider.LocationProviderManager.1
            @Override // com.android.server.location.injector.LocationPermissionsHelper.LocationPermissionsListener
            public void onLocationPermissionsChanged(java.lang.String packageName) {
                com.android.server.location.provider.LocationProviderManager.this.onLocationPermissionsChanged(packageName);
            }

            @Override // com.android.server.location.injector.LocationPermissionsHelper.LocationPermissionsListener
            public void onLocationPermissionsChanged(int uid) {
                com.android.server.location.provider.LocationProviderManager.this.onLocationPermissionsChanged(uid);
            }
        };
        this.mAppForegroundChangedListener = new com.android.server.location.injector.AppForegroundHelper.AppForegroundListener() { // from class: com.android.server.location.provider.LocationProviderManager$$ExternalSyntheticLambda12
            @Override // com.android.server.location.injector.AppForegroundHelper.AppForegroundListener
            public final void onAppForegroundChanged(int i, boolean z) {
                this.f$0.onAppForegroundChanged(i, z);
            }
        };
        this.mBackgroundThrottleIntervalChangedListener = new com.android.server.location.injector.SettingsHelper.GlobalSettingChangedListener() { // from class: com.android.server.location.provider.LocationProviderManager$$ExternalSyntheticLambda13
            @Override // com.android.server.location.injector.SettingsHelper.GlobalSettingChangedListener
            public final void onSettingChanged() {
                this.f$0.onBackgroundThrottleIntervalChanged();
            }
        };
        this.mAdasPackageAllowlistChangedListener = new com.android.server.location.injector.SettingsHelper.GlobalSettingChangedListener() { // from class: com.android.server.location.provider.LocationProviderManager$$ExternalSyntheticLambda14
            @Override // com.android.server.location.injector.SettingsHelper.GlobalSettingChangedListener
            public final void onSettingChanged() {
                this.f$0.onAdasAllowlistChanged();
            }
        };
        this.mIgnoreSettingsPackageWhitelistChangedListener = new com.android.server.location.injector.SettingsHelper.GlobalSettingChangedListener() { // from class: com.android.server.location.provider.LocationProviderManager$$ExternalSyntheticLambda15
            @Override // com.android.server.location.injector.SettingsHelper.GlobalSettingChangedListener
            public final void onSettingChanged() {
                this.f$0.onIgnoreSettingsWhitelistChanged();
            }
        };
        this.mLocationPowerSaveModeChangedListener = new com.android.server.location.injector.LocationPowerSaveModeHelper.LocationPowerSaveModeChangedListener() { // from class: com.android.server.location.provider.LocationProviderManager$$ExternalSyntheticLambda16
            @Override // com.android.server.location.injector.LocationPowerSaveModeHelper.LocationPowerSaveModeChangedListener
            public final void onLocationPowerSaveModeChanged(int i) {
                this.f$0.onLocationPowerSaveModeChanged(i);
            }
        };
        this.mScreenInteractiveChangedListener = new com.android.server.location.injector.ScreenInteractiveHelper.ScreenInteractiveChangedListener() { // from class: com.android.server.location.provider.LocationProviderManager$$ExternalSyntheticLambda6
            @Override // com.android.server.location.injector.ScreenInteractiveHelper.ScreenInteractiveChangedListener
            public final void onScreenInteractiveChanged(boolean z) {
                this.f$0.onScreenInteractiveChanged(z);
            }
        };
        this.mEmergencyStateChangedListener = new com.android.server.location.injector.EmergencyHelper.EmergencyStateChangedListener() { // from class: com.android.server.location.provider.LocationProviderManager$$ExternalSyntheticLambda7
            @Override // com.android.server.location.injector.EmergencyHelper.EmergencyStateChangedListener
            public final void onStateChanged() {
                this.f$0.onEmergencyStateChanged();
            }
        };
        this.mPackageResetResponder = new com.android.server.location.injector.PackageResetHelper.Responder() { // from class: com.android.server.location.provider.LocationProviderManager.2
            @Override // com.android.server.location.injector.PackageResetHelper.Responder
            public void onPackageReset(java.lang.String packageName) {
                com.android.server.location.provider.LocationProviderManager.this.onPackageReset(packageName);
            }

            @Override // com.android.server.location.injector.PackageResetHelper.Responder
            public boolean isResetableForPackage(java.lang.String packageName) {
                return com.android.server.location.provider.LocationProviderManager.this.isResetableForPackage(packageName);
            }
        };
        this.mAltitudeConverter = new android.location.altitude.AltitudeConverter();
        this.mIsAltitudeConverterIdle = true;
        this.mLocationProviderManagerWrapper = new com.android.server.location.provider.LocationProviderManager.LocationProviderManagerWrapper();
        this.mContext = context;
        this.mName = (java.lang.String) java.util.Objects.requireNonNull(name);
        this.mPassiveManager = passiveManager;
        this.mState = 2;
        this.mEnabled = new android.util.SparseBooleanArray(2);
        this.mLastLocations = new android.util.SparseArray<>(2);
        this.mRequiredPermissions = requiredPermissions;
        this.mEnabledListeners = new java.util.ArrayList<>();
        this.mProviderRequestListeners = new java.util.concurrent.CopyOnWriteArrayList<>();
        this.mLocationManagerInternal = (android.location.LocationManagerInternal) java.util.Objects.requireNonNull((android.location.LocationManagerInternal) com.android.server.LocalServices.getService(android.location.LocationManagerInternal.class));
        this.mLocationSettings = injector.getLocationSettings();
        this.mSettingsHelper = injector.getSettingsHelper();
        this.mUserHelper = injector.getUserInfoHelper();
        this.mAlarmHelper = injector.getAlarmHelper();
        this.mAppOpsHelper = injector.getAppOpsHelper();
        this.mLocationPermissionsHelper = injector.getLocationPermissionsHelper();
        this.mAppForegroundHelper = injector.getAppForegroundHelper();
        this.mLocationPowerSaveModeHelper = injector.getLocationPowerSaveModeHelper();
        this.mScreenInteractiveHelper = injector.getScreenInteractiveHelper();
        this.mLocationUsageLogger = injector.getLocationUsageLogger();
        this.mLocationFudger = new com.android.server.location.fudger.LocationFudger(this.mSettingsHelper.getCoarseLocationAccuracyM());
        this.mEmergencyHelper = injector.getEmergencyHelper();
        this.mPackageResetHelper = injector.getPackageResetHelper();
        this.mProvider = new com.android.server.location.provider.MockableLocationProvider(this.mMultiplexerLock);
        this.mProvider.getController().setListener(this);
    }

    public void startManager(com.android.server.location.provider.LocationProviderManager.StateChangedListener listener) {
        synchronized (this.mMultiplexerLock) {
            com.android.internal.util.Preconditions.checkState(this.mState == 2);
            this.mState = 0;
            this.mStateChangedListener = listener;
            this.mUserHelper.addListener(this.mUserChangedListener);
            this.mLocationSettings.registerLocationUserSettingsListener(this.mLocationUserSettingsListener);
            this.mSettingsHelper.addOnLocationEnabledChangedListener(this.mLocationEnabledChangedListener);
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                this.mProvider.getController().start();
                onUserStarted(-1);
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }
    }

    public void stopManager() {
        synchronized (this.mMultiplexerLock) {
            com.android.internal.util.Preconditions.checkState(this.mState == 0);
            this.mState = 1;
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                onEnabledChanged(-1);
                removeRegistrationIf(new java.util.function.Predicate() { // from class: com.android.server.location.provider.LocationProviderManager$$ExternalSyntheticLambda19
                    @Override // java.util.function.Predicate
                    public final boolean test(java.lang.Object obj) {
                        return com.android.server.location.provider.LocationProviderManager.lambda$stopManager$0(obj);
                    }
                });
                this.mProvider.getController().stop();
                android.os.Binder.restoreCallingIdentity(identity);
                this.mUserHelper.removeListener(this.mUserChangedListener);
                this.mLocationSettings.unregisterLocationUserSettingsListener(this.mLocationUserSettingsListener);
                this.mSettingsHelper.removeOnLocationEnabledChangedListener(this.mLocationEnabledChangedListener);
                com.android.internal.util.Preconditions.checkState(this.mEnabledListeners.isEmpty());
                this.mProviderRequestListeners.clear();
                this.mEnabled.clear();
                this.mLastLocations.clear();
                this.mStateChangedListener = null;
                this.mState = 2;
            } catch (java.lang.Throwable th) {
                android.os.Binder.restoreCallingIdentity(identity);
                throw th;
            }
        }
    }

    static /* synthetic */ boolean lambda$stopManager$0(java.lang.Object key) {
        return true;
    }

    public java.lang.String getName() {
        return this.mName;
    }

    public com.android.server.location.provider.AbstractLocationProvider.State getState() {
        return this.mProvider.getState();
    }

    public android.location.util.identity.CallerIdentity getProviderIdentity() {
        return this.mProvider.getState().identity;
    }

    public android.location.provider.ProviderProperties getProperties() {
        return this.mProvider.getState().properties;
    }

    public boolean hasProvider() {
        return this.mProvider.getProvider() != null;
    }

    public boolean isEnabled(int userId) {
        boolean zValueAt;
        if (userId == -10000) {
            return false;
        }
        if (userId == -2) {
            return isEnabled(this.mUserHelper.getCurrentUserId());
        }
        com.android.internal.util.Preconditions.checkArgument(userId >= 0);
        synchronized (this.mMultiplexerLock) {
            int index = this.mEnabled.indexOfKey(userId);
            if (index < 0) {
                android.util.Log.w(com.android.server.location.LocationManagerService.TAG, this.mName + " provider saw user " + userId + " unexpectedly");
                onEnabledChanged(userId);
                index = this.mEnabled.indexOfKey(userId);
            }
            zValueAt = this.mEnabled.valueAt(index);
        }
        return zValueAt;
    }

    public boolean isVisibleToCaller() {
        if (android.os.Binder.getCallingUid() == 1000 || this.mProvider.isMock()) {
            return true;
        }
        for (java.lang.String permission : this.mRequiredPermissions) {
            if (this.mContext.checkCallingOrSelfPermission(permission) != 0) {
                return false;
            }
        }
        return true;
    }

    public void addEnabledListener(android.location.LocationManagerInternal.ProviderEnabledListener listener) {
        synchronized (this.mMultiplexerLock) {
            com.android.internal.util.Preconditions.checkState(this.mState != 2);
            this.mEnabledListeners.add(listener);
        }
    }

    public void removeEnabledListener(android.location.LocationManagerInternal.ProviderEnabledListener listener) {
        synchronized (this.mMultiplexerLock) {
            com.android.internal.util.Preconditions.checkState(this.mState != 2);
            this.mEnabledListeners.remove(listener);
        }
    }

    public void addProviderRequestListener(android.location.provider.IProviderRequestListener listener) {
        this.mProviderRequestListeners.add(listener);
    }

    public void removeProviderRequestListener(android.location.provider.IProviderRequestListener listener) {
        this.mProviderRequestListeners.remove(listener);
    }

    public void setRealProvider(com.android.server.location.provider.AbstractLocationProvider provider) {
        synchronized (this.mMultiplexerLock) {
            com.android.internal.util.Preconditions.checkState(this.mState != 2);
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                this.mProvider.setRealProvider(provider);
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }
    }

    public void setMockProvider(com.android.server.location.provider.MockLocationProvider provider) {
        synchronized (this.mMultiplexerLock) {
            com.android.internal.util.Preconditions.checkState(this.mState != 2);
            com.android.server.location.eventlog.LocationEventLog.EVENT_LOG.logProviderMocked(this.mName, provider != null);
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                this.mProvider.setMockProvider(provider);
                if (provider == null) {
                    int lastLocationSize = this.mLastLocations.size();
                    for (int i = 0; i < lastLocationSize; i++) {
                        this.mLastLocations.valueAt(i).clearMock();
                    }
                    this.mLocationFudger.resetOffsets();
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }
    }

    public void setMockProviderAllowed(boolean enabled) {
        synchronized (this.mMultiplexerLock) {
            if (!this.mProvider.isMock()) {
                throw new java.lang.IllegalArgumentException(this.mName + " provider is not a test provider");
            }
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                this.mProvider.setMockProviderAllowed(enabled);
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }
    }

    public void setMockProviderLocation(android.location.Location location) {
        synchronized (this.mMultiplexerLock) {
            if (!this.mProvider.isMock()) {
                throw new java.lang.IllegalArgumentException(this.mName + " provider is not a test provider");
            }
            java.lang.String locationProvider = location.getProvider();
            if (!android.text.TextUtils.isEmpty(locationProvider) && !this.mName.equals(locationProvider)) {
                android.util.EventLog.writeEvent(1397638484, "33091107", java.lang.Integer.valueOf(android.os.Binder.getCallingUid()), this.mName + "!=" + locationProvider);
            }
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                this.mProvider.setMockProviderLocation(location);
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }
    }

    public android.location.Location getLastLocation(android.location.LastLocationRequest request, android.location.util.identity.CallerIdentity identity, int permissionLevel) {
        int op;
        android.location.LastLocationRequest request2 = calculateLastLocationRequest(request, identity);
        if (!isActive(request2.isBypass(), identity)) {
            return null;
        }
        android.location.Location location = getPermittedLocation(getLastLocationUnsafe(identity.getUserId(), permissionLevel, request2.isBypass(), Long.MAX_VALUE), permissionLevel);
        if (!TEST_PROVIDER.equals(this.mName) && location == null) {
            if (android.location.flags.Flags.enableLocationBypass() && !this.mLocationPermissionsHelper.hasLocationPermissions(permissionLevel, identity) && this.mEmergencyHelper.isInEmergency(0L) && this.mContext.checkPermission("android.permission.LOCATION_BYPASS", identity.getPid(), identity.getUid()) == 0) {
                op = 147;
            } else {
                op = com.android.server.location.LocationPermissions.asAppOp(permissionLevel);
            }
            if (!this.mAppOpsHelper.noteOpNoThrow(op, identity)) {
                return null;
            }
        }
        if (location != null) {
            int op2 = (android.location.flags.Flags.enableLocationBypass() && !this.mLocationPermissionsHelper.hasLocationPermissions(permissionLevel, identity) && this.mEmergencyHelper.isInEmergency(0L) && this.mContext.checkPermission("android.permission.LOCATION_BYPASS", identity.getPid(), identity.getUid()) == 0) ? 147 : com.android.server.location.LocationPermissions.asAppOp(permissionLevel);
            if (!this.mAppOpsHelper.noteOpNoThrow(op2, identity)) {
                return null;
            }
            if (identity.getPid() == android.os.Process.myPid()) {
                return new android.location.Location(location);
            }
            return location;
        }
        return location;
    }

    private android.location.LastLocationRequest calculateLastLocationRequest(android.location.LastLocationRequest baseRequest, android.location.util.identity.CallerIdentity identity) {
        android.location.LastLocationRequest.Builder builder = new android.location.LastLocationRequest.Builder(baseRequest);
        boolean locationSettingsIgnored = baseRequest.isLocationSettingsIgnored();
        if (locationSettingsIgnored) {
            if (!this.mSettingsHelper.getIgnoreSettingsAllowlist().contains(identity.getPackageName(), identity.getAttributionTag()) && !this.mLocationManagerInternal.isProvider((java.lang.String) null, identity)) {
                locationSettingsIgnored = false;
            }
            builder.setLocationSettingsIgnored(locationSettingsIgnored);
        }
        boolean adasGnssBypass = baseRequest.isAdasGnssBypass();
        if (adasGnssBypass) {
            if (!com.android.server.am.IOplusSceneManager.APP_SCENE_GPS.equals(this.mName)) {
                android.util.Log.e(com.android.server.location.LocationManagerService.TAG, "adas gnss bypass request received in non-gps provider");
                adasGnssBypass = false;
            } else if (!this.mUserHelper.isCurrentUserId(identity.getUserId()) || !this.mLocationSettings.getUserSettings(identity.getUserId()).isAdasGnssLocationEnabled() || !this.mSettingsHelper.getAdasAllowlist().contains(identity.getPackageName(), identity.getAttributionTag())) {
                adasGnssBypass = false;
            }
            builder.setAdasGnssBypass(adasGnssBypass);
        }
        return builder.build();
    }

    public android.location.Location getLastLocationUnsafe(int userId, int permissionLevel, boolean isBypass, long maximumAgeMs) {
        android.location.Location location;
        if (userId == -1) {
            android.location.Location lastLocation = null;
            int[] runningUserIds = this.mUserHelper.getRunningUserIds();
            for (int i : runningUserIds) {
                android.location.Location next = getLastLocationUnsafe(i, permissionLevel, isBypass, maximumAgeMs);
                if (lastLocation == null || (next != null && next.getElapsedRealtimeNanos() > lastLocation.getElapsedRealtimeNanos())) {
                    lastLocation = next;
                }
            }
            return lastLocation;
        }
        if (userId == -2) {
            return getLastLocationUnsafe(this.mUserHelper.getCurrentUserId(), permissionLevel, isBypass, maximumAgeMs);
        }
        com.android.internal.util.Preconditions.checkArgument(userId >= 0);
        synchronized (this.mMultiplexerLock) {
            com.android.internal.util.Preconditions.checkState(this.mState != 2);
            com.android.server.location.provider.LocationProviderManager.LastLocation lastLocation2 = this.mLastLocations.get(userId);
            if (lastLocation2 == null) {
                location = null;
            } else {
                location = lastLocation2.get(permissionLevel, isBypass);
            }
        }
        if (location == null || location.getElapsedRealtimeAgeMillis() > maximumAgeMs) {
            return null;
        }
        return location;
    }

    public void injectLastLocation(android.location.Location location, int userId) {
        synchronized (this.mMultiplexerLock) {
            com.android.internal.util.Preconditions.checkState(this.mState != 2);
            if (getLastLocationUnsafe(userId, 2, false, Long.MAX_VALUE) == null) {
                setLastLocation(location, userId);
            }
        }
    }

    private void setLastLocation(android.location.Location location, int userId) {
        if (userId == -1) {
            int[] runningUserIds = this.mUserHelper.getRunningUserIds();
            for (int i : runningUserIds) {
                setLastLocation(location, i);
            }
            return;
        }
        if (userId == -2) {
            setLastLocation(location, this.mUserHelper.getCurrentUserId());
            return;
        }
        com.android.internal.util.Preconditions.checkArgument(userId >= 0);
        synchronized (this.mMultiplexerLock) {
            com.android.server.location.provider.LocationProviderManager.LastLocation lastLocation = this.mLastLocations.get(userId);
            if (lastLocation == null) {
                lastLocation = new com.android.server.location.provider.LocationProviderManager.LastLocation();
                this.mLastLocations.put(userId, lastLocation);
            }
            if (isEnabled(userId)) {
                lastLocation.set(location);
            }
            lastLocation.setBypass(location);
        }
    }

    public android.os.ICancellationSignal getCurrentLocation(android.location.LocationRequest request, android.location.util.identity.CallerIdentity identity, int permissionLevel, final android.location.ILocationCallback callback) {
        if (request.getDurationMillis() > 30000) {
            request = new android.location.LocationRequest.Builder(request).setDurationMillis(30000L).build();
        }
        final com.android.server.location.provider.LocationProviderManager.GetCurrentLocationListenerRegistration registration = new com.android.server.location.provider.LocationProviderManager.GetCurrentLocationListenerRegistration(request, identity, new com.android.server.location.provider.LocationProviderManager.GetCurrentLocationTransport(callback), permissionLevel);
        synchronized (this.mMultiplexerLock) {
            com.android.internal.util.Preconditions.checkState(this.mState != 2);
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                putRegistration(callback.asBinder(), registration);
                if (!registration.isActive()) {
                    registration.deliverNull();
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }
        android.os.ICancellationSignal cancelTransport = android.os.CancellationSignal.createTransport();
        android.os.CancellationSignal.fromTransport(cancelTransport).setOnCancelListener(new android.os.CancellationSignal.OnCancelListener() { // from class: com.android.server.location.provider.LocationProviderManager$$ExternalSyntheticLambda23
            @Override // android.os.CancellationSignal.OnCancelListener
            public final void onCancel() {
                this.f$0.lambda$getCurrentLocation$2(callback, registration);
            }
        });
        return cancelTransport;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getCurrentLocation$2(android.location.ILocationCallback callback, com.android.server.location.provider.LocationProviderManager.GetCurrentLocationListenerRegistration registration) {
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            try {
                removeRegistration(callback.asBinder(), registration);
            } catch (java.lang.RuntimeException e) {
                com.android.server.FgThread.getExecutor().execute(new java.lang.Runnable() { // from class: com.android.server.location.provider.LocationProviderManager$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        com.android.server.location.provider.LocationProviderManager.lambda$getCurrentLocation$1(e);
                    }
                });
                throw e;
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    static /* synthetic */ void lambda$getCurrentLocation$1(java.lang.RuntimeException e) {
        throw new java.lang.AssertionError(e);
    }

    public void sendExtraCommand(int uid, int pid, java.lang.String command, android.os.Bundle extras) {
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            this.mProvider.getController().sendExtraCommand(uid, pid, command, extras);
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    public void registerLocationRequest(android.location.LocationRequest request, android.location.util.identity.CallerIdentity identity, int permissionLevel, android.location.ILocationListener listener) {
        com.android.server.location.provider.LocationProviderManager.LocationListenerRegistration registration = new com.android.server.location.provider.LocationProviderManager.LocationListenerRegistration(request, identity, new com.android.server.location.provider.LocationProviderManager.LocationListenerTransport(listener), permissionLevel);
        synchronized (this.mMultiplexerLock) {
            com.android.internal.util.Preconditions.checkState(this.mState != 2);
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                if (mOplusLbsClass != null) {
                    mOplusLbsClass.getProviderStatus(this.mName, this.mProvider.getState().allowed, isEnabled(identity.getUserId()), true, identity.getUserId(), identity.getPackageName());
                }
                putRegistration(listener.asBinder(), registration);
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }
    }

    public void registerLocationRequest(android.location.LocationRequest request, android.location.util.identity.CallerIdentity callerIdentity, int permissionLevel, android.app.PendingIntent pendingIntent) {
        com.android.server.location.provider.LocationProviderManager.LocationPendingIntentRegistration registration = new com.android.server.location.provider.LocationProviderManager.LocationPendingIntentRegistration(request, callerIdentity, new com.android.server.location.provider.LocationProviderManager.LocationPendingIntentTransport(this.mContext, pendingIntent), permissionLevel);
        synchronized (this.mMultiplexerLock) {
            com.android.internal.util.Preconditions.checkState(this.mState != 2);
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                putRegistration(pendingIntent, registration);
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }
    }

    public void flush(android.location.ILocationListener listener, final int requestCode) {
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            boolean flushed = updateRegistration(listener.asBinder(), new java.util.function.Predicate() { // from class: com.android.server.location.provider.LocationProviderManager$$ExternalSyntheticLambda33
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.server.location.provider.LocationProviderManager.lambda$flush$3(requestCode, (com.android.server.location.provider.LocationProviderManager.Registration) obj);
                }
            });
            if (!flushed) {
                throw new java.lang.IllegalArgumentException("unregistered listener cannot be flushed");
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    static /* synthetic */ boolean lambda$flush$3(int requestCode, com.android.server.location.provider.LocationProviderManager.Registration registration) {
        registration.flush(requestCode);
        return false;
    }

    public void flush(android.app.PendingIntent pendingIntent, final int requestCode) {
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            boolean flushed = updateRegistration(pendingIntent, new java.util.function.Predicate() { // from class: com.android.server.location.provider.LocationProviderManager$$ExternalSyntheticLambda32
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.server.location.provider.LocationProviderManager.lambda$flush$4(requestCode, (com.android.server.location.provider.LocationProviderManager.Registration) obj);
                }
            });
            if (!flushed) {
                throw new java.lang.IllegalArgumentException("unregistered pending intent cannot be flushed");
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    static /* synthetic */ boolean lambda$flush$4(int requestCode, com.android.server.location.provider.LocationProviderManager.Registration registration) {
        registration.flush(requestCode);
        return false;
    }

    public void unregisterLocationRequest(android.location.ILocationListener listener) {
        synchronized (this.mMultiplexerLock) {
            com.android.internal.util.Preconditions.checkState(this.mState != 2);
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                removeRegistration(listener.asBinder());
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }
    }

    public void unregisterLocationRequest(android.app.PendingIntent pendingIntent) {
        synchronized (this.mMultiplexerLock) {
            com.android.internal.util.Preconditions.checkState(this.mState != 2);
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                removeRegistration(pendingIntent);
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }
    }

    @Override // com.android.server.location.listeners.ListenerMultiplexer
    protected void onRegister() {
        this.mSettingsHelper.addOnBackgroundThrottleIntervalChangedListener(this.mBackgroundThrottleIntervalChangedListener);
        this.mSettingsHelper.addOnBackgroundThrottlePackageWhitelistChangedListener(this.mBackgroundThrottlePackageWhitelistChangedListener);
        this.mSettingsHelper.addOnLocationPackageBlacklistChangedListener(this.mLocationPackageBlacklistChangedListener);
        this.mSettingsHelper.addAdasAllowlistChangedListener(this.mAdasPackageAllowlistChangedListener);
        this.mSettingsHelper.addIgnoreSettingsAllowlistChangedListener(this.mIgnoreSettingsPackageWhitelistChangedListener);
        this.mLocationPermissionsHelper.addListener(this.mLocationPermissionsListener);
        this.mAppForegroundHelper.addListener(this.mAppForegroundChangedListener);
        this.mLocationPowerSaveModeHelper.addListener(this.mLocationPowerSaveModeChangedListener);
        this.mScreenInteractiveHelper.addListener(this.mScreenInteractiveChangedListener);
        if (android.location.flags.Flags.enableLocationBypass()) {
            this.mEmergencyHelper.addOnEmergencyStateChangedListener(this.mEmergencyStateChangedListener);
        }
        this.mPackageResetHelper.register(this.mPackageResetResponder);
    }

    @Override // com.android.server.location.listeners.ListenerMultiplexer
    protected void onUnregister() {
        this.mSettingsHelper.removeOnBackgroundThrottleIntervalChangedListener(this.mBackgroundThrottleIntervalChangedListener);
        this.mSettingsHelper.removeOnBackgroundThrottlePackageWhitelistChangedListener(this.mBackgroundThrottlePackageWhitelistChangedListener);
        this.mSettingsHelper.removeOnLocationPackageBlacklistChangedListener(this.mLocationPackageBlacklistChangedListener);
        this.mSettingsHelper.removeAdasAllowlistChangedListener(this.mAdasPackageAllowlistChangedListener);
        this.mSettingsHelper.removeIgnoreSettingsAllowlistChangedListener(this.mIgnoreSettingsPackageWhitelistChangedListener);
        this.mLocationPermissionsHelper.removeListener(this.mLocationPermissionsListener);
        this.mAppForegroundHelper.removeListener(this.mAppForegroundChangedListener);
        this.mLocationPowerSaveModeHelper.removeListener(this.mLocationPowerSaveModeChangedListener);
        this.mScreenInteractiveHelper.removeListener(this.mScreenInteractiveChangedListener);
        if (android.location.flags.Flags.enableLocationBypass()) {
            this.mEmergencyHelper.removeOnEmergencyStateChangedListener(this.mEmergencyStateChangedListener);
        }
        this.mPackageResetHelper.unregister(this.mPackageResetResponder);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.location.listeners.ListenerMultiplexer
    public void onRegistrationAdded(java.lang.Object key, com.android.server.location.provider.LocationProviderManager.Registration registration) {
        this.mLocationUsageLogger.logLocationApiUsage(0, 1, registration.getIdentity().getPackageName(), registration.getIdentity().getAttributionTag(), this.mName, registration.getRequest(), key instanceof android.app.PendingIntent, key instanceof android.os.IBinder, null, registration.isForeground());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.location.listeners.ListenerMultiplexer
    public void onRegistrationReplaced(java.lang.Object oldKey, com.android.server.location.provider.LocationProviderManager.Registration oldRegistration, java.lang.Object newKey, com.android.server.location.provider.LocationProviderManager.Registration newRegistration) {
        newRegistration.setLastDeliveredLocation(oldRegistration.getLastDeliveredLocation());
        super.onRegistrationReplaced(oldKey, oldRegistration, newKey, newRegistration);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.location.listeners.ListenerMultiplexer
    public void onRegistrationRemoved(java.lang.Object key, com.android.server.location.provider.LocationProviderManager.Registration registration) {
        this.mLocationUsageLogger.logLocationApiUsage(1, 1, registration.getIdentity().getPackageName(), registration.getIdentity().getAttributionTag(), this.mName, registration.getRequest(), key instanceof android.app.PendingIntent, key instanceof android.os.IBinder, null, registration.isForeground());
    }

    /* JADX INFO: renamed from: registerWithService, reason: avoid collision after fix types in other method */
    protected boolean registerWithService2(android.location.provider.ProviderRequest request, java.util.Collection<com.android.server.location.provider.LocationProviderManager.Registration> registrations) {
        if (!request.isActive()) {
            return true;
        }
        return reregisterWithService2(android.location.provider.ProviderRequest.EMPTY_REQUEST, request, registrations);
    }

    /* JADX INFO: renamed from: reregisterWithService, reason: avoid collision after fix types in other method */
    protected boolean reregisterWithService2(android.location.provider.ProviderRequest oldRequest, final android.location.provider.ProviderRequest newRequest, java.util.Collection<com.android.server.location.provider.LocationProviderManager.Registration> registrations) {
        long delayMs;
        if (!oldRequest.isBypass() && newRequest.isBypass()) {
            delayMs = 0;
        } else {
            long delayMs2 = newRequest.getIntervalMillis();
            if (delayMs2 > oldRequest.getIntervalMillis()) {
                delayMs = 0;
            } else {
                long delayMs3 = newRequest.getIntervalMillis();
                delayMs = calculateRequestDelayMillis(delayMs3, registrations);
            }
        }
        com.android.internal.util.Preconditions.checkState(delayMs >= 0 && delayMs <= newRequest.getIntervalMillis());
        if (delayMs < 30000) {
            setProviderRequest(newRequest);
        } else {
            if (com.android.server.location.LocationManagerService.D) {
                android.util.Log.d(com.android.server.location.LocationManagerService.TAG, this.mName + " provider delaying request update " + newRequest + " by " + android.util.TimeUtils.formatDuration(delayMs));
            }
            if (this.mDelayedRegister != null) {
                this.mAlarmHelper.cancel(this.mDelayedRegister);
                this.mDelayedRegister = null;
            }
            this.mDelayedRegister = new android.app.AlarmManager.OnAlarmListener() { // from class: com.android.server.location.provider.LocationProviderManager.3
                @Override // android.app.AlarmManager.OnAlarmListener
                public void onAlarm() {
                    synchronized (com.android.server.location.provider.LocationProviderManager.this.mMultiplexerLock) {
                        if (com.android.server.location.provider.LocationProviderManager.this.mDelayedRegister == this) {
                            com.android.server.location.provider.LocationProviderManager.this.mDelayedRegister = null;
                            com.android.server.location.provider.LocationProviderManager.this.setProviderRequest(newRequest);
                        }
                    }
                }
            };
            this.mAlarmHelper.setDelayedAlarm(delayMs, this.mDelayedRegister, null);
        }
        return true;
    }

    @Override // com.android.server.location.listeners.ListenerMultiplexer
    protected void unregisterWithService() {
        setProviderRequest(android.location.provider.ProviderRequest.EMPTY_REQUEST);
    }

    void setProviderRequest(final android.location.provider.ProviderRequest request) {
        if (this.mDelayedRegister != null) {
            this.mAlarmHelper.cancel(this.mDelayedRegister);
            this.mDelayedRegister = null;
        }
        com.android.server.location.eventlog.LocationEventLog.EVENT_LOG.logProviderUpdateRequest(this.mName, request);
        if (com.android.server.location.LocationManagerService.D) {
            android.util.Log.d(com.android.server.location.LocationManagerService.TAG, this.mName + " provider request changed to " + request);
        }
        this.mProvider.getController().setRequest(request);
        com.android.server.FgThread.getHandler().post(new java.lang.Runnable() { // from class: com.android.server.location.provider.LocationProviderManager$$ExternalSyntheticLambda22
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$setProviderRequest$5(request);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setProviderRequest$5(android.location.provider.ProviderRequest request) {
        for (android.location.provider.IProviderRequestListener listener : this.mProviderRequestListeners) {
            try {
                listener.onProviderRequestChanged(this.mName, request);
            } catch (android.os.RemoteException e) {
                this.mProviderRequestListeners.remove(listener);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.location.listeners.ListenerMultiplexer
    public boolean isActive(com.android.server.location.provider.LocationProviderManager.Registration registration) {
        if (!registration.isPermitted()) {
            return false;
        }
        boolean isBypass = registration.getRequest().isBypass();
        if (!isActive(isBypass, registration.getIdentity())) {
            return false;
        }
        if (!isBypass) {
            switch (this.mLocationPowerSaveModeHelper.getLocationPowerSaveMode()) {
                case 1:
                    if (!com.android.server.am.IOplusSceneManager.APP_SCENE_GPS.equals(this.mName)) {
                        return true;
                    }
                    break;
                case 2:
                case 4:
                    break;
                case 3:
                    return registration.isForeground();
                default:
                    return true;
            }
            return this.mScreenInteractiveHelper.isInteractive();
        }
        return true;
    }

    private boolean isActive(boolean isBypass, android.location.util.identity.CallerIdentity identity) {
        return identity.isSystemServer() ? isBypass || isEnabled(this.mUserHelper.getCurrentUserId()) : (isBypass || (isEnabled(identity.getUserId()) && this.mUserHelper.isVisibleUserId(identity.getUserId()))) && !this.mSettingsHelper.isLocationPackageBlacklisted(identity.getUserId(), identity.getPackageName());
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.android.server.location.listeners.ListenerMultiplexer
    protected android.location.provider.ProviderRequest mergeRegistrations(java.util.Collection<com.android.server.location.provider.LocationProviderManager.Registration> registrations) {
        long thresholdIntervalMs;
        java.util.Iterator<com.android.server.location.provider.LocationProviderManager.Registration> it = registrations.iterator();
        boolean lowPower = true;
        boolean lowPower2 = false;
        boolean locationSettingsIgnored = false;
        long maxUpdateDelayMs = Long.MAX_VALUE;
        int quality = 104;
        long intervalMs = Long.MAX_VALUE;
        while (it.hasNext()) {
            android.location.LocationRequest request = it.next().getRequest();
            if (request.getIntervalMillis() != Long.MAX_VALUE) {
                intervalMs = java.lang.Math.min(request.getIntervalMillis(), intervalMs);
                quality = java.lang.Math.min(request.getQuality(), quality);
                maxUpdateDelayMs = java.lang.Math.min(request.getMaxUpdateDelayMillis(), maxUpdateDelayMs);
                locationSettingsIgnored |= request.isAdasGnssBypass();
                lowPower2 |= request.isLocationSettingsIgnored();
                lowPower &= request.isLowPower();
            }
        }
        if (intervalMs == Long.MAX_VALUE) {
            return android.location.provider.ProviderRequest.EMPTY_REQUEST;
        }
        if (maxUpdateDelayMs / 2 < intervalMs) {
            maxUpdateDelayMs = 0;
        }
        try {
            thresholdIntervalMs = java.lang.Math.multiplyExact(java.lang.Math.addExact(intervalMs, 1000L) / 2, 3);
        } catch (java.lang.ArithmeticException e) {
            thresholdIntervalMs = 9223372036854775806L;
        }
        android.os.WorkSource workSource = new android.os.WorkSource();
        for (com.android.server.location.provider.LocationProviderManager.Registration registration : registrations) {
            if (registration.getRequest().getIntervalMillis() <= thresholdIntervalMs) {
                workSource.add(registration.getRequest().getWorkSource());
            }
        }
        return new android.location.provider.ProviderRequest.Builder().setIntervalMillis(intervalMs).setQuality(quality).setMaxUpdateDelayMillis(maxUpdateDelayMs).setAdasGnssBypass(locationSettingsIgnored).setLocationSettingsIgnored(lowPower2).setLowPower(lowPower).setWorkSource(workSource).build();
    }

    protected long calculateRequestDelayMillis(long newIntervalMs, java.util.Collection<com.android.server.location.provider.LocationProviderManager.Registration> registrations) {
        long registrationDelayMs;
        long delayMs = newIntervalMs;
        for (com.android.server.location.provider.LocationProviderManager.Registration registration : registrations) {
            if (delayMs == 0) {
                break;
            }
            android.location.LocationRequest locationRequest = registration.getRequest();
            android.location.Location last = registration.getLastDeliveredLocation();
            if (last == null && !locationRequest.isLocationSettingsIgnored()) {
                last = getLastLocationUnsafe(registration.getIdentity().getUserId(), registration.getPermissionLevel(), false, locationRequest.getIntervalMillis());
            }
            if (last == null) {
                registrationDelayMs = 0;
            } else {
                registrationDelayMs = java.lang.Math.max(0L, locationRequest.getIntervalMillis() - last.getElapsedRealtimeAgeMillis());
            }
            delayMs = java.lang.Math.min(delayMs, registrationDelayMs);
        }
        return delayMs;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onUserChanged(final int userId, int change) {
        synchronized (this.mMultiplexerLock) {
            if (this.mState == 2) {
                return;
            }
            switch (change) {
                case 1:
                case 4:
                    updateRegistrations(new java.util.function.Predicate() { // from class: com.android.server.location.provider.LocationProviderManager$$ExternalSyntheticLambda17
                        @Override // java.util.function.Predicate
                        public final boolean test(java.lang.Object obj) {
                            return com.android.server.location.provider.LocationProviderManager.lambda$onUserChanged$6(userId, (com.android.server.location.provider.LocationProviderManager.Registration) obj);
                        }
                    });
                    break;
                case 2:
                    onUserStarted(userId);
                    break;
                case 3:
                    onUserStopped(userId);
                    break;
            }
        }
    }

    static /* synthetic */ boolean lambda$onUserChanged$6(int userId, com.android.server.location.provider.LocationProviderManager.Registration registration) {
        return registration.getIdentity().getUserId() == userId;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onLocationUserSettingsChanged(final int userId, com.android.server.location.settings.LocationUserSettings oldSettings, com.android.server.location.settings.LocationUserSettings newSettings) {
        if (oldSettings.isAdasGnssLocationEnabled() != newSettings.isAdasGnssLocationEnabled()) {
            updateRegistrations(new java.util.function.Predicate() { // from class: com.android.server.location.provider.LocationProviderManager$$ExternalSyntheticLambda30
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return ((com.android.server.location.provider.LocationProviderManager.Registration) obj).onAdasGnssLocationEnabledChanged(userId);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onLocationEnabledChanged(int userId) {
        synchronized (this.mMultiplexerLock) {
            if (this.mState == 2) {
                return;
            }
            onEnabledChanged(userId);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onScreenInteractiveChanged(boolean screenInteractive) {
        switch (this.mLocationPowerSaveModeHelper.getLocationPowerSaveMode()) {
            case 1:
                if (!com.android.server.am.IOplusSceneManager.APP_SCENE_GPS.equals(this.mName)) {
                    return;
                }
                break;
            case 2:
            case 4:
                break;
            case 3:
            default:
                return;
        }
        updateRegistrations(new java.util.function.Predicate() { // from class: com.android.server.location.provider.LocationProviderManager$$ExternalSyntheticLambda34
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.location.provider.LocationProviderManager.lambda$onScreenInteractiveChanged$8((com.android.server.location.provider.LocationProviderManager.Registration) obj);
            }
        });
    }

    static /* synthetic */ boolean lambda$onScreenInteractiveChanged$8(com.android.server.location.provider.LocationProviderManager.Registration registration) {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onEmergencyStateChanged() {
        final boolean inEmergency = this.mEmergencyHelper.isInEmergency(0L);
        updateRegistrations(new java.util.function.Predicate() { // from class: com.android.server.location.provider.LocationProviderManager$$ExternalSyntheticLambda36
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.location.provider.LocationProviderManager.Registration) obj).onBypassLocationPermissionsChanged(inEmergency);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onBackgroundThrottlePackageWhitelistChanged() {
        updateRegistrations(new com.android.server.location.provider.LocationProviderManager$$ExternalSyntheticLambda24());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onBackgroundThrottleIntervalChanged() {
        updateRegistrations(new com.android.server.location.provider.LocationProviderManager$$ExternalSyntheticLambda24());
    }

    static /* synthetic */ boolean lambda$onLocationPowerSaveModeChanged$10(com.android.server.location.provider.LocationProviderManager.Registration registration) {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onLocationPowerSaveModeChanged(int locationPowerSaveMode) {
        updateRegistrations(new java.util.function.Predicate() { // from class: com.android.server.location.provider.LocationProviderManager$$ExternalSyntheticLambda20
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.location.provider.LocationProviderManager.lambda$onLocationPowerSaveModeChanged$10((com.android.server.location.provider.LocationProviderManager.Registration) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onAppForegroundChanged(final int uid, final boolean foreground) {
        updateRegistrations(new java.util.function.Predicate() { // from class: com.android.server.location.provider.LocationProviderManager$$ExternalSyntheticLambda26
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.location.provider.LocationProviderManager.Registration) obj).onForegroundChanged(uid, foreground);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onAdasAllowlistChanged() {
        updateRegistrations(new com.android.server.location.provider.LocationProviderManager$$ExternalSyntheticLambda24());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onIgnoreSettingsWhitelistChanged() {
        updateRegistrations(new com.android.server.location.provider.LocationProviderManager$$ExternalSyntheticLambda24());
    }

    static /* synthetic */ boolean lambda$onLocationPackageBlacklistChanged$12(int userId, com.android.server.location.provider.LocationProviderManager.Registration registration) {
        return registration.getIdentity().getUserId() == userId;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onLocationPackageBlacklistChanged(final int userId) {
        updateRegistrations(new java.util.function.Predicate() { // from class: com.android.server.location.provider.LocationProviderManager$$ExternalSyntheticLambda35
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.location.provider.LocationProviderManager.lambda$onLocationPackageBlacklistChanged$12(userId, (com.android.server.location.provider.LocationProviderManager.Registration) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onLocationPermissionsChanged(final java.lang.String packageName) {
        updateRegistrations(new java.util.function.Predicate() { // from class: com.android.server.location.provider.LocationProviderManager$$ExternalSyntheticLambda25
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.location.provider.LocationProviderManager.Registration) obj).onLocationPermissionsChanged(packageName);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onLocationPermissionsChanged(final int uid) {
        updateRegistrations(new java.util.function.Predicate() { // from class: com.android.server.location.provider.LocationProviderManager$$ExternalSyntheticLambda18
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.location.provider.LocationProviderManager.Registration) obj).onLocationPermissionsChanged(uid);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onPackageReset(final java.lang.String packageName) {
        updateRegistrations(new java.util.function.Predicate() { // from class: com.android.server.location.provider.LocationProviderManager$$ExternalSyntheticLambda31
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.location.provider.LocationProviderManager.lambda$onPackageReset$15(packageName, (com.android.server.location.provider.LocationProviderManager.Registration) obj);
            }
        });
    }

    static /* synthetic */ boolean lambda$onPackageReset$15(java.lang.String packageName, com.android.server.location.provider.LocationProviderManager.Registration registration) {
        if (registration.getIdentity().getPackageName().equals(packageName)) {
            registration.remove();
            return false;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isResetableForPackage(final java.lang.String packageName) {
        return findRegistration(new java.util.function.Predicate() { // from class: com.android.server.location.provider.LocationProviderManager$$ExternalSyntheticLambda1
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.location.provider.LocationProviderManager.Registration) obj).getIdentity().getPackageName().equals(packageName);
            }
        });
    }

    @Override // com.android.server.location.provider.AbstractLocationProvider.Listener
    public void onStateChanged(final com.android.server.location.provider.AbstractLocationProvider.State oldState, final com.android.server.location.provider.AbstractLocationProvider.State newState) {
        if (oldState.allowed != newState.allowed) {
            onEnabledChanged(-1);
        }
        if (!java.util.Objects.equals(oldState.properties, newState.properties)) {
            updateRegistrations(new java.util.function.Predicate() { // from class: com.android.server.location.provider.LocationProviderManager$$ExternalSyntheticLambda3
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return ((com.android.server.location.provider.LocationProviderManager.Registration) obj).onProviderPropertiesChanged();
                }
            });
        }
        if (this.mStateChangedListener != null) {
            final com.android.server.location.provider.LocationProviderManager.StateChangedListener listener = this.mStateChangedListener;
            com.android.server.FgThread.getExecutor().execute(new java.lang.Runnable() { // from class: com.android.server.location.provider.LocationProviderManager$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onStateChanged$17(listener, oldState, newState);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onStateChanged$17(com.android.server.location.provider.LocationProviderManager.StateChangedListener listener, com.android.server.location.provider.AbstractLocationProvider.State oldState, com.android.server.location.provider.AbstractLocationProvider.State newState) {
        listener.onStateChanged(this.mName, oldState, newState);
    }

    @Override // com.android.server.location.provider.AbstractLocationProvider.Listener
    public void onReportLocation(android.location.LocationResult locationResult) {
        final android.location.LocationResult processed;
        android.location.Location last;
        if (this.mPassiveManager != null) {
            processed = processReportedLocation(locationResult);
            if (processed == null) {
                return;
            } else {
                com.android.server.location.eventlog.LocationEventLog.EVENT_LOG.logProviderReceivedLocations(this.mName, processed.size());
            }
        } else {
            processed = locationResult;
        }
        if (this.mPassiveManager != null && (last = getLastLocationUnsafe(-2, 2, true, Long.MAX_VALUE)) != null && locationResult.get(0).getElapsedRealtimeNanos() < last.getElapsedRealtimeNanos()) {
            android.util.Log.e(com.android.server.location.LocationManagerService.TAG, "non-monotonic location received from " + this.mName + " provider");
        }
        setLastLocation(processed.getLastLocation(), -1);
        if (mOplusLbsClass != null) {
            mOplusLbsClass.handleLocationChanged(processed, com.android.server.location.LocationManagerService.D);
        }
        deliverToListeners(new java.util.function.Function() { // from class: com.android.server.location.provider.LocationProviderManager$$ExternalSyntheticLambda21
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return ((com.android.server.location.provider.LocationProviderManager.Registration) obj).acceptLocationChange(processed);
            }
        });
        if (this.mPassiveManager != null) {
            this.mPassiveManager.updateLocation(processed);
        }
    }

    private android.location.LocationResult processReportedLocation(android.location.LocationResult locationResult) {
        try {
            locationResult.validate();
            if (android.provider.DeviceConfig.getBoolean("location", "enable_location_provider_manager_msl", true)) {
                return locationResult.map(new java.util.function.Function() { // from class: com.android.server.location.provider.LocationProviderManager$$ExternalSyntheticLambda29
                    @Override // java.util.function.Function
                    public final java.lang.Object apply(java.lang.Object obj) {
                        return this.f$0.lambda$processReportedLocation$20((android.location.Location) obj);
                    }
                });
            }
            return locationResult;
        } catch (android.location.LocationResult.BadLocationException e) {
            android.util.Log.e(com.android.server.location.LocationManagerService.TAG, "Dropping invalid locations: " + e);
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ android.location.Location lambda$processReportedLocation$20(android.location.Location location) {
        if (!location.hasMslAltitude() && location.hasAltitude()) {
            try {
                final android.location.Location locationCopy = new android.location.Location(location);
                if (this.mAltitudeConverter.tryAddMslAltitudeToLocation(locationCopy)) {
                    return locationCopy;
                }
                if (this.mIsAltitudeConverterIdle) {
                    this.mIsAltitudeConverterIdle = false;
                    com.android.server.IoThread.getExecutor().execute(new java.lang.Runnable() { // from class: com.android.server.location.provider.LocationProviderManager$$ExternalSyntheticLambda2
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$processReportedLocation$19(locationCopy);
                        }
                    });
                }
            } catch (java.lang.IllegalArgumentException e) {
                android.util.Log.e(com.android.server.location.LocationManagerService.TAG, "not adding MSL altitude to location: " + e);
            }
        }
        return location;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processReportedLocation$19(android.location.Location locationCopy) {
        try {
            this.mAltitudeConverter.addMslAltitudeToLocation(this.mContext, locationCopy);
        } catch (java.io.IOException e) {
            android.util.Log.e(com.android.server.location.LocationManagerService.TAG, "not loading MSL altitude assets: " + e);
        }
        this.mIsAltitudeConverterIdle = true;
    }

    private void onUserStarted(int userId) {
        if (userId == -10000) {
            return;
        }
        if (userId == -1) {
            this.mEnabled.clear();
            onEnabledChanged(-1);
        } else {
            com.android.internal.util.Preconditions.checkArgument(userId >= 0);
            this.mEnabled.delete(userId);
            onEnabledChanged(userId);
        }
    }

    private void onUserStopped(int userId) {
        if (userId == -10000) {
            return;
        }
        if (userId == -1) {
            this.mEnabled.clear();
            this.mLastLocations.clear();
        } else {
            com.android.internal.util.Preconditions.checkArgument(userId >= 0);
            this.mEnabled.delete(userId);
            this.mLastLocations.remove(userId);
        }
    }

    private void onEnabledChanged(final int userId) {
        com.android.server.location.provider.LocationProviderManager.LastLocation lastLocation;
        if (userId == -10000) {
            return;
        }
        if (userId == -1) {
            int[] runningUserIds = this.mUserHelper.getRunningUserIds();
            for (int i : runningUserIds) {
                onEnabledChanged(i);
            }
            return;
        }
        com.android.internal.util.Preconditions.checkArgument(userId >= 0);
        final boolean enabled = this.mState == 0 && this.mProvider.getState().allowed && this.mSettingsHelper.isLocationEnabled(userId);
        int index = this.mEnabled.indexOfKey(userId);
        java.lang.Boolean wasEnabled = index < 0 ? null : java.lang.Boolean.valueOf(this.mEnabled.valueAt(index));
        if (wasEnabled != null && wasEnabled.booleanValue() == enabled) {
            return;
        }
        this.mEnabled.put(userId, enabled);
        if (wasEnabled != null || enabled) {
            if (com.android.server.location.LocationManagerService.D) {
                android.util.Log.d(com.android.server.location.LocationManagerService.TAG, "[u" + userId + "] " + this.mName + " provider enabled = " + enabled);
            }
            com.android.server.location.eventlog.LocationEventLog.EVENT_LOG.logProviderEnabled(this.mName, userId, enabled);
        }
        if (!enabled && (lastLocation = this.mLastLocations.get(userId)) != null) {
            lastLocation.clearLocations();
        }
        if (mOplusLbsClass != null) {
            mOplusLbsClass.updateSettings(this.mName, userId);
        }
        if (wasEnabled != null) {
            if (!"passive".equals(this.mName)) {
                android.content.Intent intent = new android.content.Intent("android.location.PROVIDERS_CHANGED").putExtra("android.location.extra.PROVIDER_NAME", this.mName).putExtra("android.location.extra.PROVIDER_ENABLED", enabled).addFlags(1073741824).addFlags(268435456);
                this.mContext.sendBroadcastAsUser(intent, android.os.UserHandle.of(userId));
            }
            if (!this.mEnabledListeners.isEmpty()) {
                final android.location.LocationManagerInternal.ProviderEnabledListener[] listeners = (android.location.LocationManagerInternal.ProviderEnabledListener[]) this.mEnabledListeners.toArray(new android.location.LocationManagerInternal.ProviderEnabledListener[0]);
                com.android.server.FgThread.getHandler().post(new java.lang.Runnable() { // from class: com.android.server.location.provider.LocationProviderManager$$ExternalSyntheticLambda27
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onEnabledChanged$21(listeners, userId, enabled);
                    }
                });
            }
        }
        updateRegistrations(new java.util.function.Predicate() { // from class: com.android.server.location.provider.LocationProviderManager$$ExternalSyntheticLambda28
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.location.provider.LocationProviderManager.lambda$onEnabledChanged$22(userId, (com.android.server.location.provider.LocationProviderManager.Registration) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onEnabledChanged$21(android.location.LocationManagerInternal.ProviderEnabledListener[] listeners, int userId, boolean enabled) {
        for (android.location.LocationManagerInternal.ProviderEnabledListener providerEnabledListener : listeners) {
            providerEnabledListener.onProviderEnabledChanged(this.mName, userId, enabled);
        }
    }

    static /* synthetic */ boolean lambda$onEnabledChanged$22(int userId, com.android.server.location.provider.LocationProviderManager.Registration registration) {
        return registration.getIdentity().getUserId() == userId;
    }

    android.location.Location getPermittedLocation(android.location.Location fineLocation, int permissionLevel) {
        switch (permissionLevel) {
            case 1:
                if (mOplusLbsClass == null) {
                    if (fineLocation != null) {
                        return this.mLocationFudger.createCoarse(fineLocation);
                    }
                    return null;
                }
                if (fineLocation != null) {
                    return mOplusLbsClass.addCoarseLocationExtra(this.mLocationFudger.createCoarse(fineLocation));
                }
                return null;
            case 2:
                return fineLocation;
            default:
                throw new java.lang.AssertionError();
        }
    }

    android.location.LocationResult getPermittedLocationResult(android.location.LocationResult fineLocationResult, int permissionLevel) {
        switch (permissionLevel) {
            case 1:
                if (mOplusLbsClass == null) {
                    if (fineLocationResult != null) {
                        return this.mLocationFudger.createCoarse(fineLocationResult);
                    }
                    return null;
                }
                if (fineLocationResult != null) {
                    return mOplusLbsClass.addCoarseLocationExtra(this.mLocationFudger.createCoarse(fineLocationResult));
                }
                return null;
            case 2:
                return fineLocationResult;
            default:
                throw new java.lang.AssertionError();
        }
    }

    public void dump(java.io.FileDescriptor fd, android.util.IndentingPrintWriter ipw, java.lang.String[] args) {
        synchronized (this.mMultiplexerLock) {
            ipw.print(this.mName);
            ipw.print(" provider");
            if (this.mProvider.isMock()) {
                ipw.print(" [mock]");
            }
            ipw.println(":");
            ipw.increaseIndent();
            super.dump(fd, (java.io.PrintWriter) ipw, args);
            int[] userIds = this.mUserHelper.getRunningUserIds();
            for (int userId : userIds) {
                if (userIds.length != 1) {
                    ipw.print("user ");
                    ipw.print(userId);
                    ipw.println(":");
                    ipw.increaseIndent();
                }
                ipw.print("last location=");
                ipw.println(getLastLocationUnsafe(userId, 2, false, Long.MAX_VALUE));
                ipw.print("enabled=");
                ipw.println(isEnabled(userId));
                if (userIds.length != 1) {
                    ipw.decreaseIndent();
                }
            }
        }
        this.mProvider.dump(fd, ipw, args);
        ipw.decreaseIndent();
    }

    @Override // com.android.server.location.listeners.ListenerMultiplexer
    protected java.lang.String getServiceState() {
        return this.mProvider.getCurrentRequest().toString();
    }

    private static class LastLocation {
        private android.location.Location mCoarseBypassLocation;
        private android.location.Location mCoarseLocation;
        private android.location.Location mFineBypassLocation;
        private android.location.Location mFineLocation;

        LastLocation() {
        }

        public void clearMock() {
            if (this.mFineLocation != null && this.mFineLocation.isMock()) {
                this.mFineLocation = null;
            }
            if (this.mCoarseLocation != null && this.mCoarseLocation.isMock()) {
                this.mCoarseLocation = null;
            }
            if (this.mFineBypassLocation != null && this.mFineBypassLocation.isMock()) {
                this.mFineBypassLocation = null;
            }
            if (this.mCoarseBypassLocation != null && this.mCoarseBypassLocation.isMock()) {
                this.mCoarseBypassLocation = null;
            }
        }

        public void clearLocations() {
            this.mFineLocation = null;
            this.mCoarseLocation = null;
        }

        public android.location.Location get(int permissionLevel, boolean isBypass) {
            switch (permissionLevel) {
                case 1:
                    if (isBypass) {
                        return this.mCoarseBypassLocation;
                    }
                    return this.mCoarseLocation;
                case 2:
                    if (isBypass) {
                        return this.mFineBypassLocation;
                    }
                    return this.mFineLocation;
                default:
                    throw new java.lang.AssertionError();
            }
        }

        public void set(android.location.Location location) {
            this.mFineLocation = calculateNextFine(this.mFineLocation, location);
            this.mCoarseLocation = calculateNextCoarse(this.mCoarseLocation, location);
        }

        public void setBypass(android.location.Location location) {
            this.mFineBypassLocation = calculateNextFine(this.mFineBypassLocation, location);
            this.mCoarseBypassLocation = calculateNextCoarse(this.mCoarseBypassLocation, location);
        }

        private android.location.Location calculateNextFine(android.location.Location oldFine, android.location.Location newFine) {
            if (oldFine == null || newFine.getElapsedRealtimeNanos() > oldFine.getElapsedRealtimeNanos()) {
                return newFine;
            }
            return oldFine;
        }

        private android.location.Location calculateNextCoarse(android.location.Location oldCoarse, android.location.Location newCoarse) {
            if (oldCoarse == null || newCoarse.getElapsedRealtimeMillis() - 600000 > oldCoarse.getElapsedRealtimeMillis()) {
                return newCoarse;
            }
            return oldCoarse;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class PendingIntentSender {
        private PendingIntentSender() {
        }

        public static void send(android.app.PendingIntent pendingIntent, android.content.Context context, android.content.Intent intent, java.lang.Runnable callback, android.os.Bundle options) throws android.app.PendingIntent.CanceledException {
            final com.android.server.location.provider.LocationProviderManager.PendingIntentSender.GatedCallback gatedCallback;
            android.app.PendingIntent.OnFinished onFinished;
            if (callback != null) {
                gatedCallback = new com.android.server.location.provider.LocationProviderManager.PendingIntentSender.GatedCallback(callback);
                onFinished = new android.app.PendingIntent.OnFinished() { // from class: com.android.server.location.provider.LocationProviderManager$PendingIntentSender$$ExternalSyntheticLambda0
                    @Override // android.app.PendingIntent.OnFinished
                    public final void onSendFinished(android.app.PendingIntent pendingIntent2, android.content.Intent intent2, int i, java.lang.String str, android.os.Bundle bundle) {
                        gatedCallback.run();
                    }
                };
            } else {
                gatedCallback = null;
                onFinished = null;
            }
            pendingIntent.send(context, 0, intent, onFinished, null, null, options);
            if (gatedCallback != null) {
                gatedCallback.allow();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        static class GatedCallback implements java.lang.Runnable {
            private java.lang.Runnable mCallback;
            private boolean mGate;
            private boolean mRun;

            private GatedCallback(java.lang.Runnable callback) {
                this.mCallback = callback;
            }

            public void allow() {
                java.lang.Runnable callback = null;
                synchronized (this) {
                    this.mGate = true;
                    if (this.mRun && this.mCallback != null) {
                        callback = this.mCallback;
                        this.mCallback = null;
                    }
                }
                if (callback != null) {
                    callback.run();
                }
            }

            @Override // java.lang.Runnable
            public void run() {
                java.lang.Runnable callback = null;
                synchronized (this) {
                    this.mRun = true;
                    if (this.mGate && this.mCallback != null) {
                        callback = this.mCallback;
                        this.mCallback = null;
                    }
                }
                if (callback != null) {
                    callback.run();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class ExternalWakeLockReleaser extends android.os.IRemoteCallback.Stub {
        private final android.location.util.identity.CallerIdentity mIdentity;
        private final android.os.PowerManager.WakeLock mWakeLock;

        ExternalWakeLockReleaser(android.location.util.identity.CallerIdentity identity, android.os.PowerManager.WakeLock wakeLock) {
            this.mIdentity = identity;
            this.mWakeLock = (android.os.PowerManager.WakeLock) java.util.Objects.requireNonNull(wakeLock);
        }

        public void sendResult(android.os.Bundle data) {
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                try {
                    this.mWakeLock.release();
                } catch (java.lang.RuntimeException e) {
                    if (e.getClass() == java.lang.RuntimeException.class) {
                        android.util.Log.e(com.android.server.location.LocationManagerService.TAG, "wakelock over-released by " + this.mIdentity, e);
                    } else {
                        com.android.server.FgThread.getExecutor().execute(new java.lang.Runnable() { // from class: com.android.server.location.provider.LocationProviderManager$ExternalWakeLockReleaser$$ExternalSyntheticLambda0
                            @Override // java.lang.Runnable
                            public final void run() {
                                com.android.server.location.provider.LocationProviderManager.ExternalWakeLockReleaser.lambda$sendResult$0(e);
                            }
                        });
                        throw e;
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        static /* synthetic */ void lambda$sendResult$0(java.lang.RuntimeException e) {
            throw new java.lang.AssertionError(e);
        }
    }

    public com.android.server.location.provider.ILocationProviderManagerWrapper getWrapper() {
        return this.mLocationProviderManagerWrapper;
    }

    public static void oplusSystemReady(android.content.Context context) {
        if (mOplusLbsClass == null) {
            mOplusLbsClass = (com.android.server.location.interfaces.IOplusLBSMainClass) com.android.server.location.common.OplusLbsFactory.getInstance().getFeature(com.android.server.location.interfaces.IOplusLBSMainClass.DEFAULT, context);
        }
        mLocationFreeze = (com.android.server.location.interfaces.ILocationFreezeProc) com.android.server.location.common.OplusLbsFactory.getInstance().getFeature(com.android.server.location.interfaces.ILocationFreezeProc.DEFAULT, context);
    }

    private class LocationProviderManagerWrapper implements com.android.server.location.provider.ILocationProviderManagerWrapper {
        private LocationProviderManagerWrapper() {
        }

        @Override // com.android.server.location.provider.ILocationProviderManagerWrapper
        public void backgroundThrottleIntervalChanged() {
            com.android.server.location.provider.LocationProviderManager.this.onBackgroundThrottleIntervalChanged();
        }
    }
}
