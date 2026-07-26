package com.android.server.devicepolicy;

/* JADX INFO: loaded from: classes.dex */
public class OverlayPackagesProvider {
    protected static final java.lang.String TAG = "OverlayPackagesProvider";
    private static final java.util.Map<java.lang.String, java.lang.String> sActionToMetadataKeyMap = new java.util.HashMap();
    private static final java.util.Set<java.lang.String> sAllowedActions;
    private final android.content.Context mContext;
    private final com.android.server.devicepolicy.OverlayPackagesProvider.Injector mInjector;
    private final android.content.pm.PackageManager mPm;
    private final com.android.server.devicepolicy.RecursiveStringArrayResourceResolver mRecursiveStringArrayResourceResolver;

    interface Injector {
        java.lang.String getActiveApexPackageNameContainingPackage(java.lang.String str);

        java.lang.String getDevicePolicyManagementRoleHolderPackageName(android.content.Context context);

        java.util.List<android.view.inputmethod.InputMethodInfo> getInputMethodListAsUser(int i);
    }

    static {
        sActionToMetadataKeyMap.put("android.app.action.PROVISION_MANAGED_USER", "android.app.REQUIRED_APP_MANAGED_USER");
        sActionToMetadataKeyMap.put("android.app.action.PROVISION_MANAGED_PROFILE", "android.app.REQUIRED_APP_MANAGED_PROFILE");
        sActionToMetadataKeyMap.put("android.app.action.PROVISION_MANAGED_DEVICE", "android.app.REQUIRED_APP_MANAGED_DEVICE");
        sAllowedActions = new java.util.HashSet();
        sAllowedActions.add("android.app.action.PROVISION_MANAGED_USER");
        sAllowedActions.add("android.app.action.PROVISION_MANAGED_PROFILE");
        sAllowedActions.add("android.app.action.PROVISION_MANAGED_DEVICE");
    }

    public OverlayPackagesProvider(android.content.Context context) {
        this(context, new com.android.server.devicepolicy.OverlayPackagesProvider.DefaultInjector(), new com.android.server.devicepolicy.RecursiveStringArrayResourceResolver(context.getResources()));
    }

    /* JADX INFO: Access modifiers changed from: private */
    static final class DefaultInjector implements com.android.server.devicepolicy.OverlayPackagesProvider.Injector {
        private DefaultInjector() {
        }

        @Override // com.android.server.devicepolicy.OverlayPackagesProvider.Injector
        public java.util.List<android.view.inputmethod.InputMethodInfo> getInputMethodListAsUser(int userId) {
            return com.android.server.inputmethod.InputMethodManagerInternal.get().getInputMethodListAsUser(userId);
        }

        @Override // com.android.server.devicepolicy.OverlayPackagesProvider.Injector
        public java.lang.String getActiveApexPackageNameContainingPackage(java.lang.String packageName) {
            return com.android.server.pm.ApexManager.getInstance().getActiveApexPackageNameContainingPackage(packageName);
        }

