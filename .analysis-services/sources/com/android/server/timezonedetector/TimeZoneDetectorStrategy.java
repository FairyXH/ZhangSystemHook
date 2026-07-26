package com.android.server.timezonedetector;

/* JADX INFO: loaded from: classes3.dex */
public interface TimeZoneDetectorStrategy extends com.android.server.timezonedetector.Dumpable {
    void addChangeListener(com.android.server.timezonedetector.StateChangeListener stateChangeListener);

    boolean confirmTimeZone(java.lang.String str);

    void enableTelephonyTimeZoneFallback(java.lang.String str);

    com.android.server.timezonedetector.MetricsTimeZoneDetectorState generateMetricsState();

    android.app.time.TimeZoneCapabilitiesAndConfig getCapabilitiesAndConfig(int i, boolean z);

    android.app.time.TimeZoneState getTimeZoneState();

    void handleLocationAlgorithmEvent(com.android.server.timezonedetector.LocationAlgorithmEvent locationAlgorithmEvent);

    boolean isGeoTimeZoneDetectionSupported();

    boolean isTelephonyTimeZoneDetectionSupported();

    void setTimeZoneState(android.app.time.TimeZoneState timeZoneState);

    boolean suggestManualTimeZone(int i, android.app.timezonedetector.ManualTimeZoneSuggestion manualTimeZoneSuggestion, boolean z);

    void suggestTelephonyTimeZone(android.app.timezonedetector.TelephonyTimeZoneSuggestion telephonyTimeZoneSuggestion);

    boolean updateConfiguration(int i, android.app.time.TimeZoneConfiguration timeZoneConfiguration, boolean z);
}
