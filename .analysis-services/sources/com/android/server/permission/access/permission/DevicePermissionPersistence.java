package com.android.server.permission.access.permission;

/* JADX INFO: compiled from: DevicePermissionPersistence.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000Z\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u0000 %2\u00020\u0001:\u0001%B\u0005¢\u0006\u0002\u0010\u0002J\u0080\u0001\u0010\u0003\u001a\u00020\u0004*\u00020\u00052r\u0010\u0006\u001an\u00122\u00120\u0012\u0004\u0012\u00020\t\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u000b0\n\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u000b0\f0\bj\u0002`\r\u00122\u00120\u0012\u0004\u0012\u00020\t\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u000b0\n\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u000b0\f0\u000ej\u0002`\u000f0\u0007j\u0002`\u0010H\u0002J\u001c\u0010\u0011\u001a\u00020\u0004*\u00020\u00052\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u000bH\u0002JB\u0010\u0015\u001a\u00020\u0004*\u00020\u000524\u0010\u0016\u001a0\u0012\u0004\u0012\u00020\t\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u000b0\n\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u000b0\f0\u000ej\u0002`\u000fH\u0002J \u0010\u0017\u001a\u00020\u0004*\u00020\u00052\u0012\u0010\u0018\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u000b0\fH\u0002J\u001a\u0010\u0019\u001a\u00020\u0004*\u00020\u00052\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u000bJJ\u0010\u001a\u001a\u00020\u0004*\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u000b24\u0010\u001d\u001a0\u0012\u0004\u0012\u00020\t\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u000b0\n\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u000b0\f0\bj\u0002`\rH\u0002J(\u0010\u001e\u001a\u00020\u0004*\u00020\u001b2\u0006\u0010\u001f\u001a\u00020\t2\u0012\u0010\u0018\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u000b0\nH\u0002J\u001c\u0010 \u001a\u00020\u0004*\u00020\u001b2\u0006\u0010!\u001a\u00020\t2\u0006\u0010\"\u001a\u00020\u000bH\u0002J\u001a\u0010#\u001a\u00020\u0004*\u00020\u001b2\u0006\u0010\u0012\u001a\u00020$2\u0006\u0010\u0014\u001a\u00020\u000b¨\u0006&"}, d2 = {"Lcom/android/server/permission/access/permission/DevicePermissionPersistence;", "", "()V", "parseAppId", "", "Lcom/android/modules/utils/BinaryXmlPullParser;", "appIdPermissionFlags", "Lcom/android/server/permission/access/immutable/MutableIntReferenceMap;", "Lcom/android/server/permission/access/immutable/IndexedReferenceMap;", "", "Lcom/android/server/permission/access/immutable/IndexedMap;", "", "Lcom/android/server/permission/access/immutable/MutableIndexedMap;", "Lcom/android/server/permission/access/DevicePermissionFlags;", "Lcom/android/server/permission/access/immutable/MutableIndexedReferenceMap;", "Lcom/android/server/permission/access/MutableDevicePermissionFlags;", "Lcom/android/server/permission/access/MutableAppIdDevicePermissionFlags;", "parseAppIdDevicePermissions", "state", "Lcom/android/server/permission/access/MutableAccessState;", "userId", "parseDevice", "deviceIdPermissionFlags", "parsePermission", "permissionFlags", "parseUserState", "serializeAppId", "Lcom/android/modules/utils/BinaryXmlSerializer;", "appId", "devicePermissionFlags", "serializeDevice", "deviceId", "serializePermission", "name", com.android.server.permission.access.permission.DevicePermissionPersistence.ATTR_FLAGS, "serializeUserState", "Lcom/android/server/permission/access/AccessState;", "Companion", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class DevicePermissionPersistence {
    private static final java.lang.String ATTR_FLAGS = "flags";
    private static final java.lang.String ATTR_ID = "id";
    private static final java.lang.String ATTR_NAME = "name";
    public static final com.android.server.permission.access.permission.DevicePermissionPersistence.Companion Companion = new com.android.server.permission.access.permission.DevicePermissionPersistence.Companion(null);
    private static final java.lang.String LOG_TAG = com.android.server.permission.access.permission.DevicePermissionPersistence.class.getSimpleName();
    private static final java.lang.String TAG_APP_ID = "app-id";
    private static final java.lang.String TAG_APP_ID_DEVICE_PERMISSIONS = "app-id-device-permissions";
    private static final java.lang.String TAG_DEVICE = "device";
    private static final java.lang.String TAG_PERMISSION = "permission";

    public final void parseUserState(com.android.modules.utils.BinaryXmlPullParser $this$parseUserState, com.android.server.permission.access.MutableAccessState state, int userId) throws org.xmlpull.v1.XmlPullParserException {
        if (com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.areEqual($this$parseUserState.getName(), TAG_APP_ID_DEVICE_PERMISSIONS)) {
            parseAppIdDevicePermissions($this$parseUserState, state, userId);
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
    private final void parseAppIdDevicePermissions(com.android.modules.utils.BinaryXmlPullParser r17, com.android.server.permission.access.MutableAccessState r18, int r19) throws org.xmlpull.v1.XmlPullParserException {
        /*
            Method dump skipped, instruction units count: 440
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.permission.access.permission.DevicePermissionPersistence.parseAppIdDevicePermissions(com.android.modules.utils.BinaryXmlPullParser, com.android.server.permission.access.MutableAccessState, int):void");
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
    private final void parseAppId(com.android.modules.utils.BinaryXmlPullParser r14, com.android.server.permission.access.immutable.MutableIntReferenceMap<com.android.server.permission.access.immutable.IndexedReferenceMap<java.lang.String, com.android.server.permission.access.immutable.IndexedMap<java.lang.String, java.lang.Integer>, com.android.server.permission.access.immutable.MutableIndexedMap<java.lang.String, java.lang.Integer>>, com.android.server.permission.access.immutable.MutableIndexedReferenceMap<java.lang.String, com.android.server.permission.access.immutable.IndexedMap<java.lang.String, java.lang.Integer>, com.android.server.permission.access.immutable.MutableIndexedMap<java.lang.String, java.lang.Integer>>> r15) throws org.xmlpull.v1.XmlPullParserException {
        /*
            Method dump skipped, instruction units count: 348
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.permission.access.permission.DevicePermissionPersistence.parseAppId(com.android.modules.utils.BinaryXmlPullParser, com.android.server.permission.access.immutable.MutableIntReferenceMap):void");
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
    private final void parseDevice(com.android.modules.utils.BinaryXmlPullParser r14, com.android.server.permission.access.immutable.MutableIndexedReferenceMap<java.lang.String, com.android.server.permission.access.immutable.IndexedMap<java.lang.String, java.lang.Integer>, com.android.server.permission.access.immutable.MutableIndexedMap<java.lang.String, java.lang.Integer>> r15) throws org.xmlpull.v1.XmlPullParserException {
        /*
            Method dump skipped, instruction units count: 356
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.permission.access.permission.DevicePermissionPersistence.parseDevice(com.android.modules.utils.BinaryXmlPullParser, com.android.server.permission.access.immutable.MutableIndexedReferenceMap):void");
    }

    private final void parsePermission(com.android.modules.utils.BinaryXmlPullParser $this$parsePermission, com.android.server.permission.access.immutable.MutableIndexedMap<java.lang.String, java.lang.Integer> mutableIndexedMap) {
        java.lang.String name$iv = $this$parsePermission.getAttributeValue($this$parsePermission.getAttributeIndexOrThrow((java.lang.String) null, "name"));
        java.lang.String name = name$iv.intern();
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(name, "intern(...)");
        int flags = $this$parsePermission.getAttributeInt((java.lang.String) null, ATTR_FLAGS);
        mutableIndexedMap.put(name, java.lang.Integer.valueOf(flags));
    }

    public final void serializeUserState(com.android.modules.utils.BinaryXmlSerializer $this$serializeUserState, com.android.server.permission.access.AccessState state, int userId) {
        com.android.server.permission.access.immutable.Immutable immutable = state.getUserStates().get(userId);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(immutable);
        com.android.server.permission.access.immutable.IntReferenceMap<com.android.server.permission.access.immutable.IndexedReferenceMap<java.lang.String, com.android.server.permission.access.immutable.IndexedMap<java.lang.String, java.lang.Integer>, com.android.server.permission.access.immutable.MutableIndexedMap<java.lang.String, java.lang.Integer>>, com.android.server.permission.access.immutable.MutableIndexedReferenceMap<java.lang.String, com.android.server.permission.access.immutable.IndexedMap<java.lang.String, java.lang.Integer>, com.android.server.permission.access.immutable.MutableIndexedMap<java.lang.String, java.lang.Integer>>> appIdDevicePermissionFlags = ((com.android.server.permission.access.UserState) immutable).getAppIdDevicePermissionFlags();
        $this$serializeUserState.startTag((java.lang.String) null, TAG_APP_ID_DEVICE_PERMISSIONS);
        int size = appIdDevicePermissionFlags.getSize();
        for (int index$iv = 0; index$iv < size; index$iv++) {
            int appId = appIdDevicePermissionFlags.keyAt(index$iv);
            serializeAppId($this$serializeUserState, appId, (com.android.server.permission.access.immutable.IndexedReferenceMap) appIdDevicePermissionFlags.valueAt(index$iv));
        }
        $this$serializeUserState.endTag((java.lang.String) null, TAG_APP_ID_DEVICE_PERMISSIONS);
    }

    private final void serializeAppId(com.android.modules.utils.BinaryXmlSerializer $this$serializeAppId, int appId, com.android.server.permission.access.immutable.IndexedReferenceMap<java.lang.String, com.android.server.permission.access.immutable.IndexedMap<java.lang.String, java.lang.Integer>, com.android.server.permission.access.immutable.MutableIndexedMap<java.lang.String, java.lang.Integer>> indexedReferenceMap) {
        $this$serializeAppId.startTag((java.lang.String) null, TAG_APP_ID);
        $this$serializeAppId.attributeInt((java.lang.String) null, ATTR_ID, appId);
        int size = indexedReferenceMap.getSize();
        for (int index$iv = 0; index$iv < size; index$iv++) {
            java.lang.String deviceId = indexedReferenceMap.keyAt(index$iv);
            serializeDevice($this$serializeAppId, deviceId, (com.android.server.permission.access.immutable.IndexedMap) indexedReferenceMap.valueAt(index$iv));
        }
        $this$serializeAppId.endTag((java.lang.String) null, TAG_APP_ID);
    }

    private final void serializeDevice(com.android.modules.utils.BinaryXmlSerializer $this$serializeDevice, java.lang.String deviceId, com.android.server.permission.access.immutable.IndexedMap<java.lang.String, java.lang.Integer> indexedMap) {
        $this$serializeDevice.startTag((java.lang.String) null, TAG_DEVICE);
        $this$serializeDevice.attributeInterned((java.lang.String) null, ATTR_ID, deviceId);
        int size = indexedMap.getSize();
        for (int index$iv = 0; index$iv < size; index$iv++) {
            java.lang.String strKeyAt = indexedMap.keyAt(index$iv);
            int flags = indexedMap.valueAt(index$iv).intValue();
            java.lang.String name = strKeyAt;
            serializePermission($this$serializeDevice, name, flags);
        }
        $this$serializeDevice.endTag((java.lang.String) null, TAG_DEVICE);
    }

    private final void serializePermission(com.android.modules.utils.BinaryXmlSerializer $this$serializePermission, java.lang.String name, int flags) {
        int serializedFlags;
        $this$serializePermission.startTag((java.lang.String) null, "permission");
        $this$serializePermission.attributeInterned((java.lang.String) null, "name", name);
        if (com.android.server.permission.access.util.IntExtensionsKt.hasBits(flags, 2097152)) {
            serializedFlags = com.android.server.permission.access.util.IntExtensionsKt.andInv(flags, 16);
        } else {
            serializedFlags = flags;
        }
        $this$serializePermission.attributeInt((java.lang.String) null, ATTR_FLAGS, serializedFlags);
        $this$serializePermission.endTag((java.lang.String) null, "permission");
    }

    /* JADX INFO: compiled from: DevicePermissionPersistence.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\t\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u0016\u0010\u0007\u001a\n \b*\u0004\u0018\u00010\u00040\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000¨\u0006\r"}, d2 = {"Lcom/android/server/permission/access/permission/DevicePermissionPersistence$Companion;", "", "()V", "ATTR_FLAGS", "", "ATTR_ID", "ATTR_NAME", "LOG_TAG", "com.android.server.permission.jarjar.kotlin.jvm.PlatformType", "TAG_APP_ID", "TAG_APP_ID_DEVICE_PERMISSIONS", "TAG_DEVICE", "TAG_PERMISSION", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(com.android.server.permission.jarjar.kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
