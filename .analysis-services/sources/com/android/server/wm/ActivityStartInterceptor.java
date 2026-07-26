package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class ActivityStartInterceptor {
    private static final java.lang.String EXTRA_SKIP_ALERT_DIALOG = "extra.skip_alert_dialog";
    private static final java.lang.String TAG = "ActivityStartInterceptor";
    android.content.pm.ActivityInfo mAInfo;
    android.app.ActivityOptions mActivityOptions;
    private java.lang.String mCallingFeatureId;
    private java.lang.String mCallingPackage;
    int mCallingPid;
    int mCallingUid;
    com.android.server.wm.Task mInTask;
    com.android.server.wm.TaskFragment mInTaskFragment;
    android.content.Intent mIntent;
    com.android.server.wm.TaskDisplayArea mPresumableLaunchDisplayArea;
    android.content.pm.ResolveInfo mRInfo;
    private int mRealCallingPid;
    private int mRealCallingUid;
    java.lang.String mResolvedType;
    private final com.android.server.wm.ActivityTaskManagerService mService;
    private final android.content.Context mServiceContext;
    private int mStartFlags;
    private final com.android.server.wm.ActivityTaskSupervisor mSupervisor;
    private int mUserId;
    private android.os.UserManager mUserManager;

    ActivityStartInterceptor(com.android.server.wm.ActivityTaskManagerService service, com.android.server.wm.ActivityTaskSupervisor supervisor) {
        this(service, supervisor, service.mContext);
    }

    ActivityStartInterceptor(com.android.server.wm.ActivityTaskManagerService service, com.android.server.wm.ActivityTaskSupervisor supervisor, android.content.Context context) {
        this.mService = service;
        this.mSupervisor = supervisor;
        this.mServiceContext = context;
    }

    void setStates(int userId, int realCallingPid, int realCallingUid, int startFlags, java.lang.String callingPackage, java.lang.String callingFeatureId) {
        this.mRealCallingPid = realCallingPid;
        this.mRealCallingUid = realCallingUid;
        this.mUserId = userId;
        this.mStartFlags = startFlags;
        this.mCallingPackage = callingPackage;
        this.mCallingFeatureId = callingFeatureId;
    }

    private android.content.IntentSender createIntentSenderForOriginalIntent(int callingUid, int flags) throws java.lang.Throwable {
        android.app.ActivityOptions activityOptions = deferCrossProfileAppsAnimationIfNecessary();
        activityOptions.setPendingIntentCreatorBackgroundActivityStartMode(1);
        com.android.server.wm.TaskFragment taskFragment = getLaunchTaskFragment();
        if (taskFragment != null) {
            activityOptions.setLaunchTaskFragmentToken(taskFragment.getFragmentToken());
        }
        android.content.IIntentSender target = this.mService.getIntentSenderLocked(2, this.mCallingPackage, this.mCallingFeatureId, callingUid, this.mUserId, null, null, 0, new android.content.Intent[]{this.mIntent}, new java.lang.String[]{this.mResolvedType}, flags, activityOptions.toBundle());
        return new android.content.IntentSender(target);
    }

    private com.android.server.wm.TaskFragment getLaunchTaskFragment() {
        android.os.IBinder taskFragToken;
        if (this.mInTaskFragment != null) {
            return this.mInTaskFragment;
        }
        if (this.mActivityOptions == null || (taskFragToken = this.mActivityOptions.getLaunchTaskFragmentToken()) == null) {
            return null;
        }
        return com.android.server.wm.TaskFragment.fromTaskFragmentToken(taskFragToken, this.mService);
    }

    boolean intercept(android.content.Intent intent, android.content.pm.ResolveInfo rInfo, android.content.pm.ActivityInfo aInfo, java.lang.String resolvedType, com.android.server.wm.Task inTask, com.android.server.wm.TaskFragment inTaskFragment, int callingPid, int callingUid, android.app.ActivityOptions activityOptions, com.android.server.wm.TaskDisplayArea presumableLaunchDisplayArea) {
        this.mUserManager = android.os.UserManager.get(this.mServiceContext);
        this.mIntent = intent;
        this.mCallingPid = callingPid;
        this.mCallingUid = callingUid;
        this.mRInfo = rInfo;
        this.mAInfo = aInfo;
        this.mResolvedType = resolvedType;
        this.mInTask = inTask;
        this.mInTaskFragment = inTaskFragment;
        this.mActivityOptions = activityOptions;
        this.mPresumableLaunchDisplayArea = presumableLaunchDisplayArea;
        if (interceptQuietProfileIfNeeded() || interceptSuspendedPackageIfNeeded() || interceptLockTaskModeViolationPackageIfNeeded() || interceptHarmfulAppIfNeeded() || interceptLockedProfileIfNeeded() || interceptHomeIfNeeded()) {
            return true;
        }
        android.util.SparseArray<com.android.server.wm.ActivityInterceptorCallback> callbacks = this.mService.getActivityInterceptorCallbacks();
        com.android.server.wm.ActivityInterceptorCallback.ActivityInterceptorInfo interceptorInfo = getInterceptorInfo(null);
        for (int i = 0; i < callbacks.size(); i++) {
            com.android.server.wm.ActivityInterceptorCallback callback = callbacks.valueAt(i);
            com.android.server.wm.ActivityInterceptorCallback.ActivityInterceptResult interceptResult = callback.onInterceptActivityLaunch(interceptorInfo);
            if (interceptResult != null) {
                this.mIntent = interceptResult.getIntent();
                this.mActivityOptions = interceptResult.getActivityOptions();
                this.mCallingPid = this.mRealCallingPid;
                this.mCallingUid = this.mRealCallingUid;
                if (!interceptResult.isActivityResolved()) {
                    this.mRInfo = this.mSupervisor.resolveIntent(this.mIntent, null, this.mUserId, 0, this.mRealCallingUid, this.mRealCallingPid);
                    this.mAInfo = this.mSupervisor.resolveActivity(this.mIntent, this.mRInfo, this.mStartFlags, null);
                    return true;
                }
                return true;
            }
        }
        return false;
    }

    private boolean hasCrossProfileAnimation() {
        return this.mActivityOptions != null && this.mActivityOptions.getAnimationType() == 12;
    }

    private android.app.ActivityOptions deferCrossProfileAppsAnimationIfNecessary() {
        if (hasCrossProfileAnimation()) {
            this.mActivityOptions = null;
            return android.app.ActivityOptions.makeOpenCrossProfileAppsAnimation();
        }
        return android.app.ActivityOptions.makeBasic();
    }

    private boolean interceptQuietProfileIfNeeded() throws java.lang.Throwable {
        if (!this.mUserManager.isQuietModeEnabled(android.os.UserHandle.of(this.mUserId))) {
            return false;
        }
        android.util.Slog.i(TAG, "Intent : " + this.mIntent + " intercepted for user: " + this.mUserId + " because quiet mode is enabled.");
        android.content.IntentSender target = createIntentSenderForOriginalIntent(this.mCallingUid, 1342177280);
        this.mIntent = com.android.internal.app.UnlaunchableAppActivity.createInQuietModeDialogIntent(this.mUserId, target, this.mRInfo);
        this.mCallingPid = this.mRealCallingPid;
        this.mCallingUid = this.mRealCallingUid;
        this.mResolvedType = null;
        android.content.pm.UserInfo parent = this.mUserManager.getProfileParent(this.mUserId);
        this.mRInfo = this.mSupervisor.resolveIntent(this.mIntent, this.mResolvedType, parent.id, 0, this.mRealCallingUid, this.mRealCallingPid);
        this.mAInfo = this.mSupervisor.resolveActivity(this.mIntent, this.mRInfo, this.mStartFlags, null);
        return true;
    }

    private boolean interceptSuspendedByAdminPackage() {
        android.app.admin.DevicePolicyManagerInternal devicePolicyManager = (android.app.admin.DevicePolicyManagerInternal) com.android.server.LocalServices.getService(android.app.admin.DevicePolicyManagerInternal.class);
        if (devicePolicyManager == null) {
            return false;
        }
        this.mIntent = devicePolicyManager.createShowAdminSupportIntent(this.mUserId, true);
        this.mIntent.putExtra("android.app.extra.RESTRICTION", "policy_suspend_packages");
        this.mCallingPid = this.mRealCallingPid;
        this.mCallingUid = this.mRealCallingUid;
        this.mResolvedType = null;
        android.content.pm.UserInfo parent = this.mUserManager.getProfileParent(this.mUserId);
        if (parent != null) {
            this.mRInfo = this.mSupervisor.resolveIntent(this.mIntent, this.mResolvedType, parent.id, 0, this.mRealCallingUid, this.mRealCallingPid);
        } else {
            this.mRInfo = this.mSupervisor.resolveIntent(this.mIntent, this.mResolvedType, this.mUserId, 0, this.mRealCallingUid, this.mRealCallingPid);
        }
        this.mAInfo = this.mSupervisor.resolveActivity(this.mIntent, this.mRInfo, this.mStartFlags, null);
        return true;
    }

    private boolean interceptSuspendedPackageIfNeeded() throws java.lang.Throwable {
        android.content.pm.PackageManagerInternal pmi;
        android.os.Bundle crossProfileOptions;
        if (!isPackageSuspended() || (pmi = this.mService.getPackageManagerInternalLocked()) == null) {
            return false;
        }
        java.lang.String suspendedPackage = this.mAInfo.applicationInfo.packageName;
        android.content.pm.UserPackage suspender = pmi.getSuspendingPackage(suspendedPackage, this.mUserId);
        if (suspender != null && com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME.equals(suspender.packageName)) {
            return interceptSuspendedByAdminPackage();
        }
        android.content.pm.SuspendDialogInfo dialogInfo = pmi.getSuspendedDialogInfo(suspendedPackage, suspender, this.mUserId);
        if (hasCrossProfileAnimation()) {
            crossProfileOptions = android.app.ActivityOptions.makeOpenCrossProfileAppsAnimation().toBundle();
        } else {
            crossProfileOptions = null;
        }
        android.content.IntentSender target = createIntentSenderForOriginalIntent(this.mCallingUid, 67108864);
        this.mIntent = com.android.internal.app.SuspendedAppActivity.createSuspendedAppInterceptIntent(suspendedPackage, suspender, dialogInfo, crossProfileOptions, target, this.mUserId);
        this.mCallingPid = this.mRealCallingPid;
        this.mCallingUid = this.mRealCallingUid;
        this.mResolvedType = null;
        this.mRInfo = this.mSupervisor.resolveIntent(this.mIntent, this.mResolvedType, this.mUserId, 0, this.mRealCallingUid, this.mRealCallingPid);
        this.mAInfo = this.mSupervisor.resolveActivity(this.mIntent, this.mRInfo, this.mStartFlags, null);
        return true;
    }

    private boolean interceptLockTaskModeViolationPackageIfNeeded() {
        if (this.mAInfo == null || this.mAInfo.applicationInfo == null) {
            return false;
        }
        com.android.server.wm.LockTaskController controller = this.mService.getLockTaskController();
        java.lang.String packageName = this.mAInfo.applicationInfo.packageName;
        int lockTaskLaunchMode = com.android.server.wm.ActivityRecord.getLockTaskLaunchMode(this.mAInfo, this.mActivityOptions);
        if (controller.isActivityAllowed(this.mUserId, packageName, lockTaskLaunchMode)) {
            return false;
        }
        if (controller.mLockTaskControllerExt.shouldSkipBlockedAppActivity()) {
            this.mIntent.putExtra(EXTRA_SKIP_ALERT_DIALOG, 1);
        }
        this.mIntent = com.android.internal.app.BlockedAppActivity.createIntent(this.mUserId, this.mAInfo.applicationInfo.packageName);
        this.mCallingPid = this.mRealCallingPid;
        this.mCallingUid = this.mRealCallingUid;
        this.mResolvedType = null;
        this.mRInfo = this.mSupervisor.resolveIntent(this.mIntent, this.mResolvedType, this.mUserId, 0, this.mRealCallingUid, this.mRealCallingPid);
        this.mAInfo = this.mSupervisor.resolveActivity(this.mIntent, this.mRInfo, this.mStartFlags, null);
        return true;
    }

    private boolean interceptLockedProfileIfNeeded() throws java.lang.Throwable {
        com.android.server.wm.Task parentTask;
        android.content.Intent interceptingIntent = interceptWithConfirmCredentialsIfNeeded(this.mAInfo, this.mUserId);
        if (interceptingIntent == null) {
            return false;
        }
        this.mIntent = interceptingIntent;
        this.mCallingPid = this.mRealCallingPid;
        this.mCallingUid = this.mRealCallingUid;
        this.mResolvedType = null;
        com.android.server.wm.TaskFragment taskFragment = getLaunchTaskFragment();
        if (this.mInTask != null) {
            this.mIntent.putExtra("android.intent.extra.TASK_ID", this.mInTask.mTaskId);
            this.mInTask = null;
        } else if (taskFragment != null && (parentTask = taskFragment.getTask()) != null) {
            this.mIntent.putExtra("android.intent.extra.TASK_ID", parentTask.mTaskId);
        }
        if (this.mActivityOptions == null) {
            this.mActivityOptions = android.app.ActivityOptions.makeBasic();
        }
        android.content.pm.UserInfo parent = this.mUserManager.getProfileParent(this.mUserId);
        this.mRInfo = this.mSupervisor.resolveIntent(this.mIntent, this.mResolvedType, parent.id, 0, this.mRealCallingUid, this.mRealCallingPid);
        this.mAInfo = this.mSupervisor.resolveActivity(this.mIntent, this.mRInfo, this.mStartFlags, null);
        return true;
    }

    private android.content.Intent interceptWithConfirmCredentialsIfNeeded(android.content.pm.ActivityInfo aInfo, int userId) throws java.lang.Throwable {
        if (!this.mService.mAmInternal.shouldConfirmCredentials(userId)) {
            return null;
        }
        if ((aInfo.flags & 8388608) != 0 && (this.mUserManager.isUserUnlocked(userId) || aInfo.directBootAware)) {
            return null;
        }
        android.content.IntentSender target = createIntentSenderForOriginalIntent(this.mCallingUid, 1409286144);
        android.app.KeyguardManager km = (android.app.KeyguardManager) this.mServiceContext.getSystemService("keyguard");
        android.content.Intent newIntent = km.createConfirmDeviceCredentialIntent(null, null, userId, true);
        if (newIntent == null) {
            return null;
        }
        newIntent.setFlags(276840448);
        newIntent.putExtra("android.intent.extra.PACKAGE_NAME", aInfo.packageName);
        newIntent.putExtra("android.intent.extra.INTENT", target);
        return newIntent;
    }

    private boolean interceptHarmfulAppIfNeeded() throws java.lang.Throwable {
        try {
            java.lang.CharSequence harmfulAppWarning = this.mService.getPackageManager().getHarmfulAppWarning(this.mAInfo.packageName, this.mUserId);
            if (harmfulAppWarning == null) {
                return false;
            }
            android.content.IntentSender target = createIntentSenderForOriginalIntent(this.mCallingUid, 1409286144);
            this.mIntent = com.android.internal.app.HarmfulAppWarningActivity.createHarmfulAppWarningIntent(this.mServiceContext, this.mAInfo.packageName, target, harmfulAppWarning);
            this.mCallingPid = this.mRealCallingPid;
            this.mCallingUid = this.mRealCallingUid;
            this.mResolvedType = null;
            this.mRInfo = this.mSupervisor.resolveIntent(this.mIntent, this.mResolvedType, this.mUserId, 0, this.mRealCallingUid, this.mRealCallingPid);
            this.mAInfo = this.mSupervisor.resolveActivity(this.mIntent, this.mRInfo, this.mStartFlags, null);
            return true;
        } catch (android.os.RemoteException | java.lang.IllegalArgumentException e) {
            return false;
        }
    }

    private boolean interceptHomeIfNeeded() {
        if (this.mPresumableLaunchDisplayArea == null || this.mService.mRootWindowContainer == null || !com.android.server.wm.ActivityRecord.isHomeIntent(this.mIntent) || !this.mIntent.hasCategory("android.intent.category.HOME") || this.mService.mRootWindowContainer.shouldPlacePrimaryHomeOnDisplay(this.mPresumableLaunchDisplayArea.getDisplayId()) || !this.mService.mRootWindowContainer.shouldPlaceSecondaryHomeOnDisplayArea(this.mPresumableLaunchDisplayArea)) {
            return false;
        }
        android.util.Pair<android.content.pm.ActivityInfo, android.content.Intent> info = this.mService.mRootWindowContainer.resolveSecondaryHomeActivity(this.mUserId, this.mPresumableLaunchDisplayArea);
        this.mIntent = (android.content.Intent) info.second;
        this.mIntent.addFlags(268435456);
        this.mCallingPid = this.mRealCallingPid;
        this.mCallingUid = this.mRealCallingUid;
        this.mResolvedType = null;
        this.mRInfo = this.mSupervisor.resolveIntent(this.mIntent, this.mResolvedType, this.mUserId, 0, this.mRealCallingUid, this.mRealCallingPid);
        this.mAInfo = this.mSupervisor.resolveActivity(this.mIntent, this.mRInfo, this.mStartFlags, null);
        return true;
    }

    private boolean isPackageSuspended() {
        return (this.mAInfo == null || this.mAInfo.applicationInfo == null || (this.mAInfo.applicationInfo.flags & 1073741824) == 0) ? false : true;
    }

    void onActivityLaunched(android.app.TaskInfo taskInfo, final com.android.server.wm.ActivityRecord r) {
        android.util.SparseArray<com.android.server.wm.ActivityInterceptorCallback> callbacks = this.mService.getActivityInterceptorCallbacks();
        com.android.server.wm.ActivityInterceptorCallback.ActivityInterceptorInfo info = getInterceptorInfo(new java.lang.Runnable() { // from class: com.android.server.wm.ActivityStartInterceptor$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onActivityLaunched$0(r);
            }
        });
        for (int i = 0; i < callbacks.size(); i++) {
            com.android.server.wm.ActivityInterceptorCallback callback = callbacks.valueAt(i);
            callback.onActivityLaunched(taskInfo, r.info, info);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onActivityLaunched$0(com.android.server.wm.ActivityRecord r) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                r.clearOptionsAnimationForSiblings();
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    private com.android.server.wm.ActivityInterceptorCallback.ActivityInterceptorInfo getInterceptorInfo(java.lang.Runnable clearOptionsAnimation) {
        return new com.android.server.wm.ActivityInterceptorCallback.ActivityInterceptorInfo.Builder(this.mCallingUid, this.mCallingPid, this.mRealCallingUid, this.mRealCallingPid, this.mUserId, this.mIntent, this.mRInfo, this.mAInfo).setResolvedType(this.mResolvedType).setCallingPackage(this.mCallingPackage).setCallingFeatureId(this.mCallingFeatureId).setCheckedOptions(this.mActivityOptions).setClearOptionsAnimationRunnable(clearOptionsAnimation).build();
    }
}
