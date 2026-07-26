package com.android.server.camera;

/* JADX INFO: loaded from: classes.dex */
public class CameraServiceProxy extends com.android.server.SystemService implements android.os.Handler.Callback, android.os.IBinder.DeathRecipient {
    private static final java.lang.String CAMERA_SERVICE_BINDER_NAME = "media.camera";
    public static final java.lang.String CAMERA_SERVICE_PROXY_BINDER_NAME = "media.camera.proxy";
    private static final boolean DEBUG = false;
    private static final float MAX_PREVIEW_FPS = 60.0f;
    private static final int MAX_STREAM_STATISTICS = 5;
    private static final int MAX_USAGE_HISTORY = 20;
    private static final float MIN_PREVIEW_FPS = 30.0f;
    private static final int MSG_NFC_STATE_CHANGED = 3000;
    private static final int MSG_NOTIFY_DEVICE_STATE = 2;
    private static final int MSG_SWITCH_USER = 1;
    private static final java.lang.String NFC_NOTIFICATION_PROP = "ro.camera.notify_nfc";
    public static final long OVERRIDE_CAMERA_RESIZABLE_AND_SDK_CHECK = 191513214;
    public static final long OVERRIDE_CAMERA_ROTATE_AND_CROP_DEFAULTS = 189229956;
    private static final int RETRY_DELAY_TIME = 20;
    private static final int RETRY_TIMES = 60;
    private static final java.lang.String TAG = "CameraService_proxy";
    private static final android.os.IBinder nfcInterfaceToken = new android.os.Binder();
    private final android.util.ArrayMap<java.lang.String, com.android.server.camera.CameraServiceProxy.CameraUsageEvent> mActiveCameraUsage;
    private final java.util.List<com.android.server.camera.CameraServiceProxy.CameraEvent> mCameraEventHistory;
    private final android.hardware.ICameraServiceProxy.Stub mCameraServiceProxy;
    private com.android.server.camera.ICameraServiceProxyExt mCameraServiceProxyExt;
    private com.android.server.camera.ICameraServiceProxyWrapper mCameraServiceProxyWrapper;
    private android.hardware.ICameraService mCameraServiceRaw;
    private final android.content.Context mContext;
    private int mDeviceState;
    private final com.android.server.camera.CameraServiceProxy.DisplayWindowListener mDisplayWindowListener;
    private java.util.Set<java.lang.Integer> mEnabledCameraUsers;
    private final android.hardware.devicestate.DeviceStateManager.FoldStateListener mFoldStateListener;
    private final android.os.Handler mHandler;
    private final com.android.server.ServiceThread mHandlerThread;
    private final android.content.BroadcastReceiver mIntentReceiver;
    private volatile boolean mIsSmallWindow;
    private int mLastReportedDeviceState;
    private int mLastUser;
    private final java.lang.Object mLock;
    private java.util.concurrent.ScheduledThreadPoolExecutor mLogWriterService;
    private final boolean mNotifyNfc;
    private android.os.UserManager mUserManager;

    private interface CameraEvent {
        void logSelf();
    }

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @interface DeviceStateFlags {
    }

    public static final class TaskInfo {
        public int displayId;
        public int frontTaskId;
        public boolean isFixedOrientationLandscape;
        public boolean isFixedOrientationPortrait;
        public boolean isResizeable;
        public int userId;
    }

    private static class CameraUsageEvent implements com.android.server.camera.CameraServiceProxy.CameraEvent {
        public final int mAPILevel;
        public final int mAction;
        public final int mCameraFacing;
        public final java.lang.String mCameraId;
        public final java.lang.String mClientName;
        public boolean mDeviceError;
        public int mInternalReconfigure;
        public final boolean mIsNdk;
        public final int mLatencyMs;
        public final long mLogId;
        public final int mOperatingMode;
        public long mRequestCount;
        public long mResultErrorCount;
        public final int mSessionIndex;
        public java.util.List<android.hardware.CameraStreamStats> mStreamStats;
        public boolean mUsedUltraWide;
        public boolean mUsedZoomOverride;
        public java.lang.String mUserTag;
        public int mVideoStabilizationMode;
        public android.hardware.CameraExtensionSessionStats mExtSessionStats = null;
        private long mDurationOrStartTimeMs = android.os.SystemClock.elapsedRealtime();
        private boolean mCompleted = false;
        public android.util.Range<java.lang.Integer> mMostRequestedFpsRange = new android.util.Range<>(0, 0);

        CameraUsageEvent(java.lang.String cameraId, int facing, java.lang.String clientName, int apiLevel, boolean isNdk, int action, int latencyMs, int operatingMode, boolean deviceError, long logId, int sessionIdx) {
            this.mCameraId = cameraId;
            this.mCameraFacing = facing;
            this.mClientName = clientName;
            this.mAPILevel = apiLevel;
            this.mIsNdk = isNdk;
            this.mAction = action;
            this.mLatencyMs = latencyMs;
            this.mOperatingMode = operatingMode;
            this.mDeviceError = deviceError;
            this.mLogId = logId;
            this.mSessionIndex = sessionIdx;
        }

        public void markCompleted(int internalReconfigure, long requestCount, long resultErrorCount, boolean deviceError, java.util.List<android.hardware.CameraStreamStats> streamStats, java.lang.String userTag, int videoStabilizationMode, boolean usedUltraWide, boolean usedZoomOverride, android.util.Range<java.lang.Integer> mostRequestedFpsRange, android.hardware.CameraExtensionSessionStats extStats) {
            if (!this.mCompleted) {
                this.mCompleted = true;
                this.mDurationOrStartTimeMs = android.os.SystemClock.elapsedRealtime() - this.mDurationOrStartTimeMs;
                this.mInternalReconfigure = internalReconfigure;
                this.mRequestCount = requestCount;
                this.mResultErrorCount = resultErrorCount;
                this.mDeviceError = deviceError;
                this.mStreamStats = streamStats;
                this.mUserTag = userTag;
                this.mVideoStabilizationMode = videoStabilizationMode;
                this.mUsedUltraWide = usedUltraWide;
                this.mUsedZoomOverride = usedZoomOverride;
                this.mExtSessionStats = extStats;
                this.mMostRequestedFpsRange = mostRequestedFpsRange;
            }
        }

