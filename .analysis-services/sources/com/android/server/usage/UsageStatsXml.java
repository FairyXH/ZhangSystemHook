package com.android.server.usage;

/* JADX INFO: loaded from: classes3.dex */
public class UsageStatsXml {
    static final java.lang.String CHECKED_IN_SUFFIX = "-c";
    private static final java.lang.String TAG = "UsageStatsXml";
    private static final java.lang.String USAGESTATS_TAG = "usagestats";
    private static final java.lang.String VERSION_ATTR = "version";

    public static void read(java.io.InputStream in, com.android.server.usage.IntervalStats statsOut) throws java.io.IOException {
        org.xmlpull.v1.XmlPullParser parser = android.util.Xml.newPullParser();
        try {
            parser.setInput(in, "utf-8");
            com.android.internal.util.XmlUtils.beginDocument(parser, USAGESTATS_TAG);
            java.lang.String versionStr = parser.getAttributeValue(null, VERSION_ATTR);
            try {
                switch (java.lang.Integer.parseInt(versionStr)) {
                    case 1:
                        com.android.server.usage.UsageStatsXmlV1.read(parser, statsOut);
                        return;
                    default:
                        android.util.Slog.e(TAG, "Unrecognized version " + versionStr);
                        throw new java.io.IOException("Unrecognized version " + versionStr);
                }
            } catch (java.lang.NumberFormatException e) {
                android.util.Slog.e(TAG, "Bad version");
                throw new java.io.IOException(e);
            }
        } catch (org.xmlpull.v1.XmlPullParserException e2) {
            android.util.Slog.e(TAG, "Failed to parse Xml", e2);
            throw new java.io.IOException(e2);
        }
    }
}
