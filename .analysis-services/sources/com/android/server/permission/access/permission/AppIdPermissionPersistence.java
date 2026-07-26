package com.android.server.permission.access.permission;

/* JADX INFO: compiled from: AppIdPermissionPersistence.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u0000 +2\u00020\u0001:\u0001+B\u0005¢\u0006\u0002\u0010\u0002J<\u0010\u0003\u001a\u00020\u0004*\u00020\u00052.\u0010\u0006\u001a*\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\n0\b\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\n0\u000b0\u0007j\u0002`\fH\u0002J \u0010\r\u001a\u00020\u0004*\u00020\u00052\u0012\u0010\u000e\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\n0\u000bH\u0002J\u001c\u0010\u000f\u001a\u00020\u0004*\u00020\u00052\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\nH\u0002J \u0010\u0013\u001a\u00020\u0004*\u00020\u00052\u0012\u0010\u0014\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u00150\u000bH\u0002J\u001c\u0010\u0016\u001a\u00020\u0004*\u00020\u00052\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0017\u001a\u00020\u0018H\u0002J\u0012\u0010\u0019\u001a\u00020\u0004*\u00020\u00052\u0006\u0010\u0010\u001a\u00020\u0011J\u001a\u0010\u001a\u001a\u00020\u0004*\u00020\u00052\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\nJ(\u0010\u001b\u001a\u00020\u0004*\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\n2\u0012\u0010\u000e\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\n0\bH\u0002J\u001c\u0010\u001e\u001a\u00020\u0004*\u00020\u001c2\u0006\u0010\u001f\u001a\u00020\t2\u0006\u0010 \u001a\u00020\nH\u0002J<\u0010!\u001a\u00020\u0004*\u00020\u001c2.\u0010\u0006\u001a*\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\n0\b\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\n0\u000b0\"j\u0002`#H\u0002J\u0014\u0010$\u001a\u00020\u0004*\u00020\u001c2\u0006\u0010%\u001a\u00020\u0015H\u0002J(\u0010&\u001a\u00020\u0004*\u00020\u001c2\u0006\u0010'\u001a\u00020\t2\u0012\u0010\u0014\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u00150\bH\u0002J\u0012\u0010(\u001a\u00020\u0004*\u00020\u001c2\u0006\u0010\u0010\u001a\u00020)J\u001a\u0010*\u001a\u00020\u0004*\u00020\u001c2\u0006\u0010\u0010\u001a\u00020)2\u0006\u0010\u0012\u001a\u00020\n¨\u0006,"}, d2 = {"Lcom/android/server/permission/access/permission/AppIdPermissionPersistence;", "", "()V", "parseAppId", "", "Lcom/android/modules/utils/BinaryXmlPullParser;", "appIdPermissionFlags", "Lcom/android/server/permission/access/immutable/MutableIntReferenceMap;", "Lcom/android/server/permission/access/immutable/IndexedMap;", "", "", "Lcom/android/server/permission/access/immutable/MutableIndexedMap;", "Lcom/android/server/permission/access/MutableAppIdPermissionFlags;", "parseAppIdPermission", "permissionFlags", "parseAppIdPermissions", "state", "Lcom/android/server/permission/access/MutableAccessState;", "userId", "parsePermission", com.android.server.permission.access.permission.AppIdPermissionPersistence.TAG_PERMISSIONS, "Lcom/android/server/permission/access/permission/Permission;", "parsePermissions", "isPermissionTree", "", "parseSystemState", "parseUserState", "serializeAppId", "Lcom/android/modules/utils/BinaryXmlSerializer;", "appId", "serializeAppIdPermission", "name", com.android.server.permission.access.permission.AppIdPermissionPersistence.ATTR_FLAGS, "serializeAppIdPermissions", "Lcom/android/server/permission/access/immutable/IntReferenceMap;", "Lcom/android/server/permission/access/AppIdPermissionFlags;", "serializePermission", "permission", "serializePermissions", "tagName", "serializeSystemState", "Lcom/android/server/permission/access/AccessState;", "serializeUserState", "Companion", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class AppIdPermissionPersistence {
    private static final java.lang.String ATTR_FLAGS = "flags";
    private static final java.lang.String ATTR_ICON = "icon";
    private static final java.lang.String ATTR_ID = "id";
    private static final java.lang.String ATTR_LABEL = "label";
    private static final java.lang.String ATTR_NAME = "name";
    private static final java.lang.String ATTR_PACKAGE_NAME = "packageName";
    private static final java.lang.String ATTR_PROTECTION_LEVEL = "protectionLevel";
    private static final java.lang.String ATTR_TYPE = "type";
    public static final com.android.server.permission.access.permission.AppIdPermissionPersistence.Companion Companion = new com.android.server.permission.access.permission.AppIdPermissionPersistence.Companion(null);
    private static final java.lang.String LOG_TAG = com.android.server.permission.access.permission.AppIdPermissionPersistence.class.getSimpleName();
    private static final java.lang.String TAG_APP_ID = "app-id";
    private static final java.lang.String TAG_APP_ID_PERMISSIONS = "app-id-permissions";
    private static final java.lang.String TAG_PERMISSION = "permission";
    private static final java.lang.String TAG_PERMISSIONS = "permissions";
    private static final java.lang.String TAG_PERMISSION_TREES = "permission-trees";

    public final void parseSystemState(com.android.modules.utils.BinaryXmlPullParser $this$parseSystemState, com.android.server.permission.access.MutableAccessState state) throws org.xmlpull.v1.XmlPullParserException {
        java.lang.String name = $this$parseSystemState.getName();
        if (!com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.areEqual(name, TAG_PERMISSION_TREES)) {
            if (com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.areEqual(name, TAG_PERMISSIONS)) {
                parsePermissions($this$parseSystemState, state, false);
                return;
            }
            return;
        }
        parsePermissions($this$parseSystemState, state, true);
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
    private final void parsePermissions(com.android.modules.utils.BinaryXmlPullParser r18, com.android.server.permission.access.MutableAccessState r19, boolean r20) throws org.xmlpull.v1.XmlPullParserException {
        /*
            Method dump skipped, instruction units count: 488
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.permission.access.permission.AppIdPermissionPersistence.parsePermissions(com.android.modules.utils.BinaryXmlPullParser, com.android.server.permission.access.MutableAccessState, boolean):void");
    }

    private final void parsePermission(com.android.modules.utils.BinaryXmlPullParser $this$parsePermission, com.android.server.permission.access.immutable.MutableIndexedMap<java.lang.String, com.android.server.permission.access.permission.Permission> mutableIndexedMap) {
        java.lang.String name$iv = $this$parsePermission.getAttributeValue($this$parsePermission.getAttributeIndexOrThrow((java.lang.String) null, "name"));
        java.lang.String name = name$iv.intern();
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(name, "intern(...)");
        android.content.pm.PermissionInfo $this$parsePermission_u24lambda_u242 = new android.content.pm.PermissionInfo();
        $this$parsePermission_u24lambda_u242.name = name;
        java.lang.String name$iv2 = $this$parsePermission.getAttributeValue($this$parsePermission.getAttributeIndexOrThrow((java.lang.String) null, "packageName"));
        java.lang.String name$iv3 = name$iv2.intern();
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(name$iv3, "intern(...)");
        $this$parsePermission_u24lambda_u242.packageName = name$iv3;
        $this$parsePermission_u24lambda_u242.protectionLevel = $this$parsePermission.getAttributeIntHex((java.lang.String) null, ATTR_PROTECTION_LEVEL);
        int type = $this$parsePermission.getAttributeInt((java.lang.String) null, "type");
        switch (type) {
            case 0:
                break;
            case 1:
            default:
                android.util.Slog.w(LOG_TAG, "Ignoring permission " + name + " with unknown type " + type);
                return;
            case 2:
                $this$parsePermission_u24lambda_u242.icon = $this$parsePermission.getAttributeIntHex((java.lang.String) null, ATTR_ICON, 0);
                java.lang.String name$iv4 = $this$parsePermission.getAttributeValue((java.lang.String) null, ATTR_LABEL);
                $this$parsePermission_u24lambda_u242.nonLocalizedLabel = name$iv4;
                break;
        }
        com.android.server.permission.access.permission.Permission permission = new com.android.server.permission.access.permission.Permission($this$parsePermission_u24lambda_u242, false, type, 0, null, false, 48, null);
        mutableIndexedMap.put(name, permission);
    }

    public final void serializeSystemState(com.android.modules.utils.BinaryXmlSerializer $this$serializeSystemState, com.android.server.permission.access.AccessState state) {
        com.android.server.permission.access.SystemState systemState = state.getSystemState();
        serializePermissions($this$serializeSystemState, TAG_PERMISSION_TREES, systemState.getPermissionTrees());
        serializePermissions($this$serializeSystemState, TAG_PERMISSIONS, systemState.getPermissions());
    }

    private final void serializePermissions(com.android.modules.utils.BinaryXmlSerializer $this$serializePermissions, java.lang.String tagName, com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.permission.Permission> indexedMap) {
        $this$serializePermissions.startTag((java.lang.String) null, tagName);
        int size = indexedMap.getSize();
        for (int index$iv = 0; index$iv < size; index$iv++) {
            indexedMap.keyAt(index$iv);
            com.android.server.permission.access.permission.Permission it = indexedMap.valueAt(index$iv);
            serializePermission($this$serializePermissions, it);
        }
        $this$serializePermissions.endTag((java.lang.String) null, tagName);
    }

    private final void serializePermission(com.android.modules.utils.BinaryXmlSerializer $this$serializePermission, com.android.server.permission.access.permission.Permission permission) {
        java.lang.String it;
        $this$serializePermission.startTag((java.lang.String) null, "permission");
        java.lang.String value$iv = permission.getPermissionInfo().name;
        $this$serializePermission.attributeInterned((java.lang.String) null, "name", value$iv);
        java.lang.String value$iv2 = permission.getPermissionInfo().packageName;
        $this$serializePermission.attributeInterned((java.lang.String) null, "packageName", value$iv2);
        int value$iv3 = permission.getPermissionInfo().protectionLevel;
        $this$serializePermission.attributeIntHex((java.lang.String) null, ATTR_PROTECTION_LEVEL, value$iv3);
        int type = permission.getType();
        $this$serializePermission.attributeInt((java.lang.String) null, "type", type);
        if (type == 2) {
            android.content.pm.PermissionInfo permissionInfo = permission.getPermissionInfo();
            int value$iv4 = permissionInfo.icon;
            if (value$iv4 != 0) {
                $this$serializePermission.attributeIntHex((java.lang.String) null, ATTR_ICON, value$iv4);
            }
            java.lang.CharSequence charSequence = permissionInfo.nonLocalizedLabel;
            if (charSequence != null && (it = charSequence.toString()) != null) {
                $this$serializePermission.attribute((java.lang.String) null, ATTR_LABEL, it);
            }
        }
        $this$serializePermission.endTag((java.lang.String) null, "permission");
    }

    public final void parseUserState(com.android.modules.utils.BinaryXmlPullParser $this$parseUserState, com.android.server.permission.access.MutableAccessState state, int userId) throws org.xmlpull.v1.XmlPullParserException {
        if (com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.areEqual($this$parseUserState.getName(), TAG_APP_ID_PERMISSIONS)) {
            parseAppIdPermissions($this$parseUserState, state, userId);
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
    private final void parseAppIdPermissions(com.android.modules.utils.BinaryXmlPullParser r17, com.android.server.permission.access.MutableAccessState r18, int r19) throws org.xmlpull.v1.XmlPullParserException {
        /*
            Method dump skipped, instruction units count: 440
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.permission.access.permission.AppIdPermissionPersistence.parseAppIdPermissions(com.android.modules.utils.BinaryXmlPullParser, com.android.server.permission.access.MutableAccessState, int):void");
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
    private final void parseAppId(com.android.modules.utils.BinaryXmlPullParser r14, com.android.server.permission.access.immutable.MutableIntReferenceMap<com.android.server.permission.access.immutable.IndexedMap<java.lang.String, java.lang.Integer>, com.android.server.permission.access.immutable.MutableIndexedMap<java.lang.String, java.lang.Integer>> r15) throws org.xmlpull.v1.XmlPullParserException {
        /*
            Method dump skipped, instruction units count: 350
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.permission.access.permission.AppIdPermissionPersistence.parseAppId(com.android.modules.utils.BinaryXmlPullParser, com.android.server.permission.access.immutable.MutableIntReferenceMap):void");
    }

    private final void parseAppIdPermission(com.android.modules.utils.BinaryXmlPullParser $this$parseAppIdPermission, com.android.server.permission.access.immutable.MutableIndexedMap<java.lang.String, java.lang.Integer> mutableIndexedMap) {
        java.lang.String name$iv = $this$parseAppIdPermission.getAttributeValue($this$parseAppIdPermission.getAttributeIndexOrThrow((java.lang.String) null, "name"));
        java.lang.String name = name$iv.intern();
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(name, "intern(...)");
        int flags = $this$parseAppIdPermission.getAttributeInt((java.lang.String) null, ATTR_FLAGS);
        mutableIndexedMap.put(name, java.lang.Integer.valueOf(flags));
    }

    public final void serializeUserState(com.android.modules.utils.BinaryXmlSerializer $this$serializeUserState, com.android.server.permission.access.AccessState state, int userId) {
        com.android.server.permission.access.immutable.Immutable immutable = state.getUserStates().get(userId);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(immutable);
        serializeAppIdPermissions($this$serializeUserState, ((com.android.server.permission.access.UserState) immutable).getAppIdPermissionFlags());
    }

    private final void serializeAppIdPermissions(com.android.modules.utils.BinaryXmlSerializer $this$serializeAppIdPermissions, com.android.server.permission.access.immutable.IntReferenceMap<com.android.server.permission.access.immutable.IndexedMap<java.lang.String, java.lang.Integer>, com.android.server.permission.access.immutable.MutableIndexedMap<java.lang.String, java.lang.Integer>> intReferenceMap) {
        $this$serializeAppIdPermissions.startTag((java.lang.String) null, TAG_APP_ID_PERMISSIONS);
        int size = intReferenceMap.getSize();
        for (int index$iv = 0; index$iv < size; index$iv++) {
            int appId = intReferenceMap.keyAt(index$iv);
            serializeAppId($this$serializeAppIdPermissions, appId, (com.android.server.permission.access.immutable.IndexedMap) intReferenceMap.valueAt(index$iv));
        }
        $this$serializeAppIdPermissions.endTag((java.lang.String) null, TAG_APP_ID_PERMISSIONS);
    }

    private final void serializeAppId(com.android.modules.utils.BinaryXmlSerializer $this$serializeAppId, int appId, com.android.server.permission.access.immutable.IndexedMap<java.lang.String, java.lang.Integer> indexedMap) {
        $this$serializeAppId.startTag((java.lang.String) null, TAG_APP_ID);
        $this$serializeAppId.attributeInt((java.lang.String) null, ATTR_ID, appId);
        int size = indexedMap.getSize();
        for (int index$iv = 0; index$iv < size; index$iv++) {
            java.lang.String strKeyAt = indexedMap.keyAt(index$iv);
            int flags = indexedMap.valueAt(index$iv).intValue();
            java.lang.String name = strKeyAt;
            serializeAppIdPermission($this$serializeAppId, name, flags);
        }
        $this$serializeAppId.endTag((java.lang.String) null, TAG_APP_ID);
    }

    private final void serializeAppIdPermission(com.android.modules.utils.BinaryXmlSerializer $this$serializeAppIdPermission, java.lang.String name, int flags) {
        int serializedFlags;
        $this$serializeAppIdPermission.startTag((java.lang.String) null, "permission");
        $this$serializeAppIdPermission.attributeInterned((java.lang.String) null, "name", name);
        if (com.android.server.permission.access.util.IntExtensionsKt.hasBits(flags, 2097152)) {
            serializedFlags = com.android.server.permission.access.util.IntExtensionsKt.andInv(flags, 16);
        } else {
            serializedFlags = flags;
        }
        $this$serializeAppIdPermission.attributeInt((java.lang.String) null, ATTR_FLAGS, serializedFlags);
        $this$serializeAppIdPermission.endTag((java.lang.String) null, "permission");
    }

    /* JADX INFO: compiled from: AppIdPermissionPersistence.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u000f\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u0016\u0010\f\u001a\n \r*\u0004\u0018\u00010\u00040\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000¨\u0006\u0013"}, d2 = {"Lcom/android/server/permission/access/permission/AppIdPermissionPersistence$Companion;", "", "()V", "ATTR_FLAGS", "", "ATTR_ICON", "ATTR_ID", "ATTR_LABEL", "ATTR_NAME", "ATTR_PACKAGE_NAME", "ATTR_PROTECTION_LEVEL", "ATTR_TYPE", "LOG_TAG", "com.android.server.permission.jarjar.kotlin.jvm.PlatformType", "TAG_APP_ID", "TAG_APP_ID_PERMISSIONS", "TAG_PERMISSION", "TAG_PERMISSIONS", "TAG_PERMISSION_TREES", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(com.android.server.permission.jarjar.kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
