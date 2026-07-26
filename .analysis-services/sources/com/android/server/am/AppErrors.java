package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
class AppErrors {
    private static final java.lang.String TAG = "ActivityManager";
    private static final java.lang.String UPLOAD_VALUE_TOW_MINUTE_LIMIT = "TwoMinuteLimit";
    private android.util.ArraySet<java.lang.String> mAppsNotReportingCrashes;
    private final android.content.Context mContext;
    private final com.android.server.PackageWatchdog mPackageWatchdog;
    private final com.android.server.am.ActivityManagerGlobalLock mProcLock;
    private final com.android.server.am.ActivityManagerService mService;
    private final com.android.internal.app.ProcessMap<java.lang.Long> mProcessCrashTimes = new com.android.internal.app.ProcessMap<>();
    private final com.android.internal.app.ProcessMap<java.lang.Long> mProcessCrashTimesPersistent = new com.android.internal.app.ProcessMap<>();
    private final com.android.internal.app.ProcessMap<java.lang.Long> mProcessCrashShowDialogTimes = new com.android.internal.app.ProcessMap<>();
    private final com.android.internal.app.ProcessMap<android.util.Pair<java.lang.Long, java.lang.Integer>> mProcessCrashCounts = new com.android.internal.app.ProcessMap<>();
    private volatile com.android.internal.app.ProcessMap<com.android.server.am.AppErrors.BadProcessInfo> mBadProcesses = new com.android.internal.app.ProcessMap<>();
    private final java.lang.Object mBadProcessLock = new java.lang.Object();
    public com.android.server.am.IAppErrorsExt mAppErrorsExt = (com.android.server.am.IAppErrorsExt) system.ext.loader.core.ExtLoader.type(com.android.server.am.IAppErrorsExt.class).create();

    AppErrors(android.content.Context context, com.android.server.am.ActivityManagerService service, com.android.server.PackageWatchdog watchdog) {
        context.assertRuntimeOverlayThemable();
        this.mService = service;
        this.mProcLock = service.mProcLock;
        this.mContext = context;
        this.mPackageWatchdog = watchdog;
    }

