package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public final class PhantomProcessRecord {
    static final java.lang.String TAG = "ActivityManager";
    long mCurrentCputime;
    final android.os.Handler mKillHandler;
    long mLastCputime;
    final java.lang.Object mLock;
    final java.util.function.Consumer<com.android.server.am.PhantomProcessRecord> mOnKillListener;
    final int mPid;
    final java.io.FileDescriptor mPidFd;
    final int mPpid;
    final java.lang.String mProcessName;
    final com.android.server.am.ActivityManagerService mService;
    java.lang.String mStringName;
    final int mUid;
    int mUpdateSeq;
    boolean mZombie;
    static final long[] LONG_OUT = new long[1];
    static final int[] LONG_FORMAT = {8202};
    private java.lang.Runnable mProcKillTimer = new java.lang.Runnable() { // from class: com.android.server.am.PhantomProcessRecord.1
        @Override // java.lang.Runnable
        public void run() {
            synchronized (com.android.server.am.PhantomProcessRecord.this.mLock) {
                android.util.Slog.w("ActivityManager", "Process " + toString() + " is still alive after " + com.android.server.am.PhantomProcessRecord.this.mService.mConstants.mProcessKillTimeoutMs + "ms");
                com.android.server.am.PhantomProcessRecord.this.mZombie = true;
                com.android.server.am.PhantomProcessRecord.this.onProcDied(false);
            }
        }
    };
    boolean mKilled = false;
    int mAdj = -1000;
    final long mKnownSince = android.os.SystemClock.elapsedRealtime();

    PhantomProcessRecord(java.lang.String processName, int uid, int pid, int ppid, com.android.server.am.ActivityManagerService service, java.util.function.Consumer<com.android.server.am.PhantomProcessRecord> onKillListener) throws java.lang.IllegalStateException {
        this.mProcessName = processName;
        this.mUid = uid;
        this.mPid = pid;
        this.mPpid = ppid;
        this.mService = service;
        this.mLock = service.mPhantomProcessList.mLock;
        this.mOnKillListener = onKillListener;
        com.android.server.am.ProcessList processList = service.mProcessList;
        this.mKillHandler = com.android.server.am.ProcessList.sKillHandler;
        if (android.os.Process.supportsPidFd()) {
            android.os.StrictMode.ThreadPolicy oldPolicy = android.os.StrictMode.allowThreadDiskReads();
            try {
                try {
                    this.mPidFd = android.os.Process.openPidFd(pid, 0);
                    if (this.mPidFd == null) {
                        throw new java.lang.IllegalStateException();
                    }
                    return;
                } catch (java.io.IOException e) {
                    android.util.Slog.w("ActivityManager", "Unable to open process " + pid + ", it might be gone");
                    java.lang.IllegalStateException ex = new java.lang.IllegalStateException();
                    ex.initCause(e);
                    throw ex;
                }
            } finally {
                android.os.StrictMode.setThreadPolicy(oldPolicy);
            }
            android.os.StrictMode.setThreadPolicy(oldPolicy);
        }
        this.mPidFd = null;
    }

    public long getRss(int pid) {
        long[] rss = android.os.Process.getRss(pid);
        if (rss == null || rss.length <= 0) {
            return 0L;
        }
        return rss[0];
    }

    void killLocked(java.lang.String reason, boolean noisy) {
        if (!this.mKilled) {
            android.os.Trace.traceBegin(64L, "kill");
            if (noisy || this.mUid == this.mService.mCurOomAdjUid) {
                this.mService.reportUidInfoMessageLocked("ActivityManager", "Killing " + toString() + ": " + reason, this.mUid);
            }
            if (android.os.Process.getUidForPid(this.mPid) != this.mUid) {
                return;
            }
            if (this.mPid > 0) {
                android.util.EventLog.writeEvent(com.android.server.am.EventLogTags.AM_KILL, java.lang.Integer.valueOf(android.os.UserHandle.getUserId(this.mUid)), java.lang.Integer.valueOf(this.mPid), this.mProcessName, java.lang.Integer.valueOf(this.mAdj), reason, java.lang.Long.valueOf(getRss(this.mPid)));
                if (!android.os.Process.supportsPidFd()) {
                    onProcDied(false);
                } else {
                    this.mKillHandler.postDelayed(this.mProcKillTimer, this, this.mService.mConstants.mProcessKillTimeoutMs);
                }
                android.os.Process.killProcessQuiet(this.mPid);
                com.android.server.am.ProcessList.killProcessGroup(this.mUid, this.mPid);
            }
            this.mKilled = true;
            android.os.Trace.traceEnd(64L);
        }
    }

    void updateAdjLocked() {
        if (android.os.Process.readProcFile("/proc/" + this.mPid + "/oom_score_adj", LONG_FORMAT, null, LONG_OUT, null)) {
            this.mAdj = (int) LONG_OUT[0];
        }
    }

    void onProcDied(boolean reallyDead) {
        if (reallyDead) {
            android.util.Slog.i("ActivityManager", "Process " + toString() + " died");
        }
        this.mKillHandler.removeCallbacks(this.mProcKillTimer, this);
        if (this.mOnKillListener != null) {
            this.mOnKillListener.accept(this);
        }
    }

    public java.lang.String toString() {
        if (this.mStringName != null) {
            return this.mStringName;
        }
        java.lang.StringBuilder sb = new java.lang.StringBuilder(128);
        sb.append("PhantomProcessRecord {");
        sb.append(java.lang.Integer.toHexString(java.lang.System.identityHashCode(this)));
        sb.append(' ');
        sb.append(this.mPid);
        sb.append(':');
        sb.append(this.mPpid);
        sb.append(':');
        sb.append(this.mProcessName);
        sb.append('/');
        if (this.mUid < 10000) {
            sb.append(this.mUid);
        } else {
            sb.append('u');
            sb.append(android.os.UserHandle.getUserId(this.mUid));
            int appId = android.os.UserHandle.getAppId(this.mUid);
            if (appId >= 10000) {
                sb.append('a');
                sb.append(appId - 10000);
            } else {
                sb.append('s');
                sb.append(appId);
            }
            if (appId >= 99000 && appId <= 99999) {
                sb.append('i');
                sb.append(appId - 99000);
            }
        }
        sb.append('}');
        java.lang.String string = sb.toString();
        this.mStringName = string;
        return string;
    }

    void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        long now = android.os.SystemClock.elapsedRealtime();
        pw.print(prefix);
        pw.print("user #");
        pw.print(android.os.UserHandle.getUserId(this.mUid));
        pw.print(" uid=");
        pw.print(this.mUid);
        pw.print(" pid=");
        pw.print(this.mPid);
        pw.print(" ppid=");
        pw.print(this.mPpid);
        pw.print(" knownSince=");
        android.util.TimeUtils.formatDuration(this.mKnownSince, now, pw);
        pw.print(" killed=");
        pw.println(this.mKilled);
        pw.print(prefix);
        pw.print("lastCpuTime=");
        pw.print(this.mLastCputime);
        if (this.mLastCputime > 0) {
            pw.print(" timeUsed=");
            android.util.TimeUtils.formatDuration(this.mCurrentCputime - this.mLastCputime, pw);
        }
        pw.print(" oom adj=");
        pw.print(this.mAdj);
        pw.print(" seq=");
        pw.println(this.mUpdateSeq);
    }

    boolean equals(java.lang.String processName, int uid, int pid) {
        return this.mUid == uid && this.mPid == pid && android.text.TextUtils.equals(this.mProcessName, processName);
    }
}
