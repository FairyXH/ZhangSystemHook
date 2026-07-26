package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class ActivityMetricsLogger {
    private static final int IGNORE_CALLER = -1;
    private static final long LATENCY_TRACKER_RECENTS_DELAY_MS = 300;
    private static final int MULTI_WINDOW_LAUNCH_TYPE_APP_PAIR = 1;
    private static final int MULTI_WINDOW_LAUNCH_TYPE_UNSPECIFIED = 0;
    private static final java.lang.String TAG = "ActivityTaskManager";
    private static final java.lang.String[] TRON_WINDOW_STATE_VARZ_STRINGS = {"window_time_0", "window_time_1", "window_time_2", "window_time_3", "window_time_4"};
    private static final long UNKNOWN_VISIBILITY_CHECK_DELAY_MS = 3000;
    private static final int WINDOW_STATE_ASSISTANT = 3;
    private static final int WINDOW_STATE_FREEFORM = 2;
    private static final int WINDOW_STATE_INVALID = -1;
    private static final int WINDOW_STATE_MULTI_WINDOW = 4;
    private static final int WINDOW_STATE_SIDE_BY_SIDE = 1;
    private static final int WINDOW_STATE_STANDARD = 0;
    public static com.android.server.wm.IActivityMetricsLoggerExt mActivityMetricsLoggerExt;
    private com.android.server.apphibernation.AppHibernationManagerInternal mAppHibernationManagerInternal;
    private android.content.pm.dex.ArtManagerInternal mArtManagerInternal;
    private final com.android.server.wm.LaunchObserverRegistryImpl mLaunchObserver;
    private final com.android.server.wm.ActivityTaskSupervisor mSupervisor;
    private int mWindowState = 0;
    private final com.android.internal.logging.MetricsLogger mMetricsLogger = new com.android.internal.logging.MetricsLogger();
    private final android.os.Handler mLoggerHandler = com.android.server.FgThread.getHandler();
    private final java.util.ArrayList<com.android.server.wm.ActivityMetricsLogger.TransitionInfo> mTransitionInfoList = new java.util.ArrayList<>();
    private final android.util.ArrayMap<com.android.server.wm.ActivityRecord, com.android.server.wm.ActivityMetricsLogger.TransitionInfo> mLastTransitionInfo = new android.util.ArrayMap<>();
    private final android.util.SparseArray<com.android.server.wm.ActivityMetricsLogger.PackageCompatStateInfo> mPackageUidToCompatStateInfo = new android.util.SparseArray<>(0);
    private final java.lang.StringBuilder mStringBuilder = new java.lang.StringBuilder();
    public com.android.server.wm.IActivityMetricsLoggerSocExt mActivityMetricsLoggerSocExt = (com.android.server.wm.IActivityMetricsLoggerSocExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IActivityMetricsLoggerSocExt.class).base(this).create();
    private final android.util.ArrayMap<java.lang.String, java.lang.Boolean> mLastHibernationStates = new android.util.ArrayMap<>();
    private long mLastLogTimeSecs = android.os.SystemClock.elapsedRealtime() / 1000;

    static final class LaunchingState {
        private static int sTraceSeqId;
        private com.android.server.wm.ActivityMetricsLogger.TransitionInfo mAssociatedTransitionInfo;
        java.lang.String mTraceName;
        final long mStartUptimeNs = android.os.SystemClock.uptimeNanos();
        final long mStartRealtimeNs = android.os.SystemClock.elapsedRealtimeNanos();

        LaunchingState() {
            if (!android.os.Trace.isTagEnabled(64L)) {
                return;
            }
            sTraceSeqId++;
            this.mTraceName = "launchingActivity#" + sTraceSeqId;
            android.os.Trace.asyncTraceBegin(64L, this.mTraceName, 0);
        }

        void stopTrace(boolean abort, com.android.server.wm.ActivityMetricsLogger.TransitionInfo endInfo) {
            java.lang.String status;
            java.lang.String status2;
            if (this.mTraceName == null) {
                return;
            }
            if (!abort && endInfo != this.mAssociatedTransitionInfo) {
                return;
            }
            android.os.Trace.asyncTraceEnd(64L, this.mTraceName, 0);
            if (this.mAssociatedTransitionInfo == null) {
                status2 = ":failed";
            } else {
                if (abort) {
                    status = ":canceled:";
                } else if (!this.mAssociatedTransitionInfo.mProcessSwitch) {
                    status = ":completed-same-process:";
                } else if (endInfo.mTransitionType == 9) {
                    status = ":completed-hot:";
                } else if (endInfo.mTransitionType == 8) {
                    status = ":completed-warm:";
                } else {
                    status = ":completed-cold:";
                }
                status2 = status + this.mAssociatedTransitionInfo.mLastLaunchedActivity.packageName;
            }
            android.os.Trace.instant(64L, this.mTraceName + status2);
            this.mTraceName = null;
        }

        boolean allDrawn() {
            return this.mAssociatedTransitionInfo != null && this.mAssociatedTransitionInfo.mIsDrawn;
        }

        boolean hasActiveTransitionInfo() {
            return this.mAssociatedTransitionInfo != null;
        }

        boolean contains(com.android.server.wm.ActivityRecord r) {
            return this.mAssociatedTransitionInfo != null && this.mAssociatedTransitionInfo.contains(r);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static final class TransitionInfo {
        int mCallingPid;
        int mCurrentTransitionDelayMs;
        boolean mIsDrawn;
        final boolean mIsInTaskActivityStart;
        com.android.server.wm.ActivityRecord mLastLaunchedActivity;
        java.lang.String mLaunchTraceName;
        final com.android.server.wm.ActivityMetricsLogger.LaunchingState mLaunchingState;
        boolean mLoggedStartingWindowDrawn;
        boolean mLoggedTransitionStarting;
        java.lang.Runnable mPendingFullyDrawn;
        final int mProcessOomAdj;
        boolean mProcessRunning;
        final int mProcessState;
        final boolean mProcessSwitch;
        boolean mRelaunched;
        int mSourceEventDelayMs;
        int mSourceType;
        int mTransitionType;
        int mWindowsDrawnDelayMs;
        int mStartingWindowDelayMs = -1;
        int mBindApplicationDelayMs = -1;
        int mReason = 3;
        int mMultiWindowLaunchType = 0;

        static com.android.server.wm.ActivityMetricsLogger.TransitionInfo create(com.android.server.wm.ActivityRecord r, com.android.server.wm.ActivityMetricsLogger.LaunchingState launchingState, android.app.ActivityOptions options, boolean processRunning, boolean processSwitch, int processState, int processOomAdj, boolean newActivityCreated, boolean isInTaskActivityStart, int startResult) {
            int transitionType;
            if (startResult != 0 && startResult != 2) {
                return null;
            }
            if (processRunning) {
                if (!newActivityCreated && r.attachedToProcess()) {
                    transitionType = 9;
                } else {
                    transitionType = 8;
                }
            } else {
                transitionType = 7;
            }
            return new com.android.server.wm.ActivityMetricsLogger.TransitionInfo(r, launchingState, options, transitionType, processRunning, processSwitch, processState, processOomAdj, isInTaskActivityStart);
        }

        private TransitionInfo(com.android.server.wm.ActivityRecord r, com.android.server.wm.ActivityMetricsLogger.LaunchingState launchingState, android.app.ActivityOptions options, int transitionType, boolean processRunning, boolean processSwitch, int processState, int processOomAdj, boolean isInTaskActivityStart) {
            android.app.ActivityOptions.SourceInfo sourceInfo;
            this.mSourceEventDelayMs = -1;
            this.mLaunchingState = launchingState;
            this.mTransitionType = transitionType;
            this.mProcessRunning = processRunning;
            this.mProcessSwitch = processSwitch;
            this.mProcessState = processState;
            this.mProcessOomAdj = processOomAdj;
            this.mIsInTaskActivityStart = isInTaskActivityStart;
            setLatestLaunchedActivity(r);
            if (launchingState.mAssociatedTransitionInfo == null) {
                launchingState.mAssociatedTransitionInfo = this;
            }
            if (options != null && (sourceInfo = options.getSourceInfo()) != null) {
                this.mSourceType = sourceInfo.type;
                this.mSourceEventDelayMs = (int) (java.util.concurrent.TimeUnit.NANOSECONDS.toMillis(launchingState.mStartUptimeNs) - sourceInfo.eventTimeMs);
            }
            this.mCallingPid = android.os.Binder.getCallingPid();
        }

        void setLatestLaunchedActivity(com.android.server.wm.ActivityRecord r) {
            if (this.mLastLaunchedActivity == r) {
                return;
            }
            if (this.mLastLaunchedActivity != null && !com.android.server.wm.ActivityMetricsLogger.mActivityMetricsLoggerExt.isMultiSearchTaskLaunched(r, this.mLastLaunchedActivity)) {
                r.mLaunchCookie = this.mLastLaunchedActivity.mLaunchCookie;
                this.mLastLaunchedActivity.mLaunchCookie = null;
                r.mLaunchRootTask = this.mLastLaunchedActivity.mLaunchRootTask;
                this.mLastLaunchedActivity.mLaunchRootTask = null;
            }
            this.mLastLaunchedActivity = r;
            this.mIsDrawn = r.isReportedDrawn();
        }

        boolean canCoalesce(com.android.server.wm.ActivityRecord r) {
            if (this.mLastLaunchedActivity.mDisplayContent != r.mDisplayContent || this.mLastLaunchedActivity.getWindowingMode() != r.getWindowingMode()) {
                return false;
            }
            com.android.server.wm.Task lastTask = this.mLastLaunchedActivity.getTask();
            com.android.server.wm.Task currentTask = r.getTask();
            if (lastTask != null && currentTask != null) {
                if (lastTask == currentTask) {
                    return true;
                }
                return lastTask.getBounds().equals(currentTask.getBounds());
            }
            return this.mLastLaunchedActivity.isUid(r.launchedFromUid);
        }

        boolean contains(com.android.server.wm.ActivityRecord r) {
            return r == this.mLastLaunchedActivity;
        }

        boolean isInterestingToLoggerAndObserver() {
            return this.mProcessSwitch;
        }

        int calculateCurrentDelay() {
            return calculateDelay(android.os.SystemClock.uptimeNanos());
        }

        int calculateDelay(long timestampNs) {
            return (int) java.util.concurrent.TimeUnit.NANOSECONDS.toMillis(timestampNs - this.mLaunchingState.mStartUptimeNs);
        }

        public java.lang.String toString() {
            return "TransitionInfo{" + java.lang.Integer.toHexString(java.lang.System.identityHashCode(this)) + " a=" + this.mLastLaunchedActivity + " d=" + this.mIsDrawn + "}";
        }
    }

    static final class TransitionInfoSnapshot {
        final int activityRecordIdHashCode;
        private final android.content.pm.ApplicationInfo applicationInfo;
        private final int bindApplicationDelayMs;
        private final java.lang.String launchedActivityAppRecordRequiredAbi;
        private final java.lang.String launchedActivityLaunchToken;
        private final java.lang.String launchedActivityLaunchedFromPackage;
        final java.lang.String launchedActivityName;
        final boolean launchedActivityProcessRunning;
        final java.lang.String launchedActivityShortComponentName;
        final int multiWindowLaunchType;
        final java.lang.String packageName;
        private final java.lang.String processName;
        private final com.android.server.wm.WindowProcessController processRecord;
        private final int reason;
        final boolean relaunched;
        final int sourceEventDelayMs;
        final int sourceType;
        private final int startingWindowDelayMs;
        final long timestampNs;
        final int type;
        final int userId;
        final int windowsDrawnDelayMs;
        final int windowsFullyDrawnDelayMs;

        private TransitionInfoSnapshot(com.android.server.wm.ActivityMetricsLogger.TransitionInfo info) {
            this(info, info.mLastLaunchedActivity, -1);
        }

        private TransitionInfoSnapshot(com.android.server.wm.ActivityMetricsLogger.TransitionInfo info, com.android.server.wm.ActivityRecord launchedActivity, int windowsFullyDrawnDelayMs) {
            java.lang.String requiredAbi;
            this.applicationInfo = launchedActivity.info.applicationInfo;
            this.packageName = launchedActivity.packageName;
            this.launchedActivityName = launchedActivity.info.name;
            this.launchedActivityLaunchedFromPackage = launchedActivity.launchedFromPackage;
            this.launchedActivityLaunchToken = launchedActivity.info.launchToken;
            if (launchedActivity.app == null) {
                requiredAbi = null;
            } else {
                requiredAbi = launchedActivity.app.getRequiredAbi();
            }
            this.launchedActivityAppRecordRequiredAbi = requiredAbi;
            this.reason = info.mReason;
            this.sourceEventDelayMs = info.mSourceEventDelayMs;
            this.startingWindowDelayMs = info.mStartingWindowDelayMs;
            this.bindApplicationDelayMs = info.mBindApplicationDelayMs;
            this.windowsDrawnDelayMs = info.mWindowsDrawnDelayMs;
            this.type = info.mTransitionType;
            this.processRecord = launchedActivity.app;
            this.processName = launchedActivity.processName;
            this.sourceType = info.mSourceType;
            this.userId = launchedActivity.mUserId;
            this.launchedActivityShortComponentName = launchedActivity.shortComponentName;
            this.activityRecordIdHashCode = java.lang.System.identityHashCode(launchedActivity);
            this.windowsFullyDrawnDelayMs = windowsFullyDrawnDelayMs;
            this.relaunched = info.mRelaunched;
            this.timestampNs = info.mLaunchingState.mStartRealtimeNs;
            this.multiWindowLaunchType = info.mMultiWindowLaunchType;
            this.launchedActivityProcessRunning = info.mProcessRunning;
        }

        int getLaunchState() {
            switch (this.type) {
                case 7:
                    return 1;
                case 8:
                    return 2;
                case 9:
                    return this.relaunched ? 4 : 3;
                default:
                    return -1;
            }
        }

        boolean isInterestedToEventLog() {
            return this.type == 8 || this.type == 7;
        }

        android.content.pm.dex.PackageOptimizationInfo getPackageOptimizationInfo(android.content.pm.dex.ArtManagerInternal artManagerInternal) {
            if (artManagerInternal == null || this.launchedActivityAppRecordRequiredAbi == null) {
                return android.content.pm.dex.PackageOptimizationInfo.createWithNoInfo();
            }
            return artManagerInternal.getPackageOptimizationInfo(this.applicationInfo, this.launchedActivityAppRecordRequiredAbi, this.launchedActivityName);
        }
    }

    private static final class PackageCompatStateInfo {
        com.android.server.wm.ActivityRecord mLastLoggedActivity;
        int mLastLoggedState;
        final java.util.ArrayList<com.android.server.wm.ActivityRecord> mVisibleActivities;

        private PackageCompatStateInfo() {
            this.mVisibleActivities = new java.util.ArrayList<>();
            this.mLastLoggedState = 1;
        }
    }

    ActivityMetricsLogger(com.android.server.wm.ActivityTaskSupervisor supervisor, android.os.Looper looper) {
        this.mSupervisor = supervisor;
        this.mLaunchObserver = new com.android.server.wm.LaunchObserverRegistryImpl(looper);
        mActivityMetricsLoggerExt = (com.android.server.wm.IActivityMetricsLoggerExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IActivityMetricsLoggerExt.class).base(this).create();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logWindowState(java.lang.String state, int durationSecs) {
        this.mMetricsLogger.count(state, durationSecs);
    }

    void logWindowState() {
        if (this.mWindowState == mActivityMetricsLoggerExt.getCompatWindowMode()) {
        }
        long now = android.os.SystemClock.elapsedRealtime() / 1000;
        if (this.mWindowState != -1) {
            this.mLoggerHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.internal.util.function.TriConsumer() { // from class: com.android.server.wm.ActivityMetricsLogger$$ExternalSyntheticLambda5
                public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3) {
                    ((com.android.server.wm.ActivityMetricsLogger) obj).logWindowState((java.lang.String) obj2, ((java.lang.Integer) obj3).intValue());
                }
            }, this, TRON_WINDOW_STATE_VARZ_STRINGS[this.mWindowState], java.lang.Integer.valueOf((int) (now - this.mLastLogTimeSecs))));
        }
        this.mLastLogTimeSecs = now;
        this.mWindowState = -1;
        com.android.server.wm.Task focusedTask = this.mSupervisor.mRootWindowContainer.getTopDisplayFocusedRootTask();
        if (focusedTask == null) {
            return;
        }
        if (focusedTask.isActivityTypeAssistant()) {
            this.mWindowState = 3;
            return;
        }
        int windowingMode = focusedTask.getWindowingMode();
        if (focusedTask.getWindowingMode() == mActivityMetricsLoggerExt.getZoomWindowMode() || focusedTask.getWindowingMode() == mActivityMetricsLoggerExt.getZoomToFullWindowMode()) {
            this.mWindowState = mActivityMetricsLoggerExt.getZoomWindowState();
            return;
        }
        if (focusedTask.getWindowingMode() == mActivityMetricsLoggerExt.getCompatWindowMode()) {
            return;
        }
        switch (windowingMode) {
            case 1:
                this.mWindowState = 0;
                break;
            case 5:
                this.mWindowState = 2;
                break;
            case 6:
                this.mWindowState = 4;
                break;
            default:
                if (windowingMode != 0) {
                    android.util.Slog.wtf(TAG, "Unknown windowing mode for task=" + focusedTask + " windowingMode=" + windowingMode);
                }
                break;
        }
    }

    private com.android.server.wm.ActivityMetricsLogger.TransitionInfo getActiveTransitionInfo(com.android.server.wm.ActivityRecord r) {
        for (int i = this.mTransitionInfoList.size() - 1; i >= 0; i--) {
            com.android.server.wm.ActivityMetricsLogger.TransitionInfo info = this.mTransitionInfoList.get(i);
            if (info.contains(r)) {
                return info;
            }
        }
        return null;
    }

    com.android.server.wm.ActivityMetricsLogger.LaunchingState notifyActivityLaunching(android.content.Intent intent) {
        return notifyActivityLaunching(intent, null, -1);
    }

    com.android.server.wm.ActivityMetricsLogger.LaunchingState notifyActivityLaunching(android.content.Intent intent, com.android.server.wm.ActivityRecord caller, int callingUid) {
        com.android.server.wm.ActivityMetricsLogger.TransitionInfo existingInfo = null;
        if (callingUid != -1) {
            int i = this.mTransitionInfoList.size() - 1;
            while (true) {
                if (i < 0) {
                    break;
                }
                com.android.server.wm.ActivityMetricsLogger.TransitionInfo info = this.mTransitionInfoList.get(i);
                if (caller != null && info.contains(caller)) {
                    existingInfo = info;
                    break;
                }
                if (existingInfo == null && callingUid == info.mLastLaunchedActivity.getUid()) {
                    existingInfo = info;
                }
                i--;
            }
        }
        if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_METRICS) {
            android.util.Slog.i(TAG, "notifyActivityLaunching intent=" + intent + " existingInfo=" + existingInfo);
        }
        if (existingInfo == null) {
            com.android.server.wm.ActivityMetricsLogger.LaunchingState launchingState = new com.android.server.wm.ActivityMetricsLogger.LaunchingState();
            launchObserverNotifyIntentStarted(intent, launchingState.mStartUptimeNs);
            return launchingState;
        }
        return existingInfo.mLaunchingState;
    }

    void notifyActivityLaunched(com.android.server.wm.ActivityMetricsLogger.LaunchingState launchingState, int resultCode, boolean newActivityCreated, com.android.server.wm.ActivityRecord launchedActivity, android.app.ActivityOptions options) {
        com.android.server.wm.WindowProcessController processController;
        int processState;
        int processOomAdj;
        if (launchedActivity == null || launchedActivity.getTask() == null) {
            abort(launchingState, "nothing launched");
            return;
        }
        if (launchedActivity.app != null) {
            processController = launchedActivity.app;
        } else {
            processController = this.mSupervisor.mService.getProcessController(launchedActivity.processName, launchedActivity.info.applicationInfo.uid);
        }
        com.android.server.wm.WindowProcessController processRecord = processController;
        boolean processRunning = processRecord != null;
        boolean processSwitch = (processRunning && processRecord.hasStartedActivity(launchedActivity)) ? false : true;
        if (processRunning) {
            int processState2 = processRecord.getCurrentProcState();
            processState = processState2;
            processOomAdj = processRecord.getCurrentAdj();
        } else {
            processState = 20;
            processOomAdj = -10000;
        }
        com.android.server.wm.ActivityMetricsLogger.TransitionInfo info = launchingState.mAssociatedTransitionInfo;
        if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_METRICS) {
            android.util.Slog.i(TAG, "notifyActivityLaunched resultCode=" + resultCode + " launchedActivity=" + launchedActivity + " processRunning=" + processRunning + " processSwitch=" + processSwitch + " processState=" + processState + " processOomAdj=" + processOomAdj + " newActivityCreated=" + newActivityCreated + " info=" + info);
        }
        if (launchedActivity.isReportedDrawn() && launchedActivity.isVisible()) {
            abort(launchingState, "launched activity already visible");
            return;
        }
        if (info != null && info.canCoalesce(launchedActivity)) {
            if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_METRICS) {
                android.util.Slog.i(TAG, "notifyActivityLaunched consecutive launch");
            }
            boolean crossPackage = !info.mLastLaunchedActivity.packageName.equals(launchedActivity.packageName);
            boolean forceUpdateTrace = ((android.os.IOplusJankMonitorExt) system.ext.loader.core.ExtLoader.type(android.os.IOplusJankMonitorExt.class).create()).forceUpdateTrace(processRunning, launchedActivity.packageName, info.mLastLaunchedActivity.packageName);
            if (crossPackage || forceUpdateTrace) {
                stopLaunchTrace(info);
                java.lang.String stageInfo = info.mStartingWindowDelayMs + " " + info.mBindApplicationDelayMs;
                ((android.os.IOplusJankMonitorExt) system.ext.loader.core.ExtLoader.type(android.os.IOplusJankMonitorExt.class).create()).stopLaunchTrace(info.mLastLaunchedActivity.shortComponentName, info.mProcessRunning, info.mLastLaunchedActivity.getPid(), android.os.SystemClock.uptimeMillis(), info.mLastLaunchedActivity.launchedFromPackage, stageInfo);
            }
            this.mLastTransitionInfo.remove(info.mLastLaunchedActivity);
            info.setLatestLaunchedActivity(launchedActivity);
            this.mLastTransitionInfo.put(launchedActivity, info);
            if (crossPackage || forceUpdateTrace) {
                startLaunchTrace(info);
                mActivityMetricsLoggerExt.startLaunchTrace(info.mProcessRunning, info.mLastLaunchedActivity.shortComponentName, info.mLastLaunchedActivity.getPid(), resultCode, info.mLastLaunchedActivity.getDisplayId());
            }
            scheduleCheckActivityToBeDrawnIfSleeping(launchedActivity);
            return;
        }
        boolean isInTaskActivityStart = launchedActivity.getTask().isVisible();
        com.android.server.wm.ActivityMetricsLogger.TransitionInfo newInfo = com.android.server.wm.ActivityMetricsLogger.TransitionInfo.create(launchedActivity, launchingState, options, processRunning, processSwitch, processState, processOomAdj, newActivityCreated, isInTaskActivityStart, resultCode);
        if (newInfo == null) {
            abort(launchingState, "unrecognized launch");
            return;
        }
        updateSplitPairLaunches(newInfo);
        if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_METRICS) {
            android.util.Slog.i(TAG, "notifyActivityLaunched successful");
        }
        this.mTransitionInfoList.add(newInfo);
        this.mLastTransitionInfo.put(launchedActivity, newInfo);
        startLaunchTrace(newInfo);
        mActivityMetricsLoggerExt.notifyActivityStarted(newInfo.mLastLaunchedActivity.packageName, newInfo.mLastLaunchedActivity.shortComponentName);
        mActivityMetricsLoggerExt.startLaunchTrace(newInfo.mProcessRunning, newInfo.mLastLaunchedActivity.shortComponentName, newInfo.mLastLaunchedActivity.getPid(), resultCode, newInfo.mLastLaunchedActivity.getDisplayId());
        if (!newInfo.isInterestingToLoggerAndObserver()) {
            launchObserverNotifyIntentFailed(newInfo.mLaunchingState.mStartUptimeNs);
        } else {
            launchObserverNotifyActivityLaunched(newInfo);
        }
        scheduleCheckActivityToBeDrawnIfSleeping(launchedActivity);
        for (int i = this.mTransitionInfoList.size() - 2; i >= 0; i--) {
            com.android.server.wm.ActivityMetricsLogger.TransitionInfo prevInfo = this.mTransitionInfoList.get(i);
            if (prevInfo.mIsDrawn || !prevInfo.mLastLaunchedActivity.isVisibleRequested()) {
                scheduleCheckActivityToBeDrawn(prevInfo.mLastLaunchedActivity, 0L);
            }
        }
    }

    private void updateSplitPairLaunches(com.android.server.wm.ActivityMetricsLogger.TransitionInfo info) {
        com.android.server.wm.Task launchedActivityTask = info.mLastLaunchedActivity.getTask();
        com.android.server.wm.Task adjacentToLaunchedTask = launchedActivityTask.getAdjacentTask();
        if (adjacentToLaunchedTask == null) {
            return;
        }
        for (int i = this.mTransitionInfoList.size() - 1; i >= 0; i--) {
            com.android.server.wm.ActivityMetricsLogger.TransitionInfo otherInfo = this.mTransitionInfoList.get(i);
            if (otherInfo != info) {
                com.android.server.wm.Task otherTask = otherInfo.mLastLaunchedActivity.getTask();
                if (otherTask.isDescendantOf(adjacentToLaunchedTask)) {
                    if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_METRICS) {
                        android.util.Slog.i(TAG, "Found adjacent tasks t1=" + launchedActivityTask.mTaskId + " t2=" + otherTask.mTaskId);
                    }
                    info.mMultiWindowLaunchType = 1;
                    otherInfo.mMultiWindowLaunchType = 1;
                }
            }
        }
    }

    private void scheduleCheckActivityToBeDrawnIfSleeping(com.android.server.wm.ActivityRecord r) {
        if (r.mDisplayContent != null && r.mDisplayContent.isSleeping()) {
            scheduleCheckActivityToBeDrawn(r, 3000L);
        }
    }

    com.android.server.wm.ActivityMetricsLogger.TransitionInfoSnapshot notifyWindowsDrawn(final com.android.server.wm.ActivityRecord r) {
        if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_METRICS) {
            android.util.Slog.i(TAG, "notifyWindowsDrawn " + r);
        }
        final long timestampNs = android.os.SystemClock.uptimeNanos();
        final com.android.server.wm.ActivityMetricsLogger.TransitionInfo info = getActiveTransitionInfo(r);
        if (info == null || info.mIsDrawn) {
            if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_METRICS) {
                android.util.Slog.i(TAG, "notifyWindowsDrawn not pending drawn " + info);
            }
            return null;
        }
        info.mWindowsDrawnDelayMs = info.calculateDelay(timestampNs);
        info.mIsDrawn = true;
        com.android.server.wm.ActivityMetricsLogger.TransitionInfoSnapshot infoSnapshot = new com.android.server.wm.ActivityMetricsLogger.TransitionInfoSnapshot(info);
        if (info.mLoggedTransitionStarting || (!r.mDisplayContent.mOpeningApps.contains(r) && !r.mTransitionController.isCollecting(r))) {
            done(false, info, "notifyWindowsDrawn", timestampNs);
        }
        if (android.app.Flags.appStartInfoTimestamps()) {
            this.mLoggerHandler.post(new java.lang.Runnable() { // from class: com.android.server.wm.ActivityMetricsLogger$$ExternalSyntheticLambda8
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$notifyWindowsDrawn$0(timestampNs, r, info);
                }
            });
        }
        return infoSnapshot;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$notifyWindowsDrawn$0(long timestampNs, com.android.server.wm.ActivityRecord r, com.android.server.wm.ActivityMetricsLogger.TransitionInfo info) {
        this.mSupervisor.mService.mWindowManager.mAmInternal.addStartInfoTimestamp(4, timestampNs, r.getUid(), r.getPid(), info.mLastLaunchedActivity.mUserId);
    }

    void notifyStartingWindowDrawn(com.android.server.wm.ActivityRecord r) {
        com.android.server.wm.ActivityMetricsLogger.TransitionInfo info = getActiveTransitionInfo(r);
        if (info == null || info.mLoggedStartingWindowDrawn) {
            return;
        }
        if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_METRICS) {
            android.util.Slog.i(TAG, "notifyStartingWindowDrawn " + r);
        }
        info.mLoggedStartingWindowDrawn = true;
        info.mStartingWindowDelayMs = info.calculateCurrentDelay();
    }

    void notifyTransitionStarting(android.util.ArrayMap<com.android.server.wm.WindowContainer, java.lang.Integer> activityToReason) {
        if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_METRICS) {
            android.util.Slog.i(TAG, "notifyTransitionStarting " + activityToReason);
        }
        long timestampNs = android.os.SystemClock.uptimeNanos();
        for (int index = activityToReason.size() - 1; index >= 0; index--) {
            com.android.server.wm.WindowContainer<?> wc = activityToReason.keyAt(index);
            com.android.server.wm.ActivityRecord activity = wc.asActivityRecord();
            com.android.server.wm.ActivityRecord r = activity != null ? activity : wc.getTopActivity(false, true);
            com.android.server.wm.ActivityMetricsLogger.TransitionInfo info = getActiveTransitionInfo(r);
            if (info != null && !info.mLoggedTransitionStarting) {
                if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_METRICS) {
                    android.util.Slog.i(TAG, "notifyTransitionStarting activity=" + wc + " info=" + info);
                }
                info.mCurrentTransitionDelayMs = info.calculateDelay(timestampNs);
                info.mReason = activityToReason.valueAt(index).intValue();
                info.mLoggedTransitionStarting = true;
                if (info.mIsDrawn) {
                    done(false, info, "notifyTransitionStarting drawn", timestampNs);
                }
            }
        }
    }

    void notifyActivityRelaunched(com.android.server.wm.ActivityRecord r) {
        com.android.server.wm.ActivityMetricsLogger.TransitionInfo info = getActiveTransitionInfo(r);
        if (info != null) {
            info.mRelaunched = true;
        }
    }

    void notifyActivityRemoved(com.android.server.wm.ActivityRecord r) {
        this.mLastTransitionInfo.remove(r);
        com.android.server.wm.ActivityMetricsLogger.TransitionInfo info = getActiveTransitionInfo(r);
        if (info != null) {
            abort(info, "removed");
        }
        int packageUid = r.info.applicationInfo.uid;
        com.android.server.wm.ActivityMetricsLogger.PackageCompatStateInfo compatStateInfo = this.mPackageUidToCompatStateInfo.get(packageUid);
        if (compatStateInfo == null) {
            return;
        }
        compatStateInfo.mVisibleActivities.remove(r);
        if (compatStateInfo.mLastLoggedActivity == r) {
            compatStateInfo.mLastLoggedActivity = null;
        }
    }

    void notifyVisibilityChanged(com.android.server.wm.ActivityRecord r) {
        com.android.server.wm.ActivityMetricsLogger.TransitionInfo info = getActiveTransitionInfo(r);
        if (info == null) {
            return;
        }
        if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_METRICS) {
            android.util.Slog.i(TAG, "notifyVisibilityChanged " + r + " visible=" + r.isVisibleRequested() + " state=" + r.getState() + " finishing=" + r.finishing);
        }
        if (r.isState(com.android.server.wm.ActivityRecord.State.RESUMED) && r.mDisplayContent.isSleeping()) {
            return;
        }
        if (!r.isVisibleRequested() || r.finishing) {
            scheduleCheckActivityToBeDrawn(r, 0L);
        }
    }

    private void scheduleCheckActivityToBeDrawn(com.android.server.wm.ActivityRecord r, long delay) {
        r.mAtmService.mH.sendMessageDelayed(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.internal.util.function.TriConsumer() { // from class: com.android.server.wm.ActivityMetricsLogger$$ExternalSyntheticLambda7
            public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3) {
                ((com.android.server.wm.ActivityMetricsLogger) obj).checkActivityToBeDrawn((com.android.server.wm.Task) obj2, (com.android.server.wm.ActivityRecord) obj3);
            }
        }, this, r.getTask(), r), delay);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkActivityToBeDrawn(com.android.server.wm.Task t, com.android.server.wm.ActivityRecord r) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mSupervisor.mService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.ActivityMetricsLogger.TransitionInfo info = getActiveTransitionInfo(r);
                if (info == null) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return;
                }
                if (t != null && t.forAllActivities(new java.util.function.Predicate() { // from class: com.android.server.wm.ActivityMetricsLogger$$ExternalSyntheticLambda1
                    @Override // java.util.function.Predicate
                    public final boolean test(java.lang.Object obj) {
                        return com.android.server.wm.ActivityMetricsLogger.lambda$checkActivityToBeDrawn$1((com.android.server.wm.ActivityRecord) obj);
                    }
                })) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return;
                }
                if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_METRICS) {
                    android.util.Slog.i(TAG, "checkActivityToBeDrawn cancels activity=" + r);
                }
                logAppTransitionCancel(info);
                abort(info, "checkActivityToBeDrawn (invisible or drawn already)");
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    static /* synthetic */ boolean lambda$checkActivityToBeDrawn$1(com.android.server.wm.ActivityRecord a) {
        return (!a.isVisibleRequested() || a.isReportedDrawn() || a.finishing) ? false : true;
    }

    private com.android.server.apphibernation.AppHibernationManagerInternal getAppHibernationManagerInternal() {
        if (!com.android.server.apphibernation.AppHibernationService.isAppHibernationEnabled()) {
            return null;
        }
        if (this.mAppHibernationManagerInternal == null) {
            this.mAppHibernationManagerInternal = (com.android.server.apphibernation.AppHibernationManagerInternal) com.android.server.LocalServices.getService(com.android.server.apphibernation.AppHibernationManagerInternal.class);
        }
        return this.mAppHibernationManagerInternal;
    }

    void notifyBeforePackageUnstopped(java.lang.String packageName) {
        com.android.server.apphibernation.AppHibernationManagerInternal ahmInternal = getAppHibernationManagerInternal();
        if (ahmInternal != null) {
            this.mLastHibernationStates.put(packageName, java.lang.Boolean.valueOf(ahmInternal.isHibernatingGlobally(packageName)));
        }
    }

    void notifyBindApplication(android.content.pm.ApplicationInfo appInfo) {
        for (int i = this.mTransitionInfoList.size() - 1; i >= 0; i--) {
            com.android.server.wm.ActivityMetricsLogger.TransitionInfo info = this.mTransitionInfoList.get(i);
            if (info.mLastLaunchedActivity.info.applicationInfo == appInfo) {
                info.mBindApplicationDelayMs = info.calculateCurrentDelay();
                if (info.mProcessRunning) {
                    info.mProcessRunning = false;
                    info.mTransitionType = 7;
                    java.lang.String msg = "Process " + info.mLastLaunchedActivity.info.processName + " restarted";
                    android.util.Slog.i(TAG, msg);
                    if (info.mLaunchingState.mTraceName != null) {
                        android.os.Trace.instant(64L, msg + "#" + com.android.server.wm.ActivityMetricsLogger.LaunchingState.sTraceSeqId);
                    }
                }
            }
        }
    }

    private void abort(com.android.server.wm.ActivityMetricsLogger.LaunchingState state, java.lang.String cause) {
        if (state.mAssociatedTransitionInfo != null) {
            abort(state.mAssociatedTransitionInfo, cause);
            return;
        }
        if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_METRICS) {
            android.util.Slog.i(TAG, "abort launch cause=" + cause);
        }
        state.stopTrace(true, null);
        launchObserverNotifyIntentFailed(state.mStartUptimeNs);
    }

    private void abort(com.android.server.wm.ActivityMetricsLogger.TransitionInfo info, java.lang.String cause) {
        done(true, info, cause, 0L);
    }

    private void done(boolean abort, com.android.server.wm.ActivityMetricsLogger.TransitionInfo info, java.lang.String cause, long timestampNs) {
        if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_METRICS) {
            android.util.Slog.i(TAG, "done abort=" + abort + " cause=" + cause + " timestamp=" + timestampNs + " info=" + info);
        }
        info.mLaunchingState.stopTrace(abort, info);
        stopLaunchTrace(info);
        java.lang.Boolean isHibernating = this.mLastHibernationStates.remove(info.mLastLaunchedActivity.packageName);
        if (abort) {
            this.mLastTransitionInfo.remove(info.mLastLaunchedActivity);
            this.mSupervisor.stopWaitingForActivityVisible(info.mLastLaunchedActivity);
            launchObserverNotifyActivityLaunchCancelled(info);
        } else {
            if (info.isInterestingToLoggerAndObserver()) {
                launchObserverNotifyActivityLaunchFinished(info, timestampNs);
            }
            logAppTransitionFinished(info, isHibernating != null ? isHibernating.booleanValue() : false);
            if (info.mReason == 5) {
                logRecentsAnimationLatency(info);
            }
        }
        this.mTransitionInfoList.remove(info);
    }

    private void logAppTransitionCancel(com.android.server.wm.ActivityMetricsLogger.TransitionInfo info) {
        int type = info.mTransitionType;
        com.android.server.wm.ActivityRecord activity = info.mLastLaunchedActivity;
        android.metrics.LogMaker builder = new android.metrics.LogMaker(1144);
        builder.setPackageName(activity.packageName);
        builder.setType(type);
        builder.addTaggedData(com.android.bluetooth.BluetoothStatsLog.LE_RADIO_SCAN_STOPPED, activity.info.name);
        this.mMetricsLogger.write(builder);
        com.android.internal.util.FrameworkStatsLog.write(49, activity.info.applicationInfo.uid, activity.packageName, getAppStartTransitionType(type, info.mRelaunched), activity.info.name);
        if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_METRICS) {
            android.util.Slog.i(TAG, java.lang.String.format("APP_START_CANCELED(%s, %s, %s, %s)", java.lang.Integer.valueOf(activity.info.applicationInfo.uid), activity.packageName, java.lang.Integer.valueOf(getAppStartTransitionType(type, info.mRelaunched)), activity.info.name));
        }
    }

    private void logAppTransitionFinished(final com.android.server.wm.ActivityMetricsLogger.TransitionInfo info, final boolean isHibernating) {
        if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_METRICS) {
            android.util.Slog.i(TAG, "logging finished transition " + info);
        }
        this.mActivityMetricsLoggerSocExt.hookLogAppTransitionFinished(info.mLastLaunchedActivity);
        final com.android.server.wm.ActivityMetricsLogger.TransitionInfoSnapshot infoSnapshot = new com.android.server.wm.ActivityMetricsLogger.TransitionInfoSnapshot(info);
        final boolean isOpaque = info.mLastLaunchedActivity.mStyleFillsParent;
        final long uptimeNs = info.mLaunchingState.mStartUptimeNs;
        final int transitionDelay = info.mCurrentTransitionDelayMs;
        final int processState = info.mProcessState;
        final int processOomAdj = info.mProcessOomAdj;
        this.mLoggerHandler.post(new java.lang.Runnable() { // from class: com.android.server.wm.ActivityMetricsLogger$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$logAppTransitionFinished$2(info, uptimeNs, transitionDelay, infoSnapshot, isHibernating, processState, processOomAdj, isOpaque);
            }
        });
        if (info.mPendingFullyDrawn != null) {
            info.mPendingFullyDrawn.run();
        }
        info.mLastLaunchedActivity.info.launchToken = null;
        java.lang.String stageInfo = infoSnapshot.startingWindowDelayMs + " " + infoSnapshot.bindApplicationDelayMs;
        ((android.os.IOplusJankMonitorExt) system.ext.loader.core.ExtLoader.type(android.os.IOplusJankMonitorExt.class).create()).stopLaunchTrace(infoSnapshot.launchedActivityShortComponentName, infoSnapshot.launchedActivityProcessRunning, infoSnapshot.processRecord != null ? infoSnapshot.processRecord.getPid() : -1, android.os.SystemClock.uptimeMillis(), infoSnapshot.launchedActivityLaunchedFromPackage, stageInfo);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$logAppTransitionFinished$2(com.android.server.wm.ActivityMetricsLogger.TransitionInfo info, long uptimeNs, int transitionDelay, com.android.server.wm.ActivityMetricsLogger.TransitionInfoSnapshot infoSnapshot, boolean isHibernating, int processState, int processOomAdj, boolean isOpaque) {
        if (info.isInterestingToLoggerAndObserver()) {
            logAppTransition(uptimeNs, transitionDelay, infoSnapshot, isHibernating, processState, processOomAdj);
        }
        if (info.mIsInTaskActivityStart) {
            logInTaskActivityStart(infoSnapshot, isOpaque, transitionDelay);
        }
        if (infoSnapshot.isInterestedToEventLog()) {
            logAppDisplayed(infoSnapshot);
        }
    }

    private void logAppTransition(long j, int i, com.android.server.wm.ActivityMetricsLogger.TransitionInfoSnapshot transitionInfoSnapshot, boolean z, int i2, int i3) {
        boolean z2;
        boolean zIsIncrementalLoading;
        int i4;
        android.metrics.LogMaker logMaker = new android.metrics.LogMaker(com.android.internal.util.FrameworkStatsLog.HOTWORD_EGRESS_SIZE_ATOM_REPORTED);
        logMaker.setPackageName(transitionInfoSnapshot.packageName);
        logMaker.setType(transitionInfoSnapshot.type);
        logMaker.addTaggedData(com.android.bluetooth.BluetoothStatsLog.LE_RADIO_SCAN_STOPPED, transitionInfoSnapshot.launchedActivityName);
        boolean zIsInstantApp = transitionInfoSnapshot.applicationInfo.isInstantApp();
        if (transitionInfoSnapshot.launchedActivityLaunchedFromPackage != null) {
            logMaker.addTaggedData(904, transitionInfoSnapshot.launchedActivityLaunchedFromPackage);
        }
        java.lang.String str = transitionInfoSnapshot.launchedActivityLaunchToken;
        if (str != null) {
            logMaker.addTaggedData(903, str);
        }
        logMaker.addTaggedData(905, java.lang.Integer.valueOf(zIsInstantApp ? 1 : 0));
        logMaker.addTaggedData(325, java.lang.Long.valueOf(java.util.concurrent.TimeUnit.NANOSECONDS.toSeconds(j)));
        logMaker.addTaggedData(319, java.lang.Integer.valueOf(i));
        logMaker.setSubtype(transitionInfoSnapshot.reason);
        if (transitionInfoSnapshot.startingWindowDelayMs != -1) {
            logMaker.addTaggedData(321, java.lang.Integer.valueOf(transitionInfoSnapshot.startingWindowDelayMs));
        }
        if (transitionInfoSnapshot.bindApplicationDelayMs != -1) {
            logMaker.addTaggedData(945, java.lang.Integer.valueOf(transitionInfoSnapshot.bindApplicationDelayMs));
        }
        logMaker.addTaggedData(322, java.lang.Integer.valueOf(transitionInfoSnapshot.windowsDrawnDelayMs));
        android.content.pm.dex.PackageOptimizationInfo packageOptimizationInfo = transitionInfoSnapshot.getPackageOptimizationInfo(getArtManagerInternal());
        logMaker.addTaggedData(1321, java.lang.Integer.valueOf(packageOptimizationInfo.getCompilationReason()));
        logMaker.addTaggedData(1320, java.lang.Integer.valueOf(packageOptimizationInfo.getCompilationFilter()));
        this.mMetricsLogger.write(logMaker);
        java.lang.String codePath = transitionInfoSnapshot.applicationInfo.getCodePath();
        if (codePath != null && android.os.incremental.IncrementalManager.isIncrementalPath(codePath)) {
            z2 = true;
            zIsIncrementalLoading = isIncrementalLoading(transitionInfoSnapshot.packageName, transitionInfoSnapshot.userId);
        } else {
            z2 = false;
            zIsIncrementalLoading = false;
        }
        boolean zWasStoppedNeedsLogging = wasStoppedNeedsLogging(transitionInfoSnapshot);
        if (zWasStoppedNeedsLogging) {
            i4 = 2;
        } else {
            i4 = 1;
        }
        boolean zWasFirstLaunch = wasFirstLaunch(transitionInfoSnapshot);
        com.android.internal.util.FrameworkStatsLog.write(48, transitionInfoSnapshot.applicationInfo.uid, transitionInfoSnapshot.packageName, getAppStartTransitionType(transitionInfoSnapshot.type, transitionInfoSnapshot.relaunched), transitionInfoSnapshot.launchedActivityName, transitionInfoSnapshot.launchedActivityLaunchedFromPackage, zIsInstantApp, 0L, transitionInfoSnapshot.reason, i, transitionInfoSnapshot.startingWindowDelayMs, transitionInfoSnapshot.bindApplicationDelayMs, transitionInfoSnapshot.windowsDrawnDelayMs, str, packageOptimizationInfo.getCompilationReason(), packageOptimizationInfo.getCompilationFilter(), transitionInfoSnapshot.sourceType, transitionInfoSnapshot.sourceEventDelayMs, z, z2, zIsIncrementalLoading, transitionInfoSnapshot.launchedActivityName.hashCode(), java.util.concurrent.TimeUnit.NANOSECONDS.toMillis(transitionInfoSnapshot.timestampNs), i2, i3, i4, false, zWasFirstLaunch, 0L, transitionInfoSnapshot.multiWindowLaunchType);
        if (transitionInfoSnapshot.processRecord != null) {
            transitionInfoSnapshot.processRecord.setWasStoppedLogged(true);
        }
        if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_METRICS) {
            android.util.Slog.i(TAG, java.lang.String.format("APP_START_OCCURRED(%s, %s, %s, %s, %s, wasStopped=%b, firstLaunch=%b)", java.lang.Integer.valueOf(transitionInfoSnapshot.applicationInfo.uid), transitionInfoSnapshot.packageName, java.lang.Integer.valueOf(getAppStartTransitionType(transitionInfoSnapshot.type, transitionInfoSnapshot.relaunched)), transitionInfoSnapshot.launchedActivityName, transitionInfoSnapshot.launchedActivityLaunchedFromPackage, java.lang.Boolean.valueOf(zWasStoppedNeedsLogging), java.lang.Boolean.valueOf(zWasFirstLaunch)));
        }
        logAppStartMemoryStateCapture(transitionInfoSnapshot);
    }

    private boolean isIncrementalLoading(java.lang.String packageName, int userId) {
        android.content.pm.IncrementalStatesInfo info = this.mSupervisor.mService.getPackageManagerInternalLocked().getIncrementalStatesInfo(packageName, 0, userId);
        return info != null && info.isLoading();
    }

    void logInTaskActivityStart(com.android.server.wm.ActivityMetricsLogger.TransitionInfoSnapshot info, boolean isOpaque, int transitionDelayMs) {
        if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_METRICS) {
            android.util.Slog.i(TAG, "IN_TASK_ACTIVITY_STARTED " + info.launchedActivityName + " transitionDelayMs=" + transitionDelayMs + "ms");
        }
        com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.IN_TASK_ACTIVITY_STARTED, info.applicationInfo.uid, getAppStartTransitionType(info.type, info.relaunched), isOpaque, transitionDelayMs, info.windowsDrawnDelayMs, java.util.concurrent.TimeUnit.NANOSECONDS.toMillis(info.timestampNs));
    }

    private void logAppDisplayed(com.android.server.wm.ActivityMetricsLogger.TransitionInfoSnapshot info) {
        this.mActivityMetricsLoggerSocExt.onNotifyAppTTId(info.packageName, info.windowsDrawnDelayMs, info.getLaunchState());
        android.util.EventLog.writeEvent(com.android.server.wm.EventLogTags.WM_ACTIVITY_LAUNCH_TIME, java.lang.Integer.valueOf(info.userId), java.lang.Integer.valueOf(info.activityRecordIdHashCode), info.launchedActivityShortComponentName, java.lang.Integer.valueOf(info.windowsDrawnDelayMs));
        java.lang.StringBuilder sb = this.mStringBuilder;
        sb.setLength(0);
        sb.append("Displayed ");
        sb.append(info.launchedActivityShortComponentName);
        sb.append(" for user ");
        sb.append(info.userId);
        sb.append(": ");
        android.util.TimeUtils.formatDuration(info.windowsDrawnDelayMs, sb);
        android.util.Log.i(TAG, sb.toString());
        this.mActivityMetricsLoggerSocExt.hookLogAppDisplayed(info.processRecord, info.packageName, info.windowsDrawnDelayMs, info.launchedActivityShortComponentName);
        ((com.android.server.pm.IPackageManagerServiceUtilsExt) system.ext.loader.core.ExtLoader.type(com.android.server.pm.IPackageManagerServiceUtilsExt.class).create()).addBootEvent("AP_Launch: " + info.launchedActivityShortComponentName + " " + info.windowsDrawnDelayMs + "ms");
    }

    private void logRecentsAnimationLatency(com.android.server.wm.ActivityMetricsLogger.TransitionInfo info) {
        final int duration = info.mSourceEventDelayMs + info.mWindowsDrawnDelayMs;
        final com.android.server.wm.ActivityRecord r = info.mLastLaunchedActivity;
        final long lastTopLossTime = r.topResumedStateLossTime;
        final com.android.server.wm.WindowManagerService wm = this.mSupervisor.mService.mWindowManager;
        final java.lang.Object controller = wm.getRecentsAnimationController();
        this.mLoggerHandler.postDelayed(new java.lang.Runnable() { // from class: com.android.server.wm.ActivityMetricsLogger$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                com.android.server.wm.ActivityMetricsLogger.lambda$logRecentsAnimationLatency$3(lastTopLossTime, r, controller, wm, duration);
            }
        }, LATENCY_TRACKER_RECENTS_DELAY_MS);
    }

    static /* synthetic */ void lambda$logRecentsAnimationLatency$3(long lastTopLossTime, com.android.server.wm.ActivityRecord r, java.lang.Object controller, com.android.server.wm.WindowManagerService wm, int duration) {
        if (lastTopLossTime != r.topResumedStateLossTime || controller != wm.getRecentsAnimationController()) {
            return;
        }
        wm.mLatencyTracker.logAction(8, duration);
    }

    private static int getAppStartTransitionType(int tronType, boolean relaunched) {
        if (tronType == 7) {
            return 3;
        }
        if (tronType == 8) {
            return 1;
        }
        if (tronType == 9) {
            if (relaunched) {
                return 4;
            }
            return 2;
        }
        return 0;
    }

    com.android.server.wm.ActivityMetricsLogger.TransitionInfoSnapshot notifyFullyDrawn(final com.android.server.wm.ActivityRecord r, final boolean restoredFromBundle) {
        long startupTimeMs;
        final com.android.server.wm.ActivityMetricsLogger.TransitionInfo info = this.mLastTransitionInfo.get(r);
        if (info == null) {
            return null;
        }
        if (!info.mIsDrawn && info.mPendingFullyDrawn == null) {
            info.mPendingFullyDrawn = new java.lang.Runnable() { // from class: com.android.server.wm.ActivityMetricsLogger$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$notifyFullyDrawn$4(r, restoredFromBundle, info);
                }
            };
            return null;
        }
        long currentTimestampNs = android.os.SystemClock.uptimeNanos();
        if (info.mPendingFullyDrawn != null) {
            startupTimeMs = info.mWindowsDrawnDelayMs;
        } else {
            startupTimeMs = java.util.concurrent.TimeUnit.NANOSECONDS.toMillis(currentTimestampNs - info.mLaunchingState.mStartUptimeNs);
        }
        final com.android.server.wm.ActivityMetricsLogger.TransitionInfoSnapshot infoSnapshot = new com.android.server.wm.ActivityMetricsLogger.TransitionInfoSnapshot(info, r, (int) startupTimeMs);
        if (infoSnapshot.isInterestedToEventLog()) {
            this.mLoggerHandler.post(new java.lang.Runnable() { // from class: com.android.server.wm.ActivityMetricsLogger$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$notifyFullyDrawn$5(infoSnapshot);
                }
            });
        }
        this.mLastTransitionInfo.remove(r);
        if (!info.isInterestingToLoggerAndObserver()) {
            return infoSnapshot;
        }
        android.os.Trace.traceBegin(64L, "ActivityManager:ReportingFullyDrawn " + info.mLastLaunchedActivity.packageName);
        this.mLoggerHandler.post(new java.lang.Runnable() { // from class: com.android.server.wm.ActivityMetricsLogger$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$notifyFullyDrawn$6(infoSnapshot, restoredFromBundle, info);
            }
        });
        android.os.Trace.traceEnd(64L);
        launchObserverNotifyReportFullyDrawn(info, currentTimestampNs);
        return infoSnapshot;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$notifyFullyDrawn$4(com.android.server.wm.ActivityRecord r, boolean restoredFromBundle, com.android.server.wm.ActivityMetricsLogger.TransitionInfo info) {
        notifyFullyDrawn(r, restoredFromBundle);
        info.mPendingFullyDrawn = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$notifyFullyDrawn$6(com.android.server.wm.ActivityMetricsLogger.TransitionInfoSnapshot infoSnapshot, boolean restoredFromBundle, com.android.server.wm.ActivityMetricsLogger.TransitionInfo info) {
        logAppFullyDrawnMetrics(infoSnapshot, restoredFromBundle, info.mProcessRunning);
    }

    private void logAppFullyDrawnMetrics(com.android.server.wm.ActivityMetricsLogger.TransitionInfoSnapshot transitionInfoSnapshot, boolean z, boolean z2) {
        int i;
        int i2;
        android.metrics.LogMaker logMaker = new android.metrics.LogMaker(1090);
        logMaker.setPackageName(transitionInfoSnapshot.packageName);
        logMaker.addTaggedData(com.android.bluetooth.BluetoothStatsLog.LE_RADIO_SCAN_STOPPED, transitionInfoSnapshot.launchedActivityName);
        logMaker.addTaggedData(1091, java.lang.Long.valueOf(transitionInfoSnapshot.windowsFullyDrawnDelayMs));
        if (z) {
            i = 13;
        } else {
            i = 12;
        }
        logMaker.setType(i);
        logMaker.addTaggedData(com.android.internal.util.FrameworkStatsLog.APP_BACKGROUND_RESTRICTIONS_INFO__EXEMPTION_REASON__REASON_ACTIVE_DEVICE_ADMIN, java.lang.Integer.valueOf(z2 ? 1 : 0));
        this.mMetricsLogger.write(logMaker);
        android.content.pm.dex.PackageOptimizationInfo packageOptimizationInfo = transitionInfoSnapshot.getPackageOptimizationInfo(getArtManagerInternal());
        boolean z3 = false;
        boolean zIsIncrementalLoading = false;
        java.lang.String codePath = transitionInfoSnapshot.applicationInfo.getCodePath();
        if (codePath != null && android.os.incremental.IncrementalManager.isIncrementalPath(codePath)) {
            z3 = true;
            zIsIncrementalLoading = isIncrementalLoading(transitionInfoSnapshot.packageName, transitionInfoSnapshot.userId);
        }
        int i3 = transitionInfoSnapshot.applicationInfo.uid;
        java.lang.String str = transitionInfoSnapshot.packageName;
        if (z) {
            i2 = 1;
        } else {
            i2 = 2;
        }
        com.android.internal.util.FrameworkStatsLog.write(50, i3, str, i2, transitionInfoSnapshot.launchedActivityName, z2, transitionInfoSnapshot.windowsFullyDrawnDelayMs, packageOptimizationInfo.getCompilationReason(), packageOptimizationInfo.getCompilationFilter(), transitionInfoSnapshot.sourceType, transitionInfoSnapshot.sourceEventDelayMs, z3, zIsIncrementalLoading, transitionInfoSnapshot.launchedActivityName.hashCode(), java.util.concurrent.TimeUnit.NANOSECONDS.toMillis(transitionInfoSnapshot.timestampNs));
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: logAppFullyDrawn, reason: merged with bridge method [inline-methods] */
    public void lambda$notifyFullyDrawn$5(com.android.server.wm.ActivityMetricsLogger.TransitionInfoSnapshot info) {
        java.lang.StringBuilder sb = this.mStringBuilder;
        sb.setLength(0);
        sb.append("Fully drawn ");
        sb.append(info.launchedActivityShortComponentName);
        sb.append(": ");
        android.util.TimeUtils.formatDuration(info.windowsFullyDrawnDelayMs, sb);
        android.util.Log.i(TAG, sb.toString());
    }

    void logAbortedBgActivityStart(android.content.Intent intent, com.android.server.wm.WindowProcessController windowProcessController, int i, java.lang.String str, int i2, boolean z, int i3, int i4, boolean z2, boolean z3) {
        long jElapsedRealtime = android.os.SystemClock.elapsedRealtime();
        long jUptimeMillis = android.os.SystemClock.uptimeMillis();
        android.metrics.LogMaker logMaker = new android.metrics.LogMaker(1513);
        logMaker.setTimestamp(java.lang.System.currentTimeMillis());
        logMaker.addTaggedData(1514, java.lang.Integer.valueOf(i));
        logMaker.addTaggedData(1515, str);
        logMaker.addTaggedData(1516, java.lang.Integer.valueOf(android.app.ActivityManager.processStateAmToProto(i2)));
        logMaker.addTaggedData(1517, java.lang.Integer.valueOf(z ? 1 : 0));
        logMaker.addTaggedData(1518, java.lang.Integer.valueOf(i3));
        logMaker.addTaggedData(1519, java.lang.Integer.valueOf(android.app.ActivityManager.processStateAmToProto(i4)));
        logMaker.addTaggedData(1520, java.lang.Integer.valueOf(z2 ? 1 : 0));
        logMaker.addTaggedData(1527, java.lang.Integer.valueOf(z3 ? 1 : 0));
        if (intent != null) {
            logMaker.addTaggedData(1528, intent.getAction());
            android.content.ComponentName component = intent.getComponent();
            if (component != null) {
                logMaker.addTaggedData(1526, component.flattenToShortString());
            }
        }
        if (windowProcessController != null) {
            logMaker.addTaggedData(1529, windowProcessController.mName);
            logMaker.addTaggedData(1530, java.lang.Integer.valueOf(android.app.ActivityManager.processStateAmToProto(windowProcessController.getCurrentProcState())));
            logMaker.addTaggedData(1531, java.lang.Integer.valueOf(windowProcessController.hasClientActivities() ? 1 : 0));
            logMaker.addTaggedData(1532, java.lang.Integer.valueOf(windowProcessController.hasForegroundServices() ? 1 : 0));
            logMaker.addTaggedData(1533, java.lang.Integer.valueOf(windowProcessController.hasForegroundActivities() ? 1 : 0));
            logMaker.addTaggedData(1534, java.lang.Integer.valueOf(windowProcessController.hasTopUi() ? 1 : 0));
            logMaker.addTaggedData(1535, java.lang.Integer.valueOf(windowProcessController.hasOverlayUi() ? 1 : 0));
            logMaker.addTaggedData(1536, java.lang.Integer.valueOf(windowProcessController.hasPendingUiClean() ? 1 : 0));
            if (windowProcessController.getInteractionEventTime() != 0) {
                logMaker.addTaggedData(com.android.server.usb.descriptors.UsbTerminalTypes.TERMINAL_EXTERN_ANALOG, java.lang.Long.valueOf(jElapsedRealtime - windowProcessController.getInteractionEventTime()));
            }
            if (windowProcessController.getFgInteractionTime() != 0) {
                logMaker.addTaggedData(com.android.server.usb.descriptors.UsbTerminalTypes.TERMINAL_EXTERN_DIGITAL, java.lang.Long.valueOf(jElapsedRealtime - windowProcessController.getFgInteractionTime()));
            }
            if (windowProcessController.getWhenUnimportant() != 0) {
                logMaker.addTaggedData(1539, java.lang.Long.valueOf(jUptimeMillis - windowProcessController.getWhenUnimportant()));
            }
        }
        this.mMetricsLogger.write(logMaker);
    }

    private void logAppStartMemoryStateCapture(com.android.server.wm.ActivityMetricsLogger.TransitionInfoSnapshot info) {
        if (info.processRecord == null) {
            if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_METRICS) {
                android.util.Slog.i(TAG, "logAppStartMemoryStateCapture processRecord null");
                return;
            }
            return;
        }
        int pid = info.processRecord.getPid();
        int uid = info.applicationInfo.uid;
        com.android.server.am.MemoryStatUtil.MemoryStat memoryStat = com.android.server.am.MemoryStatUtil.readMemoryStatFromFilesystem(uid, pid);
        if (memoryStat != null) {
            com.android.internal.util.FrameworkStatsLog.write(55, uid, info.processName, info.launchedActivityName, memoryStat.pgfault, memoryStat.pgmajfault, memoryStat.rssInBytes, memoryStat.cacheInBytes, memoryStat.swapInBytes);
            java.lang.String memoryState = memoryStat.pgfault + " " + memoryStat.pgmajfault + " " + memoryStat.rssInBytes + " " + memoryStat.cacheInBytes + " " + memoryStat.swapInBytes;
            ((android.os.IOplusJankMonitorExt) system.ext.loader.core.ExtLoader.type(android.os.IOplusJankMonitorExt.class).create()).appStartMemoryStateCapture(info.launchedActivityShortComponentName, info.processRecord.getPid(), memoryState);
        } else if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_METRICS) {
            android.util.Slog.i(TAG, "logAppStartMemoryStateCapture memoryStat null");
        }
    }

    void logAppCompatState(com.android.server.wm.ActivityRecord activity) {
        int packageUid = activity.info.applicationInfo.uid;
        int state = activity.getAppCompatState();
        if (!this.mPackageUidToCompatStateInfo.contains(packageUid)) {
            this.mPackageUidToCompatStateInfo.put(packageUid, new com.android.server.wm.ActivityMetricsLogger.PackageCompatStateInfo());
        }
        com.android.server.wm.ActivityMetricsLogger.PackageCompatStateInfo compatStateInfo = this.mPackageUidToCompatStateInfo.get(packageUid);
        int lastLoggedState = compatStateInfo.mLastLoggedState;
        com.android.server.wm.ActivityRecord lastLoggedActivity = compatStateInfo.mLastLoggedActivity;
        boolean isVisible = state != 1;
        java.util.ArrayList<com.android.server.wm.ActivityRecord> visibleActivities = compatStateInfo.mVisibleActivities;
        if (isVisible && !visibleActivities.contains(activity)) {
            visibleActivities.add(activity);
        } else if (!isVisible) {
            visibleActivities.remove(activity);
            if (visibleActivities.isEmpty()) {
                this.mPackageUidToCompatStateInfo.remove(packageUid);
            }
        }
        if (state == lastLoggedState) {
            return;
        }
        if (!isVisible && !visibleActivities.isEmpty()) {
            if (lastLoggedActivity == null || activity == lastLoggedActivity) {
                findAppCompatStateToLog(compatStateInfo, packageUid);
                return;
            }
            return;
        }
        if (lastLoggedActivity != null && activity != lastLoggedActivity && lastLoggedState != 1 && lastLoggedState != 2) {
            return;
        }
        logAppCompatStateInternal(activity, state, compatStateInfo);
    }

    private void findAppCompatStateToLog(com.android.server.wm.ActivityMetricsLogger.PackageCompatStateInfo compatStateInfo, int packageUid) {
        java.util.ArrayList<com.android.server.wm.ActivityRecord> visibleActivities = compatStateInfo.mVisibleActivities;
        int lastLoggedState = compatStateInfo.mLastLoggedState;
        com.android.server.wm.ActivityRecord activityToLog = null;
        int stateToLog = 1;
        for (int i = 0; i < visibleActivities.size(); i++) {
            com.android.server.wm.ActivityRecord activity = visibleActivities.get(i);
            int state = activity.getAppCompatState();
            if (state == lastLoggedState) {
                compatStateInfo.mLastLoggedActivity = activity;
                return;
            }
            if (state == 1) {
                android.util.Slog.w(TAG, "Visible activity with NOT_VISIBLE App Compat state for package UID: " + packageUid);
            } else if (stateToLog == 1 || (stateToLog == 2 && state != 2)) {
                activityToLog = activity;
                stateToLog = state;
            }
        }
        if (activityToLog != null && stateToLog != 1) {
            logAppCompatStateInternal(activityToLog, stateToLog, compatStateInfo);
        }
    }

    private static boolean isAppCompateStateChangedToLetterboxed(int state) {
        return state == 5 || state == 4 || state == 3;
    }

    private void logAppCompatStateInternal(com.android.server.wm.ActivityRecord activity, int state, com.android.server.wm.ActivityMetricsLogger.PackageCompatStateInfo compatStateInfo) {
        compatStateInfo.mLastLoggedState = state;
        compatStateInfo.mLastLoggedActivity = activity;
        int packageUid = activity.info.applicationInfo.uid;
        int positionToLog = 1;
        if (isAppCompateStateChangedToLetterboxed(state)) {
            positionToLog = activity.mLetterboxUiController.getLetterboxPositionForLogging();
        }
        com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.APP_COMPAT_STATE_CHANGED, packageUid, state, positionToLog);
        if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_METRICS) {
            android.util.Slog.i(TAG, java.lang.String.format("APP_COMPAT_STATE_CHANGED(%s, %s, %s)", java.lang.Integer.valueOf(packageUid), java.lang.Integer.valueOf(state), java.lang.Integer.valueOf(positionToLog)));
        }
    }

    void logLetterboxPositionChange(com.android.server.wm.ActivityRecord activity, int position) {
        int packageUid = activity.info.applicationInfo.uid;
        com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.LETTERBOX_POSITION_CHANGED, packageUid, position);
        if (!this.mPackageUidToCompatStateInfo.contains(packageUid)) {
            return;
        }
        com.android.server.wm.ActivityMetricsLogger.PackageCompatStateInfo compatStateInfo = this.mPackageUidToCompatStateInfo.get(packageUid);
        com.android.server.wm.ActivityRecord lastLoggedActivity = compatStateInfo.mLastLoggedActivity;
        if (activity != lastLoggedActivity) {
            return;
        }
        int state = activity.getAppCompatState();
        logAppCompatStateInternal(activity, state, compatStateInfo);
        if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_METRICS) {
            android.util.Slog.i(TAG, java.lang.String.format("LETTERBOX_POSITION_CHANGED(%s, %s)", java.lang.Integer.valueOf(packageUid), java.lang.Integer.valueOf(position)));
        }
    }

    void logCameraCompatControlAppearedEventReported(int state, int packageUid) {
        switch (state) {
            case 0:
                break;
            case 1:
                logCameraCompatControlEventReported(1, packageUid);
                break;
            case 2:
                logCameraCompatControlEventReported(2, packageUid);
                break;
            default:
                android.util.Slog.w(TAG, "Unexpected state in logCameraCompatControlAppearedEventReported: " + state);
                break;
        }
    }

    void logCameraCompatControlClickedEventReported(int state, int packageUid) {
        switch (state) {
            case 1:
                logCameraCompatControlEventReported(4, packageUid);
                break;
            case 2:
                logCameraCompatControlEventReported(3, packageUid);
                break;
            case 3:
                logCameraCompatControlEventReported(5, packageUid);
                break;
            default:
                android.util.Slog.w(TAG, "Unexpected state in logCameraCompatControlAppearedEventReported: " + state);
                break;
        }
    }

    private void logCameraCompatControlEventReported(int event, int packageUid) {
        com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.CAMERA_COMPAT_CONTROL_EVENT_REPORTED, packageUid, event);
        if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_METRICS) {
            android.util.Slog.i(TAG, java.lang.String.format("CAMERA_COMPAT_CONTROL_EVENT_REPORTED(%s, %s)", java.lang.Integer.valueOf(packageUid), java.lang.Integer.valueOf(event)));
        }
    }

    private android.content.pm.dex.ArtManagerInternal getArtManagerInternal() {
        if (this.mArtManagerInternal == null) {
            this.mArtManagerInternal = (android.content.pm.dex.ArtManagerInternal) com.android.server.LocalServices.getService(android.content.pm.dex.ArtManagerInternal.class);
        }
        return this.mArtManagerInternal;
    }

    private void startLaunchTrace(com.android.server.wm.ActivityMetricsLogger.TransitionInfo info) {
        if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_METRICS) {
            android.util.Slog.i(TAG, "startLaunchTrace " + info);
        }
        if (info.mLaunchingState.mTraceName == null) {
            return;
        }
        info.mLaunchTraceName = "launching: " + info.mLastLaunchedActivity.packageName;
        android.os.Trace.asyncTraceBegin(64L, info.mLaunchTraceName, (int) info.mLaunchingState.mStartRealtimeNs);
    }

    private void stopLaunchTrace(com.android.server.wm.ActivityMetricsLogger.TransitionInfo info) {
        if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_METRICS) {
            android.util.Slog.i(TAG, "stopLaunchTrace " + info);
        }
        if (info.mLaunchTraceName == null) {
            return;
        }
        android.os.Trace.asyncTraceEnd(64L, info.mLaunchTraceName, (int) info.mLaunchingState.mStartRealtimeNs);
        android.os.Trace.traceBegin(64L, "perf_launching: " + info.mLastLaunchedActivity.packageName + "#" + info.mLastLaunchedActivity.getPid() + "#" + info.mCallingPid);
        android.os.Trace.traceEnd(64L);
        info.mLaunchTraceName = null;
    }

    public com.android.server.wm.ActivityMetricsLaunchObserverRegistry getLaunchObserverRegistry() {
        return this.mLaunchObserver;
    }

    private void launchObserverNotifyIntentStarted(android.content.Intent intent, long timestampNs) {
        android.os.Trace.traceBegin(64L, "MetricsLogger:launchObserverNotifyIntentStarted");
        this.mLaunchObserver.onIntentStarted(intent, timestampNs);
        android.os.Trace.traceEnd(64L);
    }

    private void launchObserverNotifyIntentFailed(long id) {
        android.os.Trace.traceBegin(64L, "MetricsLogger:launchObserverNotifyIntentFailed");
        this.mLaunchObserver.onIntentFailed(id);
        android.os.Trace.traceEnd(64L);
    }

    private void launchObserverNotifyActivityLaunched(com.android.server.wm.ActivityMetricsLogger.TransitionInfo info) {
        android.os.Trace.traceBegin(64L, "MetricsLogger:launchObserverNotifyActivityLaunched");
        int temperature = convertTransitionTypeToLaunchObserverTemperature(info.mTransitionType);
        this.mLaunchObserver.onActivityLaunched(info.mLaunchingState.mStartUptimeNs, info.mLastLaunchedActivity.mActivityComponent, temperature, info.mLastLaunchedActivity.mUserId);
        mActivityMetricsLoggerExt.notifyActivityLaunched(info.mLastLaunchedActivity.mActivityComponent, info.mLastLaunchedActivity.mUserId, temperature);
        android.os.Trace.traceEnd(64L);
    }

    private void launchObserverNotifyReportFullyDrawn(com.android.server.wm.ActivityMetricsLogger.TransitionInfo info, long timestampNs) {
        android.os.Trace.traceBegin(64L, "MetricsLogger:launchObserverNotifyReportFullyDrawn");
        this.mLaunchObserver.onReportFullyDrawn(info.mLaunchingState.mStartUptimeNs, timestampNs);
        android.os.Trace.traceEnd(64L);
    }

    private void launchObserverNotifyActivityLaunchCancelled(com.android.server.wm.ActivityMetricsLogger.TransitionInfo info) {
        android.os.Trace.traceBegin(64L, "MetricsLogger:launchObserverNotifyActivityLaunchCancelled");
        this.mLaunchObserver.onActivityLaunchCancelled(info.mLaunchingState.mStartUptimeNs);
        android.os.Trace.traceEnd(64L);
    }

    private void launchObserverNotifyActivityLaunchFinished(com.android.server.wm.ActivityMetricsLogger.TransitionInfo info, long timestampNs) {
        android.os.Trace.traceBegin(64L, "MetricsLogger:launchObserverNotifyActivityLaunchFinished");
        this.mLaunchObserver.onActivityLaunchFinished(info.mLaunchingState.mStartUptimeNs, info.mLastLaunchedActivity.mActivityComponent, timestampNs, info.mLastLaunchedActivity.launchMode);
        android.os.Trace.traceEnd(64L);
    }

    private static int convertTransitionTypeToLaunchObserverTemperature(int transitionType) {
        switch (transitionType) {
            case 7:
                return 1;
            case 8:
                return 2;
            case 9:
                return 3;
            default:
                return -1;
        }
    }

    private boolean wasStoppedNeedsLogging(com.android.server.wm.ActivityMetricsLogger.TransitionInfoSnapshot info) {
        return info.processRecord != null ? (info.processRecord.wasForceStopped() || info.processRecord.wasFirstLaunch()) && !info.processRecord.getWasStoppedLogged() : (info.applicationInfo.flags & 2097152) != 0;
    }

    private boolean wasFirstLaunch(com.android.server.wm.ActivityMetricsLogger.TransitionInfoSnapshot info) {
        if (info.processRecord != null) {
            return info.processRecord.wasFirstLaunch() && !info.processRecord.getWasStoppedLogged();
        }
        try {
            return !this.mSupervisor.mService.getPackageManagerInternalLocked().wasPackageEverLaunched(info.packageName, info.userId);
        } catch (java.lang.Exception e) {
            return true;
        }
    }
}
