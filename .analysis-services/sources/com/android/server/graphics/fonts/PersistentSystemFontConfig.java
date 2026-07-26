package com.android.server.graphics.fonts;

/* JADX INFO: loaded from: classes2.dex */
class PersistentSystemFontConfig {
    private static final java.lang.String ATTR_VALUE = "value";
    private static final java.lang.String TAG = "PersistentSystemFontConfig";
    private static final java.lang.String TAG_FAMILY = "family";
    private static final java.lang.String TAG_LAST_MODIFIED_DATE = "lastModifiedDate";
    private static final java.lang.String TAG_ROOT = "fontConfig";
    private static final java.lang.String TAG_UPDATED_FONT_DIR = "updatedFontDir";

    PersistentSystemFontConfig() {
    }

    static class Config {
        public long lastModifiedMillis;
        public final java.util.Set<java.lang.String> updatedFontDirs = new android.util.ArraySet();
        public final java.util.List<android.graphics.fonts.FontUpdateRequest.Family> fontFamilies = new java.util.ArrayList();

        Config() {
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:27:0x0064  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static void loadFromXml(java.io.InputStream r8, com.android.server.graphics.fonts.PersistentSystemFontConfig.Config r9) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
            com.android.modules.utils.TypedXmlPullParser r0 = android.util.Xml.resolvePullParser(r8)
        L4:
            int r1 = r0.next()
            r2 = r1
            r3 = 1
            if (r1 == r3) goto La1
            r1 = 2
            if (r2 == r1) goto L10
            goto L4
        L10:
            int r4 = r0.getDepth()
            java.lang.String r5 = r0.getName()
            java.lang.String r6 = "PersistentSystemFontConfig"
            if (r4 != r3) goto L3b
            java.lang.String r1 = "fontConfig"
            boolean r1 = r1.equals(r5)
            if (r1 != 0) goto L9f
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r3 = "Invalid root tag: "
            java.lang.StringBuilder r1 = r1.append(r3)
            java.lang.StringBuilder r1 = r1.append(r5)
            java.lang.String r1 = r1.toString()
            android.util.Slog.e(r6, r1)
            return
        L3b:
            if (r4 != r1) goto L9f
            int r7 = r5.hashCode()
            switch(r7) {
                case -1540845619: goto L59;
                case -1281860764: goto L4f;
                case -23402365: goto L45;
                default: goto L44;
            }
        L44:
            goto L64
        L45:
            java.lang.String r1 = "updatedFontDir"
            boolean r1 = r5.equals(r1)
            if (r1 == 0) goto L44
            goto L65
        L4f:
            java.lang.String r3 = "family"
            boolean r3 = r5.equals(r3)
            if (r3 == 0) goto L44
            r3 = r1
            goto L65
        L59:
            java.lang.String r1 = "lastModifiedDate"
            boolean r1 = r5.equals(r1)
            if (r1 == 0) goto L44
            r3 = 0
            goto L65
        L64:
            r3 = -1
        L65:
            java.lang.String r1 = "value"
            switch(r3) {
                case 0: goto L96;
                case 1: goto L8c;
                case 2: goto L82;
                default: goto L6b;
            }
        L6b:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r3 = "Skipping unknown tag: "
            java.lang.StringBuilder r1 = r1.append(r3)
            java.lang.StringBuilder r1 = r1.append(r5)
            java.lang.String r1 = r1.toString()
            android.util.Slog.w(r6, r1)
            goto L9f
        L82:
            java.util.List<android.graphics.fonts.FontUpdateRequest$Family> r1 = r9.fontFamilies
            android.graphics.fonts.FontUpdateRequest$Family r3 = android.graphics.fonts.FontUpdateRequest.Family.readFromXml(r0)
            r1.add(r3)
            goto L9f
        L8c:
            java.util.Set<java.lang.String> r3 = r9.updatedFontDirs
            java.lang.String r1 = getAttribute(r0, r1)
            r3.add(r1)
            goto L9f
        L96:
            r6 = 0
            long r6 = parseLongAttribute(r0, r1, r6)
            r9.lastModifiedMillis = r6
        L9f:
            goto L4
        La1:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.graphics.fonts.PersistentSystemFontConfig.loadFromXml(java.io.InputStream, com.android.server.graphics.fonts.PersistentSystemFontConfig$Config):void");
    }

    public static void writeToXml(java.io.OutputStream os, com.android.server.graphics.fonts.PersistentSystemFontConfig.Config config) throws java.io.IOException {
        com.android.modules.utils.TypedXmlSerializer out = android.util.Xml.resolveSerializer(os);
        out.startDocument((java.lang.String) null, true);
        out.startTag((java.lang.String) null, TAG_ROOT);
        out.startTag((java.lang.String) null, TAG_LAST_MODIFIED_DATE);
        out.attribute((java.lang.String) null, ATTR_VALUE, java.lang.Long.toString(config.lastModifiedMillis));
        out.endTag((java.lang.String) null, TAG_LAST_MODIFIED_DATE);
        for (java.lang.String dir : config.updatedFontDirs) {
            out.startTag((java.lang.String) null, TAG_UPDATED_FONT_DIR);
            out.attribute((java.lang.String) null, ATTR_VALUE, dir);
            out.endTag((java.lang.String) null, TAG_UPDATED_FONT_DIR);
        }
        java.util.List<android.graphics.fonts.FontUpdateRequest.Family> fontFamilies = config.fontFamilies;
        for (int i = 0; i < fontFamilies.size(); i++) {
            android.graphics.fonts.FontUpdateRequest.Family fontFamily = fontFamilies.get(i);
            out.startTag((java.lang.String) null, TAG_FAMILY);
            android.graphics.fonts.FontUpdateRequest.Family.writeFamilyToXml(out, fontFamily);
            out.endTag((java.lang.String) null, TAG_FAMILY);
        }
        out.endTag((java.lang.String) null, TAG_ROOT);
        out.endDocument();
    }

    private static long parseLongAttribute(com.android.modules.utils.TypedXmlPullParser parser, java.lang.String attr, long defValue) {
        java.lang.String value = parser.getAttributeValue((java.lang.String) null, attr);
        if (android.text.TextUtils.isEmpty(value)) {
            return defValue;
        }
        try {
            return java.lang.Long.parseLong(value);
        } catch (java.lang.NumberFormatException e) {
            return defValue;
        }
    }

    private static java.lang.String getAttribute(com.android.modules.utils.TypedXmlPullParser parser, java.lang.String attr) {
        java.lang.String value = parser.getAttributeValue((java.lang.String) null, attr);
        return value == null ? "" : value;
    }
}
