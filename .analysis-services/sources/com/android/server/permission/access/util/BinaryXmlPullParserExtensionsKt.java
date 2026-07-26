package com.android.server.permission.access.util;

/* JADX INFO: compiled from: BinaryXmlPullParserExtensions.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000T\n\u0000\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\u0012\n\u0002\b\u0004\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\t\n\u0002\b\b\n\u0002\u0018\u0002\n\u0000\u001a&\u0010\u0005\u001a\u00020\u0006*\u00020\u00022\u0017\u0010\u0007\u001a\u0013\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00060\b¢\u0006\u0002\b\tH\u0086\b\u001a\u001d\u0010\n\u001a\u00020\u000b*\u00020\u00022\u0006\u0010\f\u001a\u00020\u00012\u0006\u0010\r\u001a\u00020\u000bH\u0086\b\u001a\u0015\u0010\u000e\u001a\u00020\u000b*\u00020\u00022\u0006\u0010\f\u001a\u00020\u0001H\u0086\b\u001a\u0017\u0010\u000f\u001a\u0004\u0018\u00010\u0010*\u00020\u00022\u0006\u0010\f\u001a\u00020\u0001H\u0086\b\u001a\u0015\u0010\u0011\u001a\u00020\u0010*\u00020\u00022\u0006\u0010\f\u001a\u00020\u0001H\u0086\b\u001a\u0017\u0010\u0012\u001a\u0004\u0018\u00010\u0010*\u00020\u00022\u0006\u0010\f\u001a\u00020\u0001H\u0086\b\u001a\u0015\u0010\u0013\u001a\u00020\u0010*\u00020\u00022\u0006\u0010\f\u001a\u00020\u0001H\u0086\b\u001a\u001d\u0010\u0014\u001a\u00020\u0015*\u00020\u00022\u0006\u0010\f\u001a\u00020\u00012\u0006\u0010\r\u001a\u00020\u0015H\u0086\b\u001a\u0015\u0010\u0016\u001a\u00020\u0015*\u00020\u00022\u0006\u0010\f\u001a\u00020\u0001H\u0086\b\u001a\u001d\u0010\u0017\u001a\u00020\u0018*\u00020\u00022\u0006\u0010\f\u001a\u00020\u00012\u0006\u0010\r\u001a\u00020\u0018H\u0086\b\u001a\u0015\u0010\u0019\u001a\u00020\u0018*\u00020\u00022\u0006\u0010\f\u001a\u00020\u0001H\u0086\b\u001a\u0015\u0010\u001a\u001a\u00020\u001b*\u00020\u00022\u0006\u0010\f\u001a\u00020\u0001H\u0086\b\u001a\u0015\u0010\u001c\u001a\u00020\u001b*\u00020\u00022\u0006\u0010\f\u001a\u00020\u0001H\u0086\b\u001a\u001d\u0010\u001d\u001a\u00020\u001b*\u00020\u00022\u0006\u0010\f\u001a\u00020\u00012\u0006\u0010\r\u001a\u00020\u001bH\u0086\b\u001a\u0015\u0010\u001e\u001a\u00020\u001b*\u00020\u00022\u0006\u0010\f\u001a\u00020\u0001H\u0086\b\u001a\u001d\u0010\u001f\u001a\u00020\u001b*\u00020\u00022\u0006\u0010\f\u001a\u00020\u00012\u0006\u0010\r\u001a\u00020\u001bH\u0086\b\u001a\u0015\u0010 \u001a\u00020\u001b*\u00020\u00022\u0006\u0010\f\u001a\u00020\u0001H\u0086\b\u001a\u001d\u0010!\u001a\u00020\"*\u00020\u00022\u0006\u0010\f\u001a\u00020\u00012\u0006\u0010\r\u001a\u00020\"H\u0086\b\u001a\u0015\u0010#\u001a\u00020\"*\u00020\u00022\u0006\u0010\f\u001a\u00020\u0001H\u0086\b\u001a\u001d\u0010$\u001a\u00020\"*\u00020\u00022\u0006\u0010\f\u001a\u00020\u00012\u0006\u0010\r\u001a\u00020\"H\u0086\b\u001a\u0015\u0010%\u001a\u00020\"*\u00020\u00022\u0006\u0010\f\u001a\u00020\u0001H\u0086\b\u001a\u0017\u0010&\u001a\u0004\u0018\u00010\u0001*\u00020\u00022\u0006\u0010\f\u001a\u00020\u0001H\u0086\b\u001a\u0015\u0010'\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\f\u001a\u00020\u0001H\u0086\b\u001a\u0015\u0010(\u001a\u00020\u000b*\u00020\u00022\u0006\u0010\f\u001a\u00020\u0001H\u0086\b\u001a\r\u0010)\u001a\u00020\u001b*\u00020\u0002H\u0086\b\u001a&\u0010*\u001a\u00020\u0006*\u00020+2\u0017\u0010\u0007\u001a\u0013\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00060\b¢\u0006\u0002\b\tH\u0086\b\"\u0016\u0010\u0000\u001a\u00020\u0001*\u00020\u00028Æ\u0002¢\u0006\u0006\u001a\u0004\b\u0003\u0010\u0004¨\u0006,"}, d2 = {"tagName", "", "Lcom/android/modules/utils/BinaryXmlPullParser;", "getTagName", "(Lcom/android/modules/utils/BinaryXmlPullParser;)Ljava/lang/String;", "forEachTag", "", "block", "Lkotlin/Function1;", "Lkotlin/ExtensionFunctionType;", "getAttributeBooleanOrDefault", "", "name", "defaultValue", "getAttributeBooleanOrThrow", "getAttributeBytesBase64", "", "getAttributeBytesBase64OrThrow", "getAttributeBytesHex", "getAttributeBytesHexOrThrow", "getAttributeDoubleOrDefault", "", "getAttributeDoubleOrThrow", "getAttributeFloatOrDefault", "", "getAttributeFloatOrThrow", "getAttributeIndex", "", "getAttributeIndexOrThrow", "getAttributeIntHexOrDefault", "getAttributeIntHexOrThrow", "getAttributeIntOrDefault", "getAttributeIntOrThrow", "getAttributeLongHexOrDefault", "", "getAttributeLongHexOrThrow", "getAttributeLongOrDefault", "getAttributeLongOrThrow", "getAttributeValue", "getAttributeValueOrThrow", "hasAttribute", "nextTagOrEnd", "parseBinaryXml", "Ljava/io/InputStream;", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class BinaryXmlPullParserExtensionsKt {
    public static final void parseBinaryXml(java.io.InputStream $this$parseBinaryXml, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super com.android.modules.utils.BinaryXmlPullParser, com.android.server.permission.jarjar.kotlin.Unit> function1) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        com.android.modules.utils.BinaryXmlPullParser $this$parseBinaryXml_u24lambda_u240 = new com.android.modules.utils.BinaryXmlPullParser();
        $this$parseBinaryXml_u24lambda_u240.setInput($this$parseBinaryXml, (java.lang.String) null);
        function1.invoke($this$parseBinaryXml_u24lambda_u240);
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
    public static final void forEachTag(com.android.modules.utils.BinaryXmlPullParser r9, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super com.android.modules.utils.BinaryXmlPullParser, com.android.server.permission.jarjar.kotlin.Unit> r10) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        /*
            Method dump skipped, instruction units count: 268
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.permission.access.util.BinaryXmlPullParserExtensionsKt.forEachTag(com.android.modules.utils.BinaryXmlPullParser, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1):void");
    }

    public static final int nextTagOrEnd(com.android.modules.utils.BinaryXmlPullParser $this$nextTagOrEnd) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        while (true) {
            int eventType = $this$nextTagOrEnd.next();
            switch (eventType) {
                case 1:
                case 2:
                case 3:
                    return eventType;
            }
        }
    }

    public static final java.lang.String getTagName(com.android.modules.utils.BinaryXmlPullParser $this$tagName) {
        return $this$tagName.getName();
    }

    public static final boolean hasAttribute(com.android.modules.utils.BinaryXmlPullParser $this$hasAttribute, java.lang.String name) {
        return $this$hasAttribute.getAttributeIndex((java.lang.String) null, name) != -1;
    }

    public static final int getAttributeIndex(com.android.modules.utils.BinaryXmlPullParser $this$getAttributeIndex, java.lang.String name) {
        return $this$getAttributeIndex.getAttributeIndex((java.lang.String) null, name);
    }

    public static final int getAttributeIndexOrThrow(com.android.modules.utils.BinaryXmlPullParser $this$getAttributeIndexOrThrow, java.lang.String name) throws org.xmlpull.v1.XmlPullParserException {
        return $this$getAttributeIndexOrThrow.getAttributeIndexOrThrow((java.lang.String) null, name);
    }

    public static final java.lang.String getAttributeValue(com.android.modules.utils.BinaryXmlPullParser $this$getAttributeValue, java.lang.String name) throws org.xmlpull.v1.XmlPullParserException {
        return $this$getAttributeValue.getAttributeValue((java.lang.String) null, name);
    }

    public static final java.lang.String getAttributeValueOrThrow(com.android.modules.utils.BinaryXmlPullParser $this$getAttributeValueOrThrow, java.lang.String name) throws org.xmlpull.v1.XmlPullParserException {
        return $this$getAttributeValueOrThrow.getAttributeValue($this$getAttributeValueOrThrow.getAttributeIndexOrThrow((java.lang.String) null, name));
    }

    public static final byte[] getAttributeBytesHex(com.android.modules.utils.BinaryXmlPullParser $this$getAttributeBytesHex, java.lang.String name) {
        return $this$getAttributeBytesHex.getAttributeBytesHex((java.lang.String) null, name, (byte[]) null);
    }

    public static final byte[] getAttributeBytesHexOrThrow(com.android.modules.utils.BinaryXmlPullParser $this$getAttributeBytesHexOrThrow, java.lang.String name) throws org.xmlpull.v1.XmlPullParserException {
        return $this$getAttributeBytesHexOrThrow.getAttributeBytesHex((java.lang.String) null, name);
    }

    public static final byte[] getAttributeBytesBase64(com.android.modules.utils.BinaryXmlPullParser $this$getAttributeBytesBase64, java.lang.String name) {
        return $this$getAttributeBytesBase64.getAttributeBytesBase64((java.lang.String) null, name, (byte[]) null);
    }

    public static final byte[] getAttributeBytesBase64OrThrow(com.android.modules.utils.BinaryXmlPullParser $this$getAttributeBytesBase64OrThrow, java.lang.String name) throws org.xmlpull.v1.XmlPullParserException {
        return $this$getAttributeBytesBase64OrThrow.getAttributeBytesBase64((java.lang.String) null, name);
    }

    public static final int getAttributeIntOrDefault(com.android.modules.utils.BinaryXmlPullParser $this$getAttributeIntOrDefault, java.lang.String name, int defaultValue) {
        return $this$getAttributeIntOrDefault.getAttributeInt((java.lang.String) null, name, defaultValue);
    }

    public static final int getAttributeIntOrThrow(com.android.modules.utils.BinaryXmlPullParser $this$getAttributeIntOrThrow, java.lang.String name) throws org.xmlpull.v1.XmlPullParserException {
        return $this$getAttributeIntOrThrow.getAttributeInt((java.lang.String) null, name);
    }

    public static final int getAttributeIntHexOrDefault(com.android.modules.utils.BinaryXmlPullParser $this$getAttributeIntHexOrDefault, java.lang.String name, int defaultValue) {
        return $this$getAttributeIntHexOrDefault.getAttributeIntHex((java.lang.String) null, name, defaultValue);
    }

    public static final int getAttributeIntHexOrThrow(com.android.modules.utils.BinaryXmlPullParser $this$getAttributeIntHexOrThrow, java.lang.String name) throws org.xmlpull.v1.XmlPullParserException {
        return $this$getAttributeIntHexOrThrow.getAttributeIntHex((java.lang.String) null, name);
    }

    public static final long getAttributeLongOrDefault(com.android.modules.utils.BinaryXmlPullParser $this$getAttributeLongOrDefault, java.lang.String name, long defaultValue) {
        return $this$getAttributeLongOrDefault.getAttributeLong((java.lang.String) null, name, defaultValue);
    }

    public static final long getAttributeLongOrThrow(com.android.modules.utils.BinaryXmlPullParser $this$getAttributeLongOrThrow, java.lang.String name) throws org.xmlpull.v1.XmlPullParserException {
        return $this$getAttributeLongOrThrow.getAttributeLong((java.lang.String) null, name);
    }

    public static final long getAttributeLongHexOrDefault(com.android.modules.utils.BinaryXmlPullParser $this$getAttributeLongHexOrDefault, java.lang.String name, long defaultValue) {
        return $this$getAttributeLongHexOrDefault.getAttributeLongHex((java.lang.String) null, name, defaultValue);
    }

    public static final long getAttributeLongHexOrThrow(com.android.modules.utils.BinaryXmlPullParser $this$getAttributeLongHexOrThrow, java.lang.String name) throws org.xmlpull.v1.XmlPullParserException {
        return $this$getAttributeLongHexOrThrow.getAttributeLongHex((java.lang.String) null, name);
    }

    public static final float getAttributeFloatOrDefault(com.android.modules.utils.BinaryXmlPullParser $this$getAttributeFloatOrDefault, java.lang.String name, float defaultValue) {
        return $this$getAttributeFloatOrDefault.getAttributeFloat((java.lang.String) null, name, defaultValue);
    }

    public static final float getAttributeFloatOrThrow(com.android.modules.utils.BinaryXmlPullParser $this$getAttributeFloatOrThrow, java.lang.String name) throws org.xmlpull.v1.XmlPullParserException {
        return $this$getAttributeFloatOrThrow.getAttributeFloat((java.lang.String) null, name);
    }

    public static final double getAttributeDoubleOrDefault(com.android.modules.utils.BinaryXmlPullParser $this$getAttributeDoubleOrDefault, java.lang.String name, double defaultValue) {
        return $this$getAttributeDoubleOrDefault.getAttributeDouble((java.lang.String) null, name, defaultValue);
    }

    public static final double getAttributeDoubleOrThrow(com.android.modules.utils.BinaryXmlPullParser $this$getAttributeDoubleOrThrow, java.lang.String name) throws org.xmlpull.v1.XmlPullParserException {
        return $this$getAttributeDoubleOrThrow.getAttributeDouble((java.lang.String) null, name);
    }

    public static final boolean getAttributeBooleanOrDefault(com.android.modules.utils.BinaryXmlPullParser $this$getAttributeBooleanOrDefault, java.lang.String name, boolean defaultValue) {
        return $this$getAttributeBooleanOrDefault.getAttributeBoolean((java.lang.String) null, name, defaultValue);
    }

    public static final boolean getAttributeBooleanOrThrow(com.android.modules.utils.BinaryXmlPullParser $this$getAttributeBooleanOrThrow, java.lang.String name) throws org.xmlpull.v1.XmlPullParserException {
        return $this$getAttributeBooleanOrThrow.getAttributeBoolean((java.lang.String) null, name);
    }
}
