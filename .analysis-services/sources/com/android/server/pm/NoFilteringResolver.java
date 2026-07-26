package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public class NoFilteringResolver extends com.android.server.pm.CrossProfileResolver {
    private static final java.lang.String FLAG_ALLOW_INTENT_REDIRECTION_FOR_CLONE_PROFILE = "allow_intent_redirection_for_clone_profile";

    public static boolean isIntentRedirectionAllowed(android.content.Context context, com.android.internal.config.appcloning.AppCloningDeviceConfigHelper appCloningDeviceConfigHelper, boolean resolveForStart, long flags) {
        boolean canMatchCloneProfile = ((536870912 & flags) == 0 && (17179869184L & flags) == 0) ? false : true;
        if (isAppCloningBuildingBlocksEnabled(context, appCloningDeviceConfigHelper)) {
            return resolveForStart || (canMatchCloneProfile && hasPermission(context, "android.permission.QUERY_CLONED_APPS"));
        }
        return false;
    }

    public NoFilteringResolver(com.android.server.pm.resolution.ComponentResolverApi componentResolver, com.android.server.pm.UserManagerService userManagerService) {
        super(componentResolver, userManagerService);
    }

    @Override // com.android.server.pm.CrossProfileResolver
    public java.util.List<com.android.server.pm.CrossProfileDomainInfo> resolveIntent(com.android.server.pm.Computer computer, android.content.Intent intent, java.lang.String resolvedType, int userId, int targetUserId, long flags, java.lang.String pkgName, java.util.List<com.android.server.pm.CrossProfileIntentFilter> matchingFilters, boolean hasNonNegativePriorityResult, java.util.function.Function<java.lang.String, com.android.server.pm.pkg.PackageStateInternal> pkgSettingFunction) {
        java.util.List<android.content.pm.ResolveInfo> resolveInfos = this.mComponentResolver.queryActivities(computer, intent, resolvedType, flags, targetUserId);
        java.util.List<com.android.server.pm.CrossProfileDomainInfo> crossProfileDomainInfos = new java.util.ArrayList<>();
        if (resolveInfos != null) {
            for (int index = 0; index < resolveInfos.size(); index++) {
                crossProfileDomainInfos.add(new com.android.server.pm.CrossProfileDomainInfo(resolveInfos.get(index), 0, targetUserId));
            }
        }
        return filterIfNotSystemUser(crossProfileDomainInfos, userId);
    }

    @Override // com.android.server.pm.CrossProfileResolver
    public java.util.List<com.android.server.pm.CrossProfileDomainInfo> filterResolveInfoWithDomainPreferredActivity(android.content.Intent intent, java.util.List<com.android.server.pm.CrossProfileDomainInfo> crossProfileDomainInfos, long flags, int sourceUserId, int targetUserId, int highestApprovalLevel) {
        return crossProfileDomainInfos;
    }

    private static boolean hasPermission(android.content.Context context, java.lang.String permission) {
        return context.checkCallingOrSelfPermission(permission) == 0;
    }

    /* JADX WARN: Removed duplicated region for block: B:8:0x0019  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static boolean isAppCloningBuildingBlocksEnabled(android.content.Context r4, com.android.internal.config.appcloning.AppCloningDeviceConfigHelper r5) {
        /*
            long r0 = android.os.Binder.clearCallingIdentity()
            android.content.res.Resources r2 = r4.getResources()     // Catch: java.lang.Throwable -> L1e
            r3 = 17891686(0x1110166, float:2.6633297E-38)
            boolean r2 = r2.getBoolean(r3)     // Catch: java.lang.Throwable -> L1e
            if (r2 == 0) goto L19
            boolean r2 = r5.getEnableAppCloningBuildingBlocks()     // Catch: java.lang.Throwable -> L1e
            if (r2 == 0) goto L19
            r2 = 1
            goto L1a
        L19:
            r2 = 0
        L1a:
            android.os.Binder.restoreCallingIdentity(r0)
            return r2
        L1e:
            r2 = move-exception
            android.os.Binder.restoreCallingIdentity(r0)
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.NoFilteringResolver.isAppCloningBuildingBlocksEnabled(android.content.Context, com.android.internal.config.appcloning.AppCloningDeviceConfigHelper):boolean");
    }
}
