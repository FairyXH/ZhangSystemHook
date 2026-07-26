package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public class ActivityMetricsLoggerSocExtImpl implements com.android.server.wm.IActivityMetricsLoggerSocExt {
    private static com.android.server.wm.ActivityRecord mLaunchedActivity;
    com.android.server.wm.ActivityMetricsLogger mActivityMetricsLogger;
    public static android.util.BoostFramework mUxPerf = new android.util.BoostFramework();
    public static android.util.BoostFramework mPerfBoost = new android.util.BoostFramework();

    public ActivityMetricsLoggerSocExtImpl(java.lang.Object obj) {
        this.mActivityMetricsLogger = (com.android.server.wm.ActivityMetricsLogger) obj;
    }

    @Override // com.android.server.wm.IActivityMetricsLoggerSocExt
    public void hookLogAppTransitionFinished(com.android.server.wm.ActivityRecord activityRecord) {
        mLaunchedActivity = activityRecord;
    }

    @Override // com.android.server.wm.IActivityMetricsLoggerSocExt
    public void hookLogAppDisplayed(com.android.server.wm.WindowProcessController processRecord, java.lang.String packageName, int windowsDrawnDelayMs, java.lang.String launchedActivityShortComponentName) {
        int isGame;
        if (mPerfBoost != null && processRecord != null) {
            mPerfBoost.perfHint(4162, packageName, processRecord.getPid(), windowsDrawnDelayMs);
        }
        if (mUxPerf != null) {
            if (mUxPerf.board_first_api_lvl < 33 && mUxPerf.board_api_lvl < 33) {
                mUxPerf.perfUXEngine_events(3, 0, packageName, windowsDrawnDelayMs);
            }
            if (android.app.ActivityManager.isLowRamDeviceStatic()) {
                isGame = mLaunchedActivity.isAppInfoGame();
            } else {
                isGame = mUxPerf.perfGetFeedback(5633, mLaunchedActivity.packageName) == 2 ? 1 : 0;
            }
            if (mLaunchedActivity.processName != null && !mLaunchedActivity.processName.equals(packageName)) {
                isGame = 1;
            }
            if (mUxPerf.board_first_api_lvl < 33 && mUxPerf.board_api_lvl < 33) {
                mUxPerf.perfUXEngine_events(5, 0, packageName, isGame);
            }
        }
        mLaunchedActivity.mActivityRecordSocExt.perfLockReleaseHandler();
    }
}
