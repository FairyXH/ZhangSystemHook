package com.android.server.devicepolicy;

/* JADX INFO: loaded from: classes.dex */
final class PolicyDefinition<V> {
    private static final int POLICY_FLAG_GLOBAL_ONLY_POLICY = 1;
    private static final int POLICY_FLAG_INHERITABLE = 4;
    private static final int POLICY_FLAG_LOCAL_ONLY_POLICY = 2;
    private static final int POLICY_FLAG_NONE = 0;
    private static final int POLICY_FLAG_NON_COEXISTABLE_POLICY = 8;
    private static final int POLICY_FLAG_SKIP_ENFORCEMENT_IF_UNCHANGED = 32;
    private static final int POLICY_FLAG_USER_RESTRICTION_POLICY = 16;
    static final java.lang.String TAG = "PolicyDefinition";
    private final com.android.internal.util.function.QuadFunction<V, android.content.Context, java.lang.Integer, android.app.admin.PolicyKey, java.lang.Boolean> mPolicyEnforcerCallback;
    private final int mPolicyFlags;
    private final android.app.admin.PolicyKey mPolicyKey;
    private final com.android.server.devicepolicy.PolicySerializer<V> mPolicySerializer;
    private final com.android.server.devicepolicy.ResolutionMechanism<V> mResolutionMechanism;
    private static final com.android.server.devicepolicy.MostRestrictive<java.lang.Boolean> FALSE_MORE_RESTRICTIVE = new com.android.server.devicepolicy.MostRestrictive<>(java.util.List.of(new android.app.admin.BooleanPolicyValue(false), new android.app.admin.BooleanPolicyValue(true)));
    private static final com.android.server.devicepolicy.MostRestrictive<java.lang.Boolean> TRUE_MORE_RESTRICTIVE = new com.android.server.devicepolicy.MostRestrictive<>(java.util.List.of(new android.app.admin.BooleanPolicyValue(true), new android.app.admin.BooleanPolicyValue(false)));
    static com.android.server.devicepolicy.PolicyDefinition<java.lang.Boolean> AUTO_TIMEZONE = new com.android.server.devicepolicy.PolicyDefinition<>(new android.app.admin.NoArgsPolicyKey("autoTimezone"), TRUE_MORE_RESTRICTIVE, 1, new com.android.internal.util.function.QuadFunction() { // from class: com.android.server.devicepolicy.PolicyDefinition$$ExternalSyntheticLambda1
        public final java.lang.Object apply(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3, java.lang.Object obj4) {
            return java.lang.Boolean.valueOf(com.android.server.devicepolicy.PolicyEnforcerCallbacks.setAutoTimezoneEnabled((java.lang.Boolean) obj, (android.content.Context) obj2));
        }
    }, new com.android.server.devicepolicy.BooleanPolicySerializer());
    static final com.android.server.devicepolicy.PolicyDefinition<java.lang.Integer> GENERIC_PERMISSION_GRANT = new com.android.server.devicepolicy.PolicyDefinition<>(new android.app.admin.PackagePermissionPolicyKey("permissionGrant"), new com.android.server.devicepolicy.MostRestrictive(java.util.List.of(new android.app.admin.IntegerPolicyValue(2), new android.app.admin.IntegerPolicyValue(1), new android.app.admin.IntegerPolicyValue(0))), 2, new com.android.internal.util.function.QuadFunction() { // from class: com.android.server.devicepolicy.PolicyDefinition$$ExternalSyntheticLambda10
        public final java.lang.Object apply(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3, java.lang.Object obj4) {
            return java.lang.Boolean.valueOf(com.android.server.devicepolicy.PolicyEnforcerCallbacks.setPermissionGrantState((java.lang.Integer) obj, (android.content.Context) obj2, ((java.lang.Integer) obj3).intValue(), (android.app.admin.PolicyKey) obj4));
        }
    }, new com.android.server.devicepolicy.IntegerPolicySerializer());
    static com.android.server.devicepolicy.PolicyDefinition<java.lang.Boolean> SECURITY_LOGGING = new com.android.server.devicepolicy.PolicyDefinition<>(new android.app.admin.NoArgsPolicyKey("securityLogging"), TRUE_MORE_RESTRICTIVE, 1, new com.android.internal.util.function.QuadFunction() { // from class: com.android.server.devicepolicy.PolicyDefinition$$ExternalSyntheticLambda11
        public final java.lang.Object apply(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3, java.lang.Object obj4) {
            return java.lang.Boolean.valueOf(com.android.server.devicepolicy.PolicyEnforcerCallbacks.enforceSecurityLogging((java.lang.Boolean) obj, (android.content.Context) obj2, ((java.lang.Integer) obj3).intValue(), (android.app.admin.PolicyKey) obj4));
        }
    }, new com.android.server.devicepolicy.BooleanPolicySerializer());
    static com.android.server.devicepolicy.PolicyDefinition<java.lang.Boolean> AUDIT_LOGGING = new com.android.server.devicepolicy.PolicyDefinition<>(new android.app.admin.NoArgsPolicyKey("auditLogging"), TRUE_MORE_RESTRICTIVE, 1, new com.android.internal.util.function.QuadFunction() { // from class: com.android.server.devicepolicy.PolicyDefinition$$ExternalSyntheticLambda12
        public final java.lang.Object apply(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3, java.lang.Object obj4) {
            return java.lang.Boolean.valueOf(com.android.server.devicepolicy.PolicyEnforcerCallbacks.enforceAuditLogging((java.lang.Boolean) obj, (android.content.Context) obj2, ((java.lang.Integer) obj3).intValue(), (android.app.admin.PolicyKey) obj4));
        }
    }, new com.android.server.devicepolicy.BooleanPolicySerializer());
    static com.android.server.devicepolicy.PolicyDefinition<android.app.admin.LockTaskPolicy> LOCK_TASK = new com.android.server.devicepolicy.PolicyDefinition<>(new android.app.admin.NoArgsPolicyKey("lockTask"), new com.android.server.devicepolicy.TopPriority(java.util.List.of(com.android.server.devicepolicy.EnforcingAdmin.getRoleAuthorityOf("android.app.role.SYSTEM_FINANCED_DEVICE_CONTROLLER"), "enterprise")), 2, new com.android.internal.util.function.QuadFunction() { // from class: com.android.server.devicepolicy.PolicyDefinition$$ExternalSyntheticLambda13
        public final java.lang.Object apply(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3, java.lang.Object obj4) {
            return java.lang.Boolean.valueOf(com.android.server.devicepolicy.PolicyEnforcerCallbacks.setLockTask((android.app.admin.LockTaskPolicy) obj, (android.content.Context) obj2, ((java.lang.Integer) obj3).intValue()));
        }
    }, new com.android.server.devicepolicy.LockTaskPolicySerializer());
    static com.android.server.devicepolicy.PolicyDefinition<java.util.Set<java.lang.String>> USER_CONTROLLED_DISABLED_PACKAGES = new com.android.server.devicepolicy.PolicyDefinition<>(new android.app.admin.NoArgsPolicyKey("userControlDisabledPackages"), new com.android.server.devicepolicy.PackageSetUnion(), new com.android.internal.util.function.QuadFunction() { // from class: com.android.server.devicepolicy.PolicyDefinition$$ExternalSyntheticLambda14
        public final java.lang.Object apply(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3, java.lang.Object obj4) {
            return java.lang.Boolean.valueOf(com.android.server.devicepolicy.PolicyEnforcerCallbacks.setUserControlDisabledPackages((java.util.Set) obj, (android.content.Context) obj2, ((java.lang.Integer) obj3).intValue(), (android.app.admin.PolicyKey) obj4));
        }
    }, new com.android.server.devicepolicy.PackageSetPolicySerializer());
    static com.android.server.devicepolicy.PolicyDefinition<android.content.ComponentName> GENERIC_PERSISTENT_PREFERRED_ACTIVITY = new com.android.server.devicepolicy.PolicyDefinition<>(new android.app.admin.IntentFilterPolicyKey("persistentPreferredActivity"), new com.android.server.devicepolicy.TopPriority(java.util.List.of(com.android.server.devicepolicy.EnforcingAdmin.getRoleAuthorityOf("android.app.role.SYSTEM_FINANCED_DEVICE_CONTROLLER"), "enterprise")), 2, new com.android.internal.util.function.QuadFunction() { // from class: com.android.server.devicepolicy.PolicyDefinition$$ExternalSyntheticLambda15
        public final java.lang.Object apply(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3, java.lang.Object obj4) {
            return java.lang.Boolean.valueOf(com.android.server.devicepolicy.PolicyEnforcerCallbacks.addPersistentPreferredActivity((android.content.ComponentName) obj, (android.content.Context) obj2, ((java.lang.Integer) obj3).intValue(), (android.app.admin.PolicyKey) obj4));
        }
    }, new com.android.server.devicepolicy.ComponentNamePolicySerializer());
    static com.android.server.devicepolicy.PolicyDefinition<java.lang.Boolean> GENERIC_PACKAGE_UNINSTALL_BLOCKED = new com.android.server.devicepolicy.PolicyDefinition<>(new android.app.admin.PackagePolicyKey("packageUninstallBlocked"), TRUE_MORE_RESTRICTIVE, 2, new com.android.internal.util.function.QuadFunction() { // from class: com.android.server.devicepolicy.PolicyDefinition$$ExternalSyntheticLambda16
        public final java.lang.Object apply(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3, java.lang.Object obj4) {
            return java.lang.Boolean.valueOf(com.android.server.devicepolicy.PolicyEnforcerCallbacks.setUninstallBlocked((java.lang.Boolean) obj, (android.content.Context) obj2, ((java.lang.Integer) obj3).intValue(), (android.app.admin.PolicyKey) obj4));
        }
    }, new com.android.server.devicepolicy.BooleanPolicySerializer());
    static com.android.server.devicepolicy.PolicyDefinition<android.os.Bundle> GENERIC_APPLICATION_RESTRICTIONS = new com.android.server.devicepolicy.PolicyDefinition<>(new android.app.admin.PackagePolicyKey("applicationRestrictions"), new com.android.server.devicepolicy.MostRecent(), 46, new com.android.internal.util.function.QuadFunction() { // from class: com.android.server.devicepolicy.PolicyDefinition$$ExternalSyntheticLambda17
        public final java.lang.Object apply(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3, java.lang.Object obj4) {
            return java.lang.Boolean.valueOf(com.android.server.devicepolicy.PolicyEnforcerCallbacks.setApplicationRestrictions((android.os.Bundle) obj, (android.content.Context) obj2, (java.lang.Integer) obj3, (android.app.admin.PolicyKey) obj4));
        }
    }, new com.android.server.devicepolicy.BundlePolicySerializer());
    static com.android.server.devicepolicy.PolicyDefinition<java.lang.Long> RESET_PASSWORD_TOKEN = new com.android.server.devicepolicy.PolicyDefinition<>(new android.app.admin.NoArgsPolicyKey("resetPasswordToken"), new com.android.server.devicepolicy.MostRecent(), 10, new com.android.internal.util.function.QuadFunction() { // from class: com.android.server.devicepolicy.PolicyDefinition$$ExternalSyntheticLambda18
        public final java.lang.Object apply(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3, java.lang.Object obj4) {
            return java.lang.Boolean.valueOf(com.android.server.devicepolicy.PolicyEnforcerCallbacks.noOp((java.lang.Long) obj, (android.content.Context) obj2, (java.lang.Integer) obj3, (android.app.admin.PolicyKey) obj4));
        }
    }, new com.android.server.devicepolicy.LongPolicySerializer());
    static com.android.server.devicepolicy.PolicyDefinition<java.lang.Integer> KEYGUARD_DISABLED_FEATURES = new com.android.server.devicepolicy.PolicyDefinition<>(new android.app.admin.NoArgsPolicyKey("keyguardDisabledFeatures"), new com.android.server.devicepolicy.FlagUnion(), 2, new com.android.internal.util.function.QuadFunction() { // from class: com.android.server.devicepolicy.PolicyDefinition$$ExternalSyntheticLambda2
        public final java.lang.Object apply(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3, java.lang.Object obj4) {
            return java.lang.Boolean.valueOf(com.android.server.devicepolicy.PolicyEnforcerCallbacks.noOp((java.lang.Integer) obj, (android.content.Context) obj2, (java.lang.Integer) obj3, (android.app.admin.PolicyKey) obj4));
        }
    }, new com.android.server.devicepolicy.IntegerPolicySerializer());
    static com.android.server.devicepolicy.PolicyDefinition<java.lang.Boolean> GENERIC_APPLICATION_HIDDEN = new com.android.server.devicepolicy.PolicyDefinition<>(new android.app.admin.PackagePolicyKey("applicationHidden"), TRUE_MORE_RESTRICTIVE, 6, new com.android.internal.util.function.QuadFunction() { // from class: com.android.server.devicepolicy.PolicyDefinition$$ExternalSyntheticLambda3
        public final java.lang.Object apply(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3, java.lang.Object obj4) {
            return java.lang.Boolean.valueOf(com.android.server.devicepolicy.PolicyEnforcerCallbacks.setApplicationHidden((java.lang.Boolean) obj, (android.content.Context) obj2, ((java.lang.Integer) obj3).intValue(), (android.app.admin.PolicyKey) obj4));
        }
    }, new com.android.server.devicepolicy.BooleanPolicySerializer());
    static com.android.server.devicepolicy.PolicyDefinition<java.lang.Boolean> GENERIC_ACCOUNT_MANAGEMENT_DISABLED = new com.android.server.devicepolicy.PolicyDefinition<>(new android.app.admin.AccountTypePolicyKey("accountManagementDisabled"), TRUE_MORE_RESTRICTIVE, 6, new com.android.internal.util.function.QuadFunction() { // from class: com.android.server.devicepolicy.PolicyDefinition$$ExternalSyntheticLambda4
        public final java.lang.Object apply(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3, java.lang.Object obj4) {
            return java.lang.Boolean.valueOf(com.android.server.devicepolicy.PolicyEnforcerCallbacks.noOp((java.lang.Boolean) obj, (android.content.Context) obj2, (java.lang.Integer) obj3, (android.app.admin.PolicyKey) obj4));
        }
    }, new com.android.server.devicepolicy.BooleanPolicySerializer());
    static com.android.server.devicepolicy.PolicyDefinition<java.util.Set<java.lang.String>> PERMITTED_INPUT_METHODS = new com.android.server.devicepolicy.PolicyDefinition<>(new android.app.admin.NoArgsPolicyKey("permittedInputMethods"), new com.android.server.devicepolicy.MostRecent(), 6, new com.android.internal.util.function.QuadFunction() { // from class: com.android.server.devicepolicy.PolicyDefinition$$ExternalSyntheticLambda5
        public final java.lang.Object apply(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3, java.lang.Object obj4) {
            return java.lang.Boolean.valueOf(com.android.server.devicepolicy.PolicyEnforcerCallbacks.noOp((java.util.Set) obj, (android.content.Context) obj2, (java.lang.Integer) obj3, (android.app.admin.PolicyKey) obj4));
        }
    }, new com.android.server.devicepolicy.PackageSetPolicySerializer());
    static com.android.server.devicepolicy.PolicyDefinition<java.lang.Boolean> SCREEN_CAPTURE_DISABLED = new com.android.server.devicepolicy.PolicyDefinition<>(new android.app.admin.NoArgsPolicyKey("screenCaptureDisabled"), TRUE_MORE_RESTRICTIVE, 4, new com.android.internal.util.function.QuadFunction() { // from class: com.android.server.devicepolicy.PolicyDefinition$$ExternalSyntheticLambda6
        public final java.lang.Object apply(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3, java.lang.Object obj4) {
            return java.lang.Boolean.valueOf(com.android.server.devicepolicy.PolicyEnforcerCallbacks.setScreenCaptureDisabled((java.lang.Boolean) obj, (android.content.Context) obj2, ((java.lang.Integer) obj3).intValue(), (android.app.admin.PolicyKey) obj4));
        }
    }, new com.android.server.devicepolicy.BooleanPolicySerializer());
    static com.android.server.devicepolicy.PolicyDefinition<java.lang.Boolean> PERSONAL_APPS_SUSPENDED = new com.android.server.devicepolicy.PolicyDefinition<>(new android.app.admin.NoArgsPolicyKey("personalAppsSuspended"), new com.android.server.devicepolicy.MostRecent(), 6, new com.android.internal.util.function.QuadFunction() { // from class: com.android.server.devicepolicy.PolicyDefinition$$ExternalSyntheticLambda7
        public final java.lang.Object apply(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3, java.lang.Object obj4) {
            return java.lang.Boolean.valueOf(com.android.server.devicepolicy.PolicyEnforcerCallbacks.setPersonalAppsSuspended((java.lang.Boolean) obj, (android.content.Context) obj2, ((java.lang.Integer) obj3).intValue(), (android.app.admin.PolicyKey) obj4));
        }
    }, new com.android.server.devicepolicy.BooleanPolicySerializer());
    static com.android.server.devicepolicy.PolicyDefinition<java.lang.Boolean> USB_DATA_SIGNALING = new com.android.server.devicepolicy.PolicyDefinition<>(new android.app.admin.NoArgsPolicyKey("usbDataSignaling"), FALSE_MORE_RESTRICTIVE, 1, new com.android.internal.util.function.QuadFunction() { // from class: com.android.server.devicepolicy.PolicyDefinition$$ExternalSyntheticLambda8
        public final java.lang.Object apply(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3, java.lang.Object obj4) {
            return java.lang.Boolean.valueOf(com.android.server.devicepolicy.PolicyEnforcerCallbacks.setUsbDataSignalingEnabled((java.lang.Boolean) obj, (android.content.Context) obj2));
        }
    }, new com.android.server.devicepolicy.BooleanPolicySerializer());
    static com.android.server.devicepolicy.PolicyDefinition<java.lang.Integer> CONTENT_PROTECTION = new com.android.server.devicepolicy.PolicyDefinition<>(new android.app.admin.NoArgsPolicyKey("contentProtection"), new com.android.server.devicepolicy.MostRecent(), 2, new com.android.internal.util.function.QuadFunction() { // from class: com.android.server.devicepolicy.PolicyDefinition$$ExternalSyntheticLambda9
        public final java.lang.Object apply(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3, java.lang.Object obj4) {
            return java.lang.Boolean.valueOf(com.android.server.devicepolicy.PolicyEnforcerCallbacks.setContentProtectionPolicy((java.lang.Integer) obj, (android.content.Context) obj2, (java.lang.Integer) obj3, (android.app.admin.PolicyKey) obj4));
        }
    }, new com.android.server.devicepolicy.IntegerPolicySerializer());
    static com.android.server.devicepolicy.PolicyDefinition<java.lang.Integer> PASSWORD_COMPLEXITY = new com.android.server.devicepolicy.PolicyDefinition<>(new android.app.admin.NoArgsPolicyKey("passwordComplexity"), new com.android.server.devicepolicy.MostRestrictive(java.util.List.of(new android.app.admin.IntegerPolicyValue(327680), new android.app.admin.IntegerPolicyValue(196608), new android.app.admin.IntegerPolicyValue(65536), new android.app.admin.IntegerPolicyValue(0))), 2, new com.android.internal.util.function.QuadFunction() { // from class: com.android.server.devicepolicy.PolicyDefinition$$ExternalSyntheticLambda2
        public final java.lang.Object apply(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3, java.lang.Object obj4) {
            return java.lang.Boolean.valueOf(com.android.server.devicepolicy.PolicyEnforcerCallbacks.noOp((java.lang.Integer) obj, (android.content.Context) obj2, (java.lang.Integer) obj3, (android.app.admin.PolicyKey) obj4));
        }
    }, new com.android.server.devicepolicy.IntegerPolicySerializer());
    static com.android.server.devicepolicy.PolicyDefinition<java.util.Set<java.lang.String>> PACKAGES_SUSPENDED = new com.android.server.devicepolicy.PolicyDefinition<>(new android.app.admin.NoArgsPolicyKey("packagesSuspended"), new com.android.server.devicepolicy.PackageSetUnion(), new com.android.internal.util.function.QuadFunction() { // from class: com.android.server.devicepolicy.PolicyDefinition$$ExternalSyntheticLambda5
        public final java.lang.Object apply(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3, java.lang.Object obj4) {
            return java.lang.Boolean.valueOf(com.android.server.devicepolicy.PolicyEnforcerCallbacks.noOp((java.util.Set) obj, (android.content.Context) obj2, (java.lang.Integer) obj3, (android.app.admin.PolicyKey) obj4));
        }
    }, new com.android.server.devicepolicy.PackageSetPolicySerializer());
    private static final java.util.Map<java.lang.String, com.android.server.devicepolicy.PolicyDefinition<?>> POLICY_DEFINITIONS = new java.util.HashMap();
    private static java.util.Map<java.lang.String, java.lang.Integer> USER_RESTRICTION_FLAGS = new java.util.HashMap();

