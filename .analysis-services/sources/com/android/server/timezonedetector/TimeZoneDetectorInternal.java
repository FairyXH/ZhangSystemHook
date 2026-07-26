package com.android.server.timezonedetector;

/* JADX INFO: loaded from: classes3.dex */
public interface TimeZoneDetectorInternal {
    com.android.server.timezonedetector.MetricsTimeZoneDetectorState generateMetricsState();

    android.app.time.TimeZoneCapabilitiesAndConfig getCapabilitiesAndConfigForDpm();

    void handleLocationAlgorithmEvent(com.android.server.timezonedetector.LocationAlgorithmEvent locationAlgorithmEvent);

    boolean setManualTimeZoneForDpm(android.app.timezonedetector.ManualTimeZoneSuggestion manualTimeZoneSuggestion);

    boolean updateConfigurationForDpm(android.app.time.TimeZoneConfiguration timeZoneConfiguration);
}
