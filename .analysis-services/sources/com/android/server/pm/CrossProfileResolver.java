package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public abstract class CrossProfileResolver {
    protected final com.android.server.pm.resolution.ComponentResolverApi mComponentResolver;
    protected final com.android.server.pm.UserManagerService mUserManager;

    public abstract java.util.List<com.android.server.pm.CrossProfileDomainInfo> filterResolveInfoWithDomainPreferredActivity(android.content.Intent intent, java.util.List<com.android.server.pm.CrossProfileDomainInfo> list, long j, int i, int i2, int i3);

    public abstract java.util.List<com.android.server.pm.CrossProfileDomainInfo> resolveIntent(com.android.server.pm.Computer computer, android.content.Intent intent, java.lang.String str, int i, int i2, long j, java.lang.String str2, java.util.List<com.android.server.pm.CrossProfileIntentFilter> list, boolean z, java.util.function.Function<java.lang.String, com.android.server.pm.pkg.PackageStateInternal> function);

    public CrossProfileResolver(com.android.server.pm.resolution.ComponentResolverApi componentResolver, com.android.server.pm.UserManagerService userManager) {
        this.mComponentResolver = componentResolver;
        this.mUserManager = userManager;
    }

    /* JADX WARN: Removed duplicated region for block: B:8:0x0014  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected final boolean isUserEnabled(int r5) {
        /*
            r4 = this;
            long r0 = android.os.Binder.clearCallingIdentity()
            com.android.server.pm.UserManagerService r2 = r4.mUserManager     // Catch: java.lang.Throwable -> L19
            android.content.pm.UserInfo r2 = r2.getUserInfo(r5)     // Catch: java.lang.Throwable -> L19
            if (r2 == 0) goto L14
            boolean r3 = r2.isEnabled()     // Catch: java.lang.Throwable -> L19
            if (r3 == 0) goto L14
            r3 = 1
            goto L15
        L14:
            r3 = 0
        L15:
            android.os.Binder.restoreCallingIdentity(r0)
            return r3
        L19:
            r2 = move-exception
            android.os.Binder.restoreCallingIdentity(r0)
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.CrossProfileResolver.isUserEnabled(int):boolean");
    }

    protected final java.util.List<com.android.server.pm.CrossProfileDomainInfo> filterIfNotSystemUser(java.util.List<com.android.server.pm.CrossProfileDomainInfo> crossProfileDomainInfos, int userId) {
        if (userId == 0) {
            return crossProfileDomainInfos;
        }
        for (int i = com.android.internal.util.CollectionUtils.size(crossProfileDomainInfos) - 1; i >= 0; i--) {
            android.content.pm.ResolveInfo info = crossProfileDomainInfos.get(i).mResolveInfo;
            if ((info.activityInfo.flags & 536870912) != 0) {
                crossProfileDomainInfos.remove(i);
            }
        }
        return crossProfileDomainInfos;
    }

    protected final android.content.pm.UserInfo getProfileParent(int userId) {
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            return this.mUserManager.getProfileParent(userId);
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }
}
