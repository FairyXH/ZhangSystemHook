package com.android.server.om;

/* JADX INFO: loaded from: classes2.dex */
public class OverlayActorEnforcer {
    private final com.android.server.om.PackageManagerHelper mPackageManager;

    public enum ActorState {
        TARGET_NOT_FOUND,
        NO_PACKAGES_FOR_UID,
        MISSING_TARGET_OVERLAYABLE_NAME,
        MISSING_LEGACY_PERMISSION,
        ERROR_READING_OVERLAYABLE,
        UNABLE_TO_GET_TARGET_OVERLAYABLE,
        MISSING_OVERLAYABLE,
        INVALID_OVERLAYABLE_ACTOR_NAME,
        NO_NAMED_ACTORS,
        MISSING_NAMESPACE,
        MISSING_ACTOR_NAME,
        ACTOR_NOT_FOUND,
        ACTOR_NOT_PREINSTALLED,
        INVALID_ACTOR,
        ALLOWED
    }

    static android.util.Pair<java.lang.String, com.android.server.om.OverlayActorEnforcer.ActorState> getPackageNameForActor(java.lang.String actorUriString, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> namedActors) {
        android.net.Uri actorUri = android.net.Uri.parse(actorUriString);
        java.lang.String actorScheme = actorUri.getScheme();
        java.util.List<java.lang.String> actorPathSegments = actorUri.getPathSegments();
        if (!"overlay".equals(actorScheme) || com.android.internal.util.CollectionUtils.size(actorPathSegments) != 1) {
            return android.util.Pair.create(null, com.android.server.om.OverlayActorEnforcer.ActorState.INVALID_OVERLAYABLE_ACTOR_NAME);
        }
        if (namedActors.isEmpty()) {
            return android.util.Pair.create(null, com.android.server.om.OverlayActorEnforcer.ActorState.NO_NAMED_ACTORS);
        }
        java.lang.String actorNamespace = actorUri.getAuthority();
        java.util.Map<java.lang.String, java.lang.String> namespace = namedActors.get(actorNamespace);
        if (com.android.internal.util.ArrayUtils.isEmpty(namespace)) {
            return android.util.Pair.create(null, com.android.server.om.OverlayActorEnforcer.ActorState.MISSING_NAMESPACE);
        }
        java.lang.String actorName = actorPathSegments.get(0);
        java.lang.String packageName = namespace.get(actorName);
        if (android.text.TextUtils.isEmpty(packageName)) {
            return android.util.Pair.create(null, com.android.server.om.OverlayActorEnforcer.ActorState.MISSING_ACTOR_NAME);
        }
        return android.util.Pair.create(packageName, com.android.server.om.OverlayActorEnforcer.ActorState.ALLOWED);
    }

    public OverlayActorEnforcer(com.android.server.om.PackageManagerHelper packageManager) {
        this.mPackageManager = packageManager;
    }

    void enforceActor(android.content.om.OverlayInfo overlayInfo, java.lang.String methodName, int callingUid, int userId) throws java.lang.SecurityException {
        com.android.server.om.OverlayActorEnforcer.ActorState actorState = isAllowedActor(methodName, overlayInfo, callingUid, userId);
        if (actorState == com.android.server.om.OverlayActorEnforcer.ActorState.ALLOWED) {
            return;
        }
        java.lang.String targetOverlayableName = overlayInfo.targetOverlayableName;
        java.lang.String errorMessage = "UID" + callingUid + " is not allowed to call " + methodName + " for " + (android.text.TextUtils.isEmpty(targetOverlayableName) ? "" : targetOverlayableName + " in ") + overlayInfo.targetPackageName + " for user " + userId;
        android.util.Slog.w("OverlayManager", errorMessage + " because " + actorState);
        throw new java.lang.SecurityException(errorMessage);
    }

