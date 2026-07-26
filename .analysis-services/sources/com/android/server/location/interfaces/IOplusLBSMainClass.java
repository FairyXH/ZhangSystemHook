package com.android.server.location.interfaces;

/* JADX INFO: loaded from: classes2.dex */
public interface IOplusLBSMainClass extends com.android.server.location.common.IOplusCommonFeature {
    public static final com.android.server.location.interfaces.IOplusLBSMainClass DEFAULT = new com.android.server.location.interfaces.IOplusLBSMainClass() { // from class: com.android.server.location.interfaces.IOplusLBSMainClass.1
    };
    public static final int DEFAULT_GEOCODER_TASK_CANCEL_TIME = 30000;
    public static final java.lang.String Name = "IOplusLBSMainClass";

    @Override // com.android.server.location.common.IOplusCommonFeature
    default com.android.server.location.common.OplusLbsFeatureList.OplusIndex index() {
        return com.android.server.location.common.OplusLbsFeatureList.OplusIndex.IOplusLBSMainClass;
    }

    @Override // com.android.server.location.common.IOplusCommonFeature
    default com.android.server.location.common.IOplusCommonFeature getDefault() {
        return DEFAULT;
    }

    default boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException {
        return false;
    }

    default void oplusSystemReady(com.android.server.location.LocationManagerService locMgrService) {
    }

    default void oplusSystemThirdPartyAppsCanStart() {
    }

    default boolean registerLocationListener(android.location.LocationRequest request, android.location.util.identity.CallerIdentity identity, int permissionLevel) {
        return true;
    }

    default boolean registerLocationListener(java.lang.String provider, android.location.util.identity.CallerIdentity identity, int permissionLevel) {
        return true;
    }

    default boolean sendExtraCommand(java.lang.String provider, java.lang.String command, android.os.Bundle extras) {
        return true;
    }

    default void handleLocationChanged(android.location.LocationResult locationResult, boolean debug) {
    }

    default void onGnssLocationProviderInit(android.content.Context context, com.android.server.location.gnss.GnssLocationProvider provider) {
    }

    default void onSetRequest(android.location.provider.ProviderRequest request) {
    }

    default void onStartNavigating(int interval) {
    }

    default void onStopNavigating() {
    }

    default void initOplusNlp() {
    }

    default com.android.server.location.provider.AbstractLocationProvider getLocationProvider() {
        return null;
    }

    default boolean isGeocodeAvailable() {
        return false;
    }

    default int getGeoTaskCancelTimeMs() {
        return 30000;
    }

    default void reverseGeocode(android.location.provider.ReverseGeocodeRequest request, android.location.provider.IGeocodeCallback callback) {
    }

    default void forwardGeocode(android.location.provider.ForwardGeocodeRequest request, android.location.provider.IGeocodeCallback callback) {
    }

    default java.lang.String getNlpId() {
        return null;
    }

    default boolean isUsingRegionNlp() {
        return false;
    }

    default boolean checkRequestBlocked(java.lang.String provider, java.lang.String packagename) {
        return false;
    }

    default boolean isPackageBlocked(java.lang.String packageName, java.lang.String provider) {
        return false;
    }

    default void recordPackagesLocationStatus(java.lang.String packageName, int packageUid, int packagePid, java.lang.String locationProvider) {
    }

    default void removePackagesLocationStatus(java.lang.String packageName, int packageUid, int packagePid, java.lang.String locationProvider) {
    }

    default void receiveSvInfo(android.location.GnssStatus svStatus) {
    }

    default boolean needChangeNotifyStatus(java.lang.String packageName, boolean isBlocked) {
        return false;
    }

    default boolean isAllowedPassLocationAccess(java.lang.String packageName) {
        return false;
    }

    default boolean isAllowedChangeChipData(java.lang.String provider, java.lang.String command) {
        return false;
    }

    default boolean checkInHighFreqLocationBlacklist(java.lang.String pkgName, java.lang.String provider) {
        return false;
    }

    default void refreshRequestTimer() {
    }

    default void storeSatellitesInfo(int svCount, int usedSvcount, int cn0) {
    }

    default void storeAppSvInfo(int maxCn0, float speed) {
    }

    default void incomingNewGpsUsingApp(java.lang.String providerName, java.lang.String apkName) {
    }

    default void removingGpsUsingApp(java.lang.String providerName, java.lang.String apkName) {
    }

    default boolean isForceGnssDisabled() {
        return false;
    }

    default boolean getOplusLocationMode(int userId) {
        return true;
    }

