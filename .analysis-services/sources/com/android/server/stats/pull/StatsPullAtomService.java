package com.android.server.stats.pull;

/* JADX INFO: loaded from: classes3.dex */
public class StatsPullAtomService extends com.android.server.SystemService {
    private static final long APP_OPS_SAMPLING_INITIALIZATION_DELAY_MILLIS = 45000;
    private static final int APP_OPS_SIZE_ESTIMATE = 2000;
    private static final java.lang.String APP_OPS_TARGET_COLLECTION_SIZE = "app_ops_target_collection_size";
    private static final java.lang.String COMMON_PERMISSION_PREFIX = "android.permission.";
    private static final int CPU_CYCLES_PER_UID_CLUSTER_VALUES = 3;
    private static final int CPU_TIME_PER_THREAD_FREQ_MAX_NUM_FREQUENCIES = 8;
    private static final java.lang.String DANGEROUS_PERMISSION_STATE_SAMPLE_RATE = "dangerous_permission_state_sample_rate";
    private static final boolean DEBUG = true;
    private static final int DIMENSION_KEY_SIZE_HARD_LIMIT = 800;
    private static final int DIMENSION_KEY_SIZE_SOFT_LIMIT = 500;
    private static final long EXTERNAL_STATS_SYNC_TIMEOUT_MILLIS = 2000;
    private static final int MAX_PROCSTATS_RAW_SHARD_SIZE = 58982;
    private static final int MAX_PROCSTATS_SHARDS = 5;
    private static final int MAX_PROCSTATS_SHARD_SIZE = 49152;
    private static final long MILLIS_PER_SEC = 1000;
    private static final long MILLI_AMP_HR_TO_NANO_AMP_SECS = 3600000000L;
    private static final int MIN_CPU_TIME_PER_UID_FREQ = 10;
    private static final int NET_STACK_THRE = 1000;
    private static final int OP_FLAGS_PULLED = 9;
    private static final java.lang.String RESULT_RECEIVER_CONTROLLER_KEY = "controller_activity";
    private static final int SYSTEM_UID = 1000;
    private static final java.lang.String TAG = "StatsPullAtomService";
    private com.android.server.stats.pull.AggregatedMobileDataStatsPuller mAggregatedMobileDataStatsPuller;
    private final java.lang.Object mAppOpsLock;
    private int mAppOpsSamplingRate;
    private final java.lang.Object mAppSizeLock;
    private final java.lang.Object mAppsOnExternalStorageInfoLock;
    private final java.lang.Object mAttributedAppOpsLock;
    private java.io.File mBaseDir;
    private final java.lang.Object mBinderCallsStatsExceptionsLock;
    private final java.lang.Object mBinderCallsStatsLock;
    private final java.lang.Object mBluetoothActivityInfoLock;
    private final java.lang.Object mBluetoothBytesTransferLock;
    private final java.lang.Object mBuildInformationLock;
    private final java.lang.Object mCategorySizeLock;
    private final android.content.Context mContext;
    private final java.lang.Object mCooldownDeviceLock;
    private final java.lang.Object mCpuActiveTimeLock;
    private final java.lang.Object mCpuClusterTimeLock;
    private final java.lang.Object mCpuTimePerClusterFreqLock;
    private final java.lang.Object mCpuTimePerThreadFreqLock;
    private final java.lang.Object mCpuTimePerUidFreqLock;
    private final java.lang.Object mCpuTimePerUidLock;
    private com.android.internal.os.KernelCpuUidTimeReader.KernelCpuUidActiveTimeReader mCpuUidActiveTimeReader;
    private com.android.internal.os.KernelCpuUidTimeReader.KernelCpuUidClusterTimeReader mCpuUidClusterTimeReader;
    private com.android.internal.os.KernelCpuUidTimeReader.KernelCpuUidFreqTimeReader mCpuUidFreqTimeReader;
    private com.android.internal.os.KernelCpuUidTimeReader.KernelCpuUidUserSysTimeReader mCpuUidUserSysTimeReader;
    private final android.util.ArraySet<java.lang.Integer> mDangerousAppOpsList;
    private final java.lang.Object mDangerousAppOpsListLock;
    private final java.lang.Object mDangerousPermissionStateLock;
    private final java.lang.Object mDataBytesTransferLock;
    private final java.lang.Object mDebugElapsedClockLock;
    private long mDebugElapsedClockPreviousValue;
    private long mDebugElapsedClockPullCount;
    private final java.lang.Object mDebugFailingElapsedClockLock;
    private long mDebugFailingElapsedClockPreviousValue;
    private long mDebugFailingElapsedClockPullCount;
    private final java.lang.Object mDeviceCalculatedPowerUseLock;
    private final java.lang.Object mDirectoryUsageLock;
    private final java.lang.Object mDiskIoLock;
    private final java.lang.Object mDiskStatsLock;
    private final java.lang.Object mExternalStorageInfoLock;
    private final java.lang.Object mFaceSettingsLock;
    private final java.lang.Object mHealthHalLock;
    private com.android.server.health.HealthServiceWrapper mHealthService;
    private final java.util.ArrayList<com.android.server.stats.pull.netstats.SubInfo> mHistoricalSubs;
    private android.security.metrics.IKeystoreMetrics mIKeystoreMetrics;
    private final java.lang.Object mInstalledIncrementalPackagesLock;
    private final java.lang.Object mIonHeapSizeLock;
    private com.android.internal.os.KernelCpuThreadReaderDiff mKernelCpuThreadReader;
    private final java.lang.Object mKernelWakelockLock;
    private com.android.server.power.stats.KernelWakelockReader mKernelWakelockReader;
    private final java.lang.Object mKeystoreLock;
    private final java.lang.Object mLooperStatsLock;
    private final java.lang.Object mModemActivityInfoLock;
    private final java.util.ArrayList<com.android.server.stats.pull.netstats.NetworkStatsExt> mNetworkStatsBaselines;
    private android.app.usage.NetworkStatsManager mNetworkStatsManager;
    private android.app.INotificationManager mNotificationManagerService;
    private final java.lang.Object mNotificationRemoteViewsLock;
    private final java.lang.Object mNotificationStatsLock;
    private final java.lang.Object mNumBiometricsEnrolledLock;
    private final java.lang.Object mPowerProfileLock;
    private final java.lang.Object mProcStatsLock;
    private final java.lang.Object mProcessCpuTimeLock;
    private com.android.internal.os.ProcessCpuTracker mProcessCpuTracker;
    private final java.lang.Object mProcessMemoryHighWaterMarkLock;
    private final java.lang.Object mProcessMemoryStateLock;
    private com.android.internal.app.procstats.IProcessStats mProcessStatsService;
    private final java.lang.Object mProcessSystemIonHeapSizeLock;
    private final java.lang.Object mRoleHolderLock;
    private final java.lang.Object mRuntimeAppOpAccessMessageLock;
    private final java.lang.Object mSettingsStatsLock;
    private com.android.server.stats.pull.StatsPullAtomService.StatsPullAtomCallbackImpl mStatsCallbackImpl;
    private android.app.StatsManager mStatsManager;
    private com.android.server.stats.pull.IStatsPullAtomServiceExt mStatsPullAtomServiceExt;
    private com.android.server.stats.pull.StatsPullAtomService.StatsSubscriptionsListener mStatsSubscriptionsListener;
    private android.os.storage.StorageManager mStorageManager;
    private android.os.IStoraged mStorageService;
    private final java.lang.Object mStoragedLock;
    private com.android.internal.os.StoragedUidIoStatsReader mStoragedUidIoStatsReader;
    private android.telephony.SubscriptionManager mSubscriptionManager;
    private com.android.internal.os.SelectedProcessCpuThreadReader mSurfaceFlingerProcessCpuThreadReader;
    private final java.lang.Object mSystemElapsedRealtimeLock;
    private final java.lang.Object mSystemIonHeapSizeLock;
    private final java.lang.Object mSystemUptimeLock;
    private android.telephony.TelephonyManager mTelephony;
    private final java.lang.Object mTemperatureLock;
    private final java.lang.Object mThermalLock;
    private android.os.IThermalService mThermalService;
    private final java.lang.Object mTimeZoneDataInfoLock;
    private final java.lang.Object mTimeZoneDetectionInfoLock;
    private com.android.server.power.stats.KernelWakelockStats mTmpWakelockStats;
    private final java.lang.Object mUwbActivityInfoLock;
    private android.uwb.UwbManager mUwbManager;
    private final java.lang.Object mWifiActivityInfoLock;
    private android.net.wifi.WifiManager mWifiManager;
    private boolean netStackError;
    private static final int RANDOM_SEED = new java.util.Random().nextInt();
    private static final long NETSTATS_UID_DEFAULT_BUCKET_DURATION_MS = java.util.concurrent.TimeUnit.HOURS.toMillis(2);
    public static final boolean ENABLE_MOBILE_DATA_STATS_AGGREGATED_PULLER = com.android.server.stats.Flags.addMobileBytesTransferByProcStatePuller();

    private native void initializeNativePullers();

    public StatsPullAtomService(android.content.Context context) {
        super(context);
        this.mThermalLock = new java.lang.Object();
        this.mStoragedLock = new java.lang.Object();
        this.mNotificationStatsLock = new java.lang.Object();
        this.mDebugElapsedClockPreviousValue = 0L;
        this.mDebugElapsedClockPullCount = 0L;
        this.mDebugFailingElapsedClockPreviousValue = 0L;
        this.mDebugFailingElapsedClockPullCount = 0L;
        this.mNetworkStatsManager = null;
        this.mAppOpsSamplingRate = 0;
        this.mDangerousAppOpsListLock = new java.lang.Object();
        this.mDangerousAppOpsList = new android.util.ArraySet<>();
        this.mNetworkStatsBaselines = new java.util.ArrayList<>();
        this.mHistoricalSubs = new java.util.ArrayList<>();
        this.netStackError = false;
        this.mAggregatedMobileDataStatsPuller = null;
        this.mDataBytesTransferLock = new java.lang.Object();
        this.mBluetoothBytesTransferLock = new java.lang.Object();
        this.mKernelWakelockLock = new java.lang.Object();
        this.mCpuTimePerClusterFreqLock = new java.lang.Object();
        this.mCpuTimePerUidLock = new java.lang.Object();
        this.mCpuTimePerUidFreqLock = new java.lang.Object();
        this.mCpuActiveTimeLock = new java.lang.Object();
        this.mCpuClusterTimeLock = new java.lang.Object();
        this.mWifiActivityInfoLock = new java.lang.Object();
        this.mModemActivityInfoLock = new java.lang.Object();
        this.mBluetoothActivityInfoLock = new java.lang.Object();
        this.mUwbActivityInfoLock = new java.lang.Object();
        this.mSystemElapsedRealtimeLock = new java.lang.Object();
        this.mSystemUptimeLock = new java.lang.Object();
        this.mProcessMemoryStateLock = new java.lang.Object();
        this.mProcessMemoryHighWaterMarkLock = new java.lang.Object();
        this.mSystemIonHeapSizeLock = new java.lang.Object();
        this.mIonHeapSizeLock = new java.lang.Object();
        this.mProcessSystemIonHeapSizeLock = new java.lang.Object();
        this.mTemperatureLock = new java.lang.Object();
        this.mCooldownDeviceLock = new java.lang.Object();
        this.mBinderCallsStatsLock = new java.lang.Object();
        this.mBinderCallsStatsExceptionsLock = new java.lang.Object();
        this.mLooperStatsLock = new java.lang.Object();
        this.mDiskStatsLock = new java.lang.Object();
        this.mDirectoryUsageLock = new java.lang.Object();
        this.mAppSizeLock = new java.lang.Object();
        this.mCategorySizeLock = new java.lang.Object();
        this.mNumBiometricsEnrolledLock = new java.lang.Object();
        this.mProcStatsLock = new java.lang.Object();
        this.mDiskIoLock = new java.lang.Object();
        this.mPowerProfileLock = new java.lang.Object();
        this.mProcessCpuTimeLock = new java.lang.Object();
        this.mCpuTimePerThreadFreqLock = new java.lang.Object();
        this.mDeviceCalculatedPowerUseLock = new java.lang.Object();
        this.mDebugElapsedClockLock = new java.lang.Object();
        this.mDebugFailingElapsedClockLock = new java.lang.Object();
        this.mBuildInformationLock = new java.lang.Object();
        this.mRoleHolderLock = new java.lang.Object();
        this.mTimeZoneDataInfoLock = new java.lang.Object();
        this.mTimeZoneDetectionInfoLock = new java.lang.Object();
        this.mExternalStorageInfoLock = new java.lang.Object();
        this.mAppsOnExternalStorageInfoLock = new java.lang.Object();
        this.mFaceSettingsLock = new java.lang.Object();
        this.mAppOpsLock = new java.lang.Object();
        this.mRuntimeAppOpAccessMessageLock = new java.lang.Object();
        this.mNotificationRemoteViewsLock = new java.lang.Object();
        this.mDangerousPermissionStateLock = new java.lang.Object();
        this.mHealthHalLock = new java.lang.Object();
        this.mAttributedAppOpsLock = new java.lang.Object();
        this.mSettingsStatsLock = new java.lang.Object();
        this.mInstalledIncrementalPackagesLock = new java.lang.Object();
        this.mKeystoreLock = new java.lang.Object();
        this.mStatsPullAtomServiceExt = (com.android.server.stats.pull.IStatsPullAtomServiceExt) system.ext.loader.core.ExtLoader.type(com.android.server.stats.pull.IStatsPullAtomServiceExt.class).create();
        this.mContext = context;
    }

    private final class StatsPullAtomServiceInternalImpl extends com.android.server.stats.pull.StatsPullAtomServiceInternal {
        private StatsPullAtomServiceInternalImpl() {
        }

        @Override // com.android.server.stats.pull.StatsPullAtomServiceInternal
        public void noteUidProcessState(int uid, int state) {
            if (com.android.server.stats.pull.StatsPullAtomService.ENABLE_MOBILE_DATA_STATS_AGGREGATED_PULLER && com.android.server.stats.pull.StatsPullAtomService.this.mAggregatedMobileDataStatsPuller != null) {
                long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
                long uptime = android.os.SystemClock.uptimeMillis();
                com.android.server.stats.pull.StatsPullAtomService.this.mAggregatedMobileDataStatsPuller.noteUidProcessState(uid, state, elapsedRealtime, uptime);
            }
        }
    }

