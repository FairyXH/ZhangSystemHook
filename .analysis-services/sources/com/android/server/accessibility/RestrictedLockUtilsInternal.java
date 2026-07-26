package com.android.server.accessibility;

/* JADX INFO: loaded from: classes.dex */
public class RestrictedLockUtilsInternal {
    public static com.android.settingslib.RestrictedLockUtils.EnforcedAdmin checkIfAccessibilityServiceDisallowed(android.content.Context context, java.lang.String packageName, int userId) {
        android.app.admin.DevicePolicyManager dpm = (android.app.admin.DevicePolicyManager) context.getSystemService(android.app.admin.DevicePolicyManager.class);
        if (dpm == null) {
            return null;
        }
        com.android.settingslib.RestrictedLockUtils.EnforcedAdmin admin = com.android.settingslib.RestrictedLockUtils.getProfileOrDeviceOwner(context, getUserHandleOf(userId));
        boolean permitted = true;
        if (admin != null) {
            permitted = dpm.isAccessibilityServicePermittedByAdmin(admin.component, packageName, userId);
        }
        int managedProfileId = getManagedProfileId(context, userId);
        com.android.settingslib.RestrictedLockUtils.EnforcedAdmin profileAdmin = com.android.settingslib.RestrictedLockUtils.getProfileOrDeviceOwner(context, getUserHandleOf(managedProfileId));
        boolean permittedByProfileAdmin = true;
        if (profileAdmin != null) {
            permittedByProfileAdmin = dpm.isAccessibilityServicePermittedByAdmin(profileAdmin.component, packageName, managedProfileId);
        }
        if (!permitted && !permittedByProfileAdmin) {
            return com.android.settingslib.RestrictedLockUtils.EnforcedAdmin.MULTIPLE_ENFORCED_ADMIN;
        }
        if (!permitted) {
            return admin;
        }
        if (permittedByProfileAdmin) {
            return null;
        }
        return profileAdmin;
    }

    public static com.android.settingslib.RestrictedLockUtils.EnforcedAdmin checkIfInputMethodDisallowed(android.content.Context context, java.lang.String packageName, int userId) {
        android.app.admin.DevicePolicyManager dpm = (android.app.admin.DevicePolicyManager) context.getSystemService(android.app.admin.DevicePolicyManager.class);
        if (dpm == null) {
            return null;
        }
        com.android.settingslib.RestrictedLockUtils.EnforcedAdmin admin = com.android.settingslib.RestrictedLockUtils.getProfileOrDeviceOwner(context, getUserHandleOf(userId));
        boolean permitted = true;
        if (admin != null) {
            permitted = dpm.isInputMethodPermittedByAdmin(admin.component, packageName, userId);
        }
        boolean permittedByParentAdmin = true;
        com.android.settingslib.RestrictedLockUtils.EnforcedAdmin profileAdmin = null;
        int managedProfileId = getManagedProfileId(context, userId);
        if (managedProfileId != -10000 && (profileAdmin = com.android.settingslib.RestrictedLockUtils.getProfileOrDeviceOwner(context, getUserHandleOf(managedProfileId))) != null && dpm.isOrganizationOwnedDeviceWithManagedProfile()) {
            android.app.admin.DevicePolicyManager parentDpm = dpm.getParentProfileInstance(android.os.UserManager.get(context).getUserInfo(managedProfileId));
            permittedByParentAdmin = parentDpm.isInputMethodPermittedByAdmin(profileAdmin.component, packageName, managedProfileId);
        }
        if (!permitted && !permittedByParentAdmin) {
            return com.android.settingslib.RestrictedLockUtils.EnforcedAdmin.MULTIPLE_ENFORCED_ADMIN;
        }
        if (!permitted) {
            return admin;
        }
        if (permittedByParentAdmin) {
            return null;
        }
        return profileAdmin;
    }

    private static int getManagedProfileId(android.content.Context context, int userId) {
        android.os.UserManager um = (android.os.UserManager) context.getSystemService(android.os.UserManager.class);
        java.util.List<android.content.pm.UserInfo> userProfiles = um.getProfiles(userId);
        for (android.content.pm.UserInfo uInfo : userProfiles) {
            if (uInfo.id != userId && uInfo.isManagedProfile()) {
                return uInfo.id;
            }
        }
        return -10000;
    }

    private static android.os.UserHandle getUserHandleOf(int userId) {
        if (userId == -10000) {
            return null;
        }
        return android.os.UserHandle.of(userId);
    }
}
