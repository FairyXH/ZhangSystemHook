package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
class CrossProfileIntentFilter extends com.android.server.pm.WatchedIntentFilter {
    public static final int ACCESS_LEVEL_ALL = 0;
    public static final int ACCESS_LEVEL_SYSTEM = 10;
    public static final int ACCESS_LEVEL_SYSTEM_ADD_ONLY = 20;
    private static final java.lang.String ATTR_ACCESS_CONTROL = "accessControl";
    private static final java.lang.String ATTR_FILTER = "filter";
    private static final java.lang.String ATTR_FLAGS = "flags";
    private static final java.lang.String ATTR_OWNER_PACKAGE = "ownerPackage";
    private static final java.lang.String ATTR_TARGET_USER_ID = "targetUserId";
    public static final int FLAG_ALLOW_CHAINED_RESOLUTION = 16;
    public static final int FLAG_IS_PACKAGE_FOR_FILTER = 8;
    private static final java.lang.String TAG = "CrossProfileIntentFilter";
    final int mAccessControlLevel;
    final int mFlags;
    final java.lang.String mOwnerPackage;
    final com.android.server.utils.SnapshotCache<com.android.server.pm.CrossProfileIntentFilter> mSnapshot;
    final int mTargetUserId;

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface AccessControlLevel {
    }

    private com.android.server.utils.SnapshotCache makeCache() {
        return new com.android.server.utils.SnapshotCache<com.android.server.pm.CrossProfileIntentFilter>(this, this) { // from class: com.android.server.pm.CrossProfileIntentFilter.1
            /* JADX WARN: Can't rename method to resolve collision */
            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.android.server.utils.SnapshotCache
            public com.android.server.pm.CrossProfileIntentFilter createSnapshot() {
                com.android.server.pm.CrossProfileIntentFilter s = new com.android.server.pm.CrossProfileIntentFilter();
                s.seal();
                return s;
            }
        };
    }

    CrossProfileIntentFilter(android.content.IntentFilter filter, java.lang.String ownerPackage, int targetUserId, int flags) {
        this(filter, ownerPackage, targetUserId, flags, 0);
    }

    CrossProfileIntentFilter(android.content.IntentFilter filter, java.lang.String ownerPackage, int targetUserId, int flags, int accessControlLevel) {
        super(filter);
        this.mTargetUserId = targetUserId;
        this.mOwnerPackage = ownerPackage;
        this.mFlags = flags;
        this.mAccessControlLevel = accessControlLevel;
        this.mSnapshot = makeCache();
    }

    CrossProfileIntentFilter(com.android.server.pm.WatchedIntentFilter filter, java.lang.String ownerPackage, int targetUserId, int flags) {
        this(filter.mFilter, ownerPackage, targetUserId, flags);
    }

    CrossProfileIntentFilter(com.android.server.pm.WatchedIntentFilter filter, java.lang.String ownerPackage, int targetUserId, int flags, int accessControlLevel) {
        this(filter.mFilter, ownerPackage, targetUserId, flags, accessControlLevel);
    }

    private CrossProfileIntentFilter(com.android.server.pm.CrossProfileIntentFilter f) {
        super(f);
        this.mTargetUserId = f.mTargetUserId;
        this.mOwnerPackage = f.mOwnerPackage;
        this.mFlags = f.mFlags;
        this.mAccessControlLevel = f.mAccessControlLevel;
        this.mSnapshot = new com.android.server.utils.SnapshotCache.Sealed();
    }

    public int getTargetUserId() {
        return this.mTargetUserId;
    }

    public int getFlags() {
        return this.mFlags;
    }

    public java.lang.String getOwnerPackage() {
        return this.mOwnerPackage;
    }

    public int getAccessControlLevel() {
        return this.mAccessControlLevel;
    }

    CrossProfileIntentFilter(com.android.modules.utils.TypedXmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        this.mTargetUserId = parser.getAttributeInt((java.lang.String) null, ATTR_TARGET_USER_ID, -10000);
        this.mOwnerPackage = getStringFromXml(parser, ATTR_OWNER_PACKAGE, "");
        this.mAccessControlLevel = parser.getAttributeInt((java.lang.String) null, ATTR_ACCESS_CONTROL, 0);
        this.mFlags = parser.getAttributeInt((java.lang.String) null, ATTR_FLAGS, 0);
        this.mSnapshot = makeCache();
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
                java.lang.String msg = "Unknown element under crossProfile-intent-filters: " + tagName + " at " + parser.getPositionDescription();
                com.android.server.pm.PackageManagerService.reportSettingsProblem(5, msg);
                com.android.internal.util.XmlUtils.skipCurrentTag(parser);
            }
        }
        if (tagName.equals("filter")) {
            this.mFilter.readFromXml(parser);
            return;
        }
        java.lang.String msg2 = "Missing element under CrossProfileIntentFilter: filter at " + parser.getPositionDescription();
        com.android.server.pm.PackageManagerService.reportSettingsProblem(5, msg2);
        com.android.internal.util.XmlUtils.skipCurrentTag(parser);
    }

    private java.lang.String getStringFromXml(com.android.modules.utils.TypedXmlPullParser parser, java.lang.String attribute, java.lang.String defaultValue) {
        java.lang.String value = parser.getAttributeValue((java.lang.String) null, attribute);
        if (value == null) {
            java.lang.String msg = "Missing element under CrossProfileIntentFilter: " + attribute + " at " + parser.getPositionDescription();
            com.android.server.pm.PackageManagerService.reportSettingsProblem(5, msg);
            return defaultValue;
        }
        return value;
    }

    public void writeToXml(com.android.modules.utils.TypedXmlSerializer serializer) throws java.io.IOException {
        serializer.attributeInt((java.lang.String) null, ATTR_TARGET_USER_ID, this.mTargetUserId);
        serializer.attributeInt((java.lang.String) null, ATTR_FLAGS, this.mFlags);
        serializer.attribute((java.lang.String) null, ATTR_OWNER_PACKAGE, this.mOwnerPackage);
        serializer.attributeInt((java.lang.String) null, ATTR_ACCESS_CONTROL, this.mAccessControlLevel);
        serializer.startTag((java.lang.String) null, "filter");
        this.mFilter.writeToXml(serializer);
        serializer.endTag((java.lang.String) null, "filter");
    }

    public java.lang.String toString() {
        return "CrossProfileIntentFilter{0x" + java.lang.Integer.toHexString(java.lang.System.identityHashCode(this)) + " " + java.lang.Integer.toString(this.mTargetUserId) + "}";
    }

    boolean equalsIgnoreFilter(com.android.server.pm.CrossProfileIntentFilter other) {
        return this.mTargetUserId == other.mTargetUserId && this.mOwnerPackage.equals(other.mOwnerPackage) && this.mFlags == other.mFlags && this.mAccessControlLevel == other.mAccessControlLevel;
    }

    @Override // com.android.server.pm.WatchedIntentFilter, com.android.server.utils.Snappable
    public com.android.server.pm.CrossProfileIntentFilter snapshot() {
        return this.mSnapshot.snapshot();
    }
}
