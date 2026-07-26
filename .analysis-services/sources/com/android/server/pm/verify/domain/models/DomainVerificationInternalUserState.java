package com.android.server.pm.verify.domain.models;

/* JADX INFO: loaded from: classes2.dex */
public class DomainVerificationInternalUserState {
    private final android.util.ArraySet<java.lang.String> mEnabledHosts;
    private boolean mLinkHandlingAllowed;
    private final int mUserId;

    public DomainVerificationInternalUserState(int userId) {
        this.mLinkHandlingAllowed = true;
        this.mUserId = userId;
        this.mEnabledHosts = new android.util.ArraySet<>();
    }

    public com.android.server.pm.verify.domain.models.DomainVerificationInternalUserState addHosts(android.util.ArraySet<java.lang.String> newHosts) {
        this.mEnabledHosts.addAll((android.util.ArraySet<? extends java.lang.String>) newHosts);
        return this;
    }

    public com.android.server.pm.verify.domain.models.DomainVerificationInternalUserState addHosts(java.util.Set<java.lang.String> newHosts) {
        this.mEnabledHosts.addAll(newHosts);
        return this;
    }

    public com.android.server.pm.verify.domain.models.DomainVerificationInternalUserState removeHost(java.lang.String host) {
        this.mEnabledHosts.remove(host);
        return this;
    }

    public com.android.server.pm.verify.domain.models.DomainVerificationInternalUserState removeHosts(android.util.ArraySet<java.lang.String> newHosts) {
        this.mEnabledHosts.removeAll((android.util.ArraySet<? extends java.lang.String>) newHosts);
        return this;
    }

    public com.android.server.pm.verify.domain.models.DomainVerificationInternalUserState removeHosts(java.util.Set<java.lang.String> newHosts) {
        this.mEnabledHosts.removeAll(newHosts);
        return this;
    }

    public com.android.server.pm.verify.domain.models.DomainVerificationInternalUserState retainHosts(java.util.Set<java.lang.String> hosts) {
        this.mEnabledHosts.retainAll(hosts);
        return this;
    }

    public DomainVerificationInternalUserState(int userId, android.util.ArraySet<java.lang.String> enabledHosts, boolean linkHandlingAllowed) {
        this.mLinkHandlingAllowed = true;
        this.mUserId = userId;
        com.android.internal.util.AnnotationValidations.validate(android.annotation.UserIdInt.class, (android.annotation.UserIdInt) null, this.mUserId);
        this.mEnabledHosts = enabledHosts;
        com.android.internal.util.AnnotationValidations.validate(android.annotation.NonNull.class, (android.annotation.NonNull) null, this.mEnabledHosts);
        this.mLinkHandlingAllowed = linkHandlingAllowed;
    }

    public int getUserId() {
        return this.mUserId;
    }

    public android.util.ArraySet<java.lang.String> getEnabledHosts() {
        return this.mEnabledHosts;
    }

    public boolean isLinkHandlingAllowed() {
        return this.mLinkHandlingAllowed;
    }

    public com.android.server.pm.verify.domain.models.DomainVerificationInternalUserState setLinkHandlingAllowed(boolean value) {
        this.mLinkHandlingAllowed = value;
        return this;
    }

    public java.lang.String toString() {
        return "DomainVerificationInternalUserState { userId = " + this.mUserId + ", enabledHosts = " + this.mEnabledHosts + ", linkHandlingAllowed = " + this.mLinkHandlingAllowed + " }";
    }

    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        com.android.server.pm.verify.domain.models.DomainVerificationInternalUserState that = (com.android.server.pm.verify.domain.models.DomainVerificationInternalUserState) o;
        if (this.mUserId == that.mUserId && java.util.Objects.equals(this.mEnabledHosts, that.mEnabledHosts) && this.mLinkHandlingAllowed == that.mLinkHandlingAllowed) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int _hash = (1 * 31) + this.mUserId;
        return (((_hash * 31) + java.util.Objects.hashCode(this.mEnabledHosts)) * 31) + java.lang.Boolean.hashCode(this.mLinkHandlingAllowed);
    }

    @java.lang.Deprecated
    private void __metadata() {
    }
}
