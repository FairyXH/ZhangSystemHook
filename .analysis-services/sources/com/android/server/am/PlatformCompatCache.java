package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
final class PlatformCompatCache {
    static final int CACHED_COMPAT_CHANGE_CAMERA_MICROPHONE_CAPABILITY = 1;
    static final long[] CACHED_COMPAT_CHANGE_IDS_MAPPING = {136274596, 136219221, 183972877};
    static final int CACHED_COMPAT_CHANGE_PROCESS_CAPABILITY = 0;
    static final int CACHED_COMPAT_CHANGE_USE_SHORT_FGS_USAGE_INTERACTION_TIME = 2;
    private static com.android.server.am.PlatformCompatCache sPlatformCompatCache;
    private final boolean mCacheEnabled;
    private final android.util.LongSparseArray<com.android.server.am.PlatformCompatCache.CacheItem> mCaches = new android.util.LongSparseArray<>();
    private final com.android.internal.compat.IPlatformCompat mIPlatformCompatProxy;
    private final com.android.server.compat.PlatformCompat mPlatformCompat;

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @interface CachedCompatChangeId {
    }

    private PlatformCompatCache(long[] compatChanges) {
        android.os.IBinder b = android.os.ServiceManager.getService("platform_compat");
        if (b instanceof com.android.server.compat.PlatformCompat) {
            this.mPlatformCompat = (com.android.server.compat.PlatformCompat) android.os.ServiceManager.getService("platform_compat");
            for (long changeId : compatChanges) {
                this.mCaches.put(changeId, new com.android.server.am.PlatformCompatCache.CacheItem(this.mPlatformCompat, changeId));
            }
            this.mIPlatformCompatProxy = null;
            this.mCacheEnabled = true;
            return;
        }
        this.mIPlatformCompatProxy = com.android.internal.compat.IPlatformCompat.Stub.asInterface(b);
        this.mPlatformCompat = null;
        this.mCacheEnabled = false;
    }

    static com.android.server.am.PlatformCompatCache getInstance() {
        if (sPlatformCompatCache == null) {
            sPlatformCompatCache = new com.android.server.am.PlatformCompatCache(new long[]{136274596, 136219221, 183972877});
        }
        return sPlatformCompatCache;
    }

    private boolean isChangeEnabled(long changeId, android.content.pm.ApplicationInfo app, boolean defaultValue) {
        try {
            return this.mCacheEnabled ? this.mCaches.get(changeId).isChangeEnabled(app) : this.mIPlatformCompatProxy.isChangeEnabled(changeId, app);
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(com.android.server.am.IActivityManagerServiceExt.TAG, "Error reading platform compat change " + changeId, e);
            return defaultValue;
        }
    }

    static boolean isChangeEnabled(int cachedCompatChangeId, android.content.pm.ApplicationInfo app, boolean defaultValue) {
        return getInstance().isChangeEnabled(CACHED_COMPAT_CHANGE_IDS_MAPPING[cachedCompatChangeId], app, defaultValue);
    }

    void invalidate(android.content.pm.ApplicationInfo app) {
        for (int i = this.mCaches.size() - 1; i >= 0; i--) {
            this.mCaches.valueAt(i).invalidate(app);
        }
    }

    void onApplicationInfoChanged(android.content.pm.ApplicationInfo app) {
        for (int i = this.mCaches.size() - 1; i >= 0; i--) {
            this.mCaches.valueAt(i).onApplicationInfoChanged(app);
        }
    }

    static class CacheItem implements com.android.server.compat.CompatChange.ChangeListener {
        private final long mChangeId;
        private final com.android.server.compat.PlatformCompat mPlatformCompat;
        private final java.lang.Object mLock = new java.lang.Object();
        private final android.util.ArrayMap<java.lang.String, android.util.Pair<java.lang.Boolean, java.lang.ref.WeakReference<android.content.pm.ApplicationInfo>>> mCache = new android.util.ArrayMap<>();

        CacheItem(com.android.server.compat.PlatformCompat platformCompat, long changeId) {
            this.mPlatformCompat = platformCompat;
            this.mChangeId = changeId;
            this.mPlatformCompat.registerListener(changeId, this);
        }

        boolean isChangeEnabled(android.content.pm.ApplicationInfo app) {
            synchronized (this.mLock) {
                int index = this.mCache.indexOfKey(app.packageName);
                if (index < 0) {
                    return fetchLocked(app, index);
                }
                android.util.Pair<java.lang.Boolean, java.lang.ref.WeakReference<android.content.pm.ApplicationInfo>> p = this.mCache.valueAt(index);
                if (((java.lang.ref.WeakReference) p.second).get() == app) {
                    return ((java.lang.Boolean) p.first).booleanValue();
                }
                return fetchLocked(app, index);
            }
        }

        void invalidate(android.content.pm.ApplicationInfo app) {
            synchronized (this.mLock) {
                this.mCache.remove(app.packageName);
            }
        }

        boolean fetchLocked(android.content.pm.ApplicationInfo app, int index) {
            android.util.Pair<java.lang.Boolean, java.lang.ref.WeakReference<android.content.pm.ApplicationInfo>> p = new android.util.Pair<>(java.lang.Boolean.valueOf(this.mPlatformCompat.isChangeEnabledInternalNoLogging(this.mChangeId, app)), new java.lang.ref.WeakReference(app));
            if (index >= 0) {
                this.mCache.setValueAt(index, p);
            } else {
                this.mCache.put(app.packageName, p);
            }
            return ((java.lang.Boolean) p.first).booleanValue();
        }

        void onApplicationInfoChanged(android.content.pm.ApplicationInfo app) {
            synchronized (this.mLock) {
                int index = this.mCache.indexOfKey(app.packageName);
                if (index >= 0) {
                    fetchLocked(app, index);
                }
            }
        }

        @Override // com.android.server.compat.CompatChange.ChangeListener
        public void onCompatChange(java.lang.String packageName) {
            synchronized (this.mLock) {
                int index = this.mCache.indexOfKey(packageName);
                if (index >= 0) {
                    android.content.pm.ApplicationInfo app = (android.content.pm.ApplicationInfo) ((java.lang.ref.WeakReference) this.mCache.valueAt(index).second).get();
                    if (app != null) {
                        fetchLocked(app, index);
                    } else {
                        this.mCache.removeAt(index);
                    }
                }
            }
        }
    }
}
