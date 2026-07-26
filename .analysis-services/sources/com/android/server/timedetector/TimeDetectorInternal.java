package com.android.server.timedetector;

/* JADX INFO: loaded from: classes3.dex */
public interface TimeDetectorInternal {
    void addNetworkTimeUpdateListener(com.android.server.timezonedetector.StateChangeListener stateChangeListener);

    android.app.time.TimeCapabilitiesAndConfig getCapabilitiesAndConfigForDpm();

    com.android.server.timedetector.NetworkTimeSuggestion getLatestNetworkSuggestion();

    boolean setManualTimeForDpm(android.app.timedetector.ManualTimeSuggestion manualTimeSuggestion);

    void suggestGnssTime(com.android.server.timedetector.GnssTimeSuggestion gnssTimeSuggestion);

    void suggestNetworkTime(com.android.server.timedetector.NetworkTimeSuggestion networkTimeSuggestion);

    boolean updateConfigurationForDpm(android.app.time.TimeConfiguration timeConfiguration);
}
