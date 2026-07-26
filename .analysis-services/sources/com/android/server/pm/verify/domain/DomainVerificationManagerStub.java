package com.android.server.pm.verify.domain;

/* JADX INFO: loaded from: classes2.dex */
public class DomainVerificationManagerStub extends android.content.pm.verify.domain.IDomainVerificationManager.Stub {
    private final com.android.server.pm.verify.domain.DomainVerificationService mService;

    public DomainVerificationManagerStub(com.android.server.pm.verify.domain.DomainVerificationService service) {
        this.mService = service;
    }

    public void setUriRelativeFilterGroups(java.lang.String packageName, android.os.Bundle domainToGroupsBundle) {
        try {
            this.mService.setUriRelativeFilterGroups(packageName, domainToGroupsBundle);
        } catch (java.lang.Exception e) {
            throw rethrow(e);
        }
    }

    public android.os.Bundle getUriRelativeFilterGroups(java.lang.String packageName, java.util.List<java.lang.String> domains) {
        try {
            return this.mService.getUriRelativeFilterGroups(packageName, domains);
        } catch (java.lang.Exception e) {
            throw rethrow(e);
        }
    }

    public java.util.List<java.lang.String> queryValidVerificationPackageNames() {
        try {
            return this.mService.queryValidVerificationPackageNames();
        } catch (java.lang.Exception e) {
            throw rethrow(e);
        }
    }

    public android.content.pm.verify.domain.DomainVerificationInfo getDomainVerificationInfo(java.lang.String packageName) {
        try {
            return this.mService.getDomainVerificationInfo(packageName);
        } catch (java.lang.Exception e) {
            throw rethrow(e);
        }
    }

    public int setDomainVerificationStatus(java.lang.String domainSetId, android.content.pm.verify.domain.DomainSet domainSet, int state) {
        try {
            return this.mService.setDomainVerificationStatus(java.util.UUID.fromString(domainSetId), domainSet.getDomains(), state);
        } catch (java.lang.Exception e) {
            throw rethrow(e);
        }
    }

    public void setDomainVerificationLinkHandlingAllowed(java.lang.String packageName, boolean allowed, int userId) {
        try {
            this.mService.setDomainVerificationLinkHandlingAllowed(packageName, allowed, userId);
        } catch (java.lang.Exception e) {
            throw rethrow(e);
        }
    }

    public int setDomainVerificationUserSelection(java.lang.String domainSetId, android.content.pm.verify.domain.DomainSet domainSet, boolean enabled, int userId) {
        try {
            return this.mService.setDomainVerificationUserSelection(java.util.UUID.fromString(domainSetId), domainSet.getDomains(), enabled, userId);
        } catch (java.lang.Exception e) {
            throw rethrow(e);
        }
    }

    public android.content.pm.verify.domain.DomainVerificationUserState getDomainVerificationUserState(java.lang.String packageName, int userId) {
        try {
            return this.mService.getDomainVerificationUserState(packageName, userId);
        } catch (java.lang.Exception e) {
            throw rethrow(e);
        }
    }

    public java.util.List<android.content.pm.verify.domain.DomainOwner> getOwnersForDomain(java.lang.String domain, int userId) {
        try {
            java.util.Objects.requireNonNull(domain);
            return this.mService.getOwnersForDomain(domain, userId);
        } catch (java.lang.Exception e) {
            throw rethrow(e);
        }
    }

    private java.lang.RuntimeException rethrow(java.lang.Exception exception) throws java.lang.RuntimeException {
        if (exception instanceof android.content.pm.PackageManager.NameNotFoundException) {
            return new android.os.ServiceSpecificException(1);
        }
        if (exception instanceof java.lang.RuntimeException) {
            return (java.lang.RuntimeException) exception;
        }
        return new java.lang.RuntimeException(exception);
    }
}
