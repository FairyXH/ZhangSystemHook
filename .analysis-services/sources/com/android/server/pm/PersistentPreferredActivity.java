package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
class PersistentPreferredActivity extends com.android.server.pm.WatchedIntentFilter {
    private static final java.lang.String ATTR_FILTER = "filter";
    private static final java.lang.String ATTR_NAME = "name";
    private static final java.lang.String ATTR_SET_BY_DPM = "set-by-dpm";
    private static final boolean DEBUG_FILTERS = false;
    private static final java.lang.String TAG = "PersistentPreferredActivity";
    final android.content.ComponentName mComponent;
    final boolean mIsSetByDpm;
    final com.android.server.utils.SnapshotCache<com.android.server.pm.PersistentPreferredActivity> mSnapshot;

    private com.android.server.utils.SnapshotCache makeCache() {
        return new com.android.server.utils.SnapshotCache<com.android.server.pm.PersistentPreferredActivity>(this, this) { // from class: com.android.server.pm.PersistentPreferredActivity.1
            /* JADX WARN: Can't rename method to resolve collision */
            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.android.server.utils.SnapshotCache
            public com.android.server.pm.PersistentPreferredActivity createSnapshot() {
                com.android.server.pm.PersistentPreferredActivity s = new com.android.server.pm.PersistentPreferredActivity();
                s.seal();
                return s;
            }
        };
    }

    PersistentPreferredActivity(android.content.IntentFilter filter, android.content.ComponentName activity, boolean isSetByDpm) {
        super(filter);
        this.mComponent = activity;
        this.mIsSetByDpm = isSetByDpm;
        this.mSnapshot = makeCache();
    }

    PersistentPreferredActivity(com.android.server.pm.WatchedIntentFilter filter, android.content.ComponentName activity, boolean isSetByDpm) {
        this(filter.mFilter, activity, isSetByDpm);
    }

    private PersistentPreferredActivity(com.android.server.pm.PersistentPreferredActivity f) {
        super(f);
        this.mComponent = f.mComponent;
        this.mIsSetByDpm = f.mIsSetByDpm;
        this.mSnapshot = new com.android.server.utils.SnapshotCache.Sealed();
    }

    PersistentPreferredActivity(com.android.modules.utils.TypedXmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        java.lang.String shortComponent = parser.getAttributeValue((java.lang.String) null, "name");
        this.mComponent = android.content.ComponentName.unflattenFromString(shortComponent);
        if (this.mComponent == null) {
            com.android.server.pm.PackageManagerService.reportSettingsProblem(5, "Error in package manager settings: Bad activity name " + shortComponent + " at " + parser.getPositionDescription());
        }
        this.mIsSetByDpm = parser.getAttributeBoolean((java.lang.String) null, ATTR_SET_BY_DPM, false);
        int outerDepth = parser.getDepth();
        java.lang.String tagName = parser.getName();
        while (true) {
            int type = parser.next();
            if (type == 1 || (type == 3 && parser.getDepth() <= outerDepth)) {
                break;
            }
            tagName = parser.getName();
            if (type != 3 && type != 4 && type == 2) {
                if (tagName.equals("filter")) {
                    break;
                }
                com.android.server.pm.PackageManagerService.reportSettingsProblem(5, "Unknown element: " + tagName + " at " + parser.getPositionDescription());
                com.android.internal.util.XmlUtils.skipCurrentTag(parser);
            }
        }
        if (tagName.equals("filter")) {
            this.mFilter.readFromXml(parser);
        } else {
            com.android.server.pm.PackageManagerService.reportSettingsProblem(5, "Missing element filter at " + parser.getPositionDescription());
            com.android.internal.util.XmlUtils.skipCurrentTag(parser);
        }
        this.mSnapshot = makeCache();
    }

    public void writeToXml(com.android.modules.utils.TypedXmlSerializer serializer) throws java.io.IOException {
        serializer.attribute((java.lang.String) null, "name", this.mComponent.flattenToShortString());
        serializer.attributeBoolean((java.lang.String) null, ATTR_SET_BY_DPM, this.mIsSetByDpm);
        serializer.startTag((java.lang.String) null, "filter");
        this.mFilter.writeToXml(serializer);
        serializer.endTag((java.lang.String) null, "filter");
    }

    @Override // com.android.server.pm.WatchedIntentFilter
    public android.content.IntentFilter getIntentFilter() {
        return this.mFilter;
    }

    public java.lang.String toString() {
        return "PersistentPreferredActivity{0x" + java.lang.Integer.toHexString(java.lang.System.identityHashCode(this)) + " " + this.mComponent.flattenToShortString() + ", mIsSetByDpm=" + this.mIsSetByDpm + "}";
    }

    @Override // com.android.server.pm.WatchedIntentFilter, com.android.server.utils.Snappable
    public com.android.server.pm.PersistentPreferredActivity snapshot() {
        return this.mSnapshot.snapshot();
    }
}