        @Override // com.android.server.devicepolicy.OverlayPackagesProvider.Injector
        public java.lang.String getDevicePolicyManagementRoleHolderPackageName(final android.content.Context context) {
            return (java.lang.String) android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.devicepolicy.OverlayPackagesProvider$DefaultInjector$$ExternalSyntheticLambda0
                public final java.lang.Object getOrThrow() {
                    return com.android.server.devicepolicy.OverlayPackagesProvider.DefaultInjector.lambda$getDevicePolicyManagementRoleHolderPackageName$0(context);
                }
            });
        }

        static /* synthetic */ java.lang.String lambda$getDevicePolicyManagementRoleHolderPackageName$0(android.content.Context context) throws java.lang.Exception {
            android.app.role.RoleManager roleManager = (android.app.role.RoleManager) context.getSystemService(android.app.role.RoleManager.class);
            java.util.List<java.lang.String> roleHolders = roleManager.getRoleHolders("android.app.role.DEVICE_POLICY_MANAGEMENT");
            if (roleHolders.isEmpty()) {
                return null;
            }
            return roleHolders.get(0);
        }
    }

    OverlayPackagesProvider(android.content.Context context, com.android.server.devicepolicy.OverlayPackagesProvider.Injector injector, com.android.server.devicepolicy.RecursiveStringArrayResourceResolver recursiveStringArrayResourceResolver) {
        this.mContext = context;
        this.mPm = (android.content.pm.PackageManager) java.util.Objects.requireNonNull(context.getPackageManager());
        this.mInjector = (com.android.server.devicepolicy.OverlayPackagesProvider.Injector) java.util.Objects.requireNonNull(injector);
        this.mRecursiveStringArrayResourceResolver = (com.android.server.devicepolicy.RecursiveStringArrayResourceResolver) java.util.Objects.requireNonNull(recursiveStringArrayResourceResolver);
    }

    public java.util.Set<java.lang.String> getNonRequiredApps(android.content.ComponentName admin, int userId, java.lang.String provisioningAction) {
        java.util.Objects.requireNonNull(admin);
        com.android.internal.util.Preconditions.checkArgument(sAllowedActions.contains(provisioningAction));
        java.util.Set<java.lang.String> nonRequiredApps = getLaunchableApps(userId);
        nonRequiredApps.removeAll(getRequiredApps(provisioningAction, admin.getPackageName()));
        nonRequiredApps.removeAll(getSystemInputMethods(userId));
        nonRequiredApps.addAll(getDisallowedApps(provisioningAction));
        nonRequiredApps.removeAll(getRequiredAppsMainlineModules(nonRequiredApps, provisioningAction));
        nonRequiredApps.removeAll(getDeviceManagerRoleHolders());
        return nonRequiredApps;
    }

    private java.util.Set<java.lang.String> getDeviceManagerRoleHolders() {
        java.util.HashSet<java.lang.String> result = new java.util.HashSet<>();
        java.lang.String deviceManagerRoleHolderPackageName = this.mInjector.getDevicePolicyManagementRoleHolderPackageName(this.mContext);
        if (deviceManagerRoleHolderPackageName != null) {
            result.add(deviceManagerRoleHolderPackageName);
        }
        return result;
    }

    private java.util.Set<java.lang.String> getRequiredAppsMainlineModules(java.util.Set<java.lang.String> packageNames, java.lang.String provisioningAction) {
        java.util.Set<java.lang.String> result = new java.util.HashSet<>();
        for (java.lang.String packageName : packageNames) {
            if (isMainlineModule(packageName) && isRequiredAppDeclaredInMetadata(packageName, provisioningAction)) {
                result.add(packageName);
            }
        }
        return result;
    }

    private boolean isRequiredAppDeclaredInMetadata(java.lang.String packageName, java.lang.String provisioningAction) {
        try {
            android.content.pm.PackageInfo packageInfo = this.mPm.getPackageInfo(packageName, 128);
            if (packageInfo.applicationInfo == null || packageInfo.applicationInfo.metaData == null) {
                return false;
            }
            java.lang.String metadataKey = sActionToMetadataKeyMap.get(provisioningAction);
            return packageInfo.applicationInfo.metaData.getBoolean(metadataKey);
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private boolean isMainlineModule(java.lang.String packageName) {
        return isRegularMainlineModule(packageName) || isApkInApexMainlineModule(packageName);
    }

    private boolean isRegularMainlineModule(java.lang.String packageName) {
        try {
            this.mPm.getModuleInfo(packageName, 0);
            return true;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private boolean isApkInApexMainlineModule(java.lang.String packageName) {
        java.lang.String apexPackageName = this.mInjector.getActiveApexPackageNameContainingPackage(packageName);
        return apexPackageName != null;
    }

    private java.util.Set<java.lang.String> getLaunchableApps(int userId) {
        android.content.Intent launcherIntent = new android.content.Intent("android.intent.action.MAIN");
        launcherIntent.addCategory("android.intent.category.LAUNCHER");
        java.util.List<android.content.pm.ResolveInfo> resolveInfos = this.mPm.queryIntentActivitiesAsUser(launcherIntent, 795136, userId);
        java.util.Set<java.lang.String> apps = new android.util.ArraySet<>();
        for (android.content.pm.ResolveInfo resolveInfo : resolveInfos) {
            apps.add(resolveInfo.activityInfo.packageName);
        }
        return apps;
    }

    private java.util.Set<java.lang.String> getSystemInputMethods(int userId) {
        java.util.List<android.view.inputmethod.InputMethodInfo> inputMethods = this.mInjector.getInputMethodListAsUser(userId);
        java.util.Set<java.lang.String> systemInputMethods = new android.util.ArraySet<>();
        for (android.view.inputmethod.InputMethodInfo inputMethodInfo : inputMethods) {
            android.content.pm.ApplicationInfo applicationInfo = inputMethodInfo.getServiceInfo().applicationInfo;
            if (applicationInfo.isSystemApp()) {
                systemInputMethods.add(inputMethodInfo.getPackageName());
            }
        }
        return systemInputMethods;
    }

    private java.util.Set<java.lang.String> getRequiredApps(java.lang.String provisioningAction, java.lang.String dpcPackageName) {
        java.util.Set<java.lang.String> requiredApps = new android.util.ArraySet<>();
        requiredApps.addAll(getRequiredAppsSet(provisioningAction));
        requiredApps.addAll(getVendorRequiredAppsSet(provisioningAction));
        requiredApps.add(dpcPackageName);
        return requiredApps;
    }

    private java.util.Set<java.lang.String> getDisallowedApps(java.lang.String provisioningAction) {
        java.util.Set<java.lang.String> disallowedApps = new android.util.ArraySet<>();
        disallowedApps.addAll(getDisallowedAppsSet(provisioningAction));
        disallowedApps.addAll(getVendorDisallowedAppsSet(provisioningAction));
        return disallowedApps;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:14:0x0026  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private java.util.Set<java.lang.String> getRequiredAppsSet(java.lang.String r4) {
        /*
            r3 = this;
            int r0 = r4.hashCode()
            switch(r0) {
                case -920528692: goto L1c;
                case -514404415: goto L12;
                case -340845101: goto L8;
                default: goto L7;
            }
        L7:
            goto L26
        L8:
            java.lang.String r0 = "android.app.action.PROVISION_MANAGED_PROFILE"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L7
            r0 = 1
            goto L27
        L12:
            java.lang.String r0 = "android.app.action.PROVISION_MANAGED_USER"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L7
            r0 = 0
            goto L27
        L1c:
            java.lang.String r0 = "android.app.action.PROVISION_MANAGED_DEVICE"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L7
            r0 = 2
            goto L27
        L26:
            r0 = -1
        L27:
            switch(r0) {
                case 0: goto L51;
                case 1: goto L4d;
                case 2: goto L49;
                default: goto L2a;
            }
        L2a:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "Provisioning type "
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.StringBuilder r1 = r1.append(r4)
            java.lang.String r2 = " not supported."
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.String r1 = r1.toString()
            r0.<init>(r1)
            throw r0
        L49:
            r0 = 17236225(0x1070101, float:2.4796304E-38)
            goto L54
        L4d:
            r0 = 17236226(0x1070102, float:2.4796307E-38)
            goto L54
        L51:
            r0 = 17236227(0x1070103, float:2.479631E-38)
        L54:
            java.util.Set r1 = r3.resolveStringArray(r0)
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.devicepolicy.OverlayPackagesProvider.getRequiredAppsSet(java.lang.String):java.util.Set");
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:14:0x0026  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private java.util.Set<java.lang.String> getDisallowedAppsSet(java.lang.String r4) {
        /*
            r3 = this;
            int r0 = r4.hashCode()
            switch(r0) {
                case -920528692: goto L1c;
                case -514404415: goto L12;
                case -340845101: goto L8;
                default: goto L7;
            }
        L7:
            goto L26
        L8:
            java.lang.String r0 = "android.app.action.PROVISION_MANAGED_PROFILE"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L7
            r0 = 1
            goto L27
        L12:
            java.lang.String r0 = "android.app.action.PROVISION_MANAGED_USER"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L7
            r0 = 0
            goto L27
        L1c:
            java.lang.String r0 = "android.app.action.PROVISION_MANAGED_DEVICE"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L7
            r0 = 2
            goto L27
        L26:
            r0 = -1
        L27:
            switch(r0) {
                case 0: goto L51;
                case 1: goto L4d;
                case 2: goto L49;
                default: goto L2a;
            }
        L2a:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "Provisioning type "
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.StringBuilder r1 = r1.append(r4)
            java.lang.String r2 = " not supported."
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.String r1 = r1.toString()
            r0.<init>(r1)
            throw r0
        L49:
            r0 = 17236205(0x10700ed, float:2.4796248E-38)
            goto L54
        L4d:
            r0 = 17236206(0x10700ee, float:2.479625E-38)
            goto L54
        L51:
            r0 = 17236207(0x10700ef, float:2.4796254E-38)
        L54:
            java.util.Set r1 = r3.resolveStringArray(r0)
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.devicepolicy.OverlayPackagesProvider.getDisallowedAppsSet(java.lang.String):java.util.Set");
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:14:0x0026  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private java.util.Set<java.lang.String> getVendorRequiredAppsSet(java.lang.String r4) {
        /*
            r3 = this;
            int r0 = r4.hashCode()
            switch(r0) {
                case -920528692: goto L1c;
                case -514404415: goto L12;
                case -340845101: goto L8;
                default: goto L7;
            }
        L7:
            goto L26
        L8:
            java.lang.String r0 = "android.app.action.PROVISION_MANAGED_PROFILE"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L7
            r0 = 1
            goto L27
        L12:
            java.lang.String r0 = "android.app.action.PROVISION_MANAGED_USER"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L7
            r0 = 0
            goto L27
        L1c:
            java.lang.String r0 = "android.app.action.PROVISION_MANAGED_DEVICE"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L7
            r0 = 2
            goto L27
        L26:
            r0 = -1
        L27:
            switch(r0) {
                case 0: goto L51;
                case 1: goto L4d;
                case 2: goto L49;
                default: goto L2a;
            }
        L2a:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "Provisioning type "
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.StringBuilder r1 = r1.append(r4)
            java.lang.String r2 = " not supported."
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.String r1 = r1.toString()
            r0.<init>(r1)
            throw r0
        L49:
            r0 = 17236240(0x1070110, float:2.4796346E-38)
            goto L54
        L4d:
            r0 = 17236241(0x1070111, float:2.479635E-38)
            goto L54
        L51:
            r0 = 17236242(0x1070112, float:2.4796352E-38)
        L54:
            java.util.Set r1 = r3.resolveStringArray(r0)
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.devicepolicy.OverlayPackagesProvider.getVendorRequiredAppsSet(java.lang.String):java.util.Set");
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:14:0x0026  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private java.util.Set<java.lang.String> getVendorDisallowedAppsSet(java.lang.String r4) {
        /*
            r3 = this;
            int r0 = r4.hashCode()
            switch(r0) {
                case -920528692: goto L1c;
                case -514404415: goto L12;
                case -340845101: goto L8;
                default: goto L7;
            }
        L7:
            goto L26
        L8:
            java.lang.String r0 = "android.app.action.PROVISION_MANAGED_PROFILE"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L7
            r0 = 1
            goto L27
        L12:
            java.lang.String r0 = "android.app.action.PROVISION_MANAGED_USER"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L7
            r0 = 0
            goto L27
        L1c:
            java.lang.String r0 = "android.app.action.PROVISION_MANAGED_DEVICE"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L7
            r0 = 2
            goto L27
        L26:
            r0 = -1
        L27:
            switch(r0) {
                case 0: goto L51;
                case 1: goto L4d;
                case 2: goto L49;
                default: goto L2a;
            }
        L2a:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "Provisioning type "
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.StringBuilder r1 = r1.append(r4)
            java.lang.String r2 = " not supported."
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.String r1 = r1.toString()
            r0.<init>(r1)
            throw r0
        L49:
            r0 = 17236236(0x107010c, float:2.4796335E-38)
            goto L54
        L4d:
            r0 = 17236237(0x107010d, float:2.4796338E-38)
            goto L54
        L51:
            r0 = 17236238(0x107010e, float:2.479634E-38)
        L54:
            java.util.Set r1 = r3.resolveStringArray(r0)
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.devicepolicy.OverlayPackagesProvider.getVendorDisallowedAppsSet(java.lang.String):java.util.Set");
    }

    private java.util.Set<java.lang.String> resolveStringArray(int resId) {
        if (android.app.admin.flags.Flags.isRecursiveRequiredAppMergingEnabled()) {
            return this.mRecursiveStringArrayResourceResolver.resolve(this.mContext.getPackageName(), resId);
        }
        return new android.util.ArraySet(java.util.Arrays.asList(this.mContext.getResources().getStringArray(resId)));
    }

    void dump(android.util.IndentingPrintWriter pw) {
        pw.println(TAG);
        pw.increaseIndent();
        com.android.server.devicepolicy.DevicePolicyManagerService.dumpApps(pw, "required_apps_managed_device", (java.lang.String[]) resolveStringArray(android.R.array.networkAttributes).toArray(new java.util.function.IntFunction() { // from class: com.android.server.devicepolicy.OverlayPackagesProvider$$ExternalSyntheticLambda0
            @Override // java.util.function.IntFunction
            public final java.lang.Object apply(int i) {
                return com.android.server.devicepolicy.OverlayPackagesProvider.lambda$dump$0(i);
            }
        }));
        com.android.server.devicepolicy.DevicePolicyManagerService.dumpApps(pw, "required_apps_managed_user", (java.lang.String[]) resolveStringArray(android.R.array.networks_not_clear_data).toArray(new java.util.function.IntFunction() { // from class: com.android.server.devicepolicy.OverlayPackagesProvider$$ExternalSyntheticLambda3
            @Override // java.util.function.IntFunction
            public final java.lang.Object apply(int i) {
                return com.android.server.devicepolicy.OverlayPackagesProvider.lambda$dump$1(i);
            }
        }));
        com.android.server.devicepolicy.DevicePolicyManagerService.dumpApps(pw, "required_apps_managed_profile", (java.lang.String[]) resolveStringArray(android.R.array.network_switch_type_name).toArray(new java.util.function.IntFunction() { // from class: com.android.server.devicepolicy.OverlayPackagesProvider$$ExternalSyntheticLambda4
            @Override // java.util.function.IntFunction
            public final java.lang.Object apply(int i) {
                return com.android.server.devicepolicy.OverlayPackagesProvider.lambda$dump$2(i);
            }
        }));
        com.android.server.devicepolicy.DevicePolicyManagerService.dumpApps(pw, "disallowed_apps_managed_device", (java.lang.String[]) resolveStringArray(android.R.array.demo_device_provisioning_known_signers).toArray(new java.util.function.IntFunction() { // from class: com.android.server.devicepolicy.OverlayPackagesProvider$$ExternalSyntheticLambda5
            @Override // java.util.function.IntFunction
            public final java.lang.Object apply(int i) {
                return com.android.server.devicepolicy.OverlayPackagesProvider.lambda$dump$3(i);
            }
        }));
        com.android.server.devicepolicy.DevicePolicyManagerService.dumpApps(pw, "disallowed_apps_managed_user", (java.lang.String[]) resolveStringArray(android.R.array.device_state_notification_active_titles).toArray(new java.util.function.IntFunction() { // from class: com.android.server.devicepolicy.OverlayPackagesProvider$$ExternalSyntheticLambda6
            @Override // java.util.function.IntFunction
            public final java.lang.Object apply(int i) {
                return com.android.server.devicepolicy.OverlayPackagesProvider.lambda$dump$4(i);
            }
        }));
        com.android.server.devicepolicy.DevicePolicyManagerService.dumpApps(pw, "disallowed_apps_managed_device", (java.lang.String[]) resolveStringArray(android.R.array.demo_device_provisioning_known_signers).toArray(new java.util.function.IntFunction() { // from class: com.android.server.devicepolicy.OverlayPackagesProvider$$ExternalSyntheticLambda7
            @Override // java.util.function.IntFunction
            public final java.lang.Object apply(int i) {
                return com.android.server.devicepolicy.OverlayPackagesProvider.lambda$dump$5(i);
            }
        }));
        com.android.server.devicepolicy.DevicePolicyManagerService.dumpApps(pw, "vendor_required_apps_managed_device", (java.lang.String[]) resolveStringArray(android.R.array.sim_colors).toArray(new java.util.function.IntFunction() { // from class: com.android.server.devicepolicy.OverlayPackagesProvider$$ExternalSyntheticLambda8
            @Override // java.util.function.IntFunction
            public final java.lang.Object apply(int i) {
                return com.android.server.devicepolicy.OverlayPackagesProvider.lambda$dump$6(i);
            }
        }));
        com.android.server.devicepolicy.DevicePolicyManagerService.dumpApps(pw, "vendor_required_apps_managed_user", (java.lang.String[]) resolveStringArray(android.R.array.special_locale_names).toArray(new java.util.function.IntFunction() { // from class: com.android.server.devicepolicy.OverlayPackagesProvider$$ExternalSyntheticLambda9
            @Override // java.util.function.IntFunction
            public final java.lang.Object apply(int i) {
                return com.android.server.devicepolicy.OverlayPackagesProvider.lambda$dump$7(i);
            }
        }));
        com.android.server.devicepolicy.DevicePolicyManagerService.dumpApps(pw, "vendor_required_apps_managed_profile", (java.lang.String[]) resolveStringArray(android.R.array.special_locale_codes).toArray(new java.util.function.IntFunction() { // from class: com.android.server.devicepolicy.OverlayPackagesProvider$$ExternalSyntheticLambda10
            @Override // java.util.function.IntFunction
            public final java.lang.Object apply(int i) {
                return com.android.server.devicepolicy.OverlayPackagesProvider.lambda$dump$8(i);
            }
        }));
        com.android.server.devicepolicy.DevicePolicyManagerService.dumpApps(pw, "vendor_disallowed_apps_managed_user", (java.lang.String[]) resolveStringArray(android.R.array.resolver_target_actions_pin).toArray(new java.util.function.IntFunction() { // from class: com.android.server.devicepolicy.OverlayPackagesProvider$$ExternalSyntheticLambda11
            @Override // java.util.function.IntFunction
            public final java.lang.Object apply(int i) {
                return com.android.server.devicepolicy.OverlayPackagesProvider.lambda$dump$9(i);
            }
        }));
        com.android.server.devicepolicy.DevicePolicyManagerService.dumpApps(pw, "vendor_disallowed_apps_managed_device", (java.lang.String[]) resolveStringArray(android.R.array.required_apps_managed_profile).toArray(new java.util.function.IntFunction() { // from class: com.android.server.devicepolicy.OverlayPackagesProvider$$ExternalSyntheticLambda1
            @Override // java.util.function.IntFunction
            public final java.lang.Object apply(int i) {
                return com.android.server.devicepolicy.OverlayPackagesProvider.lambda$dump$10(i);
            }
        }));
        com.android.server.devicepolicy.DevicePolicyManagerService.dumpApps(pw, "vendor_disallowed_apps_managed_profile", (java.lang.String[]) resolveStringArray(android.R.array.required_apps_managed_user).toArray(new java.util.function.IntFunction() { // from class: com.android.server.devicepolicy.OverlayPackagesProvider$$ExternalSyntheticLambda2
            @Override // java.util.function.IntFunction
            public final java.lang.Object apply(int i) {
                return com.android.server.devicepolicy.OverlayPackagesProvider.lambda$dump$11(i);
            }
        }));
        pw.decreaseIndent();
    }

    static /* synthetic */ java.lang.String[] lambda$dump$0(int x$0) {
        return new java.lang.String[x$0];
    }

    static /* synthetic */ java.lang.String[] lambda$dump$1(int x$0) {
        return new java.lang.String[x$0];
    }

    static /* synthetic */ java.lang.String[] lambda$dump$2(int x$0) {
        return new java.lang.String[x$0];
    }

    static /* synthetic */ java.lang.String[] lambda$dump$3(int x$0) {
        return new java.lang.String[x$0];
    }

    static /* synthetic */ java.lang.String[] lambda$dump$4(int x$0) {
        return new java.lang.String[x$0];
    }

    static /* synthetic */ java.lang.String[] lambda$dump$5(int x$0) {
        return new java.lang.String[x$0];
    }

    static /* synthetic */ java.lang.String[] lambda$dump$6(int x$0) {
        return new java.lang.String[x$0];
    }

    static /* synthetic */ java.lang.String[] lambda$dump$7(int x$0) {
        return new java.lang.String[x$0];
    }

    static /* synthetic */ java.lang.String[] lambda$dump$8(int x$0) {
        return new java.lang.String[x$0];
    }

    static /* synthetic */ java.lang.String[] lambda$dump$9(int x$0) {
        return new java.lang.String[x$0];
    }

    static /* synthetic */ java.lang.String[] lambda$dump$10(int x$0) {
        return new java.lang.String[x$0];
    }

    static /* synthetic */ java.lang.String[] lambda$dump$11(int x$0) {
        return new java.lang.String[x$0];
    }
}