    static {
        POLICY_DEFINITIONS.put("autoTimezone", AUTO_TIMEZONE);
        POLICY_DEFINITIONS.put("permissionGrant", GENERIC_PERMISSION_GRANT);
        POLICY_DEFINITIONS.put("securityLogging", SECURITY_LOGGING);
        POLICY_DEFINITIONS.put("auditLogging", AUDIT_LOGGING);
        POLICY_DEFINITIONS.put("lockTask", LOCK_TASK);
        POLICY_DEFINITIONS.put("userControlDisabledPackages", USER_CONTROLLED_DISABLED_PACKAGES);
        POLICY_DEFINITIONS.put("persistentPreferredActivity", GENERIC_PERSISTENT_PREFERRED_ACTIVITY);
        POLICY_DEFINITIONS.put("packageUninstallBlocked", GENERIC_PACKAGE_UNINSTALL_BLOCKED);
        POLICY_DEFINITIONS.put("applicationRestrictions", GENERIC_APPLICATION_RESTRICTIONS);
        POLICY_DEFINITIONS.put("resetPasswordToken", RESET_PASSWORD_TOKEN);
        POLICY_DEFINITIONS.put("keyguardDisabledFeatures", KEYGUARD_DISABLED_FEATURES);
        POLICY_DEFINITIONS.put("applicationHidden", GENERIC_APPLICATION_HIDDEN);
        POLICY_DEFINITIONS.put("accountManagementDisabled", GENERIC_ACCOUNT_MANAGEMENT_DISABLED);
        POLICY_DEFINITIONS.put("permittedInputMethods", PERMITTED_INPUT_METHODS);
        POLICY_DEFINITIONS.put("screenCaptureDisabled", SCREEN_CAPTURE_DISABLED);
        POLICY_DEFINITIONS.put("personalAppsSuspended", PERSONAL_APPS_SUSPENDED);
        POLICY_DEFINITIONS.put("usbDataSignaling", USB_DATA_SIGNALING);
        POLICY_DEFINITIONS.put("contentProtection", CONTENT_PROTECTION);
        POLICY_DEFINITIONS.put("passwordComplexity", PASSWORD_COMPLEXITY);
        POLICY_DEFINITIONS.put("packagesSuspended", PACKAGES_SUSPENDED);
        USER_RESTRICTION_FLAGS.put("no_modify_accounts", 0);
        USER_RESTRICTION_FLAGS.put("no_config_wifi", 0);
        USER_RESTRICTION_FLAGS.put("no_change_wifi_state", 1);
        USER_RESTRICTION_FLAGS.put("no_wifi_tethering", 1);
        USER_RESTRICTION_FLAGS.put("no_grant_admin", 0);
        USER_RESTRICTION_FLAGS.put("no_sharing_admin_configured_wifi", 0);
        USER_RESTRICTION_FLAGS.put("no_wifi_direct", 1);
        USER_RESTRICTION_FLAGS.put("no_add_wifi_config", 1);
        USER_RESTRICTION_FLAGS.put("no_config_locale", 0);
        USER_RESTRICTION_FLAGS.put("no_install_apps", 0);
        USER_RESTRICTION_FLAGS.put("no_uninstall_apps", 0);
        USER_RESTRICTION_FLAGS.put("no_share_location", 0);
        USER_RESTRICTION_FLAGS.put("no_airplane_mode", 1);
        USER_RESTRICTION_FLAGS.put("no_config_brightness", 0);
        USER_RESTRICTION_FLAGS.put("no_ambient_display", 0);
        USER_RESTRICTION_FLAGS.put("no_config_screen_timeout", 0);
        USER_RESTRICTION_FLAGS.put("no_install_unknown_sources", 0);
        USER_RESTRICTION_FLAGS.put("no_install_unknown_sources_globally", 1);
        USER_RESTRICTION_FLAGS.put("no_config_bluetooth", 0);
        USER_RESTRICTION_FLAGS.put("no_bluetooth", 0);
        USER_RESTRICTION_FLAGS.put("no_bluetooth_sharing", 0);
        USER_RESTRICTION_FLAGS.put("no_usb_file_transfer", 0);
        USER_RESTRICTION_FLAGS.put("no_config_credentials", 0);
        USER_RESTRICTION_FLAGS.put("no_remove_user", 0);
        USER_RESTRICTION_FLAGS.put("no_remove_managed_profile", 0);
        USER_RESTRICTION_FLAGS.put("no_debugging_features", 0);
        USER_RESTRICTION_FLAGS.put("no_config_vpn", 0);
        USER_RESTRICTION_FLAGS.put("no_config_location", 0);
        USER_RESTRICTION_FLAGS.put("no_config_date_time", 0);
        USER_RESTRICTION_FLAGS.put("no_config_tethering", 0);
        USER_RESTRICTION_FLAGS.put("no_network_reset", 0);
        USER_RESTRICTION_FLAGS.put("no_factory_reset", 0);
        USER_RESTRICTION_FLAGS.put("no_add_user", 0);
        USER_RESTRICTION_FLAGS.put("no_add_managed_profile", 0);
        USER_RESTRICTION_FLAGS.put("no_add_clone_profile", 0);
        USER_RESTRICTION_FLAGS.put("no_add_private_profile", 0);
        USER_RESTRICTION_FLAGS.put("ensure_verify_apps", 1);
        USER_RESTRICTION_FLAGS.put("no_config_cell_broadcasts", 0);
        USER_RESTRICTION_FLAGS.put("no_config_mobile_networks", 0);
        USER_RESTRICTION_FLAGS.put("no_control_apps", 0);
        USER_RESTRICTION_FLAGS.put("no_physical_media", 0);
        USER_RESTRICTION_FLAGS.put("no_unmute_microphone", 0);
        USER_RESTRICTION_FLAGS.put("no_adjust_volume", 0);
        USER_RESTRICTION_FLAGS.put("no_outgoing_calls", 0);
        USER_RESTRICTION_FLAGS.put("no_sms", 0);
        USER_RESTRICTION_FLAGS.put("no_fun", 0);
        USER_RESTRICTION_FLAGS.put("no_create_windows", 0);
        USER_RESTRICTION_FLAGS.put("no_system_error_dialogs", 0);
        USER_RESTRICTION_FLAGS.put("no_cross_profile_copy_paste", 0);
        USER_RESTRICTION_FLAGS.put("no_outgoing_beam", 0);
        USER_RESTRICTION_FLAGS.put("no_wallpaper", 0);
        USER_RESTRICTION_FLAGS.put("no_set_wallpaper", 0);
        USER_RESTRICTION_FLAGS.put("no_safe_boot", 0);
        USER_RESTRICTION_FLAGS.put("no_record_audio", 0);
        USER_RESTRICTION_FLAGS.put("no_run_in_background", 0);
        USER_RESTRICTION_FLAGS.put("no_camera", 0);
        USER_RESTRICTION_FLAGS.put("disallow_unmute_device", 0);
        USER_RESTRICTION_FLAGS.put("no_data_roaming", 0);
        USER_RESTRICTION_FLAGS.put("no_set_user_icon", 0);
        USER_RESTRICTION_FLAGS.put("no_oem_unlock", 0);
        USER_RESTRICTION_FLAGS.put("no_unified_password", 0);
        USER_RESTRICTION_FLAGS.put("allow_parent_profile_app_linking", 0);
        USER_RESTRICTION_FLAGS.put("no_autofill", 0);
        USER_RESTRICTION_FLAGS.put("no_content_capture", 0);
        USER_RESTRICTION_FLAGS.put("no_content_suggestions", 0);
        USER_RESTRICTION_FLAGS.put("no_user_switch", 1);
        USER_RESTRICTION_FLAGS.put("no_sharing_into_profile", 0);
        USER_RESTRICTION_FLAGS.put("no_printing", 0);
        USER_RESTRICTION_FLAGS.put("disallow_config_private_dns", 1);
        USER_RESTRICTION_FLAGS.put("disallow_microphone_toggle", 0);
        USER_RESTRICTION_FLAGS.put("disallow_camera_toggle", 0);
        USER_RESTRICTION_FLAGS.put("disallow_biometric", 0);
        USER_RESTRICTION_FLAGS.put("disallow_config_default_apps", 0);
        USER_RESTRICTION_FLAGS.put("no_cellular_2g", 1);
        USER_RESTRICTION_FLAGS.put("no_ultra_wideband_radio", 1);
        USER_RESTRICTION_FLAGS.put("no_sim_globally", 1);
        USER_RESTRICTION_FLAGS.put("no_assist_content", 0);
        if (com.android.internal.hidden_from_bootclasspath.com.android.net.thread.platform.flags.Flags.threadUserRestrictionEnabled()) {
            USER_RESTRICTION_FLAGS.put("no_thread_network", 1);
        }
        USER_RESTRICTION_FLAGS.put("no_assist_content", 0);
        for (java.lang.String key : USER_RESTRICTION_FLAGS.keySet()) {
            createAndAddUserRestrictionPolicyDefinition(key, USER_RESTRICTION_FLAGS.get(key).intValue());
        }
    }

