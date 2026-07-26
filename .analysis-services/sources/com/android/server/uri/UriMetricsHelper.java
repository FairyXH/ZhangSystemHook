package com.android.server.uri;

/* JADX INFO: loaded from: classes3.dex */
final class UriMetricsHelper {
    private static final android.app.StatsManager.PullAtomMetadata DAILY_PULL_METADATA = new android.app.StatsManager.PullAtomMetadata.Builder().setCoolDownMillis(java.util.concurrent.TimeUnit.DAYS.toMillis(1)).build();
    private final android.content.Context mContext;
    private final com.android.server.uri.UriMetricsHelper.PersistentUriGrantsProvider mPersistentUriGrantsProvider;

    interface PersistentUriGrantsProvider {
        java.util.ArrayList<com.android.server.uri.UriPermission> providePersistentUriGrants();
    }

    UriMetricsHelper(android.content.Context context, com.android.server.uri.UriMetricsHelper.PersistentUriGrantsProvider provider) {
        this.mContext = context;
        this.mPersistentUriGrantsProvider = provider;
    }

    void registerPuller() {
        android.app.StatsManager statsManager = (android.app.StatsManager) this.mContext.getSystemService(android.app.StatsManager.class);
        statsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.PERSISTENT_URI_PERMISSIONS_AMOUNT_PER_PACKAGE, DAILY_PULL_METADATA, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, new android.app.StatsManager.StatsPullAtomCallback() { // from class: com.android.server.uri.UriMetricsHelper$$ExternalSyntheticLambda0
            public final int onPullAtom(int i, java.util.List list) {
                return this.f$0.lambda$registerPuller$0(i, list);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ int lambda$registerPuller$0(int atomTag, java.util.List data) {
        reportPersistentUriPermissionsPerPackage(data);
        return 0;
    }

    void reportPersistentUriFlushed(int amount) {
        com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.PERSISTENT_URI_PERMISSIONS_FLUSHED, amount);
    }

    private void reportPersistentUriPermissionsPerPackage(java.util.List<android.util.StatsEvent> data) {
        java.util.ArrayList<com.android.server.uri.UriPermission> persistentUriGrants = this.mPersistentUriGrantsProvider.providePersistentUriGrants();
        android.util.SparseArray<java.lang.Integer> perUidCount = new android.util.SparseArray<>();
        int persistentUriGrantsSize = persistentUriGrants.size();
        for (int i = 0; i < persistentUriGrantsSize; i++) {
            com.android.server.uri.UriPermission uriPermission = persistentUriGrants.get(i);
            perUidCount.put(uriPermission.targetUid, java.lang.Integer.valueOf(perUidCount.get(uriPermission.targetUid, 0).intValue() + 1));
        }
        int perUidCountSize = perUidCount.size();
        for (int i2 = 0; i2 < perUidCountSize; i2++) {
            int uid = perUidCount.keyAt(i2);
            int amount = perUidCount.valueAt(i2).intValue();
            data.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(com.android.internal.util.FrameworkStatsLog.PERSISTENT_URI_PERMISSIONS_AMOUNT_PER_PACKAGE, uid, amount));
        }
    }
}
