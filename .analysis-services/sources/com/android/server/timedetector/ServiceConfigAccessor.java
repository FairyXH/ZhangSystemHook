package com.android.server.timedetector;

/* JADX INFO: loaded from: classes3.dex */
public interface ServiceConfigAccessor {
    void addConfigurationInternalChangeListener(com.android.server.timezonedetector.StateChangeListener stateChangeListener);

    com.android.server.timedetector.ConfigurationInternal getConfigurationInternal(int i);

    com.android.server.timedetector.ConfigurationInternal getCurrentUserConfigurationInternal();

    void removeConfigurationInternalChangeListener(com.android.server.timezonedetector.StateChangeListener stateChangeListener);

    boolean updateConfiguration(int i, android.app.time.TimeConfiguration timeConfiguration, boolean z);
}
