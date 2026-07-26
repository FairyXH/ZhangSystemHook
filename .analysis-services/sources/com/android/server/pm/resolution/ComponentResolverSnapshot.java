package com.android.server.pm.resolution;

/* JADX INFO: loaded from: classes2.dex */
public class ComponentResolverSnapshot extends com.android.server.pm.resolution.ComponentResolverBase {
    public ComponentResolverSnapshot(com.android.server.pm.resolution.ComponentResolver orig, com.android.server.pm.UserNeedsBadgingCache userNeedsBadgingCache) {
        super(com.android.server.pm.UserManagerService.getInstance());
        this.mActivities = new com.android.server.pm.resolution.ComponentResolver.ActivityIntentResolver(orig.mActivities, this.mUserManager, userNeedsBadgingCache);
        this.mProviders = new com.android.server.pm.resolution.ComponentResolver.ProviderIntentResolver(orig.mProviders, this.mUserManager);
        this.mReceivers = new com.android.server.pm.resolution.ComponentResolver.ReceiverIntentResolver(orig.mReceivers, this.mUserManager, userNeedsBadgingCache);
        this.mServices = new com.android.server.pm.resolution.ComponentResolver.ServiceIntentResolver(orig.mServices, this.mUserManager);
        this.mProvidersByAuthority = new android.util.ArrayMap<>(orig.mProvidersByAuthority);
    }
}
