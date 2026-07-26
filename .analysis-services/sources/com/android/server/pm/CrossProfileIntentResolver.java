package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
class CrossProfileIntentResolver extends com.android.server.pm.WatchedIntentResolver<com.android.server.pm.CrossProfileIntentFilter, com.android.server.pm.CrossProfileIntentFilter> implements com.android.server.utils.Snappable {
    final com.android.server.utils.SnapshotCache<com.android.server.pm.CrossProfileIntentResolver> mSnapshot;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.IntentResolver
    public com.android.server.pm.CrossProfileIntentFilter[] newArray(int size) {
        return new com.android.server.pm.CrossProfileIntentFilter[size];
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.IntentResolver
    public boolean isPackageForFilter(java.lang.String packageName, com.android.server.pm.CrossProfileIntentFilter filter) {
        return (filter.mFlags & 8) != 0;
    }

    @Override // com.android.server.pm.WatchedIntentResolver, com.android.server.IntentResolver
    protected void sortResults(java.util.List<com.android.server.pm.CrossProfileIntentFilter> results) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.IntentResolver
    public android.content.IntentFilter getIntentFilter(com.android.server.pm.CrossProfileIntentFilter input) {
        return input.getIntentFilter();
    }

    CrossProfileIntentResolver() {
        this.mSnapshot = makeCache();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.IntentResolver
    public com.android.server.pm.CrossProfileIntentFilter snapshot(com.android.server.pm.CrossProfileIntentFilter f) {
        if (f == null) {
            return null;
        }
        return f.snapshot();
    }

    private CrossProfileIntentResolver(com.android.server.pm.CrossProfileIntentResolver f) {
        copyFrom((com.android.server.pm.WatchedIntentResolver) f);
        this.mSnapshot = new com.android.server.utils.SnapshotCache.Sealed();
    }

    private com.android.server.utils.SnapshotCache makeCache() {
        return new com.android.server.utils.SnapshotCache<com.android.server.pm.CrossProfileIntentResolver>(this, this) { // from class: com.android.server.pm.CrossProfileIntentResolver.1
            /* JADX WARN: Can't rename method to resolve collision */
            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.android.server.utils.SnapshotCache
            public com.android.server.pm.CrossProfileIntentResolver createSnapshot() {
                return new com.android.server.pm.CrossProfileIntentResolver();
            }
        };
    }

    @Override // com.android.server.utils.Snappable
    public com.android.server.pm.CrossProfileIntentResolver snapshot() {
        return this.mSnapshot.snapshot();
    }
}
