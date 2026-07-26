package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
final class DisplayRotationCompatPolicy implements com.android.server.wm.CameraStateMonitor.CameraCompatStateListener, com.android.server.wm.ActivityRefresher.Evaluator {
    private final com.android.server.wm.ActivityRefresher mActivityRefresher;
    private final com.android.server.wm.CameraStateMonitor mCameraStateMonitor;
    private final com.android.server.wm.DisplayContent mDisplayContent;
    private int mLastReportedOrientation = -2;
    private final com.android.server.wm.WindowManagerService mWmService;

    DisplayRotationCompatPolicy(com.android.server.wm.DisplayContent displayContent, com.android.server.wm.CameraStateMonitor cameraStateMonitor, com.android.server.wm.ActivityRefresher activityRefresher) {
        this.mDisplayContent = displayContent;
        this.mWmService = displayContent.mWmService;
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

    int getOrientation() {
        this.mLastReportedOrientation = getOrientationInternal();
        if (this.mLastReportedOrientation != -1) {
            rememberOverriddenOrientationIfNeeded();
        } else {
            restoreOverriddenOrientationIfNeeded();
        }
        return this.mLastReportedOrientation;
    }

    private synchronized int getOrientationInternal() {
        if (!isTreatmentEnabledForDisplay()) {
            return -1;
        }
        com.android.server.wm.ActivityRecord topActivity = this.mDisplayContent.topRunningActivity(true);
        if (!isTreatmentEnabledForActivity(topActivity)) {
            return -1;
        }
        int orientation = 0;
        boolean isPortraitActivity = topActivity.getRequestedConfigurationOrientation() == 1;
        boolean isNaturalDisplayOrientationPortrait = this.mDisplayContent.getNaturalOrientation() == 1;
        if ((isPortraitActivity && isNaturalDisplayOrientationPortrait) || (!isPortraitActivity && !isNaturalDisplayOrientationPortrait)) {
            orientation = 1;
        }
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ORIENTATION_enabled[1]) {
            long protoLogParam0 = this.mDisplayContent.mDisplayId;
            java.lang.String protoLogParam1 = java.lang.String.valueOf(android.content.pm.ActivityInfo.screenOrientationToString(orientation));
            boolean protoLogParam2 = isPortraitActivity;
            boolean protoLogParam3 = isNaturalDisplayOrientationPortrait;
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ORIENTATION, 7429138692709430028L, com.android.server.display.util.OplusDisplayPanelFeatureHelper.OMMDP_DMR_SET, null, java.lang.Long.valueOf(protoLogParam0), protoLogParam1, java.lang.Boolean.valueOf(protoLogParam2), java.lang.Boolean.valueOf(protoLogParam3));
        }
        return orientation;
    }

    void onScreenRotationAnimationFinished() {
        com.android.server.wm.ActivityRecord topActivity = this.mDisplayContent.topRunningActivity(true);
        if (!isTreatmentEnabledForDisplay() || !isTreatmentEnabledForActivity(topActivity)) {
            return;
        }
        showToast(android.R.string.duration_days_medium);
    }

    java.lang.String getSummaryForDisplayRotationHistoryRecord() {
        java.lang.String summaryIfEnabled = "";
        if (isTreatmentEnabledForDisplay()) {
            com.android.server.wm.ActivityRecord topActivity = this.mDisplayContent.topRunningActivity(true);
            summaryIfEnabled = " mLastReportedOrientation=" + android.content.pm.ActivityInfo.screenOrientationToString(this.mLastReportedOrientation) + " topActivity=" + (topActivity == null ? "null" : topActivity.shortComponentName) + " isTreatmentEnabledForActivity=" + isTreatmentEnabledForActivity(topActivity) + "mCameraStateMonitor=" + this.mCameraStateMonitor.getSummary();
        }
        return "DisplayRotationCompatPolicy{ isTreatmentEnabledForDisplay=" + isTreatmentEnabledForDisplay() + summaryIfEnabled + " }";
    }

    private void restoreOverriddenOrientationIfNeeded() {
        if (isOrientationOverridden() && this.mDisplayContent.getRotationReversionController().revertOverride(1)) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ORIENTATION_enabled[1]) {
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ORIENTATION, -5176775281239247368L, 0, null, null);
            }
            this.mDisplayContent.mLastOrientationSource = null;
        }
    }

    private boolean isOrientationOverridden() {
        return this.mDisplayContent.getRotationReversionController().isOverrideActive(1);
    }

    private void rememberOverriddenOrientationIfNeeded() {
        if (!isOrientationOverridden()) {
            this.mDisplayContent.getRotationReversionController().beforeOverrideApplied(1);
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ORIENTATION_enabled[1]) {
                long protoLogParam0 = this.mDisplayContent.getLastOrientation();
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ORIENTATION, -2188976047008497712L, 1, null, java.lang.Long.valueOf(protoLogParam0));
            }
        }
    }

    @Override // com.android.server.wm.ActivityRefresher.Evaluator
    public boolean shouldRefreshActivity(com.android.server.wm.ActivityRecord activity, android.content.res.Configuration newConfig, android.content.res.Configuration lastReportedConfig) {
        boolean displayRotationChanged = newConfig.windowConfiguration.getDisplayRotation() != lastReportedConfig.windowConfiguration.getDisplayRotation();
        return isTreatmentEnabledForDisplay() && isTreatmentEnabledForActivity(activity) && activity.mLetterboxUiController.shouldRefreshActivityForCameraCompat() && (displayRotationChanged || activity.mLetterboxUiController.isCameraCompatSplitScreenAspectRatioAllowed());
    }

    private boolean isTreatmentEnabledForDisplay() {
        return this.mWmService.mLetterboxConfiguration.isCameraCompatTreatmentEnabled() && this.mDisplayContent.getIgnoreOrientationRequest() && this.mDisplayContent.getDisplay().getType() == 1;
    }

    boolean isActivityEligibleForOrientationOverride(com.android.server.wm.ActivityRecord activity) {
        return isTreatmentEnabledForDisplay() && isCameraActive(activity, true) && activity.mLetterboxUiController.shouldForceRotateForCameraCompat();
    }

    boolean isTreatmentEnabledForActivity(com.android.server.wm.ActivityRecord activity) {
        return isTreatmentEnabledForActivity(activity, true);
    }

    boolean isCameraActive(com.android.server.wm.ActivityRecord activity, boolean mustBeFullscreen) {
        return !(mustBeFullscreen && activity.inMultiWindowMode()) && this.mCameraStateMonitor.isCameraRunningForActivity(activity);
    }

    private boolean isTreatmentEnabledForActivity(com.android.server.wm.ActivityRecord activity, boolean mustBeFullscreen) {
        return (activity == null || !isCameraActive(activity, mustBeFullscreen) || activity.getRequestedConfigurationOrientation() == 0 || activity.getOverrideOrientation() == 5 || activity.getOverrideOrientation() == 14 || !activity.mLetterboxUiController.shouldForceRotateForCameraCompat()) ? false : true;
    }

    @Override // com.android.server.wm.CameraStateMonitor.CameraCompatStateListener
    public boolean onCameraOpened(com.android.server.wm.ActivityRecord cameraActivity, java.lang.String cameraId) {
        if (cameraActivity.getWindowingMode() == 1) {
            cameraActivity.mLetterboxUiController.recomputeConfigurationForCameraCompatIfNeeded();
            this.mDisplayContent.updateOrientation();
            return true;
        }
        if (cameraActivity.getTask().getWindowingMode() == 6 && isTreatmentEnabledForActivity(cameraActivity, false)) {
            android.content.pm.PackageManager packageManager = this.mWmService.mContext.getPackageManager();
            try {
                showToast(android.R.string.duration_days_medium_future, (java.lang.String) packageManager.getApplicationLabel(packageManager.getApplicationInfo(cameraActivity.packageName, 0)));
                return true;
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ORIENTATION_enabled[4]) {
                    java.lang.String protoLogParam0 = java.lang.String.valueOf(cameraActivity.packageName);
                    com.android.internal.protolog.ProtoLogImpl_209941506.e(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ORIENTATION, -1534784331886673955L, 0, null, protoLogParam0);
                }
            }
        }
        return false;
    }

    void showToast(final int stringRes) {
        com.android.server.UiThread.getHandler().post(new java.lang.Runnable() { // from class: com.android.server.wm.DisplayRotationCompatPolicy$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$showToast$0(stringRes);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showToast$0(int stringRes) {
        android.widget.Toast.makeText(this.mWmService.mContext, stringRes, 1).show();
    }

    void showToast(final int stringRes, final java.lang.String applicationLabel) {
        com.android.server.UiThread.getHandler().post(new java.lang.Runnable() { // from class: com.android.server.wm.DisplayRotationCompatPolicy$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$showToast$1(stringRes, applicationLabel);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showToast$1(int stringRes, java.lang.String applicationLabel) {
        android.widget.Toast.makeText(this.mWmService.mContext, this.mWmService.mContext.getString(stringRes, applicationLabel), 1).show();
    }

    @Override // com.android.server.wm.CameraStateMonitor.CameraCompatStateListener
    public boolean onCameraClosed(com.android.server.wm.ActivityRecord cameraActivity, java.lang.String cameraId) {
        synchronized (this) {
            if (isActivityForCameraIdRefreshing(cameraId)) {
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ORIENTATION_enabled[1]) {
                    long protoLogParam0 = this.mDisplayContent.mDisplayId;
                    com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ORIENTATION, -5121743609317543819L, 1, null, java.lang.Long.valueOf(protoLogParam0));
                }
                return false;
            }
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ORIENTATION_enabled[1]) {
                long protoLogParam02 = this.mDisplayContent.mDisplayId;
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ORIENTATION, 1769752961776628557L, 1, null, java.lang.Long.valueOf(protoLogParam02));
            }
            com.android.server.wm.ActivityRecord topActivity = this.mDisplayContent.topRunningActivity(true);
            if (topActivity == null || topActivity.getWindowingMode() != 1) {
                return true;
            }
            topActivity.mLetterboxUiController.recomputeConfigurationForCameraCompatIfNeeded();
            this.mDisplayContent.updateOrientation();
            return true;
        }
    }

    private boolean isActivityForCameraIdRefreshing(java.lang.String cameraId) {
        com.android.server.wm.ActivityRecord topActivity = this.mDisplayContent.topRunningActivity(true);
        if (!isTreatmentEnabledForActivity(topActivity) || !this.mCameraStateMonitor.isCameraWithIdRunningForActivity(topActivity, cameraId)) {
            return false;
        }
        return this.mActivityRefresher.isActivityRefreshing(topActivity);
    }
}
