package com.android.server.restrictions;

/* JADX INFO: loaded from: classes3.dex */
public final class RestrictionsManagerService extends com.android.server.SystemService {
    static final boolean DEBUG = false;
    static final java.lang.String LOG_TAG = "RestrictionsManagerService";
    private final com.android.server.restrictions.RestrictionsManagerService.RestrictionsManagerImpl mRestrictionsManagerImpl;

    public RestrictionsManagerService(android.content.Context context) {
        super(context);
        this.mRestrictionsManagerImpl = new com.android.server.restrictions.RestrictionsManagerService.RestrictionsManagerImpl(context);
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        publishBinderService("restrictions", this.mRestrictionsManagerImpl);
    }

    class RestrictionsManagerImpl extends android.content.IRestrictionsManager.Stub {
        final android.content.Context mContext;
        private final android.app.admin.IDevicePolicyManager mDpm;
        private final android.app.admin.DevicePolicyManagerInternal mDpmInternal;
        private final android.os.IUserManager mUm;

        public RestrictionsManagerImpl(android.content.Context context) {
            this.mContext = context;
            this.mUm = com.android.server.restrictions.RestrictionsManagerService.this.getBinderService("user");
            this.mDpm = com.android.server.restrictions.RestrictionsManagerService.this.getBinderService("device_policy");
            this.mDpmInternal = (android.app.admin.DevicePolicyManagerInternal) com.android.server.restrictions.RestrictionsManagerService.this.getLocalService(android.app.admin.DevicePolicyManagerInternal.class);
        }

        @java.lang.Deprecated
        public android.os.Bundle getApplicationRestrictions(java.lang.String packageName) throws android.os.RemoteException {
            return this.mUm.getApplicationRestrictions(packageName);
        }

        public java.util.List<android.os.Bundle> getApplicationRestrictionsPerAdminForUser(int userId, java.lang.String packageName) throws android.os.RemoteException {
            if (this.mDpmInternal != null) {
                return this.mDpmInternal.getApplicationRestrictionsPerAdminForUser(packageName, userId);
            }
            return new java.util.ArrayList();
        }

        public boolean hasRestrictionsProvider() throws android.os.RemoteException {
            int userHandle = android.os.UserHandle.getCallingUserId();
            if (this.mDpm == null) {
                return false;
            }
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                return this.mDpm.getRestrictionsProvider(userHandle) != null;
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public void requestPermission(java.lang.String packageName, java.lang.String requestType, java.lang.String requestId, android.os.PersistableBundle requestData) throws android.os.RemoteException {
            int callingUid = android.os.Binder.getCallingUid();
            int userHandle = android.os.UserHandle.getUserId(callingUid);
            if (this.mDpm != null) {
                long ident = android.os.Binder.clearCallingIdentity();
                try {
                    android.content.ComponentName restrictionsProvider = this.mDpm.getRestrictionsProvider(userHandle);
                    if (restrictionsProvider == null) {
                        throw new java.lang.IllegalStateException("Cannot request permission without a restrictions provider registered");
                    }
                    enforceCallerMatchesPackage(callingUid, packageName, "Package name does not match caller ");
                    android.content.Intent intent = new android.content.Intent("android.content.action.REQUEST_PERMISSION");
                    intent.setComponent(restrictionsProvider);
                    intent.putExtra("android.content.extra.PACKAGE_NAME", packageName);
                    intent.putExtra("android.content.extra.REQUEST_TYPE", requestType);
                    intent.putExtra("android.content.extra.REQUEST_ID", requestId);
                    intent.putExtra("android.content.extra.REQUEST_BUNDLE", requestData);
                    this.mContext.sendBroadcastAsUser(intent, new android.os.UserHandle(userHandle));
                } finally {
                    android.os.Binder.restoreCallingIdentity(ident);
                }
            }
        }

        public android.content.Intent createLocalApprovalIntent() throws android.os.RemoteException {
            int userHandle = android.os.UserHandle.getCallingUserId();
            if (this.mDpm != null) {
                long ident = android.os.Binder.clearCallingIdentity();
                try {
                    android.content.ComponentName restrictionsProvider = this.mDpm.getRestrictionsProvider(userHandle);
                    if (restrictionsProvider == null) {
                        throw new java.lang.IllegalStateException("Cannot request permission without a restrictions provider registered");
                    }
                    java.lang.String providerPackageName = restrictionsProvider.getPackageName();
                    android.content.Intent intent = new android.content.Intent("android.content.action.REQUEST_LOCAL_APPROVAL");
                    intent.setPackage(providerPackageName);
                    android.content.pm.ResolveInfo ri = android.app.AppGlobals.getPackageManager().resolveIntent(intent, (java.lang.String) null, 0L, userHandle);
                    if (ri != null && ri.activityInfo != null && ri.activityInfo.exported) {
                        intent.setComponent(new android.content.ComponentName(ri.activityInfo.packageName, ri.activityInfo.name));
                        return intent;
                    }
                    return null;
                } finally {
                    android.os.Binder.restoreCallingIdentity(ident);
                }
            }
            return null;
        }

        public void notifyPermissionResponse(java.lang.String packageName, android.os.PersistableBundle response) throws android.os.RemoteException {
            int callingUid = android.os.Binder.getCallingUid();
            int userHandle = android.os.UserHandle.getUserId(callingUid);
            if (this.mDpm != null) {
                long ident = android.os.Binder.clearCallingIdentity();
                try {
                    android.content.ComponentName permProvider = this.mDpm.getRestrictionsProvider(userHandle);
                    if (permProvider == null) {
                        throw new java.lang.SecurityException("No restrictions provider registered for user");
                    }
                    enforceCallerMatchesPackage(callingUid, permProvider.getPackageName(), "Restrictions provider does not match caller ");
                    android.content.Intent responseIntent = new android.content.Intent("android.content.action.PERMISSION_RESPONSE_RECEIVED");
                    responseIntent.setPackage(packageName);
                    responseIntent.putExtra("android.content.extra.RESPONSE_BUNDLE", response);
                    this.mContext.sendBroadcastAsUser(responseIntent, new android.os.UserHandle(userHandle));
                } finally {
                    android.os.Binder.restoreCallingIdentity(ident);
                }
            }
        }

        private void enforceCallerMatchesPackage(int callingUid, java.lang.String packageName, java.lang.String message) {
            try {
                java.lang.String[] pkgs = android.app.AppGlobals.getPackageManager().getPackagesForUid(callingUid);
                if (pkgs != null && !com.android.internal.util.ArrayUtils.contains(pkgs, packageName)) {
                    throw new java.lang.SecurityException(message + callingUid);
                }
            } catch (android.os.RemoteException e) {
            }
        }
    }
}
