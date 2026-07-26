package com.android.server.permission.access.appop;

/* JADX INFO: compiled from: AppIdAppOpPersistence.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000T\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u0000 \u001b2\u00020\u0001:\u0001\u001bB\u0005¢\u0006\u0002\u0010\u0002J<\u0010\u0003\u001a\u00020\u0004*\u00020\u00052.\u0010\u0006\u001a*\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\n0\b\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\n0\u000b0\u0007j\u0002`\fH\u0002J\u001c\u0010\r\u001a\u00020\u0004*\u00020\u00052\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\nH\u0002J\u001c\u0010\u0011\u001a\u00020\u0004*\u00020\u00052\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\nH\u0016J(\u0010\u0012\u001a\u00020\u0004*\u00020\u00132\u0006\u0010\u0014\u001a\u00020\n2\u0012\u0010\u0015\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\n0\bH\u0002J<\u0010\u0016\u001a\u00020\u0004*\u00020\u00132.\u0010\u0006\u001a*\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\n0\b\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\n0\u000b0\u0017j\u0002`\u0018H\u0002J\u001c\u0010\u0019\u001a\u00020\u0004*\u00020\u00132\u0006\u0010\u000e\u001a\u00020\u001a2\u0006\u0010\u0010\u001a\u00020\nH\u0016¨\u0006\u001c"}, d2 = {"Lcom/android/server/permission/access/appop/AppIdAppOpPersistence;", "Lcom/android/server/permission/access/appop/BaseAppOpPersistence;", "()V", "parseAppId", "", "Lcom/android/modules/utils/BinaryXmlPullParser;", "appIdAppOpModes", "Lcom/android/server/permission/access/immutable/MutableIntReferenceMap;", "Lcom/android/server/permission/access/immutable/IndexedMap;", "", "", "Lcom/android/server/permission/access/immutable/MutableIndexedMap;", "Lcom/android/server/permission/access/MutableAppIdAppOpModes;", "parseAppIdAppOps", "state", "Lcom/android/server/permission/access/MutableAccessState;", "userId", "parseUserState", "serializeAppId", "Lcom/android/modules/utils/BinaryXmlSerializer;", "appId", "appOpModes", "serializeAppIdAppOps", "Lcom/android/server/permission/access/immutable/IntReferenceMap;", "Lcom/android/server/permission/access/AppIdAppOpModes;", "serializeUserState", "Lcom/android/server/permission/access/AccessState;", "Companion", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class AppIdAppOpPersistence extends com.android.server.permission.access.appop.BaseAppOpPersistence {
    private static final java.lang.String ATTR_ID = "id";
    public static final com.android.server.permission.access.appop.AppIdAppOpPersistence.Companion Companion = new com.android.server.permission.access.appop.AppIdAppOpPersistence.Companion(null);
    private static final java.lang.String LOG_TAG = com.android.server.permission.access.appop.AppIdAppOpPersistence.class.getSimpleName();
    private static final java.lang.String TAG_APP_ID = "app-id";
    private static final java.lang.String TAG_APP_ID_APP_OPS = "app-id-app-ops";

    @Override // com.android.server.permission.access.appop.BaseAppOpPersistence
    public void parseUserState(com.android.modules.utils.BinaryXmlPullParser $this$parseUserState, com.android.server.permission.access.MutableAccessState state, int userId) throws org.xmlpull.v1.XmlPullParserException {
        if (com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.areEqual($this$parseUserState.getName(), TAG_APP_ID_APP_OPS)) {
            parseAppIdAppOps($this$parseUserState, state, userId);
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
    private final void parseAppIdAppOps(com.android.modules.utils.BinaryXmlPullParser r17, com.android.server.permission.access.MutableAccessState r18, int r19) throws org.xmlpull.v1.XmlPullParserException {
        /*
            Method dump skipped, instruction units count: 446
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.permission.access.appop.AppIdAppOpPersistence.parseAppIdAppOps(com.android.modules.utils.BinaryXmlPullParser, com.android.server.permission.access.MutableAccessState, int):void");
    }

    private final void parseAppId(com.android.modules.utils.BinaryXmlPullParser $this$parseAppId, com.android.server.permission.access.immutable.MutableIntReferenceMap<com.android.server.permission.access.immutable.IndexedMap<java.lang.String, java.lang.Integer>, com.android.server.permission.access.immutable.MutableIndexedMap<java.lang.String, java.lang.Integer>> mutableIntReferenceMap) {
        int appId = $this$parseAppId.getAttributeInt((java.lang.String) null, ATTR_ID);
        com.android.server.permission.access.immutable.MutableIndexedMap<java.lang.String, java.lang.Integer> mutableIndexedMap = new com.android.server.permission.access.immutable.MutableIndexedMap<>(null, 1, null);
        com.android.server.permission.access.immutable.IntReferenceMapExtensionsKt.set(mutableIntReferenceMap, appId, mutableIndexedMap);
        parseAppOps($this$parseAppId, mutableIndexedMap);
    }

    @Override // com.android.server.permission.access.appop.BaseAppOpPersistence
    public void serializeUserState(com.android.modules.utils.BinaryXmlSerializer $this$serializeUserState, com.android.server.permission.access.AccessState state, int userId) {
        com.android.server.permission.access.immutable.Immutable immutable = state.getUserStates().get(userId);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(immutable);
        serializeAppIdAppOps($this$serializeUserState, ((com.android.server.permission.access.UserState) immutable).getAppIdAppOpModes());
    }

    private final void serializeAppIdAppOps(com.android.modules.utils.BinaryXmlSerializer $this$serializeAppIdAppOps, com.android.server.permission.access.immutable.IntReferenceMap<com.android.server.permission.access.immutable.IndexedMap<java.lang.String, java.lang.Integer>, com.android.server.permission.access.immutable.MutableIndexedMap<java.lang.String, java.lang.Integer>> intReferenceMap) {
        $this$serializeAppIdAppOps.startTag((java.lang.String) null, TAG_APP_ID_APP_OPS);
        int size = intReferenceMap.getSize();
        for (int index$iv = 0; index$iv < size; index$iv++) {
            int appId = intReferenceMap.keyAt(index$iv);
            serializeAppId($this$serializeAppIdAppOps, appId, (com.android.server.permission.access.immutable.IndexedMap) intReferenceMap.valueAt(index$iv));
        }
        $this$serializeAppIdAppOps.endTag((java.lang.String) null, TAG_APP_ID_APP_OPS);
    }

    private final void serializeAppId(com.android.modules.utils.BinaryXmlSerializer $this$serializeAppId, int appId, com.android.server.permission.access.immutable.IndexedMap<java.lang.String, java.lang.Integer> indexedMap) {
        $this$serializeAppId.startTag((java.lang.String) null, TAG_APP_ID);
        $this$serializeAppId.attributeInt((java.lang.String) null, ATTR_ID, appId);
        serializeAppOps($this$serializeAppId, indexedMap);
        $this$serializeAppId.endTag((java.lang.String) null, TAG_APP_ID);
    }

    /* JADX INFO: compiled from: AppIdAppOpPersistence.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u0016\u0010\u0005\u001a\n \u0006*\u0004\u0018\u00010\u00040\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000¨\u0006\t"}, d2 = {"Lcom/android/server/permission/access/appop/AppIdAppOpPersistence$Companion;", "", "()V", "ATTR_ID", "", "LOG_TAG", "com.android.server.permission.jarjar.kotlin.jvm.PlatformType", "TAG_APP_ID", "TAG_APP_ID_APP_OPS", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(com.android.server.permission.jarjar.kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