    public com.android.server.om.OverlayActorEnforcer.ActorState isAllowedActor(java.lang.String methodName, android.content.om.OverlayInfo overlayInfo, int callingUid, int userId) {
        switch (callingUid) {
            case 0:
            case 1000:
                return com.android.server.om.OverlayActorEnforcer.ActorState.ALLOWED;
            default:
                java.lang.String targetPackageName = overlayInfo.targetPackageName;
                com.android.server.pm.pkg.PackageState targetPkgState = this.mPackageManager.getPackageStateForUser(targetPackageName, userId);
                com.android.server.pm.pkg.AndroidPackage targetPkg = targetPkgState == null ? null : targetPkgState.getAndroidPackage();
                if (targetPkg == null) {
                    return com.android.server.om.OverlayActorEnforcer.ActorState.TARGET_NOT_FOUND;
                }
                if (targetPkg.isDebuggable()) {
                    return com.android.server.om.OverlayActorEnforcer.ActorState.ALLOWED;
                }
                java.lang.String[] callingPackageNames = this.mPackageManager.getPackagesForUid(callingUid);
                if (com.android.internal.util.ArrayUtils.isEmpty(callingPackageNames)) {
                    return com.android.server.om.OverlayActorEnforcer.ActorState.NO_PACKAGES_FOR_UID;
                }
                if (com.android.internal.util.ArrayUtils.contains(callingPackageNames, targetPackageName)) {
                    return com.android.server.om.OverlayActorEnforcer.ActorState.ALLOWED;
                }
                java.lang.String targetOverlayableName = overlayInfo.targetOverlayableName;
                if (android.text.TextUtils.isEmpty(targetOverlayableName)) {
                    try {
                        if (this.mPackageManager.doesTargetDefineOverlayable(targetPackageName, userId)) {
                            return com.android.server.om.OverlayActorEnforcer.ActorState.MISSING_TARGET_OVERLAYABLE_NAME;
                        }
                        try {
                            this.mPackageManager.enforcePermission("android.permission.CHANGE_OVERLAY_PACKAGES", methodName);
                            return com.android.server.om.OverlayActorEnforcer.ActorState.ALLOWED;
                        } catch (java.lang.SecurityException e) {
                            return com.android.server.om.OverlayActorEnforcer.ActorState.MISSING_LEGACY_PERMISSION;
                        }
                    } catch (java.io.IOException e2) {
                        return com.android.server.om.OverlayActorEnforcer.ActorState.ERROR_READING_OVERLAYABLE;
                    }
                }
                try {
                    android.content.om.OverlayableInfo targetOverlayable = this.mPackageManager.getOverlayableForTarget(targetPackageName, targetOverlayableName, userId);
                    if (targetOverlayable == null) {
                        return com.android.server.om.OverlayActorEnforcer.ActorState.MISSING_OVERLAYABLE;
                    }
                    java.lang.String actor = targetOverlayable.actor;
                    if (android.text.TextUtils.isEmpty(actor)) {
                        try {
                            this.mPackageManager.enforcePermission("android.permission.CHANGE_OVERLAY_PACKAGES", methodName);
                            return com.android.server.om.OverlayActorEnforcer.ActorState.ALLOWED;
                        } catch (java.lang.SecurityException e3) {
                            return com.android.server.om.OverlayActorEnforcer.ActorState.MISSING_LEGACY_PERMISSION;
                        }
                    }
                    java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> namedActors = this.mPackageManager.getNamedActors();
                    android.util.Pair<java.lang.String, com.android.server.om.OverlayActorEnforcer.ActorState> actorUriPair = getPackageNameForActor(actor, namedActors);
                    com.android.server.om.OverlayActorEnforcer.ActorState actorUriState = (com.android.server.om.OverlayActorEnforcer.ActorState) actorUriPair.second;
                    if (actorUriState != com.android.server.om.OverlayActorEnforcer.ActorState.ALLOWED) {
                        return actorUriState;
                    }
                    java.lang.String actorPackageName = (java.lang.String) actorUriPair.first;
                    com.android.server.pm.pkg.PackageState actorPackageState = this.mPackageManager.getPackageStateForUser(actorPackageName, userId);
                    if (actorPackageState == null || actorPackageState.getAndroidPackage() == null) {
                        return com.android.server.om.OverlayActorEnforcer.ActorState.ACTOR_NOT_FOUND;
                    }
                    if (!actorPackageState.isSystem()) {
                        return com.android.server.om.OverlayActorEnforcer.ActorState.ACTOR_NOT_PREINSTALLED;
                    }
                    if (com.android.internal.util.ArrayUtils.contains(callingPackageNames, actorPackageName)) {
                        return com.android.server.om.OverlayActorEnforcer.ActorState.ALLOWED;
                    }
                    return com.android.server.om.OverlayActorEnforcer.ActorState.INVALID_ACTOR;
                } catch (java.io.IOException e4) {
                    return com.android.server.om.OverlayActorEnforcer.ActorState.UNABLE_TO_GET_TARGET_OVERLAYABLE;
                }
        }
    }
}
