package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public class PreferredIntentResolver extends com.android.server.pm.WatchedIntentResolver<com.android.server.pm.PreferredActivity, com.android.server.pm.PreferredActivity> implements com.android.server.utils.Snappable {
    final com.android.server.utils.SnapshotCache<com.android.server.pm.PreferredIntentResolver> mSnapshot;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.IntentResolver
    public com.android.server.pm.PreferredActivity[] newArray(int size) {
        return new com.android.server.pm.PreferredActivity[size];
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.IntentResolver
    public boolean isPackageForFilter(java.lang.String packageName, com.android.server.pm.PreferredActivity filter) {
        return packageName.equals(filter.mPref.mComponent.getPackageName());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.IntentResolver
    public void dumpFilter(java.io.PrintWriter out, java.lang.String prefix, com.android.server.pm.PreferredActivity filter) {
        filter.mPref.dump(out, prefix, filter);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.IntentResolver
    public android.content.IntentFilter getIntentFilter(com.android.server.pm.PreferredActivity input) {
        return input.getIntentFilter();
    }

    public boolean shouldAddPreferredActivity(com.android.server.pm.PreferredActivity pa) {
        java.util.ArrayList<com.android.server.pm.PreferredActivity> pal = findFilters(pa);
        if (pal == null || pal.isEmpty()) {
            return true;
        }
        if (!pa.mPref.mAlways) {
            return false;
        }
        int activityCount = pal.size();
        for (int i = 0; i < activityCount; i++) {
            com.android.server.pm.PreferredActivity cur = pal.get(i);
            if (cur.mPref.mAlways && cur.mPref.mMatch == (pa.mPref.mMatch & 268369920) && cur.mPref.sameSet(pa.mPref)) {
                return false;
            }
        }
        return true;
    }

    public PreferredIntentResolver() {
        this.mSnapshot = makeCache();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.IntentResolver
    public com.android.server.pm.PreferredActivity snapshot(com.android.server.pm.PreferredActivity f) {
        if (f == null) {
            return null;
        }
        return f.snapshot();
    }

    private PreferredIntentResolver(com.android.server.pm.PreferredIntentResolver f) {
        copyFrom((com.android.server.pm.WatchedIntentResolver) f);
        this.mSnapshot = new com.android.server.utils.SnapshotCache.Sealed();
    }

    private com.android.server.utils.SnapshotCache makeCache() {
        return new com.android.server.utils.SnapshotCache<com.android.server.pm.PreferredIntentResolver>(this, this) { // from class: com.android.server.pm.PreferredIntentResolver.1
            /* JADX WARN: Can't rename method to resolve collision */
            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.android.server.utils.SnapshotCache
            public com.android.server.pm.PreferredIntentResolver createSnapshot() {
                return new com.android.server.pm.PreferredIntentResolver();
            }
        };
    }

    @Override // com.android.server.utils.Snappable
    public com.android.server.pm.PreferredIntentResolver snapshot() {
        return this.mSnapshot.snapshot();
    }
}