    public void resetState() {
        android.util.Slog.i("ActivityManager", "Resetting AppErrors");
        synchronized (this.mBadProcessLock) {
            this.mAppsNotReportingCrashes.clear();
            this.mProcessCrashTimes.clear();
            this.mProcessCrashTimesPersistent.clear();
            this.mProcessCrashShowDialogTimes.clear();
            this.mProcessCrashCounts.clear();
            this.mBadProcesses = new com.android.internal.app.ProcessMap<>();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:41:0x01a6 A[Catch: all -> 0x01fb, TryCatch #2 {all -> 0x01fb, blocks: (B:30:0x0140, B:32:0x014d, B:34:0x0172, B:37:0x0186, B:42:0x01d6, B:41:0x01a6, B:43:0x01e5), top: B:61:0x0140 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    void dumpDebugLPr(android.util.proto.ProtoOutputStream r29, long r30, java.lang.String r32) {
        /*
            Method dump skipped, instruction units count: 525
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.AppErrors.dumpDebugLPr(android.util.proto.ProtoOutputStream, long, java.lang.String):void");
    }

    boolean dumpLPr(java.io.FileDescriptor fd, java.io.PrintWriter pw, boolean needSep, java.lang.String dumpPackage) {
        boolean needSep2;
        long now;
        int processCount;
        boolean needSep3;
        int processCount2;
        com.android.server.am.AppErrors appErrors = this;
        java.lang.String str = dumpPackage;
        long now2 = android.os.SystemClock.uptimeMillis();
        synchronized (appErrors.mBadProcessLock) {
            try {
                if (appErrors.mProcessCrashTimes.getMap().isEmpty()) {
                    needSep2 = needSep;
                } else {
                    boolean printed = false;
                    try {
                        android.util.ArrayMap<java.lang.String, android.util.SparseArray<java.lang.Long>> pmap = appErrors.mProcessCrashTimes.getMap();
                        int processCount3 = pmap.size();
                        needSep2 = needSep;
                        for (int ip = 0; ip < processCount3; ip++) {
                            try {
                                java.lang.String pname = pmap.keyAt(ip);
                                android.util.SparseArray<java.lang.Long> uids = pmap.valueAt(ip);
                                int uidCount = uids.size();
                                int i = 0;
                                while (i < uidCount) {
                                    int puid = uids.keyAt(i);
                                    android.util.ArrayMap<java.lang.String, android.util.SparseArray<java.lang.Long>> pmap2 = pmap;
                                    com.android.server.am.ProcessRecord r = (com.android.server.am.ProcessRecord) appErrors.mService.getProcessNamesLOSP().get(pname, puid);
                                    try {
                                        if (str != null) {
                                            if (r != null) {
                                                processCount2 = processCount3;
                                                if (!r.getPkgList().containsKey(str)) {
                                                }
                                            } else {
                                                processCount2 = processCount3;
                                            }
                                            i++;
                                            pmap = pmap2;
                                            processCount3 = processCount2;
                                        } else {
                                            processCount2 = processCount3;
                                        }
                                        pw.print("    Process ");
                                        pw.print(pname);
                                        pw.print(" uid ");
                                        pw.print(puid);
                                        pw.print(": last crashed ");
                                        needSep2 = needSep3;
                                        android.util.TimeUtils.formatDuration(now2 - uids.valueAt(i).longValue(), pw);
                                        pw.println(" ago");
                                        i++;
                                        pmap = pmap2;
                                        processCount3 = processCount2;
                                    } catch (java.lang.Throwable th) {
                                        th = th;
                                        while (true) {
                                            try {
                                                throw th;
                                            } catch (java.lang.Throwable th2) {
                                                th = th2;
                                            }
                                        }
                                    }
                                    if (printed) {
                                        needSep3 = needSep2;
                                    } else {
                                        if (needSep2) {
                                            pw.println();
                                        }
                                        needSep3 = true;
                                        try {
                                            pw.println("  Time since processes crashed:");
                                            printed = true;
                                        } catch (java.lang.Throwable th3) {
                                            th = th3;
                                            while (true) {
                                                throw th;
                                            }
                                        }
                                    }
                                }
                            } catch (java.lang.Throwable th4) {
                                th = th4;
                                while (true) {
                                    throw th;
                                }
                            }
                        }
                    } catch (java.lang.Throwable th5) {
                        th = th5;
                        while (true) {
                            throw th;
                        }
                    }
                }
                try {
                    if (!appErrors.mProcessCrashCounts.getMap().isEmpty()) {
                        boolean printed2 = false;
                        android.util.ArrayMap<java.lang.String, android.util.SparseArray<android.util.Pair<java.lang.Long, java.lang.Integer>>> pmap3 = appErrors.mProcessCrashCounts.getMap();
                        int processCount4 = pmap3.size();
                        for (int ip2 = 0; ip2 < processCount4; ip2++) {
                            java.lang.String pname2 = pmap3.keyAt(ip2);
                            android.util.SparseArray<android.util.Pair<java.lang.Long, java.lang.Integer>> uids2 = pmap3.valueAt(ip2);
                            int uidCount2 = uids2.size();
                            int i2 = 0;
                            while (i2 < uidCount2) {
                                int puid2 = uids2.keyAt(i2);
                                android.util.ArrayMap<java.lang.String, android.util.SparseArray<android.util.Pair<java.lang.Long, java.lang.Integer>>> pmap4 = pmap3;
                                com.android.server.am.ProcessRecord r2 = (com.android.server.am.ProcessRecord) appErrors.mService.getProcessNamesLOSP().get(pname2, puid2);
                                if (str != null) {
                                    if (r2 != null) {
                                        processCount = processCount4;
                                        if (!r2.getPkgList().containsKey(str)) {
                                        }
                                    } else {
                                        processCount = processCount4;
                                    }
                                    i2++;
                                    pmap3 = pmap4;
                                    processCount4 = processCount;
                                } else {
                                    processCount = processCount4;
                                }
                                if (printed2) {
                                    needSep3 = needSep2;
                                } else {
                                    if (needSep2) {
                                        pw.println();
                                    }
                                    needSep3 = true;
                                    pw.println("  First time processes crashed and counts:");
                                    printed2 = true;
                                }
                                pw.print("    Process ");
                                pw.print(pname2);
                                pw.print(" uid ");
                                pw.print(puid2);
                                pw.print(": first crashed ");
                                needSep2 = needSep3;
                                android.util.TimeUtils.formatDuration(now2 - ((java.lang.Long) uids2.valueAt(i2).first).longValue(), pw);
                                pw.print(" ago; crashes since then: ");
                                pw.println(uids2.valueAt(i2).second);
                                i2++;
                                pmap3 = pmap4;
                                processCount4 = processCount;
                            }
                        }
                    }
                    com.android.internal.app.ProcessMap<com.android.server.am.AppErrors.BadProcessInfo> badProcesses = appErrors.mBadProcesses;
                    if (!badProcesses.getMap().isEmpty()) {
                        boolean printed3 = false;
                        android.util.ArrayMap<java.lang.String, android.util.SparseArray<com.android.server.am.AppErrors.BadProcessInfo>> pmap5 = badProcesses.getMap();
                        int processCount5 = pmap5.size();
                        int ip3 = 0;
                        while (ip3 < processCount5) {
                            java.lang.String pname3 = pmap5.keyAt(ip3);
                            android.util.SparseArray<com.android.server.am.AppErrors.BadProcessInfo> uids3 = pmap5.valueAt(ip3);
                            int uidCount3 = uids3.size();
                            int i3 = 0;
                            while (i3 < uidCount3) {
                                int puid3 = uids3.keyAt(i3);
                                com.android.internal.app.ProcessMap<com.android.server.am.AppErrors.BadProcessInfo> badProcesses2 = badProcesses;
                                com.android.server.am.ProcessRecord r3 = (com.android.server.am.ProcessRecord) appErrors.mService.getProcessNamesLOSP().get(pname3, puid3);
                                if (str == null || (r3 != null && r3.getPkgList().containsKey(str))) {
                                    if (!printed3) {
                                        if (needSep2) {
                                            pw.println();
                                        }
                                        needSep2 = true;
                                        pw.println("  Bad processes:");
                                        printed3 = true;
                                    }
                                    com.android.server.am.AppErrors.BadProcessInfo info = uids3.valueAt(i3);
                                    pw.print("    Bad process ");
                                    pw.print(pname3);
                                    pw.print(" uid ");
                                    pw.print(puid3);
                                    pw.print(": crashed at time ");
                                    now = now2;
                                    pw.println(info.time);
                                    if (info.shortMsg != null) {
                                        pw.print("      Short msg: ");
                                        pw.println(info.shortMsg);
                                    }
                                    if (info.longMsg != null) {
                                        pw.print("      Long msg: ");
                                        pw.println(info.longMsg);
                                    }
                                    if (info.stack != null) {
                                        pw.println("      Stack:");
                                        int lastPos = 0;
                                        for (int pos = 0; pos < info.stack.length(); pos++) {
                                            if (info.stack.charAt(pos) == '\n') {
                                                pw.print("        ");
                                                pw.write(info.stack, lastPos, pos - lastPos);
                                                pw.println();
                                                lastPos = pos + 1;
                                            }
                                        }
                                        if (lastPos < info.stack.length()) {
                                            pw.print("        ");
                                            pw.write(info.stack, lastPos, info.stack.length() - lastPos);
                                            pw.println();
                                        }
                                    }
                                } else {
                                    now = now2;
                                }
                                i3++;
                                appErrors = this;
                                str = dumpPackage;
                                badProcesses = badProcesses2;
                                now2 = now;
                            }
                            ip3++;
                            appErrors = this;
                            str = dumpPackage;
                        }
                    }
                    return needSep2;
                } catch (java.lang.Throwable th6) {
                    th = th6;
                    while (true) {
                        throw th;
                    }
                }
            } catch (java.lang.Throwable th7) {
                th = th7;
            }
        }
    }

    boolean isBadProcess(java.lang.String processName, int uid) {
        return this.mBadProcesses.get(processName, uid) != null;
    }

    void clearBadProcess(java.lang.String processName, int uid) {
        synchronized (this.mBadProcessLock) {
            com.android.internal.app.ProcessMap<com.android.server.am.AppErrors.BadProcessInfo> badProcesses = new com.android.internal.app.ProcessMap<>();
            badProcesses.putAll(this.mBadProcesses);
            badProcesses.remove(processName, uid);
            this.mBadProcesses = badProcesses;
        }
    }

    void markBadProcess(java.lang.String processName, int uid, com.android.server.am.AppErrors.BadProcessInfo info) {
        synchronized (this.mBadProcessLock) {
            com.android.internal.app.ProcessMap<com.android.server.am.AppErrors.BadProcessInfo> badProcesses = new com.android.internal.app.ProcessMap<>();
            badProcesses.putAll(this.mBadProcesses);
            badProcesses.put(processName, uid, info);
            this.mBadProcesses = badProcesses;
        }
    }

    void resetProcessCrashTime(java.lang.String processName, int uid) {
        synchronized (this.mBadProcessLock) {
            this.mProcessCrashTimes.remove(processName, uid);
            this.mProcessCrashCounts.remove(processName, uid);
        }
    }

    void resetProcessCrashTime(boolean resetEntireUser, int appId, int userId) {
        synchronized (this.mBadProcessLock) {
            android.util.ArrayMap<java.lang.String, android.util.SparseArray<java.lang.Long>> pTimeMap = this.mProcessCrashTimes.getMap();
            for (int ip = pTimeMap.size() - 1; ip >= 0; ip--) {
                android.util.SparseArray<java.lang.Long> ba = pTimeMap.valueAt(ip);
                resetProcessCrashMapLBp(ba, resetEntireUser, appId, userId);
                if (ba.size() == 0) {
                    pTimeMap.removeAt(ip);
                }
            }
            android.util.ArrayMap<java.lang.String, android.util.SparseArray<android.util.Pair<java.lang.Long, java.lang.Integer>>> pCountMap = this.mProcessCrashCounts.getMap();
            for (int ip2 = pCountMap.size() - 1; ip2 >= 0; ip2--) {
                android.util.SparseArray<android.util.Pair<java.lang.Long, java.lang.Integer>> ba2 = pCountMap.valueAt(ip2);
                resetProcessCrashMapLBp(ba2, resetEntireUser, appId, userId);
                if (ba2.size() == 0) {
                    pCountMap.removeAt(ip2);
                }
            }
        }
    }

    private void resetProcessCrashMapLBp(android.util.SparseArray<?> ba, boolean resetEntireUser, int appId, int userId) {
        for (int i = ba.size() - 1; i >= 0; i--) {
            boolean remove = false;
            int entUid = ba.keyAt(i);
            if (!resetEntireUser) {
                if (userId == -1) {
                    if (android.os.UserHandle.getAppId(entUid) == appId) {
                        remove = true;
                    }
                } else if (entUid == android.os.UserHandle.getUid(userId, appId)) {
                    remove = true;
                }
            } else if (android.os.UserHandle.getUserId(entUid) == userId) {
                remove = true;
            }
            if (remove) {
                ba.removeAt(i);
            }
        }
    }

    void loadAppsNotReportingCrashesFromConfig(java.lang.String appsNotReportingCrashesConfig) {
        if (appsNotReportingCrashesConfig != null) {
            java.lang.String[] split = appsNotReportingCrashesConfig.split(",");
            if (split.length > 0) {
                synchronized (this.mBadProcessLock) {
                    this.mAppsNotReportingCrashes = new android.util.ArraySet<>();
                    java.util.Collections.addAll(this.mAppsNotReportingCrashes, split);
                }
            }
        }
    }

    void killAppAtUserRequestLocked(com.android.server.am.ProcessRecord app) {
        com.android.server.am.ErrorDialogController controller = app.mErrorState.getDialogController();
        int reasonCode = 6;
        int subReason = 0;
        com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
        com.android.server.am.ActivityManagerService.boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            try {
                if (controller.hasDebugWaitingDialog()) {
                    reasonCode = 13;
                    subReason = 1;
                }
                controller.clearAllErrorDialogs();
                java.lang.String str = this.mAppErrorsExt.handleAnrAnnotation(app);
                killAppImmediateLSP(app, reasonCode, subReason, "user-terminated", "user request after error" + str);
            } catch (java.lang.Throwable th) {
                com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                throw th;
            }
        }
        com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
    }

    private void killAppImmediateLSP(com.android.server.am.ProcessRecord app, int reasonCode, int subReason, java.lang.String reason, java.lang.String killReason) {
        com.android.server.am.ProcessErrorStateRecord errState = app.mErrorState;
        errState.setCrashing(false);
        errState.setCrashingReport(null);
        errState.setNotResponding(false);
        errState.setNotRespondingReport(null);
        int pid = errState.mApp.getPid();
        if (pid > 0 && pid != com.android.server.am.ActivityManagerService.MY_PID) {
            synchronized (this.mBadProcessLock) {
                handleAppCrashLSPB(app, reason, null, null, null, null);
            }
            app.killLocked(killReason, reasonCode, subReason, true);
        }
    }

    void scheduleAppCrashLocked(int uid, int initialPid, java.lang.String packageName, int userId, java.lang.String message, boolean force, int exceptionTypeId, android.os.Bundle extras) {
        com.android.server.am.ProcessRecord proc = null;
        synchronized (this.mService.mPidsSelfLocked) {
            int i = 0;
            while (true) {
                if (i >= this.mService.mPidsSelfLocked.size()) {
                    break;
                }
                com.android.server.am.ProcessRecord p = this.mService.mPidsSelfLocked.valueAt(i);
                if (uid < 0 || p.uid == uid) {
                    if (p.getPid() == initialPid) {
                        proc = p;
                        break;
                    } else if (p.getPkgList().containsKey(packageName) && (userId < 0 || p.userId == userId)) {
                        proc = p;
                    }
                }
                i++;
            }
        }
        if (proc == null) {
            android.util.Slog.w("ActivityManager", "crashApplication: nothing for uid=" + uid + " initialPid=" + initialPid + " packageName=" + packageName + " userId=" + userId);
            return;
        }
        if (exceptionTypeId == 5) {
            java.lang.String[] packages = proc.getPackageList();
            for (int i2 = 0; i2 < packages.length; i2++) {
                if (this.mService.mPackageManagerInt.isPackageStateProtected(packages[i2], proc.userId)) {
                    android.util.Slog.w("ActivityManager", "crashApplication: Can not crash protected package " + packages[i2]);
                    return;
                }
            }
        }
        this.mService.mOomAdjuster.mCachedAppOptimizer.unfreezeProcess(initialPid, 12);
        proc.scheduleCrashLocked(message, exceptionTypeId, extras);
        if (force) {
            final com.android.server.am.ProcessRecord p2 = proc;
            this.mService.mHandler.postDelayed(new java.lang.Runnable() { // from class: com.android.server.am.AppErrors$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$scheduleAppCrashLocked$0(p2);
                }
            }, 5000L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleAppCrashLocked$0(com.android.server.am.ProcessRecord p) {
        com.android.server.am.ActivityManagerService activityManagerService = this.mService;
        com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
                com.android.server.am.ActivityManagerService.boostPriorityForProcLockedSection();
                synchronized (activityManagerGlobalLock) {
                    try {
                        killAppImmediateLSP(p, 13, 14, "forced", "killed for invalid state");
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

    void sendRecoverableCrashToAppExitInfo(com.android.server.am.ProcessRecord r, android.app.ApplicationErrorReport.CrashInfo crashInfo) {
        if (r == null || crashInfo == null || !"Native crash".equals(crashInfo.exceptionClassName)) {
            return;
        }
        com.android.server.am.ActivityManagerService activityManagerService = this.mService;
        com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                this.mService.mProcessList.noteAppRecoverableCrash(r);
            } catch (java.lang.Throwable th) {
                com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
    }

    void crashApplication(com.android.server.am.ProcessRecord r, android.app.ApplicationErrorReport.CrashInfo crashInfo) {
        int callingPid = android.os.Binder.getCallingPid();
        int callingUid = android.os.Binder.getCallingUid();
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            crashApplicationInner(r, crashInfo, callingPid, callingUid);
        } finally {
            android.os.Binder.restoreCallingIdentity(origId);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:205:0x02ac A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void crashApplicationInner(com.android.server.am.ProcessRecord r28, android.app.ApplicationErrorReport.CrashInfo r29, int r30, int r31) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 782
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.AppErrors.crashApplicationInner(com.android.server.am.ProcessRecord, android.app.ApplicationErrorReport$CrashInfo, int, int):void");
    }

    private boolean handleAppCrashInActivityController(final com.android.server.am.ProcessRecord r, final android.app.ApplicationErrorReport.CrashInfo crashInfo, final java.lang.String shortMsg, final java.lang.String longMsg, final java.lang.String stackTrace, long timeMillis, int callingPid, int callingUid) {
        final java.lang.String name = r != null ? r.processName : null;
        final int pid = r != null ? r.getPid() : callingPid;
        final int uid = r != null ? r.info.uid : callingUid;
        return this.mService.mAtmInternal.handleAppCrashInActivityController(name, pid, shortMsg, longMsg, timeMillis, crashInfo.stackTrace, new java.lang.Runnable() { // from class: com.android.server.am.AppErrors$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$handleAppCrashInActivityController$1(crashInfo, name, pid, r, shortMsg, longMsg, stackTrace, uid);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleAppCrashInActivityController$1(android.app.ApplicationErrorReport.CrashInfo crashInfo, java.lang.String name, int pid, com.android.server.am.ProcessRecord r, java.lang.String shortMsg, java.lang.String longMsg, java.lang.String stackTrace, int uid) {
        if (android.os.Build.IS_DEBUGGABLE && "Native crash".equals(crashInfo.exceptionClassName)) {
            android.util.Slog.w("ActivityManager", "Skip killing native crashed app " + name + "(" + pid + ") during testing");
            return;
        }
        android.util.Slog.w("ActivityManager", "Force-killing crashed app " + name + " at watcher's request");
        if (r != null) {
            if (r.isPersistent()) {
                android.util.Slog.w("ActivityManager", r.getPid() + ":" + r.processName + " set crash due to handleAppCrashInActivityController");
            }
            if (!makeAppCrashingLocked(r, shortMsg, longMsg, stackTrace, null)) {
                r.killLocked("crash", 4, true);
                return;
            }
            return;
        }
        if (!this.mAppErrorsExt.isThreadGroupLeader("ActivityManager", pid)) {
            android.os.Process.killProcess(pid);
            com.android.server.am.ProcessList.killProcessGroup(uid, pid);
            this.mService.mProcessList.noteAppKill(pid, uid, 4, 0, "crash");
        }
    }

    private boolean makeAppCrashingLocked(com.android.server.am.ProcessRecord app, java.lang.String shortMsg, java.lang.String longMsg, java.lang.String stackTrace, com.android.server.am.AppErrorDialog.Data data) {
        boolean zHandleAppCrashLSPB;
        com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
        com.android.server.am.ActivityManagerService.boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            try {
                com.android.server.am.ProcessErrorStateRecord errState = app.mErrorState;
                errState.setCrashing(true);
                errState.setCrashingReport(generateProcessError(app, 1, null, shortMsg, longMsg, stackTrace));
                errState.startAppProblemLSP();
                app.getWindowProcessController().stopFreezingActivities();
                synchronized (this.mBadProcessLock) {
                    zHandleAppCrashLSPB = handleAppCrashLSPB(app, "force-crash", shortMsg, longMsg, stackTrace, data);
                }
            } catch (java.lang.Throwable th) {
                com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                throw th;
            }
        }
        com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
        return zHandleAppCrashLSPB;
    }

    android.app.ActivityManager.ProcessErrorStateInfo generateProcessError(com.android.server.am.ProcessRecord app, int condition, java.lang.String activity, java.lang.String shortMsg, java.lang.String longMsg, java.lang.String stackTrace) {
        android.app.ActivityManager.ProcessErrorStateInfo report = new android.app.ActivityManager.ProcessErrorStateInfo();
        report.condition = condition;
        report.processName = app.processName;
        report.pid = app.getPid();
        report.uid = app.info.uid;
        report.tag = activity;
        report.shortMsg = shortMsg;
        report.longMsg = longMsg;
        report.stackTrace = stackTrace;
        return report;
    }

    android.content.Intent createAppErrorIntentLOSP(com.android.server.am.ProcessRecord r, long timeMillis, android.app.ApplicationErrorReport.CrashInfo crashInfo) {
        android.app.ApplicationErrorReport report = createAppErrorReportLOSP(r, timeMillis, crashInfo);
        if (report == null) {
            return null;
        }
        android.content.Intent result = new android.content.Intent("android.intent.action.APP_ERROR");
        result.setComponent(r.mErrorState.getErrorReportReceiver());
        result.putExtra("android.intent.extra.BUG_REPORT", report);
        result.addFlags(268435456);
        return result;
    }

    private android.app.ApplicationErrorReport createAppErrorReportLOSP(com.android.server.am.ProcessRecord r, long timeMillis, android.app.ApplicationErrorReport.CrashInfo crashInfo) {
        com.android.server.am.ProcessErrorStateRecord errState = r.mErrorState;
        if (errState.getErrorReportReceiver() == null) {
            return null;
        }
        if (!errState.isCrashing() && !errState.isNotResponding() && !errState.isForceCrashReport()) {
            return null;
        }
        android.app.ApplicationErrorReport report = new android.app.ApplicationErrorReport();
        report.packageName = r.info.packageName;
        report.installerPackageName = errState.getErrorReportReceiver().getPackageName();
        report.processName = r.processName;
        report.time = timeMillis;
        report.systemApp = (r.info.flags & 1) != 0;
        if (errState.isCrashing() || errState.isForceCrashReport()) {
            report.type = 1;
            report.crashInfo = crashInfo;
        } else if (errState.isNotResponding()) {
            android.app.ActivityManager.ProcessErrorStateInfo anrReport = errState.getNotRespondingReport();
            if (anrReport == null) {
                return null;
            }
            report.type = 2;
            report.anrInfo = new android.app.ApplicationErrorReport.AnrInfo();
            report.anrInfo.activity = anrReport.tag;
            report.anrInfo.cause = anrReport.shortMsg;
            report.anrInfo.info = anrReport.longMsg;
        }
        return report;
    }

    /* JADX WARN: Removed duplicated region for block: B:32:0x00d2  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x00e9  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x00ec  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x0117  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x019e  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x01d2  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x01ea  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x01f2  */
    /* JADX WARN: Removed duplicated region for block: B:72:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private boolean handleAppCrashLSPB(com.android.server.am.ProcessRecord r27, java.lang.String r28, java.lang.String r29, java.lang.String r30, java.lang.String r31, com.android.server.am.AppErrorDialog.Data r32) {
        /*
            Method dump skipped, instruction units count: 511
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.AppErrors.handleAppCrashLSPB(com.android.server.am.ProcessRecord, java.lang.String, java.lang.String, java.lang.String, java.lang.String, com.android.server.am.AppErrorDialog$Data):boolean");
    }

    private void updateProcessCrashCountLBp(java.lang.String processName, int uid, long now) {
        android.util.Pair<java.lang.Long, java.lang.Integer> count;
        android.util.Pair<java.lang.Long, java.lang.Integer> count2 = (android.util.Pair) this.mProcessCrashCounts.get(processName, uid);
        if (count2 == null || ((java.lang.Long) count2.first).longValue() + com.android.server.am.ActivityManagerConstants.PROCESS_CRASH_COUNT_RESET_INTERVAL < now) {
            count = new android.util.Pair<>(java.lang.Long.valueOf(now), 1);
        } else {
            count = new android.util.Pair<>((java.lang.Long) count2.first, java.lang.Integer.valueOf(((java.lang.Integer) count2.second).intValue() + 1));
        }
        this.mProcessCrashCounts.put(processName, uid, count);
    }

    private boolean isProcOverCrashLimitLBp(com.android.server.am.ProcessRecord app, long now) {
        android.util.Pair<java.lang.Long, java.lang.Integer> crashCount = (android.util.Pair) this.mProcessCrashCounts.get(app.processName, app.uid);
        return !app.isolated && crashCount != null && now < ((java.lang.Long) crashCount.first).longValue() + com.android.server.am.ActivityManagerConstants.PROCESS_CRASH_COUNT_RESET_INTERVAL && ((java.lang.Integer) crashCount.second).intValue() >= com.android.server.am.ActivityManagerConstants.PROCESS_CRASH_COUNT_LIMIT;
    }

    /* JADX WARN: Code restructure failed: missing block: B:114:0x01da, code lost:
    
        if (r3.repeating == false) goto L108;
     */
    /* JADX WARN: Removed duplicated region for block: B:103:0x01b8  */
    /* JADX WARN: Removed duplicated region for block: B:112:0x01d6 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:119:0x0210 A[Catch: all -> 0x024d, TryCatch #3 {all -> 0x024d, blocks: (B:122:0x0227, B:124:0x0242, B:125:0x0247, B:117:0x0201, B:119:0x0210), top: B:153:0x01c6 }] */
    /* JADX WARN: Removed duplicated region for block: B:120:0x0220  */
    /* JADX WARN: Removed duplicated region for block: B:155:0x017f A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:157:0x013f A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:54:0x0108  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x010a  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x0116  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x0133  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x0135  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x0155  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    void handleShowAppErrorUi(android.os.Message r26) {
        /*
            Method dump skipped, instruction units count: 637
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.AppErrors.handleShowAppErrorUi(android.os.Message):void");
    }

    private void stopReportingCrashesLBp(com.android.server.am.ProcessRecord proc) {
        if (this.mAppsNotReportingCrashes == null) {
            this.mAppsNotReportingCrashes = new android.util.ArraySet<>();
        }
        this.mAppsNotReportingCrashes.add(proc.info.packageName);
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x003a A[Catch: all -> 0x015f, TRY_ENTER, TryCatch #2 {all -> 0x015f, blocks: (B:10:0x0029, B:15:0x003a, B:16:0x0058, B:27:0x0084, B:49:0x0134, B:52:0x013f, B:32:0x009f, B:33:0x00b9, B:63:0x015a), top: B:73:0x0029 }] */
    /* JADX WARN: Removed duplicated region for block: B:19:0x005d A[Catch: all -> 0x0157, TRY_ENTER, TryCatch #1 {all -> 0x0157, blocks: (B:8:0x001e, B:12:0x002e, B:19:0x005d, B:23:0x0077, B:29:0x008d, B:37:0x00c8, B:40:0x00d8), top: B:71:0x001e }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    void handleShowAnrUi(android.os.Message r18) {
        /*
            Method dump skipped, instruction units count: 353
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.AppErrors.handleShowAnrUi(android.os.Message):void");
    }

    void handleDismissAnrDialogs(com.android.server.am.ProcessRecord proc) {
        com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
        com.android.server.am.ActivityManagerService.boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            try {
                com.android.server.am.ProcessErrorStateRecord errState = proc.mErrorState;
                this.mService.mUiHandler.removeMessages(2, errState.getAnrData());
                if (errState.getDialogController().hasAnrDialogs()) {
                    errState.setNotResponding(false);
                    errState.setNotRespondingReport(null);
                    errState.getDialogController().clearAnrDialogs();
                }
                proc.mErrorState.setAnrData(null);
            } catch (java.lang.Throwable th) {
                com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                throw th;
            }
        }
        com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
    }

    static final class BadProcessInfo {
        final java.lang.String longMsg;
        final java.lang.String shortMsg;
        final java.lang.String stack;
        final long time;

        BadProcessInfo(long time, java.lang.String shortMsg, java.lang.String longMsg, java.lang.String stack) {
            this.time = time;
            this.shortMsg = shortMsg;
            this.longMsg = longMsg;
            this.stack = stack;
        }
    }
}
