package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public class ProcessErrorStateRecord {
    private java.lang.String mAnrAnnotation;
    private com.android.server.am.AppNotRespondingDialog.Data mAnrData;
    public final com.android.server.am.ProcessRecord mApp;
    private boolean mBad;
    private java.lang.Runnable mCrashHandler;
    private boolean mCrashing;
    private android.app.ActivityManager.ProcessErrorStateInfo mCrashingReport;
    private boolean mDefered;
    private final com.android.server.am.ErrorDialogController mDialogController;
    private android.content.ComponentName mErrorReportReceiver;
    private boolean mForceCrashReport;
    private boolean mNotResponding;
    private android.app.ActivityManager.ProcessErrorStateInfo mNotRespondingReport;
    private final com.android.server.am.ActivityManagerGlobalLock mProcLock;
    private final com.android.server.am.ActivityManagerService mService;
    public com.android.server.am.IProcessErrorStateRecordExt mProcessErrorStateRecordExt = (com.android.server.am.IProcessErrorStateRecordExt) system.ext.loader.core.ExtLoader.type(com.android.server.am.IProcessErrorStateRecordExt.class).create();
    public com.android.server.am.IProcessErrorStateRecordSocExt mSocExt = (com.android.server.am.IProcessErrorStateRecordSocExt) system.ext.loader.core.ExtLoader.type(com.android.server.am.IProcessErrorStateRecordSocExt.class).base(this).create();
    private android.os.ITheiaManagerExt mTheiaManagerExt = (android.os.ITheiaManagerExt) system.ext.loader.core.ExtLoader.type(android.os.ITheiaManagerExt.class).create();

    boolean isBad() {
        return this.mBad;
    }

    void setBad(boolean bad) {
        this.mBad = bad;
    }

    boolean isCrashing() {
        return this.mCrashing;
    }

    void setCrashing(boolean crashing) {
        this.mCrashing = crashing;
        this.mApp.getWindowProcessController().setCrashing(crashing);
    }

    boolean isForceCrashReport() {
        return this.mForceCrashReport;
    }

    void setForceCrashReport(boolean forceCrashReport) {
        this.mForceCrashReport = forceCrashReport;
    }

    boolean isNotResponding() {
        return this.mNotResponding;
    }

    void setNotResponding(boolean notResponding) {
        this.mNotResponding = notResponding;
        this.mApp.getWindowProcessController().setNotResponding(notResponding);
    }

    boolean isDefered() {
        return this.mDefered;
    }

    void setDefered(boolean defer) {
        this.mDefered = defer;
    }

    java.lang.Runnable getCrashHandler() {
        return this.mCrashHandler;
    }

    void setCrashHandler(java.lang.Runnable crashHandler) {
        this.mCrashHandler = crashHandler;
    }

    android.app.ActivityManager.ProcessErrorStateInfo getCrashingReport() {
        return this.mCrashingReport;
    }

    void setCrashingReport(android.app.ActivityManager.ProcessErrorStateInfo crashingReport) {
        this.mCrashingReport = crashingReport;
    }

    java.lang.String getAnrAnnotation() {
        return this.mAnrAnnotation;
    }

    void setAnrAnnotation(java.lang.String anrAnnotation) {
        this.mAnrAnnotation = anrAnnotation;
    }

    android.app.ActivityManager.ProcessErrorStateInfo getNotRespondingReport() {
        return this.mNotRespondingReport;
    }

    void setNotRespondingReport(android.app.ActivityManager.ProcessErrorStateInfo notRespondingReport) {
        this.mNotRespondingReport = notRespondingReport;
    }

    android.content.ComponentName getErrorReportReceiver() {
        return this.mErrorReportReceiver;
    }

    void setErrorReportReceiver(android.content.ComponentName errorReportReceiver) {
        this.mErrorReportReceiver = errorReportReceiver;
    }

    com.android.server.am.ErrorDialogController getDialogController() {
        return this.mDialogController;
    }

    void setAnrData(com.android.server.am.AppNotRespondingDialog.Data data) {
        this.mAnrData = data;
    }

    com.android.server.am.AppNotRespondingDialog.Data getAnrData() {
        return this.mAnrData;
    }

    ProcessErrorStateRecord(com.android.server.am.ProcessRecord app) {
        this.mApp = app;
        this.mService = app.mService;
        this.mProcLock = this.mService.mProcLock;
        this.mDialogController = new com.android.server.am.ErrorDialogController(app);
    }

    void appNotResponding(java.lang.String activityShortComponentName, android.content.pm.ApplicationInfo aInfo, java.lang.String parentShortComponentName, com.android.server.wm.WindowProcessController parentProcess, boolean aboveSystem, com.android.internal.os.TimeoutRecord timeoutRecord, java.util.concurrent.ExecutorService auxiliaryTaskExecutor, boolean onlyDumpSelf, boolean isContinuousAnr, java.util.concurrent.Future<java.io.File> firstPidFilePromise) throws java.lang.Throwable {
        appNotResponding(activityShortComponentName, aInfo, parentShortComponentName, parentProcess, aboveSystem, timeoutRecord, auxiliaryTaskExecutor, onlyDumpSelf, isContinuousAnr, firstPidFilePromise, java.util.UUID.randomUUID().toString());
    }

    boolean skipAnrLocked(java.lang.String annotation) {
        if (this.mService.mAtmInternal.isShuttingDown()) {
            android.util.Slog.i(com.android.server.am.IActivityManagerServiceExt.TAG, "During shutdown skipping ANR: " + this + " " + annotation);
            return true;
        }
        if (isNotResponding()) {
            android.util.Slog.i(com.android.server.am.IActivityManagerServiceExt.TAG, "Skipping duplicate ANR: " + this + " " + annotation);
            return true;
        }
        if (isCrashing()) {
            android.util.Slog.i(com.android.server.am.IActivityManagerServiceExt.TAG, "Crashing app skipping ANR: " + this + " " + annotation);
            return true;
        }
        if (this.mApp.isKilledByAm()) {
            android.util.Slog.i(com.android.server.am.IActivityManagerServiceExt.TAG, "App already killed by AM skipping ANR: " + this + " " + annotation);
            return true;
        }
        if (this.mApp.isKilled()) {
            android.util.Slog.i(com.android.server.am.IActivityManagerServiceExt.TAG, "Skipping died app ANR: " + this + " " + annotation);
            return true;
        }
        if (this.mApp.getPid() == 0) {
            android.util.Slog.i(com.android.server.am.IActivityManagerServiceExt.TAG, "Skipping restarting app ANR: " + this + " " + annotation);
            return true;
        }
        return false;
    }

    /* JADX WARN: Finally extract failed */
    /* JADX WARN: Removed duplicated region for block: B:111:0x02e1  */
    /* JADX WARN: Removed duplicated region for block: B:126:0x03f1  */
    /* JADX WARN: Removed duplicated region for block: B:128:0x03f5  */
    /* JADX WARN: Removed duplicated region for block: B:129:0x03fa  */
    /* JADX WARN: Removed duplicated region for block: B:132:0x0434 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:133:0x0435  */
    /* JADX WARN: Removed duplicated region for block: B:296:0x0892  */
    /* JADX WARN: Removed duplicated region for block: B:414:0x0ab6  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x0172  */
    /* JADX WARN: Removed duplicated region for block: B:584:0x08ac A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:68:0x0204 A[Catch: all -> 0x01dd, TRY_ENTER, TRY_LEAVE, TryCatch #30 {all -> 0x01dd, blocks: (B:56:0x01b1, B:58:0x01bb, B:68:0x0204, B:74:0x021b, B:76:0x0221, B:78:0x0228, B:79:0x022f, B:81:0x0233, B:83:0x0237), top: B:556:0x01b1 }] */
    /* JADX WARN: Removed duplicated region for block: B:85:0x0244  */
    /* JADX WARN: Removed duplicated region for block: B:86:0x0246  */
    /* JADX WARN: Removed duplicated region for block: B:87:0x024a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    void appNotResponding(java.lang.String r72, android.content.pm.ApplicationInfo r73, java.lang.String r74, com.android.server.wm.WindowProcessController r75, boolean r76, com.android.internal.os.TimeoutRecord r77, java.util.concurrent.ExecutorService r78, boolean r79, boolean r80, java.util.concurrent.Future<java.io.File> r81, java.lang.String r82) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 3188
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.ProcessErrorStateRecord.appNotResponding(java.lang.String, android.content.pm.ApplicationInfo, java.lang.String, com.android.server.wm.WindowProcessController, boolean, com.android.internal.os.TimeoutRecord, java.util.concurrent.ExecutorService, boolean, boolean, java.util.concurrent.Future, java.lang.String):void");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$appNotResponding$0(com.android.internal.os.anr.AnrLatencyTracker latencyTracker, java.lang.String annotation) {
        latencyTracker.waitingOnAMSLockStarted();
        com.android.server.am.ActivityManagerService activityManagerService = this.mService;
        com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                latencyTracker.waitingOnAMSLockEnded();
                setAnrAnnotation(annotation);
                this.mApp.killLocked("anr", 6, true);
            } catch (java.lang.Throwable th) {
                com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$appNotResponding$1(com.android.internal.os.anr.AnrLatencyTracker latencyTracker) {
        latencyTracker.updateCpuStatsNowCalled();
        this.mService.updateCpuStatsNow();
        latencyTracker.updateCpuStatsNowReturned();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$appNotResponding$2(int pid, int ppid, java.util.ArrayList firstPids, android.util.SparseBooleanArray lastPids, com.android.server.am.ProcessRecord r) {
        if (r != null && r.getThread() != null) {
            int myPid = r.getPid();
            if (android.os.Process.getUidForPid(r.getPid()) != r.uid) {
                android.util.Slog.w(com.android.server.am.IActivityManagerServiceExt.TAG, "Process " + r.getPid() + " does not match uid " + r.uid + ", skip dump its java trace");
                return;
            }
            if (myPid > 0 && myPid != pid && myPid != ppid && myPid != com.android.server.am.ActivityManagerService.MY_PID) {
                boolean isInterestProc = this.mProcessErrorStateRecordExt.hookReturnIsInterestProc(r);
                this.mProcessErrorStateRecordExt.hookAddFirstPids(r.processName, firstPids, myPid);
                if (r.isPersistent()) {
                    if (this.mProcessErrorStateRecordExt.hookAddPersistentProc(isInterestProc)) {
                        firstPids.add(java.lang.Integer.valueOf(myPid));
                        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_ANR) {
                            android.util.Slog.i(com.android.server.am.IActivityManagerServiceExt.TAG, "Adding persistent proc: " + r);
                            return;
                        }
                        return;
                    }
                    return;
                }
                if (r.mServices.isTreatedLikeActivity()) {
                    if (this.mProcessErrorStateRecordExt.hookAddLikelyIME()) {
                        firstPids.add(java.lang.Integer.valueOf(myPid));
                        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_ANR) {
                            android.util.Slog.i(com.android.server.am.IActivityManagerServiceExt.TAG, "Adding likely IME: " + r);
                            return;
                        }
                        return;
                    }
                    return;
                }
                if (this.mProcessErrorStateRecordExt.hookAddANRProc(isInterestProc)) {
                    lastPids.put(myPid, true);
                    if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_ANR) {
                        android.util.Slog.i(com.android.server.am.IActivityManagerServiceExt.TAG, "Adding ANR proc: " + r);
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.util.ArrayList lambda$appNotResponding$3(com.android.internal.os.anr.AnrLatencyTracker latencyTracker, boolean isSilentAnr, boolean finalOnlyDumpSelf, boolean smTraceEnabled) throws java.lang.Exception {
        latencyTracker.nativePidCollectionStarted();
        java.util.ArrayList<java.lang.Integer> nativePids = null;
        java.lang.String[] nativeProcs = null;
        boolean isSystemApp = this.mApp.info.isSystemApp() || this.mApp.info.isSystemExt();
        if (!isSystemApp || isSilentAnr || finalOnlyDumpSelf) {
            int i = 0;
            while (true) {
                if (i >= com.android.server.Watchdog.NATIVE_STACKS_OF_INTEREST.length) {
                    break;
                }
                if (!com.android.server.Watchdog.NATIVE_STACKS_OF_INTEREST[i].equals(this.mApp.processName)) {
                    i++;
                } else {
                    nativeProcs = new java.lang.String[]{this.mApp.processName};
                    break;
                }
            }
            int[] pids = nativeProcs == null ? null : android.os.Process.getPidsForCommands(nativeProcs);
            if (pids != null) {
                nativePids = new java.util.ArrayList<>(pids.length);
                for (int i2 : pids) {
                    nativePids.add(java.lang.Integer.valueOf(i2));
                }
            }
        } else if (!smTraceEnabled || com.android.server.am.trace.SmartTraceUtils.isDumpPredefinedPidsEnabled()) {
            com.android.server.Watchdog.getInstance();
            nativePids = com.android.server.Watchdog.getInterestingNativePids();
        }
        latencyTracker.nativePidCollectionEnded();
        return nativePids;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$appNotResponding$4() {
        com.android.server.am.ActivityManagerService activityManagerService = this.mService;
        com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                this.mApp.killLocked("anr", 6, true);
            } catch (java.lang.Throwable th) {
                com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$appNotResponding$5() {
        com.android.server.am.ActivityManagerService activityManagerService = this.mService;
        com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                this.mService.mServices.scheduleServiceTimeoutLocked(this.mApp);
            } catch (java.lang.Throwable th) {
                com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
    }

    private void makeAppNotRespondingLSP(java.lang.String activity, java.lang.String shortMsg, java.lang.String longMsg) {
        setNotResponding(true);
        if (this.mService.mAppErrors != null) {
            this.mNotRespondingReport = this.mService.mAppErrors.generateProcessError(this.mApp, 2, activity, shortMsg, longMsg, null);
        }
        startAppProblemLSP();
        this.mApp.getWindowProcessController().stopFreezingActivities();
    }

    private boolean isSmartTraceEnabled(boolean isSilentAnr) {
        return com.android.server.am.trace.SmartTraceUtils.isSmartTraceEnabled() && (!isSilentAnr || (isSilentAnr && com.android.server.am.trace.SmartTraceUtils.isSmartTraceEnabledOnBgApp()));
    }

    private boolean isPerfettoDumpEnabled(boolean isSilentAnr) {
        return com.android.server.am.trace.SmartTraceUtils.isPerfettoDumpEnabled() && (!isSilentAnr || (isSilentAnr && com.android.server.am.trace.SmartTraceUtils.isPerfettoDumpEnabledOnBgApp()));
    }

    private boolean shouldDeferAppNotResponding(boolean isSilentAnr) {
        return isSmartTraceEnabled(isSilentAnr) || isPerfettoDumpEnabled(isSilentAnr);
    }

    private java.lang.String getVersionName(android.content.Context context, java.lang.String pkgName, int userId) {
        try {
            return context.getPackageManager().getPackageInfoAsUser(pkgName, 0, userId).versionName;
        } catch (java.lang.Exception e) {
            android.util.Slog.e(com.android.server.am.IActivityManagerServiceExt.TAG, "Failed to get versionName: " + e.getMessage());
            return null;
        }
    }

    void startAppProblemLSP() {
        this.mErrorReportReceiver = null;
        for (int userId : this.mService.mUserController.getCurrentProfileIds()) {
            if (this.mApp.userId == userId) {
                this.mErrorReportReceiver = android.app.ApplicationErrorReport.getErrorReportReceiver(this.mService.mContext, this.mApp.info.packageName, this.mApp.info.flags);
            }
        }
        this.mService.getBroadcastQueue().onApplicationProblemLocked(this.mApp);
    }

    private boolean isInterestingForBackgroundTraces() {
        if (this.mApp.getPid() == com.android.server.am.ActivityManagerService.MY_PID || this.mApp.isInterestingToUserLocked()) {
            return true;
        }
        return (this.mApp.info != null && "com.android.systemui".equals(this.mApp.info.packageName)) || this.mApp.mState.hasTopUi() || this.mApp.mState.hasOverlayUi();
    }

    private boolean getShowBackground() {
        android.content.ContentResolver resolver = this.mService.mContext.getContentResolver();
        return android.provider.Settings.Secure.getIntForUser(resolver, "anr_show_background", 0, resolver.getUserId()) != 0;
    }

    private java.lang.String buildMemoryHeadersFor(int pid) {
        if (pid <= 0) {
            android.util.Slog.i(com.android.server.am.IActivityManagerServiceExt.TAG, "Memory header requested with invalid pid: " + pid);
            return null;
        }
        com.android.server.stats.pull.ProcfsMemoryUtil.MemorySnapshot snapshot = com.android.server.stats.pull.ProcfsMemoryUtil.readMemorySnapshotFromProcfs(pid);
        if (snapshot == null) {
            android.util.Slog.i(com.android.server.am.IActivityManagerServiceExt.TAG, "Failed to get memory snapshot for pid:" + pid);
            return null;
        }
        java.lang.StringBuilder memoryHeaders = new java.lang.StringBuilder();
        memoryHeaders.append("RssHwmKb: ").append(snapshot.rssHighWaterMarkInKilobytes).append("\n");
        memoryHeaders.append("RssKb: ").append(snapshot.rssInKilobytes).append("\n");
        memoryHeaders.append("RssAnonKb: ").append(snapshot.anonRssInKilobytes).append("\n");
        memoryHeaders.append("RssShmemKb: ").append(snapshot.rssShmemKilobytes).append("\n");
        memoryHeaders.append("VmSwapKb: ").append(snapshot.swapInKilobytes).append("\n");
        return memoryHeaders.toString();
    }

    boolean isSilentAnr() {
        return (getShowBackground() || isInterestingForBackgroundTraces()) ? false : true;
    }

    boolean isMonitorCpuUsage() {
        com.android.server.am.AppProfiler appProfiler = this.mService.mAppProfiler;
        return true;
    }

    void onCleanupApplicationRecordLSP() {
        this.mProcessErrorStateRecordExt.notifyTheiaAnrFinished(this.mApp.mPid, this.mApp.uid, this.mApp.processName, "end");
        getDialogController().clearAllErrorDialogs();
        setCrashing(false);
        setNotResponding(false);
    }

    void dump(java.io.PrintWriter pw, java.lang.String prefix, long nowUptime) {
        com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
        com.android.server.am.ActivityManagerService.boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            try {
                if (this.mCrashing || this.mDialogController.hasCrashDialogs() || this.mNotResponding || this.mDialogController.hasAnrDialogs() || this.mBad) {
                    pw.print(prefix);
                    pw.print(" mCrashing=" + this.mCrashing);
                    pw.print(" " + this.mDialogController.getCrashDialogs());
                    pw.print(" mNotResponding=" + this.mNotResponding);
                    pw.print(" " + this.mDialogController.getAnrDialogs());
                    pw.print(" bad=" + this.mBad);
                    if (this.mErrorReportReceiver != null) {
                        pw.print(" errorReportReceiver=");
                        pw.print(this.mErrorReportReceiver.flattenToShortString());
                    }
                    pw.println();
                }
            } catch (java.lang.Throwable th) {
                com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                throw th;
            }
        }
        com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
    }
}
