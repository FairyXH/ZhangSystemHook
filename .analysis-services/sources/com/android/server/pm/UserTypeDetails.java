package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public final class UserTypeDetails {
    public static final int UNLIMITED_NUMBER_OF_USERS = -1;
    private final int mAccessibilityString;
    private final int[] mBadgeColors;
    private final int[] mBadgeLabels;
    private final int mBadgeNoBackground;
    private final int mBadgePlain;
    private final int mBaseType;
    private final int[] mDarkThemeBadgeColors;
    private final java.util.List<com.android.server.pm.DefaultCrossProfileIntentFilter> mDefaultCrossProfileIntentFilters;
    private final android.os.Bundle mDefaultRestrictions;
    private final android.os.Bundle mDefaultSecureSettings;
    private final android.os.Bundle mDefaultSystemSettings;
    private final int mDefaultUserInfoPropertyFlags;
    private final android.content.pm.UserProperties mDefaultUserProperties;
    private final boolean mEnabled;
    private final int mIconBadge;
    private final int[] mLabels;
    private final int mMaxAllowed;
    private final int mMaxAllowedPerParent;
    private final java.lang.String mName;
    private final int mStatusBarIcon;

    private UserTypeDetails(java.lang.String name, boolean enabled, int maxAllowed, int baseType, int defaultUserInfoPropertyFlags, int[] labels, int maxAllowedPerParent, int iconBadge, int badgePlain, int badgeNoBackground, int statusBarIcon, int[] badgeLabels, int[] badgeColors, int[] darkThemeBadgeColors, android.os.Bundle defaultRestrictions, android.os.Bundle defaultSystemSettings, android.os.Bundle defaultSecureSettings, java.util.List<com.android.server.pm.DefaultCrossProfileIntentFilter> defaultCrossProfileIntentFilters, int accessibilityString, android.content.pm.UserProperties defaultUserProperties) {
        this.mName = name;
        this.mEnabled = enabled;
        this.mMaxAllowed = maxAllowed;
        this.mMaxAllowedPerParent = maxAllowedPerParent;
        this.mBaseType = baseType;
        this.mDefaultUserInfoPropertyFlags = defaultUserInfoPropertyFlags;
        this.mDefaultRestrictions = defaultRestrictions;
        this.mDefaultSystemSettings = defaultSystemSettings;
        this.mDefaultSecureSettings = defaultSecureSettings;
        this.mDefaultCrossProfileIntentFilters = defaultCrossProfileIntentFilters;
        this.mIconBadge = iconBadge;
        this.mBadgePlain = badgePlain;
        this.mBadgeNoBackground = badgeNoBackground;
        this.mStatusBarIcon = statusBarIcon;
        this.mLabels = labels;
        this.mBadgeLabels = badgeLabels;
        this.mBadgeColors = badgeColors;
        this.mDarkThemeBadgeColors = darkThemeBadgeColors;
        this.mAccessibilityString = accessibilityString;
        this.mDefaultUserProperties = defaultUserProperties;
    }

    public java.lang.String getName() {
        return this.mName;
    }

    public boolean isEnabled() {
        return this.mEnabled;
    }

    public int getMaxAllowed() {
        return this.mMaxAllowed;
    }

    public int getMaxAllowedPerParent() {
        return this.mMaxAllowedPerParent;
    }

    public int getDefaultUserInfoFlags() {
        return this.mDefaultUserInfoPropertyFlags | this.mBaseType;
    }

    public int getLabel(int badgeIndex) {
        if (this.mLabels == null || this.mLabels.length == 0 || badgeIndex < 0) {
            return 0;
        }
        return this.mLabels[java.lang.Math.min(badgeIndex, this.mLabels.length - 1)];
    }

    public boolean hasBadge() {
        return this.mIconBadge != 0;
    }

    public int getIconBadge() {
        return this.mIconBadge;
    }

    public int getBadgePlain() {
        return this.mBadgePlain;
    }

    public int getBadgeNoBackground() {
        return this.mBadgeNoBackground;
    }

    public int getStatusBarIcon() {
        return this.mStatusBarIcon;
    }

    public int getBadgeLabel(int badgeIndex) {
        if (this.mBadgeLabels == null || this.mBadgeLabels.length == 0 || badgeIndex < 0) {
            return 0;
        }
        return this.mBadgeLabels[java.lang.Math.min(badgeIndex, this.mBadgeLabels.length - 1)];
    }

    public int getBadgeColor(int badgeIndex) {
        if (this.mBadgeColors == null || this.mBadgeColors.length == 0 || badgeIndex < 0) {
            return 0;
        }
        return this.mBadgeColors[java.lang.Math.min(badgeIndex, this.mBadgeColors.length - 1)];
    }

    public int getDarkThemeBadgeColor(int badgeIndex) {
        if (this.mDarkThemeBadgeColors == null || this.mDarkThemeBadgeColors.length == 0 || badgeIndex < 0) {
            return getBadgeColor(badgeIndex);
        }
        return this.mDarkThemeBadgeColors[java.lang.Math.min(badgeIndex, this.mDarkThemeBadgeColors.length - 1)];
    }

    public android.content.pm.UserProperties getDefaultUserPropertiesReference() {
        return this.mDefaultUserProperties;
    }

    public int getAccessibilityString() {
        return this.mAccessibilityString;
    }

    public boolean isProfile() {
        return (this.mBaseType & 4096) != 0;
    }

    public boolean isFull() {
        return (this.mBaseType & 1024) != 0;
    }

    public boolean isSystem() {
        return (this.mBaseType & 2048) != 0;
    }

    android.os.Bundle getDefaultRestrictions() {
        return com.android.server.BundleUtils.clone(this.mDefaultRestrictions);
    }

    public void addDefaultRestrictionsTo(android.os.Bundle currentRestrictions) {
        com.android.server.pm.UserRestrictionsUtils.merge(currentRestrictions, this.mDefaultRestrictions);
    }

    android.os.Bundle getDefaultSystemSettings() {
        return com.android.server.BundleUtils.clone(this.mDefaultSystemSettings);
    }

    android.os.Bundle getDefaultSecureSettings() {
        return com.android.server.BundleUtils.clone(this.mDefaultSecureSettings);
    }

    java.util.List<com.android.server.pm.DefaultCrossProfileIntentFilter> getDefaultCrossProfileIntentFilters() {
        if (this.mDefaultCrossProfileIntentFilters != null) {
            return new java.util.ArrayList(this.mDefaultCrossProfileIntentFilters);
        }
        return java.util.Collections.emptyList();
    }

    public void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        pw.print(prefix);
        pw.print("mName: ");
        pw.println(this.mName);
        pw.print(prefix);
        pw.print("mBaseType: ");
        pw.println(android.content.pm.UserInfo.flagsToString(this.mBaseType));
        pw.print(prefix);
        pw.print("mEnabled: ");
        pw.println(this.mEnabled);
        pw.print(prefix);
        pw.print("mMaxAllowed: ");
        pw.println(this.mMaxAllowed);
        pw.print(prefix);
        pw.print("mMaxAllowedPerParent: ");
        pw.println(this.mMaxAllowedPerParent);
        pw.print(prefix);
        pw.print("mDefaultUserInfoFlags: ");
        pw.println(android.content.pm.UserInfo.flagsToString(this.mDefaultUserInfoPropertyFlags));
        this.mDefaultUserProperties.println(pw, prefix);
        java.lang.String restrictionsPrefix = prefix + "    ";
        if (isSystem()) {
            pw.print(prefix);
            pw.println("config_defaultFirstUserRestrictions: ");
            try {
                android.os.Bundle restrictions = new android.os.Bundle();
                java.lang.String[] defaultFirstUserRestrictions = android.content.res.Resources.getSystem().getStringArray(android.R.array.config_convert_to_emergency_number_map);
                for (java.lang.String userRestriction : defaultFirstUserRestrictions) {
                    if (com.android.server.pm.UserRestrictionsUtils.isValidRestriction(userRestriction)) {
                        restrictions.putBoolean(userRestriction, true);
                    }
                }
                com.android.server.pm.UserRestrictionsUtils.dumpRestrictions(pw, restrictionsPrefix, restrictions);
            } catch (android.content.res.Resources.NotFoundException e) {
                pw.print(restrictionsPrefix);
                pw.println("none - resource not found");
            }
        } else {
            pw.print(prefix);
            pw.println("mDefaultRestrictions: ");
            com.android.server.pm.UserRestrictionsUtils.dumpRestrictions(pw, restrictionsPrefix, this.mDefaultRestrictions);
        }
        pw.print(prefix);
        pw.print("mIconBadge: ");
        pw.println(this.mIconBadge);
        pw.print(prefix);
        pw.print("mBadgePlain: ");
        pw.println(this.mBadgePlain);
        pw.print(prefix);
        pw.print("mBadgeNoBackground: ");
        pw.println(this.mBadgeNoBackground);
        pw.print(prefix);
        pw.print("mStatusBarIcon: ");
        pw.println(this.mStatusBarIcon);
        pw.print(prefix);
        pw.print("mBadgeLabels.length: ");
        pw.println(this.mBadgeLabels != null ? java.lang.Integer.valueOf(this.mBadgeLabels.length) : "0(null)");
        pw.print(prefix);
        pw.print("mBadgeColors.length: ");
        pw.println(this.mBadgeColors != null ? java.lang.Integer.valueOf(this.mBadgeColors.length) : "0(null)");
        pw.print(prefix);
        pw.print("mDarkThemeBadgeColors.length: ");
        pw.println(this.mDarkThemeBadgeColors != null ? java.lang.Integer.valueOf(this.mDarkThemeBadgeColors.length) : "0(null)");
        pw.print(prefix);
        pw.print("mLabels.length: ");
        pw.println(this.mLabels != null ? java.lang.Integer.valueOf(this.mLabels.length) : "0(null)");
    }

    public static final class Builder {
        private int mBaseType;
        private java.lang.String mName;
        private int mMaxAllowed = -1;
        private int mMaxAllowedPerParent = -1;
        private int mDefaultUserInfoPropertyFlags = 0;
        private android.os.Bundle mDefaultRestrictions = null;
        private android.os.Bundle mDefaultSystemSettings = null;
        private android.os.Bundle mDefaultSecureSettings = null;
        private java.util.List<com.android.server.pm.DefaultCrossProfileIntentFilter> mDefaultCrossProfileIntentFilters = null;
        private int mEnabled = 1;
        private int[] mLabels = null;
        private int[] mBadgeLabels = null;
        private int[] mBadgeColors = null;
        private int[] mDarkThemeBadgeColors = null;
        private int mIconBadge = 0;
        private int mBadgePlain = 0;
        private int mBadgeNoBackground = 0;
        private int mStatusBarIcon = 0;
        private int mAccessibilityString = 0;
        private android.content.pm.UserProperties mDefaultUserProperties = null;

        public com.android.server.pm.UserTypeDetails.Builder setName(java.lang.String name) {
            this.mName = name;
            return this;
        }

        public com.android.server.pm.UserTypeDetails.Builder setEnabled(int enabled) {
            this.mEnabled = enabled;
            return this;
        }

        public com.android.server.pm.UserTypeDetails.Builder setMaxAllowed(int maxAllowed) {
            this.mMaxAllowed = maxAllowed;
            return this;
        }

        public com.android.server.pm.UserTypeDetails.Builder setMaxAllowedPerParent(int maxAllowedPerParent) {
            this.mMaxAllowedPerParent = maxAllowedPerParent;
            return this;
        }

        public com.android.server.pm.UserTypeDetails.Builder setBaseType(int baseType) {
            this.mBaseType = baseType;
            return this;
        }

        public com.android.server.pm.UserTypeDetails.Builder setDefaultUserInfoPropertyFlags(int flags) {
            this.mDefaultUserInfoPropertyFlags = flags;
            return this;
        }

        public com.android.server.pm.UserTypeDetails.Builder setBadgeLabels(int... badgeLabels) {
            this.mBadgeLabels = badgeLabels;
            return this;
        }

        public com.android.server.pm.UserTypeDetails.Builder setBadgeColors(int... badgeColors) {
            this.mBadgeColors = badgeColors;
            return this;
        }

        public com.android.server.pm.UserTypeDetails.Builder setDarkThemeBadgeColors(int... darkThemeBadgeColors) {
            this.mDarkThemeBadgeColors = darkThemeBadgeColors;
            return this;
        }

        public com.android.server.pm.UserTypeDetails.Builder setIconBadge(int badgeIcon) {
            this.mIconBadge = badgeIcon;
            return this;
        }

        public com.android.server.pm.UserTypeDetails.Builder setBadgePlain(int badgePlain) {
            this.mBadgePlain = badgePlain;
            return this;
        }

        public com.android.server.pm.UserTypeDetails.Builder setBadgeNoBackground(int badgeNoBackground) {
            this.mBadgeNoBackground = badgeNoBackground;
            return this;
        }

        public com.android.server.pm.UserTypeDetails.Builder setStatusBarIcon(int statusBarIcon) {
            this.mStatusBarIcon = statusBarIcon;
            return this;
        }

        public com.android.server.pm.UserTypeDetails.Builder setLabels(int... labels) {
            this.mLabels = labels;
            return this;
        }

        public com.android.server.pm.UserTypeDetails.Builder setDefaultRestrictions(android.os.Bundle restrictions) {
            this.mDefaultRestrictions = restrictions;
            return this;
        }

        public com.android.server.pm.UserTypeDetails.Builder setDefaultSystemSettings(android.os.Bundle settings) {
            this.mDefaultSystemSettings = settings;
            return this;
        }

        public com.android.server.pm.UserTypeDetails.Builder setDefaultSecureSettings(android.os.Bundle settings) {
            this.mDefaultSecureSettings = settings;
            return this;
        }

        public com.android.server.pm.UserTypeDetails.Builder setDefaultCrossProfileIntentFilters(java.util.List<com.android.server.pm.DefaultCrossProfileIntentFilter> intentFilters) {
            this.mDefaultCrossProfileIntentFilters = intentFilters;
            return this;
        }

        public com.android.server.pm.UserTypeDetails.Builder setAccessibilityString(int accessibilityString) {
            this.mAccessibilityString = accessibilityString;
            return this;
        }

        public com.android.server.pm.UserTypeDetails.Builder setDefaultUserProperties(android.content.pm.UserProperties.Builder userPropertiesBuilder) {
            this.mDefaultUserProperties = userPropertiesBuilder.build();
            return this;
        }

        public android.content.pm.UserProperties getDefaultUserProperties() {
            if (this.mDefaultUserProperties == null) {
                this.mDefaultUserProperties = new android.content.pm.UserProperties.Builder().build();
            }
            return this.mDefaultUserProperties;
        }

        int getBaseType() {
            return this.mBaseType;
        }

        public com.android.server.pm.UserTypeDetails createUserTypeDetails() {
            com.android.internal.util.Preconditions.checkArgument(this.mName != null, "Cannot create a UserTypeDetails with no name.");
            com.android.internal.util.Preconditions.checkArgument(hasValidBaseType(), "UserTypeDetails " + this.mName + " has invalid baseType: " + this.mBaseType);
            com.android.internal.util.Preconditions.checkArgument(hasValidPropertyFlags(), "UserTypeDetails " + this.mName + " has invalid flags: " + java.lang.Integer.toHexString(this.mDefaultUserInfoPropertyFlags));
            checkSystemAndMainUserPreconditions();
            if (hasBadge()) {
                com.android.internal.util.Preconditions.checkArgument((this.mBadgeLabels == null || this.mBadgeLabels.length == 0) ? false : true, "UserTypeDetails " + this.mName + " has badge but no badgeLabels.");
                com.android.internal.util.Preconditions.checkArgument((this.mBadgeColors == null || this.mBadgeColors.length == 0) ? false : true, "UserTypeDetails " + this.mName + " has badge but no badgeColors.");
            }
            if (!isProfile()) {
                com.android.internal.util.Preconditions.checkArgument(this.mDefaultCrossProfileIntentFilters == null || this.mDefaultCrossProfileIntentFilters.isEmpty(), "UserTypeDetails %s has a non empty defaultCrossProfileIntentFilters", new java.lang.Object[]{this.mName});
            }
            return new com.android.server.pm.UserTypeDetails(this.mName, this.mEnabled != 0, this.mMaxAllowed, this.mBaseType, this.mDefaultUserInfoPropertyFlags, this.mLabels, this.mMaxAllowedPerParent, this.mIconBadge, this.mBadgePlain, this.mBadgeNoBackground, this.mStatusBarIcon, this.mBadgeLabels, this.mBadgeColors, this.mDarkThemeBadgeColors == null ? this.mBadgeColors : this.mDarkThemeBadgeColors, this.mDefaultRestrictions, this.mDefaultSystemSettings, this.mDefaultSecureSettings, this.mDefaultCrossProfileIntentFilters, this.mAccessibilityString, getDefaultUserProperties());
        }

        private boolean hasBadge() {
            return this.mIconBadge != 0;
        }

        private boolean isProfile() {
            return (this.mBaseType & 4096) != 0;
        }

        private boolean hasValidBaseType() {
            return this.mBaseType == 1024 || this.mBaseType == 4096 || this.mBaseType == 2048 || this.mBaseType == 3072;
        }

        private boolean hasValidPropertyFlags() {
            return (this.mDefaultUserInfoPropertyFlags & 7312) == 0;
        }

        private void checkSystemAndMainUserPreconditions() {
            com.android.internal.util.Preconditions.checkArgument(((this.mBaseType & 2048) != 0) == ((this.mDefaultUserInfoPropertyFlags & 1) != 0), "UserTypeDetails " + this.mName + " cannot be SYSTEM xor PRIMARY.");
            com.android.internal.util.Preconditions.checkArgument((this.mDefaultUserInfoPropertyFlags & 16384) == 0 || this.mMaxAllowed == 1, "UserTypeDetails " + this.mName + " must not sanction more than one MainUser.");
        }
    }

    public boolean isManagedProfile() {
        return android.os.UserManager.isUserTypeManagedProfile(this.mName);
    }

    public boolean isCommunalProfile() {
        return android.os.UserManager.isUserTypeCommunalProfile(this.mName);
    }

    public boolean isPrivateProfile() {
        return android.os.UserManager.isUserTypePrivateProfile(this.mName);
    }
}
