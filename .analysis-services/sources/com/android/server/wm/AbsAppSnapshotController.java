package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
abstract class AbsAppSnapshotController<TYPE extends com.android.server.wm.WindowContainer, CACHE extends com.android.server.wm.SnapshotCache<TYPE>> {
    private static final boolean CROP_TASKSNAPSHOT_ENABLE_VALUE = android.os.SystemProperties.getBoolean("persist.sys.crop_task_snapshot_enable", false);
    static final int SNAPSHOT_MODE_APP_THEME = 1;
    static final int SNAPSHOT_MODE_NONE = 2;
    static final int SNAPSHOT_MODE_REAL = 0;
    static final java.lang.String TAG = "WindowManager";
    protected CACHE mCache;
    protected com.android.server.wm.Transition.ChangeInfo mCurrentChangeInfo;
    protected final boolean mIsRunningOnIoT;
    protected final boolean mIsRunningOnTv;
    protected final com.android.server.wm.WindowManagerService mService;
    private boolean mSnapshotEnabled;
    public com.android.server.wm.IAbsAppSnapshotControllerExt mAbsAppSnapConExt = (com.android.server.wm.IAbsAppSnapshotControllerExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IAbsAppSnapshotControllerExt.class).base(this).create();
    protected final float mHighResSnapshotScale = initSnapshotScale();

    protected abstract com.android.server.wm.ActivityRecord findAppTokenForSnapshot(TYPE type);

    protected abstract android.graphics.Rect getLetterboxInsets(com.android.server.wm.ActivityRecord activityRecord);

    abstract android.app.ActivityManager.TaskDescription getTaskDescription(TYPE type);

    abstract com.android.server.wm.ActivityRecord getTopActivity(TYPE type);

    abstract com.android.server.wm.ActivityRecord getTopFullscreenActivity(TYPE type);

    protected abstract boolean use16BitFormat();

    AbsAppSnapshotController(com.android.server.wm.WindowManagerService service) {
        this.mService = service;
        this.mIsRunningOnTv = this.mService.mContext.getPackageManager().hasSystemFeature("android.software.leanback");
        this.mIsRunningOnIoT = this.mService.mContext.getPackageManager().hasSystemFeature("android.hardware.type.embedded");
    }

    protected float initSnapshotScale() {
        float config = this.mService.mContext.getResources().getFloat(android.R.dimen.config_alertDialogSelectionScrollOffset);
        return java.lang.Math.max(java.lang.Math.min(config, 1.0f), 0.1f);
    }

    protected void initialize(CACHE cache) {
        this.mCache = cache;
    }

    void setSnapshotEnabled(boolean enabled) {
        this.mSnapshotEnabled = enabled;
    }

    public boolean getSnapshotEnabled() {
        return this.mSnapshotEnabled;
    }

    boolean shouldDisableSnapshots() {
        return this.mIsRunningOnTv || this.mIsRunningOnIoT || !this.mSnapshotEnabled;
    }

    android.window.TaskSnapshot captureSnapshot(TYPE source) {
        int mSnapshotMode = getSnapshotMode(source);
        android.util.Slog.w(TAG, "captureTaskSnapshot mSnapshotMode= " + mSnapshotMode + " source=" + source);
        switch (mSnapshotMode) {
            case 0:
                android.window.TaskSnapshot snapshot = snapshot(source);
                return snapshot;
            case 1:
                android.window.TaskSnapshot snapshot2 = drawAppThemeSnapshot(source);
                return snapshot2;
            case 2:
                return null;
            default:
                return null;
        }
    }

    final android.window.TaskSnapshot recordSnapshotInner(TYPE source) {
        android.window.TaskSnapshot snapshot;
        if (shouldDisableSnapshots() || (snapshot = captureSnapshot(source)) == null) {
            return null;
        }
        this.mCache.putSnapshot(source, snapshot);
        return snapshot;
    }