    private class StatsPullAtomCallbackImpl implements android.app.StatsManager.StatsPullAtomCallback {
        private StatsPullAtomCallbackImpl() {
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Removed duplicated region for block: B:638:0x0563 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public int onPullAtom(int r6, java.util.List<android.util.StatsEvent> r7) {
            /*
                Method dump skipped, instruction units count: 1788
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.stats.pull.StatsPullAtomService.StatsPullAtomCallbackImpl.onPullAtom(int, java.util.List):int");
        }
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        if (ENABLE_MOBILE_DATA_STATS_AGGREGATED_PULLER) {
            com.android.server.LocalServices.addService(com.android.server.stats.pull.StatsPullAtomServiceInternal.class, new com.android.server.stats.pull.StatsPullAtomService.StatsPullAtomServiceInternalImpl());
        }
    }

    @Override // com.android.server.SystemService
    public void onBootPhase(int phase) {
        super.onBootPhase(phase);
        if (phase == 500) {
            com.android.internal.os.BackgroundThread.getHandler().post(new java.lang.Runnable() { // from class: com.android.server.stats.pull.StatsPullAtomService$$ExternalSyntheticLambda20
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onBootPhase$0();
                }
            });
        } else if (phase == 600) {
            initNetworkStatsManager();
            com.android.internal.os.BackgroundThread.getHandler().post(new java.lang.Runnable() { // from class: com.android.server.stats.pull.StatsPullAtomService$$ExternalSyntheticLambda21
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onBootPhase$1();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onBootPhase$0() {
        initializeNativePullers();
        initializePullersState();
        registerPullers();
        registerEventListeners();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onBootPhase$1() {
        initAndRegisterNetworkStatsPullers();
        initAndRegisterDeferredPullers();
    }

    void initializePullersState() {
        this.mStatsManager = (android.app.StatsManager) this.mContext.getSystemService("stats");
        this.mWifiManager = (android.net.wifi.WifiManager) this.mContext.getSystemService("wifi");
        this.mTelephony = (android.telephony.TelephonyManager) this.mContext.getSystemService(com.android.server.autofill.HintsHelper.AUTOFILL_HINT_PHONE);
        this.mSubscriptionManager = (android.telephony.SubscriptionManager) this.mContext.getSystemService("telephony_subscription_service");
        this.mStatsSubscriptionsListener = new com.android.server.stats.pull.StatsPullAtomService.StatsSubscriptionsListener(this.mSubscriptionManager);
        this.mStorageManager = (android.os.storage.StorageManager) this.mContext.getSystemService(android.os.storage.StorageManager.class);
        this.mStoragedUidIoStatsReader = new com.android.internal.os.StoragedUidIoStatsReader();
        this.mBaseDir = new java.io.File(com.android.server.SystemServiceManager.ensureSystemDir(), "stats_pull");
        this.mBaseDir.mkdirs();
        this.mCpuUidUserSysTimeReader = new com.android.internal.os.KernelCpuUidTimeReader.KernelCpuUidUserSysTimeReader(false);
        this.mCpuUidFreqTimeReader = new com.android.internal.os.KernelCpuUidTimeReader.KernelCpuUidFreqTimeReader(false);
        this.mCpuUidActiveTimeReader = new com.android.internal.os.KernelCpuUidTimeReader.KernelCpuUidActiveTimeReader(false);
        this.mCpuUidClusterTimeReader = new com.android.internal.os.KernelCpuUidTimeReader.KernelCpuUidClusterTimeReader(false);
        this.mKernelWakelockReader = new com.android.server.power.stats.KernelWakelockReader();
        this.mTmpWakelockStats = new com.android.server.power.stats.KernelWakelockStats();
        this.mKernelCpuThreadReader = com.android.internal.os.KernelCpuThreadReaderSettingsObserver.getSettingsModifiedReader(this.mContext);
        try {
            this.mHealthService = com.android.server.health.HealthServiceWrapper.create(null);
        } catch (android.os.RemoteException | java.util.NoSuchElementException e) {
            android.util.Slog.e(TAG, "failed to initialize healthHalWrapper");
        }
        android.content.pm.PackageManager pm = this.mContext.getPackageManager();
        for (int op = 0; op < 149; op++) {
            java.lang.String perm = android.app.AppOpsManager.opToPermission(op);
            if (perm != null) {
                try {
                    android.content.pm.PermissionInfo permInfo = pm.getPermissionInfo(perm, 0);
                    if (permInfo.getProtection() == 1) {
                        this.mDangerousAppOpsList.add(java.lang.Integer.valueOf(op));
                    }
                } catch (android.content.pm.PackageManager.NameNotFoundException e2) {
                }
            }
        }
        this.mSurfaceFlingerProcessCpuThreadReader = new com.android.internal.os.SelectedProcessCpuThreadReader("/system/bin/surfaceflinger");
        getIKeystoreMetricsService();
    }

    /* JADX WARN: Multi-variable type inference failed */
    void registerEventListeners() {
        java.lang.Object[] objArr = 0;
        ((android.net.ConnectivityManager) this.mContext.getSystemService("connectivity")).registerNetworkCallback(new android.net.NetworkRequest.Builder().build(), new com.android.server.stats.pull.StatsPullAtomService.ConnectivityStatsCallback());
        android.os.IThermalService iThermalService = getIThermalService();
        if (iThermalService != null) {
            try {
                iThermalService.registerThermalEventListener(new com.android.server.stats.pull.StatsPullAtomService.ThermalEventListener());
                android.util.Slog.i(TAG, "register thermal listener successfully");
            } catch (android.os.RemoteException e) {
                android.util.Slog.i(TAG, "failed to register thermal listener");
            }
        }
    }

    void registerPullers() {
        android.util.Slog.d(TAG, "Registering pullers with statsd");
        this.mStatsCallbackImpl = new com.android.server.stats.pull.StatsPullAtomService.StatsPullAtomCallbackImpl();
        registerBluetoothBytesTransfer();
        registerKernelWakelock();
        registerCpuTimePerClusterFreq();
        this.mStatsPullAtomServiceExt.registerKworkerInfo(this.mStatsManager);
        this.mStatsPullAtomServiceExt.registerCpuTimePerTgid(this.mStatsManager);
        registerCpuTimePerUid();
        registerCpuCyclesPerUidCluster();
        registerCpuTimePerUidFreq();
        registerCpuCyclesPerThreadGroupCluster();
        registerCpuActiveTime();
        registerCpuClusterTime();
        registerWifiActivityInfo();
        registerModemActivityInfo();
        registerBluetoothActivityInfo();
        registerSystemElapsedRealtime();
        registerSystemUptime();
        registerProcessMemoryState();
        registerProcessMemoryHighWaterMark();
        registerProcessMemorySnapshot();
        registerSystemIonHeapSize();
        registerIonHeapSize();
        registerProcessSystemIonHeapSize();
        registerSystemMemory();
        registerProcessDmabufMemory();
        registerVmStat();
        registerTemperature();
        registerCoolingDevice();
        registerBinderCallsStats();
        registerBinderCallsStatsExceptions();
        registerLooperStats();
        registerDiskStats();
        registerDirectoryUsage();
        registerAppSize();
        registerCategorySize();
        registerNumFingerprintsEnrolled();
        registerNumFacesEnrolled();
        registerProcStats();
        registerProcStatsPkgProc();
        registerProcessState();
        registerProcessAssociation();
        registerDiskIO();
        registerPowerProfile();
        registerProcessCpuTime();
        registerCpuTimePerThreadFreq();
        registerDeviceCalculatedPowerUse();
        registerDebugElapsedClock();
        registerDebugFailingElapsedClock();
        registerBuildInformation();
        registerRoleHolder();
        registerTimeZoneDataInfo();
        registerTimeZoneDetectorState();
        registerExternalStorageInfo();
        registerAppsOnExternalStorageInfo();
        registerFaceSettings();
        registerAppOps();
        registerAttributedAppOps();
        registerRuntimeAppOpAccessMessage();
        registerNotificationRemoteViews();
        registerDangerousPermissionState();
        registerDangerousPermissionStateSampled();
        registerBatteryLevel();
        registerRemainingBatteryCapacity();
        registerFullBatteryCapacity();
        registerBatteryVoltage();
        registerBatteryCycleCount();
        registerSettingsStats();
        registerInstalledIncrementalPackages();
        registerKeystoreStorageStats();
        registerKeystoreKeyCreationWithGeneralInfo();
        registerKeystoreKeyCreationWithAuthInfo();
        registerKeystoreKeyCreationWithPurposeModesInfo();
        registerKeystoreAtomWithOverflow();
        registerKeystoreKeyOperationWithPurposeAndModesInfo();
        registerKeystoreKeyOperationWithGeneralInfo();
        registerRkpErrorStats();
        registerKeystoreCrashStats();
        registerAccessibilityShortcutStats();
        registerAccessibilityFloatingMenuStats();
        registerMediaCapabilitiesStats();
        registerPendingIntentsPerPackagePuller();
        registerPinnerServiceStats();
        registerHdrCapabilitiesPuller();
        registerCachedAppsHighWatermarkPuller();
    }

    private void initMobileDataStatsPuller() {
        android.util.Slog.d(TAG, "ENABLE_MOBILE_DATA_STATS_AGGREGATED_PULLER = " + ENABLE_MOBILE_DATA_STATS_AGGREGATED_PULLER);
        if (ENABLE_MOBILE_DATA_STATS_AGGREGATED_PULLER) {
            this.mAggregatedMobileDataStatsPuller = new com.android.server.stats.pull.AggregatedMobileDataStatsPuller((android.app.usage.NetworkStatsManager) this.mContext.getSystemService(android.app.usage.NetworkStatsManager.class));
        }
    }

    private android.app.usage.NetworkStatsManager getNetworkStatsManager() {
        if (this.mNetworkStatsManager == null) {
            throw new java.lang.IllegalStateException("NetworkStatsManager is not ready");
        }
        return this.mNetworkStatsManager;
    }

    private void initNetworkStatsManager() {
        this.mNetworkStatsManager = (android.app.usage.NetworkStatsManager) this.mContext.getSystemService(android.app.usage.NetworkStatsManager.class);
    }

    private void initAndRegisterNetworkStatsPullers() {
        android.util.Slog.d(TAG, "Registering NetworkStats pullers with statsd");
        boolean canQueryTypeProxy = canQueryNetworkStatsForTypeProxy();
        synchronized (this.mDataBytesTransferLock) {
            this.mNetworkStatsBaselines.addAll(collectNetworkStatsSnapshotForAtom(10000));
            this.mNetworkStatsBaselines.addAll(collectNetworkStatsSnapshotForAtom(10001));
            this.mNetworkStatsBaselines.addAll(collectNetworkStatsSnapshotForAtom(10002));
            this.mNetworkStatsBaselines.addAll(collectNetworkStatsSnapshotForAtom(10003));
            this.mNetworkStatsBaselines.addAll(collectNetworkStatsSnapshotForAtom(com.android.internal.util.FrameworkStatsLog.BYTES_TRANSFER_BY_TAG_AND_METERED));
            this.mNetworkStatsBaselines.addAll(collectNetworkStatsSnapshotForAtom(com.android.internal.util.FrameworkStatsLog.DATA_USAGE_BYTES_TRANSFER));
            this.mNetworkStatsBaselines.addAll(collectNetworkStatsSnapshotForAtom(com.android.internal.util.FrameworkStatsLog.OEM_MANAGED_BYTES_TRANSFER));
            if (canQueryTypeProxy) {
                this.mNetworkStatsBaselines.addAll(collectNetworkStatsSnapshotForAtom(com.android.internal.util.FrameworkStatsLog.PROXY_BYTES_TRANSFER_BY_FG_BG));
            }
        }
        java.util.concurrent.Executor sNecExecutor = ((com.android.server.IOplusNecConnectMonitor) com.android.server.OplusServiceFactory.getInstance().getFeature(com.android.server.IOplusNecConnectMonitor.DEFAULT, new java.lang.Object[]{this.mContext})).getNecExecutor();
        if (sNecExecutor != null) {
            android.util.Slog.e(TAG, "using sNecExecutor  for SubscriptionsListener");
            this.mSubscriptionManager.addOnSubscriptionsChangedListener(sNecExecutor, this.mStatsSubscriptionsListener);
        } else {
            android.util.Slog.e(TAG, "using default BackgroundThread for SubscriptionsListener");
            this.mSubscriptionManager.addOnSubscriptionsChangedListener(com.android.internal.os.BackgroundThread.getExecutor(), this.mStatsSubscriptionsListener);
        }
        registerWifiBytesTransfer();
        registerWifiBytesTransferBackground();
        registerMobileBytesTransfer();
        registerMobileBytesTransferBackground();
        if (ENABLE_MOBILE_DATA_STATS_AGGREGATED_PULLER) {
            initMobileDataStatsPuller();
            registerMobileBytesTransferByProcState();
        }
        registerBytesTransferByTagAndMetered();
        registerDataUsageBytesTransfer();
        registerOemManagedBytesTransfer();
        if (canQueryTypeProxy) {
            registerProxyBytesTransferBackground();
        }
    }

    private void registerMobileBytesTransferByProcState() {
        android.app.StatsManager.PullAtomMetadata metadata = new android.app.StatsManager.PullAtomMetadata.Builder().setAdditiveFields(new int[]{3, 4, 5, 6}).build();
        this.mStatsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.MOBILE_BYTES_TRANSFER_BY_PROC_STATE, metadata, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    private void initAndRegisterDeferredPullers() {
        this.mUwbManager = this.mContext.getPackageManager().hasSystemFeature("android.hardware.uwb") ? (android.uwb.UwbManager) this.mContext.getSystemService(android.uwb.UwbManager.class) : null;
        registerUwbActivityInfo();
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x0034 A[Catch: all -> 0x0038, TryCatch #0 {, blocks: (B:4:0x0003, B:6:0x0007, B:8:0x0018, B:11:0x0029, B:12:0x0034, B:13:0x0036), top: B:18:0x0003, inners: #1 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private android.os.IThermalService getIThermalService() {
        /*
            r4 = this;
            java.lang.Object r0 = r4.mThermalLock
            monitor-enter(r0)
            android.os.IThermalService r1 = r4.mThermalService     // Catch: java.lang.Throwable -> L38
            if (r1 != 0) goto L34
            java.lang.String r1 = "thermalservice"
            android.os.IBinder r1 = android.os.ServiceManager.getService(r1)     // Catch: java.lang.Throwable -> L38
            android.os.IThermalService r1 = android.os.IThermalService.Stub.asInterface(r1)     // Catch: java.lang.Throwable -> L38
            r4.mThermalService = r1     // Catch: java.lang.Throwable -> L38
            android.os.IThermalService r1 = r4.mThermalService     // Catch: java.lang.Throwable -> L38
            if (r1 == 0) goto L34
            android.os.IThermalService r1 = r4.mThermalService     // Catch: android.os.RemoteException -> L28 java.lang.Throwable -> L38
            android.os.IBinder r1 = r1.asBinder()     // Catch: android.os.RemoteException -> L28 java.lang.Throwable -> L38
            com.android.server.stats.pull.StatsPullAtomService$$ExternalSyntheticLambda22 r2 = new com.android.server.stats.pull.StatsPullAtomService$$ExternalSyntheticLambda22     // Catch: android.os.RemoteException -> L28 java.lang.Throwable -> L38
            r2.<init>()     // Catch: android.os.RemoteException -> L28 java.lang.Throwable -> L38
            r3 = 0
            r1.linkToDeath(r2, r3)     // Catch: android.os.RemoteException -> L28 java.lang.Throwable -> L38
            goto L34
        L28:
            r1 = move-exception
            java.lang.String r2 = "StatsPullAtomService"
            java.lang.String r3 = "linkToDeath with thermalService failed"
            android.util.Slog.e(r2, r3, r1)     // Catch: java.lang.Throwable -> L38
            r2 = 0
            r4.mThermalService = r2     // Catch: java.lang.Throwable -> L38
        L34:
            android.os.IThermalService r1 = r4.mThermalService     // Catch: java.lang.Throwable -> L38
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L38
            return r1
        L38:
            r1 = move-exception
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L38
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.stats.pull.StatsPullAtomService.getIThermalService():android.os.IThermalService");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getIThermalService$2() {
        synchronized (this.mThermalLock) {
            this.mThermalService = null;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x0033 A[Catch: all -> 0x0037, TryCatch #0 {, blocks: (B:4:0x0003, B:6:0x0007, B:8:0x0017, B:11:0x0028, B:12:0x0033, B:13:0x0035), top: B:18:0x0003, inners: #1 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private android.security.metrics.IKeystoreMetrics getIKeystoreMetricsService() {
        /*
            r4 = this;
            java.lang.Object r0 = r4.mKeystoreLock
            monitor-enter(r0)
            android.security.metrics.IKeystoreMetrics r1 = r4.mIKeystoreMetrics     // Catch: java.lang.Throwable -> L37
            if (r1 != 0) goto L33
            java.lang.String r1 = "android.security.metrics"
            android.os.IBinder r1 = android.os.ServiceManager.getService(r1)     // Catch: java.lang.Throwable -> L37
            android.security.metrics.IKeystoreMetrics r1 = android.security.metrics.IKeystoreMetrics.Stub.asInterface(r1)     // Catch: java.lang.Throwable -> L37
            r4.mIKeystoreMetrics = r1     // Catch: java.lang.Throwable -> L37
            android.security.metrics.IKeystoreMetrics r1 = r4.mIKeystoreMetrics     // Catch: java.lang.Throwable -> L37
            if (r1 == 0) goto L33
            android.security.metrics.IKeystoreMetrics r1 = r4.mIKeystoreMetrics     // Catch: android.os.RemoteException -> L27 java.lang.Throwable -> L37
            android.os.IBinder r1 = r1.asBinder()     // Catch: android.os.RemoteException -> L27 java.lang.Throwable -> L37
            com.android.server.stats.pull.StatsPullAtomService$$ExternalSyntheticLambda25 r2 = new com.android.server.stats.pull.StatsPullAtomService$$ExternalSyntheticLambda25     // Catch: android.os.RemoteException -> L27 java.lang.Throwable -> L37
            r2.<init>()     // Catch: android.os.RemoteException -> L27 java.lang.Throwable -> L37
            r3 = 0
            r1.linkToDeath(r2, r3)     // Catch: android.os.RemoteException -> L27 java.lang.Throwable -> L37
            goto L33
        L27:
            r1 = move-exception
            java.lang.String r2 = "StatsPullAtomService"
            java.lang.String r3 = "linkToDeath with IKeystoreMetrics failed"
            android.util.Slog.e(r2, r3, r1)     // Catch: java.lang.Throwable -> L37
            r2 = 0
            r4.mIKeystoreMetrics = r2     // Catch: java.lang.Throwable -> L37
        L33:
            android.security.metrics.IKeystoreMetrics r1 = r4.mIKeystoreMetrics     // Catch: java.lang.Throwable -> L37
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L37
            return r1
        L37:
            r1 = move-exception
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L37
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.stats.pull.StatsPullAtomService.getIKeystoreMetricsService():android.security.metrics.IKeystoreMetrics");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getIKeystoreMetricsService$3() {
        synchronized (this.mKeystoreLock) {
            this.mIKeystoreMetrics = null;
        }
    }

    private android.os.IStoraged getIStoragedService() {
        synchronized (this.mStoragedLock) {
            if (this.mStorageService == null) {
                this.mStorageService = android.os.IStoraged.Stub.asInterface(android.os.ServiceManager.getService("storaged"));
            }
            if (this.mStorageService != null) {
                try {
                    this.mStorageService.asBinder().linkToDeath(new android.os.IBinder.DeathRecipient() { // from class: com.android.server.stats.pull.StatsPullAtomService$$ExternalSyntheticLambda24
                        @Override // android.os.IBinder.DeathRecipient
                        public final void binderDied() {
                            this.f$0.lambda$getIStoragedService$4();
                        }
                    }, 0);
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(TAG, "linkToDeath with storagedService failed", e);
                    this.mStorageService = null;
                }
            }
        }
        return this.mStorageService;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getIStoragedService$4() {
        synchronized (this.mStoragedLock) {
            this.mStorageService = null;
        }
    }

    private android.app.INotificationManager getINotificationManagerService() {
        synchronized (this.mNotificationStatsLock) {
            if (this.mNotificationManagerService == null) {
                this.mNotificationManagerService = android.app.INotificationManager.Stub.asInterface(android.os.ServiceManager.getService("notification"));
            }
            if (this.mNotificationManagerService != null) {
                try {
                    this.mNotificationManagerService.asBinder().linkToDeath(new android.os.IBinder.DeathRecipient() { // from class: com.android.server.stats.pull.StatsPullAtomService$$ExternalSyntheticLambda9
                        @Override // android.os.IBinder.DeathRecipient
                        public final void binderDied() {
                            this.f$0.lambda$getINotificationManagerService$5();
                        }
                    }, 0);
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(TAG, "linkToDeath with notificationManager failed", e);
                    this.mNotificationManagerService = null;
                }
            }
        }
        return this.mNotificationManagerService;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getINotificationManagerService$5() {
        synchronized (this.mNotificationStatsLock) {
            this.mNotificationManagerService = null;
        }
    }

    private com.android.internal.app.procstats.IProcessStats getIProcessStatsService() {
        synchronized (this.mProcStatsLock) {
            if (this.mProcessStatsService == null) {
                this.mProcessStatsService = com.android.internal.app.procstats.IProcessStats.Stub.asInterface(android.os.ServiceManager.getService("procstats"));
            }
            if (this.mProcessStatsService != null) {
                try {
                    this.mProcessStatsService.asBinder().linkToDeath(new android.os.IBinder.DeathRecipient() { // from class: com.android.server.stats.pull.StatsPullAtomService$$ExternalSyntheticLambda2
                        @Override // android.os.IBinder.DeathRecipient
                        public final void binderDied() {
                            this.f$0.lambda$getIProcessStatsService$6();
                        }
                    }, 0);
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(TAG, "linkToDeath with ProcessStats failed", e);
                    this.mProcessStatsService = null;
                }
            }
        }
        return this.mProcessStatsService;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getIProcessStatsService$6() {
        synchronized (this.mProcStatsLock) {
            this.mProcessStatsService = null;
        }
    }

    private void registerWifiBytesTransfer() {
        android.app.StatsManager.PullAtomMetadata metadata = new android.app.StatsManager.PullAtomMetadata.Builder().setAdditiveFields(new int[]{2, 3, 4, 5}).build();
        this.mStatsManager.setPullAtomCallback(10000, metadata, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    private java.util.List<com.android.server.stats.pull.netstats.NetworkStatsExt> collectNetworkStatsSnapshotForAtom(int atomTag) {
        java.util.List<com.android.server.stats.pull.netstats.NetworkStatsExt> ret = new java.util.ArrayList<>();
        switch (atomTag) {
            case 10000:
                android.net.NetworkStats stats = getUidNetworkStatsSnapshotForTransport(1);
                if (stats != null) {
                    ret.add(new com.android.server.stats.pull.netstats.NetworkStatsExt(sliceNetworkStatsByUid(stats), new int[]{1}, false));
                }
                return ret;
            case 10001:
                android.net.NetworkStats stats2 = getUidNetworkStatsSnapshotForTransport(1);
                if (stats2 != null) {
                    ret.add(new com.android.server.stats.pull.netstats.NetworkStatsExt(sliceNetworkStatsByUidAndFgbg(stats2), new int[]{1}, true));
                }
                return ret;
            case 10002:
                android.net.NetworkStats stats3 = getUidNetworkStatsSnapshotForTransport(0);
                if (stats3 != null) {
                    ret.add(new com.android.server.stats.pull.netstats.NetworkStatsExt(sliceNetworkStatsByUid(stats3), new int[]{0}, false));
                }
                return ret;
            case 10003:
                android.net.NetworkStats stats4 = getUidNetworkStatsSnapshotForTransport(0);
                if (stats4 != null) {
                    ret.add(new com.android.server.stats.pull.netstats.NetworkStatsExt(sliceNetworkStatsByUidAndFgbg(stats4), new int[]{0}, true));
                }
                return ret;
            case com.android.internal.util.FrameworkStatsLog.DATA_USAGE_BYTES_TRANSFER /* 10082 */:
                for (com.android.server.stats.pull.netstats.SubInfo subInfo : this.mHistoricalSubs) {
                    ret.addAll(getDataUsageBytesTransferSnapshotForSub(subInfo));
                }
                return ret;
            case com.android.internal.util.FrameworkStatsLog.BYTES_TRANSFER_BY_TAG_AND_METERED /* 10083 */:
                android.net.NetworkStats wifiStats = getUidNetworkStatsSnapshotForTemplate(new android.net.NetworkTemplate.Builder(4).build(), true);
                android.net.NetworkStats cellularStats = getUidNetworkStatsSnapshotForTemplate(new android.net.NetworkTemplate.Builder(1).setMeteredness(1).build(), true);
                if (wifiStats != null && cellularStats != null) {
                    ret.add(new com.android.server.stats.pull.netstats.NetworkStatsExt(sliceNetworkStatsByUidTagAndMetered(wifiStats.add(cellularStats)), new int[]{1, 0}, false, true, true, 0, null, -1, false));
                }
                return ret;
            case com.android.internal.util.FrameworkStatsLog.OEM_MANAGED_BYTES_TRANSFER /* 10100 */:
                ret.addAll(getDataUsageBytesTransferSnapshotForOemManaged());
                return ret;
            case com.android.internal.util.FrameworkStatsLog.PROXY_BYTES_TRANSFER_BY_FG_BG /* 10200 */:
                android.net.NetworkStats stats5 = getUidNetworkStatsSnapshotForTemplate(new android.net.NetworkTemplate.Builder(9).build(), true);
                if (stats5 != null) {
                    ret.add(new com.android.server.stats.pull.netstats.NetworkStatsExt(sliceNetworkStatsByUidTagAndMetered(stats5), new int[]{2}, true, false, false, 0, null, -1, true));
                }
                return ret;
            default:
                throw new java.lang.IllegalArgumentException("Unknown atomTag " + atomTag);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int pullDataBytesTransferLocked(int atomTag, java.util.List<android.util.StatsEvent> pulledData) {
        java.util.List<com.android.server.stats.pull.netstats.NetworkStatsExt> current = collectNetworkStatsSnapshotForAtom(atomTag);
        int i = 1;
        java.lang.String str = ", return.";
        if (current == null) {
            android.util.Slog.e(TAG, "current snapshot is null for " + atomTag + ", return.");
            return 1;
        }
        for (final com.android.server.stats.pull.netstats.NetworkStatsExt item : current) {
            com.android.server.stats.pull.netstats.NetworkStatsExt baseline = (com.android.server.stats.pull.netstats.NetworkStatsExt) com.android.internal.util.CollectionUtils.find(this.mNetworkStatsBaselines, new java.util.function.Predicate() { // from class: com.android.server.stats.pull.StatsPullAtomService$$ExternalSyntheticLambda10
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return ((com.android.server.stats.pull.netstats.NetworkStatsExt) obj).hasSameSlicing(item);
                }
            });
            if (baseline == null) {
                android.util.Slog.e(TAG, "baseline is null for " + atomTag + str);
                return i;
            }
            java.util.List<com.android.server.stats.pull.netstats.NetworkStatsExt> current2 = current;
            java.lang.String str2 = str;
            com.android.server.stats.pull.netstats.NetworkStatsExt diff = new com.android.server.stats.pull.netstats.NetworkStatsExt(removeEmptyEntries(item.stats.subtract(baseline.stats)), item.transports, item.slicedByFgbg, item.slicedByTag, item.slicedByMetered, item.ratType, item.subInfo, item.oemManaged, item.isTypeProxy);
            if (diff.stats.iterator().hasNext()) {
                switch (atomTag) {
                    case com.android.internal.util.FrameworkStatsLog.DATA_USAGE_BYTES_TRANSFER /* 10082 */:
                        addDataUsageBytesTransferAtoms(diff, pulledData);
                        break;
                    case com.android.internal.util.FrameworkStatsLog.BYTES_TRANSFER_BY_TAG_AND_METERED /* 10083 */:
                        addBytesTransferByTagAndMeteredAtoms(diff, pulledData);
                        break;
                    case com.android.internal.util.FrameworkStatsLog.OEM_MANAGED_BYTES_TRANSFER /* 10100 */:
                        addOemDataUsageBytesTransferAtoms(diff, pulledData);
                        break;
                    default:
                        addNetworkStats(atomTag, pulledData, diff);
                        break;
                }
                current = current2;
                str = str2;
                i = 1;
            } else {
                current = current2;
                str = str2;
                i = 1;
            }
        }
        return 0;
    }

    private static android.net.NetworkStats removeEmptyEntries(android.net.NetworkStats stats) {
        android.net.NetworkStats ret = new android.net.NetworkStats(0L, 1);
        java.util.Iterator it = stats.iterator();
        while (it.hasNext()) {
            android.net.NetworkStats.Entry e = (android.net.NetworkStats.Entry) it.next();
            if (e.getRxBytes() != 0 || e.getRxPackets() != 0 || e.getTxBytes() != 0 || e.getTxPackets() != 0 || e.getOperations() != 0) {
                ret = ret.addEntry(e);
            }
        }
        return ret;
    }

    private void addNetworkStats(int atomTag, java.util.List<android.util.StatsEvent> ret, com.android.server.stats.pull.netstats.NetworkStatsExt statsExt) {
        android.util.StatsEvent statsEvent;
        for (android.net.NetworkStats.Entry entry : statsExt.stats) {
            if (statsExt.slicedByFgbg) {
                statsEvent = com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, entry.getUid(), entry.getSet() > 0, entry.getRxBytes(), entry.getRxPackets(), entry.getTxBytes(), entry.getTxPackets());
            } else {
                statsEvent = com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, entry.getUid(), entry.getRxBytes(), entry.getRxPackets(), entry.getTxBytes(), entry.getTxPackets());
            }
            ret.add(statsEvent);
        }
    }

    private void addBytesTransferByTagAndMeteredAtoms(com.android.server.stats.pull.netstats.NetworkStatsExt statsExt, java.util.List<android.util.StatsEvent> pulledData) {
        boolean is5GNsa = statsExt.ratType == -2;
        for (android.net.NetworkStats.Entry entry : statsExt.stats) {
            pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(com.android.internal.util.FrameworkStatsLog.BYTES_TRANSFER_BY_TAG_AND_METERED, entry.getUid(), entry.getMetered() == 1, entry.getTag(), entry.getRxBytes(), entry.getRxPackets(), entry.getTxBytes(), entry.getTxPackets(), is5GNsa ? 13 : statsExt.ratType));
        }
    }

    private void addDataUsageBytesTransferAtoms(com.android.server.stats.pull.netstats.NetworkStatsExt statsExt, java.util.List<android.util.StatsEvent> pulledData) {
        int i;
        boolean is5GNsa = statsExt.ratType == -2;
        boolean isNR = is5GNsa || statsExt.ratType == 20;
        for (android.net.NetworkStats.Entry entry : statsExt.stats) {
            int set = entry.getSet();
            long rxBytes = entry.getRxBytes();
            long rxPackets = entry.getRxPackets();
            long txBytes = entry.getTxBytes();
            long txPackets = entry.getTxPackets();
            int i2 = is5GNsa ? 13 : statsExt.ratType;
            java.lang.String str = statsExt.subInfo.mcc;
            java.lang.String str2 = statsExt.subInfo.mnc;
            int i3 = statsExt.subInfo.carrierId;
            boolean is5GNsa2 = is5GNsa;
            if (statsExt.subInfo.isOpportunistic) {
                i = 2;
            } else {
                i = 3;
            }
            pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(com.android.internal.util.FrameworkStatsLog.DATA_USAGE_BYTES_TRANSFER, set, rxBytes, rxPackets, txBytes, txPackets, i2, str, str2, i3, i, isNR));
            is5GNsa = is5GNsa2;
        }
    }

    private void addOemDataUsageBytesTransferAtoms(com.android.server.stats.pull.netstats.NetworkStatsExt statsExt, java.util.List<android.util.StatsEvent> pulledData) {
        int oemManaged = statsExt.oemManaged;
        int[] iArr = statsExt.transports;
        int length = iArr.length;
        int i = 0;
        while (i < length) {
            int transport = iArr[i];
            for (android.net.NetworkStats.Entry entry : statsExt.stats) {
                pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(com.android.internal.util.FrameworkStatsLog.OEM_MANAGED_BYTES_TRANSFER, entry.getUid(), entry.getSet() > 0, oemManaged, transport, entry.getRxBytes(), entry.getRxPackets(), entry.getTxBytes(), entry.getTxPackets()));
                length = length;
                i = i;
            }
            i++;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r9v0 */
    /* JADX WARN: Type inference failed for: r9v1, types: [int] */
    /* JADX WARN: Type inference failed for: r9v3 */
    private java.util.List<com.android.server.stats.pull.netstats.NetworkStatsExt> getDataUsageBytesTransferSnapshotForOemManaged() {
        boolean z = false;
        java.util.List<android.util.Pair> listOf = java.util.List.of(new android.util.Pair(5, 3), new android.util.Pair(1, 0), new android.util.Pair(4, 1));
        int[] iArr = {3, 1, 2};
        java.util.ArrayList arrayList = new java.util.ArrayList();
        for (android.util.Pair pair : listOf) {
            java.lang.Integer num = (java.lang.Integer) pair.first;
            int length = iArr.length;
            for (?? r9 = z; r9 < length; r9++) {
                int i = iArr[r9];
                android.net.NetworkStats uidNetworkStatsSnapshotForTemplate = getUidNetworkStatsSnapshotForTemplate(new android.net.NetworkTemplate.Builder(num.intValue()).setOemManaged(i).build(), z);
                java.lang.Integer num2 = (java.lang.Integer) pair.second;
                if (uidNetworkStatsSnapshotForTemplate != null) {
                    arrayList.add(new com.android.server.stats.pull.netstats.NetworkStatsExt(sliceNetworkStatsByUidAndFgbg(uidNetworkStatsSnapshotForTemplate), new int[]{num2.intValue()}, true, false, false, 0, null, i, false));
                }
                z = false;
            }
            z = false;
        }
        return arrayList;
    }

    private android.net.NetworkStats getUidNetworkStatsSnapshotForTransport(int transport) {
        android.net.NetworkTemplate template = null;
        switch (transport) {
            case 0:
                template = new android.net.NetworkTemplate.Builder(1).setMeteredness(1).build();
                break;
            case 1:
                template = new android.net.NetworkTemplate.Builder(4).build();
                break;
            default:
                android.util.Log.wtf(TAG, "Unexpected transport.");
                break;
        }
        return getUidNetworkStatsSnapshotForTemplate(template, false);
    }

    private static boolean canQueryNetworkStatsForTypeProxy() {
        try {
            new android.net.NetworkTemplate.Builder(9).build();
            return true;
        } catch (java.lang.IllegalArgumentException e) {
            android.util.Slog.w(TAG, "Querying network stats for TYPE_PROXY is not allowed");
            return false;
        }
    }

    private android.net.NetworkStats getUidNetworkStatsSnapshotForTemplate(android.net.NetworkTemplate template, boolean includeTags) {
        long elapsedMillisSinceBoot = android.os.SystemClock.elapsedRealtime();
        long currentTimeInMillis = java.util.concurrent.TimeUnit.MICROSECONDS.toMillis(android.os.SystemClock.currentTimeMicro());
        long bucketDuration = android.provider.Settings.Global.getLong(this.mContext.getContentResolver(), "netstats_uid_bucket_duration", NETSTATS_UID_DEFAULT_BUCKET_DURATION_MS);
        if (template.getMatchRule() == 4 && template.getSubscriberIds().isEmpty()) {
            getNetworkStatsManager().forceUpdate();
        }
        long netStatsStartTime = android.os.SystemClock.elapsedRealtime();
        android.app.usage.NetworkStats queryNonTaggedStats = getNetworkStatsManager().querySummary(template, (currentTimeInMillis - elapsedMillisSinceBoot) - bucketDuration, currentTimeInMillis);
        android.net.NetworkStats nonTaggedStats = com.android.net.module.util.NetworkStatsUtils.fromPublicNetworkStats(queryNonTaggedStats);
        queryNonTaggedStats.close();
        if (!includeTags) {
            return nonTaggedStats;
        }
        android.app.usage.NetworkStats queryTaggedStats = getNetworkStatsManager().queryTaggedSummary(template, (currentTimeInMillis - elapsedMillisSinceBoot) - bucketDuration, currentTimeInMillis);
        android.net.NetworkStats taggedStats = com.android.net.module.util.NetworkStatsUtils.fromPublicNetworkStats(queryTaggedStats);
        queryTaggedStats.close();
        long netStatsEndTime = android.os.SystemClock.elapsedRealtime();
        int uid = android.os.Process.myUid();
        if (uid == 1000) {
            if (netStatsEndTime - netStatsStartTime <= 1000) {
                this.netStackError = false;
            } else {
                android.util.Log.d(TAG, "getNetworkStatsSession stack ");
                this.netStackError = true;
            }
            if (printStack() != null) {
                ((com.android.server.IOplusNecConnectMonitor) com.android.server.OplusServiceFactory.getInstance().getFeature(com.android.server.IOplusNecConnectMonitor.DEFAULT, new java.lang.Object[]{this.mContext})).addNetStackRecord(uid, printStack(), this.netStackError);
            } else {
                ((com.android.server.IOplusNecConnectMonitor) com.android.server.OplusServiceFactory.getInstance().getFeature(com.android.server.IOplusNecConnectMonitor.DEFAULT, new java.lang.Object[]{this.mContext})).addNetStackRecord(uid, getPackageNameByUid(uid), this.netStackError);
            }
        }
        return nonTaggedStats.add(taggedStats);
    }

    private java.lang.String getPackageNameByUid(int callingUid) {
        if (callingUid == 0) {
            return "unknown";
        }
        try {
            java.lang.String packageName = this.mContext.getPackageManager().getNameForUid(callingUid);
            return packageName;
        } catch (java.lang.Exception e) {
            e.printStackTrace();
            return "unknown";
        }
    }

    private java.lang.String printStack() {
        boolean AppHasAtom = false;
        java.lang.Exception e = new java.lang.Exception();
        java.lang.StackTraceElement[] elements = e.getStackTrace();
        if (elements.length <= 0) {
            return null;
        }
        for (int i = 0; i < elements.length; i++) {
            if (elements[i].getClassName() != null && elements[i].getClassName().equals("com.android.server.stats.pull.StatsPullAtomService")) {
                AppHasAtom = true;
            }
            if (AppHasAtom && !elements[i].getClassName().equals("com.android.server.stats.pull.StatsPullAtomService")) {
                return elements[i].getClassName();
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r4v0 */
    /* JADX WARN: Type inference failed for: r4v2 */
    public java.util.List<com.android.server.stats.pull.netstats.NetworkStatsExt> getDataUsageBytesTransferSnapshotForSub(com.android.server.stats.pull.netstats.SubInfo subInfo) {
        java.util.List<com.android.server.stats.pull.netstats.NetworkStatsExt> ret = new java.util.ArrayList<>();
        int[] allCollapsedRatTypes = getAllCollapsedRatTypes();
        int length = allCollapsedRatTypes.length;
        int i = 0;
        int i2 = 0;
        while (i2 < length) {
            int ratType = allCollapsedRatTypes[i2];
            android.net.NetworkTemplate template = new android.net.NetworkTemplate.Builder(1).setSubscriberIds(java.util.Set.of(subInfo.subscriberId)).setRatType(ratType).setMeteredness(1).build();
            android.net.NetworkStats stats = getUidNetworkStatsSnapshotForTemplate(template, i);
            if (stats != null) {
                ret.add(new com.android.server.stats.pull.netstats.NetworkStatsExt(sliceNetworkStatsByFgbg(stats), new int[]{i}, true, false, false, ratType, subInfo, -1, false));
            }
            i2++;
            i = 0;
        }
        return ret;
    }

    private static int[] getAllCollapsedRatTypes() {
        int[] ratTypes = android.telephony.TelephonyManager.getAllNetworkTypes();
        java.util.HashSet<java.lang.Integer> collapsedRatTypes = new java.util.HashSet<>();
        for (int ratType : ratTypes) {
            collapsedRatTypes.add(java.lang.Integer.valueOf(android.app.usage.NetworkStatsManager.getCollapsedRatType(ratType)));
        }
        collapsedRatTypes.add(java.lang.Integer.valueOf(android.app.usage.NetworkStatsManager.getCollapsedRatType(-2)));
        collapsedRatTypes.add(0);
        return com.android.net.module.util.CollectionUtils.toIntArray(collapsedRatTypes);
    }

    private android.net.NetworkStats sliceNetworkStatsByUid(android.net.NetworkStats stats) {
        return sliceNetworkStats(stats, new java.util.function.Function() { // from class: com.android.server.stats.pull.StatsPullAtomService$$ExternalSyntheticLambda1
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return com.android.server.stats.pull.StatsPullAtomService.lambda$sliceNetworkStatsByUid$8((android.net.NetworkStats.Entry) obj);
            }
        });
    }

    static /* synthetic */ android.net.NetworkStats.Entry lambda$sliceNetworkStatsByUid$8(android.net.NetworkStats.Entry entry) {
        return new android.net.NetworkStats.Entry((java.lang.String) null, entry.getUid(), -1, 0, -1, -1, -1, entry.getRxBytes(), entry.getRxPackets(), entry.getTxBytes(), entry.getTxPackets(), 0L);
    }

    private android.net.NetworkStats sliceNetworkStatsByFgbg(android.net.NetworkStats stats) {
        return sliceNetworkStats(stats, new java.util.function.Function() { // from class: com.android.server.stats.pull.StatsPullAtomService$$ExternalSyntheticLambda18
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return com.android.server.stats.pull.StatsPullAtomService.lambda$sliceNetworkStatsByFgbg$9((android.net.NetworkStats.Entry) obj);
            }
        });
    }

    static /* synthetic */ android.net.NetworkStats.Entry lambda$sliceNetworkStatsByFgbg$9(android.net.NetworkStats.Entry entry) {
        return new android.net.NetworkStats.Entry((java.lang.String) null, -1, entry.getSet(), 0, -1, -1, -1, entry.getRxBytes(), entry.getRxPackets(), entry.getTxBytes(), entry.getTxPackets(), 0L);
    }

    private android.net.NetworkStats sliceNetworkStatsByUidAndFgbg(android.net.NetworkStats stats) {
        return sliceNetworkStats(stats, new java.util.function.Function() { // from class: com.android.server.stats.pull.StatsPullAtomService$$ExternalSyntheticLambda19
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return com.android.server.stats.pull.StatsPullAtomService.lambda$sliceNetworkStatsByUidAndFgbg$10((android.net.NetworkStats.Entry) obj);
            }
        });
    }

    static /* synthetic */ android.net.NetworkStats.Entry lambda$sliceNetworkStatsByUidAndFgbg$10(android.net.NetworkStats.Entry entry) {
        return new android.net.NetworkStats.Entry((java.lang.String) null, entry.getUid(), entry.getSet(), 0, -1, -1, -1, entry.getRxBytes(), entry.getRxPackets(), entry.getTxBytes(), entry.getTxPackets(), 0L);
    }

    private android.net.NetworkStats sliceNetworkStatsByUidTagAndMetered(android.net.NetworkStats stats) {
        return sliceNetworkStats(stats, new java.util.function.Function() { // from class: com.android.server.stats.pull.StatsPullAtomService$$ExternalSyntheticLambda8
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return com.android.server.stats.pull.StatsPullAtomService.lambda$sliceNetworkStatsByUidTagAndMetered$11((android.net.NetworkStats.Entry) obj);
            }
        });
    }

