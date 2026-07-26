package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class CameraStateMonitor {
    private static final int CAMERA_CLOSED_LETTERBOX_UPDATE_DELAY_MS = 2000;
    private static final int CAMERA_OPENED_LETTERBOX_UPDATE_DELAY_MS = 1000;
    private static final java.lang.String TAG = "WindowManager";
    private com.android.server.wm.ActivityRecord mCameraActivity;
    private final android.hardware.camera2.CameraManager mCameraManager;
    private com.android.server.wm.CameraStateMonitor.CameraCompatStateListener mCurrentListenerForCameraActivity;
    private final com.android.server.wm.DisplayContent mDisplayContent;
    private final android.os.Handler mHandler;
    private final com.android.server.wm.WindowManagerService mWmService;
    private final com.android.server.wm.CameraIdPackageNameBiMapping mCameraIdPackageBiMapping = new com.android.server.wm.CameraIdPackageNameBiMapping();
    private final java.util.Set<java.lang.String> mScheduledToBeRemovedCameraIdSet = new android.util.ArraySet();
    private final java.util.Set<java.lang.String> mScheduledCompatModeUpdateCameraIdSet = new android.util.ArraySet();
    private final java.util.ArrayList<com.android.server.wm.CameraStateMonitor.CameraCompatStateListener> mCameraStateListeners = new java.util.ArrayList<>();
    private final android.hardware.camera2.CameraManager.AvailabilityCallback mAvailabilityCallback = new android.hardware.camera2.CameraManager.AvailabilityCallback() { // from class: com.android.server.wm.CameraStateMonitor.1
        public void onCameraOpened(java.lang.String cameraId, java.lang.String packageId) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.CameraStateMonitor.this.mWmService.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.CameraStateMonitor.this.notifyCameraOpened(cameraId, packageId);
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }

        public void onCameraClosed(java.lang.String cameraId) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.CameraStateMonitor.this.mWmService.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.CameraStateMonitor.this.notifyCameraClosed(cameraId);
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }
    };

    interface CameraCompatStateListener {
        boolean onCameraClosed(com.android.server.wm.ActivityRecord activityRecord, java.lang.String str);

        boolean onCameraOpened(com.android.server.wm.ActivityRecord activityRecord, java.lang.String str);
    }

    CameraStateMonitor(com.android.server.wm.DisplayContent displayContent, android.os.Handler handler) {
        this.mHandler = handler;
        this.mDisplayContent = displayContent;
        this.mWmService = displayContent.mWmService;
        this.mCameraManager = (android.hardware.camera2.CameraManager) this.mWmService.mContext.getSystemService(android.hardware.camera2.CameraManager.class);
    }

    void startListeningToCameraState() {
        this.mCameraManager.registerAvailabilityCallback(this.mWmService.mContext.getMainExecutor(), this.mAvailabilityCallback);
    }

    void dispose() {
        if (this.mCameraManager != null) {
            this.mCameraManager.unregisterAvailabilityCallback(this.mAvailabilityCallback);
        }
    }

    void addCameraStateListener(com.android.server.wm.CameraStateMonitor.CameraCompatStateListener listener) {
        this.mCameraStateListeners.add(listener);
    }

    void removeCameraStateListener(com.android.server.wm.CameraStateMonitor.CameraCompatStateListener listener) {
        this.mCameraStateListeners.remove(listener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyCameraOpened(final java.lang.String cameraId, final java.lang.String packageName) {
        this.mScheduledToBeRemovedCameraIdSet.remove(cameraId);
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_STATES_enabled[1]) {
            long protoLogParam0 = this.mDisplayContent.mDisplayId;
            java.lang.String protoLogParam1 = java.lang.String.valueOf(cameraId);
            java.lang.String protoLogParam2 = java.lang.String.valueOf(packageName);
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_STATES, 8116030277393789125L, 1, null, java.lang.Long.valueOf(protoLogParam0), protoLogParam1, protoLogParam2);
        }
        this.mScheduledCompatModeUpdateCameraIdSet.add(cameraId);
        this.mHandler.postDelayed(new java.lang.Runnable() { // from class: com.android.server.wm.CameraStateMonitor$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$notifyCameraOpened$0(cameraId, packageName);
            }
        }, 1000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$notifyCameraOpened$0(java.lang.String cameraId, java.lang.String packageName) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mWmService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                if (!this.mScheduledCompatModeUpdateCameraIdSet.remove(cameraId)) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return;
                }
                this.mCameraIdPackageBiMapping.put(packageName, cameraId);
                this.mCameraActivity = findCameraActivity(packageName);
                if (this.mCameraActivity != null && this.mCameraActivity.getTask() != null) {
                    notifyListenersCameraOpened(this.mCameraActivity, cameraId);
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return;
                }
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    private void notifyListenersCameraOpened(com.android.server.wm.ActivityRecord cameraActivity, java.lang.String cameraId) {
        for (int i = 0; i < this.mCameraStateListeners.size(); i++) {
            com.android.server.wm.CameraStateMonitor.CameraCompatStateListener listener = this.mCameraStateListeners.get(i);
            boolean activeCameraTreatment = listener.onCameraOpened(cameraActivity, cameraId);
            if (activeCameraTreatment) {
                this.mCurrentListenerForCameraActivity = listener;
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyCameraClosed(java.lang.String cameraId) {
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_STATES_enabled[1]) {
            long protoLogParam0 = this.mDisplayContent.mDisplayId;
            java.lang.String protoLogParam1 = java.lang.String.valueOf(cameraId);
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_STATES, -3774458166471278611L, 1, null, java.lang.Long.valueOf(protoLogParam0), protoLogParam1);
        }
        this.mScheduledToBeRemovedCameraIdSet.add(cameraId);
        this.mScheduledCompatModeUpdateCameraIdSet.remove(cameraId);
        scheduleRemoveCameraId(cameraId);
    }

    boolean isCameraRunningForActivity(com.android.server.wm.ActivityRecord activity) {
        return getCameraIdForActivity(activity) != null;
    }

    boolean isCameraWithIdRunningForActivity(com.android.server.wm.ActivityRecord activity, java.lang.String cameraId) {
        return cameraId.equals(getCameraIdForActivity(activity));
    }

    void rescheduleRemoveCameraActivity(java.lang.String cameraId) {
        this.mScheduledToBeRemovedCameraIdSet.add(cameraId);
        scheduleRemoveCameraId(cameraId);
    }

    private java.lang.String getCameraIdForActivity(com.android.server.wm.ActivityRecord activity) {
        return this.mCameraIdPackageBiMapping.getCameraId(activity.packageName);
    }

    private void scheduleRemoveCameraId(final java.lang.String cameraId) {
        this.mHandler.postDelayed(new java.lang.Runnable() { // from class: com.android.server.wm.CameraStateMonitor$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$scheduleRemoveCameraId$1(cameraId);
            }
        }, 2000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: removeCameraId, reason: merged with bridge method [inline-methods] */
    public void lambda$scheduleRemoveCameraId$1(java.lang.String cameraId) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mWmService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                if (!this.mScheduledToBeRemovedCameraIdSet.remove(cameraId)) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return;
                }
                if (this.mCameraActivity != null && this.mCurrentListenerForCameraActivity != null) {
                    boolean closeSuccessful = this.mCurrentListenerForCameraActivity.onCameraClosed(this.mCameraActivity, cameraId);
                    if (closeSuccessful) {
                        this.mCameraIdPackageBiMapping.removeCameraId(cameraId);
                        this.mCurrentListenerForCameraActivity = null;
                    } else {
                        rescheduleRemoveCameraActivity(cameraId);
                    }
                }
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    private com.android.server.wm.ActivityRecord findCameraActivity(final java.lang.String packageName) {
        com.android.server.wm.ActivityRecord topActivity = this.mDisplayContent.topRunningActivity(true);
        if (topActivity != null && topActivity.packageName.equals(packageName)) {
            return topActivity;
        }
        final java.util.List<com.android.server.wm.ActivityRecord> activitiesOfPackageWhichOpenedCamera = new java.util.ArrayList<>();
        this.mDisplayContent.forAllActivities(new java.util.function.Consumer() { // from class: com.android.server.wm.CameraStateMonitor$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.wm.CameraStateMonitor.lambda$findCameraActivity$2(packageName, activitiesOfPackageWhichOpenedCamera, (com.android.server.wm.ActivityRecord) obj);
            }
        });
        if (activitiesOfPackageWhichOpenedCamera.isEmpty()) {
            android.util.Slog.w(TAG, "Cannot find camera activity.");
            return null;
        }
        if (activitiesOfPackageWhichOpenedCamera.size() == 1) {
            return (com.android.server.wm.ActivityRecord) activitiesOfPackageWhichOpenedCamera.getFirst();
        }
        android.util.Slog.w(TAG, "Cannot determine which activity opened camera.");
        return null;
    }

    static /* synthetic */ void lambda$findCameraActivity$2(java.lang.String packageName, java.util.List activitiesOfPackageWhichOpenedCamera, com.android.server.wm.ActivityRecord activityRecord) {
        if (activityRecord.isVisibleRequested() && activityRecord.packageName.equals(packageName)) {
            activitiesOfPackageWhichOpenedCamera.add(activityRecord);
        }
    }

    java.lang.String getSummary() {
        return " CameraIdPackageNameBiMapping=" + this.mCameraIdPackageBiMapping.getSummaryForDisplayRotationHistoryRecord();
    }
}
