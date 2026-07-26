package com.android.server.credentials.metrics;

/* JADX INFO: loaded from: classes.dex */
public class BrowsedAuthenticationMetric {
    private static final java.lang.String TAG = "AuthenticationMetric";
    private final int mSessionIdProvider;
    private int mProviderUid = -1;
    private com.android.server.credentials.metrics.shared.ResponseCollective mAuthEntryCollective = new com.android.server.credentials.metrics.shared.ResponseCollective(java.util.Map.of(), java.util.Map.of());
    private boolean mHasException = false;
    private java.lang.String mFrameworkException = "";
    private int mProviderStatus = -1;
    private boolean mAuthReturned = false;

    public BrowsedAuthenticationMetric(int sessionIdProvider) {
        this.mSessionIdProvider = sessionIdProvider;
    }

    public int getSessionIdProvider() {
        return this.mSessionIdProvider;
    }

    public void setProviderUid(int providerUid) {
        this.mProviderUid = providerUid;
    }

    public int getProviderUid() {
        return this.mProviderUid;
    }

    public void setAuthEntryCollective(com.android.server.credentials.metrics.shared.ResponseCollective authEntryCollective) {
        this.mAuthEntryCollective = authEntryCollective;
    }

    public com.android.server.credentials.metrics.shared.ResponseCollective getAuthEntryCollective() {
        return this.mAuthEntryCollective;
    }

    public void setHasException(boolean hasException) {
        this.mHasException = hasException;
    }

    public void setFrameworkException(java.lang.String frameworkException) {
        this.mFrameworkException = frameworkException;
    }

    public void setProviderStatus(int providerStatus) {
        this.mProviderStatus = providerStatus;
    }

    public void setAuthReturned(boolean authReturned) {
        this.mAuthReturned = authReturned;
    }

    public boolean isAuthReturned() {
        return this.mAuthReturned;
    }

    public int getProviderStatus() {
        return this.mProviderStatus;
    }

    public java.lang.String getFrameworkException() {
        return this.mFrameworkException;
    }

    public boolean isHasException() {
        return this.mHasException;
    }
}
