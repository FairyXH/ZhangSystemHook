package com.android.server.pm.permission;

/* JADX INFO: loaded from: classes2.dex */
public class LegacyPermissionManagerService extends android.permission.ILegacyPermissionManager.Stub {
    private static final java.lang.String TAG = "PermissionManager";
    private final android.content.Context mContext;
    private final com.android.server.pm.permission.DefaultPermissionGrantPolicy mDefaultPermissionGrantPolicy;
    private final com.android.server.pm.permission.LegacyPermissionManagerService.Injector mInjector;

    public static com.android.server.pm.permission.LegacyPermissionManagerInternal create(android.content.Context context) {
        com.android.server.pm.permission.LegacyPermissionManagerInternal legacyPermissionManagerInternal = (com.android.server.pm.permission.LegacyPermissionManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.permission.LegacyPermissionManagerInternal.class);
        if (legacyPermissionManagerInternal == null) {
            new com.android.server.pm.permission.LegacyPermissionManagerService(context);
            return (com.android.server.pm.permission.LegacyPermissionManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.permission.LegacyPermissionManagerInternal.class);
        }
        return legacyPermissionManagerInternal;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private LegacyPermissionManagerService(android.content.Context context) {
        this(context, new com.android.server.pm.permission.LegacyPermissionManagerService.Injector(context));
        com.android.server.LocalServices.addService(com.android.server.pm.permission.LegacyPermissionManagerInternal.class, new com.android.server.pm.permission.LegacyPermissionManagerService.Internal());
        android.os.ServiceManager.addService("legacy_permission", this);
    }

    LegacyPermissionManagerService(android.content.Context context, com.android.server.pm.permission.LegacyPermissionManagerService.Injector injector) {
        this.mContext = context;
        this.mInjector = injector;
        this.mDefaultPermissionGrantPolicy = new com.android.server.pm.permission.DefaultPermissionGrantPolicy(context);
        ((com.android.server.pm.permission.ILegacyPermissionManagerServiceExt) system.ext.loader.core.ExtLoader.type(com.android.server.pm.permission.ILegacyPermissionManagerServiceExt.class).create()).onConstruct(this.mDefaultPermissionGrantPolicy);
    }

    public int checkDeviceIdentifierAccess(java.lang.String packageName, java.lang.String message, java.lang.String callingFeatureId, int pid, int uid) {
        verifyCallerCanCheckAccess(packageName, message, pid, uid);
        int appId = android.os.UserHandle.getAppId(uid);
        if (appId == 1000 || appId == 0 || this.mInjector.checkPermission("android.permission.READ_PRIVILEGED_PHONE_STATE", pid, uid) == 0) {
            return 0;
        }
        if (packageName != null) {
            long token = this.mInjector.clearCallingIdentity();
            android.app.AppOpsManager appOpsManager = (android.app.AppOpsManager) this.mInjector.getSystemService("appops");
            try {
                if (appOpsManager.noteOpNoThrow("android:read_device_identifiers", uid, packageName, callingFeatureId, message) == 0) {
                    return 0;
                }
                this.mInjector.restoreCallingIdentity(token);
                android.app.admin.DevicePolicyManager devicePolicyManager = (android.app.admin.DevicePolicyManager) this.mInjector.getSystemService("device_policy");
                return (devicePolicyManager == null || !devicePolicyManager.hasDeviceIdentifierAccess(packageName, pid, uid)) ? -1 : 0;
            } finally {
                this.mInjector.restoreCallingIdentity(token);
            }
        }
        return -1;
    }

    public int checkPhoneNumberAccess(java.lang.String packageName, java.lang.String message, java.lang.String callingFeatureId, int pid, int uid) {
        boolean preR;
        verifyCallerCanCheckAccess(packageName, message, pid, uid);
        if (this.mInjector.checkPermission("android.permission.READ_PRIVILEGED_PHONE_STATE", pid, uid) == 0) {
            return 0;
        }
        if (packageName == null) {
            return -1;
        }
        int result = -1;
        try {
            android.content.pm.ApplicationInfo info = this.mInjector.getApplicationInfo(packageName, uid);
            boolean preR2 = info.targetSdkVersion <= 29;
            preR = preR2;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            preR = false;
        }
        if (preR && (result = checkPermissionAndAppop(packageName, "android.permission.READ_PHONE_STATE", "android:read_phone_state", callingFeatureId, message, pid, uid)) == 0) {
            return result;
        }
        if (checkPermissionAndAppop(packageName, null, "android:write_sms", callingFeatureId, message, pid, uid) == 0 || checkPermissionAndAppop(packageName, "android.permission.READ_PHONE_NUMBERS", "android:read_phone_numbers", callingFeatureId, message, pid, uid) == 0 || checkPermissionAndAppop(packageName, "android.permission.READ_SMS", "android:read_sms", callingFeatureId, message, pid, uid) == 0) {
            return 0;
        }
        return result;
    }

    private void verifyCallerCanCheckAccess(java.lang.String packageName, java.lang.String message, int pid, int uid) {
        boolean reportError = false;
        int callingUid = this.mInjector.getCallingUid();
        int callingPid = this.mInjector.getCallingPid();
        if (android.os.UserHandle.getAppId(callingUid) >= 10000 && (callingUid != uid || callingPid != pid)) {
            reportError = true;
        }
        if (packageName != null && android.os.UserHandle.getAppId(uid) >= 10000) {
            int packageUid = this.mInjector.getPackageUidForUser(packageName, android.os.UserHandle.getUserId(uid));
            if (uid != packageUid) {
                android.util.EventLog.writeEvent(1397638484, "193441322", java.lang.Integer.valueOf(android.os.UserHandle.getAppId(callingUid) >= 10000 ? callingUid : uid), "Package uid mismatch");
                reportError = true;
            }
        }
        if (reportError) {
            java.lang.String response = java.lang.String.format("Calling uid %d, pid %d cannot access for package %s (uid=%d, pid=%d): %s", java.lang.Integer.valueOf(callingUid), java.lang.Integer.valueOf(callingPid), packageName, java.lang.Integer.valueOf(uid), java.lang.Integer.valueOf(pid), message);
            android.util.Log.w(TAG, response);
            throw new java.lang.SecurityException(response);
        }
    }

    private int checkPermissionAndAppop(java.lang.String packageName, java.lang.String permission, java.lang.String appop, java.lang.String callingFeatureId, java.lang.String message, int pid, int uid) {
        if (permission != null && this.mInjector.checkPermission(permission, pid, uid) != 0) {
            return -1;
        }
        android.app.AppOpsManager appOpsManager = (android.app.AppOpsManager) this.mInjector.getSystemService("appops");
        if (appOpsManager.noteOpNoThrow(appop, uid, packageName, callingFeatureId, message) != 0) {
            return 1;
        }
        return 0;
    }

    public void grantDefaultPermissionsToCarrierServiceApp(final java.lang.String packageName, final int userId) {
        com.android.server.pm.PackageManagerServiceUtils.enforceSystemOrRoot("grantDefaultPermissionsForCarrierServiceApp");
        android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.pm.permission.LegacyPermissionManagerService$$ExternalSyntheticLambda5
            public final void runOrThrow() throws java.lang.Exception {
                this.f$0.lambda$grantDefaultPermissionsToCarrierServiceApp$0(packageName, userId);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$grantDefaultPermissionsToCarrierServiceApp$0(java.lang.String packageName, int userId) throws java.lang.Exception {
        this.mDefaultPermissionGrantPolicy.grantDefaultPermissionsToCarrierServiceApp(packageName, userId);
    }

    public void grantDefaultPermissionsToActiveLuiApp(final java.lang.String packageName, final int userId) {
        int callingUid = android.os.Binder.getCallingUid();
        com.android.server.pm.PackageManagerServiceUtils.enforceSystemOrPhoneCaller("grantDefaultPermissionsToActiveLuiApp", callingUid);
        android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.pm.permission.LegacyPermissionManagerService$$ExternalSyntheticLambda4
            public final void runOrThrow() throws java.lang.Exception {
                this.f$0.lambda$grantDefaultPermissionsToActiveLuiApp$1(packageName, userId);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$grantDefaultPermissionsToActiveLuiApp$1(java.lang.String packageName, int userId) throws java.lang.Exception {
        this.mDefaultPermissionGrantPolicy.grantDefaultPermissionsToActiveLuiApp(packageName, userId);
    }

    public void revokeDefaultPermissionsFromLuiApps(final java.lang.String[] packageNames, final int userId) {
        int callingUid = android.os.Binder.getCallingUid();
        com.android.server.pm.PackageManagerServiceUtils.enforceSystemOrPhoneCaller("revokeDefaultPermissionsFromLuiApps", callingUid);
        android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.pm.permission.LegacyPermissionManagerService$$ExternalSyntheticLambda2
            public final void runOrThrow() throws java.lang.Exception {
                this.f$0.lambda$revokeDefaultPermissionsFromLuiApps$2(packageNames, userId);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$revokeDefaultPermissionsFromLuiApps$2(java.lang.String[] packageNames, int userId) throws java.lang.Exception {
        this.mDefaultPermissionGrantPolicy.revokeDefaultPermissionsFromLuiApps(packageNames, userId);
    }

    public void grantDefaultPermissionsToEnabledImsServices(final java.lang.String[] packageNames, final int userId) {
        int callingUid = android.os.Binder.getCallingUid();
        com.android.server.pm.PackageManagerServiceUtils.enforceSystemOrPhoneCaller("grantDefaultPermissionsToEnabledImsServices", callingUid);
        android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.pm.permission.LegacyPermissionManagerService$$ExternalSyntheticLambda0
            public final void runOrThrow() throws java.lang.Exception {
                this.f$0.lambda$grantDefaultPermissionsToEnabledImsServices$3(packageNames, userId);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$grantDefaultPermissionsToEnabledImsServices$3(java.lang.String[] packageNames, int userId) throws java.lang.Exception {
        this.mDefaultPermissionGrantPolicy.grantDefaultPermissionsToEnabledImsServices(packageNames, userId);
    }

    public void grantDefaultPermissionsToEnabledTelephonyDataServices(final java.lang.String[] packageNames, final int userId) {
        int callingUid = android.os.Binder.getCallingUid();
        com.android.server.pm.PackageManagerServiceUtils.enforceSystemOrPhoneCaller("grantDefaultPermissionsToEnabledTelephonyDataServices", callingUid);
        android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.pm.permission.LegacyPermissionManagerService$$ExternalSyntheticLambda3
            public final void runOrThrow() throws java.lang.Exception {
                this.f$0.lambda$grantDefaultPermissionsToEnabledTelephonyDataServices$4(packageNames, userId);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$grantDefaultPermissionsToEnabledTelephonyDataServices$4(java.lang.String[] packageNames, int userId) throws java.lang.Exception {
        this.mDefaultPermissionGrantPolicy.grantDefaultPermissionsToEnabledTelephonyDataServices(packageNames, userId);
    }

    public void revokeDefaultPermissionsFromDisabledTelephonyDataServices(final java.lang.String[] packageNames, final int userId) {
        int callingUid = android.os.Binder.getCallingUid();
        com.android.server.pm.PackageManagerServiceUtils.enforceSystemOrPhoneCaller("revokeDefaultPermissionsFromDisabledTelephonyDataServices", callingUid);
        android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.pm.permission.LegacyPermissionManagerService$$ExternalSyntheticLambda6
            public final void runOrThrow() throws java.lang.Exception {
                this.f$0.lambda$revokeDefaultPermissionsFromDisabledTelephonyDataServices$5(packageNames, userId);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$revokeDefaultPermissionsFromDisabledTelephonyDataServices$5(java.lang.String[] packageNames, int userId) throws java.lang.Exception {
        this.mDefaultPermissionGrantPolicy.revokeDefaultPermissionsFromDisabledTelephonyDataServices(packageNames, userId);
    }

    public void grantDefaultPermissionsToEnabledCarrierApps(final java.lang.String[] packageNames, final int userId) {
        int callingUid = android.os.Binder.getCallingUid();
        com.android.server.pm.PackageManagerServiceUtils.enforceSystemOrPhoneCaller("grantPermissionsToEnabledCarrierApps", callingUid);
        android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.pm.permission.LegacyPermissionManagerService$$ExternalSyntheticLambda1
            public final void runOrThrow() throws java.lang.Exception {
                this.f$0.lambda$grantDefaultPermissionsToEnabledCarrierApps$6(packageNames, userId);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$grantDefaultPermissionsToEnabledCarrierApps$6(java.lang.String[] packageNames, int userId) throws java.lang.Exception {
        this.mDefaultPermissionGrantPolicy.grantDefaultPermissionsToEnabledCarrierApps(packageNames, userId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    class Internal implements com.android.server.pm.permission.LegacyPermissionManagerInternal {
        private Internal() {
        }

        @Override // com.android.server.pm.permission.LegacyPermissionManagerInternal
        public void resetRuntimePermissions() {
            com.android.server.pm.permission.LegacyPermissionManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.REVOKE_RUNTIME_PERMISSIONS", "revokeRuntimePermission");
            int callingUid = android.os.Binder.getCallingUid();
            if (callingUid != 1000 && callingUid != 0) {
                com.android.server.pm.permission.LegacyPermissionManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS_FULL", "resetRuntimePermissions");
            }
            android.content.pm.PackageManagerInternal packageManagerInternal = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
            final com.android.server.pm.permission.PermissionManagerServiceInternal permissionManagerInternal = (com.android.server.pm.permission.PermissionManagerServiceInternal) com.android.server.LocalServices.getService(com.android.server.pm.permission.PermissionManagerServiceInternal.class);
            for (final int userId : com.android.server.pm.UserManagerService.getInstance().getUserIds()) {
                packageManagerInternal.forEachPackage(new java.util.function.Consumer() { // from class: com.android.server.pm.permission.LegacyPermissionManagerService$Internal$$ExternalSyntheticLambda0
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        com.android.server.pm.permission.LegacyPermissionManagerService.Internal.lambda$resetRuntimePermissions$0(permissionManagerInternal, userId, (com.android.server.pm.pkg.AndroidPackage) obj);
                    }
                });
            }
        }

        static /* synthetic */ void lambda$resetRuntimePermissions$0(com.android.server.pm.permission.PermissionManagerServiceInternal permissionManagerInternal, int userId, com.android.server.pm.pkg.AndroidPackage pkg) {
            if (pkg.getUid() != -1) {
                permissionManagerInternal.resetRuntimePermissions(pkg, userId);
            }
        }

        @Override // com.android.server.pm.permission.LegacyPermissionManagerInternal
        public void setDialerAppPackagesProvider(com.android.server.pm.permission.LegacyPermissionManagerInternal.PackagesProvider provider) {
            com.android.server.pm.permission.LegacyPermissionManagerService.this.mDefaultPermissionGrantPolicy.setDialerAppPackagesProvider(provider);
        }

        @Override // com.android.server.pm.permission.LegacyPermissionManagerInternal
        public void setLocationExtraPackagesProvider(com.android.server.pm.permission.LegacyPermissionManagerInternal.PackagesProvider provider) {
            com.android.server.pm.permission.LegacyPermissionManagerService.this.mDefaultPermissionGrantPolicy.setLocationExtraPackagesProvider(provider);
        }

        @Override // com.android.server.pm.permission.LegacyPermissionManagerInternal
        public void setLocationPackagesProvider(com.android.server.pm.permission.LegacyPermissionManagerInternal.PackagesProvider provider) {
            com.android.server.pm.permission.LegacyPermissionManagerService.this.mDefaultPermissionGrantPolicy.setLocationPackagesProvider(provider);
        }

        @Override // com.android.server.pm.permission.LegacyPermissionManagerInternal
        public void setSimCallManagerPackagesProvider(com.android.server.pm.permission.LegacyPermissionManagerInternal.PackagesProvider provider) {
            com.android.server.pm.permission.LegacyPermissionManagerService.this.mDefaultPermissionGrantPolicy.setSimCallManagerPackagesProvider(provider);
        }

        @Override // com.android.server.pm.permission.LegacyPermissionManagerInternal
        public void setSmsAppPackagesProvider(com.android.server.pm.permission.LegacyPermissionManagerInternal.PackagesProvider provider) {
            com.android.server.pm.permission.LegacyPermissionManagerService.this.mDefaultPermissionGrantPolicy.setSmsAppPackagesProvider(provider);
        }

        @Override // com.android.server.pm.permission.LegacyPermissionManagerInternal
        public void setSyncAdapterPackagesProvider(com.android.server.pm.permission.LegacyPermissionManagerInternal.SyncAdapterPackagesProvider provider) {
            com.android.server.pm.permission.LegacyPermissionManagerService.this.mDefaultPermissionGrantPolicy.setSyncAdapterPackagesProvider(provider);
        }

        @Override // com.android.server.pm.permission.LegacyPermissionManagerInternal
        public void setUseOpenWifiAppPackagesProvider(com.android.server.pm.permission.LegacyPermissionManagerInternal.PackagesProvider provider) {
            com.android.server.pm.permission.LegacyPermissionManagerService.this.mDefaultPermissionGrantPolicy.setUseOpenWifiAppPackagesProvider(provider);
        }

        @Override // com.android.server.pm.permission.LegacyPermissionManagerInternal
        public void setVoiceInteractionPackagesProvider(com.android.server.pm.permission.LegacyPermissionManagerInternal.PackagesProvider provider) {
            com.android.server.pm.permission.LegacyPermissionManagerService.this.mDefaultPermissionGrantPolicy.setVoiceInteractionPackagesProvider(provider);
        }

        @Override // com.android.server.pm.permission.LegacyPermissionManagerInternal
        public void grantDefaultPermissionsToDefaultSimCallManager(java.lang.String packageName, int userId) {
            com.android.server.pm.permission.LegacyPermissionManagerService.this.mDefaultPermissionGrantPolicy.grantDefaultPermissionsToDefaultSimCallManager(packageName, userId);
        }

        @Override // com.android.server.pm.permission.LegacyPermissionManagerInternal
        public void grantDefaultPermissionsToDefaultUseOpenWifiApp(java.lang.String packageName, int userId) {
            com.android.server.pm.permission.LegacyPermissionManagerService.this.mDefaultPermissionGrantPolicy.grantDefaultPermissionsToDefaultUseOpenWifiApp(packageName, userId);
        }

        @Override // com.android.server.pm.permission.LegacyPermissionManagerInternal
        public void grantDefaultPermissions(int userId) {
            com.android.server.pm.permission.LegacyPermissionManagerService.this.mDefaultPermissionGrantPolicy.grantDefaultPermissions(userId);
        }

        @Override // com.android.server.pm.permission.LegacyPermissionManagerInternal
        public void scheduleReadDefaultPermissionExceptions() {
            com.android.server.pm.permission.LegacyPermissionManagerService.this.mDefaultPermissionGrantPolicy.scheduleReadDefaultPermissionExceptions();
        }

        @Override // com.android.server.pm.permission.LegacyPermissionManagerInternal
        public int checkSoundTriggerRecordAudioPermissionForDataDelivery(int uid, java.lang.String packageName, java.lang.String attributionTag, java.lang.String reason) {
            int result = android.content.PermissionChecker.checkPermissionForPreflight(com.android.server.pm.permission.LegacyPermissionManagerService.this.mContext, "android.permission.RECORD_AUDIO", -1, uid, packageName);
            if (result != 0) {
                return result;
            }
            ((android.app.AppOpsManager) com.android.server.pm.permission.LegacyPermissionManagerService.this.mContext.getSystemService(android.app.AppOpsManager.class)).noteOpNoThrow(120, uid, packageName, attributionTag, reason);
            return result;
        }
    }

    public static class Injector {
        private final android.content.Context mContext;
        private final android.content.pm.PackageManagerInternal mPackageManagerInternal = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);

        public Injector(android.content.Context context) {
            this.mContext = context;
        }

        public int getCallingUid() {
            return android.os.Binder.getCallingUid();
        }

        public int getCallingPid() {
            return android.os.Binder.getCallingPid();
        }

        public int checkPermission(java.lang.String permission, int pid, int uid) {
            return this.mContext.checkPermission(permission, pid, uid);
        }

        public long clearCallingIdentity() {
            return android.os.Binder.clearCallingIdentity();
        }

        public void restoreCallingIdentity(long token) {
            android.os.Binder.restoreCallingIdentity(token);
        }

        public java.lang.Object getSystemService(java.lang.String name) {
            return this.mContext.getSystemService(name);
        }

        public android.content.pm.ApplicationInfo getApplicationInfo(java.lang.String packageName, int uid) throws android.content.pm.PackageManager.NameNotFoundException {
            return this.mContext.getPackageManager().getApplicationInfoAsUser(packageName, 0, android.os.UserHandle.getUserHandleForUid(uid));
        }

        public int getPackageUidForUser(java.lang.String packageName, int userId) {
            return this.mPackageManagerInternal.getPackageUid(packageName, 0L, userId);
        }
    }
}