    static com.android.server.devicepolicy.PolicyDefinition<java.lang.Integer> PERMISSION_GRANT(java.lang.String packageName, java.lang.String permissionName) {
        if (packageName == null || permissionName == null) {
            return GENERIC_PERMISSION_GRANT;
        }
        return GENERIC_PERMISSION_GRANT.createPolicyDefinition(new android.app.admin.PackagePermissionPolicyKey("permissionGrant", packageName, permissionName));
    }

    static com.android.server.devicepolicy.PolicyDefinition<android.content.ComponentName> PERSISTENT_PREFERRED_ACTIVITY(android.content.IntentFilter intentFilter) {
        if (intentFilter == null) {
            return GENERIC_PERSISTENT_PREFERRED_ACTIVITY;
        }
        return GENERIC_PERSISTENT_PREFERRED_ACTIVITY.createPolicyDefinition(new android.app.admin.IntentFilterPolicyKey("persistentPreferredActivity", intentFilter));
    }

    static com.android.server.devicepolicy.PolicyDefinition<java.lang.Boolean> PACKAGE_UNINSTALL_BLOCKED(java.lang.String packageName) {
        if (packageName == null) {
            return GENERIC_PACKAGE_UNINSTALL_BLOCKED;
        }
        return GENERIC_PACKAGE_UNINSTALL_BLOCKED.createPolicyDefinition(new android.app.admin.PackagePolicyKey("packageUninstallBlocked", packageName));
    }

