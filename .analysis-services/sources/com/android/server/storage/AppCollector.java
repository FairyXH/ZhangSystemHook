package com.android.server.storage;

/* JADX INFO: loaded from: classes3.dex */
public class AppCollector {
    private static java.lang.String TAG = "AppCollector";
    private final com.android.server.storage.AppCollector.BackgroundHandler mBackgroundHandler;
    private java.util.concurrent.CompletableFuture<java.util.List<android.content.pm.PackageStats>> mStats;

    public AppCollector(android.content.Context context, android.os.storage.VolumeInfo volume) {
        java.util.Objects.requireNonNull(volume);
        this.mBackgroundHandler = new com.android.server.storage.AppCollector.BackgroundHandler(com.android.internal.os.BackgroundThread.get().getLooper(), volume, context.getPackageManager(), (android.os.UserManager) context.getSystemService("user"), (android.app.usage.StorageStatsManager) context.getSystemService("storagestats"));
    }

    public java.util.List<android.content.pm.PackageStats> getPackageStats(long timeoutMillis) {
        synchronized (this) {
            if (this.mStats == null) {
                this.mStats = new java.util.concurrent.CompletableFuture<>();
                this.mBackgroundHandler.sendEmptyMessage(0);
            }
        }
        try {
            java.util.List<android.content.pm.PackageStats> value = this.mStats.get(timeoutMillis, java.util.concurrent.TimeUnit.MILLISECONDS);
            return value;
        } catch (java.lang.InterruptedException | java.util.concurrent.ExecutionException e) {
            android.util.Log.e(TAG, "An exception occurred while getting app storage", e);
            return null;
        } catch (java.util.concurrent.TimeoutException e2) {
            android.util.Log.e(TAG, "AppCollector timed out");
            return null;
        }
    }

    private class BackgroundHandler extends android.os.Handler {
        static final int MSG_START_LOADING_SIZES = 0;
        private final android.content.pm.PackageManager mPm;
        private final android.app.usage.StorageStatsManager mStorageStatsManager;
        private final android.os.UserManager mUm;
        private final android.os.storage.VolumeInfo mVolume;

        BackgroundHandler(android.os.Looper looper, android.os.storage.VolumeInfo volume, android.content.pm.PackageManager pm, android.os.UserManager um, android.app.usage.StorageStatsManager storageStatsManager) {
            super(looper);
            this.mVolume = volume;
            this.mPm = pm;
            this.mUm = um;
            this.mStorageStatsManager = storageStatsManager;
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    java.util.ArrayList arrayList = new java.util.ArrayList();
                    java.util.List<android.content.pm.UserInfo> users = this.mUm.getUsers();
                    int userSize = users.size();
                    for (int userCount = 0; userCount < userSize; userCount++) {
                        android.content.pm.UserInfo user = users.get(userCount);
                        java.util.List<android.content.pm.ApplicationInfo> apps = this.mPm.getInstalledApplicationsAsUser(512, user.id);
                        int size = apps.size();
                        for (int appCount = 0; appCount < size; appCount++) {
                            android.content.pm.ApplicationInfo app = apps.get(appCount);
                            if (java.util.Objects.equals(app.volumeUuid, this.mVolume.getFsUuid())) {
                                try {
                                    android.app.usage.StorageStats storageStats = this.mStorageStatsManager.queryStatsForPackage(app.storageUuid, app.packageName, user.getUserHandle());
                                    android.content.pm.PackageStats packageStats = new android.content.pm.PackageStats(app.packageName, user.id);
                                    packageStats.cacheSize = storageStats.getCacheBytes();
                                    packageStats.codeSize = storageStats.getAppBytes();
                                    packageStats.dataSize = storageStats.getDataBytes();
                                    arrayList.add(packageStats);
                                } catch (android.content.pm.PackageManager.NameNotFoundException | java.io.IOException e) {
                                    android.util.Log.e(com.android.server.storage.AppCollector.TAG, "An exception occurred while fetching app size", e);
                                }
                            }
                        }
                    }
                    com.android.server.storage.AppCollector.this.mStats.complete(arrayList);
                    break;
            }
        }
    }
}
