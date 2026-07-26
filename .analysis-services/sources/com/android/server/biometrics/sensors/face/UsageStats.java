package com.android.server.biometrics.sensors.face;

/* JADX INFO: loaded from: classes.dex */
public class UsageStats {
    private static final int EVENT_LOG_SIZE = 100;
    private int mAcceptCount;
    private long mAcceptLatency;
    private int mAuthAttemptCount;
    private android.content.Context mContext;
    private int mErrorCount;
    private long mErrorLatency;
    private int mRejectCount;
    private long mRejectLatency;
    private java.util.ArrayDeque<com.android.server.biometrics.sensors.face.UsageStats.AuthenticationEvent> mAuthenticationEvents = new java.util.ArrayDeque<>();
    private android.util.SparseIntArray mErrorFrequencyMap = new android.util.SparseIntArray();
    private android.util.SparseLongArray mErrorLatencyMap = new android.util.SparseLongArray();

    public static final class AuthenticationEvent {
        private boolean mAuthenticated;
        private int mError;
        private long mLatency;
        private long mStartTime;
        private int mUser;
        private int mVendorError;

        public AuthenticationEvent(long startTime, long latency, boolean authenticated, int error, int vendorError, int user) {
            this.mStartTime = startTime;
            this.mLatency = latency;
            this.mAuthenticated = authenticated;
            this.mError = error;
            this.mVendorError = vendorError;
            this.mUser = user;
        }

        public java.lang.String toString(android.content.Context context) {
            return "Start: " + this.mStartTime + "\tLatency: " + this.mLatency + "\tAuthenticated: " + this.mAuthenticated + "\tError: " + this.mError + "\tVendorCode: " + this.mVendorError + "\tUser: " + this.mUser + "\t" + android.hardware.face.FaceManager.getErrorString(context, this.mError, this.mVendorError);
        }
    }

    public UsageStats(android.content.Context context) {
        this.mContext = context;
    }

    public void addEvent(com.android.server.biometrics.sensors.face.UsageStats.AuthenticationEvent event) {
        this.mAuthAttemptCount++;
        if (this.mAuthenticationEvents.size() >= 100) {
            this.mAuthenticationEvents.removeFirst();
        }
        this.mAuthenticationEvents.add(event);
        if (event.mAuthenticated) {
            this.mAcceptCount++;
            this.mAcceptLatency += event.mLatency;
        } else if (event.mError == 0) {
            this.mRejectCount++;
            this.mRejectLatency += event.mLatency;
        } else {
            this.mErrorCount++;
            this.mErrorLatency += event.mLatency;
            this.mErrorFrequencyMap.put(event.mError, this.mErrorFrequencyMap.get(event.mError, 0) + 1);
            this.mErrorLatencyMap.put(event.mError, this.mErrorLatencyMap.get(event.mError, 0L) + event.mLatency);
        }
    }

    public void print(java.io.PrintWriter pw) {
        pw.println("Printing most recent events since last reboot(" + this.mAuthenticationEvents.size() + " events)");
        for (com.android.server.biometrics.sensors.face.UsageStats.AuthenticationEvent event : this.mAuthenticationEvents) {
            pw.println(event.toString(this.mContext));
        }
        pw.println("");
        pw.println("Accept Count: " + this.mAcceptCount + "\tLatency: " + this.mAcceptLatency + "\tAverage: " + (this.mAcceptCount > 0 ? this.mAcceptLatency / ((long) this.mAcceptCount) : 0L));
        pw.println("Reject Count: " + this.mRejectCount + "\tLatency: " + this.mRejectLatency + "\tAverage: " + (this.mRejectCount > 0 ? this.mRejectLatency / ((long) this.mRejectCount) : 0L));
        pw.println("Total Error Count: " + this.mErrorCount + "\tLatency: " + this.mErrorLatency + "\tAverage: " + (this.mErrorCount > 0 ? this.mErrorLatency / ((long) this.mErrorCount) : 0L));
        pw.println("Total Attempts: " + this.mAuthAttemptCount);
        pw.println("");
        for (int i = 0; i < this.mErrorFrequencyMap.size(); i++) {
            int key = this.mErrorFrequencyMap.keyAt(i);
            int count = this.mErrorFrequencyMap.get(key);
            pw.println("Error" + key + "\tCount: " + count + "\tLatency: " + this.mErrorLatencyMap.get(key, 0L) + "\tAverage: " + (count > 0 ? this.mErrorLatencyMap.get(key, 0L) / ((long) count) : 0L) + "\t" + android.hardware.face.FaceManager.getErrorString(this.mContext, key, 0));
        }
    }
}
