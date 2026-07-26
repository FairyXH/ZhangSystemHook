package com.android.server.location.interfaces;

/* JADX INFO: loaded from: classes2.dex */
public interface IVirtualGnssHal extends com.android.server.location.common.IOplusCommonFeature {
    public static final com.android.server.location.interfaces.IVirtualGnssHal DEFAULT = new com.android.server.location.interfaces.IVirtualGnssHal() { // from class: com.android.server.location.interfaces.IVirtualGnssHal.1
    };
    public static final java.lang.String Name = "IVirtualGnssHal";
    public static final java.lang.String value = "DebugState";

    @Override // com.android.server.location.common.IOplusCommonFeature
    default com.android.server.location.common.OplusLbsFeatureList.OplusIndex index() {
        return com.android.server.location.common.OplusLbsFeatureList.OplusIndex.IVirtualGnssHal;
    }

    @Override // com.android.server.location.common.IOplusCommonFeature
    default com.android.server.location.common.IOplusCommonFeature getDefault() {
        return DEFAULT;
    }

    default com.android.server.location.gnss.hal.GnssNative.GnssHal getVirtualGnssHal(boolean hasGpsFeature) {
        return null;
    }

    default void restartHal() {
    }

    default void setIsNavigationMessageCollectionSupported(boolean supported) {
    }

    default void setIsAntennaInfoListeningSupported(boolean supported) {
    }

    default void setIsMeasurementSupported(boolean supported) {
    }

    default void setIsMeasurementCorrectionsSupported(boolean supported) {
    }

    default void setBatchSize(int batchSize) {
    }

    default void setIsGeofencingSupported(boolean supported) {
    }

    default void setPowerStats(com.android.server.location.gnss.GnssPowerStats powerStats) {
    }

    default void setIsVisibilityControlSupported(boolean supported) {
    }

    default void reportLocation(android.location.Location location) {
    }

    default void reportNavigationMessage(android.location.GnssNavigationMessage message) {
    }

    default void reportAntennaInfo(java.util.List<android.location.GnssAntennaInfo> antennaInfos) {
    }

    default boolean isMeasurementCollectionFullTracking() {
        return true;
    }

    default void reportMeasurement(android.location.GnssMeasurementsEvent event) {
    }

    default android.location.GnssMeasurementCorrections getLastInjectedCorrections() {
        return null;
    }

    default void classInitOnce() {
    }

    default boolean isSupported() {
        return true;
    }

    default void initOnce(com.android.server.location.gnss.hal.GnssNative gnssNative, boolean reinitializeGnssServiceHandle) {
    }

    default boolean init() {
        return true;
    }

    default void cleanup() {
    }

    default boolean start() {
        return true;
    }

    default boolean stop() {
        return true;
    }

    default boolean setPositionMode(int mode, int recurrence, int minInterval, int preferredAccuracy, int preferredTime, boolean lowPowerMode) {
        return true;
    }

    default java.lang.String getInternalState() {
        return value;
    }

    default void deleteAidingData(int flags) {
    }

    default int readNmea(byte[] buffer, int bufferSize) {
        return 0;
    }

    default void injectLocation(int gnssLocationFlags, double latitude, double longitude, double altitude, float speed, float bearing, float horizontalAccuracy, float verticalAccuracy, float speedAccuracy, float bearingAccuracy, long timestamp, int elapsedRealtimeFlags, long elapsedRealtimeNanos, double elapsedRealtimeUncertaintyNanos) {
    }

    default void injectBestLocation(int gnssLocationFlags, double latitude, double longitude, double altitude, float speed, float bearing, float horizontalAccuracy, float verticalAccuracy, float speedAccuracy, float bearingAccuracy, long timestamp, int elapsedRealtimeFlags, long elapsedRealtimeNanos, double elapsedRealtimeUncertaintyNanos) {
    }

    default void injectTime(long time, long timeReference, int uncertainty) {
    }

    default boolean isNavigationMessageCollectionSupported() {
        return true;
    }

    default boolean startNavigationMessageCollection() {
        return true;
    }

    default boolean stopNavigationMessageCollection() {
        return true;
    }

    default boolean isAntennaInfoSupported() {
        return true;
    }

    default boolean startAntennaInfoListening() {
        return true;
    }

    default boolean stopAntennaInfoListening() {
        return true;
    }

    default boolean isMeasurementSupported() {
        return true;
    }

    default boolean startMeasurementCollection(boolean enableFullTracking, boolean enableCorrVecOutputs, int intervalMillis) {
        return true;
    }

    default boolean stopMeasurementCollection() {
        return true;
    }

    default boolean isMeasurementCorrectionsSupported() {
        return true;
    }

    default boolean injectMeasurementCorrections(android.location.GnssMeasurementCorrections corrections) {
        return true;
    }

    default boolean startSvStatusCollection() {
        return true;
    }

    default boolean stopSvStatusCollection() {
        return true;
    }

    default boolean startNmeaMessageCollection() {
        return true;
    }

    default boolean stopNmeaMessageCollection() {
        return true;
    }

    default int getBatchSize() {
        return 0;
    }

    default boolean initBatching() {
        return true;
    }

    default void cleanupBatching() {
    }

    default boolean startBatch(long periodNanos, float minUpdateDistanceMeters, boolean wakeOnFifoFull) {
        return true;
    }

    default void flushBatch() {
    }

    default void stopBatch() {
    }

    default boolean isGeofencingSupported() {
        return true;
    }

    default boolean addGeofence(int geofenceId, double latitude, double longitude, double radius, int lastTransition, int monitorTransitions, int notificationResponsiveness, int unknownTimer) {
        return true;
    }

    default boolean resumeGeofence(int geofenceId, int monitorTransitions) {
        return true;
    }

    default boolean pauseGeofence(int geofenceId) {
        return true;
    }

    default boolean removeGeofence(int geofenceId) {
        return true;
    }

    default boolean isGnssVisibilityControlSupported() {
        return true;
    }

    default void sendNiResponse(int notificationId, int userResponse) {
    }

    default void requestPowerStats() {
    }

    default void setAgpsServer(int type, java.lang.String hostname, int port) {
    }

    default void setAgpsSetId(int type, java.lang.String setId) {
    }

    default void setAgpsReferenceLocationCellId(int type, int mcc, int mnc, int lac, long cid, int tac, int pcid, int arfcn) {
    }

    default boolean isPsdsSupported() {
        return true;
    }

    default void injectPsdsData(byte[] data, int length, int psdsType) {
    }
}
