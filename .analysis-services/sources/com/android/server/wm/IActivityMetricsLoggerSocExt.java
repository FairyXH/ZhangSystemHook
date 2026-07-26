package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IActivityMetricsLoggerSocExt {
    default void hookLogAppTransitionFinished(com.android.server.wm.ActivityRecord activityRecord) {
    }

    default void hookLogAppDisplayed(com.android.server.wm.WindowProcessController processRecord, java.lang.String packageName, int windowsDrawnDelayMs, java.lang.String launchedActivityShortComponentName) {
    }

    default void onNotifyAppTTId(java.lang.String packageName, int TTidtime, int launchState) {
    }
}
