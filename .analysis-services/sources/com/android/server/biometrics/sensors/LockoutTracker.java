package com.android.server.biometrics.sensors;

/* JADX INFO: loaded from: classes.dex */
public interface LockoutTracker {
    public static final int LOCKOUT_NONE = 0;
    public static final int LOCKOUT_PERMANENT = 2;
    public static final int LOCKOUT_TIMED = 1;

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface LockoutMode {
    }

    int getLockoutModeForUser(int i);

    void setLockoutModeForUser(int i, int i2);

    default void resetFailedAttemptsForUser(boolean clearAttemptCounter, int userId) {
    }

    default void addFailedAttemptForUser(int userId) {
    }
}