    static /* synthetic */ android.net.NetworkStats.Entry lambda$sliceNetworkStatsByUidTagAndMetered$11(android.net.NetworkStats.Entry entry) {
        return new android.net.NetworkStats.Entry((java.lang.String) null, entry.getUid(), -1, entry.getTag(), entry.getMetered(), -1, -1, entry.getRxBytes(), entry.getRxPackets(), entry.getTxBytes(), entry.getTxPackets(), 0L);
    }

    private android.net.NetworkStats sliceNetworkStats(android.net.NetworkStats stats, java.util.function.Function<android.net.NetworkStats.Entry, android.net.NetworkStats.Entry> slicer) {
        android.net.NetworkStats ret = new android.net.NetworkStats(0L, 1);
        java.util.Iterator it = stats.iterator();
        while (it.hasNext()) {
            android.net.NetworkStats.Entry e = (android.net.NetworkStats.Entry) it.next();
            ret = ret.addEntry(slicer.apply(e));
        }
        return ret;
    }

    private void registerWifiBytesTransferBackground() {
        android.app.StatsManager.PullAtomMetadata metadata = new android.app.StatsManager.PullAtomMetadata.Builder().setAdditiveFields(new int[]{3, 4, 5, 6}).build();
        this.mStatsManager.setPullAtomCallback(10001, metadata, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    private void registerMobileBytesTransfer() {
        android.app.StatsManager.PullAtomMetadata metadata = new android.app.StatsManager.PullAtomMetadata.Builder().setAdditiveFields(new int[]{2, 3, 4, 5}).build();
        this.mStatsManager.setPullAtomCallback(10002, metadata, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    private void registerMobileBytesTransferBackground() {
        android.app.StatsManager.PullAtomMetadata metadata = new android.app.StatsManager.PullAtomMetadata.Builder().setAdditiveFields(new int[]{3, 4, 5, 6}).build();
        this.mStatsManager.setPullAtomCallback(10003, metadata, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    private void registerProxyBytesTransferBackground() {
        android.app.StatsManager.PullAtomMetadata metadata = new android.app.StatsManager.PullAtomMetadata.Builder().setAdditiveFields(new int[]{3, 4, 5, 6}).build();
        this.mStatsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.PROXY_BYTES_TRANSFER_BY_FG_BG, metadata, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    private void registerBytesTransferByTagAndMetered() {
        android.app.StatsManager.PullAtomMetadata metadata = new android.app.StatsManager.PullAtomMetadata.Builder().setAdditiveFields(new int[]{4, 5, 6, 7}).build();
        this.mStatsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.BYTES_TRANSFER_BY_TAG_AND_METERED, metadata, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    private void registerDataUsageBytesTransfer() {
        android.app.StatsManager.PullAtomMetadata metadata = new android.app.StatsManager.PullAtomMetadata.Builder().setAdditiveFields(new int[]{2, 3, 4, 5}).build();
        this.mStatsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.DATA_USAGE_BYTES_TRANSFER, metadata, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    private void registerOemManagedBytesTransfer() {
        android.app.StatsManager.PullAtomMetadata metadata = new android.app.StatsManager.PullAtomMetadata.Builder().setAdditiveFields(new int[]{5, 6, 7, 8}).build();
        this.mStatsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.OEM_MANAGED_BYTES_TRANSFER, metadata, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    private void registerBluetoothBytesTransfer() {
        android.app.StatsManager.PullAtomMetadata metadata = new android.app.StatsManager.PullAtomMetadata.Builder().setAdditiveFields(new int[]{2, 3}).build();
        this.mStatsManager.setPullAtomCallback(10006, metadata, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    private static <T extends android.os.Parcelable> T awaitControllerInfo(android.os.SynchronousResultReceiver synchronousResultReceiver) {
        if (synchronousResultReceiver == null) {
            return null;
        }
        try {
            android.os.SynchronousResultReceiver.Result resultAwaitResult = synchronousResultReceiver.awaitResult(EXTERNAL_STATS_SYNC_TIMEOUT_MILLIS);
            if (resultAwaitResult.bundle != null) {
                resultAwaitResult.bundle.setDefusable(true);
                T t = (T) resultAwaitResult.bundle.getParcelable(RESULT_RECEIVER_CONTROLLER_KEY);
                if (t != null) {
                    return t;
                }
            }
        } catch (java.util.concurrent.TimeoutException e) {
            android.util.Slog.w(TAG, "timeout reading " + synchronousResultReceiver.getName() + " stats");
        }
        return null;
    }

    private android.bluetooth.BluetoothActivityEnergyInfo fetchBluetoothData() {
        android.bluetooth.BluetoothAdapter adapter = android.bluetooth.BluetoothAdapter.getDefaultAdapter();
        if (adapter != null) {
            final android.os.SynchronousResultReceiver bluetoothReceiver = new android.os.SynchronousResultReceiver("bluetooth");
            adapter.requestControllerActivityEnergyInfo(new com.android.server.SystemServerInitThreadPool$$ExternalSyntheticLambda0(), new android.bluetooth.BluetoothAdapter.OnBluetoothActivityEnergyInfoCallback() { // from class: com.android.server.stats.pull.StatsPullAtomService.1
                public void onBluetoothActivityEnergyInfoAvailable(android.bluetooth.BluetoothActivityEnergyInfo info) {
                    android.os.Bundle bundle = new android.os.Bundle();
                    bundle.putParcelable(com.android.server.stats.pull.StatsPullAtomService.RESULT_RECEIVER_CONTROLLER_KEY, info);
                    bluetoothReceiver.send(0, bundle);
                }

                public void onBluetoothActivityEnergyInfoError(int errorCode) {
                    android.util.Slog.w(com.android.server.stats.pull.StatsPullAtomService.TAG, "error reading Bluetooth stats: " + errorCode);
                    android.os.Bundle bundle = new android.os.Bundle();
                    bundle.putParcelable(com.android.server.stats.pull.StatsPullAtomService.RESULT_RECEIVER_CONTROLLER_KEY, null);
                    bluetoothReceiver.send(0, bundle);
                }
            });
            return awaitControllerInfo(bluetoothReceiver);
        }
        android.util.Slog.e(TAG, "Failed to get bluetooth adapter!");
        return null;
    }

    int pullBluetoothBytesTransferLocked(int atomTag, java.util.List<android.util.StatsEvent> pulledData) {
        android.bluetooth.BluetoothActivityEnergyInfo info = fetchBluetoothData();
        if (info == null) {
            return 1;
        }
        for (android.bluetooth.UidTraffic traffic : info.getUidTraffic()) {
            pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, traffic.getUid(), traffic.getRxBytes(), traffic.getTxBytes()));
        }
        return 0;
    }

    private void registerKernelWakelock() {
        this.mStatsManager.setPullAtomCallback(10004, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullKernelWakelockLocked(int atomTag, java.util.List<android.util.StatsEvent> pulledData) {
        com.android.server.power.stats.KernelWakelockStats wakelockStats = this.mKernelWakelockReader.readKernelWakelockStats(this.mTmpWakelockStats);
        for (java.util.Map.Entry<java.lang.String, com.android.server.power.stats.KernelWakelockStats.Entry> ent : wakelockStats.entrySet()) {
            java.lang.String name = ent.getKey();
            com.android.server.power.stats.KernelWakelockStats.Entry kws = ent.getValue();
            pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, name, kws.count, kws.version, kws.totalTimeUs));
        }
        return 0;
    }

    private void registerCpuTimePerClusterFreq() {
        if (com.android.internal.os.KernelCpuBpfTracking.isSupported()) {
            android.app.StatsManager.PullAtomMetadata metadata = new android.app.StatsManager.PullAtomMetadata.Builder().setAdditiveFields(new int[]{3}).build();
            this.mStatsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.CPU_TIME_PER_CLUSTER_FREQ, metadata, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
        }
    }

    int pullCpuTimePerClusterFreqLocked(int atomTag, java.util.List<android.util.StatsEvent> pulledData) {
        int[] freqsClusters = com.android.internal.os.KernelCpuBpfTracking.getFreqsClusters();
        long[] freqs = com.android.internal.os.KernelCpuBpfTracking.getFreqs();
        long[] timesMs = com.android.internal.os.KernelCpuTotalBpfMapReader.read();
        if (timesMs == null) {
            return 1;
        }
        for (int freqIndex = 0; freqIndex < timesMs.length; freqIndex++) {
            int cluster = freqsClusters[freqIndex];
            int freq = (int) freqs[freqIndex];
            long timeMs = timesMs[freqIndex];
            pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, cluster, freq, timeMs));
        }
        return 0;
    }

    private void registerCpuTimePerUid() {
        android.app.StatsManager.PullAtomMetadata metadata = new android.app.StatsManager.PullAtomMetadata.Builder().setAdditiveFields(new int[]{2, 3}).build();
        this.mStatsManager.setPullAtomCallback(10009, metadata, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullCpuTimePerUidLocked(final int atomTag, final java.util.List<android.util.StatsEvent> pulledData) {
        this.mCpuUidUserSysTimeReader.readAbsolute(new com.android.internal.os.KernelCpuUidTimeReader.Callback() { // from class: com.android.server.stats.pull.StatsPullAtomService$$ExternalSyntheticLambda14
            public final void onUidCpuTime(int i, java.lang.Object obj) {
                com.android.server.stats.pull.StatsPullAtomService.lambda$pullCpuTimePerUidLocked$12(pulledData, atomTag, i, (long[]) obj);
            }
        });
        return 0;
    }

    static /* synthetic */ void lambda$pullCpuTimePerUidLocked$12(java.util.List pulledData, int atomTag, int uid, long[] timesUs) {
        long userTimeUs = timesUs[0];
        long systemTimeUs = timesUs[1];
        pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, uid, userTimeUs, systemTimeUs));
    }

    private void registerCpuCyclesPerUidCluster() {
        if (com.android.internal.os.KernelCpuBpfTracking.isSupported() || com.android.internal.os.KernelCpuBpfTracking.getClusters() > 0) {
            android.app.StatsManager.PullAtomMetadata metadata = new android.app.StatsManager.PullAtomMetadata.Builder().setAdditiveFields(new int[]{3, 4, 5}).build();
            this.mStatsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.CPU_CYCLES_PER_UID_CLUSTER, metadata, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
        }
    }

    int pullCpuCyclesPerUidClusterLocked(int atomTag, java.util.List<android.util.StatsEvent> pulledData) {
        com.android.internal.os.PowerProfile powerProfile = new com.android.internal.os.PowerProfile(this.mContext);
        final int[] freqsClusters = com.android.internal.os.KernelCpuBpfTracking.getFreqsClusters();
        final int clusters = com.android.internal.os.KernelCpuBpfTracking.getClusters();
        final long[] freqs = com.android.internal.os.KernelCpuBpfTracking.getFreqs();
        final double[] freqsPowers = new double[freqs.length];
        int freqClusterIndex = 0;
        int lastCluster = -1;
        int freqIndex = 0;
        while (freqIndex < freqs.length) {
            int cluster = freqsClusters[freqIndex];
            if (cluster != lastCluster) {
                freqClusterIndex = 0;
            }
            lastCluster = cluster;
            freqsPowers[freqIndex] = powerProfile.getAveragePowerForCpuCore(cluster, freqClusterIndex);
            freqIndex++;
            freqClusterIndex++;
        }
        final android.util.SparseArray<double[]> aggregated = new android.util.SparseArray<>();
        this.mCpuUidFreqTimeReader.readAbsolute(new com.android.internal.os.KernelCpuUidTimeReader.Callback() { // from class: com.android.server.stats.pull.StatsPullAtomService$$ExternalSyntheticLambda11
            public final void onUidCpuTime(int i, java.lang.Object obj) {
                com.android.server.stats.pull.StatsPullAtomService.lambda$pullCpuCyclesPerUidClusterLocked$13(aggregated, clusters, freqsClusters, freqs, freqsPowers, i, (long[]) obj);
            }
        });
        int size = aggregated.size();
        int i = 0;
        while (i < size) {
            int uid = aggregated.keyAt(i);
            double[] values = aggregated.valueAt(i);
            int cluster2 = 0;
            while (cluster2 < clusters) {
                pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, uid, cluster2, (long) (values[cluster2 * 3] / 1000000.0d), (long) values[(cluster2 * 3) + 1], (long) (values[(cluster2 * 3) + 2] / 1000.0d)));
                cluster2++;
                powerProfile = powerProfile;
                freqsClusters = freqsClusters;
            }
            i++;
            powerProfile = powerProfile;
        }
        return 0;
    }

    static /* synthetic */ void lambda$pullCpuCyclesPerUidClusterLocked$13(android.util.SparseArray aggregated, int clusters, int[] freqsClusters, long[] freqs, double[] freqsPowers, int uid, long[] cpuFreqTimeMs) {
        int uid2;
        if (android.os.UserHandle.isIsolated(uid)) {
            return;
        }
        if (android.os.UserHandle.isSharedAppGid(uid)) {
            uid2 = 59999;
        } else {
            uid2 = android.os.UserHandle.getAppId(uid);
        }
        double[] values = (double[]) aggregated.get(uid2);
        if (values == null) {
            values = new double[clusters * 3];
            aggregated.put(uid2, values);
        }
        for (int freqIndex = 0; freqIndex < cpuFreqTimeMs.length; freqIndex++) {
            int cluster = freqsClusters[freqIndex];
            long timeMs = cpuFreqTimeMs[freqIndex];
            int i = cluster * 3;
            values[i] = values[i] + (freqs[freqIndex] * timeMs);
            int i2 = (cluster * 3) + 1;
            values[i2] = values[i2] + timeMs;
            int i3 = (cluster * 3) + 2;
            values[i3] = values[i3] + (freqsPowers[freqIndex] * timeMs);
        }
    }

    private void registerCpuTimePerUidFreq() {
        android.app.StatsManager.PullAtomMetadata metadata = new android.app.StatsManager.PullAtomMetadata.Builder().setAdditiveFields(new int[]{3}).build();
        this.mStatsManager.setPullAtomCallback(10010, metadata, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullCpuTimePerUidFreqLocked(int atomTag, java.util.List<android.util.StatsEvent> pulledData) {
        final android.util.SparseArray<long[]> aggregated = new android.util.SparseArray<>();
        this.mCpuUidFreqTimeReader.readAbsolute(new com.android.internal.os.KernelCpuUidTimeReader.Callback() { // from class: com.android.server.stats.pull.StatsPullAtomService$$ExternalSyntheticLambda16
            public final void onUidCpuTime(int i, java.lang.Object obj) {
                com.android.server.stats.pull.StatsPullAtomService.lambda$pullCpuTimePerUidFreqLocked$14(aggregated, i, (long[]) obj);
            }
        });
        int size = aggregated.size();
        for (int i = 0; i < size; i++) {
            int uid = aggregated.keyAt(i);
            long[] aggCpuFreqTimeMs = aggregated.valueAt(i);
            for (int freqIndex = 0; freqIndex < aggCpuFreqTimeMs.length; freqIndex++) {
                if (aggCpuFreqTimeMs[freqIndex] >= 10) {
                    pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, uid, freqIndex, aggCpuFreqTimeMs[freqIndex]));
                }
            }
        }
        return 0;
    }

    static /* synthetic */ void lambda$pullCpuTimePerUidFreqLocked$14(android.util.SparseArray aggregated, int uid, long[] cpuFreqTimeMs) {
        int uid2;
        if (android.os.UserHandle.isIsolated(uid)) {
            return;
        }
        if (android.os.UserHandle.isSharedAppGid(uid)) {
            uid2 = 59999;
        } else {
            uid2 = android.os.UserHandle.getAppId(uid);
        }
        long[] aggCpuFreqTimeMs = (long[]) aggregated.get(uid2);
        if (aggCpuFreqTimeMs == null) {
            aggCpuFreqTimeMs = new long[cpuFreqTimeMs.length];
            aggregated.put(uid2, aggCpuFreqTimeMs);
        }
        for (int freqIndex = 0; freqIndex < cpuFreqTimeMs.length; freqIndex++) {
            aggCpuFreqTimeMs[freqIndex] = aggCpuFreqTimeMs[freqIndex] + cpuFreqTimeMs[freqIndex];
        }
    }

    private void registerCpuCyclesPerThreadGroupCluster() {
        if (com.android.internal.os.KernelCpuBpfTracking.isSupported() && !com.android.internal.hidden_from_bootclasspath.com.android.server.power.optimization.Flags.disableSystemServicePowerAttr()) {
            android.app.StatsManager.PullAtomMetadata metadata = new android.app.StatsManager.PullAtomMetadata.Builder().setAdditiveFields(new int[]{3, 4}).build();
            this.mStatsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.CPU_CYCLES_PER_THREAD_GROUP_CLUSTER, metadata, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
        }
    }

    int pullCpuCyclesPerThreadGroupCluster(int atomTag, java.util.List<android.util.StatsEvent> pulledData) {
        com.android.server.power.stats.SystemServerCpuThreadReader.SystemServiceCpuThreadTimes times;
        if (com.android.internal.hidden_from_bootclasspath.com.android.server.power.optimization.Flags.disableSystemServicePowerAttr() || (times = ((android.os.BatteryStatsInternal) com.android.server.LocalServices.getService(android.os.BatteryStatsInternal.class)).getSystemServiceCpuThreadTimes()) == null) {
            return 1;
        }
        addCpuCyclesPerThreadGroupClusterAtoms(atomTag, pulledData, 2, times.threadCpuTimesUs);
        addCpuCyclesPerThreadGroupClusterAtoms(atomTag, pulledData, 1, times.binderThreadCpuTimesUs);
        com.android.internal.os.KernelSingleProcessCpuThreadReader.ProcessCpuUsage surfaceFlingerTimes = this.mSurfaceFlingerProcessCpuThreadReader.readAbsolute();
        if (surfaceFlingerTimes != null && surfaceFlingerTimes.threadCpuTimesMillis != null) {
            long[] surfaceFlingerTimesUs = new long[surfaceFlingerTimes.threadCpuTimesMillis.length];
            for (int i = 0; i < surfaceFlingerTimesUs.length; i++) {
                surfaceFlingerTimesUs[i] = surfaceFlingerTimes.threadCpuTimesMillis[i] * 1000;
            }
            addCpuCyclesPerThreadGroupClusterAtoms(atomTag, pulledData, 3, surfaceFlingerTimesUs);
            return 0;
        }
        return 0;
    }

    private static void addCpuCyclesPerThreadGroupClusterAtoms(int atomTag, java.util.List<android.util.StatsEvent> pulledData, int threadGroup, long[] cpuTimesUs) {
        int[] freqsClusters = com.android.internal.os.KernelCpuBpfTracking.getFreqsClusters();
        int clusters = com.android.internal.os.KernelCpuBpfTracking.getClusters();
        long[] freqs = com.android.internal.os.KernelCpuBpfTracking.getFreqs();
        long[] aggregatedCycles = new long[clusters];
        long[] aggregatedTimesUs = new long[clusters];
        for (int i = 0; i < cpuTimesUs.length; i++) {
            int i2 = freqsClusters[i];
            aggregatedCycles[i2] = aggregatedCycles[i2] + ((freqs[i] * cpuTimesUs[i]) / 1000);
            int i3 = freqsClusters[i];
            aggregatedTimesUs[i3] = aggregatedTimesUs[i3] + cpuTimesUs[i];
        }
        for (int cluster = 0; cluster < clusters; cluster++) {
            pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, threadGroup, cluster, aggregatedCycles[cluster] / 1000000, aggregatedTimesUs[cluster] / 1000));
        }
    }

    private void registerCpuActiveTime() {
        android.app.StatsManager.PullAtomMetadata metadata = new android.app.StatsManager.PullAtomMetadata.Builder().setAdditiveFields(new int[]{2}).build();
        this.mStatsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.CPU_ACTIVE_TIME, metadata, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullCpuActiveTimeLocked(final int atomTag, final java.util.List<android.util.StatsEvent> pulledData) {
        this.mCpuUidActiveTimeReader.readAbsolute(new com.android.internal.os.KernelCpuUidTimeReader.Callback() { // from class: com.android.server.stats.pull.StatsPullAtomService$$ExternalSyntheticLambda7
            public final void onUidCpuTime(int i, java.lang.Object obj) {
                pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, i, ((java.lang.Long) obj).longValue()));
            }
        });
        return 0;
    }

    private void registerCpuClusterTime() {
        android.app.StatsManager.PullAtomMetadata metadata = new android.app.StatsManager.PullAtomMetadata.Builder().setAdditiveFields(new int[]{3}).build();
        this.mStatsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.CPU_CLUSTER_TIME, metadata, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullCpuClusterTimeLocked(final int atomTag, final java.util.List<android.util.StatsEvent> pulledData) {
        this.mCpuUidClusterTimeReader.readAbsolute(new com.android.internal.os.KernelCpuUidTimeReader.Callback() { // from class: com.android.server.stats.pull.StatsPullAtomService$$ExternalSyntheticLambda28
            public final void onUidCpuTime(int i, java.lang.Object obj) {
                com.android.server.stats.pull.StatsPullAtomService.lambda$pullCpuClusterTimeLocked$16(pulledData, atomTag, i, (long[]) obj);
            }
        });
        return 0;
    }

    static /* synthetic */ void lambda$pullCpuClusterTimeLocked$16(java.util.List pulledData, int atomTag, int uid, long[] cpuClusterTimesMs) {
        for (int i = 0; i < cpuClusterTimesMs.length; i++) {
            pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, uid, i, cpuClusterTimesMs[i]));
        }
    }

    private void registerWifiActivityInfo() {
        this.mStatsManager.setPullAtomCallback(10011, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullWifiActivityInfoLocked(int atomTag, java.util.List<android.util.StatsEvent> pulledData) {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            final android.os.SynchronousResultReceiver wifiReceiver = new android.os.SynchronousResultReceiver("wifi");
            this.mWifiManager.getWifiActivityEnergyInfoAsync(new java.util.concurrent.Executor() { // from class: com.android.server.stats.pull.StatsPullAtomService.2
                @Override // java.util.concurrent.Executor
                public void execute(java.lang.Runnable runnable) {
                    runnable.run();
                }
            }, new android.net.wifi.WifiManager.OnWifiActivityEnergyInfoListener() { // from class: com.android.server.stats.pull.StatsPullAtomService$$ExternalSyntheticLambda6
                public final void onWifiActivityEnergyInfo(android.os.connectivity.WifiActivityEnergyInfo wifiActivityEnergyInfo) {
                    com.android.server.stats.pull.StatsPullAtomService.lambda$pullWifiActivityInfoLocked$17(wifiReceiver, wifiActivityEnergyInfo);
                }
            });
            android.os.connectivity.WifiActivityEnergyInfo wifiInfo = awaitControllerInfo(wifiReceiver);
            if (wifiInfo == null) {
                android.os.Binder.restoreCallingIdentity(token);
                return 1;
            }
            try {
                try {
                    pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, wifiInfo.getTimeSinceBootMillis(), wifiInfo.getStackState(), wifiInfo.getControllerTxDurationMillis(), wifiInfo.getControllerRxDurationMillis(), wifiInfo.getControllerIdleDurationMillis(), wifiInfo.getControllerEnergyUsedMicroJoules()));
                    android.os.Binder.restoreCallingIdentity(token);
                    return 0;
                } catch (java.lang.RuntimeException e) {
                    e = e;
                    android.util.Slog.e(TAG, "failed to getWifiActivityEnergyInfoAsync", e);
                    android.os.Binder.restoreCallingIdentity(token);
                    return 1;
                }
            } catch (java.lang.Throwable th) {
                e = th;
                android.os.Binder.restoreCallingIdentity(token);
                throw e;
            }
        } catch (java.lang.RuntimeException e2) {
            e = e2;
        } catch (java.lang.Throwable th2) {
            e = th2;
            android.os.Binder.restoreCallingIdentity(token);
            throw e;
        }
        android.util.Slog.e(TAG, "failed to getWifiActivityEnergyInfoAsync", e);
        android.os.Binder.restoreCallingIdentity(token);
        return 1;
    }

    static /* synthetic */ void lambda$pullWifiActivityInfoLocked$17(android.os.SynchronousResultReceiver wifiReceiver, android.os.connectivity.WifiActivityEnergyInfo info) {
        android.os.Bundle bundle = new android.os.Bundle();
        bundle.putParcelable(RESULT_RECEIVER_CONTROLLER_KEY, info);
        wifiReceiver.send(0, bundle);
    }

    private void registerModemActivityInfo() {
        this.mStatsManager.setPullAtomCallback(10012, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullModemActivityInfoLocked(int atomTag, java.util.List<android.util.StatsEvent> pulledData) throws java.lang.Exception {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            final java.util.concurrent.CompletableFuture<android.telephony.ModemActivityInfo> modemFuture = new java.util.concurrent.CompletableFuture<>();
            this.mTelephony.requestModemActivityInfo(new com.android.server.SystemServerInitThreadPool$$ExternalSyntheticLambda0(), new android.os.OutcomeReceiver<android.telephony.ModemActivityInfo, android.telephony.TelephonyManager.ModemActivityInfoException>() { // from class: com.android.server.stats.pull.StatsPullAtomService.3
                @Override // android.os.OutcomeReceiver
                public void onResult(android.telephony.ModemActivityInfo result) {
                    modemFuture.complete(result);
                }

                @Override // android.os.OutcomeReceiver
                public void onError(android.telephony.TelephonyManager.ModemActivityInfoException e) {
                    android.util.Slog.w(com.android.server.stats.pull.StatsPullAtomService.TAG, "error reading modem stats:" + e);
                    modemFuture.complete(null);
                }
            });
            try {
                try {
                    android.telephony.ModemActivityInfo modemInfo = modemFuture.get(EXTERNAL_STATS_SYNC_TIMEOUT_MILLIS, java.util.concurrent.TimeUnit.MILLISECONDS);
                    if (modemInfo == null) {
                        android.os.Binder.restoreCallingIdentity(token);
                        return 1;
                    }
                    pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, modemInfo.getTimestampMillis(), modemInfo.getSleepTimeMillis(), modemInfo.getIdleTimeMillis(), modemInfo.getTransmitDurationMillisAtPowerLevel(0), modemInfo.getTransmitDurationMillisAtPowerLevel(1), modemInfo.getTransmitDurationMillisAtPowerLevel(2), modemInfo.getTransmitDurationMillisAtPowerLevel(3), modemInfo.getTransmitDurationMillisAtPowerLevel(4), modemInfo.getReceiveTimeMillis(), -1L));
                    android.os.Binder.restoreCallingIdentity(token);
                    return 0;
                } catch (java.lang.Throwable th) {
                    e = th;
                }
            } catch (java.lang.InterruptedException | java.util.concurrent.TimeoutException e) {
                android.util.Slog.w(TAG, "timeout or interrupt reading modem stats: " + e);
                android.os.Binder.restoreCallingIdentity(token);
                return 1;
            } catch (java.util.concurrent.ExecutionException e2) {
                android.util.Slog.w(TAG, "exception reading modem stats: " + e2.getCause());
                android.os.Binder.restoreCallingIdentity(token);
                return 1;
            }
        } catch (java.lang.Throwable th2) {
            e = th2;
        }
        android.os.Binder.restoreCallingIdentity(token);
        throw e;
    }

