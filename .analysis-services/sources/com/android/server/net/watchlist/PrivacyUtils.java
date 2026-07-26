package com.android.server.net.watchlist;

/* JADX INFO: loaded from: classes2.dex */
class PrivacyUtils {
    private static final boolean DEBUG = false;
    private static final java.lang.String ENCODER_ID_PREFIX = "watchlist_encoder:";
    private static final double PROB_F = 0.469d;
    private static final double PROB_P = 0.28d;
    private static final double PROB_Q = 1.0d;
    private static final java.lang.String TAG = "PrivacyUtils";

    private PrivacyUtils() {
    }

    static android.privacy.DifferentialPrivacyEncoder createInsecureDPEncoderForTest(java.lang.String appDigest) {
        android.privacy.internal.longitudinalreporting.LongitudinalReportingConfig config = createLongitudinalReportingConfig(appDigest);
        return android.privacy.internal.longitudinalreporting.LongitudinalReportingEncoder.createInsecureEncoderForTest(config);
    }

    static android.privacy.DifferentialPrivacyEncoder createSecureDPEncoder(byte[] userSecret, java.lang.String appDigest) {
        android.privacy.internal.longitudinalreporting.LongitudinalReportingConfig config = createLongitudinalReportingConfig(appDigest);
        return android.privacy.internal.longitudinalreporting.LongitudinalReportingEncoder.createEncoder(config, userSecret);
    }

    private static android.privacy.internal.longitudinalreporting.LongitudinalReportingConfig createLongitudinalReportingConfig(java.lang.String appDigest) {
        return new android.privacy.internal.longitudinalreporting.LongitudinalReportingConfig(ENCODER_ID_PREFIX + appDigest, PROB_F, PROB_P, PROB_Q);
    }

    static java.util.Map<java.lang.String, java.lang.Boolean> createDpEncodedReportMap(boolean isSecure, byte[] userSecret, java.util.List<java.lang.String> appDigestList, com.android.server.net.watchlist.WatchlistReportDbHelper.AggregatedResult aggregatedResult) {
        android.privacy.DifferentialPrivacyEncoder encoder;
        int appDigestListSize = appDigestList.size();
        java.util.HashMap<java.lang.String, java.lang.Boolean> resultMap = new java.util.HashMap<>(appDigestListSize);
        for (int i = 0; i < appDigestListSize; i++) {
            java.lang.String appDigest = appDigestList.get(i);
            if (isSecure) {
                encoder = createSecureDPEncoder(userSecret, appDigest);
            } else {
                encoder = createInsecureDPEncoderForTest(appDigest);
            }
            boolean visitedWatchlist = aggregatedResult.appDigestList.contains(appDigest);
            boolean z = false;
            if ((encoder.encodeBoolean(visitedWatchlist)[0] & 1) == 1) {
                z = true;
            }
            boolean encodedVisitedWatchlist = z;
            resultMap.put(appDigest, java.lang.Boolean.valueOf(encodedVisitedWatchlist));
        }
        return resultMap;
    }
}