    static com.android.server.devicepolicy.PolicyDefinition<android.os.Bundle> APPLICATION_RESTRICTIONS(java.lang.String packageName) {
        if (packageName == null) {
            return GENERIC_APPLICATION_RESTRICTIONS;
        }
        return GENERIC_APPLICATION_RESTRICTIONS.createPolicyDefinition(new android.app.admin.PackagePolicyKey("applicationRestrictions", packageName));
    }

    static com.android.server.devicepolicy.PolicyDefinition<java.lang.Boolean> APPLICATION_HIDDEN(java.lang.String packageName) {
        if (packageName == null) {
            return GENERIC_APPLICATION_HIDDEN;
        }
        return GENERIC_APPLICATION_HIDDEN.createPolicyDefinition(new android.app.admin.PackagePolicyKey("applicationHidden", packageName));
    }

    static com.android.server.devicepolicy.PolicyDefinition<java.lang.Boolean> ACCOUNT_MANAGEMENT_DISABLED(java.lang.String accountType) {
        if (accountType == null) {
            return GENERIC_ACCOUNT_MANAGEMENT_DISABLED;
        }
        return GENERIC_ACCOUNT_MANAGEMENT_DISABLED.createPolicyDefinition(new android.app.admin.AccountTypePolicyKey("accountManagementDisabled", accountType));
    }

