package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
class PreferredActivity extends com.android.server.pm.WatchedIntentFilter implements com.android.server.pm.PreferredComponent.Callbacks {
    private static final boolean DEBUG_FILTERS = false;
    private static final java.lang.String TAG = "PreferredActivity";
    final com.android.server.pm.PreferredComponent mPref;
    final com.android.server.utils.SnapshotCache<com.android.server.pm.PreferredActivity> mSnapshot;

    private com.android.server.utils.SnapshotCache makeCache() {
        return new com.android.server.utils.SnapshotCache<com.android.server.pm.PreferredActivity>(this, this) { // from class: com.android.server.pm.PreferredActivity.1
            /* JADX WARN: Can't rename method to resolve collision */
            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.android.server.utils.SnapshotCache
            public com.android.server.pm.PreferredActivity createSnapshot() {
                com.android.server.pm.PreferredActivity s = new com.android.server.pm.PreferredActivity();
                s.seal();
                return s;
            }
        };
    }

    PreferredActivity(android.content.IntentFilter filter, int match, android.content.ComponentName[] set, android.content.ComponentName activity, boolean always) {
        super(filter);
        this.mPref = new com.android.server.pm.PreferredComponent(this, match, set, activity, always);
        this.mSnapshot = makeCache();
    }

    PreferredActivity(com.android.server.pm.WatchedIntentFilter filter, int match, android.content.ComponentName[] set, android.content.ComponentName activity, boolean always) {
        this(filter.mFilter, match, set, activity, always);
    }

    private PreferredActivity(com.android.server.pm.PreferredActivity f) {
        super(f);
        this.mPref = f.mPref;
        this.mSnapshot = new com.android.server.utils.SnapshotCache.Sealed();
    }

    PreferredActivity(com.android.modules.utils.TypedXmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        this.mPref = new com.android.server.pm.PreferredComponent(this, parser);
        this.mSnapshot = makeCache();
    }

    public void writeToXml(com.android.modules.utils.TypedXmlSerializer serializer, boolean full) throws java.io.IOException {
        this.mPref.writeToXml(serializer, full);
        serializer.startTag((java.lang.String) null, com.android.server.pm.verify.domain.DomainVerificationPersistence.ATTR_FILTER);
        this.mFilter.writeToXml(serializer);
        serializer.endTag((java.lang.String) null, com.android.server.pm.verify.domain.DomainVerificationPersistence.ATTR_FILTER);
    }

    @Override // com.android.server.pm.PreferredComponent.Callbacks
    public boolean onReadTag(java.lang.String tagName, com.android.modules.utils.TypedXmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        if (tagName.equals(com.android.server.pm.verify.domain.DomainVerificationPersistence.ATTR_FILTER)) {
            this.mFilter.readFromXml(parser);
            return true;
        }
        com.android.server.pm.PackageManagerService.reportSettingsProblem(5, "Unknown element under <preferred-activities>: " + parser.getName());
        com.android.internal.util.XmlUtils.skipCurrentTag(parser);
        return true;
    }

    public void dumpPref(java.io.PrintWriter out, java.lang.String prefix, com.android.server.pm.PreferredActivity filter) {
        this.mPref.dump(out, prefix, filter);
    }

    public java.lang.String toString() {
        return "PreferredActivity{0x" + java.lang.Integer.toHexString(java.lang.System.identityHashCode(this)) + " " + this.mPref.mComponent.flattenToShortString() + "}";
    }

    @Override // com.android.server.pm.WatchedIntentFilter, com.android.server.utils.Snappable
    public com.android.server.pm.PreferredActivity snapshot() {
        return this.mSnapshot.snapshot();
    }
}
