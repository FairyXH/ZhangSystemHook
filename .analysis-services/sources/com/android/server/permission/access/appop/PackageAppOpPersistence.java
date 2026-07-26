package com.android.server.permission.access.appop;

/* JADX INFO: compiled from: PackageAppOpPersistence.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000T\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u0000 \u001b2\u00020\u0001:\u0001\u001bB\u0005¢\u0006\u0002\u0010\u0002JB\u0010\u0003\u001a\u00020\u0004*\u00020\u000524\u0010\u0006\u001a0\u0012\u0004\u0012\u00020\b\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\n0\t\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\n0\u000b0\u0007j\u0002`\fH\u0002J\u001c\u0010\r\u001a\u00020\u0004*\u00020\u00052\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\nH\u0002J\u001c\u0010\u0011\u001a\u00020\u0004*\u00020\u00052\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\nH\u0016J(\u0010\u0012\u001a\u00020\u0004*\u00020\u00132\u0006\u0010\u0014\u001a\u00020\b2\u0012\u0010\u0015\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\n0\tH\u0002JB\u0010\u0016\u001a\u00020\u0004*\u00020\u001324\u0010\u0006\u001a0\u0012\u0004\u0012\u00020\b\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\n0\t\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\n0\u000b0\u0017j\u0002`\u0018H\u0002J\u001c\u0010\u0019\u001a\u00020\u0004*\u00020\u00132\u0006\u0010\u000e\u001a\u00020\u001a2\u0006\u0010\u0010\u001a\u00020\nH\u0016¨\u0006\u001c"}, d2 = {"Lcom/android/server/permission/access/appop/PackageAppOpPersistence;", "Lcom/android/server/permission/access/appop/BaseAppOpPersistence;", "()V", "parsePackage", "", "Lcom/android/modules/utils/BinaryXmlPullParser;", "packageAppOpModes", "Lcom/android/server/permission/access/immutable/MutableIndexedReferenceMap;", "", "Lcom/android/server/permission/access/immutable/IndexedMap;", "", "Lcom/android/server/permission/access/immutable/MutableIndexedMap;", "Lcom/android/server/permission/access/MutablePackageAppOpModes;", "parsePackageAppOps", "state", "Lcom/android/server/permission/access/MutableAccessState;", "userId", "parseUserState", "serializePackage", "Lcom/android/modules/utils/BinaryXmlSerializer;", com.android.server.pm.verify.domain.DomainVerificationLegacySettings.ATTR_PACKAGE_NAME, "appOpModes", "serializePackageAppOps", "Lcom/android/server/permission/access/immutable/IndexedReferenceMap;", "Lcom/android/server/permission/access/PackageAppOpModes;", "serializeUserState", "Lcom/android/server/permission/access/AccessState;", "Companion", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class PackageAppOpPersistence extends com.android.server.permission.access.appop.BaseAppOpPersistence {
    private static final java.lang.String ATTR_NAME = "name";
    public static final com.android.server.permission.access.appop.PackageAppOpPersistence.Companion Companion = new com.android.server.permission.access.appop.PackageAppOpPersistence.Companion(null);
    private static final java.lang.String LOG_TAG = com.android.server.permission.access.appop.PackageAppOpPersistence.class.getSimpleName();
    private static final java.lang.String TAG_PACKAGE = "package";
    private static final java.lang.String TAG_PACKAGE_APP_OPS = "package-app-ops";

    @Override // com.android.server.permission.access.appop.BaseAppOpPersistence
    public void parseUserState(com.android.modules.utils.BinaryXmlPullParser $this$parseUserState, com.android.server.permission.access.MutableAccessState state, int userId) throws org.xmlpull.v1.XmlPullParserException {
        if (com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.areEqual($this$parseUserState.getName(), TAG_PACKAGE_APP_OPS)) {
            parsePackageAppOps($this$parseUserState, state, userId);
        }
    }

    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Failed to find switch 'out' block (already processed)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.calcSwitchOut(SwitchRegionMaker.java:217)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.process(SwitchRegionMaker.java:68)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:112)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.makeEndlessLoop(LoopRegionMaker.java:282)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:65)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.addCases(SwitchRegionMaker.java:123)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.process(SwitchRegionMaker.java:71)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:112)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.makeEndlessLoop(LoopRegionMaker.java:282)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:65)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.addCases(SwitchRegionMaker.java:123)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.process(SwitchRegionMaker.java:71)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:112)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeMthRegion(RegionMaker.java:48)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:25)
        */
    private final void parsePackageAppOps(com.android.modules.utils.BinaryXmlPullParser r17, com.android.server.permission.access.MutableAccessState r18, int r19) throws org.xmlpull.v1.XmlPullParserException {
        /*
            Method dump skipped, instruction units count: 442
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.permission.access.appop.PackageAppOpPersistence.parsePackageAppOps(com.android.modules.utils.BinaryXmlPullParser, com.android.server.permission.access.MutableAccessState, int):void");
    }

    private final void parsePackage(com.android.modules.utils.BinaryXmlPullParser $this$parsePackage, com.android.server.permission.access.immutable.MutableIndexedReferenceMap<java.lang.String, com.android.server.permission.access.immutable.IndexedMap<java.lang.String, java.lang.Integer>, com.android.server.permission.access.immutable.MutableIndexedMap<java.lang.String, java.lang.Integer>> mutableIndexedReferenceMap) {
        java.lang.String name$iv = $this$parsePackage.getAttributeValue($this$parsePackage.getAttributeIndexOrThrow((java.lang.String) null, "name"));
        java.lang.String packageName = name$iv.intern();
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(packageName, "intern(...)");
        com.android.server.permission.access.immutable.MutableIndexedMap<java.lang.String, java.lang.Integer> mutableIndexedMap = new com.android.server.permission.access.immutable.MutableIndexedMap<>(null, 1, null);
        mutableIndexedReferenceMap.put(packageName, mutableIndexedMap);
        parseAppOps($this$parsePackage, mutableIndexedMap);
    }

    @Override // com.android.server.permission.access.appop.BaseAppOpPersistence
    public void serializeUserState(com.android.modules.utils.BinaryXmlSerializer $this$serializeUserState, com.android.server.permission.access.AccessState state, int userId) {
        com.android.server.permission.access.immutable.Immutable immutable = state.getUserStates().get(userId);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(immutable);
        serializePackageAppOps($this$serializeUserState, ((com.android.server.permission.access.UserState) immutable).getPackageAppOpModes());
    }

    private final void serializePackageAppOps(com.android.modules.utils.BinaryXmlSerializer $this$serializePackageAppOps, com.android.server.permission.access.immutable.IndexedReferenceMap<java.lang.String, com.android.server.permission.access.immutable.IndexedMap<java.lang.String, java.lang.Integer>, com.android.server.permission.access.immutable.MutableIndexedMap<java.lang.String, java.lang.Integer>> indexedReferenceMap) {
        $this$serializePackageAppOps.startTag((java.lang.String) null, TAG_PACKAGE_APP_OPS);
        int size = indexedReferenceMap.getSize();
        for (int index$iv = 0; index$iv < size; index$iv++) {
            java.lang.String packageName = indexedReferenceMap.keyAt(index$iv);
            serializePackage($this$serializePackageAppOps, packageName, (com.android.server.permission.access.immutable.IndexedMap) indexedReferenceMap.valueAt(index$iv));
        }
        $this$serializePackageAppOps.endTag((java.lang.String) null, TAG_PACKAGE_APP_OPS);
    }

    private final void serializePackage(com.android.modules.utils.BinaryXmlSerializer $this$serializePackage, java.lang.String packageName, com.android.server.permission.access.immutable.IndexedMap<java.lang.String, java.lang.Integer> indexedMap) {
        $this$serializePackage.startTag((java.lang.String) null, "package");
        $this$serializePackage.attributeInterned((java.lang.String) null, "name", packageName);
        serializeAppOps($this$serializePackage, indexedMap);
        $this$serializePackage.endTag((java.lang.String) null, "package");
    }

    /* JADX INFO: compiled from: PackageAppOpPersistence.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u0016\u0010\u0005\u001a\n \u0006*\u0004\u0018\u00010\u00040\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000¨\u0006\t"}, d2 = {"Lcom/android/server/permission/access/appop/PackageAppOpPersistence$Companion;", "", "()V", "ATTR_NAME", "", "LOG_TAG", "com.android.server.permission.jarjar.kotlin.jvm.PlatformType", "TAG_PACKAGE", "TAG_PACKAGE_APP_OPS", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(com.android.server.permission.jarjar.kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
