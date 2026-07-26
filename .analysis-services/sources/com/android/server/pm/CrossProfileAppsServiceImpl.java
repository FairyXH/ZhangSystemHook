package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public class CrossProfileAppsServiceImpl extends android.content.pm.ICrossProfileApps.Stub {
    private static final java.lang.String TAG = "CrossProfileAppsService";
    private final android.content.Context mContext;
    private final com.android.server.pm.ICrossProfileAppsServiceImplExt mImplExt;
    private final com.android.server.pm.CrossProfileAppsServiceImpl.Injector mInjector;
    private final com.android.server.pm.CrossProfileAppsServiceImpl.LocalService mLocalService;

    public interface Injector {
        int checkComponentPermission(java.lang.String str, int i, int i2, boolean z);

        long clearCallingIdentity();

        android.app.ActivityManagerInternal getActivityManagerInternal();

        com.android.server.wm.ActivityTaskManagerInternal getActivityTaskManagerInternal();

        android.app.AppOpsManager getAppOpsManager();

        int getCallingPid();

        int getCallingUid();

        android.os.UserHandle getCallingUserHandle();

        int getCallingUserId();

        android.app.admin.DevicePolicyManagerInternal getDevicePolicyManagerInternal();

        android.content.pm.IPackageManager getIPackageManager();

        android.content.pm.PackageManager getPackageManager();

        android.content.pm.PackageManagerInternal getPackageManagerInternal();

        android.os.UserManager getUserManager();

        void killUid(int i);

        void restoreCallingIdentity(long j);

        void sendBroadcastAsUser(android.content.Intent intent, android.os.UserHandle userHandle);

        <T> T withCleanCallingIdentity(com.android.internal.util.FunctionalUtils.ThrowingSupplier<T> throwingSupplier);

        void withCleanCallingIdentity(com.android.internal.util.FunctionalUtils.ThrowingRunnable throwingRunnable);
    }

    public CrossProfileAppsServiceImpl(android.content.Context context) {
        this(context, new com.android.server.pm.CrossProfileAppsServiceImpl.InjectorImpl(context));
    }

    CrossProfileAppsServiceImpl(android.content.Context context, com.android.server.pm.CrossProfileAppsServiceImpl.Injector injector) {
        this.mLocalService = new com.android.server.pm.CrossProfileAppsServiceImpl.LocalService();
        this.mImplExt = (com.android.server.pm.ICrossProfileAppsServiceImplExt) system.ext.loader.core.ExtLoader.type(com.android.server.pm.ICrossProfileAppsServiceImplExt.class).create();
        this.mContext = context;
        this.mInjector = injector;
    }

    public java.util.List<android.os.UserHandle> getTargetUserProfiles(java.lang.String callingPackage) {
        java.util.Objects.requireNonNull(callingPackage);
        verifyCallingPackage(callingPackage);
        android.app.admin.DevicePolicyEventLogger.createEvent(125).setStrings(new java.lang.String[]{callingPackage}).write();
        return getTargetUserProfilesUnchecked(callingPackage, this.mInjector.getCallingUserId());
    }

    public void startActivityAsUser(android.app.IApplicationThread caller, java.lang.String callingPackage, java.lang.String callingFeatureId, android.content.ComponentName component, int userId, boolean launchMainActivity, android.os.IBinder targetTask, android.os.Bundle options) throws android.os.RemoteException {
        android.os.Bundle options2;
        java.util.Objects.requireNonNull(callingPackage);
        java.util.Objects.requireNonNull(component);
        verifyCallingPackage(callingPackage);
        android.app.admin.DevicePolicyEventLogger.createEvent(126).setStrings(new java.lang.String[]{callingPackage}).write();
        int callerUserId = this.mInjector.getCallingUserId();
        int callingUid = this.mInjector.getCallingUid();
        int callingPid = this.mInjector.getCallingPid();
        java.util.List<android.os.UserHandle> allowedTargetUsers = getTargetUserProfilesUnchecked(callingPackage, callerUserId);
        if (!allowedTargetUsers.contains(android.os.UserHandle.of(userId))) {
            throw new java.lang.SecurityException(callingPackage + " cannot access unrelated user " + userId);
        }
        if (!callingPackage.equals(component.getPackageName())) {
            throw new java.lang.SecurityException(callingPackage + " attempts to start an activity in other package - " + component.getPackageName());
        }
        android.content.Intent launchIntent = new android.content.Intent();
        if (launchMainActivity) {
            launchIntent.setAction("android.intent.action.MAIN");
            launchIntent.addCategory("android.intent.category.LAUNCHER");
            if (targetTask == null || options != null) {
                launchIntent.addFlags(270532608);
            } else {
                launchIntent.addFlags(2097152);
            }
            launchIntent.setPackage(component.getPackageName());
        } else {
            if (callerUserId != userId) {
                if (!hasInteractAcrossProfilesPermission(callingPackage, callingUid, callingPid) && !isPermissionGranted("android.permission.START_CROSS_PROFILE_ACTIVITIES", callingUid)) {
                    throw new java.lang.SecurityException("Attempt to launch activity without one of the required android.permission.INTERACT_ACROSS_PROFILES or android.permission.START_CROSS_PROFILE_ACTIVITIES permissions.");
                }
                if (!isSameProfileGroup(callerUserId, userId)) {
                    throw new java.lang.SecurityException("Attempt to launch activity when target user is not in the same profile group.");
                }
            }
            launchIntent.setComponent(component);
        }
        verifyActivityCanHandleIntentAndExported(launchIntent, component, callingUid, userId);
        if (options == null) {
            options2 = android.app.ActivityOptions.makeOpenCrossProfileAppsAnimation().toBundle();
        } else {
            options.putAll(android.app.ActivityOptions.makeOpenCrossProfileAppsAnimation().toBundle());
            options2 = options;
        }
        launchIntent.setPackage(null);
        launchIntent.setComponent(component);
        this.mInjector.getActivityTaskManagerInternal().startActivityAsUser(caller, callingPackage, callingFeatureId, launchIntent, targetTask, 0, options2, userId);
    }

    public void startActivityAsUserByIntent(android.app.IApplicationThread caller, java.lang.String callingPackage, java.lang.String callingFeatureId, android.content.Intent intent, int userId, android.os.IBinder callingActivity, android.os.Bundle options) throws android.os.RemoteException {
        java.util.Objects.requireNonNull(callingPackage);
        java.util.Objects.requireNonNull(intent);
        java.util.Objects.requireNonNull(intent.getComponent(), "The intent must have a Component set");
        verifyCallingPackage(callingPackage);
        int callerUserId = this.mInjector.getCallingUserId();
        int callingUid = this.mInjector.getCallingUid();
        java.util.List<android.os.UserHandle> allowedTargetUsers = getTargetUserProfilesUnchecked(callingPackage, callerUserId);
        if (callerUserId != userId && !allowedTargetUsers.contains(android.os.UserHandle.of(userId))) {
            throw new java.lang.SecurityException(callingPackage + " cannot access unrelated user " + userId);
        }
        android.content.Intent launchIntent = new android.content.Intent(intent);
        launchIntent.setPackage(callingPackage);
        if (!callingPackage.equals(launchIntent.getComponent().getPackageName())) {
            throw new java.lang.SecurityException(callingPackage + " attempts to start an activity in other package - " + launchIntent.getComponent().getPackageName());
        }
        if (callerUserId != userId && !hasCallerGotInteractAcrossProfilesPermission(callingPackage)) {
            throw new java.lang.SecurityException("Attempt to launch activity without required android.permission.INTERACT_ACROSS_PROFILES permission or target user is not in the same profile group.");
        }
        verifyActivityCanHandleIntent(launchIntent, callingUid, userId);
        this.mInjector.getActivityTaskManagerInternal().startActivityAsUser(caller, callingPackage, callingFeatureId, launchIntent, callingActivity, 0, options, userId);
        logStartActivityByIntent(callingPackage);
    }

    private void logStartActivityByIntent(java.lang.String packageName) {
        android.app.admin.DevicePolicyEventLogger.createEvent(150).setStrings(new java.lang.String[]{packageName}).setBoolean(isCallingUserAManagedProfile()).write();
    }

    public boolean canRequestInteractAcrossProfiles(java.lang.String callingPackage) {
        java.util.Objects.requireNonNull(callingPackage);
        verifyCallingPackage(callingPackage);
        return canRequestInteractAcrossProfilesUnchecked(callingPackage);
    }

    private boolean canRequestInteractAcrossProfilesUnchecked(java.lang.String packageName) {
        int callingUserId = this.mInjector.getCallingUserId();
        int[] enabledProfileIds = this.mInjector.getUserManager().getProfileIdsExcludingHidden(callingUserId, true);
        if (enabledProfileIds.length >= 2 && !isProfileOwner(packageName, enabledProfileIds)) {
            return hasRequestedAppOpPermission(android.app.AppOpsManager.opToPermission(93), packageName, callingUserId);
        }
        return false;
    }

    private boolean hasRequestedAppOpPermission(java.lang.String permission, java.lang.String packageName, int userId) {
        try {
            java.lang.String[] packages = this.mInjector.getIPackageManager().getAppOpPermissionPackages(permission, userId);
            return com.android.internal.util.ArrayUtils.contains(packages, packageName);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "PackageManager dead. Cannot get permission info");
            return false;
        }
    }

    public boolean canInteractAcrossProfiles(java.lang.String callingPackage) {
        java.util.Objects.requireNonNull(callingPackage);
        verifyCallingPackage(callingPackage);
        java.util.List<android.os.UserHandle> targetUserProfiles = getTargetUserProfilesUnchecked(callingPackage, this.mInjector.getCallingUserId());
        return !targetUserProfiles.isEmpty() && hasCallerGotInteractAcrossProfilesPermission(callingPackage) && haveProfilesGotInteractAcrossProfilesPermission(callingPackage, targetUserProfiles);
    }

    private boolean hasCallerGotInteractAcrossProfilesPermission(java.lang.String callingPackage) {
        return hasInteractAcrossProfilesPermission(callingPackage, this.mInjector.getCallingUid(), this.mInjector.getCallingPid());
    }

    private boolean haveProfilesGotInteractAcrossProfilesPermission(final java.lang.String packageName, java.util.List<android.os.UserHandle> profiles) {
        for (final android.os.UserHandle profile : profiles) {
            int uid = ((java.lang.Integer) this.mInjector.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.pm.CrossProfileAppsServiceImpl$$ExternalSyntheticLambda12
                public final java.lang.Object getOrThrow() {
                    return this.f$0.lambda$haveProfilesGotInteractAcrossProfilesPermission$0(packageName, profile);
                }
            })).intValue();
            if (uid == -1 || !hasInteractAcrossProfilesPermission(packageName, uid, -1)) {
                return false;
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.lang.Integer lambda$haveProfilesGotInteractAcrossProfilesPermission$0(java.lang.String packageName, android.os.UserHandle profile) throws java.lang.Exception {
        try {
            return java.lang.Integer.valueOf(this.mInjector.getPackageManager().getPackageUidAsUser(packageName, 0, profile.getIdentifier()));
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            return -1;
        }
    }

    private boolean isCrossProfilePackageAllowlisted(final java.lang.String packageName) {
        final int userId = this.mInjector.getCallingUserId();
        return ((java.lang.Boolean) this.mInjector.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.pm.CrossProfileAppsServiceImpl$$ExternalSyntheticLambda14
            public final java.lang.Object getOrThrow() {
                return this.f$0.lambda$isCrossProfilePackageAllowlisted$1(userId, packageName);
            }
        })).booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.lang.Boolean lambda$isCrossProfilePackageAllowlisted$1(int userId, java.lang.String packageName) throws java.lang.Exception {
        return java.lang.Boolean.valueOf(this.mInjector.getDevicePolicyManagerInternal().getAllCrossProfilePackages(userId).contains(packageName));
    }

    private boolean isCrossProfilePackageAllowlistedByDefault(final java.lang.String packageName) {
        return ((java.lang.Boolean) this.mInjector.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.pm.CrossProfileAppsServiceImpl$$ExternalSyntheticLambda6
            public final java.lang.Object getOrThrow() {
                return this.f$0.lambda$isCrossProfilePackageAllowlistedByDefault$2(packageName);
            }
        })).booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.lang.Boolean lambda$isCrossProfilePackageAllowlistedByDefault$2(java.lang.String packageName) throws java.lang.Exception {
        return java.lang.Boolean.valueOf(this.mInjector.getDevicePolicyManagerInternal().getDefaultCrossProfilePackages().contains(packageName));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.util.List<android.os.UserHandle> getTargetUserProfilesUnchecked(final java.lang.String packageName, final int userId) {
        return (java.util.List) this.mInjector.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.pm.CrossProfileAppsServiceImpl$$ExternalSyntheticLambda9
            public final java.lang.Object getOrThrow() {
                return this.f$0.lambda$getTargetUserProfilesUnchecked$3(userId, packageName);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.util.List lambda$getTargetUserProfilesUnchecked$3(int userId, java.lang.String packageName) throws java.lang.Exception {
        int[] enabledProfileIds = this.mInjector.getUserManager().getProfileIdsExcludingHidden(userId, true);
        java.util.List<android.os.UserHandle> targetProfiles = new java.util.ArrayList<>();
        for (int profileId : enabledProfileIds) {
            if (profileId != userId && isPackageEnabled(packageName, profileId) && !this.mImplExt.skipProfileInGetTargetUserProfilesUnchecked(profileId, packageName)) {
                targetProfiles.add(android.os.UserHandle.of(profileId));
            }
        }
        return targetProfiles;
    }

    private boolean isPackageEnabled(final java.lang.String packageName, final int userId) {
        final int callingUid = this.mInjector.getCallingUid();
        return ((java.lang.Boolean) this.mInjector.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.pm.CrossProfileAppsServiceImpl$$ExternalSyntheticLambda11
            public final java.lang.Object getOrThrow() {
                return this.f$0.lambda$isPackageEnabled$4(packageName, callingUid, userId);
            }
        })).booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.lang.Boolean lambda$isPackageEnabled$4(java.lang.String packageName, int callingUid, int userId) throws java.lang.Exception {
        android.content.pm.PackageInfo info = this.mInjector.getPackageManagerInternal().getPackageInfo(packageName, 786432L, callingUid, userId);
        return java.lang.Boolean.valueOf(info != null && info.applicationInfo.enabled);
    }

    private void verifyActivityCanHandleIntent(final android.content.Intent launchIntent, final int callingUid, final int userId) {
        this.mInjector.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.pm.CrossProfileAppsServiceImpl$$ExternalSyntheticLambda13
            public final void runOrThrow() throws java.lang.Exception {
                this.f$0.lambda$verifyActivityCanHandleIntent$5(launchIntent, callingUid, userId);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$verifyActivityCanHandleIntent$5(android.content.Intent launchIntent, int callingUid, int userId) throws java.lang.Exception {
        java.util.List<android.content.pm.ResolveInfo> activities = this.mInjector.getPackageManagerInternal().queryIntentActivities(launchIntent, launchIntent.resolveTypeIfNeeded(this.mContext.getContentResolver()), 786432L, callingUid, userId);
        if (!activities.isEmpty()) {
        } else {
            throw new java.lang.SecurityException("Activity cannot handle intent");
        }
    }

    private void verifyActivityCanHandleIntentAndExported(final android.content.Intent launchIntent, final android.content.ComponentName component, final int callingUid, final int userId) {
        this.mInjector.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.pm.CrossProfileAppsServiceImpl$$ExternalSyntheticLambda0
            public final void runOrThrow() throws java.lang.Exception {
                this.f$0.lambda$verifyActivityCanHandleIntentAndExported$6(launchIntent, callingUid, userId, component);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$verifyActivityCanHandleIntentAndExported$6(android.content.Intent launchIntent, int callingUid, int userId, android.content.ComponentName component) throws java.lang.Exception {
        java.util.List<android.content.pm.ResolveInfo> apps = this.mInjector.getPackageManagerInternal().queryIntentActivities(launchIntent, launchIntent.resolveTypeIfNeeded(this.mContext.getContentResolver()), 786432L, callingUid, userId);
        int size = apps.size();
        for (int i = 0; i < size; i++) {
            android.content.pm.ActivityInfo activityInfo = apps.get(i).activityInfo;
            if (android.text.TextUtils.equals(activityInfo.packageName, component.getPackageName()) && android.text.TextUtils.equals(activityInfo.name, component.getClassName()) && activityInfo.exported) {
                return;
            }
        }
        throw new java.lang.SecurityException("Attempt to launch activity without  category Intent.CATEGORY_LAUNCHER or activity is not exported" + component);
    }

    /* JADX INFO: renamed from: setInteractAcrossProfilesAppOp, reason: merged with bridge method [inline-methods] */
    public void lambda$clearInteractAcrossProfilesAppOps$11(int userId, java.lang.String packageName, int newMode) {
        setInteractAcrossProfilesAppOp(packageName, newMode, userId);
    }

    private void setInteractAcrossProfilesAppOp(java.lang.String packageName, int newMode, int userId) {
        int callingUid = this.mInjector.getCallingUid();
        if (!isPermissionGranted("android.permission.INTERACT_ACROSS_USERS_FULL", callingUid) && !isPermissionGranted("android.permission.INTERACT_ACROSS_USERS", callingUid)) {
            throw new java.lang.SecurityException("INTERACT_ACROSS_USERS or INTERACT_ACROSS_USERS_FULL is required to set the app-op for interacting across profiles.");
        }
        if (!isPermissionGranted("android.permission.MANAGE_APP_OPS_MODES", callingUid) && !isPermissionGranted("android.permission.CONFIGURE_INTERACT_ACROSS_PROFILES", callingUid)) {
            throw new java.lang.SecurityException("MANAGE_APP_OPS_MODES or CONFIGURE_INTERACT_ACROSS_PROFILES is required to set the app-op for interacting across profiles.");
        }
        setInteractAcrossProfilesAppOpUnchecked(packageName, newMode, userId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setInteractAcrossProfilesAppOpUnchecked(java.lang.String packageName, int newMode, int userId) {
        if (newMode == 0 && !canConfigureInteractAcrossProfiles(packageName, userId)) {
            android.util.Slog.e(TAG, "Tried to turn on the appop for interacting across profiles for invalid app " + packageName);
            return;
        }
        int[] profileIds = this.mInjector.getUserManager().getProfileIdsExcludingHidden(userId, false);
        int length = profileIds.length;
        for (int i = 0; i < length; i++) {
            int profileId = profileIds[i];
            if (isPackageInstalled(packageName, profileId)) {
                setInteractAcrossProfilesAppOpForProfile(packageName, newMode, profileId, profileId == userId);
            }
        }
    }

    private boolean isPackageInstalled(final java.lang.String packageName, final int userId) {
        return ((java.lang.Boolean) this.mInjector.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.pm.CrossProfileAppsServiceImpl$$ExternalSyntheticLambda4
            public final java.lang.Object getOrThrow() {
                return this.f$0.lambda$isPackageInstalled$7(packageName, userId);
            }
        })).booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.lang.Boolean lambda$isPackageInstalled$7(java.lang.String packageName, int userId) throws java.lang.Exception {
        android.content.pm.PackageInfo info = this.mInjector.getPackageManagerInternal().getPackageInfo(packageName, 786432L, this.mInjector.getCallingUid(), userId);
        return java.lang.Boolean.valueOf(info != null);
    }

    private void setInteractAcrossProfilesAppOpForProfile(java.lang.String packageName, int newMode, int profileId, boolean logMetrics) {
        try {
            setInteractAcrossProfilesAppOpForProfileOrThrow(packageName, newMode, profileId, logMetrics);
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            android.util.Slog.e(TAG, "Missing package " + packageName + " on profile user ID " + profileId, e);
        }
    }

    private void setInteractAcrossProfilesAppOpForProfileOrThrow(java.lang.String packageName, final int newMode, int profileId, boolean logMetrics) throws android.content.pm.PackageManager.NameNotFoundException {
        final int uid = this.mInjector.getPackageManager().getPackageUidAsUser(packageName, 0, profileId);
        if (currentModeEquals(newMode, packageName, uid)) {
            android.util.Slog.i(TAG, "Attempt to set mode to existing value of " + newMode + " for " + packageName + " on profile user ID " + profileId);
            return;
        }
        if (this.mImplExt.interceptInSetInteractAcrossProfilesAppOpForProfileOrThrow(profileId, packageName)) {
            return;
        }
        boolean hadPermission = hasInteractAcrossProfilesPermission(packageName, uid, -1);
        if (isPermissionGranted("android.permission.CONFIGURE_INTERACT_ACROSS_PROFILES", this.mInjector.getCallingUid())) {
            this.mInjector.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.pm.CrossProfileAppsServiceImpl$$ExternalSyntheticLambda8
                public final void runOrThrow() throws java.lang.Exception {
                    this.f$0.lambda$setInteractAcrossProfilesAppOpForProfileOrThrow$8(uid, newMode);
                }
            });
        } else {
            this.mInjector.getAppOpsManager().setUidMode(93, uid, newMode);
        }
        maybeKillUid(packageName, uid, hadPermission);
        sendCanInteractAcrossProfilesChangedBroadcast(packageName, android.os.UserHandle.of(profileId));
        maybeLogSetInteractAcrossProfilesAppOp(packageName, newMode, logMetrics);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setInteractAcrossProfilesAppOpForProfileOrThrow$8(int uid, int newMode) throws java.lang.Exception {
        this.mInjector.getAppOpsManager().setUidMode(93, uid, newMode);
    }

    private void maybeKillUid(java.lang.String packageName, int uid, boolean hadPermission) {
        if (!hadPermission || hasInteractAcrossProfilesPermission(packageName, uid, -1)) {
            return;
        }
        this.mInjector.killUid(uid);
    }

    private void maybeLogSetInteractAcrossProfilesAppOp(java.lang.String packageName, int newMode, boolean logMetrics) {
        if (!logMetrics) {
            return;
        }
        android.app.admin.DevicePolicyEventLogger.createEvent(139).setStrings(new java.lang.String[]{packageName}).setInt(newMode).setBoolean(appDeclaresCrossProfileAttribute(packageName)).write();
    }

    private boolean currentModeEquals(final int otherMode, final java.lang.String packageName, final int uid) {
        final java.lang.String op = android.app.AppOpsManager.permissionToOp("android.permission.INTERACT_ACROSS_PROFILES");
        return ((java.lang.Boolean) this.mInjector.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.pm.CrossProfileAppsServiceImpl$$ExternalSyntheticLambda5
            public final java.lang.Object getOrThrow() {
                return this.f$0.lambda$currentModeEquals$9(otherMode, op, uid, packageName);
            }
        })).booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.lang.Boolean lambda$currentModeEquals$9(int otherMode, java.lang.String op, int uid, java.lang.String packageName) throws java.lang.Exception {
        return java.lang.Boolean.valueOf(otherMode == this.mInjector.getAppOpsManager().unsafeCheckOpNoThrow(op, uid, packageName));
    }

    private void sendCanInteractAcrossProfilesChangedBroadcast(java.lang.String packageName, android.os.UserHandle userHandle) {
        android.content.Intent intent = new android.content.Intent("android.content.pm.action.CAN_INTERACT_ACROSS_PROFILES_CHANGED").setPackage(packageName);
        if (appDeclaresCrossProfileAttribute(packageName)) {
            intent.addFlags(android.hardware.audio.common.V2_0.AudioFormat.EVRCB);
        } else {
            intent.addFlags(1073741824);
        }
        for (android.content.pm.ResolveInfo receiver : findBroadcastReceiversForUser(intent, userHandle)) {
            intent.setComponent(receiver.getComponentInfo().getComponentName());
            this.mInjector.sendBroadcastAsUser(intent, userHandle);
        }
    }

    private java.util.List<android.content.pm.ResolveInfo> findBroadcastReceiversForUser(android.content.Intent intent, android.os.UserHandle userHandle) {
        return this.mInjector.getPackageManager().queryBroadcastReceiversAsUser(intent, 0, userHandle);
    }

    private boolean appDeclaresCrossProfileAttribute(java.lang.String packageName) {
        return this.mInjector.getPackageManagerInternal().getPackage(packageName).isCrossProfile();
    }

    public boolean canConfigureInteractAcrossProfiles(int userId, java.lang.String packageName) {
        if (this.mInjector.getCallingUserId() != userId) {
            this.mContext.checkCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS");
        }
        return canConfigureInteractAcrossProfiles(packageName, userId);
    }

    private boolean canConfigureInteractAcrossProfiles(java.lang.String packageName, int userId) {
        if (canUserAttemptToConfigureInteractAcrossProfiles(packageName, userId) && hasOtherProfileWithPackageInstalled(packageName, userId) && hasRequestedAppOpPermission(android.app.AppOpsManager.opToPermission(93), packageName, userId)) {
            return isCrossProfilePackageAllowlisted(packageName);
        }
        return false;
    }

    public boolean canUserAttemptToConfigureInteractAcrossProfiles(int userId, java.lang.String packageName) {
        if (this.mInjector.getCallingUserId() != userId) {
            this.mContext.checkCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS");
        }
        return canUserAttemptToConfigureInteractAcrossProfiles(packageName, userId);
    }

    private boolean canUserAttemptToConfigureInteractAcrossProfiles(java.lang.String packageName, int userId) {
        int[] profileIds = this.mInjector.getUserManager().getProfileIdsExcludingHidden(userId, false);
        if (profileIds.length >= 2 && !isProfileOwner(packageName, profileIds) && hasRequestedAppOpPermission(android.app.AppOpsManager.opToPermission(93), packageName, userId)) {
            return !isPlatformSignedAppWithNonUserConfigurablePermission(packageName, profileIds);
        }
        return false;
    }

    private boolean isPlatformSignedAppWithNonUserConfigurablePermission(java.lang.String packageName, int[] profileIds) {
        return !isCrossProfilePackageAllowlistedByDefault(packageName) && isPlatformSignedAppWithAutomaticProfilesPermission(packageName, profileIds);
    }

    private boolean isPlatformSignedAppWithAutomaticProfilesPermission(java.lang.String packageName, int[] profileIds) {
        for (int userId : profileIds) {
            int uid = this.mInjector.getPackageManagerInternal().getPackageUid(packageName, 0L, userId);
            if (uid != -1 && isPermissionGranted("android.permission.INTERACT_ACROSS_PROFILES", uid)) {
                return true;
            }
        }
        return false;
    }

    private boolean hasOtherProfileWithPackageInstalled(final java.lang.String packageName, final int userId) {
        return ((java.lang.Boolean) this.mInjector.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.pm.CrossProfileAppsServiceImpl$$ExternalSyntheticLambda15
            public final java.lang.Object getOrThrow() {
                return this.f$0.lambda$hasOtherProfileWithPackageInstalled$10(userId, packageName);
            }
        })).booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.lang.Boolean lambda$hasOtherProfileWithPackageInstalled$10(int userId, java.lang.String packageName) throws java.lang.Exception {
        int[] profileIds = this.mInjector.getUserManager().getProfileIdsExcludingHidden(userId, false);
        for (int profileId : profileIds) {
            if (profileId != userId && isPackageInstalled(packageName, profileId)) {
                return true;
            }
        }
        return false;
    }

    public void resetInteractAcrossProfilesAppOps(int userId, java.util.List<java.lang.String> packageNames) {
        for (java.lang.String packageName : packageNames) {
            resetInteractAcrossProfilesAppOp(userId, packageName);
        }
    }

    private void resetInteractAcrossProfilesAppOp(int userId, java.lang.String packageName) {
        if (canConfigureInteractAcrossProfiles(packageName, userId)) {
            android.util.Slog.w(TAG, "Not resetting app-op for package " + packageName + " since it is still configurable by users.");
        } else {
            java.lang.String op = android.app.AppOpsManager.permissionToOp("android.permission.INTERACT_ACROSS_PROFILES");
            lambda$clearInteractAcrossProfilesAppOps$11(userId, packageName, android.app.AppOpsManager.opToDefaultMode(op));
        }
    }

    public void clearInteractAcrossProfilesAppOps(final int userId) {
        final int defaultMode = android.app.AppOpsManager.opToDefaultMode(android.app.AppOpsManager.permissionToOp("android.permission.INTERACT_ACROSS_PROFILES"));
        findAllPackageNames().forEach(new java.util.function.Consumer() { // from class: com.android.server.pm.CrossProfileAppsServiceImpl$$ExternalSyntheticLambda7
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$clearInteractAcrossProfilesAppOps$11(userId, defaultMode, (java.lang.String) obj);
            }
        });
    }

    private java.util.List<java.lang.String> findAllPackageNames() {
        return (java.util.List) this.mInjector.getPackageManagerInternal().getInstalledApplications(0L, this.mInjector.getCallingUserId(), this.mInjector.getCallingUid()).stream().map(new java.util.function.Function() { // from class: com.android.server.pm.CrossProfileAppsServiceImpl$$ExternalSyntheticLambda2
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return ((android.content.pm.ApplicationInfo) obj).packageName;
            }
        }).collect(java.util.stream.Collectors.toList());
    }

    android.content.pm.CrossProfileAppsInternal getLocalService() {
        return this.mLocalService;
    }

    private boolean isSameProfileGroup(final int callerUserId, final int userId) {
        return ((java.lang.Boolean) this.mInjector.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.pm.CrossProfileAppsServiceImpl$$ExternalSyntheticLambda10
            public final java.lang.Object getOrThrow() {
                return this.f$0.lambda$isSameProfileGroup$13(callerUserId, userId);
            }
        })).booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.lang.Boolean lambda$isSameProfileGroup$13(int callerUserId, int userId) throws java.lang.Exception {
        return java.lang.Boolean.valueOf(this.mInjector.getUserManager().isSameProfileGroup(callerUserId, userId));
    }

    private void verifyCallingPackage(java.lang.String callingPackage) {
        this.mInjector.getAppOpsManager().checkPackage(this.mInjector.getCallingUid(), callingPackage);
    }

    private boolean isPermissionGranted(java.lang.String permission, int uid) {
        return this.mInjector.checkComponentPermission(permission, uid, -1, true) == 0;
    }

    private boolean isCallingUserAManagedProfile() {
        return isManagedProfile(this.mInjector.getCallingUserId());
    }

    private boolean isManagedProfile(final int userId) {
        return ((java.lang.Boolean) this.mInjector.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.pm.CrossProfileAppsServiceImpl$$ExternalSyntheticLambda1
            public final java.lang.Object getOrThrow() {
                return this.f$0.lambda$isManagedProfile$14(userId);
            }
        })).booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.lang.Boolean lambda$isManagedProfile$14(int userId) throws java.lang.Exception {
        return java.lang.Boolean.valueOf(((android.os.UserManager) this.mContext.getSystemService(android.os.UserManager.class)).isManagedProfile(userId));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean hasInteractAcrossProfilesPermission(java.lang.String packageName, int uid, int pid) {
        return isPermissionGranted("android.permission.INTERACT_ACROSS_USERS_FULL", uid) || isPermissionGranted("android.permission.INTERACT_ACROSS_USERS", uid) || android.content.PermissionChecker.checkPermissionForPreflight(this.mContext, "android.permission.INTERACT_ACROSS_PROFILES", pid, uid, packageName) == 0;
    }

    private boolean isProfileOwner(java.lang.String packageName, int[] userIds) {
        for (int userId : userIds) {
            if (isProfileOwner(packageName, userId)) {
                return true;
            }
        }
        return false;
    }

    private boolean isProfileOwner(java.lang.String packageName, final int userId) {
        android.content.ComponentName profileOwner = (android.content.ComponentName) this.mInjector.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.pm.CrossProfileAppsServiceImpl$$ExternalSyntheticLambda3
            public final java.lang.Object getOrThrow() {
                return this.f$0.lambda$isProfileOwner$15(userId);
            }
        });
        if (profileOwner == null) {
            return false;
        }
        return profileOwner.getPackageName().equals(packageName);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ android.content.ComponentName lambda$isProfileOwner$15(int userId) throws java.lang.Exception {
        return this.mInjector.getDevicePolicyManagerInternal().getProfileOwnerAsUser(userId);
    }

    private static class InjectorImpl implements com.android.server.pm.CrossProfileAppsServiceImpl.Injector {
        private final android.content.Context mContext;

        public InjectorImpl(android.content.Context context) {
            this.mContext = context;
        }

        @Override // com.android.server.pm.CrossProfileAppsServiceImpl.Injector
        public int getCallingUid() {
            return android.os.Binder.getCallingUid();
        }

        @Override // com.android.server.pm.CrossProfileAppsServiceImpl.Injector
        public int getCallingPid() {
            return android.os.Binder.getCallingPid();
        }

        @Override // com.android.server.pm.CrossProfileAppsServiceImpl.Injector
        public int getCallingUserId() {
            return android.os.UserHandle.getCallingUserId();
        }

        @Override // com.android.server.pm.CrossProfileAppsServiceImpl.Injector
        public android.os.UserHandle getCallingUserHandle() {
            return android.os.Binder.getCallingUserHandle();
        }

        @Override // com.android.server.pm.CrossProfileAppsServiceImpl.Injector
        public long clearCallingIdentity() {
            return android.os.Binder.clearCallingIdentity();
        }

        @Override // com.android.server.pm.CrossProfileAppsServiceImpl.Injector
        public void restoreCallingIdentity(long token) {
            android.os.Binder.restoreCallingIdentity(token);
        }

        @Override // com.android.server.pm.CrossProfileAppsServiceImpl.Injector
        public void withCleanCallingIdentity(com.android.internal.util.FunctionalUtils.ThrowingRunnable action) {
            android.os.Binder.withCleanCallingIdentity(action);
        }

        @Override // com.android.server.pm.CrossProfileAppsServiceImpl.Injector
        public final <T> T withCleanCallingIdentity(com.android.internal.util.FunctionalUtils.ThrowingSupplier<T> throwingSupplier) {
            return (T) android.os.Binder.withCleanCallingIdentity(throwingSupplier);
        }

        @Override // com.android.server.pm.CrossProfileAppsServiceImpl.Injector
        public android.os.UserManager getUserManager() {
            return (android.os.UserManager) this.mContext.getSystemService(android.os.UserManager.class);
        }

        @Override // com.android.server.pm.CrossProfileAppsServiceImpl.Injector
        public android.content.pm.PackageManagerInternal getPackageManagerInternal() {
            return (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
        }

        @Override // com.android.server.pm.CrossProfileAppsServiceImpl.Injector
        public android.content.pm.PackageManager getPackageManager() {
            return this.mContext.getPackageManager();
        }

        @Override // com.android.server.pm.CrossProfileAppsServiceImpl.Injector
        public android.app.AppOpsManager getAppOpsManager() {
            return (android.app.AppOpsManager) this.mContext.getSystemService(android.app.AppOpsManager.class);
        }

        @Override // com.android.server.pm.CrossProfileAppsServiceImpl.Injector
        public android.app.ActivityManagerInternal getActivityManagerInternal() {
            return (android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class);
        }

        @Override // com.android.server.pm.CrossProfileAppsServiceImpl.Injector
        public com.android.server.wm.ActivityTaskManagerInternal getActivityTaskManagerInternal() {
            return (com.android.server.wm.ActivityTaskManagerInternal) com.android.server.LocalServices.getService(com.android.server.wm.ActivityTaskManagerInternal.class);
        }

        @Override // com.android.server.pm.CrossProfileAppsServiceImpl.Injector
        public android.content.pm.IPackageManager getIPackageManager() {
            return android.app.AppGlobals.getPackageManager();
        }

        @Override // com.android.server.pm.CrossProfileAppsServiceImpl.Injector
        public android.app.admin.DevicePolicyManagerInternal getDevicePolicyManagerInternal() {
            return (android.app.admin.DevicePolicyManagerInternal) com.android.server.LocalServices.getService(android.app.admin.DevicePolicyManagerInternal.class);
        }

        @Override // com.android.server.pm.CrossProfileAppsServiceImpl.Injector
        public void sendBroadcastAsUser(android.content.Intent intent, android.os.UserHandle user) {
            this.mContext.sendBroadcastAsUser(intent, user);
        }

        @Override // com.android.server.pm.CrossProfileAppsServiceImpl.Injector
        public int checkComponentPermission(java.lang.String permission, int uid, int owningUid, boolean exported) {
            return android.app.ActivityManager.checkComponentPermission(permission, uid, owningUid, exported);
        }

        @Override // com.android.server.pm.CrossProfileAppsServiceImpl.Injector
        public void killUid(int uid) {
            com.android.server.pm.permission.PermissionManagerService.killUid(android.os.UserHandle.getAppId(uid), android.os.UserHandle.getUserId(uid), "permissions revoked");
        }
    }

    class LocalService extends android.content.pm.CrossProfileAppsInternal {
        LocalService() {
        }

        public boolean verifyPackageHasInteractAcrossProfilePermission(java.lang.String packageName, int userId) throws android.content.pm.PackageManager.NameNotFoundException {
            int uid = ((android.content.pm.ApplicationInfo) java.util.Objects.requireNonNull(com.android.server.pm.CrossProfileAppsServiceImpl.this.mInjector.getPackageManager().getApplicationInfoAsUser((java.lang.String) java.util.Objects.requireNonNull(packageName), 0, userId))).uid;
            return verifyUidHasInteractAcrossProfilePermission(packageName, uid);
        }

        public boolean verifyUidHasInteractAcrossProfilePermission(java.lang.String packageName, int uid) {
            java.util.Objects.requireNonNull(packageName);
            return com.android.server.pm.CrossProfileAppsServiceImpl.this.hasInteractAcrossProfilesPermission(packageName, uid, -1);
        }

        public java.util.List<android.os.UserHandle> getTargetUserProfiles(java.lang.String packageName, int userId) {
            return com.android.server.pm.CrossProfileAppsServiceImpl.this.getTargetUserProfilesUnchecked(packageName, userId);
        }

        public void setInteractAcrossProfilesAppOp(java.lang.String packageName, int newMode, int userId) {
            com.android.server.pm.CrossProfileAppsServiceImpl.this.setInteractAcrossProfilesAppOpUnchecked(packageName, newMode, userId);
        }
    }
}
