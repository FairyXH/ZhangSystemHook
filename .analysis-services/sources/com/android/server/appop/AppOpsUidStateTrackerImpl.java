package com.android.server.appop;

/* JADX INFO: loaded from: classes.dex */
class AppOpsUidStateTrackerImpl implements com.android.server.appop.AppOpsUidStateTracker {
    private static final java.lang.String LOG_TAG = com.android.server.appop.AppOpsUidStateTrackerImpl.class.getSimpleName();
    private android.app.ActivityManagerInternal mActivityManagerInternal;
    private android.util.SparseBooleanArray mAppWidgetVisible;
    private android.util.SparseIntArray mCapability;
    private final com.android.internal.os.Clock mClock;
    private com.android.server.appop.AppOpsService.Constants mConstants;
    private final com.android.server.appop.AppOpsUidStateTrackerImpl.EventLog mEventLog;
    private final com.android.server.appop.AppOpsUidStateTrackerImpl.DelayableExecutor mExecutor;
    private android.util.SparseBooleanArray mPendingAppWidgetVisible;
    private android.util.SparseIntArray mPendingCapability;
    private android.util.SparseLongArray mPendingCommitTime;
    private android.util.SparseBooleanArray mPendingGone;
    private android.util.SparseIntArray mPendingUidStates;
    private android.util.ArrayMap<com.android.server.appop.AppOpsUidStateTracker.UidStateChangedCallback, java.util.concurrent.Executor> mUidStateChangedCallbacks;
    private android.util.SparseIntArray mUidStates;

    interface DelayableExecutor extends java.util.concurrent.Executor {
        @Override // java.util.concurrent.Executor
        void execute(java.lang.Runnable runnable);

        void executeDelayed(java.lang.Runnable runnable, long j);
    }

    /* JADX INFO: renamed from: com.android.server.appop.AppOpsUidStateTrackerImpl$1, reason: invalid class name */
    class AnonymousClass1 implements com.android.server.appop.AppOpsUidStateTrackerImpl.DelayableExecutor {
        final /* synthetic */ android.os.Handler val$handler;
        final /* synthetic */ java.util.concurrent.Executor val$lockingExecutor;

        AnonymousClass1(android.os.Handler handler, java.util.concurrent.Executor executor) {
            this.val$handler = handler;
            this.val$lockingExecutor = executor;
        }

        @Override // com.android.server.appop.AppOpsUidStateTrackerImpl.DelayableExecutor, java.util.concurrent.Executor
        public void execute(final java.lang.Runnable runnable) {
            android.os.Handler handler = this.val$handler;
            final java.util.concurrent.Executor executor = this.val$lockingExecutor;
            handler.post(new java.lang.Runnable() { // from class: com.android.server.appop.AppOpsUidStateTrackerImpl$1$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    executor.execute(runnable);
                }
            });
        }

