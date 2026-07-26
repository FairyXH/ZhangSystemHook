package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public class ContentRecorder implements com.android.server.wm.WindowContainerListener {
    private static final float MAX_ANISOTROPY = 0.025f;
    private static final java.lang.String TAG = "ContentRecorder";
    private com.android.server.wm.ContentRecorder.ContentRecorderWrapper mCRrapper;
    public com.android.server.wm.IContentRecorderExt mContentRecorderExt;
    private android.view.ContentRecordingSession mContentRecordingSession;
    private final boolean mCorrectForAnisotropicPixels;
    private final com.android.server.wm.DisplayContent mDisplayContent;
    private android.graphics.Point mLastConsumingSurfaceSize;
    private int mLastOrientation;
    private android.graphics.Rect mLastRecordedBounds;
    private int mLastRotation;
    private int mLastWindowingMode;
    private final com.android.server.wm.ContentRecorder.MediaProjectionManagerWrapper mMediaProjectionManager;
    private android.view.SurfaceControl mRecordedSurface;
    private com.android.server.wm.WindowContainer mRecordedWindowContainer;

    interface MediaProjectionManagerWrapper {
        void notifyActiveProjectionCapturedContentResized(int i, int i2);

        void notifyActiveProjectionCapturedContentVisibilityChanged(boolean z);

        void notifyWindowingModeChanged(int i, int i2, int i3);

        void stopActiveProjection();
    }

    ContentRecorder(com.android.server.wm.DisplayContent displayContent) {
        this(displayContent, new com.android.server.wm.ContentRecorder.RemoteMediaProjectionManagerWrapper(displayContent.mDisplayId), new com.android.server.display.feature.DisplayManagerFlags().isConnectedDisplayManagementEnabled() && !new com.android.server.display.feature.DisplayManagerFlags().isPixelAnisotropyCorrectionInLogicalDisplayEnabled() && displayContent.getDisplayInfo().type == 2);
    }

    ContentRecorder(com.android.server.wm.DisplayContent displayContent, com.android.server.wm.ContentRecorder.MediaProjectionManagerWrapper mediaProjectionManager, boolean correctForAnisotropicPixels) {
        this.mContentRecordingSession = null;
        this.mRecordedWindowContainer = null;
        this.mRecordedSurface = null;
        this.mLastRecordedBounds = null;
        this.mLastConsumingSurfaceSize = new android.graphics.Point(0, 0);
        this.mLastOrientation = 0;
        this.mLastWindowingMode = 0;
        this.mLastRotation = -1;
        this.mCRrapper = new com.android.server.wm.ContentRecorder.ContentRecorderWrapper();
        this.mDisplayContent = displayContent;
        this.mMediaProjectionManager = mediaProjectionManager;
        this.mCorrectForAnisotropicPixels = correctForAnisotropicPixels;
        this.mContentRecorderExt = (com.android.server.wm.IContentRecorderExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IContentRecorderExt.class).base(this).create();
    }

    void setContentRecordingSession(android.view.ContentRecordingSession session) {
        this.mContentRecordingSession = session;
    }

    boolean isContentRecordingSessionSet() {
        return this.mContentRecordingSession != null;
    }

    boolean isCurrentlyRecording() {
        return (this.mContentRecordingSession == null || this.mRecordedSurface == null) ? false : true;
    }

    void updateRecording() {
        if (isCurrentlyRecording() && (this.mDisplayContent.getLastHasContent() || this.mDisplayContent.getDisplayInfo().state == 1)) {
            pauseRecording();
        } else {
            startRecordingIfNeeded();
        }
    }

    void onMirrorOutputSurfaceOrientationChanged() {
        onConfigurationChanged(this.mLastOrientation, this.mLastWindowingMode);
    }

    void onConfigurationChanged(int lastOrientation, int lastWindowingMode) {
        int recordedContentOrientation;
        if (isCurrentlyRecording() && this.mLastRecordedBounds != null) {
            if (this.mRecordedWindowContainer == null) {
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_CONTENT_RECORDING_enabled[1]) {
                    long protoLogParam0 = this.mDisplayContent.getDisplayId();
                    com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_CONTENT_RECORDING, -6620483833570774987L, 1, "Content Recording: Unexpectedly null window container; unable to update recording for display %d", java.lang.Long.valueOf(protoLogParam0));
                    return;
                }
                return;
            }
            if (this.mContentRecordingSession.getContentToRecord() == 1) {
                com.android.server.wm.Task capturedTask = this.mRecordedWindowContainer.asTask();
                if (capturedTask.inPinnedWindowingMode()) {
                    if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_CONTENT_RECORDING_enabled[1]) {
                        long protoLogParam02 = this.mDisplayContent.getDisplayId();
                        com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_CONTENT_RECORDING, 7226080178642957768L, 1, "Content Recording: Display %d was already recording, but pause capture since the task is in PIP", java.lang.Long.valueOf(protoLogParam02));
                    }
                    pauseRecording();
                    return;
                }
            }
            int recordedContentWindowingMode = this.mRecordedWindowContainer.getWindowingMode();
            if (lastWindowingMode != recordedContentWindowingMode) {
                this.mMediaProjectionManager.notifyWindowingModeChanged(this.mContentRecordingSession.getContentToRecord(), this.mContentRecordingSession.getTargetUid(), recordedContentWindowingMode);
            }
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_CONTENT_RECORDING_enabled[1]) {
                long protoLogParam03 = this.mDisplayContent.getDisplayId();
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_CONTENT_RECORDING, -311001578548807570L, 1, "Content Recording: Display %d was already recording, so apply transformations if necessary", java.lang.Long.valueOf(protoLogParam03));
            }
            android.graphics.Rect recordedContentBounds = this.mRecordedWindowContainer.getBounds();
            int recordedContentRotation = this.mRecordedWindowContainer.getWindowConfiguration().getRotation();
            if (this.mContentRecorderExt.ifNeedRotateSurfaceForOplus(this.mDisplayContent)) {
                recordedContentOrientation = this.mRecordedWindowContainer.getOrientation();
            } else {
                recordedContentOrientation = this.mRecordedWindowContainer.getConfiguration().orientation;
            }
            android.graphics.Point surfaceSize = fetchSurfaceSizeIfPresent();
            if (this.mLastRecordedBounds.equals(recordedContentBounds) && lastOrientation == recordedContentOrientation && this.mLastConsumingSurfaceSize.equals(surfaceSize) && this.mLastRotation == recordedContentRotation) {
                return;
            }
            this.mLastRotation = recordedContentRotation;
            if (surfaceSize != null) {
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_CONTENT_RECORDING_enabled[1]) {
                    long protoLogParam04 = this.mDisplayContent.getDisplayId();
                    java.lang.String protoLogParam1 = java.lang.String.valueOf(recordedContentBounds);
                    long protoLogParam2 = recordedContentOrientation;
                    java.lang.String protoLogParam3 = java.lang.String.valueOf(surfaceSize);
                    com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_CONTENT_RECORDING, 2350883351096538149L, 17, "Content Recording: Going ahead with updating recording for display %d to new bounds %s and/or orientation %d and/or surface size %s", java.lang.Long.valueOf(protoLogParam04), protoLogParam1, java.lang.Long.valueOf(protoLogParam2), protoLogParam3);
                }
                updateMirroredSurface(this.mRecordedWindowContainer.getSyncTransaction(), recordedContentBounds, surfaceSize);
                return;
            }
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_CONTENT_RECORDING_enabled[1]) {
                long protoLogParam05 = this.mDisplayContent.getDisplayId();
                java.lang.String protoLogParam12 = java.lang.String.valueOf(recordedContentBounds);
                long protoLogParam22 = recordedContentOrientation;
                java.lang.String protoLogParam32 = java.lang.String.valueOf(surfaceSize);
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_CONTENT_RECORDING, 8446758574558556540L, 17, "Content Recording: Unable to update recording for display %d to new bounds %s and/or orientation %d and/or surface size %s, since the surface is not available.", java.lang.Long.valueOf(protoLogParam05), protoLogParam12, java.lang.Long.valueOf(protoLogParam22), protoLogParam32);
            }
        }
    }

    void pauseRecording() {
        if (this.mRecordedSurface == null) {
            return;
        }
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_CONTENT_RECORDING_enabled[1]) {
            long protoLogParam0 = this.mDisplayContent.getDisplayId();
            boolean protoLogParam1 = this.mDisplayContent.getLastHasContent();
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_CONTENT_RECORDING, -4320004054011530388L, 13, "Content Recording: Display %d has content (%b) so pause recording", java.lang.Long.valueOf(protoLogParam0), java.lang.Boolean.valueOf(protoLogParam1));
        }
        this.mDisplayContent.mWmService.mTransactionFactory.get().remove(this.mRecordedSurface).reparent(this.mDisplayContent.getWindowingLayer(), this.mDisplayContent.getSurfaceControl()).reparent(this.mDisplayContent.getOverlayLayer(), this.mDisplayContent.getSurfaceControl()).apply();
        this.mRecordedSurface = null;
        this.mContentRecorderExt.pauseRecording(this.mDisplayContent);
    }

    void stopRecording() {
        unregisterListener();
        if (this.mRecordedSurface != null) {
            this.mDisplayContent.mWmService.mTransactionFactory.get().remove(this.mRecordedSurface).apply();
            this.mRecordedSurface = null;
            clearContentRecordingSession();
        }
    }

    private void stopMediaProjection() {
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_CONTENT_RECORDING_enabled[1]) {
            long protoLogParam0 = this.mDisplayContent.getDisplayId();
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_CONTENT_RECORDING, 5951434375221687741L, 1, "Content Recording: Stop MediaProjection on virtual display %d", java.lang.Long.valueOf(protoLogParam0));
        }
        if (this.mMediaProjectionManager != null) {
            this.mMediaProjectionManager.stopActiveProjection();
        }
    }

    private void clearContentRecordingSession() {
        this.mContentRecordingSession = null;
        this.mDisplayContent.mWmService.mContentRecordingController.setContentRecordingSessionLocked(null, this.mDisplayContent.mWmService);
    }

    private void unregisterListener() {
        com.android.server.wm.Task recordedTask = this.mRecordedWindowContainer != null ? this.mRecordedWindowContainer.asTask() : null;
        if (recordedTask == null || !isRecordingContentTask()) {
            return;
        }
        recordedTask.unregisterWindowContainerListener(this);
        this.mRecordedWindowContainer = null;
    }

    private void startRecordingIfNeeded() {
        if (!this.mContentRecorderExt.shouldInterceptStartRecording(this.mDisplayContent) && !this.mDisplayContent.getLastHasContent() && !isCurrentlyRecording()) {
            if (this.mDisplayContent.getDisplayInfo().state == 1 || this.mContentRecordingSession == null) {
                return;
            }
            if (this.mContentRecordingSession.isWaitingForConsent()) {
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_CONTENT_RECORDING_enabled[1]) {
                    com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_CONTENT_RECORDING, -3395581813971405090L, 0, "Content Recording: waiting to record, so do nothing", null);
                    return;
                }
                return;
            }
            this.mRecordedWindowContainer = retrieveRecordedWindowContainer();
            if (this.mRecordedWindowContainer == null) {
                return;
            }
            android.view.SurfaceControl sourceSurface = this.mRecordedWindowContainer.getSurfaceControl();
            if (sourceSurface == null || !sourceSurface.isValid()) {
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_CONTENT_RECORDING_enabled[1]) {
                    long protoLogParam0 = this.mDisplayContent.getDisplayId();
                    com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_CONTENT_RECORDING, -7587023235027618399L, 1, "Content Recording: Unable to start recording for display %d since the surface is null or have been released.", java.lang.Long.valueOf(protoLogParam0));
                    return;
                }
                return;
            }
            int contentToRecord = this.mContentRecordingSession.getContentToRecord();
            if (contentToRecord == 1 && this.mRecordedWindowContainer.asTask().inPinnedWindowingMode()) {
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_CONTENT_RECORDING_enabled[1]) {
                    long protoLogParam02 = this.mDisplayContent.getDisplayId();
                    com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_CONTENT_RECORDING, 6779858226066635065L, 1, "Content Recording: Display %d should start recording, but don't yet since the task is in PIP", java.lang.Long.valueOf(protoLogParam02));
                    return;
                }
                return;
            }
            android.graphics.Point surfaceSize = fetchSurfaceSizeIfPresent();
            if (surfaceSize == null) {
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_CONTENT_RECORDING_enabled[1]) {
                    long protoLogParam03 = this.mDisplayContent.getDisplayId();
                    com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_CONTENT_RECORDING, 7051210836345306671L, 1, "Content Recording: Unable to start recording for display %d since the surface is not available.", java.lang.Long.valueOf(protoLogParam03));
                    return;
                }
                return;
            }
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_CONTENT_RECORDING_enabled[1]) {
                long protoLogParam04 = this.mDisplayContent.getDisplayId();
                long protoLogParam1 = this.mDisplayContent.getDisplayInfo().state;
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_CONTENT_RECORDING, 2255758299558330282L, 5, "Content Recording: Display %d has no content and is on, so start recording for state %d", java.lang.Long.valueOf(protoLogParam04), java.lang.Long.valueOf(protoLogParam1));
            }
            this.mContentRecorderExt.startRecording(this.mDisplayContent);
            this.mRecordedSurface = android.view.SurfaceControl.mirrorSurface(sourceSurface);
            android.view.SurfaceControl.Transaction transaction = this.mDisplayContent.mWmService.mTransactionFactory.get().reparent(this.mRecordedSurface, this.mDisplayContent.getSurfaceControl()).reparent(this.mDisplayContent.getWindowingLayer(), null).reparent(this.mDisplayContent.getOverlayLayer(), null);
            updateMirroredSurface(transaction, this.mRecordedWindowContainer.getBounds(), surfaceSize);
            transaction.apply();
            if (contentToRecord == 1) {
                this.mMediaProjectionManager.notifyActiveProjectionCapturedContentVisibilityChanged(this.mRecordedWindowContainer.asTask().isVisibleRequested());
            } else {
                int currentDisplayState = this.mRecordedWindowContainer.asDisplayContent().getDisplayInfo().state;
                this.mMediaProjectionManager.notifyActiveProjectionCapturedContentVisibilityChanged(currentDisplayState != 1);
            }
            this.mMediaProjectionManager.notifyWindowingModeChanged(contentToRecord, this.mContentRecordingSession.getTargetUid(), this.mRecordedWindowContainer.getWindowConfiguration().getWindowingMode());
        }
    }

    private com.android.server.wm.WindowContainer retrieveRecordedWindowContainer() {
        int contentToRecord = this.mContentRecordingSession.getContentToRecord();
        android.os.IBinder tokenToRecord = this.mContentRecordingSession.getTokenToRecord();
        switch (contentToRecord) {
            case 0:
                com.android.server.wm.DisplayContent dc = this.mDisplayContent.mWmService.mRoot.getDisplayContent(this.mContentRecordingSession.getDisplayToRecord());
                if (dc == null) {
                    this.mDisplayContent.mWmService.mDisplayManagerInternal.setWindowManagerMirroring(this.mDisplayContent.getDisplayId(), false);
                    handleStartRecordingFailed();
                    if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_CONTENT_RECORDING_enabled[1]) {
                        long protoLogParam0 = this.mDisplayContent.getDisplayId();
                        com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_CONTENT_RECORDING, 2269158922723670768L, 1, "Unable to retrieve window container to start recording for display %d", java.lang.Long.valueOf(protoLogParam0));
                    }
                    return null;
                }
                return dc;
            case 1:
                if (tokenToRecord == null || com.android.server.wm.WindowContainer.fromBinder(tokenToRecord) == null) {
                    handleStartRecordingFailed();
                    if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_CONTENT_RECORDING_enabled[1]) {
                        long protoLogParam02 = this.mDisplayContent.getDisplayId();
                        com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_CONTENT_RECORDING, -2177493963028285555L, 1, "Content Recording: Unable to start recording due to null token for display %d", java.lang.Long.valueOf(protoLogParam02));
                    }
                    return null;
                }
                com.android.server.wm.Task taskToRecord = com.android.server.wm.WindowContainer.fromBinder(tokenToRecord).asTask();
                if (taskToRecord == null) {
                    handleStartRecordingFailed();
                    if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_CONTENT_RECORDING_enabled[1]) {
                        long protoLogParam03 = this.mDisplayContent.getDisplayId();
                        com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_CONTENT_RECORDING, -928577038848872043L, 1, "Content Recording: Unable to retrieve task to start recording for display %d", java.lang.Long.valueOf(protoLogParam03));
                    }
                } else {
                    taskToRecord.registerWindowContainerListener(this);
                }
                return taskToRecord;
            default:
                handleStartRecordingFailed();
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_CONTENT_RECORDING_enabled[1]) {
                    long protoLogParam04 = this.mDisplayContent.getDisplayId();
                    com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_CONTENT_RECORDING, -3564317873468917405L, 1, "Content Recording: Unable to start recording due to invalid region for display %d", java.lang.Long.valueOf(protoLogParam04));
                }
                return null;
        }
    }

    private void handleStartRecordingFailed() {
        boolean shouldExitTaskRecording = isRecordingContentTask();
        unregisterListener();
        clearContentRecordingSession();
        if (shouldExitTaskRecording) {
            stopMediaProjection();
        }
    }

    private void computeScaling(int inputSizeX, int inputSizeY, float inputDpiX, float inputDpiY, int outputSizeX, int outputSizeY, float outputDpiX, float outputDpiY, android.graphics.PointF scaleOut) {
        float scaleX;
        float scaleY;
        int curRotation = this.mRecordedWindowContainer.getWindowConfiguration().getRotation();
        float relAnisotropy = (inputDpiY / inputDpiX) / (outputDpiY / outputDpiX);
        if (!this.mCorrectForAnisotropicPixels || (relAnisotropy > 0.975f && relAnisotropy < 1.025f)) {
            if ((curRotation == 1 || curRotation == 3) && this.mContentRecorderExt.ifNeedRotateSurfaceForOplus(this.mDisplayContent)) {
                scaleX = outputSizeY / outputSizeX;
                float scaleX2 = outputSizeX;
                scaleY = scaleX2 / inputSizeY;
            } else {
                float scaleX3 = outputSizeX;
                scaleX = scaleX3 / inputSizeX;
                float scaleX4 = outputSizeY;
                scaleY = scaleX4 / inputSizeY;
            }
            float scale = java.lang.Math.min(scaleX, scaleY);
            scaleOut.x = scale;
            scaleOut.y = scale;
            return;
        }
        float relDpiX = outputDpiX / inputDpiX;
        float relDpiY = outputDpiY / inputDpiY;
        float scale2 = java.lang.Math.min((outputSizeX / relDpiX) / inputSizeX, (outputSizeY / relDpiY) / inputSizeY);
        scaleOut.x = scale2 * relDpiX;
        scaleOut.y = scale2 * relDpiY;
    }

    void updateMirroredSurface(android.view.SurfaceControl.Transaction transaction, android.graphics.Rect recordedContentBounds, android.graphics.Point surfaceSize) {
        int shiftedX;
        int shiftedY;
        int shiftedY2;
        int shiftedX2;
        int curRotation;
        android.graphics.PointF scale;
        int curRotation2 = this.mRecordedWindowContainer.getWindowConfiguration().getRotation();
        android.view.DisplayInfo inputDisplayInfo = this.mRecordedWindowContainer.mDisplayContent.getDisplayInfo();
        android.view.DisplayInfo outputDisplayInfo = this.mDisplayContent.getDisplayInfo();
        android.graphics.PointF scale2 = new android.graphics.PointF();
        computeScaling(recordedContentBounds.width(), recordedContentBounds.height(), inputDisplayInfo.physicalXDpi, inputDisplayInfo.physicalYDpi, surfaceSize.x, surfaceSize.y, outputDisplayInfo.physicalXDpi, outputDisplayInfo.physicalYDpi, scale2);
        int scaledWidth = java.lang.Math.round(scale2.x * recordedContentBounds.width());
        int scaledHeight = java.lang.Math.round(scale2.y * recordedContentBounds.height());
        if (scaledWidth == surfaceSize.x) {
            shiftedX = 0;
        } else {
            int shiftedX3 = (surfaceSize.x - scaledWidth) / 2;
            shiftedX = shiftedX3;
        }
        if (scaledHeight == surfaceSize.y) {
            shiftedY = 0;
        } else {
            int shiftedY3 = (surfaceSize.y - scaledHeight) / 2;
            shiftedY = shiftedY3;
        }
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_CONTENT_RECORDING_enabled[1]) {
            long protoLogParam0 = shiftedX;
            long protoLogParam1 = shiftedY;
            double protoLogParam2 = scale2.x;
            double protoLogParam3 = scale2.y;
            long protoLogParam4 = recordedContentBounds.width();
            shiftedY2 = shiftedY;
            int shiftedY4 = recordedContentBounds.height();
            curRotation = curRotation2;
            long protoLogParam5 = shiftedY4;
            scale = scale2;
            long protoLogParam6 = this.mDisplayContent.getDisplayId();
            long protoLogParam62 = this.mDisplayContent.getConfiguration().screenWidthDp;
            shiftedX2 = shiftedX;
            long protoLogParam8 = this.mDisplayContent.getConfiguration().screenHeightDp;
            long protoLogParam82 = surfaceSize.x;
            long protoLogParam9 = surfaceSize.y;
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_CONTENT_RECORDING, 1100676037289065396L, 1398181, "Content Recording: Apply transformations of shift %d x %d, scale %f x %f, crop (aka recorded content size) %d x %d for display %d; display has size %d x %d; surface has size %d x %d", java.lang.Long.valueOf(protoLogParam0), java.lang.Long.valueOf(protoLogParam1), java.lang.Double.valueOf(protoLogParam2), java.lang.Double.valueOf(protoLogParam3), java.lang.Long.valueOf(protoLogParam4), java.lang.Long.valueOf(protoLogParam5), java.lang.Long.valueOf(protoLogParam6), java.lang.Long.valueOf(protoLogParam62), java.lang.Long.valueOf(protoLogParam8), java.lang.Long.valueOf(protoLogParam82), java.lang.Long.valueOf(protoLogParam9));
        } else {
            shiftedY2 = shiftedY;
            shiftedX2 = shiftedX;
            curRotation = curRotation2;
            scale = scale2;
        }
        android.graphics.PointF scale3 = scale;
        float realScale = java.lang.Math.min(scale3.x, scale3.y);
        int curRotation3 = curRotation;
        int shiftedX4 = shiftedX2;
        int shiftedY5 = shiftedY2;
        android.util.Slog.d(TAG, "Update mirror layer surface.  curRotation=: " + curRotation3 + "\n realScale=: " + realScale + " shiftedX=: " + shiftedX4 + " shiftedY=: " + shiftedY5 + "\n surfaceSize.x=: " + surfaceSize.x + "\n surfaceSize.y=: " + surfaceSize.y + "\n recordedContentwidth=: " + recordedContentBounds.width() + "\n recordedContenthight=: " + recordedContentBounds.height());
        if (this.mContentRecorderExt.ifNeedRotateSurfaceForOplus(this.mDisplayContent)) {
            this.mContentRecorderExt.rotateSurface(this.mRecordedSurface, transaction, realScale, recordedContentBounds, surfaceSize, curRotation3);
        } else {
            transaction.setWindowCrop(this.mRecordedSurface, recordedContentBounds.width(), recordedContentBounds.height()).setMatrix(this.mRecordedSurface, scale3.x, 0.0f, 0.0f, scale3.y).setPosition(this.mRecordedSurface, shiftedX4, shiftedY5);
        }
        this.mLastRecordedBounds = new android.graphics.Rect(recordedContentBounds);
        this.mLastConsumingSurfaceSize.x = surfaceSize.x;
        this.mLastConsumingSurfaceSize.y = surfaceSize.y;
        this.mMediaProjectionManager.notifyActiveProjectionCapturedContentResized(this.mLastRecordedBounds.width(), this.mLastRecordedBounds.height());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.graphics.Point fetchSurfaceSizeIfPresent() {
        android.graphics.Point surfaceSize = this.mDisplayContent.mWmService.mDisplayManagerInternal.getDisplaySurfaceDefaultSize(this.mDisplayContent.getDisplayId());
        if (surfaceSize == null) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_CONTENT_RECORDING_enabled[1]) {
                long protoLogParam0 = this.mDisplayContent.getDisplayId();
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_CONTENT_RECORDING, 2330946591287751995L, 1, "Content Recording: Provided surface for recording on display %d is not present, so do not update the surface", java.lang.Long.valueOf(protoLogParam0));
                return null;
            }
            return null;
        }
        return surfaceSize;
    }

    @Override // com.android.server.wm.WindowContainerListener
    public void onRemoved() {
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_CONTENT_RECORDING_enabled[1]) {
            long protoLogParam0 = this.mDisplayContent.getDisplayId();
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_CONTENT_RECORDING, 7993045936648632984L, 1, "Content Recording: Recorded task is removed, so stop recording on display %d", java.lang.Long.valueOf(protoLogParam0));
        }
        unregisterListener();
        clearContentRecordingSession();
        stopMediaProjection();
    }

    @Override // com.android.server.wm.ConfigurationContainerListener
    public void onMergedOverrideConfigurationChanged(android.content.res.Configuration mergedOverrideConfiguration) {
        super.onMergedOverrideConfigurationChanged(mergedOverrideConfiguration);
        onConfigurationChanged(this.mLastOrientation, this.mLastWindowingMode);
        this.mLastOrientation = mergedOverrideConfiguration.orientation;
        this.mLastWindowingMode = mergedOverrideConfiguration.windowConfiguration.getWindowingMode();
    }

    @Override // com.android.server.wm.WindowContainerListener
    public void onVisibleRequestedChanged(boolean isVisibleRequested) {
        if (isCurrentlyRecording() && this.mLastRecordedBounds != null) {
            this.mMediaProjectionManager.notifyActiveProjectionCapturedContentVisibilityChanged(isVisibleRequested);
            if (this.mContentRecordingSession.getContentToRecord() == 1) {
                this.mRecordedWindowContainer.getSyncTransaction().setVisibility(this.mRecordedSurface, isVisibleRequested);
                this.mRecordedWindowContainer.scheduleAnimation();
            }
        }
    }

    private static final class RemoteMediaProjectionManagerWrapper implements com.android.server.wm.ContentRecorder.MediaProjectionManagerWrapper {
        private final int mDisplayId;
        private android.media.projection.IMediaProjectionManager mIMediaProjectionManager = null;

        RemoteMediaProjectionManagerWrapper(int displayId) {
            this.mDisplayId = displayId;
        }

        @Override // com.android.server.wm.ContentRecorder.MediaProjectionManagerWrapper
        public void stopActiveProjection() {
            fetchMediaProjectionManager();
            if (this.mIMediaProjectionManager == null) {
                return;
            }
            try {
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_CONTENT_RECORDING_enabled[4]) {
                    long protoLogParam0 = this.mDisplayId;
                    com.android.internal.protolog.ProtoLogImpl_209941506.e(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_CONTENT_RECORDING, 3197882223327917085L, 1, "Content Recording: stopping active projection for display %d", java.lang.Long.valueOf(protoLogParam0));
                }
                this.mIMediaProjectionManager.stopActiveProjection();
            } catch (android.os.RemoteException e) {
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_CONTENT_RECORDING_enabled[4]) {
                    long protoLogParam02 = this.mDisplayId;
                    java.lang.String protoLogParam1 = java.lang.String.valueOf(e);
                    com.android.internal.protolog.ProtoLogImpl_209941506.e(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_CONTENT_RECORDING, 4391984931064789228L, 1, "Content Recording: Unable to tell MediaProjectionManagerService to stop the active projection for display %d: %s", java.lang.Long.valueOf(protoLogParam02), protoLogParam1);
                }
            }
        }

        @Override // com.android.server.wm.ContentRecorder.MediaProjectionManagerWrapper
        public void notifyActiveProjectionCapturedContentResized(int width, int height) {
            fetchMediaProjectionManager();
            if (this.mIMediaProjectionManager == null) {
                return;
            }
            try {
                this.mIMediaProjectionManager.notifyActiveProjectionCapturedContentResized(width, height);
            } catch (android.os.RemoteException e) {
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_CONTENT_RECORDING_enabled[4]) {
                    java.lang.String protoLogParam0 = java.lang.String.valueOf(e);
                    com.android.internal.protolog.ProtoLogImpl_209941506.e(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_CONTENT_RECORDING, 6721270269112237694L, 0, "Content Recording: Unable to tell MediaProjectionManagerService about resizing the active projection: %s", protoLogParam0);
                }
            }
        }

        @Override // com.android.server.wm.ContentRecorder.MediaProjectionManagerWrapper
        public void notifyActiveProjectionCapturedContentVisibilityChanged(boolean isVisible) {
            fetchMediaProjectionManager();
            if (this.mIMediaProjectionManager == null) {
                return;
            }
            try {
                this.mIMediaProjectionManager.notifyActiveProjectionCapturedContentVisibilityChanged(isVisible);
            } catch (android.os.RemoteException e) {
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_CONTENT_RECORDING_enabled[4]) {
                    java.lang.String protoLogParam0 = java.lang.String.valueOf(e);
                    com.android.internal.protolog.ProtoLogImpl_209941506.e(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_CONTENT_RECORDING, 1600318776990120244L, 0, "Content Recording: Unable to tell MediaProjectionManagerService about visibility change on the active projection: %s", protoLogParam0);
                }
            }
        }

        @Override // com.android.server.wm.ContentRecorder.MediaProjectionManagerWrapper
        public void notifyWindowingModeChanged(int contentToRecord, int targetUid, int windowingMode) {
            fetchMediaProjectionManager();
            if (this.mIMediaProjectionManager == null) {
                return;
            }
            try {
                this.mIMediaProjectionManager.notifyWindowingModeChanged(contentToRecord, targetUid, windowingMode);
            } catch (android.os.RemoteException e) {
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_CONTENT_RECORDING_enabled[4]) {
                    java.lang.String protoLogParam0 = java.lang.String.valueOf(e);
                    com.android.internal.protolog.ProtoLogImpl_209941506.e(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_CONTENT_RECORDING, -1451477179301743956L, 0, "Content Recording: Unable to tell log windowing mode change: %s", protoLogParam0);
                }
            }
        }

        private void fetchMediaProjectionManager() {
            android.os.IBinder b;
            if (this.mIMediaProjectionManager != null || (b = android.os.ServiceManager.getService("media_projection")) == null) {
                return;
            }
            this.mIMediaProjectionManager = android.media.projection.IMediaProjectionManager.Stub.asInterface(b);
        }
    }

    private boolean isRecordingContentTask() {
        return this.mContentRecordingSession != null && this.mContentRecordingSession.getContentToRecord() == 1;
    }

    public com.android.server.wm.IContentRecorderWrapper getWrapper() {
        return this.mCRrapper;
    }

    private class ContentRecorderWrapper implements com.android.server.wm.IContentRecorderWrapper {
        private ContentRecorderWrapper() {
        }

        @Override // com.android.server.wm.IContentRecorderWrapper
        public android.graphics.Rect getRectBounds() {
            return com.android.server.wm.ContentRecorder.this.mLastRecordedBounds;
        }

        @Override // com.android.server.wm.IContentRecorderWrapper
        public com.android.server.wm.WindowContainer getRecordedWindowContainer() {
            return com.android.server.wm.ContentRecorder.this.mRecordedWindowContainer;
        }

        @Override // com.android.server.wm.IContentRecorderWrapper
        public com.android.server.wm.DisplayContent getDisplayContent() {
            return com.android.server.wm.ContentRecorder.this.mDisplayContent;
        }

        @Override // com.android.server.wm.IContentRecorderWrapper
        public android.graphics.Point fetchSurfaceSizeIfPresent() {
            return com.android.server.wm.ContentRecorder.this.fetchSurfaceSizeIfPresent();
        }
    }
}
