package com.android.server.net.watchlist;

/* JADX INFO: loaded from: classes2.dex */
class ReportEncoder {
    private static final int REPORT_VERSION = 1;
    private static final java.lang.String TAG = "ReportEncoder";
    private static final int WATCHLIST_HASH_SIZE = 32;

    ReportEncoder() {
    }

    static byte[] encodeWatchlistReport(com.android.server.net.watchlist.WatchlistConfig config, byte[] userSecret, java.util.List<java.lang.String> appDigestList, com.android.server.net.watchlist.WatchlistReportDbHelper.AggregatedResult aggregatedResult) {
        java.util.Map<java.lang.String, java.lang.Boolean> resultMap = com.android.server.net.watchlist.PrivacyUtils.createDpEncodedReportMap(config.isConfigSecure(), userSecret, appDigestList, aggregatedResult);
        return serializeReport(config, resultMap);
    }

    static byte[] serializeReport(com.android.server.net.watchlist.WatchlistConfig config, java.util.Map<java.lang.String, java.lang.Boolean> encodedReportMap) {
        byte[] watchlistHash = config.getWatchlistConfigHash();
        if (watchlistHash == null) {
            android.util.Log.e(TAG, "No watchlist hash");
            return null;
        }
        if (watchlistHash.length != 32) {
            android.util.Log.e(TAG, "Unexpected hash length");
            return null;
        }
        java.io.ByteArrayOutputStream reportOutputStream = new java.io.ByteArrayOutputStream();
        android.util.proto.ProtoOutputStream proto = new android.util.proto.ProtoOutputStream(reportOutputStream);
        proto.write(1120986464257L, 1);
        proto.write(1138166333442L, com.android.internal.util.HexDump.toHexString(watchlistHash));
        for (java.util.Map.Entry<java.lang.String, java.lang.Boolean> entry : encodedReportMap.entrySet()) {
            java.lang.String key = entry.getKey();
            com.android.internal.util.HexDump.hexStringToByteArray(key);
            boolean encodedResult = entry.getValue().booleanValue();
            long token = proto.start(2246267895811L);
            proto.write(1138166333441L, key);
            proto.write(1133871366146L, encodedResult);
            proto.end(token);
        }
        proto.flush();
        return reportOutputStream.toByteArray();
    }
}
