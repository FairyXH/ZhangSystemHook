package com.android.server.devicepolicy;

/* JADX INFO: loaded from: classes.dex */
final class PolicyEnforcerCallbacks {
    private static final java.lang.String LOG_TAG = "PolicyEnforcerCallbacks";

    PolicyEnforcerCallbacks() {
    }

    static <T> boolean noOp(T value, android.content.Context context, java.lang.Integer userId, android.app.admin.PolicyKey policyKey) {
        return true;
    }

    static boolean setAutoTimezoneEnabled(final java.lang.Boolean enabled, final android.content.Context context) {
        if (!com.android.server.devicepolicy.DevicePolicyManagerService.isUnicornFlagEnabled()) {
            com.android.server.utils.Slogf.w(LOG_TAG, "Trying to enforce setAutoTimezoneEnabled while flag is off.");
            return true;
        }
        return ((java.lang.Boolean) android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.devicepolicy.PolicyEnforcerCallbacks$$ExternalSyntheticLambda7
            public final java.lang.Object getOrThrow() {
                return com.android.server.devicepolicy.PolicyEnforcerCallbacks.lambda$setAutoTimezoneEnabled$0(context, enabled);
            }
        })).booleanValue();
    }

    static /* synthetic */ java.lang.Boolean lambda$setAutoTimezoneEnabled$0(android.content.Context context, java.lang.Boolean enabled) throws java.lang.Exception {
        java.util.Objects.requireNonNull(context);
        int value = (enabled == null || !enabled.booleanValue()) ? 0 : 1;
        return java.lang.Boolean.valueOf(android.provider.Settings.Global.putInt(context.getContentResolver(), "auto_time_zone", value));
    }

    static boolean setPermissionGrantState(final java.lang.Integer grantState, final android.content.Context context, final int userId, final android.app.admin.PolicyKey policyKey) {
        if (!com.android.server.devicepolicy.DevicePolicyManagerService.isUnicornFlagEnabled()) {
            com.android.server.utils.Slogf.w(LOG_TAG, "Trying to enforce setPermissionGrantState while flag is off.");
            return true;
        }
        return java.lang.Boolean.TRUE.equals(android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.devicepolicy.PolicyEnforcerCallbacks$$ExternalSyntheticLambda9
            public final java.lang.Object getOrThrow() {
                return com.android.server.devicepolicy.PolicyEnforcerCallbacks.lambda$setPermissionGrantState$1(policyKey, context, grantState, userId);
            }
        }));
    }

    static /* synthetic */ java.lang.Boolean lambda$setPermissionGrantState$1(android.app.admin.PolicyKey policyKey, android.content.Context context, java.lang.Integer grantState, int userId) throws java.lang.Exception {
        int value;
        if (!(policyKey instanceof android.app.admin.PackagePermissionPolicyKey)) {
            throw new java.lang.IllegalArgumentException("policyKey is not of type PermissionGrantStatePolicyKey, passed in policyKey is: " + policyKey);
        }
        android.app.admin.PackagePermissionPolicyKey parsedKey = (android.app.admin.PackagePermissionPolicyKey) policyKey;
        java.util.Objects.requireNonNull(parsedKey.getPermissionName());
        java.util.Objects.requireNonNull(parsedKey.getPackageName());
        java.util.Objects.requireNonNull(context);
        if (grantState == null) {
            value = 0;
        } else {
            value = grantState.intValue();
        }
        final com.android.server.devicepolicy.PolicyEnforcerCallbacks.BlockingCallback callback = new com.android.server.devicepolicy.PolicyEnforcerCallbacks.BlockingCallback();
        android.permission.AdminPermissionControlParams permissionParams = new android.permission.AdminPermissionControlParams(parsedKey.getPackageName(), parsedKey.getPermissionName(), value, true);
        android.permission.PermissionControllerManager permissionControllerManager = getPermissionControllerManager(context, android.os.UserHandle.of(userId));
        java.lang.String packageName = context.getPackageName();
        java.util.concurrent.Executor mainExecutor = context.getMainExecutor();
        java.util.Objects.requireNonNull(callback);
        permissionControllerManager.setRuntimePermissionGrantStateByDeviceAdmin(packageName, permissionParams, mainExecutor, new java.util.function.Consumer() { // from class: com.android.server.devicepolicy.PolicyEnforcerCallbacks$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                callback.trigger((java.lang.Boolean) obj);
            }
        });
        try {
            return callback.await(20000L, java.util.concurrent.TimeUnit.MILLISECONDS);
        } catch (java.lang.Exception e) {
            return false;
        }
    }

    private static android.permission.PermissionControllerManager getPermissionControllerManager(android.content.Context context, android.os.UserHandle user) {
        if (user.equals(context.getUser())) {
            return (android.permission.PermissionControllerManager) context.getSystemService(android.permission.PermissionControllerManager.class);
        }
        try {
            return (android.permission.PermissionControllerManager) context.createPackageContextAsUser(context.getPackageName(), 0, user).getSystemService(android.permission.PermissionControllerManager.class);
        } catch (android.content.pm.PackageManager.NameNotFoundException notPossible) {
            throw new java.lang.IllegalStateException(notPossible);
        }
    }

    static boolean enforceSecurityLogging(java.lang.Boolean value, android.content.Context context, int userId, android.app.admin.PolicyKey policyKey) {
        android.app.admin.DevicePolicyManagerInternal dpmi = (android.app.admin.DevicePolicyManagerInternal) com.android.server.LocalServices.getService(android.app.admin.DevicePolicyManagerInternal.class);
        dpmi.enforceSecurityLoggingPolicy(java.lang.Boolean.TRUE.equals(value));
        return true;
    }

    static boolean enforceAuditLogging(java.lang.Boolean value, android.content.Context context, int userId, android.app.admin.PolicyKey policyKey) {
        android.app.admin.DevicePolicyManagerInternal dpmi = (android.app.admin.DevicePolicyManagerInternal) com.android.server.LocalServices.getService(android.app.admin.DevicePolicyManagerInternal.class);
        dpmi.enforceAuditLoggingPolicy(java.lang.Boolean.TRUE.equals(value));
        return true;
    }

    static boolean setLockTask(android.app.admin.LockTaskPolicy policy, android.content.Context context, int userId) {
        java.util.List<java.lang.String> packages = java.util.Collections.emptyList();
        int flags = 16;
        if (policy != null) {
            packages = java.util.List.copyOf(policy.getPackages());
            flags = policy.getFlags();
        }
        com.android.server.devicepolicy.DevicePolicyManagerService.updateLockTaskPackagesLocked(context, packages, userId);
        com.android.server.devicepolicy.DevicePolicyManagerService.updateLockTaskFeaturesLocked(flags, userId);
        return true;
    }

    static boolean setApplicationRestrictions(android.os.Bundle bundle, final android.content.Context context, final java.lang.Integer userId, final android.app.admin.PolicyKey policyKey) {
        android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.devicepolicy.PolicyEnforcerCallbacks$$ExternalSyntheticLambda10
            public final void runOrThrow() throws java.lang.Exception {
                com.android.server.devicepolicy.PolicyEnforcerCallbacks.lambda$setApplicationRestrictions$2(policyKey, context, userId);
            }
        });
        return true;
    }

    static /* synthetic */ void lambda$setApplicationRestrictions$2(android.app.admin.PolicyKey policyKey, android.content.Context context, java.lang.Integer userId) throws java.lang.Exception {
        android.app.admin.PackagePolicyKey key = (android.app.admin.PackagePolicyKey) policyKey;
        java.lang.String packageName = key.getPackageName();
        java.util.Objects.requireNonNull(packageName);
        android.content.Intent changeIntent = new android.content.Intent("android.intent.action.APPLICATION_RESTRICTIONS_CHANGED");
        changeIntent.setPackage(packageName);
        changeIntent.addFlags(1073741824);
        context.sendBroadcastAsUser(changeIntent, android.os.UserHandle.of(userId.intValue()));
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class BlockingCallback {
        private final java.util.concurrent.CountDownLatch mLatch;
        private final java.util.concurrent.atomic.AtomicReference<java.lang.Boolean> mValue;

        private BlockingCallback() {
            this.mLatch = new java.util.concurrent.CountDownLatch(1);
            this.mValue = new java.util.concurrent.atomic.AtomicReference<>();
        }

        public void trigger(java.lang.Boolean value) {
            this.mValue.set(value);
            this.mLatch.countDown();
        }

        public java.lang.Boolean await(long timeout, java.util.concurrent.TimeUnit unit) throws java.lang.InterruptedException {
            if (!this.mLatch.await(timeout, unit)) {
                com.android.server.utils.Slogf.e(com.android.server.devicepolicy.PolicyEnforcerCallbacks.LOG_TAG, "Callback was not received");
            }
            return this.mValue.get();
        }
    }

    static boolean setUserControlDisabledPackages(final java.util.Set<java.lang.String> packages, final android.content.Context context, final int userId, android.app.admin.PolicyKey policyKey) {
        android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.devicepolicy.PolicyEnforcerCallbacks$$ExternalSyntheticLambda13
            public final void runOrThrow() throws java.lang.Exception {
                com.android.server.devicepolicy.PolicyEnforcerCallbacks.lambda$setUserControlDisabledPackages$3(context, userId, packages);
            }
        });
        return true;
    }

    static /* synthetic */ void lambda$setUserControlDisabledPackages$3(android.content.Context context, int userId, java.util.Set packages) throws java.lang.Exception {
        android.content.pm.PackageManagerInternal pmi = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
        android.app.AppOpsManager appOpsManager = (android.app.AppOpsManager) context.getSystemService(android.app.AppOpsManager.class);
        pmi.setOwnerProtectedPackages(userId, packages == null ? null : packages.stream().toList());
        ((android.app.usage.UsageStatsManagerInternal) com.android.server.LocalServices.getService(android.app.usage.UsageStatsManagerInternal.class)).setAdminProtectedPackages(packages != null ? new android.util.ArraySet(packages) : null, userId);
        if (packages == null || packages.isEmpty()) {
            return;
        }
        java.util.Iterator<java.lang.Integer> it = resolveUsers(userId).iterator();
        while (it.hasNext()) {
            int user = it.next().intValue();
            if (android.app.admin.flags.Flags.disallowUserControlBgUsageFix()) {
                setBgUsageAppOp(packages, pmi, user, appOpsManager);
            }
            if (android.app.admin.flags.Flags.disallowUserControlStoppedStateFix()) {
                java.util.Iterator it2 = packages.iterator();
                while (it2.hasNext()) {
                    java.lang.String packageName = (java.lang.String) it2.next();
                    pmi.setPackageStoppedState(packageName, false, user);
                }
            }
        }
    }

    private static java.util.List<java.lang.Integer> resolveUsers(int userId) {
        if (userId == -1) {
            com.android.server.pm.UserManagerInternal userManager = (com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class);
            return userManager.getUsers(true).stream().map(new java.util.function.Function() { // from class: com.android.server.devicepolicy.PolicyEnforcerCallbacks$$ExternalSyntheticLambda8
                @Override // java.util.function.Function
                public final java.lang.Object apply(java.lang.Object obj) {
                    return java.lang.Integer.valueOf(((android.content.pm.UserInfo) obj).id);
                }
            }).toList();
        }
        return java.util.List.of(java.lang.Integer.valueOf(userId));
    }

    private static void setBgUsageAppOp(java.util.Set<java.lang.String> packages, android.content.pm.PackageManagerInternal pmi, int userId, android.app.AppOpsManager appOpsManager) {
        for (java.lang.String pkg : packages) {
            android.content.pm.ApplicationInfo appInfo = pmi.getApplicationInfo(pkg, com.android.server.permission.access.permission.PermissionFlags.MASK_RESTRICTED, android.os.Process.myUid(), userId);
            if (appInfo != null) {
                com.android.server.devicepolicy.DevicePolicyManagerService.setBgUsageAppOp(appOpsManager, appInfo);
            }
        }
    }

    static boolean addPersistentPreferredActivity(final android.content.ComponentName preferredActivity, android.content.Context context, final int userId, final android.app.admin.PolicyKey policyKey) {
        android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.devicepolicy.PolicyEnforcerCallbacks$$ExternalSyntheticLambda14
            public final void runOrThrow() throws java.lang.Exception {
                com.android.server.devicepolicy.PolicyEnforcerCallbacks.lambda$addPersistentPreferredActivity$5(policyKey, preferredActivity, userId);
            }
        });
        return true;
    }

    static /* synthetic */ void lambda$addPersistentPreferredActivity$5(android.app.admin.PolicyKey policyKey, android.content.ComponentName preferredActivity, int userId) throws java.lang.Exception {
        try {
            if (!(policyKey instanceof android.app.admin.IntentFilterPolicyKey)) {
                throw new java.lang.IllegalArgumentException("policyKey is not of type IntentFilterPolicyKey, passed in policyKey is: " + policyKey);
            }
            android.app.admin.IntentFilterPolicyKey parsedKey = (android.app.admin.IntentFilterPolicyKey) policyKey;
            android.content.IntentFilter filter = (android.content.IntentFilter) java.util.Objects.requireNonNull(parsedKey.getIntentFilter());
            android.content.pm.IPackageManager packageManager = android.app.AppGlobals.getPackageManager();
            if (preferredActivity != null) {
                packageManager.addPersistentPreferredActivity(filter, preferredActivity, userId);
            } else {
                packageManager.clearPersistentPreferredActivity(filter, userId);
            }
            packageManager.flushPackageRestrictionsAsUser(userId);
        } catch (android.os.RemoteException re) {
            android.util.Slog.wtf(LOG_TAG, "Error adding/removing persistent preferred activity", re);
        }
    }

    static boolean setUninstallBlocked(final java.lang.Boolean uninstallBlocked, android.content.Context context, final int userId, final android.app.admin.PolicyKey policyKey) {
        return java.lang.Boolean.TRUE.equals(android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.devicepolicy.PolicyEnforcerCallbacks$$ExternalSyntheticLambda11
            public final java.lang.Object getOrThrow() {
                return com.android.server.devicepolicy.PolicyEnforcerCallbacks.lambda$setUninstallBlocked$6(policyKey, uninstallBlocked, userId);
            }
        }));
    }

    static /* synthetic */ java.lang.Boolean lambda$setUninstallBlocked$6(android.app.admin.PolicyKey policyKey, java.lang.Boolean uninstallBlocked, int userId) throws java.lang.Exception {
        if (!(policyKey instanceof android.app.admin.PackagePolicyKey)) {
            throw new java.lang.IllegalArgumentException("policyKey is not of type PackagePolicyKey, passed in policyKey is: " + policyKey);
        }
        android.app.admin.PackagePolicyKey parsedKey = (android.app.admin.PackagePolicyKey) policyKey;
        java.lang.String packageName = (java.lang.String) java.util.Objects.requireNonNull(parsedKey.getPackageName());
        com.android.server.devicepolicy.DevicePolicyManagerService.setUninstallBlockedUnchecked(packageName, uninstallBlocked != null && uninstallBlocked.booleanValue(), userId);
        return true;
    }

    static boolean setUserRestriction(final java.lang.Boolean enabled, android.content.Context context, final int userId, final android.app.admin.PolicyKey policyKey) {
        return java.lang.Boolean.TRUE.equals(android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.devicepolicy.PolicyEnforcerCallbacks$$ExternalSyntheticLambda4
            public final java.lang.Object getOrThrow() {
                return com.android.server.devicepolicy.PolicyEnforcerCallbacks.lambda$setUserRestriction$7(policyKey, userId, enabled);
            }
        }));
    }

    static /* synthetic */ java.lang.Boolean lambda$setUserRestriction$7(android.app.admin.PolicyKey policyKey, int userId, java.lang.Boolean enabled) throws java.lang.Exception {
        if (!(policyKey instanceof android.app.admin.UserRestrictionPolicyKey)) {
            throw new java.lang.IllegalArgumentException("policyKey is not of type UserRestrictionPolicyKey, passed in policyKey is: " + policyKey);
        }
        android.app.admin.UserRestrictionPolicyKey parsedKey = (android.app.admin.UserRestrictionPolicyKey) policyKey;
        com.android.server.pm.UserManagerInternal userManager = (com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class);
        userManager.setUserRestriction(userId, parsedKey.getRestriction(), enabled != null && enabled.booleanValue());
        return true;
    }

    static boolean setApplicationHidden(final java.lang.Boolean hide, android.content.Context context, final int userId, final android.app.admin.PolicyKey policyKey) {
        return java.lang.Boolean.TRUE.equals(android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.devicepolicy.PolicyEnforcerCallbacks$$ExternalSyntheticLambda5
            public final java.lang.Object getOrThrow() {
                return com.android.server.devicepolicy.PolicyEnforcerCallbacks.lambda$setApplicationHidden$8(policyKey, hide, userId);
            }
        }));
    }

    static /* synthetic */ java.lang.Boolean lambda$setApplicationHidden$8(android.app.admin.PolicyKey policyKey, java.lang.Boolean hide, int userId) throws java.lang.Exception {
        if (!(policyKey instanceof android.app.admin.PackagePolicyKey)) {
            throw new java.lang.IllegalArgumentException("policyKey is not of type PackagePolicyKey, passed in policyKey is: " + policyKey);
        }
        android.app.admin.PackagePolicyKey parsedKey = (android.app.admin.PackagePolicyKey) policyKey;
        java.lang.String packageName = (java.lang.String) java.util.Objects.requireNonNull(parsedKey.getPackageName());
        android.content.pm.IPackageManager packageManager = android.app.AppGlobals.getPackageManager();
        return java.lang.Boolean.valueOf(packageManager.setApplicationHiddenSettingAsUser(packageName, hide != null && hide.booleanValue(), userId));
    }

    static boolean setScreenCaptureDisabled(final java.lang.Boolean disabled, android.content.Context context, final int userId, android.app.admin.PolicyKey policyKey) {
        android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.devicepolicy.PolicyEnforcerCallbacks$$ExternalSyntheticLambda2
            public final void runOrThrow() throws java.lang.Exception {
                com.android.server.devicepolicy.PolicyEnforcerCallbacks.lambda$setScreenCaptureDisabled$9(userId, disabled);
            }
        });
        return true;
    }

    static /* synthetic */ void lambda$setScreenCaptureDisabled$9(int userId, java.lang.Boolean disabled) throws java.lang.Exception {
        android.app.admin.DevicePolicyCache cache = android.app.admin.DevicePolicyCache.getInstance();
        if (cache instanceof com.android.server.devicepolicy.DevicePolicyCacheImpl) {
            com.android.server.devicepolicy.DevicePolicyCacheImpl parsedCache = (com.android.server.devicepolicy.DevicePolicyCacheImpl) cache;
            parsedCache.setScreenCaptureDisallowedUser(userId, disabled != null && disabled.booleanValue());
            updateScreenCaptureDisabled();
        }
    }

    static boolean setContentProtectionPolicy(final java.lang.Integer value, android.content.Context context, final java.lang.Integer userId, android.app.admin.PolicyKey policyKey) {
        android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.devicepolicy.PolicyEnforcerCallbacks$$ExternalSyntheticLambda12
            public final void runOrThrow() throws java.lang.Exception {
                com.android.server.devicepolicy.PolicyEnforcerCallbacks.lambda$setContentProtectionPolicy$10(userId, value);
            }
        });
        return true;
    }

    static /* synthetic */ void lambda$setContentProtectionPolicy$10(java.lang.Integer userId, java.lang.Integer value) throws java.lang.Exception {
        android.app.admin.DevicePolicyCache cache = android.app.admin.DevicePolicyCache.getInstance();
        if (cache instanceof com.android.server.devicepolicy.DevicePolicyCacheImpl) {
            com.android.server.devicepolicy.DevicePolicyCacheImpl cacheImpl = (com.android.server.devicepolicy.DevicePolicyCacheImpl) cache;
            cacheImpl.setContentProtectionPolicy(userId.intValue(), value);
        }
    }

    private static void updateScreenCaptureDisabled() {
        com.android.internal.os.BackgroundThread.getHandler().post(new java.lang.Runnable() { // from class: com.android.server.devicepolicy.PolicyEnforcerCallbacks$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                com.android.server.devicepolicy.PolicyEnforcerCallbacks.lambda$updateScreenCaptureDisabled$11();
            }
        });
    }

    static /* synthetic */ void lambda$updateScreenCaptureDisabled$11() {
        try {
            android.view.IWindowManager.Stub.asInterface(android.os.ServiceManager.getService("window")).refreshScreenCaptureDisabled();
        } catch (android.os.RemoteException e) {
            com.android.server.utils.Slogf.w(LOG_TAG, "Unable to notify WindowManager.", e);
        }
    }

    static boolean setPersonalAppsSuspended(final java.lang.Boolean suspended, final android.content.Context context, final int userId, android.app.admin.PolicyKey policyKey) {
        android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.devicepolicy.PolicyEnforcerCallbacks$$ExternalSyntheticLambda0
            public final void runOrThrow() throws java.lang.Exception {
                com.android.server.devicepolicy.PolicyEnforcerCallbacks.lambda$setPersonalAppsSuspended$12(suspended, context, userId);
            }
        });
        return true;
    }

    static /* synthetic */ void lambda$setPersonalAppsSuspended$12(java.lang.Boolean suspended, android.content.Context context, int userId) throws java.lang.Exception {
        if (suspended != null && suspended.booleanValue()) {
            suspendPersonalAppsInPackageManager(context, userId);
        } else {
            ((android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class)).unsuspendAdminSuspendedPackages(userId);
        }
    }

    private static void suspendPersonalAppsInPackageManager(android.content.Context context, int userId) {
        java.lang.String[] appsToSuspend = com.android.server.devicepolicy.PersonalAppsSuspensionHelper.forUser(context, userId).getPersonalAppsForSuspension();
        com.android.server.utils.Slogf.i(LOG_TAG, "Suspending personal apps: %s", java.lang.String.join(",", appsToSuspend));
        java.lang.String[] failedApps = ((android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class)).setPackagesSuspendedByAdmin(userId, appsToSuspend, true);
        if (!com.android.internal.util.ArrayUtils.isEmpty(failedApps)) {
            com.android.server.utils.Slogf.wtf(LOG_TAG, "Failed to suspend apps: " + java.lang.String.join(",", failedApps));
        }
    }

    static boolean setUsbDataSignalingEnabled(final java.lang.Boolean value, final android.content.Context context) {
        return ((java.lang.Boolean) android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.devicepolicy.PolicyEnforcerCallbacks$$ExternalSyntheticLambda3
            public final java.lang.Object getOrThrow() {
                return com.android.server.devicepolicy.PolicyEnforcerCallbacks.lambda$setUsbDataSignalingEnabled$13(context, value);
            }
        })).booleanValue();
    }

    static /* synthetic */ java.lang.Boolean lambda$setUsbDataSignalingEnabled$13(android.content.Context context, java.lang.Boolean value) throws java.lang.Exception {
        java.util.Objects.requireNonNull(context);
        boolean enabled = value == null || value.booleanValue();
        com.android.server.devicepolicy.DevicePolicyManagerService.updateUsbDataSignal(context, enabled);
        return true;
    }
}
