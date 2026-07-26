package com.android.server.permission.access.appop;

/* JADX INFO: compiled from: BaseAppOpPersistence.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b&\u0018\u0000 \u00172\u00020\u0001:\u0001\u0017B\u0005¢\u0006\u0002\u0010\u0002J \u0010\u0003\u001a\u00020\u0004*\u00020\u00052\u0012\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\t0\u0007H\u0002J \u0010\n\u001a\u00020\u0004*\u00020\u00052\u0012\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\t0\u0007H\u0004J\u001c\u0010\u000b\u001a\u00020\u0004*\u00020\u00052\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\tH&J\u001c\u0010\u000f\u001a\u00020\u0004*\u00020\u00102\u0006\u0010\u0011\u001a\u00020\b2\u0006\u0010\u0012\u001a\u00020\tH\u0002J \u0010\u0013\u001a\u00020\u0004*\u00020\u00102\u0012\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\t0\u0014H\u0004J\u001c\u0010\u0015\u001a\u00020\u0004*\u00020\u00102\u0006\u0010\f\u001a\u00020\u00162\u0006\u0010\u000e\u001a\u00020\tH&¨\u0006\u0018"}, d2 = {"Lcom/android/server/permission/access/appop/BaseAppOpPersistence;", "", "()V", "parseAppOp", "", "Lcom/android/modules/utils/BinaryXmlPullParser;", "appOpModes", "Lcom/android/server/permission/access/immutable/MutableIndexedMap;", "", "", "parseAppOps", "parseUserState", "state", "Lcom/android/server/permission/access/MutableAccessState;", "userId", "serializeAppOp", "Lcom/android/modules/utils/BinaryXmlSerializer;", "name", "mode", "serializeAppOps", "Lcom/android/server/permission/access/immutable/IndexedMap;", "serializeUserState", "Lcom/android/server/permission/access/AccessState;", "Companion", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 1, mv = {1, 9, 0}, xi = 48)
public abstract class BaseAppOpPersistence {
    private static final java.lang.String ATTR_MODE = "mode";
    private static final java.lang.String ATTR_NAME = "name";
    public static final com.android.server.permission.access.appop.BaseAppOpPersistence.Companion Companion = new com.android.server.permission.access.appop.BaseAppOpPersistence.Companion(null);
    private static final java.lang.String LOG_TAG = com.android.server.permission.access.appop.BaseAppOpPersistence.class.getSimpleName();
    private static final java.lang.String TAG_APP_OP = "app-op";

    public abstract void parseUserState(com.android.modules.utils.BinaryXmlPullParser binaryXmlPullParser, com.android.server.permission.access.MutableAccessState mutableAccessState, int i);

    public abstract void serializeUserState(com.android.modules.utils.BinaryXmlSerializer binaryXmlSerializer, com.android.server.permission.access.AccessState accessState, int i);

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
    protected final void parseAppOps(com.android.modules.utils.BinaryXmlPullParser r12, com.android.server.permission.access.immutable.MutableIndexedMap<java.lang.String, java.lang.Integer> r13) {
        /*
            Method dump skipped, instruction units count: 326
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.permission.access.appop.BaseAppOpPersistence.parseAppOps(com.android.modules.utils.BinaryXmlPullParser, com.android.server.permission.access.immutable.MutableIndexedMap):void");
    }

    private final void parseAppOp(com.android.modules.utils.BinaryXmlPullParser $this$parseAppOp, com.android.server.permission.access.immutable.MutableIndexedMap<java.lang.String, java.lang.Integer> mutableIndexedMap) {
        java.lang.String name$iv = $this$parseAppOp.getAttributeValue($this$parseAppOp.getAttributeIndexOrThrow((java.lang.String) null, "name"));
        java.lang.String name = name$iv.intern();
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(name, "intern(...)");
        int mode = $this$parseAppOp.getAttributeInt((java.lang.String) null, "mode");
        mutableIndexedMap.put(name, java.lang.Integer.valueOf(mode));
    }

    protected final void serializeAppOps(com.android.modules.utils.BinaryXmlSerializer $this$serializeAppOps, com.android.server.permission.access.immutable.IndexedMap<java.lang.String, java.lang.Integer> indexedMap) {
        int size = indexedMap.getSize();
        for (int index$iv = 0; index$iv < size; index$iv++) {
            java.lang.String strKeyAt = indexedMap.keyAt(index$iv);
            int mode = indexedMap.valueAt(index$iv).intValue();
            java.lang.String name = strKeyAt;
            serializeAppOp($this$serializeAppOps, name, mode);
        }
    }

    private final void serializeAppOp(com.android.modules.utils.BinaryXmlSerializer $this$serializeAppOp, java.lang.String name, int mode) {
        $this$serializeAppOp.startTag((java.lang.String) null, "app-op");
        $this$serializeAppOp.attributeInterned((java.lang.String) null, "name", name);
        $this$serializeAppOp.attributeInt((java.lang.String) null, "mode", mode);
        $this$serializeAppOp.endTag((java.lang.String) null, "app-op");
    }

    /* JADX INFO: compiled from: BaseAppOpPersistence.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u0016\u0010\u0006\u001a\n \u0007*\u0004\u0018\u00010\u00040\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000¨\u0006\t"}, d2 = {"Lcom/android/server/permission/access/appop/BaseAppOpPersistence$Companion;", "", "()V", "ATTR_MODE", "", "ATTR_NAME", "LOG_TAG", "com.android.server.permission.jarjar.kotlin.jvm.PlatformType", "TAG_APP_OP", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(com.android.server.permission.jarjar.kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