    int getSnapshotMode(TYPE source) {
        com.android.server.wm.ActivityRecord topChild;
        int type = source.getActivityType();
        if (type == 3 || type == 5 || this.mAbsAppSnapConExt.isActivityTypeMultiSearch(type)) {
            return 2;
        }
        if (type == 2 || (topChild = getTopActivity(source)) == null || !topChild.shouldUseAppThemeSnapshot()) {
            return 0;
        }
        return 1;
    }

    android.window.TaskSnapshot snapshot(TYPE source) {
        return snapshot(source, this.mHighResSnapshotScale);
    }

    android.window.TaskSnapshot snapshot(TYPE source, float scale) {
        android.window.TaskSnapshot.Builder builder = new android.window.TaskSnapshot.Builder();
        android.graphics.Rect crop = prepareTaskSnapshot(source, builder);
        if (crop == null) {
            return null;
        }
        android.os.Trace.traceBegin(32L, "createSnapshot");
        android.window.ScreenCapture.ScreenshotHardwareBuffer screenshotBuffer = createSnapshot(source, scale, crop, builder);
        android.os.Trace.traceEnd(32L);
        if (screenshotBuffer == null) {
            android.util.Slog.w(TAG, "snapshotTask screenshotBuffer is null");
            return null;
        }
        if (source instanceof com.android.server.wm.Task) {
            this.mAbsAppSnapConExt.snapshotTask((com.android.server.wm.Task) source, screenshotBuffer);
        }
        builder.setCaptureTime(android.os.SystemClock.elapsedRealtimeNanos());
        builder.setSnapshot(screenshotBuffer.getHardwareBuffer());
        builder.setColorSpace(screenshotBuffer.getColorSpace());
        android.window.TaskSnapshot snapshot = builder.build();
        return validateSnapshot(snapshot);
    }

    private static android.window.TaskSnapshot validateSnapshot(android.window.TaskSnapshot snapshot) {
        android.hardware.HardwareBuffer buffer = snapshot.getHardwareBuffer();
        if (buffer.getWidth() == 0 || buffer.getHeight() == 0) {
            buffer.close();
            android.util.Slog.e(TAG, "Invalid snapshot dimensions " + buffer.getWidth() + "x" + buffer.getHeight());
            return null;
        }
        return snapshot;
    }

    android.window.ScreenCapture.ScreenshotHardwareBuffer createSnapshot(TYPE source, float scaleFraction, android.graphics.Rect crop, android.window.TaskSnapshot.Builder builder) {
        android.view.SurfaceControl[] excludeLayers;
        android.window.ScreenCapture.ScreenshotHardwareBuffer screenshotBuffer;
        if (source.getSurfaceControl() == null) {
            android.util.Slog.w(TAG, "Failed to take screenshot. No surface control for " + source);
            return null;
        }
        com.android.server.wm.WindowState imeWindow = source.getDisplayContent().mInputMethodWindow;
        boolean z = true;
        boolean excludeIme = (imeWindow == null || imeWindow.getSurfaceControl() == null || source.getDisplayContent().shouldImeAttachedToApp()) ? false : true;
        com.android.server.wm.WindowState navWindow = source.getDisplayContent().getDisplayPolicy().getNavigationBar();
        boolean excludeNavBar = (navWindow == null || navWindow.getSurfaceControl() == null) ? false : true;
        if (excludeIme && excludeNavBar) {
            excludeLayers = new android.view.SurfaceControl[]{imeWindow.getSurfaceControl(), navWindow.getSurfaceControl()};
        } else if (excludeIme || excludeNavBar) {
            excludeLayers = new android.view.SurfaceControl[1];
            excludeLayers[0] = excludeIme ? imeWindow.getSurfaceControl() : navWindow.getSurfaceControl();
        } else {
            excludeLayers = new android.view.SurfaceControl[0];
        }
        if (excludeIme || imeWindow == null || (!imeWindow.isVisible() && !imeWindow.isOnScreen())) {
            z = false;
        }
        builder.setHasImeSurface(z);
        if (source instanceof com.android.server.wm.Task) {
            screenshotBuffer = this.mAbsAppSnapConExt.createTaskSnapshot((com.android.server.wm.Task) source, findAppTokenForSnapshot(source), crop, scaleFraction, builder.getPixelFormat(), excludeLayers);
        } else {
            screenshotBuffer = android.window.ScreenCapture.captureLayersExcluding(source.getSurfaceControl(), crop, scaleFraction, builder.getPixelFormat(), excludeLayers);
        }
        android.hardware.HardwareBuffer buffer = screenshotBuffer == null ? null : screenshotBuffer.getHardwareBuffer();
        if (isInvalidHardwareBuffer(buffer)) {
            android.util.Slog.d(TAG, "createTaskSnapshot isInvalidHardwareBuffer for " + source);
            return null;
        }
        return screenshotBuffer;
    }

