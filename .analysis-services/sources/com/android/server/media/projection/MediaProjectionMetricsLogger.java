package com.android.server.media.projection;

/* JADX INFO: loaded from: classes2.dex */
public class MediaProjectionMetricsLogger {
    private static final java.lang.String TAG = "MediaProjectionMetricsLogger";
    private static final int TARGET_UID_UNKNOWN = -2;
    private static final int TIME_SINCE_LAST_ACTIVE_UNKNOWN = -1;
    private static com.android.server.media.projection.MediaProjectionMetricsLogger sSingleton = null;
    private final com.android.server.media.projection.FrameworkStatsLogWrapper mFrameworkStatsLogWrapper;
    private int mPreviousState = 0;
    private final com.android.server.media.projection.MediaProjectionSessionIdGenerator mSessionIdGenerator;
    private final com.android.server.media.projection.MediaProjectionTimestampStore mTimestampStore;

    MediaProjectionMetricsLogger(com.android.server.media.projection.FrameworkStatsLogWrapper frameworkStatsLogWrapper, com.android.server.media.projection.MediaProjectionSessionIdGenerator sessionIdGenerator, com.android.server.media.projection.MediaProjectionTimestampStore timestampStore) {
        this.mFrameworkStatsLogWrapper = frameworkStatsLogWrapper;
        this.mSessionIdGenerator = sessionIdGenerator;
        this.mTimestampStore = timestampStore;
    }

    public static com.android.server.media.projection.MediaProjectionMetricsLogger getInstance(android.content.Context context) {
        if (sSingleton == null) {
            sSingleton = new com.android.server.media.projection.MediaProjectionMetricsLogger(new com.android.server.media.projection.FrameworkStatsLogWrapper(), com.android.server.media.projection.MediaProjectionSessionIdGenerator.getInstance(context), com.android.server.media.projection.MediaProjectionTimestampStore.getInstance(context));
        }
        return sSingleton;
    }

    public void logInitiated(int hostUid, int sessionCreationSource) {
        int timeSinceLastActiveInSeconds;
        android.util.Log.d(TAG, "logInitiated");
        java.time.Duration durationSinceLastActiveSession = this.mTimestampStore.timeSinceLastActiveSession();
        if (durationSinceLastActiveSession == null) {
            timeSinceLastActiveInSeconds = -1;
        } else {
            timeSinceLastActiveInSeconds = (int) durationSinceLastActiveSession.toSeconds();
        }
        writeStateChanged(this.mSessionIdGenerator.createAndGetNewSessionId(), 1, hostUid, -2, timeSinceLastActiveInSeconds, sessionCreationSource);
    }

    public void logPermissionRequestDisplayed(int hostUid) {
        android.util.Log.d(TAG, "logPermissionRequestDisplayed");
        writeStateChanged(this.mSessionIdGenerator.getCurrentSessionId(), 2, hostUid, -2, -1, 0);
    }

    public void logProjectionPermissionRequestCancelled(int hostUid) {
        writeStateChanged(this.mSessionIdGenerator.getCurrentSessionId(), 8, hostUid, -2, -1, 0);
    }

    public void logAppSelectorDisplayed(int hostUid) {
        android.util.Log.d(TAG, "logAppSelectorDisplayed");
        writeStateChanged(this.mSessionIdGenerator.getCurrentSessionId(), 3, hostUid, -2, -1, 0);
    }

    public void logInProgress(int hostUid, int targetUid) {
        android.util.Log.d(TAG, "logInProgress");
        writeStateChanged(this.mSessionIdGenerator.getCurrentSessionId(), 4, hostUid, targetUid, -1, 0);
    }

    public void logChangedWindowingMode(int contentToRecord, int hostUid, int targetUid, int windowingMode) {
        android.util.Log.d(TAG, "logChangedWindowingMode");
        writeTargetChanged(this.mSessionIdGenerator.getCurrentSessionId(), contentToRecordToTargetType(contentToRecord), hostUid, targetUid, windowingModeToTargetWindowingMode(windowingMode));
    }

    public int contentToRecordToTargetType(int recordContentType) {
        switch (recordContentType) {
            case 0:
                return 1;
            case 1:
                return 2;
            default:
                return 0;
        }
    }

    public int windowingModeToTargetWindowingMode(int windowingMode) {
        switch (windowingMode) {
            case 1:
                return 2;
            case 5:
                return 4;
            case 6:
                return 3;
            default:
                return 0;
        }
    }

    public void logStopped(int hostUid, int targetUid) {
        boolean wasCaptureInProgress = this.mPreviousState == 4;
        android.util.Log.d(TAG, "logStopped: wasCaptureInProgress=" + wasCaptureInProgress);
        writeStateChanged(this.mSessionIdGenerator.getCurrentSessionId(), 7, hostUid, targetUid, -1, 0);
        if (wasCaptureInProgress) {
            this.mTimestampStore.registerActiveSessionEnded();
        }
    }

    public void notifyProjectionStateChange(int hostUid, int state, int sessionCreationSource) {
        writeStateChanged(hostUid, state, sessionCreationSource);
    }

    private void writeStateChanged(int hostUid, int state, int sessionCreationSource) {
        this.mFrameworkStatsLogWrapper.writeStateChanged(com.android.internal.util.FrameworkStatsLog.MEDIA_PROJECTION_STATE_CHANGED, 123, state, 0, hostUid, -1, 0, sessionCreationSource);
    }

    private void writeStateChanged(int sessionId, int state, int hostUid, int targetUid, int timeSinceLastActive, int creationSource) {
        this.mFrameworkStatsLogWrapper.writeStateChanged(com.android.internal.util.FrameworkStatsLog.MEDIA_PROJECTION_STATE_CHANGED, sessionId, state, this.mPreviousState, hostUid, targetUid, timeSinceLastActive, creationSource);
        this.mPreviousState = state;
    }

    private void writeTargetChanged(int sessionId, int targetType, int hostUid, int targetUid, int targetWindowingMode) {
        this.mFrameworkStatsLogWrapper.writeTargetChanged(com.android.internal.util.FrameworkStatsLog.MEDIA_PROJECTION_TARGET_CHANGED, sessionId, targetType, hostUid, targetUid, targetWindowingMode);
    }
}
