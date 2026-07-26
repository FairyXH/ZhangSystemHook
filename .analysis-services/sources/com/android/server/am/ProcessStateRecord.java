package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public final class ProcessStateRecord {
    private static final boolean TRACE_OOM_ADJ = false;
    private static final int VALUE_FALSE = 0;
    private static final int VALUE_INVALID = -1;
    private static final int VALUE_TRUE = 1;
    private int mAdjSeq;
    private java.lang.Object mAdjSource;
    private int mAdjSourceProcState;
    private java.lang.Object mAdjTarget;
    private java.lang.String mAdjType;
    private int mAdjTypeCode;
    private final com.android.server.am.ProcessRecord mApp;
    private long mCacheOomRankerRss;
    private long mCacheOomRankerRssTimeMs;
    private int mCacheOomRankerUseCount;
    private int mCompletedAdjSeq;
    private boolean mContainsCycle;
    private long mFgInteractionTime;
    private java.lang.Object mForcingToImportant;
    private boolean mHasForegroundActivities;
    private boolean mHasOverlayUi;
    private boolean mHasShownUi;
    private boolean mHasStartedServices;
    private boolean mHasTopUi;
    private long mInteractionEventTime;
    private long mLastCanKillOnBgRestrictedAndIdleTime;
    private long mLastInvisibleTime;
    private long mLastStateTime;
    private boolean mNoKillOnBgRestrictedAndIdle;
    private boolean mNotCachedSinceIdle;
    private final com.android.server.am.ActivityManagerGlobalLock mProcLock;
    private boolean mProcStateChanged;
    private boolean mReachable;
    private boolean mRepForegroundActivities;
    private boolean mReportedInteraction;
    private boolean mRunningRemoteAnimation;
    private int mSavedPriority;
    private final com.android.server.am.ActivityManagerService mService;
    private boolean mServiceB;
    private boolean mServiceHighRam;
    private boolean mSetCached;
    private boolean mSetNoKillOnBgRestrictedAndIdle;
    private boolean mSystemNoUi;
    private long mWhenUnimportant;
    private int mMaxAdj = 1001;
    private int mCurRawAdj = -10000;
    private int mSetRawAdj = -10000;
    public int mCurAdj = -10000;
    private int mSetAdj = -10000;
    private int mVerifiedAdj = -10000;
    private int mCurCapability = 0;
    private int mSetCapability = 0;
    private int mCurSchedGroup = 0;
    private int mSetSchedGroup = 0;
    private int mCurProcState = 20;
    private int mRepProcState = 20;
    private int mCurRawProcState = 20;
    private int mSetProcState = 20;
    private long mLastTopTime = Long.MIN_VALUE;
    private boolean mBackgroundRestricted = false;
    private boolean mCurBoundByNonBgRestrictedApp = false;
    private boolean mSetBoundByNonBgRestrictedApp = false;
    private int mCachedHasActivities = -1;
    private int mCachedIsHeavyWeight = -1;
    private int mCachedHasVisibleActivities = -1;
    private int mCachedIsHomeProcess = -1;
    private int mCachedIsPreviousProcess = -1;
    private int mCachedHasRecentTasks = -1;
    private int mCachedIsReceivingBroadcast = -1;
    private int[] mCachedCompatChanges = {-1, -1, -1};
    private java.lang.String mCachedAdjType = null;
    private int mCachedAdj = -10000;
    private boolean mCachedForegroundActivities = false;
    private int mCachedProcState = 19;
    private int mCachedSchedGroup = 0;
    private boolean mScheduleLikeTopApp = false;
    private long mFollowupUpdateUptimeMs = Long.MAX_VALUE;

    ProcessStateRecord(com.android.server.am.ProcessRecord app) {
        this.mApp = app;
        this.mService = app.mService;
        this.mProcLock = this.mService.mProcLock;
    }

    void init(long now) {
        this.mLastStateTime = now;
    }

    void setMaxAdj(int maxAdj) {
        this.mMaxAdj = maxAdj;
    }

    int getMaxAdj() {
        return this.mMaxAdj;
    }

    void setCurRawAdj(int curRawAdj) {
        setCurRawAdj(curRawAdj, false);
    }

    boolean setCurRawAdj(int curRawAdj, boolean dryRun) {
        if (dryRun) {
            return this.mCurRawAdj > curRawAdj;
        }
        this.mCurRawAdj = curRawAdj;
        this.mApp.getWindowProcessController().setPerceptible(curRawAdj <= 200);
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getCurRawAdj() {
        return this.mCurRawAdj;
    }

    void setSetRawAdj(int setRawAdj) {
        this.mSetRawAdj = setRawAdj;
    }

    int getSetRawAdj() {
        return this.mSetRawAdj;
    }

    void setCurAdj(int curAdj) {
        this.mCurAdj = curAdj;
        this.mApp.getWindowProcessController().setCurrentAdj(curAdj);
    }

    int getCurAdj() {
        return this.mCurAdj;
    }

    void setSetAdj(int setAdj) {
        this.mSetAdj = setAdj;
    }

    int getSetAdj() {
        return this.mSetAdj;
    }

    int getSetAdjWithServices() {
        if (this.mSetAdj >= 900 && this.mHasStartedServices) {
            return 800;
        }
        return this.mSetAdj;
    }

    void setVerifiedAdj(int verifiedAdj) {
        this.mVerifiedAdj = verifiedAdj;
    }

    int getVerifiedAdj() {
        return this.mVerifiedAdj;
    }

    void setCurCapability(int curCapability) {
        this.mCurCapability = curCapability;
    }

    int getCurCapability() {
        return this.mCurCapability;
    }

    void setSetCapability(int setCapability) {
        this.mSetCapability = setCapability;
    }

    int getSetCapability() {
        return this.mSetCapability;
    }

    void setCurrentSchedulingGroup(int curSchedGroup) {
        this.mCurSchedGroup = curSchedGroup;
        this.mApp.getWindowProcessController().setCurrentSchedulingGroup(curSchedGroup);
    }

    int getCurrentSchedulingGroup() {
        return this.mCurSchedGroup;
    }

    void setSetSchedGroup(int setSchedGroup) {
        this.mSetSchedGroup = setSchedGroup;
    }

    int getSetSchedGroup() {
        return this.mSetSchedGroup;
    }

    void setCurProcState(int curProcState) {
        this.mCurProcState = curProcState;
        this.mApp.getWindowProcessController().setCurrentProcState(this.mCurProcState);
        this.mApp.getWrapper().getExtImpl().updateProcessState(this.mApp, curProcState);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getCurProcState() {
        return this.mCurProcState;
    }

    void setCurRawProcState(int curRawProcState) {
        setCurRawProcState(curRawProcState, false);
    }

    boolean setCurRawProcState(int curRawProcState, boolean dryRun) {
        if (dryRun) {
            return this.mCurRawProcState > curRawProcState;
        }
        this.mCurRawProcState = curRawProcState;
        return false;
    }

    int getCurRawProcState() {
        return this.mCurRawProcState;
    }

    void setReportedProcState(int repProcState) {
        this.mRepProcState = repProcState;
        this.mApp.getWindowProcessController().setReportedProcState(repProcState);
    }

    int getReportedProcState() {
        return this.mRepProcState;
    }

    void forceProcessStateUpTo(int newState) {
        if (this.mRepProcState > newState) {
            com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
            com.android.server.am.ActivityManagerService.boostPriorityForProcLockedSection();
            synchronized (activityManagerGlobalLock) {
                try {
                    int prevProcState = this.mRepProcState;
                    setReportedProcState(newState);
                    setCurProcState(newState);
                    setCurRawProcState(newState);
                    this.mService.mOomAdjuster.onProcessStateChanged(this.mApp, prevProcState);
                } catch (java.lang.Throwable th) {
                    com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                    throw th;
                }
            }
            com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
        }
    }

    void setSetProcState(int setProcState) {
        if (android.app.ActivityManager.isProcStateCached(this.mSetProcState) && !android.app.ActivityManager.isProcStateCached(setProcState)) {
            this.mCacheOomRankerUseCount++;
        }
        this.mSetProcState = setProcState;
    }

    int getSetProcState() {
        return this.mSetProcState;
    }

    void setLastStateTime(long lastStateTime) {
        this.mLastStateTime = lastStateTime;
    }

    long getLastStateTime() {
        return this.mLastStateTime;
    }

    void setSavedPriority(int savedPriority) {
        this.mSavedPriority = savedPriority;
    }

    int getSavedPriority() {
        return this.mSavedPriority;
    }

    void setServiceB(boolean serviceb) {
        this.mServiceB = serviceb;
    }

    boolean isServiceB() {
        return this.mServiceB;
    }

    void setServiceHighRam(boolean serviceHighRam) {
        this.mServiceHighRam = serviceHighRam;
    }

    boolean isServiceHighRam() {
        return this.mServiceHighRam;
    }

    void setNotCachedSinceIdle(boolean notCachedSinceIdle) {
        this.mNotCachedSinceIdle = notCachedSinceIdle;
    }

    boolean isNotCachedSinceIdle() {
        return this.mNotCachedSinceIdle;
    }

    void setHasStartedServices(boolean hasStartedServices) {
        this.mHasStartedServices = hasStartedServices;
        if (hasStartedServices) {
            this.mApp.mProfile.addHostingComponentType(128);
        } else {
            this.mApp.mProfile.clearHostingComponentType(128);
        }
    }

    boolean hasStartedServices() {
        return this.mHasStartedServices;
    }

    void setHasForegroundActivities(boolean hasForegroundActivities) {
        this.mHasForegroundActivities = hasForegroundActivities;
    }

    boolean hasForegroundActivities() {
        return this.mHasForegroundActivities;
    }

    void setRepForegroundActivities(boolean repForegroundActivities) {
        this.mRepForegroundActivities = repForegroundActivities;
    }

    boolean hasRepForegroundActivities() {
        return this.mRepForegroundActivities;
    }

    void setHasShownUi(boolean hasShownUi) {
        this.mHasShownUi = hasShownUi;
    }

    boolean hasShownUi() {
        return this.mHasShownUi;
    }

    void setHasTopUi(boolean hasTopUi) {
        this.mHasTopUi = hasTopUi;
        this.mApp.getWindowProcessController().setHasTopUi(hasTopUi);
    }

    boolean hasTopUi() {
        return this.mHasTopUi;
    }

    void setHasOverlayUi(boolean hasOverlayUi) {
        this.mHasOverlayUi = hasOverlayUi;
        this.mApp.getWindowProcessController().setHasOverlayUi(hasOverlayUi);
    }

    boolean hasOverlayUi() {
        return this.mHasOverlayUi;
    }

    boolean isRunningRemoteAnimation() {
        return this.mRunningRemoteAnimation;
    }

    void setRunningRemoteAnimation(boolean runningRemoteAnimation) {
        if (this.mRunningRemoteAnimation == runningRemoteAnimation) {
            return;
        }
        this.mRunningRemoteAnimation = runningRemoteAnimation;
        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_OOM_ADJ) {
            android.util.Slog.i(com.android.server.am.IActivityManagerServiceExt.TAG, "Setting runningRemoteAnimation=" + runningRemoteAnimation + " for pid=" + this.mApp.getPid());
        }
        this.mService.updateOomAdjLocked(this.mApp, 9);
    }

    void setProcStateChanged(boolean procStateChanged) {
        this.mProcStateChanged = procStateChanged;
    }

    boolean hasProcStateChanged() {
        return this.mProcStateChanged;
    }

    void setReportedInteraction(boolean reportedInteraction) {
        this.mReportedInteraction = reportedInteraction;
    }

    boolean hasReportedInteraction() {
        return this.mReportedInteraction;
    }

    void setInteractionEventTime(long interactionEventTime) {
        this.mInteractionEventTime = interactionEventTime;
        this.mApp.getWindowProcessController().setInteractionEventTime(interactionEventTime);
    }

    long getInteractionEventTime() {
        return this.mInteractionEventTime;
    }

    void setFgInteractionTime(long fgInteractionTime) {
        this.mFgInteractionTime = fgInteractionTime;
        this.mApp.getWindowProcessController().setFgInteractionTime(fgInteractionTime);
    }

    long getFgInteractionTime() {
        return this.mFgInteractionTime;
    }

    void setForcingToImportant(java.lang.Object forcingToImportant) {
        this.mForcingToImportant = forcingToImportant;
    }

    java.lang.Object getForcingToImportant() {
        return this.mForcingToImportant;
    }

    void setAdjSeq(int adjSeq) {
        this.mAdjSeq = adjSeq;
    }

    void decAdjSeq() {
        this.mAdjSeq--;
    }

    int getAdjSeq() {
        return this.mAdjSeq;
    }

    void setCompletedAdjSeq(int completedAdjSeq) {
        this.mCompletedAdjSeq = completedAdjSeq;
    }

    void decCompletedAdjSeq() {
        this.mCompletedAdjSeq--;
    }

    int getCompletedAdjSeq() {
        return this.mCompletedAdjSeq;
    }

    void setContainsCycle(boolean containsCycle) {
        this.mContainsCycle = containsCycle;
    }

    boolean containsCycle() {
        return this.mContainsCycle;
    }

    void setWhenUnimportant(long whenUnimportant) {
        this.mWhenUnimportant = whenUnimportant;
        this.mApp.getWindowProcessController().setWhenUnimportant(whenUnimportant);
    }

    long getWhenUnimportant() {
        return this.mWhenUnimportant;
    }

    void setLastTopTime(long lastTopTime) {
        this.mLastTopTime = lastTopTime;
    }

    long getLastTopTime() {
        return this.mLastTopTime;
    }

    boolean isEmpty() {
        return this.mCurProcState >= 19;
    }

    boolean isCached() {
        return this.mCurAdj >= 900;
    }

    int getCacheOomRankerUseCount() {
        return this.mCacheOomRankerUseCount;
    }

    void setSystemNoUi(boolean systemNoUi) {
        this.mSystemNoUi = systemNoUi;
    }

    boolean isSystemNoUi() {
        return this.mSystemNoUi;
    }

    void setAdjType(java.lang.String adjType) {
        this.mAdjType = adjType;
    }

    java.lang.String getAdjType() {
        return this.mAdjType;
    }

    void setAdjTypeCode(int adjTypeCode) {
        this.mAdjTypeCode = adjTypeCode;
    }

    int getAdjTypeCode() {
        return this.mAdjTypeCode;
    }

    void setAdjSource(java.lang.Object adjSource) {
        this.mAdjSource = adjSource;
    }

    java.lang.Object getAdjSource() {
        return this.mAdjSource;
    }

    void setAdjSourceProcState(int adjSourceProcState) {
        this.mAdjSourceProcState = adjSourceProcState;
    }

    int getAdjSourceProcState() {
        return this.mAdjSourceProcState;
    }

    void setAdjTarget(java.lang.Object adjTarget) {
        this.mAdjTarget = adjTarget;
    }

    java.lang.Object getAdjTarget() {
        return this.mAdjTarget;
    }

    boolean isReachable() {
        return this.mReachable;
    }

    void setReachable(boolean reachable) {
        this.mReachable = reachable;
    }

    void resetCachedInfo() {
        this.mCachedHasActivities = -1;
        this.mCachedIsHeavyWeight = -1;
        this.mCachedHasVisibleActivities = -1;
        this.mCachedIsHomeProcess = -1;
        this.mCachedIsPreviousProcess = -1;
        this.mCachedHasRecentTasks = -1;
        this.mCachedIsReceivingBroadcast = -1;
        this.mCachedAdj = -10000;
        this.mCachedForegroundActivities = false;
        this.mCachedProcState = 19;
        this.mCachedSchedGroup = 0;
        this.mCachedAdjType = null;
    }

    boolean getCachedHasActivities() {
        if (this.mCachedHasActivities == -1) {
            this.mCachedHasActivities = this.mApp.getWindowProcessController().hasActivities() ? 1 : 0;
            if (this.mCachedHasActivities == 1) {
                this.mApp.mProfile.addHostingComponentType(16);
            } else {
                this.mApp.mProfile.clearHostingComponentType(16);
            }
        }
        return this.mCachedHasActivities == 1;
    }

    boolean getCachedIsHeavyWeight() {
        if (this.mCachedIsHeavyWeight == -1) {
            this.mCachedIsHeavyWeight = this.mApp.getWindowProcessController().isHeavyWeightProcess() ? 1 : 0;
        }
        return this.mCachedIsHeavyWeight == 1;
    }

    boolean getCachedHasVisibleActivities() {
        if (this.mCachedHasVisibleActivities == -1) {
            this.mCachedHasVisibleActivities = this.mApp.getWindowProcessController().hasVisibleActivities() ? 1 : 0;
        }
        return this.mCachedHasVisibleActivities == 1;
    }

    boolean getCachedIsHomeProcess() {
        if (this.mCachedIsHomeProcess == -1) {
            if (this.mApp.getWindowProcessController().isHomeProcess()) {
                this.mCachedIsHomeProcess = 1;
                this.mService.mAppProfiler.mHasHomeProcess = true;
            } else {
                this.mCachedIsHomeProcess = 0;
            }
        }
        return this.mCachedIsHomeProcess == 1;
    }

    boolean getCachedIsPreviousProcess() {
        if (this.mCachedIsPreviousProcess == -1) {
            if (this.mApp.getWindowProcessController().isPreviousProcess()) {
                this.mCachedIsPreviousProcess = 1;
                this.mService.mAppProfiler.mHasPreviousProcess = true;
            } else {
                this.mCachedIsPreviousProcess = 0;
            }
        }
        return this.mCachedIsPreviousProcess == 1;
    }

    boolean getCachedHasRecentTasks() {
        if (this.mCachedHasRecentTasks == -1) {
            this.mCachedHasRecentTasks = this.mApp.getWindowProcessController().hasRecentTasks() ? 1 : 0;
        }
        return this.mCachedHasRecentTasks == 1;
    }

    boolean getCachedIsReceivingBroadcast(int[] outSchedGroup) {
        if (this.mCachedIsReceivingBroadcast == -1) {
            this.mCachedIsReceivingBroadcast = this.mService.isReceivingBroadcastLocked(this.mApp, outSchedGroup) ? 1 : 0;
            if (this.mCachedIsReceivingBroadcast == 1) {
                this.mCachedSchedGroup = outSchedGroup[0];
                this.mApp.mProfile.addHostingComponentType(32);
            } else {
                this.mApp.mProfile.clearHostingComponentType(32);
            }
        }
        return this.mCachedIsReceivingBroadcast == 1;
    }

    boolean getCachedCompatChange(int cachedCompatChangeId) {
        if (this.mCachedCompatChanges[cachedCompatChangeId] == -1) {
            this.mCachedCompatChanges[cachedCompatChangeId] = this.mService.mOomAdjuster.isChangeEnabled(cachedCompatChangeId, this.mApp.info, false) ? 1 : 0;
        }
        return this.mCachedCompatChanges[cachedCompatChangeId] == 1;
    }

    void computeOomAdjFromActivitiesIfNecessary(com.android.server.am.OomAdjuster.ComputeOomAdjWindowCallback computeOomAdjWindowCallback, int i, boolean z, boolean z2, int i2, int i3, int i4, int i5, int i6) {
        if (this.mCachedAdj != -10000) {
            return;
        }
        computeOomAdjWindowCallback.initialize(this.mApp, i, z, z2, i2, i3, i4, i5, i6);
        int iMin = java.lang.Math.min(99, this.mApp.getWindowProcessController().computeOomAdjFromActivities(computeOomAdjWindowCallback));
        this.mCachedAdj = computeOomAdjWindowCallback.adj;
        this.mCachedForegroundActivities = computeOomAdjWindowCallback.foregroundActivities;
        this.mCachedHasVisibleActivities = computeOomAdjWindowCallback.mHasVisibleActivities ? 1 : 0;
        this.mCachedProcState = computeOomAdjWindowCallback.procState;
        this.mCachedSchedGroup = computeOomAdjWindowCallback.schedGroup;
        this.mCachedAdjType = computeOomAdjWindowCallback.mAdjType;
        if (this.mCachedAdj == 100) {
            this.mCachedAdj += iMin;
        }
    }

    int getCachedAdj() {
        return this.mCachedAdj;
    }

    boolean getCachedForegroundActivities() {
        return this.mCachedForegroundActivities;
    }

    int getCachedProcState() {
        return this.mCachedProcState;
    }

    int getCachedSchedGroup() {
        return this.mCachedSchedGroup;
    }

    java.lang.String getCachedAdjType() {
        return this.mCachedAdjType;
    }

    boolean shouldScheduleLikeTopApp() {
        return this.mScheduleLikeTopApp;
    }

    void setScheduleLikeTopApp(boolean scheduleLikeTopApp) {
        this.mScheduleLikeTopApp = scheduleLikeTopApp;
    }

    long getFollowupUpdateUptimeMs() {
        return this.mFollowupUpdateUptimeMs;
    }

    void setFollowupUpdateUptimeMs(long updateUptimeMs) {
        this.mFollowupUpdateUptimeMs = updateUptimeMs;
    }

    public java.lang.String makeAdjReason() {
        if (this.mAdjSource != null || this.mAdjTarget != null) {
            java.lang.StringBuilder sb = new java.lang.StringBuilder(128);
            sb.append(' ');
            if (this.mAdjTarget instanceof android.content.ComponentName) {
                sb.append(((android.content.ComponentName) this.mAdjTarget).flattenToShortString());
            } else if (this.mAdjTarget != null) {
                sb.append(this.mAdjTarget.toString());
            } else {
                sb.append("{null}");
            }
            sb.append("<=");
            if (this.mAdjSource instanceof com.android.server.am.ProcessRecord) {
                sb.append("Proc{");
                sb.append(((com.android.server.am.ProcessRecord) this.mAdjSource).toShortString());
                sb.append("}");
            } else if (this.mAdjSource != null) {
                sb.append(this.mAdjSource.toString());
            } else {
                sb.append("{null}");
            }
            return sb.toString();
        }
        return null;
    }

    void onCleanupApplicationRecordLSP() {
        setHasForegroundActivities(false);
        this.mHasShownUi = false;
        this.mForcingToImportant = null;
        this.mVerifiedAdj = -10000;
        this.mSetAdj = -10000;
        this.mCurAdj = -10000;
        this.mSetRawAdj = -10000;
        this.mCurRawAdj = -10000;
        this.mSetCapability = 0;
        this.mCurCapability = 0;
        this.mSetSchedGroup = 0;
        this.mCurSchedGroup = 0;
        this.mSetProcState = 20;
        this.mCurRawProcState = 20;
        this.mCurProcState = 20;
        for (int i = 0; i < this.mCachedCompatChanges.length; i++) {
            this.mCachedCompatChanges[i] = -1;
        }
    }

    boolean isBackgroundRestricted() {
        return this.mBackgroundRestricted;
    }

    void setBackgroundRestricted(boolean restricted) {
        this.mBackgroundRestricted = restricted;
    }

    boolean isCurBoundByNonBgRestrictedApp() {
        return this.mCurBoundByNonBgRestrictedApp;
    }

    void setCurBoundByNonBgRestrictedApp(boolean bound) {
        this.mCurBoundByNonBgRestrictedApp = bound;
    }

    boolean isSetBoundByNonBgRestrictedApp() {
        return this.mSetBoundByNonBgRestrictedApp;
    }

    void setSetBoundByNonBgRestrictedApp(boolean bound) {
        this.mSetBoundByNonBgRestrictedApp = bound;
    }

    void updateLastInvisibleTime(boolean hasVisibleActivities) {
        if (hasVisibleActivities) {
            this.mLastInvisibleTime = Long.MAX_VALUE;
        } else if (this.mLastInvisibleTime == Long.MAX_VALUE) {
            this.mLastInvisibleTime = android.os.SystemClock.elapsedRealtime();
        }
    }

    long getLastInvisibleTime() {
        return this.mLastInvisibleTime;
    }

    void setNoKillOnBgRestrictedAndIdle(boolean shouldNotKill) {
        this.mNoKillOnBgRestrictedAndIdle = shouldNotKill;
    }

    boolean shouldNotKillOnBgRestrictedAndIdle() {
        return this.mNoKillOnBgRestrictedAndIdle;
    }

    void setSetCached(boolean cached) {
        this.mSetCached = cached;
    }

    boolean isSetCached() {
        return this.mSetCached;
    }

    void setSetNoKillOnBgRestrictedAndIdle(boolean shouldNotKill) {
        this.mSetNoKillOnBgRestrictedAndIdle = shouldNotKill;
    }

    boolean isSetNoKillOnBgRestrictedAndIdle() {
        return this.mSetNoKillOnBgRestrictedAndIdle;
    }

    void setLastCanKillOnBgRestrictedAndIdleTime(long now) {
        this.mLastCanKillOnBgRestrictedAndIdleTime = now;
    }

    long getLastCanKillOnBgRestrictedAndIdleTime() {
        return this.mLastCanKillOnBgRestrictedAndIdleTime;
    }

    public void setCacheOomRankerRss(long rss, long rssTimeMs) {
        this.mCacheOomRankerRss = rss;
        this.mCacheOomRankerRssTimeMs = rssTimeMs;
    }

    public long getCacheOomRankerRss() {
        return this.mCacheOomRankerRss;
    }

    public long getCacheOomRankerRssTimeMs() {
        return this.mCacheOomRankerRssTimeMs;
    }

    void dump(java.io.PrintWriter pw, java.lang.String prefix, long nowUptime) {
        if (this.mReportedInteraction || this.mFgInteractionTime != 0) {
            pw.print(prefix);
            pw.print("reportedInteraction=");
            pw.print(this.mReportedInteraction);
            if (this.mInteractionEventTime != 0) {
                pw.print(" time=");
                android.util.TimeUtils.formatDuration(this.mInteractionEventTime, android.os.SystemClock.elapsedRealtime(), pw);
            }
            if (this.mFgInteractionTime != 0) {
                pw.print(" fgInteractionTime=");
                android.util.TimeUtils.formatDuration(this.mFgInteractionTime, android.os.SystemClock.elapsedRealtime(), pw);
            }
            pw.println();
        }
        pw.print(prefix);
        pw.print("adjSeq=");
        pw.print(this.mAdjSeq);
        pw.print(" lruSeq=");
        pw.println(this.mApp.getLruSeq());
        pw.print(prefix);
        pw.print("oom adj: max=");
        pw.print(this.mMaxAdj);
        pw.print(" curRaw=");
        pw.print(this.mCurRawAdj);
        pw.print(" setRaw=");
        pw.print(this.mSetRawAdj);
        pw.print(" cur=");
        pw.print(this.mCurAdj);
        pw.print(" set=");
        pw.println(this.mSetAdj);
        pw.print(prefix);
        pw.print("mCurSchedGroup=");
        pw.print(this.mCurSchedGroup);
        pw.print(" setSchedGroup=");
        pw.print(this.mSetSchedGroup);
        pw.print(" systemNoUi=");
        pw.println(this.mSystemNoUi);
        pw.print(prefix);
        pw.print("curProcState=");
        pw.print(getCurProcState());
        pw.print(" mRepProcState=");
        pw.print(this.mRepProcState);
        pw.print(" setProcState=");
        pw.print(this.mSetProcState);
        pw.print(" lastStateTime=");
        android.util.TimeUtils.formatDuration(getLastStateTime(), nowUptime, pw);
        pw.println();
        pw.print(prefix);
        pw.print("curCapability=");
        android.app.ActivityManager.printCapabilitiesFull(pw, this.mCurCapability);
        pw.print(" setCapability=");
        android.app.ActivityManager.printCapabilitiesFull(pw, this.mSetCapability);
        pw.println();
        if (this.mBackgroundRestricted) {
            pw.print(" backgroundRestricted=");
            pw.print(this.mBackgroundRestricted);
            pw.print(" boundByNonBgRestrictedApp=");
            pw.print(this.mSetBoundByNonBgRestrictedApp);
        }
        pw.println();
        if (this.mHasShownUi || this.mApp.mProfile.hasPendingUiClean()) {
            pw.print(prefix);
            pw.print("hasShownUi=");
            pw.print(this.mHasShownUi);
            pw.print(" pendingUiClean=");
            pw.println(this.mApp.mProfile.hasPendingUiClean());
        }
        pw.print(prefix);
        pw.print("cached=");
        pw.print(isCached());
        pw.print(" empty=");
        pw.println(isEmpty());
        if (this.mServiceB) {
            pw.print(prefix);
            pw.print("serviceb=");
            pw.print(this.mServiceB);
            pw.print(" serviceHighRam=");
            pw.println(this.mServiceHighRam);
        }
        if (this.mNotCachedSinceIdle) {
            pw.print(prefix);
            pw.print("notCachedSinceIdle=");
            pw.print(this.mNotCachedSinceIdle);
            if (this.mService.mAppProfiler.isProfilingPss()) {
                pw.print(" initialIdlePss=");
            } else {
                pw.print(" initialIdleRss=");
            }
            pw.println(this.mApp.mProfile.getInitialIdlePssOrRss());
        }
        if (hasTopUi() || hasOverlayUi() || this.mRunningRemoteAnimation) {
            pw.print(prefix);
            pw.print("hasTopUi=");
            pw.print(hasTopUi());
            pw.print(" hasOverlayUi=");
            pw.print(hasOverlayUi());
            pw.print(" runningRemoteAnimation=");
            pw.println(this.mRunningRemoteAnimation);
        }
        if (this.mHasForegroundActivities || this.mRepForegroundActivities) {
            pw.print(prefix);
            pw.print("foregroundActivities=");
            pw.print(this.mHasForegroundActivities);
            pw.print(" (rep=");
            pw.print(this.mRepForegroundActivities);
            pw.println(")");
        }
        if (this.mSetProcState > 10) {
            pw.print(prefix);
            pw.print("whenUnimportant=");
            android.util.TimeUtils.formatDuration(this.mWhenUnimportant - nowUptime, pw);
            pw.println();
        }
        if (this.mLastTopTime > 0) {
            pw.print(prefix);
            pw.print("lastTopTime=");
            android.util.TimeUtils.formatDuration(this.mLastTopTime, nowUptime, pw);
            pw.println();
        }
        if (this.mLastInvisibleTime > 0 && this.mLastInvisibleTime < Long.MAX_VALUE) {
            pw.print(prefix);
            pw.print("lastInvisibleTime=");
            long elapsedRealtimeNow = android.os.SystemClock.elapsedRealtime();
            long currentTimeNow = java.lang.System.currentTimeMillis();
            long lastInvisibleCurrentTime = (currentTimeNow - elapsedRealtimeNow) + this.mLastInvisibleTime;
            android.util.TimeUtils.dumpTimeWithDelta(pw, lastInvisibleCurrentTime, currentTimeNow);
            pw.println();
        }
        if (this.mHasStartedServices) {
            pw.print(prefix);
            pw.print("hasStartedServices=");
            pw.println(this.mHasStartedServices);
        }
    }
}
