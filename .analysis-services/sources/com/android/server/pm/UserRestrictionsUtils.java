package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public class UserRestrictionsUtils {
    private static final java.lang.String TAG = "UserRestrictionsUtils";
    public static final java.util.Set<java.lang.String> USER_RESTRICTIONS = newSetWithUniqueCheck(new java.lang.String[]{"no_config_wifi", "no_config_locale", "no_modify_accounts", "no_install_apps", "no_uninstall_apps", "no_share_location", "no_install_unknown_sources", "no_install_unknown_sources_globally", "no_config_bluetooth", "no_bluetooth", "no_bluetooth_sharing", "no_usb_file_transfer", "no_config_credentials", "no_remove_user", "no_remove_managed_profile", "no_debugging_features", "no_config_vpn", "no_config_date_time", "no_config_tethering", "no_network_reset", "no_factory_reset", "no_add_user", "no_add_managed_profile", "no_add_clone_profile", "no_add_private_profile", "ensure_verify_apps", "no_config_cell_broadcasts", "no_config_mobile_networks", "no_control_apps", "no_physical_media", "no_unmute_microphone", "no_adjust_volume", "no_outgoing_calls", "no_sms", "no_fun", "no_create_windows", "no_system_error_dialogs", "no_cross_profile_copy_paste", "no_outgoing_beam", "no_wallpaper", "no_safe_boot", "allow_parent_profile_app_linking", "no_record_audio", "no_camera", "no_run_in_background", "no_data_roaming", "no_set_user_icon", "no_set_wallpaper", "no_oem_unlock", "disallow_unmute_device", "no_autofill", "no_content_capture", "no_content_suggestions", "no_user_switch", "no_unified_password", "no_config_location", "no_airplane_mode", "no_config_brightness", "no_sharing_into_profile", "no_ambient_display", "no_config_screen_timeout", "no_printing", "disallow_config_private_dns", "disallow_microphone_toggle", "disallow_camera_toggle", "no_change_wifi_state", "no_wifi_tethering", "no_grant_admin", "no_sharing_admin_configured_wifi", "no_wifi_direct", "no_add_wifi_config", "no_cellular_2g", "no_ultra_wideband_radio", "disallow_config_default_apps", "no_near_field_communication_radio", "no_sim_globally", "no_assist_content", "no_thread_network", "no_change_near_field_communication_radio"});
    public static final java.util.Set<java.lang.String> DEPRECATED_USER_RESTRICTIONS = com.google.android.collect.Sets.newArraySet(new java.lang.String[]{"no_add_managed_profile", "no_remove_managed_profile"});
    private static final java.util.Set<java.lang.String> NON_PERSIST_USER_RESTRICTIONS = com.google.android.collect.Sets.newArraySet(new java.lang.String[]{"no_record_audio"});
    private static final java.util.Set<java.lang.String> MAIN_USER_ONLY_RESTRICTIONS = com.google.android.collect.Sets.newArraySet(new java.lang.String[]{"no_bluetooth", "no_usb_file_transfer", "no_config_tethering", "no_network_reset", "no_factory_reset", "no_add_user", "no_config_cell_broadcasts", "no_config_mobile_networks", "no_physical_media", "no_sms", "no_fun", "no_safe_boot", "no_create_windows", "no_data_roaming", "no_airplane_mode"});
    private static final java.util.Set<java.lang.String> DEVICE_OWNER_ONLY_RESTRICTIONS = com.google.android.collect.Sets.newArraySet(new java.lang.String[]{"no_user_switch", "disallow_config_private_dns", "disallow_microphone_toggle", "disallow_camera_toggle", "no_change_wifi_state", "no_wifi_tethering", "no_wifi_direct", "no_add_wifi_config", "no_cellular_2g", "no_ultra_wideband_radio", "no_near_field_communication_radio", "no_thread_network", "no_change_near_field_communication_radio"});
    private static final java.util.Set<java.lang.String> IMMUTABLE_BY_OWNERS = com.google.android.collect.Sets.newArraySet(new java.lang.String[]{"no_record_audio", "no_wallpaper", "no_oem_unlock"});
    private static final java.util.Set<java.lang.String> GLOBAL_RESTRICTIONS = com.google.android.collect.Sets.newArraySet(new java.lang.String[]{"no_adjust_volume", "no_bluetooth_sharing", "no_config_date_time", "no_system_error_dialogs", "no_run_in_background", "no_unmute_microphone", "disallow_unmute_device", "no_camera", "no_assist_content", "disallow_config_default_apps"});
    private static final java.util.Set<java.lang.String> PROFILE_OWNER_ORGANIZATION_OWNED_PARENT_GLOBAL_RESTRICTIONS = com.google.android.collect.Sets.newArraySet(new java.lang.String[]{"no_airplane_mode", "no_config_date_time", "disallow_config_private_dns", "no_change_wifi_state", "no_debugging_features", "no_wifi_tethering", "no_wifi_direct", "no_add_wifi_config", "no_cellular_2g", "no_ultra_wideband_radio", "no_near_field_communication_radio", "no_thread_network", "no_change_near_field_communication_radio"});
    private static final java.util.Set<java.lang.String> PROFILE_OWNER_ORGANIZATION_OWNED_PROFILE_RESTRICTIONS = com.google.android.collect.Sets.newArraySet(new java.lang.String[]{"no_sim_globally"});
    private static final java.util.Set<java.lang.String> PROFILE_OWNER_ORGANIZATION_OWNED_PARENT_LOCAL_RESTRICTIONS = com.google.android.collect.Sets.newArraySet(new java.lang.String[]{"no_config_bluetooth", "no_config_location", "no_config_wifi", "no_content_capture", "no_content_suggestions", "no_debugging_features", "no_share_location", "no_outgoing_calls", "no_camera", "no_bluetooth", "no_bluetooth_sharing", "no_config_cell_broadcasts", "no_config_mobile_networks", "no_config_tethering", "no_data_roaming", "no_safe_boot", "no_sms", "no_usb_file_transfer", "no_physical_media", "no_unmute_microphone", "disallow_config_default_apps", "no_add_private_profile", "no_config_brightness", "no_config_screen_timeout"});
    private static final java.util.Set<java.lang.String> DEFAULT_ENABLED_FOR_MANAGED_PROFILES = com.google.android.collect.Sets.newArraySet(new java.lang.String[]{"no_bluetooth_sharing", "no_debugging_features"});
    private static final java.util.Set<java.lang.String> PROFILE_GLOBAL_RESTRICTIONS = com.google.android.collect.Sets.newArraySet(new java.lang.String[]{"ensure_verify_apps", "no_airplane_mode", "no_install_unknown_sources_globally", "no_sim_globally"});
    private static final java.util.Set<java.lang.String> FINANCED_DEVICE_OWNER_RESTRICTIONS = com.google.android.collect.Sets.newArraySet(new java.lang.String[]{"no_add_user", "no_debugging_features", "no_install_unknown_sources", "no_safe_boot", "no_config_date_time", "no_outgoing_calls"});

    private UserRestrictionsUtils() {
    }

    private static java.util.Set<java.lang.String> newSetWithUniqueCheck(java.lang.String[] strings) {
        java.util.Set<java.lang.String> ret = com.google.android.collect.Sets.newArraySet(strings);
        com.android.internal.util.Preconditions.checkState(ret.size() == strings.length);
        return ret;
    }

    public static boolean isValidRestriction(java.lang.String restriction) {
        if (USER_RESTRICTIONS.contains(restriction)) {
            return true;
        }
        int uid = android.os.Binder.getCallingUid();
        java.lang.String[] pkgs = null;
        try {
            pkgs = android.app.AppGlobals.getPackageManager().getPackagesForUid(uid);
        } catch (android.os.RemoteException e) {
        }
        java.lang.StringBuilder msg = new java.lang.StringBuilder("Unknown restriction queried by uid ");
        msg.append(uid);
        if (pkgs != null && pkgs.length > 0) {
            msg.append(" (");
            msg.append(pkgs[0]);
            if (pkgs.length > 1) {
                msg.append(" et al");
            }
            msg.append(")");
        }
        msg.append(": ");
        msg.append(restriction);
        if (restriction == null || !isSystemApp(uid, pkgs)) {
            android.util.Slog.e(TAG, msg.toString());
        } else {
            android.util.Slog.wtf(TAG, msg.toString());
        }
        return false;
    }

    private static boolean isSystemApp(int uid, java.lang.String[] packageList) {
        android.content.pm.ApplicationInfo appInfo;
        if (android.os.UserHandle.isCore(uid)) {
            return true;
        }
        if (packageList == null) {
            return false;
        }
        android.content.pm.IPackageManager pm = android.app.AppGlobals.getPackageManager();
        for (java.lang.String str : packageList) {
            try {
                appInfo = pm.getApplicationInfo(str, 794624L, android.os.UserHandle.getUserId(uid));
            } catch (android.os.RemoteException e) {
            }
            if (appInfo != null && appInfo.isSystemApp()) {
                return true;
            }
        }
        return false;
    }

    public static void writeRestrictions(org.xmlpull.v1.XmlSerializer serializer, android.os.Bundle restrictions, java.lang.String tag) throws java.io.IOException {
        writeRestrictions(com.android.internal.util.XmlUtils.makeTyped(serializer), restrictions, tag);
    }

    public static void writeRestrictions(com.android.modules.utils.TypedXmlSerializer serializer, android.os.Bundle restrictions, java.lang.String tag) throws java.io.IOException {
        if (restrictions == null) {
            return;
        }
        serializer.startTag((java.lang.String) null, tag);
        for (java.lang.String key : restrictions.keySet()) {
            if (!NON_PERSIST_USER_RESTRICTIONS.contains(key)) {
                if (USER_RESTRICTIONS.contains(key)) {
                    if (restrictions.getBoolean(key)) {
                        serializer.attributeBoolean((java.lang.String) null, key, true);
                    }
                } else {
                    android.util.Log.w(TAG, "Unknown user restriction detected: " + key);
                }
            }
        }
        serializer.endTag((java.lang.String) null, tag);
    }

    public static void readRestrictions(org.xmlpull.v1.XmlPullParser parser, android.os.Bundle restrictions) {
        readRestrictions(com.android.internal.util.XmlUtils.makeTyped(parser), restrictions);
    }

    public static void readRestrictions(com.android.modules.utils.TypedXmlPullParser parser, android.os.Bundle restrictions) {
        restrictions.clear();
        for (java.lang.String key : USER_RESTRICTIONS) {
            boolean value = parser.getAttributeBoolean((java.lang.String) null, key, false);
            if (value) {
                restrictions.putBoolean(key, true);
            }
        }
    }

    public static android.os.Bundle readRestrictions(org.xmlpull.v1.XmlPullParser parser) {
        return readRestrictions(com.android.internal.util.XmlUtils.makeTyped(parser));
    }

    public static android.os.Bundle readRestrictions(com.android.modules.utils.TypedXmlPullParser parser) {
        android.os.Bundle result = new android.os.Bundle();
        readRestrictions(parser, result);
        return result;
    }

    public static android.os.Bundle nonNull(android.os.Bundle in) {
        return in != null ? in : new android.os.Bundle();
    }

    public static boolean contains(android.os.Bundle in, java.lang.String restriction) {
        return in != null && in.getBoolean(restriction);
    }

    public static void merge(android.os.Bundle dest, android.os.Bundle in) {
        java.util.Objects.requireNonNull(dest);
        com.android.internal.util.Preconditions.checkArgument(dest != in);
        if (in == null) {
            return;
        }
        for (java.lang.String key : in.keySet()) {
            if (in.getBoolean(key, false)) {
                dest.putBoolean(key, true);
            }
        }
    }

    public static boolean canDeviceOwnerChange(java.lang.String restriction) {
        return !IMMUTABLE_BY_OWNERS.contains(restriction);
    }

    public static boolean canProfileOwnerChange(java.lang.String restriction, boolean isMainUser, boolean isProfileOwnerOnOrgOwnedDevice) {
        if (!android.app.admin.flags.Flags.esimManagementEnabled()) {
            return (IMMUTABLE_BY_OWNERS.contains(restriction) || DEVICE_OWNER_ONLY_RESTRICTIONS.contains(restriction) || (!isMainUser && MAIN_USER_ONLY_RESTRICTIONS.contains(restriction))) ? false : true;
        }
        if (IMMUTABLE_BY_OWNERS.contains(restriction) || DEVICE_OWNER_ONLY_RESTRICTIONS.contains(restriction)) {
            return false;
        }
        if (isMainUser || !MAIN_USER_ONLY_RESTRICTIONS.contains(restriction)) {
            return isProfileOwnerOnOrgOwnedDevice || !PROFILE_OWNER_ORGANIZATION_OWNED_PROFILE_RESTRICTIONS.contains(restriction);
        }
        return false;
    }

    public static boolean canParentOfProfileOwnerOfOrganizationOwnedDeviceChange(java.lang.String restriction) {
        return PROFILE_OWNER_ORGANIZATION_OWNED_PARENT_GLOBAL_RESTRICTIONS.contains(restriction) || PROFILE_OWNER_ORGANIZATION_OWNED_PARENT_LOCAL_RESTRICTIONS.contains(restriction);
    }

    public static java.util.Set<java.lang.String> getDefaultEnabledForManagedProfiles() {
        return DEFAULT_ENABLED_FOR_MANAGED_PROFILES;
    }

    public static boolean canFinancedDeviceOwnerChange(java.lang.String restriction) {
        return FINANCED_DEVICE_OWNER_RESTRICTIONS.contains(restriction) && canDeviceOwnerChange(restriction);
    }

    public static boolean isGlobal(int restrictionOwnerType, java.lang.String key) {
        return (restrictionOwnerType == 0 && (MAIN_USER_ONLY_RESTRICTIONS.contains(key) || GLOBAL_RESTRICTIONS.contains(key))) || (restrictionOwnerType == 2 && PROFILE_OWNER_ORGANIZATION_OWNED_PARENT_GLOBAL_RESTRICTIONS.contains(key)) || PROFILE_GLOBAL_RESTRICTIONS.contains(key) || DEVICE_OWNER_ONLY_RESTRICTIONS.contains(key);
    }

    public static boolean isLocal(int restrictionOwnerType, java.lang.String key) {
        return !isGlobal(restrictionOwnerType, key);
    }

    public static boolean areEqual(android.os.Bundle a, android.os.Bundle b) {
        if (a == b) {
            return true;
        }
        if (com.android.server.BundleUtils.isEmpty(a)) {
            return com.android.server.BundleUtils.isEmpty(b);
        }
        if (com.android.server.BundleUtils.isEmpty(b)) {
            return false;
        }
        for (java.lang.String key : a.keySet()) {
            if (a.getBoolean(key) != b.getBoolean(key)) {
                return false;
            }
        }
        for (java.lang.String key2 : b.keySet()) {
            if (a.getBoolean(key2) != b.getBoolean(key2)) {
                return false;
            }
        }
        return true;
    }

    public static void applyUserRestrictions(android.content.Context context, int userId, android.os.Bundle newRestrictions, android.os.Bundle prevRestrictions) {
        for (java.lang.String key : USER_RESTRICTIONS) {
            boolean newValue = newRestrictions.getBoolean(key);
            boolean prevValue = prevRestrictions.getBoolean(key);
            if (newValue != prevValue) {
                applyUserRestriction(context, userId, key, newValue);
            }
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:43:0x009e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static void applyUserRestriction(android.content.Context r11, int r12, java.lang.String r13, boolean r14) {
        /*
            Method dump skipped, instruction units count: 510
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.UserRestrictionsUtils.applyUserRestriction(android.content.Context, int, java.lang.String, boolean):void");
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:74:0x0123  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static boolean isSettingRestrictedForUser(android.content.Context r8, java.lang.String r9, int r10, java.lang.String r11, int r12) {
        /*
            Method dump skipped, instruction units count: 630
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.UserRestrictionsUtils.isSettingRestrictedForUser(android.content.Context, java.lang.String, int, java.lang.String, int):boolean");
    }

    public static void dumpRestrictions(java.io.PrintWriter pw, java.lang.String prefix, android.os.Bundle restrictions) {
        boolean noneSet = true;
        if (restrictions != null) {
            for (java.lang.String key : restrictions.keySet()) {
                if (restrictions.getBoolean(key, false)) {
                    pw.println(prefix + key);
                    noneSet = false;
                }
            }
            if (noneSet) {
                pw.println(prefix + "none");
                return;
            }
            return;
        }
        pw.println(prefix + "null");
    }

    public static void moveRestriction(java.lang.String restrictionKey, android.util.SparseArray<com.android.server.pm.RestrictionsSet> sourceRestrictionsSets, com.android.server.pm.RestrictionsSet destRestrictionSet) {
        for (int i = 0; i < sourceRestrictionsSets.size(); i++) {
            com.android.server.pm.RestrictionsSet sourceRestrictionsSet = sourceRestrictionsSets.valueAt(i);
            sourceRestrictionsSet.moveRestriction(destRestrictionSet, restrictionKey);
        }
    }

    public static boolean restrictionsChanged(android.os.Bundle oldRestrictions, android.os.Bundle newRestrictions, java.lang.String... restrictions) {
        if (restrictions.length == 0) {
            return areEqual(oldRestrictions, newRestrictions);
        }
        for (java.lang.String restriction : restrictions) {
            if (oldRestrictions.getBoolean(restriction, false) != newRestrictions.getBoolean(restriction, false)) {
                return true;
            }
        }
        return false;
    }

    private static void setInstallMarketAppsRestriction(android.content.ContentResolver cr, int userId, int settingValue) {
        android.provider.Settings.Secure.putIntForUser(cr, "install_non_market_apps", settingValue, userId);
    }

    private static int getNewUserRestrictionSetting(android.content.Context context, int userId, java.lang.String userRestriction, boolean newValue) {
        return (newValue || android.os.UserManager.get(context).hasUserRestriction(userRestriction, android.os.UserHandle.of(userId))) ? 0 : 1;
    }
}
