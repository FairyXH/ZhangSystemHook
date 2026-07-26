package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public final class PhantomProcessList {
    private static final java.lang.String[] CGROUP_PATH_PREFIXES = {"/acct/uid_", "/sys/fs/cgroup/uid_"};
    private static final java.lang.String CGROUP_PID_PREFIX = "/pid_";
    private static final java.lang.String CGROUP_PROCS = "/cgroup.procs";
    private static final int CGROUP_V1 = 0;
    private static final int CGROUP_V2 = 1;
    static final java.lang.String TAG = "ActivityManager";
    com.android.server.am.PhantomProcessList.Injector mInjector;
    private final android.os.Handler mKillHandler;
    private final com.android.server.am.ActivityManagerService mService;
    int mUpdateSeq;
    final java.lang.Object mLock = new java.lang.Object();
    final android.util.SparseArray<com.android.server.am.PhantomProcessRecord> mPhantomProcesses = new android.util.SparseArray<>();
    final android.util.SparseArray<android.util.SparseArray<com.android.server.am.PhantomProcessRecord>> mAppPhantomProcessMap = new android.util.SparseArray<>();
    final android.util.SparseArray<com.android.server.am.PhantomProcessRecord> mPhantomProcessesPidFds = new android.util.SparseArray<>();
    final android.util.SparseArray<com.android.server.am.PhantomProcessRecord> mZombiePhantomProcesses = new android.util.SparseArray<>();
    private final java.util.ArrayList<com.android.server.am.PhantomProcessRecord> mTempPhantomProcesses = new java.util.ArrayList<>();
    private final android.util.SparseArray<com.android.server.am.ProcessRecord> mPhantomToAppProcessMap = new android.util.SparseArray<>();
    private final android.util.SparseArray<java.io.InputStream> mCgroupProcsFds = new android.util.SparseArray<>();
    private final byte[] mDataBuffer = new byte[4096];
    private boolean mTrimPhantomProcessScheduled = false;
    int mCgroupVersion = 0;

    PhantomProcessList(com.android.server.am.ActivityManagerService service) {
        this.mService = service;
        com.android.server.am.ProcessList processList = service.mProcessList;
        this.mKillHandler = com.android.server.am.ProcessList.sKillHandler;
        this.mInjector = new com.android.server.am.PhantomProcessList.Injector();
        probeCgroupVersion();
    }

    void lookForPhantomProcessesLocked() {
        this.mPhantomToAppProcessMap.clear();
        android.os.StrictMode.ThreadPolicy oldPolicy = android.os.StrictMode.allowThreadDiskReads();
        try {
            synchronized (this.mService.mPidsSelfLocked) {
                for (int i = this.mService.mPidsSelfLocked.size() - 1; i >= 0; i--) {
                    com.android.server.am.ProcessRecord app = this.mService.mPidsSelfLocked.valueAt(i);
                    lookForPhantomProcessesLocked(app);
                }
            }
        } finally {
            android.os.StrictMode.setThreadPolicy(oldPolicy);
        }
    }

    private void lookForPhantomProcessesLocked(com.android.server.am.ProcessRecord app) throws java.io.FileNotFoundException {
        int read;
        int i;
        if (app.appZygote || app.isKilled() || app.isKilledByAm()) {
            return;
        }
        int appPid = app.getPid();
        java.io.InputStream input = this.mCgroupProcsFds.get(appPid);
        if (input == null) {
            java.lang.String path = getCgroupFilePath(app.info.uid, appPid);
            try {
                input = this.mInjector.openCgroupProcs(path);
                this.mCgroupProcsFds.put(appPid, input);
            } catch (java.io.FileNotFoundException | java.lang.SecurityException e) {
                if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_PROCESSES) {
                    android.util.Slog.w("ActivityManager", "Unable to open " + path, e);
                    return;
                }
                return;
            }
        }
        byte[] buf = this.mDataBuffer;
        int pid = 0;
        long totalRead = 0;
        do {
            try {
                read = this.mInjector.readCgroupProcs(input, buf, 0, buf.length);
                if (read == -1) {
                    break;
                }
                totalRead += (long) read;
                for (int i2 = 0; i2 < read; i2++) {
                    byte b = buf[i2];
                    if (b == 10) {
                        addChildPidLocked(app, pid, appPid);
                        pid = 0;
                    } else {
                        pid = (pid * 10) + (b - 48);
                    }
                }
                i = buf.length;
            } catch (java.io.IOException e2) {
                android.util.Slog.e("ActivityManager", "Error in reading cgroup procs from " + app, e2);
                libcore.io.IoUtils.closeQuietly(input);
                this.mCgroupProcsFds.delete(appPid);
                return;
            }
        } while (read >= i);
        if (pid != 0) {
            addChildPidLocked(app, pid, appPid);
        }
        input.skip(-totalRead);
    }

    private void probeCgroupVersion() {
        for (int i = CGROUP_PATH_PREFIXES.length - 1; i >= 0; i--) {
            if (new java.io.File(CGROUP_PATH_PREFIXES[i] + 1000).exists()) {
                this.mCgroupVersion = i;
                return;
            }
        }
    }

    java.lang.String getCgroupFilePath(int uid, int pid) {
        return CGROUP_PATH_PREFIXES[this.mCgroupVersion] + uid + CGROUP_PID_PREFIX + pid + CGROUP_PROCS;
    }

    static java.lang.String getProcessName(int pid) {
        java.lang.String procName = com.android.internal.os.ProcStatsUtil.readTerminatedProcFile("/proc/" + pid + "/cmdline", (byte) 0);
        if (procName == null) {
            return null;
        }
        int l = procName.lastIndexOf(47);
        if (l > 0 && l < procName.length() - 1) {
            return procName.substring(l + 1);
        }
        return procName;
    }

    private void addChildPidLocked(com.android.server.am.ProcessRecord app, int pid, int appPid) {
        if (appPid != pid) {
            com.android.server.am.ProcessRecord r = this.mService.mPidsSelfLocked.get(pid);
            if (r != null) {
                if (!r.appZygote && com.android.server.am.ActivityManagerDebugConfig.DEBUG_PROCESSES) {
                    android.util.Slog.w("ActivityManager", "Unexpected: " + r + " appears in the cgroup.procs of " + app);
                    return;
                }
                return;
            }
            int index = this.mPhantomToAppProcessMap.indexOfKey(pid);
            if (index >= 0) {
                com.android.server.am.ProcessRecord current = this.mPhantomToAppProcessMap.valueAt(index);
                if (app == current) {
                    return;
                } else {
                    this.mPhantomToAppProcessMap.setValueAt(index, app);
                }
            } else {
                this.mPhantomToAppProcessMap.put(pid, app);
            }
            int uid = android.os.Process.getUidForPid(pid);
            java.lang.String procName = this.mInjector.getProcessName(pid);
            if (procName == null || uid < 0) {
                this.mPhantomToAppProcessMap.delete(pid);
            } else {
                getOrCreatePhantomProcessIfNeededLocked(procName, uid, pid, true);
            }
        }
    }

    void onAppDied(int pid) {
        synchronized (this.mLock) {
            int index = this.mCgroupProcsFds.indexOfKey(pid);
            if (index >= 0) {
                java.io.InputStream inputStream = this.mCgroupProcsFds.valueAt(index);
                this.mCgroupProcsFds.removeAt(index);
                libcore.io.IoUtils.closeQuietly(inputStream);
            }
        }
    }

    com.android.server.am.PhantomProcessRecord getOrCreatePhantomProcessIfNeededLocked(java.lang.String processName, int uid, int pid, boolean createIfNeeded) {
        com.android.server.am.ProcessRecord r;
        if (isAppProcess(pid)) {
            return null;
        }
        int index = this.mPhantomProcesses.indexOfKey(pid);
        if (index >= 0) {
            com.android.server.am.PhantomProcessRecord proc = this.mPhantomProcesses.valueAt(index);
            if (proc.equals(processName, uid, pid)) {
                return proc;
            }
            android.util.Slog.w("ActivityManager", "Stale " + proc + ", removing");
            onPhantomProcessKilledLocked(proc);
        } else {
            int idx = this.mZombiePhantomProcesses.indexOfKey(pid);
            if (idx >= 0) {
                com.android.server.am.PhantomProcessRecord proc2 = this.mZombiePhantomProcesses.valueAt(idx);
                if (proc2.equals(processName, uid, pid)) {
                    return proc2;
                }
                this.mZombiePhantomProcesses.removeAt(idx);
            }
        }
        if (!createIfNeeded || (r = this.mPhantomToAppProcessMap.get(pid)) == null) {
            return null;
        }
        try {
            int appPid = r.getPid();
            com.android.server.am.PhantomProcessRecord proc3 = new com.android.server.am.PhantomProcessRecord(processName, uid, pid, appPid, this.mService, new java.util.function.Consumer() { // from class: com.android.server.am.PhantomProcessList$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.onPhantomProcessKilledLocked((com.android.server.am.PhantomProcessRecord) obj);
                }
            });
            proc3.mUpdateSeq = this.mUpdateSeq;
            this.mPhantomProcesses.put(pid, proc3);
            android.util.SparseArray<com.android.server.am.PhantomProcessRecord> array = this.mAppPhantomProcessMap.get(appPid);
            if (array == null) {
                array = new android.util.SparseArray<>();
                this.mAppPhantomProcessMap.put(appPid, array);
            }
            array.put(pid, proc3);
            if (proc3.mPidFd != null) {
                this.mKillHandler.getLooper().getQueue().addOnFileDescriptorEventListener(proc3.mPidFd, 5, new android.os.MessageQueue.OnFileDescriptorEventListener() { // from class: com.android.server.am.PhantomProcessList$$ExternalSyntheticLambda1
                    @Override // android.os.MessageQueue.OnFileDescriptorEventListener
                    public final int onFileDescriptorEvents(java.io.FileDescriptor fileDescriptor, int i) {
                        return this.f$0.onPhantomProcessFdEvent(fileDescriptor, i);
                    }
                });
                this.mPhantomProcessesPidFds.put(proc3.mPidFd.getInt$(), proc3);
            }
            scheduleTrimPhantomProcessesLocked();
            return proc3;
        } catch (java.lang.IllegalStateException e) {
            return null;
        }
    }

    private boolean isAppProcess(int pid) {
        boolean z;
        synchronized (this.mService.mPidsSelfLocked) {
            z = this.mService.mPidsSelfLocked.get(pid) != null;
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int onPhantomProcessFdEvent(java.io.FileDescriptor fd, int events) {
        synchronized (this.mLock) {
            com.android.server.am.PhantomProcessRecord proc = this.mPhantomProcessesPidFds.get(fd.getInt$());
            if (proc == null) {
                return 0;
            }
            if ((events & 1) != 0) {
                proc.onProcDied(true);
            } else {
                proc.killLocked("Process error", true);
            }
            return 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onPhantomProcessKilledLocked(com.android.server.am.PhantomProcessRecord proc) {
        if (proc.mPidFd != null && proc.mPidFd.valid()) {
            this.mKillHandler.getLooper().getQueue().removeOnFileDescriptorEventListener(proc.mPidFd);
            this.mPhantomProcessesPidFds.remove(proc.mPidFd.getInt$());
            libcore.io.IoUtils.closeQuietly(proc.mPidFd);
        }
        this.mPhantomProcesses.remove(proc.mPid);
        int index = this.mAppPhantomProcessMap.indexOfKey(proc.mPpid);
        if (index < 0) {
            return;
        }
        android.util.SparseArray<com.android.server.am.PhantomProcessRecord> array = this.mAppPhantomProcessMap.valueAt(index);
        array.remove(proc.mPid);
        if (array.size() == 0) {
            this.mAppPhantomProcessMap.removeAt(index);
        }
        if (proc.mZombie) {
            this.mZombiePhantomProcesses.put(proc.mPid, proc);
        } else {
            this.mZombiePhantomProcesses.remove(proc.mPid);
        }
    }

    private void scheduleTrimPhantomProcessesLocked() {
        if (!this.mTrimPhantomProcessScheduled) {
            this.mTrimPhantomProcessScheduled = true;
            this.mService.mHandler.post(new com.android.server.am.ActivityManagerConstants$$ExternalSyntheticLambda0(this));
        }
    }

    void trimPhantomProcessesIfNecessary() {
        if (!this.mService.mSystemReady || !android.util.FeatureFlagUtils.isEnabled(this.mService.mContext, "settings_enable_monitor_phantom_procs")) {
            return;
        }
        com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mService.mProcLock;
        com.android.server.am.ActivityManagerService.boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            try {
                synchronized (this.mLock) {
                    this.mTrimPhantomProcessScheduled = false;
                    if (this.mService.mConstants.MAX_PHANTOM_PROCESSES < this.mPhantomProcesses.size()) {
                        for (int i = this.mPhantomProcesses.size() - 1; i >= 0; i--) {
                            this.mTempPhantomProcesses.add(this.mPhantomProcesses.valueAt(i));
                        }
                        synchronized (this.mService.mPidsSelfLocked) {
                            java.util.Collections.sort(this.mTempPhantomProcesses, new java.util.Comparator() { // from class: com.android.server.am.PhantomProcessList$$ExternalSyntheticLambda2
                                @Override // java.util.Comparator
                                public final int compare(java.lang.Object obj, java.lang.Object obj2) {
                                    return this.f$0.lambda$trimPhantomProcessesIfNecessary$0((com.android.server.am.PhantomProcessRecord) obj, (com.android.server.am.PhantomProcessRecord) obj2);
                                }
                            });
                        }
                        for (int i2 = this.mTempPhantomProcesses.size() - 1; i2 >= this.mService.mConstants.MAX_PHANTOM_PROCESSES; i2--) {
                            com.android.server.am.PhantomProcessRecord proc = this.mTempPhantomProcesses.get(i2);
                            proc.killLocked("Trimming phantom processes", true);
                        }
                        this.mTempPhantomProcesses.clear();
                    }
                }
            } catch (java.lang.Throwable th) {
                com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                throw th;
            }
        }
        com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ int lambda$trimPhantomProcessesIfNecessary$0(com.android.server.am.PhantomProcessRecord a, com.android.server.am.PhantomProcessRecord b) {
        com.android.server.am.ProcessRecord ra = this.mService.mPidsSelfLocked.get(a.mPpid);
        com.android.server.am.ProcessRecord rb = this.mService.mPidsSelfLocked.get(b.mPpid);
        if (ra == null && rb == null) {
            return 0;
        }
        if (ra == null) {
            return 1;
        }
        if (rb == null) {
            return -1;
        }
        if (ra.mState.getCurAdj() != rb.mState.getCurAdj()) {
            return ra.mState.getCurAdj() - rb.mState.getCurAdj();
        }
        if (a.mKnownSince == b.mKnownSince) {
            return 0;
        }
        if (a.mKnownSince < b.mKnownSince) {
            return 1;
        }
        return -1;
    }

    void pruneStaleProcessesLocked() {
        for (int i = this.mPhantomProcesses.size() - 1; i >= 0; i--) {
            com.android.server.am.PhantomProcessRecord proc = this.mPhantomProcesses.valueAt(i);
            if (proc.mUpdateSeq < this.mUpdateSeq) {
                if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_PROCESSES) {
                    android.util.Slog.v("ActivityManager", "Pruning " + proc + " as it should have been dead.");
                }
                proc.killLocked("Stale process", true);
            }
        }
        for (int i2 = this.mZombiePhantomProcesses.size() - 1; i2 >= 0; i2--) {
            com.android.server.am.PhantomProcessRecord proc2 = this.mZombiePhantomProcesses.valueAt(i2);
            if (proc2.mUpdateSeq < this.mUpdateSeq && com.android.server.am.ActivityManagerDebugConfig.DEBUG_PROCESSES) {
                android.util.Slog.v("ActivityManager", "Pruning " + proc2 + " as it should have been dead.");
            }
        }
    }

    void killPhantomProcessGroupLocked(com.android.server.am.ProcessRecord app, com.android.server.am.PhantomProcessRecord proc, int reasonCode, int subReason, java.lang.String msg) {
        synchronized (this.mLock) {
            int index = this.mAppPhantomProcessMap.indexOfKey(proc.mPpid);
            if (index >= 0) {
                android.util.SparseArray<com.android.server.am.PhantomProcessRecord> array = this.mAppPhantomProcessMap.valueAt(index);
                for (int i = array.size() - 1; i >= 0; i--) {
                    com.android.server.am.PhantomProcessRecord r = array.valueAt(i);
                    if (r == proc) {
                        r.killLocked(msg, true);
                    } else {
                        r.killLocked("Caused by siling process: " + msg, false);
                    }
                }
            }
        }
        app.killLocked("Caused by child process: " + msg, reasonCode, subReason, true);
    }

    void forEachPhantomProcessOfApp(com.android.server.am.ProcessRecord app, java.util.function.Function<com.android.server.am.PhantomProcessRecord, java.lang.Boolean> callback) {
        synchronized (this.mLock) {
            int index = this.mAppPhantomProcessMap.indexOfKey(app.getPid());
            if (index >= 0) {
                android.util.SparseArray<com.android.server.am.PhantomProcessRecord> array = this.mAppPhantomProcessMap.valueAt(index);
                for (int i = array.size() - 1; i >= 0; i--) {
                    com.android.server.am.PhantomProcessRecord r = array.valueAt(i);
                    if (!callback.apply(r).booleanValue()) {
                        break;
                    }
                }
            }
        }
    }

    void updateProcessCpuStatesLocked(com.android.internal.os.ProcessCpuTracker tracker) {
        synchronized (this.mLock) {
            this.mUpdateSeq++;
            lookForPhantomProcessesLocked();
            for (int i = tracker.countStats() - 1; i >= 0; i--) {
                com.android.internal.os.ProcessCpuTracker.Stats st = tracker.getStats(i);
                com.android.server.am.PhantomProcessRecord r = getOrCreatePhantomProcessIfNeededLocked(st.name, st.uid, st.pid, false);
                if (r != null) {
                    r.mUpdateSeq = this.mUpdateSeq;
                    r.mCurrentCputime += (long) (st.rel_utime + st.rel_stime);
                    if (r.mLastCputime == 0) {
                        r.mLastCputime = r.mCurrentCputime;
                    }
                    r.updateAdjLocked();
                }
            }
            pruneStaleProcessesLocked();
        }
    }

    void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        synchronized (this.mLock) {
            dumpPhantomeProcessLocked(pw, prefix, "All Active App Child Processes:", this.mPhantomProcesses);
            dumpPhantomeProcessLocked(pw, prefix, "All Zombie App Child Processes:", this.mZombiePhantomProcesses);
        }
    }

    void dumpPhantomeProcessLocked(java.io.PrintWriter pw, java.lang.String prefix, java.lang.String headline, android.util.SparseArray<com.android.server.am.PhantomProcessRecord> list) {
        int size = list.size();
        if (size == 0) {
            return;
        }
        pw.println();
        pw.print(prefix);
        pw.println(headline);
        for (int i = 0; i < size; i++) {
            com.android.server.am.PhantomProcessRecord proc = list.valueAt(i);
            pw.print(prefix);
            pw.print("  proc #");
            pw.print(i);
            pw.print(": ");
            pw.println(proc.toString());
            proc.dump(pw, prefix + "    ");
        }
    }

    static class Injector {
        Injector() {
        }

        java.io.InputStream openCgroupProcs(java.lang.String path) throws java.lang.SecurityException, java.io.FileNotFoundException {
            return new java.io.FileInputStream(path);
        }

        int readCgroupProcs(java.io.InputStream input, byte[] buf, int offset, int len) throws java.io.IOException {
            return input.read(buf, offset, len);
        }

        java.lang.String getProcessName(int pid) {
            return com.android.server.am.PhantomProcessList.getProcessName(pid);
        }
    }
}
