package com.android.server.timezonedetector.location;

/* JADX INFO: loaded from: classes3.dex */
final class TimeZoneProviderRequest {
    private static final com.android.server.timezonedetector.location.TimeZoneProviderRequest STOP_UPDATES = new com.android.server.timezonedetector.location.TimeZoneProviderRequest(false, null, null);
    private final java.time.Duration mEventFilteringAgeThreshold;
    private final java.time.Duration mInitializationTimeout;
    private final boolean mSendUpdates;

    private TimeZoneProviderRequest(boolean sendUpdates, java.time.Duration initializationTimeout, java.time.Duration eventFilteringAgeThreshold) {
        this.mSendUpdates = sendUpdates;
        this.mInitializationTimeout = initializationTimeout;
        this.mEventFilteringAgeThreshold = eventFilteringAgeThreshold;
    }

    public static com.android.server.timezonedetector.location.TimeZoneProviderRequest createStartUpdatesRequest(java.time.Duration initializationTimeout, java.time.Duration eventFilteringAgeThreshold) {
        return new com.android.server.timezonedetector.location.TimeZoneProviderRequest(true, (java.time.Duration) java.util.Objects.requireNonNull(initializationTimeout), (java.time.Duration) java.util.Objects.requireNonNull(eventFilteringAgeThreshold));
    }

    public static com.android.server.timezonedetector.location.TimeZoneProviderRequest createStopUpdatesRequest() {
        return STOP_UPDATES;
    }

    public boolean sendUpdates() {
        return this.mSendUpdates;
    }

    public java.time.Duration getInitializationTimeout() {
        return this.mInitializationTimeout;
    }

    public java.time.Duration getEventFilteringAgeThreshold() {
        return this.mEventFilteringAgeThreshold;
    }

    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        com.android.server.timezonedetector.location.TimeZoneProviderRequest that = (com.android.server.timezonedetector.location.TimeZoneProviderRequest) o;
        if (this.mSendUpdates == that.mSendUpdates && java.util.Objects.equals(this.mInitializationTimeout, that.mInitializationTimeout) && java.util.Objects.equals(this.mEventFilteringAgeThreshold, that.mEventFilteringAgeThreshold)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return java.util.Objects.hash(java.lang.Boolean.valueOf(this.mSendUpdates), this.mInitializationTimeout, this.mEventFilteringAgeThreshold);
    }

    public java.lang.String toString() {
        return "TimeZoneProviderRequest{mSendUpdates=" + this.mSendUpdates + ", mInitializationTimeout=" + this.mInitializationTimeout + ", mEventFilteringAgeThreshold=" + this.mEventFilteringAgeThreshold + "}";
    }
}
