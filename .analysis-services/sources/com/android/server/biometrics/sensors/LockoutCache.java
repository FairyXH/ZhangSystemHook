package com.android.server.biometrics.sensors;

/* JADX INFO: loaded from: classes.dex */
public class LockoutCache implements com.android.server.biometrics.sensors.LockoutTracker {
    private static final java.lang.String TAG = "LockoutCache";
    private final android.util.SparseIntArray mUserLockoutStates = new android.util.SparseIntArray();

    @Override // com.android.server.biometrics.sensors.LockoutTracker
    public void setLockoutModeForUser(int userId, int mode) {
        android.util.Slog.d(TAG, "Lockout for user: " + userId + " is " + mode);
        synchronized (this) {
            this.mUserLockoutStates.put(userId, mode);
        }
    }

    @Override // com.android.server.biometrics.sensors.LockoutTracker
    public int getLockoutModeForUser(int userId) {
        int i;
        synchronized (this) {
            i = this.mUserLockoutStates.get(userId, 0);
        }
        return i;
    }
}
