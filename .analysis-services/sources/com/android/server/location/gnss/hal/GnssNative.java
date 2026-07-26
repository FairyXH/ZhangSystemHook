package com.android.server.location.gnss.hal;

/* JADX INFO: loaded from: classes2.dex */
public class GnssNative {
    public static final int AGPS_REF_LOCATION_TYPE_GSM_CELLID = 1;
    public static final int AGPS_REF_LOCATION_TYPE_LTE_CELLID = 4;
    public static final int AGPS_REF_LOCATION_TYPE_NR_CELLID = 8;
    public static final int AGPS_REF_LOCATION_TYPE_UMTS_CELLID = 2;
    public static final int AGPS_SETID_TYPE_IMSI = 1;
    public static final int AGPS_SETID_TYPE_MSISDN = 2;
    public static final int AGPS_SETID_TYPE_NONE = 0;
    public static final int GNSS_AIDING_TYPE_ALL = 65535;
    public static final int GNSS_AIDING_TYPE_ALMANAC = 2;
    public static final int GNSS_AIDING_TYPE_CELLDB_INFO = 32768;
    public static final int GNSS_AIDING_TYPE_EPHEMERIS = 1;
    public static final int GNSS_AIDING_TYPE_HEALTH = 64;
    public static final int GNSS_AIDING_TYPE_IONO = 16;
    public static final int GNSS_AIDING_TYPE_POSITION = 4;
    public static final int GNSS_AIDING_TYPE_RTI = 1024;
    public static final int GNSS_AIDING_TYPE_SADATA = 512;
    public static final int GNSS_AIDING_TYPE_SVDIR = 128;
    public static final int GNSS_AIDING_TYPE_SVSTEER = 256;
    public static final int GNSS_AIDING_TYPE_TIME = 8;
    public static final int GNSS_AIDING_TYPE_UTC = 32;
    public static final int GNSS_LOCATION_HAS_ALTITUDE = 2;
    public static final int GNSS_LOCATION_HAS_BEARING = 8;
    public static final int GNSS_LOCATION_HAS_BEARING_ACCURACY = 128;
    public static final int GNSS_LOCATION_HAS_HORIZONTAL_ACCURACY = 16;
    public static final int GNSS_LOCATION_HAS_LAT_LONG = 1;
    public static final int GNSS_LOCATION_HAS_SPEED = 4;
    public static final int GNSS_LOCATION_HAS_SPEED_ACCURACY = 64;
    public static final int GNSS_LOCATION_HAS_VERTICAL_ACCURACY = 32;
    public static final int GNSS_POSITION_MODE_MS_ASSISTED = 2;
    public static final int GNSS_POSITION_MODE_MS_BASED = 1;
    public static final int GNSS_POSITION_MODE_STANDALONE = 0;
    public static final int GNSS_POSITION_RECURRENCE_PERIODIC = 0;
    public static final int GNSS_POSITION_RECURRENCE_SINGLE = 1;
    public static final int GNSS_REALTIME_HAS_TIMESTAMP_NS = 1;
    public static final int GNSS_REALTIME_HAS_TIME_UNCERTAINTY_NS = 2;
    private static final float ITAR_SPEED_LIMIT_METERS_PER_SECOND = 400.0f;
    private static final int POWER_STATS_REQUEST_TIMEOUT_MILLIS = 100;
    private static boolean mHasGpsFeature = true;
    private static com.android.server.location.interfaces.IVirtualGnssHal mVirtualGnssHal = null;
    private static com.android.server.location.gnss.hal.GnssNative.GnssHal sGnssHal;
    private static boolean sGnssHalInitialized;
    private static com.android.server.location.gnss.hal.GnssNative sInstance;
    private com.android.server.location.gnss.hal.GnssNative.AGpsCallbacks mAGpsCallbacks;
    private final com.android.server.location.gnss.GnssConfiguration mConfiguration;
    private final com.android.server.location.injector.EmergencyHelper mEmergencyHelper;
    private com.android.server.location.gnss.hal.GnssNative.GeofenceCallbacks mGeofenceCallbacks;
    private final com.android.server.location.gnss.hal.GnssNative.GnssHal mGnssHal;
    private final android.os.Handler mHandler;
    private volatile boolean mItarSpeedLimitExceeded;
    private com.android.server.location.gnss.hal.GnssNative.LocationRequestCallbacks mLocationRequestCallbacks;
    private com.android.server.location.gnss.hal.GnssNative.NotificationCallbacks mNotificationCallbacks;
    private com.android.server.location.gnss.hal.GnssNative.PsdsCallbacks mPsdsCallbacks;
    private boolean mRegistered;
    private com.android.server.location.gnss.hal.GnssNative.TimeCallbacks mTimeCallbacks;
    private int mTopFlags;
    private com.android.server.location.gnss.hal.GnssNative.BaseCallbacks[] mBaseCallbacks = new com.android.server.location.gnss.hal.GnssNative.BaseCallbacks[0];
    private com.android.server.location.gnss.hal.GnssNative.StatusCallbacks[] mStatusCallbacks = new com.android.server.location.gnss.hal.GnssNative.StatusCallbacks[0];
    private com.android.server.location.gnss.hal.GnssNative.SvStatusCallbacks[] mSvStatusCallbacks = new com.android.server.location.gnss.hal.GnssNative.SvStatusCallbacks[0];
    private com.android.server.location.gnss.hal.GnssNative.NmeaCallbacks[] mNmeaCallbacks = new com.android.server.location.gnss.hal.GnssNative.NmeaCallbacks[0];
    private com.android.server.location.gnss.hal.GnssNative.LocationCallbacks[] mLocationCallbacks = new com.android.server.location.gnss.hal.GnssNative.LocationCallbacks[0];
    private com.android.server.location.gnss.hal.GnssNative.MeasurementCallbacks[] mMeasurementCallbacks = new com.android.server.location.gnss.hal.GnssNative.MeasurementCallbacks[0];
    private com.android.server.location.gnss.hal.GnssNative.AntennaInfoCallbacks[] mAntennaInfoCallbacks = new com.android.server.location.gnss.hal.GnssNative.AntennaInfoCallbacks[0];
    private com.android.server.location.gnss.hal.GnssNative.NavigationMessageCallbacks[] mNavigationMessageCallbacks = new com.android.server.location.gnss.hal.GnssNative.NavigationMessageCallbacks[0];
    private com.android.server.location.gnss.GnssPowerStats mLastKnownPowerStats = null;
    private final java.lang.Object mPowerStatsLock = new java.lang.Object();
    private final java.lang.Runnable mPowerStatsTimeoutCallback = new java.lang.Runnable() { // from class: com.android.server.location.gnss.hal.GnssNative$$ExternalSyntheticLambda10
        @Override // java.lang.Runnable
        public final void run() {
            this.f$0.lambda$new$0();
        }
    };
    private final java.util.List<com.android.server.location.gnss.hal.GnssNative.PowerStatsCallback> mPendingPowerStatsCallbacks = new java.util.ArrayList();
    private android.location.GnssCapabilities mCapabilities = new android.location.GnssCapabilities.Builder().build();
    private int mHardwareYear = 0;
    private java.lang.String mHardwareModelName = null;
    private long mStartRealtimeMs = 0;
    private boolean mHasFirstFix = false;
    private com.android.server.location.interfaces.IOplusLBSMainClass mOplusLbsClass = null;

    public interface AGpsCallbacks {
        public static final int AGPS_REQUEST_SETID_IMSI = 1;
        public static final int AGPS_REQUEST_SETID_MSISDN = 2;

        @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
        public @interface AgpsSetIdFlags {
        }

        void onReportAGpsStatus(int i, int i2, byte[] bArr);

        void onRequestSetID(int i);
    }

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface AgpsReferenceLocationType {
    }

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface AgpsSetIdType {
    }

    public interface AntennaInfoCallbacks {
        void onReportAntennaInfo(java.util.List<android.location.GnssAntennaInfo> list);
    }

    public interface GeofenceCallbacks {
        public static final int GEOFENCE_AVAILABILITY_AVAILABLE = 2;
        public static final int GEOFENCE_AVAILABILITY_UNAVAILABLE = 1;
        public static final int GEOFENCE_STATUS_ERROR_GENERIC = -149;
        public static final int GEOFENCE_STATUS_ERROR_ID_EXISTS = -101;
        public static final int GEOFENCE_STATUS_ERROR_ID_UNKNOWN = -102;
        public static final int GEOFENCE_STATUS_ERROR_INVALID_TRANSITION = -103;
        public static final int GEOFENCE_STATUS_ERROR_TOO_MANY_GEOFENCES = 100;
        public static final int GEOFENCE_STATUS_OPERATION_SUCCESS = 0;
        public static final int GEOFENCE_TRANSITION_ENTERED = 1;
        public static final int GEOFENCE_TRANSITION_EXITED = 2;
        public static final int GEOFENCE_TRANSITION_UNCERTAIN = 4;