    private com.android.server.devicepolicy.PolicyDefinition<V> createPolicyDefinition(android.app.admin.PolicyKey key) {
        return new com.android.server.devicepolicy.PolicyDefinition<>(key, this.mResolutionMechanism, this.mPolicyFlags, this.mPolicyEnforcerCallback, this.mPolicySerializer);
    }

    static com.android.server.devicepolicy.PolicyDefinition<java.lang.Boolean> getPolicyDefinitionForUserRestriction(java.lang.String restriction) {
        java.lang.String key = android.app.admin.DevicePolicyIdentifiers.getIdentifierForUserRestriction(restriction);
        if (!POLICY_DEFINITIONS.containsKey(key)) {
            throw new java.lang.IllegalArgumentException("Unsupported user restriction " + restriction);
        }
        return (com.android.server.devicepolicy.PolicyDefinition) POLICY_DEFINITIONS.get(key);
    }

    android.app.admin.PolicyKey getPolicyKey() {
        return this.mPolicyKey;
    }

    com.android.server.devicepolicy.ResolutionMechanism<V> getResolutionMechanism() {
        return this.mResolutionMechanism;
    }

    boolean isGlobalOnlyPolicy() {
        return (this.mPolicyFlags & 1) != 0;
    }

    boolean isLocalOnlyPolicy() {
        return (this.mPolicyFlags & 2) != 0;
    }