    static boolean isInvalidHardwareBuffer(android.hardware.HardwareBuffer buffer) {
        return buffer == null || buffer.isClosed() || buffer.getWidth() <= 1 || buffer.getHeight() <= 1;
    }

    android.graphics.Rect prepareTaskSnapshot(TYPE source, android.window.TaskSnapshot.Builder builder) {
        android.graphics.Rect outCrop;
        android.graphics.Point taskSize;
        int i;
        android.util.Pair<com.android.server.wm.ActivityRecord, com.android.server.wm.WindowState> result = checkIfReadyToSnapshot(source);
        if (result == null) {
            return null;
        }
        com.android.server.wm.ActivityRecord activity = (com.android.server.wm.ActivityRecord) result.first;
        com.android.server.wm.WindowState mainWindow = (com.android.server.wm.WindowState) result.second;
        android.graphics.Rect contentInsets = getSystemBarInsets(mainWindow.getFrame(), mainWindow.getInsetsStateWithVisibilityOverride());
        android.graphics.Rect letterboxInsets = getLetterboxInsets(activity);
        com.android.server.wm.utils.InsetUtils.addInsets(contentInsets, letterboxInsets);
        this.mAbsAppSnapConExt.prepareTaskSnapshot(contentInsets, activity, mainWindow);
        builder.setIsRealSnapshot(true);
        builder.setId(java.lang.System.currentTimeMillis());
        builder.setContentInsets(contentInsets);
        builder.setLetterboxInsets(letterboxInsets);
        boolean isWindowTranslucent = mainWindow.getAttrs().format != -1;
        boolean isShowWallpaper = mainWindow.hasWallpaper();
        int pixelFormat = builder.getPixelFormat();
        if (pixelFormat == 0) {
            if (CROP_TASKSNAPSHOT_ENABLE_VALUE) {
                pixelFormat = 4;
            } else {
                if (use16BitFormat() && activity.fillsParent() && (!isWindowTranslucent || !isShowWallpaper)) {
                    i = 4;
                } else {
                    i = 1;
                }
                pixelFormat = i;
            }
        }
        boolean isTranslucent = android.graphics.PixelFormat.formatHasAlpha(pixelFormat) && (!activity.fillsParent() || isWindowTranslucent);
        com.android.server.wm.Task task = null;
        if (source.asTask() != null) {
            task = source.asTask();
        } else if (source.asActivityRecord() != null) {
            task = source.asActivityRecord().getTask();
        }
        if (task != null && task.getTaskInfo() != null && task.getTaskInfo().appCompatTaskInfo != null) {
            builder.setOplusCompatMode(task.getTaskInfo().appCompatTaskInfo.topActivityInOplusCompatMode);
        }
        builder.setTopActivityBounds(activity.getConfiguration().windowConfiguration.getAppBounds());
        builder.setTopActivityComponent(activity.mActivityComponent);
        builder.setPixelFormat(pixelFormat);
        builder.setIsTranslucent(isTranslucent);
        com.android.server.wm.Task activityRootTask = activity.getRootTask();
        if (activityRootTask != null && activityRootTask.getTaskDisplayArea() != null && activityRootTask.getTaskDisplayArea().mTaskDisplayAreaExt.isFlexibleTask(activityRootTask)) {
            builder.setWindowingMode(100);
        } else {
            builder.setWindowingMode(source.getWindowingMode());
        }
        builder.setAppearance(getAppearance(source));
        builder.setUiMode(activity.getConfiguration().uiMode);
        android.content.res.Configuration taskConfig = activity.getTask().getConfiguration();
        int displayRotation = taskConfig.windowConfiguration.getDisplayRotation();
        android.graphics.Rect outCrop2 = new android.graphics.Rect();
        android.graphics.Point taskSize2 = new android.graphics.Point();
        com.android.server.wm.Transition.ChangeInfo changeInfo = this.mCurrentChangeInfo;
        if (changeInfo != null && changeInfo.mRotation != displayRotation) {
            outCrop = outCrop2;
            outCrop.set(changeInfo.mAbsoluteBounds);
            taskSize = taskSize2;
            taskSize.set(changeInfo.mAbsoluteBounds.right, changeInfo.mAbsoluteBounds.bottom);
            builder.setRotation(changeInfo.mRotation);
            builder.setOrientation(changeInfo.mAbsoluteBounds.height() >= changeInfo.mAbsoluteBounds.width() ? 1 : 2);
        } else {
            outCrop = outCrop2;
            taskSize = taskSize2;
            android.content.res.Configuration srcConfig = source.getConfiguration();
            outCrop.set(srcConfig.windowConfiguration.getBounds());
            android.graphics.Rect taskBounds = taskConfig.windowConfiguration.getBounds();
            taskSize.set(taskBounds.width(), taskBounds.height());
            builder.setRotation(displayRotation);
            builder.setOrientation(srcConfig.orientation);
        }
        outCrop.offsetTo(0, 0);
        builder.setTaskSize(taskSize);
        return outCrop;
    }

