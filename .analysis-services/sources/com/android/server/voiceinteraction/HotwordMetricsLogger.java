package com.android.server.voiceinteraction;

/* JADX INFO: loaded from: classes3.dex */
public final class HotwordMetricsLogger {
    private static final int AUDIO_EGRESS_DSP_DETECTOR = 1;
    private static final int AUDIO_EGRESS_NORMAL_DETECTOR = 0;
    private static final int AUDIO_EGRESS_SOFTWARE_DETECTOR = 2;
    private static final int METRICS_INIT_DETECTOR_DSP = 1;
    private static final int METRICS_INIT_DETECTOR_SOFTWARE = 2;
    private static final int METRICS_INIT_NORMAL_DETECTOR = 0;

    private HotwordMetricsLogger() {
    }

    public static void writeDetectorCreateEvent(int detectorType, boolean isCreated, int uid) {
        int metricsDetectorType = getCreateMetricsDetectorType(detectorType);
        com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.HOTWORD_DETECTOR_CREATE_REQUESTED, metricsDetectorType, isCreated, uid);
    }

    public static void writeServiceInitResultEvent(int detectorType, int result, int uid) {
        int metricsDetectorType = getInitMetricsDetectorType(detectorType);
        com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.HOTWORD_DETECTION_SERVICE_INIT_RESULT_REPORTED, metricsDetectorType, result, uid);
    }

    public static void writeServiceRestartEvent(int detectorType, int reason, int uid) {
        int metricsDetectorType = getRestartMetricsDetectorType(detectorType);
        com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.HOTWORD_DETECTION_SERVICE_RESTARTED, metricsDetectorType, reason, uid);
    }

    public static void writeKeyphraseTriggerEvent(int detectorType, int result, int uid) {
        int metricsDetectorType = getKeyphraseMetricsDetectorType(detectorType);
        com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.HOTWORD_DETECTOR_KEYPHRASE_TRIGGERED, metricsDetectorType, result, uid);
    }

    public static void writeDetectorEvent(int detectorType, int event, int uid) {
        int metricsDetectorType = getDetectorMetricsDetectorType(detectorType);
        com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.HOTWORD_DETECTOR_EVENTS, metricsDetectorType, event, uid);
    }

    public static void writeAudioEgressEvent(int detectorType, int event, int uid, int streamSizeBytes, int bundleSizeBytes, int streamCount) {
        int metricsDetectorType = getAudioEgressDetectorType(detectorType);
        com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.HOTWORD_AUDIO_EGRESS_EVENT_REPORTED, metricsDetectorType, event, uid, streamSizeBytes, bundleSizeBytes, streamCount);
    }

    public static void writeHotwordDataEgressSize(int eventType, long eventSize, int detectorType, int uid) {
        int metricsDetectorType = getHotwordEventEgressSizeDetectorType(detectorType);
        com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.HOTWORD_EGRESS_SIZE_ATOM_REPORTED, eventType, eventSize, metricsDetectorType, uid);
    }

    public static void startHotwordTriggerToUiLatencySession(android.content.Context context, java.lang.String tag) {
        com.android.internal.util.LatencyTracker.getInstance(context).onActionStart(19, tag);
    }

    public static void stopHotwordTriggerToUiLatencySession(android.content.Context context) {
        com.android.internal.util.LatencyTracker.getInstance(context).onActionEnd(19);
    }

    public static void cancelHotwordTriggerToUiLatencySession(android.content.Context context) {
        com.android.internal.util.LatencyTracker.getInstance(context).onActionCancel(19);
    }

    private static int getCreateMetricsDetectorType(int detectorType) {
        switch (detectorType) {
            case 1:
                return 1;
            case 2:
                return 2;
            default:
                return 0;
        }
    }

    private static int getRestartMetricsDetectorType(int detectorType) {
        switch (detectorType) {
            case 1:
                return 1;
            case 2:
                return 2;
            default:
                return 0;
        }
    }

    private static int getInitMetricsDetectorType(int detectorType) {
        switch (detectorType) {
            case 1:
                return 1;
            case 2:
                return 2;
            default:
                return 0;
        }
    }

    private static int getKeyphraseMetricsDetectorType(int detectorType) {
        switch (detectorType) {
            case 1:
                return 1;
            case 2:
                return 2;
            default:
                return 0;
        }
    }

    private static int getDetectorMetricsDetectorType(int detectorType) {
        switch (detectorType) {
            case 1:
                return 1;
            case 2:
                return 2;
            default:
                return 0;
        }
    }

    private static int getAudioEgressDetectorType(int detectorType) {
        switch (detectorType) {
            case 1:
                return 1;
            case 2:
                return 2;
            default:
                return 0;
        }
    }

    private static int getHotwordEventEgressSizeDetectorType(int detectorType) {
        switch (detectorType) {
            case 1:
                return 1;
            case 2:
                return 2;
            default:
                return 0;
        }
    }
}
