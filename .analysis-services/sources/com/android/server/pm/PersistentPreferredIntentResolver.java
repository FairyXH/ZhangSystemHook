package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public class PersistentPreferredIntentResolver extends com.android.server.pm.WatchedIntentResolver<com.android.server.pm.PersistentPreferredActivity, com.android.server.pm.PersistentPreferredActivity> implements com.android.server.utils.Snappable {
    final com.android.server.utils.SnapshotCache<com.android.server.pm.PersistentPreferredIntentResolver> mSnapshot;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.IntentResolver
    public com.android.server.pm.PersistentPreferredActivity[] newArray(int size) {
        return new com.android.server.pm.PersistentPreferredActivity[size];
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.IntentResolver
    public android.content.IntentFilter getIntentFilter(com.android.server.pm.PersistentPreferredActivity input) {
        return input.getIntentFilter();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.IntentResolver
    public boolean isPackageForFilter(java.lang.String packageName, com.android.server.pm.PersistentPreferredActivity filter) {
        return packageName.equals(filter.mComponent.getPackageName());
    }

    public PersistentPreferredIntentResolver() {
        this.mSnapshot = makeCache();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.IntentResolver
    public com.android.server.pm.PersistentPreferredActivity snapshot(com.android.server.pm.PersistentPreferredActivity f) {
        if (f == null) {
            return null;
        }
        return f.snapshot();
    }

    private PersistentPreferredIntentResolver(com.android.server.pm.PersistentPreferredIntentResolver f) {
        copyFrom((com.android.server.pm.WatchedIntentResolver) f);
        this.mSnapshot = new com.android.server.utils.SnapshotCache.Sealed();
    }

    private com.android.server.utils.SnapshotCache makeCache() {
        return new com.android.server.utils.SnapshotCache<com.android.server.pm.PersistentPreferredIntentResolver>(this, this) { // from class: com.android.server.pm.PersistentPreferredIntentResolver.1
            /* JADX WARN: Can't rename method to resolve collision */
            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.android.server.utils.SnapshotCache
            public com.android.server.pm.PersistentPreferredIntentResolver createSnapshot() {
                return new com.android.server.pm.PersistentPreferredIntentResolver();
            }
        };
    }

    @Override // com.android.server.utils.Snappable
    public com.android.server.pm.PersistentPreferredIntentResolver snapshot() {
        return this.mSnapshot.snapshot();
    }
}
