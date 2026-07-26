package com.android.server.integrity.parser;

/* JADX INFO: loaded from: classes2.dex */
public class RuleMetadataParser {
    public static final java.lang.String RULE_PROVIDER_TAG = "P";
    public static final java.lang.String VERSION_TAG = "V";

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:16:0x0032  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static com.android.server.integrity.model.RuleMetadata parse(java.io.InputStream r8) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
            java.lang.String r0 = ""
            java.lang.String r1 = ""
            com.android.modules.utils.TypedXmlPullParser r2 = android.util.Xml.resolvePullParser(r8)
        L8:
            int r3 = r2.next()
            r4 = r3
            r5 = 1
            if (r3 == r5) goto L5a
            r3 = 2
            if (r4 != r3) goto L8
            java.lang.String r3 = r2.getName()
            int r6 = r3.hashCode()
            switch(r6) {
                case 80: goto L28;
                case 86: goto L1f;
                default: goto L1e;
            }
        L1e:
            goto L32
        L1f:
            java.lang.String r6 = "V"
            boolean r6 = r3.equals(r6)
            if (r6 == 0) goto L1e
            goto L33
        L28:
            java.lang.String r5 = "P"
            boolean r5 = r3.equals(r5)
            if (r5 == 0) goto L1e
            r5 = 0
            goto L33
        L32:
            r5 = -1
        L33:
            switch(r5) {
                case 0: goto L54;
                case 1: goto L4f;
                default: goto L36;
            }
        L36:
            java.lang.IllegalStateException r5 = new java.lang.IllegalStateException
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r7 = "Unknown tag in metadata: "
            java.lang.StringBuilder r6 = r6.append(r7)
            java.lang.StringBuilder r6 = r6.append(r3)
            java.lang.String r6 = r6.toString()
            r5.<init>(r6)
            throw r5
        L4f:
            java.lang.String r1 = r2.nextText()
            goto L59
        L54:
            java.lang.String r0 = r2.nextText()
        L59:
            goto L8
        L5a:
            com.android.server.integrity.model.RuleMetadata r3 = new com.android.server.integrity.model.RuleMetadata
            r3.<init>(r0, r1)
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.integrity.parser.RuleMetadataParser.parse(java.io.InputStream):com.android.server.integrity.model.RuleMetadata");
    }
}
