package com.android.server.net.watchlist;

/* JADX INFO: loaded from: classes2.dex */
class WatchlistSettings {
    private static final java.lang.String FILE_NAME = "watchlist_settings.xml";
    private static final int SECRET_KEY_LENGTH = 48;
    private static final java.lang.String TAG = "WatchlistSettings";
    private static final com.android.server.net.watchlist.WatchlistSettings sInstance = new com.android.server.net.watchlist.WatchlistSettings();
    private byte[] mPrivacySecretKey;
    private final android.util.AtomicFile mXmlFile;

    public static com.android.server.net.watchlist.WatchlistSettings getInstance() {
        return sInstance;
    }

    private WatchlistSettings() {
        this(getSystemWatchlistFile());
    }

    static java.io.File getSystemWatchlistFile() {
        return new java.io.File(android.os.Environment.getDataSystemDirectory(), FILE_NAME);
    }

    protected WatchlistSettings(java.io.File xmlFile) {
        this.mPrivacySecretKey = null;
        this.mXmlFile = new android.util.AtomicFile(xmlFile, "net-watchlist");
        reloadSettings();
        if (this.mPrivacySecretKey == null) {
            this.mPrivacySecretKey = generatePrivacySecretKey();
            saveSettings();
        }
    }

    private void reloadSettings() {
        if (!this.mXmlFile.exists()) {
            return;
        }
        try {
            java.io.FileInputStream stream = this.mXmlFile.openRead();
            try {
                com.android.modules.utils.TypedXmlPullParser parser = android.util.Xml.resolvePullParser(stream);
                com.android.internal.util.XmlUtils.beginDocument(parser, "network-watchlist-settings");
                int outerDepth = parser.getDepth();
                while (com.android.internal.util.XmlUtils.nextElementWithin(parser, outerDepth)) {
                    if (parser.getName().equals("secret-key")) {
                        this.mPrivacySecretKey = parseSecretKey(parser);
                    }
                }
                android.util.Slog.i(TAG, "Reload watchlist settings done");
                if (stream != null) {
                    stream.close();
                }
            } catch (java.lang.Throwable th) {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (java.lang.Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        } catch (java.io.IOException | java.lang.IllegalStateException | java.lang.IndexOutOfBoundsException | java.lang.NullPointerException | java.lang.NumberFormatException | org.xmlpull.v1.XmlPullParserException e) {
            android.util.Slog.e(TAG, "Failed parsing xml", e);
        }
    }

    private byte[] parseSecretKey(org.xmlpull.v1.XmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        parser.require(2, null, "secret-key");
        byte[] key = com.android.internal.util.HexDump.hexStringToByteArray(parser.nextText());
        parser.require(3, null, "secret-key");
        if (key == null || key.length != 48) {
            android.util.Log.e(TAG, "Unable to parse secret key");
            return null;
        }
        return key;
    }

    synchronized byte[] getPrivacySecretKey() {
        byte[] key;
        key = new byte[48];
        java.lang.System.arraycopy(this.mPrivacySecretKey, 0, key, 0, 48);
        return key;
    }

    private byte[] generatePrivacySecretKey() {
        byte[] key = new byte[48];
        new java.security.SecureRandom().nextBytes(key);
        return key;
    }

    private void saveSettings() {
        try {
            java.io.FileOutputStream stream = this.mXmlFile.startWrite();
            try {
                com.android.modules.utils.TypedXmlSerializer out = android.util.Xml.resolveSerializer(stream);
                out.startDocument((java.lang.String) null, true);
                out.startTag((java.lang.String) null, "network-watchlist-settings");
                out.startTag((java.lang.String) null, "secret-key");
                out.text(com.android.internal.util.HexDump.toHexString(this.mPrivacySecretKey));
                out.endTag((java.lang.String) null, "secret-key");
                out.endTag((java.lang.String) null, "network-watchlist-settings");
                out.endDocument();
                this.mXmlFile.finishWrite(stream);
            } catch (java.io.IOException e) {
                android.util.Log.w(TAG, "Failed to write display settings, restoring backup.", e);
                this.mXmlFile.failWrite(stream);
            }
        } catch (java.io.IOException e2) {
            android.util.Log.w(TAG, "Failed to write display settings: " + e2);
        }
    }
}
