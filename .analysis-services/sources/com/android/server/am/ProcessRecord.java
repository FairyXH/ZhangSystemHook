package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public class ProcessRecord implements com.android.server.wm.WindowProcessListener {
    static final java.lang.String TAG = "ActivityManager";
    static java.util.HashMap<java.lang.Integer, java.lang.Boolean> sBackAnrForPids = new java.util.HashMap<>();
    final boolean appZygote;
    public volatile android.content.pm.ApplicationInfo info;
    public final boolean isSdkSandbox;
    final boolean isolated;
    private final android.util.ArrayMap<android.os.Binder, android.app.BackgroundStartPrivileges> mBackgroundStartPrivileges;
    private android.app.BackgroundStartPrivileges mBackgroundStartPrivilegesMerged;
    private volatile long mBindApplicationTime;
    private volatile boolean mBindMountPending;
    private android.content.res.CompatibilityInfo mCompat;
    private android.os.IBinder.DeathRecipient mDeathRecipient;
    private boolean mDebugging;
    private long[] mDisabledCompatChanges;
    private int mDyingPid;
    final com.android.server.am.ProcessErrorStateRecord mErrorState;
    private int[] mGids;
    private volatile com.android.server.am.HostingRecord mHostingRecord;
    android.util.IntArray mHwuiTaskThreads;
    private boolean mInFullBackup;
    private com.android.server.am.ActiveInstrumentation mInstr;
    private java.lang.String mInstructionSet;
    private java.lang.String mIsolatedEntryPoint;
    private java.lang.String[] mIsolatedEntryPointArgs;
    private long mKillTime;
    private boolean mKilled;
    private boolean mKilledByAm;
    private long mLastActivityTime;
    final com.android.server.am.OomAdjusterModernImpl.ProcessRecordNode[] mLinkedNodes;
    private long[] mLoggableCompatChanges;
    private int mLruSeq;
    private volatile int mMountMode;
    private android.app.IApplicationThread mOnewayThread;
    final com.android.server.am.ProcessCachedOptimizerRecord mOptRecord;
    private boolean mPendingFinishAttach;
    private boolean mPendingStart;
    private volatile boolean mPersistent;
    public int mPid;
    private android.util.ArraySet<java.lang.String> mPkgDeps;
    public final com.android.server.am.PackageList mPkgList;
    volatile com.android.server.am.ProcessRecord mPredecessor;
    private final com.android.server.am.ActivityManagerGlobalLock mProcLock;
    volatile boolean mProcessGroupCreated;
    private com.android.server.am.IProcessRecordExt mProcessRecordExt;
    final com.android.server.am.ProcessProfileRecord mProfile;
    final com.android.server.am.ProcessProviderRecord mProviders;
    final com.android.server.am.ProcessReceiverRecord mReceivers;
    private volatile boolean mRemoved;
    private int mRenderThreadTid;
    private java.lang.String mRequiredAbi;
    boolean mRunningRemoteAnimation;
    private volatile java.lang.String mSeInfo;
    final com.android.server.am.ActivityManagerService mService;
    public final com.android.server.am.ProcessServiceRecord mServices;
    private java.lang.String mShortStringName;
    volatile boolean mSkipProcessGroupCreation;
    private com.android.server.am.IProcessRecordSocExt mSocExt;
    private volatile long mStartElapsedTime;
    private long mStartSeq;
    private volatile int mStartUid;
    private volatile long mStartUptime;
    public com.android.server.am.ProcessStateRecord mState;
    private java.lang.String mStringName;
    volatile com.android.server.am.ProcessRecord mSuccessor;
    java.lang.Runnable mSuccessorStartRunnable;
    public android.app.IApplicationThread mThread;
    private com.oplus.uifirst.IOplusUIFirstManagerExt mUIFirstManagerExt;
    private com.android.server.am.UidRecord mUidRecord;
    private boolean mUnlocked;
    private boolean mUsingWrapper;
    private boolean mWaitedForDebugger;
    private java.lang.String mWaitingToKill;
    volatile boolean mWasForceStopped;
    private final com.android.server.wm.WindowProcessController mWindowProcessController;
    private com.android.server.am.ProcessRecord.ProcessRecordWrapper mWrapper;
    final android.content.pm.ProcessInfo processInfo;
    public final java.lang.String processName;
    final java.lang.String sdkSandboxClientAppPackage;
    final java.lang.String sdkSandboxClientAppVolumeUuid;
    public final int uid;
    public final int userId;

    void setStartParams(int startUid, com.android.server.am.HostingRecord hostingRecord, java.lang.String seInfo, long startUptime, long startElapsedTime) {
        this.mStartUid = startUid;
        this.mHostingRecord = hostingRecord;
        this.mSeInfo = seInfo;
        this.mStartUptime = startUptime;
        this.mStartElapsedTime = startElapsedTime;
    }

    void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        long nowUptime = android.os.SystemClock.uptimeMillis();
        long nowElapsedTime = android.os.SystemClock.elapsedRealtime();
        pw.print(prefix);
        pw.print("user #");
        pw.print(this.userId);
        pw.print(" uid=");
        pw.print(this.info.uid);
        if (this.uid != this.info.uid) {
            pw.print(" ISOLATED uid=");
            pw.print(this.uid);
        }
        pw.print(" gids={");
        if (this.mGids != null) {
            for (int gi = 0; gi < this.mGids.length; gi++) {
                if (gi != 0) {
                    pw.print(", ");
                }
                pw.print(this.mGids[gi]);
            }
        }
        pw.println("}");
        if (this.processInfo != null) {
            pw.print(prefix);
            pw.println("processInfo:");
            if (this.processInfo.deniedPermissions != null) {
                for (int i = 0; i < this.processInfo.deniedPermissions.size(); i++) {
                    pw.print(prefix);
                    pw.print("  deny: ");
                    pw.println((java.lang.String) this.processInfo.deniedPermissions.valueAt(i));
                }
            }
            if (this.processInfo.gwpAsanMode != -1) {
                pw.print(prefix);
                pw.println("  gwpAsanMode=" + this.processInfo.gwpAsanMode);
            }
            if (this.processInfo.memtagMode != -1) {
                pw.print(prefix);
                pw.println("  memtagMode=" + this.processInfo.memtagMode);
            }
        }
        pw.print(prefix);
        pw.print("mRequiredAbi=");
        pw.print(this.mRequiredAbi);
        pw.print(" instructionSet=");
        pw.println(this.mInstructionSet);
        if (this.info.className != null) {
            pw.print(prefix);
            pw.print("class=");
            pw.println(this.info.className);
        }
        if (this.info.manageSpaceActivityName != null) {
            pw.print(prefix);
            pw.print("manageSpaceActivityName=");
            pw.println(this.info.manageSpaceActivityName);
        }
        pw.print(prefix);
        pw.print("dir=");
        pw.print(this.info.sourceDir);
        pw.print(" publicDir=");
        pw.print(this.info.publicSourceDir);
        pw.print(" data=");
        pw.println(this.info.dataDir);
        this.mPkgList.dump(pw, prefix);
        if (this.mPkgDeps != null) {
            pw.print(prefix);
            pw.print("packageDependencies={");
            for (int i2 = 0; i2 < this.mPkgDeps.size(); i2++) {
                if (i2 > 0) {
                    pw.print(", ");
                }
                pw.print(this.mPkgDeps.valueAt(i2));
            }
            pw.println("}");
        }
        pw.print(prefix);
        pw.print("compat=");
        pw.println(this.mCompat);
        if (this.mInstr != null) {
            pw.print(prefix);
            pw.print("mInstr=");
            pw.println(this.mInstr);
        }
        pw.print(prefix);
        pw.print("thread=");
        pw.println(this.mThread);
        pw.print(prefix);
        pw.print("pid=");
        pw.println(this.mPid);
        pw.print(prefix);
        pw.print("lastActivityTime=");
        android.util.TimeUtils.formatDuration(this.mLastActivityTime, nowUptime, pw);
        pw.print(prefix);
        pw.print("startUpTime=");
        android.util.TimeUtils.formatDuration(this.mStartUptime, nowUptime, pw);
        pw.print(prefix);
        pw.print("startElapsedTime=");
        android.util.TimeUtils.formatDuration(this.mStartElapsedTime, nowElapsedTime, pw);
        pw.println();
        if (this.mPersistent || this.mRemoved) {
            pw.print(prefix);
            pw.print("persistent=");
            pw.print(this.mPersistent);
            pw.print(" removed=");
            pw.println(this.mRemoved);
        }
        if (this.mDebugging) {
            pw.print(prefix);
            pw.print("mDebugging=");
            pw.println(this.mDebugging);
        }
        if (this.mPendingStart) {
            pw.print(prefix);
            pw.print("pendingStart=");
            pw.println(this.mPendingStart);
        }
        pw.print(prefix);
        pw.print("startSeq=");
        pw.println(this.mStartSeq);
        pw.print(prefix);
        pw.print("mountMode=");
        pw.println(android.util.DebugUtils.valueToString(com.android.internal.os.Zygote.class, "MOUNT_EXTERNAL_", this.mMountMode));
        if (this.mKilled || this.mKilledByAm || this.mWaitingToKill != null) {
            pw.print(prefix);
            pw.print("killed=");
            pw.print(this.mKilled);
            pw.print(" killedByAm=");
            pw.print(this.mKilledByAm);
            pw.print(" waitingToKill=");
            pw.println(this.mWaitingToKill);
        }
        if (this.mIsolatedEntryPoint != null || this.mIsolatedEntryPointArgs != null) {
            pw.print(prefix);
            pw.print("isolatedEntryPoint=");
            pw.println(this.mIsolatedEntryPoint);
            pw.print(prefix);
            pw.print("isolatedEntryPointArgs=");
            pw.println(java.util.Arrays.toString(this.mIsolatedEntryPointArgs));
        }
        if (this.mState.getSetProcState() > 10) {
            this.mProfile.dumpCputime(pw, prefix);
        }
        this.mProfile.dumpPss(pw, prefix, nowUptime);
        this.mState.dump(pw, prefix, nowUptime);
        this.mErrorState.dump(pw, prefix, nowUptime);
        this.mServices.dump(pw, prefix, nowUptime);
        this.mProviders.dump(pw, prefix, nowUptime);
        this.mReceivers.dump(pw, prefix, nowUptime);
        this.mOptRecord.dump(pw, prefix, nowUptime);
        this.mWindowProcessController.dump(pw, prefix);
        this.mProcessRecordExt.dump(pw, prefix, nowUptime);
    }

    ProcessRecord(com.android.server.am.ActivityManagerService _service, android.content.pm.ApplicationInfo _info, java.lang.String _processName, int _uid) {
        this(_service, _info, _processName, _uid, null, -1, null);
    }

    /* JADX WARN: Multi-variable type inference failed */
    ProcessRecord(com.android.server.am.ActivityManagerService activityManagerService, android.content.pm.ApplicationInfo applicationInfo, java.lang.String str, int i, java.lang.String str2, int i2, java.lang.String str3) {
        android.content.pm.ProcessInfo processInfo;
        this.mPkgList = new com.android.server.am.PackageList(this);
        this.mBackgroundStartPrivileges = new android.util.ArrayMap<>();
        this.mBackgroundStartPrivilegesMerged = android.app.BackgroundStartPrivileges.NONE;
        this.mHwuiTaskThreads = new android.util.IntArray(2);
        this.mLinkedNodes = new com.android.server.am.OomAdjusterModernImpl.ProcessRecordNode[2];
        this.mWrapper = new com.android.server.am.ProcessRecord.ProcessRecordWrapper();
        this.mProcessRecordExt = (com.android.server.am.IProcessRecordExt) system.ext.loader.core.ExtLoader.type(com.android.server.am.IProcessRecordExt.class).base(this).create();
        this.mSocExt = (com.android.server.am.IProcessRecordSocExt) system.ext.loader.core.ExtLoader.type(com.android.server.am.IProcessRecordSocExt.class).base(this).create();
        this.mUIFirstManagerExt = (com.oplus.uifirst.IOplusUIFirstManagerExt) system.ext.loader.core.ExtLoader.type(com.oplus.uifirst.IOplusUIFirstManagerExt.class).create();
        this.mService = activityManagerService;
        this.mProcLock = activityManagerService.mProcLock;
        this.info = applicationInfo;
        android.content.pm.ProcessInfo processInfo2 = null;
        if (activityManagerService.mPackageManagerInt == null) {
            processInfo = null;
        } else {
            if (i2 > 0) {
                android.util.ArrayMap<java.lang.String, android.content.pm.ProcessInfo> processesForUid = activityManagerService.mPackageManagerInt.getProcessesForUid(i2);
                if (processesForUid != null) {
                    processInfo2 = processesForUid.get(str3);
                }
            } else {
                android.util.ArrayMap<java.lang.String, android.content.pm.ProcessInfo> processesForUid2 = activityManagerService.mPackageManagerInt.getProcessesForUid(i);
                if (processesForUid2 != null) {
                    processInfo2 = processesForUid2.get(str);
                }
            }
            if (processInfo2 != null && processInfo2.deniedPermissions == null && processInfo2.gwpAsanMode == -1 && processInfo2.memtagMode == -1 && processInfo2.nativeHeapZeroInitialized == -1) {
                processInfo = null;
            } else {
                processInfo = processInfo2;
            }
        }
        this.processInfo = processInfo;
        this.isolated = android.os.Process.isIsolated(i);
        this.isSdkSandbox = android.os.Process.isSdkSandboxUid(i);
        this.appZygote = android.os.UserHandle.getAppId(i) >= 90000 && android.os.UserHandle.getAppId(i) <= 98999;
        this.uid = i;
        this.userId = android.os.UserHandle.getUserId(i);
        this.processName = str;
        this.sdkSandboxClientAppPackage = str2;
        if (this.isSdkSandbox) {
            android.content.pm.ApplicationInfo clientInfoForSdkSandbox = getClientInfoForSdkSandbox();
            this.sdkSandboxClientAppVolumeUuid = clientInfoForSdkSandbox != null ? clientInfoForSdkSandbox.volumeUuid : null;
        } else {
            this.sdkSandboxClientAppVolumeUuid = null;
        }
        this.mPersistent = false;
        this.mRemoved = false;
        this.mProfile = new com.android.server.am.ProcessProfileRecord(this);
        this.mServices = new com.android.server.am.ProcessServiceRecord(this);
        this.mProviders = new com.android.server.am.ProcessProviderRecord(this);
        this.mReceivers = new com.android.server.am.ProcessReceiverRecord(this);
        this.mErrorState = new com.android.server.am.ProcessErrorStateRecord(this);
        this.mState = new com.android.server.am.ProcessStateRecord(this);
        this.mOptRecord = new com.android.server.am.ProcessCachedOptimizerRecord(this);
        long jUptimeMillis = android.os.SystemClock.uptimeMillis();
        this.mProfile.init(jUptimeMillis);
        this.mOptRecord.init(jUptimeMillis);
        this.mState.init(jUptimeMillis);
        this.mWindowProcessController = new com.android.server.wm.WindowProcessController(this.mService.mActivityTaskManager, this.info, this.processName, this.uid, this.userId, this, this);
        this.mPkgList.put(applicationInfo.packageName, new com.android.internal.app.procstats.ProcessStats.ProcessStateHolder(applicationInfo.longVersionCode));
        updateProcessRecordNodes(this);
    }

    static void updateProcessRecordNodes(com.android.server.am.ProcessRecord app) {
        if (app.mService.mConstants.ENABLE_NEW_OOMADJ) {
            for (int i = 0; i < app.mLinkedNodes.length; i++) {
                app.mLinkedNodes[i] = new com.android.server.am.OomAdjusterModernImpl.ProcessRecordNode(app);
            }
        }
    }

    void doEarlyCleanupIfNecessaryLocked() {
        if (getThread() == null) {
            this.mService.mOomAdjuster.onProcessEndLocked(this);
        }
    }

    void resetCrashingOnRestart() {
        this.mErrorState.setCrashing(false);
    }

    com.android.server.am.UidRecord getUidRecord() {
        return this.mUidRecord;
    }

    void setUidRecord(com.android.server.am.UidRecord uidRecord) {
        this.mUidRecord = uidRecord;
    }

    com.android.server.am.PackageList getPkgList() {
        return this.mPkgList;
    }

    android.util.ArraySet<java.lang.String> getPkgDeps() {
        return this.mPkgDeps;
    }

    void setPkgDeps(android.util.ArraySet<java.lang.String> pkgDeps) {
        this.mPkgDeps = pkgDeps;
    }

    public int getPid() {
        return this.mPid;
    }

    void setPid(int pid) {
        if (pid != this.mPid && this.mPid != 0) {
            setWasForceStopped(false);
        }
        this.mPid = pid;
        this.mWindowProcessController.setPid(pid);
        this.mShortStringName = null;
        this.mStringName = null;
        synchronized (this.mProfile.mProfilerLock) {
            this.mProfile.setPid(pid);
        }
    }

    int getSetAdj() {
        return this.mState.getSetAdj();
    }

    android.app.IApplicationThread getThread() {
        return this.mThread;
    }

    android.app.IApplicationThread getOnewayThread() {
        return this.mOnewayThread;
    }

    int getCurProcState() {
        return this.mState.getCurProcState();
    }

    int getSetProcState() {
        return this.mState.getSetProcState();
    }

    int getSetCapability() {
        return this.mState.getSetCapability();
    }

    public void makeActive(android.app.IApplicationThread thread, com.android.server.am.ProcessStatsService tracker) {
        this.mProfile.onProcessActive(thread, tracker);
        this.mThread = thread;
        if (this.mPid == android.os.Process.myPid()) {
            this.mOnewayThread = new com.android.server.am.SameProcessApplicationThread(thread, com.android.server.FgThread.getHandler());
        } else {
            this.mOnewayThread = thread;
        }
        this.mWindowProcessController.setThread(thread);
        this.mProcessRecordExt.createProcessInfo(this);
        if (this.mWindowProcessController.useFifoUiScheduling()) {
            this.mService.mSpecifiedFifoProcesses.add(this);
        }
    }

    public void makeInactive(com.android.server.am.ProcessStatsService tracker) {
        this.mThread = null;
        this.mOnewayThread = null;
        this.mWindowProcessController.setThread(null);
        if (this.mWindowProcessController.useFifoUiScheduling()) {
            this.mService.mSpecifiedFifoProcesses.remove(this);
        }
        this.mProfile.onProcessInactive(tracker);
        this.mProcessRecordExt.resetUxState();
        this.mProcessRecordExt.makeInactive(this);
    }

    boolean useFifoUiScheduling() {
        return this.mService.mUseFifoUiScheduling || (this.mService.mAllowSpecifiedFifoScheduling && this.mWindowProcessController.useFifoUiScheduling());
    }

    int getDyingPid() {
        return this.mDyingPid;
    }

    void setDyingPid(int dyingPid) {
        this.mDyingPid = dyingPid;
    }

    int[] getGids() {
        return this.mGids;
    }

    void setGids(int[] gids) {
        this.mGids = gids;
    }

    java.lang.String getRequiredAbi() {
        return this.mRequiredAbi;
    }

    void setRequiredAbi(java.lang.String requiredAbi) {
        this.mRequiredAbi = requiredAbi;
        this.mWindowProcessController.setRequiredAbi(requiredAbi);
    }

    java.lang.String getInstructionSet() {
        return this.mInstructionSet;
    }

    void setInstructionSet(java.lang.String instructionSet) {
        this.mInstructionSet = instructionSet;
    }

    void setPersistent(boolean persistent) {
        this.mPersistent = persistent;
        this.mWindowProcessController.setPersistent(persistent);
    }

    boolean isPersistent() {
        return this.mPersistent;
    }

    boolean isPendingStart() {
        return this.mPendingStart;
    }

    void setPendingStart(boolean pendingStart) {
        this.mPendingStart = pendingStart;
    }

    void setPendingFinishAttach(boolean pendingFinishAttach) {
        this.mPendingFinishAttach = pendingFinishAttach;
    }

    boolean isPendingFinishAttach() {
        return this.mPendingFinishAttach;
    }

    boolean isThreadReady() {
        return (this.mThread == null || this.mPendingFinishAttach) ? false : true;
    }

    long getStartSeq() {
        return this.mStartSeq;
    }

    void setStartSeq(long startSeq) {
        this.mStartSeq = startSeq;
    }

    com.android.server.am.HostingRecord getHostingRecord() {
        return this.mHostingRecord;
    }

    void setHostingRecord(com.android.server.am.HostingRecord hostingRecord) {
        this.mHostingRecord = hostingRecord;
    }

    java.lang.String getSeInfo() {
        return this.mSeInfo;
    }

    void setSeInfo(java.lang.String seInfo) {
        this.mSeInfo = seInfo;
    }

    long getStartUptime() {
        return this.mStartUptime;
    }

    @java.lang.Deprecated
    long getStartTime() {
        return this.mStartUptime;
    }

    long getStartElapsedTime() {
        return this.mStartElapsedTime;
    }

    long getBindApplicationTime() {
        return this.mBindApplicationTime;
    }

    void setBindApplicationTime(long bindApplicationTime) {
        this.mBindApplicationTime = bindApplicationTime;
    }

    int getStartUid() {
        return this.mStartUid;
    }

    void setStartUid(int startUid) {
        this.mStartUid = startUid;
    }

    int getMountMode() {
        return this.mMountMode;
    }

    void setMountMode(int mountMode) {
        this.mMountMode = mountMode;
    }

    boolean isBindMountPending() {
        return this.mBindMountPending;
    }

    void setBindMountPending(boolean bindMountPending) {
        this.mBindMountPending = bindMountPending;
    }

    boolean isUnlocked() {
        return this.mUnlocked;
    }

    void setUnlocked(boolean unlocked) {
        this.mUnlocked = unlocked;
    }

    int getRenderThreadTid() {
        return this.mRenderThreadTid;
    }

    void setRenderThreadTid(int renderThreadTid) {
        this.mRenderThreadTid = renderThreadTid;
    }

    android.content.res.CompatibilityInfo getCompat() {
        return this.mCompat;
    }

    void setCompat(android.content.res.CompatibilityInfo compat) {
        this.mCompat = compat;
    }

    long[] getDisabledCompatChanges() {
        return this.mDisabledCompatChanges;
    }

    long[] getLoggableCompatChanges() {
        return this.mLoggableCompatChanges;
    }

    void setDisabledCompatChanges(long[] disabledCompatChanges) {
        this.mDisabledCompatChanges = disabledCompatChanges;
    }

    void setLoggableCompatChanges(long[] loggableCompatChanges) {
        this.mLoggableCompatChanges = loggableCompatChanges;
    }

    void unlinkDeathRecipient() {
        if (this.mDeathRecipient != null && this.mThread != null) {
            this.mThread.asBinder().unlinkToDeath(this.mDeathRecipient, 0);
        }
        this.mDeathRecipient = null;
    }

    void setDeathRecipient(android.os.IBinder.DeathRecipient deathRecipient) {
        this.mDeathRecipient = deathRecipient;
    }

    android.os.IBinder.DeathRecipient getDeathRecipient() {
        return this.mDeathRecipient;
    }

    void setActiveInstrumentation(com.android.server.am.ActiveInstrumentation instr) {
        this.mInstr = instr;
        boolean isInstrumenting = instr != null;
        this.mWindowProcessController.setInstrumenting(isInstrumenting, isInstrumenting ? instr.mSourceUid : -1, isInstrumenting && instr.mHasBackgroundActivityStartsPermission);
    }

    com.android.server.am.ActiveInstrumentation getActiveInstrumentation() {
        return this.mInstr;
    }

    boolean isKilledByAm() {
        return this.mKilledByAm;
    }

    void setKilledByAm(boolean killedByAm) {
        this.mKilledByAm = killedByAm;
    }

    boolean isKilled() {
        return this.mKilled;
    }

    void setKilled(boolean killed) {
        this.mKilled = killed;
    }

    long getKillTime() {
        return this.mKillTime;
    }

    void setKillTime(long killTime) {
        this.mKillTime = killTime;
    }

    java.lang.String getWaitingToKill() {
        return this.mWaitingToKill;
    }

    void setWaitingToKill(java.lang.String waitingToKill) {
        this.mWaitingToKill = waitingToKill;
    }

    @Override // com.android.server.wm.WindowProcessListener
    public boolean isRemoved() {
        return this.mRemoved;
    }

    void setRemoved(boolean removed) {
        this.mRemoved = removed;
    }

    public boolean isDebugging() {
        return this.mDebugging;
    }

    public android.content.pm.ApplicationInfo getClientInfoForSdkSandbox() {
        if (!this.isSdkSandbox || this.sdkSandboxClientAppPackage == null) {
            throw new java.lang.IllegalStateException("getClientInfoForSdkSandbox called for non-sandbox process");
        }
        android.content.pm.PackageManagerInternal pm = this.mService.getPackageManagerInternal();
        return pm.getApplicationInfo(this.sdkSandboxClientAppPackage, 0L, 1000, this.userId);
    }

    public boolean isDebuggable() {
        if ((this.info.flags & 2) != 0) {
            return true;
        }
        if (!this.isSdkSandbox) {
            return false;
        }
        android.content.pm.ApplicationInfo clientInfo = getClientInfoForSdkSandbox();
        return (clientInfo == null || (clientInfo.flags & 2) == 0) ? false : true;
    }

    void setDebugging(boolean debugging) {
        this.mDebugging = debugging;
        this.mWindowProcessController.setDebugging(debugging);
    }

    boolean hasWaitedForDebugger() {
        return this.mWaitedForDebugger;
    }

    void setWaitedForDebugger(boolean waitedForDebugger) {
        this.mWaitedForDebugger = waitedForDebugger;
    }

    long getLastActivityTime() {
        return this.mLastActivityTime;
    }

    void setLastActivityTime(long lastActivityTime) {
        this.mLastActivityTime = lastActivityTime;
    }

    boolean isUsingWrapper() {
        return this.mUsingWrapper;
    }

    void setUsingWrapper(boolean usingWrapper) {
        this.mUsingWrapper = usingWrapper;
        this.mWindowProcessController.setUsingWrapper(usingWrapper);
    }

    int getLruSeq() {
        return this.mLruSeq;
    }

    void setLruSeq(int lruSeq) {
        this.mLruSeq = lruSeq;
    }

    java.lang.String getIsolatedEntryPoint() {
        return this.mIsolatedEntryPoint;
    }

    void setIsolatedEntryPoint(java.lang.String isolatedEntryPoint) {
        this.mIsolatedEntryPoint = isolatedEntryPoint;
    }

    java.lang.String[] getIsolatedEntryPointArgs() {
        return this.mIsolatedEntryPointArgs;
    }

    void setIsolatedEntryPointArgs(java.lang.String[] isolatedEntryPointArgs) {
        this.mIsolatedEntryPointArgs = isolatedEntryPointArgs;
    }

    boolean isInFullBackup() {
        return this.mInFullBackup;
    }

    void setInFullBackup(boolean inFullBackup) {
        this.mInFullBackup = inFullBackup;
    }

    @Override // com.android.server.wm.WindowProcessListener
    public boolean isCached() {
        return this.mState.isCached();
    }

    boolean hasActivities() {
        return this.mWindowProcessController.hasActivities();
    }

    boolean hasActivitiesOrRecentTasks() {
        return this.mWindowProcessController.hasActivitiesOrRecentTasks();
    }

    boolean hasRecentTasks() {
        return this.mWindowProcessController.hasRecentTasks();
    }

    public android.content.pm.ApplicationInfo getApplicationInfo() {
        return this.info;
    }

    boolean onCleanupApplicationRecordLSP(com.android.server.am.ProcessStatsService processStats, boolean allowRestart, boolean unlinkDeath) {
        this.mErrorState.onCleanupApplicationRecordLSP();
        resetPackageList(processStats);
        if (unlinkDeath) {
            unlinkDeathRecipient();
        }
        makeInactive(processStats);
        setWaitingToKill(null);
        this.mState.onCleanupApplicationRecordLSP();
        this.mServices.onCleanupApplicationRecordLocked();
        this.mReceivers.onCleanupApplicationRecordLocked();
        this.mService.mOomAdjuster.onProcessEndLocked(this);
        return this.mProviders.onCleanupApplicationRecordLocked(allowRestart);
    }

    public boolean isInterestingToUserLocked() {
        if (this.mWindowProcessController.isInterestingToUser()) {
            return true;
        }
        return this.mServices.hasForegroundServices();
    }

    void scheduleCrashLocked(java.lang.String message, int exceptionTypeId, android.os.Bundle extras) {
        if (!this.mKilledByAm && this.mThread != null) {
            if (this.mPid == android.os.Process.myPid()) {
                android.util.Slog.w("ActivityManager", "scheduleCrash: trying to crash system process!");
                return;
            }
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                try {
                    this.mThread.scheduleCrash(message, exceptionTypeId, extras);
                } catch (android.os.RemoteException e) {
                    killLocked("scheduleCrash for '" + message + "' failed", 4, true);
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }
    }

    public long getRss(int pid) {
        long[] rss = android.os.Process.getRss(pid);
        if (rss == null || rss.length <= 0) {
            return 0L;
        }
        return rss[0];
    }

    void killLocked(java.lang.String reason, int reasonCode, boolean noisy) {
        killLocked(reason, reasonCode, 0, noisy, true);
    }

    void killLocked(java.lang.String reason, int reasonCode, int subReason, boolean noisy) {
        killLocked(reason, reason, reasonCode, subReason, noisy, true);
    }

    void killLocked(java.lang.String reason, java.lang.String description, int reasonCode, int subReason, boolean noisy) {
        killLocked(reason, description, reasonCode, subReason, noisy, true);
    }

    void killLocked(java.lang.String reason, int reasonCode, int subReason, boolean noisy, boolean asyncKPG) {
        killLocked(reason, reason, reasonCode, subReason, noisy, asyncKPG);
    }

    void killLocked(java.lang.String reason, java.lang.String description, int reasonCode, int subReason, boolean noisy, boolean asyncKPG) {
        java.lang.String description2;
        if (!this.mKilledByAm) {
            android.os.Trace.traceBegin(64L, "kill");
            if (reasonCode == 6 && this.mErrorState.getAnrAnnotation() != null) {
                description2 = description + ": " + this.mErrorState.getAnrAnnotation();
            } else {
                description2 = description;
            }
            if (this.mService != null && (noisy || this.info.uid == this.mService.mCurOomAdjUid)) {
                this.mService.reportUidInfoMessageLocked("ActivityManager", "Killing " + toShortString() + " (adj " + this.mState.getSetAdj() + "): " + reason, this.info.uid);
            }
            int tgid = android.os.Process.getThreadGroupLeader(this.mPid);
            if (this.mPid > 0 && tgid != this.mPid) {
                android.util.Slog.w("ActivityManager", "mPid = " + this.mPid + "tgid = " + tgid + " is reused by others, skip kill [" + this.mPid + "]");
                return;
            }
            this.mOptRecord.setPendingFreeze(false);
            this.mOptRecord.setFrozen(false);
            if (this.mPid > 0) {
                this.mService.mProcessList.noteAppKill(this, reasonCode, subReason, description2);
                this.mProcessRecordExt.saveAmKillRecordToList(java.lang.System.currentTimeMillis(), this.mPid, this.processName, reason);
                android.util.EventLog.writeEvent(com.android.server.am.EventLogTags.AM_KILL, java.lang.Integer.valueOf(this.userId), java.lang.Integer.valueOf(this.mPid), this.processName, java.lang.Integer.valueOf(this.mState.getSetAdj()), reason, java.lang.Long.valueOf(getRss(this.mPid)));
                android.os.Process.killProcessQuiet(this.mPid);
                killProcessGroupIfNecessaryLocked(asyncKPG);
            } else {
                this.mPendingStart = false;
            }
            if (!this.mPersistent) {
                com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
                com.android.server.am.ActivityManagerService.boostPriorityForProcLockedSection();
                synchronized (activityManagerGlobalLock) {
                    try {
                        this.mKilled = true;
                        this.mKilledByAm = true;
                        this.mKillTime = android.os.SystemClock.uptimeMillis();
                    } catch (java.lang.Throwable th) {
                        com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                        throw th;
                    }
                }
                com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
            }
            this.mSocExt.killLocked(this.mService, this.mErrorState, this);
            android.os.Trace.traceEnd(64L);
        }
    }

    void killProcessGroupIfNecessaryLocked(boolean async) {
        boolean killProcessGroup;
        if (this.mHostingRecord != null && (this.mHostingRecord.usesWebviewZygote() || this.mHostingRecord.usesAppZygote())) {
            synchronized (this) {
                killProcessGroup = this.mProcessGroupCreated;
                if (!killProcessGroup) {
                    this.mSkipProcessGroupCreation = true;
                }
            }
        } else {
            killProcessGroup = true;
        }
        if (killProcessGroup) {
            if (!async) {
                android.os.Process.sendSignalToProcessGroup(this.uid, this.mPid, android.system.OsConstants.SIGKILL);
            }
            com.android.server.am.ProcessList.killProcessGroup(this.uid, this.mPid);
        }
    }

    @Override // com.android.server.wm.WindowProcessListener
    public void dumpDebug(android.util.proto.ProtoOutputStream proto, long fieldId) {
        dumpDebug(proto, fieldId, -1);
    }

    public void dumpDebug(android.util.proto.ProtoOutputStream proto, long fieldId, int lruIndex) {
        long token = proto.start(fieldId);
        proto.write(1120986464257L, this.mPid);
        proto.write(1138166333442L, this.processName);
        proto.write(1120986464259L, this.info.uid);
        if (android.os.UserHandle.getAppId(this.info.uid) >= 10000) {
            proto.write(1120986464260L, this.userId);
            proto.write(1120986464261L, android.os.UserHandle.getAppId(this.info.uid));
        }
        if (this.uid != this.info.uid) {
            proto.write(1120986464262L, android.os.UserHandle.getAppId(this.uid));
        }
        proto.write(1133871366151L, this.mPersistent);
        if (lruIndex >= 0) {
            proto.write(1120986464264L, lruIndex);
        }
        proto.end(token);
    }

    public java.lang.String toShortString() {
        java.lang.String shortStringName = this.mShortStringName;
        if (shortStringName != null) {
            return shortStringName;
        }
        java.lang.StringBuilder sb = new java.lang.StringBuilder(128);
        toShortString(sb);
        java.lang.String string = sb.toString();
        this.mShortStringName = string;
        return string;
    }

    void toShortString(java.lang.StringBuilder sb) {
        sb.append(this.mPid);
        sb.append(':');
        sb.append(this.processName);
        sb.append('/');
        if (this.info.uid < 10000) {
            sb.append(this.uid);
            return;
        }
        sb.append('u');
        sb.append(this.userId);
        int appId = android.os.UserHandle.getAppId(this.info.uid);
        if (appId >= 10000) {
            sb.append('a');
            sb.append(appId - 10000);
        } else {
            sb.append('s');
            sb.append(appId);
        }
        if (this.uid != this.info.uid) {
            sb.append('i');
            sb.append(android.os.UserHandle.getAppId(this.uid) - 99000);
        }
    }

    public java.lang.String toString() {
        java.lang.String stringName = this.mStringName;
        if (stringName != null) {
            return stringName;
        }
        java.lang.StringBuilder sb = new java.lang.StringBuilder(128);
        sb.append("ProcessRecord{");
        sb.append(java.lang.Integer.toHexString(java.lang.System.identityHashCode(this)));
        sb.append(' ');
        toShortString(sb);
        sb.append('}');
        java.lang.String string = sb.toString();
        this.mStringName = string;
        return string;
    }

    public boolean addPackage(java.lang.String pkg, long versionCode, com.android.server.am.ProcessStatsService tracker) {
        synchronized (tracker.mLock) {
            synchronized (this.mPkgList) {
                if (!this.mPkgList.containsKey(pkg)) {
                    com.android.internal.app.procstats.ProcessStats.ProcessStateHolder holder = new com.android.internal.app.procstats.ProcessStats.ProcessStateHolder(versionCode);
                    com.android.internal.app.procstats.ProcessState baseProcessTracker = this.mProfile.getBaseProcessTracker();
                    if (baseProcessTracker != null) {
                        tracker.updateProcessStateHolderLocked(holder, pkg, this.info.uid, versionCode, this.processName);
                        this.mPkgList.put(pkg, holder);
                        if (holder.state != baseProcessTracker) {
                            holder.state.makeActive();
                        }
                    } else {
                        this.mPkgList.put(pkg, holder);
                    }
                    return true;
                }
                return false;
            }
        }
    }

    void onProcessFrozen() {
        this.mProfile.onProcessFrozen();
    }

    void onProcessUnfrozen() {
        this.mProfile.onProcessUnfrozen();
        this.mServices.onProcessUnfrozen();
    }

    void onProcessFrozenCancelled() {
        this.mServices.onProcessFrozenCancelled();
    }

    public void resetPackageList(com.android.server.am.ProcessStatsService tracker) {
        com.android.server.am.PackageList packageList;
        synchronized (tracker.mLock) {
            final com.android.internal.app.procstats.ProcessState baseProcessTracker = this.mProfile.getBaseProcessTracker();
            com.android.server.am.PackageList packageList2 = this.mPkgList;
            try {
                synchronized (packageList2) {
                    try {
                        int numOfPkgs = this.mPkgList.size();
                        if (baseProcessTracker != null) {
                            long now = android.os.SystemClock.uptimeMillis();
                            baseProcessTracker.setState(-1, tracker.getMemFactorLocked(), now, this.mPkgList.getPackageListLocked());
                            if (numOfPkgs == 1) {
                                packageList = packageList2;
                            } else {
                                this.mPkgList.forEachPackageProcessStats(new java.util.function.Consumer() { // from class: com.android.server.am.ProcessRecord$$ExternalSyntheticLambda0
                                    @Override // java.util.function.Consumer
                                    public final void accept(java.lang.Object obj) {
                                        com.android.server.am.ProcessRecord.lambda$resetPackageList$0(baseProcessTracker, (com.android.internal.app.procstats.ProcessStats.ProcessStateHolder) obj);
                                    }
                                });
                                this.mPkgList.clear();
                                com.android.internal.app.procstats.ProcessStats.ProcessStateHolder holder = new com.android.internal.app.procstats.ProcessStats.ProcessStateHolder(this.info.longVersionCode);
                                packageList = packageList2;
                                tracker.updateProcessStateHolderLocked(holder, this.info.packageName, this.info.uid, this.info.longVersionCode, this.processName);
                                this.mPkgList.put(this.info.packageName, holder);
                                if (holder.state != baseProcessTracker) {
                                    holder.state.makeActive();
                                }
                            }
                        } else {
                            packageList = packageList2;
                            if (numOfPkgs != 1) {
                                this.mPkgList.clear();
                                this.mPkgList.put(this.info.packageName, new com.android.internal.app.procstats.ProcessStats.ProcessStateHolder(this.info.longVersionCode));
                            }
                        }
                    } catch (java.lang.Throwable th) {
                        th = th;
                        throw th;
                    }
                }
            } catch (java.lang.Throwable th2) {
                th = th2;
            }
            throw th;
        }
    }

    static /* synthetic */ void lambda$resetPackageList$0(com.android.internal.app.procstats.ProcessState baseProcessTracker, com.android.internal.app.procstats.ProcessStats.ProcessStateHolder holder) {
        if (holder.state != null && holder.state != baseProcessTracker) {
            holder.state.makeInactive();
        }
    }

    public java.lang.String[] getPackageList() {
        return this.mPkgList.getPackageList();
    }

    java.util.List<android.content.pm.VersionedPackage> getPackageListWithVersionCode() {
        return this.mPkgList.getPackageListWithVersionCode();
    }

    com.android.server.wm.WindowProcessController getWindowProcessController() {
        return this.mWindowProcessController;
    }

    void addOrUpdateBackgroundStartPrivileges(android.os.Binder entity, android.app.BackgroundStartPrivileges backgroundStartPrivileges) {
        java.util.Objects.requireNonNull(entity, "entity");
        java.util.Objects.requireNonNull(backgroundStartPrivileges, "backgroundStartPrivileges");
        com.android.internal.util.Preconditions.checkArgument(backgroundStartPrivileges.allowsAny(), "backgroundStartPrivileges does not allow anything");
        this.mWindowProcessController.addOrUpdateBackgroundStartPrivileges(entity, backgroundStartPrivileges);
        setBackgroundStartPrivileges(entity, backgroundStartPrivileges);
    }

    void removeBackgroundStartPrivileges(android.os.Binder entity) {
        java.util.Objects.requireNonNull(entity, "entity");
        this.mWindowProcessController.removeBackgroundStartPrivileges(entity);
        setBackgroundStartPrivileges(entity, null);
    }

    android.app.BackgroundStartPrivileges getBackgroundStartPrivileges() {
        android.app.BackgroundStartPrivileges backgroundStartPrivileges;
        synchronized (this.mBackgroundStartPrivileges) {
            if (this.mBackgroundStartPrivilegesMerged == null) {
                this.mBackgroundStartPrivilegesMerged = android.app.BackgroundStartPrivileges.NONE;
                for (int i = this.mBackgroundStartPrivileges.size() - 1; i >= 0; i--) {
                    this.mBackgroundStartPrivilegesMerged = this.mBackgroundStartPrivilegesMerged.merge(this.mBackgroundStartPrivileges.valueAt(i));
                }
            }
            backgroundStartPrivileges = this.mBackgroundStartPrivilegesMerged;
        }
        return backgroundStartPrivileges;
    }

    private void setBackgroundStartPrivileges(android.os.Binder entity, android.app.BackgroundStartPrivileges backgroundStartPrivileges) {
        synchronized (this.mBackgroundStartPrivileges) {
            boolean changed = true;
            if (backgroundStartPrivileges == null) {
                if (this.mBackgroundStartPrivileges.remove(entity) == null) {
                    changed = false;
                }
            } else {
                android.app.BackgroundStartPrivileges oldBsp = this.mBackgroundStartPrivileges.put(entity, backgroundStartPrivileges);
                if (backgroundStartPrivileges == oldBsp) {
                    changed = false;
                }
            }
            if (changed) {
                this.mBackgroundStartPrivilegesMerged = null;
            }
        }
    }

    @Override // com.android.server.wm.WindowProcessListener
    public void clearProfilerIfNeeded() {
        synchronized (this.mService.mAppProfiler.mProfilerLock) {
            this.mService.mAppProfiler.clearProfilerLPf();
        }
    }

    @Override // com.android.server.wm.WindowProcessListener
    public void updateServiceConnectionActivities() {
        com.android.server.am.ActivityManagerService activityManagerService = this.mService;
        com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                this.mService.mServices.updateServiceConnectionActivitiesLocked(this.mServices);
            } catch (java.lang.Throwable th) {
                com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
    }

    @Override // com.android.server.wm.WindowProcessListener
    public void setPendingUiClean(boolean pendingUiClean) {
        com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
        com.android.server.am.ActivityManagerService.boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            try {
                this.mProfile.setPendingUiClean(pendingUiClean);
            } catch (java.lang.Throwable th) {
                com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                throw th;
            }
        }
        com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
    }

    @Override // com.android.server.wm.WindowProcessListener
    public void setPendingUiCleanAndForceProcessStateUpTo(int newState) {
        com.android.server.am.ActivityManagerService activityManagerService = this.mService;
        com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                setPendingUiClean(true);
                this.mState.forceProcessStateUpTo(newState);
            } catch (java.lang.Throwable th) {
                com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
    }

    @Override // com.android.server.wm.WindowProcessListener
    public void updateProcessInfo(boolean updateServiceConnectionActivities, boolean activityChange, boolean updateOomAdj) {
        com.android.server.am.ActivityManagerService activityManagerService = this.mService;
        com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            if (updateServiceConnectionActivities) {
                try {
                    this.mService.mServices.updateServiceConnectionActivitiesLocked(this.mServices);
                } catch (java.lang.Throwable th) {
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            if (this.mThread == null) {
                com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                return;
            }
            this.mService.updateLruProcessLocked(this, activityChange, null);
            if (updateOomAdj) {
                this.mService.updateOomAdjLocked(this, 1);
            }
            com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
        }
    }

    @Override // com.android.server.wm.WindowProcessListener
    public long getCpuTime() {
        return this.mService.mAppProfiler.getCpuTimeForPid(this.mPid);
    }

    public long getCpuDelayTime() {
        return this.mService.mAppProfiler.getCpuDelayTimeForPid(this.mPid);
    }

    @Override // com.android.server.wm.WindowProcessListener
    public void onStartActivity(int topProcessState, boolean setProfileProc, java.lang.String packageName, long versionCode) {
        com.android.server.am.ActivityManagerService activityManagerService = this.mService;
        com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                this.mWaitingToKill = null;
                if (setProfileProc) {
                    synchronized (this.mService.mAppProfiler.mProfilerLock) {
                        this.mService.mAppProfiler.setProfileProcLPf(this);
                    }
                }
                if (packageName != null) {
                    addPackage(packageName, versionCode, this.mService.mProcessStats);
                }
                updateProcessInfo(false, true, true);
                setPendingUiClean(true);
                this.mProcessRecordExt.onStartActivity(this, packageName);
                this.mState.setHasShownUi(true);
                this.mState.forceProcessStateUpTo(topProcessState);
            } catch (java.lang.Throwable th) {
                com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
    }

    @Override // com.android.server.wm.WindowProcessListener
    public void appDied(java.lang.String reason) {
        com.android.server.am.ActivityManagerService activityManagerService = this.mService;
        com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                this.mService.appDiedLocked(this, reason);
            } catch (java.lang.Throwable th) {
                com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
    }

    @Override // com.android.server.wm.WindowProcessListener
    public void setRunningRemoteAnimation(boolean runningRemoteAnimation) {
        if (this.mPid == android.os.Process.myPid()) {
            android.util.Slog.wtf("ActivityManager", "system can't run remote animation");
            return;
        }
        com.android.server.am.ActivityManagerService activityManagerService = this.mService;
        com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                if (this.mRunningRemoteAnimation == runningRemoteAnimation) {
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                    return;
                }
                this.mProcessRecordExt.callOrmsSetSceneActionForRemoteAnimation(runningRemoteAnimation);
                this.mRunningRemoteAnimation = runningRemoteAnimation;
                this.mState.setRunningRemoteAnimation(runningRemoteAnimation);
                this.mUIFirstManagerExt.setTaskAsRemoteAnimationUx(this.mPid, this.mRenderThreadTid, this.mHwuiTaskThreads, this.info.packageName, runningRemoteAnimation);
                com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
            } catch (java.lang.Throwable th) {
                com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public long getInputDispatchingTimeoutMillis() {
        return this.mWindowProcessController.getInputDispatchingTimeoutMillis();
    }

    public int getProcessClassEnum() {
        if (this.mPid == com.android.server.am.ActivityManagerService.MY_PID) {
            return 3;
        }
        if (this.info == null) {
            return 0;
        }
        return (this.info.flags & 1) != 0 ? 2 : 1;
    }

    java.util.List<com.android.server.am.ProcessRecord> getLruProcessList() {
        return this.mService.mProcessList.getLruProcessesLOSP();
    }

    public void setWasForceStopped(boolean stopped) {
        this.mWasForceStopped = stopped;
    }

    public boolean wasForceStopped() {
        return this.mWasForceStopped;
    }

    boolean isFreezable() {
        return this.mService.mOomAdjuster.mCachedAppOptimizer.useFreezer() && !this.mOptRecord.isFreezeExempt() && !this.mOptRecord.shouldNotFreeze() && this.mState.getCurAdj() >= 900;
    }

    public void forEachConnectionHost(java.util.function.Consumer<com.android.server.am.ProcessRecord> consumer) {
        for (int i = this.mServices.numberOfConnections() - 1; i >= 0; i--) {
            com.android.server.am.ConnectionRecord cr = this.mServices.getConnectionAt(i);
            com.android.server.am.ProcessRecord service = cr.binding.service.app;
            consumer.accept(service);
        }
        for (int i2 = this.mServices.numberOfSdkSandboxConnections() - 1; i2 >= 0; i2--) {
            com.android.server.am.ConnectionRecord cr2 = this.mServices.getSdkSandboxConnectionAt(i2);
            com.android.server.am.ProcessRecord service2 = cr2.binding.service.app;
            consumer.accept(service2);
        }
        for (int i3 = this.mProviders.numberOfProviderConnections() - 1; i3 >= 0; i3--) {
            com.android.server.am.ContentProviderConnection cpc = this.mProviders.getProviderConnectionAt(i3);
            com.android.server.am.ProcessRecord provider = cpc.provider.proc;
            consumer.accept(provider);
        }
    }

    public com.android.server.am.IProcessRecordWrapper getWrapper() {
        return this.mWrapper;
    }

    private class ProcessRecordWrapper implements com.android.server.am.IProcessRecordWrapper {
        private ProcessRecordWrapper() {
        }

        @Override // com.android.server.am.IProcessRecordWrapper
        public com.android.server.am.IProcessRecordExt getExtImpl() {
            return com.android.server.am.ProcessRecord.this.mProcessRecordExt;
        }

        @Override // com.android.server.am.IProcessRecordWrapper
        public int getRenderThreadTid() {
            return com.android.server.am.ProcessRecord.this.mRenderThreadTid;
        }

        @Override // com.android.server.am.IProcessRecordWrapper
        public android.util.IntArray getHwuiTaskThreads() {
            return com.android.server.am.ProcessRecord.this.mHwuiTaskThreads;
        }

        private com.android.server.am.IProcessRecordSocExt getSocExtImpl() {
            return com.android.server.am.ProcessRecord.this.mSocExt;
        }

        @Override // com.android.server.am.IProcessRecordWrapper
        public long getLastActivityTime() {
            return com.android.server.am.ProcessRecord.this.mLastActivityTime;
        }
    }
}
