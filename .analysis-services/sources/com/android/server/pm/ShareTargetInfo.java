package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
class ShareTargetInfo {
    private static final java.lang.String ATTR_HOST = "host";
    private static final java.lang.String ATTR_MIME_TYPE = "mimeType";
    private static final java.lang.String ATTR_NAME = "name";
    private static final java.lang.String ATTR_PATH = "path";
    private static final java.lang.String ATTR_PATH_PATTERN = "pathPattern";
    private static final java.lang.String ATTR_PATH_PREFIX = "pathPrefix";
    private static final java.lang.String ATTR_PORT = "port";
    private static final java.lang.String ATTR_SCHEME = "scheme";
    private static final java.lang.String ATTR_TARGET_CLASS = "targetClass";
    private static final java.lang.String TAG_CATEGORY = "category";
    private static final java.lang.String TAG_DATA = "data";
    private static final java.lang.String TAG_SHARE_TARGET = "share-target";
    final java.lang.String[] mCategories;
    final java.lang.String mTargetClass;
    final com.android.server.pm.ShareTargetInfo.TargetData[] mTargetData;

    static class TargetData {
        final java.lang.String mHost;
        final java.lang.String mMimeType;
        final java.lang.String mPath;
        final java.lang.String mPathPattern;
        final java.lang.String mPathPrefix;
        final java.lang.String mPort;
        final java.lang.String mScheme;

        TargetData(java.lang.String scheme, java.lang.String host, java.lang.String port, java.lang.String path, java.lang.String pathPattern, java.lang.String pathPrefix, java.lang.String mimeType) {
            this.mScheme = scheme;
            this.mHost = host;
            this.mPort = port;
            this.mPath = path;
            this.mPathPattern = pathPattern;
            this.mPathPrefix = pathPrefix;
            this.mMimeType = mimeType;
        }

        public void toStringInner(java.lang.StringBuilder strBuilder) {
            if (!android.text.TextUtils.isEmpty(this.mScheme)) {
                strBuilder.append(" scheme=").append(this.mScheme);
            }
            if (!android.text.TextUtils.isEmpty(this.mHost)) {
                strBuilder.append(" host=").append(this.mHost);
            }
            if (!android.text.TextUtils.isEmpty(this.mPort)) {
                strBuilder.append(" port=").append(this.mPort);
            }
            if (!android.text.TextUtils.isEmpty(this.mPath)) {
                strBuilder.append(" path=").append(this.mPath);
            }
            if (!android.text.TextUtils.isEmpty(this.mPathPattern)) {
                strBuilder.append(" pathPattern=").append(this.mPathPattern);
            }
            if (!android.text.TextUtils.isEmpty(this.mPathPrefix)) {
                strBuilder.append(" pathPrefix=").append(this.mPathPrefix);
            }
            if (!android.text.TextUtils.isEmpty(this.mMimeType)) {
                strBuilder.append(" mimeType=").append(this.mMimeType);
            }
        }

        public java.lang.String toString() {
            java.lang.StringBuilder strBuilder = new java.lang.StringBuilder();
            toStringInner(strBuilder);
            return strBuilder.toString();
        }
    }

    ShareTargetInfo(com.android.server.pm.ShareTargetInfo.TargetData[] data, java.lang.String targetClass, java.lang.String[] categories) {
        this.mTargetData = data;
        this.mTargetClass = targetClass;
        this.mCategories = categories;
    }

    public java.lang.String toString() {
        java.lang.StringBuilder strBuilder = new java.lang.StringBuilder();
        strBuilder.append("targetClass=").append(this.mTargetClass);
        for (int i = 0; i < this.mTargetData.length; i++) {
            strBuilder.append(" data={");
            this.mTargetData[i].toStringInner(strBuilder);
            strBuilder.append("}");
        }
        for (int i2 = 0; i2 < this.mCategories.length; i2++) {
            strBuilder.append(" category=").append(this.mCategories[i2]);
        }
        return strBuilder.toString();
    }

