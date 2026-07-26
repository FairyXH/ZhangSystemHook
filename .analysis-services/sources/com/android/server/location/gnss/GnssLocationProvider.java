package com.android.server.location.gnss;

/* JADX INFO: loaded from: classes2.dex */
public class GnssLocationProvider extends com.android.server.location.provider.AbstractLocationProvider implements com.android.server.location.gnss.NetworkTimeHelper.InjectTimeCallback, com.android.server.location.gnss.GnssSatelliteBlocklistHelper.GnssSatelliteBlocklistCallback, com.android.server.location.gnss.hal.GnssNative.BaseCallbacks, com.android.server.location.gnss.hal.GnssNative.LocationCallbacks, com.android.server.location.gnss.hal.GnssNative.SvStatusCallbacks, com.android.server.location.gnss.hal.GnssNative.AGpsCallbacks, com.android.server.location.gnss.hal.GnssNative.PsdsCallbacks, com.android.server.location.gnss.hal.GnssNative.NotificationCallbacks, com.android.server.location.gnss.hal.GnssNative.LocationRequestCallbacks, com.android.server.location.gnss.hal.GnssNative.TimeCallbacks {
    private static final int AGPS_SUPL_MODE_MSA = 2;
    private static final int AGPS_SUPL_MODE_MSB = 1;
    private static final int DEBUG_LEVEL_DEBUG = 4;
    private static final int DEBUG_LEVEL_ERROR = 1;
    private static final int DEBUG_LEVEL_IMPORTANT = 3;
    private static final int DEBUG_LEVEL_NONE = 0;
    private static final int DEBUG_LEVEL_VERBOSE = 5;
    private static final int DEBUG_LEVEL_WARNING = 2;
    private static final long DOWNLOAD_PSDS_DATA_TIMEOUT_MS = 60000;
    private static final int EMERGENCY_LOCATION_UPDATE_DURATION_MULTIPLIER = 3;
    private static final int GPS_POLLING_THRESHOLD_INTERVAL = 10000;
    private static final long LOCATION_OFF_DELAY_THRESHOLD_ERROR_MILLIS = 15000;
    private static final long LOCATION_OFF_DELAY_THRESHOLD_WARN_MILLIS = 2000;
    private static final long LOCATION_UPDATE_DURATION_MILLIS = 10000;
    private static final long LOCATION_UPDATE_MIN_TIME_INTERVAL_MILLIS = 1000;
    private static final long MAX_BATCH_LENGTH_MS = 86400000;
    private static final long MAX_BATCH_TIMESTAMP_DELTA_MS = 500;
    private static final long MAX_RETRY_INTERVAL = 14400000;
    private static final int MIN_BATCH_INTERVAL_MS = 1000;
    private static final int NO_FIX_TIMEOUT = 60000;
    private static final long RETRY_INTERVAL = 300000;
    private static final int TCP_MAX_PORT = 65535;
    private static final int TCP_MIN_PORT = 0;
    private static final long WAKELOCK_TIMEOUT_MILLIS = 30000;
    private final android.app.AlarmManager mAlarmManager;
    private final android.app.AppOpsManager mAppOps;
    private boolean mAutomotiveSuspend;
    private android.app.AlarmManager.OnAlarmListener mBatchingAlarm;
    private boolean mBatchingEnabled;
    private boolean mBatchingStarted;
    private final com.android.internal.app.IBatteryStats mBatteryStats;
    private java.lang.String mC2KServerHost;
    private int mC2KServerPort;
    private final android.os.WorkSource mClientSource;
    private final android.content.Context mContext;
    private final java.util.Set<java.lang.Integer> mDownloadInProgressPsdsTypes;
    private final android.os.PowerManager.WakeLock mDownloadPsdsWakeLock;
    private int mDrStatus;
    private float mEBearing;
    private int mFixInterval;
    private long mFixRequestTime;
    private final java.util.ArrayList<java.lang.Runnable> mFlushListeners;
    private boolean mForceSleep;
    private final com.android.server.location.gnss.GnssConfiguration mGnssConfiguration;
    private com.android.server.location.gnss.IGnssLocationProviderExt mGnssLPExt;
    private com.android.server.location.gnss.IGnssLocationProviderSocExt mGnssLPSocExt;
    private com.android.server.location.gnss.IGnssLocationProviderWrapper mGnssLocationProviderWrapper;
    private final com.android.server.location.gnss.GnssMetrics mGnssMetrics;
    private final com.android.server.location.gnss.hal.GnssNative mGnssNative;
    private final com.android.server.location.gnss.GnssSatelliteBlocklistHelper mGnssSatelliteBlocklistHelper;
    private com.android.server.location.gnss.GnssVisibilityControl mGnssVisibilityControl;
    private boolean mGpsEnabled;
    private final android.os.Handler mHandler;
    private boolean mHasNetworkLocationListenerRegistered;
    private boolean mInitialized;
    private android.content.BroadcastReceiver mIntentReceiver;
    private long mLastFixTime;
    private com.android.server.location.gnss.GnssPositionMode mLastPositionMode;
    private final com.android.server.location.gnss.GnssLocationProvider.LocationExtras mLocationExtras;
    private final java.lang.Object mLock;
    private final com.android.internal.location.GpsNetInitiatedHandler mNIHandler;
    private final com.android.server.location.gnss.GnssNetworkConnectivityHandler mNetworkConnectivityHandler;
    private final com.android.server.location.gnss.NetworkTimeHelper mNetworkTimeHelper;
    private com.android.server.location.interfaces.IOplusLBSMainClass mOplusLbsClass;
    private final java.util.Set<java.lang.Integer> mPendingDownloadPsdsTypes;
    private int mPositionMode;
    private boolean mPreciseLocationSupported;
    private android.location.provider.ProviderRequest mProviderRequest;
    private final com.android.server.location.gnss.ExponentialBackOff mPsdsBackOff;
    private final java.lang.Object mPsdsPeriodicDownloadToken;
    private boolean mReportLocation;
    private boolean mShutdown;
    private boolean mStarted;
    private long mStartedChangedElapsedRealtime;
    private boolean mSuplEsEnabled;
    private java.lang.String mSuplServerHost;
    private int mSuplServerPort;
    private boolean mSupportsPsds;
    private int mTimeToFirstFix;
    private final android.app.AlarmManager.OnAlarmListener mTimeoutListener;
    private final android.os.PowerManager.WakeLock mWakeLock;
    private final android.app.AlarmManager.OnAlarmListener mWakeupListener;
    private static final java.lang.String TAG = "GnssLocationProvider";
    private static boolean KEYLOG = android.util.Log.isLoggable(TAG, 3);
    private static boolean DEBUG = android.util.Log.isLoggable(TAG, 3);
    private static boolean VERBOSE = android.util.Log.isLoggable(TAG, 2);
    private static final android.location.provider.ProviderProperties PROPERTIES = new android.location.provider.ProviderProperties.Builder().setHasSatelliteRequirement(true).setHasAltitudeSupport(true).setHasSpeedSupport(true).setHasBearingSupport(true).setPowerUsage(3).setAccuracy(1).build();

    private static class LocationExtras {
        private final android.os.Bundle mBundle = new android.os.Bundle();
        private int mMaxCn0;
        private int mMeanCn0;
        private int mSvCount;

        LocationExtras() {
        }

        public void set(int svCount, int meanCn0, int maxCn0) {
            synchronized (this) {
                this.mSvCount = svCount;
                this.mMeanCn0 = meanCn0;
                this.mMaxCn0 = maxCn0;
            }
            setBundle(this.mBundle);
        }

        public void reset() {
            set(0, 0, 0);
        }

        public void setBundle(android.os.Bundle extras) {
            if (extras != null) {
                synchronized (this) {
                    extras.putInt("satellites", this.mSvCount);
                    extras.putInt("meanCn0", this.mMeanCn0);
                    extras.putInt("maxCn0", this.mMaxCn0);
                }
            }
        }

