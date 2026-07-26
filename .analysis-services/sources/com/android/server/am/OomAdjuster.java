package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public class OomAdjuster {
    static final long CAMERA_MICROPHONE_CAPABILITY_CHANGE_ID = 136219221;
    private static final long NO_FOLLOW_UP_TIME = Long.MAX_VALUE;
    static final long PROCESS_CAPABILITY_CHANGE_ID = 136274596;
    static final java.lang.String TAG = "OomAdjuster";
    private static final int UAD_UX_OOMADJUSTER_EVENT_ID = 1012;
    private static final int UIFIRST_SCENE_OPT_SET = 128;
    static final long USE_SHORT_FGS_USAGE_INTERACTION_TIME = 183972877;
    private final int MSG_CHANGE_PROCESS_GROUP_BY_OCL;
    private final int SA_TYPE_HEAVY;
    com.android.server.am.ActiveUids mActiveUids;
    int mAdjSeq;
    com.android.server.am.CacheOomRanker mCacheOomRanker;
    com.android.server.am.CachedAppOptimizer mCachedAppOptimizer;
    com.android.server.am.ActivityManagerConstants mConstants;
    private final android.util.ArraySet<com.android.server.am.ProcessRecord> mFollowUpUpdateSet;
    final com.android.server.am.OomAdjuster.Injector mInjector;
    private double mLastFreeSwapPercent;
    protected int mLastReason;
    private final com.android.server.am.OomAdjusterDebugLogger mLogger;
    int mNewNumAServiceProcs;
    int mNewNumServiceProcs;
    private long mNextFollowUpUpdateUptimeMs;
    private long mNextNoKillDebugMessageTime;
    int mNumCachedHiddenProcs;
    int mNumNonCachedProcs;
    int mNumServiceProcs;
    private final int mNumSlots;
    private boolean mOomAdjUpdateOngoing;
    private com.android.server.am.IOomAdjusterWrapper mOomAdjWrapper;
    private boolean mPendingFullOomAdjUpdate;
    protected final android.util.ArraySet<com.android.server.am.ProcessRecord> mPendingProcessSet;
    final com.android.server.am.ActivityManagerGlobalLock mProcLock;
    private final android.os.Handler mProcessGroupHandler;
    final com.android.server.am.ProcessList mProcessList;
    protected int mProcessStateCurTop;
    protected final android.util.ArraySet<com.android.server.am.ProcessRecord> mProcessesInCycle;
    protected final java.util.ArrayList<com.android.server.am.ProcessRecord> mProcsToOomAdj;
    final com.android.server.am.ActivityManagerService mService;
    public com.android.server.am.IOomAdjusterSocExt mSocExt;
    protected final java.util.ArrayList<com.android.server.am.UidRecord> mTmpBecameIdle;
    protected final com.android.server.am.OomAdjuster.ComputeOomAdjWindowCallback mTmpComputeOomAdjWindowCallback;
    final long[] mTmpLong;
    protected final java.util.ArrayList<com.android.server.am.ProcessRecord> mTmpProcessList;
    protected final java.util.ArrayList<com.android.server.am.ProcessRecord> mTmpProcessList2;
    protected final android.util.ArraySet<com.android.server.am.ProcessRecord> mTmpProcessSet;
    protected final java.util.ArrayDeque<com.android.server.am.ProcessRecord> mTmpQueue;
    protected final int[] mTmpSchedGroup;
    protected final com.android.server.am.ActiveUids mTmpUidRecords;
    private com.oplus.osense.IOplusUserAwareManagerExt mUserAwareManagerExt;
    public static com.android.server.am.IOomAdjusterExt mOomAdjusterExt = (com.android.server.am.IOomAdjusterExt) system.ext.loader.core.ExtLoader.type(com.android.server.am.IOomAdjusterExt.class).create();
    private static boolean first_boot = true;

    public static final int oomAdjReasonToProto(int oomReason) {
        switch (oomReason) {
            case 0:
                return 0;
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 4;
            case 5:
                return 5;
            case 6:
                return 6;
            case 7:
                return 7;
            case 8:
                return 8;
            case 9:
                return 9;
            case 10:
                return 10;
            case 11:
                return 11;
            case 12:
                return 12;
            case 13:
                return 13;
            case 14:
                return 14;
            case 15:
                return 15;
            case 16:
                return 16;
            case 17:
                return 17;
            case 18:
                return 18;
            case 19:
                return 19;
            case 20:
                return 20;
            case 21:
                return 21;
            case 22:
                return 22;
            case 23:
                return 23;
            default:
                return -1;
        }
    }

    public static final java.lang.String oomAdjReasonToString(int oomReason) {
        switch (oomReason) {
            case 0:
                return "updateOomAdj_meh";
            case 1:
                return "updateOomAdj_activityChange";
            case 2:
                return "updateOomAdj_finishReceiver";
            case 3:
                return "updateOomAdj_startReceiver";
            case 4:
                return "updateOomAdj_bindService";
            case 5:
                return "updateOomAdj_unbindService";
            case 6:
                return "updateOomAdj_startService";
            case 7:
                return "updateOomAdj_getProvider";
            case 8:
                return "updateOomAdj_removeProvider";
            case 9:
                return "updateOomAdj_uiVisibility";
            case 10:
                return "updateOomAdj_allowlistChange";
            case 11:
                return "updateOomAdj_processBegin";
            case 12:
                return "updateOomAdj_processEnd";
            case 13:
                return "updateOomAdj_shortFgs";
            case 14:
                return "updateOomAdj_systemInit";
            case 15:
                return "updateOomAdj_backup";
            case 16:
                return "updateOomAdj_shell";
            case 17:
                return "updateOomAdj_removeTask";
            case 18:
                return "updateOomAdj_uidIdle";
            case 19:
                return "updateOomAdj_stopService";
            case 20:
                return "updateOomAdj_executingService";
            case 21:
                return "updateOomAdj_restrictionChange";
            case 22:
                return "updateOomAdj_componentDisabled";
            case 23:
                return "updateOomAdj_followUp";
            default:
                return "_unknown";
        }
    }

    public static class Injector {
        boolean isChangeEnabled(int cachedCompatChangeId, android.content.pm.ApplicationInfo app, boolean defaultValue) {
            com.android.server.am.PlatformCompatCache.getInstance();
            return com.android.server.am.PlatformCompatCache.isChangeEnabled(cachedCompatChangeId, app, defaultValue);
        }

        long getUptimeMillis() {
            return android.os.SystemClock.uptimeMillis();
        }

        long getElapsedRealtimeMillis() {
            return android.os.SystemClock.elapsedRealtime();
        }
    }

    boolean isChangeEnabled(int cachedCompatChangeId, android.content.pm.ApplicationInfo app, boolean defaultValue) {
        return this.mInjector.isChangeEnabled(cachedCompatChangeId, app, defaultValue);
    }

    OomAdjuster(com.android.server.am.ActivityManagerService service, com.android.server.am.ProcessList processList, com.android.server.am.ActiveUids activeUids) {
        this(service, processList, activeUids, createAdjusterThread());
    }

    static com.android.server.ServiceThread createAdjusterThread() {
        com.android.server.ServiceThread adjusterThread = new com.android.server.ServiceThread(TAG, -10, false);
        adjusterThread.start();
        return adjusterThread;
    }

    OomAdjuster(com.android.server.am.ActivityManagerService service, com.android.server.am.ProcessList processList, com.android.server.am.ActiveUids activeUids, com.android.server.ServiceThread adjusterThread) {
        this(service, processList, activeUids, adjusterThread, new com.android.server.am.OomAdjuster.Injector());
    }

    OomAdjuster(com.android.server.am.ActivityManagerService service, com.android.server.am.ProcessList processList, com.android.server.am.ActiveUids activeUids, com.android.server.am.OomAdjuster.Injector injector) {
        this(service, processList, activeUids, createAdjusterThread(), injector);
    }

    OomAdjuster(com.android.server.am.ActivityManagerService service, com.android.server.am.ProcessList processList, com.android.server.am.ActiveUids activeUids, com.android.server.ServiceThread adjusterThread, com.android.server.am.OomAdjuster.Injector injector) {
        this.mTmpLong = new long[3];
        this.mAdjSeq = 0;
        this.mNumServiceProcs = 0;
        this.mNewNumAServiceProcs = 0;
        this.mNewNumServiceProcs = 0;
        this.mNumNonCachedProcs = 0;
        this.mNumCachedHiddenProcs = 0;
        this.mTmpSchedGroup = new int[1];
        this.mTmpProcessList = new java.util.ArrayList<>();
        this.mTmpProcessList2 = new java.util.ArrayList<>();
        this.mTmpBecameIdle = new java.util.ArrayList<>();
        this.mTmpProcessSet = new android.util.ArraySet<>();
        this.mPendingProcessSet = new android.util.ArraySet<>();
        this.mProcessesInCycle = new android.util.ArraySet<>();
        this.mProcsToOomAdj = new java.util.ArrayList<>();
        this.mOomAdjUpdateOngoing = false;
        this.mPendingFullOomAdjUpdate = false;
        this.mSocExt = (com.android.server.am.IOomAdjusterSocExt) system.ext.loader.core.ExtLoader.type(com.android.server.am.IOomAdjusterSocExt.class).base(this).create();
        this.MSG_CHANGE_PROCESS_GROUP_BY_OCL = 1001;
        this.mUserAwareManagerExt = (com.oplus.osense.IOplusUserAwareManagerExt) system.ext.loader.core.ExtLoader.type(com.oplus.osense.IOplusUserAwareManagerExt.class).create();
        this.mProcessStateCurTop = 2;
        this.SA_TYPE_HEAVY = 2;
        this.mFollowUpUpdateSet = new android.util.ArraySet<>();
        this.mNextFollowUpUpdateUptimeMs = Long.MAX_VALUE;
        this.mLastFreeSwapPercent = 1.0d;
        this.mTmpComputeOomAdjWindowCallback = new com.android.server.am.OomAdjuster.ComputeOomAdjWindowCallback();
        this.mOomAdjWrapper = new com.android.server.am.OomAdjuster.OomAdjusterWrapper();
        this.mService = service;
        this.mInjector = injector;
        this.mProcessList = processList;
        this.mProcLock = service.mProcLock;
        this.mActiveUids = activeUids;
        this.mConstants = this.mService.mConstants;
        this.mCachedAppOptimizer = new com.android.server.am.CachedAppOptimizer(this.mService);
        this.mCacheOomRanker = new com.android.server.am.CacheOomRanker(service);
        this.mLogger = new com.android.server.am.OomAdjusterDebugLogger(this, this.mService.mConstants);
        this.mSocExt.initPerfConfig();
        this.mProcessGroupHandler = new android.os.Handler(adjusterThread.getLooper(), new android.os.Handler.Callback() { // from class: com.android.server.am.OomAdjuster$$ExternalSyntheticLambda0
            @Override // android.os.Handler.Callback
            public final boolean handleMessage(android.os.Message message) {
                return this.f$0.lambda$new$0(message);
            }
        });
        this.mTmpUidRecords = new com.android.server.am.ActiveUids(service, false);
        this.mTmpQueue = new java.util.ArrayDeque<>(this.mConstants.CUR_MAX_CACHED_PROCESSES << 1);
        this.mNumSlots = 10;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$new$0(android.os.Message msg) {
        if (first_boot) {
            android.os.Trace.traceBegin(64L, "createAdjusterThread");
            try {
                try {
                    int pid = android.os.Process.myPid();
                    int sTid = android.os.Process.myTid();
                    android.util.Slog.e(TAG, "pid is " + pid + " sTid:" + sTid);
                    if (this.mUserAwareManagerExt != null) {
                        this.mUserAwareManagerExt.reportKeyThread(TAG, sTid, pid, 1012, (android.os.Bundle) null);
                        this.mUserAwareManagerExt.requestSysResource(1012, -1, (android.os.Bundle) null);
                    }
                    if (mOomAdjusterExt != null) {
                        mOomAdjusterExt.setUxThreadValueByFile(pid, sTid, 130);
                    }
                } catch (java.lang.Exception ex) {
                    android.util.Slog.e(TAG, "No oomadj changes for " + ex);
                }
                android.os.Trace.traceEnd(64L);
                first_boot = false;
            } finally {
                android.os.Trace.traceEnd(64L);
            }
        }
        if (mOomAdjusterExt.isOclGrpRequestMsgAndSetGroup(msg)) {
            return true;
        }
        int pid2 = msg.arg1;
        int group = msg.arg2;
        com.android.server.am.ProcessRecord app = (com.android.server.am.ProcessRecord) msg.obj;
        if (pid2 == com.android.server.am.ActivityManagerService.MY_PID) {
            return true;
        }
        boolean traceEnabled = android.os.Trace.isTagEnabled(64L);
        if (traceEnabled) {
            android.os.Trace.traceBegin(64L, "setProcessGroup " + app.processName + " " + pid2 + " to " + group);
        }
        try {
            try {
                android.os.Process.setProcessGroup(pid2, group);
            } catch (java.lang.Exception e) {
                if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_ALL) {
                    android.util.Slog.w(TAG, "Failed setting process group of " + pid2 + " to " + group, e);
                }
                if (traceEnabled) {
                }
            }
            mOomAdjusterExt.notifyProcGrpChange(app, group);
            return true;
        } finally {
            if (traceEnabled) {
            }
        }
    }

    public void requestProcessGroupChange(int uid, int pid, int newGrp, int from, java.lang.String reason) {
        if (pid >= 0) {
            android.os.Message msg = this.mProcessGroupHandler.obtainMessage(1001, newGrp, from);
            android.os.Bundle data = new android.os.Bundle();
            data.putInt("uid", uid);
            data.putInt(com.android.server.oplus.osense.OsenseConstants.KEY_INTEGER_PID, pid);
            data.putString(com.android.server.policy.PhoneWindowManager.SYSTEM_DIALOG_REASON_KEY, reason);
            msg.setData(data);
            this.mProcessGroupHandler.sendMessage(msg);
        }
    }

    void initSettings() {
        this.mCachedAppOptimizer.init();
        this.mCacheOomRanker.init(android.app.ActivityThread.currentApplication().getMainExecutor());
        if (this.mService.mConstants.KEEP_WARMING_SERVICES.size() > 0) {
            android.content.IntentFilter filter = new android.content.IntentFilter("android.intent.action.USER_SWITCHED");
            this.mService.mContext.registerReceiverForAllUsers(new android.content.BroadcastReceiver() { // from class: com.android.server.am.OomAdjuster.1
                @Override // android.content.BroadcastReceiver
                public void onReceive(android.content.Context context, android.content.Intent intent) {
                    com.android.server.am.ActivityManagerService activityManagerService = com.android.server.am.OomAdjuster.this.mService;
                    com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
                    synchronized (activityManagerService) {
                        try {
                            com.android.server.am.OomAdjuster.this.handleUserSwitchedLocked();
                        } catch (java.lang.Throwable th) {
                            com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                            throw th;
                        }
                    }
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                }
            }, filter, null, this.mService.mHandler);
        }
    }

    void handleUserSwitchedLocked() {
        this.mProcessList.forEachLruProcessesLOSP(false, new java.util.function.Consumer() { // from class: com.android.server.am.OomAdjuster$$ExternalSyntheticLambda2
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.updateKeepWarmIfNecessaryForProcessLocked((com.android.server.am.ProcessRecord) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateKeepWarmIfNecessaryForProcessLocked(com.android.server.am.ProcessRecord app) {
        android.util.ArraySet<android.content.ComponentName> warmServices = this.mService.mConstants.KEEP_WARMING_SERVICES;
        boolean includeWarmPkg = false;
        com.android.server.am.PackageList pkgList = app.getPkgList();
        int j = warmServices.size() - 1;
        while (true) {
            if (j < 0) {
                break;
            }
            if (pkgList.containsKey(warmServices.valueAt(j).getPackageName())) {
                includeWarmPkg = true;
                break;
            }
            j--;
        }
        if (!includeWarmPkg) {
            return;
        }
        com.android.server.am.ProcessServiceRecord psr = app.mServices;
        for (int j2 = psr.numberOfRunningServices() - 1; j2 >= 0; j2--) {
            psr.getRunningServiceAt(j2).updateKeepWarmLocked();
        }
    }

    void updateOomAdjLocked(int oomAdjReason) {
        com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
        com.android.server.am.ActivityManagerService.boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            try {
                updateOomAdjLSP(oomAdjReason);
            } catch (java.lang.Throwable th) {
                com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                throw th;
            }
        }
        com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
    }

    private void updateOomAdjLSP(int oomAdjReason) {
        if (checkAndEnqueueOomAdjTargetLocked(null)) {
            return;
        }
        try {
            mOomAdjusterExt.onOomAdjUpdateLSP(null, oomAdjReason, true, null);
            this.mOomAdjUpdateOngoing = true;
            performUpdateOomAdjLSP(oomAdjReason);
        } finally {
            this.mOomAdjUpdateOngoing = false;
            updateOomAdjPendingTargetsLocked(oomAdjReason);
        }
    }

    protected void performUpdateOomAdjLSP(int oomAdjReason) throws java.lang.Throwable {
        com.android.server.am.ProcessRecord topApp = this.mService.getTopApp();
        this.mProcessStateCurTop = this.mService.mAtmInternal.getTopProcessState();
        this.mPendingProcessSet.clear();
        com.android.server.am.AppProfiler appProfiler = this.mService.mAppProfiler;
        this.mService.mAppProfiler.mHasHomeProcess = false;
        appProfiler.mHasPreviousProcess = false;
        updateOomAdjInnerLSP(oomAdjReason, topApp, null, null, true, true);
    }

    boolean updateOomAdjLocked(com.android.server.am.ProcessRecord app, int oomAdjReason) {
        boolean zUpdateOomAdjLSP;
        com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
        com.android.server.am.ActivityManagerService.boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            try {
                zUpdateOomAdjLSP = updateOomAdjLSP(app, oomAdjReason);
            } catch (java.lang.Throwable th) {
                com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                throw th;
            }
        }
        com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
        return zUpdateOomAdjLSP;
    }

    private boolean updateOomAdjLSP(com.android.server.am.ProcessRecord app, int oomAdjReason) {
        boolean z = true;
        if (app == null || !this.mConstants.OOMADJ_UPDATE_QUICK) {
            if (app != null) {
                mOomAdjusterExt.setFullOomAdjUpdateInfo(app.uid, app.processName, "quick update");
            }
            updateOomAdjLSP(oomAdjReason);
            return true;
        }
        if (checkAndEnqueueOomAdjTargetLocked(app)) {
            return true;
        }
        try {
            this.mOomAdjUpdateOngoing = true;
            com.android.server.am.IOomAdjusterExt iOomAdjusterExt = mOomAdjusterExt;
            if (app != null) {
                z = false;
            }
            iOomAdjusterExt.onOomAdjUpdateLSP(app, oomAdjReason, z, null);
            return performUpdateOomAdjLSP(app, oomAdjReason);
        } finally {
            this.mOomAdjUpdateOngoing = false;
            updateOomAdjPendingTargetsLocked(oomAdjReason);
        }
    }

    protected boolean performUpdateOomAdjLSP(com.android.server.am.ProcessRecord app, int oomAdjReason) throws java.lang.Throwable {
        com.android.server.am.ProcessRecord topApp = this.mService.getTopApp();
        this.mLastReason = oomAdjReason;
        android.os.Trace.traceBegin(64L, oomAdjReasonToString(oomAdjReason));
        com.android.server.am.ProcessStateRecord state = app.mState;
        java.util.ArrayList<com.android.server.am.ProcessRecord> processes = this.mTmpProcessList;
        com.android.server.am.ActiveUids uids = this.mTmpUidRecords;
        this.mPendingProcessSet.add(app);
        this.mProcessStateCurTop = enqueuePendingTopAppIfNecessaryLSP();
        boolean containsCycle = collectReachableProcessesLocked(this.mPendingProcessSet, processes, uids);
        this.mPendingProcessSet.clear();
        int size = processes.size();
        if (size > 0) {
            updateOomAdjInnerLSP(oomAdjReason, topApp, processes, uids, containsCycle, false);
        } else if (state.getCurRawAdj() == 1001) {
            processes.add(app);
            assignCachedAdjIfNecessary(processes);
            applyOomAdjLSP(app, false, this.mInjector.getUptimeMillis(), this.mInjector.getElapsedRealtimeMillis(), oomAdjReason);
        }
        this.mTmpProcessList.clear();
        this.mService.clearPendingTopAppLocked();
        android.os.Trace.traceEnd(64L);
        return true;
    }

    protected int enqueuePendingTopAppIfNecessaryLSP() {
        int prevTopProcessState = this.mService.mAtmInternal.getTopProcessState();
        this.mService.enqueuePendingTopAppIfNecessaryLocked();
        int topProcessState = this.mService.mAtmInternal.getTopProcessState();
        if (prevTopProcessState != topProcessState) {
            this.mService.enqueuePendingTopAppIfNecessaryLocked();
        }
        return topProcessState;
    }

    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    protected boolean collectReachableProcessesLocked(android.util.ArraySet<com.android.server.am.ProcessRecord> arraySet, java.util.ArrayList<com.android.server.am.ProcessRecord> arrayList, com.android.server.am.ActiveUids activeUids) {
        boolean z;
        com.android.server.am.ProcessRecord processRecord;
        com.android.server.am.OomAdjuster oomAdjuster = this;
        java.util.ArrayDeque<com.android.server.am.ProcessRecord> arrayDeque = oomAdjuster.mTmpQueue;
        arrayDeque.clear();
        arrayList.clear();
        int i = 0;
        int size = arraySet.size();
        while (true) {
            z = true;
            if (i >= size) {
                break;
            }
            com.android.server.am.ProcessRecord processRecordValueAt = arraySet.valueAt(i);
            processRecordValueAt.mState.setReachable(true);
            arrayDeque.offer(processRecordValueAt);
            i++;
        }
        activeUids.clear();
        boolean zIsReachable = false;
        com.android.server.am.ProcessRecord processRecordPoll = arrayDeque.poll();
        while (processRecordPoll != null) {
            arrayList.add(processRecordPoll);
            com.android.server.am.UidRecord uidRecord = processRecordPoll.getUidRecord();
            if (uidRecord != null) {
                activeUids.put(uidRecord.getUid(), uidRecord);
            }
            com.android.server.am.ProcessServiceRecord processServiceRecord = processRecordPoll.mServices;
            for (int iNumberOfConnections = processServiceRecord.numberOfConnections() - (z ? 1 : 0); iNumberOfConnections >= 0; iNumberOfConnections--) {
                com.android.server.am.ConnectionRecord connectionAt = processServiceRecord.getConnectionAt(iNumberOfConnections);
                com.android.server.am.ProcessRecord processRecord2 = connectionAt.hasFlag(2) ? connectionAt.binding.service.isolationHostProc : connectionAt.binding.service.app;
                if (processRecord2 != null && processRecord2 != processRecordPoll && (processRecord2.mState.getMaxAdj() < -900 || processRecord2.mState.getMaxAdj() >= 0)) {
                    zIsReachable |= processRecord2.mState.isReachable();
                    if (!processRecord2.mState.isReachable() && (!connectionAt.hasFlag(32) || !connectionAt.notHasFlag(134217856))) {
                        arrayDeque.offer(processRecord2);
                        processRecord2.mState.setReachable(z);
                    }
                }
            }
            com.android.server.am.ProcessProviderRecord processProviderRecord = processRecordPoll.mProviders;
            for (int iNumberOfProviderConnections = processProviderRecord.numberOfProviderConnections() - (z ? 1 : 0); iNumberOfProviderConnections >= 0; iNumberOfProviderConnections--) {
                com.android.server.am.ProcessRecord processRecord3 = processProviderRecord.getProviderConnectionAt(iNumberOfProviderConnections).provider.proc;
                if (processRecord3 != null && processRecord3 != processRecordPoll && (processRecord3.mState.getMaxAdj() < -900 || processRecord3.mState.getMaxAdj() >= 0)) {
                    zIsReachable |= processRecord3.mState.isReachable();
                    if (!processRecord3.mState.isReachable()) {
                        arrayDeque.offer(processRecord3);
                        processRecord3.mState.setReachable(z);
                    }
                }
            }
            java.util.List<com.android.server.am.ProcessRecord> sdkSandboxProcessesForAppLocked = oomAdjuster.mProcessList.getSdkSandboxProcessesForAppLocked(processRecordPoll.uid);
            for (int size2 = (sdkSandboxProcessesForAppLocked != null ? sdkSandboxProcessesForAppLocked.size() : 0) - 1; size2 >= 0; size2--) {
                com.android.server.am.ProcessRecord processRecord4 = sdkSandboxProcessesForAppLocked.get(size2);
                zIsReachable |= processRecord4.mState.isReachable();
                if (!processRecord4.mState.isReachable()) {
                    arrayDeque.offer(processRecord4);
                    processRecord4.mState.setReachable(z);
                }
            }
            if (processRecordPoll.isSdkSandbox) {
                int iNumberOfRunningServices = processServiceRecord.numberOfRunningServices() - (z ? 1 : 0);
                while (iNumberOfRunningServices >= 0) {
                    android.util.ArrayMap<android.os.IBinder, java.util.ArrayList<com.android.server.am.ConnectionRecord>> connections = processServiceRecord.getRunningServiceAt(iNumberOfRunningServices).getConnections();
                    int size3 = connections.size() - 1;
                    while (size3 >= 0) {
                        java.util.ArrayList<com.android.server.am.ConnectionRecord> arrayListValueAt = connections.valueAt(size3);
                        boolean z2 = zIsReachable;
                        int size4 = arrayListValueAt.size() - 1;
                        while (size4 >= 0) {
                            java.util.ArrayList<com.android.server.am.ConnectionRecord> arrayList2 = arrayListValueAt;
                            com.android.server.am.ProcessRecord processRecord5 = arrayListValueAt.get(size4).binding.attributedClient;
                            if (processRecord5 == null || processRecord5 == processRecordPoll) {
                                processRecord = processRecordPoll;
                            } else {
                                processRecord = processRecordPoll;
                                if ((processRecord5.mState.getMaxAdj() < -900 || processRecord5.mState.getMaxAdj() >= 0) && !processRecord5.mState.isReachable()) {
                                    arrayDeque.offer(processRecord5);
                                    processRecord5.mState.setReachable(true);
                                }
                            }
                            size4--;
                            arrayListValueAt = arrayList2;
                            processRecordPoll = processRecord;
                        }
                        size3--;
                        zIsReachable = z2;
                        processRecordPoll = processRecordPoll;
                    }
                    iNumberOfRunningServices--;
                    z = true;
                    processRecordPoll = processRecordPoll;
                }
            }
            com.android.server.am.ProcessRecord processRecordPoll2 = arrayDeque.poll();
            z = z ? 1 : 0;
            zIsReachable = zIsReachable;
            processRecordPoll = processRecordPoll2;
            oomAdjuster = this;
        }
        int size5 = arrayList.size();
        if (size5 > 0) {
            int i2 = 0;
            for (int i3 = size5 - 1; i2 < i3; i3--) {
                com.android.server.am.ProcessRecord processRecord6 = arrayList.get(i2);
                com.android.server.am.ProcessRecord processRecord7 = arrayList.get(i3);
                processRecord6.mState.setReachable(false);
                processRecord7.mState.setReachable(false);
                arrayList.set(i2, processRecord7);
                arrayList.set(i3, processRecord6);
                i2++;
            }
        }
        return zIsReachable;
    }

    void enqueueOomAdjTargetLocked(com.android.server.am.ProcessRecord app) {
        if (app != null && app.mState.getMaxAdj() > 0) {
            this.mPendingProcessSet.add(app);
        }
    }

    void removeOomAdjTargetLocked(com.android.server.am.ProcessRecord app, boolean procDied) {
        if (app != null) {
            this.mPendingProcessSet.remove(app);
            if (procDied) {
                com.android.server.am.PlatformCompatCache.getInstance().invalidate(app.info);
            }
        }
    }

    private boolean checkAndEnqueueOomAdjTargetLocked(com.android.server.am.ProcessRecord app) {
        if (!this.mOomAdjUpdateOngoing) {
            return false;
        }
        if (app != null) {
            this.mPendingProcessSet.add(app);
        } else {
            this.mPendingFullOomAdjUpdate = true;
        }
        return true;
    }

    void updateOomAdjPendingTargetsLocked(int oomAdjReason) {
        if (this.mPendingFullOomAdjUpdate) {
            this.mPendingFullOomAdjUpdate = false;
            this.mPendingProcessSet.clear();
            updateOomAdjLocked(oomAdjReason);
        } else {
            if (this.mPendingProcessSet.isEmpty() || this.mOomAdjUpdateOngoing) {
                return;
            }
            try {
                this.mOomAdjUpdateOngoing = true;
                performUpdateOomAdjPendingTargetsLocked(oomAdjReason);
            } finally {
                this.mOomAdjUpdateOngoing = false;
                updateOomAdjPendingTargetsLocked(oomAdjReason);
            }
        }
    }

    void updateOomAdjFollowUpTargetsLocked() {
        long now = this.mInjector.getUptimeMillis();
        long nextFollowUpUptimeMs = Long.MAX_VALUE;
        this.mNextFollowUpUpdateUptimeMs = Long.MAX_VALUE;
        for (int i = this.mFollowUpUpdateSet.size() - 1; i >= 0; i--) {
            com.android.server.am.ProcessRecord proc = (com.android.server.am.ProcessRecord) this.mFollowUpUpdateSet.valueAtUnchecked(i);
            long followUpUptimeMs = proc.mState.getFollowupUpdateUptimeMs();
            if (proc.isKilled()) {
                this.mFollowUpUpdateSet.removeAt(i);
            } else if (followUpUptimeMs <= now) {
                this.mPendingProcessSet.add(proc);
                proc.mState.setFollowupUpdateUptimeMs(Long.MAX_VALUE);
                this.mFollowUpUpdateSet.removeAt(i);
            } else if (followUpUptimeMs < nextFollowUpUptimeMs) {
                nextFollowUpUptimeMs = followUpUptimeMs;
            } else if (followUpUptimeMs == Long.MAX_VALUE) {
                this.mFollowUpUpdateSet.removeAt(i);
            }
        }
        if (nextFollowUpUptimeMs != Long.MAX_VALUE) {
            scheduleFollowUpOomAdjusterUpdateLocked(nextFollowUpUptimeMs, now);
        }
        updateOomAdjPendingTargetsLocked(23);
    }

    protected void performUpdateOomAdjPendingTargetsLocked(int oomAdjReason) {
        com.android.server.am.ProcessRecord topApp = this.mService.getTopApp();
        this.mLastReason = oomAdjReason;
        android.os.Trace.traceBegin(64L, oomAdjReasonToString(oomAdjReason));
        this.mProcessStateCurTop = enqueuePendingTopAppIfNecessaryLSP();
        java.util.ArrayList<com.android.server.am.ProcessRecord> processes = this.mTmpProcessList;
        com.android.server.am.ActiveUids uids = this.mTmpUidRecords;
        collectReachableProcessesLocked(this.mPendingProcessSet, processes, uids);
        this.mPendingProcessSet.clear();
        com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
        com.android.server.am.ActivityManagerService.boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            try {
                mOomAdjusterExt.onPendingOomAdjUpdateLSP(processes, oomAdjReason, processes == null, null);
                updateOomAdjInnerLSP(oomAdjReason, topApp, processes, uids, true, false);
            } catch (java.lang.Throwable th) {
                com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                throw th;
            }
        }
        com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
        processes.clear();
        this.mService.clearPendingTopAppLocked();
        android.os.Trace.traceEnd(64L);
    }

    private void updateOomAdjInnerLSP(int oomAdjReason, com.android.server.am.ProcessRecord topApp, java.util.ArrayList<com.android.server.am.ProcessRecord> processes, com.android.server.am.ActiveUids uids, boolean potentialCycles, boolean startProfiling) throws java.lang.Throwable {
        com.android.server.am.ActiveUids activeUids;
        int i;
        int i2;
        int numProc;
        long j;
        com.android.server.am.ActiveUids activeUids2;
        boolean z = true;
        boolean z2 = false;
        boolean fullUpdate = processes == null;
        java.util.ArrayList<com.android.server.am.ProcessRecord> activeProcesses = fullUpdate ? this.mProcessList.getLruProcessesLOSP() : processes;
        if (uids != null) {
            activeUids = uids;
        } else {
            int numUids = this.mActiveUids.size();
            com.android.server.am.ActiveUids activeUids3 = this.mTmpUidRecords;
            activeUids3.clear();
            for (int i3 = 0; i3 < numUids; i3++) {
                com.android.server.am.UidRecord uidRec = this.mActiveUids.valueAt(i3);
                activeUids3.put(uidRec.getUid(), uidRec);
            }
            activeUids = activeUids3;
        }
        this.mLastReason = oomAdjReason;
        long j2 = 64;
        if (startProfiling) {
            android.os.Trace.traceBegin(64L, oomAdjReasonToString(oomAdjReason));
        }
        long now = this.mInjector.getUptimeMillis();
        long nowElapsed = this.mInjector.getElapsedRealtimeMillis();
        long oldTime = now - this.mConstants.mMaxEmptyTimeMillis;
        int numProc2 = activeProcesses.size();
        this.mAdjSeq++;
        if (fullUpdate) {
            this.mNewNumServiceProcs = 0;
            this.mNewNumAServiceProcs = 0;
        }
        resetUidRecordsLsp(activeUids);
        if (!fullUpdate && !potentialCycles) {
            z = false;
        }
        boolean computeClients = z;
        mOomAdjusterExt.updateRecentLockApps();
        for (int i4 = numProc2 - 1; i4 >= 0; i4--) {
            com.android.server.am.ProcessStateRecord state = activeProcesses.get(i4).mState;
            state.setReachable(false);
            if (state.getAdjSeq() != this.mAdjSeq) {
                state.setContainsCycle(false);
                state.setCurRawProcState(19);
                state.setCurRawAdj(1001);
                state.setSetCapability(0);
                state.resetCachedInfo();
                state.setCurBoundByNonBgRestrictedApp(false);
            }
        }
        this.mProcessesInCycle.clear();
        int i5 = numProc2 - 1;
        boolean retryCycles = false;
        while (i5 >= 0) {
            com.android.server.am.ProcessRecord app = activeProcesses.get(i5);
            com.android.server.am.ProcessStateRecord state2 = app.mState;
            if (app.isKilledByAm() || app.getThread() == null) {
                i2 = i5;
                numProc = numProc2;
                j = j2;
                activeUids2 = activeUids;
            } else {
                state2.setProcStateChanged(z2);
                app.mOptRecord.setLastOomAdjChangeReason(oomAdjReason);
                i2 = i5;
                numProc = numProc2;
                j = j2;
                activeUids2 = activeUids;
                computeOomAdjLSP(app, 1001, topApp, fullUpdate, now, false, computeClients, oomAdjReason, true);
                boolean retryCycles2 = retryCycles | state2.containsCycle();
                state2.setCompletedAdjSeq(this.mAdjSeq);
                retryCycles = retryCycles2;
            }
            i5 = i2 - 1;
            numProc2 = numProc;
            activeUids = activeUids2;
            j2 = j;
            z2 = false;
        }
        int numProc3 = numProc2;
        long j3 = j2;
        com.android.server.am.ActiveUids activeUids4 = activeUids;
        if (this.mCacheOomRanker.useOomReranking()) {
            this.mCacheOomRanker.reRankLruCachedAppsLSP(this.mProcessList.getLruProcessesLSP(), this.mProcessList.getLruProcessServiceStartLOSP());
        }
        if (computeClients) {
            int cycleCount = 0;
            while (retryCycles && cycleCount < 10) {
                int cycleCount2 = cycleCount + 1;
                for (int i6 = 0; i6 < numProc3; i6++) {
                    com.android.server.am.ProcessRecord app2 = activeProcesses.get(i6);
                    com.android.server.am.ProcessStateRecord state3 = app2.mState;
                    if (!app2.isKilledByAm() && app2.getThread() != null && state3.containsCycle()) {
                        state3.decAdjSeq();
                        state3.decCompletedAdjSeq();
                    }
                }
                retryCycles = false;
                int i7 = 0;
                while (i7 < numProc3) {
                    com.android.server.am.ProcessRecord app3 = activeProcesses.get(i7);
                    com.android.server.am.ProcessStateRecord state4 = app3.mState;
                    if (app3.isKilledByAm() || app3.getThread() == null || !state4.containsCycle()) {
                        i = i7;
                    } else {
                        i = i7;
                        if (computeOomAdjLSP(app3, 1001, topApp, true, now, true, true, oomAdjReason, true)) {
                            retryCycles = true;
                        }
                    }
                    i7 = i + 1;
                }
                cycleCount = cycleCount2;
            }
        }
        this.mProcessesInCycle.clear();
        assignCachedAdjIfNecessary(this.mProcessList.getLruProcessesLOSP());
        postUpdateOomAdjInnerLSP(oomAdjReason, activeUids4, now, nowElapsed, oldTime, true);
        if (startProfiling) {
            android.os.Trace.traceEnd(j3);
        }
    }

    private void resetUidRecordsLsp(com.android.server.am.ActiveUids activeUids) {
        for (int i = activeUids.size() - 1; i >= 0; i--) {
            com.android.server.am.UidRecord uidRec = activeUids.valueAt(i);
            if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_UID_OBSERVERS) {
                android.util.Slog.i(com.android.server.am.ActivityManagerService.TAG_UID_OBSERVERS, "Starting update of " + uidRec);
            }
            uidRec.reset();
        }
    }

    protected void postUpdateOomAdjInnerLSP(int oomAdjReason, com.android.server.am.ActiveUids activeUids, long now, long nowElapsed, long oldTime, boolean doingAll) throws java.lang.Throwable {
        this.mNumNonCachedProcs = 0;
        this.mNumCachedHiddenProcs = 0;
        updateAndTrimProcessLSP(now, nowElapsed, oldTime, activeUids, oomAdjReason, doingAll);
        this.mNumServiceProcs = this.mNewNumServiceProcs;
        if (this.mService.mAlwaysFinishActivities) {
            this.mService.mAtmInternal.scheduleDestroyAllActivities("always-finish");
        }
        updateUidsLSP(activeUids, nowElapsed);
        synchronized (this.mService.mProcessStats.mLock) {
            long nowUptime = this.mInjector.getUptimeMillis();
            if (this.mService.mProcessStats.shouldWriteNowLocked(nowUptime)) {
                this.mService.mHandler.post(new com.android.server.am.ActivityManagerService.ProcStatsRunnable(this.mService, this.mService.mProcessStats));
            }
            this.mService.mProcessStats.updateTrackingAssociationsLocked(this.mAdjSeq, nowUptime);
        }
        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_OOM_ADJ) {
            long duration = this.mInjector.getUptimeMillis() - now;
            android.util.Slog.d(com.android.server.am.ActivityManagerService.TAG_OOM_ADJ, "Did OOM ADJ in " + duration + "ms");
        }
    }

    protected void assignCachedAdjIfNecessary(java.util.ArrayList<com.android.server.am.ProcessRecord> lruList) {
        int numLru;
        int curCachedAdj;
        int emptyProcessLimit;
        int cachedProcessLimit;
        int numEmptyProcs;
        int stepEmpty;
        int stepEmpty2;
        int lastCachedGroup;
        int lastCachedGroupImportance;
        int curCachedImpAdj;
        int curEmptyAdj;
        int curEmptyAdj2;
        int emptyFactor;
        int stepEmpty3;
        int lastCachedGroup2;
        int stepEmpty4;
        int curCachedAdj2;
        java.util.ArrayList<com.android.server.am.ProcessRecord> arrayList = lruList;
        int numLru2 = lruList.size();
        if (this.mConstants.USE_TIERED_CACHED_ADJ) {
            long now = this.mInjector.getUptimeMillis();
            for (int i = numLru2 - 1; i >= 0; i--) {
                com.android.server.am.ProcessRecord app = arrayList.get(i);
                com.android.server.am.ProcessStateRecord state = app.mState;
                com.android.server.am.ProcessCachedOptimizerRecord opt = app.mOptRecord;
                if (!app.isKilledByAm() && app.getThread() != null && state.getCurAdj() >= 1001) {
                    com.android.server.am.ProcessServiceRecord psr = app.mServices;
                    int targetAdj = (opt == null || !opt.isFreezeExempt()) ? (state.getSetAdj() < 900 || state.getLastStateTime() + this.mConstants.TIERED_CACHED_ADJ_DECAY_TIME >= now) ? 900 + 10 : 900 + 50 : 900 + 0;
                    state.setCurRawAdj(targetAdj);
                    state.setCurAdj(psr.modifyRawOomAdj(targetAdj));
                }
            }
            return;
        }
        int curCachedAdj3 = 900;
        int nextCachedAdj = 900 + 10;
        int curEmptyAdj3 = 0;
        int lastCachedGroupImportance2 = 905;
        int nextEmptyAdj = 905 + 10;
        int emptyProcessLimit2 = this.mConstants.CUR_MAX_EMPTY_PROCESSES;
        int cachedProcessLimit2 = this.mConstants.CUR_MAX_CACHED_PROCESSES - emptyProcessLimit2;
        int numEmptyProcs2 = (numLru2 - this.mNumNonCachedProcs) - this.mNumCachedHiddenProcs;
        if (numEmptyProcs2 > cachedProcessLimit2) {
            numEmptyProcs2 = cachedProcessLimit2;
        }
        int cachedFactor = (this.mNumCachedHiddenProcs > 0 ? (this.mNumCachedHiddenProcs + this.mNumSlots) - 1 : 1) / this.mNumSlots;
        if (cachedFactor < 1) {
            cachedFactor = 1;
        }
        int emptyFactor2 = ((this.mNumSlots + numEmptyProcs2) - 1) / this.mNumSlots;
        if (emptyFactor2 < 1) {
            emptyFactor2 = 1;
        }
        int stepCached = -1;
        int lastCachedGroup3 = -1;
        int lastCachedGroupUid = 0;
        int curCachedImpAdj2 = 0;
        int lastCachedGroupUid2 = 0;
        int i2 = numLru2 - 1;
        while (i2 >= 0) {
            com.android.server.am.ProcessRecord app2 = arrayList.get(i2);
            com.android.server.am.ProcessStateRecord state2 = app2.mState;
            if (app2.isKilledByAm() || app2.getThread() == null) {
                numLru = numLru2;
                curCachedAdj = curCachedAdj3;
                emptyProcessLimit = emptyProcessLimit2;
                cachedProcessLimit = cachedProcessLimit2;
                numEmptyProcs = numEmptyProcs2;
                stepEmpty = lastCachedGroup3;
                stepEmpty2 = lastCachedGroupUid;
                lastCachedGroup = lastCachedGroupUid2;
                int i3 = curCachedImpAdj2;
                lastCachedGroupImportance = curEmptyAdj3;
                curCachedImpAdj = lastCachedGroupImportance2;
                curEmptyAdj = i3;
            } else {
                numLru = numLru2;
                emptyProcessLimit = emptyProcessLimit2;
                if (state2.getCurAdj() >= 1001) {
                    com.android.server.am.ProcessServiceRecord psr2 = app2.mServices;
                    cachedProcessLimit = cachedProcessLimit2;
                    numEmptyProcs = numEmptyProcs2;
                    switch (state2.getCurProcState()) {
                        case 16:
                        case 17:
                        case 18:
                            boolean inGroup = false;
                            int connectionGroup = psr2.getConnectionGroup();
                            if (connectionGroup != 0) {
                                emptyFactor = emptyFactor2;
                                int connectionImportance = psr2.getConnectionImportance();
                                stepEmpty3 = lastCachedGroup3;
                                int stepEmpty5 = app2.uid;
                                curEmptyAdj2 = lastCachedGroupImportance2;
                                int curEmptyAdj4 = lastCachedGroupUid2;
                                if (curEmptyAdj4 == stepEmpty5 && (lastCachedGroup2 = lastCachedGroupUid) == connectionGroup) {
                                    int lastCachedGroupUid3 = curCachedImpAdj2;
                                    if (connectionImportance <= lastCachedGroupUid3) {
                                        curCachedImpAdj2 = lastCachedGroupUid3;
                                    } else if (curCachedAdj3 < nextCachedAdj) {
                                        curCachedImpAdj2 = connectionImportance;
                                        if (curCachedAdj3 < 999) {
                                            curEmptyAdj3++;
                                        }
                                    } else {
                                        curCachedImpAdj2 = connectionImportance;
                                    }
                                    inGroup = true;
                                    lastCachedGroupUid2 = curEmptyAdj4;
                                    lastCachedGroupUid = lastCachedGroup2;
                                } else {
                                    int lastCachedGroupImportance3 = curEmptyAdj3;
                                    int curCachedImpAdj3 = app2.uid;
                                    lastCachedGroupUid = connectionGroup;
                                    lastCachedGroupUid2 = curCachedImpAdj3;
                                    curEmptyAdj3 = lastCachedGroupImportance3;
                                    curCachedImpAdj2 = connectionImportance;
                                }
                            } else {
                                curEmptyAdj2 = lastCachedGroupImportance2;
                                emptyFactor = emptyFactor2;
                                stepEmpty3 = lastCachedGroup3;
                                int stepEmpty6 = lastCachedGroupUid;
                                int curEmptyAdj5 = curCachedImpAdj2;
                                curCachedImpAdj2 = curEmptyAdj5;
                                lastCachedGroupUid = stepEmpty6;
                            }
                            if (!inGroup && curCachedAdj3 != nextCachedAdj) {
                                stepCached++;
                                curEmptyAdj3 = 0;
                                if (stepCached >= cachedFactor) {
                                    stepCached = 0;
                                    curCachedAdj3 = nextCachedAdj;
                                    nextCachedAdj += 10;
                                    if (nextCachedAdj > 999) {
                                        nextCachedAdj = 999;
                                    }
                                }
                            }
                            state2.setCurRawAdj(curCachedAdj3 + curEmptyAdj3);
                            state2.setCurAdj(psr2.modifyRawOomAdj(curCachedAdj3 + curEmptyAdj3));
                            if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_LRU) {
                                android.util.Slog.d(com.android.server.am.ActivityManagerService.TAG_LRU, "Assigning activity LRU #" + i2 + " adj: " + state2.getCurAdj() + " (curCachedAdj=" + curCachedAdj3 + " curCachedImpAdj=" + curEmptyAdj3 + ")");
                            }
                            emptyFactor2 = emptyFactor;
                            lastCachedGroup3 = stepEmpty3;
                            lastCachedGroupImportance2 = curEmptyAdj2;
                            break;
                        default:
                            int curEmptyAdj6 = lastCachedGroupImportance2;
                            int emptyFactor3 = emptyFactor2;
                            int stepEmpty7 = lastCachedGroup3;
                            int stepEmpty8 = lastCachedGroupUid;
                            int curEmptyAdj7 = curCachedImpAdj2;
                            int lastCachedGroup4 = lastCachedGroupUid2;
                            int lastCachedGroupImportance4 = curEmptyAdj3;
                            int curEmptyAdj8 = curEmptyAdj6;
                            if (curEmptyAdj8 != nextEmptyAdj) {
                                stepEmpty4 = stepEmpty7 + 1;
                                emptyFactor2 = emptyFactor3;
                                if (stepEmpty4 >= emptyFactor2) {
                                    stepEmpty4 = 0;
                                    curEmptyAdj8 = nextEmptyAdj;
                                    nextEmptyAdj += 10;
                                    if (nextEmptyAdj > 999) {
                                        nextEmptyAdj = 999;
                                    }
                                }
                            } else {
                                emptyFactor2 = emptyFactor3;
                                stepEmpty4 = stepEmpty7;
                            }
                            state2.setCurRawAdj(curEmptyAdj8);
                            state2.setCurAdj(psr2.modifyRawOomAdj(curEmptyAdj8));
                            if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_LRU) {
                                curCachedAdj2 = curCachedAdj3;
                                android.util.Slog.d(com.android.server.am.ActivityManagerService.TAG_LRU, "Assigning empty LRU #" + i2 + " adj: " + state2.getCurAdj() + " (curEmptyAdj=" + curEmptyAdj8 + ")");
                            } else {
                                curCachedAdj2 = curCachedAdj3;
                            }
                            lastCachedGroupUid2 = lastCachedGroup4;
                            curCachedAdj3 = curCachedAdj2;
                            lastCachedGroupUid = stepEmpty8;
                            lastCachedGroup3 = stepEmpty4;
                            lastCachedGroupImportance2 = curEmptyAdj8;
                            curEmptyAdj3 = lastCachedGroupImportance4;
                            curCachedImpAdj2 = curEmptyAdj7;
                            break;
                    }
                    i2--;
                    arrayList = lruList;
                    numLru2 = numLru;
                    emptyProcessLimit2 = emptyProcessLimit;
                    cachedProcessLimit2 = cachedProcessLimit;
                    numEmptyProcs2 = numEmptyProcs;
                } else {
                    curCachedAdj = curCachedAdj3;
                    cachedProcessLimit = cachedProcessLimit2;
                    numEmptyProcs = numEmptyProcs2;
                    stepEmpty = lastCachedGroup3;
                    stepEmpty2 = lastCachedGroupUid;
                    lastCachedGroup = lastCachedGroupUid2;
                    int i4 = curCachedImpAdj2;
                    lastCachedGroupImportance = curEmptyAdj3;
                    curCachedImpAdj = lastCachedGroupImportance2;
                    curEmptyAdj = i4;
                }
            }
            lastCachedGroupUid2 = lastCachedGroup;
            curCachedAdj3 = curCachedAdj;
            lastCachedGroupUid = stepEmpty2;
            lastCachedGroup3 = stepEmpty;
            int i5 = curEmptyAdj;
            lastCachedGroupImportance2 = curCachedImpAdj;
            curEmptyAdj3 = lastCachedGroupImportance;
            curCachedImpAdj2 = i5;
            i2--;
            arrayList = lruList;
            numLru2 = numLru;
            emptyProcessLimit2 = emptyProcessLimit;
            cachedProcessLimit2 = cachedProcessLimit;
            numEmptyProcs2 = numEmptyProcs;
        }
    }

    private static double getFreeSwapPercent() {
        return com.android.server.am.CachedAppOptimizer.getFreeSwapPercent();
    }

    private void updateAndTrimProcessLSP(long now, long nowElapsed, long oldTime, com.android.server.am.ActiveUids activeUids, int oomAdjReason, boolean doingAll) throws java.lang.Throwable {
        int i;
        double freeSwapPercent;
        int i2;
        com.android.server.am.ProcessRecord lruCachedApp;
        java.util.ArrayList<com.android.server.am.ProcessRecord> lruList;
        double lowSwapThresholdPercent;
        int numEmpty;
        int lastCachedGroup;
        int lastCachedGroup2;
        int lastCachedGroupUid;
        com.android.server.am.ProcessStateRecord state;
        com.android.server.am.ProcessRecord app;
        int lastCachedGroup3;
        int lastCachedGroup4;
        java.util.ArrayList<com.android.server.am.ProcessRecord> lruList2 = this.mProcessList.getLruProcessesLOSP();
        int numLru = lruList2.size();
        boolean doKillExcessiveProcesses = shouldKillExcessiveProcesses(now);
        if (!doKillExcessiveProcesses && this.mNextNoKillDebugMessageTime < now) {
            android.util.Slog.d(TAG, "Not killing cached processes");
            this.mNextNoKillDebugMessageTime = now + 5000;
        }
        int i3 = Integer.MAX_VALUE;
        if (!doKillExcessiveProcesses) {
            i = Integer.MAX_VALUE;
        } else {
            i = this.mConstants.CUR_MAX_EMPTY_PROCESSES;
        }
        int emptyProcessLimit = i;
        if (doKillExcessiveProcesses) {
            i3 = this.mConstants.CUR_MAX_CACHED_PROCESSES - emptyProcessLimit;
        }
        int cachedProcessLimit = i3;
        boolean proactiveKillsEnabled = com.android.server.am.ActivityManagerConstants.PROACTIVE_KILLS_ENABLED;
        double lowSwapThresholdPercent2 = com.android.server.am.ActivityManagerConstants.LOW_SWAP_THRESHOLD_PERCENT;
        double freeSwapPercent2 = proactiveKillsEnabled ? getFreeSwapPercent() : 1.0d;
        int numCachedExtraGroup = 0;
        int numEmpty2 = 0;
        int numTrimming = 0;
        com.android.server.am.ProcessRecord lruCachedApp2 = null;
        int lastCachedGroup5 = 0;
        int lastCachedGroupUid2 = 0;
        int numCached = 0;
        int numCached2 = numLru - 1;
        while (numCached2 >= 0) {
            double lowSwapThresholdPercent3 = lowSwapThresholdPercent2;
            com.android.server.am.ProcessRecord app2 = lruList2.get(numCached2);
            com.android.server.am.ProcessStateRecord state2 = app2.mState;
            if (!app2.isKilledByAm() && app2.getThread() != null) {
                if (state2.getCompletedAdjSeq() != this.mAdjSeq) {
                    i2 = numCached2;
                    lruCachedApp = lruCachedApp2;
                    lastCachedGroup2 = lastCachedGroup5;
                    lastCachedGroupUid = lastCachedGroupUid2;
                    state = state2;
                    app = app2;
                    lruList = lruList2;
                    lowSwapThresholdPercent = lowSwapThresholdPercent3;
                    numEmpty = numEmpty2;
                } else {
                    lruList = lruList2;
                    i2 = numCached2;
                    numEmpty = numEmpty2;
                    lruCachedApp = lruCachedApp2;
                    lastCachedGroup2 = lastCachedGroup5;
                    lastCachedGroupUid = lastCachedGroupUid2;
                    state = state2;
                    lowSwapThresholdPercent = lowSwapThresholdPercent3;
                    app = app2;
                    applyOomAdjLSP(app2, doingAll, now, nowElapsed, oomAdjReason, true);
                }
                if (app.isPendingFinishAttach()) {
                    updateAppUidRecLSP(app);
                    lastCachedGroup = lastCachedGroup2;
                    lastCachedGroupUid2 = lastCachedGroupUid;
                } else {
                    com.android.server.am.ProcessRecord app3 = app;
                    com.android.server.am.ProcessServiceRecord psr = app3.mServices;
                    switch (state.getCurProcState()) {
                        case 16:
                        case 17:
                            this.mNumCachedHiddenProcs++;
                            int numCached3 = numCached + 1;
                            int connectionGroup = psr.getConnectionGroup();
                            if (connectionGroup != 0) {
                                lastCachedGroupUid2 = lastCachedGroupUid;
                                if (lastCachedGroupUid2 == app3.info.uid && (lastCachedGroup3 = lastCachedGroup2) == connectionGroup) {
                                    numCachedExtraGroup++;
                                    lastCachedGroup5 = lastCachedGroup3;
                                } else {
                                    lastCachedGroupUid2 = app3.info.uid;
                                    lastCachedGroup5 = connectionGroup;
                                }
                            } else {
                                lastCachedGroup5 = 0;
                                lastCachedGroupUid2 = 0;
                            }
                            if (numCached3 - numCachedExtraGroup > cachedProcessLimit) {
                                if (!mOomAdjusterExt.onHookKillCacheEmpty(app3.getWindowProcessController())) {
                                    app3.killLocked("cached #" + numCached3, "too many cached", 13, 2, true);
                                }
                            } else if (proactiveKillsEnabled) {
                                lruCachedApp2 = app3;
                                numCached = numCached3;
                                numEmpty2 = numEmpty;
                            }
                            numCached = numCached3;
                            numEmpty2 = numEmpty;
                            lruCachedApp2 = lruCachedApp;
                            break;
                        case 18:
                        default:
                            lastCachedGroup4 = lastCachedGroup2;
                            lastCachedGroupUid2 = lastCachedGroupUid;
                            this.mNumNonCachedProcs++;
                            lastCachedGroup5 = lastCachedGroup4;
                            numEmpty2 = numEmpty;
                            lruCachedApp2 = lruCachedApp;
                            break;
                        case 19:
                            if (numEmpty > this.mConstants.CUR_TRIM_EMPTY_PROCESSES && app3.getLastActivityTime() < oldTime) {
                                if (mOomAdjusterExt.onHookKillCacheEmpty(app3.getWindowProcessController())) {
                                    lastCachedGroup4 = lastCachedGroup2;
                                    lastCachedGroupUid2 = lastCachedGroupUid;
                                } else {
                                    app3.killLocked("empty for " + ((now - app3.getLastActivityTime()) / 1000) + "s", "empty for too long", 13, 4, true);
                                    lastCachedGroup4 = lastCachedGroup2;
                                    lastCachedGroupUid2 = lastCachedGroupUid;
                                }
                                lastCachedGroup5 = lastCachedGroup4;
                                numEmpty2 = numEmpty;
                                lruCachedApp2 = lruCachedApp;
                            } else {
                                numEmpty2 = numEmpty + 1;
                                if (numEmpty2 > emptyProcessLimit) {
                                    if (!mOomAdjusterExt.onHookKillCacheEmpty(app3.getWindowProcessController())) {
                                        app3.killLocked("empty #" + numEmpty2, "too many empty", 13, 3, true);
                                    }
                                } else if (proactiveKillsEnabled) {
                                    lruCachedApp2 = app3;
                                    lastCachedGroup5 = lastCachedGroup2;
                                    lastCachedGroupUid2 = lastCachedGroupUid;
                                }
                                lruCachedApp2 = lruCachedApp;
                                lastCachedGroup5 = lastCachedGroup2;
                                lastCachedGroupUid2 = lastCachedGroupUid;
                            }
                            break;
                    }
                    if (app3.isolated && psr.numberOfRunningServices() <= 0 && app3.getIsolatedEntryPoint() == null) {
                        app3.killLocked("isolated not needed", 13, 17, true);
                    } else if (app3.isSdkSandbox && psr.numberOfRunningServices() <= 0 && app3.getActiveInstrumentation() == null) {
                        app3.killLocked("sandbox not needed", 13, 28, true);
                    } else {
                        updateAppUidRecLSP(app3);
                    }
                    if (state.getCurProcState() >= 14 && !app3.isKilledByAm()) {
                        numTrimming++;
                    }
                    numCached2 = i2 - 1;
                    lruList2 = lruList;
                    lowSwapThresholdPercent2 = lowSwapThresholdPercent;
                }
            } else {
                i2 = numCached2;
                lruCachedApp = lruCachedApp2;
                lruList = lruList2;
                lowSwapThresholdPercent = lowSwapThresholdPercent3;
                numEmpty = numEmpty2;
                lastCachedGroup = lastCachedGroup5;
            }
            lastCachedGroup5 = lastCachedGroup;
            numEmpty2 = numEmpty;
            lruCachedApp2 = lruCachedApp;
            numCached2 = i2 - 1;
            lruList2 = lruList;
            lowSwapThresholdPercent2 = lowSwapThresholdPercent;
        }
        com.android.server.am.ProcessRecord lruCachedApp3 = lruCachedApp2;
        double lowSwapThresholdPercent4 = lowSwapThresholdPercent2;
        int numEmpty3 = numEmpty2;
        if (!this.mProcsToOomAdj.isEmpty()) {
            com.android.server.am.ProcessList.batchSetOomAdj(this.mProcsToOomAdj);
            this.mProcsToOomAdj.clear();
        }
        if (proactiveKillsEnabled && doKillExcessiveProcesses) {
            freeSwapPercent = freeSwapPercent2;
            if (freeSwapPercent < lowSwapThresholdPercent4 && lruCachedApp3 != null && freeSwapPercent < this.mLastFreeSwapPercent) {
                lruCachedApp3.killLocked("swap low and too many cached", 13, 2, true);
            }
        } else {
            freeSwapPercent = freeSwapPercent2;
        }
        this.mLastFreeSwapPercent = freeSwapPercent;
        this.mService.mAppProfiler.updateLowMemStateLSP(numCached, numEmpty3, numTrimming, now);
    }

    protected void updateAppUidRecIfNecessaryLSP(com.android.server.am.ProcessRecord app) {
        if (!app.isKilledByAm() && app.getThread() != null) {
            if (!app.isolated || app.mServices.numberOfRunningServices() > 0 || app.getIsolatedEntryPoint() != null) {
                updateAppUidRecLSP(app);
            }
        }
    }

    private void updateAppUidRecLSP(com.android.server.am.ProcessRecord app) {
        com.android.server.am.UidRecord uidRec = app.getUidRecord();
        if (uidRec != null) {
            com.android.server.am.ProcessStateRecord state = app.mState;
            uidRec.setEphemeral(app.info.isInstantApp());
            if (uidRec.getCurProcState() > state.getCurProcState()) {
                uidRec.setCurProcState(state.getCurProcState());
            }
            if (app.mServices.hasForegroundServices()) {
                uidRec.setForegroundServices(true);
            }
            uidRec.setCurCapability(uidRec.getCurCapability() | state.getCurCapability());
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:100:0x0244  */
    /* JADX WARN: Removed duplicated region for block: B:105:0x0252  */
    /* JADX WARN: Removed duplicated region for block: B:108:0x0269  */
    /* JADX WARN: Removed duplicated region for block: B:129:0x0270 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:62:0x0196  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x0198  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x019f  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x01ab  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x01ad  */
    /* JADX WARN: Removed duplicated region for block: B:73:0x01b0  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x01bd  */
    /* JADX WARN: Removed duplicated region for block: B:80:0x01cb  */
    /* JADX WARN: Removed duplicated region for block: B:83:0x01d2  */
    /* JADX WARN: Removed duplicated region for block: B:92:0x0210  */
    /* JADX WARN: Removed duplicated region for block: B:98:0x0233  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected void updateUidsLSP(com.android.server.am.ActiveUids r26, long r27) {
        /*
            Method dump skipped, instruction units count: 694
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.OomAdjuster.updateUidsLSP(com.android.server.am.ActiveUids, long):void");
    }

    private boolean shouldKillExcessiveProcesses(long nowUptime) {
        long lastUserUnlockingUptime = this.mService.mUserController.getLastUserUnlockingUptime();
        if (lastUserUnlockingUptime == 0) {
            return !this.mConstants.mNoKillCachedProcessesUntilBootCompleted;
        }
        long noKillCachedProcessesPostBootCompletedDurationMillis = this.mConstants.mNoKillCachedProcessesPostBootCompletedDurationMillis;
        return lastUserUnlockingUptime + noKillCachedProcessesPostBootCompletedDurationMillis <= nowUptime;
    }

    final class ComputeOomAdjWindowCallback implements com.android.server.wm.WindowProcessController.ComputeOomAdjCallback {
        int adj;
        com.android.server.am.ProcessRecord app;
        int appUid;
        boolean foregroundActivities;
        int logUid;
        java.lang.String mAdjType;
        boolean mHasVisibleActivities;
        com.android.server.am.ProcessStateRecord mState;
        int procState;
        int processStateCurTop;
        int schedGroup;

        ComputeOomAdjWindowCallback() {
        }

        void initialize(com.android.server.am.ProcessRecord app, int adj, boolean foregroundActivities, boolean hasVisibleActivities, int procState, int schedGroup, int appUid, int logUid, int processStateCurTop) {
            this.app = app;
            this.adj = adj;
            this.foregroundActivities = foregroundActivities;
            this.mHasVisibleActivities = hasVisibleActivities;
            this.procState = procState;
            this.schedGroup = schedGroup;
            this.appUid = appUid;
            this.logUid = logUid;
            this.processStateCurTop = processStateCurTop;
            this.mAdjType = app.mState.getAdjType();
            this.mState = app.mState;
        }

        @Override // com.android.server.wm.WindowProcessController.ComputeOomAdjCallback
        public void onVisibleActivity() {
            if (this.adj > 100) {
                this.adj = 100;
                this.mAdjType = "vis-activity";
                if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_OOM_ADJ_REASON || this.logUid == this.appUid) {
                    com.android.server.am.OomAdjuster.this.reportOomAdjMessageLocked(com.android.server.am.ActivityManagerService.TAG_OOM_ADJ, "Raise adj to vis-activity: " + this.app);
                }
            }
            if (this.procState > this.processStateCurTop) {
                this.procState = this.processStateCurTop;
                this.mAdjType = "vis-activity";
                if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_OOM_ADJ_REASON || this.logUid == this.appUid) {
                    com.android.server.am.OomAdjuster.this.reportOomAdjMessageLocked(com.android.server.am.ActivityManagerService.TAG_OOM_ADJ, "Raise procstate to vis-activity (top): " + this.app);
                }
            }
            if (this.schedGroup < 2) {
                this.schedGroup = 2;
            }
            this.foregroundActivities = true;
            this.mHasVisibleActivities = true;
        }

        @Override // com.android.server.wm.WindowProcessController.ComputeOomAdjCallback
        public void onPausedActivity() {
            if (this.adj > 200) {
                this.adj = 200;
                this.mAdjType = "pause-activity";
                if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_OOM_ADJ_REASON || this.logUid == this.appUid) {
                    com.android.server.am.OomAdjuster.this.reportOomAdjMessageLocked(com.android.server.am.ActivityManagerService.TAG_OOM_ADJ, "Raise adj to pause-activity: " + this.app);
                }
            }
            if (this.procState > this.processStateCurTop) {
                this.procState = this.processStateCurTop;
                this.mAdjType = "pause-activity";
                if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_OOM_ADJ_REASON || this.logUid == this.appUid) {
                    com.android.server.am.OomAdjuster.this.reportOomAdjMessageLocked(com.android.server.am.ActivityManagerService.TAG_OOM_ADJ, "Raise procstate to pause-activity (top): " + this.app);
                }
            }
            if (this.schedGroup < 2) {
                this.schedGroup = 2;
            }
            this.foregroundActivities = true;
            this.mHasVisibleActivities = false;
        }

        @Override // com.android.server.wm.WindowProcessController.ComputeOomAdjCallback
        public void onStoppingActivity(boolean finishing) {
            if (this.adj > 200) {
                this.adj = 200;
                this.mAdjType = "stop-activity";
                if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_OOM_ADJ_REASON || this.logUid == this.appUid) {
                    com.android.server.am.OomAdjuster.this.reportOomAdjMessageLocked(com.android.server.am.ActivityManagerService.TAG_OOM_ADJ, "Raise adj to stop-activity: " + this.app);
                }
            }
            if (!finishing && this.procState > 15) {
                this.procState = 15;
                this.mAdjType = "stop-activity";
                if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_OOM_ADJ_REASON || this.logUid == this.appUid) {
                    com.android.server.am.OomAdjuster.this.reportOomAdjMessageLocked(com.android.server.am.ActivityManagerService.TAG_OOM_ADJ, "Raise procstate to stop-activity: " + this.app);
                }
            }
            this.foregroundActivities = true;
            this.mHasVisibleActivities = false;
        }

        @Override // com.android.server.wm.WindowProcessController.ComputeOomAdjCallback
        public void onOtherActivity() {
            if (this.procState > 16) {
                this.procState = 16;
                this.mAdjType = "cch-act";
                if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_OOM_ADJ_REASON || this.logUid == this.appUid) {
                    com.android.server.am.OomAdjuster.this.reportOomAdjMessageLocked(com.android.server.am.ActivityManagerService.TAG_OOM_ADJ, "Raise procstate to cached activity: " + this.app);
                }
            }
            this.mHasVisibleActivities = false;
        }
    }

    private boolean isScreenOnOrAnimatingLocked(com.android.server.am.ProcessStateRecord state) {
        return this.mService.mWakefulness.get() == 1 || state.isRunningRemoteAnimation();
    }

    /* JADX WARN: Removed duplicated region for block: B:139:0x03ae  */
    /* JADX WARN: Removed duplicated region for block: B:151:0x0400  */
    /* JADX WARN: Removed duplicated region for block: B:154:0x040f  */
    /* JADX WARN: Removed duplicated region for block: B:157:0x0419  */
    /* JADX WARN: Removed duplicated region for block: B:161:0x0423 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:164:0x042b  */
    /* JADX WARN: Removed duplicated region for block: B:168:0x0440  */
    /* JADX WARN: Removed duplicated region for block: B:169:0x0442  */
    /* JADX WARN: Removed duplicated region for block: B:170:0x044b  */
    /* JADX WARN: Removed duplicated region for block: B:175:0x045f  */
    /* JADX WARN: Removed duplicated region for block: B:189:0x04b7  */
    /* JADX WARN: Removed duplicated region for block: B:192:0x04c0  */
    /* JADX WARN: Removed duplicated region for block: B:209:0x0539  */
    /* JADX WARN: Removed duplicated region for block: B:213:0x0549  */
    /* JADX WARN: Removed duplicated region for block: B:222:0x056f  */
    /* JADX WARN: Removed duplicated region for block: B:226:0x05a0 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:227:0x05a2  */
    /* JADX WARN: Removed duplicated region for block: B:231:0x05bf A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:232:0x05c1  */
    /* JADX WARN: Removed duplicated region for block: B:235:0x05df  */
    /* JADX WARN: Removed duplicated region for block: B:249:0x0638  */
    /* JADX WARN: Removed duplicated region for block: B:263:0x0695  */
    /* JADX WARN: Removed duplicated region for block: B:293:0x0759  */
    /* JADX WARN: Removed duplicated region for block: B:296:0x075e  */
    /* JADX WARN: Removed duplicated region for block: B:299:0x079d  */
    /* JADX WARN: Removed duplicated region for block: B:303:0x07a7  */
    /* JADX WARN: Removed duplicated region for block: B:315:0x07f1  */
    /* JADX WARN: Removed duplicated region for block: B:321:0x082e  */
    /* JADX WARN: Removed duplicated region for block: B:367:0x0944  */
    /* JADX WARN: Removed duplicated region for block: B:428:0x0b30  */
    /* JADX WARN: Removed duplicated region for block: B:470:0x0ca3  */
    /* JADX WARN: Removed duplicated region for block: B:488:0x0d2b  */
    /* JADX WARN: Removed duplicated region for block: B:495:0x0d48  */
    /* JADX WARN: Removed duplicated region for block: B:525:0x0dd3  */
    /* JADX WARN: Removed duplicated region for block: B:529:0x0dea  */
    /* JADX WARN: Removed duplicated region for block: B:534:0x0dfe  */
    /* JADX WARN: Removed duplicated region for block: B:539:0x0e1c  */
    /* JADX WARN: Removed duplicated region for block: B:544:0x0e28  */
    /* JADX WARN: Removed duplicated region for block: B:550:0x0e3d A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:553:0x0e44  */
    /* JADX WARN: Removed duplicated region for block: B:556:0x0e4d  */
    /* JADX WARN: Removed duplicated region for block: B:559:0x0e55  */
    /* JADX WARN: Removed duplicated region for block: B:561:0x0e65  */
    /* JADX WARN: Removed duplicated region for block: B:572:0x0b0f A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:576:0x0c89 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected boolean computeOomAdjLSP(com.android.server.am.ProcessRecord r49, int r50, com.android.server.am.ProcessRecord r51, boolean r52, long r53, boolean r55, boolean r56, int r57, boolean r58) {
        /*
            Method dump skipped, instruction units count: 3762
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.OomAdjuster.computeOomAdjLSP(com.android.server.am.ProcessRecord, int, com.android.server.am.ProcessRecord, boolean, long, boolean, boolean, int, boolean):boolean");
    }

    protected int setIntermediateAdjLSP(com.android.server.am.ProcessRecord app, int adj, int prevRawAppAdj, int schedGroup) {
        com.android.server.am.ProcessStateRecord state = app.mState;
        state.setCurRawAdj(adj);
        int adj2 = app.mServices.modifyRawOomAdj(adj);
        if (adj2 > state.getMaxAdj() && (adj2 = state.getMaxAdj()) <= 250) {
            schedGroup = 2;
        }
        state.setCurAdj(adj2);
        return schedGroup;
    }

    protected void setIntermediateProcStateLSP(com.android.server.am.ProcessRecord app, int procState, int prevProcState) {
        com.android.server.am.ProcessStateRecord state = app.mState;
        state.setCurProcState(procState);
        state.setCurRawProcState(procState);
    }

    protected void setIntermediateSchedGroupLSP(com.android.server.am.ProcessStateRecord state, int schedGroup) {
        if (state.getCurProcState() >= 5 && this.mService.mWakefulness.get() != 1 && !state.shouldScheduleLikeTopApp() && schedGroup > 1) {
            schedGroup = 1;
        }
        state.setCurrentSchedulingGroup(schedGroup);
    }

    /* JADX WARN: Removed duplicated region for block: B:135:0x0231 A[PHI: r3
  0x0231: PHI (r3v20 'lbAdj' int) = (r3v19 'lbAdj' int), (r3v19 'lbAdj' int), (r3v27 'lbAdj' int) binds: [B:130:0x0223, B:131:0x0225, B:133:0x022a] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:152:0x025f A[PHI: r3
  0x025f: PHI (r3v22 'lbAdj' int) = (r3v21 'lbAdj' int), (r3v21 'lbAdj' int), (r3v21 'lbAdj' int), (r3v25 'lbAdj' int) binds: [B:145:0x024d, B:147:0x0253, B:148:0x0255, B:150:0x025a] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:158:0x026f A[PHI: r3
  0x026f: PHI (r3v23 'lbAdj' int) = (r3v22 'lbAdj' int), (r3v22 'lbAdj' int), (r3v24 'lbAdj' int) binds: [B:153:0x0265, B:154:0x0267, B:156:0x026a] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:182:0x02c0  */
    /* JADX WARN: Removed duplicated region for block: B:211:0x0320  */
    /* JADX WARN: Removed duplicated region for block: B:230:0x0351  */
    /* JADX WARN: Removed duplicated region for block: B:236:0x0360  */
    /* JADX WARN: Removed duplicated region for block: B:258:0x03f6  */
    /* JADX WARN: Removed duplicated region for block: B:305:0x04cb  */
    /* JADX WARN: Removed duplicated region for block: B:307:0x04cf  */
    /* JADX WARN: Removed duplicated region for block: B:323:0x0503  */
    /* JADX WARN: Removed duplicated region for block: B:325:0x0511 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:326:0x0512  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean computeServiceHostOomAdjLSP(com.android.server.am.ConnectionRecord r33, com.android.server.am.ProcessRecord r34, com.android.server.am.ProcessRecord r35, long r36, com.android.server.am.ProcessRecord r38, boolean r39, boolean r40, boolean r41, int r42, int r43, boolean r44, boolean r45) {
        /*
            Method dump skipped, instruction units count: 1318
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.OomAdjuster.computeServiceHostOomAdjLSP(com.android.server.am.ConnectionRecord, com.android.server.am.ProcessRecord, com.android.server.am.ProcessRecord, long, com.android.server.am.ProcessRecord, boolean, boolean, boolean, int, int, boolean, boolean):boolean");
    }

    public boolean computeProviderHostOomAdjLSP(com.android.server.am.ContentProviderConnection conn, com.android.server.am.ProcessRecord app, com.android.server.am.ProcessRecord client, long now, com.android.server.am.ProcessRecord topApp, boolean doingAll, boolean cycleReEval, boolean computeClients, int oomAdjReason, int cachedAdj, boolean couldRecurse, boolean dryRun) {
        com.android.server.am.ProcessStateRecord cstate;
        com.android.server.am.ProcessStateRecord state;
        com.android.server.am.ProcessStateRecord state2;
        int adj;
        java.lang.String adjType;
        java.lang.String adjType2;
        int prevRawAdj;
        boolean z;
        com.android.server.am.ContentProviderConnection contentProviderConnection;
        int prevSchedGroup;
        int prevProcState;
        int prevRawAdj2;
        boolean z2;
        if (app.isPendingFinishAttach()) {
            return false;
        }
        com.android.server.am.ProcessStateRecord state3 = app.mState;
        com.android.server.am.ProcessStateRecord cstate2 = client.mState;
        if (client == app) {
            return false;
        }
        if (!couldRecurse) {
            cstate = cstate2;
            state = state3;
        } else {
            if (computeClients) {
                state = state3;
                z2 = false;
                computeOomAdjLSP(client, cachedAdj, topApp, doingAll, now, cycleReEval, true, oomAdjReason, true);
                cstate = cstate2;
            } else {
                state = state3;
                z2 = false;
                if (couldRecurse) {
                    cstate = cstate2;
                    cstate.setCurRawAdj(cstate2.getCurAdj());
                    cstate.setCurRawProcState(cstate.getCurProcState());
                } else {
                    cstate = cstate2;
                }
            }
            if (shouldSkipDueToCycle(app, cstate, state.getCurRawProcState(), state.getCurRawAdj(), cycleReEval)) {
                return z2;
            }
        }
        int clientAdj = cstate.getCurRawAdj();
        int clientProcState = cstate.getCurRawProcState();
        int adj2 = state.getCurRawAdj();
        int procState = state.getCurRawProcState();
        int schedGroup = state.getCurrentSchedulingGroup();
        int capability = state.getCurCapability();
        int appUid = app.info.uid;
        int logUid = this.mService.mCurOomAdjUid;
        int capability2 = capability | getBfslCapabilityFromClient(client);
        if (clientProcState >= 16) {
            clientProcState = 19;
        }
        if (client.mOptRecord.shouldNotFreeze() && app.mOptRecord.setShouldNotFreeze(true, dryRun)) {
            return true;
        }
        if (dryRun) {
            state2 = state;
        } else {
            state2 = state;
            state2.setCurBoundByNonBgRestrictedApp(state.isCurBoundByNonBgRestrictedApp() || cstate.isCurBoundByNonBgRestrictedApp() || clientProcState <= 3 || (clientProcState == 4 && !cstate.isBackgroundRestricted()));
        }
        if (adj2 <= clientAdj) {
            adj = adj2;
            adjType = null;
        } else {
            if (state2.hasShownUi() && !state2.getCachedIsHomeProcess()) {
                if (clientAdj > 200) {
                    adj = adj2;
                    adjType = "cch-ui-provider";
                }
                if (state2.isCached() && !cstate.isCached() && dryRun) {
                    return true;
                }
            }
            adj = java.lang.Math.max(clientAdj, 0);
            if (state2.setCurRawAdj(adj, dryRun)) {
                return true;
            }
            adjType = "provider";
            if (state2.isCached()) {
                return true;
            }
        }
        if (clientProcState > 4) {
            adjType2 = adjType;
        } else {
            if (adjType == null) {
                adjType = "provider";
            }
            if (clientProcState == 2) {
                clientProcState = 3;
                adjType2 = adjType;
            } else {
                clientProcState = 5;
                adjType2 = adjType;
            }
        }
        if (!dryRun) {
            prevRawAdj = adj2;
            z = false;
            contentProviderConnection = conn;
            contentProviderConnection.trackProcState(clientProcState, this.mAdjSeq);
        } else {
            prevRawAdj = adj2;
            z = false;
            contentProviderConnection = conn;
        }
        if (procState > clientProcState) {
            procState = clientProcState;
            if (state2.setCurRawProcState(procState, dryRun)) {
                return true;
            }
        }
        if (cstate.getCurrentSchedulingGroup() > schedGroup) {
            schedGroup = 2;
        }
        if (adjType2 != null && !dryRun) {
            state2.setAdjType(adjType2);
            state2.setAdjTypeCode(1);
            state2.setAdjSource(client);
            state2.setAdjSourceProcState(clientProcState);
            state2.setAdjTarget(contentProviderConnection.provider.name);
            if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_OOM_ADJ_REASON || logUid == appUid) {
                reportOomAdjMessageLocked(com.android.server.am.ActivityManagerService.TAG_OOM_ADJ, "Raise to " + adjType2 + ": " + app + ", due to " + client + " adj=" + adj + " procState=" + com.android.server.am.ProcessList.makeProcStateString(procState));
            }
        }
        if (procState > 5) {
            capability2 &= -17;
        }
        if (!dryRun) {
            prevSchedGroup = schedGroup;
            prevProcState = procState;
            prevRawAdj2 = prevRawAdj;
        } else {
            prevRawAdj2 = prevRawAdj;
            if (adj >= prevRawAdj2 && procState >= (prevProcState = procState) && schedGroup <= (prevSchedGroup = schedGroup)) {
                if (capability2 != capability && (capability2 & capability) == capability) {
                    return true;
                }
            }
            return true;
        }
        if (adj < prevRawAdj2) {
            schedGroup = setIntermediateAdjLSP(app, adj, prevRawAdj2, schedGroup);
        }
        if (procState < prevProcState) {
            setIntermediateProcStateLSP(app, procState, prevProcState);
        }
        if (schedGroup > prevSchedGroup) {
            setIntermediateSchedGroupLSP(state2, schedGroup);
        }
        state2.setCurCapability(capability2);
        return z;
    }

    protected int getDefaultCapability(com.android.server.am.ProcessRecord app, int procState) {
        int baseCapabilities;
        int networkCapabilities = android.net.NetworkPolicyManager.getDefaultProcessNetworkCapabilities(procState);
        switch (procState) {
            case 0:
            case 1:
            case 2:
                baseCapabilities = 127;
                break;
            case 3:
                baseCapabilities = 16;
                break;
            case 4:
                if (app.getActiveInstrumentation() != null) {
                    baseCapabilities = 6;
                } else if (!app.mServices.hasForegroundServices() && !android.app.compat.CompatChanges.isChangeEnabled(254662522L, app.info.uid)) {
                    baseCapabilities = 6;
                } else {
                    baseCapabilities = 0;
                }
                break;
            default:
                baseCapabilities = 0;
                break;
        }
        return baseCapabilities | networkCapabilities;
    }

    protected int getBfslCapabilityFromClient(com.android.server.am.ProcessRecord client) {
        if (client.mState.getCurProcState() < 4) {
            return 16;
        }
        return client.mState.getCurCapability() & 16;
    }

    private boolean shouldSkipDueToCycle(com.android.server.am.ProcessRecord app, com.android.server.am.ProcessStateRecord client, int procState, int adj, boolean cycleReEval) {
        if (client.containsCycle()) {
            app.mState.setContainsCycle(true);
            this.mProcessesInCycle.add(app);
            if (client.getCompletedAdjSeq() < this.mAdjSeq && cycleReEval && client.getCurRawProcState() >= procState && client.getCurRawAdj() >= adj && (client.getCurCapability() & app.mState.getCurCapability()) == client.getCurCapability()) {
                return true;
            }
        }
        return false;
    }

    protected void reportOomAdjMessageLocked(java.lang.String tag, java.lang.String msg) {
        android.util.Slog.d(tag, msg);
        synchronized (this.mService.mOomAdjObserverLock) {
            if (this.mService.mCurOomAdjObserver != null) {
                this.mService.mUiHandler.obtainMessage(70, msg).sendToTarget();
            }
        }
    }

    void onWakefulnessChanged(int wakefulness) {
        this.mCachedAppOptimizer.onWakefulnessChanged(wakefulness);
    }

    protected boolean applyOomAdjLSP(com.android.server.am.ProcessRecord app, boolean doingAll, long now, long nowElapsed, int oomAdjReason) {
        return applyOomAdjLSP(app, doingAll, now, nowElapsed, oomAdjReason, false);
    }

    /* JADX WARN: Removed duplicated region for block: B:103:0x025a  */
    /* JADX WARN: Removed duplicated region for block: B:108:0x0292  */
    /* JADX WARN: Removed duplicated region for block: B:109:0x029d  */
    /* JADX WARN: Removed duplicated region for block: B:112:0x02ae  */
    /* JADX WARN: Removed duplicated region for block: B:123:0x02e3  */
    /* JADX WARN: Removed duplicated region for block: B:125:0x02eb  */
    /* JADX WARN: Removed duplicated region for block: B:252:0x0342 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:73:0x01c8 A[Catch: Exception -> 0x0251, TRY_LEAVE, TryCatch #10 {Exception -> 0x0251, blocks: (B:51:0x017e, B:54:0x0188, B:71:0x01c0, B:73:0x01c8, B:64:0x01ae), top: B:258:0x017e }] */
    /* JADX WARN: Removed duplicated region for block: B:80:0x01ed  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected boolean applyOomAdjLSP(final com.android.server.am.ProcessRecord r30, boolean r31, long r32, long r34, int r36, boolean r37) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 1572
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.OomAdjuster.applyOomAdjLSP(com.android.server.am.ProcessRecord, boolean, long, long, int, boolean):boolean");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$applyOomAdjLSP$1(com.android.server.am.ProcessRecord app) {
        com.android.server.am.ActivityManagerService activityManagerService = this.mService;
        com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                this.mService.mServices.stopAllForegroundServicesLocked(app.uid, app.info.packageName);
            } catch (java.lang.Throwable th) {
                com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
    }

    void setAttachingProcessStatesLSP(com.android.server.am.ProcessRecord app) {
        int initialSchedGroup = 2;
        int initialProcState = 19;
        int initialCapability = 0;
        com.android.server.am.ProcessStateRecord state = app.mState;
        int prevProcState = state.getCurProcState();
        int prevAdj = state.getCurRawAdj();
        if (state.hasForegroundActivities()) {
            try {
                app.getWindowProcessController().onTopProcChanged();
                if (app.useFifoUiScheduling()) {
                    com.android.server.am.ActivityManagerService.scheduleAsFifoPriority(app.getPid(), true);
                } else {
                    android.os.Process.setThreadPriority(app.getPid(), -10);
                }
                mOomAdjusterExt.onHookadjustUxProcess(app, app.getRenderThreadTid(), 1, false);
                com.android.server.am.ProcessRecord topApp = this.mService.getTopAppOnlyLocked();
                if (topApp != null) {
                    mOomAdjusterExt.adjustTopApp(topApp.info.packageName, topApp.mPid, topApp.getRenderThreadTid(), topApp.mHwuiTaskThreads, topApp.uid);
                }
                if (isScreenOnOrAnimatingLocked(state)) {
                    initialSchedGroup = 3;
                    initialProcState = 2;
                }
                initialCapability = 127;
            } catch (java.lang.Exception e) {
                android.util.Slog.w(TAG, "Failed to pre-set top priority to " + app + " " + e);
            }
        }
        state.setCurrentSchedulingGroup(initialSchedGroup);
        state.setCurProcState(initialProcState);
        state.setCurRawProcState(initialProcState);
        state.setCurCapability(initialCapability);
        state.setCurAdj(0);
        state.setCurRawAdj(0);
        state.setForcingToImportant(null);
        state.setHasShownUi(false);
        onProcessStateChanged(app, prevProcState);
        onProcessOomAdjChanged(app, prevAdj);
    }

    void maybeUpdateUsageStats(com.android.server.am.ProcessRecord app, long nowElapsed) {
        com.android.server.am.ActivityManagerService activityManagerService = this.mService;
        com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
                com.android.server.am.ActivityManagerService.boostPriorityForProcLockedSection();
                synchronized (activityManagerGlobalLock) {
                    try {
                        maybeUpdateUsageStatsLSP(app, nowElapsed);
                    } catch (java.lang.Throwable th) {
                        com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                        throw th;
                    }
                }
                com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
            } catch (java.lang.Throwable th2) {
                com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                throw th2;
            }
        }
        com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
    }

    private void maybeUpdateUsageStatsLSP(com.android.server.am.ProcessRecord app, long nowElapsed) {
        boolean isInteraction;
        long interactionTime;
        long interactionThreshold;
        com.android.server.am.ProcessStateRecord state = app.mState;
        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_USAGE_STATS) {
            android.util.Slog.d(TAG, "Checking proc [" + java.util.Arrays.toString(app.getPackageList()) + "] state changes: old = " + state.getSetProcState() + ", new = " + state.getCurProcState());
        }
        if (this.mService.mUsageStatsService == null) {
            return;
        }
        boolean fgsInteractionChangeEnabled = state.getCachedCompatChange(2);
        if (android.app.ActivityManager.isProcStateConsideredInteraction(state.getCurProcState())) {
            isInteraction = true;
            state.setFgInteractionTime(0L);
        } else if (state.getCurProcState() > 4) {
            isInteraction = state.getCurProcState() <= 6;
            state.setFgInteractionTime(0L);
        } else if (state.getFgInteractionTime() == 0) {
            state.setFgInteractionTime(nowElapsed);
            isInteraction = false;
        } else {
            if (fgsInteractionChangeEnabled) {
                interactionTime = this.mConstants.SERVICE_USAGE_INTERACTION_TIME_POST_S;
            } else {
                interactionTime = this.mConstants.SERVICE_USAGE_INTERACTION_TIME_PRE_S;
            }
            isInteraction = nowElapsed > state.getFgInteractionTime() + interactionTime;
        }
        if (fgsInteractionChangeEnabled) {
            interactionThreshold = this.mConstants.USAGE_STATS_INTERACTION_INTERVAL_POST_S;
        } else {
            interactionThreshold = this.mConstants.USAGE_STATS_INTERACTION_INTERVAL_PRE_S;
        }
        if (isInteraction && (!state.hasReportedInteraction() || nowElapsed - state.getInteractionEventTime() > interactionThreshold)) {
            state.setInteractionEventTime(nowElapsed);
            java.lang.String[] packages = app.getPackageList();
            if (packages != null) {
                for (java.lang.String str : packages) {
                    this.mService.mUsageStatsService.reportEvent(str, app.userId, 6);
                }
            }
        }
        state.setReportedInteraction(isInteraction);
        if (!isInteraction) {
            state.setInteractionEventTime(0L);
        }
    }

    private void maybeUpdateLastTopTime(com.android.server.am.ProcessStateRecord state, long nowUptime) {
        if (state.getSetProcState() <= 2 && state.getCurProcState() > 2) {
            state.setLastTopTime(nowUptime);
        }
    }

    void idleUidsLocked() {
        int N = this.mActiveUids.size();
        this.mService.mHandler.removeMessages(58);
        if (N <= 0) {
            return;
        }
        long nowElapsed = this.mInjector.getElapsedRealtimeMillis();
        long maxBgTime = nowElapsed - this.mConstants.BACKGROUND_SETTLE_TIME;
        if (this.mService.mLocalPowerManager != null) {
            this.mService.mLocalPowerManager.startUidChanges();
        }
        long nextTime = 0;
        boolean shouldLogMisc = false;
        for (int i = N - 1; i >= 0; i--) {
            com.android.server.am.UidRecord uidRec = this.mActiveUids.valueAt(i);
            long bgTime = uidRec.getLastBackgroundTime();
            long idleTime = uidRec.getLastIdleTimeIfStillIdle();
            if (bgTime > 0 && (!uidRec.isIdle() || idleTime == 0)) {
                if (bgTime <= maxBgTime) {
                    com.android.server.am.EventLogTags.writeAmUidIdle(uidRec.getUid());
                    com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
                    com.android.server.am.ActivityManagerService.boostPriorityForProcLockedSection();
                    synchronized (activityManagerGlobalLock) {
                        try {
                            uidRec.setIdle(true);
                            uidRec.setSetIdle(true);
                            uidRec.setLastIdleTime(nowElapsed);
                        } catch (java.lang.Throwable th) {
                            com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                            throw th;
                        }
                    }
                    com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                    this.mService.doStopUidLocked(uidRec.getUid(), uidRec);
                } else {
                    if (nextTime == 0 || nextTime > bgTime) {
                        nextTime = bgTime;
                    }
                    if (this.mLogger.shouldLog(uidRec.getUid())) {
                        shouldLogMisc = true;
                    }
                }
            }
        }
        if (this.mService.mLocalPowerManager != null) {
            this.mService.mLocalPowerManager.finishUidChanges();
        }
        if (this.mService.mConstants.mKillBgRestrictedAndCachedIdle) {
            android.util.ArraySet<com.android.server.am.ProcessRecord> apps = this.mProcessList.mAppsInBackgroundRestricted;
            int size = apps.size();
            for (int i2 = 0; i2 < size; i2++) {
                long bgTime2 = this.mProcessList.lambda$killAppIfBgRestrictedAndCachedIdleLocked$6(apps.valueAt(i2), nowElapsed) - this.mConstants.BACKGROUND_SETTLE_TIME;
                if (bgTime2 > 0 && (nextTime == 0 || nextTime > bgTime2)) {
                    nextTime = bgTime2;
                }
            }
        }
        if (nextTime > 0) {
            long delay = (this.mConstants.BACKGROUND_SETTLE_TIME + nextTime) - nowElapsed;
            if (shouldLogMisc) {
                this.mLogger.logScheduleUidIdle3(delay);
            }
            this.mService.mHandler.sendEmptyMessageDelayed(58, delay);
        }
    }

    void setUidTempAllowlistStateLSP(int uid, boolean onAllowlist) {
        com.android.server.am.UidRecord uidRec = this.mActiveUids.get(uid);
        if (uidRec != null && uidRec.isCurAllowListed() != onAllowlist) {
            uidRec.setCurAllowListed(onAllowlist);
            if (com.android.server.am.Flags.migrateFullOomadjUpdates()) {
                for (int i = uidRec.getNumOfProcs() - 1; i >= 0; i--) {
                    enqueueOomAdjTargetLocked(uidRec.getProcessRecordByIndex(i));
                }
                updateOomAdjPendingTargetsLocked(10);
                return;
            }
            mOomAdjusterExt.setFullOomAdjUpdateInfo(uid, null, onAllowlist ? "add tmp white" : "rm tmp white");
            updateOomAdjLSP(10);
        }
    }

    void dumpProcessListVariablesLocked(android.util.proto.ProtoOutputStream proto) {
        proto.write(1120986464305L, this.mAdjSeq);
        proto.write(1120986464306L, this.mProcessList.getLruSeqLOSP());
        proto.write(1120986464307L, this.mNumNonCachedProcs);
        proto.write(1120986464309L, this.mNumServiceProcs);
        proto.write(1120986464310L, this.mNewNumServiceProcs);
    }

    void dumpSequenceNumbersLocked(java.io.PrintWriter pw) {
        pw.println("  mAdjSeq=" + this.mAdjSeq + " mLruSeq=" + this.mProcessList.getLruSeqLOSP());
        mOomAdjusterExt.dumpOomAdjStatsLocked(pw);
    }

    void dumpProcCountsLocked(java.io.PrintWriter pw) {
        pw.println("  mNumNonCachedProcs=" + this.mNumNonCachedProcs + " (" + this.mProcessList.getLruSizeLOSP() + " total) mNumCachedHiddenProcs=" + this.mNumCachedHiddenProcs + " mNumServiceProcs=" + this.mNumServiceProcs + " mNewNumServiceProcs=" + this.mNewNumServiceProcs);
    }

    void dumpCachedAppOptimizerSettings(java.io.PrintWriter pw) {
        this.mCachedAppOptimizer.dump(pw);
    }

    void dumpCacheOomRankerSettings(java.io.PrintWriter pw) {
        this.mCacheOomRanker.dump(pw);
    }

    void updateAppFreezeStateLSP(com.android.server.am.ProcessRecord app, int oomAdjReason, boolean immediate) {
        if (!this.mCachedAppOptimizer.useFreezer() || app.mOptRecord.isFreezeExempt()) {
            return;
        }
        com.android.server.am.ProcessCachedOptimizerRecord opt = app.mOptRecord;
        if (opt.isFrozen() && opt.shouldNotFreeze()) {
            this.mCachedAppOptimizer.unfreezeAppLSP(app, com.android.server.am.CachedAppOptimizer.getUnfreezeReasonCodeFromOomAdjReason(oomAdjReason));
            return;
        }
        com.android.server.am.ProcessStateRecord state = app.mState;
        if (state.getCurAdj() >= 900 && !opt.isFrozen() && !opt.shouldNotFreeze()) {
            if (!immediate) {
                this.mCachedAppOptimizer.freezeAppAsyncLSP(app);
                return;
            } else {
                this.mCachedAppOptimizer.freezeAppAsyncAtEarliestLSP(app);
                return;
            }
        }
        if (state.getSetAdj() < 900) {
            this.mCachedAppOptimizer.unfreezeAppLSP(app, com.android.server.am.CachedAppOptimizer.getUnfreezeReasonCodeFromOomAdjReason(oomAdjReason));
        }
    }

    void unfreezeTemporarily(com.android.server.am.ProcessRecord app, int reason) {
        if (!this.mCachedAppOptimizer.useFreezer()) {
            return;
        }
        com.android.server.am.ProcessCachedOptimizerRecord opt = app.mOptRecord;
        if (!opt.isFrozen() && !opt.isPendingFreeze()) {
            return;
        }
        java.util.ArrayList<com.android.server.am.ProcessRecord> processes = this.mTmpProcessList;
        com.android.server.am.ActiveUids uids = this.mTmpUidRecords;
        this.mTmpProcessSet.add(app);
        collectReachableProcessesLocked(this.mTmpProcessSet, processes, uids);
        this.mTmpProcessSet.clear();
        int size = processes.size();
        for (int i = 0; i < size; i++) {
            com.android.server.am.ProcessRecord proc = processes.get(i);
            this.mCachedAppOptimizer.unfreezeTemporarily(proc, reason);
        }
        processes.clear();
    }

    void onProcessEndLocked(com.android.server.am.ProcessRecord app) {
    }

    void onProcessStateChanged(com.android.server.am.ProcessRecord app, int prevProcState) {
    }

    void onProcessOomAdjChanged(com.android.server.am.ProcessRecord app, int prevAdj) {
    }

    void resetInternal() {
    }

    protected int getInitialAdj(com.android.server.am.ProcessRecord app) {
        return app.mState.getCurAdj();
    }

    protected int getInitialProcState(com.android.server.am.ProcessRecord app) {
        return app.mState.getCurProcState();
    }

    protected int getInitialCapability(com.android.server.am.ProcessRecord app) {
        return app.mState.getCurCapability();
    }

    protected boolean getInitialIsCurBoundByNonBgRestrictedApp(com.android.server.am.ProcessRecord app) {
        return app.mState.isCurBoundByNonBgRestrictedApp();
    }

    boolean evaluateServiceConnectionAdd(com.android.server.am.ProcessRecord client, com.android.server.am.ProcessRecord app, com.android.server.am.ConnectionRecord cr) {
        if (evaluateConnectionPrelude(client, app)) {
            return true;
        }
        if (app.getSetAdj() <= client.getSetAdj() && app.getSetProcState() <= client.getSetProcState() && ((app.getSetCapability() & client.getSetCapability()) == client.getSetCapability() || cr.notHasFlag(4294971392L))) {
            return false;
        }
        return computeServiceHostOomAdjLSP(cr, app, client, this.mInjector.getUptimeMillis(), this.mService.getTopApp(), false, false, false, 0, 900, false, true);
    }

    boolean evaluateServiceConnectionRemoval(com.android.server.am.ProcessRecord client, com.android.server.am.ProcessRecord app, com.android.server.am.ConnectionRecord cr) {
        return evaluateConnectionPrelude(client, app) || app.getSetAdj() >= client.getSetAdj() || app.getSetProcState() >= client.getSetProcState() || !((app.getSetCapability() & client.getSetCapability()) == 0 || cr.notHasFlag(4294971392L));
    }

    boolean evaluateProviderConnectionAdd(com.android.server.am.ProcessRecord client, com.android.server.am.ProcessRecord app) {
        if (evaluateConnectionPrelude(client, app)) {
            return true;
        }
        if (app.getSetAdj() <= client.getSetAdj() && app.getSetProcState() <= client.getSetProcState()) {
            return false;
        }
        return computeProviderHostOomAdjLSP(null, app, client, this.mInjector.getUptimeMillis(), this.mService.getTopApp(), false, false, false, 0, 900, false, true);
    }

    boolean evaluateProviderConnectionRemoval(com.android.server.am.ProcessRecord client, com.android.server.am.ProcessRecord app) {
        return evaluateConnectionPrelude(client, app) || app.getSetAdj() >= client.getSetAdj() || app.getSetProcState() >= client.getSetProcState();
    }

    private boolean evaluateConnectionPrelude(com.android.server.am.ProcessRecord client, com.android.server.am.ProcessRecord app) {
        if (client == null || app == null || app.isSdkSandbox || app.isolated || app.isKilledByAm() || app.isKilled()) {
            return true;
        }
        return false;
    }

    private void maybeSetProcessFollowUpUpdateLocked(com.android.server.am.ProcessRecord proc, long updateUptimeMs, long now) {
        if (!com.android.server.am.Flags.followUpOomadjUpdates() || updateUptimeMs <= now) {
            return;
        }
        this.mFollowUpUpdateSet.add(proc);
        proc.mState.setFollowupUpdateUptimeMs(updateUptimeMs);
        scheduleFollowUpOomAdjusterUpdateLocked(updateUptimeMs, now);
    }

    private void scheduleFollowUpOomAdjusterUpdateLocked(long updateUptimeMs, long now) {
        if (this.mConstants.FOLLOW_UP_OOMADJ_UPDATE_WAIT_DURATION + updateUptimeMs >= this.mNextFollowUpUpdateUptimeMs) {
            return;
        }
        if (updateUptimeMs < this.mConstants.FOLLOW_UP_OOMADJ_UPDATE_WAIT_DURATION + now) {
            updateUptimeMs = now + this.mConstants.FOLLOW_UP_OOMADJ_UPDATE_WAIT_DURATION;
        }
        this.mNextFollowUpUpdateUptimeMs = updateUptimeMs;
        this.mService.mHandler.sendEmptyMessageAtTime(86, this.mNextFollowUpUpdateUptimeMs);
    }

    public com.android.server.am.IOomAdjusterWrapper getWrapper() {
        return this.mOomAdjWrapper;
    }

    private class OomAdjusterWrapper implements com.android.server.am.IOomAdjusterWrapper {
        private OomAdjusterWrapper() {
        }

        @Override // com.android.server.am.IOomAdjusterWrapper
        public void setFullOomAdjUpdateInfo(int uid, java.lang.String pkgName, java.lang.String extra) {
            com.android.server.am.OomAdjuster.mOomAdjusterExt.setFullOomAdjUpdateInfo(uid, pkgName, extra);
        }
    }
}
