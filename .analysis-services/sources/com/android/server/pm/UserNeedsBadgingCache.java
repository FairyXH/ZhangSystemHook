package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public class UserNeedsBadgingCache {
    private final java.lang.Object mLock = new java.lang.Object();
    private final android.util.SparseBooleanArray mUserCache = new android.util.SparseBooleanArray();
    private final com.android.server.pm.UserManagerService mUserManager;

    public UserNeedsBadgingCache(com.android.server.pm.UserManagerService userManager) {
        this.mUserManager = userManager;
    }

    public void delete(int userId) {
        synchronized (this.mLock) {
            this.mUserCache.delete(userId);
        }
    }

    public boolean get(int userId) {
        synchronized (this.mLock) {
            int index = this.mUserCache.indexOfKey(userId);
            if (index >= 0) {
                return this.mUserCache.valueAt(index);
            }
            long token = android.os.Binder.clearCallingIdentity();
            try {
                android.content.pm.UserInfo userInfo = this.mUserManager.getUserInfo(userId);
                boolean b = userInfo != null && userInfo.isManagedProfile();
                synchronized (this.mLock) {
                    this.mUserCache.put(userId, b);
                }
                return b;
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }
    }
}
