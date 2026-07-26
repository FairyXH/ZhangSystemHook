package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class ActivityStarter {
    static final long ASM_RESTRICTIONS = 230590090;
    static final long ENABLE_PENDING_INTENT_BAL_OPTION = 192341120;
    private static final int INVALID_LAUNCH_MODE = -1;
    private static final long MAX_TASK_WEIGHT_FOR_ADDING_ACTIVITY = 300;
    private static final int MOVE_TO_FRONT_ALLOWED = 0;
    private static final int MOVE_TO_FRONT_AVOID_LEGACY = 2;
    private static final int MOVE_TO_FRONT_AVOID_PI_ONLY_CREATOR_ALLOWS = 1;
    boolean mAddingToTask;
    private com.android.server.wm.TaskFragment mAddingToTaskFragment;
    private int mBalCode;
    private int mCallingUid;
    private final com.android.server.wm.ActivityStartController mController;
    private boolean mDisplayLockAndOccluded;
    private boolean mDoResume;
    private boolean mFrozeTaskList;
    private com.android.server.wm.Task mInTask;
    private com.android.server.wm.TaskFragment mInTaskFragment;
    private android.content.Intent mIntent;
    private boolean mIntentDelivered;
    private final com.android.server.wm.ActivityStartInterceptor mInterceptor;
    private boolean mIsTaskCleared;
    private com.android.server.wm.ActivityRecord mLastStartActivityRecord;
    private int mLastStartActivityResult;
    private long mLastStartActivityTimeMs;
    private java.lang.String mLastStartReason;
    private int mLaunchFlags;
    private int mLaunchMode;
    private boolean mLaunchTaskBehind;
    private boolean mMovedToFront;
    com.android.server.wm.ActivityRecord mMovedToTopActivity;
    private boolean mNoAnimation;
    private com.android.server.wm.ActivityRecord mNotTop;
    private android.app.ActivityOptions mOptions;
    private com.android.server.wm.TaskDisplayArea mPreferredTaskDisplayArea;
    private int mPreferredWindowingMode;
    private com.android.server.wm.Task mPriorAboveTask;
    private int mRealCallingUid;
    private final com.android.server.wm.RootWindowContainer mRootWindowContainer;
    private final com.android.server.wm.ActivityTaskManagerService mService;
    private com.android.server.wm.ActivityRecord mSourceRecord;
    private com.android.server.wm.Task mSourceRootTask;
    com.android.server.wm.ActivityRecord mStartActivity;
    private int mStartFlags;
    private final com.android.server.wm.ActivityTaskSupervisor mSupervisor;
    private com.android.server.wm.Task mTargetRootTask;
    private com.android.server.wm.Task mTargetTask;
    private boolean mTransientLaunch;
    private com.android.internal.app.IVoiceInteractor mVoiceInteractor;
    private android.service.voice.IVoiceInteractionSession mVoiceSession;
    private static final java.lang.String TAG = "ActivityTaskManager";
    private static final java.lang.String TAG_RESULTS = TAG + com.android.server.wm.ActivityTaskManagerDebugConfig.POSTFIX_RESULTS;
    private static final java.lang.String TAG_FOCUS = TAG + com.android.server.wm.ActivityTaskManagerDebugConfig.POSTFIX_FOCUS;
    private static final java.lang.String TAG_CONFIGURATION = TAG + com.android.server.wm.ActivityTaskManagerDebugConfig.POSTFIX_CONFIGURATION;
    private static final java.lang.String TAG_USER_LEAVING = TAG + com.android.server.wm.ActivityTaskManagerDebugConfig.POSTFIX_USER_LEAVING;
    private com.android.server.wm.LaunchParamsController.LaunchParams mLaunchParams = new com.android.server.wm.LaunchParamsController.LaunchParams();
    private int mCanMoveToFrontCode = 0;
    public com.android.server.zenmode.IZenModeManagerExt mZenModeManagerExt = (com.android.server.zenmode.IZenModeManagerExt) system.ext.loader.core.ExtLoader.type(com.android.server.zenmode.IZenModeManagerExt.class).create();
    private com.oplus.uifirst.IOplusUIFirstManagerExt mUIFirstManagerExt = (com.oplus.uifirst.IOplusUIFirstManagerExt) system.ext.loader.core.ExtLoader.type(com.oplus.uifirst.IOplusUIFirstManagerExt.class).create();
    com.android.server.wm.ActivityStarter.Request mRequest = new com.android.server.wm.ActivityStarter.Request();
    private com.android.server.wm.ActivityStarter.ActivityStarterWrapper mASWrapper = new com.android.server.wm.ActivityStarter.ActivityStarterWrapper();

    interface Factory {
        com.android.server.wm.ActivityStarter obtain();

        void recycle(com.android.server.wm.ActivityStarter activityStarter);

        void setController(com.android.server.wm.ActivityStartController activityStartController);
    }

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface MoveToFrontCode {
    }

    static class DefaultFactory implements com.android.server.wm.ActivityStarter.Factory {
        private com.android.server.wm.ActivityStartController mController;
        private com.android.server.wm.ActivityStartInterceptor mInterceptor;
        private com.android.server.wm.ActivityTaskManagerService mService;
        private com.android.server.wm.ActivityTaskSupervisor mSupervisor;
        private final int MAX_STARTER_COUNT = 3;
        private android.util.Pools.SynchronizedPool<com.android.server.wm.ActivityStarter> mStarterPool = new android.util.Pools.SynchronizedPool<>(3);

        DefaultFactory(com.android.server.wm.ActivityTaskManagerService service, com.android.server.wm.ActivityTaskSupervisor supervisor, com.android.server.wm.ActivityStartInterceptor interceptor) {
            this.mService = service;
            this.mSupervisor = supervisor;
            this.mInterceptor = interceptor;
        }

        @Override // com.android.server.wm.ActivityStarter.Factory
        public void setController(com.android.server.wm.ActivityStartController controller) {
            this.mController = controller;
        }

        @Override // com.android.server.wm.ActivityStarter.Factory
        public com.android.server.wm.ActivityStarter obtain() {
            com.android.server.wm.ActivityStarter starter = (com.android.server.wm.ActivityStarter) this.mStarterPool.acquire();
            if (starter == null) {
                if (this.mService.mRootWindowContainer == null) {
                    throw new java.lang.IllegalStateException("Too early to start activity.");
                }
                return new com.android.server.wm.ActivityStarter(this.mController, this.mService, this.mSupervisor, this.mInterceptor);
            }
            return starter;
        }

        @Override // com.android.server.wm.ActivityStarter.Factory
        public void recycle(com.android.server.wm.ActivityStarter starter) {
            starter.reset(true);
            this.mStarterPool.release(starter);
        }
    }

    static class Request {
        private static final int DEFAULT_CALLING_PID = 0;
        private static final int DEFAULT_CALLING_UID = -1;
        static final int DEFAULT_REAL_CALLING_PID = 0;
        static final int DEFAULT_REAL_CALLING_UID = -1;
        android.content.pm.ActivityInfo activityInfo;
        com.android.server.wm.SafeActivityOptions activityOptions;
        boolean allowPendingRemoteAnimationRegistryLookup;
        boolean avoidMoveToFront;
        android.app.IApplicationThread caller;
        java.lang.String callingFeatureId;
        java.lang.String callingPackage;
        boolean componentSpecified;
        android.content.Intent ephemeralIntent;
        android.os.IBinder errorCallbackToken;
        int filterCallingUid;
        android.app.BackgroundStartPrivileges forcedBalByPiSender;
        boolean freezeScreen;
        android.content.res.Configuration globalConfig;
        boolean ignoreTargetSecurity;
        com.android.server.wm.Task inTask;
        com.android.server.wm.TaskFragment inTaskFragment;
        android.content.Intent intent;
        com.android.server.uri.NeededUriGrants intentGrants;
        com.android.server.am.PendingIntentRecord originatingPendingIntent;
        com.android.server.wm.ActivityRecord[] outActivity;
        android.app.ProfilerInfo profilerInfo;
        java.lang.String reason;
        int requestCode;
        android.content.pm.ResolveInfo resolveInfo;
        java.lang.String resolvedType;
        android.os.IBinder resultTo;
        java.lang.String resultWho;
        int startFlags;
        int userId;
        com.android.internal.app.IVoiceInteractor voiceInteractor;
        android.service.voice.IVoiceInteractionSession voiceSession;
        android.app.WaitResult waitResult;
        int callingPid = 0;
        int callingUid = -1;
        int realCallingPid = 0;
        int realCallingUid = -1;
        final java.lang.StringBuilder logMessage = new java.lang.StringBuilder();

        Request() {
            reset();
        }

        void reset() {
            this.caller = null;
            this.intent = null;
            this.intentGrants = null;
            this.ephemeralIntent = null;
            this.resolvedType = null;
            this.activityInfo = null;
            this.resolveInfo = null;
            this.voiceSession = null;
            this.voiceInteractor = null;
            this.resultTo = null;
            this.resultWho = null;
            this.requestCode = 0;
            this.callingPid = 0;
            this.callingUid = -1;
            this.callingPackage = null;
            this.callingFeatureId = null;
            this.realCallingPid = 0;
            this.realCallingUid = -1;
            this.startFlags = 0;
            this.activityOptions = null;
            this.ignoreTargetSecurity = false;
            this.componentSpecified = false;
            this.outActivity = null;
            this.inTask = null;
            this.inTaskFragment = null;
            this.reason = null;
            this.profilerInfo = null;
            this.globalConfig = null;
            this.userId = 0;
            this.waitResult = null;
            this.avoidMoveToFront = false;
            this.allowPendingRemoteAnimationRegistryLookup = true;
            this.filterCallingUid = -10000;
            this.originatingPendingIntent = null;
            this.forcedBalByPiSender = android.app.BackgroundStartPrivileges.NONE;
            this.freezeScreen = false;
            this.errorCallbackToken = null;
        }

        void set(com.android.server.wm.ActivityStarter.Request request) {
            this.caller = request.caller;
            this.intent = request.intent;
            this.intentGrants = request.intentGrants;
            this.ephemeralIntent = request.ephemeralIntent;
            this.resolvedType = request.resolvedType;
            this.activityInfo = request.activityInfo;
            this.resolveInfo = request.resolveInfo;
            this.voiceSession = request.voiceSession;
            this.voiceInteractor = request.voiceInteractor;
            this.resultTo = request.resultTo;
            this.resultWho = request.resultWho;
            this.requestCode = request.requestCode;
            this.callingPid = request.callingPid;
            this.callingUid = request.callingUid;
            this.callingPackage = request.callingPackage;
            this.callingFeatureId = request.callingFeatureId;
            this.realCallingPid = request.realCallingPid;
            this.realCallingUid = request.realCallingUid;
            this.startFlags = request.startFlags;
            this.activityOptions = request.activityOptions;
            this.ignoreTargetSecurity = request.ignoreTargetSecurity;
            this.componentSpecified = request.componentSpecified;
            this.outActivity = request.outActivity;
            this.inTask = request.inTask;
            this.inTaskFragment = request.inTaskFragment;
            this.reason = request.reason;
            this.profilerInfo = request.profilerInfo;
            this.globalConfig = request.globalConfig;
            this.userId = request.userId;
            this.waitResult = request.waitResult;
            this.avoidMoveToFront = request.avoidMoveToFront;
            this.allowPendingRemoteAnimationRegistryLookup = request.allowPendingRemoteAnimationRegistryLookup;
            this.filterCallingUid = request.filterCallingUid;
            this.originatingPendingIntent = request.originatingPendingIntent;
            this.forcedBalByPiSender = request.forcedBalByPiSender;
            this.freezeScreen = request.freezeScreen;
            this.errorCallbackToken = request.errorCallbackToken;
        }

        void resolveActivity(com.android.server.wm.ActivityTaskSupervisor supervisor) {
            supervisor.getWrapper().getExtImpl().resolveActivity(this.intent);
            if (this.realCallingPid == 0) {
                this.realCallingPid = android.os.Binder.getCallingPid();
            }
            if (this.realCallingUid == -1) {
                this.realCallingUid = android.os.Binder.getCallingUid();
            }
            if (this.callingUid >= 0) {
                this.callingPid = -1;
            } else if (this.caller == null) {
                this.callingPid = this.realCallingPid;
                this.callingUid = this.realCallingUid;
            } else {
                this.callingUid = -1;
                this.callingPid = -1;
            }
            int resolvedCallingUid = this.callingUid;
            if (this.caller != null) {
                com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = supervisor.mService.mGlobalLock;
                com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
                synchronized (windowManagerGlobalLock) {
                    try {
                        com.android.server.wm.WindowProcessController callerApp = supervisor.mService.getProcessController(this.caller);
                        if (callerApp != null) {
                            resolvedCallingUid = callerApp.mInfo.uid;
                        }
                    } catch (java.lang.Throwable th) {
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        throw th;
                    }
                }
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            }
            int resolvedCallingUid2 = supervisor.getWrapper().getExtImpl().resolvedCallingUid(com.android.server.wm.ActivityRecord.forTokenLocked(this.resultTo), this.intent, resolvedCallingUid);
            this.ephemeralIntent = new android.content.Intent(this.intent);
            this.intent = new android.content.Intent(this.intent);
            if (this.intent.getComponent() != null && ((!"android.intent.action.VIEW".equals(this.intent.getAction()) || this.intent.getData() != null) && !"android.intent.action.INSTALL_INSTANT_APP_PACKAGE".equals(this.intent.getAction()) && !"android.intent.action.RESOLVE_INSTANT_APP_PACKAGE".equals(this.intent.getAction()) && supervisor.mService.getPackageManagerInternalLocked().isInstantAppInstallerComponent(this.intent.getComponent()))) {
                this.intent.setComponent(null);
            }
            this.resolveInfo = supervisor.resolveIntent(this.intent, this.resolvedType, this.userId, 0, com.android.server.wm.ActivityStarter.computeResolveFilterUid(this.callingUid, this.realCallingUid, this.filterCallingUid), this.realCallingPid);
            if (this.resolveInfo == null) {
                this.resolveInfo = resolveIntentForLockedOrStoppedProfiles(supervisor);
            }
            this.resolveInfo = supervisor.getWrapper().getExtImpl().getMultiAppResolveInfoIfNeed(this.resolveInfo, this.userId, supervisor, this.intent, this.resolvedType, com.android.server.wm.ActivityStarter.computeResolveFilterUid(this.callingUid, this.realCallingUid, this.filterCallingUid), this.callingPid);
            this.activityInfo = supervisor.resolveActivity(this.intent, this.resolveInfo, this.startFlags, this.profilerInfo);
            if (this.activityInfo != null) {
                if (android.security.Flags.contentUriPermissionApis()) {
                    this.intentGrants = supervisor.mService.mUgmInternal.checkGrantUriPermissionFromIntent(this.intent, resolvedCallingUid2, this.activityInfo.applicationInfo.packageName, android.os.UserHandle.getUserId(this.activityInfo.applicationInfo.uid), this.activityInfo.requireContentUriPermissionFromCaller);
                } else {
                    this.intentGrants = supervisor.mService.mUgmInternal.checkGrantUriPermissionFromIntent(this.intent, resolvedCallingUid2, this.activityInfo.applicationInfo.packageName, android.os.UserHandle.getUserId(this.activityInfo.applicationInfo.uid));
                }
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:14:0x0035  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        android.content.pm.ResolveInfo resolveIntentForLockedOrStoppedProfiles(com.android.server.wm.ActivityTaskSupervisor r13) {
            /*
                r12 = this;
                int r0 = r12.userId
                android.content.pm.UserInfo r0 = r13.getUserInfo(r0)
                if (r0 == 0) goto L5c
                boolean r1 = r0.isProfile()
                if (r1 == 0) goto L5c
                com.android.server.wm.ActivityTaskManagerService r1 = r13.mService
                android.content.Context r1 = r1.mContext
                android.os.UserManager r1 = android.os.UserManager.get(r1)
                r2 = 0
                long r3 = android.os.Binder.clearCallingIdentity()
                int r5 = r12.userId     // Catch: java.lang.Throwable -> L57
                android.content.pm.UserInfo r5 = r1.getProfileParent(r5)     // Catch: java.lang.Throwable -> L57
                if (r5 == 0) goto L35
                int r6 = r5.id     // Catch: java.lang.Throwable -> L57
                boolean r6 = r1.isUserUnlockingOrUnlocked(r6)     // Catch: java.lang.Throwable -> L57
                if (r6 == 0) goto L35
                int r6 = r12.userId     // Catch: java.lang.Throwable -> L57
                boolean r6 = r1.isUserUnlockingOrUnlocked(r6)     // Catch: java.lang.Throwable -> L57
                if (r6 != 0) goto L35
                r6 = 1
                goto L36
            L35:
                r6 = 0
            L36:
                r2 = r6
                android.os.Binder.restoreCallingIdentity(r3)
                if (r2 == 0) goto L5c
                android.content.Intent r6 = r12.intent
                java.lang.String r7 = r12.resolvedType
                int r8 = r12.userId
                int r5 = r12.callingUid
                int r9 = r12.realCallingUid
                int r10 = r12.filterCallingUid
                int r10 = com.android.server.wm.ActivityStarter.computeResolveFilterUid(r5, r9, r10)
                int r11 = r12.realCallingPid
                r9 = 786432(0xc0000, float:1.102026E-39)
                r5 = r13
                android.content.pm.ResolveInfo r5 = r5.resolveIntent(r6, r7, r8, r9, r10, r11)
                return r5
            L57:
                r5 = move-exception
                android.os.Binder.restoreCallingIdentity(r3)
                throw r5
            L5c:
                r1 = 0
                return r1
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.wm.ActivityStarter.Request.resolveIntentForLockedOrStoppedProfiles(com.android.server.wm.ActivityTaskSupervisor):android.content.pm.ResolveInfo");
        }
    }

    ActivityStarter(com.android.server.wm.ActivityStartController controller, com.android.server.wm.ActivityTaskManagerService service, com.android.server.wm.ActivityTaskSupervisor supervisor, com.android.server.wm.ActivityStartInterceptor interceptor) {
        this.mController = controller;
        this.mService = service;
        this.mRootWindowContainer = service.mRootWindowContainer;
        this.mSupervisor = supervisor;
        this.mInterceptor = interceptor;
        reset(true);
        this.mASWrapper.getSocExtImpl().initSoc();
    }

    void set(com.android.server.wm.ActivityStarter starter) {
        this.mStartActivity = starter.mStartActivity;
        this.mIntent = starter.mIntent;
        this.mCallingUid = starter.mCallingUid;
        this.mRealCallingUid = starter.mRealCallingUid;
        this.mOptions = starter.mOptions;
        this.mBalCode = starter.mBalCode;
        this.mLaunchTaskBehind = starter.mLaunchTaskBehind;
        this.mLaunchFlags = starter.mLaunchFlags;
        this.mLaunchMode = starter.mLaunchMode;
        this.mLaunchParams.set(starter.mLaunchParams);
        this.mNotTop = starter.mNotTop;
        this.mDoResume = starter.mDoResume;
        this.mStartFlags = starter.mStartFlags;
        this.mSourceRecord = starter.mSourceRecord;
        this.mPreferredTaskDisplayArea = starter.mPreferredTaskDisplayArea;
        this.mPreferredWindowingMode = starter.mPreferredWindowingMode;
        this.mInTask = starter.mInTask;
        this.mInTaskFragment = starter.mInTaskFragment;
        this.mAddingToTask = starter.mAddingToTask;
        this.mSourceRootTask = starter.mSourceRootTask;
        this.mTargetTask = starter.mTargetTask;
        this.mTargetRootTask = starter.mTargetRootTask;
        this.mIsTaskCleared = starter.mIsTaskCleared;
        this.mMovedToFront = starter.mMovedToFront;
        this.mNoAnimation = starter.mNoAnimation;
        this.mCanMoveToFrontCode = starter.mCanMoveToFrontCode;
        this.mFrozeTaskList = starter.mFrozeTaskList;
        this.mVoiceSession = starter.mVoiceSession;
        this.mVoiceInteractor = starter.mVoiceInteractor;
        this.mIntentDelivered = starter.mIntentDelivered;
        this.mLastStartActivityResult = starter.mLastStartActivityResult;
        this.mLastStartActivityTimeMs = starter.mLastStartActivityTimeMs;
        this.mLastStartReason = starter.mLastStartReason;
        this.mRequest.set(starter.mRequest);
    }

    boolean relatedToPackage(java.lang.String packageName) {
        return (this.mLastStartActivityRecord != null && packageName.equals(this.mLastStartActivityRecord.packageName)) || (this.mStartActivity != null && packageName.equals(this.mStartActivity.packageName));
    }

    /* JADX WARN: Finally extract failed */
    /* JADX WARN: Removed duplicated region for block: B:85:0x02cd  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    int execute() {
        /*
            Method dump skipped, instruction units count: 1190
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.wm.ActivityStarter.execute():int");
    }

    private int resolveToHeavyWeightSwitcherIfNeeded() throws java.lang.Throwable {
        com.android.server.wm.WindowProcessController heavy;
        if (this.mRequest.activityInfo == null || !this.mService.mHasHeavyWeightFeature || (this.mRequest.activityInfo.applicationInfo.privateFlags & 2) == 0 || !this.mRequest.activityInfo.processName.equals(this.mRequest.activityInfo.applicationInfo.packageName) || (heavy = this.mService.mHeavyWeightProcess) == null || (heavy.mInfo.uid == this.mRequest.activityInfo.applicationInfo.uid && heavy.mName.equals(this.mRequest.activityInfo.processName))) {
            return 0;
        }
        int appCallingUid = this.mRequest.callingUid;
        if (this.mRequest.caller != null) {
            com.android.server.wm.WindowProcessController callerApp = this.mService.getProcessController(this.mRequest.caller);
            if (callerApp != null) {
                appCallingUid = callerApp.mInfo.uid;
            } else {
                android.util.Slog.w(TAG, "Unable to find app for caller " + this.mRequest.caller + " (pid=" + this.mRequest.callingPid + ") when starting: " + this.mRequest.intent.toString());
                com.android.server.wm.SafeActivityOptions.abort(this.mRequest.activityOptions);
                return -94;
            }
        }
        android.content.IIntentSender target = this.mService.getIntentSenderLocked(2, com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME, null, appCallingUid, this.mRequest.userId, null, null, 0, new android.content.Intent[]{this.mRequest.intent}, new java.lang.String[]{this.mRequest.resolvedType}, 1342177280, null);
        android.content.Intent newIntent = new android.content.Intent();
        if (this.mRequest.requestCode >= 0) {
            newIntent.putExtra("has_result", true);
        }
        newIntent.putExtra("intent", new android.content.IntentSender(target));
        heavy.updateIntentForHeavyWeightActivity(newIntent);
        newIntent.putExtra("new_app", this.mRequest.activityInfo.packageName);
        newIntent.setFlags(this.mRequest.intent.getFlags());
        newIntent.setClassName(com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME, com.android.internal.app.HeavyWeightSwitcherActivity.class.getName());
        this.mRequest.intent = newIntent;
        this.mRequest.resolvedType = null;
        this.mRequest.caller = null;
        this.mRequest.callingUid = android.os.Binder.getCallingUid();
        this.mRequest.callingPid = android.os.Binder.getCallingPid();
        this.mRequest.componentSpecified = true;
        this.mRequest.resolveInfo = this.mSupervisor.resolveIntent(this.mRequest.intent, null, this.mRequest.userId, 0, computeResolveFilterUid(this.mRequest.callingUid, this.mRequest.realCallingUid, this.mRequest.filterCallingUid), this.mRequest.realCallingPid);
        this.mRequest.activityInfo = this.mRequest.resolveInfo != null ? this.mRequest.resolveInfo.activityInfo : null;
        if (this.mRequest.activityInfo != null) {
            this.mRequest.activityInfo = this.mService.mAmInternal.getActivityInfoForUser(this.mRequest.activityInfo, this.mRequest.userId);
        }
        return 0;
    }

    private int waitResultIfNeeded(android.app.WaitResult waitResult, com.android.server.wm.ActivityRecord r, com.android.server.wm.ActivityMetricsLogger.LaunchingState launchingState) {
        int res = waitResult.result;
        if (res == 3 || (res == 2 && r.nowVisible && r.isState(com.android.server.wm.ActivityRecord.State.RESUMED))) {
            waitResult.timeout = false;
            waitResult.who = r.mActivityComponent;
            waitResult.totalTime = 0L;
            return res;
        }
        this.mSupervisor.waitActivityVisibleOrLaunched(waitResult, r, launchingState);
        if (res == 0 && waitResult.result == 2) {
            return 2;
        }
        return res;
    }

    /* JADX WARN: Removed duplicated region for block: B:231:0x07ed  */
    /* JADX WARN: Removed duplicated region for block: B:237:0x0817  */
    /* JADX WARN: Removed duplicated region for block: B:240:0x086a  */
    /* JADX WARN: Removed duplicated region for block: B:241:0x086c  */
    /* JADX WARN: Removed duplicated region for block: B:244:0x08a0 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:245:0x08a1  */
    /* JADX WARN: Removed duplicated region for block: B:266:0x0947  */
    /* JADX WARN: Removed duplicated region for block: B:268:0x0950  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private int executeRequest(com.android.server.wm.ActivityStarter.Request r73) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 2525
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.wm.ActivityStarter.executeRequest(com.android.server.wm.ActivityStarter$Request):int");
    }

    private boolean handleBackgroundActivityAbort(com.android.server.wm.ActivityRecord r) {
        boolean abort = !this.mService.isBackgroundActivityStartsEnabled();
        if (!abort) {
            return false;
        }
        com.android.server.wm.ActivityRecord resultRecord = r.resultTo;
        java.lang.String resultWho = r.resultWho;
        int requestCode = r.requestCode;
        if (resultRecord != null) {
            resultRecord.sendResult(-1, resultWho, requestCode, 0, null, null, null);
        }
        android.app.ActivityOptions.abort(r.getOptions());
        return true;
    }

    static int getExternalResult(int result) {
        if (result != 102) {
            return result;
        }
        return 0;
    }

    private void onExecutionComplete() {
        this.mASWrapper.getExtImpl().notifyActivityLaunched(this.mService, this.mSupervisor, this.mRequest.intent);
        this.mController.onExecutionComplete(this);
    }

    private void onExecutionStarted() {
        this.mController.onExecutionStarted();
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mSupervisor.getWrapper().getExtImpl().notifyActivityLaunching(this.mRequest.intent, com.android.server.wm.ActivityRecord.forTokenLocked(this.mRequest.resultTo));
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    private android.content.Intent createLaunchIntent(android.content.pm.AuxiliaryResolveInfo auxiliaryResponse, android.content.Intent originalIntent, java.lang.String callingPackage, java.lang.String callingFeatureId, android.os.Bundle verificationBundle, java.lang.String resolvedType, int userId) {
        android.content.Intent intent;
        android.content.ComponentName componentName;
        java.lang.String str;
        if (auxiliaryResponse != null && auxiliaryResponse.needsPhaseTwo) {
            android.content.pm.PackageManagerInternal packageManager = this.mService.getPackageManagerInternalLocked();
            boolean isRequesterInstantApp = packageManager.isInstantApp(callingPackage, userId);
            packageManager.requestInstantAppResolutionPhaseTwo(auxiliaryResponse, originalIntent, resolvedType, callingPackage, callingFeatureId, isRequesterInstantApp, verificationBundle, userId);
        }
        android.content.Intent intentSanitizeIntent = com.android.server.pm.InstantAppResolver.sanitizeIntent(originalIntent);
        java.util.List list = null;
        if (auxiliaryResponse != null) {
            intent = auxiliaryResponse.failureIntent;
        } else {
            intent = null;
        }
        if (auxiliaryResponse != null) {
            componentName = auxiliaryResponse.installFailureActivity;
        } else {
            componentName = null;
        }
        if (auxiliaryResponse != null) {
            str = auxiliaryResponse.token;
        } else {
            str = null;
        }
        boolean z = auxiliaryResponse != null && auxiliaryResponse.needsPhaseTwo;
        if (auxiliaryResponse != null) {
            list = auxiliaryResponse.filters;
        }
        return com.android.server.pm.InstantAppResolver.buildEphemeralInstallerIntent(originalIntent, intentSanitizeIntent, intent, callingPackage, callingFeatureId, verificationBundle, resolvedType, userId, componentName, str, z, list);
    }

    void postStartActivityProcessing(com.android.server.wm.ActivityRecord r, int result, com.android.server.wm.Task startedActivityRootTask) {
        com.android.server.wm.Task targetTask;
        if (!android.app.ActivityManager.isStartResultSuccessful(result) && this.mFrozeTaskList) {
            this.mSupervisor.mRecentTasks.resetFreezeTaskListReorderingOnTimeout();
        }
        if (android.app.ActivityManager.isStartResultFatalError(result)) {
            return;
        }
        this.mSupervisor.reportWaitingActivityLaunchedIfNeeded(r, result);
        if (r.getTask() != null) {
            targetTask = r.getTask();
        } else {
            targetTask = this.mTargetTask;
        }
        if (startedActivityRootTask == null || targetTask == null || !targetTask.isAttached()) {
            return;
        }
        if (result == 2 || result == 3) {
            com.android.server.wm.TaskDisplayArea taskDisplayArea = targetTask.getDisplayArea();
            boolean homeTaskVisible = false;
            if (taskDisplayArea != null) {
                com.android.server.wm.Task rootHomeTask = taskDisplayArea.getRootHomeTask();
                homeTaskVisible = rootHomeTask != null && rootHomeTask.shouldBeVisible(null);
            }
            com.android.server.wm.ActivityRecord top = targetTask.getTopNonFinishingActivity();
            boolean visible = top != null && top.isVisible();
            this.mService.getTaskChangeNotificationController().notifyActivityRestartAttempt(targetTask.getTaskInfo(), homeTaskVisible, this.mIsTaskCleared, visible);
        }
        if (android.app.ActivityManager.isStartResultSuccessful(result)) {
            this.mInterceptor.onActivityLaunched(targetTask.getTaskInfo(), r);
        }
        this.mASWrapper.getExtImpl().hookPostStartActivityProcessing(result, targetTask, r);
    }

    static int computeResolveFilterUid(int customCallingUid, int actualCallingUid, int filterCallingUid) {
        if (filterCallingUid != -10000) {
            return filterCallingUid;
        }
        return customCallingUid >= 0 ? customCallingUid : actualCallingUid;
    }

    /* JADX WARN: Not initialized variable reg: 12, insn: 0x028f: MOVE (r3 I:??[OBJECT, ARRAY]) = (r12 I:??[OBJECT, ARRAY] A[D('newTransition' com.android.server.wm.Transition)]), block:B:112:0x028f */
    /* JADX WARN: Removed duplicated region for block: B:108:0x026e A[Catch: all -> 0x028e, TryCatch #7 {all -> 0x028e, blocks: (B:102:0x0233, B:105:0x025c, B:106:0x026a, B:108:0x026e, B:109:0x027f, B:110:0x028d, B:86:0x01d5, B:89:0x01dd, B:90:0x01eb, B:92:0x01ef, B:93:0x0200), top: B:127:0x00e5 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private int startActivityUnchecked(com.android.server.wm.ActivityRecord r17, com.android.server.wm.ActivityRecord r18, android.service.voice.IVoiceInteractionSession r19, com.android.internal.app.IVoiceInteractor r20, int r21, android.app.ActivityOptions r22, com.android.server.wm.Task r23, com.android.server.wm.TaskFragment r24, com.android.server.wm.BackgroundActivityStartController.BalVerdict r25, com.android.server.uri.NeededUriGrants r26, int r27) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 668
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.wm.ActivityStarter.startActivityUnchecked(com.android.server.wm.ActivityRecord, com.android.server.wm.ActivityRecord, android.service.voice.IVoiceInteractionSession, com.android.internal.app.IVoiceInteractor, int, android.app.ActivityOptions, com.android.server.wm.Task, com.android.server.wm.TaskFragment, com.android.server.wm.BackgroundActivityStartController$BalVerdict, com.android.server.uri.NeededUriGrants, int):int");
    }

    private boolean avoidMoveToFront() {
        return this.mCanMoveToFrontCode != 0;
    }

    private boolean avoidMoveToFrontPIOnlyCreatorAllows() {
        return this.mCanMoveToFrontCode == 1;
    }

    /* JADX WARN: Removed duplicated region for block: B:72:0x00dc  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x00fd  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x0102  */
    /* JADX WARN: Removed duplicated region for block: B:81:0x012e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private com.android.server.wm.Task handleStartResult(com.android.server.wm.ActivityRecord r22, android.app.ActivityOptions r23, int r24, com.android.server.wm.Transition r25, android.window.RemoteTransition r26) {
        /*
            Method dump skipped, instruction units count: 451
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.wm.ActivityStarter.handleStartResult(com.android.server.wm.ActivityRecord, android.app.ActivityOptions, int, com.android.server.wm.Transition, android.window.RemoteTransition):com.android.server.wm.Task");
    }

    int startActivityInner(com.android.server.wm.ActivityRecord r, com.android.server.wm.ActivityRecord sourceRecord, android.service.voice.IVoiceInteractionSession voiceSession, com.android.internal.app.IVoiceInteractor voiceInteractor, int startFlags, android.app.ActivityOptions options, com.android.server.wm.Task inTask, com.android.server.wm.TaskFragment inTaskFragment, com.android.server.wm.BackgroundActivityStartController.BalVerdict balVerdict, com.android.server.uri.NeededUriGrants intentGrants, int realCallingUid) throws java.lang.Throwable {
        boolean dreamStopping;
        boolean z;
        com.android.server.wm.Task targetTask;
        com.android.server.wm.Task targetTask2;
        com.android.server.wm.Task prevTopTask;
        boolean dreamStopping2;
        com.android.server.wm.ActivityRecord activityRecord;
        android.app.ActivityOptions options2;
        int i;
        com.android.server.wm.Task prevTopTask2;
        boolean resumeTopAvoidMoveTask;
        com.android.server.uri.NeededUriGrants neededUriGrants;
        android.app.ActivityOptions options3;
        boolean z2;
        com.android.server.wm.Task targetTask3;
        com.android.server.wm.Task reusedTask;
        java.lang.String str;
        com.android.server.wm.ActivityRecord activity;
        this.mASWrapper.getExtImpl().hookActivityBoost();
        android.app.ActivityOptions options4 = this.mASWrapper.getExtImpl().adjustOptionsForFlexibleWindow(this.mASWrapper.getExtImpl().createOptionsForZoom(this.mASWrapper.getExtImpl().adjustOptionsForSplitScreen(this.mASWrapper.getExtImpl().modifyOptionsForCompactModeIfNeed(options, r, sourceRecord), r), sourceRecord, r, this.mRequest.realCallingPid), sourceRecord, r);
        setInitialState(r, options4, inTask, inTaskFragment, startFlags, sourceRecord, voiceSession, voiceInteractor, balVerdict.getCode(), realCallingUid);
        computeLaunchingTaskFlags();
        this.mLaunchFlags = this.mASWrapper.getExtImpl().adjustLaunchFlagsForFlexible(this.mSourceRecord, options4, this.mLaunchFlags);
        this.mIntent.setFlags(this.mLaunchFlags);
        java.util.Iterator<com.android.server.wm.ActivityRecord> it = this.mSupervisor.mStoppingActivities.iterator();
        while (true) {
            if (!it.hasNext()) {
                dreamStopping = false;
                break;
            }
            com.android.server.wm.ActivityRecord stoppingActivity = it.next();
            if (stoppingActivity.getActivityType() == 5) {
                dreamStopping = true;
                break;
            }
        }
        com.android.server.wm.Task prevTopRootTask = this.mPreferredTaskDisplayArea.getFocusedRootTask();
        com.android.server.wm.Task prevTopTask3 = prevTopRootTask != null ? prevTopRootTask.getTopLeafTask() : null;
        boolean sourceActivityLaunchedFromBubble = sourceRecord != null && sourceRecord.getLaunchedFromBubble();
        if (!com.android.wm.shell.Flags.onlyReuseBubbledTaskWhenLaunchedFromBubble()) {
            z = true;
        } else {
            z = sourceActivityLaunchedFromBubble;
        }
        boolean includeLaunchedFromBubble = z;
        com.android.server.wm.Task reusedTask2 = resolveReusableTask(includeLaunchedFromBubble);
        this.mASWrapper.getExtImpl().changeReusedTask(reusedTask2);
        com.android.server.wm.Task reusedTask3 = this.mASWrapper.getExtImpl().changeReusedTaskForAppInner(reusedTask2, r, this.mPreferredTaskDisplayArea);
        android.util.Pair<java.lang.Boolean, com.android.server.wm.Task> passwordActivity = this.mASWrapper.getExtImpl().isAppUnlockPasswordActivity(this.mRootWindowContainer, options4, this.mOptions, this.mAddingToTask, r, sourceRecord);
        if (passwordActivity != null) {
            this.mAddingToTask = ((java.lang.Boolean) passwordActivity.first).booleanValue();
            this.mInTask = (com.android.server.wm.Task) passwordActivity.second;
        }
        if (this.mOptions != null && this.mOptions.freezeRecentTasksReordering() && this.mSupervisor.mRecentTasks.isCallerRecents(r.launchedFromUid) && !this.mSupervisor.mRecentTasks.isFreezeTaskListReorderingSet()) {
            this.mFrozeTaskList = true;
            this.mSupervisor.mRecentTasks.setFreezeTaskListReordering();
        }
        com.android.server.wm.Task targetTask4 = this.mASWrapper.getExtImpl().handleReuseTaskForFlexibleTaskIfNeed(reusedTask3 != null ? reusedTask3 : computeTargetTask(), r, options4);
        android.app.ActivityOptions options5 = this.mASWrapper.getExtImpl().adjustOptionsForSmartMultiWindow(r, options4, this.mIntent, sourceRecord, targetTask4);
        if (options5 != null && options5.mActivityOptionsExt.isFlexibleSmartInnerTaskJump()) {
            targetTask = this.mASWrapper.getExtImpl().handleTargetTaskForInnerTaskJump(this.mStartActivity, this.mPreferredTaskDisplayArea, targetTask4);
            targetTask2 = targetTask;
        } else {
            targetTask = targetTask4;
            targetTask2 = reusedTask3;
        }
        boolean newTask = targetTask == null;
        if (com.android.server.wm.ActivityTaskManagerService.LTW_DISABLE) {
            prevTopTask = prevTopTask3;
            dreamStopping2 = dreamStopping;
            activityRecord = r;
            options2 = options5;
        } else {
            prevTopTask = prevTopTask3;
            dreamStopping2 = dreamStopping;
            activityRecord = r;
            android.app.ActivityOptions options6 = this.mASWrapper.getExtImpl().handleRemoteTaskIfNeeded(r, this.mPreferredTaskDisplayArea, this.mSourceRecord, sourceRecord, options5, this.mNotTop, newTask, targetTask2, this.mLaunchFlags, this.mLaunchMode, this.mIntent);
            this.mOptions = options6;
            options2 = options6;
        }
        this.mASWrapper.getExtImpl().setForceUpdateWindow(targetTask, activityRecord);
        this.mTargetTask = targetTask;
        this.mASWrapper.getExtImpl().checkStartActivityToLabFromFlexible(targetTask, options2);
        com.android.server.wm.ActivityRecord activityRecord2 = activityRecord;
        com.android.server.wm.Task reusedTask4 = targetTask2;
        com.android.server.wm.Task targetTask5 = targetTask;
        com.android.server.wm.Task prevTopTask4 = prevTopTask;
        android.app.ActivityOptions options7 = this.mASWrapper.getExtImpl().adjustOptionsForAcrossEmbeddedTask(targetTask, options2, this.mStartActivity, sourceRecord, r, this.mRequest.realCallingPid, this.mRequest.realCallingUid);
        this.mOptions = options7;
        android.app.ActivityOptions options8 = this.mASWrapper.getExtImpl().adjustOptionsForFlexibleTask(targetTask5, this.mPreferredTaskDisplayArea, options7, this.mStartActivity, sourceRecord, prevTopTask4, this.mRequest);
        this.mOptions = options8;
        if (!this.mASWrapper.getExtImpl().interceptStartActivityInVisibleTask(prevTopRootTask, targetTask5, options8, this.mStartActivity)) {
            this.mASWrapper.getExtImpl().updateFlexibleWindowTask(targetTask5, reusedTask4, options8, this.mStartActivity, this.mSourceRecord, this.mRequest.realCallingPid);
            if (!this.mASWrapper.getExtImpl().interceptStartActivityFromFlexibleWindow(prevTopRootTask, targetTask5, options8, this.mStartActivity, this.mRequest, sourceRecord)) {
                if (this.mASWrapper.getExtImpl().pullPuttTaskBack(this, this.mStartActivity, targetTask5, options8, this.mOptions, sourceRecord)) {
                    android.util.Slog.w(TAG, "start Activity abort as putt, a: " + this.mStartActivity + " tt: " + targetTask5 + " s:" + sourceRecord);
                    return 102;
                }
                android.app.ActivityOptions options9 = this.mASWrapper.getExtImpl().adjustOptionsForZoom(options8, sourceRecord, r, targetTask5, this.mRequest.realCallingPid);
                this.mASWrapper.getExtImpl().launchIntoCompatMode(options9, sourceRecord, activityRecord2, targetTask5);
                computeLaunchParams(activityRecord2, sourceRecord, targetTask5);
                int startResult = isAllowedToStart(activityRecord2, newTask, targetTask5);
                if (startResult != 0) {
                    if (activityRecord2.resultTo != null) {
                        activityRecord2.resultTo.sendResult(-1, activityRecord2.resultWho, activityRecord2.requestCode, 0, null, null, null);
                    }
                    return startResult;
                }
                boolean resumeTopAvoidMoveTask2 = false;
                if (targetTask5 == null) {
                    i = realCallingUid;
                    prevTopTask2 = prevTopTask4;
                } else {
                    if (targetTask5.getTreeWeight() > MAX_TASK_WEIGHT_FOR_ADDING_ACTIVITY) {
                        android.util.Slog.e(TAG, "Remove " + targetTask5 + " because it has contained too many activities or windows (abort starting " + activityRecord2 + " from uid=" + this.mCallingUid);
                        targetTask5.removeImmediately("bulky-task");
                        return 102;
                    }
                    if (!avoidMoveToFront()) {
                        if (this.mService.mHomeProcess != null) {
                            i = realCallingUid;
                            if (this.mService.mHomeProcess.mUid == i) {
                                prevTopTask2 = prevTopTask4;
                            }
                        } else {
                            i = realCallingUid;
                        }
                        prevTopTask2 = prevTopTask4;
                        if (prevTopTask2 != null && prevTopTask2.isActivityTypeHomeOrRecents() && activityRecord2.mTransitionController.isTransientHide(targetTask5)) {
                            this.mCanMoveToFrontCode = 2;
                        }
                    } else {
                        i = realCallingUid;
                        prevTopTask2 = prevTopTask4;
                    }
                    if (com.android.window.flags.Flags.balDontBringExistingBackgroundTaskStackToFg() && !avoidMoveToFront() && balVerdict.onlyCreatorAllows()) {
                        this.mCanMoveToFrontCode = 1;
                    }
                    this.mPriorAboveTask = com.android.server.wm.TaskDisplayArea.getRootTaskAbove(targetTask5.getRootTask());
                }
                if (!this.mASWrapper.getExtImpl().isLaunchingRootActivity(activityRecord2, targetTask5) && !this.mASWrapper.getExtImpl().isActivityStartWithSpruceKey(options9)) {
                    if (this.mCanMoveToFrontCode == 2 && activityRecord2.mTransitionController.isTransientHide(targetTask5) && balVerdict.getCode() != 6) {
                        android.util.Slog.d(TAG, "when swipe to home, transient hide task should avoid move to front and resumed!");
                        this.mDoResume = false;
                        resumeTopAvoidMoveTask2 = true;
                    } else if (this.mService.mHomeProcess == null || this.mService.mHomeProcess.mUid != i) {
                        if (targetTask5 != null && this.mASWrapper.getExtImpl().shouldAvoidMoveToFrontIfNeeded(this.mService, targetTask5) && balVerdict.getCode() != 6 && balVerdict.getCode() != 7) {
                            android.util.Slog.d(TAG, "when key to home,  target task should avoid move to front and resumed!");
                            this.mCanMoveToFrontCode = 2;
                            this.mDoResume = false;
                            resumeTopAvoidMoveTask2 = true;
                        } else if (targetTask5 == null && this.mASWrapper.getExtImpl().checkLaunchInSameTaskBackground(sourceRecord, activityRecord2)) {
                            android.util.Slog.d(TAG, "when go to home, activity background start to new task should be intercepted!");
                            return 0;
                        }
                    }
                }
                if (this.mASWrapper.getExtImpl().skipMoveToFront(sourceRecord, activityRecord2, targetTask5)) {
                    android.util.Slog.d(TAG, "source activity: " + sourceRecord + " is stop and launcher is top resumed, don't start r: " + activityRecord2);
                    this.mCanMoveToFrontCode = 2;
                    this.mDoResume = false;
                    resumeTopAvoidMoveTask = true;
                } else {
                    resumeTopAvoidMoveTask = resumeTopAvoidMoveTask2;
                }
                com.android.server.wm.ActivityRecord targetTaskTop = newTask ? null : targetTask5.getTopNonFinishingActivity();
                this.mASWrapper.getExtImpl().shouldClearReusedActivity(reusedTask4, targetTaskTop, options9, this.mStartActivity);
                boolean resumeTopAvoidMoveTask3 = resumeTopAvoidMoveTask;
                com.android.server.wm.Task prevTopTask5 = prevTopTask2;
                if (this.mASWrapper.getExtImpl().interceptStartForMirageCarMode(this.mIntent, sourceRecord, r, reusedTask4, options9, this)) {
                    return 0;
                }
                if (this.mASWrapper.getExtImpl().interceptStartForSplitScreenMode(this.mIntent, this.mRequest, sourceRecord, r, options9, reusedTask4, java.lang.Boolean.valueOf(newTask))) {
                    this.mASWrapper.getExtImpl().collapsePanelsForFlexibleWindow(options9, this.mService, realCallingUid, 0, null, false);
                    return 0;
                }
                this.mASWrapper.getExtImpl().activityPreloadHandleStartActivity(activityRecord2);
                if (targetTaskTop == null) {
                    this.mAddingToTask = true;
                } else {
                    if (3 == this.mLaunchMode && this.mSourceRecord != null && targetTask5 == this.mSourceRecord.getTask() && (activity = this.mRootWindowContainer.findActivity(this.mIntent, this.mStartActivity.info, false)) != null && activity.getTask() != targetTask5) {
                        activity.destroyIfPossible("Removes redundant singleInstance");
                    }
                    if (this.mLastStartActivityRecord != null) {
                        targetTaskTop.mLaunchSourceType = this.mLastStartActivityRecord.mLaunchSourceType;
                    }
                    recordTransientLaunchIfNeeded(targetTaskTop);
                    if (!this.mASWrapper.getExtImpl().onStartFromPrimaryScreen(reusedTask4, sourceRecord, this.mStartActivity, options9, this.mPreferredTaskDisplayArea, this.mIntent) && !this.mASWrapper.getExtImpl().startPreloadActivityWhilePreloading(reusedTask4, sourceRecord, targetTaskTop, options9, activityRecord2.launchedFromPackage, this.mLastStartReason)) {
                        this.mASWrapper.getExtImpl().updateTaskForZoom(options9, sourceRecord, r, targetTask5, this.mRequest.realCallingPid, this.mRequest.callingPackage, prevTopRootTask);
                        this.mASWrapper.getExtImpl().markFlexibleSubTaskIfForceStopNeeded(sourceRecord, this.mStartActivity, this.mTargetTask);
                        this.mASWrapper.getExtImpl().fixLaunchSourceType(activityRecord2, targetTaskTop);
                        startResult = recycleTask(targetTask5, targetTaskTop, reusedTask4, intentGrants, balVerdict);
                        if (startResult != 0) {
                            this.mASWrapper.getExtImpl().collapsePanelsForFlexibleWindow(options9, this.mService, realCallingUid, startResult, targetTask5, true);
                            return startResult;
                        }
                    } else {
                        return 0;
                    }
                }
                com.android.server.wm.Task topRootTask = this.mPreferredTaskDisplayArea.getFocusedRootTask();
                if (topRootTask == null) {
                    neededUriGrants = intentGrants;
                } else {
                    neededUriGrants = intentGrants;
                    int startResult2 = deliverToCurrentTopIfNeeded(topRootTask, neededUriGrants);
                    if (startResult2 != 0) {
                        return startResult2;
                    }
                }
                if (this.mTargetRootTask == null) {
                    this.mTargetRootTask = getOrCreateRootTask(this.mStartActivity, this.mLaunchFlags, targetTask5, this.mOptions);
                }
                if (!newTask) {
                    if (this.mAddingToTask) {
                        options3 = options9;
                        this.mASWrapper.getExtImpl().parseFlexibleActivityInfo(options3, sourceRecord, activityRecord2);
                        addOrReparentStartingActivity(targetTask5, "adding to task");
                    } else {
                        options3 = options9;
                    }
                } else {
                    com.android.server.wm.Task taskToAffiliate = (!this.mLaunchTaskBehind || this.mSourceRecord == null) ? null : this.mSourceRecord.getTask();
                    setNewTask(taskToAffiliate);
                    options3 = options9;
                }
                if (!com.android.server.wm.ActivityTaskManagerService.LTW_DISABLE) {
                    this.mService.getWrapper().getExtImpl().getRemoteTaskManager().updateRemoteTaskIfNeeded(r.getRootTask(), options3);
                }
                recordTransientLaunchIfNeeded(this.mLastStartActivityRecord);
                this.mASWrapper.getExtImpl().updateTransitionInfoBundleIfNeed(this.mStartActivity, this.mTargetRootTask);
                if (!this.mDoResume) {
                    z2 = true;
                } else if (!avoidMoveToFront()) {
                    this.mService.getWrapper().getFlexibleExtImpl().moveTaskToFront(this.mTargetRootTask, options3, null);
                    this.mTargetRootTask.getRootTask().moveToFront("reuseOrNewTask", targetTask5);
                    if (this.mTargetRootTask.isTopRootTaskInDisplayArea() || !this.mService.isDreaming() || dreamStopping2) {
                        z2 = true;
                    } else {
                        z2 = true;
                        this.mLaunchTaskBehind = true;
                        activityRecord2.mLaunchTaskBehind = true;
                    }
                } else {
                    z2 = true;
                    logPIOnlyCreatorAllowsBAL();
                }
                this.mService.mUgmInternal.grantUriPermissionUncheckedFromIntent(neededUriGrants, this.mStartActivity.getUriPermissionsLocked());
                if (this.mStartActivity.resultTo == null || this.mStartActivity.resultTo.info == null) {
                    targetTask3 = targetTask5;
                    reusedTask = reusedTask4;
                    if (this.mStartActivity.mShareIdentity) {
                        this.mService.getPackageManagerInternalLocked().grantImplicitAccess(this.mStartActivity.mUserId, this.mIntent, android.os.UserHandle.getAppId(this.mStartActivity.info.applicationInfo.uid), activityRecord2.launchedFromUid, true);
                    }
                } else {
                    android.content.pm.PackageManagerInternal pmInternal = this.mService.getPackageManagerInternalLocked();
                    targetTask3 = targetTask5;
                    reusedTask = reusedTask4;
                    int resultToUid = pmInternal.getPackageUid(this.mStartActivity.resultTo.info.packageName, 0L, this.mStartActivity.mUserId);
                    pmInternal.grantImplicitAccess(this.mStartActivity.mUserId, this.mIntent, android.os.UserHandle.getAppId(this.mStartActivity.info.applicationInfo.uid), resultToUid, true);
                }
                com.android.server.wm.Task startedTask = this.mStartActivity.getTask();
                if (newTask) {
                    com.android.server.wm.EventLogTags.writeWmCreateTask(this.mStartActivity.mUserId, startedTask.mTaskId, startedTask.getRootTaskId(), startedTask.getDisplayId());
                }
                this.mStartActivity.logStartActivity(com.android.server.wm.EventLogTags.WM_CREATE_ACTIVITY, startedTask);
                this.mStartActivity.getTaskFragment().clearLastPausedActivity();
                this.mRootWindowContainer.startPowerModeLaunchIfNeeded(false, this.mStartActivity);
                boolean isTaskSwitch = startedTask != prevTopTask5 ? z2 : false;
                com.android.server.wm.IActivityRecordExt extImpl = this.mStartActivity.getWrapper().getExtImpl();
                if (!avoidMoveToFront() || this.mDoResume) {
                    z2 = false;
                }
                extImpl.setSkipAppTransitionWhenStarting(z2);
                this.mStartActivity.getWrapper().getExtImpl().enableWaitDrawnForCameraIfNeed();
                this.mTargetRootTask.startActivityLocked(this.mStartActivity, topRootTask, newTask, isTaskSwitch, this.mOptions, sourceRecord);
                this.mASWrapper.getExtImpl().updateTaskForZoom(options3, sourceRecord, r, startedTask, this.mRequest.realCallingPid, this.mRequest.callingPackage, prevTopRootTask);
                if (this.mDoResume) {
                    com.android.server.wm.ActivityRecord topTaskActivity = startedTask.topRunningActivityLocked();
                    if (!this.mTargetRootTask.isTopActivityFocusable() || (topTaskActivity != null && topTaskActivity.isTaskOverlay() && this.mStartActivity != topTaskActivity)) {
                        this.mTargetRootTask.ensureActivitiesVisible(null);
                        this.mTargetRootTask.mDisplayContent.executeAppTransition();
                    } else {
                        if (this.mTargetRootTask.isTopActivityFocusable() && !this.mRootWindowContainer.isTopDisplayFocusedRootTask(this.mTargetRootTask)) {
                            if (!avoidMoveToFront()) {
                                this.mTargetRootTask.moveToFront("startActivityInner");
                            } else {
                                logPIOnlyCreatorAllowsBAL();
                            }
                        }
                        this.mRootWindowContainer.resumeFocusedTasksTopActivities(this.mTargetRootTask, this.mStartActivity, this.mOptions, this.mTransientLaunch);
                    }
                    str = TAG;
                } else if (!resumeTopAvoidMoveTask3) {
                    str = TAG;
                } else {
                    str = TAG;
                    android.util.Slog.d(str, "force resumeFocusedTasksTopActivities while swiping to home.");
                    this.mRootWindowContainer.resumeFocusedTasksTopActivities(this.mTargetRootTask, this.mStartActivity, this.mOptions, this.mTransientLaunch);
                }
                this.mRootWindowContainer.updateUserRootTask(this.mStartActivity.mUserId, this.mTargetRootTask);
                if (this.mStartActivity.getTask() == null) {
                    android.util.Slog.w(str, "startActivityInner: NSTART_ABORTED for task is null, mStartActivity = " + this.mStartActivity);
                    return 102;
                }
                this.mASWrapper.getExtImpl().markFlexibleSubTaskIfForceStopNeeded(sourceRecord, this.mStartActivity, null);
                this.mSupervisor.mRecentTasks.add(startedTask);
                this.mSupervisor.handleNonResizableTaskIfNeeded(startedTask, this.mPreferredWindowingMode, this.mPreferredTaskDisplayArea, this.mTargetRootTask);
                if (this.mOptions != null && this.mOptions.isLaunchIntoPip() && sourceRecord != null && sourceRecord.getTask() == this.mStartActivity.getTask() && balVerdict.allows()) {
                    this.mRootWindowContainer.moveActivityToPinnedRootTask(this.mStartActivity, sourceRecord, "launch-into-pip");
                }
                this.mSupervisor.getBackgroundActivityLaunchController().onNewActivityLaunched(this.mStartActivity);
                return 0;
            }
            return 0;
        }
        return 0;
    }

    private void logPIOnlyCreatorAllowsBAL() {
        if (avoidMoveToFrontPIOnlyCreatorAllows()) {
            java.lang.String realCallingPackage = this.mService.mContext.getPackageManager().getNameForUid(this.mRealCallingUid);
            if (realCallingPackage == null) {
                realCallingPackage = "uid=" + this.mRealCallingUid;
            }
            android.util.Slog.wtf(TAG, "Without Android 15 BAL hardening this activity would be moved to the foreground. The activity is started by a PendingIntent. However, only the creator of the PendingIntent allows BAL while the sender does not allow BAL. realCallingPackage: " + realCallingPackage + "; callingPackage: " + this.mRequest.callingPackage + "; mTargetRootTask:" + this.mTargetRootTask + "; mIntent: " + this.mIntent + "; mTargetRootTask.getTopNonFinishingActivity: " + this.mTargetRootTask.getTopNonFinishingActivity() + "; mTargetRootTask.getRootActivity: " + this.mTargetRootTask.getRootActivity());
        }
    }

    private void recordTransientLaunchIfNeeded(com.android.server.wm.ActivityRecord r) {
        if (r == null || !this.mTransientLaunch) {
            return;
        }
        com.android.server.wm.TransitionController controller = r.mTransitionController;
        if (controller.isCollecting() && !controller.isTransientCollect(r)) {
            controller.setTransientLaunch(r, this.mPriorAboveTask);
        }
    }

    private com.android.server.wm.Task computeTargetTask() {
        if (this.mStartActivity.resultTo == null && this.mInTask == null && !this.mAddingToTask && (this.mLaunchFlags & 268435456) != 0) {
            return null;
        }
        if (this.mSourceRecord != null) {
            if (this.mASWrapper.getExtImpl().replaceNewTaskIfNeed(this.mSourceRecord, this.mStartActivity)) {
                return null;
            }
            if (this.mASWrapper.getExtImpl().isAppUnlockActivityFromPocketStudio(this.mSourceRecord, this.mStartActivity) && this.mInTask != null) {
                return this.mInTask;
            }
            return this.mSourceRecord.getTask();
        }
        if (this.mInTask != null) {
            if (!this.mInTask.isAttached()) {
                getOrCreateRootTask(this.mStartActivity, this.mLaunchFlags, this.mInTask, this.mOptions);
            }
            return this.mInTask;
        }
        com.android.server.wm.Task rootTask = getOrCreateRootTask(this.mStartActivity, this.mLaunchFlags, null, this.mOptions);
        com.android.server.wm.ActivityRecord top = rootTask.getTopNonFinishingActivity();
        if (top != null) {
            return top.getTask();
        }
        rootTask.removeIfPossible("computeTargetTask");
        return null;
    }

    private void computeLaunchParams(com.android.server.wm.ActivityRecord r, com.android.server.wm.ActivityRecord sourceRecord, com.android.server.wm.Task targetTask) {
        com.android.server.wm.TaskDisplayArea defaultTaskDisplayArea;
        this.mOptions = this.mASWrapper.getExtImpl().hookOptionsForSplit(r, sourceRecord, targetTask, this.mOptions);
        this.mSupervisor.getLaunchParamsController().calculate(targetTask, r.info.windowLayout, r, sourceRecord, this.mOptions, this.mRequest, 3, this.mLaunchParams);
        if (this.mLaunchParams.hasPreferredTaskDisplayArea()) {
            defaultTaskDisplayArea = this.mLaunchParams.mPreferredTaskDisplayArea;
        } else {
            defaultTaskDisplayArea = this.mRootWindowContainer.getDefaultTaskDisplayArea();
        }
        this.mPreferredTaskDisplayArea = defaultTaskDisplayArea;
        this.mPreferredWindowingMode = this.mLaunchParams.mWindowingMode;
    }

    private com.android.server.wm.TaskDisplayArea computeSuggestedLaunchDisplayArea(com.android.server.wm.Task task, com.android.server.wm.ActivityRecord source, android.app.ActivityOptions options) {
        this.mSupervisor.getLaunchParamsController().calculate(task, null, null, source, options, this.mRequest, 0, this.mLaunchParams);
        if (this.mLaunchParams.hasPreferredTaskDisplayArea()) {
            return this.mLaunchParams.mPreferredTaskDisplayArea;
        }
        return this.mRootWindowContainer.getDefaultTaskDisplayArea();
    }

    int isAllowedToStart(com.android.server.wm.ActivityRecord r, boolean newTask, com.android.server.wm.Task targetTask) {
        com.android.server.wm.DisplayContent displayContent;
        int launchingFromDisplayId;
        if (r.packageName != null) {
            if (!this.mASWrapper.getExtImpl().isAllowedToStartIncompactWindowingmode(this.mStartActivity, targetTask)) {
                return -96;
            }
            if (r.isActivityTypeHome() && !this.mRootWindowContainer.canStartHomeOnDisplayArea(r.info, this.mPreferredTaskDisplayArea, true)) {
                android.util.Slog.w(TAG, "Cannot launch home on display area " + this.mPreferredTaskDisplayArea);
                return -96;
            }
            boolean blockBalInTask = newTask || !targetTask.isUidPresent(this.mCallingUid) || (3 == this.mLaunchMode && targetTask.inPinnedWindowingMode());
            if (this.mBalCode == 0 && blockBalInTask && handleBackgroundActivityAbort(r)) {
                android.util.Slog.e(TAG, "Abort background activity starts from " + this.mCallingUid);
                return 102;
            }
            boolean isNewClearTask = (this.mLaunchFlags & 268468224) == 268468224;
            if (!newTask) {
                if (this.mService.getLockTaskController().isLockTaskModeViolation(targetTask, isNewClearTask)) {
                    android.util.Slog.e(TAG, "Attempted Lock Task Mode violation r=" + r);
                    return 101;
                }
            } else if (this.mService.getLockTaskController().isNewTaskLockTaskModeViolation(r)) {
                android.util.Slog.e(TAG, "Attempted Lock Task Mode violation r=" + r);
                return 101;
            }
            if (this.mASWrapper.getExtImpl().interceptActivityForAppShareModeIfNeed(newTask, isNewClearTask, targetTask, this.mStartActivity, this.mRootWindowContainer, this.mSourceRootTask, this.mSourceRecord)) {
                android.util.Slog.d(TAG, "is In AppShareMirageMode mStartActivity = " + this.mStartActivity);
                return 101;
            }
            if (!this.mASWrapper.getExtImpl().isAllowedToStartActivityInZoom(r, newTask, targetTask)) {
                android.util.Slog.d(TAG, "zoom root task is exiting mStartActivity = " + this.mStartActivity);
                return -96;
            }
            if (this.mASWrapper.getExtImpl().interceptWhenAnr(this.mService, r)) {
                android.util.Slog.d(TAG, "interceptWhenAnr mStartActivity = " + this.mStartActivity);
                return -96;
            }
            if (this.mPreferredTaskDisplayArea != null && (displayContent = this.mRootWindowContainer.getDisplayContentOrCreate(this.mPreferredTaskDisplayArea.getDisplayId())) != null) {
                int targetWindowingMode = targetTask != null ? targetTask.getWindowingMode() : displayContent.getWindowingMode();
                if (this.mSourceRecord != null) {
                    launchingFromDisplayId = this.mSourceRecord.getDisplayId();
                } else {
                    launchingFromDisplayId = 0;
                }
                if (!displayContent.mDwpcHelper.canActivityBeLaunched(r.info, r.intent, targetWindowingMode, launchingFromDisplayId, newTask)) {
                    android.util.Slog.w(TAG, "Abort to launch " + r.info.getComponentName() + " on display area " + this.mPreferredTaskDisplayArea);
                    return 102;
                }
            }
            return !this.mSupervisor.getBackgroundActivityLaunchController().checkActivityAllowedToStart(this.mSourceRecord, r, newTask, avoidMoveToFront(), targetTask, this.mLaunchFlags, this.mBalCode, this.mCallingUid, this.mRealCallingUid, this.mPreferredTaskDisplayArea) ? 102 : 0;
        }
        android.app.ActivityOptions.abort(this.mOptions);
        return -92;
    }

    static int canEmbedActivity(com.android.server.wm.TaskFragment taskFragment, com.android.server.wm.ActivityRecord starting, com.android.server.wm.Task targetTask) {
        com.android.server.wm.Task hostTask = taskFragment.getTask();
        if (hostTask == null || targetTask != hostTask) {
            return 3;
        }
        return taskFragment.isAllowedToEmbedActivity(starting);
    }

    int recycleTask(com.android.server.wm.Task targetTask, com.android.server.wm.ActivityRecord targetTaskTop, com.android.server.wm.Task reusedTask, com.android.server.uri.NeededUriGrants intentGrants, com.android.server.wm.BackgroundActivityStartController.BalVerdict balVerdict) {
        com.android.server.wm.ActivityRecord targetTaskTop2;
        if (targetTask.mUserId != this.mStartActivity.mUserId) {
            this.mTargetRootTask = targetTask.getRootTask();
            this.mAddingToTask = true;
            return 0;
        }
        if (reusedTask != null) {
            if (this.mStartActivity.getWrapper().getExtImpl().getLaunchedFromMultiSearch()) {
                targetTask.getWrapper().getExtImpl().setLaunchedFromMultiSearch(true);
            }
            if (targetTask.intent == null) {
                targetTask.setIntent(this.mStartActivity);
            } else {
                boolean taskOnHome = (this.mStartActivity.intent.getFlags() & 16384) != 0;
                if (taskOnHome) {
                    targetTask.intent.addFlags(16384);
                } else {
                    targetTask.intent.removeFlags(16384);
                }
            }
        }
        this.mRootWindowContainer.startPowerModeLaunchIfNeeded(false, targetTaskTop);
        setTargetRootTaskIfNeeded(targetTaskTop);
        this.mASWrapper.getExtImpl().setHandleForcedResizableFlag(targetTaskTop, reusedTask, targetTask, balVerdict);
        if (this.mLastStartActivityRecord != null && (this.mLastStartActivityRecord.finishing || this.mLastStartActivityRecord.noDisplay)) {
            this.mLastStartActivityRecord = targetTaskTop;
        }
        if ((this.mStartFlags & 1) == 0) {
            complyActivityFlags(targetTask, reusedTask != null ? reusedTask.getTopNonFinishingActivity() : null, intentGrants);
            if (this.mAddingToTask) {
                this.mSupervisor.getBackgroundActivityLaunchController().clearTopIfNeeded(targetTask, this.mSourceRecord, this.mStartActivity, this.mCallingUid, this.mRealCallingUid, this.mLaunchFlags, this.mBalCode);
                return 0;
            }
            if (targetTaskTop.finishing) {
                targetTaskTop2 = targetTask.getTopNonFinishingActivity();
            } else {
                targetTaskTop2 = targetTaskTop;
            }
            if (this.mMovedToFront) {
                this.mASWrapper.getExtImpl().shouldShowStartingwidnowWhenMoveToFront(this.mStartActivity, reusedTask, targetTaskTop2);
            } else if (this.mDoResume) {
                this.mTargetRootTask.moveToFront("intentActivityFound");
            }
            resumeTargetRootTaskIfNeeded();
            if (this.mService.isDreaming() && targetTaskTop2.canTurnScreenOn()) {
                targetTaskTop2.mTaskSupervisor.wakeUp("recycleTask#turnScreenOnFlag");
            }
            this.mLastStartActivityRecord = targetTaskTop2;
            this.mASWrapper.getExtImpl().updateTransitionInfoBundleIfNeed(this.mStartActivity, this.mTargetRootTask);
            return this.mMovedToFront ? 2 : 3;
        }
        if (!this.mMovedToFront && this.mDoResume) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_TASKS_enabled[0]) {
                java.lang.String protoLogParam0 = java.lang.String.valueOf(this.mTargetRootTask);
                java.lang.String protoLogParam1 = java.lang.String.valueOf(targetTaskTop);
                com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_TASKS, -2867366986304729L, 0, null, protoLogParam0, protoLogParam1);
            }
            this.mTargetRootTask.moveToFront("intentActivityFound");
        }
        resumeTargetRootTaskIfNeeded();
        return 1;
    }

    private int deliverToCurrentTopIfNeeded(com.android.server.wm.Task topRootTask, com.android.server.uri.NeededUriGrants intentGrants) {
        com.android.server.wm.ActivityRecord top = topRootTask.topRunningNonDelayedActivityLocked(this.mNotTop);
        boolean dontStart = top != null && top.mActivityComponent.equals(this.mStartActivity.mActivityComponent) && top.mUserId == this.mStartActivity.mUserId && top.attachedToProcess() && ((this.mLaunchFlags & 536870912) != 0 || 1 == this.mLaunchMode) && ((!top.isActivityTypeHome() || top.getDisplayArea() == this.mPreferredTaskDisplayArea) && !this.mASWrapper.getExtImpl().canClearActivityRecord(top));
        if (!dontStart) {
            return 0;
        }
        top.getTaskFragment().clearLastPausedActivity();
        if (this.mDoResume) {
            this.mRootWindowContainer.resumeFocusedTasksTopActivities();
        }
        android.app.ActivityOptions.abort(this.mOptions);
        if ((this.mStartFlags & 1) != 0) {
            return 1;
        }
        if (this.mStartActivity.resultTo != null) {
            this.mStartActivity.resultTo.sendResult(-1, this.mStartActivity.resultWho, this.mStartActivity.requestCode, 0, null, null, null);
            this.mStartActivity.resultTo = null;
        }
        deliverNewIntent(top, intentGrants);
        this.mSupervisor.handleNonResizableTaskIfNeeded(top.getTask(), this.mLaunchParams.mWindowingMode, this.mPreferredTaskDisplayArea, topRootTask);
        return 3;
    }

    private void complyActivityFlags(com.android.server.wm.Task targetTask, com.android.server.wm.ActivityRecord reusedActivity, com.android.server.uri.NeededUriGrants intentGrants) {
        com.android.server.wm.ActivityRecord targetTaskTop = targetTask.getTopNonFinishingActivity();
        boolean resetTask = (reusedActivity == null || (this.mLaunchFlags & 2097152) == 0) ? false : true;
        if (resetTask) {
            targetTaskTop = this.mTargetRootTask.resetTaskIfNeeded(targetTaskTop, this.mStartActivity);
        }
        if ((this.mLaunchFlags & 268468224) == 268468224) {
            if (com.android.server.wm.ActivityTaskManagerService.LTW_DISABLE || !this.mService.getWrapper().getExtImpl().getRemoteTaskManager().inAnyInterceptSession()) {
                targetTask.performClearTaskForReuse(true);
            }
            targetTask.setIntent(this.mStartActivity);
            this.mAddingToTask = true;
            this.mIsTaskCleared = true;
            return;
        }
        if ((this.mLaunchFlags & 67108864) != 0 || isDocumentLaunchesIntoExisting(this.mLaunchFlags) || isLaunchModeOneOf(3, 2, 4)) {
            if (!com.android.server.wm.ActivityTaskManagerService.LTW_DISABLE && this.mService.getWrapper().getExtImpl().getRemoteTaskManager().inAnyInterceptSession() && (this.mLaunchFlags & 67108864) == 0) {
                return;
            }
            int[] finishCount = new int[1];
            com.android.server.wm.ActivityRecord clearTop = targetTask.performClearTop(this.mStartActivity, this.mLaunchFlags, finishCount);
            if (clearTop != null && !clearTop.finishing) {
                if (finishCount[0] > 0) {
                    this.mMovedToTopActivity = clearTop;
                }
                if (clearTop.isRootOfTask()) {
                    clearTop.getTask().setIntent(this.mStartActivity);
                }
                deliverNewIntent(clearTop, intentGrants);
                return;
            }
            this.mAddingToTask = true;
            if (clearTop != null && clearTop.getTaskFragment() != null && clearTop.getTaskFragment().isEmbedded()) {
                this.mAddingToTaskFragment = clearTop.getTaskFragment();
            }
            if (targetTask.getRootTask() == null) {
                this.mTargetRootTask = getOrCreateRootTask(this.mStartActivity, this.mLaunchFlags, null, this.mOptions);
                this.mTargetRootTask.addChild(targetTask, !this.mLaunchTaskBehind, (this.mStartActivity.info.flags & 1024) != 0);
                return;
            }
            return;
        }
        if ((67108864 & this.mLaunchFlags) == 0 && !this.mAddingToTask && (this.mLaunchFlags & 131072) != 0) {
            com.android.server.wm.ActivityRecord act = targetTask.findActivityInHistory(this.mStartActivity.mActivityComponent, this.mStartActivity.mUserId);
            if (act != null) {
                com.android.server.wm.Task task = act.getTask();
                boolean actuallyMoved = task.moveActivityToFront(act);
                if (actuallyMoved) {
                    this.mMovedToTopActivity = act;
                    if (this.mNoAnimation) {
                        act.mDisplayContent.prepareAppTransition(0);
                    } else {
                        act.mDisplayContent.prepareAppTransition(3);
                    }
                }
                act.updateOptionsLocked(this.mOptions);
                deliverNewIntent(act, intentGrants);
                act.getTaskFragment().clearLastPausedActivity();
                return;
            }
            this.mAddingToTask = true;
            return;
        }
        if (this.mStartActivity.mActivityComponent.equals(targetTask.realActivity)) {
            if (targetTask != this.mInTask) {
                if (((this.mLaunchFlags & 536870912) != 0 || 1 == this.mLaunchMode) && targetTaskTop.mActivityComponent.equals(this.mStartActivity.mActivityComponent) && this.mStartActivity.resultTo == null) {
                    if (targetTaskTop.isRootOfTask()) {
                        targetTaskTop.getTask().setIntent(this.mStartActivity);
                    }
                    deliverNewIntent(targetTaskTop, intentGrants);
                    return;
                } else if (!targetTask.isSameIntentFilter(this.mStartActivity)) {
                    this.mAddingToTask = true;
                    return;
                } else {
                    if (reusedActivity == null) {
                        this.mAddingToTask = true;
                        return;
                    }
                    return;
                }
            }
            return;
        }
        if (!resetTask) {
            this.mAddingToTask = true;
        } else if (!targetTask.rootWasReset) {
            targetTask.setIntent(this.mStartActivity);
        }
    }

    void reset(boolean clearRequest) {
        this.mStartActivity = null;
        this.mIntent = null;
        this.mCallingUid = -1;
        this.mRealCallingUid = -1;
        this.mOptions = null;
        this.mBalCode = 1;
        this.mLaunchTaskBehind = false;
        this.mLaunchFlags = 0;
        this.mLaunchMode = -1;
        this.mLaunchParams.reset();
        this.mNotTop = null;
        this.mDoResume = false;
        this.mStartFlags = 0;
        this.mSourceRecord = null;
        this.mPreferredTaskDisplayArea = null;
        this.mPreferredWindowingMode = 0;
        this.mInTask = null;
        this.mInTaskFragment = null;
        this.mAddingToTaskFragment = null;
        this.mAddingToTask = false;
        this.mAddingToTaskFragment = null;
        this.mSourceRootTask = null;
        this.mTargetRootTask = null;
        this.mTargetTask = null;
        this.mIsTaskCleared = false;
        this.mMovedToFront = false;
        this.mNoAnimation = false;
        this.mCanMoveToFrontCode = 0;
        this.mFrozeTaskList = false;
        this.mTransientLaunch = false;
        this.mPriorAboveTask = null;
        this.mDisplayLockAndOccluded = false;
        this.mVoiceSession = null;
        this.mVoiceInteractor = null;
        this.mIntentDelivered = false;
        if (clearRequest) {
            this.mRequest.reset();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:99:0x0237  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void setInitialState(com.android.server.wm.ActivityRecord r20, android.app.ActivityOptions r21, com.android.server.wm.Task r22, com.android.server.wm.TaskFragment r23, int r24, com.android.server.wm.ActivityRecord r25, android.service.voice.IVoiceInteractionSession r26, com.android.internal.app.IVoiceInteractor r27, int r28, int r29) {
        /*
            Method dump skipped, instruction units count: 742
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.wm.ActivityStarter.setInitialState(com.android.server.wm.ActivityRecord, android.app.ActivityOptions, com.android.server.wm.Task, com.android.server.wm.TaskFragment, int, com.android.server.wm.ActivityRecord, android.service.voice.IVoiceInteractionSession, com.android.internal.app.IVoiceInteractor, int, int):void");
    }

    private void sendNewTaskResultRequestIfNeeded() {
        if (this.mStartActivity.resultTo != null && (this.mLaunchFlags & 268435456) != 0) {
            android.util.Slog.w(TAG, "Activity is launching as a new task, so cancelling activity result.");
            this.mStartActivity.resultTo.sendResult(-1, this.mStartActivity.resultWho, this.mStartActivity.requestCode, 0, null, null, null);
            this.mStartActivity.resultTo = null;
        }
    }

    private void computeLaunchingTaskFlags() {
        if (this.mSourceRecord == null && this.mInTask != null && this.mInTask.getRootTask() != null) {
            android.content.Intent baseIntent = this.mInTask.getBaseIntent();
            com.android.server.wm.ActivityRecord root = this.mInTask.getRootActivity();
            if (baseIntent == null) {
                android.app.ActivityOptions.abort(this.mOptions);
                throw new java.lang.IllegalArgumentException("Launching into task without base intent: " + this.mInTask);
            }
            if (isLaunchModeOneOf(3, 2)) {
                if (!baseIntent.getComponent().equals(this.mStartActivity.intent.getComponent())) {
                    android.app.ActivityOptions.abort(this.mOptions);
                    throw new java.lang.IllegalArgumentException("Trying to launch singleInstance/Task " + this.mStartActivity + " into different task " + this.mInTask);
                }
                if (root != null) {
                    android.app.ActivityOptions.abort(this.mOptions);
                    throw new java.lang.IllegalArgumentException("Caller with mInTask " + this.mInTask + " has root " + root + " but target is singleInstance/Task");
                }
            }
            if (root == null) {
                this.mLaunchFlags = (this.mLaunchFlags & (-403185665)) | (baseIntent.getFlags() & 403185664);
                this.mIntent.setFlags(this.mLaunchFlags);
                this.mInTask.setIntent(this.mStartActivity);
                this.mAddingToTask = true;
            } else if ((this.mLaunchFlags & 268435456) != 0) {
                this.mAddingToTask = false;
            } else {
                this.mAddingToTask = true;
            }
        } else {
            this.mInTask = null;
            if ((this.mStartActivity.isResolverOrDelegateActivity() || this.mStartActivity.noDisplay) && this.mSourceRecord != null && this.mSourceRecord.inFreeformWindowingMode()) {
                this.mAddingToTask = true;
            }
        }
        if (this.mInTask == null) {
            if (this.mSourceRecord == null) {
                if ((this.mLaunchFlags & 268435456) == 0 && this.mInTask == null) {
                    android.util.Slog.w(TAG, "startActivity called from non-Activity context; forcing Intent.FLAG_ACTIVITY_NEW_TASK for: " + this.mIntent);
                    this.mLaunchFlags |= 268435456;
                }
            } else if (this.mSourceRecord.launchMode == 3) {
                this.mLaunchFlags |= 268435456;
            } else if (isLaunchModeOneOf(3, 2) && !this.mASWrapper.getExtImpl().newTaskFlagDisable(this.mStartActivity, this.mSourceRecord)) {
                this.mLaunchFlags |= 268435456;
            }
        }
        if ((this.mLaunchFlags & 4096) != 0) {
            if ((this.mLaunchFlags & 268435456) == 0 || this.mSourceRecord == null) {
                this.mLaunchFlags &= -4097;
            }
        }
    }

    private com.android.server.wm.Task resolveReusableTask(boolean includeLaunchedFromBubble) {
        if (this.mOptions != null && this.mOptions.getLaunchTaskId() != -1) {
            com.android.server.wm.Task launchTask = this.mRootWindowContainer.anyTaskForId(this.mOptions.getLaunchTaskId());
            if (launchTask == null || !launchTask.isLeafTask()) {
                return null;
            }
            return launchTask;
        }
        boolean putIntoExistingTask = ((this.mLaunchFlags & 268435456) != 0 && (this.mLaunchFlags & 134217728) == 0) || isLaunchModeOneOf(3, 2);
        com.android.server.wm.ActivityRecord intentActivity = null;
        if (putIntoExistingTask & (this.mInTask == null && this.mStartActivity.resultTo == null)) {
            if (3 == this.mLaunchMode) {
                intentActivity = this.mRootWindowContainer.findActivity(this.mIntent, this.mStartActivity.info, false);
                if (intentActivity != null && this.mStartActivity.isActivityTypeHome() && !intentActivity.isActivityTypeHome()) {
                    intentActivity.destroyIfPossible("Removes redundant singleInstance");
                    intentActivity = null;
                }
            } else if ((this.mLaunchFlags & 4096) != 0) {
                intentActivity = this.mRootWindowContainer.findActivity(this.mIntent, this.mStartActivity.info, 2 != this.mLaunchMode);
            } else {
                com.android.server.wm.ActivityRecord intentActivity2 = this.mRootWindowContainer.findTask(this.mStartActivity, this.mPreferredTaskDisplayArea, includeLaunchedFromBubble);
                intentActivity = this.mASWrapper.getExtImpl().handleReuseActivityForSubDisplayIfNeed(intentActivity2, this.mOptions, this.mStartActivity, this.mPreferredTaskDisplayArea, includeLaunchedFromBubble);
            }
        } else if (!com.android.server.wm.ActivityTaskManagerService.LTW_DISABLE) {
            intentActivity = this.mService.getWrapper().getExtImpl().getRemoteTaskManager().findTaskForReuseIfNeeded(this.mStartActivity, this.mOptions, this.mPreferredTaskDisplayArea, this.mLaunchFlags);
        }
        if (intentActivity != null && this.mLaunchMode == 4 && !intentActivity.getTask().getRootActivity().mActivityComponent.equals(this.mStartActivity.mActivityComponent)) {
            intentActivity = null;
        }
        if (intentActivity != null && ((this.mStartActivity.isActivityTypeHome() || intentActivity.isActivityTypeHome()) && intentActivity.getDisplayArea() != this.mPreferredTaskDisplayArea)) {
            intentActivity = null;
        }
        com.android.server.wm.ActivityRecord intentActivity3 = this.mASWrapper.getExtImpl().isAppUnlockPasswordActivity(intentActivity, this.mSourceRecord, this.mStartActivity, this.mInTask);
        if (intentActivity3 != null) {
            return intentActivity3.getTask();
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setTargetRootTaskIfNeeded(com.android.server.wm.ActivityRecord intentActivity) {
        boolean differentTopTask;
        com.android.server.wm.ActivityRecord curTop;
        if (intentActivity.getTaskFragment() != null) {
            intentActivity.getTaskFragment().clearLastPausedActivity();
        }
        com.android.server.wm.Task intentTask = intentActivity.getTask();
        com.android.server.wm.Task origRootTask = intentTask != null ? intentTask.getRootTask() : null;
        if (this.mTargetRootTask == null) {
            if (this.mSourceRecord == null || this.mSourceRecord.mLaunchRootTask == null) {
                this.mTargetRootTask = getOrCreateRootTask(this.mStartActivity, this.mLaunchFlags, intentTask, this.mOptions);
            } else {
                this.mTargetRootTask = com.android.server.wm.Task.fromWindowContainerToken(this.mSourceRecord.mLaunchRootTask);
            }
        }
        if (this.mTargetRootTask.getDisplayArea() == this.mPreferredTaskDisplayArea) {
            com.android.server.wm.Task focusRootTask = this.mTargetRootTask.mDisplayContent.getFocusedRootTask();
            if (focusRootTask != null) {
                curTop = focusRootTask.topRunningNonDelayedActivityLocked(this.mNotTop);
            } else {
                curTop = null;
            }
            com.android.server.wm.Task topTask = curTop != null ? curTop.getTask() : null;
            boolean differentTopTask2 = topTask != intentTask || !(focusRootTask == null || topTask == focusRootTask.getTopMostTask()) || (!(focusRootTask == null || focusRootTask == origRootTask) || this.mASWrapper.getExtImpl().getSubDifferentTopTask(intentTask, this.mPreferredTaskDisplayArea, this.mOptions, this.mSourceRecord) || this.mASWrapper.getExtImpl().checkIsStartToSplit(this.mOptions));
            differentTopTask = differentTopTask2;
        } else {
            differentTopTask = true;
        }
        if (differentTopTask && !avoidMoveToFront()) {
            this.mStartActivity.intent.addFlags(4194304);
            if (this.mLaunchTaskBehind && this.mSourceRecord != null) {
                intentActivity.setTaskToAffiliateWith(this.mSourceRecord.getTask());
            }
            if (intentActivity.isDescendantOf(this.mTargetRootTask)) {
                if (this.mTargetRootTask != intentTask && this.mTargetRootTask != intentTask.getParent().asTask()) {
                    intentTask.getParent().positionChildAt(Integer.MAX_VALUE, intentTask, false);
                    intentTask = intentTask.getParent().asTaskFragment().getTask();
                }
                boolean wasTopOfVisibleRootTask = intentActivity.isVisibleRequested() && intentActivity.inMultiWindowMode() && intentActivity == this.mTargetRootTask.topRunningActivity() && !intentActivity.mTransitionController.isTransientHide(this.mTargetRootTask);
                boolean noAnimation = this.mNoAnimation;
                if (!com.android.server.wm.ActivityTaskManagerService.LTW_DISABLE) {
                    noAnimation = this.mService.getWrapper().getExtImpl().getRemoteTaskManager().isDisplaySwitchDetected() ? true : this.mNoAnimation;
                }
                this.mTargetRootTask.moveTaskToFront(intentTask, noAnimation, this.mOptions, this.mStartActivity.appTimeTracker, true, "bringingFoundTaskToFront");
                this.mMovedToFront = wasTopOfVisibleRootTask ? false : true;
            } else if (intentActivity.getWindowingMode() != 2 && !this.mASWrapper.mASExt.notReparentForComapctWindow(intentTask, intentActivity, this.mTargetRootTask)) {
                if (intentTask != null) {
                    intentTask.reparent(this.mTargetRootTask, true, 0, true, true, "reparentToTargetRootTask");
                }
                intentTask.reparent(this.mTargetRootTask, true, 0, true, true, "reparentToTargetRootTask");
                if (com.android.server.wm.OplusPairTaskManager.isPairTaskEnabled()) {
                    this.mTargetRootTask.moveTaskToFront(intentTask, this.mNoAnimation, this.mOptions, this.mStartActivity.appTimeTracker, true, "bringingFoundTaskToFront");
                }
                this.mMovedToFront = true;
            }
            this.mOptions = null;
        }
        if (differentTopTask) {
            logPIOnlyCreatorAllowsBAL();
        }
        this.mASWrapper.mASExt.transferLaunchCookie(intentActivity, this.mStartActivity, this.mTargetRootTask, this.mLaunchFlags);
        if (this.mStartActivity.mPendingRemoteAnimation != null) {
            intentActivity.mPendingRemoteAnimation = this.mStartActivity.mPendingRemoteAnimation;
        }
        this.mTargetRootTask = intentActivity.getRootTask();
        this.mASWrapper.getExtImpl().handleNonResizableTask(this.mSupervisor, intentTask, 0, this.mRootWindowContainer.getDefaultTaskDisplayArea(), this.mTargetRootTask);
    }

    private void resumeTargetRootTaskIfNeeded() {
        if (this.mDoResume) {
            com.android.server.wm.ActivityRecord next = this.mTargetRootTask.topRunningActivity(true);
            if (next != null) {
                next.setCurrentLaunchCanTurnScreenOn(true);
            }
            if (this.mTargetRootTask.isFocusable()) {
                this.mRootWindowContainer.resumeFocusedTasksTopActivities(this.mTargetRootTask, null, this.mOptions, this.mTransientLaunch);
            } else {
                this.mRootWindowContainer.ensureActivitiesVisible();
            }
        } else {
            android.app.ActivityOptions.abort(this.mOptions);
        }
        this.mRootWindowContainer.updateUserRootTask(this.mStartActivity.mUserId, this.mTargetRootTask);
    }

    private void setNewTask(com.android.server.wm.Task taskToAffiliate) {
        boolean toTop = (this.mLaunchTaskBehind || avoidMoveToFront()) ? false : true;
        com.android.server.wm.Task task = this.mTargetRootTask.reuseOrCreateTask(this.mStartActivity.info, this.mIntent, this.mVoiceSession, this.mVoiceInteractor, toTop, this.mStartActivity, this.mSourceRecord, this.mOptions);
        task.mTransitionController.collectExistenceChange(task);
        addOrReparentStartingActivity(task, "setTaskFromReuseOrCreateNewTask");
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_TASKS_enabled[1]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(this.mStartActivity);
            java.lang.String protoLogParam1 = java.lang.String.valueOf(this.mStartActivity.getTask());
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_TASKS, -2190454940975874759L, 0, null, protoLogParam0, protoLogParam1);
        }
        if (taskToAffiliate != null) {
            this.mStartActivity.setTaskToAffiliateWith(taskToAffiliate);
        }
    }

    private void deliverNewIntent(com.android.server.wm.ActivityRecord activity, com.android.server.uri.NeededUriGrants intentGrants) {
        if (this.mIntentDelivered) {
            return;
        }
        activity.logStartActivity(com.android.server.wm.EventLogTags.WM_NEW_INTENT, activity.getTask());
        activity.deliverNewIntentLocked(this.mCallingUid, this.mStartActivity.intent, intentGrants, this.mStartActivity.launchedFromPackage, this.mStartActivity.mShareIdentity, this.mStartActivity.mUserId, android.os.UserHandle.getAppId(this.mStartActivity.info.applicationInfo.uid));
        this.mIntentDelivered = true;
    }

    private void addOrReparentStartingActivity(com.android.server.wm.Task task, java.lang.String reason) {
        this.mStartActivity.getWrapper().getSocExtImpl().acquireActivityBoost(this.mStartActivity.packageName, this.mStartActivity.app, this.mStartActivity.info, this.mService, this.mStartActivity.processName);
        com.android.server.wm.TaskFragment newParent = task;
        if (this.mInTaskFragment != null) {
            int embeddingCheckResult = canEmbedActivity(this.mInTaskFragment, this.mStartActivity, task);
            if (embeddingCheckResult == 0) {
                newParent = this.mInTaskFragment;
                this.mStartActivity.mRequestedLaunchingTaskFragmentToken = this.mInTaskFragment.getFragmentToken();
            } else {
                sendCanNotEmbedActivityError(this.mInTaskFragment, embeddingCheckResult);
            }
        } else {
            com.android.server.wm.TaskFragment candidateTf = this.mAddingToTaskFragment;
            if (candidateTf == null) {
                candidateTf = findCandidateTaskFragment(task);
                if (!this.mASWrapper.getExtImpl().canAddingToTaskFragment(task, candidateTf, this.mStartActivity)) {
                    candidateTf = null;
                }
            }
            if (candidateTf != null && candidateTf.isEmbedded() && canEmbedActivity(candidateTf, this.mStartActivity, task) == 0) {
                newParent = candidateTf;
            }
        }
        com.android.server.wm.TaskFragment newParent2 = this.mASWrapper.getExtImpl().modifyParentForEmbeddingSettingIfNeed(this.mStartActivity, task, newParent);
        this.mStartActivity.getWrapper().getExtImpl().setSourceRecordHint(this.mSourceRecord);
        if (this.mStartActivity.getTaskFragment() == null || this.mStartActivity.getTaskFragment() == newParent2) {
            newParent2.addChild(this.mStartActivity, Integer.MAX_VALUE);
        } else {
            this.mStartActivity.reparent(newParent2, newParent2.getChildCount(), reason);
        }
    }

    private com.android.server.wm.TaskFragment findCandidateTaskFragment(com.android.server.wm.Task task) {
        com.android.server.wm.TaskFragment sourceTaskFragment = this.mSourceRecord != null ? this.mSourceRecord.getTaskFragment() : null;
        for (int i = task.getChildCount() - 1; i >= 0; i--) {
            com.android.server.wm.WindowContainer<?> wc = task.getChildAt(i);
            com.android.server.wm.ActivityRecord activity = wc.asActivityRecord();
            if (activity != null) {
                if (!activity.finishing) {
                    return null;
                }
            } else {
                com.android.server.wm.TaskFragment taskFragment = wc.asTaskFragment();
                if (taskFragment != null && !taskFragment.isRemovalRequested() && taskFragment.getActivity(new com.android.server.wm.ActivityStarter$$ExternalSyntheticLambda0()) != null) {
                    if (taskFragment.isIsolatedNav()) {
                        return null;
                    }
                    if (sourceTaskFragment != null && sourceTaskFragment == taskFragment) {
                        return taskFragment;
                    }
                    if (!taskFragment.isPinned()) {
                        return taskFragment;
                    }
                }
            }
        }
        return null;
    }

    private void sendCanNotEmbedActivityError(com.android.server.wm.TaskFragment taskFragment, int result) {
        java.lang.String errMsg;
        switch (result) {
            case 1:
                errMsg = "The app:" + this.mCallingUid + "is not trusted to " + this.mStartActivity;
                break;
            case 2:
                errMsg = "Cannot embed " + this.mStartActivity + ". TaskFragment's bounds:" + taskFragment.getBounds() + ", minimum dimensions:" + this.mStartActivity.getMinDimensions();
                break;
            case 3:
                errMsg = "Cannot embed " + this.mStartActivity + " that launched on another task,mLaunchMode=" + android.content.pm.ActivityInfo.launchModeToString(this.mLaunchMode) + ",mLaunchFlag=" + java.lang.Integer.toHexString(this.mLaunchFlags);
                break;
            default:
                errMsg = "Unhandled embed result:" + result;
                break;
        }
        if (taskFragment.isOrganized()) {
            this.mService.mWindowOrganizerController.sendTaskFragmentOperationFailure(taskFragment.getTaskFragmentOrganizer(), this.mRequest.errorCallbackToken, taskFragment, 2, new java.lang.SecurityException(errMsg));
        } else {
            android.util.Slog.w(TAG, errMsg);
        }
    }

    private int adjustLaunchFlagsToDocumentMode(com.android.server.wm.ActivityRecord r, boolean launchSingleInstance, boolean launchSingleTask, int launchFlags) {
        if ((launchFlags & 524288) != 0 && (launchSingleInstance || launchSingleTask)) {
            android.util.Slog.i(TAG, "Ignoring FLAG_ACTIVITY_NEW_DOCUMENT, launchMode is \"singleInstance\" or \"singleTask\"");
            return launchFlags & (-134742017);
        }
        switch (r.info.documentLaunchMode) {
            case 0:
            default:
                return launchFlags;
            case 1:
                return launchFlags | 524288;
            case 2:
                return launchFlags | 524288;
            case 3:
                if (this.mLaunchMode == 4) {
                    if ((524288 & launchFlags) != 0) {
                        return launchFlags & (-134742017);
                    }
                    return launchFlags;
                }
                return launchFlags & (-134742017);
        }
    }

    private com.android.server.wm.Task getOrCreateRootTask(com.android.server.wm.ActivityRecord r, int launchFlags, com.android.server.wm.Task task, android.app.ActivityOptions aOptions) {
        boolean onTop = (aOptions == null || !aOptions.getAvoidMoveToFront()) && !this.mLaunchTaskBehind;
        boolean onTop2 = this.mASWrapper.getExtImpl().getScenarioTaskOrder(task, this.mPreferredTaskDisplayArea, this.mRequest.callingPackage, this.mOptions, this.mSourceRecord, onTop);
        com.android.server.wm.Task sourceTask = this.mSourceRecord != null ? this.mSourceRecord.getTask() : null;
        return this.mRootWindowContainer.getOrCreateRootTask(r, aOptions, task, sourceTask, onTop2, this.mLaunchParams, launchFlags);
    }

    private boolean isLaunchModeOneOf(int mode1, int mode2) {
        return mode1 == this.mLaunchMode || mode2 == this.mLaunchMode;
    }

    private boolean isLaunchModeOneOf(int mode1, int mode2, int mode3) {
        return mode1 == this.mLaunchMode || mode2 == this.mLaunchMode || mode3 == this.mLaunchMode;
    }

    static boolean isDocumentLaunchesIntoExisting(int flags) {
        return (524288 & flags) != 0 && (134217728 & flags) == 0;
    }

    com.android.server.wm.ActivityStarter setIntent(android.content.Intent intent) {
        this.mRequest.intent = intent;
        return this;
    }

    android.content.Intent getIntent() {
        return this.mRequest.intent;
    }

    com.android.server.wm.ActivityStarter setIntentGrants(com.android.server.uri.NeededUriGrants intentGrants) {
        this.mRequest.intentGrants = intentGrants;
        return this;
    }

    com.android.server.wm.ActivityStarter setReason(java.lang.String reason) {
        this.mRequest.reason = reason;
        return this;
    }

    com.android.server.wm.ActivityStarter setCaller(android.app.IApplicationThread caller) {
        this.mRequest.caller = caller;
        return this;
    }

    com.android.server.wm.ActivityStarter setResolvedType(java.lang.String type) {
        this.mRequest.resolvedType = type;
        return this;
    }

    com.android.server.wm.ActivityStarter setActivityInfo(android.content.pm.ActivityInfo info) {
        this.mRequest.activityInfo = info;
        return this;
    }

    com.android.server.wm.ActivityStarter setResolveInfo(android.content.pm.ResolveInfo info) {
        this.mRequest.resolveInfo = info;
        return this;
    }

    com.android.server.wm.ActivityStarter setVoiceSession(android.service.voice.IVoiceInteractionSession voiceSession) {
        this.mRequest.voiceSession = voiceSession;
        return this;
    }

    com.android.server.wm.ActivityStarter setVoiceInteractor(com.android.internal.app.IVoiceInteractor voiceInteractor) {
        this.mRequest.voiceInteractor = voiceInteractor;
        return this;
    }

    com.android.server.wm.ActivityStarter setResultTo(android.os.IBinder resultTo) {
        this.mRequest.resultTo = resultTo;
        return this;
    }

    com.android.server.wm.ActivityStarter setResultWho(java.lang.String resultWho) {
        this.mRequest.resultWho = resultWho;
        return this;
    }

    com.android.server.wm.ActivityStarter setRequestCode(int requestCode) {
        this.mRequest.requestCode = requestCode;
        return this;
    }

    com.android.server.wm.ActivityStarter setCallingPid(int pid) {
        this.mRequest.callingPid = pid;
        return this;
    }

    com.android.server.wm.ActivityStarter setCallingUid(int uid) {
        this.mRequest.callingUid = uid;
        return this;
    }

    com.android.server.wm.ActivityStarter setCallingPackage(java.lang.String callingPackage) {
        this.mRequest.callingPackage = callingPackage;
        return this;
    }

    com.android.server.wm.ActivityStarter setCallingFeatureId(java.lang.String callingFeatureId) {
        this.mRequest.callingFeatureId = callingFeatureId;
        return this;
    }

    com.android.server.wm.ActivityStarter setRealCallingPid(int pid) {
        this.mRequest.realCallingPid = pid;
        return this;
    }

    com.android.server.wm.ActivityStarter setRealCallingUid(int uid) {
        this.mRequest.realCallingUid = uid;
        return this;
    }

    com.android.server.wm.ActivityStarter setStartFlags(int startFlags) {
        this.mRequest.startFlags = startFlags;
        return this;
    }

    com.android.server.wm.ActivityStarter setActivityOptions(com.android.server.wm.SafeActivityOptions options) {
        this.mRequest.activityOptions = options;
        return this;
    }

    com.android.server.wm.ActivityStarter setActivityOptions(android.os.Bundle bOptions) {
        return setActivityOptions(com.android.server.wm.SafeActivityOptions.fromBundle(bOptions));
    }

    com.android.server.wm.ActivityStarter setIgnoreTargetSecurity(boolean ignoreTargetSecurity) {
        this.mRequest.ignoreTargetSecurity = ignoreTargetSecurity;
        return this;
    }

    com.android.server.wm.ActivityStarter setFilterCallingUid(int filterCallingUid) {
        this.mRequest.filterCallingUid = filterCallingUid;
        return this;
    }

    com.android.server.wm.ActivityStarter setComponentSpecified(boolean componentSpecified) {
        this.mRequest.componentSpecified = componentSpecified;
        return this;
    }

    com.android.server.wm.ActivityStarter setOutActivity(com.android.server.wm.ActivityRecord[] outActivity) {
        this.mRequest.outActivity = outActivity;
        return this;
    }

    com.android.server.wm.ActivityStarter setInTask(com.android.server.wm.Task inTask) {
        this.mRequest.inTask = inTask;
        return this;
    }

    com.android.server.wm.ActivityStarter setInTaskFragment(com.android.server.wm.TaskFragment taskFragment) {
        this.mRequest.inTaskFragment = taskFragment;
        return this;
    }

    com.android.server.wm.ActivityStarter setWaitResult(android.app.WaitResult result) {
        this.mRequest.waitResult = result;
        return this;
    }

    com.android.server.wm.ActivityStarter setProfilerInfo(android.app.ProfilerInfo info) {
        this.mRequest.profilerInfo = info;
        return this;
    }

    com.android.server.wm.ActivityStarter setGlobalConfiguration(android.content.res.Configuration config) {
        this.mRequest.globalConfig = config;
        return this;
    }

    com.android.server.wm.ActivityStarter setUserId(int userId) {
        this.mRequest.userId = userId;
        return this;
    }

    com.android.server.wm.ActivityStarter setAllowPendingRemoteAnimationRegistryLookup(boolean allowLookup) {
        this.mRequest.allowPendingRemoteAnimationRegistryLookup = allowLookup;
        return this;
    }

    com.android.server.wm.ActivityStarter setOriginatingPendingIntent(com.android.server.am.PendingIntentRecord originatingPendingIntent) {
        this.mRequest.originatingPendingIntent = originatingPendingIntent;
        return this;
    }

    com.android.server.wm.ActivityStarter setBackgroundStartPrivileges(android.app.BackgroundStartPrivileges forcedBalByPiSender) {
        this.mRequest.forcedBalByPiSender = forcedBalByPiSender;
        return this;
    }

    com.android.server.wm.ActivityStarter setFreezeScreen(boolean freezeScreen) {
        this.mRequest.freezeScreen = freezeScreen;
        return this;
    }

    com.android.server.wm.ActivityStarter setErrorCallbackToken(android.os.IBinder errorCallbackToken) {
        this.mRequest.errorCallbackToken = errorCallbackToken;
        return this;
    }

    void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        pw.print(prefix);
        pw.print("mCurrentUser=");
        pw.println(this.mRootWindowContainer.mCurrentUser);
        pw.print(prefix);
        pw.print("mLastStartReason=");
        pw.println(this.mLastStartReason);
        pw.print(prefix);
        pw.print("mLastStartActivityTimeMs=");
        pw.println(java.text.DateFormat.getDateTimeInstance().format(new java.util.Date(this.mLastStartActivityTimeMs)));
        pw.print(prefix);
        pw.print("mLastStartActivityResult=");
        pw.println(this.mLastStartActivityResult);
        if (this.mLastStartActivityRecord != null) {
            pw.print(prefix);
            pw.println("mLastStartActivityRecord:");
            this.mLastStartActivityRecord.dump(pw, prefix + "  ", true);
        }
        if (this.mStartActivity != null) {
            pw.print(prefix);
            pw.println("mStartActivity:");
            this.mStartActivity.dump(pw, prefix + "  ", true);
        }
        if (this.mIntent != null) {
            pw.print(prefix);
            pw.print("mIntent=");
            pw.println(this.mIntent);
        }
        if (this.mOptions != null) {
            pw.print(prefix);
            pw.print("mOptions=");
            pw.println(this.mOptions);
        }
        pw.print(prefix);
        pw.print("mLaunchMode=");
        pw.print(android.content.pm.ActivityInfo.launchModeToString(this.mLaunchMode));
        pw.print(prefix);
        pw.print("mLaunchFlags=0x");
        pw.print(java.lang.Integer.toHexString(this.mLaunchFlags));
        pw.print(" mDoResume=");
        pw.print(this.mDoResume);
        pw.print(" mAddingToTask=");
        pw.print(this.mAddingToTask);
        pw.print(" mInTaskFragment=");
        pw.println(this.mInTaskFragment);
    }

    public com.android.server.wm.IActivityStarterWrapper getWrapper() {
        return this.mASWrapper;
    }

    private class ActivityStarterWrapper implements com.android.server.wm.IActivityStarterWrapper {
        private com.android.server.wm.IActivityStarterExt mASExt;
        private com.android.server.wm.IActivityStarterSocExt mActivityStarterSocExt;

        private ActivityStarterWrapper() {
            this.mASExt = (com.android.server.wm.IActivityStarterExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IActivityStarterExt.class).base(com.android.server.wm.ActivityStarter.this).create();
            this.mActivityStarterSocExt = (com.android.server.wm.IActivityStarterSocExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IActivityStarterSocExt.class).base(com.android.server.wm.ActivityStarter.this).create();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public com.android.server.wm.IActivityStarterSocExt getSocExtImpl() {
            return this.mActivityStarterSocExt;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public com.android.server.wm.IActivityStarterExt getExtImpl() {
            return this.mASExt;
        }

        @Override // com.android.server.wm.IActivityStarterWrapper
        public void setTargetRootTaskIfNeeded(com.android.server.wm.ActivityRecord intentActivity, com.android.server.wm.BackgroundActivityStartController.BalVerdict balVerdict) {
            com.android.server.wm.ActivityStarter.this.setTargetRootTaskIfNeeded(intentActivity);
        }

        @Override // com.android.server.wm.IActivityStarterWrapper
        public void setSourceRecord(com.android.server.wm.ActivityRecord activityRecord) {
            com.android.server.wm.ActivityStarter.this.mSourceRecord = activityRecord;
        }

        @Override // com.android.server.wm.IActivityStarterWrapper
        public void setSourceRootTask(com.android.server.wm.Task task) {
            com.android.server.wm.ActivityStarter.this.mSourceRootTask = task;
        }

        @Override // com.android.server.wm.IActivityStarterWrapper
        public void setInTask(com.android.server.wm.Task task) {
            com.android.server.wm.ActivityStarter.this.mInTask = task;
        }

        @Override // com.android.server.wm.IActivityStarterWrapper
        public com.android.server.wm.ActivityTaskManagerService getService() {
            return com.android.server.wm.ActivityStarter.this.mService;
        }

        @Override // com.android.server.wm.IActivityStarterWrapper
        public android.app.ActivityOptions getOptions() {
            return com.android.server.wm.ActivityStarter.this.mOptions;
        }

        @Override // com.android.server.wm.IActivityStarterWrapper
        public void setOptions(android.app.ActivityOptions options) {
            com.android.server.wm.ActivityStarter.this.mOptions = options;
        }
    }
}
