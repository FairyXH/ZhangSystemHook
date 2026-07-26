package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public class AnrController {
    private static final long PRE_DUMP_MIN_INTERVAL_MS = java.util.concurrent.TimeUnit.SECONDS.toMillis(20);
    private static final long PRE_DUMP_MONITOR_TIMEOUT_MS = java.util.concurrent.TimeUnit.SECONDS.toMillis(1);
    private static com.android.server.wm.IAnrControllerExt sAnrControllerExt = (com.android.server.wm.IAnrControllerExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IAnrControllerExt.class).create();
    private volatile long mLastPreDumpTimeMs;
    private final com.android.server.wm.WindowManagerService mService;
    private final android.util.SparseArray<com.android.server.wm.ActivityRecord> mUnresponsiveAppByDisplay = new android.util.SparseArray<>();

    AnrController(com.android.server.wm.WindowManagerService service) {
        this.mService = service;
    }

    void notifyAppUnresponsive(android.view.InputApplicationHandle applicationHandle, com.android.internal.os.TimeoutRecord timeoutRecord) {
        com.android.internal.os.anr.AnrLatencyTracker anrLatencyTracker;
        try {
            timeoutRecord.mLatencyTracker.notifyAppUnresponsiveStarted();
            timeoutRecord.mLatencyTracker.preDumpIfLockTooSlowStarted();
            preDumpIfLockTooSlow();
            timeoutRecord.mLatencyTracker.preDumpIfLockTooSlowEnded();
            timeoutRecord.mLatencyTracker.waitingOnGlobalLockStarted();
            boolean blamePendingFocusRequest = false;
            com.android.server.wm.WindowState targetWindowState = null;
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    timeoutRecord.mLatencyTracker.waitingOnGlobalLockEnded();
                    com.android.server.wm.ActivityRecord activity = com.android.server.wm.ActivityRecord.forTokenLocked(applicationHandle.token);
                    sAnrControllerExt.getNoFocusedWindowInfo(this.mService, activity);
                    if (activity == null) {
                        android.util.Slog.e("WindowManager", "Unknown app appToken:" + applicationHandle.name + ". Dropping notifyNoFocusedWindowAnr request");
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return;
                    }
                    if (activity.mAppStopped) {
                        android.util.Slog.d("WindowManager", "App is in stopped state:" + applicationHandle.name + ". Dropping notifyNoFocusedWindowAnr request");
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return;
                    }
                    com.android.server.wm.DisplayContent display = this.mService.mRoot.getDisplayContent(activity.getDisplayId());
                    android.os.IBinder focusToken = display != null ? display.getInputMonitor().mInputFocus : null;
                    com.android.server.wm.InputTarget focusTarget = this.mService.getInputTargetFromToken(focusToken);
                    if (focusTarget != null) {
                        targetWindowState = focusTarget.getWindowState();
                        blamePendingFocusRequest = android.os.SystemClock.uptimeMillis() - display.getInputMonitor().mInputFocusRequestTimeMillis >= com.android.server.wm.ActivityTaskManagerService.getInputDispatchingTimeoutMillisLocked(targetWindowState.getActivityRecord());
                    }
                    if (!blamePendingFocusRequest) {
                        android.util.Slog.i("WindowManager", "ANR in " + activity.getName() + ".  Reason: " + timeoutRecord.mReason);
                        this.mUnresponsiveAppByDisplay.put(activity.getDisplayId(), activity);
                    }
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    if (blamePendingFocusRequest && notifyWindowUnresponsive(focusToken, timeoutRecord)) {
                        android.util.Slog.i("WindowManager", "Blamed " + targetWindowState.getName() + " using pending focus request. Focused activity: " + activity.getName());
                    } else {
                        activity.inputDispatchingTimedOut(timeoutRecord, -1);
                    }
                    if (!blamePendingFocusRequest) {
                        dumpAnrStateAsync(activity, null, timeoutRecord.mReason);
                    }
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } finally {
            timeoutRecord.mLatencyTracker.notifyAppUnresponsiveEnded();
        }
    }

    void notifyWindowUnresponsive(android.os.IBinder token, java.util.OptionalInt pid, com.android.internal.os.TimeoutRecord timeoutRecord) {
        try {
            timeoutRecord.mLatencyTracker.notifyWindowUnresponsiveStarted();
            if (notifyWindowUnresponsive(token, timeoutRecord)) {
                return;
            }
            if (pid.isPresent()) {
                notifyWindowUnresponsive(pid.getAsInt(), timeoutRecord);
            } else {
                android.util.Slog.w("WindowManager", "Failed to notify that window token=" + token + " was unresponsive.");
            }
        } finally {
            timeoutRecord.mLatencyTracker.notifyWindowUnresponsiveEnded();
        }
    }

    private boolean notifyWindowUnresponsive(android.os.IBinder inputToken, com.android.internal.os.TimeoutRecord timeoutRecord) {
        timeoutRecord.mLatencyTracker.preDumpIfLockTooSlowStarted();
        preDumpIfLockTooSlow();
        timeoutRecord.mLatencyTracker.preDumpIfLockTooSlowEnded();
        timeoutRecord.mLatencyTracker.waitingOnGlobalLockStarted();
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                timeoutRecord.mLatencyTracker.waitingOnGlobalLockEnded();
                com.android.server.wm.InputTarget target = this.mService.getInputTargetFromToken(inputToken);
                if (target == null) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return false;
                }
                com.android.server.wm.WindowState windowState = target.getWindowState();
                int pid = target.getPid();
                com.android.server.wm.ActivityRecord activity = windowState.mInputChannelToken == inputToken ? windowState.mActivityRecord : null;
                android.util.Slog.i("WindowManager", "ANR in " + target + ". Reason:" + timeoutRecord.mReason);
                boolean aboveSystem = isWindowAboveSystem(windowState);
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                if (activity != null) {
                    activity.inputDispatchingTimedOut(timeoutRecord, pid);
                } else {
                    this.mService.mAmInternal.inputDispatchingTimedOut(pid, aboveSystem, timeoutRecord);
                }
                dumpAnrStateAsync(activity, windowState, timeoutRecord.mReason);
                return true;
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    private void notifyWindowUnresponsive(int pid, com.android.internal.os.TimeoutRecord timeoutRecord) {
        android.util.Slog.i("WindowManager", "ANR in input window owned by pid=" + pid + ". Reason: " + timeoutRecord.mReason);
        this.mService.mAmInternal.inputDispatchingTimedOut(pid, true, timeoutRecord);
        dumpAnrStateAsync(null, null, timeoutRecord.mReason);
    }

    void notifyWindowResponsive(android.os.IBinder token, java.util.OptionalInt pid) {
        if (notifyWindowResponsive(token)) {
            return;
        }
        if (!pid.isPresent()) {
            android.util.Slog.w("WindowManager", "Failed to notify that window token=" + token + " was responsive.");
        } else {
            notifyWindowResponsive(pid.getAsInt());
        }
    }

    private boolean notifyWindowResponsive(android.os.IBinder inputToken) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.InputTarget target = this.mService.getInputTargetFromToken(inputToken);
                if (target == null) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return false;
                }
                int pid = target.getPid();
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                this.mService.mAmInternal.inputDispatchingResumed(pid);
                return true;
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    private void notifyWindowResponsive(int pid) {
        this.mService.mAmInternal.inputDispatchingResumed(pid);
    }

    void onFocusChanged(com.android.server.wm.WindowState newFocus) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.ActivityRecord unresponsiveApp = this.mUnresponsiveAppByDisplay.get(newFocus.getDisplayId());
                if (unresponsiveApp != null && unresponsiveApp == newFocus.mActivityRecord) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    this.mService.mAmInternal.inputDispatchingResumed(unresponsiveApp.getPid());
                    this.mUnresponsiveAppByDisplay.remove(newFocus.getDisplayId());
                    return;
                }
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    private void preDumpIfLockTooSlow() {
    }

    /* JADX INFO: renamed from: com.android.server.wm.AnrController$1, reason: invalid class name */
    class AnonymousClass1 extends java.lang.Thread {
        final /* synthetic */ java.util.concurrent.CountDownLatch val$latch;
        final /* synthetic */ java.lang.Runnable val$monitor;
        final /* synthetic */ java.lang.String val$name;
        final /* synthetic */ long val$now;
        final /* synthetic */ boolean[] val$shouldDumpSf;

        AnonymousClass1(java.lang.Runnable runnable, java.util.concurrent.CountDownLatch countDownLatch, long j, java.lang.String str, boolean[] zArr) {
            this.val$monitor = runnable;
            this.val$latch = countDownLatch;
            this.val$now = j;
            this.val$name = str;
            this.val$shouldDumpSf = zArr;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            this.val$monitor.run();
            this.val$latch.countDown();
            long elapsed = android.os.SystemClock.uptimeMillis() - this.val$now;
            if (elapsed > com.android.server.wm.AnrController.PRE_DUMP_MONITOR_TIMEOUT_MS) {
                android.util.Slog.i("WindowManager", "Pre-dump acquired " + this.val$name + " in " + elapsed + "ms");
            } else if ("WindowManager".equals(this.val$name)) {
                this.val$shouldDumpSf[0] = false;
            }
        }
    }

    private void dumpAnrStateAsync(final com.android.server.wm.ActivityRecord activity, final com.android.server.wm.WindowState windowState, final java.lang.String reason) {
        com.android.server.FgThread.getExecutor().execute(new java.lang.Runnable() { // from class: com.android.server.wm.AnrController$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$dumpAnrStateAsync$0(activity, windowState, reason);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$dumpAnrStateAsync$0(com.android.server.wm.ActivityRecord activity, com.android.server.wm.WindowState windowState, java.lang.String reason) {
        try {
            android.os.Trace.traceBegin(64L, "dumpAnrStateLocked()");
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    android.util.Slog.i("WindowManager", "dumpAnrStateAsync windowState num: " + this.mService.mWindowMap.size());
                    this.mService.saveANRStateLocked(activity, windowState, reason);
                    this.mService.mAtmService.saveANRState(activity, reason);
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        } finally {
            android.os.Trace.traceEnd(64L);
        }
    }

    private boolean isWindowAboveSystem(com.android.server.wm.WindowState windowState) {
        int systemAlertLayer = this.mService.mPolicy.getWindowLayerFromTypeLw(2038, windowState.mOwnerCanAddInternalSystemWindow);
        return windowState.mBaseLayer > systemAlertLayer;
    }
}
