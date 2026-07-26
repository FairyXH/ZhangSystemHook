package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public class UpdateOwnershipHelper {
    private static final int MAX_DENYLIST_SIZE = 500;
    private static final java.lang.String TAG_OWNERSHIP_OPT_OUT = "deny-ownership";
    private final android.util.ArrayMap<java.lang.String, android.util.ArraySet<java.lang.String>> mUpdateOwnerOptOutsToOwners = new android.util.ArrayMap<>(200);
    private final java.lang.Object mLock = new java.lang.Object();

    static boolean hasValidOwnershipDenyList(com.android.server.pm.PackageSetting pkgSetting) {
        com.android.internal.pm.parsing.pkg.AndroidPackageInternal pkg = pkgSetting.getPkg();
        return pkg != null && (pkgSetting.isSystem() || pkgSetting.isUpdatedSystemApp()) && pkg.getProperties().containsKey("android.app.PROPERTY_LEGACY_UPDATE_OWNERSHIP_DENYLIST") && usesAnyPermission(pkg, "android.permission.INSTALL_PACKAGES", "android.permission.INSTALL_PACKAGE_UPDATES");
    }

    private static boolean usesAnyPermission(com.android.server.pm.pkg.AndroidPackage pkgSetting, java.lang.String... permissions) {
        java.util.List<com.android.internal.pm.pkg.component.ParsedUsesPermission> usesPermissions = pkgSetting.getUsesPermissions();
        for (int i = 0; i < usesPermissions.size(); i++) {
            for (java.lang.String str : permissions) {
                if (str.equals(usesPermissions.get(i).getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    /* JADX WARN: Code restructure failed: missing block: B:23:0x0083, code lost:
    
        android.util.Slog.w("PackageManager", "Deny list defined by " + r3.getPackageName() + " was trucated to maximum size of 500");
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public android.util.ArraySet<java.lang.String> readUpdateOwnerDenyList(com.android.server.pm.PackageSetting r21) {
        /*
            Method dump skipped, instruction units count: 218
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.UpdateOwnershipHelper.readUpdateOwnerDenyList(com.android.server.pm.PackageSetting):android.util.ArraySet");
    }

    public void addToUpdateOwnerDenyList(java.lang.String listOwner, android.util.ArraySet<java.lang.String> listContents) {
        synchronized (this.mLock) {
            for (int i = 0; i < listContents.size(); i++) {
                java.lang.String packageName = listContents.valueAt(i);
                android.util.ArraySet<java.lang.String> priorDenyListOwners = this.mUpdateOwnerOptOutsToOwners.putIfAbsent(packageName, new android.util.ArraySet<>(new java.lang.String[]{listOwner}));
                if (priorDenyListOwners != null) {
                    priorDenyListOwners.add(listOwner);
                }
            }
        }
    }

    public void removeUpdateOwnerDenyList(java.lang.String listOwner) {
        synchronized (this.mLock) {
            for (int i = this.mUpdateOwnerOptOutsToOwners.size() - 1; i >= 0; i--) {
                android.util.ArraySet<java.lang.String> packageDenyListContributors = this.mUpdateOwnerOptOutsToOwners.get(this.mUpdateOwnerOptOutsToOwners.keyAt(i));
                if (packageDenyListContributors.remove(listOwner) && packageDenyListContributors.isEmpty()) {
                    this.mUpdateOwnerOptOutsToOwners.removeAt(i);
                }
            }
        }
    }

    public boolean isUpdateOwnershipDenylisted(java.lang.String packageName) {
        boolean zContainsKey;
        synchronized (this.mLock) {
            zContainsKey = this.mUpdateOwnerOptOutsToOwners.containsKey(packageName);
        }
        return zContainsKey;
    }

    public boolean isUpdateOwnershipDenyListProvider(java.lang.String packageName) {
        if (packageName == null) {
            return false;
        }
        synchronized (this.mLock) {
            for (int i = this.mUpdateOwnerOptOutsToOwners.size() - 1; i >= 0; i--) {
                if (this.mUpdateOwnerOptOutsToOwners.valueAt(i).contains(packageName)) {
                    return true;
                }
            }
            return false;
        }
    }
}
