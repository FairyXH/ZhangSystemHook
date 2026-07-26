package com.android.server.content;

/* JADX INFO: loaded from: classes.dex */
class SyncAdapterStateFetcher {
    private final java.util.HashMap<android.content.pm.UserPackage, java.lang.Integer> mBucketCache = new java.util.HashMap<>();

    public int getStandbyBucket(int userId, java.lang.String packageName) {
        android.content.pm.UserPackage key = android.content.pm.UserPackage.of(userId, packageName);
        java.lang.Integer cached = this.mBucketCache.get(key);
        if (cached != null) {
            return cached.intValue();
        }
        android.app.usage.UsageStatsManagerInternal usmi = (android.app.usage.UsageStatsManagerInternal) com.android.server.LocalServices.getService(android.app.usage.UsageStatsManagerInternal.class);
        if (usmi == null) {
            return -1;
        }
        int value = usmi.getAppStandbyBucket(packageName, userId, android.os.SystemClock.elapsedRealtime());
        this.mBucketCache.put(key, java.lang.Integer.valueOf(value));
        return value;
    }

    public boolean isAppActive(int uid) {
        android.app.ActivityManagerInternal ami = (android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class);
        if (ami != null) {
            return ami.isUidActive(uid);
        }
        return false;
    }
}