    void saveToXml(com.android.modules.utils.TypedXmlSerializer out) throws java.io.IOException {
        out.startTag((java.lang.String) null, TAG_SHARE_TARGET);
        com.android.server.pm.ShortcutService.writeAttr(out, ATTR_TARGET_CLASS, this.mTargetClass);
        for (int i = 0; i < this.mTargetData.length; i++) {
            out.startTag((java.lang.String) null, "data");
            com.android.server.pm.ShortcutService.writeAttr(out, ATTR_SCHEME, this.mTargetData[i].mScheme);
            com.android.server.pm.ShortcutService.writeAttr(out, "host", this.mTargetData[i].mHost);
            com.android.server.pm.ShortcutService.writeAttr(out, ATTR_PORT, this.mTargetData[i].mPort);
            com.android.server.pm.ShortcutService.writeAttr(out, ATTR_PATH, this.mTargetData[i].mPath);
            com.android.server.pm.ShortcutService.writeAttr(out, ATTR_PATH_PATTERN, this.mTargetData[i].mPathPattern);
            com.android.server.pm.ShortcutService.writeAttr(out, ATTR_PATH_PREFIX, this.mTargetData[i].mPathPrefix);
            com.android.server.pm.ShortcutService.writeAttr(out, ATTR_MIME_TYPE, this.mTargetData[i].mMimeType);
            out.endTag((java.lang.String) null, "data");
        }
        for (int i2 = 0; i2 < this.mCategories.length; i2++) {
            out.startTag((java.lang.String) null, TAG_CATEGORY);
            com.android.server.pm.ShortcutService.writeAttr(out, "name", this.mCategories[i2]);
            out.endTag((java.lang.String) null, TAG_CATEGORY);
        }
        out.endTag((java.lang.String) null, TAG_SHARE_TARGET);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:16:0x003b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    static com.android.server.pm.ShareTargetInfo loadFromXml(com.android.modules.utils.TypedXmlPullParser r7) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
            java.lang.String r0 = "targetClass"
            java.lang.String r0 = com.android.server.pm.ShortcutService.parseStringAttribute(r7, r0)
            java.util.ArrayList r1 = new java.util.ArrayList
            r1.<init>()
            java.util.ArrayList r2 = new java.util.ArrayList
            r2.<init>()
        L11:
            int r3 = r7.next()
            r4 = r3
            r5 = 1
            if (r3 == r5) goto L65
            r3 = 2
            if (r4 != r3) goto L54
            java.lang.String r3 = r7.getName()
            int r6 = r3.hashCode()
            switch(r6) {
                case 3076010: goto L31;
                case 50511102: goto L28;
                default: goto L27;
            }
        L27:
            goto L3b
        L28:
            java.lang.String r6 = "category"
            boolean r3 = r3.equals(r6)
            if (r3 == 0) goto L27
            goto L3c
        L31:
            java.lang.String r5 = "data"
            boolean r3 = r3.equals(r5)
            if (r3 == 0) goto L27
            r5 = 0
            goto L3c
        L3b:
            r5 = -1
        L3c:
            switch(r5) {
                case 0: goto L4b;
                case 1: goto L40;
                default: goto L3f;
            }
        L3f:
            goto L53
        L40:
            java.lang.String r3 = "name"
            java.lang.String r3 = com.android.server.pm.ShortcutService.parseStringAttribute(r7, r3)
            r2.add(r3)
            goto L53
        L4b:
            com.android.server.pm.ShareTargetInfo$TargetData r3 = parseTargetData(r7)
            r1.add(r3)
        L53:
            goto L11
        L54:
            r3 = 3
            if (r4 != r3) goto L11
            java.lang.String r3 = r7.getName()
            java.lang.String r5 = "share-target"
            boolean r3 = r3.equals(r5)
            if (r3 == 0) goto L11
        L65:
            boolean r3 = r1.isEmpty()
            if (r3 != 0) goto L92
            if (r0 == 0) goto L92
            boolean r3 = r2.isEmpty()
            if (r3 == 0) goto L74
            goto L92
        L74:
            com.android.server.pm.ShareTargetInfo r3 = new com.android.server.pm.ShareTargetInfo
            int r5 = r1.size()
            com.android.server.pm.ShareTargetInfo$TargetData[] r5 = new com.android.server.pm.ShareTargetInfo.TargetData[r5]
            java.lang.Object[] r5 = r1.toArray(r5)
            com.android.server.pm.ShareTargetInfo$TargetData[] r5 = (com.android.server.pm.ShareTargetInfo.TargetData[]) r5
            int r6 = r2.size()
            java.lang.String[] r6 = new java.lang.String[r6]
            java.lang.Object[] r6 = r2.toArray(r6)
            java.lang.String[] r6 = (java.lang.String[]) r6
            r3.<init>(r5, r0, r6)
            return r3
        L92:
            r3 = 0
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.ShareTargetInfo.loadFromXml(com.android.modules.utils.TypedXmlPullParser):com.android.server.pm.ShareTargetInfo");
    }

    private static com.android.server.pm.ShareTargetInfo.TargetData parseTargetData(com.android.modules.utils.TypedXmlPullParser parser) {
        java.lang.String scheme = com.android.server.pm.ShortcutService.parseStringAttribute(parser, ATTR_SCHEME);
        java.lang.String host = com.android.server.pm.ShortcutService.parseStringAttribute(parser, "host");
        java.lang.String port = com.android.server.pm.ShortcutService.parseStringAttribute(parser, ATTR_PORT);
        java.lang.String path = com.android.server.pm.ShortcutService.parseStringAttribute(parser, ATTR_PATH);
        java.lang.String pathPattern = com.android.server.pm.ShortcutService.parseStringAttribute(parser, ATTR_PATH_PATTERN);
        java.lang.String pathPrefix = com.android.server.pm.ShortcutService.parseStringAttribute(parser, ATTR_PATH_PREFIX);
        java.lang.String mimeType = com.android.server.pm.ShortcutService.parseStringAttribute(parser, ATTR_MIME_TYPE);
        return new com.android.server.pm.ShareTargetInfo.TargetData(scheme, host, port, path, pathPattern, pathPrefix, mimeType);
    }
}
