package com.android.server.pm.verify.domain;

/* JADX INFO: loaded from: classes2.dex */
public class DomainVerificationEnforcer {
    private com.android.server.pm.verify.domain.DomainVerificationEnforcer.Callback mCallback;
    private final android.content.Context mContext;

    public interface Callback {
        boolean doesUserExist(int i);

        boolean filterAppAccess(java.lang.String str, int i, int i2);
    }

    public DomainVerificationEnforcer(android.content.Context context) {
        this.mContext = context;
    }

    public void setCallback(com.android.server.pm.verify.domain.DomainVerificationEnforcer.Callback callback) {
        this.mCallback = callback;
    }

    public void assertInternal(int callingUid) {
        switch (callingUid) {
            case 0:
            case 1000:
            case 2000:
                return;
            default:
                throw new java.lang.SecurityException("Caller " + callingUid + " is not allowed to change internal state");
        }
    }

    public void assertApprovedQuerent(int callingUid, com.android.server.pm.verify.domain.proxy.DomainVerificationProxy proxy) {
        switch (callingUid) {
            case 0:
            case 1000:
            case 2000:
                break;
            default:
                if (!proxy.isCallerVerifier(callingUid)) {
                    this.mContext.enforcePermission("android.permission.DUMP", android.os.Binder.getCallingPid(), callingUid, "Caller " + callingUid + " is not allowed to query domain verification state");
                } else {
                    this.mContext.enforcePermission("android.permission.QUERY_ALL_PACKAGES", android.os.Binder.getCallingPid(), callingUid, "Caller " + callingUid + " does not hold android.permission.QUERY_ALL_PACKAGES");
                }
                break;
        }
    }

    public void assertApprovedVerifier(int callingUid, com.android.server.pm.verify.domain.proxy.DomainVerificationProxy proxy) throws java.lang.SecurityException {
        boolean isAllowed;
        switch (callingUid) {
            case 0:
            case 1000:
            case 2000:
                isAllowed = true;
                break;
            default:
                int callingPid = android.os.Binder.getCallingPid();
                boolean isLegacyVerificationAgent = false;
                if (this.mContext.checkPermission("android.permission.DOMAIN_VERIFICATION_AGENT", callingPid, callingUid) != 0) {
                    isLegacyVerificationAgent = this.mContext.checkPermission("android.permission.INTENT_FILTER_VERIFICATION_AGENT", callingPid, callingUid) == 0;
                    if (!isLegacyVerificationAgent) {
                        throw new java.lang.SecurityException("Caller " + callingUid + " does not hold android.permission.DOMAIN_VERIFICATION_AGENT");
                    }
                }
                if (!isLegacyVerificationAgent) {
                    this.mContext.enforcePermission("android.permission.QUERY_ALL_PACKAGES", callingPid, callingUid, "Caller " + callingUid + " does not hold android.permission.QUERY_ALL_PACKAGES");
                }
                isAllowed = proxy.isCallerVerifier(callingUid);
                break;
        }
        if (!isAllowed) {
            throw new java.lang.SecurityException("Caller " + callingUid + " is not the approved domain verification agent");
        }
    }

    public boolean assertApprovedUserStateQuerent(int callingUid, int callingUserId, java.lang.String packageName, int targetUserId) throws java.lang.SecurityException {
        if (callingUserId != targetUserId) {
            this.mContext.enforcePermission("android.permission.INTERACT_ACROSS_USERS", android.os.Binder.getCallingPid(), callingUid, "Caller is not allowed to edit other users");
        }
        if (!this.mCallback.doesUserExist(callingUserId)) {
            throw new java.lang.SecurityException("User " + callingUserId + " does not exist");
        }
        if (!this.mCallback.doesUserExist(targetUserId)) {
            throw new java.lang.SecurityException("User " + targetUserId + " does not exist");
        }
        return !this.mCallback.filterAppAccess(packageName, callingUid, targetUserId);
    }

