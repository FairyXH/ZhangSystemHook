package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class ActivityClientController extends android.app.IActivityClientController.Stub {
    public static final long ACCESS_SHARED_IDENTITY = 259743961;
    private static final int SET_PIP_ASPECT_RATIO_LIMIT = 60;
    private static final long SET_PIP_ASPECT_RATIO_TIME_WINDOW_MS = 60000;
    private static final java.lang.String TAG = "ActivityTaskManager";
    public com.android.server.wm.IActivityClientControllerExt mActivityClientControllerExt = (com.android.server.wm.IActivityClientControllerExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IActivityClientControllerExt.class).base(this).create();
    private com.android.internal.app.AssistUtils mAssistUtils;
    private final android.content.Context mContext;
    private final com.android.server.wm.WindowManagerGlobalLock mGlobalLock;
    private final com.android.server.wm.ActivityTaskManagerService mService;
    com.android.server.utils.quota.CountQuotaTracker mSetPipAspectRatioQuotaTracker;
    private final com.android.server.wm.ActivityTaskSupervisor mTaskSupervisor;

    ActivityClientController(com.android.server.wm.ActivityTaskManagerService service) {
        this.mService = service;
        this.mGlobalLock = service.mGlobalLock;
        this.mTaskSupervisor = service.mTaskSupervisor;
        this.mContext = service.mContext;
    }

    void onSystemReady() {
        this.mAssistUtils = new com.android.internal.app.AssistUtils(this.mContext);
    }

    public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException {
        try {
            return super.onTransact(code, data, reply, flags);
        } catch (java.lang.RuntimeException e) {
            throw com.android.server.wm.ActivityTaskManagerService.logAndRethrowRuntimeExceptionOnTransact("ActivityClientController", e);
        }
    }

    public void activityIdle(android.os.IBinder token, android.content.res.Configuration config, boolean stopProfiling) {
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    android.os.Trace.traceBegin(32L, "activityIdle");
                    com.android.server.wm.ActivityRecord r = com.android.server.wm.ActivityRecord.forTokenLocked(token);
                    if (r != null) {
                        this.mTaskSupervisor.activityIdleInternal(r, false, false, config);
                        if (stopProfiling && r.hasProcess()) {
                            r.app.clearProfilerIfNeeded();
                        }
                        this.mService.mSocExt.onEndOfActivityIdle(this.mContext, com.android.server.wm.ActivityRecord.forTokenLocked(token));
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return;
                    }
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } finally {
            android.os.Trace.traceEnd(32L);
            android.os.Binder.restoreCallingIdentity(origId);
        }
    }

    public void activityResumed(android.os.IBinder token, boolean handleSplashScreenExit) {
        long origId = android.os.Binder.clearCallingIdentity();
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.ActivityRecord.activityResumedLocked(token, handleSplashScreenExit);
                this.mActivityClientControllerExt.hookActivityResumed(token);
                com.android.server.wm.ActivityRecord record = com.android.server.wm.ActivityRecord.forTokenLocked(token);
                if (record != null) {
                    this.mActivityClientControllerExt.activityResumed(token, record.mUserId);
                    record.getWrapper().getExtImpl().interceptActivityOnSecondary(record, this.mTaskSupervisor.getKeyguardController());
                }
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        android.os.Binder.restoreCallingIdentity(origId);
    }

    public void activityRefreshed(android.os.IBinder token) {
        long origId = android.os.Binder.clearCallingIdentity();
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.ActivityRecord.activityRefreshedLocked(token);
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        android.os.Binder.restoreCallingIdentity(origId);
    }

    public void activityTopResumedStateLost() {
        long origId = android.os.Binder.clearCallingIdentity();
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mTaskSupervisor.handleTopResumedStateReleased(false);
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        android.os.Binder.restoreCallingIdentity(origId);
    }

    public void activityPaused(android.os.IBinder token) {
        long origId = android.os.Binder.clearCallingIdentity();
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                android.os.Trace.traceBegin(32L, "activityPaused");
                com.android.server.wm.ActivityRecord r = com.android.server.wm.ActivityRecord.forTokenLocked(token);
                if (r != null) {
                    this.mActivityClientControllerExt.notifyFlexibleWindowTaskVanish(r, false, true);
                    r.activityPaused(false);
                }
                android.os.Trace.traceEnd(32L);
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        android.os.Binder.restoreCallingIdentity(origId);
    }

    public void activityStopped(android.os.IBinder token, android.os.Bundle icicle, android.os.PersistableBundle persistentState, java.lang.CharSequence description) {
        com.android.server.wm.ActivityRecord r;
        if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_ALL) {
            android.util.Slog.v(TAG, "Activity stopped: token=" + token);
        }
        if (icicle != null && icicle.hasFileDescriptors()) {
            throw new java.lang.IllegalArgumentException("File descriptors passed in Bundle");
        }
        long origId = android.os.Binder.clearCallingIdentity();
        java.lang.String restartingName = null;
        int restartingUid = 0;
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                android.os.Trace.traceBegin(32L, "activityStopped");
                r = com.android.server.wm.ActivityRecord.isInRootTaskLocked(token);
                if (r != null) {
                    if (!r.isState(com.android.server.wm.ActivityRecord.State.STOPPING, com.android.server.wm.ActivityRecord.State.RESTARTING_PROCESS) && this.mTaskSupervisor.hasScheduledRestartTimeouts(r)) {
                        r.setState(com.android.server.wm.ActivityRecord.State.RESTARTING_PROCESS, "continue-restart");
                    }
                    if (r.attachedToProcess() && r.isState(com.android.server.wm.ActivityRecord.State.RESTARTING_PROCESS)) {
                        restartingName = r.app.mName;
                        restartingUid = r.app.mUid;
                    }
                    r.activityStopped(icicle, persistentState, description);
                }
                android.os.Trace.traceEnd(32L);
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        if (restartingName != null) {
            this.mTaskSupervisor.removeRestartTimeouts(r);
            this.mService.mAmInternal.killProcess(restartingName, restartingUid, "restartActivityProcess");
        }
        this.mService.mAmInternal.trimApplications();
        android.os.Binder.restoreCallingIdentity(origId);
    }

    public void activityDestroyed(android.os.IBinder token) {
        if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_SWITCH) {
            android.util.Slog.v(com.android.server.wm.ActivityTaskManagerService.TAG_SWITCH, "ACTIVITY DESTROYED: " + token);
        }
        long origId = android.os.Binder.clearCallingIdentity();
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                android.os.Trace.traceBegin(32L, "activityDestroyed");
                try {
                    com.android.server.wm.ActivityRecord r = com.android.server.wm.ActivityRecord.forTokenLocked(token);
                    this.mActivityClientControllerExt.activityDestroyed(r);
                    if (r != null) {
                        r.destroyed("activityDestroyed");
                    }
                } finally {
                    android.os.Trace.traceEnd(32L);
                    android.os.Binder.restoreCallingIdentity(origId);
                }
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    public void activityLocalRelaunch(android.os.IBinder token) {
        long origId = android.os.Binder.clearCallingIdentity();
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.ActivityRecord r = com.android.server.wm.ActivityRecord.forTokenLocked(token);
                if (r != null) {
                    r.startRelaunching();
                }
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        android.os.Binder.restoreCallingIdentity(origId);
    }

    public void activityRelaunched(android.os.IBinder token) {
        long origId = android.os.Binder.clearCallingIdentity();
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.ActivityRecord r = com.android.server.wm.ActivityRecord.forTokenLocked(token);
                if (r != null) {
                    r.finishRelaunching();
                }
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        android.os.Binder.restoreCallingIdentity(origId);
    }

    public void reportSizeConfigurations(android.os.IBinder token, android.window.SizeConfigurationBuckets sizeConfigurations) {
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_CONFIGURATION_enabled[1]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(token);
            java.lang.String protoLogParam1 = java.lang.String.valueOf(sizeConfigurations);
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_CONFIGURATION, -4921282642721622589L, 0, null, protoLogParam0, protoLogParam1);
        }
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.ActivityRecord r = com.android.server.wm.ActivityRecord.isInRootTaskLocked(token);
                if (r != null) {
                    r.setSizeConfigurations(sizeConfigurations);
                }
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    public boolean moveActivityTaskToBack(android.os.IBinder token, boolean nonRoot) {
        com.android.server.wm.ActivityTaskManagerService.enforceNotIsolatedCaller("moveActivityTaskToBack");
        android.util.Slog.i(TAG, "moveActivityTaskToBack callingPid:" + android.os.Binder.getCallingPid() + ", callingUid:" + android.os.Binder.getCallingUid());
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    int taskId = com.android.server.wm.ActivityRecord.getTaskForActivityLocked(token, !nonRoot);
                    com.android.server.wm.Task task = this.mService.mRootWindowContainer.anyTaskForId(taskId);
                    if (task == null) {
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return false;
                    }
                    this.mActivityClientControllerExt.moveActivityTaskToBack(task, token, nonRoot);
                    this.mActivityClientControllerExt.notifyFlexibleWindowTaskVanish(com.android.server.wm.ActivityRecord.isInRootTaskLocked(token), true, true);
                    boolean ret = com.android.server.wm.ActivityRecord.getRootTask(token).moveTaskToBack(task);
                    if (!com.android.server.wm.ActivityTaskManagerService.LTW_DISABLE && ret) {
                        this.mActivityClientControllerExt.closeRemoteTask(this.mService, taskId);
                    }
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return ret;
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(origId);
        }
    }

    public boolean shouldUpRecreateTask(android.os.IBinder token, java.lang.String destAffinity) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.ActivityRecord srec = com.android.server.wm.ActivityRecord.forTokenLocked(token);
                if (srec != null) {
                    boolean zShouldUpRecreateTaskLocked = srec.getRootTask().shouldUpRecreateTaskLocked(srec, destAffinity);
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return zShouldUpRecreateTaskLocked;
                }
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                return false;
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    /* JADX WARN: Finally extract failed */
    public boolean navigateUpTo(android.os.IBinder token, android.content.Intent destIntent, java.lang.String resolvedType, int resultCode, android.content.Intent resultData) throws java.lang.Throwable {
        boolean zNavigateUpTo;
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.ActivityRecord r = com.android.server.wm.ActivityRecord.isInRootTaskLocked(token);
                if (r == null) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return false;
                }
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                com.android.server.uri.NeededUriGrants destGrants = this.mService.collectGrants(destIntent, r);
                com.android.server.uri.NeededUriGrants resultGrants = this.mService.collectGrants(resultData, r.resultTo);
                com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock2 = this.mGlobalLock;
                com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
                synchronized (windowManagerGlobalLock2) {
                    try {
                        zNavigateUpTo = r.getRootTask().navigateUpTo(r, destIntent, resolvedType, destGrants, resultCode, resultData, resultGrants);
                    } catch (java.lang.Throwable th) {
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        throw th;
                    }
                }
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                return zNavigateUpTo;
            } catch (java.lang.Throwable th2) {
                th = th2;
                while (true) {
                    try {
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        throw th;
                    } catch (java.lang.Throwable th3) {
                        th = th3;
                    }
                }
            }
        }
    }

    public boolean releaseActivityInstance(android.os.IBinder token) {
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.ActivityRecord r = com.android.server.wm.ActivityRecord.isInRootTaskLocked(token);
                    if (r != null && r.isDestroyable()) {
                        r.destroyImmediately("app-req");
                        boolean zIsState = r.isState(com.android.server.wm.ActivityRecord.State.DESTROYING, com.android.server.wm.ActivityRecord.State.DESTROYED);
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return zIsState;
                    }
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    android.os.Binder.restoreCallingIdentity(origId);
                    return false;
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(origId);
        }
    }

    public boolean finishActivity(android.os.IBinder token, int resultCode, android.content.Intent resultData, int finishTask) {
        long j;
        boolean res;
        if (resultData != null && resultData.hasFileDescriptors()) {
            throw new java.lang.IllegalArgumentException("File descriptors passed in Intent");
        }
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.ActivityRecord r = com.android.server.wm.ActivityRecord.isInRootTaskLocked(token);
                boolean z = true;
                if (r == null) {
                    return true;
                }
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                com.android.server.uri.NeededUriGrants resultGrants = this.mService.collectGrants(resultData, r.resultTo);
                com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock2 = this.mGlobalLock;
                com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
                synchronized (windowManagerGlobalLock2) {
                    try {
                        if (!r.isInHistory()) {
                            return true;
                        }
                        com.android.server.wm.Task tr = r.getTask();
                        com.android.server.wm.ActivityRecord rootR = tr.getRootActivity();
                        if (rootR == null) {
                            android.util.Slog.w(TAG, "Finishing task with all activities already finished");
                        }
                        if (this.mService.getLockTaskController().activityBlockedFromFinish(r)) {
                            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                            return false;
                        }
                        if (this.mService.mController != null) {
                            com.android.server.wm.ActivityRecord next = r.getRootTask().topRunningActivity(token, -1);
                            if (next != null) {
                                boolean resumeOK = true;
                                try {
                                    resumeOK = this.mService.mController.activityResuming(next.packageName);
                                } catch (android.os.RemoteException e) {
                                    this.mService.mController = null;
                                    com.android.server.Watchdog.getInstance().setActivityController(null);
                                }
                                if (!resumeOK) {
                                    android.util.Slog.i(TAG, "Not finishing activity because controller resumed");
                                    this.mActivityClientControllerExt.hookActivityFinishIfResumeNotOK(r);
                                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                                    return false;
                                }
                            }
                        }
                        if (r.app != null) {
                            r.app.setLastActivityFinishTimeIfNeeded(android.os.SystemClock.uptimeMillis());
                        }
                        long origId = android.os.Binder.clearCallingIdentity();
                        android.os.Trace.traceBegin(32L, "finishActivity");
                        try {
                            r.mActivityRecordSocExt.hookOnWindowsDrawn();
                            if (finishTask != 1) {
                                z = false;
                            }
                            boolean finishWithRootActivity = z;
                            this.mTaskSupervisor.getBackgroundActivityLaunchController().onActivityRequestedFinishing(r);
                            try {
                                try {
                                    if (finishTask == 2) {
                                        j = 32;
                                    } else if (finishWithRootActivity && r == rootR) {
                                        j = 32;
                                    } else {
                                        try {
                                            boolean moveTaskToBack = this.mActivityClientControllerExt.onBackPressed(r, token);
                                            this.mActivityClientControllerExt.notifyFlexibleWindowTaskVanish(r, moveTaskToBack, moveTaskToBack);
                                            if (moveTaskToBack) {
                                                this.mActivityClientControllerExt.hookActivityFinishEnd(r);
                                                android.os.Trace.traceEnd(32L);
                                                android.os.Binder.restoreCallingIdentity(origId);
                                                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                                                return false;
                                            }
                                            j = 32;
                                            try {
                                                r.finishIfPossible(resultCode, resultData, resultGrants, "app-request", true);
                                                res = r.finishing;
                                                if (!com.android.server.wm.ActivityTaskManagerService.LTW_DISABLE) {
                                                    this.mService.getWrapper().getExtImpl().getRemoteTaskManager().handleFinishActivity(tr, r);
                                                }
                                                if (!res) {
                                                    android.util.Slog.i(TAG, "Failed to finish by app-request");
                                                }
                                                this.mActivityClientControllerExt.hookActivityFinishEnd(r);
                                                android.os.Trace.traceEnd(j);
                                                android.os.Binder.restoreCallingIdentity(origId);
                                                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                                                return res;
                                            } catch (java.lang.Throwable th) {
                                                th = th;
                                                this.mActivityClientControllerExt.hookActivityFinishEnd(r);
                                                android.os.Trace.traceEnd(j);
                                                android.os.Binder.restoreCallingIdentity(origId);
                                                throw th;
                                            }
                                        } catch (java.lang.Throwable th2) {
                                            th = th2;
                                            j = 32;
                                        }
                                    }
                                    this.mTaskSupervisor.removeTask(tr, false, finishWithRootActivity, "finish-activity", r.getUid(), r.getPid(), r.info.name);
                                    r.mRelaunchReason = 0;
                                    res = true;
                                    this.mActivityClientControllerExt.hookActivityFinishEnd(r);
                                    android.os.Trace.traceEnd(j);
                                    android.os.Binder.restoreCallingIdentity(origId);
                                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                                    return res;
                                } catch (java.lang.Throwable th3) {
                                    th = th3;
                                    this.mActivityClientControllerExt.hookActivityFinishEnd(r);
                                    android.os.Trace.traceEnd(j);
                                    android.os.Binder.restoreCallingIdentity(origId);
                                    throw th;
                                }
                            } catch (java.lang.Throwable th4) {
                                th = th4;
                                this.mActivityClientControllerExt.hookActivityFinishEnd(r);
                                android.os.Trace.traceEnd(j);
                                android.os.Binder.restoreCallingIdentity(origId);
                                throw th;
                            }
                        } catch (java.lang.Throwable th5) {
                            th = th5;
                            j = 32;
                        }
                    } finally {
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    }
                }
            } finally {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            }
        }
    }

    public boolean finishActivityAffinity(android.os.IBinder token) {
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    final com.android.server.wm.ActivityRecord r = com.android.server.wm.ActivityRecord.isInRootTaskLocked(token);
                    if (r == null) {
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return false;
                    }
                    if (this.mService.getLockTaskController().activityBlockedFromFinish(r)) {
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return false;
                    }
                    r.getTask().forAllActivities(new java.util.function.Predicate() { // from class: com.android.server.wm.ActivityClientController$$ExternalSyntheticLambda0
                        @Override // java.util.function.Predicate
                        public final boolean test(java.lang.Object obj) {
                            return r.finishIfSameAffinity((com.android.server.wm.ActivityRecord) obj);
                        }
                    }, r, true, true);
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return true;
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(origId);
        }
    }

    public void finishSubActivity(android.os.IBinder token, final java.lang.String resultWho, final int requestCode) {
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    final com.android.server.wm.ActivityRecord r = com.android.server.wm.ActivityRecord.isInRootTaskLocked(token);
                    if (r != null) {
                        r.getRootTask().forAllActivities(new java.util.function.Consumer() { // from class: com.android.server.wm.ActivityClientController$$ExternalSyntheticLambda3
                            @Override // java.util.function.Consumer
                            public final void accept(java.lang.Object obj) {
                                ((com.android.server.wm.ActivityRecord) obj).finishIfSubActivity(r, resultWho, requestCode);
                            }
                        }, true);
                        this.mService.updateOomAdj();
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return;
                    }
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(origId);
        }
    }

    public void setForceSendResultForMediaProjection(android.os.IBinder token) {
        this.mService.mAmInternal.enforceCallingPermission("android.permission.MANAGE_MEDIA_PROJECTION", "setForceSendResultForMediaProjection");
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.ActivityRecord r = com.android.server.wm.ActivityRecord.isInRootTaskLocked(token);
                if (r != null && r.isInHistory()) {
                    r.setForceSendResultForMediaProjection();
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return;
                }
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public boolean isTopOfTask(android.os.IBinder token) {
        boolean z;
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.ActivityRecord r = com.android.server.wm.ActivityRecord.isInRootTaskLocked(token);
                z = r != null && r.getTask().getTopNonFinishingActivity() == r;
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        return z;
    }

    public boolean willActivityBeVisible(android.os.IBinder token) {
        boolean z;
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.Task rootTask = com.android.server.wm.ActivityRecord.getRootTask(token);
                z = rootTask != null && rootTask.willActivityBeVisible(token);
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        return z;
    }

    public int getDisplayId(android.os.IBinder activityToken) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.Task rootTask = com.android.server.wm.ActivityRecord.getRootTask(activityToken);
                if (rootTask == null) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return 0;
                }
                int displayId = rootTask.getDisplayId();
                int i = displayId != -1 ? displayId : 0;
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                return i;
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public int getTaskForActivity(android.os.IBinder token, boolean onlyRoot) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.ActivityRecord r = com.android.server.wm.ActivityRecord.forTokenLocked(token);
                if (r == null) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return -1;
                }
                com.android.server.wm.Task task = r.getTask();
                if (onlyRoot) {
                    int i = task.getRootActivity() == r ? task.mTaskId : -1;
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return i;
                }
                int i2 = task.mTaskId;
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                return i2;
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public android.content.res.Configuration getTaskConfiguration(android.os.IBinder activityToken) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.ActivityRecord ar = com.android.server.wm.ActivityRecord.isInAnyTask(activityToken);
                if (ar == null) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return null;
                }
                android.content.res.Configuration configuration = ar.getTask().getConfiguration();
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                return configuration;
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public android.os.IBinder getActivityTokenBelow(android.os.IBinder activityToken) {
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.ActivityRecord ar = com.android.server.wm.ActivityRecord.isInAnyTask(activityToken);
                    if (ar == null) {
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return null;
                    }
                    com.android.server.wm.ActivityRecord below = ar.getTask().getActivity(new java.util.function.Predicate() { // from class: com.android.server.wm.ActivityClientController$$ExternalSyntheticLambda1
                        @Override // java.util.function.Predicate
                        public final boolean test(java.lang.Object obj) {
                            return com.android.server.wm.ActivityClientController.lambda$getActivityTokenBelow$2((com.android.server.wm.ActivityRecord) obj);
                        }
                    }, ar, false, true);
                    if (below == null || below.getUid() != ar.getUid()) {
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return null;
                    }
                    android.os.IBinder iBinder = below.token;
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return iBinder;
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    static /* synthetic */ boolean lambda$getActivityTokenBelow$2(com.android.server.wm.ActivityRecord r) {
        return !r.finishing;
    }

    public android.content.ComponentName getCallingActivity(android.os.IBinder token) {
        android.content.ComponentName component;
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.ActivityRecord r = getCallingRecord(token);
                component = r != null ? r.intent.getComponent() : null;
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        return component;
    }

    public java.lang.String getCallingPackage(android.os.IBinder token) {
        java.lang.String callingPackage;
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.ActivityRecord r = getCallingRecord(token);
                if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_STACK) {
                    android.util.Slog.v(TAG, "getCallingPackage token " + token);
                }
                java.lang.String resultPkg = r != null ? r.info.packageName : null;
                callingPackage = this.mActivityClientControllerExt.getCallingPackage(resultPkg, token, this.mGlobalLock);
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        return callingPackage;
    }

    private static com.android.server.wm.ActivityRecord getCallingRecord(android.os.IBinder token) {
        com.android.server.wm.ActivityRecord r = com.android.server.wm.ActivityRecord.isInRootTaskLocked(token);
        if (r != null) {
            return r.resultTo;
        }
        return null;
    }

    public int getLaunchedFromUid(android.os.IBinder token) {
        return getUid(token, null, false);
    }

    public java.lang.String getLaunchedFromPackage(android.os.IBinder token) {
        return getPackage(token, null, false);
    }

    public int getActivityCallerUid(android.os.IBinder activityToken, android.os.IBinder callerToken) {
        return getUid(activityToken, callerToken, true);
    }

    public java.lang.String getActivityCallerPackage(android.os.IBinder activityToken, android.os.IBinder callerToken) {
        return getPackage(activityToken, callerToken, true);
    }

    private int getUid(android.os.IBinder activityToken, android.os.IBinder callerToken, boolean isActivityCallerCall) {
        int uid = android.os.Binder.getCallingUid();
        boolean isInternalCaller = isInternalCallerGetLaunchedFrom(uid);
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.ActivityRecord r = com.android.server.wm.ActivityRecord.forTokenLocked(activityToken);
                if (r != null && ((isInternalCaller || canGetLaunchedFromLocked(uid, r, callerToken, isActivityCallerCall)) && isValidCaller(r, callerToken, isActivityCallerCall))) {
                    int callerUid = isActivityCallerCall ? r.getCallerUid(callerToken) : r.launchedFromUid;
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return callerUid;
                }
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                return -1;
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    private java.lang.String getPackage(android.os.IBinder activityToken, android.os.IBinder callerToken, boolean isActivityCallerCall) {
        int uid = android.os.Binder.getCallingUid();
        boolean isInternalCaller = isInternalCallerGetLaunchedFrom(uid);
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.ActivityRecord r = com.android.server.wm.ActivityRecord.forTokenLocked(activityToken);
                if (r != null && ((isInternalCaller || canGetLaunchedFromLocked(uid, r, callerToken, isActivityCallerCall)) && isValidCaller(r, callerToken, isActivityCallerCall))) {
                    java.lang.String callerPackage = isActivityCallerCall ? r.getCallerPackage(callerToken) : r.launchedFromPackage;
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return callerPackage;
                }
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                return null;
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    private boolean isValidCaller(com.android.server.wm.ActivityRecord r, android.os.IBinder callerToken, boolean isActivityCallerCall) {
        return isActivityCallerCall ? r.hasCaller(callerToken) : callerToken == null;
    }

    public int checkActivityCallerContentUriPermission(android.os.IBinder activityToken, android.os.IBinder callerToken, android.net.Uri uri, int modeFlags, int userId) {
        com.android.server.uri.GrantUri grantUri = new com.android.server.uri.GrantUri(userId, uri, modeFlags);
        if (!this.mService.mUgmInternal.checkUriPermission(grantUri, android.os.Binder.getCallingUid(), modeFlags, true)) {
            throw new java.lang.SecurityException("You don't have access to the content URI, hence can't check if the caller has access to it: " + uri);
        }
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.ActivityRecord r = com.android.server.wm.ActivityRecord.forTokenLocked(activityToken);
                if (r == null) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return -1;
                }
                boolean granted = r.checkContentUriPermission(callerToken, grantUri, modeFlags);
                int i = granted ? 0 : -1;
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                return i;
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    private boolean isInternalCallerGetLaunchedFrom(int uid) {
        if (android.os.UserHandle.getAppId(uid) == 1000) {
            return true;
        }
        android.content.pm.PackageManagerInternal pm = this.mService.mWindowManager.mPmInternal;
        com.android.server.pm.pkg.AndroidPackage callingPkg = pm.getPackage(uid);
        if (callingPkg == null) {
            return false;
        }
        if (callingPkg.isSignedWithPlatformKey()) {
            return true;
        }
        java.lang.String[] installerNames = pm.getKnownPackageNames(2, android.os.UserHandle.getUserId(uid));
        return installerNames.length > 0 && callingPkg.getPackageName().equals(installerNames[0]);
    }

    private static boolean canGetLaunchedFromLocked(int uid, com.android.server.wm.ActivityRecord r, android.os.IBinder callerToken, boolean isActivityCallerCall) {
        if (!android.app.compat.CompatChanges.isChangeEnabled(ACCESS_SHARED_IDENTITY, uid)) {
            return false;
        }
        boolean isShareIdentityEnabled = isActivityCallerCall ? r.isCallerShareIdentityEnabled(callerToken) : r.mShareIdentity;
        int callerUid = isActivityCallerCall ? r.getCallerUid(callerToken) : r.launchedFromUid;
        return isShareIdentityEnabled || callerUid == uid;
    }

    public void setRequestedOrientation(android.os.IBinder token, int requestedOrientation) {
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.ActivityRecord r = com.android.server.wm.ActivityRecord.isInRootTaskLocked(token);
                    if (r != null) {
                        android.util.Slog.d(TAG, "Requested Orientation r = " + r + ", requestedOrientation = " + android.content.pm.ActivityInfo.screenOrientationToString(requestedOrientation) + " (" + requestedOrientation + ")");
                        this.mActivityClientControllerExt.onActivityRequestOrientation();
                        boolean result = this.mActivityClientControllerExt.setRequestedOrientationBefore(r, requestedOrientation, android.content.pm.ActivityInfo.isFixedOrientationLandscape(requestedOrientation));
                        com.android.server.wm.EventLogTags.writeWmSetRequestedOrientation(requestedOrientation, r.shortComponentName);
                        r.setRequestedOrientation(requestedOrientation);
                        this.mActivityClientControllerExt.setRequestedOrientation(r, requestedOrientation, android.content.pm.ActivityInfo.isFixedOrientationLandscape(requestedOrientation));
                        if (result) {
                            this.mActivityClientControllerExt.setRequestedOrientationAfter(r, requestedOrientation, android.content.pm.ActivityInfo.isFixedOrientationLandscape(requestedOrientation));
                        }
                    }
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        } finally {
            android.os.Binder.restoreCallingIdentity(origId);
        }
    }

    public int getRequestedOrientation(android.os.IBinder token) {
        int overrideOrientation;
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.ActivityRecord r = com.android.server.wm.ActivityRecord.isInRootTaskLocked(token);
                if (r != null) {
                    overrideOrientation = r.getOverrideOrientation();
                } else {
                    overrideOrientation = -1;
                }
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        return overrideOrientation;
    }

    /* JADX WARN: Finally extract failed */
    public boolean convertFromTranslucent(android.os.IBinder token) {
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.ActivityRecord r = com.android.server.wm.ActivityRecord.isInRootTaskLocked(token);
                    if (r != null) {
                        com.android.server.wm.Transition transition = (!r.mTransitionController.isShellTransitionsEnabled() || r.mTransitionController.isCollecting()) ? null : r.mTransitionController.createTransition(4);
                        boolean changed = r.setOccludesParent(true);
                        if (transition != null) {
                            if (changed && !this.mService.getWrapper().getExtImpl().withNoneTransition(r, r.getTask(), null, 4, "convertFromTranslucent")) {
                                transition.setOverrideAnimation(android.window.TransitionInfo.AnimationOptions.makeSceneTransitionAnimOptions(), null, null);
                                r.mTransitionController.requestStartTransition(transition, null, null, null);
                                r.mTransitionController.setReady(r.getDisplayContent());
                            } else {
                                transition.abort();
                            }
                        }
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        android.os.Binder.restoreCallingIdentity(origId);
                        return changed;
                    }
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    android.os.Binder.restoreCallingIdentity(origId);
                    return false;
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } catch (java.lang.Throwable th2) {
            android.os.Binder.restoreCallingIdentity(origId);
            throw th2;
        }
    }

    public boolean convertToTranslucent(android.os.IBinder token, android.os.Bundle options) {
        com.android.server.wm.Transition transition;
        com.android.server.wm.SafeActivityOptions safeOptions = com.android.server.wm.SafeActivityOptions.fromBundle(options);
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.ActivityRecord r = com.android.server.wm.ActivityRecord.isInRootTaskLocked(token);
                    if (r != null) {
                        com.android.server.wm.ActivityRecord under = r.getTask().getActivityBelow(r);
                        if (under != null) {
                            under.returningOptions = safeOptions != null ? safeOptions.getOptions(r) : null;
                        }
                        boolean isEmbedding = r.mTransitionController.isShellTransitionsEnabled() && r.isEmbedded();
                        if ((!isEmbedding && !r.mTransitionController.inPlayingTransition(r)) || r.mTransitionController.isCollecting()) {
                            transition = null;
                        } else {
                            transition = r.mTransitionController.createTransition(3);
                        }
                        boolean changed = r.setOccludesParent(false);
                        if (transition != null) {
                            if (changed) {
                                r.mTransitionController.requestStartTransition(transition, null, null, null);
                                r.mTransitionController.setReady(r.getDisplayContent());
                                if (under != null && under.returningOptions != null && under.returningOptions.getAnimationType() == 5) {
                                    transition.setOverrideAnimation(android.window.TransitionInfo.AnimationOptions.makeSceneTransitionAnimOptions(), null, null);
                                }
                            } else {
                                transition.abort();
                            }
                        }
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return changed;
                    }
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return false;
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(origId);
        }
    }

    public boolean isImmersive(android.os.IBinder token) {
        boolean z;
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.ActivityRecord r = com.android.server.wm.ActivityRecord.isInRootTaskLocked(token);
                if (r == null) {
                    throw new java.lang.IllegalArgumentException();
                }
                z = r.immersive;
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        return z;
    }

    public void setImmersive(android.os.IBinder token, boolean immersive) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.ActivityRecord r = com.android.server.wm.ActivityRecord.isInRootTaskLocked(token);
                if (r == null) {
                    throw new java.lang.IllegalArgumentException();
                }
                r.immersive = immersive;
                if (r.isFocusedActivityOnDisplay()) {
                    if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_IMMERSIVE_enabled[0]) {
                        java.lang.String protoLogParam0 = java.lang.String.valueOf(r);
                        com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_IMMERSIVE, -1597980207704427048L, 0, null, protoLogParam0);
                    }
                    this.mService.applyUpdateLockStateLocked(r);
                }
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    public boolean enterPictureInPictureMode(android.os.IBinder token, android.app.PictureInPictureParams params) {
        boolean zEnterPictureInPictureMode;
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            ensureSetPipAspectRatioQuotaTracker();
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.ActivityRecord r = ensureValidPictureInPictureActivityParams("enterPictureInPictureMode", token, params);
                    zEnterPictureInPictureMode = this.mService.enterPictureInPictureMode(r, params, true);
                } finally {
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            return zEnterPictureInPictureMode;
        } finally {
            android.os.Binder.restoreCallingIdentity(origId);
        }
    }

    public void setPictureInPictureParams(android.os.IBinder token, android.app.PictureInPictureParams params) {
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            ensureSetPipAspectRatioQuotaTracker();
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.ActivityRecord r = ensureValidPictureInPictureActivityParams("setPictureInPictureParams", token, params);
                    r.setPictureInPictureParams(params);
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        } finally {
            android.os.Binder.restoreCallingIdentity(origId);
        }
    }

    public void setShouldDockBigOverlays(android.os.IBinder token, boolean shouldDockBigOverlays) {
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.ActivityRecord r = com.android.server.wm.ActivityRecord.forTokenLocked(token);
                    r.setShouldDockBigOverlays(shouldDockBigOverlays);
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        } finally {
            android.os.Binder.restoreCallingIdentity(origId);
        }
    }

    public void splashScreenAttached(android.os.IBinder token) {
        long origId = android.os.Binder.clearCallingIdentity();
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.ActivityRecord.splashScreenAttachedLocked(token);
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        android.os.Binder.restoreCallingIdentity(origId);
    }

    public void requestCompatCameraControl(android.os.IBinder token, boolean showControl, boolean transformationApplied, android.app.ICompatCameraControlCallback callback) {
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.ActivityRecord r = com.android.server.wm.ActivityRecord.isInRootTaskLocked(token);
                    if (r != null) {
                        r.updateCameraCompatState(showControl, transformationApplied, callback);
                    }
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        } finally {
            android.os.Binder.restoreCallingIdentity(origId);
        }
    }

    public void updateActivitySpecificConfig(android.os.IBinder token, android.content.res.Configuration config) {
        long origId = android.os.Binder.clearCallingIdentity();
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.ActivityRecord r = com.android.server.wm.ActivityRecord.forTokenLocked(token);
                if (r != null) {
                    r.getWrapper().getExtImpl().updateActivitySpecificConfig(config);
                }
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        android.os.Binder.restoreCallingIdentity(origId);
    }

    private void ensureSetPipAspectRatioQuotaTracker() {
        if (this.mSetPipAspectRatioQuotaTracker == null) {
            this.mSetPipAspectRatioQuotaTracker = new com.android.server.utils.quota.CountQuotaTracker(this.mContext, com.android.server.utils.quota.Categorizer.SINGLE_CATEGORIZER);
            this.mSetPipAspectRatioQuotaTracker.setCountLimit(com.android.server.utils.quota.Category.SINGLE_CATEGORY, 60, 60000L);
        }
    }

    private com.android.server.wm.ActivityRecord ensureValidPictureInPictureActivityParams(java.lang.String caller, android.os.IBinder token, android.app.PictureInPictureParams params) {
        if (!this.mService.mSupportsPictureInPicture) {
            throw new java.lang.IllegalStateException(caller + ": Device doesn't support picture-in-picture mode.");
        }
        com.android.server.wm.ActivityRecord r = com.android.server.wm.ActivityRecord.forTokenLocked(token);
        if (r == null) {
            throw new java.lang.IllegalStateException(caller + ": Can't find activity for token=" + token);
        }
        if (!r.supportsPictureInPicture()) {
            throw new java.lang.IllegalStateException(caller + ": Current activity does not support picture-in-picture.");
        }
        int userId = android.os.UserHandle.getCallingUserId();
        if (r.pictureInPictureArgs.hasSetAspectRatio() && params.hasSetAspectRatio() && !r.pictureInPictureArgs.getAspectRatio().equals(params.getAspectRatio()) && !this.mSetPipAspectRatioQuotaTracker.noteEvent(userId, r.packageName, "setPipAspectRatio")) {
            throw new java.lang.IllegalStateException(caller + ": Too many PiP aspect ratio change requests from " + r.packageName);
        }
        float minAspectRatio = this.mContext.getResources().getFloat(android.R.dimen.config_hoverTapSlop);
        float maxAspectRatio = this.mContext.getResources().getFloat(android.R.dimen.config_horizontalScrollFactor);
        if (params.hasSetAspectRatio() && !this.mService.mWindowManager.isValidPictureInPictureAspectRatio(r.mDisplayContent, params.getAspectRatioFloat())) {
            throw new java.lang.IllegalArgumentException(java.lang.String.format(caller + ": Aspect ratio is too extreme (must be between %f and %f).", java.lang.Float.valueOf(minAspectRatio), java.lang.Float.valueOf(maxAspectRatio)));
        }
        if (this.mService.mSupportsExpandedPictureInPicture && params.hasSetExpandedAspectRatio() && !this.mService.mWindowManager.isValidExpandedPictureInPictureAspectRatio(r.mDisplayContent, params.getExpandedAspectRatioFloat())) {
            throw new java.lang.IllegalArgumentException(java.lang.String.format(caller + ": Expanded aspect ratio is not extreme enough (must not be between %f and %f).", java.lang.Float.valueOf(minAspectRatio), java.lang.Float.valueOf(maxAspectRatio)));
        }
        params.truncateActions(android.app.ActivityTaskManager.getMaxNumPictureInPictureActions(this.mContext));
        return r;
    }

    boolean requestPictureInPictureMode(com.android.server.wm.ActivityRecord r) {
        if (r.inPinnedWindowingMode()) {
            return false;
        }
        boolean canEnterPictureInPicture = r.checkEnterPictureInPictureState("requestPictureInPictureMode", false);
        if (!canEnterPictureInPicture) {
            return false;
        }
        if (r.pictureInPictureArgs.isAutoEnterEnabled()) {
            return this.mService.enterPictureInPictureMode(r, r.pictureInPictureArgs, false);
        }
        try {
            this.mService.getLifecycleManager().scheduleTransactionItem(r.app.getThread(), android.app.servertransaction.EnterPipRequestedItem.obtain(r.token));
            return true;
        } catch (java.lang.Exception e) {
            android.util.Slog.w(TAG, "Failed to send enter pip requested item: " + r.intent.getComponent(), e);
            return false;
        }
    }

    void onPictureInPictureUiStateChanged(com.android.server.wm.ActivityRecord r, android.app.PictureInPictureUiState pipState) {
        try {
            this.mService.getLifecycleManager().scheduleTransactionItem(r.app.getThread(), android.app.servertransaction.PipStateTransactionItem.obtain(r.token, pipState));
        } catch (java.lang.Exception e) {
            android.util.Slog.w(TAG, "Failed to send pip state transaction item: " + r.intent.getComponent(), e);
        }
    }

    public void toggleFreeformWindowingMode(android.os.IBinder token) {
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.ActivityRecord r = com.android.server.wm.ActivityRecord.forTokenLocked(token);
                    if (r == null) {
                        throw new java.lang.IllegalArgumentException("toggleFreeformWindowingMode: No activity record matching token=" + token);
                    }
                    com.android.server.wm.Task rootTask = r.getRootTask();
                    if (rootTask == null) {
                        throw new java.lang.IllegalStateException("toggleFreeformWindowingMode: the activity doesn't have a root task");
                    }
                    if (!rootTask.inFreeformWindowingMode() && rootTask.getWindowingMode() != 1) {
                        throw new java.lang.IllegalStateException("toggleFreeformWindowingMode: You can only toggle between fullscreen and freeform.");
                    }
                    if (rootTask.inFreeformWindowingMode()) {
                        rootTask.setWindowingMode(1);
                        rootTask.setBounds(null);
                    } else {
                        if (!r.supportsFreeform()) {
                            throw new java.lang.IllegalStateException("This activity is currently not freeform-enabled");
                        }
                        if (rootTask.getParent().inFreeformWindowingMode()) {
                            rootTask.setWindowingMode(0);
                        } else {
                            rootTask.setWindowingMode(5);
                        }
                    }
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    private int validateMultiwindowFullscreenRequestLocked(com.android.server.wm.Task topFocusedRootTask, int fullscreenRequest, com.android.server.wm.ActivityRecord requesterActivity) {
        if (requesterActivity.getWindowingMode() == 2) {
            return 0;
        }
        if (requesterActivity != topFocusedRootTask.getTopMostActivity()) {
            return 2;
        }
        return (fullscreenRequest != 0 || (topFocusedRootTask.getWindowingMode() == 1 && topFocusedRootTask.mMultiWindowRestoreWindowingMode != -1)) ? 0 : 1;
    }

    public void requestMultiwindowFullscreen(android.os.IBinder callingActivity, int fullscreenRequest, android.os.IRemoteCallback callback) {
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    requestMultiwindowFullscreenLocked(callingActivity, fullscreenRequest, callback);
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    private void requestMultiwindowFullscreenLocked(android.os.IBinder callingActivity, final int fullscreenRequest, final android.os.IRemoteCallback callback) {
        final com.android.server.wm.ActivityRecord r = com.android.server.wm.ActivityRecord.forTokenLocked(callingActivity);
        if (r == null) {
            return;
        }
        com.android.server.wm.TransitionController controller = r.mTransitionController;
        if (!controller.isShellTransitionsEnabled()) {
            com.android.server.wm.Task topFocusedRootTask = this.mService.getTopDisplayFocusedRootTask();
            int validateResult = validateMultiwindowFullscreenRequestLocked(topFocusedRootTask, fullscreenRequest, r);
            reportMultiwindowFullscreenRequestValidatingResult(callback, validateResult);
            if (validateResult == 0) {
                executeMultiWindowFullscreenRequest(fullscreenRequest, topFocusedRootTask);
                return;
            }
            return;
        }
        final com.android.server.wm.Transition transition = new com.android.server.wm.Transition(6, 0, controller, this.mService.mWindowManager.mSyncEngine);
        r.mTransitionController.startCollectOrQueue(transition, new com.android.server.wm.TransitionController.OnStartCollect() { // from class: com.android.server.wm.ActivityClientController$$ExternalSyntheticLambda4
            @Override // com.android.server.wm.TransitionController.OnStartCollect
            public final void onCollectStarted(boolean z) {
                this.f$0.lambda$requestMultiwindowFullscreenLocked$3(fullscreenRequest, callback, r, transition, z);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: executeFullscreenRequestTransition, reason: merged with bridge method [inline-methods] */
    public void lambda$requestMultiwindowFullscreenLocked$3(int fullscreenRequest, android.os.IRemoteCallback callback, com.android.server.wm.ActivityRecord r, com.android.server.wm.Transition transition, boolean queued) {
        com.android.server.wm.Task topFocusedRootTask = this.mService.getTopDisplayFocusedRootTask();
        int validateResult = validateMultiwindowFullscreenRequestLocked(topFocusedRootTask, fullscreenRequest, r);
        reportMultiwindowFullscreenRequestValidatingResult(callback, validateResult);
        if (validateResult != 0) {
            transition.abort();
            return;
        }
        com.android.server.wm.Task requestingTask = r.getTask();
        transition.collect(requestingTask);
        executeMultiWindowFullscreenRequest(fullscreenRequest, requestingTask);
        r.mTransitionController.requestStartTransition(transition, requestingTask, null, null);
        transition.setReady(requestingTask, true);
    }

    private static void reportMultiwindowFullscreenRequestValidatingResult(android.os.IRemoteCallback callback, int result) {
        if (callback == null) {
            return;
        }
        android.os.Bundle res = new android.os.Bundle();
        res.putInt("result", result);
        try {
            callback.sendResult(res);
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(TAG, "client throws an exception back to the server, ignore it");
        }
    }

    private static void executeMultiWindowFullscreenRequest(int fullscreenRequest, com.android.server.wm.Task requester) {
        int targetWindowingMode;
        if (fullscreenRequest == 1) {
            int restoreWindowingMode = requester.getRequestedOverrideWindowingMode();
            targetWindowingMode = 1;
            requester.setWindowingMode(1);
            requester.mMultiWindowRestoreWindowingMode = restoreWindowingMode;
            requester.mMultiWindowRestoreParent = requester.getParent().mRemoteToken.toWindowContainerToken();
        } else {
            targetWindowingMode = requester.mMultiWindowRestoreWindowingMode;
            requester.restoreWindowingMode();
        }
        if (targetWindowingMode == 1) {
            requester.setBounds(null);
        }
    }

    public void startLockTaskModeByToken(android.os.IBinder token) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.ActivityRecord r = com.android.server.wm.ActivityRecord.forTokenLocked(token);
                if (r != null) {
                    this.mService.startLockTaskMode(r.getTask(), false);
                }
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    public void stopLockTaskModeByToken(android.os.IBinder token) {
        this.mService.stopLockTaskModeInternal(token, false);
    }

    public void showLockTaskEscapeMessage(android.os.IBinder token) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                if (com.android.server.wm.ActivityRecord.forTokenLocked(token) != null) {
                    this.mService.getLockTaskController().showLockTaskToast();
                }
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    public void setTaskDescription(android.os.IBinder token, android.app.ActivityManager.TaskDescription td) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.ActivityRecord r = com.android.server.wm.ActivityRecord.isInRootTaskLocked(token);
                if (r != null) {
                    r.setTaskDescription(td);
                }
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    public boolean showAssistFromActivity(android.os.IBinder token, android.os.Bundle args) {
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.ActivityRecord caller = com.android.server.wm.ActivityRecord.forTokenLocked(token);
                    com.android.server.wm.Task topRootTask = this.mService.getTopDisplayFocusedRootTask();
                    com.android.server.wm.ActivityRecord top = topRootTask != null ? topRootTask.getTopNonFinishingActivity() : null;
                    if (top != caller) {
                        android.util.Slog.w(TAG, "showAssistFromActivity failed: caller " + caller + " is not current top " + top);
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return false;
                    }
                    if (top.nowVisible) {
                        java.lang.String callingAttributionTag = top.launchedFromFeatureId;
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return this.mAssistUtils.showSessionForActiveService(args, 8, callingAttributionTag, (com.android.internal.app.IVoiceInteractionSessionShowCallback) null, token);
                    }
                    android.util.Slog.w(TAG, "showAssistFromActivity failed: caller " + caller + " is not visible");
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return false;
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    public boolean isRootVoiceInteraction(android.os.IBinder token) {
        boolean z;
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.ActivityRecord r = com.android.server.wm.ActivityRecord.isInRootTaskLocked(token);
                z = r != null && r.rootVoiceInteraction;
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        return z;
    }

    public void startLocalVoiceInteraction(android.os.IBinder callingActivity, android.os.Bundle options) {
        android.util.Slog.i(TAG, "Activity tried to startLocalVoiceInteraction");
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.Task topRootTask = this.mService.getTopDisplayFocusedRootTask();
                com.android.server.wm.ActivityRecord activity = topRootTask != null ? topRootTask.getTopNonFinishingActivity() : null;
                if (com.android.server.wm.ActivityRecord.forTokenLocked(callingActivity) != activity) {
                    throw new java.lang.SecurityException("Only focused activity can call startVoiceInteraction");
                }
                if (this.mService.mRunningVoice == null && activity.getTask().voiceSession == null && activity.voiceSession == null) {
                    if (activity.pendingVoiceInteractionStart) {
                        android.util.Slog.w(TAG, "Pending start of voice interaction already.");
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return;
                    } else {
                        activity.pendingVoiceInteractionStart = true;
                        java.lang.String callingAttributionTag = activity.launchedFromFeatureId;
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        ((android.service.voice.VoiceInteractionManagerInternal) com.android.server.LocalServices.getService(android.service.voice.VoiceInteractionManagerInternal.class)).startLocalVoiceInteraction(callingActivity, callingAttributionTag, options);
                        return;
                    }
                }
                android.util.Slog.w(TAG, "Already in a voice interaction, cannot start new voice interaction");
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public void stopLocalVoiceInteraction(android.os.IBinder callingActivity) {
        ((android.service.voice.VoiceInteractionManagerInternal) com.android.server.LocalServices.getService(android.service.voice.VoiceInteractionManagerInternal.class)).stopLocalVoiceInteraction(callingActivity);
    }

    public void setShowWhenLocked(android.os.IBinder token, boolean showWhenLocked) {
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.ActivityRecord r = com.android.server.wm.ActivityRecord.isInRootTaskLocked(token);
                    if (r != null) {
                        r.setShowWhenLocked(showWhenLocked);
                    }
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        } finally {
            android.os.Binder.restoreCallingIdentity(origId);
        }
    }

    public void setInheritShowWhenLocked(android.os.IBinder token, boolean inheritShowWhenLocked) {
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.ActivityRecord r = com.android.server.wm.ActivityRecord.isInRootTaskLocked(token);
                    if (r != null) {
                        r.setInheritShowWhenLocked(inheritShowWhenLocked);
                    }
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        } finally {
            android.os.Binder.restoreCallingIdentity(origId);
        }
    }

    public void setTurnScreenOn(android.os.IBinder token, boolean turnScreenOn) {
        android.util.Slog.i(TAG, "setTurnScreenOn callingPid:" + android.os.Binder.getCallingPid() + ", callingUid:" + android.os.Binder.getCallingUid());
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.ActivityRecord r = com.android.server.wm.ActivityRecord.isInRootTaskLocked(token);
                    if (r != null && !this.mActivityClientControllerExt.skipSetTurnScreenOn(r, turnScreenOn)) {
                        r.setTurnScreenOn(turnScreenOn);
                    }
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        } finally {
            android.os.Binder.restoreCallingIdentity(origId);
        }
    }

    public void setAllowCrossUidActivitySwitchFromBelow(android.os.IBinder token, boolean allowed) {
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.ActivityRecord r = com.android.server.wm.ActivityRecord.isInRootTaskLocked(token);
                    if (r != null) {
                        r.setAllowCrossUidActivitySwitchFromBelow(allowed);
                    }
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        } finally {
            android.os.Binder.restoreCallingIdentity(origId);
        }
    }

    public void reportActivityFullyDrawn(android.os.IBinder token, boolean restoredFromBundle) {
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.ActivityRecord r = com.android.server.wm.ActivityRecord.isInRootTaskLocked(token);
                    if (r != null) {
                        this.mTaskSupervisor.getActivityMetricsLogger().notifyFullyDrawn(r, restoredFromBundle);
                    }
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        } finally {
            android.os.Binder.restoreCallingIdentity(origId);
        }
    }

    public void overrideActivityTransition(android.os.IBinder token, boolean open, int enterAnim, int exitAnim, int backgroundColor) {
        long origId = android.os.Binder.clearCallingIdentity();
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.ActivityRecord r = com.android.server.wm.ActivityRecord.isInRootTaskLocked(token);
                if (r != null) {
                    r.overrideCustomTransition(open, enterAnim, exitAnim, backgroundColor);
                }
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        android.os.Binder.restoreCallingIdentity(origId);
    }

    public void clearOverrideActivityTransition(android.os.IBinder token, boolean open) {
        long origId = android.os.Binder.clearCallingIdentity();
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.ActivityRecord r = com.android.server.wm.ActivityRecord.isInRootTaskLocked(token);
                if (r != null) {
                    r.clearCustomTransition(open);
                }
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        android.os.Binder.restoreCallingIdentity(origId);
    }

    public void overridePendingTransition(android.os.IBinder token, java.lang.String packageName, int enterAnim, int exitAnim, int backgroundColor) throws java.lang.Throwable {
        long origId = android.os.Binder.clearCallingIdentity();
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                try {
                    com.android.server.wm.ActivityRecord r = com.android.server.wm.ActivityRecord.isInRootTaskLocked(token);
                    if (this.mActivityClientControllerExt.ignoringOverridePendingTransition(r)) {
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return;
                    }
                    if (r != null && (r.isState(com.android.server.wm.ActivityRecord.State.RESUMED, com.android.server.wm.ActivityRecord.State.PAUSING) || this.mActivityClientControllerExt.canOverridePendingTransition(r))) {
                        r.mDisplayContent.mAppTransition.overridePendingAppTransition(packageName, enterAnim, exitAnim, backgroundColor, null, null, r.mOverrideTaskTransition);
                        r.mTransitionController.setOverrideAnimation(android.window.TransitionInfo.AnimationOptions.makeCustomAnimOptions(packageName, enterAnim, exitAnim, backgroundColor, r.mOverrideTaskTransition), null, null);
                    }
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    android.os.Binder.restoreCallingIdentity(origId);
                } catch (java.lang.Throwable th) {
                    th = th;
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            } catch (java.lang.Throwable th2) {
                th = th2;
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    /* JADX WARN: Finally extract failed */
    public int setVrMode(android.os.IBinder token, boolean enabled, android.content.ComponentName packageName) {
        com.android.server.wm.ActivityRecord r;
        this.mService.enforceSystemHasVrFeature();
        com.android.server.vr.VrManagerInternal vrService = (com.android.server.vr.VrManagerInternal) com.android.server.LocalServices.getService(com.android.server.vr.VrManagerInternal.class);
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                r = com.android.server.wm.ActivityRecord.isInRootTaskLocked(token);
            } finally {
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        if (r == null) {
            throw new java.lang.IllegalArgumentException();
        }
        int err = vrService.hasVrPackage(packageName, r.mUserId);
        if (err != 0) {
            return err;
        }
        long callingId = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock2 = this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock2) {
                try {
                    r.requestedVrComponent = enabled ? packageName : null;
                    if (r.isFocusedActivityOnDisplay()) {
                        this.mService.applyUpdateVrModeLocked(r);
                    }
                } finally {
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            android.os.Binder.restoreCallingIdentity(callingId);
            return 0;
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(callingId);
            throw th;
        }
    }

    public void setRecentsScreenshotEnabled(android.os.IBinder token, boolean enabled) {
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.ActivityRecord r = com.android.server.wm.ActivityRecord.isInRootTaskLocked(token);
                    if (r != null) {
                        r.setRecentsScreenshotEnabled(enabled);
                    }
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        } finally {
            android.os.Binder.restoreCallingIdentity(origId);
        }
    }

    void restartActivityProcessIfVisible(android.os.IBinder token) {
        com.android.server.wm.ActivityTaskManagerService.enforceTaskPermission("restartActivityProcess");
        long callingId = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.ActivityRecord r = com.android.server.wm.ActivityRecord.isInRootTaskLocked(token);
                    if (r != null) {
                        r.restartProcessIfVisible();
                    }
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        } finally {
            android.os.Binder.restoreCallingIdentity(callingId);
        }
    }

    public void invalidateHomeTaskSnapshot(android.os.IBinder token) {
        com.android.server.wm.ActivityRecord r;
        if (token == null) {
            com.android.server.wm.ActivityTaskManagerService.enforceTaskPermission("invalidateHomeTaskSnapshot");
        }
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                if (token == null) {
                    com.android.server.wm.Task rootTask = this.mService.mRootWindowContainer.getDefaultTaskDisplayArea().getRootHomeTask();
                    r = rootTask != null ? rootTask.topRunningActivity() : null;
                } else {
                    r = com.android.server.wm.ActivityRecord.isInRootTaskLocked(token);
                }
                if (r != null && r.isActivityTypeHome()) {
                    this.mService.mWindowManager.mTaskSnapshotController.removeSnapshotCache(r.getTask().mTaskId);
                }
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    public void dismissKeyguard(android.os.IBinder token, com.android.internal.policy.IKeyguardDismissCallback callback, java.lang.CharSequence message) {
        if (message != null) {
            this.mService.mAmInternal.enforceCallingPermission("android.permission.SHOW_KEYGUARD_MESSAGE", "dismissKeyguard");
        }
        long callingId = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    this.mService.mKeyguardController.dismissKeyguard(token, callback, message);
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        } finally {
            android.os.Binder.restoreCallingIdentity(callingId);
        }
    }

    public void registerRemoteAnimations(android.os.IBinder token, android.view.RemoteAnimationDefinition definition) {
        this.mService.mAmInternal.enforceCallingPermission("android.permission.CONTROL_REMOTE_APP_TRANSITION_ANIMATIONS", "registerRemoteAnimations");
        definition.setCallingPidUid(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid());
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.ActivityRecord r = com.android.server.wm.ActivityRecord.isInRootTaskLocked(token);
                    if (r != null) {
                        r.registerRemoteAnimations(definition);
                    }
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        } finally {
            android.os.Binder.restoreCallingIdentity(origId);
        }
    }

    public void unregisterRemoteAnimations(android.os.IBinder token) {
        this.mService.mAmInternal.enforceCallingPermission("android.permission.CONTROL_REMOTE_APP_TRANSITION_ANIMATIONS", "unregisterRemoteAnimations");
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.ActivityRecord r = com.android.server.wm.ActivityRecord.isInRootTaskLocked(token);
                    if (r != null) {
                        r.unregisterRemoteAnimations();
                    }
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        } finally {
            android.os.Binder.restoreCallingIdentity(origId);
        }
    }

    private static boolean isRelativeTaskRootActivity(final com.android.server.wm.ActivityRecord r, com.android.server.wm.ActivityRecord taskRoot) {
        com.android.server.wm.TaskFragment taskFragment = r.getTaskFragment();
        return r == taskFragment.getActivity(new java.util.function.Predicate() { // from class: com.android.server.wm.ActivityClientController$$ExternalSyntheticLambda2
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.wm.ActivityClientController.lambda$isRelativeTaskRootActivity$4(r, (com.android.server.wm.ActivityRecord) obj);
            }
        }, false) && taskRoot.getTaskFragment().getCompanionTaskFragment() == taskFragment;
    }

    static /* synthetic */ boolean lambda$isRelativeTaskRootActivity$4(com.android.server.wm.ActivityRecord r, com.android.server.wm.ActivityRecord ar) {
        return !ar.finishing || ar == r;
    }

    private static boolean isTopActivityInTaskFragment(com.android.server.wm.ActivityRecord activity) {
        return activity.getTaskFragment().topRunningActivity() == activity;
    }

    private void requestCallbackFinish(android.app.IRequestFinishCallback callback) {
        try {
            callback.requestFinish();
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Failed to invoke request finish callback", e);
        }
    }

    public void onBackPressed(android.os.IBinder token, android.app.IRequestFinishCallback callback) {
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.ActivityRecord r = com.android.server.wm.ActivityRecord.isInRootTaskLocked(token);
                    if (r == null) {
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return;
                    }
                    com.android.server.wm.Task task = r.getTask();
                    com.android.server.wm.ActivityRecord root = task.getRootActivity(false, true);
                    if (r == root && this.mService.mWindowOrganizerController.mTaskOrganizerController.handleInterceptBackPressedOnTaskRoot(r.getRootTask())) {
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    } else if (shouldMoveTaskToBack(r, root)) {
                        moveActivityTaskToBack(token, true);
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    } else {
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        requestCallbackFinish(callback);
                    }
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(origId);
        }
    }

    static boolean shouldMoveTaskToBack(com.android.server.wm.ActivityRecord r, com.android.server.wm.ActivityRecord rootActivity) {
        if (r != rootActivity && !isRelativeTaskRootActivity(r, rootActivity)) {
            return false;
        }
        boolean isBaseActivity = rootActivity.mActivityComponent.equals(r.getTask().realActivity);
        android.content.Intent baseActivityIntent = isBaseActivity ? rootActivity.intent : null;
        return baseActivityIntent != null && isTopActivityInTaskFragment(r) && rootActivity.isLaunchSourceType(2) && com.android.server.wm.ActivityRecord.isMainIntent(baseActivityIntent);
    }

    public void enableTaskLocaleOverride(android.os.IBinder token) {
        if (android.os.UserHandle.getAppId(android.os.Binder.getCallingUid()) != 1000) {
            return;
        }
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.ActivityRecord r = com.android.server.wm.ActivityRecord.forTokenLocked(token);
                if (r != null) {
                    r.getTask().mAlignActivityLocaleWithTask = true;
                }
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    public boolean isRequestedToLaunchInTaskFragment(android.os.IBinder activityToken, android.os.IBinder taskFragmentToken) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.ActivityRecord r = com.android.server.wm.ActivityRecord.isInRootTaskLocked(activityToken);
                if (r == null) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return false;
                }
                boolean z = r.mRequestedLaunchingTaskFragmentToken == taskFragmentToken;
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                return z;
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public void setActivityRecordInputSinkEnabled(android.os.IBinder activityToken, boolean enabled) {
        if (!com.android.window.flags.Flags.allowDisableActivityRecordInputSink()) {
            return;
        }
        this.mService.mAmInternal.enforceCallingPermission("android.permission.INTERNAL_SYSTEM_WINDOW", "setActivityRecordInputSinkEnabled");
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.ActivityRecord r = com.android.server.wm.ActivityRecord.forTokenLocked(activityToken);
                if (r != null) {
                    r.mActivityRecordInputSinkEnabled = enabled;
                }
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }
}
