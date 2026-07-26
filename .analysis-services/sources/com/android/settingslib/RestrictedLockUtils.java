package com.android.settingslib;

/* JADX INFO: loaded from: classes3.dex */
public class RestrictedLockUtils {
    public static com.android.settingslib.RestrictedLockUtils.EnforcedAdmin getProfileOrDeviceOwner(android.content.Context context, android.os.UserHandle user) {
        return getProfileOrDeviceOwner(context, null, user);
    }

    public static com.android.settingslib.RestrictedLockUtils.EnforcedAdmin getProfileOrDeviceOwner(android.content.Context context, java.lang.String enforcedRestriction, android.os.UserHandle user) {
        android.app.admin.DevicePolicyManager dpm;
        android.content.ComponentName adminComponent;
        if (user == null || (dpm = (android.app.admin.DevicePolicyManager) context.getSystemService("device_policy")) == null) {
            return null;
        }
        try {
            android.content.Context userContext = context.createPackageContextAsUser(context.getPackageName(), 0, user);
            android.content.ComponentName adminComponent2 = ((android.app.admin.DevicePolicyManager) userContext.getSystemService(android.app.admin.DevicePolicyManager.class)).getProfileOwner();
            if (adminComponent2 != null) {
                return new com.android.settingslib.RestrictedLockUtils.EnforcedAdmin(adminComponent2, enforcedRestriction, user);
            }
            if (!java.util.Objects.equals(dpm.getDeviceOwnerUser(), user) || (adminComponent = dpm.getDeviceOwnerComponentOnAnyUser()) == null) {
                return null;
            }
            return new com.android.settingslib.RestrictedLockUtils.EnforcedAdmin(adminComponent, enforcedRestriction, user);
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            throw new java.lang.IllegalStateException(e);
        }
    }

    public static void sendShowAdminSupportDetailsIntent(android.content.Context context, com.android.settingslib.RestrictedLockUtils.EnforcedAdmin admin) {
        android.content.Intent intent = getShowAdminSupportDetailsIntent(admin);
        int targetUserId = android.os.UserHandle.myUserId();
        if (admin != null) {
            if (admin.user != null && isCurrentUserOrProfile(context, admin.user.getIdentifier())) {
                targetUserId = admin.user.getIdentifier();
            }
            intent.putExtra("android.app.extra.RESTRICTION", admin.enforcedRestriction);
        }
        context.startActivityAsUser(intent, android.os.UserHandle.of(targetUserId));
    }

    public static android.content.Intent getShowAdminSupportDetailsIntent(android.content.Context context, com.android.settingslib.RestrictedLockUtils.EnforcedAdmin admin) {
        return getShowAdminSupportDetailsIntent(admin);
    }

    public static android.content.Intent getShowAdminSupportDetailsIntent(com.android.settingslib.RestrictedLockUtils.EnforcedAdmin admin) {
        android.content.Intent intent = new android.content.Intent("android.settings.SHOW_ADMIN_SUPPORT_DETAILS");
        if (admin != null) {
            if (admin.component != null) {
                intent.putExtra("android.app.extra.DEVICE_ADMIN", admin.component);
            }
            intent.putExtra("android.intent.extra.USER", admin.user);
        }
        return intent;
    }

    public static boolean isCurrentUserOrProfile(android.content.Context context, int userId) {
        android.os.UserManager um = (android.os.UserManager) context.getSystemService(android.os.UserManager.class);
        return um.getUserProfiles().contains(android.os.UserHandle.of(userId));
    }

    public static class EnforcedAdmin {
        public static final com.android.settingslib.RestrictedLockUtils.EnforcedAdmin MULTIPLE_ENFORCED_ADMIN = new com.android.settingslib.RestrictedLockUtils.EnforcedAdmin();
        public android.content.ComponentName component;
        public java.lang.String enforcedRestriction;
        public android.os.UserHandle user;

        public static com.android.settingslib.RestrictedLockUtils.EnforcedAdmin createDefaultEnforcedAdminWithRestriction(java.lang.String enforcedRestriction) {
            com.android.settingslib.RestrictedLockUtils.EnforcedAdmin enforcedAdmin = new com.android.settingslib.RestrictedLockUtils.EnforcedAdmin();
            enforcedAdmin.enforcedRestriction = enforcedRestriction;
            return enforcedAdmin;
        }

        public EnforcedAdmin(android.content.ComponentName component, android.os.UserHandle user) {
            this.component = null;
            this.enforcedRestriction = null;
            this.user = null;
            this.component = component;
            this.user = user;
        }

        public EnforcedAdmin(android.content.ComponentName component, java.lang.String enforcedRestriction, android.os.UserHandle user) {
            this.component = null;
            this.enforcedRestriction = null;
            this.user = null;
            this.component = component;
            this.enforcedRestriction = enforcedRestriction;
            this.user = user;
        }

        public EnforcedAdmin(com.android.settingslib.RestrictedLockUtils.EnforcedAdmin other) {
            this.component = null;
            this.enforcedRestriction = null;
            this.user = null;
            if (other == null) {
                throw new java.lang.IllegalArgumentException();
            }
            this.component = other.component;
            this.enforcedRestriction = other.enforcedRestriction;
            this.user = other.user;
        }

        public EnforcedAdmin() {
            this.component = null;
            this.enforcedRestriction = null;
            this.user = null;
        }

        public static com.android.settingslib.RestrictedLockUtils.EnforcedAdmin combine(com.android.settingslib.RestrictedLockUtils.EnforcedAdmin admin1, com.android.settingslib.RestrictedLockUtils.EnforcedAdmin admin2) {
            if (admin1 == null) {
                return admin2;
            }
            if (admin2 == null || admin1.equals(admin2)) {
                return admin1;
            }
            if (!admin1.enforcedRestriction.equals(admin2.enforcedRestriction)) {
                throw new java.lang.IllegalArgumentException("Admins with different restriction cannot be combined");
            }
            return MULTIPLE_ENFORCED_ADMIN;
        }

        public boolean equals(java.lang.Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            com.android.settingslib.RestrictedLockUtils.EnforcedAdmin that = (com.android.settingslib.RestrictedLockUtils.EnforcedAdmin) o;
            if (java.util.Objects.equals(this.user, that.user) && java.util.Objects.equals(this.component, that.component) && java.util.Objects.equals(this.enforcedRestriction, that.enforcedRestriction)) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            return java.util.Objects.hash(this.component, this.enforcedRestriction, this.user);
        }

        public java.lang.String toString() {
            return "EnforcedAdmin{component=" + this.component + ", enforcedRestriction='" + this.enforcedRestriction + ", user=" + this.user + '}';
        }
    }

    @java.lang.Deprecated
    public static void sendShowRestrictedSettingDialogIntent(android.content.Context context, java.lang.String packageName, int uid) {
        android.content.Intent intent = getShowRestrictedSettingsIntent(packageName, uid);
        context.startActivity(intent);
    }

    @java.lang.Deprecated
    private static android.content.Intent getShowRestrictedSettingsIntent(java.lang.String packageName, int uid) {
        android.content.Intent intent = new android.content.Intent("android.settings.SHOW_RESTRICTED_SETTING_DIALOG");
        intent.putExtra("android.intent.extra.PACKAGE_NAME", packageName);
        intent.putExtra("android.intent.extra.UID", uid);
        return intent;
    }
}
