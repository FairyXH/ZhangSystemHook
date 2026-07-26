package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public final class ProcessStatsService extends com.android.internal.app.procstats.IProcessStats.Stub {
    static final boolean DEBUG = false;
    static final int MAX_HISTORIC_STATES = 8;
    static final java.lang.String STATE_FILE_CHECKIN_SUFFIX = ".ci";
    static final java.lang.String STATE_FILE_PREFIX = "state-v2-";
    static final java.lang.String STATE_FILE_SUFFIX = ".bin";
    static final java.lang.String TAG = "ProcessStatsService";
    static long WRITE_PERIOD = 1800000;
    final com.android.server.am.ActivityManagerService mAm;
    final java.io.File mBaseDir;
    boolean mCommitPending;
    android.util.AtomicFile mFile;
    java.lang.Boolean mInjectedScreenState;
    long mLastWriteTime;
    boolean mMemFactorLowered;
    android.os.Parcel mPendingWrite;
    boolean mPendingWriteCommitted;
    android.util.AtomicFile mPendingWriteFile;
    final com.android.internal.app.procstats.ProcessStats mProcessStats;
    boolean mShuttingDown;
    final java.lang.Object mLock = new java.lang.Object();
    final java.lang.Object mPendingWriteLock = new java.lang.Object();
    final java.util.concurrent.locks.ReentrantLock mFileLock = new java.util.concurrent.locks.ReentrantLock();
    int mLastMemOnlyState = -1;

    public ProcessStatsService(com.android.server.am.ActivityManagerService am, java.io.File file) {
        this.mAm = am;
        this.mBaseDir = file;
        this.mBaseDir.mkdirs();
        synchronized (this.mLock) {
            this.mProcessStats = new com.android.internal.app.procstats.ProcessStats(true);
            updateFileLocked();
        }
        android.os.SystemProperties.addChangeCallback(new java.lang.Runnable() { // from class: com.android.server.am.ProcessStatsService.1
            @Override // java.lang.Runnable
            public void run() {
                synchronized (com.android.server.am.ProcessStatsService.this.mLock) {
                    if (com.android.server.am.ProcessStatsService.this.mProcessStats.evaluateSystemProperties(false)) {
                        com.android.server.am.ProcessStatsService.this.mProcessStats.mFlags |= 4;
                        com.android.server.am.ProcessStatsService.this.writeStateLocked(true, true);
                        com.android.server.am.ProcessStatsService.this.mProcessStats.evaluateSystemProperties(true);
                    }
                }
            }
        });
    }

    public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException {
        try {
            return super.onTransact(code, data, reply, flags);
        } catch (java.lang.RuntimeException e) {
            if (!(e instanceof java.lang.SecurityException)) {
                android.util.Slog.wtf(TAG, "Process Stats Crash", e);
            }
            throw e;
        }
    }

    void updateProcessStateHolderLocked(com.android.internal.app.procstats.ProcessStats.ProcessStateHolder holder, java.lang.String packageName, int uid, long versionCode, java.lang.String processName) {
        holder.pkg = this.mProcessStats.getPackageStateLocked(packageName, uid, versionCode);
        holder.state = this.mProcessStats.getProcessStateLocked(holder.pkg, processName);
    }

    com.android.internal.app.procstats.ProcessState getProcessStateLocked(java.lang.String packageName, int uid, long versionCode, java.lang.String processName) {
        return this.mProcessStats.getProcessStateLocked(packageName, uid, versionCode, processName);
    }

    com.android.internal.app.procstats.ServiceState getServiceState(java.lang.String packageName, int uid, long versionCode, java.lang.String processName, java.lang.String className) {
        com.android.internal.app.procstats.ServiceState serviceStateLocked;
        synchronized (this.mLock) {
            serviceStateLocked = this.mProcessStats.getServiceStateLocked(packageName, uid, versionCode, processName, className);
        }
        return serviceStateLocked;
    }

    boolean isMemFactorLowered() {
        return this.mMemFactorLowered;
    }

    boolean setMemFactorLocked(int memFactor, boolean screenOn, long now) {
        this.mMemFactorLowered = memFactor < this.mLastMemOnlyState;
        this.mLastMemOnlyState = memFactor;
        if (this.mInjectedScreenState != null) {
            screenOn = this.mInjectedScreenState.booleanValue();
        }
        if (screenOn) {
            memFactor += 4;
        }
        if (memFactor == this.mProcessStats.mMemFactor) {
            return false;
        }
        if (this.mProcessStats.mMemFactor != -1) {
            long[] jArr = this.mProcessStats.mMemFactorDurations;
            int i = this.mProcessStats.mMemFactor;
            jArr[i] = jArr[i] + (now - this.mProcessStats.mStartTime);
        }
        this.mProcessStats.mMemFactor = memFactor;
        this.mProcessStats.mStartTime = now;
        android.util.ArrayMap<java.lang.String, android.util.SparseArray<android.util.LongSparseArray<com.android.internal.app.procstats.ProcessStats.PackageState>>> pmap = this.mProcessStats.mPackages.getMap();
        for (int ipkg = pmap.size() - 1; ipkg >= 0; ipkg--) {
            android.util.SparseArray<android.util.LongSparseArray<com.android.internal.app.procstats.ProcessStats.PackageState>> uids = pmap.valueAt(ipkg);
            for (int iuid = uids.size() - 1; iuid >= 0; iuid--) {
                android.util.LongSparseArray<com.android.internal.app.procstats.ProcessStats.PackageState> vers = uids.valueAt(iuid);
                for (int iver = vers.size() - 1; iver >= 0; iver--) {
                    com.android.internal.app.procstats.ProcessStats.PackageState pkg = vers.valueAt(iver);
                    android.util.ArrayMap<java.lang.String, com.android.internal.app.procstats.ServiceState> services = pkg.mServices;
                    for (int isvc = services.size() - 1; isvc >= 0; isvc--) {
                        com.android.internal.app.procstats.ServiceState service = services.valueAt(isvc);
                        service.setMemFactor(memFactor, now);
                    }
                }
            }
        }
        return true;
    }

    int getMemFactorLocked() {
        if (this.mProcessStats.mMemFactor != -1) {
            return this.mProcessStats.mMemFactor;
        }
        return 0;
    }

    void addSysMemUsageLocked(long cachedMem, long freeMem, long zramMem, long kernelMem, long nativeMem) {
        this.mProcessStats.addSysMemUsage(cachedMem, freeMem, zramMem, kernelMem, nativeMem);
    }

    void updateTrackingAssociationsLocked(int curSeq, long now) {
        this.mProcessStats.updateTrackingAssociationsLocked(curSeq, now);
    }

    boolean shouldWriteNowLocked(long now) {
        if (now <= this.mLastWriteTime + WRITE_PERIOD) {
            return false;
        }
        if (android.os.SystemClock.elapsedRealtime() > this.mProcessStats.mTimePeriodStartRealtime + com.android.internal.app.procstats.ProcessStats.COMMIT_PERIOD && android.os.SystemClock.uptimeMillis() > this.mProcessStats.mTimePeriodStartUptime + com.android.internal.app.procstats.ProcessStats.COMMIT_UPTIME_PERIOD) {
            this.mCommitPending = true;
        }
        return true;
    }

    void shutdown() {
        android.util.Slog.w(TAG, "Writing process stats before shutdown...");
        synchronized (this.mLock) {
            this.mProcessStats.mFlags |= 2;
            writeStateSyncLocked();
            this.mShuttingDown = true;
        }
    }

    void writeStateAsync() {
        synchronized (this.mLock) {
            writeStateLocked(false);
        }
    }

    private void writeStateSyncLocked() {
        writeStateLocked(true);
    }

    private void writeStateLocked(boolean sync) {
        if (this.mShuttingDown) {
            return;
        }
        boolean commitPending = this.mCommitPending;
        this.mCommitPending = false;
        writeStateLocked(sync, commitPending);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void writeStateLocked(boolean sync, boolean commit) {
        synchronized (this.mPendingWriteLock) {
            long now = android.os.SystemClock.uptimeMillis();
            if (this.mPendingWrite == null || !this.mPendingWriteCommitted) {
                this.mPendingWrite = android.os.Parcel.obtain();
                this.mProcessStats.mTimePeriodEndRealtime = android.os.SystemClock.elapsedRealtime();
                this.mProcessStats.mTimePeriodEndUptime = now;
                if (commit) {
                    this.mProcessStats.mFlags |= 1;
                }
                this.mProcessStats.writeToParcel(this.mPendingWrite, 0);
                this.mPendingWriteFile = new android.util.AtomicFile(getCurrentFile());
                this.mPendingWriteCommitted = commit;
            }
            if (commit) {
                this.mProcessStats.resetSafely();
                updateFileLocked();
                scheduleRequestPssAllProcs(true, false);
            }
            this.mLastWriteTime = android.os.SystemClock.uptimeMillis();
            final long totalTime = android.os.SystemClock.uptimeMillis() - now;
            if (!sync) {
                com.android.internal.os.BackgroundThread.getHandler().post(new java.lang.Runnable() { // from class: com.android.server.am.ProcessStatsService.2
                    @Override // java.lang.Runnable
                    public void run() {
                        com.android.server.am.ProcessStatsService.this.performWriteState(totalTime);
                    }
                });
            } else {
                performWriteState(totalTime);
            }
        }
    }

    private void scheduleRequestPssAllProcs(final boolean always, final boolean memLowered) {
        this.mAm.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.ProcessStatsService$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$scheduleRequestPssAllProcs$0(always, memLowered);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleRequestPssAllProcs$0(boolean always, boolean memLowered) {
        com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mAm.mProcLock;
        com.android.server.am.ActivityManagerService.boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            try {
                this.mAm.mAppProfiler.requestPssAllProcsLPr(android.os.SystemClock.uptimeMillis(), always, memLowered);
            } catch (java.lang.Throwable th) {
                com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                throw th;
            }
        }
        com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
    }

    private void updateFileLocked() {
        this.mFileLock.lock();
        try {
            this.mFile = new android.util.AtomicFile(new java.io.File(this.mBaseDir, STATE_FILE_PREFIX + this.mProcessStats.mTimePeriodStartClockStr + STATE_FILE_SUFFIX));
            this.mFileLock.unlock();
            this.mLastWriteTime = android.os.SystemClock.uptimeMillis();
        } catch (java.lang.Throwable th) {
            this.mFileLock.unlock();
            throw th;
        }
    }

    private java.io.File getCurrentFile() {
        this.mFileLock.lock();
        try {
            return this.mFile.getBaseFile();
        } finally {
            this.mFileLock.unlock();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void performWriteState(long initialTime) {
        synchronized (this.mPendingWriteLock) {
            android.os.Parcel data = this.mPendingWrite;
            android.util.AtomicFile file = this.mPendingWriteFile;
            this.mPendingWriteCommitted = false;
            if (data == null) {
                return;
            }
            this.mPendingWrite = null;
            this.mPendingWriteFile = null;
            this.mFileLock.lock();
            long startTime = android.os.SystemClock.uptimeMillis();
            java.io.FileOutputStream stream = null;
            try {
                try {
                    stream = file.startWrite();
                    stream.write(data.marshall());
                    stream.flush();
                    file.finishWrite(stream);
                    com.android.internal.logging.EventLogTags.writeCommitSysConfigFile("procstats", (android.os.SystemClock.uptimeMillis() - startTime) + initialTime);
                } catch (java.io.IOException e) {
                    android.util.Slog.w(TAG, "Error writing process statistics", e);
                    file.failWrite(stream);
                }
            } finally {
                data.recycle();
                trimHistoricStatesWriteLF();
                this.mFileLock.unlock();
            }
        }
    }

    private boolean readLF(com.android.internal.app.procstats.ProcessStats stats, android.util.AtomicFile file) {
        try {
            java.io.FileInputStream stream = file.openRead();
            stats.read(stream);
            stream.close();
            if (stats.mReadError != null) {
                android.util.Slog.w(TAG, "Ignoring existing stats; " + stats.mReadError);
                return false;
            }
            return true;
        } catch (java.lang.Throwable e) {
            stats.mReadError = "caught exception: " + e;
            android.util.Slog.e(TAG, "Error reading process statistics", e);
            return false;
        }
    }

    private java.util.ArrayList<java.lang.String> getCommittedFilesLF(int minNum, boolean inclCurrent, boolean inclCheckedIn) {
        java.io.File[] files = this.mBaseDir.listFiles();
        if (files == null || files.length <= minNum) {
            return null;
        }
        java.util.ArrayList<java.lang.String> filesArray = new java.util.ArrayList<>(files.length);
        java.lang.String currentFile = this.mFile.getBaseFile().getPath();
        for (java.io.File file : files) {
            java.lang.String fileStr = file.getPath();
            if (file.getName().startsWith(STATE_FILE_PREFIX) && ((inclCheckedIn || !fileStr.endsWith(STATE_FILE_CHECKIN_SUFFIX)) && (inclCurrent || !fileStr.equals(currentFile)))) {
                filesArray.add(fileStr);
            }
        }
        java.util.Collections.sort(filesArray);
        return filesArray;
    }

    private void trimHistoricStatesWriteLF() {
        java.io.File[] files = this.mBaseDir.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                if (!files[i].getName().startsWith(STATE_FILE_PREFIX)) {
                    files[i].delete();
                }
            }
        }
        java.util.ArrayList<java.lang.String> filesArray = getCommittedFilesLF(8, false, true);
        if (filesArray != null) {
            while (filesArray.size() > 8) {
                java.lang.String file = filesArray.remove(0);
                android.util.Slog.i(TAG, "Pruning old procstats: " + file);
                new java.io.File(file).delete();
            }
        }
    }

    private boolean dumpFilteredProcessesCsvLocked(java.io.PrintWriter pw, java.lang.String header, boolean sepScreenStates, int[] screenStates, boolean sepMemStates, int[] memStates, boolean sepProcStates, int[] procStates, long now, java.lang.String reqPackage) {
        java.util.ArrayList<com.android.internal.app.procstats.ProcessState> procs = this.mProcessStats.collectProcessesLocked(screenStates, memStates, procStates, procStates, now, reqPackage, false);
        if (procs.size() > 0) {
            if (header != null) {
                pw.println(header);
            }
            com.android.internal.app.procstats.DumpUtils.dumpProcessListCsv(pw, procs, sepScreenStates, screenStates, sepMemStates, memStates, sepProcStates, procStates, now);
            return true;
        }
        return false;
    }

    static int[] parseStateList(java.lang.String[] states, int mult, java.lang.String arg, boolean[] outSep, java.lang.String[] outError) {
        java.util.ArrayList<java.lang.Integer> res = new java.util.ArrayList<>();
        int lastPos = 0;
        int i = 0;
        while (i <= arg.length()) {
            char c = i < arg.length() ? arg.charAt(i) : (char) 0;
            if (c == ',' || c == '+' || c == ' ' || c == 0) {
                boolean isSep = c == ',';
                if (lastPos == 0) {
                    outSep[0] = isSep;
                } else if (c != 0 && outSep[0] != isSep) {
                    outError[0] = "inconsistent separators (can't mix ',' with '+')";
                    return null;
                }
                if (lastPos < i - 1) {
                    java.lang.String str = arg.substring(lastPos, i);
                    int j = 0;
                    while (true) {
                        if (j >= states.length) {
                            break;
                        }
                        if (!str.equals(states[j])) {
                            j++;
                        } else {
                            res.add(java.lang.Integer.valueOf(j));
                            str = null;
                            break;
                        }
                    }
                    if (str != null) {
                        outError[0] = "invalid word \"" + str + "\"";
                        return null;
                    }
                }
                lastPos = i + 1;
            }
            i++;
        }
        int i2 = res.size();
        int[] finalRes = new int[i2];
        for (int i3 = 0; i3 < res.size(); i3++) {
            finalRes[i3] = res.get(i3).intValue() * mult;
        }
        return finalRes;
    }

    static int parseSectionOptions(java.lang.String optionsStr) {
        java.lang.String[] sectionsStr = optionsStr.split(",");
        if (sectionsStr.length == 0) {
            return 31;
        }
        int res = 0;
        java.util.List<java.lang.String> optionStrList = java.util.Arrays.asList(com.android.internal.app.procstats.ProcessStats.OPTIONS_STR);
        for (java.lang.String sectionStr : sectionsStr) {
            int optionIndex = optionStrList.indexOf(sectionStr);
            if (optionIndex != -1) {
                res |= com.android.internal.app.procstats.ProcessStats.OPTIONS[optionIndex];
            }
        }
        return res;
    }

    public byte[] getCurrentStats(java.util.List<android.os.ParcelFileDescriptor> historic) {
        super.getCurrentStats_enforcePermission();
        android.os.Parcel current = android.os.Parcel.obtain();
        synchronized (this.mLock) {
            long now = android.os.SystemClock.uptimeMillis();
            this.mProcessStats.mTimePeriodEndRealtime = android.os.SystemClock.elapsedRealtime();
            this.mProcessStats.mTimePeriodEndUptime = now;
            this.mProcessStats.writeToParcel(current, now, 0);
        }
        this.mFileLock.lock();
        if (historic != null) {
            try {
                java.util.ArrayList<java.lang.String> files = getCommittedFilesLF(0, false, true);
                if (files != null) {
                    for (int i = files.size() - 1; i >= 0; i--) {
                        try {
                            android.os.ParcelFileDescriptor pfd = android.os.ParcelFileDescriptor.open(new java.io.File(files.get(i)), 268435456);
                            historic.add(pfd);
                        } catch (java.io.IOException e) {
                            android.util.Slog.w(TAG, "Failure opening procstat file " + files.get(i), e);
                        }
                    }
                }
            } catch (java.lang.Throwable th) {
                this.mFileLock.unlock();
                throw th;
            }
        }
        this.mFileLock.unlock();
        return current.marshall();
    }

    public long getCommittedStats(long highWaterMarkMs, int section, boolean doAggregate, java.util.List<android.os.ParcelFileDescriptor> committedStats) {
        return getCommittedStatsMerged(highWaterMarkMs, section, doAggregate, committedStats, new com.android.internal.app.procstats.ProcessStats(false));
    }

    public long getCommittedStatsMerged(long highWaterMarkMs, int section, boolean doAggregate, java.util.List<android.os.ParcelFileDescriptor> committedStats, com.android.internal.app.procstats.ProcessStats mergedStats) throws java.lang.Throwable {
        java.util.ArrayList<java.lang.String> files;
        java.lang.String str;
        com.android.internal.app.procstats.ProcessStats stats;
        java.lang.String str2;
        java.lang.String str3 = STATE_FILE_PREFIX;
        java.lang.String str4 = TAG;
        super.getCommittedStatsMerged_enforcePermission();
        long newHighWaterMark = highWaterMarkMs;
        this.mFileLock.lock();
        try {
            files = getCommittedFilesLF(0, false, true);
        } catch (java.io.IOException e) {
            e = e;
        } catch (java.lang.Throwable th) {
            th = th;
            this.mFileLock.unlock();
            throw th;
        }
        if (files == null) {
            this.mFileLock.unlock();
            return newHighWaterMark;
        }
        try {
            try {
                java.lang.String highWaterMarkStr = android.text.format.DateFormat.format("yyyy-MM-dd-HH-mm-ss", highWaterMarkMs).toString();
                int i = files.size() - 1;
                while (i >= 0) {
                    java.lang.String fileName = files.get(i);
                    try {
                        java.lang.String startTimeStr = fileName.substring(fileName.lastIndexOf(str3) + str3.length(), fileName.lastIndexOf(STATE_FILE_SUFFIX));
                        if (startTimeStr.compareToIgnoreCase(highWaterMarkStr) > 0) {
                            android.os.ParcelFileDescriptor pfd = android.os.ParcelFileDescriptor.open(new java.io.File(fileName), 268435456);
                            java.io.InputStream is = new android.os.ParcelFileDescriptor.AutoCloseInputStream(pfd);
                            str = str3;
                            try {
                                stats = new com.android.internal.app.procstats.ProcessStats(false);
                                stats.read(is);
                                is.close();
                                str2 = str4;
                            } catch (java.io.IOException e2) {
                                e = e2;
                            } catch (java.lang.IndexOutOfBoundsException e3) {
                                e = e3;
                            }
                            try {
                                if (stats.mTimePeriodStartClock > newHighWaterMark) {
                                    newHighWaterMark = stats.mTimePeriodStartClock;
                                }
                                if (doAggregate) {
                                    mergedStats.add(stats);
                                } else if (committedStats != null) {
                                    committedStats.add(protoToParcelFileDescriptor(stats, section));
                                }
                                if (stats.mReadError != null) {
                                    str4 = str2;
                                    android.util.Log.w(str4, "Failure reading process stats: " + stats.mReadError);
                                } else {
                                    str4 = str2;
                                }
                            } catch (java.io.IOException e4) {
                                e = e4;
                                str4 = str2;
                                android.util.Slog.w(str4, "Failure opening procstat file " + fileName, e);
                            } catch (java.lang.IndexOutOfBoundsException e5) {
                                e = e5;
                                str4 = str2;
                                android.util.Slog.w(str4, "Failure to read and parse commit file " + fileName, e);
                            }
                        } else {
                            str = str3;
                        }
                    } catch (java.io.IOException e6) {
                        e = e6;
                        str = str3;
                    } catch (java.lang.IndexOutOfBoundsException e7) {
                        e = e7;
                        str = str3;
                    }
                    i--;
                    str3 = str;
                }
                if (doAggregate && committedStats != null) {
                    committedStats.add(protoToParcelFileDescriptor(mergedStats, section));
                }
                this.mFileLock.unlock();
                return newHighWaterMark;
            } catch (java.io.IOException e8) {
                e = e8;
                android.util.Slog.w(str4, "Failure opening procstat file", e);
                this.mFileLock.unlock();
                return newHighWaterMark;
            }
        } catch (java.lang.Throwable th2) {
            th = th2;
            this.mFileLock.unlock();
            throw th;
        }
    }

    public long getMinAssociationDumpDuration() {
        com.android.server.am.ActivityManagerConstants activityManagerConstants = this.mAm.mConstants;
        return com.android.server.am.ActivityManagerConstants.MIN_ASSOC_LOG_DURATION;
    }

    private static android.os.ParcelFileDescriptor protoToParcelFileDescriptor(final com.android.internal.app.procstats.ProcessStats stats, final int section) throws java.io.IOException {
        final android.os.ParcelFileDescriptor[] fds = android.os.ParcelFileDescriptor.createPipe();
        java.lang.Thread thr = new java.lang.Thread("ProcessStats pipe output") { // from class: com.android.server.am.ProcessStatsService.3
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                try {
                    java.io.FileOutputStream fout = new android.os.ParcelFileDescriptor.AutoCloseOutputStream(fds[1]);
                    android.util.proto.ProtoOutputStream proto = new android.util.proto.ProtoOutputStream(fout);
                    stats.dumpDebug(proto, stats.mTimePeriodEndRealtime, section);
                    proto.flush();
                    fout.close();
                } catch (java.io.IOException e) {
                    android.util.Slog.w(com.android.server.am.ProcessStatsService.TAG, "Failure writing pipe", e);
                }
            }
        };
        thr.start();
        return fds[0];
    }

    public android.os.ParcelFileDescriptor getStatsOverTime(long minTime) {
        long curTime;
        super.getStatsOverTime_enforcePermission();
        android.os.Parcel current = android.os.Parcel.obtain();
        synchronized (this.mLock) {
            long now = android.os.SystemClock.uptimeMillis();
            this.mProcessStats.mTimePeriodEndRealtime = android.os.SystemClock.elapsedRealtime();
            this.mProcessStats.mTimePeriodEndUptime = now;
            this.mProcessStats.writeToParcel(current, now, 0);
            curTime = this.mProcessStats.mTimePeriodEndRealtime - this.mProcessStats.mTimePeriodStartRealtime;
        }
        this.mFileLock.lock();
        try {
            if (curTime < minTime) {
                try {
                    java.util.ArrayList<java.lang.String> files = getCommittedFilesLF(0, false, true);
                    if (files != null && files.size() > 0) {
                        current.setDataPosition(0);
                        com.android.internal.app.procstats.ProcessStats stats = (com.android.internal.app.procstats.ProcessStats) com.android.internal.app.procstats.ProcessStats.CREATOR.createFromParcel(current);
                        current.recycle();
                        int i = files.size() - 1;
                        while (i >= 0 && stats.mTimePeriodEndRealtime - stats.mTimePeriodStartRealtime < minTime) {
                            android.util.AtomicFile file = new android.util.AtomicFile(new java.io.File(files.get(i)));
                            i--;
                            com.android.internal.app.procstats.ProcessStats moreStats = new com.android.internal.app.procstats.ProcessStats(false);
                            readLF(moreStats, file);
                            if (moreStats.mReadError == null) {
                                stats.add(moreStats);
                                java.lang.StringBuilder sb = new java.lang.StringBuilder();
                                sb.append("Added stats: ");
                                sb.append(moreStats.mTimePeriodStartClockStr);
                                sb.append(", over ");
                                android.util.TimeUtils.formatDuration(moreStats.mTimePeriodEndRealtime - moreStats.mTimePeriodStartRealtime, sb);
                                android.util.Slog.i(TAG, sb.toString());
                            } else {
                                android.util.Slog.w(TAG, "Failure reading " + files.get(i + 1) + "; " + moreStats.mReadError);
                            }
                        }
                        current = android.os.Parcel.obtain();
                        stats.writeToParcel(current, 0);
                    }
                } catch (java.io.IOException e) {
                    android.util.Slog.w(TAG, "Failed building output pipe", e);
                    this.mFileLock.unlock();
                    return null;
                }
            }
            final byte[] outData = current.marshall();
            current.recycle();
            final android.os.ParcelFileDescriptor[] fds = android.os.ParcelFileDescriptor.createPipe();
            java.lang.Thread thr = new java.lang.Thread("ProcessStats pipe output") { // from class: com.android.server.am.ProcessStatsService.4
                @Override // java.lang.Thread, java.lang.Runnable
                public void run() {
                    java.io.FileOutputStream fout = new android.os.ParcelFileDescriptor.AutoCloseOutputStream(fds[1]);
                    try {
                        fout.write(outData);
                        fout.close();
                    } catch (java.io.IOException e2) {
                        android.util.Slog.w(com.android.server.am.ProcessStatsService.TAG, "Failure writing pipe", e2);
                    }
                }
            };
            thr.start();
            return fds[0];
        } finally {
            this.mFileLock.unlock();
        }
    }

    public int getCurrentMemoryState() {
        int i;
        synchronized (this.mLock) {
            i = this.mLastMemOnlyState;
        }
        return i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.util.SparseArray<long[]> getUidProcStateStatsOverTime(long minTime) {
        long curTime;
        com.android.internal.app.procstats.ProcessStats stats = new com.android.internal.app.procstats.ProcessStats();
        synchronized (this.mLock) {
            long now = android.os.SystemClock.uptimeMillis();
            this.mProcessStats.mTimePeriodEndRealtime = android.os.SystemClock.elapsedRealtime();
            this.mProcessStats.mTimePeriodEndUptime = now;
            stats.add(this.mProcessStats);
            curTime = this.mProcessStats.mTimePeriodEndRealtime - this.mProcessStats.mTimePeriodStartRealtime;
        }
        if (curTime < minTime) {
            try {
                this.mFileLock.lock();
                java.util.ArrayList<java.lang.String> files = getCommittedFilesLF(0, false, true);
                if (files != null && files.size() > 0) {
                    int i = files.size() - 1;
                    while (i >= 0) {
                        if (stats.mTimePeriodEndRealtime - stats.mTimePeriodStartRealtime >= minTime) {
                            break;
                        }
                        android.util.AtomicFile file = new android.util.AtomicFile(new java.io.File(files.get(i)));
                        i--;
                        com.android.internal.app.procstats.ProcessStats moreStats = new com.android.internal.app.procstats.ProcessStats(false);
                        readLF(moreStats, file);
                        if (moreStats.mReadError == null) {
                            stats.add(moreStats);
                        } else {
                            android.util.Slog.w(TAG, "Failure reading " + files.get(i + 1) + "; " + moreStats.mReadError);
                        }
                    }
                }
            } finally {
                this.mFileLock.unlock();
            }
        }
        android.util.SparseArray<com.android.internal.app.procstats.UidState> uidStates = stats.mUidStates;
        android.util.SparseArray<long[]> results = new android.util.SparseArray<>();
        int size = uidStates.size();
        for (int i2 = 0; i2 < size; i2++) {
            int uid = uidStates.keyAt(i2);
            com.android.internal.app.procstats.UidState uidState = uidStates.valueAt(i2);
            results.put(uid, uidState.getAggregatedDurationsInStates());
        }
        return results;
    }

    void publish() {
        com.android.server.LocalServices.addService(com.android.internal.app.procstats.ProcessStatsInternal.class, new com.android.server.am.ProcessStatsService.LocalService());
    }

    private final class LocalService extends com.android.internal.app.procstats.ProcessStatsInternal {
        private LocalService() {
        }

        public android.util.SparseArray<long[]> getUidProcStateStatsOverTime(long minTime) {
            return com.android.server.am.ProcessStatsService.this.getUidProcStateStatsOverTime(minTime);
        }
    }

    private void dumpAggregatedStats(java.io.PrintWriter pw, long aggregateHours, long now, java.lang.String reqPackage, boolean isCompact, boolean dumpDetails, boolean dumpFullDetails, boolean dumpAll, boolean activeOnly, int section) {
        android.os.ParcelFileDescriptor pfd = getStatsOverTime((((aggregateHours * 60) * 60) * 1000) - (com.android.internal.app.procstats.ProcessStats.COMMIT_PERIOD / 2));
        if (pfd == null) {
            pw.println("Unable to build stats!");
            return;
        }
        com.android.internal.app.procstats.ProcessStats stats = new com.android.internal.app.procstats.ProcessStats(false);
        java.io.InputStream stream = new android.os.ParcelFileDescriptor.AutoCloseInputStream(pfd);
        stats.read(stream);
        if (stats.mReadError != null) {
            pw.print("Failure reading: ");
            pw.println(stats.mReadError);
        } else if (isCompact) {
            stats.dumpCheckinLocked(pw, reqPackage, section);
        } else if (dumpDetails || dumpFullDetails) {
            stats.dumpLocked(pw, reqPackage, now, !dumpFullDetails, dumpDetails, dumpAll, activeOnly, section);
        } else {
            stats.dumpSummaryLocked(pw, reqPackage, now, activeOnly);
        }
    }

    private static void dumpHelp(java.io.PrintWriter pw) {
        pw.println("Process stats (procstats) dump options:");
        pw.println("    [--checkin|-c|--csv] [--csv-screen] [--csv-proc] [--csv-mem]");
        pw.println("    [--details] [--full-details] [--current] [--hours N] [--last N]");
        pw.println("    [--max N] --active] [--commit] [--reset] [--clear] [--write] [-h]");
        pw.println("    [--start-testing] [--stop-testing] ");
        pw.println("    [--pretend-screen-on] [--pretend-screen-off] [--stop-pretend-screen]");
        pw.println("    [<package.name>]");
        pw.println("  --checkin: perform a checkin: print and delete old committed states.");
        pw.println("  -c: print only state in checkin format.");
        pw.println("  --csv: output data suitable for putting in a spreadsheet.");
        pw.println("  --csv-screen: on, off.");
        pw.println("  --csv-mem: norm, mod, low, crit.");
        pw.println("  --csv-proc: pers, top, fore, vis, precept, backup,");
        pw.println("    service, home, prev, cached");
        pw.println("  --details: dump per-package details, not just summary.");
        pw.println("  --full-details: dump all timing and active state details.");
        pw.println("  --current: only dump current state.");
        pw.println("  --hours: aggregate over about N last hours.");
        pw.println("  --last: only show the last committed stats at index N (starting at 1).");
        pw.println("  --max: for -a, max num of historical batches to print.");
        pw.println("  --active: only show currently active processes/services.");
        pw.println("  --commit: commit current stats to disk and reset to start new stats.");
        pw.println("  --section: proc|pkg-proc|pkg-svc|pkg-asc|pkg-all|all ");
        pw.println("    options can be combined to select desired stats");
        pw.println("  --reset: reset current stats, without committing.");
        pw.println("  --clear: clear all stats; does both --reset and deletes old stats.");
        pw.println("  --write: write current in-memory stats to disk.");
        pw.println("  --read: replace current stats with last-written stats.");
        pw.println("  --start-testing: clear all stats and starting high frequency pss sampling.");
        pw.println("  --stop-testing: stop high frequency pss sampling.");
        pw.println("  --pretend-screen-on: pretend screen is on.");
        pw.println("  --pretend-screen-off: pretend screen is off.");
        pw.println("  --stop-pretend-screen: forget \"pretend screen\" and use the real state.");
        pw.println("  -a: print everything.");
        pw.println("  -h: print this help text.");
        pw.println("  <package.name>: optional name of package to filter output by.");
    }

    protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
        if (com.android.internal.util.DumpUtils.checkDumpAndUsageStatsPermission(this.mAm.mContext, TAG, pw)) {
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                if (args.length > 0) {
                    if ("--proto".equals(args[0])) {
                        dumpProto(fd);
                        return;
                    } else if ("--statsd".equals(args[0])) {
                        dumpProtoForStatsd(fd);
                        return;
                    }
                }
                dumpInner(pw, args);
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:337:0x0790  */
    /* JADX WARN: Removed duplicated region for block: B:339:0x07b8 A[Catch: all -> 0x07dc, TRY_LEAVE, TryCatch #27 {all -> 0x07dc, blocks: (B:338:0x07b4, B:339:0x07b8), top: B:468:0x07b4 }] */
    /* JADX WARN: Removed duplicated region for block: B:349:0x080c A[Catch: all -> 0x0833, TRY_LEAVE, TryCatch #9 {all -> 0x0833, blocks: (B:347:0x0807, B:349:0x080c), top: B:433:0x0807 }] */
    /* JADX WARN: Removed duplicated region for block: B:351:0x082e  */
    /* JADX WARN: Removed duplicated region for block: B:370:0x087c  */
    /* JADX WARN: Removed duplicated region for block: B:413:0x0938  */
    @dalvik.annotation.optimization.NeverCompile
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void dumpInner(java.io.PrintWriter r46, java.lang.String[] r47) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 2367
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.ProcessStatsService.dumpInner(java.io.PrintWriter, java.lang.String[]):void");
    }

    private void dumpAggregatedStats(android.util.proto.ProtoOutputStream proto, long fieldId, int aggregateHours, long now) {
        android.os.ParcelFileDescriptor pfd = getStatsOverTime(((long) (((aggregateHours * 60) * 60) * 1000)) - (com.android.internal.app.procstats.ProcessStats.COMMIT_PERIOD / 2));
        if (pfd == null) {
            return;
        }
        com.android.internal.app.procstats.ProcessStats stats = new com.android.internal.app.procstats.ProcessStats(false);
        java.io.InputStream stream = new android.os.ParcelFileDescriptor.AutoCloseInputStream(pfd);
        stats.read(stream);
        if (stats.mReadError != null) {
            return;
        }
        long token = proto.start(fieldId);
        stats.dumpDebug(proto, now, 31);
        proto.end(token);
    }

    private void dumpProto(java.io.FileDescriptor fd) {
        long now;
        android.util.proto.ProtoOutputStream proto = new android.util.proto.ProtoOutputStream(fd);
        synchronized (this.mLock) {
            now = android.os.SystemClock.uptimeMillis();
            long token = proto.start(1146756268033L);
            this.mProcessStats.dumpDebug(proto, now, 31);
            proto.end(token);
        }
        dumpAggregatedStats(proto, 1146756268034L, 3, now);
        dumpAggregatedStats(proto, 1146756268035L, 24, now);
        proto.flush();
    }

    private void dumpProtoForStatsd(java.io.FileDescriptor fd) throws java.lang.Throwable {
        android.util.proto.ProtoOutputStream[] protos = {new android.util.proto.ProtoOutputStream(fd)};
        com.android.internal.app.procstats.ProcessStats procStats = new com.android.internal.app.procstats.ProcessStats(false);
        getCommittedStatsMerged(0L, 0, true, null, procStats);
        procStats.dumpAggregatedProtoForStatsd(protos, 999999L);
        protos[0].flush();
    }
}