    android.util.Pair<com.android.server.wm.ActivityRecord, com.android.server.wm.WindowState> checkIfReadyToSnapshot(TYPE source) {
        if (!this.mService.mPolicy.isScreenOn()) {
            android.util.Slog.i(TAG, "Attempted to take screenshot while display was off.");
            if (!this.mAbsAppSnapConExt.isSecondScreenOn(this.mService.mPolicy) && (source instanceof com.android.server.wm.Task) && !this.mAbsAppSnapConExt.snapshotForScreenOffActPreload((com.android.server.wm.Task) source)) {
                return null;
            }
        }
        com.android.server.wm.ActivityRecord activity = findAppTokenForSnapshot(source);
        if (activity == null) {
            android.util.Slog.w(TAG, "Failed to take screenshot. No visible windows for " + source);
            return null;
        }
        com.android.server.wm.WindowState mainWindow = activity.findMainWindow();
        if (mainWindow == null) {
            android.util.Slog.w(TAG, "Failed to take screenshot. No main window for " + source);
            return null;
        }
        if (activity.hasFixedRotationTransform()) {
            android.util.Slog.i(TAG, "Skip taking screenshot. App has fixed app = " + activity);
            return null;
        }
        return new android.util.Pair<>(activity, mainWindow);
    }

    private android.window.TaskSnapshot drawAppThemeSnapshot(TYPE source) {
        com.android.server.wm.WindowState mainWindow;
        int color;
        com.android.server.wm.ActivityRecord topActivity = getTopActivity(source);
        if (topActivity == null || (mainWindow = topActivity.findMainWindow()) == null || this.mAbsAppSnapConExt.skipDrawAppThemeSnapshot(topActivity)) {
            return null;
        }
        android.app.ActivityManager.TaskDescription taskDescription = getTaskDescription(source);
        int color2 = com.android.internal.graphics.ColorUtils.setAlphaComponent(taskDescription.getBackgroundColor(), 255);
        if (!(source instanceof com.android.server.wm.Task)) {
            color = color2;
        } else {
            color = this.mAbsAppSnapConExt.drawAppThemeSnapshot(color2, (com.android.server.wm.Task) source);
        }
        android.view.WindowManager.LayoutParams attrs = mainWindow.getAttrs();
        android.graphics.Rect taskBounds = source.getBounds();
        android.view.InsetsState insetsState = mainWindow.getInsetsStateWithVisibilityOverride();
        android.graphics.Rect systemBarInsets = getSystemBarInsets(mainWindow.getFrame(), insetsState);
        android.window.SnapshotDrawerUtils.SystemBarBackgroundPainter decorPainter = new android.window.SnapshotDrawerUtils.SystemBarBackgroundPainter(attrs.flags, attrs.privateFlags, attrs.insetsFlags.appearance, taskDescription, this.mHighResSnapshotScale, mainWindow.getRequestedVisibleTypes());
        int taskWidth = taskBounds.width();
        int taskHeight = taskBounds.height();
        int width = (int) (taskWidth * this.mHighResSnapshotScale);
        int height = (int) (taskHeight * this.mHighResSnapshotScale);
        android.graphics.RenderNode node = android.graphics.RenderNode.create("SnapshotController", null);
        node.setLeftTopRightBottom(0, 0, width, height);
        node.setClipToBounds(false);
        android.graphics.RecordingCanvas c = node.start(width, height);
        c.drawColor(color);
        decorPainter.setInsets(systemBarInsets);
        decorPainter.drawDecors(c, (android.graphics.Rect) null);
        node.end(c);
        android.graphics.Bitmap hwBitmap = android.view.ThreadedRenderer.createHardwareBitmap(node, width, height);
        if (hwBitmap != null) {
            android.graphics.Rect contentInsets = new android.graphics.Rect(systemBarInsets);
            android.graphics.Rect letterboxInsets = getLetterboxInsets(topActivity);
            com.android.server.wm.utils.InsetUtils.addInsets(contentInsets, letterboxInsets);
            android.window.TaskSnapshot taskSnapshot = new android.window.TaskSnapshot(java.lang.System.currentTimeMillis(), android.os.SystemClock.elapsedRealtimeNanos(), topActivity.mActivityComponent, hwBitmap.getHardwareBuffer(), hwBitmap.getColorSpace(), mainWindow.getConfiguration().orientation, mainWindow.getWindowConfiguration().getRotation(), new android.graphics.Point(taskWidth, taskHeight), contentInsets, letterboxInsets, false, false, source.getWindowingMode(), getAppearance(source), false, false, topActivity.getConfiguration().uiMode);
            return validateSnapshot(taskSnapshot);
        }
        android.util.Slog.d(TAG, "drawAppThemeSnapshot, hwBitmap is null");
        return null;
    }

