package com.android.server.credentials.metrics;

/* JADX INFO: loaded from: classes.dex */
public class CandidatePhaseMetric {
    private static final java.lang.String TAG = "CandidateProviderMetric";
    private final int mSessionIdProvider;
    private boolean mQueryReturned = false;
    private int mCandidateUid = -1;
    private long mServiceBeganTimeNanoseconds = -1;
    private long mStartQueryTimeNanoseconds = -1;
    private long mQueryFinishTimeNanoseconds = -1;
    private int mProviderQueryStatus = -1;
    private boolean mHasException = false;
    private java.lang.String mFrameworkException = "";
    private com.android.server.credentials.metrics.shared.ResponseCollective mResponseCollective = new com.android.server.credentials.metrics.shared.ResponseCollective(java.util.Map.of(), java.util.Map.of());
    private boolean mIsPrimary = false;

    public CandidatePhaseMetric(int sessionIdTrackTwo) {
        this.mSessionIdProvider = sessionIdTrackTwo;
    }

    public void setServiceBeganTimeNanoseconds(long serviceBeganTimeNanoseconds) {
        this.mServiceBeganTimeNanoseconds = serviceBeganTimeNanoseconds;
    }

    public void setStartQueryTimeNanoseconds(long startQueryTimeNanoseconds) {
        this.mStartQueryTimeNanoseconds = startQueryTimeNanoseconds;
    }

    public void setQueryFinishTimeNanoseconds(long queryFinishTimeNanoseconds) {
        this.mQueryFinishTimeNanoseconds = queryFinishTimeNanoseconds;
    }

    public long getServiceBeganTimeNanoseconds() {
        return this.mServiceBeganTimeNanoseconds;
    }

    public long getStartQueryTimeNanoseconds() {
        return this.mStartQueryTimeNanoseconds;
    }

    public long getQueryFinishTimeNanoseconds() {
        return this.mQueryFinishTimeNanoseconds;
    }

    public int getQueryLatencyMicroseconds() {
        return (int) ((getQueryFinishTimeNanoseconds() - getStartQueryTimeNanoseconds()) / 1000);
    }

    public int getTimestampFromReferenceStartMicroseconds(long specificTimestamp) {
        if (specificTimestamp < this.mServiceBeganTimeNanoseconds) {
            android.util.Slog.i(TAG, "The timestamp is before service started, falling back to default int");
            return -1;
        }
        return (int) ((specificTimestamp - this.mServiceBeganTimeNanoseconds) / 1000);
    }

    public void setProviderQueryStatus(int providerQueryStatus) {
        this.mProviderQueryStatus = providerQueryStatus;
    }

    public int getProviderQueryStatus() {
        return this.mProviderQueryStatus;
    }

    public void setCandidateUid(int candidateUid) {
        this.mCandidateUid = candidateUid;
    }

    public int getCandidateUid() {
        return this.mCandidateUid;
    }

    public int getSessionIdProvider() {
        return this.mSessionIdProvider;
    }

    public void setQueryReturned(boolean queryReturned) {
        this.mQueryReturned = queryReturned;
    }

    public boolean isQueryReturned() {
        return this.mQueryReturned;
    }

    public void setHasException(boolean hasException) {
        this.mHasException = hasException;
    }

    public boolean isHasException() {
        return this.mHasException;
    }

    public void setResponseCollective(com.android.server.credentials.metrics.shared.ResponseCollective responseCollective) {
        this.mResponseCollective = responseCollective;
    }

    public com.android.server.credentials.metrics.shared.ResponseCollective getResponseCollective() {
        return this.mResponseCollective;
    }

    public void setFrameworkException(java.lang.String frameworkException) {
        this.mFrameworkException = frameworkException;
    }

    public java.lang.String getFrameworkException() {
        return this.mFrameworkException;
    }

    public void setPrimary(boolean primary) {
        this.mIsPrimary = primary;
    }

    public boolean isPrimary() {
        return this.mIsPrimary;
    }
}