        @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
        public @interface GeofenceAvailability {
        }

        @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
        public @interface GeofenceStatus {
        }

        @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
        public @interface GeofenceTransition {
        }

        void onReportGeofenceAddStatus(int i, int i2);

        void onReportGeofencePauseStatus(int i, int i2);

        void onReportGeofenceRemoveStatus(int i, int i2);

        void onReportGeofenceResumeStatus(int i, int i2);

        void onReportGeofenceStatus(int i, android.location.Location location);

        void onReportGeofenceTransition(int i, android.location.Location location, int i2, long j);
    }

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface GnssAidingTypeFlags {
    }

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface GnssLocationFlags {
    }

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface GnssPositionMode {
    }

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface GnssPositionRecurrence {
    }

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface GnssRealtimeFlags {
    }

    public interface LocationCallbacks {
        void onReportLocation(boolean z, android.location.Location location);

        void onReportLocations(android.location.Location[] locationArr);
    }

    public interface LocationRequestCallbacks {
        void onRequestLocation(boolean z, boolean z2);

        void onRequestRefLocation();
    }

    public interface MeasurementCallbacks {
        void onReportMeasurements(android.location.GnssMeasurementsEvent gnssMeasurementsEvent);
    }

    @java.lang.annotation.Target({java.lang.annotation.ElementType.METHOD})
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    private @interface NativeEntryPoint {
    }

    public interface NavigationMessageCallbacks {
        void onReportNavigationMessage(android.location.GnssNavigationMessage gnssNavigationMessage);
    }

    public interface NmeaCallbacks {
        void onReportNmea(long j);
    }

    public interface NotificationCallbacks {
        void onReportNfwNotification(java.lang.String str, byte b, java.lang.String str2, byte b2, java.lang.String str3, byte b3, boolean z, boolean z2);
    }

    public interface PowerStatsCallback {
        void onReportPowerStats(com.android.server.location.gnss.GnssPowerStats gnssPowerStats);
    }

    public interface PsdsCallbacks {
        void onRequestPsdsDownload(int i);
    }

    public interface StatusCallbacks {
        public static final int GNSS_STATUS_ENGINE_OFF = 4;
        public static final int GNSS_STATUS_ENGINE_ON = 3;
        public static final int GNSS_STATUS_NONE = 0;
        public static final int GNSS_STATUS_SESSION_BEGIN = 1;
        public static final int GNSS_STATUS_SESSION_END = 2;

        @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
        public @interface GnssStatusValue {
        }

        void onReportFirstFix(int i);

        void onReportStatus(int i);
    }

    public interface SvStatusCallbacks {
        void onReportSvStatus(android.location.GnssStatus gnssStatus);
    }

    public interface TimeCallbacks {
        void onRequestUtcTime();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean native_add_geofence(int i, double d, double d2, double d3, int i2, int i3, int i4, int i5);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void native_agps_set_id(int i, java.lang.String str);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void native_agps_set_ref_location_cellid(int i, int i2, int i3, int i4, long j, int i5, int i6, int i7);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void native_class_init_once();

    /* JADX INFO: Access modifiers changed from: private */
    public static native void native_cleanup();

    /* JADX INFO: Access modifiers changed from: private */
    public static native void native_cleanup_batching();

    /* JADX INFO: Access modifiers changed from: private */
    public static native void native_delete_aiding_data(int i);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void native_flush_batch();

    /* JADX INFO: Access modifiers changed from: private */
    public static native int native_get_batch_size();

    /* JADX INFO: Access modifiers changed from: private */
    public static native java.lang.String native_get_internal_state();

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean native_init();

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean native_init_batching();

