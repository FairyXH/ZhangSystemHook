package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public class DefaultAppProvider {
    private final java.util.function.Supplier<android.app.role.RoleManager> mRoleManagerSupplier;
    private final java.util.function.Supplier<com.android.server.pm.UserManagerInternal> mUserManagerInternalSupplier;

    public DefaultAppProvider(java.util.function.Supplier<android.app.role.RoleManager> roleManagerSupplier, java.util.function.Supplier<com.android.server.pm.UserManagerInternal> userManagerInternalSupplier) {
        this.mRoleManagerSupplier = roleManagerSupplier;
        this.mUserManagerInternalSupplier = userManagerInternalSupplier;
    }

    public java.lang.String getDefaultBrowser(int userId) {
        return getRoleHolder("android.app.role.BROWSER", userId);
    }

    public void setDefaultBrowser(final java.lang.String packageName, int userId) {
        android.app.role.RoleManager roleManager = this.mRoleManagerSupplier.get();
        if (roleManager == null) {
            return;
        }
        android.os.UserHandle user = android.os.UserHandle.of(userId);
        java.util.concurrent.Executor executor = com.android.server.FgThread.getExecutor();
        java.util.function.Consumer<java.lang.Boolean> callback = new java.util.function.Consumer() { // from class: com.android.server.pm.DefaultAppProvider$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.pm.DefaultAppProvider.lambda$setDefaultBrowser$0(packageName, (java.lang.Boolean) obj);
            }
        };
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            if (packageName != null) {
                roleManager.addRoleHolderAsUser("android.app.role.BROWSER", packageName, 0, user, executor, callback);
            } else {
                roleManager.clearRoleHoldersAsUser("android.app.role.BROWSER", 0, user, executor, callback);
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    static /* synthetic */ void lambda$setDefaultBrowser$0(java.lang.String packageName, java.lang.Boolean successful) {
        if (!successful.booleanValue()) {
            android.util.Slog.e("PackageManager", "Failed to set default browser to " + packageName);
        }
    }

    public java.lang.String getDefaultDialer(int userId) {
        return getRoleHolder("android.app.role.DIALER", userId);
    }

    public java.lang.String getDefaultHome(int userId) {
        return getRoleHolder("android.app.role.HOME", this.mUserManagerInternalSupplier.get().getProfileParentId(userId));
    }

    public boolean setDefaultHome(java.lang.String packageName, int userId, java.util.concurrent.Executor executor, java.util.function.Consumer<java.lang.Boolean> callback) {
        android.app.role.RoleManager roleManager = this.mRoleManagerSupplier.get();
        if (roleManager == null) {
            return false;
        }
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            roleManager.addRoleHolderAsUser("android.app.role.HOME", packageName, 0, android.os.UserHandle.of(userId), executor, callback);
            android.os.Binder.restoreCallingIdentity(identity);
            return true;
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(identity);
            throw th;
        }
    }

    private java.lang.String getRoleHolder(java.lang.String roleName, int userId) {
        android.app.role.RoleManager roleManager = this.mRoleManagerSupplier.get();
        if (roleManager == null) {
            return null;
        }
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            return (java.lang.String) com.android.internal.util.CollectionUtils.firstOrNull(roleManager.getRoleHoldersAsUser(roleName, android.os.UserHandle.of(userId)));
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }
}
