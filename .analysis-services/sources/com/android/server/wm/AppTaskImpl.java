package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class AppTaskImpl extends android.app.IAppTask.Stub {
    private static final java.lang.String TAG = "AppTaskImpl";
    private final int mCallingUid;
    private final com.android.server.wm.ActivityTaskManagerService mService;
    private final int mTaskId;

    public AppTaskImpl(com.android.server.wm.ActivityTaskManagerService service, int taskId, int callingUid) {
        this.mService = service;
        this.mTaskId = taskId;
        this.mCallingUid = callingUid;
    }

    private void checkCallerOrSystemOrRoot() {
        if (this.mCallingUid != android.os.Binder.getCallingUid() && 1000 != android.os.Binder.getCallingUid() && android.os.Binder.getCallingUid() != 0) {
            throw new java.lang.SecurityException("Caller " + this.mCallingUid + " does not match caller of getAppTasks(): " + android.os.Binder.getCallingUid());
        }
    }

    public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException {
        try {
            return super.onTransact(code, data, reply, flags);
        } catch (java.lang.RuntimeException e) {
            throw com.android.server.wm.ActivityTaskManagerService.logAndRethrowRuntimeExceptionOnTransact(TAG, e);
        }
    }

    public void finishAndRemoveTask() {
        checkCallerOrSystemOrRoot();
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                int origCallingUid = android.os.Binder.getCallingUid();
                int origCallingPid = android.os.Binder.getCallingPid();
                long callingIdentity = android.os.Binder.clearCallingIdentity();
                try {
                    if (!this.mService.mTaskSupervisor.removeTaskById(this.mTaskId, false, true, "finish-and-remove-task", origCallingUid, origCallingPid)) {
                        throw new java.lang.IllegalArgumentException("Unable to find task ID " + this.mTaskId);
                    }
                } finally {
                    android.os.Binder.restoreCallingIdentity(callingIdentity);
                }
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    public android.app.ActivityManager.RecentTaskInfo getTaskInfo() {
        android.app.ActivityManager.RecentTaskInfo recentTaskInfoCreateRecentTaskInfo;
        checkCallerOrSystemOrRoot();
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                long origId = android.os.Binder.clearCallingIdentity();
                try {
                    com.android.server.wm.Task task = this.mService.mRootWindowContainer.anyTaskForId(this.mTaskId, 1);
                    if (task == null) {
                        throw new java.lang.IllegalArgumentException("Unable to find task ID " + this.mTaskId);
                    }
                    recentTaskInfoCreateRecentTaskInfo = this.mService.getRecentTasks().createRecentTaskInfo(task, false, true);
                } finally {
                    android.os.Binder.restoreCallingIdentity(origId);
                }
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        return recentTaskInfoCreateRecentTaskInfo;
    }

    /* JADX WARN: Removed duplicated region for block: B:23:0x008b A[Catch: all -> 0x00c2, TryCatch #1 {all -> 0x00c2, blocks: (B:13:0x0054, B:15:0x005e, B:17:0x0066, B:18:0x007f, B:21:0x0087, B:23:0x008b, B:24:0x00a4, B:30:0x00bb), top: B:41:0x0023 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void moveToFront(android.app.IApplicationThread r23, java.lang.String r24) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 202
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.wm.AppTaskImpl.moveToFront(android.app.IApplicationThread, java.lang.String):void");
    }

    public int startActivity(android.os.IBinder whoThread, java.lang.String callingPackage, java.lang.String callingFeatureId, android.content.Intent intent, java.lang.String resolvedType, android.os.Bundle bOptions) {
        com.android.server.wm.Task task;
        android.app.IApplicationThread appThread;
        checkCallerOrSystemOrRoot();
        this.mService.assertPackageMatchesCallingUid(callingPackage);
        int callingUser = android.os.UserHandle.getCallingUserId();
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                task = this.mService.mRootWindowContainer.anyTaskForId(this.mTaskId, 1);
                if (task == null) {
                    throw new java.lang.IllegalArgumentException("Unable to find task ID " + this.mTaskId);
                }
                appThread = android.app.IApplicationThread.Stub.asInterface(whoThread);
                if (appThread == null) {
                    throw new java.lang.IllegalArgumentException("Bad app thread " + appThread);
                }
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        return this.mService.getActivityStartController().obtainStarter(intent, TAG).setCaller(appThread).setCallingPackage(callingPackage).setCallingFeatureId(callingFeatureId).setResolvedType(resolvedType).setActivityOptions(bOptions).setUserId(callingUser).setInTask(task).execute();
    }

    public void setExcludeFromRecents(boolean exclude) {
        checkCallerOrSystemOrRoot();
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                long origId = android.os.Binder.clearCallingIdentity();
                try {
                    com.android.server.wm.Task task = this.mService.mRootWindowContainer.anyTaskForId(this.mTaskId, 1);
                    if (task == null) {
                        throw new java.lang.IllegalArgumentException("Unable to find task ID " + this.mTaskId);
                    }
                    android.content.Intent intent = task.getBaseIntent();
                    if (exclude) {
                        intent.addFlags(8388608);
                    } else {
                        intent.setFlags(intent.getFlags() & (-8388609));
                    }
                } finally {
                    android.os.Binder.restoreCallingIdentity(origId);
                }
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }
}
