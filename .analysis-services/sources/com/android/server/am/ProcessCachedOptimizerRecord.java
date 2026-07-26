package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
final class ProcessCachedOptimizerRecord {
    static final java.lang.String IS_FROZEN = "isFrozen";
    private final com.android.server.am.ProcessRecord mApp;
    private long mEarliestFreezableTimeMillis;
    private boolean mForceCompact;
    private boolean mFreezeExempt;
    private boolean mFreezeSticky;
    private long mFreezeUnfreezeTime;
    boolean mFreezerOverride;
    private boolean mFrozen;
    private boolean mHasCollectedFrozenPSS;
    private com.android.server.am.CachedAppOptimizer.CompactProfile mLastCompactProfile;
    private long mLastCompactTime;
    private int mLastOomAdjChangeReason;
    private long mLastUsedTimeout;
    private boolean mPendingCompact;
    private boolean mPendingFreeze;
    private final com.android.server.am.ActivityManagerGlobalLock mProcLock;
    private com.android.server.am.CachedAppOptimizer.CompactProfile mReqCompactProfile;
    private com.android.server.am.CachedAppOptimizer.CompactSource mReqCompactSource;
    private boolean mShouldNotFreeze;

    long getLastCompactTime() {
        return this.mLastCompactTime;
    }

    void setLastCompactTime(long lastCompactTime) {
        this.mLastCompactTime = lastCompactTime;
    }

    com.android.server.am.CachedAppOptimizer.CompactProfile getReqCompactProfile() {
        return this.mReqCompactProfile;
    }

    void setReqCompactProfile(com.android.server.am.CachedAppOptimizer.CompactProfile reqCompactProfile) {
        this.mReqCompactProfile = reqCompactProfile;
    }

    com.android.server.am.CachedAppOptimizer.CompactSource getReqCompactSource() {
        return this.mReqCompactSource;
    }

    void setReqCompactSource(com.android.server.am.CachedAppOptimizer.CompactSource stat) {
        this.mReqCompactSource = stat;
    }

    void setLastOomAdjChangeReason(int reason) {
        this.mLastOomAdjChangeReason = reason;
    }

    int getLastOomAdjChangeReason() {
        return this.mLastOomAdjChangeReason;
    }

    com.android.server.am.CachedAppOptimizer.CompactProfile getLastCompactProfile() {
        if (this.mLastCompactProfile == null) {
            this.mLastCompactProfile = com.android.server.am.CachedAppOptimizer.CompactProfile.SOME;
        }
        return this.mLastCompactProfile;
    }

    void setLastCompactProfile(com.android.server.am.CachedAppOptimizer.CompactProfile lastCompactProfile) {
        this.mLastCompactProfile = lastCompactProfile;
    }

    boolean hasPendingCompact() {
        return this.mPendingCompact;
    }

    void setHasPendingCompact(boolean pendingCompact) {
        this.mPendingCompact = pendingCompact;
    }

    boolean isForceCompact() {
        return this.mForceCompact;
    }

    void setForceCompact(boolean forceCompact) {
        this.mForceCompact = forceCompact;
    }

    boolean isFrozen() {
        return this.mFrozen;
    }

    void setFrozen(boolean frozen) {
        this.mFrozen = frozen;
    }

    void setFreezeSticky(boolean sticky) {
        this.mFreezeSticky = sticky;
    }

    boolean isFreezeSticky() {
        return this.mFreezeSticky;
    }

    boolean skipPSSCollectionBecauseFrozen() {
        boolean collected = this.mHasCollectedFrozenPSS;
        if (!this.mFrozen) {
            return false;
        }
        this.mHasCollectedFrozenPSS = true;
        return collected;
    }

    void setHasCollectedFrozenPSS(boolean collected) {
        this.mHasCollectedFrozenPSS = collected;
    }

    boolean hasFreezerOverride() {
        return this.mFreezerOverride;
    }

    void setFreezerOverride(boolean freezerOverride) {
        this.mFreezerOverride = freezerOverride;
    }

    long getFreezeUnfreezeTime() {
        return this.mFreezeUnfreezeTime;
    }

    void setFreezeUnfreezeTime(long freezeUnfreezeTime) {
        this.mFreezeUnfreezeTime = freezeUnfreezeTime;
    }

    boolean shouldNotFreeze() {
        return this.mShouldNotFreeze;
    }

    void setShouldNotFreeze(boolean shouldNotFreeze) {
        setShouldNotFreeze(shouldNotFreeze, false);
    }

    boolean setShouldNotFreeze(boolean shouldNotFreeze, boolean dryRun) {
        if (dryRun) {
            return this.mFrozen && !shouldNotFreeze;
        }
        this.mShouldNotFreeze = shouldNotFreeze;
        return false;
    }

    long getEarliestFreezableTime() {
        return this.mEarliestFreezableTimeMillis;
    }

    void setEarliestFreezableTime(long earliestFreezableTimeMillis) {
        this.mEarliestFreezableTimeMillis = earliestFreezableTimeMillis;
    }

    long getLastUsedTimeout() {
        return this.mLastUsedTimeout;
    }

    void setLastUsedTimeout(long lastUsedTimeout) {
        this.mLastUsedTimeout = lastUsedTimeout;
    }

    boolean isFreezeExempt() {
        return this.mFreezeExempt;
    }

    void setPendingFreeze(boolean freeze) {
        this.mPendingFreeze = freeze;
    }

    boolean isPendingFreeze() {
        return this.mPendingFreeze;
    }

    void setFreezeExempt(boolean exempt) {
        this.mFreezeExempt = exempt;
    }

    ProcessCachedOptimizerRecord(com.android.server.am.ProcessRecord app) {
        this.mApp = app;
        this.mProcLock = app.mService.mProcLock;
    }

    void init(long nowUptime) {
        this.mFreezeUnfreezeTime = nowUptime;
    }

    @dalvik.annotation.optimization.NeverCompile
    void dump(java.io.PrintWriter pw, java.lang.String prefix, long nowUptime) {
        pw.print(prefix);
        pw.print("lastCompactTime=");
        pw.print(this.mLastCompactTime);
        pw.print(" lastCompactProfile=");
        pw.println(this.mLastCompactProfile);
        pw.print(prefix);
        pw.print("hasPendingCompaction=");
        pw.print(this.mPendingCompact);
        pw.print(prefix);
        pw.print("isFreezeExempt=");
        pw.print(this.mFreezeExempt);
        pw.print(" isPendingFreeze=");
        pw.print(this.mPendingFreeze);
        pw.print(" isFrozen=");
        pw.println(this.mFrozen);
        pw.print(prefix);
        pw.print("earliestFreezableTimeMs=");
        android.util.TimeUtils.formatDuration(this.mEarliestFreezableTimeMillis, nowUptime, pw);
        pw.println();
    }
}