    boolean isInheritable() {
        return (this.mPolicyFlags & 4) != 0;
    }

    boolean isNonCoexistablePolicy() {
        return (this.mPolicyFlags & 8) != 0;
    }

    boolean isUserRestrictionPolicy() {
        return (this.mPolicyFlags & 16) != 0;
    }

    boolean shouldSkipEnforcementIfNotChanged() {
        return (this.mPolicyFlags & 32) != 0;
    }

    android.app.admin.PolicyValue<V> resolvePolicy(java.util.LinkedHashMap<com.android.server.devicepolicy.EnforcingAdmin, android.app.admin.PolicyValue<V>> adminsPolicy) {
        return this.mResolutionMechanism.resolve(adminsPolicy);
    }

    boolean enforcePolicy(V value, android.content.Context context, int userId) {
        return ((java.lang.Boolean) this.mPolicyEnforcerCallback.apply(value, context, java.lang.Integer.valueOf(userId), this.mPolicyKey)).booleanValue();
    }

    private static void createAndAddUserRestrictionPolicyDefinition(java.lang.String restriction, int flags) {
        java.lang.String identifier = android.app.admin.DevicePolicyIdentifiers.getIdentifierForUserRestriction(restriction);
        android.app.admin.UserRestrictionPolicyKey key = new android.app.admin.UserRestrictionPolicyKey(identifier, restriction);
        com.android.server.devicepolicy.PolicyDefinition<java.lang.Boolean> definition = new com.android.server.devicepolicy.PolicyDefinition<>(key, TRUE_MORE_RESTRICTIVE, flags | 20, new com.android.internal.util.function.QuadFunction() { // from class: com.android.server.devicepolicy.PolicyDefinition$$ExternalSyntheticLambda0
            public final java.lang.Object apply(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3, java.lang.Object obj4) {
                return java.lang.Boolean.valueOf(com.android.server.devicepolicy.PolicyEnforcerCallbacks.setUserRestriction((java.lang.Boolean) obj, (android.content.Context) obj2, ((java.lang.Integer) obj3).intValue(), (android.app.admin.PolicyKey) obj4));
            }
        }, new com.android.server.devicepolicy.BooleanPolicySerializer());
        POLICY_DEFINITIONS.put(key.getIdentifier(), definition);
    }