        public android.os.Bundle getBundle() {
            android.os.Bundle bundle;
            synchronized (this) {
                bundle = new android.os.Bundle(this.mBundle);
            }
            return bundle;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onUpdateSatelliteBlocklist$0(int[] constellations, int[] svids) {
        this.mGnssConfiguration.setSatelliteBlocklist(constellations, svids);
    }

    @Override // com.android.server.location.gnss.GnssSatelliteBlocklistHelper.GnssSatelliteBlocklistCallback
    public void onUpdateSatelliteBlocklist(final int[] constellations, final int[] svids) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.location.gnss.GnssLocationProvider$$ExternalSyntheticLambda13
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onUpdateSatelliteBlocklist$0(constellations, svids);
            }
        });
        this.mGnssMetrics.resetConstellationTypes();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void subscriptionOrCarrierConfigChanged() {
        if (DEBUG) {
            android.util.Log.d(TAG, "received SIM related action: ");
        }
        android.telephony.TelephonyManager phone = (android.telephony.TelephonyManager) this.mContext.getSystemService(com.android.server.autofill.HintsHelper.AUTOFILL_HINT_PHONE);
        android.telephony.CarrierConfigManager configManager = (android.telephony.CarrierConfigManager) this.mContext.getSystemService("carrier_config");
        int ddSubId = android.telephony.SubscriptionManager.getDefaultDataSubscriptionId();
        if (android.telephony.SubscriptionManager.isValidSubscriptionId(ddSubId)) {
            phone = phone.createForSubscriptionId(ddSubId);
        }
        java.lang.String mccMnc = phone.getSimOperator();
        boolean isKeepLppProfile = false;
        if (!android.text.TextUtils.isEmpty(mccMnc)) {
            if (DEBUG) {
                android.util.Log.d(TAG, "SIM MCC/MNC is available: " + mccMnc);
            }
            if (configManager != null) {
                android.os.PersistableBundle b = android.telephony.SubscriptionManager.isValidSubscriptionId(ddSubId) ? configManager.getConfigForSubId(ddSubId) : null;
                if (b != null) {
                    isKeepLppProfile = b.getBoolean("gps.persist_lpp_mode_bool");
                }
            }
            if (!isKeepLppProfile) {
                android.os.SystemProperties.set("persist.sys.gps.lpp", "");
            } else {
                this.mGnssConfiguration.loadPropertiesFromCarrierConfig(false, -1);
                java.lang.String lpp_profile = this.mGnssConfiguration.getLppProfile();
                if (lpp_profile != null) {
                    android.os.SystemProperties.set("persist.sys.gps.lpp", lpp_profile);
                }
            }
            reloadGpsProperties();
            return;
        }
        if (DEBUG) {
            android.util.Log.d(TAG, "SIM MCC/MNC is still not available");
        }
        this.mGnssConfiguration.reloadGpsProperties();
    }

    private void reloadGpsProperties() {
        this.mGnssConfiguration.reloadGpsProperties();
        setSuplHostPort();
        this.mC2KServerHost = this.mGnssConfiguration.getC2KHost();
        this.mC2KServerPort = this.mGnssConfiguration.getC2KPort(0);
        this.mNIHandler.setEmergencyExtensionSeconds(this.mGnssConfiguration.getEsExtensionSec());
        this.mSuplEsEnabled = this.mGnssConfiguration.getSuplEs(0) == 1;
        if (this.mGnssVisibilityControl != null) {
            this.mGnssVisibilityControl.onConfigurationUpdated(this.mGnssConfiguration);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerNetworkLocationListener() {
        if (!this.mHasNetworkLocationListenerRegistered) {
            android.location.LocationManager locationManager = (android.location.LocationManager) java.util.Objects.requireNonNull((android.location.LocationManager) this.mContext.getSystemService(android.location.LocationManager.class));
            if (locationManager.getAllProviders().contains("network")) {
                locationManager.requestLocationUpdates("network", new android.location.LocationRequest.Builder(Long.MAX_VALUE).setMinUpdateIntervalMillis(0L).setHiddenFromAppOps(true).build(), com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, new com.android.server.location.gnss.GnssLocationProvider$$ExternalSyntheticLambda20(this));
                this.mHasNetworkLocationListenerRegistered = true;
                android.util.Log.d(TAG, "re-register network location listener success");
            }
        }
    }

    public GnssLocationProvider(android.content.Context context, com.android.server.location.gnss.hal.GnssNative gnssNative, com.android.server.location.gnss.GnssMetrics gnssMetrics) {
        super(com.android.server.FgThread.getExecutor(), android.location.util.identity.CallerIdentity.fromContext(context), PROPERTIES, java.util.Collections.emptySet());
        this.mLock = new java.lang.Object();
        this.mPsdsBackOff = new com.android.server.location.gnss.ExponentialBackOff(300000L, 14400000L);
        this.mFixInterval = 1000;
        this.mFixRequestTime = 0L;
        this.mTimeToFirstFix = 0;
        this.mClientSource = new android.os.WorkSource();
        this.mPsdsPeriodicDownloadToken = new java.lang.Object();
        this.mPendingDownloadPsdsTypes = new java.util.HashSet();
        this.mDownloadInProgressPsdsTypes = new java.util.HashSet();
        this.mSuplServerPort = 0;
        this.mSuplEsEnabled = false;
        this.mHasNetworkLocationListenerRegistered = false;
        this.mForceSleep = false;
        this.mLocationExtras = new com.android.server.location.gnss.GnssLocationProvider.LocationExtras();
        this.mWakeupListener = new android.app.AlarmManager.OnAlarmListener() { // from class: com.android.server.location.gnss.GnssLocationProvider$$ExternalSyntheticLambda24
            @Override // android.app.AlarmManager.OnAlarmListener
            public final void onAlarm() {
                this.f$0.startNavigating();
            }
        };
        this.mTimeoutListener = new android.app.AlarmManager.OnAlarmListener() { // from class: com.android.server.location.gnss.GnssLocationProvider$$ExternalSyntheticLambda25
            @Override // android.app.AlarmManager.OnAlarmListener
            public final void onAlarm() {
                this.f$0.hibernate();
            }
        };
        this.mFlushListeners = new java.util.ArrayList<>(0);
        this.mIntentReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.location.gnss.GnssLocationProvider.5
            /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
            /* JADX WARN: Removed duplicated region for block: B:22:0x0056  */
            @Override // android.content.BroadcastReceiver
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct add '--show-bad-code' argument
            */
            public void onReceive(android.content.Context r4, android.content.Intent r5) {
                /*
                    r3 = this;
                    java.lang.String r0 = r5.getAction()
                    boolean r1 = com.android.server.location.gnss.GnssLocationProvider.m4908$$Nest$sfgetDEBUG()
                    if (r1 == 0) goto L23
                    java.lang.StringBuilder r1 = new java.lang.StringBuilder
                    r1.<init>()
                    java.lang.String r2 = "receive broadcast intent, action: "
                    java.lang.StringBuilder r1 = r1.append(r2)
                    java.lang.StringBuilder r1 = r1.append(r0)
                    java.lang.String r1 = r1.toString()
                    java.lang.String r2 = "GnssLocationProvider"
                    android.util.Log.d(r2, r1)
                L23:
                    if (r0 != 0) goto L26
                    return
                L26:
                    int r1 = r0.hashCode()
                    switch(r1) {
                        case -1138588223: goto L4c;
                        case -873963303: goto L42;
                        case -25388475: goto L38;
                        case 2142067319: goto L2e;
                        default: goto L2d;
                    }
                L2d:
                    goto L56
                L2e:
                    java.lang.String r1 = "android.intent.action.DATA_SMS_RECEIVED"
                    boolean r1 = r0.equals(r1)
                    if (r1 == 0) goto L2d
                    r1 = 3
                    goto L57
                L38:
                    java.lang.String r1 = "android.intent.action.ACTION_DEFAULT_DATA_SUBSCRIPTION_CHANGED"
                    boolean r1 = r0.equals(r1)
                    if (r1 == 0) goto L2d
                    r1 = 1
                    goto L57
                L42:
                    java.lang.String r1 = "android.provider.Telephony.WAP_PUSH_RECEIVED"
                    boolean r1 = r0.equals(r1)
                    if (r1 == 0) goto L2d
                    r1 = 2
                    goto L57
                L4c:
                    java.lang.String r1 = "android.telephony.action.CARRIER_CONFIG_CHANGED"
                    boolean r1 = r0.equals(r1)
                    if (r1 == 0) goto L2d
                    r1 = 0
                    goto L57
                L56:
                    r1 = -1
                L57:
                    switch(r1) {
                        case 0: goto L61;
                        case 1: goto L61;
                        case 2: goto L5b;
                        case 3: goto L5b;
                        default: goto L5a;
                    }
                L5a:
                    goto L67
                L5b:
                    com.android.server.location.gnss.GnssLocationProvider r1 = com.android.server.location.gnss.GnssLocationProvider.this
                    com.android.server.location.gnss.GnssLocationProvider.m4901$$Nest$minjectSuplInit(r1, r5)
                    goto L67
                L61:
                    com.android.server.location.gnss.GnssLocationProvider r1 = com.android.server.location.gnss.GnssLocationProvider.this
                    com.android.server.location.gnss.GnssLocationProvider.m4905$$Nest$msubscriptionOrCarrierConfigChanged(r1)
                L67:
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.server.location.gnss.GnssLocationProvider.AnonymousClass5.onReceive(android.content.Context, android.content.Intent):void");
            }
        };
        this.mGnssLocationProviderWrapper = new com.android.server.location.gnss.GnssLocationProvider.GnssLocationProviderWrapper();
        this.mReportLocation = false;
        this.mOplusLbsClass = null;
        this.mGnssLPSocExt = (com.android.server.location.gnss.IGnssLocationProviderSocExt) system.ext.loader.core.ExtLoader.type(com.android.server.location.gnss.IGnssLocationProviderSocExt.class).base(this).create();
        this.mPreciseLocationSupported = false;
        this.mGnssLPExt = (com.android.server.location.gnss.IGnssLocationProviderExt) system.ext.loader.core.ExtLoader.type(com.android.server.location.gnss.IGnssLocationProviderExt.class).base(this).create();
        this.mDrStatus = -1;
        this.mEBearing = -1.0f;
        this.mContext = context;
        this.mGnssNative = gnssNative;
        this.mGnssMetrics = gnssMetrics;
        android.os.PowerManager powerManager = (android.os.PowerManager) java.util.Objects.requireNonNull((android.os.PowerManager) this.mContext.getSystemService(android.os.PowerManager.class));
        this.mWakeLock = powerManager.newWakeLock(1, "*location*:GnssLocationProvider");
        this.mWakeLock.setReferenceCounted(true);
        this.mDownloadPsdsWakeLock = powerManager.newWakeLock(1, "*location*:PsdsDownload");
        this.mDownloadPsdsWakeLock.setReferenceCounted(true);
        this.mAlarmManager = (android.app.AlarmManager) this.mContext.getSystemService(com.android.server.am.HostingRecord.TRIGGER_TYPE_ALARM);
        this.mAppOps = (android.app.AppOpsManager) this.mContext.getSystemService(android.app.AppOpsManager.class);
        this.mBatteryStats = com.android.internal.app.IBatteryStats.Stub.asInterface(android.os.ServiceManager.getService("batterystats"));
        this.mHandler = com.android.server.FgThread.getHandler();
        this.mGnssConfiguration = this.mGnssNative.getConfiguration();
        com.android.internal.location.GpsNetInitiatedHandler.EmergencyCallCallback emergencyCallCallback = new com.android.server.location.gnss.GnssLocationProvider.AnonymousClass1();
        this.mNIHandler = new com.android.internal.location.GpsNetInitiatedHandler(context, emergencyCallCallback, this.mSuplEsEnabled);
        this.mPendingDownloadPsdsTypes.add(1);
        this.mNetworkConnectivityHandler = new com.android.server.location.gnss.GnssNetworkConnectivityHandler(context, new com.android.server.location.gnss.GnssNetworkConnectivityHandler.GnssNetworkListener() { // from class: com.android.server.location.gnss.GnssLocationProvider$$ExternalSyntheticLambda26
            @Override // com.android.server.location.gnss.GnssNetworkConnectivityHandler.GnssNetworkListener
            public final void onNetworkAvailable() {
                this.f$0.onNetworkAvailable();
            }
        }, this.mHandler.getLooper(), this.mNIHandler);
        this.mNetworkTimeHelper = com.android.server.location.gnss.NetworkTimeHelper.create(this.mContext, this.mHandler.getLooper(), this);
        this.mGnssSatelliteBlocklistHelper = new com.android.server.location.gnss.GnssSatelliteBlocklistHelper(this.mContext, this.mHandler.getLooper(), this);
        setAllowed(true);
        if (this.mNetworkTimeHelper instanceof com.android.server.location.gnss.NtpNetworkTimeHelper) {
            this.mGnssLPSocExt.init(context, this.mHandler, (com.android.server.location.gnss.NtpNetworkTimeHelper) this.mNetworkTimeHelper);
        } else {
            android.util.Log.e(TAG, "mNetworkTimeHelper type error!");
        }
        this.mGnssNative.addBaseCallbacks(this);
        this.mGnssNative.addLocationCallbacks(this);
        this.mGnssNative.addSvStatusCallbacks(this);
        this.mGnssNative.setAGpsCallbacks(this);
        this.mGnssNative.setPsdsCallbacks(this);
        this.mGnssNative.setNotificationCallbacks(this);
        this.mGnssNative.setLocationRequestCallbacks(this);
        this.mGnssNative.setTimeCallbacks(this);
    }

    /* JADX INFO: renamed from: com.android.server.location.gnss.GnssLocationProvider$1, reason: invalid class name */
    class AnonymousClass1 implements com.android.internal.location.GpsNetInitiatedHandler.EmergencyCallCallback {
        AnonymousClass1() {
        }

        public void onEmergencyCallStart(final int subId) {
            if (!com.android.server.location.gnss.GnssLocationProvider.this.mGnssConfiguration.isActiveSimEmergencySuplEnabled()) {
                return;
            }
            com.android.server.location.gnss.GnssLocationProvider.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.location.gnss.GnssLocationProvider$1$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onEmergencyCallStart$0(subId);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onEmergencyCallStart$0(int subId) {
            com.android.server.location.gnss.GnssLocationProvider.this.mGnssConfiguration.reloadGpsProperties(com.android.server.location.gnss.GnssLocationProvider.this.mNIHandler.getInEmergency(), subId);
        }

        public void onEmergencyCallEnd() {
            if (!com.android.server.location.gnss.GnssLocationProvider.this.mGnssConfiguration.isActiveSimEmergencySuplEnabled()) {
                return;
            }
            com.android.server.location.gnss.GnssLocationProvider.this.mHandler.postDelayed(new java.lang.Runnable() { // from class: com.android.server.location.gnss.GnssLocationProvider$1$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onEmergencyCallEnd$1();
                }
            }, java.util.concurrent.TimeUnit.SECONDS.toMillis(com.android.server.location.gnss.GnssLocationProvider.this.mGnssConfiguration.getEsExtensionSec()));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onEmergencyCallEnd$1() {
            com.android.server.location.gnss.GnssLocationProvider.this.mGnssConfiguration.reloadGpsProperties(false, android.telephony.SubscriptionManager.getDefaultDataSubscriptionId());
        }
    }

    public synchronized void onSystemReady() {
        this.mContext.registerReceiverAsUser(new android.content.BroadcastReceiver() { // from class: com.android.server.location.gnss.GnssLocationProvider.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context, android.content.Intent intent) {
                if (getSendingUserId() == -1) {
                    com.android.server.location.gnss.GnssLocationProvider.this.mShutdown = true;
                    com.android.server.location.gnss.GnssLocationProvider.this.updateEnabled();
                }
            }
        }, android.os.UserHandle.ALL, new android.content.IntentFilter("android.intent.action.ACTION_SHUTDOWN"), null, this.mHandler);
        this.mContext.getContentResolver().registerContentObserver(android.provider.Settings.Secure.getUriFor("location_mode"), true, new android.database.ContentObserver(this.mHandler) { // from class: com.android.server.location.gnss.GnssLocationProvider.3
            @Override // android.database.ContentObserver
            public void onChange(boolean selfChange) {
                com.android.server.location.gnss.GnssLocationProvider.this.updateEnabled();
            }
        }, -1);
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.location.gnss.GnssLocationProvider$$ExternalSyntheticLambda11
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.handleInitialize();
            }
        });
        android.os.Handler handler = this.mHandler;
        final com.android.server.location.gnss.GnssSatelliteBlocklistHelper gnssSatelliteBlocklistHelper = this.mGnssSatelliteBlocklistHelper;
        java.util.Objects.requireNonNull(gnssSatelliteBlocklistHelper);
        handler.post(new java.lang.Runnable() { // from class: com.android.server.location.gnss.GnssLocationProvider$$ExternalSyntheticLambda12
            @Override // java.lang.Runnable
            public final void run() {
                gnssSatelliteBlocklistHelper.updateSatelliteBlocklist();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleInitialize() {
        if (this.mGnssNative.isGnssVisibilityControlSupported()) {
            this.mGnssVisibilityControl = new com.android.server.location.gnss.GnssVisibilityControl(this.mContext, this.mHandler.getLooper(), this.mNIHandler);
        }
        reloadGpsProperties();
        android.content.IntentFilter intentFilter = new android.content.IntentFilter();
        intentFilter.addAction("android.telephony.action.CARRIER_CONFIG_CHANGED");
        intentFilter.addAction("android.intent.action.ACTION_DEFAULT_DATA_SUBSCRIPTION_CHANGED");
        this.mContext.registerReceiver(this.mIntentReceiver, intentFilter, null, this.mHandler);
        if (this.mNetworkConnectivityHandler.isNativeAgpsRilSupported() && this.mGnssConfiguration.isNiSuplMessageInjectionEnabled()) {
            android.content.IntentFilter intentFilter2 = new android.content.IntentFilter();
            intentFilter2.addAction("android.provider.Telephony.WAP_PUSH_RECEIVED");
            try {
                intentFilter2.addDataType("application/vnd.omaloc-supl-init");
            } catch (android.content.IntentFilter.MalformedMimeTypeException e) {
                android.util.Log.w(TAG, "Malformed SUPL init mime type");
            }
            this.mContext.registerReceiver(this.mIntentReceiver, intentFilter2, null, this.mHandler);
            android.content.IntentFilter intentFilter3 = new android.content.IntentFilter();
            intentFilter3.addAction("android.intent.action.DATA_SMS_RECEIVED");
            intentFilter3.addDataScheme(com.android.server.am.IOplusSceneManager.APP_SCENE_DEFAULT_SMS);
            intentFilter3.addDataAuthority("localhost", "7275");
            this.mContext.registerReceiver(this.mIntentReceiver, intentFilter3, null, this.mHandler);
        }
        this.mNetworkConnectivityHandler.registerNetworkCallbacks();
        android.location.LocationManager locationManager = (android.location.LocationManager) java.util.Objects.requireNonNull((android.location.LocationManager) this.mContext.getSystemService(android.location.LocationManager.class));
        if (locationManager.getAllProviders().contains("network")) {
            locationManager.requestLocationUpdates("network", new android.location.LocationRequest.Builder(Long.MAX_VALUE).setMinUpdateIntervalMillis(0L).setHiddenFromAppOps(true).build(), com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, new com.android.server.location.gnss.GnssLocationProvider$$ExternalSyntheticLambda20(this));
            this.mHasNetworkLocationListenerRegistered = true;
            android.util.Log.d(TAG, "register network location listener success");
        }
        updateEnabled();
        synchronized (this.mLock) {
            this.mInitialized = true;
        }
        this.mOplusLbsClass = (com.android.server.location.interfaces.IOplusLBSMainClass) com.android.server.location.common.OplusLbsFactory.getInstance().getFeature(com.android.server.location.interfaces.IOplusLBSMainClass.DEFAULT, this.mContext);
        if (this.mOplusLbsClass != null) {
            this.mOplusLbsClass.onGnssLocationProviderInit(this.mContext, this);
            this.mPreciseLocationSupported = this.mOplusLbsClass.isPreciseLocationSupported();
            this.mOplusLbsClass.registerLbsConfigListener(new com.android.server.location.interfaces.IOplusConfigListener() { // from class: com.android.server.location.gnss.GnssLocationProvider.4
                @Override // com.android.server.location.interfaces.IOplusConfigListener
                public void onDebugLevelChanged(int level) {
                    com.android.server.location.gnss.GnssLocationProvider.KEYLOG = level >= 2;
                    com.android.server.location.gnss.GnssLocationProvider.DEBUG = level >= 3;
                    com.android.server.location.gnss.GnssLocationProvider.VERBOSE = level >= 5;
                    android.util.Log.i(com.android.server.location.gnss.GnssLocationProvider.TAG, "onDebugLevelChanged, level: " + level + ", K: " + com.android.server.location.gnss.GnssLocationProvider.KEYLOG + ", D: " + com.android.server.location.gnss.GnssLocationProvider.DEBUG + ", V:" + com.android.server.location.gnss.GnssLocationProvider.VERBOSE);
                }

                @Override // com.android.server.location.interfaces.IOplusConfigListener
                public void onPhysicalDisNetChanged(boolean disNet) {
                    com.android.server.location.gnss.GnssLocationProvider.this.mForceSleep = disNet;
                    android.util.Log.i(com.android.server.location.gnss.GnssLocationProvider.TAG, "oplus gps provider setForceSleep " + com.android.server.location.gnss.GnssLocationProvider.this.mForceSleep);
                }
            });
        }
        this.mGnssLPSocExt.onGnssLocationProviderInitialize();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void injectSuplInit(android.content.Intent intent) {
        if (!isNfwLocationAccessAllowed()) {
            android.util.Log.w(TAG, "Reject SUPL INIT as no NFW location access");
            return;
        }
        int slotIndex = intent.getIntExtra("android.telephony.extra.SLOT_INDEX", -1);
        if (slotIndex == -1) {
            android.util.Log.e(TAG, "Invalid slot index");
            return;
        }
        java.lang.String action = intent.getAction();
        if (action.equals("android.intent.action.DATA_SMS_RECEIVED")) {
            android.telephony.SmsMessage[] messages = android.provider.Telephony.Sms.Intents.getMessagesFromIntent(intent);
            if (messages == null) {
                android.util.Log.e(TAG, "Message does not exist in the intent");
                return;
            }
            for (android.telephony.SmsMessage message : messages) {
                byte[] suplInit = message.getUserData();
                injectSuplInit(suplInit, slotIndex);
            }
            return;
        }
        if (action.equals("android.provider.Telephony.WAP_PUSH_RECEIVED")) {
            byte[] suplInit2 = intent.getByteArrayExtra("data");
            injectSuplInit(suplInit2, slotIndex);
        }
    }

    private void injectSuplInit(byte[] suplInit, int slotIndex) {
        if (suplInit != null) {
            if (DEBUG) {
                android.util.Log.d(TAG, "suplInit = " + com.android.internal.util.HexDump.toHexString(suplInit) + " slotIndex = " + slotIndex);
            }
            this.mGnssNative.injectNiSuplMessageData(suplInit, suplInit.length, slotIndex);
        }
    }

    private boolean isNfwLocationAccessAllowed() {
        if (this.mGnssNative.isInEmergencySession()) {
            return true;
        }
        return this.mGnssVisibilityControl != null && this.mGnssVisibilityControl.hasLocationPermissionEnabledProxyApps();
    }

    @Override // com.android.server.location.gnss.NetworkTimeHelper.InjectTimeCallback
    public void injectTime(long unixEpochTimeMillis, long elapsedRealtimeMillis, int uncertaintyMillis) {
        this.mGnssNative.injectTime(unixEpochTimeMillis, elapsedRealtimeMillis, uncertaintyMillis);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onNetworkAvailable() {
        this.mNetworkTimeHelper.onNetworkAvailable();
        if (this.mSupportsPsds) {
            synchronized (this.mLock) {
                java.util.Iterator<java.lang.Integer> it = this.mPendingDownloadPsdsTypes.iterator();
                while (it.hasNext()) {
                    final int psdsType = it.next().intValue();
                    postWithWakeLockHeld(new java.lang.Runnable() { // from class: com.android.server.location.gnss.GnssLocationProvider$$ExternalSyntheticLambda19
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$onNetworkAvailable$1(psdsType);
                        }
                    });
                }
                this.mPendingDownloadPsdsTypes.clear();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: handleRequestLocation, reason: merged with bridge method [inline-methods] */
    public void lambda$onRequestLocation$16(boolean independentFromGnss, boolean isUserEmergency) {
        java.lang.String provider;
        android.location.LocationListener locationListener;
        if (isRequestLocationRateLimited()) {
            if (DEBUG) {
                android.util.Log.d(TAG, "RequestLocation is denied due to too frequent requests.");
                return;
            }
            return;
        }
        android.content.ContentResolver resolver = this.mContext.getContentResolver();
        long durationMillis = android.provider.Settings.Global.getLong(resolver, "gnss_hal_location_request_duration_millis", 10000L);
        if (durationMillis == 0) {
            android.util.Log.i(TAG, "GNSS HAL location request is disabled by Settings.");
            return;
        }
        android.location.LocationManager locationManager = (android.location.LocationManager) this.mContext.getSystemService("location");
        android.location.LocationRequest.Builder locationRequest = new android.location.LocationRequest.Builder(1000L).setMaxUpdates(1);
        if (independentFromGnss) {
            provider = "network";
            locationListener = new android.location.LocationListener() { // from class: com.android.server.location.gnss.GnssLocationProvider$$ExternalSyntheticLambda14
                @Override // android.location.LocationListener
                public final void onLocationChanged(android.location.Location location) {
                    com.android.server.location.gnss.GnssLocationProvider.lambda$handleRequestLocation$2(location);
                }
            };
            locationRequest.setQuality(104);
            this.mGnssLPSocExt.onRequestLocation(this.mGnssNative);
        } else {
            provider = "fused";
            locationListener = new android.location.LocationListener() { // from class: com.android.server.location.gnss.GnssLocationProvider$$ExternalSyntheticLambda15
                @Override // android.location.LocationListener
                public final void onLocationChanged(android.location.Location location) {
                    this.f$0.injectBestLocation(location);
                }
            };
            locationRequest.setQuality(100);
        }
        if (this.mNIHandler.getInEmergency()) {
            com.android.server.location.gnss.GnssConfiguration.HalInterfaceVersion halVersion = this.mGnssConfiguration.getHalInterfaceVersion();
            if (isUserEmergency || halVersion.mMajor < 2) {
                locationRequest.setLocationSettingsIgnored(true);
                durationMillis *= 3;
            }
        }
        locationRequest.setDurationMillis(durationMillis);
        android.util.Log.i(TAG, java.lang.String.format("GNSS HAL Requesting location updates from %s provider for %d millis.", provider, java.lang.Long.valueOf(durationMillis)));
        if (locationManager.getProvider(provider) != null) {
            locationManager.requestLocationUpdates(provider, locationRequest.build(), com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, locationListener);
        }
    }

    static /* synthetic */ void lambda$handleRequestLocation$2(android.location.Location location) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void injectBestLocation(android.location.Location location) {
        if (DEBUG) {
            android.util.Log.d(TAG, "injectBestLocation: " + location);
        }
        if (location.isMock()) {
            return;
        }
        this.mGnssNative.injectBestLocation(location);
    }

    private boolean isRequestLocationRateLimited() {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: handleDownloadPsdsData, reason: merged with bridge method [inline-methods] and merged with bridge method [inline-methods] and merged with bridge method [inline-methods] and merged with bridge method [inline-methods] */
    public void lambda$onRequestPsdsDownload$15(final int psdsType) {
        if (!this.mSupportsPsds) {
            android.util.Log.d(TAG, "handleDownloadPsdsData() called when PSDS not supported");
            return;
        }
        if (!this.mNetworkConnectivityHandler.isDataNetworkConnected()) {
            synchronized (this.mLock) {
                this.mPendingDownloadPsdsTypes.add(java.lang.Integer.valueOf(psdsType));
            }
            return;
        }
        synchronized (this.mLock) {
            if (this.mDownloadInProgressPsdsTypes.contains(java.lang.Integer.valueOf(psdsType))) {
                if (DEBUG) {
                    android.util.Log.d(TAG, "PSDS type " + psdsType + " download in progress. Ignore the request.");
                }
            } else {
                this.mDownloadPsdsWakeLock.acquire(60000L);
                this.mDownloadInProgressPsdsTypes.add(java.lang.Integer.valueOf(psdsType));
                android.util.Log.i(TAG, "WakeLock acquired by handleDownloadPsdsData()");
                java.util.concurrent.Executors.newSingleThreadExecutor().execute(new java.lang.Runnable() { // from class: com.android.server.location.gnss.GnssLocationProvider$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$handleDownloadPsdsData$6(psdsType);
                    }
                });
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleDownloadPsdsData$6(final int psdsType) {
        long backoffMillis;
        com.android.server.location.gnss.GnssPsdsDownloader psdsDownloader = new com.android.server.location.gnss.GnssPsdsDownloader(this.mGnssConfiguration.getProperties());
        final byte[] data = psdsDownloader.downloadPsdsData(psdsType);
        if (data != null) {
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.location.gnss.GnssLocationProvider$$ExternalSyntheticLambda16
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$handleDownloadPsdsData$3(psdsType, data);
                }
            });
            android.content.pm.PackageManager pm = this.mContext.getPackageManager();
            if (pm != null && pm.hasSystemFeature("android.hardware.type.watch") && psdsType == 1 && this.mGnssConfiguration.isPsdsPeriodicDownloadEnabled()) {
                if (DEBUG) {
                    android.util.Log.d(TAG, "scheduling next long term Psds download");
                }
                this.mHandler.removeCallbacksAndMessages(this.mPsdsPeriodicDownloadToken);
                this.mHandler.postDelayed(new java.lang.Runnable() { // from class: com.android.server.location.gnss.GnssLocationProvider$$ExternalSyntheticLambda17
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$handleDownloadPsdsData$4(psdsType);
                    }
                }, this.mPsdsPeriodicDownloadToken, 86400000L);
            }
        } else {
            synchronized (this.mLock) {
                backoffMillis = this.mPsdsBackOff.nextBackoffMillis();
            }
            this.mHandler.postDelayed(new java.lang.Runnable() { // from class: com.android.server.location.gnss.GnssLocationProvider$$ExternalSyntheticLambda18
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$handleDownloadPsdsData$5(psdsType);
                }
            }, backoffMillis);
        }
        synchronized (this.mLock) {
            if (this.mDownloadPsdsWakeLock.isHeld()) {
                this.mDownloadPsdsWakeLock.release();
                if (DEBUG) {
                    android.util.Log.d(TAG, "WakeLock released by handleDownloadPsdsData()");
                }
            } else {
                android.util.Log.e(TAG, "WakeLock expired before release in handleDownloadPsdsData()");
            }
            this.mDownloadInProgressPsdsTypes.remove(java.lang.Integer.valueOf(psdsType));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleDownloadPsdsData$3(int psdsType, byte[] data) {
        com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.GNSS_PSDS_DOWNLOAD_REPORTED, psdsType);
        if (DEBUG) {
            android.util.Log.d(TAG, "calling native_inject_psds_data");
        }
        this.mGnssNative.injectPsdsData(data, data.length, psdsType);
        synchronized (this.mLock) {
            this.mPsdsBackOff.reset();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void injectLocation(android.location.Location location) {
        if (!location.isMock()) {
            this.mGnssNative.injectLocation(location);
        }
    }

    private void setSuplHostPort() {
        this.mSuplServerHost = this.mGnssConfiguration.getSuplHost();
        this.mSuplServerPort = this.mGnssConfiguration.getSuplPort(0);
        if (this.mSuplServerHost != null && this.mSuplServerPort > 0 && this.mSuplServerPort <= 65535) {
            this.mGnssNative.setAgpsServer(1, this.mSuplServerHost, this.mSuplServerPort);
        }
    }

    private int getSuplMode(boolean agpsEnabled) {
        int suplMode;
        if (!agpsEnabled || (suplMode = this.mGnssConfiguration.getSuplMode(0)) == 0 || !this.mGnssNative.getCapabilities().hasMsb() || (suplMode & 1) == 0) {
            return 0;
        }
        return 1;
    }

    private void setGpsEnabled(boolean enabled) {
        synchronized (this.mLock) {
            this.mGpsEnabled = enabled;
        }
    }

    public void setAutomotiveGnssSuspended(boolean suspended) {
        synchronized (this.mLock) {
            this.mAutomotiveSuspend = suspended;
        }
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.location.gnss.GnssLocationProvider$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.updateEnabled();
            }
        });
    }

    public boolean isAutomotiveGnssSuspended() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mAutomotiveSuspend && !this.mGpsEnabled;
        }
        return z;
    }

    private void handleEnable() {
        if (DEBUG) {
            android.util.Log.d(TAG, "handleEnable");
        }
        boolean inited = this.mGnssNative.init();
        boolean z = false;
        if (inited) {
            setGpsEnabled(true);
            this.mSupportsPsds = this.mGnssNative.isPsdsSupported();
            if (this.mSuplServerHost != null) {
                this.mGnssNative.setAgpsServer(1, this.mSuplServerHost, this.mSuplServerPort);
            }
            if (this.mC2KServerHost != null) {
                this.mGnssNative.setAgpsServer(2, this.mC2KServerHost, this.mC2KServerPort);
            }
            if (this.mGnssNative.initBatching() && this.mGnssNative.getBatchSize() > 1) {
                z = true;
            }
            this.mBatchingEnabled = z;
            if (this.mGnssVisibilityControl != null) {
                this.mGnssVisibilityControl.onGpsEnabledChanged(true);
                return;
            }
            return;
        }
        setGpsEnabled(false);
        android.util.Log.w(TAG, "Failed to enable location provider");
    }

    private void handleDisable() {
        if (DEBUG) {
            android.util.Log.d(TAG, "handleDisable");
        }
        if (this.mOplusLbsClass != null) {
            this.mOplusLbsClass.stopController();
        }
        setGpsEnabled(false);
        if (this.mOplusLbsClass != null) {
            this.mOplusLbsClass.forceNotifyEmptyWorksource();
        }
        updateClientUids(new android.os.WorkSource());
        stopNavigating();
        stopBatching();
        if (this.mGnssVisibilityControl != null) {
            this.mGnssVisibilityControl.onGpsEnabledChanged(false);
        }
        this.mGnssNative.cleanupBatching();
        this.mGnssNative.cleanup();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateEnabled() {
        boolean enabled;
        boolean enabled2 = false;
        android.location.LocationManager locationManager = (android.location.LocationManager) this.mContext.getSystemService(android.location.LocationManager.class);
        java.util.Set<android.os.UserHandle> visibleUserHandles = ((android.os.UserManager) this.mContext.getSystemService(android.os.UserManager.class)).getVisibleUsers();
        for (android.os.UserHandle visibleUserHandle : visibleUserHandles) {
            enabled2 |= locationManager.isLocationEnabledForUser(visibleUserHandle);
        }
        boolean enabled3 = enabled2 | (this.mProviderRequest != null && this.mProviderRequest.isActive() && this.mProviderRequest.isBypass());
        synchronized (this.mLock) {
            enabled = enabled3 & (this.mAutomotiveSuspend ? false : true);
        }
        boolean enabled4 = enabled & (!this.mShutdown);
        if (enabled4 == isGpsEnabled()) {
            return;
        }
        if (enabled4) {
            handleEnable();
        } else {
            handleDisable();
        }
    }

    private boolean isGpsEnabled() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mGpsEnabled;
        }
        return z;
    }

    public int getBatchSize() {
        return this.mGnssNative.getBatchSize();
    }

    @Override // com.android.server.location.provider.AbstractLocationProvider
    protected void onFlush(java.lang.Runnable listener) {
        boolean added = false;
        synchronized (this.mLock) {
            if (this.mBatchingEnabled) {
                added = this.mFlushListeners.add(listener);
            }
        }
        if (!added) {
            listener.run();
        } else {
            this.mGnssNative.flushBatch();
        }
    }

    @Override // com.android.server.location.provider.AbstractLocationProvider
    public void onSetRequest(android.location.provider.ProviderRequest request) {
        this.mProviderRequest = request;
        updateEnabled();
        updateRequirements();
    }

    private void updateRequirements() {
        if (this.mProviderRequest == null || this.mProviderRequest.getWorkSource() == null) {
            return;
        }
        if (KEYLOG) {
            android.util.Log.i(TAG, "setRequest " + this.mProviderRequest);
        }
        if (this.mProviderRequest.isActive() && isGpsEnabled()) {
            updateClientUids(this.mProviderRequest.getWorkSource());
            if (this.mProviderRequest.getIntervalMillis() <= 2147483647L) {
                this.mFixInterval = (int) this.mProviderRequest.getIntervalMillis();
            } else {
                android.util.Log.w(TAG, "interval overflow: " + this.mProviderRequest.getIntervalMillis());
                this.mFixInterval = Integer.MAX_VALUE;
            }
            int batchIntervalMs = java.lang.Math.max(this.mFixInterval, 1000);
            long batchLengthMs = java.lang.Math.min(this.mProviderRequest.getMaxUpdateDelayMillis(), 86400000L);
            if (this.mBatchingEnabled && batchLengthMs / 2 >= batchIntervalMs) {
                stopNavigating();
                this.mFixInterval = batchIntervalMs;
                startBatching(batchLengthMs);
                return;
            }
            stopBatching();
            if (this.mStarted && this.mGnssNative.getCapabilities().hasScheduling()) {
                if (!setPositionMode(this.mPositionMode, 0, this.mFixInterval, this.mProviderRequest.isLowPower())) {
                    android.util.Log.e(TAG, "set_position_mode failed in updateRequirements");
                    return;
                }
                return;
            } else {
                if (!this.mStarted) {
                    if (this.mOplusLbsClass != null && !this.mOplusLbsClass.resistStartGps()) {
                        this.mOplusLbsClass.setUp();
                        startNavigating();
                        return;
                    }
                    return;
                }
                this.mAlarmManager.cancel(this.mTimeoutListener);
                if (this.mFixInterval >= 60000) {
                    this.mAlarmManager.set(2, android.os.SystemClock.elapsedRealtime() + 60000, TAG, this.mTimeoutListener, this.mHandler);
                    return;
                }
                return;
            }
        }
        if (this.mOplusLbsClass != null) {
            this.mOplusLbsClass.stopController();
            this.mOplusLbsClass.forceNotifyEmptyWorksource();
        }
        updateClientUids(new android.os.WorkSource());
        stopNavigating();
        stopBatching();
    }

    private boolean setPositionMode(int mode, int recurrence, int minInterval, boolean lowPowerMode) {
        com.android.server.location.gnss.GnssPositionMode positionMode = new com.android.server.location.gnss.GnssPositionMode(mode, recurrence, minInterval, 0, 0, lowPowerMode);
        if (this.mLastPositionMode != null && this.mLastPositionMode.equals(positionMode)) {
            return true;
        }
        boolean result = this.mGnssNative.setPositionMode(mode, recurrence, minInterval, 0, 0, lowPowerMode);
        if (result) {
            this.mLastPositionMode = positionMode;
        } else {
            this.mLastPositionMode = null;
        }
        return result;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateClientUids(android.os.WorkSource source) {
        if (source.equals(this.mClientSource)) {
            return;
        }
        if (this.mOplusLbsClass != null) {
            this.mOplusLbsClass.storeWorkSource(source);
            if (this.mOplusLbsClass.isEngineOffByStrategy()) {
                return;
            }
        }
        try {
            this.mBatteryStats.noteGpsChanged(this.mClientSource, source);
        } catch (android.os.RemoteException e) {
            android.util.Log.w(TAG, "RemoteException", e);
        }
        java.util.List<android.os.WorkSource.WorkChain>[] diffs = android.os.WorkSource.diffChains(this.mClientSource, source);
        if (diffs != null) {
            java.util.List<android.os.WorkSource.WorkChain> newChains = diffs[0];
            java.util.List<android.os.WorkSource.WorkChain> goneChains = diffs[1];
            if (newChains != null) {
                for (android.os.WorkSource.WorkChain newChain : newChains) {
                    this.mAppOps.startOpNoThrow(2, newChain.getAttributionUid(), newChain.getAttributionTag());
                }
            }
            if (goneChains != null) {
                for (android.os.WorkSource.WorkChain goneChain : goneChains) {
                    this.mAppOps.finishOp(2, goneChain.getAttributionUid(), goneChain.getAttributionTag());
                }
            }
            this.mClientSource.transferWorkChains(source);
        }
        android.os.WorkSource[] changes = this.mClientSource.setReturningDiffs(source);
        if (changes != null) {
            android.os.WorkSource newWork = changes[0];
            android.os.WorkSource goneWork = changes[1];
            if (newWork != null) {
                for (int i = 0; i < newWork.size(); i++) {
                    this.mGnssLPExt.updateGpsChanged(newWork.getUid(i), newWork.getPackageName(i), true);
                    this.mAppOps.startOpNoThrow(2, newWork.getUid(i), newWork.getPackageName(i));
                }
            }
            if (goneWork != null) {
                for (int i2 = 0; i2 < goneWork.size(); i2++) {
                    this.mGnssLPExt.updateGpsChanged(goneWork.getUid(i2), goneWork.getPackageName(i2), false);
                    this.mAppOps.finishOp(2, goneWork.getUid(i2), goneWork.getPackageName(i2));
                }
            }
        }
    }

    @Override // com.android.server.location.provider.AbstractLocationProvider
    public void onExtraCommand(int uid, int pid, java.lang.String command, android.os.Bundle extras) {
        if ("delete_aiding_data".equals(command)) {
            deleteAidingData(extras);
            return;
        }
        if ("force_time_injection".equals(command)) {
            demandUtcTimeInjection();
            return;
        }
        if ("force_psds_injection".equals(command)) {
            if (this.mSupportsPsds) {
                postWithWakeLockHeld(new java.lang.Runnable() { // from class: com.android.server.location.gnss.GnssLocationProvider$$ExternalSyntheticLambda5
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onExtraCommand$7();
                    }
                });
            }
        } else if ("request_power_stats".equals(command)) {
            this.mGnssNative.requestPowerStats(new com.android.server.SystemServerInitThreadPool$$ExternalSyntheticLambda0(), new com.android.server.location.gnss.hal.GnssNative.PowerStatsCallback() { // from class: com.android.server.location.gnss.GnssLocationProvider$$ExternalSyntheticLambda6
                @Override // com.android.server.location.gnss.hal.GnssNative.PowerStatsCallback
                public final void onReportPowerStats(com.android.server.location.gnss.GnssPowerStats gnssPowerStats) {
                    com.android.server.location.gnss.GnssLocationProvider.lambda$onExtraCommand$8(gnssPowerStats);
                }
            });
        } else {
            android.util.Log.w(TAG, "sendExtraCommand: unknown command " + command);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onExtraCommand$7() {
        lambda$onRequestPsdsDownload$15(1);
    }

    static /* synthetic */ void lambda$onExtraCommand$8(com.android.server.location.gnss.GnssPowerStats powerStats) {
    }

    private void deleteAidingData(android.os.Bundle extras) {
        int flags;
        if (extras == null) {
            flags = 65535;
        } else {
            flags = extras.getBoolean("ephemeris") ? 0 | 1 : 0;
            if (extras.getBoolean("almanac")) {
                flags |= 2;
            }
            if (extras.getBoolean("position")) {
                flags |= 4;
            }
            if (extras.getBoolean("time")) {
                flags |= 8;
            }
            if (extras.getBoolean("iono")) {
                flags |= 16;
            }
            if (extras.getBoolean("utc")) {
                flags |= 32;
            }
            if (extras.getBoolean("health")) {
                flags |= 64;
            }
            if (extras.getBoolean("svdir")) {
                flags |= 128;
            }
            if (extras.getBoolean("svsteer")) {
                flags |= 256;
            }
            if (extras.getBoolean("sadata")) {
                flags |= 512;
            }
            if (extras.getBoolean("rti")) {
                flags |= 1024;
            }
            if (extras.getBoolean("celldb-info")) {
                flags |= 32768;
            }
            if (extras.getBoolean("all")) {
                flags |= 65535;
            }
        }
        int flags2 = this.mGnssLPSocExt.onDeleteAidingData(extras, flags);
        if (flags2 != 0) {
            this.mGnssNative.deleteAidingData(flags2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startNavigating() {
        java.lang.String mode;
        if (!this.mStarted) {
            if (DEBUG) {
                android.util.Log.d(TAG, "startNavigating");
            }
            if (this.mOplusLbsClass != null) {
                this.mOplusLbsClass.refreshRequestTimer();
            }
            this.mTimeToFirstFix = 0;
            this.mLastFixTime = 0L;
            boolean agpsEnabled = true;
            setStarted(true);
            this.mPositionMode = 0;
            if (android.provider.Settings.Global.getInt(this.mContext.getContentResolver(), "assisted_gps_enabled", 1) == 0) {
                agpsEnabled = false;
            }
            if (this.mOplusLbsClass != null) {
                agpsEnabled = this.mOplusLbsClass.isForceAgpsEnabled(agpsEnabled);
            }
            this.mPositionMode = getSuplMode(agpsEnabled);
            if (this.mOplusLbsClass != null) {
                this.mPositionMode = this.mOplusLbsClass.customizePositionMode(this.mPositionMode);
            }
            if (this.mOplusLbsClass != null) {
                this.mOplusLbsClass.setEngMode(this.mPositionMode);
            }
            if (DEBUG) {
                switch (this.mPositionMode) {
                    case 0:
                        mode = "standalone";
                        break;
                    case 1:
                        mode = "MS_BASED";
                        break;
                    case 2:
                        mode = "MS_ASSISTED";
                        break;
                    default:
                        mode = "unknown";
                        break;
                }
                android.util.Log.d(TAG, "setting position_mode to " + mode);
            }
            int interval = this.mGnssNative.getCapabilities().hasScheduling() ? this.mFixInterval : 1000;
            if (this.mOplusLbsClass != null) {
                this.mOplusLbsClass.setEngInterval(interval);
            }
            if (!setPositionMode(this.mPositionMode, 0, interval, this.mProviderRequest.isLowPower())) {
                setStarted(false);
                android.util.Log.e(TAG, "set_position_mode failed in startNavigating()");
                return;
            }
            if (!this.mGnssNative.start()) {
                setStarted(false);
                android.util.Log.e(TAG, "native_start failed in startNavigating()");
                return;
            }
            if (this.mOplusLbsClass != null) {
                this.mOplusLbsClass.onStartNavigating(interval);
            }
            this.mLocationExtras.reset();
            this.mFixRequestTime = android.os.SystemClock.elapsedRealtime();
            if (!this.mGnssNative.getCapabilities().hasScheduling() && this.mFixInterval >= 60000) {
                this.mAlarmManager.set(2, android.os.SystemClock.elapsedRealtime() + 60000, TAG, this.mTimeoutListener, this.mHandler);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopNavigating() {
        if (DEBUG) {
            android.util.Log.d(TAG, "stopNavigating");
        }
        if (this.mStarted) {
            setStarted(false);
            this.mGnssNative.stop();
            this.mLastFixTime = 0L;
            this.mLastPositionMode = null;
            this.mLocationExtras.reset();
            if (this.mOplusLbsClass != null) {
                this.mOplusLbsClass.onStopNavigating();
            }
        }
        this.mAlarmManager.cancel(this.mTimeoutListener);
        this.mAlarmManager.cancel(this.mWakeupListener);
    }

    private void startBatching(final long batchLengthMs) {
        long batchSize = batchLengthMs / ((long) this.mFixInterval);
        if (DEBUG) {
            android.util.Log.d(TAG, "startBatching " + this.mFixInterval + " " + batchLengthMs);
        }
        if (this.mGnssNative.startBatch(java.util.concurrent.TimeUnit.MILLISECONDS.toNanos(this.mFixInterval), 0.0f, true)) {
            this.mBatchingStarted = true;
            if (batchSize < getBatchSize()) {
                this.mBatchingAlarm = new android.app.AlarmManager.OnAlarmListener() { // from class: com.android.server.location.gnss.GnssLocationProvider$$ExternalSyntheticLambda10
                    @Override // android.app.AlarmManager.OnAlarmListener
                    public final void onAlarm() {
                        this.f$0.lambda$startBatching$9(batchLengthMs);
                    }
                };
                this.mAlarmManager.setExact(2, android.os.SystemClock.elapsedRealtime() + batchLengthMs, TAG, this.mBatchingAlarm, com.android.server.FgThread.getHandler());
                return;
            }
            return;
        }
        android.util.Log.e(TAG, "native_start_batch failed in startBatching()");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startBatching$9(long batchLengthMs) {
        boolean flush = false;
        synchronized (this.mLock) {
            if (this.mBatchingAlarm != null) {
                flush = true;
                this.mAlarmManager.setExact(2, android.os.SystemClock.elapsedRealtime() + batchLengthMs, TAG, this.mBatchingAlarm, com.android.server.FgThread.getHandler());
            }
        }
        if (flush) {
            this.mGnssNative.flushBatch();
        }
    }

    private void stopBatching() {
        if (DEBUG) {
            android.util.Log.d(TAG, "stopBatching");
        }
        if (this.mBatchingStarted) {
            if (this.mBatchingAlarm != null) {
                this.mAlarmManager.cancel(this.mBatchingAlarm);
                this.mBatchingAlarm = null;
            }
            this.mGnssNative.flushBatch();
            this.mGnssNative.stopBatch();
            this.mBatchingStarted = false;
        }
    }

    private void setStarted(boolean started) {
        if (this.mStarted != started) {
            this.mStarted = started;
            this.mStartedChangedElapsedRealtime = android.os.SystemClock.elapsedRealtime();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hibernate() {
        stopNavigating();
        long now = android.os.SystemClock.elapsedRealtime();
        this.mAlarmManager.set(2, now + ((long) this.mFixInterval), TAG, this.mWakeupListener, this.mHandler);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: handleReportLocation, reason: merged with bridge method [inline-methods] */
    public void lambda$onReportLocation$13(boolean hasLatLong, android.location.Location location) {
        if (KEYLOG) {
            android.util.Log.i(TAG, "reportLocation " + location.toString());
        }
        if (this.mPreciseLocationSupported && hasLatLong) {
            this.mOplusLbsClass.reduceAccuracyOfLocation(location);
        }
        location.setExtras(this.mLocationExtras.getBundle());
        android.os.Bundle extra = location.getExtras();
        extra.putInt("dr", this.mDrStatus);
        extra.putFloat("eb", this.mEBearing);
        location.setExtras(extra);
        try {
            reportLocation(android.location.LocationResult.wrap(new android.location.Location[]{location}).validate());
            if (this.mStarted) {
                this.mGnssMetrics.logReceivedLocationStatus(hasLatLong);
                if (hasLatLong) {
                    if (location.hasAccuracy()) {
                        this.mGnssMetrics.logPositionAccuracyMeters(location.getAccuracy());
                    }
                    if (this.mTimeToFirstFix > 0) {
                        int timeBetweenFixes = (int) (android.os.SystemClock.elapsedRealtime() - this.mLastFixTime);
                        this.mGnssMetrics.logMissedReports(this.mFixInterval, timeBetweenFixes);
                    }
                }
            } else {
                long locationAfterStartedFalseMillis = android.os.SystemClock.elapsedRealtime() - this.mStartedChangedElapsedRealtime;
                if (locationAfterStartedFalseMillis > LOCATION_OFF_DELAY_THRESHOLD_WARN_MILLIS) {
                    java.lang.String logMessage = "Unexpected GNSS Location report " + android.util.TimeUtils.formatDuration(locationAfterStartedFalseMillis) + " after location turned off";
                    if (locationAfterStartedFalseMillis > LOCATION_OFF_DELAY_THRESHOLD_ERROR_MILLIS) {
                        android.util.Log.e(TAG, logMessage);
                    } else {
                        android.util.Log.w(TAG, logMessage);
                    }
                }
            }
            this.mLastFixTime = android.os.SystemClock.elapsedRealtime();
            if (this.mTimeToFirstFix == 0 && hasLatLong) {
                this.mTimeToFirstFix = (int) (this.mLastFixTime - this.mFixRequestTime);
                if (DEBUG) {
                    android.util.Log.d(TAG, "TTFF: " + this.mTimeToFirstFix);
                }
                if (this.mStarted) {
                    this.mGnssMetrics.logTimeToFirstFixMilliSecs(this.mTimeToFirstFix);
                }
            }
            if (this.mOplusLbsClass != null) {
                this.mOplusLbsClass.storeAppSvInfo(this.mLocationExtras.getBundle().getInt("maxCn0"), location.getSpeed());
            }
            if (this.mStarted && !this.mGnssNative.getCapabilities().hasScheduling() && this.mFixInterval < 60000) {
                this.mAlarmManager.cancel(this.mTimeoutListener);
            }
            if (!this.mGnssNative.getCapabilities().hasScheduling() && this.mStarted && this.mFixInterval > 10000) {
                if (DEBUG) {
                    android.util.Log.d(TAG, "got fix, hibernating");
                }
                hibernate();
            }
        } catch (android.location.LocationResult.BadLocationException e) {
            android.util.Log.e(TAG, "Dropping invalid location: " + e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: handleReportSvStatus, reason: merged with bridge method [inline-methods] */
    public void lambda$onReportSvStatus$14(android.location.GnssStatus gnssStatus) {
        this.mGnssMetrics.logCn0(gnssStatus);
        if (KEYLOG) {
            android.util.Log.i(TAG, "SV count: " + gnssStatus.getSatelliteCount());
        }
        java.util.Set<android.util.Pair<java.lang.Integer, java.lang.Integer>> satellites = new java.util.HashSet<>();
        int usedInFixCount = 0;
        int maxCn0 = 0;
        int meanCn0 = 0;
        for (int i = 0; i < gnssStatus.getSatelliteCount(); i++) {
            if (gnssStatus.usedInFix(i)) {
                satellites.add(new android.util.Pair<>(java.lang.Integer.valueOf(gnssStatus.getConstellationType(i)), java.lang.Integer.valueOf(gnssStatus.getSvid(i))));
                usedInFixCount++;
                if (gnssStatus.getCn0DbHz(i) > maxCn0) {
                    maxCn0 = (int) gnssStatus.getCn0DbHz(i);
                }
                meanCn0 = (int) (meanCn0 + gnssStatus.getCn0DbHz(i));
                this.mGnssMetrics.logConstellationType(gnssStatus.getConstellationType(i));
            }
        }
        if (usedInFixCount > 0) {
            meanCn0 /= usedInFixCount;
        }
        this.mLocationExtras.set(satellites.size(), meanCn0, maxCn0);
        this.mGnssLPSocExt.onReportSvStatus(gnssStatus);
        if (this.mOplusLbsClass != null) {
            this.mOplusLbsClass.storeSatellitesInfo(gnssStatus.getSatelliteCount(), usedInFixCount, maxCn0);
            this.mOplusLbsClass.collectSvStatus(gnssStatus);
            this.mOplusLbsClass.receiveSvInfo(gnssStatus);
            this.mOplusLbsClass.onSvStatusChanged(gnssStatus);
        }
        this.mGnssMetrics.logSvStatus(gnssStatus);
    }

    private void restartLocationRequest() {
        if (DEBUG) {
            android.util.Log.d(TAG, "restartLocationRequest");
        }
        setStarted(false);
        updateRequirements();
    }

    private void demandUtcTimeInjection() {
        if (DEBUG) {
            android.util.Log.d(TAG, "demandUtcTimeInjection");
        }
        final com.android.server.location.gnss.NetworkTimeHelper networkTimeHelper = this.mNetworkTimeHelper;
        java.util.Objects.requireNonNull(networkTimeHelper);
        postWithWakeLockHeld(new java.lang.Runnable() { // from class: com.android.server.location.gnss.GnssLocationProvider$$ExternalSyntheticLambda21
            @Override // java.lang.Runnable
            public final void run() {
                networkTimeHelper.demandUtcTimeInjection();
            }
        });
    }

    private static int getCellType(android.telephony.CellInfo ci) {
        if (ci instanceof android.telephony.CellInfoGsm) {
            return 1;
        }
        if (ci instanceof android.telephony.CellInfoWcdma) {
            return 4;
        }
        if (ci instanceof android.telephony.CellInfoLte) {
            return 3;
        }
        if (ci instanceof android.telephony.CellInfoNr) {
            return 6;
        }
        return 0;
    }

    private static long getCidFromCellIdentity(android.telephony.CellIdentity id) {
        if (id == null) {
            return -1L;
        }
        long cid = -1;
        switch (id.getType()) {
            case 1:
                cid = ((android.telephony.CellIdentityGsm) id).getCid();
                break;
            case 3:
                cid = ((android.telephony.CellIdentityLte) id).getCi();
                break;
            case 4:
                cid = ((android.telephony.CellIdentityWcdma) id).getCid();
                break;
            case 6:
                cid = ((android.telephony.CellIdentityNr) id).getNci();
                break;
        }
        if (cid == (id.getType() == 6 ? Long.MAX_VALUE : 2147483647L)) {
            return -1L;
        }
        return cid;
    }

    private void setRefLocation(int type, android.telephony.CellIdentity ci) {
        int pcid;
        int arfcn;
        long cid;
        java.lang.String mcc_str = ci.getMccString();
        java.lang.String mnc_str = ci.getMncString();
        int mcc = mcc_str != null ? java.lang.Integer.parseInt(mcc_str) : Integer.MAX_VALUE;
        int mnc = mnc_str != null ? java.lang.Integer.parseInt(mnc_str) : Integer.MAX_VALUE;
        int lac = Integer.MAX_VALUE;
        int tac = Integer.MAX_VALUE;
        switch (type) {
            case 1:
                android.telephony.CellIdentityGsm cig = (android.telephony.CellIdentityGsm) ci;
                long cid2 = cig.getCid();
                lac = cig.getLac();
                pcid = Integer.MAX_VALUE;
                arfcn = Integer.MAX_VALUE;
                cid = cid2;
                break;
            case 2:
                android.telephony.CellIdentityWcdma ciw = (android.telephony.CellIdentityWcdma) ci;
                long cid3 = ciw.getCid();
                lac = ciw.getLac();
                pcid = Integer.MAX_VALUE;
                arfcn = Integer.MAX_VALUE;
                cid = cid3;
                break;
            case 4:
                android.telephony.CellIdentityLte cil = (android.telephony.CellIdentityLte) ci;
                long cid4 = cil.getCi();
                tac = cil.getTac();
                int pcid2 = cil.getPci();
                pcid = pcid2;
                arfcn = Integer.MAX_VALUE;
                cid = cid4;
                break;
            case 8:
                android.telephony.CellIdentityNr cin = (android.telephony.CellIdentityNr) ci;
                long cid5 = cin.getNci();
                tac = cin.getTac();
                int pcid3 = cin.getPci();
                int arfcn2 = cin.getNrarfcn();
                pcid = pcid3;
                arfcn = arfcn2;
                cid = cid5;
                break;
            default:
                pcid = Integer.MAX_VALUE;
                arfcn = Integer.MAX_VALUE;
                cid = Long.MAX_VALUE;
                break;
        }
        this.mGnssNative.setAgpsReferenceLocationCellId(type, mcc, mnc, lac, cid, tac, pcid, arfcn);
    }

    private void requestRefLocation() {
        android.telephony.TelephonyManager phone = (android.telephony.TelephonyManager) this.mContext.getSystemService(com.android.server.autofill.HintsHelper.AUTOFILL_HINT_PHONE);
        int phoneType = phone.getPhoneType();
        if (phoneType == 1) {
            java.util.List<android.telephony.CellInfo> cil = phone.getAllCellInfo();
            if (cil == null) {
                android.util.Log.e(TAG, "Error getting cell location info.");
                return;
            }
            java.util.HashMap<java.lang.Integer, android.telephony.CellIdentity> cellIdentityMap = new java.util.HashMap<>();
            cil.sort(java.util.Comparator.comparingInt(new java.util.function.ToIntFunction() { // from class: com.android.server.location.gnss.GnssLocationProvider$$ExternalSyntheticLambda23
                @Override // java.util.function.ToIntFunction
                public final int applyAsInt(java.lang.Object obj) {
                    return ((android.telephony.CellInfo) obj).getCellSignalStrength().getAsuLevel();
                }
            }).reversed());
            for (android.telephony.CellInfo ci : cil) {
                int status = ci.getCellConnectionStatus();
                if (ci.isRegistered() || status == 1 || status == 2) {
                    android.telephony.CellIdentity c = ci.getCellIdentity();
                    int t = getCellType(ci);
                    if (getCidFromCellIdentity(c) != -1 && !cellIdentityMap.containsKey(java.lang.Integer.valueOf(t))) {
                        cellIdentityMap.put(java.lang.Integer.valueOf(t), c);
                    }
                }
            }
            if (cellIdentityMap.containsKey(1)) {
                setRefLocation(1, cellIdentityMap.get(1));
                return;
            }
            if (cellIdentityMap.containsKey(4)) {
                setRefLocation(2, cellIdentityMap.get(4));
                return;
            }
            if (cellIdentityMap.containsKey(3)) {
                setRefLocation(4, cellIdentityMap.get(3));
                return;
            } else if (cellIdentityMap.containsKey(6)) {
                setRefLocation(8, cellIdentityMap.get(6));
                return;
            } else {
                android.util.Log.e(TAG, "No available serving cell information.");
                return;
            }
        }
        if (phoneType == 2) {
            android.util.Log.e(TAG, "CDMA not supported.");
        }
    }

    private void postWithWakeLockHeld(final java.lang.Runnable runnable) {
        this.mWakeLock.acquire(30000L);
        if (this.mOplusLbsClass != null) {
            this.mOplusLbsClass.recordHeldWakelock("*location*:GnssLocationProvider");
            if (this.mReportLocation) {
                this.mReportLocation = false;
                this.mOplusLbsClass.recordTaskMark("gnssReportLocation");
            }
        }
        boolean success = this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.location.gnss.GnssLocationProvider$$ExternalSyntheticLambda27
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$postWithWakeLockHeld$11(runnable);
            }
        });
        if (!success) {
            if (this.mOplusLbsClass != null) {
                this.mOplusLbsClass.recordReleaseWakelock("*location*:GnssLocationProvider");
            }
            this.mWakeLock.release();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$postWithWakeLockHeld$11(java.lang.Runnable runnable) {
        try {
            runnable.run();
        } finally {
            if (this.mOplusLbsClass != null) {
                this.mOplusLbsClass.recordReleaseWakelock("*location*:GnssLocationProvider");
            }
            this.mWakeLock.release();
        }
    }

    @Override // com.android.server.location.provider.AbstractLocationProvider
    public void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
        java.lang.String opt;
        boolean dumpAll = false;
        int opti = 0;
        while (true) {
            if (opti >= args.length || (opt = args[opti]) == null || opt.length() <= 0 || opt.charAt(0) != '-') {
                break;
            }
            opti++;
            if ("-a".equals(opt)) {
                dumpAll = true;
                break;
            }
        }
        pw.print("mStarted=" + this.mStarted + "   (changed ");
        android.util.TimeUtils.formatDuration(android.os.SystemClock.elapsedRealtime() - this.mStartedChangedElapsedRealtime, pw);
        pw.println(" ago)");
        pw.println("isInPowerSavingMode=" + this.mOplusLbsClass.getInPowerSaveMode());
        pw.println("mBatchingEnabled=" + this.mBatchingEnabled);
        pw.println("mBatchingStarted=" + this.mBatchingStarted);
        pw.println("mBatchSize=" + getBatchSize());
        pw.println("mFixInterval=" + this.mFixInterval);
        pw.print(this.mGnssMetrics.dumpGnssMetricsAsText());
        if (dumpAll) {
            this.mNetworkTimeHelper.dump(pw);
            pw.println("mSupportsPsds=" + this.mSupportsPsds);
            pw.println("PsdsServerConfigured=" + this.mGnssConfiguration.isLongTermPsdsServerConfigured());
            pw.println("native internal state: ");
            pw.println("  " + this.mGnssNative.getInternalState());
        }
    }

    @Override // com.android.server.location.gnss.hal.GnssNative.BaseCallbacks
    public void onHalRestarted() {
        reloadGpsProperties();
        if (isGpsEnabled()) {
            setGpsEnabled(false);
            updateEnabled();
            restartLocationRequest();
        }
        synchronized (this.mLock) {
            if (this.mInitialized) {
                this.mNetworkConnectivityHandler.unregisterNetworkCallbacks();
                this.mNetworkConnectivityHandler.registerNetworkCallbacks();
            }
        }
    }

    @Override // com.android.server.location.gnss.hal.GnssNative.BaseCallbacks
    public void onCapabilitiesChanged(android.location.GnssCapabilities oldCapabilities, android.location.GnssCapabilities newCapabilities) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.location.gnss.GnssLocationProvider$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onCapabilitiesChanged$12();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCapabilitiesChanged$12() {
        boolean useOnDemandTimeInjection = this.mGnssNative.getCapabilities().hasOnDemandTime();
        this.mNetworkTimeHelper.setPeriodicTimeInjectionMode(useOnDemandTimeInjection);
        if (useOnDemandTimeInjection) {
            demandUtcTimeInjection();
        }
        restartLocationRequest();
    }

    @Override // com.android.server.location.gnss.hal.GnssNative.LocationCallbacks
    public void onReportLocation(final boolean hasLatLong, final android.location.Location location) {
        this.mReportLocation = true;
        postWithWakeLockHeld(new java.lang.Runnable() { // from class: com.android.server.location.gnss.GnssLocationProvider$$ExternalSyntheticLambda9
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onReportLocation$13(hasLatLong, location);
            }
        });
    }

    @Override // com.android.server.location.gnss.hal.GnssNative.LocationCallbacks
    public void onReportLocations(android.location.Location[] locations) {
        int i;
        java.lang.Runnable[] listeners;
        if (DEBUG) {
            android.util.Log.d(TAG, "Location batch of size " + locations.length + " reported");
        }
        if (locations.length > 0) {
            if (locations.length > 1) {
                boolean fixRealtime = false;
                int i2 = locations.length - 2;
                while (true) {
                    if (i2 < 0) {
                        break;
                    }
                    long timeDeltaMs = locations[i2 + 1].getTime() - locations[i2].getTime();
                    long realtimeDeltaMs = locations[i2 + 1].getElapsedRealtimeMillis() - locations[i2].getElapsedRealtimeMillis();
                    if (java.lang.Math.abs(timeDeltaMs - realtimeDeltaMs) > 500) {
                        fixRealtime = true;
                        break;
                    }
                    i2--;
                }
                if (fixRealtime) {
                    java.util.Arrays.sort(locations, java.util.Comparator.comparingLong(new java.util.function.ToLongFunction() { // from class: com.android.server.location.gnss.GnssLocationProvider$$ExternalSyntheticLambda2
                        @Override // java.util.function.ToLongFunction
                        public final long applyAsLong(java.lang.Object obj) {
                            return ((android.location.Location) obj).getTime();
                        }
                    }));
                    long expectedDeltaMs = locations[locations.length - 1].getTime() - locations[locations.length - 1].getElapsedRealtimeMillis();
                    for (int i3 = locations.length - 2; i3 >= 0; i3--) {
                        locations[i3].setElapsedRealtimeNanos(java.util.concurrent.TimeUnit.MILLISECONDS.toNanos(java.lang.Math.max(locations[i3].getTime() - expectedDeltaMs, 0L)));
                    }
                } else {
                    java.util.Arrays.sort(locations, java.util.Comparator.comparingLong(new java.util.function.ToLongFunction() { // from class: com.android.server.location.gnss.GnssLocationProvider$$ExternalSyntheticLambda3
                        @Override // java.util.function.ToLongFunction
                        public final long applyAsLong(java.lang.Object obj) {
                            return ((android.location.Location) obj).getElapsedRealtimeNanos();
                        }
                    }));
                }
            }
            try {
                reportLocation(android.location.LocationResult.wrap(locations).validate());
            } catch (android.location.LocationResult.BadLocationException e) {
                android.util.Log.e(TAG, "Dropping invalid locations: " + e);
                return;
            }
        }
        synchronized (this.mLock) {
            listeners = (java.lang.Runnable[]) this.mFlushListeners.toArray(new java.lang.Runnable[0]);
            this.mFlushListeners.clear();
        }
        for (java.lang.Runnable listener : listeners) {
            listener.run();
        }
    }

    @Override // com.android.server.location.gnss.hal.GnssNative.SvStatusCallbacks
    public void onReportSvStatus(final android.location.GnssStatus gnssStatus) {
        postWithWakeLockHeld(new java.lang.Runnable() { // from class: com.android.server.location.gnss.GnssLocationProvider$$ExternalSyntheticLambda22
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onReportSvStatus$14(gnssStatus);
            }
        });
    }

    @Override // com.android.server.location.gnss.hal.GnssNative.AGpsCallbacks
    public void onReportAGpsStatus(int agpsType, int agpsStatus, byte[] suplIpAddr) {
        this.mNetworkConnectivityHandler.onReportAGpsStatus(agpsType, agpsStatus, suplIpAddr);
    }

    @Override // com.android.server.location.gnss.hal.GnssNative.PsdsCallbacks
    public void onRequestPsdsDownload(final int psdsType) {
        postWithWakeLockHeld(new java.lang.Runnable() { // from class: com.android.server.location.gnss.GnssLocationProvider$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onRequestPsdsDownload$15(psdsType);
            }
        });
    }

    @Override // com.android.server.location.gnss.hal.GnssNative.AGpsCallbacks
    public void onRequestSetID(int flags) {
        android.telephony.TelephonyManager phone = (android.telephony.TelephonyManager) this.mContext.getSystemService(com.android.server.autofill.HintsHelper.AUTOFILL_HINT_PHONE);
        int type = 0;
        java.lang.String setId = null;
        int subId = android.telephony.SubscriptionManager.getDefaultDataSubscriptionId();
        if (this.mGnssConfiguration.isActiveSimEmergencySuplEnabled() && this.mNIHandler.getInEmergency() && this.mNetworkConnectivityHandler.getActiveSubId() >= 0) {
            subId = this.mNetworkConnectivityHandler.getActiveSubId();
        }
        if (android.telephony.SubscriptionManager.isValidSubscriptionId(subId)) {
            phone = phone.createForSubscriptionId(subId);
        }
        if ((flags & 1) == 1) {
            setId = phone.getSubscriberId();
            if (setId != null) {
                type = 1;
            }
        } else if ((flags & 2) == 2 && (setId = phone.getLine1Number()) != null) {
            type = 2;
        }
        this.mGnssNative.setAgpsSetId(type, setId == null ? "" : setId);
    }

    @Override // com.android.server.location.gnss.hal.GnssNative.LocationRequestCallbacks
    public void onRequestLocation(final boolean independentFromGnss, final boolean isUserEmergency) {
        if (DEBUG) {
            android.util.Log.d(TAG, "requestLocation. independentFromGnss: " + independentFromGnss + ", isUserEmergency: " + isUserEmergency);
        }
        if (independentFromGnss && !isUserEmergency && this.mForceSleep) {
            android.util.Log.i(TAG, "GNSS HAL location request is disabled by Deep Sleep Network Freeze.");
        } else {
            postWithWakeLockHeld(new java.lang.Runnable() { // from class: com.android.server.location.gnss.GnssLocationProvider$$ExternalSyntheticLambda7
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onRequestLocation$16(independentFromGnss, isUserEmergency);
                }
            });
        }
    }

    @Override // com.android.server.location.gnss.hal.GnssNative.TimeCallbacks
    public void onRequestUtcTime() {
        demandUtcTimeInjection();
    }

    @Override // com.android.server.location.gnss.hal.GnssNative.LocationRequestCallbacks
    public void onRequestRefLocation() {
        requestRefLocation();
    }

    @Override // com.android.server.location.gnss.hal.GnssNative.NotificationCallbacks
    public void onReportNfwNotification(java.lang.String proxyAppPackageName, byte protocolStack, java.lang.String otherProtocolStackName, byte requestor, java.lang.String requestorId, byte responseType, boolean inEmergencyMode, boolean isCachedLocation) {
        if (this.mGnssVisibilityControl == null) {
            android.util.Log.e(TAG, "reportNfwNotification: mGnssVisibilityControl uninitialized.");
        } else {
            this.mGnssVisibilityControl.reportNfwNotification(proxyAppPackageName, protocolStack, otherProtocolStackName, requestor, requestorId, responseType, inEmergencyMode, isCachedLocation);
        }
    }

    public com.android.server.location.gnss.IGnssLocationProviderWrapper getGnssLocationProviderWrapper() {
        return this.mGnssLocationProviderWrapper;
    }

    private class GnssLocationProviderWrapper implements com.android.server.location.gnss.IGnssLocationProviderWrapper {
        private GnssLocationProviderWrapper() {
        }

        @Override // com.android.server.location.gnss.IGnssLocationProviderWrapper
        public com.android.server.location.gnss.hal.GnssNative getGnssNative() {
            return com.android.server.location.gnss.GnssLocationProvider.this.mGnssNative;
        }

        @Override // com.android.server.location.gnss.IGnssLocationProviderWrapper
        public void startNavigating() {
            com.android.server.location.gnss.GnssLocationProvider.this.startNavigating();
        }

        @Override // com.android.server.location.gnss.IGnssLocationProviderWrapper
        public void stopNavigating() {
            com.android.server.location.gnss.GnssLocationProvider.this.stopNavigating();
        }

        @Override // com.android.server.location.gnss.IGnssLocationProviderWrapper
        public void updateClientUids(android.os.WorkSource source) {
            com.android.server.location.gnss.GnssLocationProvider.this.updateClientUids(source);
        }

        @Override // com.android.server.location.gnss.IGnssLocationProviderWrapper
        public void reportLocation(android.location.LocationResult result) {
            com.android.server.location.gnss.GnssLocationProvider.this.reportLocation(result);
        }

        @Override // com.android.server.location.gnss.IGnssLocationProviderWrapper
        public void subscriptionOrCarrierConfigChanged() {
            com.android.server.location.gnss.GnssLocationProvider.this.subscriptionOrCarrierConfigChanged();
        }

        @Override // com.android.server.location.gnss.IGnssLocationProviderWrapper
        public void updateData(android.os.Bundle extra) {
            int drStatus = extra.getInt("drStatus", -1);
            float ebearing = extra.getFloat("ebearing", -1.0f);
            if (drStatus != -1) {
                com.android.server.location.gnss.GnssLocationProvider.this.mDrStatus = drStatus;
            }
            if (ebearing != -1.0f) {
                com.android.server.location.gnss.GnssLocationProvider.this.mEBearing = ebearing;
            }
        }

        @Override // com.android.server.location.gnss.IGnssLocationProviderWrapper
        public void registerNetworkLocationListener() {
            com.android.server.location.gnss.GnssLocationProvider.this.registerNetworkLocationListener();
        }
    }
}
