package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class VisibleActivityProcessTracker {
    final com.android.server.wm.ActivityTaskManagerService mAtms;
    private final android.util.ArrayMap<com.android.server.wm.WindowProcessController, com.android.server.wm.VisibleActivityProcessTracker.CpuTimeRecord> mProcMap = new android.util.ArrayMap<>();
    final java.util.concurrent.Executor mBgExecutor = com.android.internal.os.BackgroundThread.getExecutor();

    VisibleActivityProcessTracker(com.android.server.wm.ActivityTaskManagerService atms) {
        this.mAtms = atms;
    }

    void onAnyActivityVisible(com.android.server.wm.WindowProcessController wpc) {
        com.android.server.wm.VisibleActivityProcessTracker.CpuTimeRecord r = new com.android.server.wm.VisibleActivityProcessTracker.CpuTimeRecord(wpc);
        synchronized (this.mProcMap) {
            this.mProcMap.put(wpc, r);
        }
        if (wpc.hasResumedActivity()) {
            r.mShouldGetCpuTime = true;
            this.mBgExecutor.execute(r);
        }
    }

    void onAllActivitiesInvisible(com.android.server.wm.WindowProcessController wpc) {
        com.android.server.wm.VisibleActivityProcessTracker.CpuTimeRecord r = removeProcess(wpc);
        if (r != null && r.mShouldGetCpuTime) {
            this.mBgExecutor.execute(r);
        }
    }

    void onActivityResumedWhileVisible(com.android.server.wm.WindowProcessController wpc) {
        com.android.server.wm.VisibleActivityProcessTracker.CpuTimeRecord r;
        synchronized (this.mProcMap) {
            r = this.mProcMap.get(wpc);
        }
        if (r != null && !r.mShouldGetCpuTime) {
            r.mShouldGetCpuTime = true;
            this.mBgExecutor.execute(r);
        }
    }

    boolean hasResumedActivity(int uid) {
        return match(uid, new java.util.function.Predicate() { // from class: com.android.server.wm.VisibleActivityProcessTracker$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.wm.WindowProcessController) obj).hasResumedActivity();
            }
        });
    }

    boolean hasVisibleActivity(int uid) {
        return match(uid, null);
    }

    private boolean match(int uid, java.util.function.Predicate<com.android.server.wm.WindowProcessController> predicate) {
        synchronized (this.mProcMap) {
            for (int i = this.mProcMap.size() - 1; i >= 0; i--) {
                com.android.server.wm.WindowProcessController wpc = this.mProcMap.keyAt(i);
                if (wpc.mUid == uid && (predicate == null || predicate.test(wpc))) {
                    return true;
                }
            }
            return false;
        }
    }

    com.android.server.wm.VisibleActivityProcessTracker.CpuTimeRecord removeProcess(com.android.server.wm.WindowProcessController wpc) {
        com.android.server.wm.VisibleActivityProcessTracker.CpuTimeRecord cpuTimeRecordRemove;
        synchronized (this.mProcMap) {
            cpuTimeRecordRemove = this.mProcMap.remove(wpc);
        }
        return cpuTimeRecordRemove;
    }

    void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        pw.print(prefix + "VisibleActivityProcess:[");
        synchronized (this.mProcMap) {
            for (int i = this.mProcMap.size() - 1; i >= 0; i--) {
                pw.print(" " + this.mProcMap.keyAt(i));
            }
        }
        pw.println("]");
    }

    /* JADX INFO: Access modifiers changed from: private */
    class CpuTimeRecord implements java.lang.Runnable {
        private long mCpuTime;
        private boolean mHasStartCpuTime;
        private final com.android.server.wm.WindowProcessController mProc;
        boolean mShouldGetCpuTime;

        CpuTimeRecord(com.android.server.wm.WindowProcessController wpc) {
            this.mProc = wpc;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (this.mProc.getPid() == 0) {
                return;
            }
            if (!this.mHasStartCpuTime) {
                this.mHasStartCpuTime = true;
                this.mCpuTime = this.mProc.getCpuTime();
            } else {
                long diff = this.mProc.getCpuTime() - this.mCpuTime;
                if (diff > 0) {
                    com.android.server.wm.VisibleActivityProcessTracker.this.mAtms.mAmInternal.updateForegroundTimeIfOnBattery(this.mProc.mInfo.packageName, this.mProc.mInfo.uid, diff);
                }
            }
        }
    }
}