    private PolicyDefinition(android.app.admin.PolicyKey key, com.android.server.devicepolicy.ResolutionMechanism<V> resolutionMechanism, com.android.internal.util.function.QuadFunction<V, android.content.Context, java.lang.Integer, android.app.admin.PolicyKey, java.lang.Boolean> policyEnforcerCallback, com.android.server.devicepolicy.PolicySerializer<V> policySerializer) {
        this(key, resolutionMechanism, 0, policyEnforcerCallback, policySerializer);
    }

    private PolicyDefinition(android.app.admin.PolicyKey policyKey, com.android.server.devicepolicy.ResolutionMechanism<V> resolutionMechanism, int policyFlags, com.android.internal.util.function.QuadFunction<V, android.content.Context, java.lang.Integer, android.app.admin.PolicyKey, java.lang.Boolean> policyEnforcerCallback, com.android.server.devicepolicy.PolicySerializer<V> policySerializer) {
        java.util.Objects.requireNonNull(policyKey);
        this.mPolicyKey = policyKey;
        this.mResolutionMechanism = resolutionMechanism;
        this.mPolicyFlags = policyFlags;
        this.mPolicyEnforcerCallback = policyEnforcerCallback;
        this.mPolicySerializer = policySerializer;
        if (isNonCoexistablePolicy() && !isLocalOnlyPolicy()) {
            throw new java.lang.UnsupportedOperationException("Non-coexistable global policies not supported,please add support.");
        }
    }