        @Override // com.android.server.appop.AppOpsUidStateTrackerImpl.DelayableExecutor
        public void executeDelayed(final java.lang.Runnable runnable, long delay) {
            android.os.Handler handler = this.val$handler;
            final java.util.concurrent.Executor executor = this.val$lockingExecutor;
            handler.postDelayed(new java.lang.Runnable() { // from class: com.android.server.appop.AppOpsUidStateTrackerImpl$1$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    executor.execute(runnable);
                }
            }, delay);
        }
    }

    AppOpsUidStateTrackerImpl(android.app.ActivityManagerInternal activityManagerInternal, android.os.Handler handler, java.util.concurrent.Executor lockingExecutor, com.android.internal.os.Clock clock, com.android.server.appop.AppOpsService.Constants constants) {
        this(activityManagerInternal, new com.android.server.appop.AppOpsUidStateTrackerImpl.AnonymousClass1(handler, lockingExecutor), clock, constants, handler.getLooper().getThread());
    }

    AppOpsUidStateTrackerImpl(android.app.ActivityManagerInternal activityManagerInternal, com.android.server.appop.AppOpsUidStateTrackerImpl.DelayableExecutor executor, com.android.internal.os.Clock clock, com.android.server.appop.AppOpsService.Constants constants, java.lang.Thread executorThread) {
        this.mUidStates = new android.util.SparseIntArray();
        this.mPendingUidStates = new android.util.SparseIntArray();
        this.mCapability = new android.util.SparseIntArray();
        this.mPendingCapability = new android.util.SparseIntArray();
        this.mAppWidgetVisible = new android.util.SparseBooleanArray();
        this.mPendingAppWidgetVisible = new android.util.SparseBooleanArray();
        this.mPendingCommitTime = new android.util.SparseLongArray();
        this.mPendingGone = new android.util.SparseBooleanArray();
        this.mUidStateChangedCallbacks = new android.util.ArrayMap<>();
        this.mActivityManagerInternal = activityManagerInternal;
        this.mExecutor = executor;
        this.mClock = clock;
        this.mConstants = constants;
        this.mEventLog = new com.android.server.appop.AppOpsUidStateTrackerImpl.EventLog(executor, executorThread);
    }

    @Override // com.android.server.appop.AppOpsUidStateTracker
    public int getUidState(int uid) {
        return getUidStateLocked(uid);
    }

    private int getUidStateLocked(int uid) {
        updateUidPendingStateIfNeeded(uid);
        return this.mUidStates.get(uid, com.android.server.am.ProcessList.PREVIOUS_APP_ADJ);
    }

    @Override // com.android.server.appop.AppOpsUidStateTracker
    public int evalMode(int uid, int code, int mode) {
        if (mode != 4) {
            return mode;
        }
        int uidState = getUidState(uid);
        int uidCapability = getUidCapability(uid);
        int result = evalModeInternal(uid, code, uidState, uidCapability);
        this.mEventLog.logEvalForegroundMode(uid, uidState, uidCapability, code, result);
        return result;
    }

    private int evalModeInternal(int uid, int code, int uidState, int uidCapability) {
        if (getUidAppWidgetVisible(uid) || this.mActivityManagerInternal.isPendingTopUid(uid) || this.mActivityManagerInternal.isTempAllowlistedForFgsWhileInUse(uid)) {
            return 0;
        }
        int opCapability = getOpCapability(code);
        return opCapability != 0 ? (uidCapability & opCapability) == 0 ? 1 : 0 : uidState > android.app.AppOpsManager.resolveFirstUnrestrictedUidState(code) ? 1 : 0;
    }

    private int getOpCapability(int opCode) {
        switch (opCode) {
            case 0:
            case 1:
            case 41:
            case 42:
                return 1;
            case 26:
                return 2;
            case 27:
            case 121:
                return 4;
            case 32:
                return 64;
            default:
                return 0;
        }
    }

    @Override // com.android.server.appop.AppOpsUidStateTracker
    public boolean isUidInForeground(int uid) {
        return evalMode(uid, -1, 4) == 0;
    }

    @Override // com.android.server.appop.AppOpsUidStateTracker
    public void addUidStateChangedCallback(java.util.concurrent.Executor executor, com.android.server.appop.AppOpsUidStateTracker.UidStateChangedCallback callback) {
        if (this.mUidStateChangedCallbacks.containsKey(callback)) {
            throw new java.lang.IllegalStateException("Callback is already registered.");
        }
        this.mUidStateChangedCallbacks.put(callback, executor);
    }

    @Override // com.android.server.appop.AppOpsUidStateTracker
    public void removeUidStateChangedCallback(com.android.server.appop.AppOpsUidStateTracker.UidStateChangedCallback callback) {
        if (!this.mUidStateChangedCallbacks.containsKey(callback)) {
            throw new java.lang.IllegalStateException("Callback is not registered.");
        }
        this.mUidStateChangedCallbacks.remove(callback);
    }

    @Override // com.android.server.appop.AppOpsUidStateTracker
    public void updateAppWidgetVisibility(android.util.SparseArray<java.lang.String> uidPackageNames, boolean visible) {
        int numUids = uidPackageNames.size();
        for (int i = 0; i < numUids; i++) {
            int uid = uidPackageNames.keyAt(i);
            this.mPendingAppWidgetVisible.put(uid, visible);
            commitUidPendingState(uid);
        }
    }

    @Override // com.android.server.appop.AppOpsUidStateTracker
    public void updateUidProcState(int uid, int procState, int capability) {
        long settleTime;
        int uidState = com.android.server.appop.AppOpsUidStateTracker.processStateToUidState(procState);
        int prevUidState = this.mUidStates.get(uid, com.android.server.am.ProcessList.PREVIOUS_APP_ADJ);
        int prevCapability = this.mCapability.get(uid, 0);
        int pendingUidState = this.mPendingUidStates.get(uid, com.android.server.am.ProcessList.PREVIOUS_APP_ADJ);
        int pendingCapability = this.mPendingCapability.get(uid, 0);
        long pendingStateCommitTime = this.mPendingCommitTime.get(uid, 0L);
        if (pendingStateCommitTime != 0 || (uidState == prevUidState && capability == prevCapability)) {
            if (pendingStateCommitTime == 0) {
                return;
            }
            if (uidState == pendingUidState && capability == pendingCapability) {
                return;
            }
        }
        this.mEventLog.logUpdateUidProcState(uid, procState, capability);
        this.mPendingUidStates.put(uid, uidState);
        this.mPendingCapability.put(uid, capability);
        if (procState == 20) {
            this.mPendingGone.put(uid, true);
            commitUidPendingState(uid);
            return;
        }
        if (uidState < prevUidState || (uidState <= 500 && prevUidState > 500)) {
            commitUidPendingState(uid);
            return;
        }
        if (uidState == prevUidState && capability != prevCapability) {
            commitUidPendingState(uid);
            return;
        }
        if (uidState <= 500) {
            commitUidPendingState(uid);
            return;
        }
        if (pendingStateCommitTime == 0) {
            if (prevUidState <= 200) {
                settleTime = this.mConstants.TOP_STATE_SETTLE_TIME;
            } else if (prevUidState <= 400) {
                settleTime = this.mConstants.FG_SERVICE_STATE_SETTLE_TIME;
            } else {
                settleTime = this.mConstants.BG_STATE_SETTLE_TIME;
            }
            long commitTime = this.mClock.elapsedRealtime() + settleTime;
            this.mPendingCommitTime.put(uid, commitTime);
            this.mExecutor.executeDelayed(com.android.internal.util.function.pooled.PooledLambda.obtainRunnable(new java.util.function.BiConsumer() { // from class: com.android.server.appop.AppOpsUidStateTrackerImpl$$ExternalSyntheticLambda0
                @Override // java.util.function.BiConsumer
                public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                    ((com.android.server.appop.AppOpsUidStateTrackerImpl) obj).updateUidPendingStateIfNeeded(((java.lang.Integer) obj2).intValue());
                }
            }, this, java.lang.Integer.valueOf(uid)), settleTime + 1);
        }
    }

    @Override // com.android.server.appop.AppOpsUidStateTracker
    public void dumpUidState(java.io.PrintWriter pw, int uid, long nowElapsed) {
        int state = this.mUidStates.get(uid, com.android.server.am.ProcessList.PREVIOUS_APP_ADJ);
        int pendingState = this.mPendingUidStates.get(uid, state);
        pw.print("    state=");
        pw.println(android.app.AppOpsManager.getUidStateName(state));
        if (state != pendingState) {
            pw.print("    pendingState=");
            pw.println(android.app.AppOpsManager.getUidStateName(pendingState));
        }
        int capability = this.mCapability.get(uid, 0);
        int pendingCapability = this.mPendingCapability.get(uid, capability);
        pw.print("    capability=");
        android.app.ActivityManager.printCapabilitiesFull(pw, capability);
        pw.println();
        if (capability != pendingCapability) {
            pw.print("    pendingCapability=");
            android.app.ActivityManager.printCapabilitiesFull(pw, pendingCapability);
            pw.println();
        }
        boolean appWidgetVisible = this.mAppWidgetVisible.get(uid, false);
        boolean pendingAppWidgetVisible = this.mPendingAppWidgetVisible.get(uid, appWidgetVisible);
        pw.print("    appWidgetVisible=");
        pw.println(appWidgetVisible);
        if (appWidgetVisible != pendingAppWidgetVisible) {
            pw.print("    pendingAppWidgetVisible=");
            pw.println(pendingAppWidgetVisible);
        }
        long pendingStateCommitTime = this.mPendingCommitTime.get(uid, 0L);
        if (pendingStateCommitTime != 0) {
            pw.print("    pendingStateCommitTime=");
            android.util.TimeUtils.formatDuration(pendingStateCommitTime, nowElapsed, pw);
            pw.println();
        }
    }

    @Override // com.android.server.appop.AppOpsUidStateTracker
    public void dumpEvents(java.io.PrintWriter pw) {
        this.mEventLog.dumpEvents(pw);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateUidPendingStateIfNeeded(int uid) {
        updateUidPendingStateIfNeededLocked(uid);
    }

    private void updateUidPendingStateIfNeededLocked(int uid) {
        long pendingCommitTime = this.mPendingCommitTime.get(uid, 0L);
        if (pendingCommitTime != 0) {
            long currentTime = this.mClock.elapsedRealtime();
            if (currentTime < this.mPendingCommitTime.get(uid)) {
                return;
            }
            commitUidPendingState(uid);
        }
    }

    private void commitUidPendingState(int uid) {
        int uidState = this.mUidStates.get(uid, com.android.server.am.ProcessList.PREVIOUS_APP_ADJ);
        int capability = this.mCapability.get(uid, 0);
        boolean appWidgetVisible = this.mAppWidgetVisible.get(uid, false);
        int pendingUidState = this.mPendingUidStates.get(uid, uidState);
        int pendingCapability = this.mPendingCapability.get(uid, capability);
        boolean pendingAppWidgetVisible = this.mPendingAppWidgetVisible.get(uid, appWidgetVisible);
        boolean foregroundChange = ((uidState <= 500) == (pendingUidState <= 500) && capability == pendingCapability && appWidgetVisible == pendingAppWidgetVisible) ? false : true;
        if (uidState != pendingUidState || capability != pendingCapability || appWidgetVisible != pendingAppWidgetVisible) {
            if (foregroundChange) {
                this.mEventLog.logCommitUidState(uid, pendingUidState, pendingCapability, pendingAppWidgetVisible, appWidgetVisible != pendingAppWidgetVisible);
            }
            for (int i = 0; i < this.mUidStateChangedCallbacks.size(); i++) {
                com.android.server.appop.AppOpsUidStateTracker.UidStateChangedCallback cb = this.mUidStateChangedCallbacks.keyAt(i);
                java.util.concurrent.Executor executor = this.mUidStateChangedCallbacks.valueAt(i);
                executor.execute(com.android.internal.util.function.pooled.PooledLambda.obtainRunnable(new com.android.internal.util.function.QuadConsumer() { // from class: com.android.server.appop.AppOpsUidStateTrackerImpl$$ExternalSyntheticLambda1
                    public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3, java.lang.Object obj4) {
                        ((com.android.server.appop.AppOpsUidStateTracker.UidStateChangedCallback) obj).onUidStateChanged(((java.lang.Integer) obj2).intValue(), ((java.lang.Integer) obj3).intValue(), ((java.lang.Boolean) obj4).booleanValue());
                    }
                }, cb, java.lang.Integer.valueOf(uid), java.lang.Integer.valueOf(pendingUidState), java.lang.Boolean.valueOf(foregroundChange)));
            }
        }
        if (this.mPendingGone.get(uid, false)) {
            this.mUidStates.delete(uid);
            this.mCapability.delete(uid);
            this.mAppWidgetVisible.delete(uid);
            this.mPendingGone.delete(uid);
            if (com.android.internal.hidden_from_bootclasspath.android.permission.flags.Flags.finishRunningOpsForKilledPackages()) {
                for (int i2 = 0; i2 < this.mUidStateChangedCallbacks.size(); i2++) {
                    com.android.server.appop.AppOpsUidStateTracker.UidStateChangedCallback cb2 = this.mUidStateChangedCallbacks.keyAt(i2);
                    java.util.concurrent.Executor executor2 = this.mUidStateChangedCallbacks.valueAt(i2);
                    executor2.execute(com.android.internal.util.function.pooled.PooledLambda.obtainRunnable(new com.android.internal.util.function.QuadConsumer() { // from class: com.android.server.appop.AppOpsUidStateTrackerImpl$$ExternalSyntheticLambda1
                        public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3, java.lang.Object obj4) {
                            ((com.android.server.appop.AppOpsUidStateTracker.UidStateChangedCallback) obj).onUidStateChanged(((java.lang.Integer) obj2).intValue(), ((java.lang.Integer) obj3).intValue(), ((java.lang.Boolean) obj4).booleanValue());
                        }
                    }, cb2, java.lang.Integer.valueOf(uid), Integer.MAX_VALUE, java.lang.Boolean.valueOf(foregroundChange)));
                }
            }
        } else {
            this.mUidStates.put(uid, pendingUidState);
            this.mCapability.put(uid, pendingCapability);
            this.mAppWidgetVisible.put(uid, pendingAppWidgetVisible);
        }
        this.mPendingUidStates.delete(uid);
        this.mPendingCapability.delete(uid);
        this.mPendingAppWidgetVisible.delete(uid);
        this.mPendingCommitTime.delete(uid);
    }

    private int getUidCapability(int uid) {
        return this.mCapability.get(uid, 0);
    }

    private boolean getUidAppWidgetVisible(int uid) {
        return this.mAppWidgetVisible.get(uid, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class EventLog {
        private static final int APP_WIDGET_VISIBLE = 1;
        private static final int APP_WIDGET_VISIBLE_CHANGED = 2;
        private static final int COMMIT_UID_STATE_LOG_MAX_SIZE = 200;
        private static final int EVAL_FOREGROUND_MODE_MAX_SIZE = 200;
        private static final int UPDATE_UID_PROC_STATE_LOG_MAX_SIZE = 200;
        private final com.android.server.appop.AppOpsUidStateTrackerImpl.DelayableExecutor mExecutor;
        private final java.lang.Thread mExecutorThread;
        private int[][] mUpdateUidProcStateLog = (int[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.Integer.TYPE, 200, 3);
        private long[] mUpdateUidProcStateLogTimestamps = new long[200];
        private int mUpdateUidProcStateLogSize = 0;
        private int mUpdateUidProcStateLogHead = 0;
        private int[][] mCommitUidStateLog = (int[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.Integer.TYPE, 200, 4);
        private long[] mCommitUidStateLogTimestamps = new long[200];
        private int mCommitUidStateLogSize = 0;
        private int mCommitUidStateLogHead = 0;
        private int[][] mEvalForegroundModeLog = (int[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.Integer.TYPE, 200, 5);
        private long[] mEvalForegroundModeLogTimestamps = new long[200];
        private int mEvalForegroundModeLogSize = 0;
        private int mEvalForegroundModeLogHead = 0;

        EventLog(com.android.server.appop.AppOpsUidStateTrackerImpl.DelayableExecutor executor, java.lang.Thread executorThread) {
            this.mExecutor = executor;
            this.mExecutorThread = executorThread;
        }

        void logUpdateUidProcState(int uid, int procState, int capability) {
            this.mExecutor.execute(com.android.internal.util.function.pooled.PooledLambda.obtainRunnable(new com.android.internal.util.function.QuintConsumer() { // from class: com.android.server.appop.AppOpsUidStateTrackerImpl$EventLog$$ExternalSyntheticLambda2
                public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3, java.lang.Object obj4, java.lang.Object obj5) {
                    ((com.android.server.appop.AppOpsUidStateTrackerImpl.EventLog) obj).logUpdateUidProcStateAsync(((java.lang.Long) obj2).longValue(), ((java.lang.Integer) obj3).intValue(), ((java.lang.Integer) obj4).intValue(), ((java.lang.Integer) obj5).intValue());
                }
            }, this, java.lang.Long.valueOf(java.lang.System.currentTimeMillis()), java.lang.Integer.valueOf(uid), java.lang.Integer.valueOf(procState), java.lang.Integer.valueOf(capability)));
        }

        void logUpdateUidProcStateAsync(long timestamp, int uid, int procState, int capability) {
            int idx = (this.mUpdateUidProcStateLogHead + this.mUpdateUidProcStateLogSize) % 200;
            if (this.mUpdateUidProcStateLogSize == 200) {
                this.mUpdateUidProcStateLogHead = (this.mUpdateUidProcStateLogHead + 1) % 200;
            } else {
                this.mUpdateUidProcStateLogSize++;
            }
            this.mUpdateUidProcStateLog[idx][0] = uid;
            this.mUpdateUidProcStateLog[idx][1] = procState;
            this.mUpdateUidProcStateLog[idx][2] = capability;
            this.mUpdateUidProcStateLogTimestamps[idx] = timestamp;
        }

        void logCommitUidState(int uid, int uidState, int capability, boolean appWidgetVisible, boolean appWidgetVisibleChanged) {
            this.mExecutor.execute(com.android.internal.util.function.pooled.PooledLambda.obtainRunnable(new com.android.internal.util.function.HeptConsumer() { // from class: com.android.server.appop.AppOpsUidStateTrackerImpl$EventLog$$ExternalSyntheticLambda0
                public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3, java.lang.Object obj4, java.lang.Object obj5, java.lang.Object obj6, java.lang.Object obj7) {
                    ((com.android.server.appop.AppOpsUidStateTrackerImpl.EventLog) obj).logCommitUidStateAsync(((java.lang.Long) obj2).longValue(), ((java.lang.Integer) obj3).intValue(), ((java.lang.Integer) obj4).intValue(), ((java.lang.Integer) obj5).intValue(), ((java.lang.Boolean) obj6).booleanValue(), ((java.lang.Boolean) obj7).booleanValue());
                }
            }, this, java.lang.Long.valueOf(java.lang.System.currentTimeMillis()), java.lang.Integer.valueOf(uid), java.lang.Integer.valueOf(uidState), java.lang.Integer.valueOf(capability), java.lang.Boolean.valueOf(appWidgetVisible), java.lang.Boolean.valueOf(appWidgetVisibleChanged)));
        }

        void logCommitUidStateAsync(long timestamp, int uid, int uidState, int capability, boolean appWidgetVisible, boolean appWidgetVisibleChanged) {
            int idx = (this.mCommitUidStateLogHead + this.mCommitUidStateLogSize) % 200;
            if (this.mCommitUidStateLogSize == 200) {
                this.mCommitUidStateLogHead = (this.mCommitUidStateLogHead + 1) % 200;
            } else {
                this.mCommitUidStateLogSize++;
            }
            this.mCommitUidStateLog[idx][0] = uid;
            this.mCommitUidStateLog[idx][1] = uidState;
            this.mCommitUidStateLog[idx][2] = capability;
            this.mCommitUidStateLog[idx][3] = 0;
            if (appWidgetVisible) {
                int[] iArr = this.mCommitUidStateLog[idx];
                iArr[3] = iArr[3] + 1;
            }
            if (appWidgetVisibleChanged) {
                int[] iArr2 = this.mCommitUidStateLog[idx];
                iArr2[3] = iArr2[3] + 2;
            }
            this.mCommitUidStateLogTimestamps[idx] = timestamp;
        }

        void logEvalForegroundMode(int uid, int uidState, int capability, int code, int result) {
            this.mExecutor.execute(com.android.internal.util.function.pooled.PooledLambda.obtainRunnable(new com.android.internal.util.function.HeptConsumer() { // from class: com.android.server.appop.AppOpsUidStateTrackerImpl$EventLog$$ExternalSyntheticLambda1
                public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3, java.lang.Object obj4, java.lang.Object obj5, java.lang.Object obj6, java.lang.Object obj7) {
                    ((com.android.server.appop.AppOpsUidStateTrackerImpl.EventLog) obj).logEvalForegroundModeAsync(((java.lang.Long) obj2).longValue(), ((java.lang.Integer) obj3).intValue(), ((java.lang.Integer) obj4).intValue(), ((java.lang.Integer) obj5).intValue(), ((java.lang.Integer) obj6).intValue(), ((java.lang.Integer) obj7).intValue());
                }
            }, this, java.lang.Long.valueOf(java.lang.System.currentTimeMillis()), java.lang.Integer.valueOf(uid), java.lang.Integer.valueOf(uidState), java.lang.Integer.valueOf(capability), java.lang.Integer.valueOf(code), java.lang.Integer.valueOf(result)));
        }

        void logEvalForegroundModeAsync(long timestamp, int uid, int uidState, int capability, int code, int result) {
            int idx = (this.mEvalForegroundModeLogHead + this.mEvalForegroundModeLogSize) % 200;
            if (this.mEvalForegroundModeLogSize == 200) {
                this.mEvalForegroundModeLogHead = (this.mEvalForegroundModeLogHead + 1) % 200;
            } else {
                this.mEvalForegroundModeLogSize++;
            }
            this.mEvalForegroundModeLog[idx][0] = uid;
            this.mEvalForegroundModeLog[idx][1] = uidState;
            this.mEvalForegroundModeLog[idx][2] = capability;
            this.mEvalForegroundModeLog[idx][3] = code;
            this.mEvalForegroundModeLog[idx][4] = result;
            this.mEvalForegroundModeLogTimestamps[idx] = timestamp;
        }

        void dumpEvents(java.io.PrintWriter pw) {
            int updateIdx = 0;
            int commitIdx = 0;
            int evalIdx = 0;
            while (true) {
                if (updateIdx < this.mUpdateUidProcStateLogSize || commitIdx < this.mCommitUidStateLogSize || evalIdx < this.mEvalForegroundModeLogSize) {
                    int updatePtr = (this.mUpdateUidProcStateLogHead + updateIdx) % 200;
                    int commitPtr = (this.mCommitUidStateLogHead + commitIdx) % 200;
                    int evalPtr = (this.mEvalForegroundModeLogHead + evalIdx) % 200;
                    long aTimestamp = updateIdx < this.mUpdateUidProcStateLogSize ? this.mUpdateUidProcStateLogTimestamps[updatePtr] : Long.MAX_VALUE;
                    long bTimestamp = commitIdx < this.mCommitUidStateLogSize ? this.mCommitUidStateLogTimestamps[commitPtr] : Long.MAX_VALUE;
                    long cTimestamp = evalIdx < this.mEvalForegroundModeLogSize ? this.mEvalForegroundModeLogTimestamps[evalPtr] : Long.MAX_VALUE;
                    if (aTimestamp <= bTimestamp && aTimestamp <= cTimestamp) {
                        dumpUpdateUidProcState(pw, updatePtr);
                        updateIdx++;
                    } else if (bTimestamp <= cTimestamp) {
                        dumpCommitUidState(pw, commitPtr);
                        commitIdx++;
                    } else {
                        dumpEvalForegroundMode(pw, evalPtr);
                        evalIdx++;
                    }
                } else {
                    return;
                }
            }
        }

        void dumpUpdateUidProcState(java.io.PrintWriter pw, int idx) {
            long timestamp = this.mUpdateUidProcStateLogTimestamps[idx];
            int uid = this.mUpdateUidProcStateLog[idx][0];
            int procState = this.mUpdateUidProcStateLog[idx][1];
            int capability = this.mUpdateUidProcStateLog[idx][2];
            android.util.TimeUtils.dumpTime(pw, timestamp);
            pw.print(" UPDATE_UID_PROC_STATE");
            pw.print(" uid=");
            pw.print(java.lang.String.format("%-8d", java.lang.Integer.valueOf(uid)));
            pw.print(" procState=");
            pw.print(java.lang.String.format("%-30s", android.app.ActivityManager.procStateToString(procState)));
            pw.print(" capability=");
            pw.print(android.app.ActivityManager.getCapabilitiesSummary(capability) + " ");
            pw.println();
        }

        void dumpCommitUidState(java.io.PrintWriter pw, int idx) {
            long timestamp = this.mCommitUidStateLogTimestamps[idx];
            int uid = this.mCommitUidStateLog[idx][0];
            int uidState = this.mCommitUidStateLog[idx][1];
            int capability = this.mCommitUidStateLog[idx][2];
            boolean appWidgetVisible = (this.mCommitUidStateLog[idx][3] & 1) != 0;
            boolean appWidgetVisibleChanged = (2 & this.mCommitUidStateLog[idx][3]) != 0;
            android.util.TimeUtils.dumpTime(pw, timestamp);
            pw.print(" COMMIT_UID_STATE     ");
            pw.print(" uid=");
            pw.print(java.lang.String.format("%-8d", java.lang.Integer.valueOf(uid)));
            pw.print(" uidState=");
            pw.print(java.lang.String.format("%-30s", android.app.AppOpsManager.uidStateToString(uidState)));
            pw.print(" capability=");
            pw.print(android.app.ActivityManager.getCapabilitiesSummary(capability) + " ");
            pw.print(" appWidgetVisible=");
            pw.print(appWidgetVisible);
            if (appWidgetVisibleChanged) {
                pw.print(" (changed)");
            }
            pw.println();
        }

        void dumpEvalForegroundMode(java.io.PrintWriter pw, int idx) {
            long timestamp = this.mEvalForegroundModeLogTimestamps[idx];
            int uid = this.mEvalForegroundModeLog[idx][0];
            int uidState = this.mEvalForegroundModeLog[idx][1];
            int capability = this.mEvalForegroundModeLog[idx][2];
            int code = this.mEvalForegroundModeLog[idx][3];
            int result = this.mEvalForegroundModeLog[idx][4];
            android.util.TimeUtils.dumpTime(pw, timestamp);
            pw.print(" EVAL_FOREGROUND_MODE ");
            pw.print(" uid=");
            pw.print(java.lang.String.format("%-8d", java.lang.Integer.valueOf(uid)));
            pw.print(" uidState=");
            pw.print(java.lang.String.format("%-30s", android.app.AppOpsManager.uidStateToString(uidState)));
            pw.print(" capability=");
            pw.print(android.app.ActivityManager.getCapabilitiesSummary(capability) + " ");
            pw.print(" code=");
            pw.print(java.lang.String.format("%-20s", android.app.AppOpsManager.opToName(code)));
            pw.print(" result=");
            pw.print(android.app.AppOpsManager.modeToName(result));
            pw.println();
        }
    }
}