    default boolean isForceAgpsEnabled(boolean agpsEnabledFromSettings) {
        return true;
    }

    default int customizePositionMode(int originalPositionMode) {
        return originalPositionMode;
    }

    default void getAppInfoForTr(java.lang.String methodName, java.lang.String providerName, int pid, java.lang.String packageName) {
    }

    default void setDebug(boolean isDebug) {
    }

    default boolean registerLbsConfigListener(com.android.server.location.interfaces.IOplusConfigListener listener) {
        return false;
    }

    default boolean logoutLbsConfigListener(com.android.server.location.interfaces.IOplusConfigListener listener) {
        return false;
    }

    default void initGnssPowerSaver(com.android.server.location.gnss.GnssLocationProvider provider) {
    }

    default void startController() {
    }

    default void stopController() {
    }

    default void storeWorkSource(android.os.WorkSource source) {
    }

    default boolean getInPowerSaveMode() {
        return false;
    }

    default boolean isEngineOffByStrategy() {
        return false;
    }

    default void forceNotifyEmptyWorksource() {
    }

    default void setUp() {
    }

    default void collectSvStatus(android.location.GnssStatus svStatus) {
    }

    default boolean resistStartGps() {
        return false;
    }

    default int getNavigateMode() {
        return -1;
    }

    @java.lang.Deprecated
    default boolean checkDumpCommand(java.lang.String[] args) {
        return false;
    }

    @java.lang.Deprecated
    default boolean powerSaveEnabled() {
        return false;
    }

    default java.util.List<java.lang.String> getInUsePackagesList() {
        return null;
    }

    default void onGnssMeasurementsProviderInit(com.android.server.location.gnss.GnssMeasurementsProvider provider) {
    }

    default com.android.server.location.gnss.GnssMeasurementsProvider getGnssMeasurementsProvider() {
        return null;
    }

    default int getFlpResId(java.lang.String resName) {
        return -1;
    }

    default void initFlpCoordinator(android.content.Context context) {
    }

    default void setGnssLocationProvider(com.android.server.location.gnss.GnssLocationProvider provider) {
    }

    default void startFlpAiding() {
    }

    default void stopFlpAiding() {
    }

    default void updateGpsWorksourceStatus(android.os.WorkSource worksource) {
    }

    default boolean isPdrActive() {
        return false;
    }

    default boolean shouldReportFlpAsGps(android.location.Location location, java.lang.String pkgName) {
        return false;
    }

    default void setOlsPackageName(android.content.Intent intent) {
    }

    default boolean isFlpReqLimited(java.lang.String pkgName) {
        return false;
    }

    default boolean shouldReportPnetLocationAsGps(android.location.Location location, java.lang.String pkgName) {
        return false;
    }

    default void onStationaryThrottlingLocationProviderInit(java.lang.String name, com.android.server.location.provider.StationaryThrottlingLocationProvider provider) {
    }

    default void onAddMockProvider(java.lang.String packageName, java.lang.String providerName) {
    }

    default void onRemoveMockProvider(java.lang.String packageName, java.lang.String providerName) {
    }

    default int getRec() {
        return -1;
    }

    default boolean ignoreDisabled(java.lang.String name, boolean allowed) {
        return false;
    }

    default void updateSettings(java.lang.String name, int uid) {
    }

    default void getProviderStatus(java.lang.String providerName, boolean isProviderActivated, boolean isProviderActuallyWork, boolean isForceShow, int currentUserId, java.lang.String packageName) {
    }

    default boolean isForegroundActivity(int uidImportance) {
        return false;
    }

    default boolean isForegroundActivity(java.lang.String packageName) {
        return false;
    }

    default void updateBindStatus(boolean hasBind) {
    }

    default boolean checkOpNoThrow(android.app.AppOpsManager appOps, int appOp, android.location.util.identity.CallerIdentity callerIdentity, long identity) {
        return false;
    }

    default void locationStatisticsInit(android.content.Context context) {
    }

    default boolean handleCommand(java.lang.String provider, java.lang.String command, android.os.Bundle extras) {
        return false;
    }

    default void startRequesting(android.location.util.identity.CallerIdentity identity, java.lang.String providerName, android.location.LocationRequest req, boolean isForeground, java.lang.String hash) {
    }

    default void stopRequesting(android.location.util.identity.CallerIdentity identity, java.lang.String providerName, android.location.LocationRequest req, java.lang.String hash) {
    }

    default void deliverLocation(android.location.util.identity.CallerIdentity identity, java.lang.String providerName, java.lang.String hash) {
    }

