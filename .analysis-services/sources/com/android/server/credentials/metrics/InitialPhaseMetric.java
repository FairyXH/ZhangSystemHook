package com.android.server.credentials.metrics;

/* JADX INFO: loaded from: classes.dex */
public class InitialPhaseMetric {
    private static final java.lang.String TAG = "InitialPhaseMetric";
    private final int mSessionIdCaller;
    private int mApiName = com.android.server.credentials.metrics.ApiName.UNKNOWN.getMetricCode();
    private int mCallerUid = -1;
    private long mCredentialServiceStartedTimeNanoseconds = -1;
    private long mCredentialServiceBeginQueryTimeNanoseconds = -1;
    private boolean mOriginSpecified = false;
    private java.util.Map<java.lang.String, java.lang.Integer> mRequestCounts = new java.util.LinkedHashMap();
    private int mAutofillSessionId = -1;
    private int mAutofillRequestId = -1;

    public InitialPhaseMetric(int sessionIdTrackOne) {
        this.mSessionIdCaller = sessionIdTrackOne;
    }

    public int getServiceStartToQueryLatencyMicroseconds() {
        return (int) ((this.mCredentialServiceStartedTimeNanoseconds - this.mCredentialServiceBeginQueryTimeNanoseconds) / 1000);
    }

    public void setCredentialServiceStartedTimeNanoseconds(long credentialServiceStartedTimeNanoseconds) {
        this.mCredentialServiceStartedTimeNanoseconds = credentialServiceStartedTimeNanoseconds;
    }

    public void setCredentialServiceBeginQueryTimeNanoseconds(long credentialServiceBeginQueryTimeNanoseconds) {
        this.mCredentialServiceBeginQueryTimeNanoseconds = credentialServiceBeginQueryTimeNanoseconds;
    }

    public long getCredentialServiceStartedTimeNanoseconds() {
        return this.mCredentialServiceStartedTimeNanoseconds;
    }

    public long getCredentialServiceBeginQueryTimeNanoseconds() {
        return this.mCredentialServiceBeginQueryTimeNanoseconds;
    }

    public void setApiName(int apiName) {
        this.mApiName = apiName;
    }

    public int getApiName() {
        return this.mApiName;
    }

    public void setCallerUid(int callerUid) {
        this.mCallerUid = callerUid;
    }

    public int getCallerUid() {
        return this.mCallerUid;
    }

    public int getSessionIdCaller() {
        return this.mSessionIdCaller;
    }

    public int getCountRequestClassType() {
        return this.mRequestCounts.size();
    }

    public void setOriginSpecified(boolean originSpecified) {
        this.mOriginSpecified = originSpecified;
    }

    public boolean isOriginSpecified() {
        return this.mOriginSpecified;
    }

    public void setAutofillSessionId(int autofillSessionId) {
        this.mAutofillSessionId = autofillSessionId;
    }

    public int getAutofillSessionId() {
        return this.mAutofillSessionId;
    }

    public void setAutofillRequestId(int autofillRequestId) {
        this.mAutofillRequestId = autofillRequestId;
    }

    public int getAutofillRequestId() {
        return this.mAutofillRequestId;
    }

    public void setRequestCounts(java.util.Map<java.lang.String, java.lang.Integer> requestCounts) {
        this.mRequestCounts = requestCounts;
    }

    public java.lang.String[] getUniqueRequestStrings() {
        java.lang.String[] result = new java.lang.String[this.mRequestCounts.keySet().size()];
        this.mRequestCounts.keySet().toArray(result);
        return result;
    }

    public int[] getUniqueRequestCounts() {
        return this.mRequestCounts.values().stream().mapToInt(new com.android.server.audio.AudioService$$ExternalSyntheticLambda0()).toArray();
    }
}
