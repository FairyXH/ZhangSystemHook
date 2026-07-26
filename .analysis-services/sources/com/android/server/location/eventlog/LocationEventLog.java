package com.android.server.location.eventlog;

/* JADX INFO: loaded from: classes2.dex */
public class LocationEventLog extends com.android.server.location.eventlog.LocalEventLog<java.lang.Object> {
    public static final com.android.server.location.eventlog.LocationEventLog EVENT_LOG = new com.android.server.location.eventlog.LocationEventLog();
    private final android.util.ArrayMap<java.lang.String, android.util.ArrayMap<android.location.util.identity.CallerIdentity, com.android.server.location.eventlog.LocationEventLog.AggregateStats>> mAggregateStats;
    private final android.util.ArrayMap<android.location.util.identity.CallerIdentity, com.android.server.location.eventlog.LocationEventLog.GnssMeasurementAggregateStats> mGnssMeasAggregateStats;
    private final com.android.server.location.eventlog.ILocationEventLogWrapper mLocationWrapper;
    private final com.android.server.location.eventlog.LocationEventLog.LocationsEventLog mLocationsLog;

    private static int getLogSize() {
        if (com.android.server.location.LocationManagerService.D) {
            return 600;
        }
        return 300;
    }

    private static int getLocationsLogSize() {
        if (com.android.server.location.LocationManagerService.D) {
            return 400;
        }
        return 200;
    }

    private LocationEventLog() {
        super(getLogSize(), java.lang.Object.class);
        this.mLocationWrapper = new com.android.server.location.eventlog.LocationEventLog.LocationEventLogWrapper();
        this.mAggregateStats = new android.util.ArrayMap<>(4);
        this.mGnssMeasAggregateStats = new android.util.ArrayMap<>();
        this.mLocationsLog = new com.android.server.location.eventlog.LocationEventLog.LocationsEventLog(getLocationsLogSize());
    }