    default void recordGnssNavigatingStarted(long interval) {
    }

    default void recordGnssNavigatingStopped() {
    }

    default void recordGnssPowerSaveStarted(int strategyCode) {
    }

    default void recordGnssPowerSaveStopped(int strategyCode) {
    }

    default void recordHeldWakelock(java.lang.String name) {
    }

    default void recordReleaseWakelock(java.lang.String name) {
    }

    default void recordNlpNavigatingStarted() {
    }

    default void recordNlpNavigatingStopped() {
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

    default void forceStopStatistics() {
    }

    default boolean isMetalCaseDetectEnabled() {
        return false;
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

    default void initLocationStatusMonitor(android.content.Context context) {
    }

    default void startRecordMonitor() {
    }

    default void stopRecordMonitor() {
    }

    default void recordLocationBlocked(java.lang.String packageName) {
    }

    default void setGpsBackgroundFlag(java.lang.String packageName, boolean flag) {
    }

    default void updateForeground(java.lang.String packageName, java.lang.String providerName, boolean isForeground) {
    }

    default void onFirstFix(int timeToFirstFix) {
    }

    default void onStatusChanged(boolean isNavigating) {
    }

    default void onSvStatusChanged(android.location.GnssStatus status) {
    }

    default void checkLocationHasChanged(java.lang.String provider, java.lang.String packageName, int hashCode) {
    }

    default int generateStatusChangedExtra(java.lang.String provider, java.lang.String packageName, android.os.Bundle extras, int status) {
        return 0;
    }

    default android.location.Location getLastLocation(android.location.Location originLocation, android.location.LastLocationRequest request, int permissionLevel) {
        return null;
    }

    default void triggerLogCollect(int type) {
    }

    default void stopLogCollect(int type) {
    }

    default void collectLbsData(int type, android.os.Bundle extra) {
    }

    default void listenEmergencyCallStatus() {
    }

    default android.location.GnssStatus onGnssSvStrategy(android.location.GnssStatus status) {
        return status;
    }

    default com.android.server.location.gnss.GnssPowerStats reportGnssPowerStatsExt(com.android.server.location.gnss.GnssPowerStats powerStats) {
        return powerStats;
    }

    default void injectMeasurementCorrectionsStatsExt(boolean success) {
    }

    default void dump(java.io.PrintWriter pw, java.lang.String[] args) {
    }

    default boolean dealDumpCommand(java.io.PrintWriter pw, java.lang.String[] args) {
        return false;
    }

    default void dumpOplusContent(java.io.PrintWriter pw) {
    }

    default android.os.HandlerThread getThread(int priority) {
        return com.android.server.FgThread.get();
    }

    default android.os.Handler getHandler(int priority) {
        return com.android.server.FgThread.getHandler();
    }

    default java.util.concurrent.Executor getExecutor(int priority) {
        return com.android.server.FgThread.getExecutor();
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

    default java.util.List<java.lang.String> getNfwProxyApps(java.util.List<java.lang.String> proxyApps) {
        return new java.util.ArrayList();
    }

    default android.location.Location addCoarseLocationExtra(android.location.Location location) {
        return location;
    }

    default android.location.LocationResult addCoarseLocationExtra(android.location.LocationResult locationResult) {
        return locationResult;
    }

    default boolean isGpsEnableForSpecialApp(java.lang.String provider, int userId, java.lang.String calledPackage) {
        return false;
    }

    default boolean isPreciseLocationSupported() {
        return false;
    }

    default android.location.Location reduceAccuracyOfLocation(android.location.Location location) {
        return location;
    }

    default java.lang.String reduceAccuracyOfNmeaSentences(java.lang.String nmea) {
        return nmea;
    }

    default boolean isStealthSecurity() {
        return false;
    }

    default void onActiveDataSubscriptionIdChanged() {
    }

    default void reportQcomConnectStatus(int status) {
    }

    default boolean isSatelliteCommunicationEnable() {
        return false;
    }

    default boolean isVirtualGpsVisibleOnlyForPad(int uid) {
        return true;
    }

    default boolean isStationaryThrottlingEnable() {
        return true;
    }

    default void handleAppLackLocationPermission(int uid, int requiredPermissionLevel) {
    }

    default void deliverLocationForSnapshot(android.location.util.identity.CallerIdentity identity, java.lang.String providerName, java.lang.String hash, android.location.LocationResult locationResult) {
    }

    default void setEngMode(int mode) {
    }

    default void setEngInterval(int interval) {
    }
}
