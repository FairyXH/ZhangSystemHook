package com.android.server.location.interfaces;

/* JADX INFO: loaded from: classes2.dex */
public interface IOplusLocationStatistics extends android.common.IOplusCommonFeature {
    public static final com.android.server.location.interfaces.IOplusLocationStatistics DEFAULT = new com.android.server.location.interfaces.IOplusLocationStatistics() { // from class: com.android.server.location.interfaces.IOplusLocationStatistics.1
    };
    public static final int GEOCODER_ERROR_FAILED = 3;
    public static final int GEOCODER_ERROR_NO_RESULT = 2;
    public static final int GEOCODER_ERROR_NO_SERVICE = 1;
    public static final int GEOCODER_ERROR_REPEAT_REQUEST = 4;
    public static final int GEOCODER_ERROR_REQUEST_REJECTED = 5;
    public static final int GNSS_STRATEGY_OF_ACTIVITY_STOP = 301;
    public static final int GNSS_STRATEGY_OF_ACTIVITY_TOTAL = 3;
    public static final int GNSS_STRATEGY_OF_ACTIVITY_WIFI_STOP = 501;
    public static final int GNSS_STRATEGY_OF_ACTIVITY_WIFI_TOTAL = 5;
    public static final int GNSS_STRATEGY_OF_GPS_IN_DOOR = 302;
    public static final int GNSS_STRATEGY_OF_LIGHT_STILL_CONDITION = 2;
    public static final int GNSS_STRATEGY_OF_STILL_CONDITION = 1;
    public static final int GNSS_STRATEGY_OF_WIFI_INLIST = 402;
    public static final int GNSS_STRATEGY_OF_WIFI_STOP = 401;
    public static final int GNSS_STRATEGY_OF_WIFI_TOTAL = 4;
    public static final int NLP_ERROR_NO_NETWORK = 5;
    public static final int NLP_ERROR_NO_RESPONSE = 2;
    public static final int NLP_ERROR_NO_SERVICE = 1;
    public static final int NLP_ERROR_REPEAT_REQUEST = 4;
    public static final int NLP_ERROR_REQUEST_SHORT = 3;
    public static final java.lang.String Name = "IOplusLocationStatistics";
    public static final int TYPE_CACHE_OPLUS = 1;
    public static final int TYPE_CACHE_ORIGIN = 0;
    public static final int TYPE_CACHE_UNREPORTED = 2;

    default android.common.OplusFeatureList.OplusIndex index() {
        return android.common.OplusFeatureList.OplusIndex.IOplusLocationStatistics;
    }

    default android.common.IOplusCommonFeature getDefault() {
        return DEFAULT;
    }

    default boolean handleCommand(java.lang.String provider, java.lang.String command, android.os.Bundle extras) {
        return false;
    }

    default void stopRequesting(android.location.util.identity.CallerIdentity identity, java.lang.String providerName, long intervalMs, java.lang.String hash) {
    }

    default void startRequesting(android.location.util.identity.CallerIdentity identity, java.lang.String providerName, long intervalMs, boolean isForeground, java.lang.String hash) {
    }

    default void deliverLocation(android.location.util.identity.CallerIdentity identity, java.lang.String providerName, java.lang.String hash) {
    }

    default void updateForeground(android.location.util.identity.CallerIdentity identity, java.lang.String providerName, boolean isForeground) {
    }

    default void recordGnssNavigatingStarted(long interval) {
    }

    default void recordGnssNavigatingStopped() {
    }

    default void recordGnssPowerSaveStarted(int strategyCode) {
    }

    default void recordGnssPowerSaveStopped(int strategyCode) {
    }

    default void recordSvInfo(android.location.GnssStatus svStatus) {
    }

    default void recordMtkMetalCaseStat(java.lang.String cmd) {
    }

    default void recordHeldWakelock(java.lang.String name) {
    }

    default void recordReleaseWakelock(java.lang.String name) {
    }

    default void recordNlpNavigatingStarted() {
    }

    default void recordNlpNavigatingStopped() {
    }

    default void recordNlpLocationUpdate() {
    }

    default void recordNlpError(int code) {
    }

    default void recordNlpScanWifiTotal(java.lang.String packageName) {
    }

    default void recordNlpScanWifiSucceed(java.lang.String packageName) {
    }

    default void recordGeocoderRequestStarted() {
    }

    default void recordGeocoderRequestStopped(long costTime) {
    }

    default void recordGeocoderError(int code) {
    }

    default void recordRgcRequestStarted() {
    }

    default void recordRgcRequestStopped(long costTime) {
    }

    default void recordRgcError(int code) {
    }

    default void recordOplusCacheRequestNlp() {
    }

    default void recordCacheReported(int type, boolean reported, long locationTimestamp, int permissionLevel) {
    }

    default void forceStopStatistics() {
    }

    default void startPowerStatistics() {
    }

    default void stopPowerStatistics() {
    }

    default void resetPowerStatistics() {
    }

    default java.lang.String collectPowerStatistics() {
        return "";
    }

    default boolean recordTaskMark(java.lang.String key) {
        return false;
    }

    default boolean releaseTaskMark(java.lang.String key) {
        return false;
    }

    default boolean recordTaskTime(java.lang.String key, java.lang.Long time) {
        return false;
    }
}
