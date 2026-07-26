package com.android.server.companion.devicepresence;

/* JADX INFO: loaded from: classes.dex */
public class ObservableUuid {
    private final java.lang.String mPackageName;
    private final long mTimeApprovedMs;
    private final int mUserId;
    private final android.os.ParcelUuid mUuid;

    public ObservableUuid(int userId, android.os.ParcelUuid uuid, java.lang.String packageName, java.lang.Long timeApprovedMs) {
        this.mUserId = userId;
        this.mUuid = uuid;
        this.mPackageName = packageName;
        this.mTimeApprovedMs = timeApprovedMs.longValue();
    }

    public int getUserId() {
        return this.mUserId;
    }

    public android.os.ParcelUuid getUuid() {
        return this.mUuid;
    }

    public java.lang.String getPackageName() {
        return this.mPackageName;
    }

    public long getTimeApprovedMs() {
        return this.mTimeApprovedMs;
    }
}
