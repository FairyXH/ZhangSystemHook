package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public class FgsTempAllowList<E> {
    private static final int DEFAULT_MAX_SIZE = 100;
    private final java.lang.Object mLock;
    private int mMaxSize;
    private final android.util.SparseArray<android.util.Pair<java.lang.Long, E>> mTempAllowList;

    public FgsTempAllowList() {
        this.mTempAllowList = new android.util.SparseArray<>();
        this.mMaxSize = 100;
        this.mLock = new java.lang.Object();
    }

    public FgsTempAllowList(int maxSize) {
        this.mTempAllowList = new android.util.SparseArray<>();
        this.mMaxSize = 100;
        this.mLock = new java.lang.Object();
        if (maxSize <= 0) {
            android.util.Slog.e(com.android.server.am.IActivityManagerServiceExt.TAG, "Invalid FgsTempAllowList maxSize:" + maxSize + ", force default maxSize:100");
            this.mMaxSize = 100;
        } else {
            this.mMaxSize = maxSize;
        }
    }

    public void add(int uid, long durationMs, E entry) {
        synchronized (this.mLock) {
            if (durationMs <= 0) {
                android.util.Slog.e(com.android.server.am.IActivityManagerServiceExt.TAG, "FgsTempAllowList bad duration:" + durationMs + " key: " + uid);
                return;
            }
            long now = android.os.SystemClock.elapsedRealtime();
            int size = this.mTempAllowList.size();
            if (size > this.mMaxSize) {
                android.util.Slog.w(com.android.server.am.IActivityManagerServiceExt.TAG, "FgsTempAllowList length:" + size + " exceeds maxSize" + this.mMaxSize);
                for (int index = size - 1; index >= 0; index--) {
                    if (((java.lang.Long) this.mTempAllowList.valueAt(index).first).longValue() < now) {
                        this.mTempAllowList.removeAt(index);
                    }
                }
            }
            android.util.Pair<java.lang.Long, E> existing = this.mTempAllowList.get(uid);
            long expirationTime = now + durationMs;
            if (existing == null || ((java.lang.Long) existing.first).longValue() < expirationTime) {
                this.mTempAllowList.put(uid, new android.util.Pair<>(java.lang.Long.valueOf(expirationTime), entry));
            }
        }
    }

    public android.util.Pair<java.lang.Long, E> get(int uid) {
        synchronized (this.mLock) {
            int index = this.mTempAllowList.indexOfKey(uid);
            if (index < 0) {
                return null;
            }
            if (((java.lang.Long) this.mTempAllowList.valueAt(index).first).longValue() < android.os.SystemClock.elapsedRealtime()) {
                this.mTempAllowList.removeAt(index);
                return null;
            }
            return this.mTempAllowList.valueAt(index);
        }
    }

    public boolean isAllowed(int uid) {
        android.util.Pair<java.lang.Long, E> entry = get(uid);
        return entry != null;
    }

    public void removeUid(int uid) {
        synchronized (this.mLock) {
            this.mTempAllowList.remove(uid);
        }
    }

    public void removeAppId(int appId) {
        synchronized (this.mLock) {
            for (int i = this.mTempAllowList.size() - 1; i >= 0; i--) {
                int uid = this.mTempAllowList.keyAt(i);
                if (android.os.UserHandle.getAppId(uid) == appId) {
                    this.mTempAllowList.removeAt(i);
                }
            }
        }
    }

    public void forEach(java.util.function.BiConsumer<java.lang.Integer, android.util.Pair<java.lang.Long, E>> callback) {
        synchronized (this.mLock) {
            for (int i = 0; i < this.mTempAllowList.size(); i++) {
                int uid = this.mTempAllowList.keyAt(i);
                android.util.Pair<java.lang.Long, E> entry = this.mTempAllowList.valueAt(i);
                if (entry != null) {
                    callback.accept(java.lang.Integer.valueOf(uid), entry);
                }
            }
        }
    }
}
