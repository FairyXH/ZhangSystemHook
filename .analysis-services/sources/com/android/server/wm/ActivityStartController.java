package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public class ActivityStartController {
    private static final int DO_PENDING_ACTIVITY_LAUNCHES_MSG = 1;
    private static final java.lang.String TAG = "ActivityTaskManager";
    boolean mCheckedForSetup;
    private final com.android.server.wm.ActivityStarter.Factory mFactory;
    private boolean mInExecution;
    private com.android.server.wm.ActivityRecord mLastHomeActivityStartRecord;
    private int mLastHomeActivityStartResult;
    private com.android.server.wm.ActivityStarter mLastStarter;
    private final com.android.server.wm.PendingRemoteAnimationRegistry mPendingRemoteAnimationRegistry;
    private final com.android.server.wm.ActivityTaskManagerService mService;
    private final com.android.server.wm.ActivityTaskSupervisor mSupervisor;
    private com.android.server.wm.ActivityRecord[] tmpOutRecord;

    ActivityStartController(com.android.server.wm.ActivityTaskManagerService service) {
        this(service, service.mTaskSupervisor, new com.android.server.wm.ActivityStarter.DefaultFactory(service, service.mTaskSupervisor, new com.android.server.wm.ActivityStartInterceptor(service, service.mTaskSupervisor)));
    }

    ActivityStartController(com.android.server.wm.ActivityTaskManagerService service, com.android.server.wm.ActivityTaskSupervisor supervisor, com.android.server.wm.ActivityStarter.Factory factory) {
        this.tmpOutRecord = new com.android.server.wm.ActivityRecord[1];
        this.mCheckedForSetup = false;
        this.mInExecution = false;
        this.mService = service;
        this.mSupervisor = supervisor;
        this.mFactory = factory;
        this.mFactory.setController(this);
        this.mPendingRemoteAnimationRegistry = new com.android.server.wm.PendingRemoteAnimationRegistry(service.mGlobalLock, service.mH);
    }

    com.android.server.wm.ActivityStarter obtainStarter(android.content.Intent intent, java.lang.String reason) {
        return this.mFactory.obtain().setIntent(intent).setReason(reason);
    }

    void onExecutionStarted() {
        this.mInExecution = true;
    }

    boolean isInExecution() {
        return this.mInExecution;
    }

    void onExecutionComplete(com.android.server.wm.ActivityStarter starter) {
        this.mInExecution = false;
        if (this.mLastStarter == null) {
            this.mLastStarter = this.mFactory.obtain();
        }
        this.mLastStarter.set(starter);
        this.mFactory.recycle(starter);
    }

    void postStartActivityProcessingForLastStarter(com.android.server.wm.ActivityRecord r, int result, com.android.server.wm.Task targetRootTask) {
        if (this.mLastStarter == null) {
            return;
        }
        this.mLastStarter.postStartActivityProcessing(r, result, targetRootTask);
    }

    void startHomeActivity(android.content.Intent intent, android.content.pm.ActivityInfo aInfo, java.lang.String reason, com.android.server.wm.TaskDisplayArea taskDisplayArea) {
        android.app.ActivityOptions options = android.app.ActivityOptions.makeBasic();
        options.setLaunchWindowingMode(1);
        if (!com.android.server.wm.ActivityRecord.isResolverActivity(aInfo.name)) {
            options.setLaunchActivityType(2);
        }
        int displayId = taskDisplayArea.getDisplayId();
        options.setLaunchDisplayId(displayId);
        options.setLaunchTaskDisplayArea(taskDisplayArea.mRemoteToken.toWindowContainerToken());
        this.mSupervisor.beginDeferResume();
        try {
            com.android.server.wm.Task rootHomeTask = taskDisplayArea.getOrCreateRootHomeTask(true);
            this.mSupervisor.endDeferResume();
            this.mLastHomeActivityStartResult = obtainStarter(intent, "startHomeActivity: " + reason).setOutActivity(this.tmpOutRecord).setCallingUid(0).setActivityInfo(aInfo).setActivityOptions(options.toBundle()).execute();
            this.mLastHomeActivityStartRecord = this.tmpOutRecord[0];
            android.view.DisplayInfo displayInfo = rootHomeTask != null ? rootHomeTask.getDisplayInfo() : null;
            if (rootHomeTask != null) {
                if (rootHomeTask.mInResumeTopActivity || (displayInfo != null && displayInfo.displayId != 0 && displayInfo.type == 1)) {
                    this.mSupervisor.scheduleResumeTopActivities();
                }
            }
        } catch (java.lang.Throwable th) {
            this.mSupervisor.endDeferResume();
            throw th;
        }
    }

    void startSetupActivity() {
        java.lang.String vers;
        if (this.mCheckedForSetup) {
            return;
        }
        android.content.ContentResolver resolver = this.mService.mContext.getContentResolver();
        if (this.mService.mFactoryTest != 1 && android.provider.Settings.Global.getInt(resolver, "device_provisioned", 0) != 0) {
            this.mCheckedForSetup = true;
            android.content.Intent intent = new android.content.Intent("android.intent.action.UPGRADE_SETUP");
            java.util.List<android.content.pm.ResolveInfo> ris = this.mService.mContext.getPackageManager().queryIntentActivities(intent, 1049728);
            if (!ris.isEmpty()) {
                android.content.pm.ResolveInfo ri = ris.get(0);
                if (ri.activityInfo.metaData != null) {
                    vers = ri.activityInfo.metaData.getString("android.SETUP_VERSION");
                } else {
                    vers = null;
                }
                if (vers == null && ri.activityInfo.applicationInfo.metaData != null) {
                    vers = ri.activityInfo.applicationInfo.metaData.getString("android.SETUP_VERSION");
                }
                java.lang.String lastVers = android.provider.Settings.Secure.getStringForUser(resolver, "last_setup_shown", resolver.getUserId());
                if (vers != null && !vers.equals(lastVers)) {
                    intent.setFlags(268435456);
                    intent.setComponent(new android.content.ComponentName(ri.activityInfo.packageName, ri.activityInfo.name));
                    obtainStarter(intent, "startSetupActivity").setCallingUid(0).setActivityInfo(ri.activityInfo).execute();
                }
            }
        }
    }

    int checkTargetUser(int targetUserId, boolean validateIncomingUser, int realCallingPid, int realCallingUid, java.lang.String reason) {
        if (validateIncomingUser) {
            return this.mService.handleIncomingUser(realCallingPid, realCallingUid, targetUserId, reason);
        }
        this.mService.mAmInternal.ensureNotSpecialUser(targetUserId);
        return targetUserId;
    }

    final int startActivityInPackage(int uid, int realCallingPid, int realCallingUid, java.lang.String callingPackage, java.lang.String callingFeatureId, android.content.Intent intent, java.lang.String resolvedType, android.os.IBinder resultTo, java.lang.String resultWho, int requestCode, int startFlags, com.android.server.wm.SafeActivityOptions options, int userId, com.android.server.wm.Task inTask, java.lang.String reason, boolean validateIncomingUser, com.android.server.am.PendingIntentRecord originatingPendingIntent, android.app.BackgroundStartPrivileges forcedBalByPiSender) {
        return obtainStarter(intent, reason).setCallingUid(uid).setRealCallingPid(realCallingPid).setRealCallingUid(realCallingUid).setCallingPackage(callingPackage).setCallingFeatureId(callingFeatureId).setResolvedType(resolvedType).setResultTo(resultTo).setResultWho(resultWho).setRequestCode(requestCode).setStartFlags(startFlags).setActivityOptions(options).setUserId(checkTargetUser(userId, validateIncomingUser, realCallingPid, realCallingUid, reason)).setInTask(inTask).setOriginatingPendingIntent(originatingPendingIntent).setBackgroundStartPrivileges(forcedBalByPiSender).execute();
    }

    final int startActivitiesInPackage(int uid, java.lang.String callingPackage, java.lang.String callingFeatureId, android.content.Intent[] intents, java.lang.String[] resolvedTypes, android.os.IBinder resultTo, com.android.server.wm.SafeActivityOptions options, int userId, boolean validateIncomingUser, com.android.server.am.PendingIntentRecord originatingPendingIntent, android.app.BackgroundStartPrivileges forcedBalByPiSender) {
        return startActivitiesInPackage(uid, 0, -1, callingPackage, callingFeatureId, intents, resolvedTypes, resultTo, options, userId, validateIncomingUser, originatingPendingIntent, forcedBalByPiSender);
    }

    final int startActivitiesInPackage(int uid, int realCallingPid, int realCallingUid, java.lang.String callingPackage, java.lang.String callingFeatureId, android.content.Intent[] intents, java.lang.String[] resolvedTypes, android.os.IBinder resultTo, com.android.server.wm.SafeActivityOptions options, int userId, boolean validateIncomingUser, com.android.server.am.PendingIntentRecord originatingPendingIntent, android.app.BackgroundStartPrivileges forcedBalByPiSender) {
        return startActivities(null, uid, realCallingPid, realCallingUid, callingPackage, callingFeatureId, intents, resolvedTypes, resultTo, options, checkTargetUser(userId, validateIncomingUser, android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), "startActivityInPackage"), "startActivityInPackage", originatingPendingIntent, forcedBalByPiSender);
    }

    /* JADX WARN: Code restructure failed: missing block: B:100:0x0250, code lost:
    
        monitor-enter(r13);
     */
    /* JADX WARN: Code restructure failed: missing block: B:101:0x0251, code lost:
    
        r29.mService.deferWindowLayout();
        r29.mService.mWindowManager.mStartingSurfaceController.beginDeferAddStartingWindow();
     */
    /* JADX WARN: Code restructure failed: missing block: B:102:0x025f, code lost:
    
        r0 = 0;
        r15 = r38;
     */
    /* JADX WARN: Code restructure failed: missing block: B:103:0x0262, code lost:
    
        r17 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:105:0x0265, code lost:
    
        if (r0 >= r2.length) goto L206;
     */
    /* JADX WARN: Code restructure failed: missing block: B:106:0x0267, code lost:
    
        r3 = r2[r0].setResultTo(r15).setOutActivity(r12).execute();
     */
    /* JADX WARN: Code restructure failed: missing block: B:107:0x0275, code lost:
    
        if (r3 >= 0) goto L123;
     */
    /* JADX WARN: Code restructure failed: missing block: B:108:0x0277, code lost:
    
        r4 = r0 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:110:0x027c, code lost:
    
        if (r4 >= r2.length) goto L211;
     */
    /* JADX WARN: Code restructure failed: missing block: B:112:0x0280, code lost:
    
        r22 = r6;
     */
    /* JADX WARN: Code restructure failed: missing block: B:113:0x0282, code lost:
    
        r29.mFactory.recycle(r2[r4]);
     */
    /* JADX WARN: Code restructure failed: missing block: B:114:0x0287, code lost:
    
        r4 = r4 + 1;
        r6 = r22;
     */
    /* JADX WARN: Code restructure failed: missing block: B:116:0x0291, code lost:
    
        r4 = r29.mService.mWindowManager.mStartingSurfaceController;
     */
    /* JADX WARN: Code restructure failed: missing block: B:117:0x0297, code lost:
    
        if (r39 == null) goto L119;
     */
    /* JADX WARN: Code restructure failed: missing block: B:118:0x0299, code lost:
    
        r17 = r39.getOriginalOptions();
     */
    /* JADX WARN: Code restructure failed: missing block: B:119:0x029d, code lost:
    
        r4.endDeferAddStartingWindow(r17);
        r29.mService.continueWindowLayout();
     */
    /* JADX WARN: Code restructure failed: missing block: B:120:0x02a7, code lost:
    
        monitor-exit(r13);
     */
    /* JADX WARN: Code restructure failed: missing block: B:121:0x02a8, code lost:
    
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        android.os.Binder.restoreCallingIdentity(r18);
     */
    /* JADX WARN: Code restructure failed: missing block: B:122:0x02ae, code lost:
    
        return r3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:123:0x02af, code lost:
    
        r22 = r6;
     */
    /* JADX WARN: Code restructure failed: missing block: B:124:0x02b1, code lost:
    
        r4 = r12[0];
     */
    /* JADX WARN: Code restructure failed: missing block: B:125:0x02b3, code lost:
    
        if (r4 == null) goto L129;
     */
    /* JADX WARN: Code restructure failed: missing block: B:127:0x02b9, code lost:
    
        if (r4.getUid() != r14) goto L129;
     */
    /* JADX WARN: Code restructure failed: missing block: B:128:0x02bb, code lost:
    
        r15 = r4.token;
     */
    /* JADX WARN: Code restructure failed: missing block: B:129:0x02bf, code lost:
    
        r15 = r38;
     */
    /* JADX WARN: Code restructure failed: missing block: B:130:0x02c3, code lost:
    
        if (r0 >= (r2.length - 1)) goto L210;
     */
    /* JADX WARN: Code restructure failed: missing block: B:131:0x02c5, code lost:
    
        r2[r0 + 1].getIntent().addFlags(268435456);
     */
    /* JADX WARN: Code restructure failed: missing block: B:132:0x02d2, code lost:
    
        r0 = r0 + 1;
        r6 = r22;
     */
    /* JADX WARN: Code restructure failed: missing block: B:133:0x02dd, code lost:
    
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:136:0x02e1, code lost:
    
        r0 = r29.mService.mWindowManager.mStartingSurfaceController;
     */
    /* JADX WARN: Code restructure failed: missing block: B:137:0x02e7, code lost:
    
        if (r39 == null) goto L139;
     */
    /* JADX WARN: Code restructure failed: missing block: B:138:0x02e9, code lost:
    
        r17 = r39.getOriginalOptions();
     */
    /* JADX WARN: Code restructure failed: missing block: B:139:0x02ed, code lost:
    
        r0.endDeferAddStartingWindow(r17);
        r29.mService.continueWindowLayout();
     */
    /* JADX WARN: Code restructure failed: missing block: B:140:0x02f8, code lost:
    
        monitor-exit(r13);
     */
    /* JADX WARN: Code restructure failed: missing block: B:141:0x02f9, code lost:
    
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
     */
    /* JADX WARN: Code restructure failed: missing block: B:142:0x02fc, code lost:
    
        android.os.Binder.restoreCallingIdentity(r18);
     */
    /* JADX WARN: Code restructure failed: missing block: B:143:0x0300, code lost:
    
        return 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:144:0x0301, code lost:
    
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:146:0x0304, code lost:
    
        r3 = r29.mService.mWindowManager.mStartingSurfaceController;
     */
    /* JADX WARN: Code restructure failed: missing block: B:147:0x030a, code lost:
    
        if (r39 != null) goto L148;
     */
    /* JADX WARN: Code restructure failed: missing block: B:148:0x030c, code lost:
    
        r17 = r39.getOriginalOptions();
     */
    /* JADX WARN: Code restructure failed: missing block: B:149:0x0310, code lost:
    
        r3.endDeferAddStartingWindow(r17);
        r29.mService.continueWindowLayout();
     */
    /* JADX WARN: Code restructure failed: missing block: B:150:0x031b, code lost:
    
        throw r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:151:0x031c, code lost:
    
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:153:0x0321, code lost:
    
        monitor-exit(r13);
     */
    /* JADX WARN: Code restructure failed: missing block: B:154:0x0322, code lost:
    
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
     */
    /* JADX WARN: Code restructure failed: missing block: B:155:0x0325, code lost:
    
        throw r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:156:0x0326, code lost:
    
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:158:0x0328, code lost:
    
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:160:0x032a, code lost:
    
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:83:0x01f3, code lost:
    
        r2 = r12;
        r7 = r13;
     */
    /* JADX WARN: Code restructure failed: missing block: B:86:0x0200, code lost:
    
        if (r7.size() <= 1) goto L98;
     */
    /* JADX WARN: Code restructure failed: missing block: B:87:0x0202, code lost:
    
        r0 = new java.lang.StringBuilder("startActivities: different apps [");
        r10 = r7.size();
        r12 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:88:0x020f, code lost:
    
        if (r12 >= r10) goto L203;
     */
    /* JADX WARN: Code restructure failed: missing block: B:89:0x0211, code lost:
    
        r13 = r0.append(r7.valueAt(r12));
     */
    /* JADX WARN: Code restructure failed: missing block: B:90:0x021d, code lost:
    
        if (r12 != (r10 - 1)) goto L92;
     */
    /* JADX WARN: Code restructure failed: missing block: B:91:0x021f, code lost:
    
        r15 = "]";
     */
    /* JADX WARN: Code restructure failed: missing block: B:92:0x0222, code lost:
    
        r15 = ", ";
     */
    /* JADX WARN: Code restructure failed: missing block: B:93:0x0224, code lost:
    
        r13.append(r15);
        r12 = r12 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:94:0x022a, code lost:
    
        r0.append(" from ").append(r34);
        android.util.Slog.wtf(com.android.server.wm.ActivityStartController.TAG, r0.toString());
     */
    /* JADX WARN: Code restructure failed: missing block: B:99:0x0247, code lost:
    
        r12 = new com.android.server.wm.ActivityRecord[1];
        r13 = r29.mService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    int startActivities(android.app.IApplicationThread r30, int r31, int r32, int r33, java.lang.String r34, java.lang.String r35, android.content.Intent[] r36, java.lang.String[] r37, android.os.IBinder r38, com.android.server.wm.SafeActivityOptions r39, int r40, java.lang.String r41, com.android.server.am.PendingIntentRecord r42, android.app.BackgroundStartPrivileges r43) {
        /*
            Method dump skipped, instruction units count: 864
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.wm.ActivityStartController.startActivities(android.app.IApplicationThread, int, int, int, java.lang.String, java.lang.String, android.content.Intent[], java.lang.String[], android.os.IBinder, com.android.server.wm.SafeActivityOptions, int, java.lang.String, com.android.server.am.PendingIntentRecord, android.app.BackgroundStartPrivileges):int");
    }

    static /* synthetic */ android.content.Intent[] lambda$startActivities$0(int x$0) {
        return new android.content.Intent[x$0];
    }

    int startActivityInTaskFragment(com.android.server.wm.TaskFragment taskFragment, android.content.Intent activityIntent, com.android.server.wm.SafeActivityOptions activityOptions, android.os.IBinder resultTo, int callingUid, int callingPid, android.os.IBinder errorCallbackToken) {
        com.android.server.wm.ActivityRecord caller = resultTo != null ? com.android.server.wm.ActivityRecord.forTokenLocked(resultTo) : null;
        java.lang.String resolvedType = activityIntent.resolveTypeIfNeeded(this.mService.mContext.getContentResolver());
        return obtainStarter(activityIntent, "startActivityInTaskFragment").setActivityOptions(activityOptions).setInTaskFragment(taskFragment).setResultTo(resultTo).setRequestCode(-1).setResolvedType(resolvedType).setCallingUid(callingUid).setCallingPid(callingPid).setRealCallingUid(callingUid).setRealCallingPid(callingPid).setUserId(caller != null ? caller.mUserId : this.mService.getCurrentUserId()).setErrorCallbackToken(errorCallbackToken).execute();
    }

    boolean startExistingRecentsIfPossible(android.content.Intent intent, android.app.ActivityOptions options) {
        try {
            android.os.Trace.traceBegin(32L, "startExistingRecents");
            if (startExistingRecents(intent, options)) {
                android.os.Trace.traceEnd(32L);
                return true;
            }
            android.os.Trace.traceEnd(32L);
            return false;
        } catch (java.lang.Throwable th) {
            android.os.Trace.traceEnd(32L);
            throw th;
        }
    }

    private boolean startExistingRecents(android.content.Intent intent, android.app.ActivityOptions options) {
        com.android.server.wm.ActivityRecord r;
        int activityType = this.mService.getRecentTasks().getRecentsComponent().equals(intent.getComponent()) ? 3 : 2;
        com.android.server.wm.Task rootTask = this.mService.mRootWindowContainer.getDefaultTaskDisplayArea().getRootTask(0, activityType);
        if (rootTask != null && (r = rootTask.topRunningActivity()) != null && ((!r.isVisibleRequested() || !rootTask.isTopRootTaskInDisplayArea()) && r.attachedToProcess() && r.mActivityComponent.equals(intent.getComponent()) && this.mService.isCallerRecents(r.getUid()) && !r.mDisplayContent.isKeyguardLocked())) {
            this.mService.mRootWindowContainer.startPowerModeLaunchIfNeeded(true, r);
            com.android.server.wm.ActivityMetricsLogger.LaunchingState launchingState = this.mSupervisor.getActivityMetricsLogger().notifyActivityLaunching(intent);
            com.android.server.wm.Task task = r.getTask();
            this.mService.deferWindowLayout();
            try {
                com.android.server.wm.TransitionController controller = r.mTransitionController;
                com.android.server.wm.Transition transition = controller.getCollectingTransition();
                if (transition != null) {
                    transition.setRemoteAnimationApp(r.app.getThread());
                    controller.setTransientLaunch(r, com.android.server.wm.TaskDisplayArea.getRootTaskAbove(rootTask));
                }
                task.moveToFront("startExistingRecents");
                task.mInResumeTopActivity = true;
                task.resumeTopActivity(null, options, true);
                this.mSupervisor.getActivityMetricsLogger().notifyActivityLaunched(launchingState, 2, false, r, options);
                return true;
            } finally {
                task.mInResumeTopActivity = false;
                this.mService.continueWindowLayout();
            }
        }
        return false;
    }

    void registerRemoteAnimationForNextActivityStart(java.lang.String packageName, android.view.RemoteAnimationAdapter adapter, android.os.IBinder launchCookie) {
        this.mPendingRemoteAnimationRegistry.addPendingAnimation(packageName, adapter, launchCookie);
    }

    com.android.server.wm.PendingRemoteAnimationRegistry getPendingRemoteAnimationRegistry() {
        return this.mPendingRemoteAnimationRegistry;
    }

    com.android.server.wm.ActivityRecord getLastStartActivity() {
        if (this.mLastStarter != null) {
            return this.mLastStarter.mStartActivity;
        }
        return null;
    }

    void dumpLastHomeActivityStartResult(java.io.PrintWriter pw, java.lang.String prefix) {
        pw.print(prefix);
        pw.print("mLastHomeActivityStartResult=");
        pw.println(this.mLastHomeActivityStartResult);
    }

    void dump(java.io.PrintWriter pw, java.lang.String prefix, java.lang.String dumpPackage) {
        boolean dumped = false;
        boolean dump = false;
        boolean dumpPackagePresent = dumpPackage != null;
        if (this.mLastHomeActivityStartRecord != null && (!dumpPackagePresent || dumpPackage.equals(this.mLastHomeActivityStartRecord.packageName))) {
            dumped = true;
            dumpLastHomeActivityStartResult(pw, prefix);
            pw.print(prefix);
            pw.println("mLastHomeActivityStartRecord:");
            this.mLastHomeActivityStartRecord.dump(pw, prefix + "  ", true);
        }
        if (this.mLastStarter != null) {
            if (!dumpPackagePresent || this.mLastStarter.relatedToPackage(dumpPackage) || (this.mLastHomeActivityStartRecord != null && dumpPackage.equals(this.mLastHomeActivityStartRecord.packageName))) {
                dump = true;
            }
            if (dump) {
                if (!dumped) {
                    dumped = true;
                    dumpLastHomeActivityStartResult(pw, prefix);
                }
                pw.print(prefix);
                pw.println("mLastStarter:");
                this.mLastStarter.dump(pw, prefix + "  ");
                if (dumpPackagePresent) {
                    return;
                }
            }
        }
        if (!dumped) {
            pw.print(prefix);
            pw.println("(nothing)");
        }
    }
}
