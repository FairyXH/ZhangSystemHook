package com.android.server.biometrics;

/* JADX INFO: loaded from: classes.dex */
public class AuthenticationStats {
    private static final float FRR_NOT_ENOUGH_ATTEMPTS = -1.0f;
    private static final java.lang.String TAG = "AuthenticationStats";
    private int mEnrollmentNotifications;
    private final int mModality;
    private int mRejectedAttempts;
    private int mTotalAttempts;
    private final int mUserId;

    public AuthenticationStats(int userId, int totalAttempts, int rejectedAttempts, int enrollmentNotifications, int modality) {
        this.mUserId = userId;
        this.mTotalAttempts = totalAttempts;
        this.mRejectedAttempts = rejectedAttempts;
        this.mEnrollmentNotifications = enrollmentNotifications;
        this.mModality = modality;
    }

    public AuthenticationStats(int userId, int modality) {
        this.mUserId = userId;
        this.mTotalAttempts = 0;
        this.mRejectedAttempts = 0;
        this.mEnrollmentNotifications = 0;
        this.mModality = modality;
    }

    public int getUserId() {
        return this.mUserId;
    }

    public int getTotalAttempts() {
        return this.mTotalAttempts;
    }

    public int getRejectedAttempts() {
        return this.mRejectedAttempts;
    }

    public int getEnrollmentNotifications() {
        return this.mEnrollmentNotifications;
    }

    public int getModality() {
        return this.mModality;
    }

    public float getFrr() {
        if (this.mTotalAttempts > 0) {
            return this.mRejectedAttempts / this.mTotalAttempts;
        }
        return -1.0f;
    }

    public void authenticate(boolean authenticated) {
        if (!authenticated) {
            this.mRejectedAttempts++;
        }
        this.mTotalAttempts++;
    }

    public void resetData() {
        this.mTotalAttempts = 0;
        this.mRejectedAttempts = 0;
        android.util.Slog.d(TAG, "Reset Counters.");
    }

    public void updateNotificationCounter() {
        this.mEnrollmentNotifications++;
    }

    public boolean equals(java.lang.Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof com.android.server.biometrics.AuthenticationStats)) {
            return false;
        }
        com.android.server.biometrics.AuthenticationStats target = (com.android.server.biometrics.AuthenticationStats) obj;
        return getUserId() == target.getUserId() && getTotalAttempts() == target.getTotalAttempts() && getRejectedAttempts() == target.getRejectedAttempts() && getEnrollmentNotifications() == target.getEnrollmentNotifications() && getModality() == target.getModality();
    }

    public int hashCode() {
        return java.lang.String.format("userId: %d, totalAttempts: %d, rejectedAttempts: %d, enrollmentNotifications: %d, modality: %d", java.lang.Integer.valueOf(this.mUserId), java.lang.Integer.valueOf(this.mTotalAttempts), java.lang.Integer.valueOf(this.mRejectedAttempts), java.lang.Integer.valueOf(this.mEnrollmentNotifications), java.lang.Integer.valueOf(this.mModality)).hashCode();
    }
}