    public android.util.ArrayMap<java.lang.String, android.util.ArrayMap<android.location.util.identity.CallerIdentity, com.android.server.location.eventlog.LocationEventLog.AggregateStats>> copyAggregateStats() {
        android.util.ArrayMap<java.lang.String, android.util.ArrayMap<android.location.util.identity.CallerIdentity, com.android.server.location.eventlog.LocationEventLog.AggregateStats>> copy;
        synchronized (this.mAggregateStats) {
            copy = new android.util.ArrayMap<>(this.mAggregateStats);
            for (int i = 0; i < copy.size(); i++) {
                copy.setValueAt(i, new android.util.ArrayMap<>(copy.valueAt(i)));
            }
        }
        return copy;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.location.eventlog.LocationEventLog.AggregateStats getAggregateStats(java.lang.String provider, android.location.util.identity.CallerIdentity identity) {
        com.android.server.location.eventlog.LocationEventLog.AggregateStats stats;
        synchronized (this.mAggregateStats) {
            android.util.ArrayMap<android.location.util.identity.CallerIdentity, com.android.server.location.eventlog.LocationEventLog.AggregateStats> packageMap = this.mAggregateStats.get(provider);
            if (packageMap == null) {
                packageMap = new android.util.ArrayMap<>(2);
                this.mAggregateStats.put(provider, packageMap);
            }
            android.location.util.identity.CallerIdentity aggregate = android.location.util.identity.CallerIdentity.forAggregation(identity);
            stats = packageMap.get(aggregate);
            if (stats == null) {
                stats = new com.android.server.location.eventlog.LocationEventLog.AggregateStats();
                packageMap.put(aggregate, stats);
            }
        }
        return stats;
    }

    public android.util.ArrayMap<android.location.util.identity.CallerIdentity, com.android.server.location.eventlog.LocationEventLog.GnssMeasurementAggregateStats> copyGnssMeasurementAggregateStats() {
        android.util.ArrayMap<android.location.util.identity.CallerIdentity, com.android.server.location.eventlog.LocationEventLog.GnssMeasurementAggregateStats> copy;
        synchronized (this.mGnssMeasAggregateStats) {
            copy = new android.util.ArrayMap<>(this.mGnssMeasAggregateStats);
        }
        return copy;
    }

    private com.android.server.location.eventlog.LocationEventLog.GnssMeasurementAggregateStats getGnssMeasurementAggregateStats(android.location.util.identity.CallerIdentity identity) {
        com.android.server.location.eventlog.LocationEventLog.GnssMeasurementAggregateStats stats;
        synchronized (this.mGnssMeasAggregateStats) {
            android.location.util.identity.CallerIdentity aggregate = android.location.util.identity.CallerIdentity.forAggregation(identity);
            stats = this.mGnssMeasAggregateStats.get(aggregate);
            if (stats == null) {
                stats = new com.android.server.location.eventlog.LocationEventLog.GnssMeasurementAggregateStats();
                this.mGnssMeasAggregateStats.put(aggregate, stats);
            }
        }
        return stats;
    }

    public void logUserSwitched(int userIdFrom, int userIdTo) {
        addLog(new com.android.server.location.eventlog.LocationEventLog.UserSwitchedEvent(userIdFrom, userIdTo));
    }

    public void logUserVisibilityChanged(int userId, boolean visible) {
        addLog(new com.android.server.location.eventlog.LocationEventLog.UserVisibilityChangedEvent(userId, visible));
    }

    public void logLocationEnabled(int userId, boolean enabled) {
        addLog(new com.android.server.location.eventlog.LocationEventLog.LocationEnabledEvent(userId, enabled));
    }

    public void logAdasLocationEnabled(int userId, boolean enabled) {
        addLog(new com.android.server.location.eventlog.LocationEventLog.LocationAdasEnabledEvent(userId, enabled));
    }

    public void logProviderEnabled(java.lang.String provider, int userId, boolean enabled) {
        addLog(new com.android.server.location.eventlog.LocationEventLog.ProviderEnabledEvent(provider, userId, enabled));
    }

    public void logProviderMocked(java.lang.String provider, boolean mocked) {
        addLog(new com.android.server.location.eventlog.LocationEventLog.ProviderMockedEvent(provider, mocked));
    }

    public void logProviderClientRegistered(java.lang.String provider, android.location.util.identity.CallerIdentity identity, android.location.LocationRequest request) {
        addLog(new com.android.server.location.eventlog.LocationEventLog.ProviderClientRegisterEvent(provider, true, identity, request));
        getAggregateStats(provider, identity).markRequestAdded(request.getIntervalMillis());
    }

    public void logProviderClientUnregistered(java.lang.String provider, android.location.util.identity.CallerIdentity identity) {
        addLog(new com.android.server.location.eventlog.LocationEventLog.ProviderClientRegisterEvent(provider, false, identity, null));
        getAggregateStats(provider, identity).markRequestRemoved();
    }

    public void logProviderClientActive(java.lang.String provider, android.location.util.identity.CallerIdentity identity) {
        getAggregateStats(provider, identity).markRequestActive();
    }

    public void logProviderClientInactive(java.lang.String provider, android.location.util.identity.CallerIdentity identity) {
        getAggregateStats(provider, identity).markRequestInactive();
    }

    public void logProviderClientForeground(java.lang.String provider, android.location.util.identity.CallerIdentity identity) {
        if (com.android.server.location.LocationManagerService.D) {
            addLog(new com.android.server.location.eventlog.LocationEventLog.ProviderClientForegroundEvent(provider, true, identity));
        }
        getAggregateStats(provider, identity).markRequestForeground();
    }

    public void logProviderClientBackground(java.lang.String provider, android.location.util.identity.CallerIdentity identity) {
        if (com.android.server.location.LocationManagerService.D) {
            addLog(new com.android.server.location.eventlog.LocationEventLog.ProviderClientForegroundEvent(provider, false, identity));
        }
        getAggregateStats(provider, identity).markRequestBackground();
    }

    public void logProviderClientPermitted(java.lang.String provider, android.location.util.identity.CallerIdentity identity) {
        if (com.android.server.location.LocationManagerService.D) {
            addLog(new com.android.server.location.eventlog.LocationEventLog.ProviderClientPermittedEvent(provider, true, identity));
        }
    }

    public void logProviderClientUnpermitted(java.lang.String provider, android.location.util.identity.CallerIdentity identity) {
        if (com.android.server.location.LocationManagerService.D) {
            addLog(new com.android.server.location.eventlog.LocationEventLog.ProviderClientPermittedEvent(provider, false, identity));
        }
    }

    public void logProviderUpdateRequest(java.lang.String provider, android.location.provider.ProviderRequest request) {
        addLog(new com.android.server.location.eventlog.LocationEventLog.ProviderUpdateEvent(provider, request));
    }

    public void logProviderReceivedLocations(java.lang.String provider, int numLocations) {
        synchronized (this) {
            this.mLocationsLog.logProviderReceivedLocations(provider, numLocations);
        }
    }

    public void logProviderDeliveredLocations(java.lang.String provider, int numLocations, android.location.util.identity.CallerIdentity identity) {
        if (getExtLoader() != null && getExtLoader().enablePassiveDeliveredLocations(provider)) {
            getAggregateStats(provider, identity).markLocationDelivered();
            return;
        }
        synchronized (this) {
            this.mLocationsLog.logProviderDeliveredLocations(provider, numLocations, identity);
        }
        getAggregateStats(provider, identity).markLocationDelivered();
    }

    public void logProviderStationaryThrottled(java.lang.String provider, boolean throttled, android.location.provider.ProviderRequest request) {
        addLog(new com.android.server.location.eventlog.LocationEventLog.ProviderStationaryThrottledEvent(provider, throttled, request));
    }

    public void logLocationPowerSaveMode(int locationPowerSaveMode) {
        addLog(new com.android.server.location.eventlog.LocationEventLog.LocationPowerSaveModeEvent(locationPowerSaveMode));
    }

    public void logGnssMeasurementClientRegistered(android.location.util.identity.CallerIdentity identity, android.location.GnssMeasurementRequest request) {
        addLog(new com.android.server.location.eventlog.LocationEventLog.GnssMeasurementClientRegisterEvent(true, identity, request));
        getGnssMeasurementAggregateStats(identity).markRequestAdded(request.getIntervalMillis(), request.isFullTracking());
    }

    public void logGnssMeasurementClientUnregistered(android.location.util.identity.CallerIdentity identity) {
        addLog(new com.android.server.location.eventlog.LocationEventLog.GnssMeasurementClientRegisterEvent(false, identity, null));
        getGnssMeasurementAggregateStats(identity).markRequestRemoved();
    }

    public void logGnssMeasurementsDelivered(int numGnssMeasurements, android.location.util.identity.CallerIdentity identity) {
        synchronized (this) {
            this.mLocationsLog.logDeliveredGnssMeasurements(numGnssMeasurements, identity);
        }
        getGnssMeasurementAggregateStats(identity).markGnssMeasurementDelivered();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addLog(java.lang.Object logEvent) {
        addLog(android.os.SystemClock.elapsedRealtime(), logEvent);
    }

    @Override // com.android.server.location.eventlog.LocalEventLog
    public synchronized void iterate(com.android.server.location.eventlog.LocalEventLog.LogConsumer<? super java.lang.Object> consumer) {
        iterate(consumer, this, this.mLocationsLog);
    }

    public void iterate(java.util.function.Consumer<java.lang.String> consumer) {
        iterate(consumer, (java.lang.String) null);
    }

    public void iterate(final java.util.function.Consumer<java.lang.String> consumer, final java.lang.String providerFilter) {
        final long systemTimeDeltaMs = java.lang.System.currentTimeMillis() - android.os.SystemClock.elapsedRealtime();
        final java.lang.StringBuilder builder = new java.lang.StringBuilder();
        iterate(new com.android.server.location.eventlog.LocalEventLog.LogConsumer() { // from class: com.android.server.location.eventlog.LocationEventLog$$ExternalSyntheticLambda0
            @Override // com.android.server.location.eventlog.LocalEventLog.LogConsumer
            public final void acceptLog(long j, java.lang.Object obj) {
                com.android.server.location.eventlog.LocationEventLog.lambda$iterate$0(providerFilter, builder, systemTimeDeltaMs, consumer, j, obj);
            }
        });
    }

    static /* synthetic */ void lambda$iterate$0(java.lang.String providerFilter, java.lang.StringBuilder builder, long systemTimeDeltaMs, java.util.function.Consumer consumer, long time, java.lang.Object logEvent) {
        boolean match = providerFilter == null || ((logEvent instanceof com.android.server.location.eventlog.LocationEventLog.ProviderEvent) && providerFilter.equals(((com.android.server.location.eventlog.LocationEventLog.ProviderEvent) logEvent).mProvider));
        if (match) {
            builder.setLength(0);
            builder.append(android.util.TimeUtils.logTimeOfDay(time + systemTimeDeltaMs));
            builder.append(": ");
            builder.append(logEvent);
            consumer.accept(builder.toString());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static abstract class ProviderEvent {
        protected final java.lang.String mProvider;

        ProviderEvent(java.lang.String provider) {
            this.mProvider = provider;
        }
    }

    private static final class ProviderEnabledEvent extends com.android.server.location.eventlog.LocationEventLog.ProviderEvent {
        private final boolean mEnabled;
        private final int mUserId;

        ProviderEnabledEvent(java.lang.String provider, int userId, boolean enabled) {
            super(provider);
            this.mUserId = userId;
            this.mEnabled = enabled;
        }

        public java.lang.String toString() {
            return this.mProvider + " provider [u" + this.mUserId + "] " + (this.mEnabled ? com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_ENABLED : com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_DISABLED);
        }
    }

    private static final class ProviderMockedEvent extends com.android.server.location.eventlog.LocationEventLog.ProviderEvent {
        private final boolean mMocked;

        ProviderMockedEvent(java.lang.String provider, boolean mocked) {
            super(provider);
            this.mMocked = mocked;
        }

        public java.lang.String toString() {
            if (this.mMocked) {
                return this.mProvider + " provider added mock provider override";
            }
            return this.mProvider + " provider removed mock provider override";
        }
    }

    private static final class ProviderClientRegisterEvent extends com.android.server.location.eventlog.LocationEventLog.ProviderEvent {
        private final android.location.util.identity.CallerIdentity mIdentity;
        private final android.location.LocationRequest mLocationRequest;
        private final boolean mRegistered;

        ProviderClientRegisterEvent(java.lang.String provider, boolean registered, android.location.util.identity.CallerIdentity identity, android.location.LocationRequest locationRequest) {
            super(provider);
            this.mRegistered = registered;
            this.mIdentity = identity;
            this.mLocationRequest = locationRequest;
        }

        public java.lang.String toString() {
            if (this.mRegistered) {
                return this.mProvider + " provider +registration " + this.mIdentity + " -> " + this.mLocationRequest;
            }
            return this.mProvider + " provider -registration " + this.mIdentity;
        }
    }

    private static final class ProviderClientForegroundEvent extends com.android.server.location.eventlog.LocationEventLog.ProviderEvent {
        private final boolean mForeground;
        private final android.location.util.identity.CallerIdentity mIdentity;

        ProviderClientForegroundEvent(java.lang.String provider, boolean foreground, android.location.util.identity.CallerIdentity identity) {
            super(provider);
            this.mForeground = foreground;
            this.mIdentity = identity;
        }

        public java.lang.String toString() {
            return this.mProvider + " provider client " + this.mIdentity + " -> " + (this.mForeground ? "foreground" : "background");
        }
    }

    private static final class ProviderClientPermittedEvent extends com.android.server.location.eventlog.LocationEventLog.ProviderEvent {
        private final android.location.util.identity.CallerIdentity mIdentity;
        private final boolean mPermitted;

        ProviderClientPermittedEvent(java.lang.String provider, boolean permitted, android.location.util.identity.CallerIdentity identity) {
            super(provider);
            this.mPermitted = permitted;
            this.mIdentity = identity;
        }

        public java.lang.String toString() {
            return this.mProvider + " provider client " + this.mIdentity + " -> " + (this.mPermitted ? "permitted" : "unpermitted");
        }
    }

    private static final class ProviderUpdateEvent extends com.android.server.location.eventlog.LocationEventLog.ProviderEvent {
        private final android.location.provider.ProviderRequest mRequest;

        ProviderUpdateEvent(java.lang.String provider, android.location.provider.ProviderRequest request) {
            super(provider);
            this.mRequest = request;
        }

        public java.lang.String toString() {
            return this.mProvider + " provider request = " + this.mRequest;
        }
    }

    private static final class ProviderReceiveLocationEvent extends com.android.server.location.eventlog.LocationEventLog.ProviderEvent {
        private final int mNumLocations;

        ProviderReceiveLocationEvent(java.lang.String provider, int numLocations) {
            super(provider);
            this.mNumLocations = numLocations;
        }

        public java.lang.String toString() {
            return this.mProvider + " provider received location[" + this.mNumLocations + "]";
        }
    }

    private static final class ProviderDeliverLocationEvent extends com.android.server.location.eventlog.LocationEventLog.ProviderEvent {
        private final android.location.util.identity.CallerIdentity mIdentity;
        private final int mNumLocations;

        ProviderDeliverLocationEvent(java.lang.String provider, int numLocations, android.location.util.identity.CallerIdentity identity) {
            super(provider);
            this.mNumLocations = numLocations;
            this.mIdentity = identity;
        }

        public java.lang.String toString() {
            return this.mProvider + " provider delivered location[" + this.mNumLocations + "] to " + this.mIdentity;
        }
    }

    private static final class ProviderStationaryThrottledEvent extends com.android.server.location.eventlog.LocationEventLog.ProviderEvent {
        private final android.location.provider.ProviderRequest mRequest;
        private final boolean mStationaryThrottled;

        ProviderStationaryThrottledEvent(java.lang.String provider, boolean stationaryThrottled, android.location.provider.ProviderRequest request) {
            super(provider);
            this.mStationaryThrottled = stationaryThrottled;
            this.mRequest = request;
        }

        public java.lang.String toString() {
            return this.mProvider + " provider stationary/idle " + (this.mStationaryThrottled ? "throttled" : "unthrottled") + ", request = " + this.mRequest;
        }
    }

    private static final class LocationPowerSaveModeEvent {
        private final int mLocationPowerSaveMode;

        LocationPowerSaveModeEvent(int locationPowerSaveMode) {
            this.mLocationPowerSaveMode = locationPowerSaveMode;
        }

        public java.lang.String toString() {
            java.lang.String mode;
            switch (this.mLocationPowerSaveMode) {
                case 0:
                    mode = "NO_CHANGE";
                    break;
                case 1:
                    mode = "GPS_DISABLED_WHEN_SCREEN_OFF";
                    break;
                case 2:
                    mode = "ALL_DISABLED_WHEN_SCREEN_OFF";
                    break;
                case 3:
                    mode = "FOREGROUND_ONLY";
                    break;
                case 4:
                    mode = "THROTTLE_REQUESTS_WHEN_SCREEN_OFF";
                    break;
                default:
                    mode = "UNKNOWN";
                    break;
            }
            return "location power save mode changed to " + mode;
        }
    }

    private static final class UserSwitchedEvent {
        private final int mUserIdFrom;
        private final int mUserIdTo;

        UserSwitchedEvent(int userIdFrom, int userIdTo) {
            this.mUserIdFrom = userIdFrom;
            this.mUserIdTo = userIdTo;
        }

        public java.lang.String toString() {
            return "current user switched from u" + this.mUserIdFrom + " to u" + this.mUserIdTo;
        }
    }

    private static final class UserVisibilityChangedEvent {
        private final int mUserId;
        private final boolean mVisible;

        UserVisibilityChangedEvent(int userId, boolean visible) {
            this.mUserId = userId;
            this.mVisible = visible;
        }

        public java.lang.String toString() {
            return "[u" + this.mUserId + "] " + (this.mVisible ? com.android.server.wm.ActivityTaskManagerService.DUMP_VISIBLE_ACTIVITIES : "invisible");
        }
    }

    private static final class LocationEnabledEvent {
        private final boolean mEnabled;
        private final int mUserId;

        LocationEnabledEvent(int userId, boolean enabled) {
            this.mUserId = userId;
            this.mEnabled = enabled;
        }

        public java.lang.String toString() {
            return "location [u" + this.mUserId + "] " + (this.mEnabled ? com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_ENABLED : com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_DISABLED);
        }
    }

    private static final class LocationAdasEnabledEvent {
        private final boolean mEnabled;
        private final int mUserId;

        LocationAdasEnabledEvent(int userId, boolean enabled) {
            this.mUserId = userId;
            this.mEnabled = enabled;
        }

        public java.lang.String toString() {
            return "adas location [u" + this.mUserId + "] " + (this.mEnabled ? com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_ENABLED : com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_DISABLED);
        }
    }

    private static final class GnssMeasurementClientRegisterEvent {
        private final android.location.GnssMeasurementRequest mGnssMeasurementRequest;
        private final android.location.util.identity.CallerIdentity mIdentity;
        private final boolean mRegistered;

        GnssMeasurementClientRegisterEvent(boolean registered, android.location.util.identity.CallerIdentity identity, android.location.GnssMeasurementRequest measurementRequest) {
            this.mRegistered = registered;
            this.mIdentity = identity;
            this.mGnssMeasurementRequest = measurementRequest;
        }

        public java.lang.String toString() {
            if (this.mRegistered) {
                return "gnss measurements +registration " + this.mIdentity + " -> " + this.mGnssMeasurementRequest;
            }
            return "gnss measurements -registration " + this.mIdentity;
        }
    }

    private static final class GnssMeasurementDeliverEvent {
        private final android.location.util.identity.CallerIdentity mIdentity;
        private final int mNumGnssMeasurements;

        GnssMeasurementDeliverEvent(int numGnssMeasurements, android.location.util.identity.CallerIdentity identity) {
            this.mNumGnssMeasurements = numGnssMeasurements;
            this.mIdentity = identity;
        }

        public java.lang.String toString() {
            return "gnss measurements delivered GnssMeasurements[" + this.mNumGnssMeasurements + "] to " + this.mIdentity;
        }
    }

    private static final class LocationsEventLog extends com.android.server.location.eventlog.LocalEventLog<java.lang.Object> {
        LocationsEventLog(int size) {
            super(size, java.lang.Object.class);
        }

        public void logProviderReceivedLocations(java.lang.String provider, int numLocations) {
            addLog(new com.android.server.location.eventlog.LocationEventLog.ProviderReceiveLocationEvent(provider, numLocations));
        }

        public void logDeliveredGnssMeasurements(int numGnssMeasurements, android.location.util.identity.CallerIdentity identity) {
            addLog(new com.android.server.location.eventlog.LocationEventLog.GnssMeasurementDeliverEvent(numGnssMeasurements, identity));
        }

        public void logProviderDeliveredLocations(java.lang.String provider, int numLocations, android.location.util.identity.CallerIdentity identity) {
            addLog(new com.android.server.location.eventlog.LocationEventLog.ProviderDeliverLocationEvent(provider, numLocations, identity));
        }

        private void addLog(java.lang.Object logEvent) {
            addLog(android.os.SystemClock.elapsedRealtime(), logEvent);
        }
    }

    public static final class AggregateStats {
        private int mActiveRequestCount;
        private long mActiveTimeLastUpdateRealtimeMs;
        private long mActiveTimeTotalMs;
        private int mAddedRequestCount;
        private long mAddedTimeLastUpdateRealtimeMs;
        private long mAddedTimeTotalMs;
        private int mDeliveredLocationCount;
        private int mForegroundRequestCount;
        private long mForegroundTimeLastUpdateRealtimeMs;
        private long mForegroundTimeTotalMs;
        private long mFastestIntervalMs = Long.MAX_VALUE;
        private long mSlowestIntervalMs = 0;

        AggregateStats() {
        }

        synchronized void markRequestAdded(long intervalMillis) {
            int i = this.mAddedRequestCount;
            this.mAddedRequestCount = i + 1;
            if (i == 0) {
                this.mAddedTimeLastUpdateRealtimeMs = android.os.SystemClock.elapsedRealtime();
            }
            this.mFastestIntervalMs = java.lang.Math.min(intervalMillis, this.mFastestIntervalMs);
            this.mSlowestIntervalMs = java.lang.Math.max(intervalMillis, this.mSlowestIntervalMs);
        }

        synchronized void markRequestRemoved() {
            updateTotals();
            boolean z = true;
            this.mAddedRequestCount--;
            if (this.mAddedRequestCount < 0) {
                z = false;
            }
            com.android.internal.util.Preconditions.checkState(z);
            this.mActiveRequestCount = java.lang.Math.min(this.mAddedRequestCount, this.mActiveRequestCount);
            this.mForegroundRequestCount = java.lang.Math.min(this.mAddedRequestCount, this.mForegroundRequestCount);
        }

        synchronized void markRequestActive() {
            com.android.internal.util.Preconditions.checkState(this.mAddedRequestCount > 0);
            int i = this.mActiveRequestCount;
            this.mActiveRequestCount = i + 1;
            if (i == 0) {
                this.mActiveTimeLastUpdateRealtimeMs = android.os.SystemClock.elapsedRealtime();
            }
        }

        synchronized void markRequestInactive() {
            updateTotals();
            boolean z = true;
            this.mActiveRequestCount--;
            if (this.mActiveRequestCount < 0) {
                z = false;
            }
            com.android.internal.util.Preconditions.checkState(z);
        }

        synchronized void markRequestForeground() {
            com.android.internal.util.Preconditions.checkState(this.mAddedRequestCount > 0);
            int i = this.mForegroundRequestCount;
            this.mForegroundRequestCount = i + 1;
            if (i == 0) {
                this.mForegroundTimeLastUpdateRealtimeMs = android.os.SystemClock.elapsedRealtime();
            }
        }

        synchronized void markRequestBackground() {
            updateTotals();
            boolean z = true;
            this.mForegroundRequestCount--;
            if (this.mForegroundRequestCount < 0) {
                z = false;
            }
            com.android.internal.util.Preconditions.checkState(z);
        }

        synchronized void markLocationDelivered() {
            this.mDeliveredLocationCount++;
        }

        public synchronized void updateTotals() {
            if (this.mAddedRequestCount > 0) {
                long realtimeMs = android.os.SystemClock.elapsedRealtime();
                this.mAddedTimeTotalMs += realtimeMs - this.mAddedTimeLastUpdateRealtimeMs;
                this.mAddedTimeLastUpdateRealtimeMs = realtimeMs;
            }
            if (this.mActiveRequestCount > 0) {
                long realtimeMs2 = android.os.SystemClock.elapsedRealtime();
                this.mActiveTimeTotalMs += realtimeMs2 - this.mActiveTimeLastUpdateRealtimeMs;
                this.mActiveTimeLastUpdateRealtimeMs = realtimeMs2;
            }
            if (this.mForegroundRequestCount > 0) {
                long realtimeMs3 = android.os.SystemClock.elapsedRealtime();
                this.mForegroundTimeTotalMs += realtimeMs3 - this.mForegroundTimeLastUpdateRealtimeMs;
                this.mForegroundTimeLastUpdateRealtimeMs = realtimeMs3;
            }
        }

        public synchronized java.lang.String toString() {
            return "min/max interval = " + intervalToString(this.mFastestIntervalMs) + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + intervalToString(this.mSlowestIntervalMs) + ", total/active/foreground duration = " + android.util.TimeUtils.formatDuration(this.mAddedTimeTotalMs) + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + android.util.TimeUtils.formatDuration(this.mActiveTimeTotalMs) + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + android.util.TimeUtils.formatDuration(this.mForegroundTimeTotalMs) + ", locations = " + this.mDeliveredLocationCount;
        }

        private static java.lang.String intervalToString(long intervalMs) {
            if (intervalMs == Long.MAX_VALUE) {
                return "passive";
            }
            return java.util.concurrent.TimeUnit.MILLISECONDS.toSeconds(intervalMs) + "s";
        }
    }

    public static final class GnssMeasurementAggregateStats {
        private int mAddedRequestCount;
        private long mAddedTimeLastUpdateRealtimeMs;
        private long mAddedTimeTotalMs;
        private boolean mHasDutyCycling;
        private boolean mHasFullTracking;
        private int mReceivedMeasurementEventCount;
        private long mFastestIntervalMs = Long.MAX_VALUE;
        private long mSlowestIntervalMs = 0;

        GnssMeasurementAggregateStats() {
        }

        synchronized void markRequestAdded(long intervalMillis, boolean fullTracking) {
            int i = this.mAddedRequestCount;
            this.mAddedRequestCount = i + 1;
            if (i == 0) {
                this.mAddedTimeLastUpdateRealtimeMs = android.os.SystemClock.elapsedRealtime();
            }
            if (fullTracking) {
                this.mHasFullTracking = true;
            } else {
                this.mHasDutyCycling = true;
            }
            this.mFastestIntervalMs = java.lang.Math.min(intervalMillis, this.mFastestIntervalMs);
            this.mSlowestIntervalMs = java.lang.Math.max(intervalMillis, this.mSlowestIntervalMs);
        }

        synchronized void markRequestRemoved() {
            updateTotals();
            boolean z = true;
            this.mAddedRequestCount--;
            if (this.mAddedRequestCount < 0) {
                z = false;
            }
            com.android.internal.util.Preconditions.checkState(z);
        }

        synchronized void markGnssMeasurementDelivered() {
            this.mReceivedMeasurementEventCount++;
        }

        public synchronized void updateTotals() {
            if (this.mAddedRequestCount > 0) {
                long realtimeMs = android.os.SystemClock.elapsedRealtime();
                this.mAddedTimeTotalMs += realtimeMs - this.mAddedTimeLastUpdateRealtimeMs;
                this.mAddedTimeLastUpdateRealtimeMs = realtimeMs;
            }
        }

        public synchronized java.lang.String toString() {
            return "min/max interval = " + intervalToString(this.mFastestIntervalMs) + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + intervalToString(this.mSlowestIntervalMs) + ", total duration = " + android.util.TimeUtils.formatDuration(this.mAddedTimeTotalMs) + ", tracking mode = " + trackingModeToString() + ", GNSS measurement events = " + this.mReceivedMeasurementEventCount;
        }

        private static java.lang.String intervalToString(long intervalMs) {
            if (intervalMs == 2147483647L) {
                return "passive";
            }
            return java.util.concurrent.TimeUnit.MILLISECONDS.toSeconds(intervalMs) + "s";
        }

        private java.lang.String trackingModeToString() {
            if (this.mHasFullTracking && this.mHasDutyCycling) {
                return "mixed tracking mode";
            }
            if (this.mHasFullTracking) {
                return "always full-tracking";
            }
            return "always duty-cycling";
        }
    }

    public com.android.server.location.eventlog.ILocationEventLogWrapper getLocationWrapper() {
        return this.mLocationWrapper;
    }

    private com.android.server.location.interfaces.ILocationEventLogExt getExtLoader() {
        return (com.android.server.location.interfaces.ILocationEventLogExt) com.android.server.location.common.OplusLbsFactory.getInstance().getFeature(com.android.server.location.interfaces.ILocationEventLogExt.DEFAULT, null);
    }

    private class LocationEventLogWrapper implements com.android.server.location.eventlog.ILocationEventLogWrapper {
        private LocationEventLogWrapper() {
        }

        @Override // com.android.server.location.eventlog.ILocationEventLogWrapper
        public void addExtLog(java.lang.Object logEvent) {
            com.android.server.location.eventlog.LocationEventLog.this.addLog(android.os.SystemClock.elapsedRealtime(), logEvent);
        }

        @Override // com.android.server.location.eventlog.ILocationEventLogWrapper
        public void updateEventsLocationSize(int newSize) {
            com.android.server.location.eventlog.LocationEventLog.this.mLocationsLog.getLocalWrapper().updateEventsLogSize(newSize);
        }

        @Override // com.android.server.location.eventlog.ILocationEventLogWrapper
        public void addLogToProviderEvent(java.lang.String provider, android.location.util.identity.CallerIdentity identity, java.lang.Object logEvent, long intervalMillis, boolean register) {
            com.android.server.location.eventlog.LocationEventLog.this.addLog(logEvent);
            if (register) {
                com.android.server.location.eventlog.LocationEventLog.this.getAggregateStats(provider, identity).markRequestAdded(intervalMillis);
            } else {
                com.android.server.location.eventlog.LocationEventLog.this.getAggregateStats(provider, identity).markRequestRemoved();
            }
        }
    }
}