    public boolean assertApprovedUserSelector(int callingUid, int callingUserId, java.lang.String packageName, int targetUserId) throws java.lang.SecurityException {
        if (callingUserId != targetUserId) {
            this.mContext.enforcePermission("android.permission.INTERACT_ACROSS_USERS", android.os.Binder.getCallingPid(), callingUid, "Caller is not allowed to edit other users");
        }
        this.mContext.enforcePermission("android.permission.UPDATE_DOMAIN_VERIFICATION_USER_SELECTION", android.os.Binder.getCallingPid(), callingUid, "Caller is not allowed to edit user selections");
        if (!this.mCallback.doesUserExist(callingUserId)) {
            throw new java.lang.SecurityException("User " + callingUserId + " does not exist");
        }
        if (!this.mCallback.doesUserExist(targetUserId)) {
            throw new java.lang.SecurityException("User " + targetUserId + " does not exist");
        }
        if (packageName == null) {
            return true;
        }
        return true ^ this.mCallback.filterAppAccess(packageName, callingUid, targetUserId);
    }

    public boolean callerIsLegacyUserSelector(int callingUid, int callingUserId, java.lang.String packageName, int targetUserId) {
        this.mContext.enforcePermission("android.permission.SET_PREFERRED_APPLICATIONS", android.os.Binder.getCallingPid(), callingUid, "Caller is not allowed to edit user state");
        if (callingUserId != targetUserId && this.mContext.checkPermission("android.permission.INTERACT_ACROSS_USERS", android.os.Binder.getCallingPid(), callingUid) != 0) {
            return false;
        }
        if (!this.mCallback.doesUserExist(callingUserId)) {
            throw new java.lang.SecurityException("User " + callingUserId + " does not exist");
        }
        if (!this.mCallback.doesUserExist(targetUserId)) {
            throw new java.lang.SecurityException("User " + targetUserId + " does not exist");
        }
        return !this.mCallback.filterAppAccess(packageName, callingUid, targetUserId);
    }

    public boolean callerIsLegacyUserQuerent(int callingUid, int callingUserId, java.lang.String packageName, int targetUserId) {
        if (callingUserId != targetUserId) {
            this.mContext.enforcePermission("android.permission.INTERACT_ACROSS_USERS_FULL", android.os.Binder.getCallingPid(), callingUid, "Caller is not allowed to edit other users");
        }
        if (!this.mCallback.doesUserExist(callingUserId)) {
            throw new java.lang.SecurityException("User " + callingUserId + " does not exist");
        }
        if (!this.mCallback.doesUserExist(targetUserId)) {
            throw new java.lang.SecurityException("User " + targetUserId + " does not exist");
        }
        return !this.mCallback.filterAppAccess(packageName, callingUid, targetUserId);
    }

    public void assertOwnerQuerent(int callingUid, int callingUserId, int targetUserId) {
        int callingPid = android.os.Binder.getCallingPid();
        if (callingUserId != targetUserId) {
            this.mContext.enforcePermission("android.permission.INTERACT_ACROSS_USERS", callingPid, callingUid, "Caller is not allowed to query other users");
        }
        this.mContext.enforcePermission("android.permission.QUERY_ALL_PACKAGES", callingPid, callingUid, "Caller " + callingUid + " does not hold android.permission.QUERY_ALL_PACKAGES");
        this.mContext.enforcePermission("android.permission.UPDATE_DOMAIN_VERIFICATION_USER_SELECTION", callingPid, callingUid, "Caller is not allowed to query user selections");
        if (!this.mCallback.doesUserExist(callingUserId)) {
            throw new java.lang.SecurityException("User " + callingUserId + " does not exist");
        }
        if (!this.mCallback.doesUserExist(targetUserId)) {
            throw new java.lang.SecurityException("User " + targetUserId + " does not exist");
        }
    }
}
