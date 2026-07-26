package com.android.server.credentials.metrics;

/* JADX INFO: loaded from: classes.dex */
public class ChosenProviderFinalPhaseMetric {
    private static final java.lang.String TAG = "ChosenFinalPhaseMetric";
    private final int mSessionIdCaller;
    private final int mSessionIdProvider;
    private boolean mUiReturned = false;
    private int mChosenUid = -1;
    private int mPreQueryPhaseLatencyMicroseconds = -1;
    private int mQueryPhaseLatencyMicroseconds = -1;
    private long mServiceBeganTimeNanoseconds = -1;
    private long mQueryStartTimeNanoseconds = -1;
    private long mQueryEndTimeNanoseconds = -1;
    private long mUiCallStartTimeNanoseconds = -1;
    private long mUiCallEndTimeNanoseconds = -1;
    private long mFinalFinishTimeNanoseconds = -1;
    private int mOemUiUid = -1;
    private int mFallbackUiUid = -1;
    private com.android.server.credentials.metrics.OemUiUsageStatus mOemUiUsageStatus = com.android.server.credentials.metrics.OemUiUsageStatus.UNKNOWN;
    private int mChosenProviderStatus = -1;
    private boolean mHasException = false;
    private java.lang.String mFrameworkException = "";
    private com.android.server.credentials.metrics.shared.ResponseCollective mResponseCollective = new com.android.server.credentials.metrics.shared.ResponseCollective(java.util.Map.of(), java.util.Map.of());
    private boolean mIsPrimary = false;

    public ChosenProviderFinalPhaseMetric(int sessionIdCaller, int sessionIdProvider) {
        this.mSessionIdCaller = sessionIdCaller;
        this.mSessionIdProvider = sessionIdProvider;
    }

    public int getChosenUid() {
        return this.mChosenUid;
    }

    public void setChosenUid(int chosenUid) {
        this.mChosenUid = chosenUid;
    }

    public void setPreQueryPhaseLatencyMicroseconds(int preQueryPhaseLatencyMicroseconds) {
        this.mPreQueryPhaseLatencyMicroseconds = preQueryPhaseLatencyMicroseconds;
    }

    public void setQueryPhaseLatencyMicroseconds(int queryPhaseLatencyMicroseconds) {
        this.mQueryPhaseLatencyMicroseconds = queryPhaseLatencyMicroseconds;
    }

    public int getPreQueryPhaseLatencyMicroseconds() {
        return this.mPreQueryPhaseLatencyMicroseconds;
    }

    public int getQueryPhaseLatencyMicroseconds() {
        return this.mQueryPhaseLatencyMicroseconds;
    }

    public int getUiPhaseLatencyMicroseconds() {
        return (int) ((this.mUiCallEndTimeNanoseconds - this.mUiCallStartTimeNanoseconds) / 1000);
    }

    public int getEntireProviderLatencyMicroseconds() {
        return (int) ((this.mFinalFinishTimeNanoseconds - this.mQueryStartTimeNanoseconds) / 1000);
    }

    public int getEntireLatencyMicroseconds() {
        return (int) ((this.mFinalFinishTimeNanoseconds - this.mServiceBeganTimeNanoseconds) / 1000);
    }

    public void setServiceBeganTimeNanoseconds(long serviceBeganTimeNanoseconds) {
        this.mServiceBeganTimeNanoseconds = serviceBeganTimeNanoseconds;
    }

    public void setQueryStartTimeNanoseconds(long queryStartTimeNanoseconds) {
        this.mQueryStartTimeNanoseconds = queryStartTimeNanoseconds;
    }

    public void setQueryEndTimeNanoseconds(long queryEndTimeNanoseconds) {
        this.mQueryEndTimeNanoseconds = queryEndTimeNanoseconds;
    }

    public void setUiCallStartTimeNanoseconds(long uiCallStartTimeNanoseconds) {
        this.mUiCallStartTimeNanoseconds = uiCallStartTimeNanoseconds;
    }

    public void setUiCallEndTimeNanoseconds(long uiCallEndTimeNanoseconds) {
        this.mUiCallEndTimeNanoseconds = uiCallEndTimeNanoseconds;
    }

    public void setFinalFinishTimeNanoseconds(long finalFinishTimeNanoseconds) {
        this.mFinalFinishTimeNanoseconds = finalFinishTimeNanoseconds;
    }

    public long getServiceBeganTimeNanoseconds() {
        return this.mServiceBeganTimeNanoseconds;
    }

    public long getQueryStartTimeNanoseconds() {
        return this.mQueryStartTimeNanoseconds;
    }

    public long getQueryEndTimeNanoseconds() {
        return this.mQueryEndTimeNanoseconds;
    }

    public long getUiCallStartTimeNanoseconds() {
        return this.mUiCallStartTimeNanoseconds;
    }

    public long getUiCallEndTimeNanoseconds() {
        return this.mUiCallEndTimeNanoseconds;
    }

    public long getFinalFinishTimeNanoseconds() {
        return this.mFinalFinishTimeNanoseconds;
    }

    public int getTimestampFromReferenceStartMicroseconds(long specificTimestamp) {
        if (specificTimestamp < this.mServiceBeganTimeNanoseconds) {
            android.util.Slog.i(TAG, "The timestamp is before service started, falling back to default int");
            return -1;
        }
        return (int) ((specificTimestamp - this.mServiceBeganTimeNanoseconds) / 1000);
    }

    public int getChosenProviderStatus() {
        return this.mChosenProviderStatus;
    }

    public void setChosenProviderStatus(int chosenProviderStatus) {
        this.mChosenProviderStatus = chosenProviderStatus;
    }

    public int getSessionIdProvider() {
        return this.mSessionIdProvider;
    }

    public void setUiReturned(boolean uiReturned) {
        this.mUiReturned = uiReturned;
    }

    public boolean isUiReturned() {
        return this.mUiReturned;
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

    public int getSessionIdCaller() {
        return this.mSessionIdCaller;
    }

    public void setPrimary(boolean primary) {
        this.mIsPrimary = primary;
    }

    public boolean isPrimary() {
        return this.mIsPrimary;
    }

    public void setOemUiUid(int oemUiUid) {
        this.mOemUiUid = oemUiUid;
    }

    public int getOemUiUid() {
        return this.mOemUiUid;
    }

    public void setFallbackUiUid(int fallbackUiUid) {
        this.mFallbackUiUid = fallbackUiUid;
    }

    public int getFallbackUiUid() {
        return this.mFallbackUiUid;
    }

    public void setOemUiUsageStatus(com.android.server.credentials.metrics.OemUiUsageStatus oemUiUsageStatus) {
        this.mOemUiUsageStatus = oemUiUsageStatus;
    }

    public int getOemUiUsageStatus() {
        return this.mOemUiUsageStatus.getLoggingInt();
    }
}
