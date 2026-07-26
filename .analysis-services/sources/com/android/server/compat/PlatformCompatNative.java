package com.android.server.compat;

/* JADX INFO: loaded from: classes.dex */
public class PlatformCompatNative extends com.android.internal.compat.IPlatformCompatNative.Stub {
    private final com.android.server.compat.PlatformCompat mPlatformCompat;

    public PlatformCompatNative(com.android.server.compat.PlatformCompat platformCompat) {
        this.mPlatformCompat = platformCompat;
    }

    public void reportChangeByPackageName(long changeId, java.lang.String packageName, int userId) {
        this.mPlatformCompat.reportChangeByPackageName(changeId, packageName, userId);
    }

    public void reportChangeByUid(long changeId, int uid) {
        this.mPlatformCompat.reportChangeByUid(changeId, uid);
    }

    public boolean isChangeEnabledByPackageName(long changeId, java.lang.String packageName, int userId) {
        return this.mPlatformCompat.isChangeEnabledByPackageName(changeId, packageName, userId);
    }

    public boolean isChangeEnabledByUid(long changeId, int uid) {
        return this.mPlatformCompat.isChangeEnabledByUid(changeId, uid);
    }
}
