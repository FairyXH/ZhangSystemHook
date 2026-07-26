package com.android.server.vibrator;

/* JADX INFO: loaded from: classes3.dex */
public class VibratorFrameworkStatsLogger {
    private static final java.lang.String TAG = "VibratorFrameworkStatsLogger";
    private static final int VIBRATION_REPORTED_MAX_QUEUE_SIZE = 300;
    private static final int VIBRATION_REPORTED_MIN_INTERVAL_MILLIS = 10;
    private static final int VIBRATION_REPORTED_WARNING_QUEUE_SIZE = 200;
    private final java.lang.Runnable mConsumeVibrationStatsQueueRunnable;
    private final android.os.Handler mHandler;
    private long mLastVibrationReportedLogUptime;
    private final java.lang.Object mLock;
    private final long mVibrationReportedLogIntervalMillis;
    private final long mVibrationReportedQueueMaxSize;
    private java.util.Queue<com.android.server.vibrator.VibrationStats.StatsInfo> mVibrationStatsQueue;
    private static final com.android.modules.expresslog.Histogram sVibrationParamRequestLatencyHistogram = new com.android.modules.expresslog.Histogram("vibrator.value_vibration_param_request_latency", new com.android.modules.expresslog.Histogram.UniformOptions(20, 0.0f, 100.0f));
    private static final com.android.modules.expresslog.Histogram sVibrationParamScaleHistogram = new com.android.modules.expresslog.Histogram("vibrator.value_vibration_param_scale", new com.android.modules.expresslog.Histogram.UniformOptions(20, 0.0f, 2.0f));
    private static final com.android.modules.expresslog.Histogram sAdaptiveHapticScaleHistogram = new com.android.modules.expresslog.Histogram("vibrator.value_vibration_adaptive_haptic_scale", new com.android.modules.expresslog.Histogram.UniformOptions(20, 0.0f, 2.0f));

    VibratorFrameworkStatsLogger(android.os.Handler handler) {
        this(handler, 10, 300);
    }

    VibratorFrameworkStatsLogger(android.os.Handler handler, int vibrationReportedLogIntervalMillis, int vibrationReportedQueueMaxSize) {
        this.mLock = new java.lang.Object();
        this.mConsumeVibrationStatsQueueRunnable = new java.lang.Runnable() { // from class: com.android.server.vibrator.VibratorFrameworkStatsLogger$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$new$0();
            }
        };
        this.mVibrationStatsQueue = new java.util.ArrayDeque();
        this.mHandler = handler;
        this.mVibrationReportedLogIntervalMillis = vibrationReportedLogIntervalMillis;
        this.mVibrationReportedQueueMaxSize = vibrationReportedQueueMaxSize;
    }

    public void writeVibratorStateOnAsync(final int uid, final long duration) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.vibrator.VibratorFrameworkStatsLogger$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                com.android.internal.util.FrameworkStatsLog.write_non_chained(84, uid, (java.lang.String) null, 1, duration);
            }
        });
    }

    public void writeVibratorStateOffAsync(final int uid) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.vibrator.VibratorFrameworkStatsLogger$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                com.android.internal.util.FrameworkStatsLog.write_non_chained(84, uid, (java.lang.String) null, 0, 0);
            }
        });
    }

    public void writeVibrationReportedAsync(com.android.server.vibrator.VibrationStats.StatsInfo metrics) {
        int queueSize;
        boolean needsScheduling;
        long scheduleDelayMs;
        synchronized (this.mLock) {
            queueSize = this.mVibrationStatsQueue.size();
            needsScheduling = queueSize == 0;
            if (queueSize < this.mVibrationReportedQueueMaxSize) {
                this.mVibrationStatsQueue.offer(metrics);
            }
            long nextLogUptime = this.mLastVibrationReportedLogUptime + this.mVibrationReportedLogIntervalMillis;
            scheduleDelayMs = java.lang.Math.max(0L, nextLogUptime - android.os.SystemClock.uptimeMillis());
        }
        if (queueSize + 1 == 200) {
            android.util.Slog.w(TAG, " Approaching vibration metrics queue limit, events might be dropped.");
        }
        if (needsScheduling) {
            this.mHandler.postDelayed(this.mConsumeVibrationStatsQueueRunnable, scheduleDelayMs);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: writeVibrationReportedFromQueue, reason: merged with bridge method [inline-methods] */
    public void lambda$new$0() {
        com.android.server.vibrator.VibrationStats.StatsInfo stats;
        boolean needsScheduling;
        synchronized (this.mLock) {
            stats = this.mVibrationStatsQueue.poll();
            needsScheduling = !this.mVibrationStatsQueue.isEmpty();
            if (stats != null) {
                this.mLastVibrationReportedLogUptime = android.os.SystemClock.uptimeMillis();
            }
        }
        if (stats == null) {
            android.util.Slog.w(TAG, "Unexpected vibration metric flush with empty queue. Ignoring.");
        } else {
            stats.writeVibrationReported();
        }
        if (needsScheduling) {
            this.mHandler.postDelayed(this.mConsumeVibrationStatsQueueRunnable, this.mVibrationReportedLogIntervalMillis);
        }
    }

    public void logVibrationAdaptiveHapticScale(int uid, float scale) {
        if (java.lang.Float.compare(scale, 1.0f) != 0) {
            sAdaptiveHapticScaleHistogram.logSampleWithUid(uid, scale);
        }
    }

    public void logVibrationParamScale(float scale) {
        sVibrationParamScaleHistogram.logSample(scale);
    }

    public void logVibrationParamRequestLatency(int uid, long latencyMs) {
        sVibrationParamRequestLatencyHistogram.logSampleWithUid(uid, latencyMs);
    }

    public void logVibrationParamRequestTimeout(int uid) {
        com.android.modules.expresslog.Counter.logIncrementWithUid("vibrator.value_vibration_param_request_timeout", uid);
    }

    public void logVibrationParamResponseIgnored() {
        com.android.modules.expresslog.Counter.logIncrement("vibrator.value_vibration_param_response_ignored");
    }

    public static void logPerformHapticsFeedbackIfKeyboard(int uid, int hapticsFeedbackEffect) {
        boolean isKeyboard;
        switch (hapticsFeedbackEffect) {
            case 3:
            case 7:
                isKeyboard = true;
                break;
            default:
                isKeyboard = false;
                break;
        }
        if (isKeyboard) {
            com.android.modules.expresslog.Counter.logIncrementWithUid("vibrator.value_perform_haptic_feedback_keyboard", uid);
        }
    }
}
