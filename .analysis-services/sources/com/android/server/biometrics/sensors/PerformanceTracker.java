package com.android.server.biometrics.sensors;

/* JADX INFO: loaded from: classes.dex */
public class PerformanceTracker {
    private static final java.lang.String TAG = "PerformanceTracker";
    private static android.util.SparseArray<com.android.server.biometrics.sensors.PerformanceTracker> sTrackers;
    private final android.util.SparseArray<com.android.server.biometrics.sensors.PerformanceTracker.Info> mAllUsersInfo = new android.util.SparseArray<>();
    private int mHALDeathCount;

    public static com.android.server.biometrics.sensors.PerformanceTracker getInstanceForSensorId(int sensorId) {
        if (sTrackers == null) {
            sTrackers = new android.util.SparseArray<>();
        }
        if (!sTrackers.contains(sensorId)) {
            sTrackers.put(sensorId, new com.android.server.biometrics.sensors.PerformanceTracker());
        }
        return sTrackers.get(sensorId);
    }

    private static class Info {
        int mAccept;
        int mAcceptCrypto;
        int mAcquire;
        int mAcquireCrypto;
        int mPermanentLockout;
        int mReject;
        int mRejectCrypto;
        int mTimedLockout;

        private Info() {
        }
    }

    private PerformanceTracker() {
    }

    private void createUserEntryIfNecessary(int userId) {
        if (!this.mAllUsersInfo.contains(userId)) {
            this.mAllUsersInfo.put(userId, new com.android.server.biometrics.sensors.PerformanceTracker.Info());
        }
    }

    public void incrementAuthForUser(int userId, boolean accepted) {
        createUserEntryIfNecessary(userId);
        if (accepted) {
            this.mAllUsersInfo.get(userId).mAccept++;
        } else {
            this.mAllUsersInfo.get(userId).mReject++;
        }
    }

    void incrementCryptoAuthForUser(int userId, boolean accepted) {
        createUserEntryIfNecessary(userId);
        if (accepted) {
            this.mAllUsersInfo.get(userId).mAcceptCrypto++;
        } else {
            this.mAllUsersInfo.get(userId).mRejectCrypto++;
        }
    }

    public void incrementAcquireForUser(int userId, boolean isCrypto) {
        createUserEntryIfNecessary(userId);
        if (isCrypto) {
            this.mAllUsersInfo.get(userId).mAcquireCrypto++;
        } else {
            this.mAllUsersInfo.get(userId).mAcquire++;
        }
    }

    public void incrementTimedLockoutForUser(int userId) {
        createUserEntryIfNecessary(userId);
        this.mAllUsersInfo.get(userId).mTimedLockout++;
    }

    public void incrementPermanentLockoutForUser(int userId) {
        createUserEntryIfNecessary(userId);
        this.mAllUsersInfo.get(userId).mPermanentLockout++;
    }

    public void incrementHALDeathCount() {
        this.mHALDeathCount++;
    }

    public void clear() {
        this.mAllUsersInfo.clear();
        this.mHALDeathCount = 0;
    }

    public int getAcceptForUser(int userId) {
        if (this.mAllUsersInfo.contains(userId)) {
            return this.mAllUsersInfo.get(userId).mAccept;
        }
        return 0;
    }

    public int getRejectForUser(int userId) {
        if (this.mAllUsersInfo.contains(userId)) {
            return this.mAllUsersInfo.get(userId).mReject;
        }
        return 0;
    }

    public int getAcquireForUser(int userId) {
        if (this.mAllUsersInfo.contains(userId)) {
            return this.mAllUsersInfo.get(userId).mAcquire;
        }
        return 0;
    }

    public int getAcceptCryptoForUser(int userId) {
        if (this.mAllUsersInfo.contains(userId)) {
            return this.mAllUsersInfo.get(userId).mAcceptCrypto;
        }
        return 0;
    }

    public int getRejectCryptoForUser(int userId) {
        if (this.mAllUsersInfo.contains(userId)) {
            return this.mAllUsersInfo.get(userId).mRejectCrypto;
        }
        return 0;
    }

    public int getAcquireCryptoForUser(int userId) {
        if (this.mAllUsersInfo.contains(userId)) {
            return this.mAllUsersInfo.get(userId).mAcquireCrypto;
        }
        return 0;
    }

    public int getTimedLockoutForUser(int userId) {
        if (this.mAllUsersInfo.contains(userId)) {
            return this.mAllUsersInfo.get(userId).mTimedLockout;
        }
        return 0;
    }

    public int getPermanentLockoutForUser(int userId) {
        if (this.mAllUsersInfo.contains(userId)) {
            return this.mAllUsersInfo.get(userId).mPermanentLockout;
        }
        return 0;
    }

    public int getHALDeathCount() {
        return this.mHALDeathCount;
    }
}
