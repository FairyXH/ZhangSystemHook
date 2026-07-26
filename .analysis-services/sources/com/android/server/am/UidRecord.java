package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public final class UidRecord {
    static final int CHANGE_ACTIVE = 4;
    static final int CHANGE_CACHED = 8;
    static final int CHANGE_CAPABILITY = 32;
    static final int CHANGE_GONE = 1;
    static final int CHANGE_IDLE = 2;
    static final int CHANGE_PROCADJ = 64;
    static final int CHANGE_PROCSTATE = Integer.MIN_VALUE;
    static final int CHANGE_UNCACHED = 16;
    private static int[] ORIG_ENUMS = {1, 2, 4, 8, 16, 32, Integer.MIN_VALUE};
    private static int[] PROTO_ENUMS = {0, 1, 2, 3, 4, 5, 6};
    long curProcStateSeq;
    volatile boolean hasInternetPermission;
    long lastNetworkUpdatedProcStateSeq;
    private int mCurAdj;
    private boolean mCurAllowList;
    private int mCurCapability;
    private int mCurProcState;
    private boolean mEphemeral;
    private boolean mForegroundServices;
    private boolean mIdle;
    private long mLastBackgroundTime;
    private long mLastIdleTimeIfStillIdle;
    private int mLastReportedChange;
    private int mNumProcs;
    private boolean mProcAdjChanged;
    private final com.android.server.am.ActivityManagerGlobalLock mProcLock;
    private long mRealLastIdleTime;
    private final com.android.server.am.ActivityManagerService mService;
    private int mSetAdj;
    private boolean mSetAllowList;
    private int mSetCapability;
    private boolean mSetIdle;
    private final int mUid;
    private boolean mUidIsFrozen;
    volatile long procStateSeqWaitingForNetwork;
    private int mSetProcState = 20;
    private android.util.ArraySet<com.android.server.am.ProcessRecord> mProcRecords = new android.util.ArraySet<>();
    final java.lang.Object networkStateLock = new java.lang.Object();
    final com.android.server.am.UidObserverController.ChangeRecord pendingChange = new com.android.server.am.UidObserverController.ChangeRecord();

    public UidRecord(int uid, com.android.server.am.ActivityManagerService service) {
        this.mUid = uid;
        this.mService = service;
        this.mProcLock = service != null ? service.mProcLock : null;
        this.mIdle = true;
        reset();
    }

    int getUid() {
        return this.mUid;
    }

    int getCurProcState() {
        return this.mCurProcState;
    }

    void setCurProcState(int curProcState) {
        this.mCurProcState = curProcState;
    }

    int getSetProcState() {
        return this.mSetProcState;
    }

    void setSetProcState(int setProcState) {
        this.mSetProcState = setProcState;
    }

    void noteProcAdjChanged() {
        this.mProcAdjChanged = true;
    }

    void clearProcAdjChanged() {
        this.mProcAdjChanged = false;
    }

    boolean getProcAdjChanged() {
        return this.mProcAdjChanged;
    }

    int getMinProcAdj() {
        int minAdj = 1001;
        for (int i = this.mProcRecords.size() - 1; i >= 0; i--) {
            int adj = this.mProcRecords.valueAt(i).getSetAdj();
            if (adj < minAdj) {
                minAdj = adj;
            }
        }
        return minAdj;
    }

    int getCurCapability() {
        return this.mCurCapability;
    }

    void setCurCapability(int curCapability) {
        this.mCurCapability = curCapability;
    }

    int getSetCapability() {
        return this.mSetCapability;
    }

    void setSetCapability(int setCapability) {
        this.mSetCapability = setCapability;
    }

    long getLastBackgroundTime() {
        return this.mLastBackgroundTime;
    }

    void setLastBackgroundTime(long lastBackgroundTime) {
        this.mLastBackgroundTime = lastBackgroundTime;
    }

    long getLastIdleTimeIfStillIdle() {
        return this.mLastIdleTimeIfStillIdle;
    }

    long getRealLastIdleTime() {
        return this.mRealLastIdleTime;
    }

    void setLastIdleTime(long lastIdleTime) {
        this.mLastIdleTimeIfStillIdle = lastIdleTime;
        if (lastIdleTime > 0) {
            this.mRealLastIdleTime = lastIdleTime;
        }
    }

    boolean isEphemeral() {
        return this.mEphemeral;
    }

    void setEphemeral(boolean ephemeral) {
        this.mEphemeral = ephemeral;
    }

    boolean hasForegroundServices() {
        return this.mForegroundServices;
    }

    void setForegroundServices(boolean foregroundServices) {
        this.mForegroundServices = foregroundServices;
    }

    boolean isCurAllowListed() {
        return this.mCurAllowList;
    }

    void setCurAllowListed(boolean curAllowList) {
        this.mCurAllowList = curAllowList;
    }

    boolean isSetAllowListed() {
        return this.mSetAllowList;
    }

    void setSetAllowListed(boolean setAllowlist) {
        this.mSetAllowList = setAllowlist;
    }

    boolean isIdle() {
        return this.mIdle;
    }

    void setIdle(boolean idle) {
        this.mIdle = idle;
    }

    boolean isSetIdle() {
        return this.mSetIdle;
    }

    void setSetIdle(boolean setIdle) {
        this.mSetIdle = setIdle;
    }

    int getNumOfProcs() {
        return this.mProcRecords.size();
    }

    void forEachProcess(java.util.function.Consumer<com.android.server.am.ProcessRecord> callback) {
        for (int i = this.mProcRecords.size() - 1; i >= 0; i--) {
            callback.accept(this.mProcRecords.valueAt(i));
        }
    }

    com.android.server.am.ProcessRecord getProcessRecordByIndex(int idx) {
        return this.mProcRecords.valueAt(idx);
    }

    com.android.server.am.ProcessRecord getProcessInPackage(java.lang.String packageName) {
        for (int i = this.mProcRecords.size() - 1; i >= 0; i--) {
            com.android.server.am.ProcessRecord app = this.mProcRecords.valueAt(i);
            if (app != null && android.text.TextUtils.equals(app.info.packageName, packageName)) {
                return app;
            }
        }
        return null;
    }

    public boolean areAllProcessesFrozen(com.android.server.am.ProcessRecord excluding) {
        for (int i = this.mProcRecords.size() - 1; i >= 0; i--) {
            com.android.server.am.ProcessRecord app = this.mProcRecords.valueAt(i);
            com.android.server.am.ProcessCachedOptimizerRecord opt = app.mOptRecord;
            if (excluding != app && !opt.isFrozen()) {
                return false;
            }
        }
        return true;
    }

    public boolean areAllProcessesFrozen() {
        return areAllProcessesFrozen(null);
    }

    public void setFrozen(boolean frozen) {
        this.mUidIsFrozen = frozen;
    }

    public boolean isFrozen() {
        return this.mUidIsFrozen;
    }

    void addProcess(com.android.server.am.ProcessRecord app) {
        this.mProcRecords.add(app);
    }

    void removeProcess(com.android.server.am.ProcessRecord app) {
        this.mProcRecords.remove(app);
    }

    void setLastReportedChange(int lastReportedChange) {
        this.mLastReportedChange = lastReportedChange;
    }

    void reset() {
        setCurProcState(19);
        this.mForegroundServices = false;
        this.mCurCapability = 0;
    }

    public void updateHasInternetPermission() {
        this.hasInternetPermission = android.app.ActivityManager.checkUidPermission("android.permission.INTERNET", this.mUid) == 0;
    }

    void dumpDebug(android.util.proto.ProtoOutputStream proto, long fieldId) {
        long token = proto.start(fieldId);
        proto.write(1120986464257L, this.mUid);
        proto.write(1159641169922L, com.android.server.am.ProcessList.makeProcStateProtoEnum(this.mCurProcState));
        proto.write(1133871366147L, this.mEphemeral);
        proto.write(1133871366148L, this.mForegroundServices);
        proto.write(1133871366149L, this.mCurAllowList);
        android.util.proto.ProtoUtils.toDuration(proto, 1146756268038L, this.mLastBackgroundTime, android.os.SystemClock.elapsedRealtime());
        proto.write(1133871366151L, this.mIdle);
        if (this.mLastReportedChange != 0) {
            android.util.proto.ProtoUtils.writeBitWiseFlagsToProtoEnum(proto, 2259152797704L, this.mLastReportedChange, ORIG_ENUMS, PROTO_ENUMS);
        }
        proto.write(1120986464265L, this.mNumProcs);
        long seqToken = proto.start(1146756268042L);
        proto.write(1112396529665L, this.curProcStateSeq);
        proto.write(1112396529666L, this.lastNetworkUpdatedProcStateSeq);
        proto.end(seqToken);
        proto.end(token);
    }

    public java.lang.String toString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder(128);
        sb.append("UidRecord{");
        sb.append(java.lang.Integer.toHexString(java.lang.System.identityHashCode(this)));
        sb.append(' ');
        android.os.UserHandle.formatUid(sb, this.mUid);
        sb.append(' ');
        sb.append(com.android.server.am.ProcessList.makeProcStateString(this.mCurProcState));
        if (this.mEphemeral) {
            sb.append(" ephemeral");
        }
        if (this.mForegroundServices) {
            sb.append(" fgServices");
        }
        if (this.mCurAllowList) {
            sb.append(" allowlist");
        }
        if (this.mLastBackgroundTime > 0) {
            sb.append(" bg:");
            android.util.TimeUtils.formatDuration(android.os.SystemClock.elapsedRealtime() - this.mLastBackgroundTime, sb);
        }
        if (this.mIdle) {
            sb.append(" idle");
        }
        if (this.mLastReportedChange != 0) {
            sb.append(" change:");
            boolean printed = false;
            if ((this.mLastReportedChange & 1) != 0) {
                printed = true;
                sb.append("gone");
            }
            if ((this.mLastReportedChange & 2) != 0) {
                if (printed) {
                    sb.append("|");
                }
                printed = true;
                sb.append("idle");
            }
            if ((this.mLastReportedChange & 4) != 0) {
                if (printed) {
                    sb.append("|");
                }
                printed = true;
                sb.append(com.android.server.pm.verify.domain.DomainVerificationPersistence.TAG_ACTIVE);
            }
            if ((this.mLastReportedChange & 8) != 0) {
                if (printed) {
                    sb.append("|");
                }
                printed = true;
                sb.append("cached");
            }
            if ((this.mLastReportedChange & 16) != 0) {
                if (printed) {
                    sb.append("|");
                }
                sb.append("uncached");
            }
            if ((this.mLastReportedChange & Integer.MIN_VALUE) != 0) {
                if (printed) {
                    sb.append("|");
                }
                sb.append("procstate");
            }
            if ((this.mLastReportedChange & 64) != 0) {
                if (printed) {
                    sb.append("|");
                }
                sb.append("procadj");
            }
        }
        sb.append(" procs:");
        sb.append(this.mNumProcs);
        sb.append(" seq(");
        sb.append(this.curProcStateSeq);
        sb.append(",");
        sb.append(this.lastNetworkUpdatedProcStateSeq);
        sb.append(")}");
        sb.append(" caps=");
        android.app.ActivityManager.printCapabilitiesSummary(sb, this.mCurCapability);
        return sb.toString();
    }
}
