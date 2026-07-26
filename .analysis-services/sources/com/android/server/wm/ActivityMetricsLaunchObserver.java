package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public class ActivityMetricsLaunchObserver {
    public static final int TEMPERATURE_COLD = 1;
    public static final int TEMPERATURE_HOT = 3;
    public static final int TEMPERATURE_WARM = 2;

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @interface Temperature {
    }

    public void onIntentStarted(android.content.Intent intent, long timestampNanos) {
    }

    public void onIntentFailed(long id) {
    }

    public void onActivityLaunched(long id, android.content.ComponentName name, int temperature, int userId) {
    }

    public void onActivityLaunchCancelled(long id) {
    }

    public void onActivityLaunchFinished(long id, android.content.ComponentName name, long timestampNanos, int launchMode) {
    }

    public void onReportFullyDrawn(long id, long timestampNanos) {
    }
}