    void saveToXml(com.android.modules.utils.TypedXmlSerializer serializer) throws java.io.IOException {
        this.mPolicyKey.saveToXml(serializer);
    }

    static <V> com.android.server.devicepolicy.PolicyDefinition<V> readFromXml(com.android.modules.utils.TypedXmlPullParser typedXmlPullParser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        android.app.admin.PolicyKey policyKeyFromXml = readPolicyKeyFromXml(typedXmlPullParser);
        if (policyKeyFromXml == null) {
            com.android.server.utils.Slogf.wtf(TAG, "Error parsing PolicyDefinition, PolicyKey is null.");
            return null;
        }
        com.android.server.devicepolicy.PolicyDefinition<?> policyDefinition = POLICY_DEFINITIONS.get(policyKeyFromXml.getIdentifier());
        if (policyDefinition == null) {
            com.android.server.utils.Slogf.wtf(TAG, "Unknown generic policy key: " + policyKeyFromXml);
            return null;
        }
        return (com.android.server.devicepolicy.PolicyDefinition<V>) policyDefinition.createPolicyDefinition(policyKeyFromXml);
    }

    static <V> android.app.admin.PolicyKey readPolicyKeyFromXml(com.android.modules.utils.TypedXmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        android.app.admin.PolicyKey policyKey = android.app.admin.PolicyKey.readGenericPolicyKeyFromXml(parser);
        if (policyKey == null) {
            com.android.server.utils.Slogf.wtf(TAG, "Error parsing PolicyKey, GenericPolicyKey is null");
            return null;
        }
        com.android.server.devicepolicy.PolicyDefinition<?> policyDefinition = POLICY_DEFINITIONS.get(policyKey.getIdentifier());
        if (policyDefinition == null) {
            com.android.server.utils.Slogf.wtf(TAG, "Error parsing PolicyKey, Unknown generic policy key: " + policyKey);
            return null;
        }
        return ((com.android.server.devicepolicy.PolicyDefinition) policyDefinition).mPolicyKey.readFromXml(parser);
    }

    void savePolicyValueToXml(com.android.modules.utils.TypedXmlSerializer serializer, V value) throws java.io.IOException {
        this.mPolicySerializer.saveToXml(serializer, value);
    }

    android.app.admin.PolicyValue<V> readPolicyValueFromXml(com.android.modules.utils.TypedXmlPullParser parser) {
        return this.mPolicySerializer.readFromXml(parser);
    }

    public java.lang.String toString() {
        return "PolicyDefinition{ mPolicyKey= " + this.mPolicyKey + ", mResolutionMechanism= " + this.mResolutionMechanism + ", mPolicyFlags= " + this.mPolicyFlags + " }";
    }
}
