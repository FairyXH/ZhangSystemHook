package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public class CrossProfileIntentFilterHelper {
    private final android.content.Context mContext;
    private final com.android.server.pm.PackageManagerTracedLock mLock;
    private final com.android.server.pm.Settings mSettings;
    private final com.android.server.pm.UserManagerInternal mUserManagerInternal;
    private final com.android.server.pm.UserManagerService mUserManagerService;

    public CrossProfileIntentFilterHelper(com.android.server.pm.Settings settings, com.android.server.pm.UserManagerService userManagerService, com.android.server.pm.PackageManagerTracedLock lock, com.android.server.pm.UserManagerInternal userManagerInternal, android.content.Context context) {
        this.mSettings = settings;
        this.mUserManagerService = userManagerService;
        this.mLock = lock;
        this.mContext = context;
        this.mUserManagerInternal = userManagerInternal;
    }

    public void updateDefaultCrossProfileIntentFilter() {
        int parentUserId;
        for (android.content.pm.UserInfo userInfo : this.mUserManagerInternal.getUsers(false)) {
            android.content.pm.UserProperties currentUserProperties = this.mUserManagerInternal.getUserProperties(userInfo.id);
            if (currentUserProperties != null && currentUserProperties.getUpdateCrossProfileIntentFiltersOnOTA() && (parentUserId = this.mUserManagerInternal.getProfileParentId(userInfo.id)) != userInfo.id) {
                clearCrossProfileIntentFilters(userInfo.id, this.mContext.getOpPackageName(), java.lang.Integer.valueOf(parentUserId));
                clearCrossProfileIntentFilters(parentUserId, this.mContext.getOpPackageName(), java.lang.Integer.valueOf(userInfo.id));
                this.mUserManagerInternal.setDefaultCrossProfileIntentFilters(parentUserId, userInfo.id);
            }
        }
    }

    public void clearCrossProfileIntentFilters(int sourceUserId, java.lang.String ownerPackage, java.lang.Integer targetUserId) {
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                com.android.server.pm.CrossProfileIntentResolver resolver = this.mSettings.editCrossProfileIntentResolverLPw(sourceUserId);
                android.util.ArraySet<com.android.server.pm.CrossProfileIntentFilter> set = new android.util.ArraySet<>((java.util.Collection<? extends com.android.server.pm.CrossProfileIntentFilter>) resolver.filterSet());
                for (com.android.server.pm.CrossProfileIntentFilter filter : set) {
                    if (filter.getOwnerPackage().equals(ownerPackage) && ((targetUserId == null || filter.mTargetUserId == targetUserId.intValue()) && this.mUserManagerService.isCrossProfileIntentFilterAccessible(sourceUserId, filter.mTargetUserId, false))) {
                        resolver.removeFilter(filter);
                    }
                }
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
    }
}
