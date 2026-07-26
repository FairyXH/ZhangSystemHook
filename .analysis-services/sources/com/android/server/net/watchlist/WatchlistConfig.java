package com.android.server.net.watchlist;

/* JADX INFO: loaded from: classes2.dex */
class WatchlistConfig {
    private static final java.lang.String NETWORK_WATCHLIST_DB_FOR_TEST_PATH = "/data/misc/network_watchlist/network_watchlist_for_test.xml";
    private static final java.lang.String NETWORK_WATCHLIST_DB_PATH = "/data/misc/network_watchlist/network_watchlist.xml";
    private static final java.lang.String TAG = "WatchlistConfig";
    private static final com.android.server.net.watchlist.WatchlistConfig sInstance = new com.android.server.net.watchlist.WatchlistConfig();
    private volatile com.android.server.net.watchlist.WatchlistConfig.CrcShaDigests mDomainDigests;
    private volatile com.android.server.net.watchlist.WatchlistConfig.CrcShaDigests mIpDigests;
    private boolean mIsSecureConfig;
    private java.io.File mXmlFile;

    private static class XmlTags {
        private static final java.lang.String CRC32_DOMAIN = "crc32-domain";
        private static final java.lang.String CRC32_IP = "crc32-ip";
        private static final java.lang.String HASH = "hash";
        private static final java.lang.String SHA256_DOMAIN = "sha256-domain";
        private static final java.lang.String SHA256_IP = "sha256-ip";
        private static final java.lang.String WATCHLIST_CONFIG = "watchlist-config";

        private XmlTags() {
        }
    }

    private static class CrcShaDigests {
        public final com.android.server.net.watchlist.HarmfulCrcs crc32s;
        public final com.android.server.net.watchlist.HarmfulDigests sha256Digests;

        CrcShaDigests(com.android.server.net.watchlist.HarmfulCrcs crc32s, com.android.server.net.watchlist.HarmfulDigests sha256Digests) {
            this.crc32s = crc32s;
            this.sha256Digests = sha256Digests;
        }
    }

    public static com.android.server.net.watchlist.WatchlistConfig getInstance() {
        return sInstance;
    }

    private WatchlistConfig() {
        this(new java.io.File(NETWORK_WATCHLIST_DB_PATH));
    }

    protected WatchlistConfig(java.io.File xmlFile) {
        this.mIsSecureConfig = true;
        this.mXmlFile = xmlFile;
        reloadConfig();
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:11:0x0050  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void reloadConfig() {
        /*
            Method dump skipped, instruction units count: 266
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.net.watchlist.WatchlistConfig.reloadConfig():void");
    }

    private void parseHashes(org.xmlpull.v1.XmlPullParser parser, java.lang.String tagName, java.util.List<byte[]> hashList) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        parser.require(2, null, tagName);
        while (parser.nextTag() == 2) {
            parser.require(2, null, "hash");
            byte[] hash = com.android.internal.util.HexDump.hexStringToByteArray(parser.nextText());
            parser.require(3, null, "hash");
            hashList.add(hash);
        }
        parser.require(3, null, tagName);
    }

    public boolean containsDomain(java.lang.String domain) {
        com.android.server.net.watchlist.WatchlistConfig.CrcShaDigests domainDigests = this.mDomainDigests;
        if (domainDigests == null) {
            return false;
        }
        int crc32 = getCrc32(domain);
        if (!domainDigests.crc32s.contains(crc32)) {
            return false;
        }
        byte[] sha256 = getSha256(domain);
        return domainDigests.sha256Digests.contains(sha256);
    }

    public boolean containsIp(java.lang.String ip) {
        com.android.server.net.watchlist.WatchlistConfig.CrcShaDigests ipDigests = this.mIpDigests;
        if (ipDigests == null) {
            return false;
        }
        int crc32 = getCrc32(ip);
        if (!ipDigests.crc32s.contains(crc32)) {
            return false;
        }
        byte[] sha256 = getSha256(ip);
        return ipDigests.sha256Digests.contains(sha256);
    }

    private int getCrc32(java.lang.String str) {
        java.util.zip.CRC32 crc = new java.util.zip.CRC32();
        crc.update(str.getBytes());
        return (int) crc.getValue();
    }

    private byte[] getSha256(java.lang.String str) {
        try {
            java.security.MessageDigest messageDigest = java.security.MessageDigest.getInstance("SHA256");
            messageDigest.update(str.getBytes());
            return messageDigest.digest();
        } catch (java.security.NoSuchAlgorithmException e) {
            return null;
        }
    }

    public boolean isConfigSecure() {
        return this.mIsSecureConfig;
    }

    public byte[] getWatchlistConfigHash() {
        if (!this.mXmlFile.exists()) {
            return null;
        }
        try {
            return com.android.server.net.watchlist.DigestUtils.getSha256Hash(this.mXmlFile);
        } catch (java.io.IOException | java.security.NoSuchAlgorithmException e) {
            android.util.Log.e(TAG, "Unable to get watchlist config hash", e);
            return null;
        }
    }

    public void setTestMode(java.io.InputStream testConfigInputStream) throws java.io.IOException {
        android.util.Log.i(TAG, "Setting watchlist testing config");
        android.os.FileUtils.copyToFileOrThrow(testConfigInputStream, new java.io.File(NETWORK_WATCHLIST_DB_FOR_TEST_PATH));
        this.mIsSecureConfig = false;
        this.mXmlFile = new java.io.File(NETWORK_WATCHLIST_DB_FOR_TEST_PATH);
        reloadConfig();
    }

    public void removeTestModeConfig() {
        try {
            java.io.File f = new java.io.File(NETWORK_WATCHLIST_DB_FOR_TEST_PATH);
            if (f.exists()) {
                f.delete();
            }
        } catch (java.lang.Exception e) {
            android.util.Log.e(TAG, "Unable to delete test config");
        }
    }

    public void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
        byte[] hash = getWatchlistConfigHash();
        pw.println("Watchlist config hash: " + (hash != null ? com.android.internal.util.HexDump.toHexString(hash) : null));
        pw.println("Domain CRC32 digest list:");
        if (this.mDomainDigests != null) {
            this.mDomainDigests.crc32s.dump(fd, pw, args);
        }
        pw.println("Domain SHA256 digest list:");
        if (this.mDomainDigests != null) {
            this.mDomainDigests.sha256Digests.dump(fd, pw, args);
        }
        pw.println("Ip CRC32 digest list:");
        if (this.mIpDigests != null) {
            this.mIpDigests.crc32s.dump(fd, pw, args);
        }
        pw.println("Ip SHA256 digest list:");
        if (this.mIpDigests != null) {
            this.mIpDigests.sha256Digests.dump(fd, pw, args);
        }
    }
}