    private void registerBluetoothActivityInfo() {
        this.mStatsManager.setPullAtomCallback(10007, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullBluetoothActivityInfoLocked(int atomTag, java.util.List<android.util.StatsEvent> pulledData) {
        android.bluetooth.BluetoothActivityEnergyInfo info = fetchBluetoothData();
        if (info == null) {
            return 1;
        }
        pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, info.getTimestampMillis(), info.getBluetoothStackState(), info.getControllerTxTimeMillis(), info.getControllerRxTimeMillis(), info.getControllerIdleTimeMillis(), info.getControllerEnergyUsed()));
        return 0;
    }

    private void registerUwbActivityInfo() {
        if (this.mUwbManager == null) {
            return;
        }
        this.mStatsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.UWB_ACTIVITY_INFO, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullUwbActivityInfoLocked(int atomTag, java.util.List<android.util.StatsEvent> pulledData) {
        final android.os.SynchronousResultReceiver uwbReceiver;
        long token = android.os.Binder.clearCallingIdentity();
        try {
            try {
                uwbReceiver = new android.os.SynchronousResultReceiver("uwb");
            } catch (java.lang.Throwable th) {
                e = th;
                android.os.Binder.restoreCallingIdentity(token);
                throw e;
            }
        } catch (java.lang.RuntimeException e) {
            e = e;
        } catch (java.lang.Throwable th2) {
            e = th2;
        }
        try {
            this.mUwbManager.getUwbActivityEnergyInfoAsync(new com.android.server.SystemServerInitThreadPool$$ExternalSyntheticLambda0(), new java.util.function.Consumer() { // from class: com.android.server.stats.pull.StatsPullAtomService$$ExternalSyntheticLambda27
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    com.android.server.stats.pull.StatsPullAtomService.lambda$pullUwbActivityInfoLocked$18(uwbReceiver, (android.uwb.UwbActivityEnergyInfo) obj);
                }
            });
            android.uwb.UwbActivityEnergyInfo uwbInfo = awaitControllerInfo(uwbReceiver);
            if (uwbInfo == null) {
                android.os.Binder.restoreCallingIdentity(token);
                return 1;
            }
            try {
                pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, uwbInfo.getControllerTxDurationMillis(), uwbInfo.getControllerRxDurationMillis(), uwbInfo.getControllerIdleDurationMillis(), uwbInfo.getControllerWakeCount()));
                android.os.Binder.restoreCallingIdentity(token);
                return 0;
            } catch (java.lang.RuntimeException e2) {
                e = e2;
            }
        } catch (java.lang.RuntimeException e3) {
            e = e3;
        } catch (java.lang.Throwable th3) {
            e = th3;
            android.os.Binder.restoreCallingIdentity(token);
            throw e;
        }
        android.util.Slog.e(TAG, "failed to getUwbActivityEnergyInfoAsync", e);
        android.os.Binder.restoreCallingIdentity(token);
        return 1;
    }

    static /* synthetic */ void lambda$pullUwbActivityInfoLocked$18(android.os.SynchronousResultReceiver uwbReceiver, android.uwb.UwbActivityEnergyInfo info) {
        android.os.Bundle bundle = new android.os.Bundle();
        bundle.putParcelable(RESULT_RECEIVER_CONTROLLER_KEY, info);
        uwbReceiver.send(0, bundle);
    }

    private void registerSystemElapsedRealtime() {
        android.app.StatsManager.PullAtomMetadata metadata = new android.app.StatsManager.PullAtomMetadata.Builder().setCoolDownMillis(1000L).setTimeoutMillis(500L).build();
        this.mStatsManager.setPullAtomCallback(10014, metadata, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullSystemElapsedRealtimeLocked(int atomTag, java.util.List<android.util.StatsEvent> pulledData) {
        pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, android.os.SystemClock.elapsedRealtime()));
        return 0;
    }

    private void registerSystemUptime() {
        this.mStatsManager.setPullAtomCallback(10015, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullSystemUptimeLocked(int atomTag, java.util.List<android.util.StatsEvent> pulledData) {
        pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, android.os.SystemClock.uptimeMillis()));
        return 0;
    }

    private void registerProcessMemoryState() {
        android.app.StatsManager.PullAtomMetadata metadata = new android.app.StatsManager.PullAtomMetadata.Builder().setAdditiveFields(new int[]{4, 5, 6, 7, 8}).build();
        this.mStatsManager.setPullAtomCallback(10013, metadata, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullProcessMemoryStateLocked(int atomTag, java.util.List<android.util.StatsEvent> pulledData) {
        java.util.List<android.app.ProcessMemoryState> processMemoryStates = ((android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class)).getMemoryStateForProcesses();
        java.util.Iterator<android.app.ProcessMemoryState> it = processMemoryStates.iterator();
        while (it.hasNext()) {
            android.app.ProcessMemoryState processMemoryState = it.next();
            com.android.server.am.MemoryStatUtil.MemoryStat memoryStat = com.android.server.am.MemoryStatUtil.readMemoryStatFromFilesystem(processMemoryState.uid, processMemoryState.pid);
            if (memoryStat != null) {
                pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, processMemoryState.uid, processMemoryState.processName, processMemoryState.oomScore, memoryStat.pgfault, memoryStat.pgmajfault, memoryStat.rssInBytes, memoryStat.cacheInBytes, memoryStat.swapInBytes, -1L, -1L, -1));
                processMemoryStates = processMemoryStates;
                it = it;
            }
        }
        return 0;
    }

    private void registerProcessMemoryHighWaterMark() {
        this.mStatsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.PROCESS_MEMORY_HIGH_WATER_MARK, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullProcessMemoryHighWaterMarkLocked(int atomTag, java.util.List<android.util.StatsEvent> pulledData) {
        java.util.List<android.app.ProcessMemoryState> managedProcessList = ((android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class)).getMemoryStateForProcesses();
        for (android.app.ProcessMemoryState managedProcess : managedProcessList) {
            com.android.server.stats.pull.ProcfsMemoryUtil.MemorySnapshot snapshot = com.android.server.stats.pull.ProcfsMemoryUtil.readMemorySnapshotFromProcfs(managedProcess.pid);
            if (snapshot != null) {
                pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, managedProcess.uid, managedProcess.processName, ((long) snapshot.rssHighWaterMarkInKilobytes) * 1024, snapshot.rssHighWaterMarkInKilobytes));
            }
        }
        final android.util.SparseArray<java.lang.String> processCmdlines = com.android.server.stats.pull.ProcfsMemoryUtil.getProcessCmdlines();
        managedProcessList.forEach(new java.util.function.Consumer() { // from class: com.android.server.stats.pull.StatsPullAtomService$$ExternalSyntheticLambda13
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                processCmdlines.delete(((android.app.ProcessMemoryState) obj).pid);
            }
        });
        int size = processCmdlines.size();
        for (int i = 0; i < size; i++) {
            com.android.server.stats.pull.ProcfsMemoryUtil.MemorySnapshot snapshot2 = com.android.server.stats.pull.ProcfsMemoryUtil.readMemorySnapshotFromProcfs(processCmdlines.keyAt(i));
            if (snapshot2 != null) {
                pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, snapshot2.uid, processCmdlines.valueAt(i), ((long) snapshot2.rssHighWaterMarkInKilobytes) * 1024, snapshot2.rssHighWaterMarkInKilobytes));
            }
        }
        android.os.SystemProperties.set("sys.rss_hwm_reset.on", "1");
        return 0;
    }

    private void registerProcessMemorySnapshot() {
        this.mStatsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.PROCESS_MEMORY_SNAPSHOT, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullProcessMemorySnapshot(int atomTag, java.util.List<android.util.StatsEvent> pulledData) {
        java.util.List<android.app.ProcessMemoryState> managedProcessList = ((android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class)).getMemoryStateForProcesses();
        com.android.internal.os.KernelAllocationStats.ProcessGpuMem[] gpuAllocations = com.android.internal.os.KernelAllocationStats.getGpuAllocations();
        android.util.SparseIntArray gpuMemPerPid = new android.util.SparseIntArray(gpuAllocations.length);
        for (com.android.internal.os.KernelAllocationStats.ProcessGpuMem processGpuMem : gpuAllocations) {
            gpuMemPerPid.put(processGpuMem.pid, processGpuMem.gpuMemoryKb);
        }
        java.util.Iterator<android.app.ProcessMemoryState> it = managedProcessList.iterator();
        while (it.hasNext()) {
            android.app.ProcessMemoryState managedProcess = it.next();
            com.android.server.stats.pull.ProcfsMemoryUtil.MemorySnapshot snapshot = com.android.server.stats.pull.ProcfsMemoryUtil.readMemorySnapshotFromProcfs(managedProcess.pid);
            if (snapshot != null) {
                pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, managedProcess.uid, managedProcess.processName, managedProcess.pid, managedProcess.oomScore, snapshot.rssInKilobytes, snapshot.anonRssInKilobytes, snapshot.swapInKilobytes, snapshot.anonRssInKilobytes + snapshot.swapInKilobytes, gpuMemPerPid.get(managedProcess.pid), managedProcess.hasForegroundServices, snapshot.rssShmemKilobytes, managedProcess.mHostingComponentTypes, managedProcess.mHistoricalHostingComponentTypes));
                gpuAllocations = gpuAllocations;
                it = it;
            }
        }
        final android.util.SparseArray<java.lang.String> processCmdlines = com.android.server.stats.pull.ProcfsMemoryUtil.getProcessCmdlines();
        managedProcessList.forEach(new java.util.function.Consumer() { // from class: com.android.server.stats.pull.StatsPullAtomService$$ExternalSyntheticLambda26
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                processCmdlines.delete(((android.app.ProcessMemoryState) obj).pid);
            }
        });
        int size = processCmdlines.size();
        for (int i = 0; i < size; i++) {
            int pid = processCmdlines.keyAt(i);
            com.android.server.stats.pull.ProcfsMemoryUtil.MemorySnapshot snapshot2 = com.android.server.stats.pull.ProcfsMemoryUtil.readMemorySnapshotFromProcfs(pid);
            if (snapshot2 != null) {
                pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, snapshot2.uid, processCmdlines.valueAt(i), pid, com.android.server.job.JobSchedulerShellCommand.CMD_ERR_NO_JOB, snapshot2.rssInKilobytes, snapshot2.anonRssInKilobytes, snapshot2.swapInKilobytes, snapshot2.anonRssInKilobytes + snapshot2.swapInKilobytes, gpuMemPerPid.get(pid), false, snapshot2.rssShmemKilobytes, 0, 0));
            }
        }
        return 0;
    }

    private void registerSystemIonHeapSize() {
        this.mStatsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.SYSTEM_ION_HEAP_SIZE, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullSystemIonHeapSizeLocked(int atomTag, java.util.List<android.util.StatsEvent> pulledData) {
        long systemIonHeapSizeInBytes = com.android.server.stats.pull.IonMemoryUtil.readSystemIonHeapSizeFromDebugfs();
        pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, systemIonHeapSizeInBytes));
        return 0;
    }

    private void registerIonHeapSize() {
        if (!new java.io.File("/sys/kernel/ion/total_heaps_kb").exists()) {
            return;
        }
        this.mStatsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.ION_HEAP_SIZE, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullIonHeapSizeLocked(int atomTag, java.util.List<android.util.StatsEvent> pulledData) {
        int ionHeapSizeInKilobytes = (int) android.os.Debug.getIonHeapsSizeKb();
        pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, ionHeapSizeInKilobytes));
        return 0;
    }

    private void registerProcessSystemIonHeapSize() {
        this.mStatsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.PROCESS_SYSTEM_ION_HEAP_SIZE, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullProcessSystemIonHeapSizeLocked(int atomTag, java.util.List<android.util.StatsEvent> pulledData) {
        java.util.List<com.android.server.stats.pull.IonMemoryUtil.IonAllocations> result = com.android.server.stats.pull.IonMemoryUtil.readProcessSystemIonHeapSizesFromDebugfs();
        for (com.android.server.stats.pull.IonMemoryUtil.IonAllocations allocations : result) {
            pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, android.os.Process.getUidForPid(allocations.pid), com.android.server.stats.pull.ProcfsMemoryUtil.readCmdlineFromProcfs(allocations.pid), (int) (allocations.totalSizeInBytes / 1024), allocations.count, (int) (allocations.maxSizeInBytes / 1024)));
        }
        return 0;
    }

    private void registerProcessDmabufMemory() {
        this.mStatsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.PROCESS_DMABUF_MEMORY, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullProcessDmabufMemory(int atomTag, java.util.List<android.util.StatsEvent> pulledData) {
        com.android.internal.os.KernelAllocationStats.ProcessDmabuf[] procBufs = com.android.internal.os.KernelAllocationStats.getDmabufAllocations();
        if (procBufs == null) {
            return 1;
        }
        for (com.android.internal.os.KernelAllocationStats.ProcessDmabuf procBuf : procBufs) {
            pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, procBuf.uid, procBuf.processName, procBuf.oomScore, procBuf.retainedSizeKb, procBuf.retainedBuffersCount, 0, 0, procBuf.surfaceFlingerSizeKb, procBuf.surfaceFlingerCount));
        }
        return 0;
    }

    private void registerSystemMemory() {
        this.mStatsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.SYSTEM_MEMORY, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullSystemMemory(int atomTag, java.util.List<android.util.StatsEvent> pulledData) {
        com.android.server.stats.pull.SystemMemoryUtil.Metrics metrics = com.android.server.stats.pull.SystemMemoryUtil.getMetrics();
        pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, metrics.unreclaimableSlabKb, metrics.vmallocUsedKb, metrics.pageTablesKb, metrics.kernelStackKb, metrics.totalIonKb, metrics.unaccountedKb, metrics.gpuTotalUsageKb, metrics.gpuPrivateAllocationsKb, metrics.dmaBufTotalExportedKb, metrics.shmemKb, metrics.totalKb, metrics.freeKb, metrics.availableKb, metrics.activeKb, metrics.inactiveKb, metrics.activeAnonKb, metrics.inactiveAnonKb, metrics.activeFileKb, metrics.inactiveFileKb, metrics.swapTotalKb, metrics.swapFreeKb, metrics.cmaTotalKb, metrics.cmaFreeKb));
        return 0;
    }

    private void registerVmStat() {
        this.mStatsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.VMSTAT, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullVmStat(int atomTag, java.util.List<android.util.StatsEvent> pulledData) {
        com.android.server.stats.pull.ProcfsMemoryUtil.VmStat vmStat = com.android.server.stats.pull.ProcfsMemoryUtil.readVmStat();
        if (vmStat != null) {
            pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, vmStat.oomKillCount));
            return 0;
        }
        return 0;
    }

    private void registerTemperature() {
        this.mStatsManager.setPullAtomCallback(10021, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullTemperatureLocked(int atomTag, java.util.List<android.util.StatsEvent> pulledData) {
        android.os.IThermalService thermalService = getIThermalService();
        if (thermalService == null) {
            return 1;
        }
        long callingToken = android.os.Binder.clearCallingIdentity();
        try {
            android.os.Temperature[] temperatures = thermalService.getCurrentTemperatures();
            for (android.os.Temperature temp : temperatures) {
                pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, temp.getType(), temp.getName(), (int) (temp.getValue() * 10.0f), temp.getStatus()));
            }
            return 0;
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Disconnected from thermal service. Cannot pull temperatures.");
            return 1;
        } finally {
            android.os.Binder.restoreCallingIdentity(callingToken);
        }
    }

    private void registerCoolingDevice() {
        this.mStatsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.COOLING_DEVICE, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullCooldownDeviceLocked(int atomTag, java.util.List<android.util.StatsEvent> pulledData) {
        android.os.IThermalService thermalService = getIThermalService();
        if (thermalService == null) {
            return 1;
        }
        long callingToken = android.os.Binder.clearCallingIdentity();
        try {
            android.os.CoolingDevice[] devices = thermalService.getCurrentCoolingDevices();
            for (android.os.CoolingDevice device : devices) {
                pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, device.getType(), device.getName(), (int) device.getValue()));
            }
            return 0;
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Disconnected from thermal service. Cannot pull temperatures.");
            return 1;
        } finally {
            android.os.Binder.restoreCallingIdentity(callingToken);
        }
    }

    private void registerBinderCallsStats() {
        android.app.StatsManager.PullAtomMetadata metadata = new android.app.StatsManager.PullAtomMetadata.Builder().setAdditiveFields(new int[]{4, 5, 6, 8, 12}).build();
        this.mStatsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.BINDER_CALLS, metadata, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullBinderCallsStatsLocked(int atomTag, java.util.List<android.util.StatsEvent> pulledData) {
        com.android.server.BinderCallsStatsService.Internal binderStats = (com.android.server.BinderCallsStatsService.Internal) com.android.server.LocalServices.getService(com.android.server.BinderCallsStatsService.Internal.class);
        if (binderStats == null) {
            android.util.Slog.e(TAG, "failed to get binderStats");
            return 1;
        }
        java.util.List<com.android.internal.os.BinderCallsStats.ExportedCallStat> callStats = binderStats.getExportedCallStats();
        binderStats.reset();
        for (com.android.internal.os.BinderCallsStats.ExportedCallStat callStat : callStats) {
            pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, callStat.workSourceUid, callStat.className, callStat.methodName, callStat.callCount, callStat.exceptionCount, callStat.latencyMicros, callStat.maxLatencyMicros, callStat.cpuTimeMicros, callStat.maxCpuTimeMicros, callStat.maxReplySizeBytes, callStat.maxRequestSizeBytes, callStat.recordedCallCount, callStat.screenInteractive, callStat.callingUid));
            binderStats = binderStats;
            callStats = callStats;
        }
        return 0;
    }

    private void registerBinderCallsStatsExceptions() {
        this.mStatsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.BINDER_CALLS_EXCEPTIONS, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullBinderCallsStatsExceptionsLocked(int atomTag, java.util.List<android.util.StatsEvent> pulledData) {
        com.android.server.BinderCallsStatsService.Internal binderStats = (com.android.server.BinderCallsStatsService.Internal) com.android.server.LocalServices.getService(com.android.server.BinderCallsStatsService.Internal.class);
        if (binderStats == null) {
            android.util.Slog.e(TAG, "failed to get binderStats");
            return 1;
        }
        android.util.ArrayMap<java.lang.String, java.lang.Integer> exceptionStats = binderStats.getExportedExceptionStats();
        for (java.util.Map.Entry<java.lang.String, java.lang.Integer> entry : exceptionStats.entrySet()) {
            pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, entry.getKey(), entry.getValue().intValue()));
        }
        return 0;
    }

    private void registerLooperStats() {
        android.app.StatsManager.PullAtomMetadata metadata = new android.app.StatsManager.PullAtomMetadata.Builder().setAdditiveFields(new int[]{5, 6, 7, 8, 9}).build();
        this.mStatsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.LOOPER_STATS, metadata, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullLooperStatsLocked(int atomTag, java.util.List<android.util.StatsEvent> pulledData) {
        com.android.internal.os.LooperStats looperStats = (com.android.internal.os.LooperStats) com.android.server.LocalServices.getService(com.android.internal.os.LooperStats.class);
        if (looperStats == null) {
            return 1;
        }
        java.util.List<com.android.internal.os.LooperStats.ExportedEntry> entries = looperStats.getEntries();
        looperStats.reset();
        for (com.android.internal.os.LooperStats.ExportedEntry entry : entries) {
            pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, entry.workSourceUid, entry.handlerClassName, entry.threadName, entry.messageName, entry.messageCount, entry.exceptionCount, entry.recordedMessageCount, entry.totalLatencyMicros, entry.cpuUsageMicros, entry.isInteractive, entry.maxCpuUsageMicros, entry.maxLatencyMicros, entry.recordedDelayMessageCount, entry.delayMillis, entry.maxDelayMillis));
            looperStats = looperStats;
            entries = entries;
        }
        return 0;
    }

    private void registerDiskStats() {
        this.mStatsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.DISK_STATS, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    /* JADX WARN: Removed duplicated region for block: B:28:0x004f  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x0056  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x0068 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:42:0x006a A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:10:0x002e -> B:46:0x0044). Please report as a decompilation issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    int pullDiskStatsLocked(int r15, java.util.List<android.util.StatsEvent> r16) throws android.os.RemoteException {
        /*
            r14 = this;
            r0 = 512(0x200, float:7.17E-43)
            byte[] r1 = new byte[r0]
            r0 = 0
        L5:
            int r2 = r1.length
            if (r0 >= r2) goto Le
            byte r2 = (byte) r0
            r1[r0] = r2
            int r0 = r0 + 1
            goto L5
        Le:
            java.io.File r0 = new java.io.File
            java.io.File r2 = android.os.Environment.getDataDirectory()
            java.lang.String r3 = "system/statsdperftest.tmp"
            r0.<init>(r2, r3)
            r2 = r0
            r3 = 0
            r4 = 0
            long r5 = android.os.SystemClock.elapsedRealtime()
            java.io.FileOutputStream r0 = new java.io.FileOutputStream     // Catch: java.lang.Throwable -> L30 java.io.IOException -> L3c
            r0.<init>(r2)     // Catch: java.lang.Throwable -> L30 java.io.IOException -> L3c
            r3 = r0
            r3.write(r1)     // Catch: java.lang.Throwable -> L30 java.io.IOException -> L3c
            r3.close()     // Catch: java.io.IOException -> L2e
        L2d:
            goto L44
        L2e:
            r0 = move-exception
            goto L44
        L30:
            r0 = move-exception
            r7 = r0
            if (r3 == 0) goto L3a
            r3.close()     // Catch: java.io.IOException -> L38
            goto L3a
        L38:
            r0 = move-exception
            goto L3b
        L3a:
        L3b:
            throw r7
        L3c:
            r0 = move-exception
            r4 = r0
            if (r3 == 0) goto L2d
            r3.close()     // Catch: java.io.IOException -> L2e
            goto L2d
        L44:
            long r7 = android.os.SystemClock.elapsedRealtime()
            long r7 = r7 - r5
            boolean r0 = r2.exists()
            if (r0 == 0) goto L52
            r2.delete()
        L52:
            java.lang.String r9 = "StatsPullAtomService"
            if (r4 == 0) goto L5d
            java.lang.String r0 = "Error performing diskstats latency test"
            android.util.Slog.e(r9, r0)
            r7 = -1
        L5d:
            boolean r10 = android.os.storage.StorageManager.isFileEncrypted()
            r11 = -1
            android.os.IStoraged r12 = r14.getIStoragedService()
            if (r12 != 0) goto L6a
            r0 = 1
            return r0
        L6a:
            int r0 = r12.getRecentPerf()     // Catch: android.os.RemoteException -> L70
            r11 = r0
            goto L79
        L70:
            r0 = move-exception
            r13 = r0
            r0 = r13
            java.lang.String r13 = "storaged not found"
            android.util.Slog.e(r9, r13)
        L79:
            r9 = r15
            android.util.StatsEvent r0 = com.android.internal.util.FrameworkStatsLog.buildStatsEvent(r15, r7, r10, r11)
            r13 = r16
            r13.add(r0)
            r0 = 0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.stats.pull.StatsPullAtomService.pullDiskStatsLocked(int, java.util.List):int");
    }

    private void registerDirectoryUsage() {
        this.mStatsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.DIRECTORY_USAGE, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullDirectoryUsageLocked(int atomTag, java.util.List<android.util.StatsEvent> pulledData) {
        android.os.StatFs statFsData = new android.os.StatFs(android.os.Environment.getDataDirectory().getAbsolutePath());
        android.os.StatFs statFsSystem = new android.os.StatFs(android.os.Environment.getRootDirectory().getAbsolutePath());
        android.os.StatFs statFsCache = new android.os.StatFs(android.os.Environment.getDownloadCacheDirectory().getAbsolutePath());
        android.os.StatFs metadataFsSystem = new android.os.StatFs(android.os.Environment.getMetadataDirectory().getAbsolutePath());
        pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, 1, statFsData.getAvailableBytes(), statFsData.getTotalBytes()));
        pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, 2, statFsCache.getAvailableBytes(), statFsCache.getTotalBytes()));
        pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, 3, statFsSystem.getAvailableBytes(), statFsSystem.getTotalBytes()));
        pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, 4, metadataFsSystem.getAvailableBytes(), metadataFsSystem.getTotalBytes()));
        return 0;
    }

    private void registerAppSize() {
        this.mStatsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.APP_SIZE, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullAppSizeLocked(int atomTag, java.util.List<android.util.StatsEvent> pulledData) {
        try {
            java.lang.String jsonStr = libcore.io.IoUtils.readFileAsString(com.android.server.storage.DiskStatsLoggingService.DUMPSYS_CACHE_PATH);
            org.json.JSONObject json = new org.json.JSONObject(jsonStr);
            long cache_time = json.optLong(com.android.server.storage.DiskStatsFileLogger.LAST_QUERY_TIMESTAMP_KEY, -1L);
            org.json.JSONArray pkg_names = json.getJSONArray(com.android.server.storage.DiskStatsFileLogger.PACKAGE_NAMES_KEY);
            org.json.JSONArray app_sizes = json.getJSONArray(com.android.server.storage.DiskStatsFileLogger.APP_SIZES_KEY);
            org.json.JSONArray app_data_sizes = json.getJSONArray(com.android.server.storage.DiskStatsFileLogger.APP_DATA_KEY);
            org.json.JSONArray app_cache_sizes = json.getJSONArray(com.android.server.storage.DiskStatsFileLogger.APP_CACHES_KEY);
            int length = pkg_names.length();
            try {
                if (app_sizes.length() != length || app_data_sizes.length() != length || app_cache_sizes.length() != length) {
                    android.util.Slog.e(TAG, "formatting error in diskstats cache file!");
                    return 1;
                }
                int i = 0;
                while (i < length) {
                    int i2 = i;
                    org.json.JSONArray app_cache_sizes2 = app_cache_sizes;
                    int length2 = length;
                    org.json.JSONArray app_sizes2 = app_sizes;
                    org.json.JSONArray app_data_sizes2 = app_data_sizes;
                    pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, pkg_names.getString(i), app_sizes.optLong(i, -1L), app_data_sizes.optLong(i, -1L), app_cache_sizes.optLong(i, -1L), cache_time));
                    i = i2 + 1;
                    app_cache_sizes = app_cache_sizes2;
                    length = length2;
                    app_sizes = app_sizes2;
                    app_data_sizes = app_data_sizes2;
                }
                return 0;
            } catch (java.io.IOException | org.json.JSONException e) {
            }
        } catch (java.io.IOException | org.json.JSONException e2) {
        }
        android.util.Slog.w(TAG, "Unable to read diskstats cache file within pullAppSize");
        return 1;
    }

    private void registerCategorySize() {
        this.mStatsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.CATEGORY_SIZE, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullCategorySizeLocked(int atomTag, java.util.List<android.util.StatsEvent> pulledData) {
        try {
            java.lang.String jsonStr = libcore.io.IoUtils.readFileAsString(com.android.server.storage.DiskStatsLoggingService.DUMPSYS_CACHE_PATH);
            org.json.JSONObject json = new org.json.JSONObject(jsonStr);
            long cacheTime = json.optLong(com.android.server.storage.DiskStatsFileLogger.LAST_QUERY_TIMESTAMP_KEY, -1L);
            pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, 1, json.optLong(com.android.server.storage.DiskStatsFileLogger.APP_SIZE_AGG_KEY, -1L), cacheTime));
            pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, 2, json.optLong(com.android.server.storage.DiskStatsFileLogger.APP_DATA_SIZE_AGG_KEY, -1L), cacheTime));
            pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, 3, json.optLong(com.android.server.storage.DiskStatsFileLogger.APP_CACHE_AGG_KEY, -1L), cacheTime));
            pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, 4, json.optLong(com.android.server.storage.DiskStatsFileLogger.PHOTOS_KEY, -1L), cacheTime));
            pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, 5, json.optLong(com.android.server.storage.DiskStatsFileLogger.VIDEOS_KEY, -1L), cacheTime));
            pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, 6, json.optLong(com.android.server.storage.DiskStatsFileLogger.AUDIO_KEY, -1L), cacheTime));
            pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, 7, json.optLong(com.android.server.storage.DiskStatsFileLogger.DOWNLOADS_KEY, -1L), cacheTime));
            pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, 8, json.optLong(com.android.server.storage.DiskStatsFileLogger.SYSTEM_KEY, -1L), cacheTime));
            pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, 9, json.optLong(com.android.server.storage.DiskStatsFileLogger.MISC_KEY, -1L), cacheTime));
            return 0;
        } catch (java.io.IOException | org.json.JSONException e) {
            android.util.Slog.w(TAG, "Unable to read diskstats cache file within pullCategorySize");
            return 1;
        }
    }

    private void registerNumFingerprintsEnrolled() {
        this.mStatsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.NUM_FINGERPRINTS_ENROLLED, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    private void registerNumFacesEnrolled() {
        this.mStatsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.NUM_FACES_ENROLLED, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int pullNumBiometricsEnrolledLocked(int modality, int atomTag, java.util.List<android.util.StatsEvent> pulledData) throws java.lang.Throwable {
        android.hardware.fingerprint.FingerprintManager fingerprintManager;
        android.os.UserManager userManager;
        int numEnrolled;
        android.content.pm.PackageManager pm = this.mContext.getPackageManager();
        if (pm.hasSystemFeature("android.hardware.fingerprint")) {
            android.hardware.fingerprint.FingerprintManager fingerprintManager2 = (android.hardware.fingerprint.FingerprintManager) this.mContext.getSystemService(android.hardware.fingerprint.FingerprintManager.class);
            fingerprintManager = fingerprintManager2;
        } else {
            fingerprintManager = null;
        }
        android.hardware.face.FaceManager faceManager = pm.hasSystemFeature("android.hardware.biometrics.face") ? (android.hardware.face.FaceManager) this.mContext.getSystemService(android.hardware.face.FaceManager.class) : null;
        if (modality == 1 && fingerprintManager == null) {
            return 1;
        }
        int i = 4;
        if ((modality == 4 && faceManager == null) || (userManager = (android.os.UserManager) this.mContext.getSystemService(android.os.UserManager.class)) == null) {
            return 1;
        }
        long token = android.os.Binder.clearCallingIdentity();
        try {
            for (android.content.pm.UserInfo user : userManager.getUsers()) {
                int userId = user.getUserHandle().getIdentifier();
                if (modality == 1) {
                    numEnrolled = fingerprintManager.getEnrolledFingerprints(userId).size();
                } else {
                    if (modality != i) {
                        android.os.Binder.restoreCallingIdentity(token);
                        return 1;
                    }
                    numEnrolled = faceManager.getEnrolledFaces(userId).size();
                }
                try {
                    try {
                        pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, userId, numEnrolled));
                        i = 4;
                    } catch (java.lang.Throwable th) {
                        th = th;
                        android.os.Binder.restoreCallingIdentity(token);
                        throw th;
                    }
                } catch (java.lang.Throwable th2) {
                    th = th2;
                    android.os.Binder.restoreCallingIdentity(token);
                    throw th;
                }
            }
            android.os.Binder.restoreCallingIdentity(token);
            return 0;
        } catch (java.lang.Throwable th3) {
            th = th3;
        }
    }

    private void registerProcStats() {
        this.mStatsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.PROC_STATS, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    private void registerProcStatsPkgProc() {
        this.mStatsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.PROC_STATS_PKG_PROC, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    private void registerProcessState() {
        this.mStatsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.PROCESS_STATE, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    private void registerProcessAssociation() {
        this.mStatsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.PROCESS_ASSOCIATION, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    private com.android.internal.app.procstats.ProcessStats getStatsFromProcessStatsService(int atomTag) {
        com.android.internal.app.procstats.IProcessStats processStatsService = getIProcessStatsService();
        if (processStatsService == null) {
            return null;
        }
        long token = android.os.Binder.clearCallingIdentity();
        try {
            long lastHighWaterMark = readProcStatsHighWaterMark(atomTag);
            com.android.internal.app.procstats.ProcessStats procStats = new com.android.internal.app.procstats.ProcessStats(false);
            long highWaterMark = processStatsService.getCommittedStatsMerged(lastHighWaterMark, 31, true, (java.util.List) null, procStats);
            new java.io.File(this.mBaseDir.getAbsolutePath() + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + highWaterMarkFilePrefix(atomTag) + "_" + lastHighWaterMark).delete();
            new java.io.File(this.mBaseDir.getAbsolutePath() + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + highWaterMarkFilePrefix(atomTag) + "_" + highWaterMark).createNewFile();
            return procStats;
        } catch (android.os.RemoteException | java.io.IOException e) {
            android.util.Slog.e(TAG, "Getting procstats failed: ", e);
            return null;
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int pullProcStatsLocked(int atomTag, java.util.List<android.util.StatsEvent> pulledData) {
        com.android.internal.app.procstats.ProcessStats procStats = getStatsFromProcessStatsService(atomTag);
        if (procStats == null) {
            return 1;
        }
        android.util.proto.ProtoOutputStream[] protoStreams = new android.util.proto.ProtoOutputStream[5];
        for (int i = 0; i < protoStreams.length; i++) {
            protoStreams[i] = new android.util.proto.ProtoOutputStream();
        }
        procStats.dumpAggregatedProtoForStatsd(protoStreams, 58982L);
        for (int i2 = 0; i2 < protoStreams.length; i2++) {
            byte[] bytes = protoStreams[i2].getBytes();
            if (bytes.length > 0) {
                pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, bytes, i2));
            }
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int pullProcessStateLocked(int atomTag, java.util.List<android.util.StatsEvent> pulledData) {
        com.android.internal.app.procstats.ProcessStats procStats = getStatsFromProcessStatsService(atomTag);
        if (procStats == null) {
            return 1;
        }
        procStats.dumpProcessState(atomTag, new com.android.internal.app.procstats.StatsEventOutput(pulledData));
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int pullProcessAssociationLocked(int atomTag, java.util.List<android.util.StatsEvent> pulledData) {
        com.android.internal.app.procstats.ProcessStats procStats = getStatsFromProcessStatsService(atomTag);
        if (procStats == null) {
            return 1;
        }
        procStats.dumpProcessAssociation(atomTag, new com.android.internal.app.procstats.StatsEventOutput(pulledData));
        return 0;
    }

    private java.lang.String highWaterMarkFilePrefix(int atomTag) {
        if (atomTag == 10029) {
            return java.lang.String.valueOf(31);
        }
        if (atomTag == 10034) {
            return java.lang.String.valueOf(2);
        }
        return "atom-" + atomTag;
    }

    private long readProcStatsHighWaterMark(final int atomTag) {
        try {
            java.io.File[] files = this.mBaseDir.listFiles(new java.io.FilenameFilter() { // from class: com.android.server.stats.pull.StatsPullAtomService$$ExternalSyntheticLambda17
                @Override // java.io.FilenameFilter
                public final boolean accept(java.io.File file, java.lang.String str) {
                    return this.f$0.lambda$readProcStatsHighWaterMark$21(atomTag, file, str);
                }
            });
            if (files != null && files.length != 0) {
                if (files.length > 1) {
                    android.util.Slog.e(TAG, "Only 1 file expected for high water mark. Found " + files.length);
                }
                return java.lang.Long.valueOf(files[0].getName().split("_")[1]).longValue();
            }
            return 0L;
        } catch (java.lang.NumberFormatException e) {
            android.util.Slog.e(TAG, "Failed to parse file name.", e);
            return 0L;
        } catch (java.lang.SecurityException e2) {
            android.util.Slog.e(TAG, "Failed to get procstats high watermark file.", e2);
            return 0L;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$readProcStatsHighWaterMark$21(int atomTag, java.io.File d, java.lang.String name) {
        return name.toLowerCase().startsWith(highWaterMarkFilePrefix(atomTag) + '_');
    }

    private void registerDiskIO() {
        android.app.StatsManager.PullAtomMetadata metadata = new android.app.StatsManager.PullAtomMetadata.Builder().setAdditiveFields(new int[]{2, 3, 4, 5, 6, 7, 8, 9, 10, 11}).setCoolDownMillis(3000L).build();
        this.mStatsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.DISK_IO, metadata, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullDiskIOLocked(final int atomTag, final java.util.List<android.util.StatsEvent> pulledData) {
        this.mStoragedUidIoStatsReader.readAbsolute(new com.android.internal.os.StoragedUidIoStatsReader.Callback() { // from class: com.android.server.stats.pull.StatsPullAtomService$$ExternalSyntheticLambda12
            public final void onUidStorageStats(int i, long j, long j2, long j3, long j4, long j5, long j6, long j7, long j8, long j9, long j10) {
                pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, i, j, j2, j3, j4, j5, j6, j7, j8, j9, j10));
            }
        });
        return 0;
    }

    private void registerPowerProfile() {
        this.mStatsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.POWER_PROFILE, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullPowerProfileLocked(int atomTag, java.util.List<android.util.StatsEvent> pulledData) {
        com.android.internal.os.PowerProfile powerProfile = new com.android.internal.os.PowerProfile(this.mContext);
        android.util.proto.ProtoOutputStream proto = new android.util.proto.ProtoOutputStream();
        powerProfile.dumpDebug(proto);
        proto.flush();
        pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, proto.getBytes()));
        return 0;
    }

    private void registerProcessCpuTime() {
        android.app.StatsManager.PullAtomMetadata metadata = new android.app.StatsManager.PullAtomMetadata.Builder().setCoolDownMillis(5000L).build();
        this.mStatsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.PROCESS_CPU_TIME, metadata, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullProcessCpuTimeLocked(int atomTag, java.util.List<android.util.StatsEvent> pulledData) {
        if (this.mProcessCpuTracker == null) {
            this.mProcessCpuTracker = new com.android.internal.os.ProcessCpuTracker(false);
            this.mProcessCpuTracker.init();
        }
        this.mProcessCpuTracker.update();
        for (int i = 0; i < this.mProcessCpuTracker.countStats(); i++) {
            com.android.internal.os.ProcessCpuTracker.Stats st = this.mProcessCpuTracker.getStats(i);
            pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, st.uid, st.name, st.base_utime, st.base_stime));
        }
        return 0;
    }

    private void registerCpuTimePerThreadFreq() {
        android.app.StatsManager.PullAtomMetadata metadata = new android.app.StatsManager.PullAtomMetadata.Builder().setAdditiveFields(new int[]{7, 9, 11, 13, 15, 17, 19, 21}).build();
        this.mStatsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.CPU_TIME_PER_THREAD_FREQ, metadata, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullCpuTimePerThreadFreqLocked(int atomTag, java.util.List<android.util.StatsEvent> pulledData) {
        if (this.mKernelCpuThreadReader == null) {
            android.util.Slog.e(TAG, "mKernelCpuThreadReader is null");
            return 1;
        }
        java.util.ArrayList<com.android.internal.os.KernelCpuThreadReader.ProcessCpuUsage> processCpuUsages = this.mKernelCpuThreadReader.getProcessCpuUsageDiffed();
        if (processCpuUsages == null) {
            android.util.Slog.e(TAG, "processCpuUsages is null");
            return 1;
        }
        int[] cpuFrequencies = this.mKernelCpuThreadReader.getCpuFrequenciesKhz();
        if (cpuFrequencies.length > 8) {
            java.lang.String message = "Expected maximum 8 frequencies, but got " + cpuFrequencies.length;
            android.util.Slog.w(TAG, message);
            return 1;
        }
        for (int i = 0; i < processCpuUsages.size(); i++) {
            com.android.internal.os.KernelCpuThreadReader.ProcessCpuUsage processCpuUsage = processCpuUsages.get(i);
            java.util.ArrayList<com.android.internal.os.KernelCpuThreadReader.ThreadCpuUsage> threadCpuUsages = processCpuUsage.threadCpuUsages;
            for (int j = 0; j < threadCpuUsages.size(); j++) {
                com.android.internal.os.KernelCpuThreadReader.ThreadCpuUsage threadCpuUsage = threadCpuUsages.get(j);
                if (threadCpuUsage.usageTimesMillis.length != cpuFrequencies.length) {
                    java.lang.String message2 = "Unexpected number of usage times, expected " + cpuFrequencies.length + " but got " + threadCpuUsage.usageTimesMillis.length;
                    android.util.Slog.w(TAG, message2);
                    return 1;
                }
                int[] frequencies = new int[8];
                int[] usageTimesMillis = new int[8];
                for (int k = 0; k < 8; k++) {
                    if (k < cpuFrequencies.length) {
                        frequencies[k] = cpuFrequencies[k];
                        usageTimesMillis[k] = threadCpuUsage.usageTimesMillis[k];
                    } else {
                        frequencies[k] = 0;
                        usageTimesMillis[k] = 0;
                    }
                }
                int k2 = processCpuUsage.uid;
                pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, k2, processCpuUsage.processId, threadCpuUsage.threadId, processCpuUsage.processName, threadCpuUsage.threadName, frequencies[0], usageTimesMillis[0], frequencies[1], usageTimesMillis[1], frequencies[2], usageTimesMillis[2], frequencies[3], usageTimesMillis[3], frequencies[4], usageTimesMillis[4], frequencies[5], usageTimesMillis[5], frequencies[6], usageTimesMillis[6], frequencies[7], usageTimesMillis[7]));
            }
        }
        return 0;
    }

    private long milliAmpHrsToNanoAmpSecs(double mAh) {
        return (long) ((3.6E9d * mAh) + 0.5d);
    }

    private void registerDeviceCalculatedPowerUse() {
        this.mStatsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.DEVICE_CALCULATED_POWER_USE, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullDeviceCalculatedPowerUseLocked(int atomTag, java.util.List<android.util.StatsEvent> pulledData) {
        android.os.BatteryStatsManager bsm = (android.os.BatteryStatsManager) this.mContext.getSystemService(android.os.BatteryStatsManager.class);
        try {
            android.os.BatteryUsageStats stats = bsm.getBatteryUsageStats();
            pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, milliAmpHrsToNanoAmpSecs(stats.getConsumedPower())));
            return 0;
        } catch (java.lang.Exception e) {
            android.util.Log.e(TAG, "Could not obtain battery usage stats", e);
            return 1;
        }
    }

    private void registerDebugElapsedClock() {
        android.app.StatsManager.PullAtomMetadata metadata = new android.app.StatsManager.PullAtomMetadata.Builder().setAdditiveFields(new int[]{1, 2, 3, 4}).build();
        this.mStatsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.DEBUG_ELAPSED_CLOCK, metadata, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullDebugElapsedClockLocked(int atomTag, java.util.List<android.util.StatsEvent> pulledData) {
        long clockDiffMillis;
        long elapsedMillis;
        long elapsedMillis2 = android.os.SystemClock.elapsedRealtime();
        if (this.mDebugElapsedClockPreviousValue != 0) {
            clockDiffMillis = elapsedMillis2 - this.mDebugElapsedClockPreviousValue;
        } else {
            clockDiffMillis = 0;
        }
        pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, this.mDebugElapsedClockPullCount, elapsedMillis2, elapsedMillis2, clockDiffMillis, 1));
        if (this.mDebugElapsedClockPullCount % 2 != 1) {
            elapsedMillis = elapsedMillis2;
        } else {
            elapsedMillis = elapsedMillis2;
            pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, this.mDebugElapsedClockPullCount, elapsedMillis2, elapsedMillis, clockDiffMillis, 2));
        }
        this.mDebugElapsedClockPullCount++;
        this.mDebugElapsedClockPreviousValue = elapsedMillis;
        return 0;
    }

    private void registerDebugFailingElapsedClock() {
        android.app.StatsManager.PullAtomMetadata metadata = new android.app.StatsManager.PullAtomMetadata.Builder().setAdditiveFields(new int[]{1, 2, 3, 4}).build();
        this.mStatsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.DEBUG_FAILING_ELAPSED_CLOCK, metadata, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullDebugFailingElapsedClockLocked(int atomTag, java.util.List<android.util.StatsEvent> pulledData) {
        long j;
        long elapsedMillis = android.os.SystemClock.elapsedRealtime();
        long j2 = this.mDebugFailingElapsedClockPullCount;
        this.mDebugFailingElapsedClockPullCount = 1 + j2;
        if (j2 % 5 == 0) {
            this.mDebugFailingElapsedClockPreviousValue = elapsedMillis;
            android.util.Slog.e(TAG, "Failing debug elapsed clock");
            return 1;
        }
        long j3 = this.mDebugFailingElapsedClockPullCount;
        if (this.mDebugFailingElapsedClockPreviousValue == 0) {
            j = 0;
        } else {
            j = elapsedMillis - this.mDebugFailingElapsedClockPreviousValue;
        }
        pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, j3, elapsedMillis, elapsedMillis, j));
        this.mDebugFailingElapsedClockPreviousValue = elapsedMillis;
        return 0;
    }

    private void registerBuildInformation() {
        this.mStatsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.BUILD_INFORMATION, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullBuildInformationLocked(int atomTag, java.util.List<android.util.StatsEvent> pulledData) {
        pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, android.os.Build.FINGERPRINT, android.os.Build.BRAND, android.os.Build.PRODUCT, android.os.Build.DEVICE, android.os.Build.VERSION.RELEASE_OR_CODENAME, android.os.Build.ID, android.os.Build.VERSION.INCREMENTAL, android.os.Build.TYPE, android.os.Build.TAGS));
        return 0;
    }

    private void registerRoleHolder() {
        this.mStatsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.ROLE_HOLDER, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    /* JADX WARN: Finally extract failed */
    int pullRoleHolderLocked(int atomTag, java.util.List<android.util.StatsEvent> pulledData) {
        android.content.pm.PackageManager pm;
        long callingToken = android.os.Binder.clearCallingIdentity();
        try {
            android.content.pm.PackageManager pm2 = this.mContext.getPackageManager();
            com.android.role.RoleManagerLocal roleManagerLocal = (com.android.role.RoleManagerLocal) com.android.server.LocalManagerRegistry.getManager(com.android.role.RoleManagerLocal.class);
            java.util.List<android.content.pm.UserInfo> users = ((android.os.UserManager) this.mContext.getSystemService(android.os.UserManager.class)).getUsers();
            int numUsers = users.size();
            int userNum = 0;
            while (true) {
                int i = 0;
                if (userNum < numUsers) {
                    int userId = users.get(userNum).getUserHandle().getIdentifier();
                    java.util.Map<java.lang.String, java.util.Set<java.lang.String>> roles = roleManagerLocal.getRolesAndHolders(userId);
                    for (java.util.Map.Entry<java.lang.String, java.util.Set<java.lang.String>> roleEntry : roles.entrySet()) {
                        java.lang.String roleName = roleEntry.getKey();
                        java.util.Set<java.lang.String> packageNames = roleEntry.getValue();
                        if (!packageNames.isEmpty()) {
                            for (java.lang.String packageName : packageNames) {
                                try {
                                    android.content.pm.PackageInfo pkg = pm2.getPackageInfoAsUser(packageName, i, userId);
                                    android.content.pm.PackageManager pm3 = pm2;
                                    pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, pkg.applicationInfo.uid, packageName, roleName));
                                    i = 0;
                                    pm2 = pm3;
                                } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                                    android.util.Slog.w(TAG, "Role holder " + packageName + " not found");
                                    android.os.Binder.restoreCallingIdentity(callingToken);
                                    return 1;
                                }
                            }
                            pm = pm2;
                        } else {
                            pm = pm2;
                            pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, -1, "", roleName));
                        }
                        i = 0;
                        pm2 = pm;
                    }
                    userNum++;
                } else {
                    android.os.Binder.restoreCallingIdentity(callingToken);
                    return 0;
                }
            }
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(callingToken);
            throw th;
        }
    }

    private void registerDangerousPermissionState() {
        this.mStatsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.DANGEROUS_PERMISSION_STATE, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullDangerousPermissionStateLocked(int atomTag, java.util.List<android.util.StatsEvent> pulledData) {
        int pkgNum;
        int numPkgs;
        java.util.List<android.content.pm.PackageInfo> pkgs;
        android.os.UserHandle user;
        int userNum;
        int numUsers;
        java.util.List<android.content.pm.UserInfo> users;
        android.content.pm.PackageManager pm;
        java.util.Set<java.lang.Integer> reportedUids;
        android.content.pm.PackageInfo pkg;
        int pkgNum2;
        int numPkgs2;
        java.util.List<android.content.pm.PackageInfo> pkgs2;
        android.os.UserHandle user2;
        int numPerms;
        int permNum;
        int numUsers2;
        java.util.List<android.content.pm.UserInfo> users2;
        android.content.pm.PackageManager pm2;
        int userNum2;
        java.util.Set<java.lang.Integer> reportedUids2;
        android.content.pm.PermissionInfo permissionInfo;
        android.util.StatsEvent e;
        int i = atomTag;
        long token = android.os.Binder.clearCallingIdentity();
        float samplingRate = android.provider.DeviceConfig.getFloat("permissions", DANGEROUS_PERMISSION_STATE_SAMPLE_RATE, 0.015f);
        java.util.Set<java.lang.Integer> reportedUids3 = new java.util.HashSet<>();
        try {
            android.content.pm.PackageManager pm3 = this.mContext.getPackageManager();
            java.util.List<android.content.pm.UserInfo> users3 = ((android.os.UserManager) this.mContext.getSystemService(android.os.UserManager.class)).getUsers();
            int numUsers3 = users3.size();
            int userNum3 = 0;
            while (userNum3 < numUsers3) {
                android.os.UserHandle user3 = users3.get(userNum3).getUserHandle();
                java.util.List<android.content.pm.PackageInfo> pkgs3 = pm3.getInstalledPackagesAsUser(4096, user3.getIdentifier());
                int numPkgs3 = pkgs3.size();
                int pkgNum3 = 0;
                while (pkgNum3 < numPkgs3) {
                    android.content.pm.PackageInfo pkg2 = pkgs3.get(pkgNum3);
                    if (pkg2.requestedPermissions == null) {
                        pkgNum = pkgNum3;
                        numPkgs = numPkgs3;
                        pkgs = pkgs3;
                        user = user3;
                        userNum = userNum3;
                        numUsers = numUsers3;
                        users = users3;
                        pm = pm3;
                        reportedUids = reportedUids3;
                    } else if (reportedUids3.contains(java.lang.Integer.valueOf(pkg2.applicationInfo.uid))) {
                        pkgNum = pkgNum3;
                        numPkgs = numPkgs3;
                        pkgs = pkgs3;
                        user = user3;
                        userNum = userNum3;
                        numUsers = numUsers3;
                        users = users3;
                        pm = pm3;
                        reportedUids = reportedUids3;
                    } else {
                        reportedUids3.add(java.lang.Integer.valueOf(pkg2.applicationInfo.uid));
                        if (i == 10067) {
                            try {
                                if (java.util.concurrent.ThreadLocalRandom.current().nextFloat() > samplingRate) {
                                    pkgNum = pkgNum3;
                                    numPkgs = numPkgs3;
                                    pkgs = pkgs3;
                                    user = user3;
                                    userNum = userNum3;
                                    numUsers = numUsers3;
                                    users = users3;
                                    pm = pm3;
                                    reportedUids = reportedUids3;
                                }
                            } catch (java.lang.Throwable th) {
                                t = th;
                                try {
                                    android.util.Log.e(TAG, "Could not read permissions", t);
                                    return 1;
                                } finally {
                                    android.os.Binder.restoreCallingIdentity(token);
                                }
                            }
                        }
                        int numPerms2 = pkg2.requestedPermissions.length;
                        int permNum2 = 0;
                        while (permNum2 < numPerms2) {
                            int userNum4 = userNum3;
                            java.lang.String permName = pkg2.requestedPermissions[permNum2];
                            try {
                                permissionInfo = pm3.getPermissionInfo(permName, 0);
                            } catch (android.content.pm.PackageManager.NameNotFoundException e2) {
                                pkg = pkg2;
                                pkgNum2 = pkgNum3;
                                numPkgs2 = numPkgs3;
                                pkgs2 = pkgs3;
                                user2 = user3;
                                numPerms = numPerms2;
                                permNum = permNum2;
                                numUsers2 = numUsers3;
                                users2 = users3;
                                pm2 = pm3;
                                userNum2 = userNum4;
                            }
                            try {
                                int permissionFlags = pm3.getPermissionFlags(permName, pkg2.packageName, user3);
                                numPerms = numPerms2;
                                if (permName.startsWith(COMMON_PERMISSION_PREFIX)) {
                                    permName = permName.substring(COMMON_PERMISSION_PREFIX.length());
                                }
                                if (i == 10050) {
                                    pkgNum2 = pkgNum3;
                                    pkg = pkg2;
                                    numPkgs2 = numPkgs3;
                                    pkgs2 = pkgs3;
                                    user2 = user3;
                                    permNum = permNum2;
                                    e = com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, permName, pkg2.applicationInfo.uid, "", (pkg2.requestedPermissionsFlags[permNum2] & 2) != 0, permissionFlags, permissionInfo.getProtection() | permissionInfo.getProtectionFlags());
                                    numUsers2 = numUsers3;
                                    users2 = users3;
                                    pm2 = pm3;
                                    userNum2 = userNum4;
                                    reportedUids2 = reportedUids3;
                                } else {
                                    pkg = pkg2;
                                    pkgNum2 = pkgNum3;
                                    numPkgs2 = numPkgs3;
                                    pkgs2 = pkgs3;
                                    user2 = user3;
                                    permNum = permNum2;
                                    userNum2 = userNum4;
                                    numUsers2 = numUsers3;
                                    users2 = users3;
                                    pm2 = pm3;
                                    reportedUids2 = reportedUids3;
                                    try {
                                        e = com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, permName, pkg.applicationInfo.uid, (pkg.requestedPermissionsFlags[permNum] & 2) != 0, permissionFlags, permissionInfo.getProtection() | permissionInfo.getProtectionFlags());
                                    } catch (java.lang.Throwable th2) {
                                        t = th2;
                                        android.util.Log.e(TAG, "Could not read permissions", t);
                                        return 1;
                                    }
                                }
                            } catch (android.content.pm.PackageManager.NameNotFoundException e3) {
                                pkg = pkg2;
                                pkgNum2 = pkgNum3;
                                numPkgs2 = numPkgs3;
                                pkgs2 = pkgs3;
                                user2 = user3;
                                numPerms = numPerms2;
                                permNum = permNum2;
                                numUsers2 = numUsers3;
                                users2 = users3;
                                pm2 = pm3;
                                userNum2 = userNum4;
                                reportedUids2 = reportedUids3;
                            }
                            try {
                                pulledData.add(e);
                                permNum2 = permNum + 1;
                                users3 = users2;
                                pm3 = pm2;
                                userNum3 = userNum2;
                                numUsers3 = numUsers2;
                                pkg2 = pkg;
                                reportedUids3 = reportedUids2;
                                numPerms2 = numPerms;
                                pkgNum3 = pkgNum2;
                                numPkgs3 = numPkgs2;
                                pkgs3 = pkgs2;
                                user3 = user2;
                                i = atomTag;
                            } catch (java.lang.Throwable th3) {
                                t = th3;
                                android.util.Log.e(TAG, "Could not read permissions", t);
                                return 1;
                            }
                        }
                        pkgNum = pkgNum3;
                        numPkgs = numPkgs3;
                        pkgs = pkgs3;
                        user = user3;
                        userNum = userNum3;
                        numUsers = numUsers3;
                        users = users3;
                        pm = pm3;
                        reportedUids = reportedUids3;
                    }
                    i = atomTag;
                    users3 = users;
                    pm3 = pm;
                    userNum3 = userNum;
                    numUsers3 = numUsers;
                    reportedUids3 = reportedUids;
                    numPkgs3 = numPkgs;
                    pkgs3 = pkgs;
                    user3 = user;
                    pkgNum3 = pkgNum + 1;
                }
                userNum3++;
                i = atomTag;
            }
            return 0;
        } catch (java.lang.Throwable th4) {
            t = th4;
        }
    }

    private void registerTimeZoneDataInfo() {
        this.mStatsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.TIME_ZONE_DATA_INFO, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullTimeZoneDataInfoLocked(int atomTag, java.util.List<android.util.StatsEvent> pulledData) {
        try {
            java.lang.String tzDbVersion = android.icu.util.TimeZone.getTZDataVersion();
            pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, tzDbVersion));
            return 0;
        } catch (java.util.MissingResourceException e) {
            android.util.Slog.e(TAG, "Getting tzdb version failed: ", e);
            return 1;
        }
    }

    private void registerTimeZoneDetectorState() {
        this.mStatsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.TIME_ZONE_DETECTOR_STATE, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullTimeZoneDetectorStateLocked(int atomTag, java.util.List<android.util.StatsEvent> pulledData) {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            try {
                com.android.server.timezonedetector.TimeZoneDetectorInternal timeZoneDetectorInternal = (com.android.server.timezonedetector.TimeZoneDetectorInternal) com.android.server.LocalServices.getService(com.android.server.timezonedetector.TimeZoneDetectorInternal.class);
                com.android.server.timezonedetector.MetricsTimeZoneDetectorState metricsState = timeZoneDetectorInternal.generateMetricsState();
                try {
                    pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, metricsState.isTelephonyDetectionSupported(), metricsState.isGeoDetectionSupported(), metricsState.getUserLocationEnabledSetting(), metricsState.getAutoDetectionEnabledSetting(), metricsState.getGeoDetectionEnabledSetting(), convertToMetricsDetectionMode(metricsState.getDetectionMode()), metricsState.getDeviceTimeZoneIdOrdinal(), convertTimeZoneSuggestionToProtoBytes(metricsState.getLatestManualSuggestion()), convertTimeZoneSuggestionToProtoBytes(metricsState.getLatestTelephonySuggestion()), convertTimeZoneSuggestionToProtoBytes(metricsState.getLatestGeolocationSuggestion()), metricsState.isTelephonyTimeZoneFallbackSupported(), metricsState.getDeviceTimeZoneId(), metricsState.isEnhancedMetricsCollectionEnabled(), metricsState.getGeoDetectionRunInBackgroundEnabled()));
                    android.os.Binder.restoreCallingIdentity(token);
                    return 0;
                } catch (java.lang.RuntimeException e) {
                    e = e;
                    android.util.Slog.e(TAG, "Getting time zone detection state failed: ", e);
                    android.os.Binder.restoreCallingIdentity(token);
                    return 1;
                }
            } catch (java.lang.Throwable th) {
                e = th;
                android.os.Binder.restoreCallingIdentity(token);
                throw e;
            }
        } catch (java.lang.RuntimeException e2) {
            e = e2;
        } catch (java.lang.Throwable th2) {
            e = th2;
            android.os.Binder.restoreCallingIdentity(token);
            throw e;
        }
    }

    private static int convertToMetricsDetectionMode(int detectionMode) {
        switch (detectionMode) {
            case 1:
                return 1;
            case 2:
                return 3;
            case 3:
                return 2;
            default:
                return 0;
        }
    }

    private static byte[] convertTimeZoneSuggestionToProtoBytes(com.android.server.timezonedetector.MetricsTimeZoneDetectorState.MetricsTimeZoneSuggestion suggestion) {
        int typeProtoValue;
        if (suggestion == null) {
            return null;
        }
        java.io.ByteArrayOutputStream byteArrayOutputStream = new java.io.ByteArrayOutputStream();
        android.util.proto.ProtoOutputStream protoOutputStream = new android.util.proto.ProtoOutputStream(byteArrayOutputStream);
        if (suggestion.isCertain()) {
            typeProtoValue = 1;
        } else {
            typeProtoValue = 2;
        }
        protoOutputStream.write(1159641169921L, typeProtoValue);
        if (suggestion.isCertain()) {
            for (int zoneIdOrdinal : suggestion.getZoneIdOrdinals()) {
                protoOutputStream.write(2220498092034L, zoneIdOrdinal);
            }
            java.lang.String[] zoneIds = suggestion.getZoneIds();
            if (zoneIds != null) {
                for (java.lang.String zoneId : zoneIds) {
                    protoOutputStream.write(2237677961219L, zoneId);
                }
            }
        }
        protoOutputStream.flush();
        libcore.io.IoUtils.closeQuietly(byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void registerExternalStorageInfo() {
        this.mStatsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.EXTERNAL_STORAGE_INFO, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullExternalStorageInfoLocked(int atomTag, java.util.List<android.util.StatsEvent> pulledData) {
        int externalStorageType;
        if (this.mStorageManager == null) {
            return 1;
        }
        java.util.List<android.os.storage.VolumeInfo> volumes = this.mStorageManager.getVolumes();
        for (android.os.storage.VolumeInfo vol : volumes) {
            java.lang.String envState = android.os.storage.VolumeInfo.getEnvironmentForState(vol.getState());
            android.os.storage.DiskInfo diskInfo = vol.getDisk();
            if (diskInfo != null && envState.equals("mounted")) {
                int volumeType = 3;
                if (vol.getType() == 0) {
                    volumeType = 1;
                } else if (vol.getType() == 1) {
                    volumeType = 2;
                }
                if (diskInfo.isSd()) {
                    externalStorageType = 1;
                } else if (diskInfo.isUsb()) {
                    externalStorageType = 2;
                } else {
                    externalStorageType = 3;
                }
                pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, externalStorageType, volumeType, diskInfo.size));
            }
        }
        return 0;
    }

    private void registerAppsOnExternalStorageInfo() {
        this.mStatsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.APPS_ON_EXTERNAL_STORAGE_INFO, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullAppsOnExternalStorageInfoLocked(int atomTag, java.util.List<android.util.StatsEvent> pulledData) {
        android.os.storage.VolumeInfo volumeInfo;
        android.os.storage.DiskInfo diskInfo;
        if (this.mStorageManager == null) {
            return 1;
        }
        android.content.pm.PackageManager pm = this.mContext.getPackageManager();
        java.util.List<android.content.pm.ApplicationInfo> apps = pm.getInstalledApplications(0);
        for (android.content.pm.ApplicationInfo appInfo : apps) {
            java.util.UUID storageUuid = appInfo.storageUuid;
            if (storageUuid != null && (volumeInfo = this.mStorageManager.findVolumeByUuid(appInfo.storageUuid.toString())) != null && (diskInfo = volumeInfo.getDisk()) != null) {
                int externalStorageType = -1;
                if (diskInfo.isSd()) {
                    externalStorageType = 1;
                } else if (diskInfo.isUsb()) {
                    externalStorageType = 2;
                } else if (appInfo.isExternal()) {
                    externalStorageType = 3;
                }
                if (externalStorageType != -1) {
                    pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, externalStorageType, appInfo.packageName));
                }
            }
        }
        return 0;
    }

    private void registerFaceSettings() {
        this.mStatsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.FACE_SETTINGS, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    /* JADX WARN: Multi-variable type inference failed */
    int pullFaceSettingsLocked(int atomTag, java.util.List<android.util.StatsEvent> pulledData) throws java.lang.Throwable {
        long callingToken = android.os.Binder.clearCallingIdentity();
        try {
            android.os.UserManager manager = (android.os.UserManager) this.mContext.getSystemService(android.os.UserManager.class);
            int i = 1;
            if (manager == null) {
                android.os.Binder.restoreCallingIdentity(callingToken);
                return 1;
            }
            java.util.List<android.content.pm.UserInfo> users = manager.getUsers();
            int numUsers = users.size();
            int userNum = 0;
            while (userNum < numUsers) {
                int userId = users.get(userNum).getUserHandle().getIdentifier();
                int unlockKeyguardEnabled = android.provider.Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "face_unlock_keyguard_enabled", i, userId);
                int unlockDismissesKeyguard = android.provider.Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "face_unlock_dismisses_keyguard", i, userId);
                int unlockAttentionRequired = android.provider.Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "face_unlock_attention_required", 0, userId);
                int unlockAppEnabled = android.provider.Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "face_unlock_app_enabled", i, userId);
                int unlockAlwaysRequireConfirmation = android.provider.Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "face_unlock_always_require_confirmation", 0, userId);
                int unlockDiversityRequired = android.provider.Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "face_unlock_diversity_required", i, userId);
                try {
                    pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, unlockKeyguardEnabled != 0 ? i : 0, unlockDismissesKeyguard != 0 ? i : 0, unlockAttentionRequired != 0 ? i : 0, unlockAppEnabled != 0 ? i : 0, unlockAlwaysRequireConfirmation != 0 ? i : 0, unlockDiversityRequired != 0 ? i : 0));
                    userNum++;
                    i = 1;
                } catch (java.lang.Throwable th) {
                    th = th;
                }
            }
            android.os.Binder.restoreCallingIdentity(callingToken);
            return 0;
        } catch (java.lang.Throwable th2) {
            th = th2;
        }
        android.os.Binder.restoreCallingIdentity(callingToken);
        throw th;
    }

    private void registerAppOps() {
        this.mStatsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.APP_OPS, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    private void registerRuntimeAppOpAccessMessage() {
        this.mStatsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.RUNTIME_APP_OP_ACCESS, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    private class AppOpEntry {
        public final java.lang.String mAttributionTag;
        public final int mHash;
        public final android.app.AppOpsManager.HistoricalOp mOp;
        public final java.lang.String mPackageName;
        public final int mUid;

        AppOpEntry(java.lang.String packageName, java.lang.String attributionTag, android.app.AppOpsManager.HistoricalOp op, int uid) {
            this.mPackageName = packageName;
            this.mAttributionTag = attributionTag;
            this.mUid = uid;
            this.mOp = op;
            this.mHash = ((packageName.hashCode() + com.android.server.stats.pull.StatsPullAtomService.RANDOM_SEED) & Integer.MAX_VALUE) % 100;
        }
    }

    int pullAppOpsLocked(int atomTag, java.util.List<android.util.StatsEvent> pulledData) {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            android.app.AppOpsManager appOps = (android.app.AppOpsManager) this.mContext.getSystemService(android.app.AppOpsManager.class);
            java.util.concurrent.CompletableFuture<android.app.AppOpsManager.HistoricalOps> ops = new java.util.concurrent.CompletableFuture<>();
            android.app.AppOpsManager.HistoricalOpsRequest histOpsRequest = new android.app.AppOpsManager.HistoricalOpsRequest.Builder(0L, Long.MAX_VALUE).setFlags(9).build();
            java.util.concurrent.Executor executor = android.os.AsyncTask.THREAD_POOL_EXECUTOR;
            java.util.Objects.requireNonNull(ops);
            appOps.getHistoricalOps(histOpsRequest, executor, new com.android.server.stats.pull.StatsPullAtomService$$ExternalSyntheticLambda0(ops));
            android.app.AppOpsManager.HistoricalOps histOps = ops.get(EXTERNAL_STATS_SYNC_TIMEOUT_MILLIS, java.util.concurrent.TimeUnit.MILLISECONDS);
            java.util.List<com.android.server.stats.pull.StatsPullAtomService.AppOpEntry> opsList = processHistoricalOps(histOps, atomTag, 100);
            int samplingRate = sampleAppOps(pulledData, opsList, atomTag, 100);
            if (samplingRate != 100) {
                android.util.Slog.e(TAG, "Atom 10060 downsampled - too many dimensions");
            }
            android.os.Binder.restoreCallingIdentity(token);
            return 0;
        } catch (java.lang.Throwable t) {
            try {
                android.util.Slog.e(TAG, "Could not read appops", t);
                android.os.Binder.restoreCallingIdentity(token);
                return 1;
            } catch (java.lang.Throwable th) {
                android.os.Binder.restoreCallingIdentity(token);
                throw th;
            }
        }
    }

    private int sampleAppOps(java.util.List<android.util.StatsEvent> pulledData, java.util.List<com.android.server.stats.pull.StatsPullAtomService.AppOpEntry> opsList, int atomTag, int samplingRate) {
        int i;
        int nOps;
        android.util.StatsEvent e;
        java.util.List<android.util.StatsEvent> list = pulledData;
        java.util.List<com.android.server.stats.pull.StatsPullAtomService.AppOpEntry> list2 = opsList;
        int i2 = atomTag;
        int i3 = samplingRate;
        int nOps2 = opsList.size();
        int i4 = 0;
        while (i4 < nOps2) {
            com.android.server.stats.pull.StatsPullAtomService.AppOpEntry entry = list2.get(i4);
            if (entry.mHash >= i3) {
                i = i4;
                nOps = nOps2;
            } else {
                if (i2 == 10075) {
                    i = i4;
                    nOps = nOps2;
                    e = com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, entry.mUid, entry.mPackageName, entry.mAttributionTag, entry.mOp.getOpCode(), entry.mOp.getForegroundAccessCount(9), entry.mOp.getBackgroundAccessCount(9), entry.mOp.getForegroundRejectCount(9), entry.mOp.getBackgroundRejectCount(9), entry.mOp.getForegroundAccessDuration(9), entry.mOp.getBackgroundAccessDuration(9), this.mDangerousAppOpsList.contains(java.lang.Integer.valueOf(entry.mOp.getOpCode())), samplingRate);
                } else {
                    i = i4;
                    nOps = nOps2;
                    e = com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, entry.mUid, entry.mPackageName, entry.mOp.getOpCode(), entry.mOp.getForegroundAccessCount(9), entry.mOp.getBackgroundAccessCount(9), entry.mOp.getForegroundRejectCount(9), entry.mOp.getBackgroundRejectCount(9), entry.mOp.getForegroundAccessDuration(9), entry.mOp.getBackgroundAccessDuration(9), this.mDangerousAppOpsList.contains(java.lang.Integer.valueOf(entry.mOp.getOpCode())));
                }
                list = pulledData;
                list.add(e);
            }
            i4 = i + 1;
            list2 = opsList;
            i2 = atomTag;
            i3 = samplingRate;
            nOps2 = nOps;
        }
        if (pulledData.size() <= 800) {
            return samplingRate;
        }
        int adjustedSamplingRate = android.util.MathUtils.constrain((samplingRate * 500) / pulledData.size(), 0, samplingRate - 1);
        pulledData.clear();
        return sampleAppOps(list, opsList, atomTag, adjustedSamplingRate);
    }

    private void registerAttributedAppOps() {
        this.mStatsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.ATTRIBUTED_APP_OPS, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullAttributedAppOpsLocked(int atomTag, java.util.List<android.util.StatsEvent> pulledData) {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            android.app.AppOpsManager appOps = (android.app.AppOpsManager) this.mContext.getSystemService(android.app.AppOpsManager.class);
            java.util.concurrent.CompletableFuture<android.app.AppOpsManager.HistoricalOps> ops = new java.util.concurrent.CompletableFuture<>();
            android.app.AppOpsManager.HistoricalOpsRequest histOpsRequest = new android.app.AppOpsManager.HistoricalOpsRequest.Builder(0L, Long.MAX_VALUE).setFlags(9).build();
            java.util.concurrent.Executor executor = android.os.AsyncTask.THREAD_POOL_EXECUTOR;
            java.util.Objects.requireNonNull(ops);
            appOps.getHistoricalOps(histOpsRequest, executor, new com.android.server.stats.pull.StatsPullAtomService$$ExternalSyntheticLambda0(ops));
            android.app.AppOpsManager.HistoricalOps histOps = ops.get(EXTERNAL_STATS_SYNC_TIMEOUT_MILLIS, java.util.concurrent.TimeUnit.MILLISECONDS);
            if (this.mAppOpsSamplingRate == 0) {
                this.mContext.getMainThreadHandler().postDelayed(new java.lang.Runnable() { // from class: com.android.server.stats.pull.StatsPullAtomService.4
                    @Override // java.lang.Runnable
                    public void run() {
                        try {
                            com.android.server.stats.pull.StatsPullAtomService.this.estimateAppOpsSamplingRate();
                        } finally {
                        }
                    }
                }, APP_OPS_SAMPLING_INITIALIZATION_DELAY_MILLIS);
                this.mAppOpsSamplingRate = 100;
            }
            java.util.List<com.android.server.stats.pull.StatsPullAtomService.AppOpEntry> opsList = processHistoricalOps(histOps, atomTag, this.mAppOpsSamplingRate);
            int newSamplingRate = sampleAppOps(pulledData, opsList, atomTag, this.mAppOpsSamplingRate);
            this.mAppOpsSamplingRate = java.lang.Math.min(this.mAppOpsSamplingRate, newSamplingRate);
            android.os.Binder.restoreCallingIdentity(token);
            return 0;
        } catch (java.lang.Throwable t) {
            try {
                android.util.Slog.e(TAG, "Could not read appops", t);
                android.os.Binder.restoreCallingIdentity(token);
                return 1;
            } catch (java.lang.Throwable t2) {
                android.os.Binder.restoreCallingIdentity(token);
                throw t2;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void estimateAppOpsSamplingRate() throws java.lang.Exception {
        int appOpsTargetCollectionSize = android.provider.DeviceConfig.getInt("permissions", APP_OPS_TARGET_COLLECTION_SIZE, 2000);
        android.app.AppOpsManager appOps = (android.app.AppOpsManager) this.mContext.getSystemService(android.app.AppOpsManager.class);
        java.util.concurrent.CompletableFuture<android.app.AppOpsManager.HistoricalOps> ops = new java.util.concurrent.CompletableFuture<>();
        android.app.AppOpsManager.HistoricalOpsRequest histOpsRequest = new android.app.AppOpsManager.HistoricalOpsRequest.Builder(java.lang.Math.max(java.time.Instant.now().minus(1L, (java.time.temporal.TemporalUnit) java.time.temporal.ChronoUnit.DAYS).toEpochMilli(), 0L), Long.MAX_VALUE).setFlags(9).build();
        java.util.concurrent.Executor executor = android.os.AsyncTask.THREAD_POOL_EXECUTOR;
        java.util.Objects.requireNonNull(ops);
        appOps.getHistoricalOps(histOpsRequest, executor, new com.android.server.stats.pull.StatsPullAtomService$$ExternalSyntheticLambda0(ops));
        android.app.AppOpsManager.HistoricalOps histOps = ops.get(EXTERNAL_STATS_SYNC_TIMEOUT_MILLIS, java.util.concurrent.TimeUnit.MILLISECONDS);
        java.util.List<com.android.server.stats.pull.StatsPullAtomService.AppOpEntry> opsList = processHistoricalOps(histOps, com.android.internal.util.FrameworkStatsLog.ATTRIBUTED_APP_OPS, 100);
        long estimatedSize = 0;
        int nOps = opsList.size();
        for (int i = 0; i < nOps; i++) {
            com.android.server.stats.pull.StatsPullAtomService.AppOpEntry entry = opsList.get(i);
            estimatedSize += (long) (entry.mPackageName.length() + 32 + (entry.mAttributionTag == null ? 1 : entry.mAttributionTag.length()));
        }
        int i2 = appOpsTargetCollectionSize * 100;
        int estimatedSamplingRate = (int) android.util.MathUtils.constrain(((long) i2) / estimatedSize, 0L, 100L);
        synchronized (this.mAttributedAppOpsLock) {
            this.mAppOpsSamplingRate = java.lang.Math.min(this.mAppOpsSamplingRate, estimatedSamplingRate);
        }
    }

    private java.util.List<com.android.server.stats.pull.StatsPullAtomService.AppOpEntry> processHistoricalOps(android.app.AppOpsManager.HistoricalOps histOps, int atomTag, int samplingRatio) {
        java.util.List<com.android.server.stats.pull.StatsPullAtomService.AppOpEntry> opsList = new java.util.ArrayList<>();
        for (int uidIdx = 0; uidIdx < histOps.getUidCount(); uidIdx++) {
            android.app.AppOpsManager.HistoricalUidOps uidOps = histOps.getUidOpsAt(uidIdx);
            int uid = uidOps.getUid();
            for (int pkgIdx = 0; pkgIdx < uidOps.getPackageCount(); pkgIdx++) {
                android.app.AppOpsManager.HistoricalPackageOps packageOps = uidOps.getPackageOpsAt(pkgIdx);
                if (atomTag != 10075) {
                    if (atomTag == 10060) {
                        for (int opIdx = 0; opIdx < packageOps.getOpCount(); opIdx++) {
                            android.app.AppOpsManager.HistoricalOp op = packageOps.getOpAt(opIdx);
                            processHistoricalOp(op, opsList, uid, samplingRatio, packageOps.getPackageName(), null);
                        }
                    }
                } else {
                    for (int attributionIdx = 0; attributionIdx < packageOps.getAttributedOpsCount(); attributionIdx++) {
                        int opIdx2 = 0;
                        for (android.app.AppOpsManager.AttributedHistoricalOps attributedOps = packageOps.getAttributedOpsAt(attributionIdx); opIdx2 < attributedOps.getOpCount(); attributedOps = attributedOps) {
                            android.app.AppOpsManager.HistoricalOp op2 = attributedOps.getOpAt(opIdx2);
                            processHistoricalOp(op2, opsList, uid, samplingRatio, packageOps.getPackageName(), attributedOps.getTag());
                            opIdx2++;
                        }
                    }
                }
            }
        }
        return opsList;
    }

    private void processHistoricalOp(android.app.AppOpsManager.HistoricalOp op, java.util.List<com.android.server.stats.pull.StatsPullAtomService.AppOpEntry> opsList, int uid, int samplingRatio, java.lang.String packageName, java.lang.String attributionTag) {
        int firstChar = 0;
        if (attributionTag != null && attributionTag.startsWith(packageName) && (firstChar = packageName.length()) < attributionTag.length() && attributionTag.charAt(firstChar) == '.') {
            firstChar++;
        }
        com.android.server.stats.pull.StatsPullAtomService.AppOpEntry entry = new com.android.server.stats.pull.StatsPullAtomService.AppOpEntry(packageName, attributionTag == null ? null : attributionTag.substring(firstChar), op, uid);
        if (entry.mHash < samplingRatio) {
            opsList.add(entry);
        }
    }

    int pullRuntimeAppOpAccessMessageLocked(int atomTag, java.util.List<android.util.StatsEvent> pulledData) {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            android.app.AppOpsManager appOps = (android.app.AppOpsManager) this.mContext.getSystemService(android.app.AppOpsManager.class);
            android.app.RuntimeAppOpAccessMessage message = appOps.collectRuntimeAppOpAccessMessage();
            if (message == null) {
                android.util.Slog.i(TAG, "No runtime appop access message collected");
                return 0;
            }
            try {
                pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, message.getUid(), message.getPackageName(), "", message.getAttributionTag() == null ? "" : message.getAttributionTag(), message.getMessage(), message.getSamplingStrategy(), android.app.AppOpsManager.strOpToOp(message.getOp())));
                return 0;
            } catch (java.lang.Throwable th) {
                t = th;
                try {
                    android.util.Slog.e(TAG, "Could not read runtime appop access message", t);
                    android.os.Binder.restoreCallingIdentity(token);
                    return 1;
                } finally {
                    android.os.Binder.restoreCallingIdentity(token);
                }
            }
        } catch (java.lang.Throwable th2) {
            t = th2;
        }
    }

    static void unpackStreamedData(int atomTag, java.util.List<android.util.StatsEvent> pulledData, java.util.List<android.os.ParcelFileDescriptor> statsFiles) throws java.io.IOException {
        java.io.InputStream stream = new android.os.ParcelFileDescriptor.AutoCloseInputStream(statsFiles.get(0));
        int[] len = new int[1];
        byte[] stats = readFully(stream, len);
        pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, java.util.Arrays.copyOf(stats, len[0])));
    }

    static byte[] readFully(java.io.InputStream stream, int[] outLen) throws java.io.IOException {
        int pos = 0;
        int initialAvail = stream.available();
        byte[] data = new byte[initialAvail > 0 ? initialAvail + 1 : 16384];
        while (true) {
            int amt = stream.read(data, pos, data.length - pos);
            android.util.Slog.i(TAG, "Read " + amt + " bytes at " + pos + " of avail " + data.length);
            if (amt < 0) {
                android.util.Slog.i(TAG, "**** FINISHED READING: pos=" + pos + " len=" + data.length);
                outLen[0] = pos;
                return data;
            }
            pos += amt;
            if (pos >= data.length) {
                byte[] newData = new byte[pos + 16384];
                android.util.Slog.i(TAG, "Copying " + pos + " bytes to new array len " + newData.length);
                java.lang.System.arraycopy(data, 0, newData, 0, pos);
                data = newData;
            }
        }
    }

    private void registerNotificationRemoteViews() {
        this.mStatsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.NOTIFICATION_REMOTE_VIEWS, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullNotificationRemoteViewsLocked(int atomTag, java.util.List<android.util.StatsEvent> pulledData) throws java.io.IOException {
        android.app.INotificationManager notificationManagerService = getINotificationManagerService();
        if (notificationManagerService == null) {
            return 1;
        }
        long callingToken = android.os.Binder.clearCallingIdentity();
        try {
            try {
                long wallClockNanos = android.os.SystemClock.currentTimeMicro() * 1000;
                long lastNotificationStatsNs = wallClockNanos - java.util.concurrent.TimeUnit.NANOSECONDS.convert(1L, java.util.concurrent.TimeUnit.DAYS);
                java.util.List<android.os.ParcelFileDescriptor> statsFiles = new java.util.ArrayList<>();
                notificationManagerService.pullStats(lastNotificationStatsNs, 1, true, statsFiles);
                if (statsFiles.size() != 1) {
                    android.os.Binder.restoreCallingIdentity(callingToken);
                    return 1;
                }
                try {
                    unpackStreamedData(atomTag, pulledData, statsFiles);
                    android.os.Binder.restoreCallingIdentity(callingToken);
                    return 0;
                } catch (android.os.RemoteException e) {
                    e = e;
                    android.util.Slog.e(TAG, "Getting notistats failed: ", e);
                    android.os.Binder.restoreCallingIdentity(callingToken);
                    return 1;
                } catch (java.io.IOException e2) {
                    e = e2;
                    android.util.Slog.e(TAG, "Getting notistats failed: ", e);
                    android.os.Binder.restoreCallingIdentity(callingToken);
                    return 1;
                } catch (java.lang.SecurityException e3) {
                    e = e3;
                    android.util.Slog.e(TAG, "Getting notistats failed: ", e);
                    android.os.Binder.restoreCallingIdentity(callingToken);
                    return 1;
                }
            } catch (java.lang.Throwable th) {
                e = th;
                android.os.Binder.restoreCallingIdentity(callingToken);
                throw e;
            }
        } catch (android.os.RemoteException e4) {
            e = e4;
        } catch (java.io.IOException e5) {
            e = e5;
        } catch (java.lang.SecurityException e6) {
            e = e6;
        } catch (java.lang.Throwable th2) {
            e = th2;
            android.os.Binder.restoreCallingIdentity(callingToken);
            throw e;
        }
    }

    private void registerDangerousPermissionStateSampled() {
        this.mStatsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.DANGEROUS_PERMISSION_STATE_SAMPLED, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    private void registerBatteryLevel() {
        this.mStatsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.BATTERY_LEVEL, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    private void registerRemainingBatteryCapacity() {
        this.mStatsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.REMAINING_BATTERY_CAPACITY, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    private void registerFullBatteryCapacity() {
        this.mStatsManager.setPullAtomCallback(10020, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    private void registerBatteryVoltage() {
        this.mStatsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.BATTERY_VOLTAGE, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    private void registerBatteryCycleCount() {
        this.mStatsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.BATTERY_CYCLE_COUNT, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullHealthHalLocked(int atomTag, java.util.List<android.util.StatsEvent> pulledData) {
        int pulledValue;
        if (this.mHealthService == null) {
            return 1;
        }
        try {
            android.hardware.health.HealthInfo healthInfo = this.mHealthService.getHealthInfo();
            if (healthInfo == null) {
                return 1;
            }
            switch (atomTag) {
                case com.android.internal.util.FrameworkStatsLog.REMAINING_BATTERY_CAPACITY /* 10019 */:
                    pulledValue = healthInfo.batteryChargeCounterUah;
                    break;
                case 10020:
                    pulledValue = healthInfo.batteryFullChargeUah;
                    break;
                case com.android.internal.util.FrameworkStatsLog.BATTERY_VOLTAGE /* 10030 */:
                    pulledValue = healthInfo.batteryVoltageMillivolts;
                    break;
                case com.android.internal.util.FrameworkStatsLog.BATTERY_LEVEL /* 10043 */:
                    pulledValue = healthInfo.batteryLevel;
                    break;
                case com.android.internal.util.FrameworkStatsLog.BATTERY_CYCLE_COUNT /* 10045 */:
                    pulledValue = healthInfo.batteryCycleCount;
                    break;
                default:
                    return 1;
            }
            pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, pulledValue));
            return 0;
        } catch (android.os.RemoteException | java.lang.IllegalStateException e) {
            return 1;
        }
    }

    private void registerSettingsStats() {
        this.mStatsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.SETTING_SNAPSHOT, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullSettingsStatsLocked(int atomTag, java.util.List<android.util.StatsEvent> pulledData) {
        android.os.UserManager userManager = (android.os.UserManager) this.mContext.getSystemService(android.os.UserManager.class);
        if (userManager == null) {
            return 1;
        }
        long token = android.os.Binder.clearCallingIdentity();
        try {
            for (android.content.pm.UserInfo user : userManager.getUsers()) {
                int userId = user.getUserHandle().getIdentifier();
                if (userId == 0) {
                    pulledData.addAll(com.android.server.stats.pull.SettingsStatsUtil.logGlobalSettings(this.mContext, atomTag, 0));
                }
                pulledData.addAll(com.android.server.stats.pull.SettingsStatsUtil.logSystemSettings(this.mContext, atomTag, userId));
                pulledData.addAll(com.android.server.stats.pull.SettingsStatsUtil.logSecureSettings(this.mContext, atomTag, userId));
            }
            return 0;
        } catch (java.lang.Exception e) {
            android.util.Slog.e(TAG, "failed to pullSettingsStats", e);
            return 1;
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    private void registerInstalledIncrementalPackages() {
        this.mStatsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.INSTALLED_INCREMENTAL_PACKAGE, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullInstalledIncrementalPackagesLocked(int atomTag, java.util.List<android.util.StatsEvent> pulledData) throws java.lang.Exception {
        long token;
        android.content.pm.PackageManager pm = this.mContext.getPackageManager();
        android.content.pm.PackageManagerInternal pmIntenral = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
        int i = 0;
        if (!pm.hasSystemFeature("android.software.incremental_delivery")) {
            return 0;
        }
        long token2 = android.os.Binder.clearCallingIdentity();
        try {
            try {
                int[] userIds = ((com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class)).getUserIds();
                int length = userIds.length;
                int i2 = 0;
                while (i2 < length) {
                    int userId = userIds[i2];
                    java.util.List<android.content.pm.PackageInfo> installedPackages = pm.getInstalledPackagesAsUser(i, userId);
                    for (android.content.pm.PackageInfo pi : installedPackages) {
                        if (android.os.incremental.IncrementalManager.isIncrementalPath(pi.applicationInfo.getBaseCodePath())) {
                            android.content.pm.IncrementalStatesInfo info = pmIntenral.getIncrementalStatesInfo(pi.packageName, 1000, userId);
                            token = token2;
                            try {
                            } catch (java.lang.Exception e) {
                                e = e;
                            } catch (java.lang.Throwable th) {
                                e = th;
                            }
                            try {
                                try {
                                    pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, pi.applicationInfo.uid, info.isLoading(), info.getLoadingCompletedTime()));
                                } catch (java.lang.Exception e2) {
                                    e = e2;
                                    android.util.Slog.e(TAG, "failed to pullInstalledIncrementalPackagesLocked", e);
                                    android.os.Binder.restoreCallingIdentity(token);
                                    return 1;
                                }
                            } catch (java.lang.Exception e3) {
                                e = e3;
                                android.util.Slog.e(TAG, "failed to pullInstalledIncrementalPackagesLocked", e);
                                android.os.Binder.restoreCallingIdentity(token);
                                return 1;
                            } catch (java.lang.Throwable th2) {
                                e = th2;
                                android.os.Binder.restoreCallingIdentity(token);
                                throw e;
                            }
                        } else {
                            token = token2;
                        }
                        token2 = token;
                    }
                    i2++;
                    token2 = token2;
                    i = 0;
                }
                android.os.Binder.restoreCallingIdentity(token2);
                return 0;
            } catch (java.lang.Throwable th3) {
                e = th3;
            }
        } catch (java.lang.Exception e4) {
            e = e4;
            token = token2;
        } catch (java.lang.Throwable th4) {
            e = th4;
            token = token2;
        }
    }

    private void registerKeystoreStorageStats() {
        this.mStatsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.KEYSTORE2_STORAGE_STATS, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    private void registerKeystoreKeyCreationWithGeneralInfo() {
        this.mStatsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.KEYSTORE2_KEY_CREATION_WITH_GENERAL_INFO, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    private void registerKeystoreKeyCreationWithAuthInfo() {
        this.mStatsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.KEYSTORE2_KEY_CREATION_WITH_AUTH_INFO, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    private void registerKeystoreKeyCreationWithPurposeModesInfo() {
        this.mStatsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.KEYSTORE2_KEY_CREATION_WITH_PURPOSE_AND_MODES_INFO, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    private void registerKeystoreAtomWithOverflow() {
        this.mStatsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.KEYSTORE2_ATOM_WITH_OVERFLOW, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    private void registerKeystoreKeyOperationWithPurposeAndModesInfo() {
        this.mStatsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.KEYSTORE2_KEY_OPERATION_WITH_PURPOSE_AND_MODES_INFO, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    private void registerKeystoreKeyOperationWithGeneralInfo() {
        this.mStatsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.KEYSTORE2_KEY_OPERATION_WITH_GENERAL_INFO, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    private void registerRkpErrorStats() {
        this.mStatsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.RKP_ERROR_STATS, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    private void registerKeystoreCrashStats() {
        this.mStatsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.KEYSTORE2_CRASH_STATS, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    private void registerAccessibilityShortcutStats() {
        this.mStatsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.ACCESSIBILITY_SHORTCUT_STATS, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    private void registerAccessibilityFloatingMenuStats() {
        this.mStatsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.ACCESSIBILITY_FLOATING_MENU_STATS, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    private void registerMediaCapabilitiesStats() {
        this.mStatsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.MEDIA_CAPABILITIES, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int parseKeystoreStorageStats(android.security.metrics.KeystoreAtom[] atoms, java.util.List<android.util.StatsEvent> pulledData) {
        for (android.security.metrics.KeystoreAtom atomWrapper : atoms) {
            if (atomWrapper.payload.getTag() != 0) {
                return 1;
            }
            android.security.metrics.StorageStats atom = atomWrapper.payload.getStorageStats();
            pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(com.android.internal.util.FrameworkStatsLog.KEYSTORE2_STORAGE_STATS, atom.storage_type, atom.size, atom.unused_size));
        }
        return 0;
    }

    int parseKeystoreKeyCreationWithGeneralInfo(android.security.metrics.KeystoreAtom[] atoms, java.util.List<android.util.StatsEvent> pulledData) {
        for (android.security.metrics.KeystoreAtom atomWrapper : atoms) {
            if (atomWrapper.payload.getTag() != 1) {
                return 1;
            }
            android.security.metrics.KeyCreationWithGeneralInfo atom = atomWrapper.payload.getKeyCreationWithGeneralInfo();
            pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(com.android.internal.util.FrameworkStatsLog.KEYSTORE2_KEY_CREATION_WITH_GENERAL_INFO, atom.algorithm, atom.key_size, atom.ec_curve, atom.key_origin, atom.error_code, atom.attestation_requested, atomWrapper.count));
        }
        return 0;
    }

    int parseKeystoreKeyCreationWithAuthInfo(android.security.metrics.KeystoreAtom[] atoms, java.util.List<android.util.StatsEvent> pulledData) {
        for (android.security.metrics.KeystoreAtom atomWrapper : atoms) {
            if (atomWrapper.payload.getTag() != 2) {
                return 1;
            }
            android.security.metrics.KeyCreationWithAuthInfo atom = atomWrapper.payload.getKeyCreationWithAuthInfo();
            pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(com.android.internal.util.FrameworkStatsLog.KEYSTORE2_KEY_CREATION_WITH_AUTH_INFO, atom.user_auth_type, atom.log10_auth_key_timeout_seconds, atom.security_level, atomWrapper.count));
        }
        return 0;
    }

    int parseKeystoreKeyCreationWithPurposeModesInfo(android.security.metrics.KeystoreAtom[] atoms, java.util.List<android.util.StatsEvent> pulledData) {
        for (android.security.metrics.KeystoreAtom atomWrapper : atoms) {
            if (atomWrapper.payload.getTag() != 3) {
                return 1;
            }
            android.security.metrics.KeyCreationWithPurposeAndModesInfo atom = atomWrapper.payload.getKeyCreationWithPurposeAndModesInfo();
            pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(com.android.internal.util.FrameworkStatsLog.KEYSTORE2_KEY_CREATION_WITH_PURPOSE_AND_MODES_INFO, atom.algorithm, atom.purpose_bitmap, atom.padding_mode_bitmap, atom.digest_bitmap, atom.block_mode_bitmap, atomWrapper.count));
        }
        return 0;
    }

    int parseKeystoreAtomWithOverflow(android.security.metrics.KeystoreAtom[] atoms, java.util.List<android.util.StatsEvent> pulledData) {
        for (android.security.metrics.KeystoreAtom atomWrapper : atoms) {
            if (atomWrapper.payload.getTag() != 4) {
                return 1;
            }
            android.security.metrics.Keystore2AtomWithOverflow atom = atomWrapper.payload.getKeystore2AtomWithOverflow();
            pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(com.android.internal.util.FrameworkStatsLog.KEYSTORE2_ATOM_WITH_OVERFLOW, atom.atom_id, atomWrapper.count));
        }
        return 0;
    }

    int parseKeystoreKeyOperationWithPurposeModesInfo(android.security.metrics.KeystoreAtom[] atoms, java.util.List<android.util.StatsEvent> pulledData) {
        for (android.security.metrics.KeystoreAtom atomWrapper : atoms) {
            if (atomWrapper.payload.getTag() != 5) {
                return 1;
            }
            android.security.metrics.KeyOperationWithPurposeAndModesInfo atom = atomWrapper.payload.getKeyOperationWithPurposeAndModesInfo();
            pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(com.android.internal.util.FrameworkStatsLog.KEYSTORE2_KEY_OPERATION_WITH_PURPOSE_AND_MODES_INFO, atom.purpose, atom.padding_mode_bitmap, atom.digest_bitmap, atom.block_mode_bitmap, atomWrapper.count));
        }
        return 0;
    }

    int parseKeystoreKeyOperationWithGeneralInfo(android.security.metrics.KeystoreAtom[] atoms, java.util.List<android.util.StatsEvent> pulledData) {
        for (android.security.metrics.KeystoreAtom atomWrapper : atoms) {
            if (atomWrapper.payload.getTag() != 6) {
                return 1;
            }
            android.security.metrics.KeyOperationWithGeneralInfo atom = atomWrapper.payload.getKeyOperationWithGeneralInfo();
            pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(com.android.internal.util.FrameworkStatsLog.KEYSTORE2_KEY_OPERATION_WITH_GENERAL_INFO, atom.outcome, atom.error_code, atom.key_upgraded, atom.security_level, atomWrapper.count));
        }
        return 0;
    }

    int parseRkpErrorStats(android.security.metrics.KeystoreAtom[] atoms, java.util.List<android.util.StatsEvent> pulledData) {
        for (android.security.metrics.KeystoreAtom atomWrapper : atoms) {
            if (atomWrapper.payload.getTag() != 7) {
                return 1;
            }
            android.security.metrics.RkpErrorStats atom = atomWrapper.payload.getRkpErrorStats();
            pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(com.android.internal.util.FrameworkStatsLog.RKP_ERROR_STATS, atom.rkpError, atomWrapper.count, atom.security_level));
        }
        return 0;
    }

    int parseKeystoreCrashStats(android.security.metrics.KeystoreAtom[] atoms, java.util.List<android.util.StatsEvent> pulledData) {
        for (android.security.metrics.KeystoreAtom atomWrapper : atoms) {
            if (atomWrapper.payload.getTag() != 8) {
                return 1;
            }
            android.security.metrics.CrashStats atom = atomWrapper.payload.getCrashStats();
            pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(com.android.internal.util.FrameworkStatsLog.KEYSTORE2_CRASH_STATS, atom.count_of_crash_events));
        }
        return 0;
    }

    int pullKeystoreAtoms(int atomTag, java.util.List<android.util.StatsEvent> pulledData) {
        android.security.metrics.IKeystoreMetrics keystoreMetricsService = getIKeystoreMetricsService();
        if (keystoreMetricsService == null) {
            android.util.Slog.w(TAG, "Keystore service is null");
            return 1;
        }
        long callingToken = android.os.Binder.clearCallingIdentity();
        try {
            android.security.metrics.KeystoreAtom[] atoms = keystoreMetricsService.pullMetrics(atomTag);
            switch (atomTag) {
                case com.android.internal.util.FrameworkStatsLog.KEYSTORE2_STORAGE_STATS /* 10103 */:
                    return parseKeystoreStorageStats(atoms, pulledData);
                case com.android.internal.util.FrameworkStatsLog.KEYSTORE2_KEY_CREATION_WITH_GENERAL_INFO /* 10118 */:
                    return parseKeystoreKeyCreationWithGeneralInfo(atoms, pulledData);
                case com.android.internal.util.FrameworkStatsLog.KEYSTORE2_KEY_CREATION_WITH_AUTH_INFO /* 10119 */:
                    return parseKeystoreKeyCreationWithAuthInfo(atoms, pulledData);
                case com.android.internal.util.FrameworkStatsLog.KEYSTORE2_KEY_CREATION_WITH_PURPOSE_AND_MODES_INFO /* 10120 */:
                    return parseKeystoreKeyCreationWithPurposeModesInfo(atoms, pulledData);
                case com.android.internal.util.FrameworkStatsLog.KEYSTORE2_ATOM_WITH_OVERFLOW /* 10121 */:
                    return parseKeystoreAtomWithOverflow(atoms, pulledData);
                case com.android.internal.util.FrameworkStatsLog.KEYSTORE2_KEY_OPERATION_WITH_PURPOSE_AND_MODES_INFO /* 10122 */:
                    return parseKeystoreKeyOperationWithPurposeModesInfo(atoms, pulledData);
                case com.android.internal.util.FrameworkStatsLog.KEYSTORE2_KEY_OPERATION_WITH_GENERAL_INFO /* 10123 */:
                    return parseKeystoreKeyOperationWithGeneralInfo(atoms, pulledData);
                case com.android.internal.util.FrameworkStatsLog.RKP_ERROR_STATS /* 10124 */:
                    return parseRkpErrorStats(atoms, pulledData);
                case com.android.internal.util.FrameworkStatsLog.KEYSTORE2_CRASH_STATS /* 10125 */:
                    return parseKeystoreCrashStats(atoms, pulledData);
                default:
                    android.util.Slog.w(TAG, "Unsupported keystore atom: " + atomTag);
                    return 1;
            }
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Disconnected from keystore service. Cannot pull.", e);
            return 1;
        } catch (android.os.ServiceSpecificException e2) {
            android.util.Slog.e(TAG, "pulling keystore metrics failed", e2);
            return 1;
        } finally {
            android.os.Binder.restoreCallingIdentity(callingToken);
        }
    }

    int pullAccessibilityShortcutStatsLocked(java.util.List<android.util.StatsEvent> pulledData) {
        android.os.UserManager userManager = (android.os.UserManager) this.mContext.getSystemService(android.os.UserManager.class);
        if (userManager == null) {
            return 1;
        }
        long token = android.os.Binder.clearCallingIdentity();
        try {
            try {
                android.content.ContentResolver resolver = this.mContext.getContentResolver();
                for (android.content.pm.UserInfo userInfo : userManager.getUsers()) {
                    int userId = userInfo.getUserHandle().getIdentifier();
                    if (isAccessibilityShortcutUser(this.mContext, userId)) {
                        int software_shortcut_type = convertToAccessibilityShortcutType(android.provider.Settings.Secure.getIntForUser(resolver, "accessibility_button_mode", 0, userId));
                        java.lang.String software_shortcut_list = android.provider.Settings.Secure.getStringForUser(resolver, "accessibility_button_targets", userId);
                        int software_shortcut_service_num = countAccessibilityServices(software_shortcut_list);
                        java.lang.String hardware_shortcut_list = android.provider.Settings.Secure.getStringForUser(resolver, "accessibility_shortcut_target_service", userId);
                        int hardware_shortcut_service_num = countAccessibilityServices(hardware_shortcut_list);
                        java.lang.String qs_shortcut_list = android.provider.Settings.Secure.getStringForUser(resolver, "accessibility_qs_targets", userId);
                        boolean qs_shortcut_enabled = !android.text.TextUtils.isEmpty(qs_shortcut_list);
                        int triple_tap_service_num = android.provider.Settings.Secure.getIntForUser(resolver, "accessibility_display_magnification_enabled", 0, userId);
                        try {
                            pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(com.android.internal.util.FrameworkStatsLog.ACCESSIBILITY_SHORTCUT_STATS, software_shortcut_type, software_shortcut_service_num, 2, hardware_shortcut_service_num, 3, triple_tap_service_num, 9, qs_shortcut_enabled));
                        } catch (java.lang.RuntimeException e) {
                            e = e;
                            android.util.Slog.e(TAG, "pulling accessibility shortcuts stats failed at getUsers", e);
                            android.os.Binder.restoreCallingIdentity(token);
                            return 1;
                        }
                    }
                }
                android.os.Binder.restoreCallingIdentity(token);
                return 0;
            } catch (java.lang.Throwable th) {
                e = th;
                android.os.Binder.restoreCallingIdentity(token);
                throw e;
            }
        } catch (java.lang.RuntimeException e2) {
            e = e2;
        } catch (java.lang.Throwable th2) {
            e = th2;
            android.os.Binder.restoreCallingIdentity(token);
            throw e;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    int pullAccessibilityFloatingMenuStatsLocked(int atomTag, java.util.List<android.util.StatsEvent> pulledData) {
        android.content.ContentResolver resolver;
        com.android.server.stats.pull.StatsPullAtomService statsPullAtomService = this;
        android.os.UserManager userManager = (android.os.UserManager) statsPullAtomService.mContext.getSystemService(android.os.UserManager.class);
        int i = 1;
        if (userManager == null) {
            return 1;
        }
        long token = android.os.Binder.clearCallingIdentity();
        try {
            android.content.ContentResolver resolver2 = statsPullAtomService.mContext.getContentResolver();
            for (android.content.pm.UserInfo userInfo : userManager.getUsers()) {
                int userId = userInfo.getUserHandle().getIdentifier();
                if (statsPullAtomService.isAccessibilityFloatingMenuUser(statsPullAtomService.mContext, userId)) {
                    int size = android.provider.Settings.Secure.getIntForUser(resolver2, "accessibility_floating_menu_size", 0, userId);
                    int type = android.provider.Settings.Secure.getIntForUser(resolver2, "accessibility_floating_menu_icon_type", 0, userId);
                    boolean z = android.provider.Settings.Secure.getIntForUser(resolver2, "accessibility_floating_menu_fade_enabled", i, userId) == i ? i : 0;
                    float opacity = android.provider.Settings.Secure.getFloatForUser(resolver2, "accessibility_floating_menu_opacity", 0.55f, userId);
                    resolver = resolver2;
                    try {
                        try {
                            try {
                                pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, size, type, z, opacity));
                            } catch (java.lang.RuntimeException e) {
                                e = e;
                                android.util.Slog.e(TAG, "pulling accessibility floating menu stats failed at getUsers", e);
                                android.os.Binder.restoreCallingIdentity(token);
                                return 1;
                            }
                        } catch (java.lang.Throwable th) {
                            e = th;
                            android.os.Binder.restoreCallingIdentity(token);
                            throw e;
                        }
                    } catch (java.lang.RuntimeException e2) {
                        e = e2;
                        android.util.Slog.e(TAG, "pulling accessibility floating menu stats failed at getUsers", e);
                        android.os.Binder.restoreCallingIdentity(token);
                        return 1;
                    } catch (java.lang.Throwable th2) {
                        e = th2;
                        android.os.Binder.restoreCallingIdentity(token);
                        throw e;
                    }
                } else {
                    resolver = resolver2;
                }
                i = 1;
                statsPullAtomService = this;
                resolver2 = resolver;
            }
            android.os.Binder.restoreCallingIdentity(token);
            return 0;
        } catch (java.lang.RuntimeException e3) {
            e = e3;
        } catch (java.lang.Throwable th3) {
            e = th3;
        }
    }

    int pullMediaCapabilitiesStats(int atomTag, java.util.List<android.util.StatsEvent> pulledData) {
        android.media.AudioManager audioManager;
        byte[] sinkHdrFormats;
        int hdcpLevel;
        int userPreferredWidth;
        boolean hasUserDisabledAllm;
        if (!this.mContext.getPackageManager().hasSystemFeature("android.software.leanback") || (audioManager = (android.media.AudioManager) this.mContext.getSystemService(android.media.AudioManager.class)) == null) {
            return 1;
        }
        java.util.Map surroundEncodingsMap = audioManager.getSurroundFormats();
        byte[] surroundEncodings = toBytes(new java.util.ArrayList(surroundEncodingsMap.keySet()));
        byte[] sinkSurroundEncodings = toBytes((java.util.List<java.lang.Integer>) audioManager.getReportedSurroundFormats());
        java.util.List<java.lang.Integer> disabledSurroundEncodingsList = new java.util.ArrayList<>();
        java.util.List<java.lang.Integer> enabledSurroundEncodingsList = new java.util.ArrayList<>();
        java.util.Iterator<java.lang.Integer> it = surroundEncodingsMap.keySet().iterator();
        while (it.hasNext()) {
            int surroundEncoding = it.next().intValue();
            if (!audioManager.isSurroundFormatEnabled(surroundEncoding)) {
                disabledSurroundEncodingsList.add(java.lang.Integer.valueOf(surroundEncoding));
            } else {
                enabledSurroundEncodingsList.add(java.lang.Integer.valueOf(surroundEncoding));
            }
        }
        byte[] disabledSurroundEncodings = toBytes(disabledSurroundEncodingsList);
        byte[] enabledSurroundEncodings = toBytes(enabledSurroundEncodingsList);
        int surroundOutputMode = audioManager.getEncodedSurroundMode();
        android.hardware.display.DisplayManager displayManager = (android.hardware.display.DisplayManager) this.mContext.getSystemService(android.hardware.display.DisplayManager.class);
        android.view.Display display = displayManager.getDisplay(0);
        android.view.Display.HdrCapabilities hdrCapabilities = display.getHdrCapabilities();
        byte[] sinkHdrFormats2 = new byte[0];
        if (hdrCapabilities != null) {
            byte[] sinkHdrFormats3 = toBytes(hdrCapabilities.getSupportedHdrTypes());
            sinkHdrFormats = sinkHdrFormats3;
        } else {
            sinkHdrFormats = sinkHdrFormats2;
        }
        byte[] sinkDisplayModes = toBytes(display.getSupportedModes());
        int hdcpLevel2 = -1;
        java.util.List<java.util.UUID> uuids = android.media.MediaDrm.getSupportedCryptoSchemes();
        try {
            if (!uuids.isEmpty()) {
                android.media.MediaDrm mediaDrm = new android.media.MediaDrm(uuids.get(0));
                hdcpLevel2 = mediaDrm.getConnectedHdcpLevel();
            }
            hdcpLevel = hdcpLevel2;
        } catch (android.media.UnsupportedSchemeException exception) {
            android.util.Slog.e(TAG, "pulling hdcp level failed.", exception);
            hdcpLevel = -1;
        }
        int matchContentFrameRateUserPreference = displayManager.getMatchContentFrameRateUserPreference();
        byte[] userDisabledHdrTypes = toBytes(displayManager.getUserDisabledHdrTypes());
        android.view.Display.Mode userPreferredDisplayMode = displayManager.getGlobalUserPreferredDisplayMode();
        int physicalHeight = -1;
        if (userPreferredDisplayMode == null) {
            userPreferredWidth = -1;
        } else {
            userPreferredWidth = userPreferredDisplayMode.getPhysicalWidth();
        }
        if (userPreferredDisplayMode != null) {
            physicalHeight = userPreferredDisplayMode.getPhysicalHeight();
        }
        int userPreferredHeight = physicalHeight;
        float userPreferredRefreshRate = userPreferredDisplayMode != null ? userPreferredDisplayMode.getRefreshRate() : 0.0f;
        try {
            hasUserDisabledAllm = android.provider.Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "minimal_post_processing_allowed", 1) == 0;
        } catch (android.provider.Settings.SettingNotFoundException exception2) {
            android.util.Slog.e(TAG, "unable to find setting for MINIMAL_POST_PROCESSING_ALLOWED.", exception2);
            hasUserDisabledAllm = false;
        }
        pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, surroundEncodings, sinkSurroundEncodings, disabledSurroundEncodings, enabledSurroundEncodings, surroundOutputMode, sinkHdrFormats, sinkDisplayModes, hdcpLevel, matchContentFrameRateUserPreference, userDisabledHdrTypes, userPreferredWidth, userPreferredHeight, userPreferredRefreshRate, hasUserDisabledAllm));
        return 0;
    }

    private void registerPendingIntentsPerPackagePuller() {
        this.mStatsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.PENDING_INTENTS_PER_PACKAGE, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int pullHdrCapabilities(int atomTag, java.util.List<android.util.StatsEvent> pulledData) {
        boolean userDisabledHdrConversion;
        boolean hdrOutputControlSupported;
        android.hardware.display.DisplayManager displayManager = (android.hardware.display.DisplayManager) this.mContext.getSystemService(android.hardware.display.DisplayManager.class);
        android.view.Display display = displayManager.getDisplay(0);
        int hdrConversionMode = displayManager.getHdrConversionMode().getConversionMode();
        int preferredHdrType = displayManager.getHdrConversionMode().getPreferredHdrOutputType();
        if (hdrConversionMode != 1) {
            userDisabledHdrConversion = false;
        } else {
            userDisabledHdrConversion = true;
        }
        int forceHdrFormat = preferredHdrType == -1 ? 0 : preferredHdrType;
        boolean hasDolbyVisionIssue = hasDolbyVisionIssue(display);
        byte[] hdrOutputTypes = toBytes(displayManager.getSupportedHdrOutputTypes());
        if (hdrConversionMode == 0) {
            hdrOutputControlSupported = false;
        } else {
            hdrOutputControlSupported = true;
        }
        pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, hdrOutputTypes, userDisabledHdrConversion, forceHdrFormat, hasDolbyVisionIssue, hdrOutputControlSupported));
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int pullCachedAppsHighWatermark(int atomTag, java.util.List<android.util.StatsEvent> pulledData) {
        pulledData.add((android.util.StatsEvent) ((android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class)).getCachedAppsHighWatermarkStats(atomTag, true));
        return 0;
    }

    private boolean hasDolbyVisionIssue(android.view.Display display) {
        final java.util.concurrent.atomic.AtomicInteger modesSupportingDolbyVision = new java.util.concurrent.atomic.AtomicInteger();
        java.util.Arrays.stream(display.getSupportedModes()).map(new java.util.function.Function() { // from class: com.android.server.stats.pull.StatsPullAtomService$$ExternalSyntheticLambda3
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return ((android.view.Display.Mode) obj).getSupportedHdrTypes();
            }
        }).filter(new java.util.function.Predicate() { // from class: com.android.server.stats.pull.StatsPullAtomService$$ExternalSyntheticLambda4
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return java.util.Arrays.stream((int[]) obj).anyMatch(new java.util.function.IntPredicate() { // from class: com.android.server.stats.pull.StatsPullAtomService$$ExternalSyntheticLambda23
                    @Override // java.util.function.IntPredicate
                    public final boolean test(int i) {
                        return com.android.server.stats.pull.StatsPullAtomService.lambda$hasDolbyVisionIssue$23(i);
                    }
                });
            }
        }).forEach(new java.util.function.Consumer() { // from class: com.android.server.stats.pull.StatsPullAtomService$$ExternalSyntheticLambda5
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                modesSupportingDolbyVision.incrementAndGet();
            }
        });
        if (modesSupportingDolbyVision.get() != 0 && modesSupportingDolbyVision.get() < display.getSupportedModes().length) {
            return true;
        }
        return false;
    }

    static /* synthetic */ boolean lambda$hasDolbyVisionIssue$23(int hdrType) {
        return hdrType == 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int pullPendingIntentsPerPackage(int atomTag, java.util.List<android.util.StatsEvent> pulledData) {
        java.util.List<android.app.PendingIntentStats> pendingIntentStats = ((android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class)).getPendingIntentStats();
        for (android.app.PendingIntentStats stats : pendingIntentStats) {
            pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, stats.uid, stats.count, stats.sizeKb));
        }
        return 0;
    }

    private void registerPinnerServiceStats() {
        this.mStatsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.PINNED_FILE_SIZES_PER_PACKAGE, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    private void registerHdrCapabilitiesPuller() {
        this.mStatsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.HDR_CAPABILITIES, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    private void registerCachedAppsHighWatermarkPuller() {
        this.mStatsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.CACHED_APPS_HIGH_WATERMARK, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    int pullSystemServerPinnerStats(int atomTag, java.util.List<android.util.StatsEvent> pulledData) {
        com.android.server.PinnerService pinnerService = (com.android.server.PinnerService) com.android.server.LocalServices.getService(com.android.server.PinnerService.class);
        java.util.List<com.android.server.PinnerService.PinnedFileStats> pinnedFileStats = pinnerService.dumpDataForStatsd();
        for (com.android.server.PinnerService.PinnedFileStats pfstats : pinnedFileStats) {
            pulledData.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, pfstats.uid, pfstats.filename, pfstats.sizeKb));
        }
        return 0;
    }

    private byte[] toBytes(java.util.List<java.lang.Integer> audioEncodings) {
        android.util.proto.ProtoOutputStream protoOutputStream = new android.util.proto.ProtoOutputStream();
        java.util.Iterator<java.lang.Integer> it = audioEncodings.iterator();
        while (it.hasNext()) {
            int audioEncoding = it.next().intValue();
            protoOutputStream.write(2259152797697L, audioEncoding);
        }
        return protoOutputStream.getBytes();
    }

    private byte[] toBytes(int[] array) {
        android.util.proto.ProtoOutputStream protoOutputStream = new android.util.proto.ProtoOutputStream();
        for (int element : array) {
            protoOutputStream.write(2259152797697L, element);
        }
        return protoOutputStream.getBytes();
    }

    private byte[] toBytes(android.view.Display.Mode[] displayModes) {
        java.util.Map<java.lang.Integer, java.lang.Integer> modeGroupIds = createModeGroups(displayModes);
        android.util.proto.ProtoOutputStream protoOutputStream = new android.util.proto.ProtoOutputStream();
        for (android.view.Display.Mode element : displayModes) {
            android.util.proto.ProtoOutputStream protoOutputStreamMode = new android.util.proto.ProtoOutputStream();
            protoOutputStreamMode.write(1120986464257L, element.getPhysicalHeight());
            protoOutputStreamMode.write(1120986464258L, element.getPhysicalWidth());
            protoOutputStreamMode.write(1108101562371L, element.getRefreshRate());
            protoOutputStreamMode.write(1120986464260L, modeGroupIds.get(java.lang.Integer.valueOf(element.getModeId())).intValue());
            protoOutputStream.write(2246267895809L, protoOutputStreamMode.getBytes());
        }
        return protoOutputStream.getBytes();
    }

    private java.util.Map<java.lang.Integer, java.lang.Integer> createModeGroups(android.view.Display.Mode[] supportedModes) {
        java.util.Map<java.lang.Integer, java.lang.Integer> modeGroupIds = new android.util.ArrayMap<>();
        int groupId = 1;
        for (android.view.Display.Mode mode : supportedModes) {
            if (!modeGroupIds.containsKey(java.lang.Integer.valueOf(mode.getModeId()))) {
                modeGroupIds.put(java.lang.Integer.valueOf(mode.getModeId()), java.lang.Integer.valueOf(groupId));
                for (float refreshRate : mode.getAlternativeRefreshRates()) {
                    int alternativeModeId = findModeId(supportedModes, mode.getPhysicalWidth(), mode.getPhysicalHeight(), refreshRate);
                    if (alternativeModeId != -1 && !modeGroupIds.containsKey(java.lang.Integer.valueOf(alternativeModeId))) {
                        modeGroupIds.put(java.lang.Integer.valueOf(alternativeModeId), java.lang.Integer.valueOf(groupId));
                    }
                }
                groupId++;
            }
        }
        return modeGroupIds;
    }

    private int findModeId(android.view.Display.Mode[] modes, int width, int height, float refreshRate) {
        for (android.view.Display.Mode mode : modes) {
            if (mode.matches(width, height, refreshRate)) {
                return mode.getModeId();
            }
        }
        return -1;
    }

    private int countAccessibilityServices(java.lang.String semicolonList) {
        if (android.text.TextUtils.isEmpty(semicolonList)) {
            return 0;
        }
        int semiColonNums = (int) semicolonList.chars().filter(new java.util.function.IntPredicate() { // from class: com.android.server.stats.pull.StatsPullAtomService$$ExternalSyntheticLambda15
            @Override // java.util.function.IntPredicate
            public final boolean test(int i) {
                return com.android.server.stats.pull.StatsPullAtomService.lambda$countAccessibilityServices$26(i);
            }
        }).count();
        if (android.text.TextUtils.isEmpty(semicolonList)) {
            return 0;
        }
        return semiColonNums + 1;
    }

    static /* synthetic */ boolean lambda$countAccessibilityServices$26(int ch) {
        return ch == 58;
    }

    private boolean isAccessibilityShortcutUser(android.content.Context context, int userId) {
        android.content.ContentResolver resolver = context.getContentResolver();
        java.lang.String software_shortcut_list = android.provider.Settings.Secure.getStringForUser(resolver, "accessibility_button_targets", userId);
        java.lang.String hardware_shortcut_list = android.provider.Settings.Secure.getStringForUser(resolver, "accessibility_shortcut_target_service", userId);
        java.lang.String qs_shortcut_list = android.provider.Settings.Secure.getStringForUser(resolver, "accessibility_qs_targets", userId);
        boolean hardware_shortcut_dialog_shown = android.provider.Settings.Secure.getIntForUser(resolver, "accessibility_shortcut_dialog_shown", 0, userId) == 1;
        boolean software_shortcut_enabled = !android.text.TextUtils.isEmpty(software_shortcut_list);
        boolean hardware_shortcut_enabled = hardware_shortcut_dialog_shown && !android.text.TextUtils.isEmpty(hardware_shortcut_list);
        boolean qs_shortcut_enabled = !android.text.TextUtils.isEmpty(qs_shortcut_list);
        boolean triple_tap_shortcut_enabled = android.provider.Settings.Secure.getIntForUser(resolver, "accessibility_display_magnification_enabled", 0, userId) == 1;
        return software_shortcut_enabled || hardware_shortcut_enabled || triple_tap_shortcut_enabled || qs_shortcut_enabled;
    }

    private boolean isAccessibilityFloatingMenuUser(android.content.Context context, int userId) {
        android.content.ContentResolver resolver = context.getContentResolver();
        int mode = android.provider.Settings.Secure.getIntForUser(resolver, "accessibility_button_mode", 0, userId);
        java.lang.String software_string = android.provider.Settings.Secure.getStringForUser(resolver, "accessibility_button_targets", userId);
        return mode == 1 && !android.text.TextUtils.isEmpty(software_string);
    }

    private int convertToAccessibilityShortcutType(int shortcutType) {
        switch (shortcutType) {
            case 0:
                return 1;
            case 1:
                return 5;
            case 2:
                return 6;
            default:
                return 0;
        }
    }

    private static final class ThermalEventListener extends android.os.IThermalEventListener.Stub {
        private ThermalEventListener() {
        }

        public void notifyThrottling(android.os.Temperature temp) {
            com.android.internal.util.FrameworkStatsLog.write(189, temp.getType(), temp.getName(), (int) (temp.getValue() * 10.0f), temp.getStatus());
        }
    }

    private static final class ConnectivityStatsCallback extends android.net.ConnectivityManager.NetworkCallback {
        private ConnectivityStatsCallback() {
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onAvailable(android.net.Network network) {
            com.android.internal.util.FrameworkStatsLog.write(98, network.getNetId(), 1);
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onLost(android.net.Network network) {
            com.android.internal.util.FrameworkStatsLog.write(98, network.getNetId(), 2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    final class StatsSubscriptionsListener extends android.telephony.SubscriptionManager.OnSubscriptionsChangedListener {
        private final android.telephony.SubscriptionManager mSm;

        StatsSubscriptionsListener(android.telephony.SubscriptionManager sm) {
            this.mSm = sm;
        }

        @Override // android.telephony.SubscriptionManager.OnSubscriptionsChangedListener
        public void onSubscriptionsChanged() {
            java.util.List<android.telephony.SubscriptionInfo> currentSubs = this.mSm.getCompleteActiveSubscriptionInfoList();
            for (final android.telephony.SubscriptionInfo sub : currentSubs) {
                com.android.server.stats.pull.netstats.SubInfo match = (com.android.server.stats.pull.netstats.SubInfo) com.android.internal.util.CollectionUtils.find(com.android.server.stats.pull.StatsPullAtomService.this.mHistoricalSubs, new java.util.function.Predicate() { // from class: com.android.server.stats.pull.StatsPullAtomService$StatsSubscriptionsListener$$ExternalSyntheticLambda0
                    @Override // java.util.function.Predicate
                    public final boolean test(java.lang.Object obj) {
                        return com.android.server.stats.pull.StatsPullAtomService.StatsSubscriptionsListener.lambda$onSubscriptionsChanged$0(sub, (com.android.server.stats.pull.netstats.SubInfo) obj);
                    }
                });
                if (match == null) {
                    int subId = sub.getSubscriptionId();
                    java.lang.String mcc = sub.getMccString();
                    java.lang.String mnc = sub.getMncString();
                    java.lang.String subscriberId = com.android.server.stats.pull.StatsPullAtomService.this.mTelephony.getSubscriberId(subId);
                    if (android.text.TextUtils.isEmpty(subscriberId) || android.text.TextUtils.isEmpty(mcc) || android.text.TextUtils.isEmpty(mnc) || sub.getCarrierId() == -1) {
                        android.util.Slog.e(com.android.server.stats.pull.StatsPullAtomService.TAG, "subInfo of subId " + subId + " is invalid, ignored.");
                    } else {
                        com.android.server.stats.pull.netstats.SubInfo subInfo = new com.android.server.stats.pull.netstats.SubInfo(subId, sub.getCarrierId(), mcc, mnc, subscriberId, sub.isOpportunistic());
                        android.util.Slog.i(com.android.server.stats.pull.StatsPullAtomService.TAG, "subId " + subId + " added into historical sub list");
                        synchronized (com.android.server.stats.pull.StatsPullAtomService.this.mDataBytesTransferLock) {
                            com.android.server.stats.pull.StatsPullAtomService.this.mHistoricalSubs.add(subInfo);
                            com.android.server.stats.pull.StatsPullAtomService.this.mNetworkStatsBaselines.addAll(com.android.server.stats.pull.StatsPullAtomService.this.getDataUsageBytesTransferSnapshotForSub(subInfo));
                        }
                    }
                }
            }
        }

        static /* synthetic */ boolean lambda$onSubscriptionsChanged$0(android.telephony.SubscriptionInfo sub, com.android.server.stats.pull.netstats.SubInfo it) {
            return it.subId == sub.getSubscriptionId();
        }
    }
}
