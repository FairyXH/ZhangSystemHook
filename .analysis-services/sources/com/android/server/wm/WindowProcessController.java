package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public class WindowProcessController extends com.android.server.wm.ConfigurationContainer<com.android.server.wm.ConfigurationContainer> implements com.android.server.wm.ConfigurationContainerListener {
    private static final int ACTIVITY_STATE_FLAG_HAS_ACTIVITY_IN_VISIBLE_TASK = 4194304;
    private static final int ACTIVITY_STATE_FLAG_HAS_RESUMED = 2097152;
    private static final int ACTIVITY_STATE_FLAG_IS_PAUSING_OR_PAUSED = 131072;
    private static final int ACTIVITY_STATE_FLAG_IS_STOPPING = 262144;
    private static final int ACTIVITY_STATE_FLAG_IS_STOPPING_FINISHING = 524288;
    private static final int ACTIVITY_STATE_FLAG_IS_VISIBLE = 65536;
    private static final int ACTIVITY_STATE_FLAG_IS_WINDOW_VISIBLE = 1048576;
    private static final int ACTIVITY_STATE_FLAG_MASK_MIN_TASK_LAYER = 65535;
    static final int ANIMATING_REASON_LEGACY_RECENT_ANIMATION = 4;
    static final int ANIMATING_REASON_REMOTE_ANIMATION = 1;
    static final int ANIMATING_REASON_WAKEFULNESS_CHANGE = 2;
    private static final int CACHED_CONFIG_PROC_STATE = 16;
    private static final int MAX_RAPID_ACTIVITY_LAUNCH_COUNT = 200;
    private static final long RAPID_ACTIVITY_LAUNCH_MS = 500;
    private static final int REMOTE_ACTIVITY_FLAG_EMBEDDED_ACTIVITY = 2;
    private static final int REMOTE_ACTIVITY_FLAG_HOST_ACTIVITY = 1;
    private static final long RESET_RAPID_ACTIVITY_LAUNCH_MS = 1500;
    public static final int STOPPED_STATE_FIRST_LAUNCH = 1;
    public static final int STOPPED_STATE_FORCE_STOPPED = 2;
    public static final int STOPPED_STATE_NOT_STOPPED = 0;
    private int mAnimatingReasons;
    private final com.android.server.wm.ActivityTaskManagerService mAtm;
    private final com.android.server.wm.BackgroundLaunchProcessController mBgLaunchController;
    private com.android.server.wm.ActivityRecord mConfigActivityRecord;
    private volatile boolean mCrashing;
    private volatile int mCurSchedGroup;
    private volatile boolean mDebugging;
    private com.android.server.wm.DisplayArea mDisplayArea;
    private volatile long mFgInteractionTime;
    private volatile boolean mHasActivities;
    private volatile boolean mHasCachedConfiguration;
    private volatile boolean mHasClientActivities;
    private volatile boolean mHasForegroundServices;
    private volatile boolean mHasImeService;
    private volatile boolean mHasOverlayUi;
    private boolean mHasPendingConfigurationChange;
    private volatile boolean mHasRecentTasks;
    private volatile boolean mHasTopUi;
    private java.util.ArrayList<com.android.server.wm.ActivityRecord> mInactiveActivities;
    final android.content.pm.ApplicationInfo mInfo;
    private volatile boolean mInstrumenting;
    private volatile boolean mInstrumentingWithBackgroundActivityStartPrivileges;
    private volatile long mInteractionEventTime;
    private volatile boolean mIsActivityConfigOverrideAllowed;
    private volatile long mLastActivityFinishTime;
    private volatile long mLastActivityLaunchTime;
    private final com.android.server.wm.WindowProcessListener mListener;
    final java.lang.String mName;
    private volatile boolean mNotResponding;
    public final java.lang.Object mOwner;
    private int mPauseConfigurationDispatchCount;
    private volatile boolean mPendingUiClean;
    private volatile boolean mPerceptible;
    private volatile boolean mPersistent;
    public volatile int mPid;
    private int mRapidActivityLaunchCount;
    private android.util.ArrayMap<com.android.server.wm.ActivityRecord, int[]> mRemoteActivities;
    private volatile java.lang.String mRequiredAbi;
    private volatile int mStoppedState;
    private android.app.IApplicationThread mThread;
    public final int mUid;
    private final boolean mUseFifoUiScheduling;
    final int mUserId;
    private volatile boolean mUsingWrapper;
    int mVrThreadTid;
    private volatile boolean mWasStoppedLogged;
    private volatile long mWhenUnimportant;
    com.android.server.wm.Session mWindowSession;
    private static final java.lang.String TAG = "ActivityTaskManager";
    private static final java.lang.String TAG_RELEASE = TAG + com.android.server.wm.ActivityTaskManagerDebugConfig.POSTFIX_RELEASE;
    private static final java.lang.String TAG_CONFIGURATION = TAG + com.android.server.wm.ActivityTaskManagerDebugConfig.POSTFIX_CONFIGURATION;
    private static final boolean isAgingVersion = "1".equals(android.os.SystemProperties.get("persist.sys.agingtest", "0"));
    private final java.util.ArrayList<java.lang.String> mPkgList = new java.util.ArrayList<>(1);
    private volatile int mCurProcState = 20;
    private volatile int mRepProcState = 20;
    private volatile int mCurAdj = -10000;
    private volatile int mInstrumentationSourceUid = -1;
    private final java.util.ArrayList<com.android.server.wm.ActivityRecord> mActivities = new java.util.ArrayList<>();
    private final java.util.ArrayList<com.android.server.wm.Task> mRecentTasks = new java.util.ArrayList<>();
    private com.android.server.wm.ActivityRecord mPreQTopResumedActivity = null;
    private final android.content.res.Configuration mLastReportedConfiguration = new android.content.res.Configuration();
    private com.android.server.wm.IWindowProcessControllerWrapper mWindowProcessControllerWrapper = new com.android.server.wm.WindowProcessController.WindowProcessControllerWrapper();
    private int mLastTopActivityDeviceId = 0;
    private volatile int mActivityStateFlags = 65535;
    private com.android.server.wm.IWindowProcessControllerExt mWindProcessConExt = (com.android.server.wm.IWindowProcessControllerExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IWindowProcessControllerExt.class).base(this).create();

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @interface AnimatingReason {
    }

    public interface ComputeOomAdjCallback {
        void onOtherActivity();

        void onPausedActivity();

        void onStoppingActivity(boolean z);

        void onVisibleActivity();
    }

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface StoppedState {
    }

    public WindowProcessController(final com.android.server.wm.ActivityTaskManagerService atm, android.content.pm.ApplicationInfo info, java.lang.String name, int uid, int userId, java.lang.Object owner, com.android.server.wm.WindowProcessListener listener) {
        boolean z = true;
        this.mIsActivityConfigOverrideAllowed = true;
        this.mInfo = info;
        this.mName = name;
        this.mUid = uid;
        this.mUserId = userId;
        this.mOwner = owner;
        this.mListener = listener;
        this.mAtm = atm;
        java.util.Objects.requireNonNull(atm);
        this.mBgLaunchController = new com.android.server.wm.BackgroundLaunchProcessController(new java.util.function.IntPredicate() { // from class: com.android.server.wm.WindowProcessController$$ExternalSyntheticLambda12
            @Override // java.util.function.IntPredicate
            public final boolean test(int i) {
                return atm.hasActiveVisibleWindow(i);
            }
        }, atm.getBackgroundActivityStartCallback());
        boolean isSysUiPackage = info.packageName.equals(this.mAtm.getSysUiServiceComponentLocked().getPackageName());
        if (isSysUiPackage || android.os.UserHandle.getAppId(this.mUid) == 1000) {
            this.mIsActivityConfigOverrideAllowed = false;
        }
        if (!com.android.window.flags.Flags.fifoPriorityForMajorUiProcesses() || (!isSysUiPackage && !this.mAtm.isCallerRecents(uid))) {
            z = false;
        }
        this.mUseFifoUiScheduling = z;
        onConfigurationChanged(atm.getGlobalConfiguration());
        this.mAtm.mPackageConfigPersister.updateConfigIfNeeded(this, this.mUserId, this.mInfo.packageName);
    }

    public void setPid(int pid) {
        this.mPid = pid;
    }

    public int getPid() {
        return this.mPid;
    }

    public java.util.ArrayList<com.android.server.wm.ActivityRecord> getActivities() {
        java.util.ArrayList<com.android.server.wm.ActivityRecord> arrayList;
        synchronized (this.mAtm.mGlobalLockWithoutBoost) {
            arrayList = this.mActivities;
        }
        return arrayList;
    }

    public void setThread(android.app.IApplicationThread thread) {
        synchronized (this.mAtm.mGlobalLockWithoutBoost) {
            this.mThread = thread;
            if (thread != null) {
                setLastReportedConfiguration(getConfiguration());
            } else {
                this.mAtm.mVisibleActivityProcessTracker.removeProcess(this);
            }
        }
    }

    android.app.IApplicationThread getThread() {
        return this.mThread;
    }

    boolean hasThread() {
        return this.mThread != null;
    }

    public void setCurrentSchedulingGroup(int curSchedGroup) {
        this.mCurSchedGroup = curSchedGroup;
    }

    int getCurrentSchedulingGroup() {
        return this.mCurSchedGroup;
    }

    public void setCurrentProcState(int curProcState) {
        this.mCurProcState = curProcState;
    }

    int getCurrentProcState() {
        return this.mCurProcState;
    }

    public void setCurrentAdj(int curAdj) {
        this.mCurAdj = curAdj;
    }

    int getCurrentAdj() {
        return this.mCurAdj;
    }

    public void setReportedProcState(int repProcState) {
        android.app.servertransaction.ClientTransactionItem clientTransactionItemObtain;
        int prevProcState = this.mRepProcState;
        this.mRepProcState = repProcState;
        android.app.IApplicationThread thread = this.mThread;
        if (prevProcState >= 16 && repProcState < 16 && thread != null && this.mHasCachedConfiguration) {
            synchronized (this.mLastReportedConfiguration) {
                onConfigurationChangePreScheduled(this.mLastReportedConfiguration);
                clientTransactionItemObtain = android.app.servertransaction.ConfigurationChangeItem.obtain(this.mLastReportedConfiguration, this.mLastTopActivityDeviceId);
            }
            try {
                this.mAtm.getLifecycleManager().scheduleTransactionItemNow(thread, clientTransactionItemObtain);
            } catch (java.lang.Exception e) {
                android.util.Slog.e(TAG_CONFIGURATION, "Failed to schedule ConfigurationChangeItem=" + clientTransactionItemObtain + " owner=" + this.mOwner, e);
            }
        }
    }

    int getReportedProcState() {
        return this.mRepProcState;
    }

    public void setCrashing(boolean crashing) {
        this.mCrashing = crashing;
    }

    void handleAppCrash() {
        java.util.ArrayList<com.android.server.wm.ActivityRecord> activities = new java.util.ArrayList<>(this.mActivities);
        for (int i = activities.size() - 1; i >= 0; i--) {
            com.android.server.wm.ActivityRecord r = activities.get(i);
            android.util.Slog.w(TAG, "  Force finishing activity " + r.mActivityComponent.flattenToShortString());
            r.detachFromProcess();
            r.mDisplayContent.requestTransitionAndLegacyPrepare(2, 16);
            r.destroyIfPossible("handleAppCrashed");
        }
    }

    boolean isCrashing() {
        return this.mCrashing;
    }

    public void setNotResponding(boolean notResponding) {
        this.mNotResponding = notResponding;
    }

    boolean isNotResponding() {
        return this.mNotResponding;
    }

    public void setPersistent(boolean persistent) {
        this.mPersistent = persistent;
    }

    boolean isPersistent() {
        return this.mPersistent;
    }

    public void setHasForegroundServices(boolean hasForegroundServices) {
        this.mHasForegroundServices = hasForegroundServices;
    }

    boolean hasForegroundServices() {
        return this.mHasForegroundServices;
    }

    boolean hasForegroundActivities() {
        return this.mAtm.mTopApp == this || (this.mActivityStateFlags & 458752) != 0;
    }

    public void setHasClientActivities(boolean hasClientActivities) {
        this.mHasClientActivities = hasClientActivities;
    }

    boolean hasClientActivities() {
        return this.mHasClientActivities;
    }

    public void setHasTopUi(boolean hasTopUi) {
        this.mHasTopUi = hasTopUi;
    }

    boolean hasTopUi() {
        return this.mHasTopUi;
    }

    public void setHasOverlayUi(boolean hasOverlayUi) {
        this.mHasOverlayUi = hasOverlayUi;
    }

    boolean hasOverlayUi() {
        return this.mHasOverlayUi;
    }

    public void setPendingUiClean(boolean hasPendingUiClean) {
        this.mPendingUiClean = hasPendingUiClean;
    }

    boolean hasPendingUiClean() {
        return this.mPendingUiClean;
    }

    boolean registeredForDisplayAreaConfigChanges() {
        return this.mDisplayArea != null;
    }

    boolean registeredForActivityConfigChanges() {
        return this.mConfigActivityRecord != null;
    }

    void postPendingUiCleanMsg(boolean pendingUiClean) {
        android.os.Message m = com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.BiConsumer() { // from class: com.android.server.wm.WindowProcessController$$ExternalSyntheticLambda1
            @Override // java.util.function.BiConsumer
            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                ((com.android.server.wm.WindowProcessListener) obj).setPendingUiClean(((java.lang.Boolean) obj2).booleanValue());
            }
        }, this.mListener, java.lang.Boolean.valueOf(pendingUiClean));
        this.mAtm.mH.sendMessage(m);
    }

    public void setInteractionEventTime(long interactionEventTime) {
        this.mInteractionEventTime = interactionEventTime;
    }

    long getInteractionEventTime() {
        return this.mInteractionEventTime;
    }

    public void setFgInteractionTime(long fgInteractionTime) {
        this.mFgInteractionTime = fgInteractionTime;
    }

    long getFgInteractionTime() {
        return this.mFgInteractionTime;
    }

    public void setWhenUnimportant(long whenUnimportant) {
        this.mWhenUnimportant = whenUnimportant;
    }

    long getWhenUnimportant() {
        return this.mWhenUnimportant;
    }

    public void setRequiredAbi(java.lang.String requiredAbi) {
        this.mRequiredAbi = requiredAbi;
    }

    java.lang.String getRequiredAbi() {
        return this.mRequiredAbi;
    }

    com.android.server.wm.DisplayArea getDisplayArea() {
        return this.mDisplayArea;
    }

    public void setDebugging(boolean debugging) {
        this.mDebugging = debugging;
    }

    boolean isDebugging() {
        return this.mDebugging;
    }

    public void setUsingWrapper(boolean usingWrapper) {
        this.mUsingWrapper = usingWrapper;
    }

    boolean isUsingWrapper() {
        return this.mUsingWrapper;
    }

    boolean hasEverLaunchedActivity() {
        return this.mLastActivityLaunchTime > 0;
    }

    void setLastActivityLaunchTime(com.android.server.wm.ActivityRecord r) {
        long launchTime = r.lastLaunchTime;
        if (launchTime > this.mLastActivityLaunchTime) {
            updateRapidActivityLaunch(r, launchTime, this.mLastActivityLaunchTime);
            this.mLastActivityLaunchTime = launchTime;
        } else if (launchTime < this.mLastActivityLaunchTime) {
            android.util.Slog.w(TAG, "Tried to set launchTime (" + launchTime + ") < mLastActivityLaunchTime (" + this.mLastActivityLaunchTime + ")");
        }
    }

    void updateRapidActivityLaunch(com.android.server.wm.ActivityRecord r, long launchTime, long lastLaunchTime) {
        if (this.mInstrumenting || this.mDebugging || lastLaunchTime <= 0) {
            return;
        }
        com.android.server.wm.WindowProcessController caller = this.mAtm.mProcessMap.getProcess(r.launchedFromPid);
        if (caller != null && caller.mInstrumenting) {
            return;
        }
        long diff = launchTime - lastLaunchTime;
        if (diff < 500) {
            this.mRapidActivityLaunchCount++;
        } else if (diff >= RESET_RAPID_ACTIVITY_LAUNCH_MS) {
            this.mRapidActivityLaunchCount = 0;
        }
        if (this.mRapidActivityLaunchCount > 200) {
            this.mRapidActivityLaunchCount = 0;
            if (this.mWindProcessConExt.hookUpdateRapidActivityLaunchSkipApp(this.mName)) {
                return;
            }
            final com.android.server.wm.Task task = r.getTask();
            android.util.Slog.w(TAG, "Removing task " + task.mTaskId + " because of rapid activity launch");
            this.mAtm.mH.post(new java.lang.Runnable() { // from class: com.android.server.wm.WindowProcessController$$ExternalSyntheticLambda9
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$updateRapidActivityLaunch$0(task);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateRapidActivityLaunch$0(com.android.server.wm.Task task) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mAtm.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                task.removeImmediately("rapid-activity-launch");
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        this.mAtm.mAmInternal.killProcess(this.mName, this.mUid, "rapidActivityLaunch");
    }

    void setLastActivityFinishTimeIfNeeded(long finishTime) {
        if (finishTime <= this.mLastActivityFinishTime || !hasActivityInVisibleTask()) {
            return;
        }
        this.mLastActivityFinishTime = finishTime;
    }

    public void addOrUpdateBackgroundStartPrivileges(android.os.Binder entity, android.app.BackgroundStartPrivileges backgroundStartPrivileges) {
        java.util.Objects.requireNonNull(entity, "entity");
        java.util.Objects.requireNonNull(backgroundStartPrivileges, "backgroundStartPrivileges");
        com.android.internal.util.Preconditions.checkArgument(backgroundStartPrivileges.allowsAny(), "backgroundStartPrivileges does not allow anything");
        this.mBgLaunchController.addOrUpdateAllowBackgroundStartPrivileges(entity, backgroundStartPrivileges);
    }

    public void removeBackgroundStartPrivileges(android.os.Binder entity) {
        java.util.Objects.requireNonNull(entity, "entity");
        this.mBgLaunchController.removeAllowBackgroundStartPrivileges(entity);
    }

    public boolean areBackgroundFgsStartsAllowed() {
        return areBackgroundActivityStartsAllowed(this.mAtm.getBalAppSwitchesState(), true).allows();
    }

    com.android.server.wm.BackgroundActivityStartController.BalVerdict areBackgroundActivityStartsAllowed(int appSwitchState) {
        return areBackgroundActivityStartsAllowed(appSwitchState, false);
    }

    private com.android.server.wm.BackgroundActivityStartController.BalVerdict areBackgroundActivityStartsAllowed(int appSwitchState, boolean isCheckingForFgsStart) {
        return this.mBgLaunchController.areBackgroundActivityStartsAllowed(this.mPid, this.mUid, this.mInfo.packageName, appSwitchState, isCheckingForFgsStart, hasActivityInVisibleTask(), this.mInstrumentingWithBackgroundActivityStartPrivileges, this.mAtm.getLastStopAppSwitchesTime(), this.mLastActivityLaunchTime, this.mLastActivityFinishTime);
    }

    boolean canCloseSystemDialogsByToken() {
        return this.mBgLaunchController.canCloseSystemDialogsByToken(this.mUid);
    }

    public void clearBoundClientUids() {
        this.mBgLaunchController.clearBalOptInBoundClientUids();
    }

    public void addBoundClientUid(int clientUid, java.lang.String clientPackageName, long bindFlags) {
        this.mBgLaunchController.addBoundClientUid(clientUid, clientPackageName, bindFlags);
    }

    public void setInstrumenting(boolean instrumenting, int sourceUid, boolean hasBackgroundActivityStartPrivileges) {
        com.android.internal.util.Preconditions.checkArgument(instrumenting || sourceUid == -1);
        this.mInstrumenting = instrumenting;
        this.mInstrumentationSourceUid = sourceUid;
        this.mInstrumentingWithBackgroundActivityStartPrivileges = hasBackgroundActivityStartPrivileges;
    }

    boolean isInstrumenting() {
        return this.mInstrumenting;
    }

    int getInstrumentationSourceUid() {
        return this.mInstrumentationSourceUid;
    }

    public void setPerceptible(boolean perceptible) {
        this.mPerceptible = perceptible;
    }

    boolean isPerceptible() {
        return this.mPerceptible;
    }

    @Override // com.android.server.wm.ConfigurationContainer
    protected int getChildCount() {
        return 0;
    }

    @Override // com.android.server.wm.ConfigurationContainer
    protected com.android.server.wm.ConfigurationContainer getChildAt(int index) {
        return null;
    }

    @Override // com.android.server.wm.ConfigurationContainer
    protected com.android.server.wm.ConfigurationContainer getParent() {
        return this.mAtm.mRootWindowContainer;
    }

    public void addPackage(java.lang.String packageName) {
        synchronized (this.mPkgList) {
            if (!this.mPkgList.contains(packageName)) {
                this.mPkgList.add(packageName);
            }
        }
    }

    public void clearPackageList() {
        synchronized (this.mPkgList) {
            this.mPkgList.clear();
        }
    }

    boolean containsPackage(java.lang.String packageName) {
        boolean zContains;
        synchronized (this.mPkgList) {
            zContains = this.mPkgList.contains(packageName);
        }
        return zContains;
    }

    java.util.List<java.lang.String> getPackageList() {
        java.util.ArrayList arrayList;
        synchronized (this.mPkgList) {
            arrayList = new java.util.ArrayList(this.mPkgList);
        }
        return arrayList;
    }

    void addActivityIfNeeded(com.android.server.wm.ActivityRecord r) {
        setLastActivityLaunchTime(r);
        if (this.mActivities.contains(r)) {
            return;
        }
        this.mWindProcessConExt.handleAddActivity(this, this.mActivities.isEmpty());
        this.mActivities.add(r);
        this.mHasActivities = true;
        if (this.mInactiveActivities != null) {
            this.mInactiveActivities.remove(r);
        }
        updateActivityConfigurationListener();
    }

    void removeActivity(com.android.server.wm.ActivityRecord r, boolean keepAssociation) {
        if (keepAssociation) {
            if (this.mInactiveActivities == null) {
                this.mInactiveActivities = new java.util.ArrayList<>();
                this.mInactiveActivities.add(r);
            } else if (!this.mInactiveActivities.contains(r)) {
                this.mInactiveActivities.add(r);
            }
        } else if (this.mInactiveActivities != null) {
            this.mInactiveActivities.remove(r);
        }
        boolean hasActivityBeforeRemove = !this.mActivities.isEmpty();
        this.mActivities.remove(r);
        this.mHasActivities = !this.mActivities.isEmpty();
        updateActivityConfigurationListener();
        this.mWindProcessConExt.handleRemoveActivity(this, hasActivityBeforeRemove);
    }

    void clearActivities() {
        this.mInactiveActivities = null;
        this.mActivities.clear();
        this.mHasActivities = false;
        updateActivityConfigurationListener();
    }

    public boolean hasActivities() {
        return this.mHasActivities;
    }

    public boolean hasVisibleActivities() {
        return (this.mActivityStateFlags & 65536) != 0;
    }

    boolean hasActivityInVisibleTask() {
        return (this.mActivityStateFlags & 4194304) != 0;
    }

    public boolean hasActivitiesOrRecentTasks() {
        return this.mHasActivities || this.mHasRecentTasks;
    }

    com.android.server.wm.TaskDisplayArea getTopActivityDisplayArea() {
        if (this.mActivities.isEmpty()) {
            return null;
        }
        int lastIndex = this.mActivities.size() - 1;
        com.android.server.wm.ActivityRecord topRecord = this.mActivities.get(lastIndex);
        com.android.server.wm.TaskDisplayArea displayArea = topRecord.getDisplayArea();
        for (int index = lastIndex - 1; index >= 0; index--) {
            com.android.server.wm.ActivityRecord nextRecord = this.mActivities.get(index);
            com.android.server.wm.TaskDisplayArea nextDisplayArea = nextRecord.getDisplayArea();
            if (nextRecord.compareTo((com.android.server.wm.WindowContainer) topRecord) > 0 && nextDisplayArea != null) {
                topRecord = nextRecord;
                displayArea = nextDisplayArea;
            }
        }
        return displayArea;
    }

    boolean updateTopResumingActivityInProcessIfNeeded(final com.android.server.wm.ActivityRecord activity) {
        com.android.server.wm.DisplayContent topDisplay;
        com.android.server.wm.TaskFragment taskFrag;
        com.android.server.wm.ActivityRecord ar;
        com.android.server.wm.Task task = activity.getTask();
        boolean dontPauseAfterQActivity = task == null || task.getWrapper().getExtImpl().dontPauseAfterQActivityIfNeed(task);
        if ((this.mInfo.targetSdkVersion >= 29 && dontPauseAfterQActivity) || this.mPreQTopResumedActivity == activity) {
            return true;
        }
        if (task != null && task.getWrapper().getExtImpl().supportMultiResume(activity)) {
            return true;
        }
        if (!activity.isAttached()) {
            return false;
        }
        if (activity.getWindowingMode() == 120 && dontPauseAfterQActivity) {
            return true;
        }
        boolean canUpdate = false;
        if (this.mPreQTopResumedActivity != null && this.mPreQTopResumedActivity.isAttached()) {
            topDisplay = this.mPreQTopResumedActivity.mDisplayContent;
        } else {
            topDisplay = null;
        }
        if (topDisplay == null || !this.mPreQTopResumedActivity.isVisibleRequested() || !this.mPreQTopResumedActivity.isFocusable()) {
            canUpdate = true;
        }
        com.android.server.wm.DisplayContent display = activity.mDisplayContent;
        if (!canUpdate && topDisplay.compareTo((com.android.server.wm.WindowContainer) display) < 0) {
            canUpdate = true;
        }
        if (!canUpdate && (ar = topDisplay.getActivity(new java.util.function.Predicate() { // from class: com.android.server.wm.WindowProcessController$$ExternalSyntheticLambda11
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.wm.WindowProcessController.lambda$updateTopResumingActivityInProcessIfNeeded$1(activity, (com.android.server.wm.ActivityRecord) obj);
            }
        }, true, this.mPreQTopResumedActivity)) != null && ar != this.mPreQTopResumedActivity) {
            canUpdate = true;
        }
        if (canUpdate) {
            if (this.mPreQTopResumedActivity != null && this.mPreQTopResumedActivity.isState(com.android.server.wm.ActivityRecord.State.RESUMED) && (taskFrag = this.mPreQTopResumedActivity.getTaskFragment()) != null) {
                com.android.server.wm.ActivityRecord taskFragPausingActivity = taskFrag.getPausingActivity();
                if (this.mWindProcessConExt.canSetPreQTopResumedActivity(taskFrag, this.mInfo.targetSdkVersion)) {
                    this.mPreQTopResumedActivity = activity;
                }
                boolean userLeaving = taskFrag.shouldBeVisible(null);
                android.util.Slog.d(TAG, "updateTopResumingActivityInProcess pkg=" + this.mInfo.packageName + ",v=" + this.mInfo.targetSdkVersion + ",dont=" + dontPauseAfterQActivity + "," + activity + ",mPreQTopResumedActivity=" + this.mPreQTopResumedActivity + ",taskFrag taskFragPausingActivity=" + taskFragPausingActivity);
                taskFrag.startPausing(userLeaving, false, activity, "top-resumed-changed");
            }
            this.mPreQTopResumedActivity = activity;
        }
        return canUpdate;
    }

    static /* synthetic */ boolean lambda$updateTopResumingActivityInProcessIfNeeded$1(com.android.server.wm.ActivityRecord activity, com.android.server.wm.ActivityRecord r) {
        return r == activity;
    }

    public void stopFreezingActivities() {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mAtm.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                int i = this.mActivities.size();
                while (i > 0) {
                    i--;
                    this.mActivities.get(i).stopFreezingScreen(true, true);
                }
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    void finishActivities() {
        java.util.ArrayList<com.android.server.wm.ActivityRecord> activities = new java.util.ArrayList<>(this.mActivities);
        for (int i = 0; i < activities.size(); i++) {
            com.android.server.wm.ActivityRecord r = activities.get(i);
            if (!r.finishing && r.isInRootTaskLocked()) {
                r.finishIfPossible("finish-heavy", true);
            }
        }
    }

    public boolean isInterestingToUser() {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mAtm.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                int size = this.mActivities.size();
                for (int i = 0; i < size; i++) {
                    com.android.server.wm.ActivityRecord r = this.mActivities.get(i);
                    if (r.isInterestingToUserLocked()) {
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return true;
                    }
                }
                if (hasEmbeddedWindow()) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return true;
                }
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                return false;
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    private boolean hasEmbeddedWindow() {
        if (this.mRemoteActivities == null) {
            return false;
        }
        for (int i = this.mRemoteActivities.size() - 1; i >= 0; i--) {
            if ((this.mRemoteActivities.valueAt(i)[0] & 1) != 0) {
                com.android.server.wm.ActivityRecord r = this.mRemoteActivities.keyAt(i);
                if (r.isInterestingToUserLocked()) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hasRunningActivity(java.lang.String packageName) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mAtm.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                for (int i = this.mActivities.size() - 1; i >= 0; i--) {
                    com.android.server.wm.ActivityRecord r = this.mActivities.get(i);
                    if (packageName.equals(r.packageName)) {
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return true;
                    }
                }
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                return false;
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    void updateAppSpecificSettingsForAllActivitiesInPackage(java.lang.String packageName, java.lang.Integer nightMode, android.os.LocaleList localesOverride, int gender) {
        for (int i = this.mActivities.size() - 1; i >= 0; i--) {
            com.android.server.wm.ActivityRecord r = this.mActivities.get(i);
            if (packageName.equals(r.packageName) && r.applyAppSpecificConfig(nightMode, localesOverride, java.lang.Integer.valueOf(gender)) && r.isVisibleRequested()) {
                r.ensureActivityConfiguration();
            }
        }
    }

    public void clearPackagePreferredForHomeActivities() {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mAtm.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                for (int i = this.mActivities.size() - 1; i >= 0; i--) {
                    com.android.server.wm.ActivityRecord r = this.mActivities.get(i);
                    if (r.isActivityTypeHome()) {
                        android.util.Log.i(TAG, "Clearing package preferred activities from " + r.packageName);
                        try {
                            android.app.ActivityThread.getPackageManager().clearPackagePreferredActivities(r.packageName);
                        } catch (android.os.RemoteException e) {
                        }
                    }
                }
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    boolean hasStartedActivity(com.android.server.wm.ActivityRecord launchedActivity) {
        for (int i = this.mActivities.size() - 1; i >= 0; i--) {
            com.android.server.wm.ActivityRecord activity = this.mActivities.get(i);
            if (launchedActivity != activity && !activity.mAppStopped) {
                return true;
            }
        }
        return false;
    }

    boolean hasResumedActivity() {
        return (this.mActivityStateFlags & 2097152) != 0;
    }

    void updateIntentForHeavyWeightActivity(android.content.Intent intent) {
        if (this.mActivities.isEmpty()) {
            return;
        }
        com.android.server.wm.ActivityRecord hist = this.mActivities.get(0);
        intent.putExtra("cur_app", hist.packageName);
        intent.putExtra("cur_task", hist.getTask().mTaskId);
    }

    boolean shouldKillProcessForRemovedTask(com.android.server.wm.Task task) {
        for (int k = 0; k < this.mActivities.size(); k++) {
            com.android.server.wm.ActivityRecord activity = this.mActivities.get(k);
            if (!activity.mAppStopped) {
                return false;
            }
            com.android.server.wm.Task otherTask = activity.getTask();
            if (task != null && otherTask != null && task.mTaskId != otherTask.mTaskId && otherTask.inRecents) {
                return false;
            }
        }
        return true;
    }

    void releaseSomeActivities(java.lang.String reason) {
        java.util.ArrayList<com.android.server.wm.ActivityRecord> candidates = null;
        if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_RELEASE) {
            android.util.Slog.d(TAG_RELEASE, "Trying to release some activities in " + this);
        }
        for (int i = 0; i < this.mActivities.size(); i++) {
            com.android.server.wm.ActivityRecord r = this.mActivities.get(i);
            if (r.finishing || r.isState(com.android.server.wm.ActivityRecord.State.DESTROYING, com.android.server.wm.ActivityRecord.State.DESTROYED)) {
                if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_RELEASE) {
                    android.util.Slog.d(TAG_RELEASE, "Abort release; already destroying: " + r);
                    return;
                }
                return;
            }
            if (r.isVisibleRequested() || !r.mAppStopped || !r.hasSavedState() || !r.isDestroyable() || r.isState(com.android.server.wm.ActivityRecord.State.STARTED, com.android.server.wm.ActivityRecord.State.RESUMED, com.android.server.wm.ActivityRecord.State.PAUSING, com.android.server.wm.ActivityRecord.State.PAUSED, com.android.server.wm.ActivityRecord.State.STOPPING)) {
                if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_RELEASE) {
                    android.util.Slog.d(TAG_RELEASE, "Not releasing in-use activity: " + r);
                }
            } else if (r.getParent() != null) {
                if (candidates == null) {
                    candidates = new java.util.ArrayList<>();
                }
                candidates.add(r);
            }
        }
        if (candidates != null) {
            candidates.sort(new java.util.Comparator() { // from class: com.android.server.wm.WindowProcessController$$ExternalSyntheticLambda2
                @Override // java.util.Comparator
                public final int compare(java.lang.Object obj, java.lang.Object obj2) {
                    return ((com.android.server.wm.ActivityRecord) obj).compareTo((com.android.server.wm.WindowContainer) obj2);
                }
            });
            int maxRelease = java.lang.Math.max(candidates.size(), 1);
            do {
                com.android.server.wm.ActivityRecord r2 = candidates.remove(0);
                android.util.Slog.v(TAG_RELEASE, "Destroying " + r2 + " in state " + r2.getState() + " for reason " + reason + " " + android.os.Debug.getCallers(5));
                r2.destroyImmediately(reason);
                maxRelease--;
            } while (maxRelease > 0);
        }
    }

    public void getDisplayContextsWithErrorDialogs(java.util.List<android.content.Context> displayContexts) {
        if (displayContexts == null) {
            return;
        }
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mAtm.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.RootWindowContainer root = this.mAtm.mWindowManager.mRoot;
                root.getDisplayContextsWithNonToastVisibleWindows(this.mPid, displayContexts);
                for (int i = this.mActivities.size() - 1; i >= 0; i--) {
                    com.android.server.wm.ActivityRecord r = this.mActivities.get(i);
                    int displayId = r.getDisplayId();
                    android.content.Context c = root.getDisplayUiContext(displayId);
                    if (c != null && r.isVisibleRequested() && !displayContexts.contains(c)) {
                        displayContexts.add(c);
                    }
                }
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    void addHostActivity(com.android.server.wm.ActivityRecord r) {
        int[] flags = getRemoteActivityFlags(r);
        flags[0] = flags[0] | 1;
    }

    void removeHostActivity(com.android.server.wm.ActivityRecord r) {
        removeRemoteActivityFlags(r, 1);
    }

    void addEmbeddedActivity(com.android.server.wm.ActivityRecord r) {
        int[] flags = getRemoteActivityFlags(r);
        flags[0] = flags[0] | 2;
    }

    void removeEmbeddedActivity(com.android.server.wm.ActivityRecord r) {
        removeRemoteActivityFlags(r, 2);
    }

    private int[] getRemoteActivityFlags(com.android.server.wm.ActivityRecord r) {
        if (this.mRemoteActivities == null) {
            this.mRemoteActivities = new android.util.ArrayMap<>();
        }
        int[] flags = this.mRemoteActivities.get(r);
        if (flags == null) {
            int[] flags2 = new int[1];
            this.mRemoteActivities.put(r, flags2);
            return flags2;
        }
        return flags;
    }

    private void removeRemoteActivityFlags(com.android.server.wm.ActivityRecord r, int flags) {
        int index;
        if (this.mRemoteActivities != null && (index = this.mRemoteActivities.indexOfKey(r)) >= 0) {
            int[] currentFlags = this.mRemoteActivities.valueAt(index);
            currentFlags[0] = currentFlags[0] & (~flags);
            if (currentFlags[0] == 0) {
                this.mRemoteActivities.removeAt(index);
            }
        }
    }

    public int computeOomAdjFromActivities(com.android.server.wm.WindowProcessController.ComputeOomAdjCallback callback) {
        int flags = this.mActivityStateFlags;
        if ((65536 & flags) != 0) {
            callback.onVisibleActivity();
        } else if ((131072 & flags) != 0) {
            callback.onPausedActivity();
        } else if ((262144 & flags) != 0) {
            callback.onStoppingActivity((524288 & flags) != 0);
        } else {
            callback.onOtherActivity();
        }
        return 65535 & flags;
    }

    void computeProcessActivityState() {
        int layer;
        com.android.server.wm.ActivityRecord.State bestInvisibleState = com.android.server.wm.ActivityRecord.State.DESTROYED;
        boolean allStoppingFinishing = true;
        boolean visible = false;
        int minTaskLayer = Integer.MAX_VALUE;
        int stateFlags = 0;
        boolean wasResumed = hasResumedActivity();
        boolean wasAnyVisible = (this.mActivityStateFlags & 1114112) != 0;
        for (int i = this.mActivities.size() - 1; i >= 0; i--) {
            com.android.server.wm.ActivityRecord r = this.mActivities.get(i);
            if (r.isVisible()) {
                stateFlags |= 1048576;
            }
            com.android.server.wm.Task task = r.getTask();
            if (task != null && task.mLayerRank != -1) {
                stateFlags |= 4194304;
            }
            if (r.isVisibleRequested()) {
                if (r.isState(com.android.server.wm.ActivityRecord.State.RESUMED)) {
                    stateFlags |= 2097152;
                }
                if (task != null && minTaskLayer > 0 && (layer = task.mLayerRank) >= 0 && minTaskLayer > layer) {
                    minTaskLayer = layer;
                }
                visible = true;
            } else if (!visible && bestInvisibleState != com.android.server.wm.ActivityRecord.State.PAUSING) {
                if (r.isState(com.android.server.wm.ActivityRecord.State.PAUSING, com.android.server.wm.ActivityRecord.State.PAUSED)) {
                    bestInvisibleState = com.android.server.wm.ActivityRecord.State.PAUSING;
                } else if (r.isState(com.android.server.wm.ActivityRecord.State.STOPPING)) {
                    bestInvisibleState = com.android.server.wm.ActivityRecord.State.STOPPING;
                    allStoppingFinishing &= r.finishing;
                }
            }
        }
        if (this.mRemoteActivities != null) {
            for (int i2 = this.mRemoteActivities.size() - 1; i2 >= 0; i2--) {
                if ((this.mRemoteActivities.valueAt(i2)[0] & 2) != 0 && this.mRemoteActivities.keyAt(i2).isVisibleRequested()) {
                    stateFlags |= 65536;
                }
            }
        }
        int stateFlags2 = stateFlags | (65535 & minTaskLayer);
        if (visible) {
            stateFlags2 |= 65536;
        } else if (bestInvisibleState == com.android.server.wm.ActivityRecord.State.PAUSING) {
            stateFlags2 |= 131072;
        } else if (bestInvisibleState == com.android.server.wm.ActivityRecord.State.STOPPING) {
            stateFlags2 |= 262144;
            if (allStoppingFinishing) {
                stateFlags2 |= 524288;
            }
        }
        this.mActivityStateFlags = stateFlags2;
        boolean anyVisible = (1114112 & stateFlags2) != 0;
        if (!wasAnyVisible && anyVisible) {
            this.mAtm.mVisibleActivityProcessTracker.onAnyActivityVisible(this);
            this.mAtm.mWindowManager.onProcessActivityVisibilityChanged(this.mUid, true);
        } else if (wasAnyVisible && !anyVisible) {
            this.mAtm.mVisibleActivityProcessTracker.onAllActivitiesInvisible(this);
            this.mAtm.mWindowManager.onProcessActivityVisibilityChanged(this.mUid, false);
        } else if (wasAnyVisible && !wasResumed && hasResumedActivity()) {
            this.mAtm.mVisibleActivityProcessTracker.onActivityResumedWhileVisible(this);
        }
    }

    private void prepareOomAdjustment() {
        this.mAtm.mRootWindowContainer.rankTaskLayers();
        this.mAtm.mTaskSupervisor.computeProcessActivityStateBatch();
    }

    public int computeRelaunchReason() {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mAtm.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                int activitiesSize = this.mActivities.size();
                for (int i = activitiesSize - 1; i >= 0; i--) {
                    com.android.server.wm.ActivityRecord r = this.mActivities.get(i);
                    if (r.mRelaunchReason != 0) {
                        int i2 = r.mRelaunchReason;
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return i2;
                    }
                }
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                return 0;
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public long getInputDispatchingTimeoutMillis() {
        long j;
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mAtm.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                if (isInstrumenting() || isUsingWrapper()) {
                    j = 60000;
                } else {
                    j = android.os.InputConstants.DEFAULT_DISPATCHING_TIMEOUT_MILLIS;
                }
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        return j;
    }

    void clearProfilerIfNeeded() {
        this.mAtm.mH.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.Consumer() { // from class: com.android.server.wm.WindowProcessController$$ExternalSyntheticLambda5
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.wm.WindowProcessListener) obj).clearProfilerIfNeeded();
            }
        }, this.mListener));
    }

    void updateProcessInfo(boolean updateServiceConnectionActivities, boolean activityChange, boolean updateOomAdj, boolean addPendingTopUid) {
        if (addPendingTopUid) {
            addToPendingTop();
        }
        if (updateOomAdj) {
            prepareOomAdjustment();
        }
        android.os.Message m = com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.server.wm.WindowProcessController$$ExternalSyntheticLambda6(), this.mListener, java.lang.Boolean.valueOf(updateServiceConnectionActivities), java.lang.Boolean.valueOf(activityChange), java.lang.Boolean.valueOf(updateOomAdj));
        this.mAtm.mH.sendMessageAtFrontOfQueue(m);
    }

    void scheduleUpdateOomAdj() {
        this.mAtm.mH.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.server.wm.WindowProcessController$$ExternalSyntheticLambda6(), this.mListener, false, false, true));
    }

    void addToPendingTop() {
        this.mAtm.mAmInternal.addPendingTopUid(this.mUid, this.mPid, this.mThread);
    }

    void updateServiceConnectionActivities() {
        this.mAtm.mH.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.Consumer() { // from class: com.android.server.wm.WindowProcessController$$ExternalSyntheticLambda3
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.wm.WindowProcessListener) obj).updateServiceConnectionActivities();
            }
        }, this.mListener));
    }

    void setPendingUiCleanAndForceProcessStateUpTo(int newState) {
        android.os.Message m = com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.BiConsumer() { // from class: com.android.server.wm.WindowProcessController$$ExternalSyntheticLambda0
            @Override // java.util.function.BiConsumer
            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                ((com.android.server.wm.WindowProcessListener) obj).setPendingUiCleanAndForceProcessStateUpTo(((java.lang.Integer) obj2).intValue());
            }
        }, this.mListener, java.lang.Integer.valueOf(newState));
        this.mAtm.mH.sendMessage(m);
    }

    boolean isRemoved() {
        return this.mListener.isRemoved();
    }

    private boolean shouldSetProfileProc() {
        return this.mAtm.mProfileApp != null && this.mAtm.mProfileApp.equals(this.mName) && (this.mAtm.mProfileProc == null || this.mAtm.mProfileProc == this);
    }

    android.app.ProfilerInfo createProfilerInfoIfNeeded() {
        android.app.ProfilerInfo currentProfilerInfo = this.mAtm.mProfilerInfo;
        if (currentProfilerInfo == null || currentProfilerInfo.profileFile == null || !shouldSetProfileProc()) {
            return null;
        }
        if (currentProfilerInfo.profileFd != null) {
            try {
                currentProfilerInfo.profileFd = currentProfilerInfo.profileFd.dup();
            } catch (java.io.IOException e) {
                currentProfilerInfo.closeFd();
            }
        }
        return new android.app.ProfilerInfo(currentProfilerInfo);
    }

    void onStartActivity(int topProcessState, android.content.pm.ActivityInfo info) {
        java.lang.String packageName = null;
        if ((info.flags & 1) == 0 || !com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME.equals(info.packageName)) {
            packageName = info.packageName;
        }
        if (topProcessState == 2) {
            this.mAtm.mAmInternal.addPendingTopUid(this.mUid, this.mPid, this.mThread);
        }
        prepareOomAdjustment();
        android.os.Message m = com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.internal.util.function.QuintConsumer() { // from class: com.android.server.wm.WindowProcessController$$ExternalSyntheticLambda10
            public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3, java.lang.Object obj4, java.lang.Object obj5) {
                ((com.android.server.wm.WindowProcessListener) obj).onStartActivity(((java.lang.Integer) obj2).intValue(), ((java.lang.Boolean) obj3).booleanValue(), (java.lang.String) obj4, ((java.lang.Long) obj5).longValue());
            }
        }, this.mListener, java.lang.Integer.valueOf(topProcessState), java.lang.Boolean.valueOf(shouldSetProfileProc()), packageName, java.lang.Long.valueOf(info.applicationInfo.longVersionCode));
        this.mAtm.mH.sendMessageAtFrontOfQueue(m);
    }

    void appDied(java.lang.String reason) {
        android.os.Message m = com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.BiConsumer() { // from class: com.android.server.wm.WindowProcessController$$ExternalSyntheticLambda7
            @Override // java.util.function.BiConsumer
            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                ((com.android.server.wm.WindowProcessListener) obj).appDied((java.lang.String) obj2);
            }
        }, this.mListener, reason);
        this.mAtm.mH.sendMessage(m);
    }

    boolean handleAppDied() {
        this.mAtm.mTaskSupervisor.removeHistoryRecords(this);
        boolean hasVisibleActivities = false;
        boolean hasInactiveActivities = (this.mInactiveActivities == null || this.mInactiveActivities.isEmpty()) ? false : true;
        java.util.ArrayList<com.android.server.wm.ActivityRecord> activities = (this.mHasActivities || hasInactiveActivities) ? new java.util.ArrayList<>() : this.mActivities;
        if (this.mHasActivities) {
            activities.addAll(this.mActivities);
        }
        if (hasInactiveActivities) {
            activities.addAll(this.mInactiveActivities);
        }
        if (isRemoved() && this.mWindProcessConExt.shouldMakeActivityFinishing(this.mInfo.packageName, this.mUserId)) {
            for (int i = activities.size() - 1; i >= 0; i--) {
                activities.get(i).makeFinishingLocked();
            }
        }
        int i2 = activities.size();
        for (int i3 = i2 - 1; i3 >= 0; i3--) {
            com.android.server.wm.ActivityRecord r = activities.get(i3);
            if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_AMS) {
                android.util.Slog.d(TAG, "handleAppDied: r = " + r + ", isVisibleRequested = " + r.isVisibleRequested() + ", isVisible = " + r.isVisible());
            }
            if (r.isVisibleRequested() || r.isVisible()) {
                hasVisibleActivities = true;
            }
            com.android.server.wm.TaskFragment taskFragment = r.getTaskFragment();
            if (taskFragment != null) {
                hasVisibleActivities |= taskFragment.handleAppDied(this);
            }
            r.handleAppDied();
        }
        clearRecentTasks();
        clearActivities();
        return hasVisibleActivities;
    }

    void registerDisplayAreaConfigurationListener(com.android.server.wm.DisplayArea displayArea) {
        if (displayArea == null || displayArea.containsListener(this)) {
            return;
        }
        unregisterConfigurationListeners();
        this.mDisplayArea = displayArea;
        displayArea.registerConfigurationChangeListener(this);
    }

    void unregisterDisplayAreaConfigurationListener() {
        if (this.mDisplayArea == null) {
            return;
        }
        this.mDisplayArea.unregisterConfigurationChangeListener(this);
        this.mDisplayArea = null;
        onMergedOverrideConfigurationChanged(android.content.res.Configuration.EMPTY);
    }

    void registerActivityConfigurationListener(com.android.server.wm.ActivityRecord activityRecord) {
        if (activityRecord == null || activityRecord.containsListener(this) || !this.mIsActivityConfigOverrideAllowed) {
            return;
        }
        unregisterConfigurationListeners();
        this.mConfigActivityRecord = activityRecord;
        activityRecord.registerConfigurationChangeListener(this);
        if (this.mThread == null) {
            this.mHasPendingConfigurationChange = true;
        }
    }

    private void unregisterActivityConfigurationListener() {
        if (this.mConfigActivityRecord == null) {
            return;
        }
        this.mConfigActivityRecord.unregisterConfigurationChangeListener(this);
        this.mConfigActivityRecord = null;
        onMergedOverrideConfigurationChanged(android.content.res.Configuration.EMPTY);
    }

    private void unregisterConfigurationListeners() {
        unregisterActivityConfigurationListener();
        unregisterDisplayAreaConfigurationListener();
    }

    void destroy() {
        unregisterConfigurationListeners();
    }

    private void updateActivityConfigurationListener() {
        if (!this.mIsActivityConfigOverrideAllowed) {
            return;
        }
        for (int i = this.mActivities.size() - 1; i >= 0; i--) {
            com.android.server.wm.ActivityRecord activityRecord = this.mActivities.get(i);
            if (!activityRecord.finishing && !activityRecord.getWrapper().getExtImpl().isActivityConfigOverrideDisable(activityRecord, this)) {
                registerActivityConfigurationListener(activityRecord);
                return;
            }
        }
        unregisterActivityConfigurationListener();
    }

    @Override // com.android.server.wm.ConfigurationContainer
    public void onConfigurationChanged(android.content.res.Configuration newGlobalConfig) {
        super.onConfigurationChanged(newGlobalConfig);
        boolean topActivityDeviceChanged = false;
        int deviceId = getTopActivityDeviceId();
        if (deviceId != this.mLastTopActivityDeviceId) {
            topActivityDeviceChanged = true;
            this.mLastTopActivityDeviceId = deviceId;
        }
        android.content.res.Configuration config = getConfiguration();
        if (this.mLastReportedConfiguration.equals(config) & (!topActivityDeviceChanged)) {
            if (android.os.Build.IS_DEBUGGABLE && this.mHasImeService) {
                android.util.Slog.w(TAG_CONFIGURATION, "Current config: " + config + " unchanged for IME proc " + this.mName);
                return;
            }
            return;
        }
        if (this.mPauseConfigurationDispatchCount > 0) {
            this.mHasPendingConfigurationChange = true;
        } else {
            dispatchConfiguration(config);
        }
    }

    private int getTopActivityDeviceId() {
        com.android.server.wm.ActivityRecord topActivity = getTopNonFinishingActivity();
        if (topActivity == null || topActivity.mDisplayContent == null) {
            return 0;
        }
        int updatedDeviceId = this.mAtm.mTaskSupervisor.getDeviceIdForDisplayId(topActivity.mDisplayContent.mDisplayId);
        return updatedDeviceId;
    }

    private com.android.server.wm.ActivityRecord getTopNonFinishingActivity() {
        if (this.mActivities.isEmpty()) {
            return null;
        }
        for (int i = this.mActivities.size() - 1; i >= 0; i--) {
            if (!this.mActivities.get(i).finishing) {
                return this.mActivities.get(i);
            }
        }
        return null;
    }

    @Override // com.android.server.wm.ConfigurationContainerListener
    public void onMergedOverrideConfigurationChanged(android.content.res.Configuration mergedOverrideConfig) {
        super.onRequestedOverrideConfigurationChanged(mergedOverrideConfig);
    }

    @Override // com.android.server.wm.ConfigurationContainer
    void resolveOverrideConfiguration(android.content.res.Configuration newParentConfig) {
        com.android.server.wm.DisplayContent displayContent;
        android.content.res.Configuration requestedOverrideConfig = getRequestedOverrideConfiguration();
        if (requestedOverrideConfig.assetsSeq != 0 && newParentConfig.assetsSeq > requestedOverrideConfig.assetsSeq) {
            requestedOverrideConfig.assetsSeq = 0;
        }
        super.resolveOverrideConfiguration(newParentConfig);
        android.content.res.Configuration resolvedConfig = getResolvedOverrideConfiguration();
        resolvedConfig.windowConfiguration.setActivityType(0);
        this.mWindProcessConExt.resolveOverrideConfiguration(resolvedConfig, this.mConfigActivityRecord);
        resolvedConfig.seq = newParentConfig.seq;
        if (this.mConfigActivityRecord != null) {
            return;
        }
        if (this.mAtm.mWindowManager != null) {
            displayContent = this.mAtm.mWindowManager.getDefaultDisplayContentLocked();
        } else {
            displayContent = null;
        }
        applySizeOverrideIfNeeded(displayContent, this.mInfo, newParentConfig, resolvedConfig, false, false, false);
    }

    void dispatchConfiguration(android.content.res.Configuration config) {
        this.mHasPendingConfigurationChange = false;
        android.app.IApplicationThread thread = this.mThread;
        if (thread == null) {
            if (android.os.Build.IS_DEBUGGABLE && this.mHasImeService && !isAgingVersion) {
                android.util.Slog.w(TAG_CONFIGURATION, "Unable to send config for IME proc " + this.mName + ": no app thread");
                return;
            }
            return;
        }
        boolean isConfigChange = this.mWindProcessConExt.isConfigChange(config, this.mLastReportedConfiguration, this.mInfo);
        config.seq = this.mAtm.increaseConfigurationSeqLocked();
        setLastReportedConfiguration(config);
        if (this.mRepProcState >= 16 && !isConfigChange) {
            this.mHasCachedConfiguration = true;
            if (this.mRepProcState >= 16) {
                return;
            }
        }
        onConfigurationChangePreScheduled(config);
        scheduleClientTransactionItem(thread, android.app.servertransaction.ConfigurationChangeItem.obtain(config, this.mLastTopActivityDeviceId));
    }

    private void onConfigurationChangePreScheduled(android.content.res.Configuration config) {
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_CONFIGURATION_enabled[1]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(this.mName);
            java.lang.String protoLogParam1 = java.lang.String.valueOf(config);
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_CONFIGURATION, -4629255026637000251L, 0, null, protoLogParam0, protoLogParam1);
        }
        if (android.os.Build.IS_DEBUGGABLE && this.mHasImeService) {
            android.util.Slog.v(TAG_CONFIGURATION, "Sending to IME proc " + this.mName + " new config " + config);
        }
        this.mHasCachedConfiguration = false;
    }

    void scheduleClientTransactionItem(android.app.servertransaction.ClientTransactionItem transactionItem) {
        android.app.IApplicationThread thread = this.mThread;
        if (thread == null) {
            if (android.os.Build.IS_DEBUGGABLE) {
                android.util.Slog.w(TAG_CONFIGURATION, "Unable to send transaction to client proc " + this.mName + ": no app thread");
                return;
            }
            return;
        }
        scheduleClientTransactionItem(thread, transactionItem);
    }

    private void scheduleClientTransactionItem(android.app.IApplicationThread thread, android.app.servertransaction.ClientTransactionItem transactionItem) {
        try {
            if (this.mWindowSession != null && this.mWindowSession.hasWindow()) {
                this.mAtm.getLifecycleManager().scheduleTransactionItem(thread, transactionItem);
            } else {
                this.mAtm.getLifecycleManager().scheduleTransactionItemNow(thread, transactionItem);
            }
        } catch (android.os.DeadObjectException e) {
            android.util.Slog.w(TAG_CONFIGURATION, "Failed for dead process. ClientTransactionItem=" + transactionItem + " owner=" + this.mOwner);
        } catch (java.lang.Exception e2) {
            android.util.Slog.e(TAG_CONFIGURATION, "Failed to schedule ClientTransactionItem=" + transactionItem + " owner=" + this.mOwner, e2);
        }
    }

    void setLastReportedConfiguration(android.content.res.Configuration config) {
        synchronized (this.mLastReportedConfiguration) {
            this.mLastReportedConfiguration.setTo(config);
        }
    }

    void pauseConfigurationDispatch() {
        this.mPauseConfigurationDispatchCount++;
    }

    boolean resumeConfigurationDispatch() {
        if (this.mPauseConfigurationDispatchCount == 0) {
            return false;
        }
        this.mPauseConfigurationDispatchCount--;
        return this.mHasPendingConfigurationChange;
    }

    void updateAssetConfiguration(int assetSeq) {
        if (!this.mHasActivities || !this.mIsActivityConfigOverrideAllowed) {
            android.content.res.Configuration overrideConfig = new android.content.res.Configuration(getRequestedOverrideConfiguration());
            overrideConfig.assetsSeq = assetSeq;
            onRequestedOverrideConfigurationChanged(overrideConfig);
            return;
        }
        for (int i = this.mActivities.size() - 1; i >= 0; i--) {
            com.android.server.wm.ActivityRecord r = this.mActivities.get(i);
            android.content.res.Configuration overrideConfig2 = new android.content.res.Configuration(r.getRequestedOverrideConfiguration());
            overrideConfig2.assetsSeq = assetSeq;
            r.onRequestedOverrideConfigurationChanged(overrideConfig2);
            android.util.Slog.w(TAG, "updateAssetConfiguration assetSeq = " + assetSeq + "; r = " + r + "; fullConfiguration = " + r.getConfiguration() + "; requestOverrideConfig = " + r.getRequestedOverrideConfiguration());
            if (r.isVisibleRequested()) {
                r.ensureActivityConfiguration();
            }
        }
    }

    android.content.res.Configuration prepareConfigurationForLaunchingActivity() {
        android.content.res.Configuration config = getConfiguration();
        if (this.mHasPendingConfigurationChange) {
            this.mHasPendingConfigurationChange = false;
            config.seq = this.mAtm.increaseConfigurationSeqLocked();
        }
        this.mHasCachedConfiguration = false;
        return config;
    }

    public long getCpuTime() {
        return this.mListener.getCpuTime();
    }

    void addRecentTask(com.android.server.wm.Task task) {
        this.mRecentTasks.add(task);
        this.mHasRecentTasks = true;
    }

    void removeRecentTask(com.android.server.wm.Task task) {
        this.mRecentTasks.remove(task);
        this.mHasRecentTasks = !this.mRecentTasks.isEmpty();
    }

    public boolean hasRecentTasks() {
        return this.mHasRecentTasks;
    }

    void clearRecentTasks() {
        for (int i = this.mRecentTasks.size() - 1; i >= 0; i--) {
            this.mRecentTasks.get(i).clearRootProcess();
        }
        this.mRecentTasks.clear();
        this.mHasRecentTasks = false;
    }

    public void appEarlyNotResponding(java.lang.String annotation, java.lang.Runnable killAppCallback) {
        java.lang.Runnable targetRunnable = null;
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mAtm.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                if (this.mWindProcessConExt.hookappEarlyNotRespondingForAging()) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return;
                }
                if (this.mAtm.mController == null) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return;
                }
                try {
                    int res = this.mAtm.mController.appEarlyNotResponding(this.mName, this.mPid, annotation);
                    if (res < 0) {
                        if (this.mPid != com.android.server.wm.WindowManagerService.MY_PID) {
                            targetRunnable = killAppCallback;
                        }
                    }
                } catch (android.os.RemoteException e) {
                    this.mAtm.mController = null;
                    com.android.server.Watchdog.getInstance().setActivityController(null);
                    this.mWindProcessConExt.hookappEarlyNotRespondingPrecess(this.mAtm);
                }
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                if (targetRunnable != null) {
                    targetRunnable.run();
                }
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:20:0x003a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean appNotResponding(java.lang.String r7, java.lang.Runnable r8, java.lang.Runnable r9) throws android.os.RemoteException {
        /*
            r6 = this;
            r0 = 0
            com.android.server.wm.ActivityTaskManagerService r1 = r6.mAtm
            com.android.server.wm.WindowManagerGlobalLock r1 = r1.mGlobalLock
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection()
            monitor-enter(r1)
            com.android.server.wm.IWindowProcessControllerExt r2 = r6.mWindProcessConExt     // Catch: java.lang.Throwable -> L61
            boolean r2 = r2.hookappNotRespondingForAgine()     // Catch: java.lang.Throwable -> L61
            r3 = 0
            if (r2 == 0) goto L17
            monitor-exit(r1)     // Catch: java.lang.Throwable -> L61
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection()
            return r3
        L17:
            com.android.server.wm.ActivityTaskManagerService r2 = r6.mAtm     // Catch: java.lang.Throwable -> L61
            android.app.IActivityController r2 = r2.mController     // Catch: java.lang.Throwable -> L61
            if (r2 != 0) goto L22
            monitor-exit(r1)     // Catch: java.lang.Throwable -> L61
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection()
            return r3
        L22:
            com.android.server.wm.ActivityTaskManagerService r2 = r6.mAtm     // Catch: android.os.RemoteException -> L48 java.lang.Throwable -> L61
            android.app.IActivityController r2 = r2.mController     // Catch: android.os.RemoteException -> L48 java.lang.Throwable -> L61
            java.lang.String r4 = r6.mName     // Catch: android.os.RemoteException -> L48 java.lang.Throwable -> L61
            int r5 = r6.mPid     // Catch: android.os.RemoteException -> L48 java.lang.Throwable -> L61
            int r2 = r2.appNotResponding(r4, r5, r7)     // Catch: android.os.RemoteException -> L48 java.lang.Throwable -> L61
            if (r2 == 0) goto L3b
            if (r2 >= 0) goto L3a
            int r4 = r6.mPid     // Catch: android.os.RemoteException -> L48 java.lang.Throwable -> L61
            int r5 = com.android.server.wm.WindowManagerService.MY_PID     // Catch: android.os.RemoteException -> L48 java.lang.Throwable -> L61
            if (r4 == r5) goto L3a
            r0 = r8
            goto L3b
        L3a:
            r0 = r9
        L3b:
            monitor-exit(r1)     // Catch: java.lang.Throwable -> L61
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection()
            if (r0 == 0) goto L47
            r0.run()
            r1 = 1
            return r1
        L47:
            return r3
        L48:
            r2 = move-exception
            com.android.server.wm.ActivityTaskManagerService r4 = r6.mAtm     // Catch: java.lang.Throwable -> L61
            r5 = 0
            r4.mController = r5     // Catch: java.lang.Throwable -> L61
            com.android.server.Watchdog r4 = com.android.server.Watchdog.getInstance()     // Catch: java.lang.Throwable -> L61
            r4.setActivityController(r5)     // Catch: java.lang.Throwable -> L61
            com.android.server.wm.IWindowProcessControllerExt r4 = r6.mWindProcessConExt     // Catch: java.lang.Throwable -> L61
            com.android.server.wm.ActivityTaskManagerService r5 = r6.mAtm     // Catch: java.lang.Throwable -> L61
            r4.hookappNotRespondingProcess(r5)     // Catch: java.lang.Throwable -> L61
            monitor-exit(r1)     // Catch: java.lang.Throwable -> L61
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection()
            return r3
        L61:
            r2 = move-exception
            monitor-exit(r1)     // Catch: java.lang.Throwable -> L61
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection()
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.wm.WindowProcessController.appNotResponding(java.lang.String, java.lang.Runnable, java.lang.Runnable):boolean");
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:17:0x002d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void onServiceStarted(android.content.pm.ServiceInfo r5) {
        /*
            r4 = this;
            java.lang.String r0 = r5.permission
            if (r0 != 0) goto L5
            return
        L5:
            int r1 = r0.hashCode()
            r2 = 1
            r3 = 0
            switch(r1) {
                case -769871357: goto L23;
                case 1412417858: goto L19;
                case 1448369304: goto Lf;
                default: goto Le;
            }
        Le:
            goto L2d
        Lf:
            java.lang.String r1 = "android.permission.BIND_INPUT_METHOD"
            boolean r1 = r0.equals(r1)
            if (r1 == 0) goto Le
            r1 = r3
            goto L2e
        L19:
            java.lang.String r1 = "android.permission.BIND_ACCESSIBILITY_SERVICE"
            boolean r1 = r0.equals(r1)
            if (r1 == 0) goto Le
            r1 = r2
            goto L2e
        L23:
            java.lang.String r1 = "android.permission.BIND_VOICE_INTERACTION"
            boolean r1 = r0.equals(r1)
            if (r1 == 0) goto Le
            r1 = 2
            goto L2e
        L2d:
            r1 = -1
        L2e:
            switch(r1) {
                case 0: goto L32;
                case 1: goto L34;
                case 2: goto L34;
                default: goto L31;
            }
        L31:
            goto L3a
        L32:
            r4.mHasImeService = r2
        L34:
            r4.mIsActivityConfigOverrideAllowed = r3
            r4.unregisterActivityConfigurationListener()
        L3a:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.wm.WindowProcessController.onServiceStarted(android.content.pm.ServiceInfo):void");
    }

    public boolean useFifoUiScheduling() {
        return this.mUseFifoUiScheduling;
    }

    public void onTopProcChanged() {
        if (this.mAtm.mVrController.isInterestingToSchedGroup()) {
            this.mAtm.mH.post(new java.lang.Runnable() { // from class: com.android.server.wm.WindowProcessController$$ExternalSyntheticLambda8
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onTopProcChanged$2();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onTopProcChanged$2() {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mAtm.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mAtm.mVrController.onTopProcChangedLocked(this);
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    public boolean isHomeProcess() {
        return this == this.mAtm.mHomeProcess;
    }

    public boolean isShowingUiWhileDozing() {
        return this == this.mAtm.mVisibleDozeUiProcess;
    }

    public boolean isPreviousProcess() {
        return this == this.mAtm.mPreviousProcess;
    }

    public boolean isHeavyWeightProcess() {
        return this == this.mAtm.mHeavyWeightProcess;
    }

    public boolean isFactoryTestProcess() {
        android.content.ComponentName topComponent;
        int factoryTestMode = this.mAtm.mFactoryTest;
        if (factoryTestMode == 0) {
            return false;
        }
        if (factoryTestMode == 1 && (topComponent = this.mAtm.mTopComponent) != null && this.mName.equals(topComponent.getPackageName())) {
            return true;
        }
        if (factoryTestMode != 2 || (this.mInfo.flags & 16) == 0) {
            return false;
        }
        return true;
    }

    public void setStoppedState(int stoppedState) {
        this.mStoppedState = stoppedState;
    }

    boolean getWasStoppedLogged() {
        return this.mWasStoppedLogged;
    }

    void setWasStoppedLogged(boolean logged) {
        this.mWasStoppedLogged = logged;
    }

    public boolean wasForceStopped() {
        return this.mStoppedState == 2;
    }

    boolean wasFirstLaunch() {
        return this.mStoppedState == 1;
    }

    void setRunningRecentsAnimation(boolean running) {
        if (running) {
            addAnimatingReason(4);
        } else {
            removeAnimatingReason(4);
        }
    }

    void setRunningRemoteAnimation(boolean running) {
        if (running) {
            addAnimatingReason(1);
        } else {
            removeAnimatingReason(1);
        }
    }

    void addAnimatingReason(int reason) {
        int prevReasons = this.mAnimatingReasons;
        this.mAnimatingReasons |= reason;
        if (prevReasons == 0) {
            setAnimating(true);
        }
    }

    void removeAnimatingReason(int reason) {
        int prevReasons = this.mAnimatingReasons;
        this.mAnimatingReasons &= ~reason;
        if (prevReasons != 0 && this.mAnimatingReasons == 0) {
            setAnimating(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setAnimating$3(boolean animating) {
        this.mListener.setRunningRemoteAnimation(animating);
    }

    private void setAnimating(final boolean animating) {
        this.mAtm.mH.post(new java.lang.Runnable() { // from class: com.android.server.wm.WindowProcessController$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$setAnimating$3(animating);
            }
        });
    }

    boolean isRunningRemoteTransition() {
        return (this.mAnimatingReasons & 1) != 0;
    }

    void setRunningAnimationUnsafe() {
        this.mListener.setRunningRemoteAnimation(true);
    }

    public java.lang.String toString() {
        if (this.mOwner != null) {
            return this.mOwner.toString();
        }
        return null;
    }

    public void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mAtm.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                if (this.mActivities.size() > 0) {
                    pw.print(prefix);
                    pw.println("Activities:");
                    for (int i = 0; i < this.mActivities.size(); i++) {
                        pw.print(prefix);
                        pw.print("  - ");
                        pw.println(this.mActivities.get(i));
                    }
                }
                if (this.mRemoteActivities != null && !this.mRemoteActivities.isEmpty()) {
                    pw.print(prefix);
                    pw.println("Remote Activities:");
                    for (int i2 = this.mRemoteActivities.size() - 1; i2 >= 0; i2--) {
                        pw.print(prefix);
                        pw.print("  - ");
                        pw.print(this.mRemoteActivities.keyAt(i2));
                        pw.print(" flags=");
                        int flags = this.mRemoteActivities.valueAt(i2)[0];
                        if ((flags & 1) != 0) {
                            pw.print("host ");
                        }
                        if ((flags & 2) != 0) {
                            pw.print("embedded");
                        }
                        pw.println();
                    }
                }
                if (this.mRecentTasks.size() > 0) {
                    pw.println(prefix + "Recent Tasks:");
                    for (int i3 = 0; i3 < this.mRecentTasks.size(); i3++) {
                        pw.println(prefix + "  - " + this.mRecentTasks.get(i3));
                    }
                }
                int i4 = this.mVrThreadTid;
                if (i4 != 0) {
                    pw.print(prefix);
                    pw.print("mVrThreadTid=");
                    pw.println(this.mVrThreadTid);
                }
                this.mBgLaunchController.dump(pw, prefix);
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        pw.println(prefix + " Configuration=" + getConfiguration());
        pw.println(prefix + " OverrideConfiguration=" + getRequestedOverrideConfiguration());
        pw.println(prefix + " mLastReportedConfiguration=" + (this.mHasCachedConfiguration ? "(cached) " + this.mLastReportedConfiguration : this.mLastReportedConfiguration));
        int animatingReasons = this.mAnimatingReasons;
        if (animatingReasons != 0) {
            pw.print(prefix + " mAnimatingReasons=");
            if ((animatingReasons & 1) != 0) {
                pw.print("remote-animation|");
            }
            if ((animatingReasons & 2) != 0) {
                pw.print("wakefulness|");
            }
            if ((animatingReasons & 4) != 0) {
                pw.print("legacy-recents");
            }
            pw.println();
        }
        if (this.mUseFifoUiScheduling) {
            pw.println(prefix + " mUseFifoUiScheduling=true");
        }
        int stateFlags = this.mActivityStateFlags;
        if (stateFlags != 65535) {
            pw.print(prefix + " mActivityStateFlags=");
            if ((1048576 & stateFlags) != 0) {
                pw.print("W|");
            }
            if ((65536 & stateFlags) != 0) {
                pw.print("V|");
                if ((2097152 & stateFlags) != 0) {
                    pw.print("R|");
                }
            } else if ((131072 & stateFlags) != 0) {
                pw.print("P|");
            } else if ((262144 & stateFlags) != 0) {
                pw.print("S|");
                if ((524288 & stateFlags) != 0) {
                    pw.print("F|");
                }
            }
            if ((4194304 & stateFlags) != 0) {
                pw.print("VT|");
            }
            int taskLayer = stateFlags & 65535;
            if (taskLayer != 65535) {
                pw.print("taskLayer=" + taskLayer);
            }
            pw.println();
        }
        pw.println(prefix + " mWaitActivityToAttach=" + this.mWindProcessConExt.waitActivityToAttach());
    }

    void dumpDebug(android.util.proto.ProtoOutputStream proto, long fieldId) {
        this.mListener.dumpDebug(proto, fieldId);
    }

    public com.android.server.wm.IWindowProcessControllerWrapper getWrapper() {
        return this.mWindowProcessControllerWrapper;
    }

    private class WindowProcessControllerWrapper implements com.android.server.wm.IWindowProcessControllerWrapper {
        private WindowProcessControllerWrapper() {
        }

        @Override // com.android.server.wm.IWindowProcessControllerWrapper
        public com.android.server.wm.IWindowProcessControllerExt getExtImpl() {
            return com.android.server.wm.WindowProcessController.this.mWindProcessConExt;
        }

        @Override // com.android.server.wm.IWindowProcessControllerWrapper
        public java.util.ArrayList<java.lang.String> getPkgList() {
            return com.android.server.wm.WindowProcessController.this.mPkgList;
        }

        @Override // com.android.server.wm.IWindowProcessControllerWrapper
        public java.util.ArrayList<com.android.server.wm.ActivityRecord> getActivities() {
            return com.android.server.wm.WindowProcessController.this.mActivities;
        }

        @Override // com.android.server.wm.IWindowProcessControllerWrapper
        public com.android.server.wm.ActivityTaskManagerService getAtm() {
            return com.android.server.wm.WindowProcessController.this.mAtm;
        }

        @Override // com.android.server.wm.IWindowProcessControllerWrapper
        public boolean hasActivityInVisibleTask() {
            return (com.android.server.wm.WindowProcessController.this.mActivityStateFlags & 4194304) != 0;
        }

        @Override // com.android.server.wm.IWindowProcessControllerWrapper
        public boolean hasResumedActivity() {
            return (com.android.server.wm.WindowProcessController.this.mActivityStateFlags & 2097152) != 0;
        }
    }

    @Override // com.android.server.wm.ConfigurationContainer
    protected boolean setOverrideGender(android.content.res.Configuration requestsTmpConfig, int gender) {
        return applyConfigGenderOverride(requestsTmpConfig, gender, this.mAtm.mGrammaticalManagerInternal, this.mUid);
    }

    static boolean applyConfigGenderOverride(android.content.res.Configuration overrideConfig, int override, com.android.server.grammaticalinflection.GrammaticalInflectionManagerInternal service, int uid) {
        int targetValue;
        boolean canGetSystemValue = service != null && service.canGetSystemGrammaticalGender(uid);
        if (override != 0) {
            targetValue = override;
        } else if (canGetSystemValue) {
            targetValue = -1;
        } else if (service != null) {
            targetValue = service.getGrammaticalGenderFromDeveloperSettings();
        } else {
            targetValue = 0;
        }
        if (overrideConfig.getGrammaticalGenderRaw() == targetValue) {
            return false;
        }
        overrideConfig.setGrammaticalGender(targetValue);
        return true;
    }
}
