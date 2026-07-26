package com.android.server.companion.utils;

/* JADX INFO: loaded from: classes.dex */
public final class RolesUtils {
    private static final java.lang.String TAG = "CDM_RolesUtils";

    public static boolean isRoleHolder(android.content.Context context, int userId, java.lang.String packageName, java.lang.String role) {
        android.app.role.RoleManager roleManager = (android.app.role.RoleManager) context.getSystemService(android.app.role.RoleManager.class);
        java.util.List<java.lang.String> roleHolders = roleManager.getRoleHoldersAsUser(role, android.os.UserHandle.of(userId));
        return roleHolders.contains(packageName);
    }

    public static void addRoleHolderForAssociation(android.content.Context context, android.companion.AssociationInfo associationInfo, java.util.function.Consumer<java.lang.Boolean> roleGrantResult) {
        java.lang.String deviceProfile = associationInfo.getDeviceProfile();
        if (deviceProfile == null) {
            roleGrantResult.accept(true);
            return;
        }
        android.app.role.RoleManager roleManager = (android.app.role.RoleManager) context.getSystemService(android.app.role.RoleManager.class);
        java.lang.String packageName = associationInfo.getPackageName();
        int userId = associationInfo.getUserId();
        android.os.UserHandle userHandle = android.os.UserHandle.of(userId);
        roleManager.addRoleHolderAsUser(deviceProfile, packageName, 1, userHandle, context.getMainExecutor(), roleGrantResult);
    }

    public static void removeRoleHolderForAssociation(final android.content.Context context, final int userId, final java.lang.String packageName, final java.lang.String deviceProfile) {
        if (deviceProfile == null) {
            return;
        }
        final android.app.role.RoleManager roleManager = (android.app.role.RoleManager) context.getSystemService(android.app.role.RoleManager.class);
        final android.os.UserHandle userHandle = android.os.UserHandle.of(userId);
        android.util.Slog.i(TAG, "Removing CDM role=" + deviceProfile + " for userId=" + userId + ", packageName=" + packageName);
        android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.companion.utils.RolesUtils$$ExternalSyntheticLambda0
            public final void runOrThrow() {
                android.app.role.RoleManager roleManager2 = roleManager;
                java.lang.String str = deviceProfile;
                java.lang.String str2 = packageName;
                roleManager2.removeRoleHolderAsUser(str, str2, 1, userHandle, context.getMainExecutor(), new java.util.function.Consumer() { // from class: com.android.server.companion.utils.RolesUtils$$ExternalSyntheticLambda1
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        com.android.server.companion.utils.RolesUtils.lambda$removeRoleHolderForAssociation$0(i, str2, str, (java.lang.Boolean) obj);
                    }
                });
            }
        });
    }

    static /* synthetic */ void lambda$removeRoleHolderForAssociation$0(int userId, java.lang.String packageName, java.lang.String deviceProfile, java.lang.Boolean success) {
        if (!success.booleanValue()) {
            android.util.Slog.e(TAG, "Failed to remove userId=" + userId + ", packageName=" + packageName + " from the list of " + deviceProfile + " holders.");
        }
    }

    private RolesUtils() {
    }
}
