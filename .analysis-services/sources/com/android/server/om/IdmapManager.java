package com.android.server.om;

/* JADX INFO: loaded from: classes2.dex */
final class IdmapManager {
    static final int IDMAP_IS_MODIFIED = 2;
    static final int IDMAP_IS_VERIFIED = 1;
    static final int IDMAP_NOT_EXIST = 0;
    private static final boolean VENDOR_IS_Q_OR_LATER;
    private final java.lang.String mConfigSignaturePackage;
    private final com.android.server.om.IdmapDaemon mIdmapDaemon;
    private final com.android.server.om.PackageManagerHelper mPackageManager;

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface IdmapStatus {
    }

    static {
        boolean isQOrLater;
        java.lang.String value = android.os.SystemProperties.get("ro.vndk.version", "29");
        try {
            isQOrLater = java.lang.Integer.parseInt(value) >= 29;
        } catch (java.lang.NumberFormatException e) {
            isQOrLater = true;
        }
        VENDOR_IS_Q_OR_LATER = isQOrLater;
    }

    IdmapManager(com.android.server.om.IdmapDaemon idmapDaemon, com.android.server.om.PackageManagerHelper packageManager) {
        this.mPackageManager = packageManager;
        this.mIdmapDaemon = idmapDaemon;
        this.mConfigSignaturePackage = packageManager.getConfigSignaturePackage();
    }

    int createIdmap(com.android.server.pm.pkg.AndroidPackage targetPackage, com.android.server.pm.pkg.PackageState overlayPackageState, com.android.server.pm.pkg.AndroidPackage overlayPackage, java.lang.String overlayBasePath, java.lang.String overlayName, int userId) {
        java.lang.String targetPath;
        if (com.android.server.om.OverlayManagerService.DEBUG) {
            android.util.Slog.d("OverlayManager", "create idmap for " + targetPackage.getPackageName() + " and " + overlayPackage.getPackageName());
        }
        java.lang.String targetPath2 = ((com.android.server.pm.pkg.AndroidPackageSplit) targetPackage.getSplits().get(0)).getPath();
        try {
            int policies = calculateFulfilledPolicies(targetPackage, overlayPackageState, overlayPackage, userId);
            boolean enforce = enforceOverlayable(overlayPackageState, overlayPackage);
            if (this.mIdmapDaemon.verifyIdmap(targetPath2, overlayBasePath, overlayName, policies, enforce, userId)) {
                return 1;
            }
            targetPath = targetPath2;
            try {
                boolean idmapCreated = this.mIdmapDaemon.createIdmap(targetPath2, overlayBasePath, overlayName, policies, enforce, userId) != null;
                return idmapCreated ? 3 : 0;
            } catch (java.lang.Exception e) {
                e = e;
            }
        } catch (java.lang.Exception e2) {
            e = e2;
            targetPath = targetPath2;
        }
        android.util.Slog.w("OverlayManager", "failed to generate idmap for " + targetPath + " and " + overlayBasePath, e);
        return 0;
    }

    boolean removeIdmap(android.content.om.OverlayInfo oi, int userId) {
        if (com.android.server.om.OverlayManagerService.DEBUG) {
            android.util.Slog.d("OverlayManager", "remove idmap for " + oi.baseCodePath);
        }
        try {
            return this.mIdmapDaemon.removeIdmap(oi.baseCodePath, userId);
        } catch (java.lang.Exception e) {
            android.util.Slog.w("OverlayManager", "failed to remove idmap for " + oi.baseCodePath, e);
            return false;
        }
    }

    boolean idmapExists(android.content.om.OverlayInfo oi) {
        return this.mIdmapDaemon.idmapExists(oi.baseCodePath, oi.userId);
    }

    java.util.List<android.os.FabricatedOverlayInfo> getFabricatedOverlayInfos() {
        return this.mIdmapDaemon.getFabricatedOverlayInfos();
    }

    android.os.FabricatedOverlayInfo createFabricatedOverlay(android.os.FabricatedOverlayInternal overlay) {
        return this.mIdmapDaemon.createFabricatedOverlay(overlay);
    }

    boolean deleteFabricatedOverlay(java.lang.String path) {
        return this.mIdmapDaemon.deleteFabricatedOverlay(path);
    }

    java.lang.String dumpIdmap(java.lang.String overlayPath) {
        return this.mIdmapDaemon.dumpIdmap(overlayPath);
    }

    private boolean enforceOverlayable(com.android.server.pm.pkg.PackageState overlayPackageState, com.android.server.pm.pkg.AndroidPackage overlayPackage) {
        if (overlayPackage.getTargetSdkVersion() >= 29) {
            return true;
        }
        if (overlayPackageState.isVendor()) {
            return VENDOR_IS_Q_OR_LATER;
        }
        return (overlayPackageState.isSystem() || overlayPackage.isSignedWithPlatformKey()) ? false : true;
    }

    private int calculateFulfilledPolicies(com.android.server.pm.pkg.AndroidPackage targetPackage, com.android.server.pm.pkg.PackageState overlayPackageState, com.android.server.pm.pkg.AndroidPackage overlayPackage, int userId) {
        int fulfilledPolicies = 1;
        if (this.mPackageManager.signaturesMatching(targetPackage.getPackageName(), overlayPackage.getPackageName(), userId)) {
            fulfilledPolicies = 1 | 16;
        }
        if (matchesActorSignature(targetPackage, overlayPackage, userId)) {
            fulfilledPolicies |= 128;
        }
        if (!android.text.TextUtils.isEmpty(this.mConfigSignaturePackage) && this.mPackageManager.signaturesMatching(this.mConfigSignaturePackage, overlayPackage.getPackageName(), userId)) {
            fulfilledPolicies |= 256;
        }
        if (overlayPackageState.isVendor()) {
            return fulfilledPolicies | 4;
        }
        if (overlayPackageState.isProduct()) {
            return fulfilledPolicies | 8;
        }
        if (overlayPackageState.isOdm()) {
            return fulfilledPolicies | 32;
        }
        if (overlayPackageState.isOem()) {
            return fulfilledPolicies | 64;
        }
        if (overlayPackageState.isSystem() || overlayPackageState.isSystemExt()) {
            return fulfilledPolicies | 2;
        }
        return fulfilledPolicies;
    }

    private boolean matchesActorSignature(com.android.server.pm.pkg.AndroidPackage targetPackage, com.android.server.pm.pkg.AndroidPackage overlayPackage, int userId) {
        java.lang.String targetOverlayableName = overlayPackage.getOverlayTargetOverlayableName();
        if (targetOverlayableName != null && !this.mPackageManager.getNamedActors().isEmpty()) {
            try {
                android.content.om.OverlayableInfo overlayableInfo = this.mPackageManager.getOverlayableForTarget(targetPackage.getPackageName(), targetOverlayableName, userId);
                if (overlayableInfo != null && overlayableInfo.actor != null) {
                    java.lang.String actorPackageName = (java.lang.String) com.android.server.om.OverlayActorEnforcer.getPackageNameForActor(overlayableInfo.actor, this.mPackageManager.getNamedActors()).first;
                    if (this.mPackageManager.signaturesMatching(actorPackageName, overlayPackage.getPackageName(), userId)) {
                        return true;
                    }
                    return false;
                }
                return false;
            } catch (java.io.IOException e) {
                return false;
            }
        }
        return false;
    }
}