        public long getDuration() {
            if (this.mCompleted) {
                return this.mDurationOrStartTimeMs;
            }
            return 0L;
        }

        @Override // com.android.server.camera.CameraServiceProxy.CameraEvent
        public void logSelf() {
            boolean extensionIsAdvanced;
            int extensionCaptureFormat;
            int streamCount;
            int facing = 0;
            switch (this.mCameraFacing) {
                case 0:
                    facing = 1;
                    break;
                case 1:
                    facing = 2;
                    break;
                case 2:
                    facing = 3;
                    break;
                default:
                    android.util.Slog.w(com.android.server.camera.CameraServiceProxy.TAG, "Unknown camera facing: " + this.mCameraFacing);
                    break;
            }
            int extensionType = -1;
            if (this.mExtSessionStats == null) {
                extensionIsAdvanced = false;
                extensionCaptureFormat = 0;
            } else {
                switch (this.mExtSessionStats.type) {
                    case 0:
                        extensionType = 0;
                        break;
                    case 1:
                        extensionType = 1;
                        break;
                    case 2:
                        extensionType = 2;
                        break;
                    case 3:
                        extensionType = 3;
                        break;
                    case 4:
                        extensionType = 4;
                        break;
                    default:
                        android.util.Slog.w(com.android.server.camera.CameraServiceProxy.TAG, "Unknown extension type: " + this.mExtSessionStats.type);
                        break;
                }
                boolean extensionIsAdvanced2 = this.mExtSessionStats.isAdvanced;
                if (!com.android.internal.camera.flags.Flags.analytics24q3()) {
                    extensionIsAdvanced = extensionIsAdvanced2;
                    extensionCaptureFormat = 0;
                } else {
                    int extensionCaptureFormat2 = this.mExtSessionStats.captureFormat;
                    extensionIsAdvanced = extensionIsAdvanced2;
                    extensionCaptureFormat = extensionCaptureFormat2;
                }
            }
            if (this.mStreamStats == null) {
                streamCount = 0;
            } else {
                int streamCount2 = this.mStreamStats.size();
                streamCount = streamCount2;
            }
            android.stats.camera.nano.CameraProtos.CameraStreamProto[] streamProtos = new android.stats.camera.nano.CameraProtos.CameraStreamProto[5];
            for (int i = 0; i < 5; i++) {
                streamProtos[i] = new android.stats.camera.nano.CameraProtos.CameraStreamProto();
                if (i < streamCount) {
                    android.hardware.CameraStreamStats streamStats = this.mStreamStats.get(i);
                    streamProtos[i].width = streamStats.getWidth();
                    streamProtos[i].height = streamStats.getHeight();
                    streamProtos[i].format = streamStats.getFormat();
                    streamProtos[i].dataSpace = streamStats.getDataSpace();
                    streamProtos[i].usage = streamStats.getUsage();
                    streamProtos[i].requestCount = streamStats.getRequestCount();
                    streamProtos[i].errorCount = streamStats.getErrorCount();
                    streamProtos[i].firstCaptureLatencyMillis = streamStats.getStartLatencyMs();
                    streamProtos[i].maxHalBuffers = streamStats.getMaxHalBuffers();
                    streamProtos[i].maxAppBuffers = streamStats.getMaxAppBuffers();
                    streamProtos[i].histogramType = streamStats.getHistogramType();
                    streamProtos[i].histogramBins = streamStats.getHistogramBins();
                    streamProtos[i].histogramCounts = streamStats.getHistogramCounts();
                    streamProtos[i].dynamicRangeProfile = streamStats.getDynamicRangeProfile();
                    streamProtos[i].streamUseCase = streamStats.getStreamUseCase();
                    streamProtos[i].colorSpace = streamStats.getColorSpace();
                }
            }
            int streamCount3 = streamCount;
            int streamCount4 = facing;
            com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.CAMERA_ACTION_EVENT, getDuration(), this.mAPILevel, this.mClientName, streamCount4, this.mCameraId, this.mAction, this.mIsNdk, this.mLatencyMs, this.mOperatingMode, this.mInternalReconfigure, this.mRequestCount, this.mResultErrorCount, this.mDeviceError, streamCount3, com.android.framework.protobuf.nano.MessageNano.toByteArray(streamProtos[0]), com.android.framework.protobuf.nano.MessageNano.toByteArray(streamProtos[1]), com.android.framework.protobuf.nano.MessageNano.toByteArray(streamProtos[2]), com.android.framework.protobuf.nano.MessageNano.toByteArray(streamProtos[3]), com.android.framework.protobuf.nano.MessageNano.toByteArray(streamProtos[4]), this.mUserTag, this.mVideoStabilizationMode, this.mLogId, this.mSessionIndex, extensionType, extensionIsAdvanced, this.mUsedUltraWide, this.mUsedZoomOverride, ((java.lang.Integer) this.mMostRequestedFpsRange.getLower()).intValue(), ((java.lang.Integer) this.mMostRequestedFpsRange.getUpper()).intValue(), extensionCaptureFormat);
        }
    }

    private static class CameraFeatureCombinationQueryEvent implements com.android.server.camera.CameraServiceProxy.CameraEvent {
        private android.hardware.CameraFeatureCombinationStats mFeatureCombinationStats;

        CameraFeatureCombinationQueryEvent(android.hardware.CameraFeatureCombinationStats featureCombinationStats) {
            this.mFeatureCombinationStats = featureCombinationStats;
        }

        @Override // com.android.server.camera.CameraServiceProxy.CameraEvent
        public void logSelf() {
            int statusCode = -1;
            switch (this.mFeatureCombinationStats.mStatus) {
                case 0:
                    statusCode = 0;
                    break;
                case 3:
                    statusCode = 3;
                    break;
                case 10:
                    statusCode = 10;
                    break;
            }
            if (statusCode == -1) {
                android.util.Slog.w(com.android.server.camera.CameraServiceProxy.TAG, "Unknown feature combination query status code: " + this.mFeatureCombinationStats.mStatus);
            } else {
                com.android.internal.util.FrameworkStatsLog.write(900, this.mFeatureCombinationStats.mUid, this.mFeatureCombinationStats.mCameraId, this.mFeatureCombinationStats.mQueryType, this.mFeatureCombinationStats.mFeatureCombination, statusCode);
            }
        }
    }

    private final class DisplayWindowListener extends android.view.IDisplayWindowListener.Stub {
        private DisplayWindowListener() {
        }

        public void onDisplayConfigurationChanged(int displayId, android.content.res.Configuration newConfig) {
            android.hardware.ICameraService cs = com.android.server.camera.CameraServiceProxy.this.getCameraServiceRawLocked();
            if (cs == null) {
                return;
            }
            try {
                cs.notifyDisplayConfigurationChange();
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(com.android.server.camera.CameraServiceProxy.TAG, "Could not notify cameraserver, remote exception: " + e);
            }
        }

        public void onDisplayAdded(int displayId) {
        }

        public void onDisplayRemoved(int displayId) {
        }

        public void onFixedRotationStarted(int displayId, int newRotation) {
        }

        public void onFixedRotationFinished(int displayId) {
        }

        public void onKeepClearAreasChanged(int displayId, java.util.List<android.graphics.Rect> restricted, java.util.List<android.graphics.Rect> unrestricted) {
        }
    }

    private static boolean isMOrBelow(android.content.Context ctx, java.lang.String packageName) {
        try {
            return ctx.getPackageManager().getPackageInfo(packageName, 0).applicationInfo.targetSdkVersion <= 23;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            android.util.Slog.e(TAG, "Package name not found!");
            return false;
        }
    }

    public static int getCropRotateScale(android.content.Context ctx, java.lang.String packageName, com.android.server.camera.CameraServiceProxy.TaskInfo taskInfo, int displayRotation, int lensFacing, boolean ignoreResizableAndSdkCheck) {
        int rotationDegree;
        if (taskInfo == null) {
            return 0;
        }
        if (ctx.getResources().getBoolean(android.R.bool.config_hasPermanentDpad)) {
            android.util.Slog.v(TAG, "Disable Rotate and Crop to avoid conflicts with WM force rotation treatment.");
            return 0;
        }
        if (lensFacing != 0 && lensFacing != 1) {
            android.util.Log.v(TAG, "lensFacing=" + lensFacing + ". Crop-rotate-scale is disabled.");
            return 0;
        }
        if (!ignoreResizableAndSdkCheck && !isMOrBelow(ctx, packageName) && taskInfo.isResizeable) {
            android.util.Slog.v(TAG, "The activity is N or above and claims to support resizeable-activity. Crop-rotate-scale is disabled.");
            return 0;
        }
        if (!taskInfo.isFixedOrientationPortrait && !taskInfo.isFixedOrientationLandscape) {
            android.util.Log.v(TAG, "Non-fixed orientation activity. Crop-rotate-scale is disabled.");
            return 0;
        }
        switch (displayRotation) {
            case 0:
                rotationDegree = 0;
                break;
            case 1:
                rotationDegree = 90;
                break;
            case 2:
                rotationDegree = 180;
                break;
            case 3:
                rotationDegree = 270;
                break;
            default:
                android.util.Log.e(TAG, "Unsupported display rotation: " + displayRotation);
                return 0;
        }
        android.util.Slog.v(TAG, "Display.getRotation()=" + rotationDegree + " isFixedOrientationPortrait=" + taskInfo.isFixedOrientationPortrait + " isFixedOrientationLandscape=" + taskInfo.isFixedOrientationLandscape);
        if (rotationDegree == 0) {
            return 0;
        }
        if (lensFacing == 0) {
            rotationDegree = 360 - rotationDegree;
        }
        switch (rotationDegree) {
            case 90:
                break;
            case 180:
                break;
            case 270:
                break;
        }
        return 0;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public CameraServiceProxy(android.content.Context context) {
        super(context);
        this.mLock = new java.lang.Object();
        this.mActiveCameraUsage = new android.util.ArrayMap<>();
        this.mCameraEventHistory = new java.util.ArrayList();
        this.mLogWriterService = new java.util.concurrent.ScheduledThreadPoolExecutor(1);
        this.mIsSmallWindow = false;
        this.mDisplayWindowListener = new com.android.server.camera.CameraServiceProxy.DisplayWindowListener();
        this.mIntentReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.camera.CameraServiceProxy.1
            /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
            /* JADX WARN: Removed duplicated region for block: B:29:0x0055  */
            @Override // android.content.BroadcastReceiver
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct add '--show-bad-code' argument
            */
            public void onReceive(android.content.Context r6, android.content.Intent r7) {
                /*
                    Method dump skipped, instruction units count: 208
                    To view this dump add '--comments-level debug' option
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.server.camera.CameraServiceProxy.AnonymousClass1.onReceive(android.content.Context, android.content.Intent):void");
            }
        };
        this.mCameraServiceProxy = new android.hardware.ICameraServiceProxy.Stub() { // from class: com.android.server.camera.CameraServiceProxy.2
            public int getRotateAndCropOverride(java.lang.String packageName, int lensFacing, int userId) {
                if (android.os.Binder.getCallingUid() != 1047) {
                    android.util.Slog.e(com.android.server.camera.CameraServiceProxy.TAG, "Calling UID: " + android.os.Binder.getCallingUid() + " doesn't match expected  camera service UID!");
                    return 0;
                }
                com.android.server.camera.CameraServiceProxy.TaskInfo taskInfo = null;
                try {
                    android.content.pm.ParceledListSlice<android.app.ActivityManager.RecentTaskInfo> recentTasks = android.app.ActivityTaskManager.getService().getRecentTasks(2, 0, userId);
                    if (recentTasks != null && !recentTasks.getList().isEmpty()) {
                        java.util.Iterator it = recentTasks.getList().iterator();
                        while (true) {
                            if (!it.hasNext()) {
                                break;
                            }
                            android.app.ActivityManager.RecentTaskInfo task = (android.app.ActivityManager.RecentTaskInfo) it.next();
                            if (task.topActivityInfo != null && packageName.equals(task.topActivityInfo.packageName)) {
                                taskInfo = new com.android.server.camera.CameraServiceProxy.TaskInfo();
                                taskInfo.frontTaskId = task.taskId;
                                taskInfo.isResizeable = task.topActivityInfo.resizeMode != 0;
                                taskInfo.displayId = task.displayId;
                                taskInfo.userId = task.userId;
                                taskInfo.isFixedOrientationLandscape = android.content.pm.ActivityInfo.isFixedOrientationLandscape(task.topActivityInfo.screenOrientation);
                                taskInfo.isFixedOrientationPortrait = android.content.pm.ActivityInfo.isFixedOrientationPortrait(task.topActivityInfo.screenOrientation);
                            }
                        }
                        if (taskInfo == null) {
                            android.util.Log.e(com.android.server.camera.CameraServiceProxy.TAG, "Recent tasks don't include camera client package name: " + packageName);
                            return 0;
                        }
                        if (taskInfo != null && android.app.compat.CompatChanges.isChangeEnabled(com.android.server.camera.CameraServiceProxy.OVERRIDE_CAMERA_ROTATE_AND_CROP_DEFAULTS, packageName, android.os.UserHandle.getUserHandleForUid(taskInfo.userId))) {
                            android.util.Slog.v(com.android.server.camera.CameraServiceProxy.TAG, "OVERRIDE_CAMERA_ROTATE_AND_CROP_DEFAULTS enabled!");
                            return 0;
                        }
                        boolean ignoreResizableAndSdkCheck = false;
                        if (taskInfo != null && android.app.compat.CompatChanges.isChangeEnabled(com.android.server.camera.CameraServiceProxy.OVERRIDE_CAMERA_RESIZABLE_AND_SDK_CHECK, packageName, android.os.UserHandle.getUserHandleForUid(taskInfo.userId))) {
                            android.util.Slog.v(com.android.server.camera.CameraServiceProxy.TAG, "OVERRIDE_CAMERA_RESIZABLE_AND_SDK_CHECK enabled!");
                            ignoreResizableAndSdkCheck = true;
                        }
                        android.hardware.display.DisplayManager displayManager = (android.hardware.display.DisplayManager) com.android.server.camera.CameraServiceProxy.this.mContext.getSystemService(android.hardware.display.DisplayManager.class);
                        if (displayManager != null) {
                            android.view.Display display = displayManager.getDisplay(taskInfo.displayId);
                            if (display == null) {
                                android.util.Slog.e(com.android.server.camera.CameraServiceProxy.TAG, "Invalid display id: " + taskInfo.displayId);
                                return 0;
                            }
                            int displayRotation = display.getRotation();
                            return com.android.server.camera.CameraServiceProxy.getCropRotateScale(com.android.server.camera.CameraServiceProxy.this.mContext, packageName, taskInfo, displayRotation, lensFacing, ignoreResizableAndSdkCheck);
                        }
                        android.util.Slog.e(com.android.server.camera.CameraServiceProxy.TAG, "Failed to query display manager!");
                        return 0;
                    }
                    android.util.Log.e(com.android.server.camera.CameraServiceProxy.TAG, "Recent task list is empty!");
                    return 0;
                } catch (android.os.RemoteException e) {
                    android.util.Log.e(com.android.server.camera.CameraServiceProxy.TAG, "Failed to query recent tasks!");
                    return 0;
                }
            }

            public int getAutoframingOverride(java.lang.String packageName) {
                return 0;
            }

            public void pingForUserUpdate() {
                if (android.os.Binder.getCallingUid() != 1047) {
                    android.util.Slog.e(com.android.server.camera.CameraServiceProxy.TAG, "Calling UID: " + android.os.Binder.getCallingUid() + " doesn't match expected  camera service UID!");
                } else {
                    com.android.server.camera.CameraServiceProxy.this.notifySwitchWithRetries(60);
                    com.android.server.camera.CameraServiceProxy.this.notifyDeviceStateWithRetries(60);
                }
            }

            public void notifyCameraState(android.hardware.CameraSessionStats cameraState) throws java.lang.Throwable {
                if (android.os.Binder.getCallingUid() != 1047) {
                    android.util.Slog.e(com.android.server.camera.CameraServiceProxy.TAG, "Calling UID: " + android.os.Binder.getCallingUid() + " doesn't match expected  camera service UID!");
                    return;
                }
                com.android.server.camera.CameraServiceProxy.cameraStateToString(cameraState.getNewCameraState());
                com.android.server.camera.CameraServiceProxy.cameraFacingToString(cameraState.getFacing());
                com.android.server.camera.CameraServiceProxy.this.updateActivityCount(cameraState);
            }

            public void notifyFeatureCombinationStats(android.hardware.CameraFeatureCombinationStats featureCombStats) {
                if (!com.android.internal.camera.flags.Flags.analytics24q3()) {
                    return;
                }
                if (android.os.Binder.getCallingUid() != 1047) {
                    android.util.Slog.e(com.android.server.camera.CameraServiceProxy.TAG, "Calling UID: " + android.os.Binder.getCallingUid() + " doesn't match expected  camera service UID!");
                } else {
                    com.android.server.camera.CameraServiceProxy.this.updateFeatureCombinationQuery(featureCombStats);
                }
            }

            public boolean isCameraDisabled(int userId) {
                if (android.os.Binder.getCallingUid() != 1047) {
                    android.util.Slog.e(com.android.server.camera.CameraServiceProxy.TAG, "Calling UID: " + android.os.Binder.getCallingUid() + " doesn't match expected camera service UID!");
                    return false;
                }
                long ident = android.os.Binder.clearCallingIdentity();
                try {
                    android.app.admin.DevicePolicyManager dpm = (android.app.admin.DevicePolicyManager) com.android.server.camera.CameraServiceProxy.this.mContext.getSystemService(android.app.admin.DevicePolicyManager.class);
                    if (dpm != null) {
                        return dpm.getCameraDisabled(null, userId);
                    }
                    android.util.Slog.e(com.android.server.camera.CameraServiceProxy.TAG, "Failed to get the device policy manager service");
                    return false;
                } catch (java.lang.Exception e) {
                    e.printStackTrace();
                    return false;
                } finally {
                    android.os.Binder.restoreCallingIdentity(ident);
                }
            }

            /* JADX WARN: Multi-variable type inference failed */
            public void onShellCommand(java.io.FileDescriptor in, java.io.FileDescriptor out, java.io.FileDescriptor err, java.lang.String[] args, android.os.ShellCallback callback, android.os.ResultReceiver resultReceiver) throws android.os.RemoteException {
                new com.android.server.camera.CameraServiceProxy.AnonymousClass2.CSPShellCmd(com.android.server.camera.CameraServiceProxy.this).exec(this, in, out, err, args, callback, resultReceiver);
            }

            /* JADX INFO: renamed from: com.android.server.camera.CameraServiceProxy$2$CSPShellCmd */
            private static class CSPShellCmd extends android.os.ShellCommand {
                private static final java.lang.String TAG = "CSPShellCmd";
                private static final java.lang.String USAGE = "usage: cmd media.camera.proxy SUBCMD [args]\n\nSUBCMDs:\n    dump_events: Write out all collected camera usage events to statsd.\n        Does not print to terminal.\n    help: You're reading it.\n";
                private final com.android.server.camera.CameraServiceProxy mCameraServiceProxy;

                CSPShellCmd(com.android.server.camera.CameraServiceProxy proxy) {
                    this.mCameraServiceProxy = proxy;
                }

                public int onCommand(java.lang.String cmd) {
                    byte b;
                    if (cmd == null) {
                        return handleDefaultCommands(cmd);
                    }
                    java.io.PrintWriter pw = getOutPrintWriter();
                    try {
                        java.lang.String strReplace = cmd.replace('-', '_');
                        switch (strReplace.hashCode()) {
                            case -1224390204:
                                if (strReplace.equals("dump_events")) {
                                    b = 0;
                                    break;
                                }
                            default:
                                b = -1;
                                break;
                        }
                        switch (b) {
                            case 0:
                                int eventCount = this.mCameraServiceProxy.getUsageEventCount();
                                this.mCameraServiceProxy.dumpCameraEvents();
                                pw.println("Camera usage events dumped: " + eventCount);
                                return 0;
                            default:
                                return handleDefaultCommands(cmd);
                        }
                    } catch (java.lang.Exception e) {
                        android.util.Slog.e(com.android.server.camera.CameraServiceProxy.TAG, "Error running shell command", e);
                        return 1;
                    }
                }

                public void onHelp() {
                    getOutPrintWriter().println(USAGE);
                }
            }
        };
        this.mCameraServiceProxyWrapper = new com.android.server.camera.CameraServiceProxy.CameraServiceProxyWrapper();
        this.mContext = context;
        this.mHandlerThread = new com.android.server.ServiceThread(TAG, -4, false);
        this.mHandlerThread.start();
        this.mHandler = new android.os.Handler(this.mHandlerThread.getLooper(), this);
        this.mCameraServiceProxyExt = (com.android.server.camera.ICameraServiceProxyExt) system.ext.loader.core.ExtLoader.type(com.android.server.camera.ICameraServiceProxyExt.class).base(this).create();
        this.mNotifyNfc = android.os.SystemProperties.getInt(NFC_NOTIFICATION_PROP, 0) > 0;
        this.mLogWriterService.setKeepAliveTime(1L, java.util.concurrent.TimeUnit.SECONDS);
        this.mLogWriterService.allowCoreThreadTimeOut(true);
        this.mFoldStateListener = new android.hardware.devicestate.DeviceStateManager.FoldStateListener(this.mContext, new java.util.function.Consumer() { // from class: com.android.server.camera.CameraServiceProxy$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$new$0((java.lang.Boolean) obj);
            }
        });
        android.provider.Settings.System.putIntForUser(this.mContext.getContentResolver(), "oplus_camera_3rd_activity", 0, -2);
        this.mCameraServiceProxyExt.registerAppSwitchObserver();
        if (!this.mCameraServiceProxyExt.getIsRegistered() && this.mCameraServiceProxyExt.getRegisterTimes() < 5) {
            this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(2000), 60000L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(java.lang.Boolean folded) {
        if (folded.booleanValue()) {
            setDeviceStateFlags(4);
        } else {
            clearDeviceStateFlags(4);
        }
    }

    private void setDeviceStateFlags(int deviceStateFlags) {
        synchronized (this.mLock) {
            this.mHandler.removeMessages(2);
            this.mDeviceState |= deviceStateFlags;
            if (this.mDeviceState != this.mLastReportedDeviceState) {
                notifyDeviceStateWithRetriesLocked(60);
            }
        }
    }

    private void clearDeviceStateFlags(int deviceStateFlags) {
        synchronized (this.mLock) {
            this.mHandler.removeMessages(2);
            this.mDeviceState &= ~deviceStateFlags;
            if (this.mDeviceState != this.mLastReportedDeviceState) {
                notifyDeviceStateWithRetriesLocked(60);
            }
        }
    }

    @Override // android.os.Handler.Callback
    public boolean handleMessage(android.os.Message msg) {
        switch (msg.what) {
            case 1:
                notifySwitchWithRetries(msg.arg1);
                return true;
            case 2:
                notifyDeviceStateWithRetries(msg.arg1);
                return true;
            case 2000:
                this.mCameraServiceProxyExt.registerAppSwitchObserver();
                if (!this.mCameraServiceProxyExt.getIsRegistered()) {
                    android.os.Message message = this.mHandler.obtainMessage(2000);
                    this.mHandler.sendMessageDelayed(message, 60000L);
                }
                return true;
            case com.android.server.camera.ICameraServiceProxyExt.MSG_FLOAT_WINDOW_SHOW /* 2002 */:
                synchronized (this.mLock) {
                    this.mIsSmallWindow = this.mCameraServiceProxyExt.checkCameraFloatWindow();
                    break;
                }
                return true;
            case 3000:
                boolean isEmpty = this.mActiveCameraUsage.isEmpty();
                if (!isEmpty && this.mNotifyNfc && !this.mCameraServiceProxyExt.getNfcSwitchState()) {
                    for (int i = 0; i < this.mActiveCameraUsage.size(); i++) {
                        if (this.mActiveCameraUsage.valueAt(i).mCameraFacing == 0) {
                            notifyNfcService(false);
                        } else {
                            notifyNfcService(true);
                        }
                    }
                }
                return true;
            default:
                android.util.Slog.e(TAG, "CameraServiceProxy error, invalid message: " + msg.what);
                return true;
        }
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        this.mUserManager = android.os.UserManager.get(this.mContext);
        if (this.mUserManager == null) {
            throw new java.lang.IllegalStateException("UserManagerService must start before CameraServiceProxy!");
        }
        android.content.IntentFilter filter = new android.content.IntentFilter();
        filter.addAction("android.intent.action.USER_ADDED");
        filter.addAction("android.intent.action.USER_REMOVED");
        filter.addAction("android.intent.action.USER_INFO_CHANGED");
        filter.addAction("android.intent.action.MANAGED_PROFILE_ADDED");
        filter.addAction("android.intent.action.MANAGED_PROFILE_REMOVED");
        filter.addAction("android.hardware.usb.action.USB_DEVICE_ATTACHED");
        filter.addAction("android.hardware.usb.action.USB_DEVICE_DETACHED");
        this.mContext.registerReceiver(this.mIntentReceiver, filter);
        publishBinderService(CAMERA_SERVICE_PROXY_BINDER_NAME, this.mCameraServiceProxy);
        publishLocalService(com.android.server.camera.CameraServiceProxy.class, this);
    }

    @Override // com.android.server.SystemService
    public void onBootPhase(int phase) {
        if (phase == 1000) {
            com.android.server.camera.CameraStatsJobService.schedule(this.mContext);
            try {
                int[] displayIds = android.view.WindowManagerGlobal.getWindowManagerService().registerDisplayWindowListener(this.mDisplayWindowListener);
                for (int i : displayIds) {
                    this.mDisplayWindowListener.onDisplayAdded(i);
                }
            } catch (android.os.RemoteException e) {
                android.util.Log.e(TAG, "Failed to register display window listener!");
            }
            ((android.hardware.devicestate.DeviceStateManager) this.mContext.getSystemService(android.hardware.devicestate.DeviceStateManager.class)).registerCallback(new android.os.HandlerExecutor(this.mHandler), this.mFoldStateListener);
        }
    }

    @Override // com.android.server.SystemService
    public void onUserStarting(com.android.server.SystemService.TargetUser user) {
        synchronized (this.mLock) {
            if (this.mEnabledCameraUsers == null) {
                switchUserLocked(user.getUserIdentifier());
            }
        }
    }

    @Override // com.android.server.SystemService
    public void onUserSwitching(com.android.server.SystemService.TargetUser from, com.android.server.SystemService.TargetUser to) {
        synchronized (this.mLock) {
            switchUserLocked(to.getUserIdentifier());
        }
    }

    @Override // android.os.IBinder.DeathRecipient
    public void binderDied() {
        synchronized (this.mLock) {
            this.mCameraServiceRaw = null;
            boolean wasEmpty = this.mActiveCameraUsage.isEmpty();
            this.mActiveCameraUsage.clear();
            if (this.mNotifyNfc && !wasEmpty && !this.mCameraServiceProxyExt.getNfcSwitchState()) {
                notifyNfcService(true);
            }
            this.mCameraServiceProxyExt.unregisterAppSwitchObserver();
            this.mCameraServiceProxyExt.extendNotifyCameraState(-1, null, 1, "-1");
        }
    }

    private class EventWriterTask implements java.lang.Runnable {
        private static final long WRITER_SLEEP_MS = 100;
        private java.util.List<com.android.server.camera.CameraServiceProxy.CameraEvent> mEventList;

        EventWriterTask(java.util.List<com.android.server.camera.CameraServiceProxy.CameraEvent> eventList) {
            this.mEventList = eventList;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (this.mEventList != null) {
                for (com.android.server.camera.CameraServiceProxy.CameraEvent event : this.mEventList) {
                    event.logSelf();
                    try {
                        java.lang.Thread.sleep(WRITER_SLEEP_MS);
                    } catch (java.lang.InterruptedException e) {
                    }
                }
                this.mEventList.clear();
            }
        }
    }

    int getUsageEventCount() {
        int size;
        synchronized (this.mLock) {
            size = this.mCameraEventHistory.size();
        }
        return size;
    }

    void dumpCameraEvents() {
        synchronized (this.mLock) {
            java.util.Collections.shuffle(this.mCameraEventHistory);
            this.mLogWriterService.execute(new com.android.server.camera.CameraServiceProxy.EventWriterTask(new java.util.ArrayList(this.mCameraEventHistory)));
            this.mCameraEventHistory.clear();
        }
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.camera.CameraStatsJobService.schedule(this.mContext);
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.hardware.ICameraService getCameraServiceRawLocked() {
        if (this.mCameraServiceRaw == null) {
            android.os.IBinder cameraServiceBinder = getBinderService(CAMERA_SERVICE_BINDER_NAME);
            if (cameraServiceBinder == null) {
                return null;
            }
            try {
                cameraServiceBinder.linkToDeath(this, 0);
                this.mCameraServiceRaw = android.hardware.ICameraService.Stub.asInterface(cameraServiceBinder);
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(TAG, "Could not link to death of native camera service");
                return null;
            }
        }
        return this.mCameraServiceRaw;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void switchUserLocked(int userHandle) {
        java.util.Set<java.lang.Integer> currentUserHandles = getEnabledUserHandles(userHandle);
        this.mLastUser = userHandle;
        if (this.mEnabledCameraUsers == null || !this.mEnabledCameraUsers.equals(currentUserHandles)) {
            this.mEnabledCameraUsers = currentUserHandles;
            notifySwitchWithRetriesLocked(60);
        }
    }

    private boolean isAutomotive() {
        return this.mContext.getPackageManager().hasSystemFeature("android.hardware.type.automotive");
    }

    private java.util.Set<java.lang.Integer> getEnabledUserHandles(int currentUserHandle) {
        int[] userProfiles = this.mUserManager.getEnabledProfileIds(currentUserHandle);
        java.util.Set<java.lang.Integer> handles = new android.util.ArraySet<>(userProfiles.length);
        for (int id : userProfiles) {
            handles.add(java.lang.Integer.valueOf(id));
        }
        if (com.android.internal.camera.flags.Flags.cameraHsumPermission() && android.os.UserManager.isHeadlessSystemUserMode() && isAutomotive()) {
            handles.add(0);
        }
        return handles;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifySwitchWithRetries(int retries) {
        synchronized (this.mLock) {
            notifySwitchWithRetriesLocked(retries);
        }
    }

    private void notifySwitchWithRetriesLocked(int retries) {
        if (this.mEnabledCameraUsers == null) {
            return;
        }
        if (notifyCameraserverLocked(1, this.mEnabledCameraUsers)) {
            retries = 0;
        }
        if (retries <= 0) {
            return;
        }
        android.util.Slog.i(TAG, "Could not notify camera service of user switch, retrying...");
        this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(1, retries - 1, 0, null), 20L);
    }

    private boolean notifyCameraserverLocked(int eventType, java.util.Set<java.lang.Integer> updatedUserHandles) {
        android.hardware.ICameraService cameraService = getCameraServiceRawLocked();
        if (cameraService == null) {
            android.util.Slog.w(TAG, "Could not notify cameraserver, camera service not available.");
            return false;
        }
        try {
            this.mCameraServiceRaw.notifySystemEvent(eventType, toArray(updatedUserHandles));
            return true;
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(TAG, "Could not notify cameraserver, remote exception: " + e);
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyDeviceStateWithRetries(int retries) {
        synchronized (this.mLock) {
            notifyDeviceStateWithRetriesLocked(retries);
        }
    }

    private void notifyDeviceStateWithRetriesLocked(int retries) {
        if (notifyDeviceStateChangeLocked(this.mDeviceState) || retries <= 0) {
            return;
        }
        android.util.Slog.i(TAG, "Could not notify camera service of device state change, retrying...");
        this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(2, retries - 1, 0, null), 20L);
    }

    private boolean notifyDeviceStateChangeLocked(int deviceState) {
        android.hardware.ICameraService cameraService = getCameraServiceRawLocked();
        if (cameraService == null) {
            android.util.Slog.w(TAG, "Could not notify cameraserver, camera service not available.");
            return false;
        }
        try {
            this.mCameraServiceRaw.notifyDeviceStateChange(deviceState);
            this.mLastReportedDeviceState = deviceState;
            return true;
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(TAG, "Could not notify cameraserver, remote exception: " + e);
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean notifyUsbDeviceHotplugLocked(android.hardware.usb.UsbDevice device, boolean attached) {
        if (!device.getHasVideoCapture()) {
            return false;
        }
        android.hardware.ICameraService cameraService = getCameraServiceRawLocked();
        if (cameraService == null) {
            android.util.Slog.w(TAG, "Could not notify cameraserver, camera service not available.");
            return false;
        }
        int eventType = attached ? 2 : 3;
        try {
            this.mCameraServiceRaw.notifySystemEvent(eventType, new int[]{device.getDeviceId()});
            return true;
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(TAG, "Could not notify cameraserver, remote exception: " + e);
            return false;
        }
    }

    private float getMinFps(android.hardware.CameraSessionStats cameraState) {
        float maxFps = cameraState.getMaxPreviewFps();
        return java.lang.Math.max(java.lang.Math.min(maxFps, 60.0f), MIN_PREVIEW_FPS);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Can't wrap try/catch for region: R(12:92|(7:172|93|(4:95|(1:97)(1:98)|168|99)(1:103)|156|104|105|(4:185|106|166|114))|(1:121)(4:118|(0)(9:125|(3:174|127|(1:129))|130|152|135|136|137|138|139)|148|149)|160|123|(0)(1:134)|152|135|136|137|138|139) */
    /* JADX WARN: Code restructure failed: missing block: B:140:0x0381, code lost:
    
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:142:0x0385, code lost:
    
        r0 = th;
     */
    /* JADX WARN: Removed duplicated region for block: B:116:0x0347  */
    /* JADX WARN: Removed duplicated region for block: B:122:0x0353 A[ADDED_TO_REGION, REMOVE] */
    /* JADX WARN: Removed duplicated region for block: B:125:0x035d A[PHI: r4
  0x035d: PHI (r4v44 'facing' int) = (r4v42 'facing' int), (r4v46 'facing' int) binds: [B:124:0x035b, B:119:0x034d] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:134:0x0376  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void updateActivityCount(android.hardware.CameraSessionStats r67) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 940
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.camera.CameraServiceProxy.updateActivityCount(android.hardware.CameraSessionStats):void");
    }

    private boolean isNeedDisableNfc(int facing) {
        return facing == android.os.SystemProperties.getInt("ro.oplus.camera.facing.front.need.disable.nfc", -1) && facing == 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateFeatureCombinationQuery(android.hardware.CameraFeatureCombinationStats featureCombStats) {
        synchronized (this.mLock) {
            com.android.server.camera.CameraServiceProxy.CameraFeatureCombinationQueryEvent e = new com.android.server.camera.CameraServiceProxy.CameraFeatureCombinationQueryEvent(featureCombStats);
            this.mCameraEventHistory.add(e);
            if (this.mCameraEventHistory.size() > 20) {
                dumpCameraEvents();
            }
        }
    }

    private void notifyNfcService(boolean enablePolling) {
        android.nfc.NfcManager nfcManager = (android.nfc.NfcManager) this.mContext.getSystemService(android.nfc.NfcManager.class);
        if (nfcManager == null) {
            android.util.Slog.w(TAG, "Could not connect to NFC service to notify it of camera state");
            return;
        }
        android.nfc.NfcAdapter nfcAdapter = nfcManager.getDefaultAdapter();
        if (nfcAdapter == null) {
            android.util.Slog.w(TAG, "Could not connect to NFC service to notify it of camera state");
        } else {
            nfcAdapter.setReaderModePollingEnabled(enablePolling);
        }
    }

    private static int[] toArray(java.util.Collection<java.lang.Integer> c) {
        int len = c.size();
        int[] ret = new int[len];
        int idx = 0;
        for (java.lang.Integer i : c) {
            ret[idx] = i.intValue();
            idx++;
        }
        return ret;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static java.lang.String cameraStateToString(int newCameraState) {
        switch (newCameraState) {
            case 0:
                return "CAMERA_STATE_OPEN";
            case 1:
                return "CAMERA_STATE_ACTIVE";
            case 2:
                return "CAMERA_STATE_IDLE";
            case 3:
                return "CAMERA_STATE_CLOSED";
            case 4:
                return "CAMERA_STATE_EXCEPTION";
            default:
                return "CAMERA_STATE_UNKNOWN";
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static java.lang.String cameraFacingToString(int cameraFacing) {
        switch (cameraFacing) {
            case 0:
                return "CAMERA_FACING_BACK";
            case 1:
                return "CAMERA_FACING_FRONT";
            case 2:
                return "CAMERA_FACING_EXTERNAL";
            default:
                return "CAMERA_FACING_UNKNOWN";
        }
    }

    private static java.lang.String cameraHistogramTypeToString(int cameraHistogramType) {
        switch (cameraHistogramType) {
            case 1:
                return "HISTOGRAM_TYPE_CAPTURE_LATENCY";
            default:
                return "HISTOGRAM_TYPE_UNKNOWN";
        }
    }

    public com.android.server.camera.ICameraServiceProxyWrapper getWrapper() {
        return this.mCameraServiceProxyWrapper;
    }

    private class CameraServiceProxyWrapper implements com.android.server.camera.ICameraServiceProxyWrapper {
        private CameraServiceProxyWrapper() {
        }

        @Override // com.android.server.camera.ICameraServiceProxyWrapper
        public android.os.Handler getHandler() {
            return com.android.server.camera.CameraServiceProxy.this.mHandler;
        }
    }

    private static java.lang.String cameraFeatureCombinationTypeToString(int featureCombinationType) {
        switch (featureCombinationType) {
            case 0:
                return "QUERY_FEATURE_COMBINATION";
            case 1:
                return "QUERY_SESSION_CHARACTERISTICS";
            default:
                return "FEATURE_COMBINATION_TYPE_UNKNOWN";
        }
    }

    private static java.lang.String cameraFeatureCombinationToString(long featureCombination) {
        java.lang.StringBuilder combinationStr = new java.lang.StringBuilder("{");
        if ((1 & featureCombination) != 0) {
            combinationStr.append("60fps ");
        }
        if ((2 & featureCombination) != 0) {
            combinationStr.append("stabilization ");
        }
        if ((4 & featureCombination) != 0) {
            combinationStr.append("hlg10 ");
        }
        if ((8 & featureCombination) != 0) {
            combinationStr.append("jpeg ");
        }
        if ((16 & featureCombination) != 0) {
            combinationStr.append("jpeg_r ");
        }
        if ((32 & featureCombination) != 0) {
            combinationStr.append("4k ");
        }
        combinationStr.append("}");
        return combinationStr.toString();
    }
}
