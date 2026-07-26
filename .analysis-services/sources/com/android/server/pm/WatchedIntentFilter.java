package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public class WatchedIntentFilter extends com.android.server.utils.WatchableImpl implements com.android.server.utils.Snappable<com.android.server.pm.WatchedIntentFilter> {
    protected android.content.IntentFilter mFilter;

    private class WatchedIterator<E> implements java.util.Iterator<E> {
        private final java.util.Iterator<E> mIterator;

        WatchedIterator(java.util.Iterator<E> i) {
            this.mIterator = i;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.mIterator.hasNext();
        }

        @Override // java.util.Iterator
        public E next() {
            return this.mIterator.next();
        }

        @Override // java.util.Iterator
        public void remove() {
            this.mIterator.remove();
            com.android.server.pm.WatchedIntentFilter.this.onChanged();
        }

        @Override // java.util.Iterator
        public void forEachRemaining(java.util.function.Consumer<? super E> action) {
            this.mIterator.forEachRemaining(action);
            com.android.server.pm.WatchedIntentFilter.this.onChanged();
        }
    }

    private <E> java.util.Iterator<E> maybeWatch(java.util.Iterator<E> i) {
        return i == null ? i : new com.android.server.pm.WatchedIntentFilter.WatchedIterator(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onChanged() {
        dispatchChange(this);
    }

    protected WatchedIntentFilter() {
        this.mFilter = new android.content.IntentFilter();
    }

    public WatchedIntentFilter(android.content.IntentFilter f) {
        this.mFilter = new android.content.IntentFilter(f);
    }

    protected WatchedIntentFilter(com.android.server.pm.WatchedIntentFilter f) {
        this(f.getIntentFilter());
    }

    public WatchedIntentFilter(java.lang.String action) {
        this.mFilter = new android.content.IntentFilter(action);
    }

    public WatchedIntentFilter(java.lang.String action, java.lang.String dataType) throws android.content.IntentFilter.MalformedMimeTypeException {
        this.mFilter = new android.content.IntentFilter(action, dataType);
    }

    public com.android.server.pm.WatchedIntentFilter cloneFilter() {
        return new com.android.server.pm.WatchedIntentFilter(this.mFilter);
    }

    public android.content.IntentFilter getIntentFilter() {
        return this.mFilter;
    }

    public final void setPriority(int priority) {
        this.mFilter.setPriority(priority);
        onChanged();
    }

    public final int getPriority() {
        return this.mFilter.getPriority();
    }

    public final void setOrder(int order) {
        this.mFilter.setOrder(order);
        onChanged();
    }

    public final int getOrder() {
        return this.mFilter.getOrder();
    }

    public final boolean getAutoVerify() {
        return this.mFilter.getAutoVerify();
    }

    public final boolean handleAllWebDataURI() {
        return this.mFilter.handleAllWebDataURI();
    }

    public final boolean handlesWebUris(boolean onlyWebSchemes) {
        return this.mFilter.handlesWebUris(onlyWebSchemes);
    }

    public final boolean needsVerification() {
        return this.mFilter.needsVerification();
    }

    public void setVerified(boolean verified) {
        this.mFilter.setVerified(verified);
        onChanged();
    }

    public void setVisibilityToInstantApp(int visibility) {
        this.mFilter.setVisibilityToInstantApp(visibility);
        onChanged();
    }

    public int getVisibilityToInstantApp() {
        return this.mFilter.getVisibilityToInstantApp();
    }

    public boolean isVisibleToInstantApp() {
        return this.mFilter.isVisibleToInstantApp();
    }

    public boolean isExplicitlyVisibleToInstantApp() {
        return this.mFilter.isExplicitlyVisibleToInstantApp();
    }

    public boolean isImplicitlyVisibleToInstantApp() {
        return this.mFilter.isImplicitlyVisibleToInstantApp();
    }

    public final void addAction(java.lang.String action) {
        this.mFilter.addAction(action);
        onChanged();
    }

    public final int countActions() {
        return this.mFilter.countActions();
    }

    public final java.lang.String getAction(int index) {
        return this.mFilter.getAction(index);
    }

    public final boolean hasAction(java.lang.String action) {
        return this.mFilter.hasAction(action);
    }

    public final boolean matchAction(java.lang.String action) {
        return this.mFilter.matchAction(action);
    }

    public final java.util.Iterator<java.lang.String> actionsIterator() {
        return maybeWatch(this.mFilter.actionsIterator());
    }

    public final void addDataType(java.lang.String type) throws android.content.IntentFilter.MalformedMimeTypeException {
        this.mFilter.addDataType(type);
        onChanged();
    }

    public final void addDynamicDataType(java.lang.String type) throws android.content.IntentFilter.MalformedMimeTypeException {
        this.mFilter.addDynamicDataType(type);
        onChanged();
    }

    public final void clearDynamicDataTypes() {
        this.mFilter.clearDynamicDataTypes();
        onChanged();
    }

    public int countStaticDataTypes() {
        return this.mFilter.countStaticDataTypes();
    }

    public final boolean hasDataType(java.lang.String type) {
        return this.mFilter.hasDataType(type);
    }

    public final boolean hasExactDynamicDataType(java.lang.String type) {
        return this.mFilter.hasExactDynamicDataType(type);
    }

    public final boolean hasExactStaticDataType(java.lang.String type) {
        return this.mFilter.hasExactStaticDataType(type);
    }

    public final int countDataTypes() {
        return this.mFilter.countDataTypes();
    }

    public final java.lang.String getDataType(int index) {
        return this.mFilter.getDataType(index);
    }

    public final java.util.Iterator<java.lang.String> typesIterator() {
        return maybeWatch(this.mFilter.typesIterator());
    }

    public final java.util.List<java.lang.String> dataTypes() {
        return this.mFilter.dataTypes();
    }

    public final void addMimeGroup(java.lang.String name) {
        this.mFilter.addMimeGroup(name);
        onChanged();
    }

    public final boolean hasMimeGroup(java.lang.String name) {
        return this.mFilter.hasMimeGroup(name);
    }

    public final java.lang.String getMimeGroup(int index) {
        return this.mFilter.getMimeGroup(index);
    }

    public final int countMimeGroups() {
        return this.mFilter.countMimeGroups();
    }

    public final java.util.Iterator<java.lang.String> mimeGroupsIterator() {
        return maybeWatch(this.mFilter.mimeGroupsIterator());
    }

    public final void addDataScheme(java.lang.String scheme) {
        this.mFilter.addDataScheme(scheme);
        onChanged();
    }

    public final int countDataSchemes() {
        return this.mFilter.countDataSchemes();
    }

    public final java.lang.String getDataScheme(int index) {
        return this.mFilter.getDataScheme(index);
    }

    public final boolean hasDataScheme(java.lang.String scheme) {
        return this.mFilter.hasDataScheme(scheme);
    }

    public final java.util.Iterator<java.lang.String> schemesIterator() {
        return maybeWatch(this.mFilter.schemesIterator());
    }

    public final void addDataSchemeSpecificPart(java.lang.String ssp, int type) {
        this.mFilter.addDataSchemeSpecificPart(ssp, type);
        onChanged();
    }

    public final void addDataSchemeSpecificPart(android.os.PatternMatcher ssp) {
        this.mFilter.addDataSchemeSpecificPart(ssp);
        onChanged();
    }

    public final int countDataSchemeSpecificParts() {
        return this.mFilter.countDataSchemeSpecificParts();
    }

    public final android.os.PatternMatcher getDataSchemeSpecificPart(int index) {
        return this.mFilter.getDataSchemeSpecificPart(index);
    }

    public final boolean hasDataSchemeSpecificPart(java.lang.String data) {
        return this.mFilter.hasDataSchemeSpecificPart(data);
    }

    public final java.util.Iterator<android.os.PatternMatcher> schemeSpecificPartsIterator() {
        return maybeWatch(this.mFilter.schemeSpecificPartsIterator());
    }

    public final void addDataAuthority(java.lang.String host, java.lang.String port) {
        this.mFilter.addDataAuthority(host, port);
        onChanged();
    }

    public final void addDataAuthority(android.content.IntentFilter.AuthorityEntry ent) {
        this.mFilter.addDataAuthority(ent);
        onChanged();
    }

    public final int countDataAuthorities() {
        return this.mFilter.countDataAuthorities();
    }

    public final android.content.IntentFilter.AuthorityEntry getDataAuthority(int index) {
        return this.mFilter.getDataAuthority(index);
    }

    public final boolean hasDataAuthority(android.net.Uri data) {
        return this.mFilter.hasDataAuthority(data);
    }

    public final java.util.Iterator<android.content.IntentFilter.AuthorityEntry> authoritiesIterator() {
        return maybeWatch(this.mFilter.authoritiesIterator());
    }

    public final void addDataPath(java.lang.String path, int type) {
        this.mFilter.addDataPath(path, type);
        onChanged();
    }

    public final void addDataPath(android.os.PatternMatcher path) {
        this.mFilter.addDataPath(path);
        onChanged();
    }

    public final int countDataPaths() {
        return this.mFilter.countDataPaths();
    }

    public final android.os.PatternMatcher getDataPath(int index) {
        return this.mFilter.getDataPath(index);
    }

    public final boolean hasDataPath(java.lang.String data) {
        return this.mFilter.hasDataPath(data);
    }

    public final java.util.Iterator<android.os.PatternMatcher> pathsIterator() {
        return maybeWatch(this.mFilter.pathsIterator());
    }

    public final int matchDataAuthority(android.net.Uri data) {
        return this.mFilter.matchDataAuthority(data);
    }

    public final int matchDataAuthority(android.net.Uri data, boolean wildcardSupported) {
        return this.mFilter.matchDataAuthority(data, wildcardSupported);
    }

    public final int matchData(java.lang.String type, java.lang.String scheme, android.net.Uri data) {
        return this.mFilter.matchData(type, scheme, data);
    }

    public final void addCategory(java.lang.String category) {
        this.mFilter.addCategory(category);
    }

    public final int countCategories() {
        return this.mFilter.countCategories();
    }

    public final java.lang.String getCategory(int index) {
        return this.mFilter.getCategory(index);
    }

    public final boolean hasCategory(java.lang.String category) {
        return this.mFilter.hasCategory(category);
    }

    public final java.util.Iterator<java.lang.String> categoriesIterator() {
        return maybeWatch(this.mFilter.categoriesIterator());
    }

    public final java.lang.String matchCategories(java.util.Set<java.lang.String> categories) {
        return this.mFilter.matchCategories(categories);
    }

    public final int match(android.content.ContentResolver resolver, android.content.Intent intent, boolean resolve, java.lang.String logTag) {
        return this.mFilter.match(resolver, intent, resolve, logTag);
    }

    public final int match(java.lang.String action, java.lang.String type, java.lang.String scheme, android.net.Uri data, java.util.Set<java.lang.String> categories, java.lang.String logTag) {
        return this.mFilter.match(action, type, scheme, data, categories, logTag);
    }

    public final int match(java.lang.String action, java.lang.String type, java.lang.String scheme, android.net.Uri data, java.util.Set<java.lang.String> categories, java.lang.String logTag, boolean supportWildcards, java.util.Collection<java.lang.String> ignoreActions) {
        return this.mFilter.match(action, type, scheme, data, categories, logTag, supportWildcards, ignoreActions);
    }

    public void dump(android.util.Printer du, java.lang.String prefix) {
        this.mFilter.dump(du, prefix);
    }

    public final int describeContents() {
        return this.mFilter.describeContents();
    }

    public boolean debugCheck() {
        return this.mFilter.debugCheck();
    }

    public boolean checkDataPathAndSchemeSpecificParts() {
        return this.mFilter.checkDataPathAndSchemeSpecificParts();
    }

    public java.util.ArrayList<java.lang.String> getHostsList() {
        return this.mFilter.getHostsList();
    }

    public java.lang.String[] getHosts() {
        return this.mFilter.getHosts();
    }

    public static java.util.List<com.android.server.pm.WatchedIntentFilter> toWatchedIntentFilterList(java.util.List<android.content.IntentFilter> inList) {
        java.util.ArrayList<com.android.server.pm.WatchedIntentFilter> outList = new java.util.ArrayList<>();
        for (int i = 0; i < inList.size(); i++) {
            outList.add(new com.android.server.pm.WatchedIntentFilter(inList.get(i)));
        }
        return outList;
    }

    public static java.util.List<android.content.IntentFilter> toIntentFilterList(java.util.List<com.android.server.pm.WatchedIntentFilter> inList) {
        java.util.ArrayList<android.content.IntentFilter> outList = new java.util.ArrayList<>();
        for (int i = 0; i < inList.size(); i++) {
            outList.add(inList.get(i).getIntentFilter());
        }
        return outList;
    }

    @Override // com.android.server.utils.Snappable
    public com.android.server.pm.WatchedIntentFilter snapshot() {
        return new com.android.server.pm.WatchedIntentFilter(this);
    }
}