    /* JADX INFO: Access modifiers changed from: private */
    public native void native_init_once(boolean z);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void native_inject_best_location(int i, double d, double d2, double d3, float f, float f2, float f3, float f4, float f5, float f6, long j, int i2, long j2, double d4);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void native_inject_location(int i, double d, double d2, double d3, float f, float f2, float f3, float f4, float f5, float f6, long j, int i2, long j2, double d4);

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean native_inject_measurement_corrections(android.location.GnssMeasurementCorrections gnssMeasurementCorrections);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void native_inject_ni_supl_message_data(byte[] bArr, int i, int i2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void native_inject_psds_data(byte[] bArr, int i, int i2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void native_inject_time(long j, long j2, int i);

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean native_is_antenna_info_supported();

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean native_is_geofence_supported();

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean native_is_gnss_visibility_control_supported();

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean native_is_measurement_corrections_supported();

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean native_is_measurement_supported();

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean native_is_navigation_message_supported();

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean native_is_supported();

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean native_pause_geofence(int i);

    /* JADX INFO: Access modifiers changed from: private */
    public static native int native_read_nmea(byte[] bArr, int i);

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean native_remove_geofence(int i);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void native_request_power_stats();

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean native_resume_geofence(int i, int i2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void native_set_agps_server(int i, java.lang.String str, int i2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean native_set_position_mode(int i, int i2, int i3, int i4, int i5, boolean z);

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean native_start();

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean native_start_antenna_info_listening();

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean native_start_batch(long j, float f, boolean z);

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean native_start_measurement_collection(boolean z, boolean z2, int i);

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean native_start_navigation_message_collection();

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean native_start_nmea_message_collection();

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean native_start_sv_status_collection();

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean native_stop();

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean native_stop_antenna_info_listening();

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean native_stop_batch();

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean native_stop_measurement_collection();

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean native_stop_navigation_message_collection();

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean native_stop_nmea_message_collection();

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean native_stop_sv_status_collection();

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean native_supports_psds();

    public interface BaseCallbacks {
        void onHalRestarted();

        default void onHalStarted() {
        }

        default void onCapabilitiesChanged(android.location.GnssCapabilities oldCapabilities, android.location.GnssCapabilities newCapabilities) {
        }
    }

    public static synchronized void setGnssHalForTest(com.android.server.location.gnss.hal.GnssNative.GnssHal gnssHal) {
        sGnssHal = (com.android.server.location.gnss.hal.GnssNative.GnssHal) java.util.Objects.requireNonNull(gnssHal);
        sGnssHalInitialized = false;
        sInstance = null;
    }

    private static synchronized void initializeHal() {
        if (!sGnssHalInitialized) {
            if (sGnssHal == null) {
                if (mHasGpsFeature) {
                    sGnssHal = new com.android.server.location.gnss.hal.GnssNative.GnssHal();
                    android.util.Log.d(com.android.server.location.gnss.GnssManagerService.TAG, "initializeHal GnssHal");
                } else {
                    sGnssHal = mVirtualGnssHal.getVirtualGnssHal(mHasGpsFeature);
                    android.util.Log.d(com.android.server.location.gnss.GnssManagerService.TAG, "initializeHal VirtualGnssHal");
                }
            }
            sGnssHal.classInitOnce();
            sGnssHalInitialized = true;
        }
    }

    public static synchronized boolean isSupported() {
        initializeHal();
        return sGnssHal.isSupported();
    }

    public static synchronized com.android.server.location.gnss.hal.GnssNative create(com.android.server.location.injector.Injector injector, com.android.server.location.gnss.GnssConfiguration configuration) {
        com.android.server.location.gnss.hal.GnssNative gnssNative;
        try {
            com.android.internal.util.Preconditions.checkState(isSupported());
        } catch (java.lang.IllegalStateException e) {
            android.util.Log.d(com.android.server.location.gnss.GnssManagerService.TAG, "ignore isSupported");
        }
        android.util.Log.d(com.android.server.location.gnss.GnssManagerService.TAG, "create GnssNative1");
        com.android.internal.util.Preconditions.checkState(sInstance == null);
        gnssNative = new com.android.server.location.gnss.hal.GnssNative(sGnssHal, injector, configuration);
        sInstance = gnssNative;
        return gnssNative;
    }

    public static synchronized com.android.server.location.gnss.hal.GnssNative create(com.android.server.location.injector.Injector injector, com.android.server.location.gnss.GnssConfiguration configuration, boolean hasGpsFeature, com.android.server.location.interfaces.IVirtualGnssHal virtualHal) {
        com.android.server.location.gnss.hal.GnssNative gnssNative;
        mHasGpsFeature = hasGpsFeature;
        mVirtualGnssHal = virtualHal;
        try {
            com.android.internal.util.Preconditions.checkState(isSupported());
        } catch (java.lang.IllegalStateException e) {
            android.util.Log.d(com.android.server.location.gnss.GnssManagerService.TAG, "ignore isSupported");
        }
        android.util.Log.d(com.android.server.location.gnss.GnssManagerService.TAG, "create GnssNative");
        com.android.internal.util.Preconditions.checkState(sInstance == null);
        gnssNative = new com.android.server.location.gnss.hal.GnssNative(sGnssHal, injector, configuration);
        sInstance = gnssNative;
        return gnssNative;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        android.util.Log.d(com.android.server.location.gnss.GnssManagerService.TAG, "Request for power stats timed out");
        reportGnssPowerStats(null);
    }

    private GnssNative(com.android.server.location.gnss.hal.GnssNative.GnssHal gnssHal, com.android.server.location.injector.Injector injector, com.android.server.location.gnss.GnssConfiguration configuration) {
        if (mHasGpsFeature) {
            this.mGnssHal = (com.android.server.location.gnss.hal.GnssNative.GnssHal) java.util.Objects.requireNonNull(gnssHal);
            android.util.Log.d(com.android.server.location.gnss.GnssManagerService.TAG, "init GnssHal");
        } else {
            this.mGnssHal = (com.android.server.location.gnss.hal.GnssNative.GnssHal) java.util.Objects.requireNonNull(mVirtualGnssHal.getVirtualGnssHal(mHasGpsFeature));
            android.util.Log.d(com.android.server.location.gnss.GnssManagerService.TAG, "init VirtualGnssHal");
        }
        this.mEmergencyHelper = injector.getEmergencyHelper();
        this.mConfiguration = configuration;
        this.mHandler = com.android.server.FgThread.getHandler();
    }

    public void addBaseCallbacks(com.android.server.location.gnss.hal.GnssNative.BaseCallbacks callbacks) {
        com.android.internal.util.Preconditions.checkState(!this.mRegistered);
        this.mBaseCallbacks = (com.android.server.location.gnss.hal.GnssNative.BaseCallbacks[]) com.android.internal.util.ArrayUtils.appendElement(com.android.server.location.gnss.hal.GnssNative.BaseCallbacks.class, this.mBaseCallbacks, callbacks);
    }

    public void addStatusCallbacks(com.android.server.location.gnss.hal.GnssNative.StatusCallbacks callbacks) {
        com.android.internal.util.Preconditions.checkState(!this.mRegistered);
        this.mStatusCallbacks = (com.android.server.location.gnss.hal.GnssNative.StatusCallbacks[]) com.android.internal.util.ArrayUtils.appendElement(com.android.server.location.gnss.hal.GnssNative.StatusCallbacks.class, this.mStatusCallbacks, callbacks);
    }

    public void addSvStatusCallbacks(com.android.server.location.gnss.hal.GnssNative.SvStatusCallbacks callbacks) {
        com.android.internal.util.Preconditions.checkState(!this.mRegistered);
        this.mSvStatusCallbacks = (com.android.server.location.gnss.hal.GnssNative.SvStatusCallbacks[]) com.android.internal.util.ArrayUtils.appendElement(com.android.server.location.gnss.hal.GnssNative.SvStatusCallbacks.class, this.mSvStatusCallbacks, callbacks);
    }

    public void addNmeaCallbacks(com.android.server.location.gnss.hal.GnssNative.NmeaCallbacks callbacks) {
        com.android.internal.util.Preconditions.checkState(!this.mRegistered);
        this.mNmeaCallbacks = (com.android.server.location.gnss.hal.GnssNative.NmeaCallbacks[]) com.android.internal.util.ArrayUtils.appendElement(com.android.server.location.gnss.hal.GnssNative.NmeaCallbacks.class, this.mNmeaCallbacks, callbacks);
    }

    public void addLocationCallbacks(com.android.server.location.gnss.hal.GnssNative.LocationCallbacks callbacks) {
        com.android.internal.util.Preconditions.checkState(!this.mRegistered);
        this.mLocationCallbacks = (com.android.server.location.gnss.hal.GnssNative.LocationCallbacks[]) com.android.internal.util.ArrayUtils.appendElement(com.android.server.location.gnss.hal.GnssNative.LocationCallbacks.class, this.mLocationCallbacks, callbacks);
    }

    public void addMeasurementCallbacks(com.android.server.location.gnss.hal.GnssNative.MeasurementCallbacks callbacks) {
        com.android.internal.util.Preconditions.checkState(!this.mRegistered);
        this.mMeasurementCallbacks = (com.android.server.location.gnss.hal.GnssNative.MeasurementCallbacks[]) com.android.internal.util.ArrayUtils.appendElement(com.android.server.location.gnss.hal.GnssNative.MeasurementCallbacks.class, this.mMeasurementCallbacks, callbacks);
    }

    public void addAntennaInfoCallbacks(com.android.server.location.gnss.hal.GnssNative.AntennaInfoCallbacks callbacks) {
        com.android.internal.util.Preconditions.checkState(!this.mRegistered);
        this.mAntennaInfoCallbacks = (com.android.server.location.gnss.hal.GnssNative.AntennaInfoCallbacks[]) com.android.internal.util.ArrayUtils.appendElement(com.android.server.location.gnss.hal.GnssNative.AntennaInfoCallbacks.class, this.mAntennaInfoCallbacks, callbacks);
    }

    public void addNavigationMessageCallbacks(com.android.server.location.gnss.hal.GnssNative.NavigationMessageCallbacks callbacks) {
        com.android.internal.util.Preconditions.checkState(!this.mRegistered);
        this.mNavigationMessageCallbacks = (com.android.server.location.gnss.hal.GnssNative.NavigationMessageCallbacks[]) com.android.internal.util.ArrayUtils.appendElement(com.android.server.location.gnss.hal.GnssNative.NavigationMessageCallbacks.class, this.mNavigationMessageCallbacks, callbacks);
    }

    public void setGeofenceCallbacks(com.android.server.location.gnss.hal.GnssNative.GeofenceCallbacks callbacks) {
        com.android.internal.util.Preconditions.checkState(!this.mRegistered);
        com.android.internal.util.Preconditions.checkState(this.mGeofenceCallbacks == null);
        this.mGeofenceCallbacks = (com.android.server.location.gnss.hal.GnssNative.GeofenceCallbacks) java.util.Objects.requireNonNull(callbacks);
    }

    public void setTimeCallbacks(com.android.server.location.gnss.hal.GnssNative.TimeCallbacks callbacks) {
        com.android.internal.util.Preconditions.checkState(!this.mRegistered);
        com.android.internal.util.Preconditions.checkState(this.mTimeCallbacks == null);
        this.mTimeCallbacks = (com.android.server.location.gnss.hal.GnssNative.TimeCallbacks) java.util.Objects.requireNonNull(callbacks);
    }

    public void setLocationRequestCallbacks(com.android.server.location.gnss.hal.GnssNative.LocationRequestCallbacks callbacks) {
        com.android.internal.util.Preconditions.checkState(!this.mRegistered);
        com.android.internal.util.Preconditions.checkState(this.mLocationRequestCallbacks == null);
        this.mLocationRequestCallbacks = (com.android.server.location.gnss.hal.GnssNative.LocationRequestCallbacks) java.util.Objects.requireNonNull(callbacks);
    }

    public void setPsdsCallbacks(com.android.server.location.gnss.hal.GnssNative.PsdsCallbacks callbacks) {
        com.android.internal.util.Preconditions.checkState(!this.mRegistered);
        com.android.internal.util.Preconditions.checkState(this.mPsdsCallbacks == null);
        this.mPsdsCallbacks = (com.android.server.location.gnss.hal.GnssNative.PsdsCallbacks) java.util.Objects.requireNonNull(callbacks);
    }

    public void setAGpsCallbacks(com.android.server.location.gnss.hal.GnssNative.AGpsCallbacks callbacks) {
        com.android.internal.util.Preconditions.checkState(!this.mRegistered);
        com.android.internal.util.Preconditions.checkState(this.mAGpsCallbacks == null);
        this.mAGpsCallbacks = (com.android.server.location.gnss.hal.GnssNative.AGpsCallbacks) java.util.Objects.requireNonNull(callbacks);
    }

    public void setNotificationCallbacks(com.android.server.location.gnss.hal.GnssNative.NotificationCallbacks callbacks) {
        com.android.internal.util.Preconditions.checkState(!this.mRegistered);
        com.android.internal.util.Preconditions.checkState(this.mNotificationCallbacks == null);
        this.mNotificationCallbacks = (com.android.server.location.gnss.hal.GnssNative.NotificationCallbacks) java.util.Objects.requireNonNull(callbacks);
    }

    public void register() {
        com.android.internal.util.Preconditions.checkState(!this.mRegistered);
        this.mRegistered = true;
        initializeGnss(false);
        android.util.Log.i(com.android.server.location.gnss.GnssManagerService.TAG, "gnss hal started");
        for (int i = 0; i < this.mBaseCallbacks.length; i++) {
            this.mBaseCallbacks[i].onHalStarted();
        }
    }

    private void initializeGnss(boolean restart) {
        com.android.internal.util.Preconditions.checkState(this.mRegistered);
        this.mTopFlags = 0;
        this.mGnssHal.initOnce(this, restart);
        if (this.mGnssHal.init()) {
            this.mGnssHal.cleanup();
            android.util.Log.i(com.android.server.location.gnss.GnssManagerService.TAG, "gnss hal initialized");
        } else {
            android.util.Log.e(com.android.server.location.gnss.GnssManagerService.TAG, "gnss hal initialization failed");
        }
    }

    public com.android.server.location.gnss.GnssConfiguration getConfiguration() {
        return this.mConfiguration;
    }

    public boolean init() {
        com.android.internal.util.Preconditions.checkState(this.mRegistered);
        return this.mGnssHal.init();
    }

    public void cleanup() {
        com.android.internal.util.Preconditions.checkState(this.mRegistered);
        this.mGnssHal.cleanup();
    }

    public com.android.server.location.gnss.GnssPowerStats getLastKnownPowerStats() {
        return this.mLastKnownPowerStats;
    }

    public android.location.GnssCapabilities getCapabilities() {
        return this.mCapabilities;
    }

    public int getHardwareYear() {
        return this.mHardwareYear;
    }

    public java.lang.String getHardwareModelName() {
        return this.mHardwareModelName;
    }

    public boolean isItarSpeedLimitExceeded() {
        return this.mItarSpeedLimitExceeded;
    }

    public boolean start() {
        com.android.internal.util.Preconditions.checkState(this.mRegistered);
        this.mStartRealtimeMs = android.os.SystemClock.elapsedRealtime();
        this.mHasFirstFix = false;
        return this.mGnssHal.start();
    }

    public boolean stop() {
        com.android.internal.util.Preconditions.checkState(this.mRegistered);
        return this.mGnssHal.stop();
    }

    public boolean setPositionMode(int mode, int recurrence, int minInterval, int preferredAccuracy, int preferredTime, boolean lowPowerMode) {
        com.android.internal.util.Preconditions.checkState(this.mRegistered);
        return this.mGnssHal.setPositionMode(mode, recurrence, minInterval, preferredAccuracy, preferredTime, lowPowerMode);
    }

    public java.lang.String getInternalState() {
        com.android.internal.util.Preconditions.checkState(this.mRegistered);
        return this.mGnssHal.getInternalState();
    }

    public void deleteAidingData(int flags) {
        com.android.internal.util.Preconditions.checkState(this.mRegistered);
        this.mGnssHal.deleteAidingData(flags);
    }

    public int readNmea(byte[] buffer, int bufferSize) {
        com.android.internal.util.Preconditions.checkState(this.mRegistered);
        return this.mGnssHal.readNmea(buffer, bufferSize);
    }

    public void injectLocation(android.location.Location location) {
        com.android.internal.util.Preconditions.checkState(this.mRegistered);
        if (location.hasAccuracy()) {
            int gnssLocationFlags = (location.hasAltitude() ? 2 : 0) | 1 | (location.hasSpeed() ? 4 : 0) | (location.hasBearing() ? 8 : 0) | (location.hasAccuracy() ? 16 : 0) | (location.hasVerticalAccuracy() ? 32 : 0) | (location.hasSpeedAccuracy() ? 64 : 0) | (location.hasBearingAccuracy() ? 128 : 0);
            double latitudeDegrees = location.getLatitude();
            double longitudeDegrees = location.getLongitude();
            double altitudeMeters = location.getAltitude();
            float speedMetersPerSec = location.getSpeed();
            float bearingDegrees = location.getBearing();
            float horizontalAccuracyMeters = location.getAccuracy();
            float verticalAccuracyMeters = location.getVerticalAccuracyMeters();
            float speedAccuracyMetersPerSecond = location.getSpeedAccuracyMetersPerSecond();
            float bearingAccuracyDegrees = location.getBearingAccuracyDegrees();
            long timestamp = location.getTime();
            int elapsedRealtimeFlags = (location.hasElapsedRealtimeUncertaintyNanos() ? 2 : 0) | 1;
            long elapsedRealtimeNanos = location.getElapsedRealtimeNanos();
            double elapsedRealtimeUncertaintyNanos = location.getElapsedRealtimeUncertaintyNanos();
            this.mGnssHal.injectLocation(gnssLocationFlags, latitudeDegrees, longitudeDegrees, altitudeMeters, speedMetersPerSec, bearingDegrees, horizontalAccuracyMeters, verticalAccuracyMeters, speedAccuracyMetersPerSecond, bearingAccuracyDegrees, timestamp, elapsedRealtimeFlags, elapsedRealtimeNanos, elapsedRealtimeUncertaintyNanos);
        }
    }

    public void injectBestLocation(android.location.Location location) {
        com.android.internal.util.Preconditions.checkState(this.mRegistered);
        int gnssLocationFlags = (location.hasAltitude() ? 2 : 0) | 1 | (location.hasSpeed() ? 4 : 0) | (location.hasBearing() ? 8 : 0) | (location.hasAccuracy() ? 16 : 0) | (location.hasVerticalAccuracy() ? 32 : 0) | (location.hasSpeedAccuracy() ? 64 : 0) | (location.hasBearingAccuracy() ? 128 : 0);
        double latitudeDegrees = location.getLatitude();
        double longitudeDegrees = location.getLongitude();
        double altitudeMeters = location.getAltitude();
        float speedMetersPerSec = location.getSpeed();
        float bearingDegrees = location.getBearing();
        float horizontalAccuracyMeters = location.getAccuracy();
        float verticalAccuracyMeters = location.getVerticalAccuracyMeters();
        float speedAccuracyMetersPerSecond = location.getSpeedAccuracyMetersPerSecond();
        float bearingAccuracyDegrees = location.getBearingAccuracyDegrees();
        long timestamp = location.getTime();
        int elapsedRealtimeFlags = (location.hasElapsedRealtimeUncertaintyNanos() ? 2 : 0) | 1;
        long elapsedRealtimeNanos = location.getElapsedRealtimeNanos();
        double elapsedRealtimeUncertaintyNanos = location.getElapsedRealtimeUncertaintyNanos();
        this.mGnssHal.injectBestLocation(gnssLocationFlags, latitudeDegrees, longitudeDegrees, altitudeMeters, speedMetersPerSec, bearingDegrees, horizontalAccuracyMeters, verticalAccuracyMeters, speedAccuracyMetersPerSecond, bearingAccuracyDegrees, timestamp, elapsedRealtimeFlags, elapsedRealtimeNanos, elapsedRealtimeUncertaintyNanos);
    }

    public void injectTime(long time, long timeReference, int uncertainty) {
        com.android.internal.util.Preconditions.checkState(this.mRegistered);
        this.mGnssHal.injectTime(time, timeReference, uncertainty);
    }

    public boolean isNavigationMessageCollectionSupported() {
        com.android.internal.util.Preconditions.checkState(this.mRegistered);
        return this.mGnssHal.isNavigationMessageCollectionSupported();
    }

    public boolean startNavigationMessageCollection() {
        com.android.internal.util.Preconditions.checkState(this.mRegistered);
        return this.mGnssHal.startNavigationMessageCollection();
    }

    public boolean stopNavigationMessageCollection() {
        com.android.internal.util.Preconditions.checkState(this.mRegistered);
        return this.mGnssHal.stopNavigationMessageCollection();
    }

    public boolean isAntennaInfoSupported() {
        com.android.internal.util.Preconditions.checkState(this.mRegistered);
        return this.mGnssHal.isAntennaInfoSupported();
    }

    public boolean startAntennaInfoListening() {
        com.android.internal.util.Preconditions.checkState(this.mRegistered);
        return this.mGnssHal.startAntennaInfoListening();
    }

    public boolean stopAntennaInfoListening() {
        com.android.internal.util.Preconditions.checkState(this.mRegistered);
        return this.mGnssHal.stopAntennaInfoListening();
    }

    public boolean isMeasurementSupported() {
        com.android.internal.util.Preconditions.checkState(this.mRegistered);
        return this.mGnssHal.isMeasurementSupported();
    }

    public boolean startMeasurementCollection(boolean enableFullTracking, boolean enableCorrVecOutputs, int intervalMillis) {
        com.android.internal.util.Preconditions.checkState(this.mRegistered);
        return this.mGnssHal.startMeasurementCollection(enableFullTracking, enableCorrVecOutputs, intervalMillis);
    }

    public boolean stopMeasurementCollection() {
        com.android.internal.util.Preconditions.checkState(this.mRegistered);
        return this.mGnssHal.stopMeasurementCollection();
    }

    public boolean startSvStatusCollection() {
        com.android.internal.util.Preconditions.checkState(this.mRegistered);
        return this.mGnssHal.startSvStatusCollection();
    }

    public boolean stopSvStatusCollection() {
        com.android.internal.util.Preconditions.checkState(this.mRegistered);
        return this.mGnssHal.stopSvStatusCollection();
    }

    public boolean startNmeaMessageCollection() {
        com.android.internal.util.Preconditions.checkState(this.mRegistered);
        return this.mGnssHal.startNmeaMessageCollection();
    }

    public boolean stopNmeaMessageCollection() {
        com.android.internal.util.Preconditions.checkState(this.mRegistered);
        return this.mGnssHal.stopNmeaMessageCollection();
    }

    public boolean isMeasurementCorrectionsSupported() {
        com.android.internal.util.Preconditions.checkState(this.mRegistered);
        return this.mGnssHal.isMeasurementCorrectionsSupported();
    }

    public boolean injectMeasurementCorrections(android.location.GnssMeasurementCorrections corrections) {
        com.android.internal.util.Preconditions.checkState(this.mRegistered);
        boolean ret = this.mGnssHal.injectMeasurementCorrections(corrections);
        if (this.mOplusLbsClass == null) {
            this.mOplusLbsClass = (com.android.server.location.interfaces.IOplusLBSMainClass) com.android.server.location.common.OplusLbsFactory.getInstance().getFeature(com.android.server.location.interfaces.IOplusLBSMainClass.DEFAULT, null);
        }
        if (this.mOplusLbsClass != null) {
            this.mOplusLbsClass.injectMeasurementCorrectionsStatsExt(ret);
        }
        return ret;
    }

    public boolean initBatching() {
        com.android.internal.util.Preconditions.checkState(this.mRegistered);
        return this.mGnssHal.initBatching();
    }

    public void cleanupBatching() {
        com.android.internal.util.Preconditions.checkState(this.mRegistered);
        this.mGnssHal.cleanupBatching();
    }

    public boolean startBatch(long periodNanos, float minUpdateDistanceMeters, boolean wakeOnFifoFull) {
        com.android.internal.util.Preconditions.checkState(this.mRegistered);
        return this.mGnssHal.startBatch(periodNanos, minUpdateDistanceMeters, wakeOnFifoFull);
    }

    public void flushBatch() {
        com.android.internal.util.Preconditions.checkState(this.mRegistered);
        this.mGnssHal.flushBatch();
    }

    public void stopBatch() {
        com.android.internal.util.Preconditions.checkState(this.mRegistered);
        this.mGnssHal.stopBatch();
    }

    public int getBatchSize() {
        com.android.internal.util.Preconditions.checkState(this.mRegistered);
        return this.mGnssHal.getBatchSize();
    }

    public boolean isGeofencingSupported() {
        com.android.internal.util.Preconditions.checkState(this.mRegistered);
        return this.mGnssHal.isGeofencingSupported();
    }

    public boolean addGeofence(int geofenceId, double latitude, double longitude, double radius, int lastTransition, int monitorTransitions, int notificationResponsiveness, int unknownTimer) {
        com.android.internal.util.Preconditions.checkState(this.mRegistered);
        return this.mGnssHal.addGeofence(geofenceId, latitude, longitude, radius, lastTransition, monitorTransitions, notificationResponsiveness, unknownTimer);
    }

    public boolean resumeGeofence(int geofenceId, int monitorTransitions) {
        com.android.internal.util.Preconditions.checkState(this.mRegistered);
        return this.mGnssHal.resumeGeofence(geofenceId, monitorTransitions);
    }

    public boolean pauseGeofence(int geofenceId) {
        com.android.internal.util.Preconditions.checkState(this.mRegistered);
        return this.mGnssHal.pauseGeofence(geofenceId);
    }

    public boolean removeGeofence(int geofenceId) {
        com.android.internal.util.Preconditions.checkState(this.mRegistered);
        return this.mGnssHal.removeGeofence(geofenceId);
    }

    public boolean isGnssVisibilityControlSupported() {
        com.android.internal.util.Preconditions.checkState(this.mRegistered);
        return this.mGnssHal.isGnssVisibilityControlSupported();
    }

    public void requestPowerStats(final java.util.concurrent.Executor executor, final com.android.server.location.gnss.hal.GnssNative.PowerStatsCallback callback) {
        com.android.internal.util.Preconditions.checkState(this.mRegistered);
        synchronized (this.mPowerStatsLock) {
            this.mPendingPowerStatsCallbacks.add(new com.android.server.location.gnss.hal.GnssNative.PowerStatsCallback() { // from class: com.android.server.location.gnss.hal.GnssNative$$ExternalSyntheticLambda17
                @Override // com.android.server.location.gnss.hal.GnssNative.PowerStatsCallback
                public final void onReportPowerStats(com.android.server.location.gnss.GnssPowerStats gnssPowerStats) {
                    android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.location.gnss.hal.GnssNative$$ExternalSyntheticLambda18
                        public final void runOrThrow() {
                            executor.execute(new java.lang.Runnable() { // from class: com.android.server.location.gnss.hal.GnssNative$$ExternalSyntheticLambda5
                                @Override // java.lang.Runnable
                                public final void run() {
                                    powerStatsCallback.onReportPowerStats(gnssPowerStats);
                                }
                            });
                        }
                    });
                }
            });
            if (this.mPendingPowerStatsCallbacks.size() == 1) {
                this.mGnssHal.requestPowerStats();
                this.mHandler.postDelayed(this.mPowerStatsTimeoutCallback, 100L);
            }
        }
    }

    public com.android.server.location.gnss.GnssPowerStats requestPowerStatsBlocking() {
        final java.util.concurrent.atomic.AtomicReference<com.android.server.location.gnss.GnssPowerStats> statsWrapper = new java.util.concurrent.atomic.AtomicReference<>();
        final java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(1);
        requestPowerStats(new com.android.server.SystemServerInitThreadPool$$ExternalSyntheticLambda0(), new com.android.server.location.gnss.hal.GnssNative.PowerStatsCallback() { // from class: com.android.server.location.gnss.hal.GnssNative$$ExternalSyntheticLambda15
            @Override // com.android.server.location.gnss.hal.GnssNative.PowerStatsCallback
            public final void onReportPowerStats(com.android.server.location.gnss.GnssPowerStats gnssPowerStats) {
                com.android.server.location.gnss.hal.GnssNative.lambda$requestPowerStatsBlocking$4(statsWrapper, latch, gnssPowerStats);
            }
        });
        try {
            latch.await(100L, java.util.concurrent.TimeUnit.MILLISECONDS);
        } catch (java.lang.InterruptedException e) {
            android.util.Log.d(com.android.server.location.gnss.GnssManagerService.TAG, "Interrupted while waiting for power stats");
            java.lang.Thread.currentThread().interrupt();
        }
        return statsWrapper.get();
    }

    static /* synthetic */ void lambda$requestPowerStatsBlocking$4(java.util.concurrent.atomic.AtomicReference statsWrapper, java.util.concurrent.CountDownLatch latch, com.android.server.location.gnss.GnssPowerStats powerStats) {
        statsWrapper.set(powerStats);
        latch.countDown();
    }

    public void setAgpsServer(int type, java.lang.String hostname, int port) {
        com.android.internal.util.Preconditions.checkState(this.mRegistered);
        this.mGnssHal.setAgpsServer(type, hostname, port);
    }

    public void setAgpsSetId(int type, java.lang.String setId) {
        com.android.internal.util.Preconditions.checkState(this.mRegistered);
        this.mGnssHal.setAgpsSetId(type, setId);
    }

    public void setAgpsReferenceLocationCellId(int type, int mcc, int mnc, int lac, long cid, int tac, int pcid, int arfcn) {
        com.android.internal.util.Preconditions.checkState(this.mRegistered);
        this.mGnssHal.setAgpsReferenceLocationCellId(type, mcc, mnc, lac, cid, tac, pcid, arfcn);
    }

    public boolean isPsdsSupported() {
        com.android.internal.util.Preconditions.checkState(this.mRegistered);
        return this.mGnssHal.isPsdsSupported();
    }

    public void injectPsdsData(byte[] data, int length, int psdsType) {
        com.android.internal.util.Preconditions.checkState(this.mRegistered);
        this.mGnssHal.injectPsdsData(data, length, psdsType);
    }

    public void injectNiSuplMessageData(byte[] data, int length, int slotIndex) {
        com.android.internal.util.Preconditions.checkState(this.mRegistered);
        this.mGnssHal.injectNiSuplMessageData(data, length, slotIndex);
    }

    void reportGnssServiceDied() {
        android.util.Log.e(com.android.server.location.gnss.GnssManagerService.TAG, "gnss hal died - restarting shortly...");
        com.android.server.FgThread.getExecutor().execute(new java.lang.Runnable() { // from class: com.android.server.location.gnss.hal.GnssNative$$ExternalSyntheticLambda19
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.restartHal();
            }
        });
    }

    void restartHal() {
        initializeGnss(true);
        android.util.Log.e(com.android.server.location.gnss.GnssManagerService.TAG, "gnss hal restarted");
        for (int i = 0; i < this.mBaseCallbacks.length; i++) {
            this.mBaseCallbacks[i].onHalRestarted();
        }
    }

    void reportLocation(final boolean hasLatLong, final android.location.Location location) {
        android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.location.gnss.hal.GnssNative$$ExternalSyntheticLambda26
            public final void runOrThrow() throws java.lang.Exception {
                this.f$0.lambda$reportLocation$5(hasLatLong, location);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$reportLocation$5(boolean hasLatLong, android.location.Location location) throws java.lang.Exception {
        if (hasLatLong && !this.mHasFirstFix) {
            this.mHasFirstFix = true;
            int ttff = (int) (android.os.SystemClock.elapsedRealtime() - this.mStartRealtimeMs);
            for (int i = 0; i < this.mStatusCallbacks.length; i++) {
                this.mStatusCallbacks[i].onReportFirstFix(ttff);
            }
        }
        if (location.hasSpeed()) {
            boolean exceeded = location.getSpeed() > ITAR_SPEED_LIMIT_METERS_PER_SECOND;
            if (!this.mItarSpeedLimitExceeded && exceeded) {
                android.util.Log.w(com.android.server.location.gnss.GnssManagerService.TAG, "speed nearing ITAR threshold - blocking further GNSS output");
            } else if (this.mItarSpeedLimitExceeded && !exceeded) {
                android.util.Log.w(com.android.server.location.gnss.GnssManagerService.TAG, "speed leaving ITAR threshold - allowing further GNSS output");
            }
            this.mItarSpeedLimitExceeded = exceeded;
        }
        if (this.mItarSpeedLimitExceeded) {
            return;
        }
        for (int i2 = 0; i2 < this.mLocationCallbacks.length; i2++) {
            this.mLocationCallbacks[i2].onReportLocation(hasLatLong, location);
        }
    }

    void reportStatus(final int gnssStatus) {
        android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.location.gnss.hal.GnssNative$$ExternalSyntheticLambda0
            public final void runOrThrow() throws java.lang.Exception {
                this.f$0.lambda$reportStatus$6(gnssStatus);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$reportStatus$6(int gnssStatus) throws java.lang.Exception {
        for (int i = 0; i < this.mStatusCallbacks.length; i++) {
            this.mStatusCallbacks[i].onReportStatus(gnssStatus);
        }
    }

    void reportSvStatus(final int svCount, final int[] svidWithFlags, final float[] cn0DbHzs, final float[] elevations, final float[] azimuths, final float[] carrierFrequencies, final float[] basebandCn0DbHzs) {
        android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.location.gnss.hal.GnssNative$$ExternalSyntheticLambda12
            public final void runOrThrow() throws java.lang.Exception {
                this.f$0.lambda$reportSvStatus$7(svCount, svidWithFlags, cn0DbHzs, elevations, azimuths, carrierFrequencies, basebandCn0DbHzs);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$reportSvStatus$7(int svCount, int[] svidWithFlags, float[] cn0DbHzs, float[] elevations, float[] azimuths, float[] carrierFrequencies, float[] basebandCn0DbHzs) throws java.lang.Exception {
        android.location.GnssStatus gnssStatus = android.location.GnssStatus.wrap(svCount, svidWithFlags, cn0DbHzs, elevations, azimuths, carrierFrequencies, basebandCn0DbHzs);
        if (this.mOplusLbsClass != null) {
            gnssStatus = this.mOplusLbsClass.onGnssSvStrategy(gnssStatus);
        } else {
            this.mOplusLbsClass = (com.android.server.location.interfaces.IOplusLBSMainClass) com.android.server.location.common.OplusLbsFactory.getInstance().getFeature(com.android.server.location.interfaces.IOplusLBSMainClass.DEFAULT, null);
        }
        for (int i = 0; i < this.mSvStatusCallbacks.length; i++) {
            this.mSvStatusCallbacks[i].onReportSvStatus(gnssStatus);
        }
    }

    void reportAGpsStatus(final int agpsType, final int agpsStatus, final byte[] suplIpAddr) {
        android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.location.gnss.hal.GnssNative$$ExternalSyntheticLambda4
            public final void runOrThrow() throws java.lang.Exception {
                this.f$0.lambda$reportAGpsStatus$8(agpsType, agpsStatus, suplIpAddr);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$reportAGpsStatus$8(int agpsType, int agpsStatus, byte[] suplIpAddr) throws java.lang.Exception {
        this.mAGpsCallbacks.onReportAGpsStatus(agpsType, agpsStatus, suplIpAddr);
    }

    void reportNmea(final long timestamp) {
        android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.location.gnss.hal.GnssNative$$ExternalSyntheticLambda25
            public final void runOrThrow() throws java.lang.Exception {
                this.f$0.lambda$reportNmea$9(timestamp);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$reportNmea$9(long timestamp) throws java.lang.Exception {
        if (this.mItarSpeedLimitExceeded) {
            return;
        }
        for (int i = 0; i < this.mNmeaCallbacks.length; i++) {
            this.mNmeaCallbacks[i].onReportNmea(timestamp);
        }
    }

    void reportMeasurementData(final android.location.GnssMeasurementsEvent event) {
        android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.location.gnss.hal.GnssNative$$ExternalSyntheticLambda16
            public final void runOrThrow() throws java.lang.Exception {
                this.f$0.lambda$reportMeasurementData$10(event);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$reportMeasurementData$10(android.location.GnssMeasurementsEvent event) throws java.lang.Exception {
        if (this.mItarSpeedLimitExceeded) {
            return;
        }
        for (int i = 0; i < this.mMeasurementCallbacks.length; i++) {
            this.mMeasurementCallbacks[i].onReportMeasurements(event);
        }
    }

    void reportAntennaInfo(final java.util.List<android.location.GnssAntennaInfo> antennaInfos) {
        android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.location.gnss.hal.GnssNative$$ExternalSyntheticLambda24
            public final void runOrThrow() throws java.lang.Exception {
                this.f$0.lambda$reportAntennaInfo$11(antennaInfos);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$reportAntennaInfo$11(java.util.List antennaInfos) throws java.lang.Exception {
        for (int i = 0; i < this.mAntennaInfoCallbacks.length; i++) {
            this.mAntennaInfoCallbacks[i].onReportAntennaInfo(antennaInfos);
        }
    }

    void reportNavigationMessage(final android.location.GnssNavigationMessage event) {
        android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.location.gnss.hal.GnssNative$$ExternalSyntheticLambda21
            public final void runOrThrow() throws java.lang.Exception {
                this.f$0.lambda$reportNavigationMessage$12(event);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$reportNavigationMessage$12(android.location.GnssNavigationMessage event) throws java.lang.Exception {
        if (this.mItarSpeedLimitExceeded) {
            return;
        }
        for (int i = 0; i < this.mNavigationMessageCallbacks.length; i++) {
            this.mNavigationMessageCallbacks[i].onReportNavigationMessage(event);
        }
    }

    void setTopHalCapabilities(int capabilities, boolean isAdrCapabilityKnown) {
        this.mTopFlags |= capabilities;
        android.location.GnssCapabilities oldCapabilities = this.mCapabilities;
        this.mCapabilities = oldCapabilities.withTopHalFlags(this.mTopFlags, isAdrCapabilityKnown);
        onCapabilitiesChanged(oldCapabilities, this.mCapabilities);
    }

    void setSubHalMeasurementCorrectionsCapabilities(int capabilities) {
        android.location.GnssCapabilities oldCapabilities = this.mCapabilities;
        this.mCapabilities = oldCapabilities.withSubHalMeasurementCorrectionsFlags(capabilities);
        onCapabilitiesChanged(oldCapabilities, this.mCapabilities);
    }

    void setSubHalPowerIndicationCapabilities(int capabilities) {
        android.location.GnssCapabilities oldCapabilities = this.mCapabilities;
        this.mCapabilities = oldCapabilities.withSubHalPowerFlags(capabilities);
        onCapabilitiesChanged(oldCapabilities, this.mCapabilities);
    }

    void setSignalTypeCapabilities(java.util.List<android.location.GnssSignalType> signalTypes) {
        android.location.GnssCapabilities oldCapabilities = this.mCapabilities;
        this.mCapabilities = oldCapabilities.withSignalTypes(signalTypes);
        onCapabilitiesChanged(oldCapabilities, this.mCapabilities);
    }

    private void onCapabilitiesChanged(final android.location.GnssCapabilities oldCapabilities, final android.location.GnssCapabilities newCapabilities) {
        android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.location.gnss.hal.GnssNative$$ExternalSyntheticLambda22
            public final void runOrThrow() throws java.lang.Exception {
                this.f$0.lambda$onCapabilitiesChanged$13(newCapabilities, oldCapabilities);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCapabilitiesChanged$13(android.location.GnssCapabilities newCapabilities, android.location.GnssCapabilities oldCapabilities) throws java.lang.Exception {
        if (newCapabilities.equals(oldCapabilities)) {
            return;
        }
        android.util.Log.i(com.android.server.location.gnss.GnssManagerService.TAG, "gnss capabilities changed to " + newCapabilities);
        for (int i = 0; i < this.mBaseCallbacks.length; i++) {
            this.mBaseCallbacks[i].onCapabilitiesChanged(oldCapabilities, newCapabilities);
        }
    }

    void reportGnssPowerStats(final com.android.server.location.gnss.GnssPowerStats powerStats) {
        synchronized (this.mPowerStatsLock) {
            this.mHandler.removeCallbacks(this.mPowerStatsTimeoutCallback);
            if (powerStats != null) {
                if (this.mOplusLbsClass == null) {
                    this.mOplusLbsClass = (com.android.server.location.interfaces.IOplusLBSMainClass) com.android.server.location.common.OplusLbsFactory.getInstance().getFeature(com.android.server.location.interfaces.IOplusLBSMainClass.DEFAULT, null);
                }
                if (this.mOplusLbsClass != null) {
                    this.mLastKnownPowerStats = this.mOplusLbsClass.reportGnssPowerStatsExt(powerStats);
                } else {
                    this.mLastKnownPowerStats = powerStats;
                }
            }
            this.mPendingPowerStatsCallbacks.forEach(new java.util.function.Consumer() { // from class: com.android.server.location.gnss.hal.GnssNative$$ExternalSyntheticLambda8
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    ((com.android.server.location.gnss.hal.GnssNative.PowerStatsCallback) obj).onReportPowerStats(powerStats);
                }
            });
            this.mPendingPowerStatsCallbacks.clear();
        }
    }

    void setGnssYearOfHardware(int year) {
        this.mHardwareYear = year;
    }

    private void setGnssHardwareModelName(java.lang.String modelName) {
        this.mHardwareModelName = modelName;
    }

    void reportLocationBatch(final android.location.Location[] locations) {
        android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.location.gnss.hal.GnssNative$$ExternalSyntheticLambda7
            public final void runOrThrow() throws java.lang.Exception {
                this.f$0.lambda$reportLocationBatch$15(locations);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$reportLocationBatch$15(android.location.Location[] locations) throws java.lang.Exception {
        for (int i = 0; i < this.mLocationCallbacks.length; i++) {
            this.mLocationCallbacks[i].onReportLocations(locations);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$psdsDownloadRequest$16(int psdsType) throws java.lang.Exception {
        this.mPsdsCallbacks.onRequestPsdsDownload(psdsType);
    }

    void psdsDownloadRequest(final int psdsType) {
        android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.location.gnss.hal.GnssNative$$ExternalSyntheticLambda13
            public final void runOrThrow() throws java.lang.Exception {
                this.f$0.lambda$psdsDownloadRequest$16(psdsType);
            }
        });
    }

    void reportGeofenceTransition(final int geofenceId, final android.location.Location location, final int transition, final long transitionTimestamp) {
        android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.location.gnss.hal.GnssNative$$ExternalSyntheticLambda11
            public final void runOrThrow() throws java.lang.Exception {
                this.f$0.lambda$reportGeofenceTransition$17(geofenceId, location, transition, transitionTimestamp);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$reportGeofenceTransition$17(int geofenceId, android.location.Location location, int transition, long transitionTimestamp) throws java.lang.Exception {
        this.mGeofenceCallbacks.onReportGeofenceTransition(geofenceId, location, transition, transitionTimestamp);
    }

    void reportGeofenceStatus(final int status, final android.location.Location location) {
        android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.location.gnss.hal.GnssNative$$ExternalSyntheticLambda3
            public final void runOrThrow() throws java.lang.Exception {
                this.f$0.lambda$reportGeofenceStatus$18(status, location);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$reportGeofenceStatus$18(int status, android.location.Location location) throws java.lang.Exception {
        this.mGeofenceCallbacks.onReportGeofenceStatus(status, location);
    }

    void reportGeofenceAddStatus(final int geofenceId, final int status) {
        android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.location.gnss.hal.GnssNative$$ExternalSyntheticLambda9
            public final void runOrThrow() throws java.lang.Exception {
                this.f$0.lambda$reportGeofenceAddStatus$19(geofenceId, status);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$reportGeofenceAddStatus$19(int geofenceId, int status) throws java.lang.Exception {
        this.mGeofenceCallbacks.onReportGeofenceAddStatus(geofenceId, status);
    }

    void reportGeofenceRemoveStatus(final int geofenceId, final int status) {
        android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.location.gnss.hal.GnssNative$$ExternalSyntheticLambda23
            public final void runOrThrow() throws java.lang.Exception {
                this.f$0.lambda$reportGeofenceRemoveStatus$20(geofenceId, status);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$reportGeofenceRemoveStatus$20(int geofenceId, int status) throws java.lang.Exception {
        this.mGeofenceCallbacks.onReportGeofenceRemoveStatus(geofenceId, status);
    }

    void reportGeofencePauseStatus(final int geofenceId, final int status) {
        android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.location.gnss.hal.GnssNative$$ExternalSyntheticLambda1
            public final void runOrThrow() throws java.lang.Exception {
                this.f$0.lambda$reportGeofencePauseStatus$21(geofenceId, status);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$reportGeofencePauseStatus$21(int geofenceId, int status) throws java.lang.Exception {
        this.mGeofenceCallbacks.onReportGeofencePauseStatus(geofenceId, status);
    }

    void reportGeofenceResumeStatus(final int geofenceId, final int status) {
        android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.location.gnss.hal.GnssNative$$ExternalSyntheticLambda6
            public final void runOrThrow() throws java.lang.Exception {
                this.f$0.lambda$reportGeofenceResumeStatus$22(geofenceId, status);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$reportGeofenceResumeStatus$22(int geofenceId, int status) throws java.lang.Exception {
        this.mGeofenceCallbacks.onReportGeofenceResumeStatus(geofenceId, status);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestSetID$23(int flags) throws java.lang.Exception {
        this.mAGpsCallbacks.onRequestSetID(flags);
    }

    void requestSetID(final int flags) {
        android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.location.gnss.hal.GnssNative$$ExternalSyntheticLambda29
            public final void runOrThrow() throws java.lang.Exception {
                this.f$0.lambda$requestSetID$23(flags);
            }
        });
    }

    void requestLocation(final boolean independentFromGnss, final boolean isUserEmergency) {
        android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.location.gnss.hal.GnssNative$$ExternalSyntheticLambda14
            public final void runOrThrow() throws java.lang.Exception {
                this.f$0.lambda$requestLocation$24(independentFromGnss, isUserEmergency);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestLocation$24(boolean independentFromGnss, boolean isUserEmergency) throws java.lang.Exception {
        this.mLocationRequestCallbacks.onRequestLocation(independentFromGnss, isUserEmergency);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestUtcTime$25() throws java.lang.Exception {
        this.mTimeCallbacks.onRequestUtcTime();
    }

    void requestUtcTime() {
        android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.location.gnss.hal.GnssNative$$ExternalSyntheticLambda20
            public final void runOrThrow() throws java.lang.Exception {
                this.f$0.lambda$requestUtcTime$25();
            }
        });
    }

    void requestRefLocation() {
        android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.location.gnss.hal.GnssNative$$ExternalSyntheticLambda27
            public final void runOrThrow() throws java.lang.Exception {
                this.f$0.lambda$requestRefLocation$26();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestRefLocation$26() throws java.lang.Exception {
        this.mLocationRequestCallbacks.onRequestRefLocation();
    }

    void reportNfwNotification(final java.lang.String proxyAppPackageName, final byte protocolStack, final java.lang.String otherProtocolStackName, final byte requestor, final java.lang.String requestorId, final byte responseType, final boolean inEmergencyMode, final boolean isCachedLocation) {
        android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.location.gnss.hal.GnssNative$$ExternalSyntheticLambda28
            public final void runOrThrow() throws java.lang.Exception {
                this.f$0.lambda$reportNfwNotification$27(proxyAppPackageName, protocolStack, otherProtocolStackName, requestor, requestorId, responseType, inEmergencyMode, isCachedLocation);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$reportNfwNotification$27(java.lang.String proxyAppPackageName, byte protocolStack, java.lang.String otherProtocolStackName, byte requestor, java.lang.String requestorId, byte responseType, boolean inEmergencyMode, boolean isCachedLocation) throws java.lang.Exception {
        this.mNotificationCallbacks.onReportNfwNotification(proxyAppPackageName, protocolStack, otherProtocolStackName, requestor, requestorId, responseType, inEmergencyMode, isCachedLocation);
    }

    public boolean isInEmergencySession() {
        return ((java.lang.Boolean) android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.location.gnss.hal.GnssNative$$ExternalSyntheticLambda2
            public final java.lang.Object getOrThrow() {
                return this.f$0.lambda$isInEmergencySession$28();
            }
        })).booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.lang.Boolean lambda$isInEmergencySession$28() throws java.lang.Exception {
        return java.lang.Boolean.valueOf(this.mEmergencyHelper.isInEmergency(java.util.concurrent.TimeUnit.SECONDS.toMillis(this.mConfiguration.getEsExtensionSec())));
    }

    public static class GnssHal {
        protected GnssHal() {
        }

        protected void classInitOnce() {
            com.android.server.location.gnss.hal.GnssNative.native_class_init_once();
        }

        protected boolean isSupported() {
            return com.android.server.location.gnss.hal.GnssNative.native_is_supported();
        }

        protected void initOnce(com.android.server.location.gnss.hal.GnssNative gnssNative, boolean reinitializeGnssServiceHandle) {
            gnssNative.native_init_once(reinitializeGnssServiceHandle);
        }

        protected boolean init() {
            return com.android.server.location.gnss.hal.GnssNative.native_init();
        }

        protected void cleanup() {
            com.android.server.location.gnss.hal.GnssNative.native_cleanup();
        }

        protected boolean start() {
            return com.android.server.location.gnss.hal.GnssNative.native_start();
        }

        protected boolean stop() {
            return com.android.server.location.gnss.hal.GnssNative.native_stop();
        }

        protected boolean setPositionMode(int mode, int recurrence, int minInterval, int preferredAccuracy, int preferredTime, boolean lowPowerMode) {
            return com.android.server.location.gnss.hal.GnssNative.native_set_position_mode(mode, recurrence, minInterval, preferredAccuracy, preferredTime, lowPowerMode);
        }

        protected java.lang.String getInternalState() {
            return com.android.server.location.gnss.hal.GnssNative.native_get_internal_state();
        }

        protected void deleteAidingData(int flags) {
            com.android.server.location.gnss.hal.GnssNative.native_delete_aiding_data(flags);
        }

        protected int readNmea(byte[] buffer, int bufferSize) {
            return com.android.server.location.gnss.hal.GnssNative.native_read_nmea(buffer, bufferSize);
        }

        protected void injectLocation(int gnssLocationFlags, double latitude, double longitude, double altitude, float speed, float bearing, float horizontalAccuracy, float verticalAccuracy, float speedAccuracy, float bearingAccuracy, long timestamp, int elapsedRealtimeFlags, long elapsedRealtimeNanos, double elapsedRealtimeUncertaintyNanos) {
            com.android.server.location.gnss.hal.GnssNative.native_inject_location(gnssLocationFlags, latitude, longitude, altitude, speed, bearing, horizontalAccuracy, verticalAccuracy, speedAccuracy, bearingAccuracy, timestamp, elapsedRealtimeFlags, elapsedRealtimeNanos, elapsedRealtimeUncertaintyNanos);
        }

        protected void injectBestLocation(int gnssLocationFlags, double latitude, double longitude, double altitude, float speed, float bearing, float horizontalAccuracy, float verticalAccuracy, float speedAccuracy, float bearingAccuracy, long timestamp, int elapsedRealtimeFlags, long elapsedRealtimeNanos, double elapsedRealtimeUncertaintyNanos) {
            com.android.server.location.gnss.hal.GnssNative.native_inject_best_location(gnssLocationFlags, latitude, longitude, altitude, speed, bearing, horizontalAccuracy, verticalAccuracy, speedAccuracy, bearingAccuracy, timestamp, elapsedRealtimeFlags, elapsedRealtimeNanos, elapsedRealtimeUncertaintyNanos);
        }

        protected void injectTime(long time, long timeReference, int uncertainty) {
            com.android.server.location.gnss.hal.GnssNative.native_inject_time(time, timeReference, uncertainty);
        }

        protected boolean isNavigationMessageCollectionSupported() {
            return com.android.server.location.gnss.hal.GnssNative.native_is_navigation_message_supported();
        }

        protected boolean startNavigationMessageCollection() {
            return com.android.server.location.gnss.hal.GnssNative.native_start_navigation_message_collection();
        }

        protected boolean stopNavigationMessageCollection() {
            return com.android.server.location.gnss.hal.GnssNative.native_stop_navigation_message_collection();
        }

        protected boolean isAntennaInfoSupported() {
            return com.android.server.location.gnss.hal.GnssNative.native_is_antenna_info_supported();
        }

        protected boolean startAntennaInfoListening() {
            return com.android.server.location.gnss.hal.GnssNative.native_start_antenna_info_listening();
        }

        protected boolean stopAntennaInfoListening() {
            return com.android.server.location.gnss.hal.GnssNative.native_stop_antenna_info_listening();
        }

        protected boolean isMeasurementSupported() {
            return com.android.server.location.gnss.hal.GnssNative.native_is_measurement_supported();
        }

        protected boolean startMeasurementCollection(boolean enableFullTracking, boolean enableCorrVecOutputs, int intervalMillis) {
            return com.android.server.location.gnss.hal.GnssNative.native_start_measurement_collection(enableFullTracking, enableCorrVecOutputs, intervalMillis);
        }

        protected boolean stopMeasurementCollection() {
            return com.android.server.location.gnss.hal.GnssNative.native_stop_measurement_collection();
        }

        protected boolean isMeasurementCorrectionsSupported() {
            return com.android.server.location.gnss.hal.GnssNative.native_is_measurement_corrections_supported();
        }

        protected boolean injectMeasurementCorrections(android.location.GnssMeasurementCorrections corrections) {
            return com.android.server.location.gnss.hal.GnssNative.native_inject_measurement_corrections(corrections);
        }

        protected boolean startSvStatusCollection() {
            return com.android.server.location.gnss.hal.GnssNative.native_start_sv_status_collection();
        }

        protected boolean stopSvStatusCollection() {
            return com.android.server.location.gnss.hal.GnssNative.native_stop_sv_status_collection();
        }

        protected boolean startNmeaMessageCollection() {
            return com.android.server.location.gnss.hal.GnssNative.native_start_nmea_message_collection();
        }

        protected boolean stopNmeaMessageCollection() {
            return com.android.server.location.gnss.hal.GnssNative.native_stop_nmea_message_collection();
        }

        protected int getBatchSize() {
            return com.android.server.location.gnss.hal.GnssNative.native_get_batch_size();
        }

        protected boolean initBatching() {
            return com.android.server.location.gnss.hal.GnssNative.native_init_batching();
        }

        protected void cleanupBatching() {
            com.android.server.location.gnss.hal.GnssNative.native_cleanup_batching();
        }

        protected boolean startBatch(long periodNanos, float minUpdateDistanceMeters, boolean wakeOnFifoFull) {
            return com.android.server.location.gnss.hal.GnssNative.native_start_batch(periodNanos, minUpdateDistanceMeters, wakeOnFifoFull);
        }

        protected void flushBatch() {
            com.android.server.location.gnss.hal.GnssNative.native_flush_batch();
        }

        protected void stopBatch() {
            com.android.server.location.gnss.hal.GnssNative.native_stop_batch();
        }

        protected boolean isGeofencingSupported() {
            return com.android.server.location.gnss.hal.GnssNative.native_is_geofence_supported();
        }

        protected boolean addGeofence(int geofenceId, double latitude, double longitude, double radius, int lastTransition, int monitorTransitions, int notificationResponsiveness, int unknownTimer) {
            return com.android.server.location.gnss.hal.GnssNative.native_add_geofence(geofenceId, latitude, longitude, radius, lastTransition, monitorTransitions, notificationResponsiveness, unknownTimer);
        }

        protected boolean resumeGeofence(int geofenceId, int monitorTransitions) {
            return com.android.server.location.gnss.hal.GnssNative.native_resume_geofence(geofenceId, monitorTransitions);
        }

        protected boolean pauseGeofence(int geofenceId) {
            return com.android.server.location.gnss.hal.GnssNative.native_pause_geofence(geofenceId);
        }

        protected boolean removeGeofence(int geofenceId) {
            return com.android.server.location.gnss.hal.GnssNative.native_remove_geofence(geofenceId);
        }

        protected boolean isGnssVisibilityControlSupported() {
            return com.android.server.location.gnss.hal.GnssNative.native_is_gnss_visibility_control_supported();
        }

        protected void requestPowerStats() {
            com.android.server.location.gnss.hal.GnssNative.native_request_power_stats();
        }

        protected void setAgpsServer(int type, java.lang.String hostname, int port) {
            com.android.server.location.gnss.hal.GnssNative.native_set_agps_server(type, hostname, port);
        }

        protected void setAgpsSetId(int type, java.lang.String setId) {
            com.android.server.location.gnss.hal.GnssNative.native_agps_set_id(type, setId);
        }

        protected void setAgpsReferenceLocationCellId(int type, int mcc, int mnc, int lac, long cid, int tac, int pcid, int arfcn) {
            com.android.server.location.gnss.hal.GnssNative.native_agps_set_ref_location_cellid(type, mcc, mnc, lac, cid, tac, pcid, arfcn);
        }

        protected boolean isPsdsSupported() {
            return com.android.server.location.gnss.hal.GnssNative.native_supports_psds();
        }

        protected void injectPsdsData(byte[] data, int length, int psdsType) {
            com.android.server.location.gnss.hal.GnssNative.native_inject_psds_data(data, length, psdsType);
        }

        protected void injectNiSuplMessageData(byte[] data, int length, int slotIndex) {
            com.android.server.location.gnss.hal.GnssNative.native_inject_ni_supl_message_data(data, length, slotIndex);
        }
    }
}
