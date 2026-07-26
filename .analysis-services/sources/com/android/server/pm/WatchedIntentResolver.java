package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public abstract class WatchedIntentResolver<F extends com.android.server.pm.WatchedIntentFilter, R extends com.android.server.pm.WatchedIntentFilter> extends com.android.server.IntentResolver<F, R> implements com.android.server.utils.Watchable, com.android.server.utils.Snappable {
    private static final java.util.Comparator<com.android.server.pm.WatchedIntentFilter> sResolvePrioritySorter = new java.util.Comparator<com.android.server.pm.WatchedIntentFilter>() { // from class: com.android.server.pm.WatchedIntentResolver.2
        @Override // java.util.Comparator
        public int compare(com.android.server.pm.WatchedIntentFilter o1, com.android.server.pm.WatchedIntentFilter o2) {
            int q1 = o1.getPriority();
            int q2 = o2.getPriority();
            if (q1 > q2) {
                return -1;
            }
            return q1 < q2 ? 1 : 0;
        }
    };
    private final com.android.server.utils.Watchable mWatchable = new com.android.server.utils.WatchableImpl();
    private final com.android.server.utils.Watcher mWatcher = new com.android.server.utils.Watcher() { // from class: com.android.server.pm.WatchedIntentResolver.1
        @Override // com.android.server.utils.Watcher
        public void onChange(com.android.server.utils.Watchable what) {
            com.android.server.pm.WatchedIntentResolver.this.dispatchChange(what);
        }
    };

    @Override // com.android.server.utils.Watchable
    public void registerObserver(com.android.server.utils.Watcher observer) {
        this.mWatchable.registerObserver(observer);
    }

    @Override // com.android.server.utils.Watchable
    public void unregisterObserver(com.android.server.utils.Watcher observer) {
        this.mWatchable.unregisterObserver(observer);
    }

    @Override // com.android.server.utils.Watchable
    public boolean isRegisteredObserver(com.android.server.utils.Watcher observer) {
        return this.mWatchable.isRegisteredObserver(observer);
    }

    @Override // com.android.server.utils.Watchable
    public void dispatchChange(com.android.server.utils.Watchable what) {
        this.mWatchable.dispatchChange(what);
    }

    protected void onChanged() {
        dispatchChange(this);
    }

    @Override // com.android.server.IntentResolver
    public void addFilter(com.android.server.pm.snapshot.PackageDataSnapshot snapshot, F f) {
        super.addFilter(snapshot, f);
        f.registerObserver(this.mWatcher);
        onChanged();
    }

    @Override // com.android.server.IntentResolver
    public void removeFilter(F f) {
        f.unregisterObserver(this.mWatcher);
        super.removeFilter(f);
        onChanged();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.IntentResolver
    public void removeFilterInternal(F f) {
        f.unregisterObserver(this.mWatcher);
        super.removeFilterInternal(f);
        onChanged();
    }

    @Override // com.android.server.IntentResolver
    protected void sortResults(java.util.List<R> results) {
        java.util.Collections.sort(results, sResolvePrioritySorter);
    }

    public java.util.ArrayList<F> findFilters(com.android.server.pm.WatchedIntentFilter matching) {
        return super.findFilters(matching.getIntentFilter());
    }

    protected void copyFrom(com.android.server.pm.WatchedIntentResolver orig) {
        super.copyFrom((com.android.server.IntentResolver) orig);
    }
}
