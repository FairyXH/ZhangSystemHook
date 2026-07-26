package com.android.server.pm.resolution;

/* JADX INFO: loaded from: classes2.dex */
public interface IComponentResolverExt {
    default void onAddActivitiesLocked(com.android.internal.pm.pkg.component.ParsedActivity a, com.android.server.pm.pkg.AndroidPackage pkg) {
    }

    default void onAddProvidersLocked(com.android.internal.pm.pkg.component.ParsedProvider p, com.android.server.pm.pkg.AndroidPackage pkg) {
    }

    default void onAddReceiversLocked(com.android.internal.pm.pkg.component.ParsedActivity a, com.android.server.pm.pkg.AndroidPackage pkg) {
    }

    default void onAddServicesLocked(com.android.internal.pm.pkg.component.ParsedService s, com.android.server.pm.pkg.AndroidPackage pkg) {
    }

    default boolean shouldOverrideProviderByAuthority(java.lang.String authName, com.android.server.pm.pkg.AndroidPackage pkg, com.android.internal.pm.pkg.component.ParsedProvider other) {
        return false;
    }

    public interface IStaticExt {
        default boolean onIsFilterStopped(com.android.server.pm.pkg.PackageStateInternal pkg, boolean isStopped) {
            return isStopped;
        }
    }
}
