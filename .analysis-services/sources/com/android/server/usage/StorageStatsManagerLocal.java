package com.android.server.usage;

/* JADX INFO: loaded from: classes3.dex */
@android.annotation.SystemApi(client = android.annotation.SystemApi.Client.SYSTEM_SERVER)
public interface StorageStatsManagerLocal {

    public interface StorageStatsAugmenter {
        void augmentStatsForPackageForUser(android.content.pm.PackageStats packageStats, java.lang.String str, android.os.UserHandle userHandle, boolean z);

        void augmentStatsForUid(android.content.pm.PackageStats packageStats, int i, boolean z);

        void augmentStatsForUser(android.content.pm.PackageStats packageStats, android.os.UserHandle userHandle);
    }

    void registerStorageStatsAugmenter(com.android.server.usage.StorageStatsManagerLocal.StorageStatsAugmenter storageStatsAugmenter, java.lang.String str);
}
