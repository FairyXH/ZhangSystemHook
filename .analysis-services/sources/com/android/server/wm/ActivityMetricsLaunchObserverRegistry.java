package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface ActivityMetricsLaunchObserverRegistry {
    void registerLaunchObserver(com.android.server.wm.ActivityMetricsLaunchObserver activityMetricsLaunchObserver);

    void unregisterLaunchObserver(com.android.server.wm.ActivityMetricsLaunchObserver activityMetricsLaunchObserver);
}