    static android.graphics.Rect getSystemBarInsets(android.graphics.Rect frame, android.view.InsetsState state) {
        return state.calculateInsets(frame, android.view.WindowInsets.Type.systemBars(), false).toRect();
    }

    private int getAppearance(TYPE source) {
        com.android.server.wm.WindowState topFullscreenWindow;
        com.android.server.wm.ActivityRecord topFullscreenActivity = getTopFullscreenActivity(source);
        if (topFullscreenActivity != null) {
            topFullscreenWindow = topFullscreenActivity.findMainWindow();
        } else {
            topFullscreenWindow = null;
        }
        if (topFullscreenWindow != null) {
            return topFullscreenWindow.mAttrs.insetsFlags.appearance;
        }
        return 0;
    }

    void onAppRemoved(com.android.server.wm.ActivityRecord activity) {
        this.mCache.onAppRemoved(activity);
    }

    void onAppDied(com.android.server.wm.ActivityRecord activity) {
        this.mCache.onAppDied(activity);
    }

    boolean isAnimatingByRecents(com.android.server.wm.Task task) {
        return task.isAnimatingByRecents();
    }

    void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        pw.println(prefix + "mHighResSnapshotScale=" + this.mHighResSnapshotScale);
        pw.println(prefix + "mSnapshotEnabled=" + this.mSnapshotEnabled);
        this.mCache.dump(pw, prefix);
    }
}
