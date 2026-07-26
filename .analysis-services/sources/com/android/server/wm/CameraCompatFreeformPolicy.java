package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
final class CameraCompatFreeformPolicy implements com.android.server.wm.CameraStateMonitor.CameraCompatStateListener, com.android.server.wm.ActivityRefresher.Evaluator {
    private static final java.lang.String TAG = "WindowManager";
    private final com.android.server.wm.ActivityRefresher mActivityRefresher;
    private final com.android.server.wm.CameraStateMonitor mCameraStateMonitor;
    private final com.android.server.wm.DisplayContent mDisplayContent;
    private boolean mIsCameraCompatTreatmentPending = false;

    CameraCompatFreeformPolicy(com.android.server.wm.DisplayContent displayContent, com.android.server.wm.CameraStateMonitor cameraStateMonitor, com.android.server.wm.ActivityRefresher activityRefresher) {
        this.mDisplayContent = displayContent;
        this.mCameraStateMonitor = cameraStateMonitor;
        this.mActivityRefresher = activityRefresher;
    }

    void start() {
        this.mCameraStateMonitor.addCameraStateListener(this);
        this.mActivityRefresher.addEvaluator(this);
    }

    void dispose() {
        this.mCameraStateMonitor.removeCameraStateListener(this);
        this.mActivityRefresher.removeEvaluator(this);
    }

    @Override // com.android.server.wm.ActivityRefresher.Evaluator
    public boolean shouldRefreshActivity(com.android.server.wm.ActivityRecord activity, android.content.res.Configuration newConfig, android.content.res.Configuration lastReportedConfig) {
        return isTreatmentEnabledForActivity(activity) && this.mIsCameraCompatTreatmentPending;
    }

    boolean shouldApplyFreeformTreatmentForCameraCompat(com.android.server.wm.ActivityRecord activity) {
        return com.android.window.flags.Flags.cameraCompatForFreeform() && !activity.info.isChangeEnabled(314961188L);
    }

    @Override // com.android.server.wm.CameraStateMonitor.CameraCompatStateListener
    public boolean onCameraOpened(com.android.server.wm.ActivityRecord cameraActivity, java.lang.String cameraId) {
        if (!isTreatmentEnabledForActivity(cameraActivity)) {
            return false;
        }
        int existingCameraCompatMode = cameraActivity.mLetterboxUiController.getFreeformCameraCompatMode();
        int newCameraCompatMode = getCameraCompatMode(cameraActivity);
        if (newCameraCompatMode != existingCameraCompatMode) {
            this.mIsCameraCompatTreatmentPending = true;
            cameraActivity.mLetterboxUiController.setFreeformCameraCompatMode(newCameraCompatMode);
            forceUpdateActivityAndTask(cameraActivity);
            return true;
        }
        this.mIsCameraCompatTreatmentPending = false;
        return false;
    }

    @Override // com.android.server.wm.CameraStateMonitor.CameraCompatStateListener
    public boolean onCameraClosed(com.android.server.wm.ActivityRecord cameraActivity, java.lang.String cameraId) {
        if (isActivityForCameraIdRefreshing(cameraId)) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_STATES_enabled[1]) {
                long protoLogParam0 = this.mDisplayContent.mDisplayId;
                java.lang.String protoLogParam1 = java.lang.String.valueOf(cameraId);
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_STATES, -2283066544361882071L, 1, null, java.lang.Long.valueOf(protoLogParam0), protoLogParam1);
            }
            return false;
        }
        cameraActivity.mLetterboxUiController.setFreeformCameraCompatMode(0);
        forceUpdateActivityAndTask(cameraActivity);
        this.mIsCameraCompatTreatmentPending = false;
        return true;
    }

    private void forceUpdateActivityAndTask(com.android.server.wm.ActivityRecord cameraActivity) {
        cameraActivity.recomputeConfiguration();
        cameraActivity.updateReportedConfigurationAndSend();
        com.android.server.wm.Task cameraTask = cameraActivity.getTask();
        if (cameraTask != null) {
            cameraTask.dispatchTaskInfoChangedIfNeeded(true);
        }
    }

    private static int getCameraCompatMode(com.android.server.wm.ActivityRecord topActivity) {
        switch (topActivity.getRequestedConfigurationOrientation()) {
            case 1:
                return 1;
            case 2:
                return 2;
            default:
                return 0;
        }
    }

    private boolean isTreatmentEnabledForActivity(com.android.server.wm.ActivityRecord activity) {
        int orientation = activity.getRequestedConfigurationOrientation();
        return (!shouldApplyFreeformTreatmentForCameraCompat(activity) || !this.mCameraStateMonitor.isCameraRunningForActivity(activity) || orientation == 0 || !activity.inFreeformWindowingMode() || activity.getRequestedOrientation() == 5 || activity.getRequestedOrientation() == 14 || activity.isEmbedded()) ? false : true;
    }

    private boolean isActivityForCameraIdRefreshing(java.lang.String cameraId) {
        com.android.server.wm.ActivityRecord topActivity = this.mDisplayContent.topRunningActivity(true);
        if (topActivity == null || !isTreatmentEnabledForActivity(topActivity) || this.mCameraStateMonitor.isCameraWithIdRunningForActivity(topActivity, cameraId)) {
            return false;
        }
        return topActivity.mLetterboxUiController.isRefreshRequested();
    }
}
