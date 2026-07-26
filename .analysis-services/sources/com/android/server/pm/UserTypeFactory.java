package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public final class UserTypeFactory {
    private static final java.lang.String LOG_TAG = "UserTypeFactory";

    private UserTypeFactory() {
    }

    public static android.util.ArrayMap<java.lang.String, com.android.server.pm.UserTypeDetails> getUserTypes() {
        android.util.ArrayMap<java.lang.String, com.android.server.pm.UserTypeDetails.Builder> builders = getDefaultBuilders();
        android.content.res.XmlResourceParser parser = android.content.res.Resources.getSystem().getXml(android.R.xml.config_user_types);
        try {
            customizeBuilders(builders, parser);
            if (parser != null) {
                parser.close();
            }
            android.util.ArrayMap<java.lang.String, com.android.server.pm.UserTypeDetails> types = new android.util.ArrayMap<>(builders.size());
            for (int i = 0; i < builders.size(); i++) {
                types.put(builders.keyAt(i), builders.valueAt(i).createUserTypeDetails());
            }
            return types;
        } catch (java.lang.Throwable th) {
            if (parser != null) {
                try {
                    parser.close();
                } catch (java.lang.Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    private static android.util.ArrayMap<java.lang.String, com.android.server.pm.UserTypeDetails.Builder> getDefaultBuilders() {
        android.util.ArrayMap<java.lang.String, com.android.server.pm.UserTypeDetails.Builder> builders = new android.util.ArrayMap<>();
        builders.put("android.os.usertype.profile.MANAGED", getDefaultTypeProfileManaged());
        builders.put("android.os.usertype.full.SYSTEM", getDefaultTypeFullSystem());
        builders.put("android.os.usertype.full.SECONDARY", getDefaultTypeFullSecondary());
        builders.put("android.os.usertype.full.GUEST", getDefaultTypeFullGuest());
        builders.put("android.os.usertype.full.DEMO", getDefaultTypeFullDemo());
        builders.put("android.os.usertype.full.RESTRICTED", getDefaultTypeFullRestricted());
        builders.put("android.os.usertype.system.HEADLESS", getDefaultTypeSystemHeadless());
        builders.put("android.os.usertype.profile.CLONE", getDefaultTypeProfileClone());
        builders.put("android.os.usertype.profile.COMMUNAL", getDefaultTypeProfileCommunal());
        builders.put("android.os.usertype.profile.PRIVATE", getDefaultTypeProfilePrivate());
        if (android.os.Build.IS_DEBUGGABLE) {
            builders.put("android.os.usertype.profile.TEST", getDefaultTypeProfileTest());
        }
        return builders;
    }

    private static com.android.server.pm.UserTypeDetails.Builder getDefaultTypeProfileClone() {
        return new com.android.server.pm.UserTypeDetails.Builder().setName("android.os.usertype.profile.CLONE").setBaseType(4096).setMaxAllowedPerParent(1).setLabels(android.R.string.policylab_expirePassword).setIconBadge(android.R.drawable.ic_audio_ring_notif).setBadgePlain(android.R.drawable.ic_audio_notification_mute_am_alpha).setBadgeNoBackground(android.R.drawable.ic_audio_notification_mute_am_alpha).setStatusBarIcon(0).setBadgeLabels(android.R.string.common_name_suffixes).setBadgeColors(android.R.color.system_neutral2_800).setDarkThemeBadgeColors(android.R.color.system_neutral2_900).setAccessibilityString(android.R.string.accessibility_autoclick_scroll_up).setDefaultRestrictions(null).setDefaultCrossProfileIntentFilters(getDefaultCloneCrossProfileIntentFilter()).setDefaultSecureSettings(getDefaultNonManagedProfileSecureSettings()).setDefaultUserProperties(new android.content.pm.UserProperties.Builder().setStartWithParent(true).setShowInLauncher(0).setShowInSettings(0).setInheritDevicePolicy(1).setUseParentsContacts(true).setUpdateCrossProfileIntentFiltersOnOTA(true).setCrossProfileIntentFilterAccessControl(10).setCrossProfileIntentResolutionStrategy(1).setShowInQuietMode(2).setShowInSharingSurfaces(0).setMediaSharedWithParent(true).setCredentialShareableWithParent(true).setDeleteAppWithParent(true).setCrossProfileContentSharingStrategy(1));
    }

    private static com.android.server.pm.UserTypeDetails.Builder getDefaultTypeProfileManaged() {
        return new com.android.server.pm.UserTypeDetails.Builder().setName("android.os.usertype.profile.MANAGED").setBaseType(4096).setDefaultUserInfoPropertyFlags(32).setMaxAllowedPerParent(1).setLabels(android.R.string.policylab_setGlobalProxy, android.R.string.policylab_watchLogin, android.R.string.policylab_wipeData).setIconBadge(android.R.drawable.ic_btn_square_browser_zoom_page_overview_disabled).setBadgePlain(android.R.drawable.ic_btn_search_go).setBadgeNoBackground(android.R.drawable.ic_btn_square_browser_zoom_fit_page_disabled).setStatusBarIcon(android.R.drawable.spinner_ab_pressed_holo_dark_am).setBadgeLabels(android.R.string.lockscreen_screen_locked, android.R.string.lockscreen_sim_locked_message, android.R.string.lockscreen_sim_puk_locked_instructions).setBadgeColors(android.R.color.materialColorTertiaryContainer, android.R.color.materialColorTertiaryFixedDim, android.R.color.materialColorTextPrimaryInverse).setDarkThemeBadgeColors(android.R.color.materialColorTertiaryFixed, android.R.color.materialColorTextHintInverse, android.R.color.materialColorTextPrimaryInverseDisableOnly).setAccessibilityString(android.R.string.accessibility_binding_label).setDefaultRestrictions(getDefaultProfileRestrictions()).setDefaultSecureSettings(getDefaultManagedProfileSecureSettings()).setDefaultCrossProfileIntentFilters(getDefaultManagedCrossProfileIntentFilter()).setDefaultUserProperties(new android.content.pm.UserProperties.Builder().setStartWithParent(true).setShowInLauncher(1).setShowInSettings(1).setShowInQuietMode(0).setShowInSharingSurfaces(1).setAuthAlwaysRequiredToDisableQuietMode(false).setCredentialShareableWithParent(true));
    }

    private static com.android.server.pm.UserTypeDetails.Builder getDefaultTypeProfileTest() {
        android.os.Bundle restrictions = getDefaultProfileRestrictions();
        restrictions.putBoolean("no_fun", true);
        return new com.android.server.pm.UserTypeDetails.Builder().setName("android.os.usertype.profile.TEST").setBaseType(4096).setMaxAllowedPerParent(2).setLabels(android.R.string.policylab_resetPassword, android.R.string.policylab_resetPassword, android.R.string.policylab_resetPassword).setIconBadge(android.R.drawable.ic_print).setBadgePlain(android.R.drawable.ic_popup_sync_5).setBadgeNoBackground(android.R.drawable.ic_popup_sync_6).setStatusBarIcon(android.R.drawable.ic_popup_sync_5).setBadgeLabels(android.R.string.lockscreen_screen_locked, android.R.string.lockscreen_sim_locked_message, android.R.string.lockscreen_sim_puk_locked_instructions).setBadgeColors(android.R.color.materialColorTertiaryContainer, android.R.color.materialColorTertiaryFixedDim, android.R.color.materialColorTextPrimaryInverse).setDarkThemeBadgeColors(android.R.color.materialColorTertiaryFixed, android.R.color.materialColorTextHintInverse, android.R.color.materialColorTextPrimaryInverseDisableOnly).setDefaultRestrictions(restrictions).setDefaultSecureSettings(getDefaultNonManagedProfileSecureSettings());
    }

    private static com.android.server.pm.UserTypeDetails.Builder getDefaultTypeProfileCommunal() {
        return new com.android.server.pm.UserTypeDetails.Builder().setName("android.os.usertype.profile.COMMUNAL").setBaseType(4096).setMaxAllowed(1).setEnabled(android.os.UserManager.isCommunalProfileEnabled() ? 1 : 0).setLabels(android.R.string.policylab_forceLock).setIconBadge(android.R.drawable.ic_print).setBadgePlain(android.R.drawable.ic_popup_sync_5).setBadgeNoBackground(android.R.drawable.ic_popup_sync_6).setStatusBarIcon(android.R.drawable.ic_popup_sync_5).setBadgeLabels(android.R.string.lockscreen_screen_locked, android.R.string.lockscreen_sim_locked_message, android.R.string.lockscreen_sim_puk_locked_instructions).setBadgeColors(android.R.color.materialColorTertiaryContainer, android.R.color.materialColorTertiaryFixedDim, android.R.color.materialColorTextPrimaryInverse).setDarkThemeBadgeColors(android.R.color.materialColorTertiaryFixed, android.R.color.materialColorTextHintInverse, android.R.color.materialColorTextPrimaryInverseDisableOnly).setDefaultRestrictions(getDefaultProfileRestrictions()).setDefaultSecureSettings(getDefaultNonManagedProfileSecureSettings()).setDefaultUserProperties(new android.content.pm.UserProperties.Builder().setStartWithParent(false).setShowInLauncher(1).setShowInSettings(1).setCredentialShareableWithParent(false).setAlwaysVisible(true));
    }

    private static com.android.server.pm.UserTypeDetails.Builder getDefaultTypeProfilePrivate() {
        return new com.android.server.pm.UserTypeDetails.Builder().setName("android.os.usertype.profile.PRIVATE").setBaseType(4096).setMaxAllowedPerParent(1).setEnabled(android.os.UserManager.isPrivateProfileEnabled() ? 1 : 0).setLabels(android.R.string.policylab_limitPassword).setIconBadge(android.R.drawable.ic_menu_share_holo_light).setBadgePlain(android.R.drawable.ic_menu_share_holo_dark).setBadgeNoBackground(android.R.drawable.ic_menu_share_holo_dark).setStatusBarIcon(android.R.drawable.spinner_ab_pressed_holo_light).setBadgeLabels(android.R.string.policydesc_setGlobalProxy).setBadgeColors(android.R.color.black).setDarkThemeBadgeColors(android.R.color.white).setAccessibilityString(android.R.string.accessibility_button_instructional_text).setDefaultRestrictions(getDefaultPrivateProfileRestrictions()).setDefaultCrossProfileIntentFilters(getDefaultPrivateCrossProfileIntentFilter()).setDefaultUserProperties(new android.content.pm.UserProperties.Builder().setStartWithParent(true).setCredentialShareableWithParent(true).setAuthAlwaysRequiredToDisableQuietMode(true).setAllowStoppingUserWithDelayedLocking(true).setMediaSharedWithParent(false).setShowInLauncher(1).setShowInSettings(1).setShowInQuietMode(1).setShowInSharingSurfaces(1).setCrossProfileIntentFilterAccessControl(10).setInheritDevicePolicy(1).setCrossProfileContentSharingStrategy(1).setProfileApiVisibility(1).setItemsRestrictedOnHomeScreen(true).setUpdateCrossProfileIntentFiltersOnOTA(true));
    }

    private static com.android.server.pm.UserTypeDetails.Builder getDefaultTypeFullSecondary() {
        return new com.android.server.pm.UserTypeDetails.Builder().setName("android.os.usertype.full.SECONDARY").setBaseType(1024).setMaxAllowed(-1).setDefaultRestrictions(getDefaultSecondaryUserRestrictions());
    }

    private static com.android.server.pm.UserTypeDetails.Builder getDefaultTypeFullGuest() {
        boolean ephemeralGuests = android.content.res.Resources.getSystem().getBoolean(android.R.bool.config_expandLockScreenUserSwitcher);
        int flags = (ephemeralGuests ? 256 : 0) | 4;
        return new com.android.server.pm.UserTypeDetails.Builder().setName("android.os.usertype.full.GUEST").setBaseType(1024).setDefaultUserInfoPropertyFlags(flags).setMaxAllowed(1).setDefaultRestrictions(getDefaultGuestUserRestrictions());
    }

    private static com.android.server.pm.UserTypeDetails.Builder getDefaultTypeFullDemo() {
        return new com.android.server.pm.UserTypeDetails.Builder().setName("android.os.usertype.full.DEMO").setBaseType(1024).setDefaultUserInfoPropertyFlags(512).setMaxAllowed(-1).setDefaultRestrictions(null);
    }

    private static com.android.server.pm.UserTypeDetails.Builder getDefaultTypeFullRestricted() {
        return new com.android.server.pm.UserTypeDetails.Builder().setName("android.os.usertype.full.RESTRICTED").setBaseType(1024).setDefaultUserInfoPropertyFlags(8).setMaxAllowed(-1).setDefaultRestrictions(null);
    }

    private static com.android.server.pm.UserTypeDetails.Builder getDefaultTypeFullSystem() {
        return new com.android.server.pm.UserTypeDetails.Builder().setName("android.os.usertype.full.SYSTEM").setBaseType(3072).setDefaultUserInfoPropertyFlags(16387).setMaxAllowed(1);
    }

    private static com.android.server.pm.UserTypeDetails.Builder getDefaultTypeSystemHeadless() {
        return new com.android.server.pm.UserTypeDetails.Builder().setName("android.os.usertype.system.HEADLESS").setBaseType(2048).setDefaultUserInfoPropertyFlags(3).setMaxAllowed(1);
    }

    private static android.os.Bundle getDefaultSecondaryUserRestrictions() {
        android.os.Bundle restrictions = new android.os.Bundle();
        restrictions.putBoolean("no_outgoing_calls", true);
        restrictions.putBoolean("no_sms", true);
        return restrictions;
    }

    private static android.os.Bundle getDefaultGuestUserRestrictions() {
        android.os.Bundle restrictions = getDefaultSecondaryUserRestrictions();
        restrictions.putBoolean("no_config_wifi", true);
        restrictions.putBoolean("no_install_unknown_sources", true);
        restrictions.putBoolean("no_config_credentials", true);
        return restrictions;
    }

    private static android.os.Bundle getDefaultProfileRestrictions() {
        android.os.Bundle restrictions = new android.os.Bundle();
        restrictions.putBoolean("no_wallpaper", true);
        return restrictions;
    }

    static android.os.Bundle getDefaultPrivateProfileRestrictions() {
        android.os.Bundle restrictions = getDefaultProfileRestrictions();
        restrictions.putBoolean("no_bluetooth_sharing", true);
        return restrictions;
    }

    private static android.os.Bundle getDefaultManagedProfileSecureSettings() {
        android.os.Bundle settings = new android.os.Bundle();
        settings.putString("managed_profile_contact_remote_search", "1");
        settings.putString("cross_profile_calendar_enabled", "1");
        return settings;
    }

    private static java.util.List<com.android.server.pm.DefaultCrossProfileIntentFilter> getDefaultManagedCrossProfileIntentFilter() {
        return com.android.server.pm.DefaultCrossProfileIntentFiltersUtils.getDefaultManagedProfileFilters();
    }

    private static java.util.List<com.android.server.pm.DefaultCrossProfileIntentFilter> getDefaultCloneCrossProfileIntentFilter() {
        return com.android.server.pm.DefaultCrossProfileIntentFiltersUtils.getDefaultCloneProfileFilters();
    }

    private static java.util.List<com.android.server.pm.DefaultCrossProfileIntentFilter> getDefaultPrivateCrossProfileIntentFilter() {
        return com.android.server.pm.DefaultCrossProfileIntentFiltersUtils.getDefaultPrivateProfileFilters();
    }

    private static android.os.Bundle getDefaultNonManagedProfileSecureSettings() {
        android.os.Bundle settings = new android.os.Bundle();
        settings.putString("user_setup_complete", "1");
        return settings;
    }

    static void customizeBuilders(android.util.ArrayMap<java.lang.String, com.android.server.pm.UserTypeDetails.Builder> builders, android.content.res.XmlResourceParser parser) {
        boolean isProfile;
        final com.android.server.pm.UserTypeDetails.Builder builder;
        try {
            com.android.internal.util.XmlUtils.beginDocument(parser, "user-types");
            com.android.internal.util.XmlUtils.nextElement(parser);
            while (true) {
                boolean isValid = true;
                if (parser.getEventType() != 1) {
                    java.lang.String elementName = parser.getName();
                    if ("profile-type".equals(elementName)) {
                        isProfile = true;
                    } else if ("full-type".equals(elementName)) {
                        isProfile = false;
                    } else {
                        if ("change-user-type".equals(elementName)) {
                            com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                        } else {
                            android.util.Slog.w(LOG_TAG, "Skipping unknown element " + elementName + " in " + parser.getPositionDescription());
                            com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                        }
                        com.android.internal.util.XmlUtils.nextElement(parser);
                    }
                    java.lang.String typeName = parser.getAttributeValue(null, "name");
                    if (typeName == null || typeName.equals("")) {
                        android.util.Slog.w(LOG_TAG, "Skipping user type with no name in " + parser.getPositionDescription());
                        com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                    } else {
                        java.lang.String typeName2 = typeName.intern();
                        if (typeName2.startsWith("android.")) {
                            android.util.Slog.i(LOG_TAG, "Customizing user type " + typeName2);
                            builder = builders.get(typeName2);
                            if (builder == null) {
                                throw new java.lang.IllegalArgumentException("Illegal custom user type name " + typeName2 + ": Non-AOSP user types cannot start with 'android.'");
                            }
                            if ((!isProfile || builder.getBaseType() != 4096) && (isProfile || builder.getBaseType() != 1024)) {
                                isValid = false;
                            }
                            if (!isValid) {
                                throw new java.lang.IllegalArgumentException("Wrong base type to customize user type (" + typeName2 + "), which is type " + android.content.pm.UserInfo.flagsToString(builder.getBaseType()));
                            }
                        } else if (isProfile) {
                            android.util.Slog.i(LOG_TAG, "Creating custom user type " + typeName2);
                            builder = new com.android.server.pm.UserTypeDetails.Builder();
                            builder.setName(typeName2);
                            builder.setBaseType(4096);
                            builders.put(typeName2, builder);
                        } else {
                            throw new java.lang.IllegalArgumentException("Creation of non-profile user type (" + typeName2 + ") is not currently supported.");
                        }
                        if (isProfile) {
                            java.util.Objects.requireNonNull(builder);
                            setIntAttribute(parser, "max-allowed-per-parent", new java.util.function.Consumer() { // from class: com.android.server.pm.UserTypeFactory$$ExternalSyntheticLambda0
                                @Override // java.util.function.Consumer
                                public final void accept(java.lang.Object obj) {
                                    builder.setMaxAllowedPerParent(((java.lang.Integer) obj).intValue());
                                }
                            });
                            java.util.Objects.requireNonNull(builder);
                            setResAttribute(parser, "icon-badge", new java.util.function.Consumer() { // from class: com.android.server.pm.UserTypeFactory$$ExternalSyntheticLambda1
                                @Override // java.util.function.Consumer
                                public final void accept(java.lang.Object obj) {
                                    builder.setIconBadge(((java.lang.Integer) obj).intValue());
                                }
                            });
                            java.util.Objects.requireNonNull(builder);
                            setResAttribute(parser, "badge-plain", new java.util.function.Consumer() { // from class: com.android.server.pm.UserTypeFactory$$ExternalSyntheticLambda2
                                @Override // java.util.function.Consumer
                                public final void accept(java.lang.Object obj) {
                                    builder.setBadgePlain(((java.lang.Integer) obj).intValue());
                                }
                            });
                            java.util.Objects.requireNonNull(builder);
                            setResAttribute(parser, "badge-no-background", new java.util.function.Consumer() { // from class: com.android.server.pm.UserTypeFactory$$ExternalSyntheticLambda3
                                @Override // java.util.function.Consumer
                                public final void accept(java.lang.Object obj) {
                                    builder.setBadgeNoBackground(((java.lang.Integer) obj).intValue());
                                }
                            });
                            java.util.Objects.requireNonNull(builder);
                            setResAttribute(parser, "status-bar-icon", new java.util.function.Consumer() { // from class: com.android.server.pm.UserTypeFactory$$ExternalSyntheticLambda4
                                @Override // java.util.function.Consumer
                                public final void accept(java.lang.Object obj) {
                                    builder.setStatusBarIcon(((java.lang.Integer) obj).intValue());
                                }
                            });
                        }
                        java.util.Objects.requireNonNull(builder);
                        setIntAttribute(parser, com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_ENABLED, new java.util.function.Consumer() { // from class: com.android.server.pm.UserTypeFactory$$ExternalSyntheticLambda5
                            @Override // java.util.function.Consumer
                            public final void accept(java.lang.Object obj) {
                                builder.setEnabled(((java.lang.Integer) obj).intValue());
                            }
                        });
                        java.util.Objects.requireNonNull(builder);
                        setIntAttribute(parser, "max-allowed", new java.util.function.Consumer() { // from class: com.android.server.pm.UserTypeFactory$$ExternalSyntheticLambda6
                            @Override // java.util.function.Consumer
                            public final void accept(java.lang.Object obj) {
                                builder.setMaxAllowed(((java.lang.Integer) obj).intValue());
                            }
                        });
                        int depth = parser.getDepth();
                        while (com.android.internal.util.XmlUtils.nextElementWithin(parser, depth)) {
                            java.lang.String childName = parser.getName();
                            if ("default-restrictions".equals(childName)) {
                                android.os.Bundle restrictions = com.android.server.pm.UserRestrictionsUtils.readRestrictions(com.android.internal.util.XmlUtils.makeTyped(parser));
                                builder.setDefaultRestrictions(restrictions);
                            } else if (isProfile && "badge-labels".equals(childName)) {
                                java.util.Objects.requireNonNull(builder);
                                setResAttributeArray(parser, new java.util.function.Consumer() { // from class: com.android.server.pm.UserTypeFactory$$ExternalSyntheticLambda7
                                    @Override // java.util.function.Consumer
                                    public final void accept(java.lang.Object obj) {
                                        builder.setBadgeLabels((int[]) obj);
                                    }
                                });
                            } else if (isProfile && "badge-colors".equals(childName)) {
                                java.util.Objects.requireNonNull(builder);
                                setResAttributeArray(parser, new java.util.function.Consumer() { // from class: com.android.server.pm.UserTypeFactory$$ExternalSyntheticLambda8
                                    @Override // java.util.function.Consumer
                                    public final void accept(java.lang.Object obj) {
                                        builder.setBadgeColors((int[]) obj);
                                    }
                                });
                            } else if (isProfile && "badge-colors-dark".equals(childName)) {
                                java.util.Objects.requireNonNull(builder);
                                setResAttributeArray(parser, new java.util.function.Consumer() { // from class: com.android.server.pm.UserTypeFactory$$ExternalSyntheticLambda9
                                    @Override // java.util.function.Consumer
                                    public final void accept(java.lang.Object obj) {
                                        builder.setDarkThemeBadgeColors((int[]) obj);
                                    }
                                });
                            } else if ("user-properties".equals(childName)) {
                                builder.getDefaultUserProperties().updateFromXml(com.android.internal.util.XmlUtils.makeTyped(parser));
                            } else {
                                android.util.Slog.w(LOG_TAG, "Unrecognized tag " + childName + " in " + parser.getPositionDescription());
                            }
                        }
                    }
                    com.android.internal.util.XmlUtils.nextElement(parser);
                } else {
                    return;
                }
            }
        } catch (java.io.IOException | org.xmlpull.v1.XmlPullParserException e) {
            android.util.Slog.w(LOG_TAG, "Cannot read user type configuration file.", e);
        }
    }

    private static void setIntAttribute(android.content.res.XmlResourceParser parser, java.lang.String attributeName, java.util.function.Consumer<java.lang.Integer> fcn) {
        java.lang.String intValue = parser.getAttributeValue(null, attributeName);
        if (intValue == null) {
            return;
        }
        try {
            fcn.accept(java.lang.Integer.valueOf(java.lang.Integer.parseInt(intValue)));
        } catch (java.lang.NumberFormatException e) {
            android.util.Slog.e(LOG_TAG, "Cannot parse value of '" + intValue + "' for " + attributeName + " in " + parser.getPositionDescription(), e);
            throw e;
        }
    }

    private static void setResAttribute(android.content.res.XmlResourceParser parser, java.lang.String attributeName, java.util.function.Consumer<java.lang.Integer> fcn) {
        if (parser.getAttributeValue(null, attributeName) != null) {
            int resId = parser.getAttributeResourceValue(null, attributeName, 0);
            fcn.accept(java.lang.Integer.valueOf(resId));
        }
    }

    private static void setResAttributeArray(android.content.res.XmlResourceParser parser, java.util.function.Consumer<int[]> fcn) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        java.util.ArrayList<java.lang.Integer> resList = new java.util.ArrayList<>();
        int depth = parser.getDepth();
        while (com.android.internal.util.XmlUtils.nextElementWithin(parser, depth)) {
            java.lang.String elementName = parser.getName();
            if (!com.android.server.pm.Settings.TAG_ITEM.equals(elementName)) {
                android.util.Slog.w(LOG_TAG, "Skipping unknown child element " + elementName + " in " + parser.getPositionDescription());
                com.android.internal.util.XmlUtils.skipCurrentTag(parser);
            } else {
                int resId = parser.getAttributeResourceValue(null, "res", -1);
                if (resId != -1) {
                    resList.add(java.lang.Integer.valueOf(resId));
                }
            }
        }
        int[] result = new int[resList.size()];
        for (int i = 0; i < resList.size(); i++) {
            result[i] = resList.get(i).intValue();
        }
        fcn.accept(result);
    }

    public static int getUserTypeVersion() {
        android.content.res.XmlResourceParser parser = android.content.res.Resources.getSystem().getXml(android.R.xml.config_user_types);
        try {
            int userTypeVersion = getUserTypeVersion(parser);
            if (parser != null) {
                parser.close();
            }
            return userTypeVersion;
        } catch (java.lang.Throwable th) {
            if (parser != null) {
                try {
                    parser.close();
                } catch (java.lang.Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    static int getUserTypeVersion(android.content.res.XmlResourceParser parser) {
        try {
            com.android.internal.util.XmlUtils.beginDocument(parser, "user-types");
            java.lang.String versionValue = parser.getAttributeValue(null, "version");
            if (versionValue == null) {
                return 0;
            }
            try {
                int version = java.lang.Integer.parseInt(versionValue);
                return version;
            } catch (java.lang.NumberFormatException e) {
                android.util.Slog.e(LOG_TAG, "Cannot parse value of '" + versionValue + "' for version in " + parser.getPositionDescription(), e);
                throw e;
            }
        } catch (java.io.IOException | org.xmlpull.v1.XmlPullParserException e2) {
            android.util.Slog.w(LOG_TAG, "Cannot read user type configuration file.", e2);
            return 0;
        }
    }

    public static java.util.List<com.android.server.pm.UserTypeFactory.UserTypeUpgrade> getUserTypeUpgrades() {
        android.content.res.XmlResourceParser parser = android.content.res.Resources.getSystem().getXml(android.R.xml.config_user_types);
        try {
            java.util.List<com.android.server.pm.UserTypeFactory.UserTypeUpgrade> userUpgrades = parseUserUpgrades(getDefaultBuilders(), parser);
            if (parser != null) {
                parser.close();
            }
            return userUpgrades;
        } catch (java.lang.Throwable th) {
            if (parser != null) {
                try {
                    parser.close();
                } catch (java.lang.Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    static java.util.List<com.android.server.pm.UserTypeFactory.UserTypeUpgrade> parseUserUpgrades(android.util.ArrayMap<java.lang.String, com.android.server.pm.UserTypeDetails.Builder> builders, android.content.res.XmlResourceParser parser) {
        java.util.List<com.android.server.pm.UserTypeFactory.UserTypeUpgrade> userUpgrades = new java.util.ArrayList<>();
        try {
            com.android.internal.util.XmlUtils.beginDocument(parser, "user-types");
            com.android.internal.util.XmlUtils.nextElement(parser);
            while (parser.getEventType() != 1) {
                java.lang.String elementName = parser.getName();
                if ("change-user-type".equals(elementName)) {
                    java.lang.String fromType = parser.getAttributeValue(null, "from");
                    java.lang.String toType = parser.getAttributeValue(null, "to");
                    validateUserTypeIsProfile(fromType, builders);
                    validateUserTypeIsProfile(toType, builders);
                    try {
                        int maxVersionToConvert = java.lang.Integer.parseInt(parser.getAttributeValue(null, "whenVersionLeq"));
                        com.android.server.pm.UserTypeFactory.UserTypeUpgrade userTypeUpgrade = new com.android.server.pm.UserTypeFactory.UserTypeUpgrade(fromType, toType, maxVersionToConvert);
                        userUpgrades.add(userTypeUpgrade);
                    } catch (java.lang.NumberFormatException e) {
                        android.util.Slog.e(LOG_TAG, "Cannot parse value of whenVersionLeq in " + parser.getPositionDescription(), e);
                        throw e;
                    }
                } else {
                    com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                }
                com.android.internal.util.XmlUtils.nextElement(parser);
            }
        } catch (java.io.IOException | org.xmlpull.v1.XmlPullParserException e2) {
            android.util.Slog.w(LOG_TAG, "Cannot read user type configuration file.", e2);
        }
        return userUpgrades;
    }

    private static void validateUserTypeIsProfile(java.lang.String userType, android.util.ArrayMap<java.lang.String, com.android.server.pm.UserTypeDetails.Builder> builders) {
        com.android.server.pm.UserTypeDetails.Builder builder = builders.get(userType);
        if (builder != null && builder.getBaseType() != 4096) {
            throw new java.lang.IllegalArgumentException("Illegal upgrade of user type " + userType + " : Can only upgrade profiles user types");
        }
    }

    public static class UserTypeUpgrade {
        private final java.lang.String mFromType;
        private final java.lang.String mToType;
        private final int mUpToVersion;

        public UserTypeUpgrade(java.lang.String fromType, java.lang.String toType, int upToVersion) {
            this.mFromType = fromType;
            this.mToType = toType;
            this.mUpToVersion = upToVersion;
        }

        public java.lang.String getFromType() {
            return this.mFromType;
        }

        public java.lang.String getToType() {
            return this.mToType;
        }

        public int getUpToVersion() {
            return this.mUpToVersion;
        }
    }
}
