package com.android.server.power.stats;

/* JADX INFO: loaded from: classes3.dex */
public class BatteryStatsImpl extends android.os.BatteryStats {
    public static final int BATTERY_PLUGGED_NONE = 0;
    private static final int CELL_SIGNAL_STRENGTH_LEVEL_COUNT;
    private static final boolean DEBUG = false;
    private static final boolean DEBUG_BINDER_STATS = false;
    public static final boolean DEBUG_ENERGY = false;
    private static final boolean DEBUG_ENERGY_CPU = false;
    private static final boolean DEBUG_MEMORY = false;
    static final long DELAY_UPDATE_WAKELOCKS = 60000;
    private static final int GPS_SIGNAL_QUALITY_NONE = 2;
    static final int MAX_DAILY_ITEMS = 10;
    static final int MAX_LEVEL_STEPS = 200;
    private static final int MAX_WAKELOCKS_PER_UID;
    private static final double MILLISECONDS_IN_HOUR = 3600000.0d;
    private static final long MILLISECONDS_IN_YEAR = 31536000000L;
    protected static final long MOBILE_RADIO_POWER_STATE_UPDATE_FREQ_MS = 600000;
    private static final int MODEM_TX_POWER_LEVEL_COUNT;
    static final int MSG_REPORT_CHARGING = 3;
    static final int MSG_REPORT_CPU_UPDATE_NEEDED = 1;
    static final int MSG_REPORT_POWER_CHANGE = 2;
    static final int MSG_REPORT_RESET_STATS = 4;
    private static final int NR_FREQUENCY_COUNT = 5;
    private static final int NUM_BT_TX_LEVELS = 1;
    private static final int NUM_WIFI_TX_LEVELS = 1;
    public static final int PER_UID_MODEM_POWER_MODEL_MOBILE_RADIO_ACTIVE_TIME = 1;
    public static final int PER_UID_MODEM_POWER_MODEL_MODEM_ACTIVITY_INFO_RX_TX = 2;
    private static final int PROC_STATE_TIME_COUNTER_STATE_COUNT = 8;
    public static final int RESET_REASON_ADB_COMMAND = 2;
    public static final int RESET_REASON_CORRUPT_FILE = 1;
    public static final int RESET_REASON_ENERGY_CONSUMER_BUCKETS_CHANGE = 4;
    public static final int RESET_REASON_FULL_CHARGE = 3;
    public static final int RESET_REASON_PLUGGED_IN_FOR_LONG_DURATION = 5;
    private static final long RPM_STATS_UPDATE_FREQ_MS = 1000;
    private static final int[] SUPPORTED_PER_PROCESS_STATE_STANDARD_ENERGY_BUCKETS;
    private static final java.lang.String TAG = "BatteryStatsImpl";
    private static final int USB_DATA_CONNECTED = 2;
    private static final int USB_DATA_DISCONNECTED = 1;
    private static final int USB_DATA_UNKNOWN = 0;
    public static final int VERSION;
    public static final int WAKE_LOCK_WEIGHT = 50;
    private static final android.os.BatteryStats.LongCounter ZERO_LONG_COUNTER;
    private static final android.os.BatteryStats.LongCounter[] ZERO_LONG_COUNTER_ARRAY;
    private final android.os.BatteryStats.HistoryEventTracker mActiveEvents;
    int mActiveRat;
    private android.app.AlarmManager mAlarmManager;
    int mAudioOnNesting;
    com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer mAudioOnTimer;
    private final java.util.ArrayList<com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer> mAudioTurnedOnTimers;
    private int mBatteryChargeUah;
    private int mBatteryHealth;
    private int mBatteryLevel;
    private int mBatteryPlugType;
    private boolean mBatteryPluggedIn;
    private long mBatteryPluggedInRealTimeMs;
    protected final com.android.server.power.stats.BatteryStatsImpl.BatteryStatsConfig mBatteryStatsConfig;
    public com.android.server.power.stats.IBatteryStatsImplExt mBatteryStatsImplExt;
    private int mBatteryStatus;
    private int mBatteryTemperature;
    private long mBatteryTimeToFullSeconds;
    private com.android.server.power.stats.BatteryUsageStatsProvider mBatteryUsageStatsProvider;
    private int mBatteryVoltageMv;
    private com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounterArray mBinderThreadCpuTimesUs;
    com.android.server.power.stats.BatteryStatsImpl.ControllerActivityCounterImpl mBluetoothActivity;
    com.android.server.power.stats.BluetoothPowerCalculator mBluetoothPowerCalculator;
    private final com.android.server.power.stats.BluetoothPowerStatsCollector mBluetoothPowerStatsCollector;
    int mBluetoothScanNesting;
    private final java.util.ArrayList<com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer> mBluetoothScanOnTimers;
    com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer mBluetoothScanTimer;
    private com.android.server.power.stats.BatteryStatsImpl.BatteryStatsImplWrapper mBsiWrapper;
    private com.android.server.power.stats.BatteryStatsImpl.BatteryCallback mCallback;
    int mCameraOnNesting;
    com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer mCameraOnTimer;
    private final com.android.server.power.stats.CameraPowerStatsCollector mCameraPowerStatsCollector;
    private final java.util.ArrayList<com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer> mCameraTurnedOnTimers;
    final android.os.BatteryStats.LevelStepTracker mChargeStepTracker;
    boolean mCharging;
    public final android.util.AtomicFile mCheckinFile;
    protected com.android.internal.os.Clock mClock;
    protected final com.android.server.power.stats.BatteryStatsImpl.Constants mConstants;
    private int[] mCpuPowerBracketMap;
    com.android.server.power.stats.CpuPowerCalculator mCpuPowerCalculator;
    private final com.android.server.power.stats.CpuPowerStatsCollector mCpuPowerStatsCollector;
    protected com.android.internal.os.CpuScalingPolicies mCpuScalingPolicies;
    private long mCpuTimeReadsTrackingStartTimeMs;
    protected com.android.internal.os.KernelCpuUidTimeReader.KernelCpuUidActiveTimeReader mCpuUidActiveTimeReader;
    protected com.android.internal.os.KernelCpuUidTimeReader.KernelCpuUidClusterTimeReader mCpuUidClusterTimeReader;
    protected com.android.internal.os.KernelCpuUidTimeReader.KernelCpuUidFreqTimeReader mCpuUidFreqTimeReader;
    protected com.android.internal.os.KernelCpuUidTimeReader.KernelCpuUidUserSysTimeReader mCpuUidUserSysTimeReader;
    int mCurStepMode;
    final android.os.BatteryStats.LevelStepTracker mDailyChargeStepTracker;
    final android.os.BatteryStats.LevelStepTracker mDailyDischargeStepTracker;
    public final android.util.AtomicFile mDailyFile;
    final java.util.ArrayList<android.os.BatteryStats.DailyItem> mDailyItems;
    java.util.ArrayList<android.os.BatteryStats.PackageChange> mDailyPackageChanges;
    long mDailyStartTimeMs;
    private final java.lang.Runnable mDeferSetCharging;
    int mDeviceIdleMode;
    com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer mDeviceIdleModeFullTimer;
    com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer mDeviceIdleModeLightTimer;
    boolean mDeviceIdling;
    com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer mDeviceIdlingTimer;
    boolean mDeviceLightIdling;
    com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer mDeviceLightIdlingTimer;
    int mDischargeAmountScreenDoze;
    int mDischargeAmountScreenDozeSinceCharge;
    int mDischargeAmountScreenOff;
    int mDischargeAmountScreenOffSinceCharge;
    int mDischargeAmountScreenOn;
    int mDischargeAmountScreenOnSinceCharge;
    private com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounter mDischargeCounter;
    int mDischargeCurrentLevel;
    private com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounter mDischargeDeepDozeCounter;
    private com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounter mDischargeLightDozeCounter;
    int mDischargePlugLevel;
    private com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounter mDischargeScreenDozeCounter;
    int mDischargeScreenDozeUnplugLevel;
    private com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounter mDischargeScreenOffCounter;
    int mDischargeScreenOffUnplugLevel;
    int mDischargeScreenOnUnplugLevel;
    final android.os.BatteryStats.LevelStepTracker mDischargeStepTracker;
    int mDischargeUnplugLevel;
    private int mDisplayMismatchWtfCount;
    private final java.util.ArrayList<com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer> mDrawTimers;
    java.lang.String mEndPlatformVersion;
    public final com.android.server.power.stats.BatteryStatsImpl.EnergyStatsRetriever mEnergyConsumerRetriever;
    protected com.android.internal.power.EnergyConsumerStats.Config mEnergyConsumerStatsConfig;
    private int mEstimatedBatteryCapacityMah;
    private com.android.server.power.stats.BatteryStatsImpl.ExternalStatsSync mExternalSync;
    int mFlashlightOnNesting;
    com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer mFlashlightOnTimer;
    private final java.util.ArrayList<com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer> mFlashlightTurnedOnTimers;
    private final com.android.server.power.stats.BatteryStatsImpl.FrameworkStatsLogger mFrameworkStatsLogger;
    private final java.util.ArrayList<com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer> mFullTimers;
    private final java.util.ArrayList<com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer> mFullWifiLockTimers;
    protected com.android.internal.power.EnergyConsumerStats mGlobalEnergyConsumerStats;
    boolean mGlobalWifiRunning;
    com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer mGlobalWifiRunningTimer;
    private final com.android.server.power.stats.GnssPowerStatsCollector mGnssPowerStatsCollector;
    int mGpsNesting;
    int mGpsSignalQualityBin;
    final com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer[] mGpsSignalQualityTimer;
    public android.os.Handler mHandler;
    boolean mHasBluetoothReporting;
    boolean mHasModemReporting;
    boolean mHasWifiReporting;
    private boolean mHaveBatteryLevel;
    int mHighDischargeAmountSinceCharge;
    private final com.android.internal.os.BatteryStatsHistory mHistory;
    boolean mIgnoreNextExternalStats;
    int mInitStepMode;
    boolean mInteractive;
    com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer mInteractiveTimer;
    protected com.android.internal.os.KernelCpuSpeedReader[] mKernelCpuSpeedReaders;
    private com.android.internal.os.KernelMemoryBandwidthStats mKernelMemoryBandwidthStats;
    private final android.util.LongSparseArray<com.android.server.power.stats.BatteryStatsImpl.SamplingTimer> mKernelMemoryStats;
    protected com.android.internal.os.KernelSingleUidTimeReader mKernelSingleUidTimeReader;
    protected com.android.server.power.stats.KernelWakelockReader mKernelWakelockReader;
    private final java.util.HashMap<java.lang.String, com.android.server.power.stats.BatteryStatsImpl.SamplingTimer> mKernelWakelockStats;
    private final com.android.server.power.stats.BatteryStatsImpl.BluetoothActivityInfoCache mLastBluetoothActivityInfo;
    int mLastChargeStepLevel;
    int mLastDischargeStepLevel;
    long mLastIdleTimeStartMs;
    private int mLastLearnedBatteryCapacityUah;
    private android.telephony.ModemActivityInfo mLastModemActivityInfo;
    private android.net.NetworkStats mLastModemNetworkStats;
    protected java.util.ArrayList<com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer> mLastPartialTimers;
    private long mLastRpmStatsUpdateTimeMs;
    long mLastWakeupElapsedTimeMs;
    java.lang.String mLastWakeupReason;
    long mLastWakeupUptimeMs;
    private android.net.NetworkStats mLastWifiNetworkStats;
    long mLastWriteTimeMs;
    private final android.app.AlarmManager.OnAlarmListener mLongPlugInAlarmHandler;
    long mLongestFullIdleTimeMs;
    long mLongestLightIdleTimeMs;
    int mLowDischargeAmountSinceCharge;
    int mMaxChargeStepLevel;
    private int mMaxLearnedBatteryCapacityUah;
    int mMinDischargeStepLevel;
    private int mMinLearnedBatteryCapacityUah;
    com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounter mMobileRadioActiveAdjustedTime;
    com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer mMobileRadioActivePerAppTimer;
    long mMobileRadioActiveStartTimeMs;
    com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer mMobileRadioActiveTimer;
    com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounter mMobileRadioActiveUnknownCount;
    com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounter mMobileRadioActiveUnknownTime;
    com.android.server.power.stats.MobileRadioPowerCalculator mMobileRadioPowerCalculator;
    int mMobileRadioPowerState;
    private final com.android.server.power.stats.MobileRadioPowerStatsCollector mMobileRadioPowerStatsCollector;
    int mModStepMode;
    com.android.server.power.stats.BatteryStatsImpl.ControllerActivityCounterImpl mModemActivity;
    private java.lang.String[] mModemIfaces;
    private final java.lang.Object mModemNetworkLock;
    private final com.android.internal.os.MonotonicClock mMonotonicClock;
    long mMonotonicEndTime;
    long mMonotonicStartTime;
    final com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounter[] mNetworkByteActivityCounters;
    final com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounter[] mNetworkPacketActivityCounters;
    long mNextMaxDailyDeadlineMs;
    long mNextMinDailyDeadlineMs;
    boolean mNoAutoReset;
    com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer mNrNsaTimer;
    int mNrState;
    private int mNumAllUidCpuTimeReads;
    private int mNumConnectivityChange;
    private long mNumSingleUidCpuTimeReads;
    private int mNumUidsRemoved;
    boolean mOnBattery;
    protected boolean mOnBatteryInternal;
    protected final com.android.server.power.stats.BatteryStatsImpl.TimeBase mOnBatteryScreenOffTimeBase;
    protected final com.android.server.power.stats.BatteryStatsImpl.TimeBase mOnBatteryTimeBase;
    protected java.util.ArrayList<com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer> mPartialTimers;
    protected java.util.Queue<com.android.server.power.stats.BatteryStatsImpl.UidToRemove> mPendingRemovedUids;
    com.android.server.power.stats.BatteryStatsImpl.DisplayBatteryStats[] mPerDisplayBatteryStats;
    public boolean mPerProcStateCpuTimesAvailable;
    com.android.server.power.stats.BatteryStatsImpl.RadioAccessTechnologyBatteryStats[] mPerRatBatteryStats;
    int mPhoneDataConnectionType;
    final com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer[] mPhoneDataConnectionsTimer;
    boolean mPhoneOn;
    com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer mPhoneOnTimer;
    private int mPhoneServiceState;
    private int mPhoneServiceStateRaw;
    com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer mPhoneSignalScanningTimer;
    int mPhoneSignalStrengthBin;
    int mPhoneSignalStrengthBinRaw;
    final com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer[] mPhoneSignalStrengthsTimer;
    private int mPhoneSimStateRaw;
    private final com.android.server.power.stats.BatteryStatsImpl.PlatformIdleStateCallback mPlatformIdleStateCallback;
    protected com.android.internal.os.PowerProfile mPowerProfile;
    boolean mPowerSaveModeEnabled;
    com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer mPowerSaveModeEnabledTimer;
    private final android.util.SparseBooleanArray mPowerStatsCollectorEnabled;
    private final com.android.server.power.stats.BatteryStatsImpl.PowerStatsCollectorInjector mPowerStatsCollectorInjector;
    private com.android.server.power.stats.PowerStatsStore mPowerStatsStore;
    protected final com.android.server.power.stats.PowerStatsUidResolver mPowerStatsUidResolver;
    boolean mPretendScreenOff;
    long mRealtimeStartUs;
    long mRealtimeUs;
    public boolean mRecordAllHistory;
    private final java.util.HashMap<java.lang.String, com.android.server.power.stats.BatteryStatsImpl.SamplingTimer> mRpmStats;
    private boolean mSaveBatteryUsageStatsOnReset;
    int mScreenBrightnessBin;
    final com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer[] mScreenBrightnessTimer;
    com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer mScreenDozeTimer;
    private final java.util.HashMap<java.lang.String, com.android.server.power.stats.BatteryStatsImpl.SamplingTimer> mScreenOffRpmStats;
    com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer mScreenOnTimer;
    protected int mScreenState;
    int mSensorNesting;
    private final android.util.SparseArray<java.util.ArrayList<com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer>> mSensorTimers;
    private boolean mShuttingDown;
    long mStartClockTimeMs;
    int mStartCount;
    java.lang.String mStartPlatformVersion;
    private final android.util.AtomicFile mStatsFile;
    private final com.android.server.power.stats.BatteryStatsImpl.HistoryStepDetailsCalculatorImpl mStepDetailsCalculator;
    private boolean mSystemReady;
    protected com.android.server.power.stats.SystemServerCpuThreadReader mSystemServerCpuThreadReader;
    long mTempTotalCpuSystemTimeUs;
    long mTempTotalCpuUserTimeUs;
    private com.android.internal.os.LongArrayMultiStateCounter.LongArrayContainer mTmpCpuTimeInFreq;
    private com.android.internal.os.RailStats mTmpRailStats;
    private com.android.internal.os.RpmStats mTmpRpmStats;
    private final com.android.server.power.stats.KernelWakelockStats mTmpWakelockStats;
    private final android.util.SparseArray<com.android.server.power.stats.BatteryStatsImpl.Uid> mUidStats;
    long mUptimeStartUs;
    long mUptimeUs;
    int mUsbDataState;
    protected com.android.server.power.stats.BatteryStatsImpl.UserInfoProvider mUserInfoProvider;
    int mVideoOnNesting;
    com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer mVideoOnTimer;
    private final java.util.ArrayList<com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer> mVideoTurnedOnTimers;
    long[][] mWakeLockAllocationsUs;
    boolean mWakeLockImportant;
    int mWakeLockNesting;
    private final java.util.HashMap<java.lang.String, com.android.server.power.stats.BatteryStatsImpl.SamplingTimer> mWakeupReasonStats;
    com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer mWifiActiveTimer;
    com.android.server.power.stats.BatteryStatsImpl.ControllerActivityCounterImpl mWifiActivity;
    private final android.util.SparseArray<java.util.ArrayList<com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer>> mWifiBatchedScanTimers;
    private int mWifiFullLockNesting;
    private java.lang.String[] mWifiIfaces;
    private int mWifiMulticastNesting;
    private final java.util.ArrayList<com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer> mWifiMulticastTimers;
    com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer mWifiMulticastWakelockTimer;
    private final java.lang.Object mWifiNetworkLock;
    boolean mWifiOn;
    com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer mWifiOnTimer;
    com.android.server.power.stats.WifiPowerCalculator mWifiPowerCalculator;
    private final com.android.server.power.stats.WifiPowerStatsCollector mWifiPowerStatsCollector;
    int mWifiRadioPowerState;
    private final java.util.ArrayList<com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer> mWifiRunningTimers;
    int mWifiScanNesting;
    private final java.util.ArrayList<com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer> mWifiScanTimers;
    int mWifiSignalStrengthBin;
    final com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer[] mWifiSignalStrengthsTimer;
    int mWifiState;
    final com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer[] mWifiStateTimer;
    private final com.android.server.power.stats.WifiPowerStatsCollector.WifiStatsRetriever mWifiStatsRetriever;
    int mWifiSupplState;
    final com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer[] mWifiSupplStateTimer;
    private final java.util.ArrayList<com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer> mWindowTimers;
    private final java.lang.Runnable mWriteAsyncRunnable;
    private final java.util.concurrent.locks.ReentrantLock mWriteLock;

    public interface BatteryCallback {
        void batteryNeedsCpuUpdate();

        void batteryPowerChanged(boolean z);

        void batterySendBroadcast(android.content.Intent intent);

        void batteryStatsReset();
    }

    public interface EnergyStatsRetriever {
        void fillRailDataStats(com.android.internal.os.RailStats railStats);
    }

    public interface ExternalStatsSync {
        public static final int RESET = 128;
        public static final int UPDATE_ALL = 127;
        public static final int UPDATE_BT = 8;
        public static final int UPDATE_CAMERA = 64;
        public static final int UPDATE_CPU = 1;
        public static final int UPDATE_DISPLAY = 32;
        public static final int UPDATE_ON_PROC_STATE_CHANGE = 14;
        public static final int UPDATE_ON_RESET = 255;
        public static final int UPDATE_RADIO = 4;
        public static final int UPDATE_RPM = 16;
        public static final int UPDATE_WIFI = 2;

        @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
        public @interface ExternalUpdateFlag {
        }

        void cancelCpuSyncDueToWakelockChange();

        java.util.concurrent.Future<?> scheduleCleanupDueToRemovedUser(int i);

        java.util.concurrent.Future<?> scheduleCpuSyncDueToRemovedUid(int i);

        java.util.concurrent.Future<?> scheduleCpuSyncDueToWakelockChange(long j);

        java.util.concurrent.Future<?> scheduleSync(java.lang.String str, int i);

        java.util.concurrent.Future<?> scheduleSyncDueToBatteryLevelChange(long j);

        void scheduleSyncDueToProcessStateChange(int i, long j);

        java.util.concurrent.Future<?> scheduleSyncDueToScreenStateChange(int i, boolean z, boolean z2, int i2, int[] iArr);
    }

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface PerUidModemPowerModel {
    }

    public interface PlatformIdleStateCallback {
        void fillLowPowerStats(com.android.internal.os.RpmStats rpmStats);

        java.lang.String getSubsystemLowPowerStats();
    }

    static {
        VERSION = !com.android.internal.hidden_from_bootclasspath.com.android.server.power.optimization.Flags.disableSystemServicePowerAttr() ? com.android.internal.util.FrameworkStatsLog.DEVICE_POLICY_EVENT__EVENT_ID__PLATFORM_ROLE_HOLDER_UPDATE_FINISHED : com.android.internal.util.FrameworkStatsLog.DEVICE_POLICY_EVENT__EVENT_ID__PLATFORM_ROLE_HOLDER_UPDATE_FAILED;
        MAX_WAKELOCKS_PER_UID = isLowRamDevice() ? 40 : 200;
        CELL_SIGNAL_STRENGTH_LEVEL_COUNT = getCellSignalStrengthLevelCount();
        MODEM_TX_POWER_LEVEL_COUNT = getModemTxPowerLevelCount();
        ZERO_LONG_COUNTER = new android.os.BatteryStats.LongCounter() { // from class: com.android.server.power.stats.BatteryStatsImpl.1
            public long getCountLocked(int which) {
                return 0L;
            }

            public long getCountForProcessState(int procState) {
                return 0L;
            }

            public void logState(android.util.Printer pw, java.lang.String prefix) {
                pw.println(prefix + "mCount=0");
            }
        };
        ZERO_LONG_COUNTER_ARRAY = new android.os.BatteryStats.LongCounter[]{ZERO_LONG_COUNTER};
        SUPPORTED_PER_PROCESS_STATE_STANDARD_ENERGY_BUCKETS = new int[]{3, 7, 4, 5};
    }

    private class BluetoothStatsRetrieverImpl implements com.android.server.power.stats.BluetoothPowerStatsCollector.BluetoothStatsRetriever {
        private final android.bluetooth.BluetoothManager mBluetoothManager;

        BluetoothStatsRetrieverImpl(android.bluetooth.BluetoothManager bluetoothManager) {
            this.mBluetoothManager = bluetoothManager;
        }

        @Override // com.android.server.power.stats.BluetoothPowerStatsCollector.BluetoothStatsRetriever
        public void retrieveBluetoothScanTimes(com.android.server.power.stats.BluetoothPowerStatsCollector.BluetoothStatsRetriever.Callback callback) {
            synchronized (com.android.server.power.stats.BatteryStatsImpl.this) {
                com.android.server.power.stats.BatteryStatsImpl.this.retrieveBluetoothScanTimesLocked(callback);
            }
        }

        @Override // com.android.server.power.stats.BluetoothPowerStatsCollector.BluetoothStatsRetriever
        public boolean requestControllerActivityEnergyInfo(java.util.concurrent.Executor executor, android.bluetooth.BluetoothAdapter.OnBluetoothActivityEnergyInfoCallback callback) {
            android.bluetooth.BluetoothAdapter adapter;
            if (this.mBluetoothManager == null || (adapter = this.mBluetoothManager.getAdapter()) == null) {
                return false;
            }
            adapter.requestControllerActivityEnergyInfo(executor, callback);
            return true;
        }
    }

    public android.util.LongSparseArray<com.android.server.power.stats.BatteryStatsImpl.SamplingTimer> getKernelMemoryStats() {
        return this.mKernelMemoryStats;
    }

    public com.android.internal.os.BatteryStatsHistory getHistory() {
        return this.mHistory;
    }

    com.android.internal.os.BatteryStatsHistory copyHistory() {
        return this.mHistory.copy();
    }

    public final class UidToRemove {
        private final int mEndUid;
        private final int mStartUid;
        private final long mUidRemovalTimestamp;

        public UidToRemove(com.android.server.power.stats.BatteryStatsImpl this$0, int uid, long timestamp) {
            this(uid, uid, timestamp);
        }

        public UidToRemove(int startUid, int endUid, long timestamp) {
            this.mStartUid = startUid;
            this.mEndUid = endUid;
            this.mUidRemovalTimestamp = timestamp;
        }

        public long getUidRemovalTimestamp() {
            return this.mUidRemovalTimestamp;
        }

        void removeLocked() {
            com.android.server.power.stats.BatteryStatsImpl.this.removeCpuStatsForUidRangeLocked(this.mStartUid, this.mEndUid);
        }
    }

    public static abstract class UserInfoProvider {
        private int[] userIds;

        protected abstract int[] getUserIds();

        public final void refreshUserIds() {
            this.userIds = getUserIds();
        }

        public boolean exists(int userId) {
            if (this.userIds != null) {
                return com.android.internal.util.ArrayUtils.contains(this.userIds, userId);
            }
            return true;
        }
    }

    public static class BatteryStatsConfig {
        static final int RESET_ON_UNPLUG_AFTER_SIGNIFICANT_CHARGE_FLAG = 2;
        static final int RESET_ON_UNPLUG_HIGH_BATTERY_LEVEL_FLAG = 1;
        private final java.lang.Long mDefaultPowerStatsThrottlePeriod;
        private final int mFlags;
        private final java.util.Map<java.lang.String, java.lang.Long> mPowerStatsThrottlePeriods;

        public BatteryStatsConfig() {
            this.mFlags = 0;
            this.mDefaultPowerStatsThrottlePeriod = 0L;
            this.mPowerStatsThrottlePeriods = java.util.Map.of();
        }

        private BatteryStatsConfig(com.android.server.power.stats.BatteryStatsImpl.BatteryStatsConfig.Builder builder) {
            int flags = builder.mResetOnUnplugHighBatteryLevel ? 0 | 1 : 0;
            this.mFlags = builder.mResetOnUnplugAfterSignificantCharge ? flags | 2 : flags;
            this.mDefaultPowerStatsThrottlePeriod = java.lang.Long.valueOf(builder.mDefaultPowerStatsThrottlePeriod);
            this.mPowerStatsThrottlePeriods = builder.mPowerStatsThrottlePeriods;
        }

        public boolean shouldResetOnUnplugHighBatteryLevel() {
            return (this.mFlags & 1) == 1;
        }

        public boolean shouldResetOnUnplugAfterSignificantCharge() {
            return (this.mFlags & 2) == 2;
        }

        public long getPowerStatsThrottlePeriod(java.lang.String powerComponentName) {
            return this.mPowerStatsThrottlePeriods.getOrDefault(powerComponentName, this.mDefaultPowerStatsThrottlePeriod).longValue();
        }

        public static class Builder {
            public static final long DEFAULT_POWER_STATS_THROTTLE_PERIOD = java.util.concurrent.TimeUnit.HOURS.toMillis(1);
            public static final long DEFAULT_POWER_STATS_THROTTLE_PERIOD_CPU = java.util.concurrent.TimeUnit.MINUTES.toMillis(1);
            private long mDefaultPowerStatsThrottlePeriod = DEFAULT_POWER_STATS_THROTTLE_PERIOD;
            private final java.util.Map<java.lang.String, java.lang.Long> mPowerStatsThrottlePeriods = new java.util.HashMap();
            private boolean mResetOnUnplugHighBatteryLevel = true;
            private boolean mResetOnUnplugAfterSignificantCharge = true;

            public Builder() {
                setPowerStatsThrottlePeriodMillis(android.os.BatteryConsumer.powerComponentIdToString(1), DEFAULT_POWER_STATS_THROTTLE_PERIOD_CPU);
            }

            public com.android.server.power.stats.BatteryStatsImpl.BatteryStatsConfig build() {
                return new com.android.server.power.stats.BatteryStatsImpl.BatteryStatsConfig(this);
            }

            public com.android.server.power.stats.BatteryStatsImpl.BatteryStatsConfig.Builder setResetOnUnplugHighBatteryLevel(boolean reset) {
                this.mResetOnUnplugHighBatteryLevel = reset;
                return this;
            }

            public com.android.server.power.stats.BatteryStatsImpl.BatteryStatsConfig.Builder setResetOnUnplugAfterSignificantCharge(boolean reset) {
                this.mResetOnUnplugAfterSignificantCharge = reset;
                return this;
            }

            public com.android.server.power.stats.BatteryStatsImpl.BatteryStatsConfig.Builder setPowerStatsThrottlePeriodMillis(java.lang.String powerComponentName, long periodMs) {
                this.mPowerStatsThrottlePeriods.put(powerComponentName, java.lang.Long.valueOf(periodMs));
                return this;
            }

            public com.android.server.power.stats.BatteryStatsImpl.BatteryStatsConfig.Builder setDefaultPowerStatsThrottlePeriodMillis(long periodMs) {
                this.mDefaultPowerStatsThrottlePeriod = periodMs;
                return this;
            }
        }
    }

    final class MyHandler extends android.os.Handler {
        public MyHandler(android.os.Looper looper) {
            super(looper, null, true);
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            java.lang.String action;
            if (com.android.server.power.stats.BatteryStatsImpl.this.mBatteryStatsImplExt.onBatteryStatsMessageHandle(msg)) {
                return;
            }
            com.android.server.power.stats.BatteryStatsImpl.BatteryCallback cb = com.android.server.power.stats.BatteryStatsImpl.this.mCallback;
            switch (msg.what) {
                case 1:
                    if (cb != null) {
                        cb.batteryNeedsCpuUpdate();
                        return;
                    }
                    return;
                case 2:
                    if (cb != null) {
                        cb.batteryPowerChanged(msg.arg1 != 0);
                        return;
                    }
                    return;
                case 3:
                    if (cb != null) {
                        synchronized (com.android.server.power.stats.BatteryStatsImpl.this) {
                            action = com.android.server.power.stats.BatteryStatsImpl.this.mCharging ? "android.os.action.CHARGING" : "android.os.action.DISCHARGING";
                            break;
                        }
                        android.content.Intent intent = new android.content.Intent(action);
                        intent.addFlags(67108864);
                        cb.batterySendBroadcast(intent);
                        return;
                    }
                    return;
                case 4:
                    if (cb != null) {
                        cb.batteryStatsReset();
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    public void postBatteryNeedsCpuUpdateMsg() {
        this.mHandler.sendEmptyMessage(1);
    }

    public void updateProcStateCpuTimesLocked(int uid, long elapsedRealtimeMs, long uptimeMs) {
        if (this.mPowerStatsCollectorEnabled.get(1)) {
            return;
        }
        ensureKernelSingleUidTimeReaderLocked();
        com.android.server.power.stats.BatteryStatsImpl.Uid u = getUidStatsLocked(uid);
        this.mNumSingleUidCpuTimeReads++;
        com.android.internal.os.LongArrayMultiStateCounter onBatteryCounter = u.getProcStateTimeCounter(elapsedRealtimeMs).getCounter();
        com.android.internal.os.LongArrayMultiStateCounter onBatteryScreenOffCounter = u.getProcStateScreenOffTimeCounter(elapsedRealtimeMs).getCounter();
        this.mKernelSingleUidTimeReader.addDelta(uid, onBatteryCounter, elapsedRealtimeMs);
        this.mKernelSingleUidTimeReader.addDelta(uid, onBatteryScreenOffCounter, elapsedRealtimeMs);
        if (u.mChildUids != null) {
            com.android.internal.os.LongArrayMultiStateCounter.LongArrayContainer deltaContainer = getCpuTimeInFreqContainer();
            int childUidCount = u.mChildUids.size();
            for (int j = childUidCount - 1; j >= 0; j--) {
                com.android.internal.os.LongArrayMultiStateCounter cpuTimeInFreqCounter = u.mChildUids.valueAt(j).cpuTimeInFreqCounter;
                if (cpuTimeInFreqCounter != null) {
                    this.mKernelSingleUidTimeReader.addDelta(u.mChildUids.keyAt(j), cpuTimeInFreqCounter, elapsedRealtimeMs, deltaContainer);
                    onBatteryCounter.addCounts(deltaContainer);
                    onBatteryScreenOffCounter.addCounts(deltaContainer);
                }
            }
        }
    }

    public void clearPendingRemovedUidsLocked() {
        long cutOffTimeMs = this.mClock.elapsedRealtime() - this.mConstants.UID_REMOVE_DELAY_MS;
        while (!this.mPendingRemovedUids.isEmpty() && this.mPendingRemovedUids.peek().getUidRemovalTimestamp() < cutOffTimeMs) {
            this.mPendingRemovedUids.poll().removeLocked();
        }
    }

    public void updateCpuTimesForAllUids() {
        android.util.SparseArray<long[]> allUidCpuFreqTimesMs;
        if (this.mPowerStatsCollectorEnabled.get(1)) {
            this.mCpuPowerStatsCollector.schedule();
            return;
        }
        synchronized (this) {
            if (trackPerProcStateCpuTimes()) {
                ensureKernelSingleUidTimeReaderLocked();
                android.util.SparseArray<long[]> allUidCpuFreqTimesMs2 = this.mCpuUidFreqTimeReader.getAllUidCpuFreqTimeMs();
                int i = allUidCpuFreqTimesMs2.size() - 1;
                while (i >= 0) {
                    int uid = allUidCpuFreqTimesMs2.keyAt(i);
                    int parentUid = mapUid(uid);
                    com.android.server.power.stats.BatteryStatsImpl.Uid u = getAvailableUidStatsLocked(parentUid);
                    if (u == null) {
                        allUidCpuFreqTimesMs = allUidCpuFreqTimesMs2;
                    } else {
                        int procState = u.mProcessState;
                        if (procState == 7) {
                            allUidCpuFreqTimesMs = allUidCpuFreqTimesMs2;
                        } else {
                            long elapsedRealtimeMs = this.mClock.elapsedRealtime();
                            this.mClock.uptimeMillis();
                            com.android.internal.os.LongArrayMultiStateCounter onBatteryCounter = u.getProcStateTimeCounter(elapsedRealtimeMs).getCounter();
                            com.android.internal.os.LongArrayMultiStateCounter onBatteryScreenOffCounter = u.getProcStateScreenOffTimeCounter(elapsedRealtimeMs).getCounter();
                            if (uid == parentUid || android.os.Process.isSdkSandboxUid(uid)) {
                                allUidCpuFreqTimesMs = allUidCpuFreqTimesMs2;
                                com.android.internal.os.LongArrayMultiStateCounter onBatteryScreenOffCounter2 = onBatteryScreenOffCounter;
                                com.android.internal.os.LongArrayMultiStateCounter onBatteryCounter2 = onBatteryCounter;
                                this.mKernelSingleUidTimeReader.addDelta(parentUid, onBatteryCounter2, elapsedRealtimeMs);
                                this.mKernelSingleUidTimeReader.addDelta(parentUid, onBatteryScreenOffCounter2, elapsedRealtimeMs);
                            } else {
                                com.android.server.power.stats.BatteryStatsImpl.Uid.ChildUid childUid = u.getChildUid(uid);
                                if (childUid == null) {
                                    allUidCpuFreqTimesMs = allUidCpuFreqTimesMs2;
                                } else {
                                    com.android.internal.os.LongArrayMultiStateCounter counter = childUid.cpuTimeInFreqCounter;
                                    if (counter == null) {
                                        allUidCpuFreqTimesMs = allUidCpuFreqTimesMs2;
                                    } else {
                                        com.android.internal.os.LongArrayMultiStateCounter.LongArrayContainer deltaContainer = getCpuTimeInFreqContainer();
                                        allUidCpuFreqTimesMs = allUidCpuFreqTimesMs2;
                                        this.mKernelSingleUidTimeReader.addDelta(uid, counter, elapsedRealtimeMs, deltaContainer);
                                        onBatteryCounter.addCounts(deltaContainer);
                                        onBatteryScreenOffCounter.addCounts(deltaContainer);
                                    }
                                }
                            }
                        }
                    }
                    i--;
                    allUidCpuFreqTimesMs2 = allUidCpuFreqTimesMs;
                }
            }
        }
    }

    private void ensureKernelSingleUidTimeReaderLocked() {
        if (this.mPowerStatsCollectorEnabled.get(1) || this.mKernelSingleUidTimeReader != null) {
            return;
        }
        this.mKernelSingleUidTimeReader = new com.android.internal.os.KernelSingleUidTimeReader(this.mCpuScalingPolicies.getScalingStepCount());
        this.mPerProcStateCpuTimesAvailable = this.mCpuUidFreqTimeReader.perClusterTimesAvailable() && this.mKernelSingleUidTimeReader.singleUidCpuTimesAvailable();
    }

    private static class DisplayBatteryStats {
        public com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer screenDozeTimer;
        public com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer screenOnTimer;
        public int screenState = 0;
        public int screenBrightnessBin = -1;
        public com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer[] screenBrightnessTimers = new com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer[5];
        public int screenStateAtLastEnergyMeasurement = 0;

        DisplayBatteryStats(com.android.internal.os.Clock clock, com.android.server.power.stats.BatteryStatsImpl.TimeBase timeBase) {
            this.screenOnTimer = new com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer(clock, null, -1, null, timeBase);
            this.screenDozeTimer = new com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer(clock, null, -1, null, timeBase);
            for (int i = 0; i < 5; i++) {
                this.screenBrightnessTimers[i] = new com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer(clock, null, (-100) - i, null, timeBase);
            }
        }

        public void reset(long elapsedRealtimeUs) {
            this.screenOnTimer.reset(false, elapsedRealtimeUs);
            this.screenDozeTimer.reset(false, elapsedRealtimeUs);
            for (int i = 0; i < 5; i++) {
                this.screenBrightnessTimers[i].reset(false, elapsedRealtimeUs);
            }
        }

        public void writeSummaryToParcel(android.os.Parcel out, long elapsedRealtimeUs) {
            this.screenOnTimer.writeSummaryFromParcelLocked(out, elapsedRealtimeUs);
            this.screenDozeTimer.writeSummaryFromParcelLocked(out, elapsedRealtimeUs);
            for (int i = 0; i < 5; i++) {
                this.screenBrightnessTimers[i].writeSummaryFromParcelLocked(out, elapsedRealtimeUs);
            }
        }

        public void readSummaryFromParcel(android.os.Parcel in) {
            this.screenOnTimer.readSummaryFromParcelLocked(in);
            this.screenDozeTimer.readSummaryFromParcelLocked(in);
            for (int i = 0; i < 5; i++) {
                this.screenBrightnessTimers[i].readSummaryFromParcelLocked(in);
            }
        }
    }

    private static class RadioAccessTechnologyBatteryStats {
        public final com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer[][] perStateTimers;
        private boolean mActive = false;
        private int mFrequencyRange = 0;
        private int mSignalStrength = 0;
        private com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounter[][] mPerStateTxDurationMs = null;
        private com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounter[] mPerFrequencyRxDurationMs = null;

        RadioAccessTechnologyBatteryStats(int freqCount, com.android.internal.os.Clock clock, com.android.server.power.stats.BatteryStatsImpl.TimeBase timeBase) {
            this.perStateTimers = (com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer.class, freqCount, 5);
            for (int i = 0; i < freqCount; i++) {
                for (int j = 0; j < 5; j++) {
                    this.perStateTimers[i][j] = new com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer(clock, null, -1, null, timeBase);
                }
            }
        }

        public void noteActive(boolean active, long elapsedRealtimeMs) {
            if (this.mActive == active) {
                return;
            }
            this.mActive = active;
            if (this.mActive) {
                this.perStateTimers[this.mFrequencyRange][this.mSignalStrength].startRunningLocked(elapsedRealtimeMs);
            } else {
                this.perStateTimers[this.mFrequencyRange][this.mSignalStrength].stopRunningLocked(elapsedRealtimeMs);
            }
        }

        public void noteFrequencyRange(int frequencyRange, long elapsedRealtimeMs) {
            if (this.mFrequencyRange == frequencyRange) {
                return;
            }
            if (!this.mActive) {
                this.mFrequencyRange = frequencyRange;
                return;
            }
            this.perStateTimers[this.mFrequencyRange][this.mSignalStrength].stopRunningLocked(elapsedRealtimeMs);
            this.perStateTimers[frequencyRange][this.mSignalStrength].startRunningLocked(elapsedRealtimeMs);
            this.mFrequencyRange = frequencyRange;
        }

        public void noteSignalStrength(int signalStrength, long elapsedRealtimeMs) {
            if (this.mSignalStrength == signalStrength) {
                return;
            }
            if (!this.mActive) {
                this.mSignalStrength = signalStrength;
                return;
            }
            this.perStateTimers[this.mFrequencyRange][this.mSignalStrength].stopRunningLocked(elapsedRealtimeMs);
            this.perStateTimers[this.mFrequencyRange][signalStrength].startRunningLocked(elapsedRealtimeMs);
            this.mSignalStrength = signalStrength;
        }

        public long getTimeSinceMark(int frequencyRange, int signalStrength, long elapsedRealtimeMs) {
            return this.perStateTimers[frequencyRange][signalStrength].getTimeSinceMarkLocked(elapsedRealtimeMs * 1000) / 1000;
        }

        public void setMark(long elapsedRealtimeMs) {
            int size = this.perStateTimers.length;
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < 5; j++) {
                    this.perStateTimers[i][j].setMark(elapsedRealtimeMs);
                }
            }
        }

        public int getFrequencyRangeCount() {
            return this.perStateTimers.length;
        }

        public void incrementTxDuration(int frequencyRange, int signalStrength, long durationMs) {
            getTxDurationCounter(frequencyRange, signalStrength, true).addCountLocked(durationMs);
        }

        public void incrementRxDuration(int frequencyRange, long durationMs) {
            getRxDurationCounter(frequencyRange, true).addCountLocked(durationMs);
        }

        public void reset(long elapsedRealtimeUs) {
            int size = this.perStateTimers.length;
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < 5; j++) {
                    this.perStateTimers[i][j].reset(false, elapsedRealtimeUs);
                    if (this.mPerStateTxDurationMs != null) {
                        this.mPerStateTxDurationMs[i][j].reset(false, elapsedRealtimeUs);
                    }
                }
                if (this.mPerFrequencyRxDurationMs != null) {
                    this.mPerFrequencyRxDurationMs[i].reset(false, elapsedRealtimeUs);
                }
            }
        }

        public void writeSummaryToParcel(android.os.Parcel out, long elapsedRealtimeUs) {
            int freqCount = this.perStateTimers.length;
            out.writeInt(freqCount);
            out.writeInt(5);
            for (int i = 0; i < freqCount; i++) {
                for (int j = 0; j < 5; j++) {
                    this.perStateTimers[i][j].writeSummaryFromParcelLocked(out, elapsedRealtimeUs);
                }
            }
            if (this.mPerStateTxDurationMs == null) {
                out.writeInt(0);
            } else {
                out.writeInt(1);
                for (int i2 = 0; i2 < freqCount; i2++) {
                    for (int j2 = 0; j2 < 5; j2++) {
                        this.mPerStateTxDurationMs[i2][j2].writeSummaryFromParcelLocked(out);
                    }
                }
            }
            if (this.mPerFrequencyRxDurationMs == null) {
                out.writeInt(0);
                return;
            }
            out.writeInt(1);
            for (int i3 = 0; i3 < freqCount; i3++) {
                this.mPerFrequencyRxDurationMs[i3].writeSummaryFromParcelLocked(out);
            }
        }

        public void readSummaryFromParcel(android.os.Parcel in) {
            int oldFreqCount = in.readInt();
            int oldSignalStrengthCount = in.readInt();
            int currFreqCount = this.perStateTimers.length;
            for (int freq = 0; freq < oldFreqCount; freq++) {
                for (int strength = 0; strength < oldSignalStrengthCount; strength++) {
                    if (freq >= currFreqCount || strength >= 5) {
                        com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer temp = new com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer(null, null, -1, null, new com.android.server.power.stats.BatteryStatsImpl.TimeBase());
                        temp.readSummaryFromParcelLocked(in);
                    } else {
                        this.perStateTimers[freq][strength].readSummaryFromParcelLocked(in);
                    }
                }
            }
            int freq2 = in.readInt();
            if (freq2 == 1) {
                for (int freq3 = 0; freq3 < oldFreqCount; freq3++) {
                    for (int strength2 = 0; strength2 < oldSignalStrengthCount; strength2++) {
                        if (freq3 >= currFreqCount || strength2 >= 5) {
                            com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer temp2 = new com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer(null, null, -1, null, new com.android.server.power.stats.BatteryStatsImpl.TimeBase());
                            temp2.readSummaryFromParcelLocked(in);
                        }
                        getTxDurationCounter(freq3, strength2, true).readSummaryFromParcelLocked(in);
                    }
                }
            }
            int freq4 = in.readInt();
            if (freq4 == 1) {
                for (int freq5 = 0; freq5 < oldFreqCount; freq5++) {
                    if (freq5 >= currFreqCount) {
                        com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer temp3 = new com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer(null, null, -1, null, new com.android.server.power.stats.BatteryStatsImpl.TimeBase());
                        temp3.readSummaryFromParcelLocked(in);
                    } else {
                        getRxDurationCounter(freq5, true).readSummaryFromParcelLocked(in);
                    }
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounter getTxDurationCounter(int frequencyRange, int signalStrength, boolean make) {
            if (this.mPerStateTxDurationMs == null) {
                if (!make) {
                    return null;
                }
                int freqCount = getFrequencyRangeCount();
                int signalStrengthCount = this.perStateTimers[0].length;
                com.android.server.power.stats.BatteryStatsImpl.TimeBase timeBase = this.perStateTimers[0][0].mTimeBase;
                this.mPerStateTxDurationMs = (com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounter[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounter.class, freqCount, signalStrengthCount);
                for (int freq = 0; freq < freqCount; freq++) {
                    for (int strength = 0; strength < signalStrengthCount; strength++) {
                        this.mPerStateTxDurationMs[freq][strength] = new com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounter(timeBase);
                    }
                }
            }
            if (frequencyRange < 0 || frequencyRange >= getFrequencyRangeCount()) {
                android.util.Slog.w(com.android.server.power.stats.BatteryStatsImpl.TAG, "Unexpected frequency range (" + frequencyRange + ") requested in getTxDurationCounter");
                return null;
            }
            if (signalStrength < 0 || signalStrength >= this.perStateTimers[0].length) {
                android.util.Slog.w(com.android.server.power.stats.BatteryStatsImpl.TAG, "Unexpected signal strength (" + signalStrength + ") requested in getTxDurationCounter");
                return null;
            }
            return this.mPerStateTxDurationMs[frequencyRange][signalStrength];
        }

        /* JADX INFO: Access modifiers changed from: private */
        public com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounter getRxDurationCounter(int frequencyRange, boolean make) {
            if (this.mPerFrequencyRxDurationMs == null) {
                if (!make) {
                    return null;
                }
                int freqCount = getFrequencyRangeCount();
                com.android.server.power.stats.BatteryStatsImpl.TimeBase timeBase = this.perStateTimers[0][0].mTimeBase;
                this.mPerFrequencyRxDurationMs = new com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounter[freqCount];
                for (int freq = 0; freq < freqCount; freq++) {
                    this.mPerFrequencyRxDurationMs[freq] = new com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounter(timeBase);
                }
            }
            if (frequencyRange < 0 || frequencyRange >= getFrequencyRangeCount()) {
                android.util.Slog.w(com.android.server.power.stats.BatteryStatsImpl.TAG, "Unexpected frequency range (" + frequencyRange + ") requested in getRxDurationCounter");
                return null;
            }
            return this.mPerFrequencyRxDurationMs[frequencyRange];
        }
    }

    private com.android.server.power.stats.BatteryStatsImpl.RadioAccessTechnologyBatteryStats getRatBatteryStatsLocked(int rat) {
        com.android.server.power.stats.BatteryStatsImpl.RadioAccessTechnologyBatteryStats stats = this.mPerRatBatteryStats[rat];
        if (stats == null) {
            int freqCount = rat == 2 ? 5 : 1;
            com.android.server.power.stats.BatteryStatsImpl.RadioAccessTechnologyBatteryStats stats2 = new com.android.server.power.stats.BatteryStatsImpl.RadioAccessTechnologyBatteryStats(freqCount, this.mClock, this.mOnBatteryTimeBase);
            this.mPerRatBatteryStats[rat] = stats2;
            return stats2;
        }
        return stats;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1() {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.power.stats.BatteryStatsImpl$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$new$0();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        synchronized (this) {
            maybeResetWhilePluggedInLocked();
        }
    }

    public java.util.Map<java.lang.String, ? extends com.android.server.power.stats.BatteryStatsImpl.Timer> getRpmStats() {
        return this.mRpmStats;
    }

    public java.util.Map<java.lang.String, ? extends com.android.server.power.stats.BatteryStatsImpl.Timer> getScreenOffRpmStats() {
        return this.mScreenOffRpmStats;
    }

    public java.util.Map<java.lang.String, ? extends com.android.server.power.stats.BatteryStatsImpl.Timer> getKernelWakelockStats() {
        return this.mKernelWakelockStats;
    }

    public android.os.WakeLockStats getWakeLockStats() {
        long realtimeMs = this.mClock.elapsedRealtime();
        java.util.List<android.os.WakeLockStats.WakeLock> uidWakeLockStats = new java.util.ArrayList<>();
        java.util.List<android.os.WakeLockStats.WakeLock> uidAggregatedWakeLockStats = new java.util.ArrayList<>();
        for (int i = this.mUidStats.size() - 1; i >= 0; i--) {
            com.android.server.power.stats.BatteryStatsImpl.Uid uid = this.mUidStats.valueAt(i);
            android.util.ArrayMap<java.lang.String, com.android.server.power.stats.BatteryStatsImpl.Uid.Wakelock> map = uid.mWakelockStats.getMap();
            for (int j = map.size() - 1; j >= 0; j--) {
                java.lang.String name = map.keyAt(j);
                com.android.server.power.stats.BatteryStatsImpl.Uid.Wakelock wakelock = map.valueAt(j);
                android.os.WakeLockStats.WakeLock wakeLockItem = createWakeLock(uid, name, false, wakelock.mTimerPartial, realtimeMs);
                if (wakeLockItem != null) {
                    uidWakeLockStats.add(wakeLockItem);
                }
            }
            android.os.WakeLockStats.WakeLock aggregatedWakeLockItem = createWakeLock(uid, "wakelockstats_aggregated", true, uid.mAggregatedPartialWakelockTimer, realtimeMs);
            if (aggregatedWakeLockItem != null) {
                uidAggregatedWakeLockStats.add(aggregatedWakeLockItem);
            }
        }
        return new android.os.WakeLockStats(uidWakeLockStats, uidAggregatedWakeLockStats);
    }

    private android.os.WakeLockStats.WakeLock createWakeLock(com.android.server.power.stats.BatteryStatsImpl.Uid uid, java.lang.String name, boolean isAggregated, com.android.server.power.stats.BatteryStatsImpl.DualTimer timer, long realtimeMs) {
        if (timer == null) {
            return null;
        }
        android.os.WakeLockStats.WakeLockData totalWakeLockData = createWakeLockData(timer, realtimeMs);
        android.os.WakeLockStats.WakeLockData backgroundWakeLockData = createWakeLockData(timer.getSubTimer(), realtimeMs);
        if (!android.os.WakeLockStats.WakeLock.isDataValid(totalWakeLockData, backgroundWakeLockData)) {
            return null;
        }
        return new android.os.WakeLockStats.WakeLock(uid.getUid(), name, isAggregated, totalWakeLockData, backgroundWakeLockData);
    }

    private android.os.WakeLockStats.WakeLockData createWakeLockData(com.android.server.power.stats.BatteryStatsImpl.DurationTimer timer, long realtimeMs) {
        if (timer == null) {
            return android.os.WakeLockStats.WakeLockData.EMPTY;
        }
        long totalTimeLockHeldMs = timer.getTotalTimeLocked(realtimeMs * 1000, 0) / 1000;
        if (totalTimeLockHeldMs == 0) {
            return android.os.WakeLockStats.WakeLockData.EMPTY;
        }
        return new android.os.WakeLockStats.WakeLockData(timer.getCountLocked(0), totalTimeLockHeldMs, timer.isRunningLocked() ? timer.getCurrentDurationMsLocked(realtimeMs) : 0L);
    }

    public android.os.BluetoothBatteryStats getBluetoothBatteryStats() {
        long scanTimeMs;
        long totalTimeLocked;
        int countLocked;
        long rxTimeMs;
        long txTimeMs;
        long j = 1000;
        long elapsedRealtimeUs = this.mClock.elapsedRealtime() * 1000;
        java.util.ArrayList<android.os.BluetoothBatteryStats.UidStats> uidStats = new java.util.ArrayList<>();
        int i = this.mUidStats.size() - 1;
        while (i >= 0) {
            com.android.server.power.stats.BatteryStatsImpl.Uid uid = this.mUidStats.valueAt(i);
            com.android.server.power.stats.BatteryStatsImpl.Timer scanTimer = uid.getBluetoothScanTimer();
            if (scanTimer != null) {
                scanTimeMs = scanTimer.getTotalTimeLocked(elapsedRealtimeUs, 0) / j;
            } else {
                scanTimeMs = 0;
            }
            com.android.server.power.stats.BatteryStatsImpl.Timer unoptimizedScanTimer = uid.getBluetoothUnoptimizedScanTimer();
            if (unoptimizedScanTimer != null) {
                totalTimeLocked = unoptimizedScanTimer.getTotalTimeLocked(elapsedRealtimeUs, 0) / j;
            } else {
                totalTimeLocked = 0;
            }
            long unoptimizedScanTimeMs = totalTimeLocked;
            com.android.server.power.stats.BatteryStatsImpl.Counter scanResultCounter = uid.getBluetoothScanResultCounter();
            if (scanResultCounter != null) {
                countLocked = scanResultCounter.getCountLocked(0);
            } else {
                countLocked = 0;
            }
            int scanResultCount = countLocked;
            android.os.BatteryStats.ControllerActivityCounter counter = uid.getBluetoothControllerActivity();
            if (counter != null) {
                rxTimeMs = counter.getRxTimeCounter().getCountLocked(0);
            } else {
                rxTimeMs = 0;
            }
            if (counter != null) {
                txTimeMs = counter.getTxTimeCounters()[0].getCountLocked(0);
            } else {
                txTimeMs = 0;
            }
            if (scanTimeMs != 0 || unoptimizedScanTimeMs != 0 || scanResultCount != 0 || rxTimeMs != 0 || txTimeMs != 0) {
                uidStats.add(new android.os.BluetoothBatteryStats.UidStats(uid.getUid(), scanTimeMs, unoptimizedScanTimeMs, scanResultCount, rxTimeMs, txTimeMs));
            }
            i--;
            j = 1000;
        }
        return new android.os.BluetoothBatteryStats(uidStats);
    }

    public java.util.Map<java.lang.String, ? extends com.android.server.power.stats.BatteryStatsImpl.Timer> getWakeupReasonStats() {
        return this.mWakeupReasonStats;
    }

    public long getUahDischarge(int which) {
        return this.mDischargeCounter.getCountLocked(which);
    }

    public long getUahDischargeScreenOff(int which) {
        return this.mDischargeScreenOffCounter.getCountLocked(which);
    }

    public long getUahDischargeScreenDoze(int which) {
        return this.mDischargeScreenDozeCounter.getCountLocked(which);
    }

    public long getUahDischargeLightDoze(int which) {
        return this.mDischargeLightDozeCounter.getCountLocked(which);
    }

    public long getUahDischargeDeepDoze(int which) {
        return this.mDischargeDeepDozeCounter.getCountLocked(which);
    }

    public int getEstimatedBatteryCapacity() {
        return this.mEstimatedBatteryCapacityMah;
    }

    public int getLearnedBatteryCapacity() {
        return this.mLastLearnedBatteryCapacityUah;
    }

    public int getMinLearnedBatteryCapacity() {
        return this.mMinLearnedBatteryCapacityUah;
    }

    public int getMaxLearnedBatteryCapacity() {
        return this.mMaxLearnedBatteryCapacityUah;
    }

    public static class FrameworkStatsLogger {
        public void uidProcessStateChanged(int uid, int state) {
            com.android.internal.util.FrameworkStatsLog.write(27, uid, android.app.ActivityManager.processStateAmToProto(state));
        }

        public void wakelockStateChanged(int uid, android.os.WorkSource.WorkChain wc, java.lang.String name, int procState, boolean acquired, int powerManagerWakeLockLevel) {
            int event;
            if (acquired) {
                event = 1;
            } else {
                event = 0;
            }
            if (wc != null) {
                com.android.internal.util.FrameworkStatsLog.write(10, wc.getUids(), wc.getTags(), powerManagerWakeLockLevel, name, event, procState);
            } else {
                com.android.internal.util.FrameworkStatsLog.write_non_chained(10, uid, null, powerManagerWakeLockLevel, name, event, procState);
            }
        }

        public void kernelWakeupReported(long deltaUptimeUs, java.lang.String lastWakeupReason, long lastWakeupElapsedTimeMs) {
            com.android.internal.util.FrameworkStatsLog.write(36, lastWakeupReason, deltaUptimeUs, lastWakeupElapsedTimeMs);
        }

        public void gpsScanStateChanged(int uid, android.os.WorkSource.WorkChain workChain, boolean stateOn) {
            int event;
            if (stateOn) {
                event = 1;
            } else {
                event = 0;
            }
            if (workChain == null) {
                com.android.internal.util.FrameworkStatsLog.write_non_chained(6, uid, null, event);
            } else {
                com.android.internal.util.FrameworkStatsLog.write(6, workChain.getUids(), workChain.getTags(), event);
            }
        }

        public void batterySaverModeChanged(boolean enabled) {
            int i;
            if (enabled) {
                i = 1;
            } else {
                i = 0;
            }
            com.android.internal.util.FrameworkStatsLog.write(20, i);
        }

        public void deviceIdlingModeStateChanged(int mode) {
            com.android.internal.util.FrameworkStatsLog.write(22, mode);
        }

        public void deviceIdleModeStateChanged(int mode) {
            com.android.internal.util.FrameworkStatsLog.write(21, mode);
        }

        public void chargingStateChanged(int status) {
            com.android.internal.util.FrameworkStatsLog.write(31, status);
        }

        public void pluggedStateChanged(int plugType) {
            com.android.internal.util.FrameworkStatsLog.write(32, plugType);
        }

        public void batteryLevelChanged(int level) {
            com.android.internal.util.FrameworkStatsLog.write(30, level);
        }

        public void phoneServiceStateChanged(int state, int simState, int strengthBin) {
            com.android.internal.util.FrameworkStatsLog.write(94, state, simState, strengthBin);
        }

        public void phoneSignalStrengthChanged(int strengthBin) {
            com.android.internal.util.FrameworkStatsLog.write(40, strengthBin);
        }

        public void writeCommitSysConfigFile(java.lang.String fileName, long durationMs) {
            com.android.internal.logging.EventLogTags.writeCommitSysConfigFile(fileName, durationMs);
        }
    }

    private void initKernelStatsReaders() {
        if (!isKernelStatsAvailable()) {
            return;
        }
        this.mCpuUidUserSysTimeReader = new com.android.internal.os.KernelCpuUidTimeReader.KernelCpuUidUserSysTimeReader(true, this.mClock);
        this.mCpuUidFreqTimeReader = new com.android.internal.os.KernelCpuUidTimeReader.KernelCpuUidFreqTimeReader(true, this.mClock);
        this.mCpuUidActiveTimeReader = new com.android.internal.os.KernelCpuUidTimeReader.KernelCpuUidActiveTimeReader(true, this.mClock);
        this.mCpuUidClusterTimeReader = new com.android.internal.os.KernelCpuUidTimeReader.KernelCpuUidClusterTimeReader(true, this.mClock);
        this.mKernelWakelockReader = new com.android.server.power.stats.KernelWakelockReader();
        if (!com.android.internal.hidden_from_bootclasspath.com.android.server.power.optimization.Flags.disableSystemServicePowerAttr()) {
            this.mSystemServerCpuThreadReader = com.android.server.power.stats.SystemServerCpuThreadReader.create();
        }
        this.mKernelMemoryBandwidthStats = new com.android.internal.os.KernelMemoryBandwidthStats();
        this.mTmpRailStats = new com.android.internal.os.RailStats();
    }

    /* JADX INFO: Access modifiers changed from: private */
    class PowerStatsCollectorInjector implements com.android.server.power.stats.CpuPowerStatsCollector.Injector, com.android.server.power.stats.MobileRadioPowerStatsCollector.Injector, com.android.server.power.stats.WifiPowerStatsCollector.Injector, com.android.server.power.stats.BluetoothPowerStatsCollector.Injector, com.android.server.power.stats.EnergyConsumerPowerStatsCollector.Injector {
        private com.android.server.power.stats.BluetoothPowerStatsCollector.BluetoothStatsRetriever mBluetoothStatsRetriever;
        private com.android.server.power.stats.PowerStatsCollector.ConsumedEnergyRetriever mConsumedEnergyRetriever;
        private android.app.usage.NetworkStatsManager mNetworkStatsManager;
        private android.content.pm.PackageManager mPackageManager;
        private android.telephony.TelephonyManager mTelephonyManager;
        private android.net.wifi.WifiManager mWifiManager;

        private PowerStatsCollectorInjector() {
        }

        void setContext(android.content.Context context) {
            this.mPackageManager = context.getPackageManager();
            this.mConsumedEnergyRetriever = new com.android.server.power.stats.PowerStatsCollector.ConsumedEnergyRetrieverImpl((android.power.PowerStatsInternal) com.android.server.LocalServices.getService(android.power.PowerStatsInternal.class));
            this.mNetworkStatsManager = (android.app.usage.NetworkStatsManager) context.getSystemService(android.app.usage.NetworkStatsManager.class);
            this.mTelephonyManager = (android.telephony.TelephonyManager) context.getSystemService(android.telephony.TelephonyManager.class);
            this.mWifiManager = (android.net.wifi.WifiManager) context.getSystemService(android.net.wifi.WifiManager.class);
            this.mBluetoothStatsRetriever = com.android.server.power.stats.BatteryStatsImpl.this.new BluetoothStatsRetrieverImpl((android.bluetooth.BluetoothManager) context.getSystemService(android.bluetooth.BluetoothManager.class));
        }

        @Override // com.android.server.power.stats.CpuPowerStatsCollector.Injector, com.android.server.power.stats.MobileRadioPowerStatsCollector.Injector, com.android.server.power.stats.WifiPowerStatsCollector.Injector, com.android.server.power.stats.BluetoothPowerStatsCollector.Injector, com.android.server.power.stats.EnergyConsumerPowerStatsCollector.Injector
        public android.os.Handler getHandler() {
            return com.android.server.power.stats.BatteryStatsImpl.this.mHandler;
        }

        @Override // com.android.server.power.stats.CpuPowerStatsCollector.Injector, com.android.server.power.stats.MobileRadioPowerStatsCollector.Injector, com.android.server.power.stats.WifiPowerStatsCollector.Injector, com.android.server.power.stats.BluetoothPowerStatsCollector.Injector, com.android.server.power.stats.EnergyConsumerPowerStatsCollector.Injector
        public com.android.internal.os.Clock getClock() {
            return com.android.server.power.stats.BatteryStatsImpl.this.mClock;
        }

        @Override // com.android.server.power.stats.CpuPowerStatsCollector.Injector, com.android.server.power.stats.MobileRadioPowerStatsCollector.Injector, com.android.server.power.stats.WifiPowerStatsCollector.Injector, com.android.server.power.stats.BluetoothPowerStatsCollector.Injector, com.android.server.power.stats.EnergyConsumerPowerStatsCollector.Injector
        public long getPowerStatsCollectionThrottlePeriod(java.lang.String powerComponentName) {
            return com.android.server.power.stats.BatteryStatsImpl.this.mBatteryStatsConfig.getPowerStatsThrottlePeriod(powerComponentName);
        }

        @Override // com.android.server.power.stats.CpuPowerStatsCollector.Injector, com.android.server.power.stats.MobileRadioPowerStatsCollector.Injector, com.android.server.power.stats.WifiPowerStatsCollector.Injector, com.android.server.power.stats.BluetoothPowerStatsCollector.Injector, com.android.server.power.stats.EnergyConsumerPowerStatsCollector.Injector
        public com.android.server.power.stats.PowerStatsUidResolver getUidResolver() {
            return com.android.server.power.stats.BatteryStatsImpl.this.mPowerStatsUidResolver;
        }

        @Override // com.android.server.power.stats.CpuPowerStatsCollector.Injector
        public com.android.internal.os.CpuScalingPolicies getCpuScalingPolicies() {
            return com.android.server.power.stats.BatteryStatsImpl.this.mCpuScalingPolicies;
        }

        @Override // com.android.server.power.stats.CpuPowerStatsCollector.Injector
        public com.android.internal.os.PowerProfile getPowerProfile() {
            return com.android.server.power.stats.BatteryStatsImpl.this.mPowerProfile;
        }

        @Override // com.android.server.power.stats.CpuPowerStatsCollector.Injector
        public com.android.server.power.stats.CpuPowerStatsCollector.KernelCpuStatsReader getKernelCpuStatsReader() {
            return new com.android.server.power.stats.CpuPowerStatsCollector.KernelCpuStatsReader();
        }

        @Override // com.android.server.power.stats.MobileRadioPowerStatsCollector.Injector, com.android.server.power.stats.WifiPowerStatsCollector.Injector, com.android.server.power.stats.BluetoothPowerStatsCollector.Injector
        public android.content.pm.PackageManager getPackageManager() {
            return this.mPackageManager;
        }

        @Override // com.android.server.power.stats.CpuPowerStatsCollector.Injector, com.android.server.power.stats.MobileRadioPowerStatsCollector.Injector, com.android.server.power.stats.WifiPowerStatsCollector.Injector, com.android.server.power.stats.BluetoothPowerStatsCollector.Injector, com.android.server.power.stats.EnergyConsumerPowerStatsCollector.Injector
        public com.android.server.power.stats.PowerStatsCollector.ConsumedEnergyRetriever getConsumedEnergyRetriever() {
            return this.mConsumedEnergyRetriever;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ int lambda$getVoltageSupplier$0() {
            return com.android.server.power.stats.BatteryStatsImpl.this.mBatteryVoltageMv;
        }

        @Override // com.android.server.power.stats.CpuPowerStatsCollector.Injector, com.android.server.power.stats.MobileRadioPowerStatsCollector.Injector, com.android.server.power.stats.WifiPowerStatsCollector.Injector, com.android.server.power.stats.BluetoothPowerStatsCollector.Injector, com.android.server.power.stats.EnergyConsumerPowerStatsCollector.Injector
        public java.util.function.IntSupplier getVoltageSupplier() {
            return new java.util.function.IntSupplier() { // from class: com.android.server.power.stats.BatteryStatsImpl$PowerStatsCollectorInjector$$ExternalSyntheticLambda4
                @Override // java.util.function.IntSupplier
                public final int getAsInt() {
                    return this.f$0.lambda$getVoltageSupplier$0();
                }
            };
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ android.net.NetworkStats lambda$getMobileNetworkStatsSupplier$1() {
            return com.android.server.power.stats.BatteryStatsImpl.this.readMobileNetworkStatsLocked(this.mNetworkStatsManager);
        }

        @Override // com.android.server.power.stats.MobileRadioPowerStatsCollector.Injector
        public java.util.function.Supplier<android.net.NetworkStats> getMobileNetworkStatsSupplier() {
            return new java.util.function.Supplier() { // from class: com.android.server.power.stats.BatteryStatsImpl$PowerStatsCollectorInjector$$ExternalSyntheticLambda0
                @Override // java.util.function.Supplier
                public final java.lang.Object get() {
                    return this.f$0.lambda$getMobileNetworkStatsSupplier$1();
                }
            };
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ android.net.NetworkStats lambda$getWifiNetworkStatsSupplier$2() {
            return com.android.server.power.stats.BatteryStatsImpl.this.readWifiNetworkStatsLocked(this.mNetworkStatsManager);
        }

        @Override // com.android.server.power.stats.WifiPowerStatsCollector.Injector
        public java.util.function.Supplier<android.net.NetworkStats> getWifiNetworkStatsSupplier() {
            return new java.util.function.Supplier() { // from class: com.android.server.power.stats.BatteryStatsImpl$PowerStatsCollectorInjector$$ExternalSyntheticLambda1
                @Override // java.util.function.Supplier
                public final java.lang.Object get() {
                    return this.f$0.lambda$getWifiNetworkStatsSupplier$2();
                }
            };
        }

        @Override // com.android.server.power.stats.WifiPowerStatsCollector.Injector
        public com.android.server.power.stats.WifiPowerStatsCollector.WifiStatsRetriever getWifiStatsRetriever() {
            return com.android.server.power.stats.BatteryStatsImpl.this.mWifiStatsRetriever;
        }

        @Override // com.android.server.power.stats.MobileRadioPowerStatsCollector.Injector
        public android.telephony.TelephonyManager getTelephonyManager() {
            return this.mTelephonyManager;
        }

        @Override // com.android.server.power.stats.WifiPowerStatsCollector.Injector
        public android.net.wifi.WifiManager getWifiManager() {
            return this.mWifiManager;
        }

        @Override // com.android.server.power.stats.BluetoothPowerStatsCollector.Injector
        public com.android.server.power.stats.BluetoothPowerStatsCollector.BluetoothStatsRetriever getBluetoothStatsRetriever() {
            return this.mBluetoothStatsRetriever;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ long lambda$getCallDurationSupplier$3() {
            return com.android.server.power.stats.BatteryStatsImpl.this.mPhoneOnTimer.getTotalTimeLocked(com.android.server.power.stats.BatteryStatsImpl.this.mClock.elapsedRealtime() * 1000, 0);
        }

        @Override // com.android.server.power.stats.MobileRadioPowerStatsCollector.Injector
        public java.util.function.LongSupplier getCallDurationSupplier() {
            return new java.util.function.LongSupplier() { // from class: com.android.server.power.stats.BatteryStatsImpl$PowerStatsCollectorInjector$$ExternalSyntheticLambda3
                @Override // java.util.function.LongSupplier
                public final long getAsLong() {
                    return this.f$0.lambda$getCallDurationSupplier$3();
                }
            };
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ long lambda$getPhoneSignalScanDurationSupplier$4() {
            return com.android.server.power.stats.BatteryStatsImpl.this.mPhoneSignalScanningTimer.getTotalTimeLocked(com.android.server.power.stats.BatteryStatsImpl.this.mClock.elapsedRealtime() * 1000, 0);
        }

        @Override // com.android.server.power.stats.MobileRadioPowerStatsCollector.Injector
        public java.util.function.LongSupplier getPhoneSignalScanDurationSupplier() {
            return new java.util.function.LongSupplier() { // from class: com.android.server.power.stats.BatteryStatsImpl$PowerStatsCollectorInjector$$ExternalSyntheticLambda2
                @Override // java.util.function.LongSupplier
                public final long getAsLong() {
                    return this.f$0.lambda$getPhoneSignalScanDurationSupplier$4();
                }
            };
        }
    }

    public interface TimeBaseObs {
        void detach();

        void onTimeStarted(long j, long j2, long j3);

        void onTimeStopped(long j, long j2, long j3);

        boolean reset(boolean z, long j);

        default boolean reset(boolean detachIfReset) {
            return reset(detachIfReset, android.os.SystemClock.elapsedRealtime() * 1000);
        }
    }

    public static class TimeBase {
        protected final java.util.Collection<com.android.server.power.stats.BatteryStatsImpl.TimeBaseObs> mObservers;
        protected long mPastRealtimeUs;
        protected long mPastUptimeUs;
        protected long mRealtimeStartUs;
        protected long mRealtimeUs;
        protected boolean mRunning;
        protected long mUnpluggedRealtimeUs;
        protected long mUnpluggedUptimeUs;
        protected long mUptimeStartUs;
        protected long mUptimeUs;

        public void dump(java.io.PrintWriter pw, java.lang.String prefix) {
            java.lang.StringBuilder sb = new java.lang.StringBuilder(128);
            pw.print(prefix);
            pw.print("mRunning=");
            pw.println(this.mRunning);
            sb.setLength(0);
            sb.append(prefix);
            sb.append("mUptime=");
            android.os.BatteryStats.formatTimeMs(sb, this.mUptimeUs / 1000);
            pw.println(sb.toString());
            sb.setLength(0);
            sb.append(prefix);
            sb.append("mRealtime=");
            android.os.BatteryStats.formatTimeMs(sb, this.mRealtimeUs / 1000);
            pw.println(sb.toString());
            sb.setLength(0);
            sb.append(prefix);
            sb.append("mPastUptime=");
            android.os.BatteryStats.formatTimeMs(sb, this.mPastUptimeUs / 1000);
            sb.append("mUptimeStart=");
            android.os.BatteryStats.formatTimeMs(sb, this.mUptimeStartUs / 1000);
            sb.append("mUnpluggedUptime=");
            android.os.BatteryStats.formatTimeMs(sb, this.mUnpluggedUptimeUs / 1000);
            pw.println(sb.toString());
            sb.setLength(0);
            sb.append(prefix);
            sb.append("mPastRealtime=");
            android.os.BatteryStats.formatTimeMs(sb, this.mPastRealtimeUs / 1000);
            sb.append("mRealtimeStart=");
            android.os.BatteryStats.formatTimeMs(sb, this.mRealtimeStartUs / 1000);
            sb.append("mUnpluggedRealtime=");
            android.os.BatteryStats.formatTimeMs(sb, this.mUnpluggedRealtimeUs / 1000);
            pw.println(sb.toString());
        }

        public TimeBase(boolean isLongList) {
            this.mObservers = isLongList ? new java.util.HashSet<>() : new java.util.ArrayList<>();
        }

        public TimeBase() {
            this(false);
        }

        public void add(com.android.server.power.stats.BatteryStatsImpl.TimeBaseObs observer) {
            this.mObservers.add(observer);
        }

        public void remove(com.android.server.power.stats.BatteryStatsImpl.TimeBaseObs observer) {
            this.mObservers.remove(observer);
        }

        public boolean hasObserver(com.android.server.power.stats.BatteryStatsImpl.TimeBaseObs observer) {
            return this.mObservers.contains(observer);
        }

        public void init(long uptimeUs, long elapsedRealtimeUs) {
            this.mRealtimeUs = 0L;
            this.mUptimeUs = 0L;
            this.mPastUptimeUs = 0L;
            this.mPastRealtimeUs = 0L;
            this.mUptimeStartUs = uptimeUs;
            this.mRealtimeStartUs = elapsedRealtimeUs;
            this.mUnpluggedUptimeUs = getUptime(this.mUptimeStartUs);
            this.mUnpluggedRealtimeUs = getRealtime(this.mRealtimeStartUs);
        }

        public void reset(long uptimeUs, long elapsedRealtimeUs) {
            if (!this.mRunning) {
                this.mPastUptimeUs = 0L;
                this.mPastRealtimeUs = 0L;
            } else {
                this.mUptimeStartUs = uptimeUs;
                this.mRealtimeStartUs = elapsedRealtimeUs;
                this.mUnpluggedUptimeUs = getUptime(uptimeUs);
                this.mUnpluggedRealtimeUs = getRealtime(elapsedRealtimeUs);
            }
        }

        public long computeUptime(long curTimeUs, int which) {
            return this.mUptimeUs + getUptime(curTimeUs);
        }

        public long computeRealtime(long curTimeUs, int which) {
            return this.mRealtimeUs + getRealtime(curTimeUs);
        }

        public long getUptime(long curTimeUs) {
            long time = this.mPastUptimeUs;
            if (this.mRunning) {
                return time + (curTimeUs - this.mUptimeStartUs);
            }
            return time;
        }

        public long getRealtime(long curTimeUs) {
            long time = this.mPastRealtimeUs;
            if (this.mRunning) {
                return time + (curTimeUs - this.mRealtimeStartUs);
            }
            return time;
        }

        public long getUptimeStart() {
            return this.mUptimeStartUs;
        }

        public long getRealtimeStart() {
            return this.mRealtimeStartUs;
        }

        public boolean isRunning() {
            return this.mRunning;
        }

        public boolean setRunning(boolean running, long uptimeUs, long elapsedRealtimeUs) {
            if (this.mRunning != running) {
                this.mRunning = running;
                if (!running) {
                    this.mPastUptimeUs += uptimeUs - this.mUptimeStartUs;
                    this.mPastRealtimeUs += elapsedRealtimeUs - this.mRealtimeStartUs;
                    long batteryUptimeUs = getUptime(uptimeUs);
                    long batteryRealtimeUs = getRealtime(elapsedRealtimeUs);
                    java.util.Iterator<com.android.server.power.stats.BatteryStatsImpl.TimeBaseObs> iter = this.mObservers.iterator();
                    while (iter.hasNext()) {
                        iter.next().onTimeStopped(elapsedRealtimeUs, batteryUptimeUs, batteryRealtimeUs);
                    }
                    return true;
                }
                this.mUptimeStartUs = uptimeUs;
                this.mRealtimeStartUs = elapsedRealtimeUs;
                long batteryUptimeUs2 = getUptime(uptimeUs);
                this.mUnpluggedUptimeUs = batteryUptimeUs2;
                long batteryRealtimeUs2 = getRealtime(elapsedRealtimeUs);
                this.mUnpluggedRealtimeUs = batteryRealtimeUs2;
                java.util.Iterator<com.android.server.power.stats.BatteryStatsImpl.TimeBaseObs> iter2 = this.mObservers.iterator();
                while (iter2.hasNext()) {
                    iter2.next().onTimeStarted(elapsedRealtimeUs, batteryUptimeUs2, batteryRealtimeUs2);
                }
                return true;
            }
            return false;
        }

        public void readSummaryFromParcel(android.os.Parcel in) {
            this.mUptimeUs = in.readLong();
            this.mRealtimeUs = in.readLong();
        }

        public void writeSummaryToParcel(android.os.Parcel out, long uptimeUs, long elapsedRealtimeUs) {
            out.writeLong(computeUptime(uptimeUs, 0));
            out.writeLong(computeRealtime(elapsedRealtimeUs, 0));
        }

        public void readFromParcel(android.os.Parcel in) {
            this.mRunning = false;
            this.mUptimeUs = in.readLong();
            this.mPastUptimeUs = in.readLong();
            this.mUptimeStartUs = in.readLong();
            this.mRealtimeUs = in.readLong();
            this.mPastRealtimeUs = in.readLong();
            this.mRealtimeStartUs = in.readLong();
            this.mUnpluggedUptimeUs = in.readLong();
            this.mUnpluggedRealtimeUs = in.readLong();
        }

        public void writeToParcel(android.os.Parcel out, long uptimeUs, long elapsedRealtimeUs) {
            long runningUptime = getUptime(uptimeUs);
            long runningRealtime = getRealtime(elapsedRealtimeUs);
            out.writeLong(this.mUptimeUs);
            out.writeLong(runningUptime);
            out.writeLong(this.mUptimeStartUs);
            out.writeLong(this.mRealtimeUs);
            out.writeLong(runningRealtime);
            out.writeLong(this.mRealtimeStartUs);
            out.writeLong(this.mUnpluggedUptimeUs);
            out.writeLong(this.mUnpluggedRealtimeUs);
        }
    }

    public static class Counter extends android.os.BatteryStats.Counter implements com.android.server.power.stats.BatteryStatsImpl.TimeBaseObs {
        final java.util.concurrent.atomic.AtomicInteger mCount = new java.util.concurrent.atomic.AtomicInteger();
        final com.android.server.power.stats.BatteryStatsImpl.TimeBase mTimeBase;

        public Counter(com.android.server.power.stats.BatteryStatsImpl.TimeBase timeBase, android.os.Parcel in) {
            this.mTimeBase = timeBase;
            this.mCount.set(in.readInt());
            timeBase.add(this);
        }

        public Counter(com.android.server.power.stats.BatteryStatsImpl.TimeBase timeBase) {
            this.mTimeBase = timeBase;
            timeBase.add(this);
        }

        public void writeToParcel(android.os.Parcel out) {
            out.writeInt(this.mCount.get());
        }

        @Override // com.android.server.power.stats.BatteryStatsImpl.TimeBaseObs
        public void onTimeStarted(long elapsedRealtimeUs, long baseUptimeUs, long baseRealtimeUs) {
        }

        @Override // com.android.server.power.stats.BatteryStatsImpl.TimeBaseObs
        public void onTimeStopped(long elapsedRealtimeUs, long baseUptimeUs, long baseRealtimeUs) {
        }

        public int getCountLocked(int which) {
            return this.mCount.get();
        }

        public void logState(android.util.Printer pw, java.lang.String prefix) {
            pw.println(prefix + "mCount=" + this.mCount.get());
        }

        public void stepAtomic() {
            if (this.mTimeBase.isRunning()) {
                this.mCount.incrementAndGet();
            }
        }

        void addAtomic(int delta) {
            if (this.mTimeBase.isRunning()) {
                this.mCount.addAndGet(delta);
            }
        }

        @Override // com.android.server.power.stats.BatteryStatsImpl.TimeBaseObs
        public boolean reset(boolean detachIfReset, long elapsedRealtimeUs) {
            this.mCount.set(0);
            if (detachIfReset) {
                detach();
                return true;
            }
            return true;
        }

        @Override // com.android.server.power.stats.BatteryStatsImpl.TimeBaseObs
        public void detach() {
            this.mTimeBase.remove(this);
        }

        public void writeSummaryFromParcelLocked(android.os.Parcel out) {
            out.writeInt(this.mCount.get());
        }

        public void readSummaryFromParcelLocked(android.os.Parcel in) {
            this.mCount.set(in.readInt());
        }
    }

    public static class LongSamplingCounterArray extends android.os.BatteryStats.LongCounterArray implements com.android.server.power.stats.BatteryStatsImpl.TimeBaseObs {
        public long[] mCounts;
        final com.android.server.power.stats.BatteryStatsImpl.TimeBase mTimeBase;

        private LongSamplingCounterArray(com.android.server.power.stats.BatteryStatsImpl.TimeBase timeBase, android.os.Parcel in) {
            this.mTimeBase = timeBase;
            this.mCounts = in.createLongArray();
            timeBase.add(this);
        }

        public LongSamplingCounterArray(com.android.server.power.stats.BatteryStatsImpl.TimeBase timeBase) {
            this.mTimeBase = timeBase;
            timeBase.add(this);
        }

        private void writeToParcel(android.os.Parcel out) {
            out.writeLongArray(this.mCounts);
        }

        @Override // com.android.server.power.stats.BatteryStatsImpl.TimeBaseObs
        public void onTimeStarted(long elapsedRealTimeUs, long baseUptimeUs, long baseRealtimeUs) {
        }

        @Override // com.android.server.power.stats.BatteryStatsImpl.TimeBaseObs
        public void onTimeStopped(long elapsedRealtimeUs, long baseUptimeUs, long baseRealtimeUs) {
        }

        public long[] getCountsLocked(int which) {
            if (this.mCounts == null) {
                return null;
            }
            return java.util.Arrays.copyOf(this.mCounts, this.mCounts.length);
        }

        public void logState(android.util.Printer pw, java.lang.String prefix) {
            pw.println(prefix + "mCounts=" + java.util.Arrays.toString(this.mCounts));
        }

        public void addCountLocked(long[] counts) {
            addCountLocked(counts, this.mTimeBase.isRunning());
        }

        public void addCountLocked(long[] counts, boolean isRunning) {
            if (counts != null && isRunning) {
                if (this.mCounts == null) {
                    this.mCounts = new long[counts.length];
                }
                for (int i = 0; i < counts.length; i++) {
                    long[] jArr = this.mCounts;
                    jArr[i] = jArr[i] + counts[i];
                }
            }
        }

        public int getSize() {
            if (this.mCounts == null) {
                return 0;
            }
            return this.mCounts.length;
        }

        @Override // com.android.server.power.stats.BatteryStatsImpl.TimeBaseObs
        public boolean reset(boolean detachIfReset, long elapsedRealtimeUs) {
            if (this.mCounts != null) {
                java.util.Arrays.fill(this.mCounts, 0L);
            }
            if (detachIfReset) {
                detach();
                return true;
            }
            return true;
        }

        @Override // com.android.server.power.stats.BatteryStatsImpl.TimeBaseObs
        public void detach() {
            this.mTimeBase.remove(this);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void writeSummaryToParcelLocked(android.os.Parcel out) {
            out.writeLongArray(this.mCounts);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void readSummaryFromParcelLocked(android.os.Parcel in) {
            this.mCounts = in.createLongArray();
        }

        public static void writeToParcel(android.os.Parcel out, com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounterArray counterArray) {
            if (counterArray != null) {
                out.writeInt(1);
                counterArray.writeToParcel(out);
            } else {
                out.writeInt(0);
            }
        }

        public static com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounterArray readFromParcel(android.os.Parcel in, com.android.server.power.stats.BatteryStatsImpl.TimeBase timeBase) {
            if (in.readInt() != 0) {
                return new com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounterArray(timeBase, in);
            }
            return null;
        }

        public static void writeSummaryToParcelLocked(android.os.Parcel out, com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounterArray counterArray) {
            if (counterArray != null) {
                out.writeInt(1);
                counterArray.writeSummaryToParcelLocked(out);
            } else {
                out.writeInt(0);
            }
        }

        public static com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounterArray readSummaryFromParcelLocked(android.os.Parcel in, com.android.server.power.stats.BatteryStatsImpl.TimeBase timeBase) {
            if (in.readInt() != 0) {
                com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounterArray counterArray = new com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounterArray(timeBase);
                counterArray.readSummaryFromParcelLocked(in);
                return counterArray;
            }
            return null;
        }
    }

    private static class TimeMultiStateCounter extends android.os.BatteryStats.LongCounter implements com.android.server.power.stats.BatteryStatsImpl.TimeBaseObs {
        private final com.android.internal.os.LongMultiStateCounter mCounter;
        private final com.android.server.power.stats.BatteryStatsImpl.TimeBase mTimeBase;

        private TimeMultiStateCounter(com.android.server.power.stats.BatteryStatsImpl.TimeBase timeBase, int stateCount, long timestampMs) {
            this(timeBase, new com.android.internal.os.LongMultiStateCounter(stateCount), timestampMs);
        }

        private TimeMultiStateCounter(com.android.server.power.stats.BatteryStatsImpl.TimeBase timeBase, com.android.internal.os.LongMultiStateCounter counter, long timestampMs) {
            this.mTimeBase = timeBase;
            this.mCounter = counter;
            this.mCounter.setEnabled(this.mTimeBase.isRunning(), timestampMs);
            timeBase.add(this);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static com.android.server.power.stats.BatteryStatsImpl.TimeMultiStateCounter readFromParcel(android.os.Parcel in, com.android.server.power.stats.BatteryStatsImpl.TimeBase timeBase, int stateCount, long timestampMs) {
            com.android.internal.os.LongMultiStateCounter counter = (com.android.internal.os.LongMultiStateCounter) com.android.internal.os.LongMultiStateCounter.CREATOR.createFromParcel(in);
            if (counter.getStateCount() != stateCount) {
                return null;
            }
            return new com.android.server.power.stats.BatteryStatsImpl.TimeMultiStateCounter(timeBase, counter, timestampMs);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void writeToParcel(android.os.Parcel out) {
            this.mCounter.writeToParcel(out, 0);
        }

        @Override // com.android.server.power.stats.BatteryStatsImpl.TimeBaseObs
        public void onTimeStarted(long elapsedRealtimeUs, long baseUptimeUs, long baseRealtimeUs) {
            this.mCounter.setEnabled(true, elapsedRealtimeUs / 1000);
        }

        @Override // com.android.server.power.stats.BatteryStatsImpl.TimeBaseObs
        public void onTimeStopped(long elapsedRealtimeUs, long baseUptimeUs, long baseRealtimeUs) {
            this.mCounter.setEnabled(false, elapsedRealtimeUs / 1000);
        }

        public int getStateCount() {
            return this.mCounter.getStateCount();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setState(int processState, long elapsedRealtimeMs) {
            this.mCounter.setState(processState, elapsedRealtimeMs);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public long update(long value, long timestampMs) {
            return this.mCounter.updateValue(value, timestampMs);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void increment(long increment, long timestampMs) {
            this.mCounter.incrementValue(increment, timestampMs);
        }

        public long getCountForProcessState(int procState) {
            return this.mCounter.getCount(procState);
        }

        public long getTotalCountLocked() {
            return this.mCounter.getTotalCount();
        }

        public long getCountLocked(int statsType) {
            return getTotalCountLocked();
        }

        public void logState(android.util.Printer pw, java.lang.String prefix) {
            pw.println(prefix + "mCounter=" + this.mCounter);
        }

        @Override // com.android.server.power.stats.BatteryStatsImpl.TimeBaseObs
        public boolean reset(boolean detachIfReset, long elapsedRealtimeUs) {
            this.mCounter.reset();
            if (detachIfReset) {
                detach();
                return true;
            }
            return true;
        }

        @Override // com.android.server.power.stats.BatteryStatsImpl.TimeBaseObs
        public void detach() {
            this.mTimeBase.remove(this);
        }
    }

    private static class TimeInFreqMultiStateCounter implements com.android.server.power.stats.BatteryStatsImpl.TimeBaseObs {
        private final com.android.internal.os.LongArrayMultiStateCounter mCounter;
        private final com.android.server.power.stats.BatteryStatsImpl.TimeBase mTimeBase;

        private TimeInFreqMultiStateCounter(com.android.server.power.stats.BatteryStatsImpl.TimeBase timeBase, int stateCount, int cpuFreqCount, long timestampMs) {
            this(timeBase, new com.android.internal.os.LongArrayMultiStateCounter(stateCount, cpuFreqCount), timestampMs);
        }

        private TimeInFreqMultiStateCounter(com.android.server.power.stats.BatteryStatsImpl.TimeBase timeBase, com.android.internal.os.LongArrayMultiStateCounter counter, long timestampMs) {
            this.mTimeBase = timeBase;
            this.mCounter = counter;
            this.mCounter.setEnabled(this.mTimeBase.isRunning(), timestampMs);
            timeBase.add(this);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void writeToParcel(android.os.Parcel out) {
            this.mCounter.writeToParcel(out, 0);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static com.android.server.power.stats.BatteryStatsImpl.TimeInFreqMultiStateCounter readFromParcel(android.os.Parcel in, com.android.server.power.stats.BatteryStatsImpl.TimeBase timeBase, int stateCount, int cpuFreqCount, long timestampMs) {
            com.android.internal.os.LongArrayMultiStateCounter counter = (com.android.internal.os.LongArrayMultiStateCounter) com.android.internal.os.LongArrayMultiStateCounter.CREATOR.createFromParcel(in);
            if (counter.getStateCount() != stateCount || counter.getArrayLength() != cpuFreqCount) {
                return null;
            }
            return new com.android.server.power.stats.BatteryStatsImpl.TimeInFreqMultiStateCounter(timeBase, counter, timestampMs);
        }

        @Override // com.android.server.power.stats.BatteryStatsImpl.TimeBaseObs
        public void onTimeStarted(long elapsedRealtimeUs, long baseUptimeUs, long baseRealtimeUs) {
            this.mCounter.setEnabled(true, elapsedRealtimeUs / 1000);
        }

        @Override // com.android.server.power.stats.BatteryStatsImpl.TimeBaseObs
        public void onTimeStopped(long elapsedRealtimeUs, long baseUptimeUs, long baseRealtimeUs) {
            this.mCounter.setEnabled(false, elapsedRealtimeUs / 1000);
        }

        public com.android.internal.os.LongArrayMultiStateCounter getCounter() {
            return this.mCounter;
        }

        public int getStateCount() {
            return this.mCounter.getStateCount();
        }

        public void setTrackingEnabled(boolean enabled, long timestampMs) {
            this.mCounter.setEnabled(enabled && this.mTimeBase.isRunning(), timestampMs);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setState(int uidRunningState, long elapsedRealtimeMs) {
            this.mCounter.setState(uidRunningState, elapsedRealtimeMs);
        }

        public boolean getCountsLocked(long[] counts, int procState) {
            if (counts.length != this.mCounter.getArrayLength()) {
                return false;
            }
            this.mCounter.getCounts(counts, procState);
            for (int i = counts.length - 1; i >= 0; i--) {
                if (counts[i] != 0) {
                    return true;
                }
            }
            return false;
        }

        public void logState(android.util.Printer pw, java.lang.String prefix) {
            pw.println(prefix + "mCounter=" + this.mCounter);
        }

        @Override // com.android.server.power.stats.BatteryStatsImpl.TimeBaseObs
        public boolean reset(boolean detachIfReset, long elapsedRealtimeUs) {
            this.mCounter.reset();
            if (detachIfReset) {
                detach();
                return true;
            }
            return true;
        }

        @Override // com.android.server.power.stats.BatteryStatsImpl.TimeBaseObs
        public void detach() {
            this.mTimeBase.remove(this);
        }
    }

    public static class LongSamplingCounter extends android.os.BatteryStats.LongCounter implements com.android.server.power.stats.BatteryStatsImpl.TimeBaseObs {
        private long mCount;
        final com.android.server.power.stats.BatteryStatsImpl.TimeBase mTimeBase;

        public LongSamplingCounter(com.android.server.power.stats.BatteryStatsImpl.TimeBase timeBase, android.os.Parcel in) {
            this.mTimeBase = timeBase;
            this.mCount = in.readLong();
            timeBase.add(this);
        }

        public LongSamplingCounter(com.android.server.power.stats.BatteryStatsImpl.TimeBase timeBase) {
            this.mTimeBase = timeBase;
            timeBase.add(this);
        }

        public void writeToParcel(android.os.Parcel out) {
            out.writeLong(this.mCount);
        }

        @Override // com.android.server.power.stats.BatteryStatsImpl.TimeBaseObs
        public void onTimeStarted(long elapsedRealtimeUs, long baseUptimeUs, long baseRealtimeUs) {
        }

        @Override // com.android.server.power.stats.BatteryStatsImpl.TimeBaseObs
        public void onTimeStopped(long elapsedRealtimeUs, long baseUptimeUs, long baseRealtimeUs) {
        }

        public long getCountLocked(int which) {
            return this.mCount;
        }

        public long getCountForProcessState(int procState) {
            if (procState == 0) {
                return getCountLocked(0);
            }
            return 0L;
        }

        public void logState(android.util.Printer pw, java.lang.String prefix) {
            pw.println(prefix + "mCount=" + this.mCount);
        }

        public void addCountLocked(long count) {
            addCountLocked(count, this.mTimeBase.isRunning());
        }

        public void addCountLocked(long count, boolean isRunning) {
            if (isRunning) {
                this.mCount += count;
            }
        }

        @Override // com.android.server.power.stats.BatteryStatsImpl.TimeBaseObs
        public boolean reset(boolean detachIfReset, long elapsedRealtimeUs) {
            this.mCount = 0L;
            if (detachIfReset) {
                detach();
                return true;
            }
            return true;
        }

        @Override // com.android.server.power.stats.BatteryStatsImpl.TimeBaseObs
        public void detach() {
            this.mTimeBase.remove(this);
        }

        public void writeSummaryFromParcelLocked(android.os.Parcel out) {
            out.writeLong(this.mCount);
        }

        public void readSummaryFromParcelLocked(android.os.Parcel in) {
            this.mCount = in.readLong();
        }
    }

    public static abstract class Timer extends android.os.BatteryStats.Timer implements com.android.server.power.stats.BatteryStatsImpl.TimeBaseObs {
        protected final com.android.internal.os.Clock mClock;
        protected int mCount;
        protected final com.android.server.power.stats.BatteryStatsImpl.TimeBase mTimeBase;
        protected long mTimeBeforeMarkUs;
        protected long mTotalTimeUs;
        protected final int mType;

        protected abstract int computeCurrentCountLocked();

        protected abstract long computeRunTimeLocked(long j, long j2);

        public Timer(com.android.internal.os.Clock clock, int type, com.android.server.power.stats.BatteryStatsImpl.TimeBase timeBase, android.os.Parcel in) {
            this.mClock = clock;
            this.mType = type;
            this.mTimeBase = timeBase;
            this.mCount = in.readInt();
            this.mTotalTimeUs = in.readLong();
            this.mTimeBeforeMarkUs = in.readLong();
            timeBase.add(this);
        }

        public Timer(com.android.internal.os.Clock clock, int type, com.android.server.power.stats.BatteryStatsImpl.TimeBase timeBase) {
            this.mClock = clock;
            this.mType = type;
            this.mTimeBase = timeBase;
            timeBase.add(this);
        }

        public void writeToParcel(android.os.Parcel out, long elapsedRealtimeUs) {
            out.writeInt(computeCurrentCountLocked());
            out.writeLong(computeRunTimeLocked(this.mTimeBase.getRealtime(elapsedRealtimeUs), elapsedRealtimeUs));
            out.writeLong(this.mTimeBeforeMarkUs);
        }

        @Override // com.android.server.power.stats.BatteryStatsImpl.TimeBaseObs
        public boolean reset(boolean detachIfReset) {
            return reset(detachIfReset, this.mClock.elapsedRealtime() * 1000);
        }

        public boolean reset(boolean detachIfReset, long elapsedRealtimeUs) {
            this.mTimeBeforeMarkUs = 0L;
            this.mTotalTimeUs = 0L;
            this.mCount = 0;
            if (detachIfReset) {
                detach();
                return true;
            }
            return true;
        }

        public void detach() {
            this.mTimeBase.remove(this);
        }

        public void onTimeStarted(long elapsedRealtimeUs, long timeBaseUptimeUs, long baseRealtimeUs) {
        }

        public void onTimeStopped(long elapsedRealtimeUs, long baseUptimeUs, long baseRealtimeUs) {
            this.mTotalTimeUs = computeRunTimeLocked(baseRealtimeUs, elapsedRealtimeUs);
            this.mCount = computeCurrentCountLocked();
        }

        public static void writeTimerToParcel(android.os.Parcel out, com.android.server.power.stats.BatteryStatsImpl.Timer timer, long elapsedRealtimeUs) {
            if (timer == null) {
                out.writeInt(0);
            } else {
                out.writeInt(1);
                timer.writeToParcel(out, elapsedRealtimeUs);
            }
        }

        public long getTotalTimeLocked(long elapsedRealtimeUs, int which) {
            return computeRunTimeLocked(this.mTimeBase.getRealtime(elapsedRealtimeUs), elapsedRealtimeUs);
        }

        public int getCountLocked(int which) {
            return computeCurrentCountLocked();
        }

        public long getTimeSinceMarkLocked(long elapsedRealtimeUs) {
            long val = computeRunTimeLocked(this.mTimeBase.getRealtime(elapsedRealtimeUs), elapsedRealtimeUs);
            return val - this.mTimeBeforeMarkUs;
        }

        public void logState(android.util.Printer pw, java.lang.String prefix) {
            pw.println(prefix + "mCount=" + this.mCount);
            pw.println(prefix + "mTotalTime=" + this.mTotalTimeUs);
        }

        public void writeSummaryFromParcelLocked(android.os.Parcel out, long elapsedRealtimeUs) {
            long runTimeUs = computeRunTimeLocked(this.mTimeBase.getRealtime(elapsedRealtimeUs), elapsedRealtimeUs);
            out.writeLong(runTimeUs);
            out.writeInt(computeCurrentCountLocked());
        }

        public void readSummaryFromParcelLocked(android.os.Parcel in) {
            this.mTotalTimeUs = in.readLong();
            this.mCount = in.readInt();
            this.mTimeBeforeMarkUs = this.mTotalTimeUs;
        }
    }

    public static class SamplingTimer extends com.android.server.power.stats.BatteryStatsImpl.Timer {
        int mBaseReportedCount;
        long mBaseReportedTotalTimeUs;
        int mCurrentReportedCount;
        long mCurrentReportedTotalTimeUs;
        boolean mTimeBaseRunning;
        boolean mTrackingReportedValues;
        int mUpdateVersion;

        public SamplingTimer(com.android.internal.os.Clock clock, com.android.server.power.stats.BatteryStatsImpl.TimeBase timeBase, android.os.Parcel in) {
            super(clock, 0, timeBase, in);
            this.mCurrentReportedCount = in.readInt();
            this.mBaseReportedCount = in.readInt();
            this.mCurrentReportedTotalTimeUs = in.readLong();
            this.mBaseReportedTotalTimeUs = in.readLong();
            this.mTrackingReportedValues = in.readInt() == 1;
            this.mTimeBaseRunning = timeBase.isRunning();
        }

        public SamplingTimer(com.android.internal.os.Clock clock, com.android.server.power.stats.BatteryStatsImpl.TimeBase timeBase) {
            super(clock, 0, timeBase);
            this.mTrackingReportedValues = false;
            this.mTimeBaseRunning = timeBase.isRunning();
        }

        public void endSample() {
            endSample(this.mClock.elapsedRealtime() * 1000);
        }

        public void endSample(long elapsedRealtimeUs) {
            this.mTotalTimeUs = computeRunTimeLocked(0L, elapsedRealtimeUs);
            this.mCount = computeCurrentCountLocked();
            this.mCurrentReportedTotalTimeUs = 0L;
            this.mBaseReportedTotalTimeUs = 0L;
            this.mCurrentReportedCount = 0;
            this.mBaseReportedCount = 0;
            this.mTrackingReportedValues = false;
        }

        public void setUpdateVersion(int version) {
            this.mUpdateVersion = version;
        }

        public int getUpdateVersion() {
            return this.mUpdateVersion;
        }

        public void update(long totalTimeUs, int count, long elapsedRealtimeUs) {
            update(totalTimeUs, 0L, count, elapsedRealtimeUs);
        }

        public void update(long totalTimeUs, long activeTimeUs, int count, long elapsedRealtimeUs) {
            if (this.mTimeBaseRunning && !this.mTrackingReportedValues) {
                this.mBaseReportedTotalTimeUs = totalTimeUs - activeTimeUs;
                this.mBaseReportedCount = activeTimeUs == 0 ? count : count - 1;
            }
            this.mTrackingReportedValues = true;
            if (totalTimeUs < this.mCurrentReportedTotalTimeUs || count < this.mCurrentReportedCount) {
                endSample(elapsedRealtimeUs);
            }
            this.mCurrentReportedTotalTimeUs = totalTimeUs;
            this.mCurrentReportedCount = count;
        }

        public void add(long deltaTimeUs, int deltaCount) {
            add(deltaTimeUs, deltaCount, this.mClock.elapsedRealtime() * 1000);
        }

        public void add(long deltaTimeUs, int deltaCount, long elapsedRealtimeUs) {
            update(this.mCurrentReportedTotalTimeUs + deltaTimeUs, this.mCurrentReportedCount + deltaCount, elapsedRealtimeUs);
        }

        @Override // com.android.server.power.stats.BatteryStatsImpl.Timer, com.android.server.power.stats.BatteryStatsImpl.TimeBaseObs
        public void onTimeStarted(long elapsedRealtimeUs, long baseUptimeUs, long baseRealtimeUs) {
            super.onTimeStarted(elapsedRealtimeUs, baseUptimeUs, baseRealtimeUs);
            if (this.mTrackingReportedValues) {
                this.mBaseReportedTotalTimeUs = this.mCurrentReportedTotalTimeUs;
                this.mBaseReportedCount = this.mCurrentReportedCount;
            }
            this.mTimeBaseRunning = true;
        }

        @Override // com.android.server.power.stats.BatteryStatsImpl.Timer, com.android.server.power.stats.BatteryStatsImpl.TimeBaseObs
        public void onTimeStopped(long elapsedRealtimeUs, long baseUptimeUs, long baseRealtimeUs) {
            super.onTimeStopped(elapsedRealtimeUs, baseUptimeUs, baseRealtimeUs);
            this.mTimeBaseRunning = false;
        }

        @Override // com.android.server.power.stats.BatteryStatsImpl.Timer
        public void logState(android.util.Printer pw, java.lang.String prefix) {
            super.logState(pw, prefix);
            pw.println(prefix + "mCurrentReportedCount=" + this.mCurrentReportedCount + " mBaseReportedCount=" + this.mBaseReportedCount + " mCurrentReportedTotalTime=" + this.mCurrentReportedTotalTimeUs + " mBaseReportedTotalTimeUs=" + this.mBaseReportedTotalTimeUs);
        }

        @Override // com.android.server.power.stats.BatteryStatsImpl.Timer
        protected long computeRunTimeLocked(long curBatteryRealtime, long elapsedRealtimeUs) {
            return this.mTotalTimeUs + ((this.mTimeBaseRunning && this.mTrackingReportedValues) ? this.mCurrentReportedTotalTimeUs - this.mBaseReportedTotalTimeUs : 0L);
        }

        @Override // com.android.server.power.stats.BatteryStatsImpl.Timer
        protected int computeCurrentCountLocked() {
            return this.mCount + ((this.mTimeBaseRunning && this.mTrackingReportedValues) ? this.mCurrentReportedCount - this.mBaseReportedCount : 0);
        }

        @Override // com.android.server.power.stats.BatteryStatsImpl.Timer
        public void writeToParcel(android.os.Parcel parcel, long j) {
            super.writeToParcel(parcel, j);
            parcel.writeInt(this.mCurrentReportedCount);
            parcel.writeInt(this.mBaseReportedCount);
            parcel.writeLong(this.mCurrentReportedTotalTimeUs);
            parcel.writeLong(this.mBaseReportedTotalTimeUs);
            parcel.writeInt(this.mTrackingReportedValues ? 1 : 0);
        }

        @Override // com.android.server.power.stats.BatteryStatsImpl.Timer, com.android.server.power.stats.BatteryStatsImpl.TimeBaseObs
        public boolean reset(boolean detachIfReset, long elapsedRealtimeUs) {
            super.reset(detachIfReset, elapsedRealtimeUs);
            this.mTrackingReportedValues = false;
            this.mBaseReportedTotalTimeUs = 0L;
            this.mBaseReportedCount = 0;
            return true;
        }
    }

    public static class BatchTimer extends com.android.server.power.stats.BatteryStatsImpl.Timer {
        boolean mInDischarge;
        long mLastAddedDurationUs;
        long mLastAddedTimeUs;
        final com.android.server.power.stats.BatteryStatsImpl.Uid mUid;

        BatchTimer(com.android.internal.os.Clock clock, com.android.server.power.stats.BatteryStatsImpl.Uid uid, int type, com.android.server.power.stats.BatteryStatsImpl.TimeBase timeBase, android.os.Parcel in) {
            super(clock, type, timeBase, in);
            this.mUid = uid;
            this.mLastAddedTimeUs = in.readLong();
            this.mLastAddedDurationUs = in.readLong();
            this.mInDischarge = timeBase.isRunning();
        }

        BatchTimer(com.android.internal.os.Clock clock, com.android.server.power.stats.BatteryStatsImpl.Uid uid, int type, com.android.server.power.stats.BatteryStatsImpl.TimeBase timeBase) {
            super(clock, type, timeBase);
            this.mUid = uid;
            this.mInDischarge = timeBase.isRunning();
        }

        @Override // com.android.server.power.stats.BatteryStatsImpl.Timer
        public void writeToParcel(android.os.Parcel out, long elapsedRealtimeUs) {
            super.writeToParcel(out, elapsedRealtimeUs);
            out.writeLong(this.mLastAddedTimeUs);
            out.writeLong(this.mLastAddedDurationUs);
        }

        @Override // com.android.server.power.stats.BatteryStatsImpl.Timer, com.android.server.power.stats.BatteryStatsImpl.TimeBaseObs
        public void onTimeStopped(long elapsedRealtimeUs, long baseUptimeUs, long baseRealtimeUs) {
            recomputeLastDuration(elapsedRealtimeUs, false);
            this.mInDischarge = false;
            super.onTimeStopped(elapsedRealtimeUs, baseUptimeUs, baseRealtimeUs);
        }

        @Override // com.android.server.power.stats.BatteryStatsImpl.Timer, com.android.server.power.stats.BatteryStatsImpl.TimeBaseObs
        public void onTimeStarted(long elapsedRealtimeUs, long baseUptimeUs, long baseRealtimeUs) {
            recomputeLastDuration(elapsedRealtimeUs, false);
            this.mInDischarge = true;
            if (this.mLastAddedTimeUs == elapsedRealtimeUs) {
                this.mTotalTimeUs += this.mLastAddedDurationUs;
            }
            super.onTimeStarted(elapsedRealtimeUs, baseUptimeUs, baseRealtimeUs);
        }

        @Override // com.android.server.power.stats.BatteryStatsImpl.Timer
        public void logState(android.util.Printer pw, java.lang.String prefix) {
            super.logState(pw, prefix);
            pw.println(prefix + "mLastAddedTime=" + this.mLastAddedTimeUs + " mLastAddedDuration=" + this.mLastAddedDurationUs);
        }

        private long computeOverage(long curTimeUs) {
            if (this.mLastAddedTimeUs > 0) {
                return this.mLastAddedDurationUs - curTimeUs;
            }
            return 0L;
        }

        private void recomputeLastDuration(long curTimeUs, boolean abort) {
            long overage = computeOverage(curTimeUs);
            if (overage > 0) {
                if (this.mInDischarge) {
                    this.mTotalTimeUs -= overage;
                }
                if (abort) {
                    this.mLastAddedTimeUs = 0L;
                } else {
                    this.mLastAddedTimeUs = curTimeUs;
                    this.mLastAddedDurationUs -= overage;
                }
            }
        }

        public void addDuration(long durationMs, long elapsedRealtimeMs) {
            long nowUs = elapsedRealtimeMs * 1000;
            recomputeLastDuration(nowUs, true);
            this.mLastAddedTimeUs = nowUs;
            this.mLastAddedDurationUs = 1000 * durationMs;
            if (this.mInDischarge) {
                this.mTotalTimeUs += this.mLastAddedDurationUs;
                this.mCount++;
            }
        }

        public void abortLastDuration(long elapsedRealtimeMs) {
            long nowUs = 1000 * elapsedRealtimeMs;
            recomputeLastDuration(nowUs, true);
        }

        @Override // com.android.server.power.stats.BatteryStatsImpl.Timer
        protected int computeCurrentCountLocked() {
            return this.mCount;
        }

        @Override // com.android.server.power.stats.BatteryStatsImpl.Timer
        protected long computeRunTimeLocked(long curBatteryRealtimeUs, long elapsedRealtimeUs) {
            long overage = computeOverage(elapsedRealtimeUs);
            if (overage > 0) {
                this.mTotalTimeUs = overage;
                return overage;
            }
            return this.mTotalTimeUs;
        }

        @Override // com.android.server.power.stats.BatteryStatsImpl.Timer, com.android.server.power.stats.BatteryStatsImpl.TimeBaseObs
        public boolean reset(boolean detachIfReset, long elapsedRealtimeUs) {
            recomputeLastDuration(elapsedRealtimeUs, true);
            boolean stillActive = this.mLastAddedTimeUs == elapsedRealtimeUs;
            super.reset(!stillActive && detachIfReset, elapsedRealtimeUs);
            return !stillActive;
        }
    }

    public static class DurationTimer extends com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer {
        long mCurrentDurationMs;
        long mMaxDurationMs;
        long mStartTimeMs;
        long mTotalDurationMs;

        public DurationTimer(com.android.internal.os.Clock clock, com.android.server.power.stats.BatteryStatsImpl.Uid uid, int type, java.util.ArrayList<com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer> timerPool, com.android.server.power.stats.BatteryStatsImpl.TimeBase timeBase, android.os.Parcel in) {
            super(clock, uid, type, timerPool, timeBase, in);
            this.mStartTimeMs = -1L;
            this.mMaxDurationMs = in.readLong();
            this.mTotalDurationMs = in.readLong();
            this.mCurrentDurationMs = in.readLong();
        }

        public DurationTimer(com.android.internal.os.Clock clock, com.android.server.power.stats.BatteryStatsImpl.Uid uid, int type, java.util.ArrayList<com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer> timerPool, com.android.server.power.stats.BatteryStatsImpl.TimeBase timeBase) {
            super(clock, uid, type, timerPool, timeBase);
            this.mStartTimeMs = -1L;
        }

        @Override // com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer, com.android.server.power.stats.BatteryStatsImpl.Timer
        public void writeToParcel(android.os.Parcel out, long elapsedRealtimeUs) {
            super.writeToParcel(out, elapsedRealtimeUs);
            out.writeLong(getMaxDurationMsLocked(elapsedRealtimeUs / 1000));
            out.writeLong(this.mTotalDurationMs);
            out.writeLong(getCurrentDurationMsLocked(elapsedRealtimeUs / 1000));
        }

        @Override // com.android.server.power.stats.BatteryStatsImpl.Timer
        public void writeSummaryFromParcelLocked(android.os.Parcel out, long elapsedRealtimeUs) {
            super.writeSummaryFromParcelLocked(out, elapsedRealtimeUs);
            out.writeLong(getMaxDurationMsLocked(elapsedRealtimeUs / 1000));
            out.writeLong(getTotalDurationMsLocked(elapsedRealtimeUs / 1000));
        }

        @Override // com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer, com.android.server.power.stats.BatteryStatsImpl.Timer
        public void readSummaryFromParcelLocked(android.os.Parcel in) {
            super.readSummaryFromParcelLocked(in);
            this.mMaxDurationMs = in.readLong();
            this.mTotalDurationMs = in.readLong();
            this.mStartTimeMs = -1L;
            this.mCurrentDurationMs = 0L;
        }

        @Override // com.android.server.power.stats.BatteryStatsImpl.Timer, com.android.server.power.stats.BatteryStatsImpl.TimeBaseObs
        public void onTimeStarted(long elapsedRealtimeUs, long baseUptimeUs, long baseRealtimeUs) {
            super.onTimeStarted(elapsedRealtimeUs, baseUptimeUs, baseRealtimeUs);
            if (this.mNesting > 0) {
                this.mStartTimeMs = baseRealtimeUs / 1000;
            }
        }

        @Override // com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer, com.android.server.power.stats.BatteryStatsImpl.Timer, com.android.server.power.stats.BatteryStatsImpl.TimeBaseObs
        public void onTimeStopped(long elapsedRealtimeUs, long baseUptimeUs, long baseRealtimeUs) {
            super.onTimeStopped(elapsedRealtimeUs, baseUptimeUs, baseRealtimeUs);
            if (this.mNesting > 0) {
                this.mCurrentDurationMs += (baseRealtimeUs / 1000) - this.mStartTimeMs;
            }
            this.mStartTimeMs = -1L;
        }

        @Override // com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer, com.android.server.power.stats.BatteryStatsImpl.Timer
        public void logState(android.util.Printer pw, java.lang.String prefix) {
            super.logState(pw, prefix);
        }

        @Override // com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer
        public void startRunningLocked(long elapsedRealtimeMs) {
            super.startRunningLocked(elapsedRealtimeMs);
            if (this.mNesting == 1 && this.mTimeBase.isRunning()) {
                this.mStartTimeMs = this.mTimeBase.getRealtime(elapsedRealtimeMs * 1000) / 1000;
            }
        }

        @Override // com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer
        public void stopRunningLocked(long elapsedRealtimeMs) {
            if (this.mNesting == 1) {
                long durationMs = getCurrentDurationMsLocked(elapsedRealtimeMs);
                this.mTotalDurationMs += durationMs;
                if (durationMs > this.mMaxDurationMs) {
                    this.mMaxDurationMs = durationMs;
                }
                this.mStartTimeMs = -1L;
                this.mCurrentDurationMs = 0L;
            }
            super.stopRunningLocked(elapsedRealtimeMs);
        }

        @Override // com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer, com.android.server.power.stats.BatteryStatsImpl.Timer, com.android.server.power.stats.BatteryStatsImpl.TimeBaseObs
        public boolean reset(boolean detachIfReset, long elapsedRealtimeUs) {
            boolean result = super.reset(detachIfReset, elapsedRealtimeUs);
            this.mMaxDurationMs = 0L;
            this.mTotalDurationMs = 0L;
            this.mCurrentDurationMs = 0L;
            if (this.mNesting > 0) {
                this.mStartTimeMs = this.mTimeBase.getRealtime(elapsedRealtimeUs) / 1000;
            } else {
                this.mStartTimeMs = -1L;
            }
            return result;
        }

        public long getMaxDurationMsLocked(long elapsedRealtimeMs) {
            if (this.mNesting > 0) {
                long durationMs = getCurrentDurationMsLocked(elapsedRealtimeMs);
                if (durationMs > this.mMaxDurationMs) {
                    return durationMs;
                }
            }
            return this.mMaxDurationMs;
        }

        public long getCurrentDurationMsLocked(long elapsedRealtimeMs) {
            long durationMs = this.mCurrentDurationMs;
            if (this.mNesting > 0 && this.mTimeBase.isRunning()) {
                return durationMs + ((this.mTimeBase.getRealtime(elapsedRealtimeMs * 1000) / 1000) - this.mStartTimeMs);
            }
            return durationMs;
        }

        public long getTotalDurationMsLocked(long elapsedRealtimeMs) {
            return this.mTotalDurationMs + getCurrentDurationMsLocked(elapsedRealtimeMs);
        }
    }

    public static class StopwatchTimer extends com.android.server.power.stats.BatteryStatsImpl.Timer {
        long mAcquireTimeUs;
        public boolean mInList;
        int mNesting;
        long mTimeoutUs;
        final java.util.ArrayList<com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer> mTimerPool;
        final com.android.server.power.stats.BatteryStatsImpl.Uid mUid;
        long mUpdateTimeUs;

        public StopwatchTimer(com.android.internal.os.Clock clock, com.android.server.power.stats.BatteryStatsImpl.Uid uid, int type, java.util.ArrayList<com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer> timerPool, com.android.server.power.stats.BatteryStatsImpl.TimeBase timeBase, android.os.Parcel in) {
            super(clock, type, timeBase, in);
            this.mAcquireTimeUs = -1L;
            this.mUid = uid;
            this.mTimerPool = timerPool;
            this.mUpdateTimeUs = in.readLong();
        }

        public StopwatchTimer(com.android.internal.os.Clock clock, com.android.server.power.stats.BatteryStatsImpl.Uid uid, int type, java.util.ArrayList<com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer> timerPool, com.android.server.power.stats.BatteryStatsImpl.TimeBase timeBase) {
            super(clock, type, timeBase);
            this.mAcquireTimeUs = -1L;
            this.mUid = uid;
            this.mTimerPool = timerPool;
        }

        public void setTimeout(long timeoutUs) {
            this.mTimeoutUs = timeoutUs;
        }

        @Override // com.android.server.power.stats.BatteryStatsImpl.Timer
        public void writeToParcel(android.os.Parcel out, long elapsedRealtimeUs) {
            super.writeToParcel(out, elapsedRealtimeUs);
            out.writeLong(this.mUpdateTimeUs);
        }

        @Override // com.android.server.power.stats.BatteryStatsImpl.Timer, com.android.server.power.stats.BatteryStatsImpl.TimeBaseObs
        public void onTimeStopped(long elapsedRealtimeUs, long baseUptimeUs, long baseRealtimeUs) {
            if (this.mNesting > 0) {
                super.onTimeStopped(elapsedRealtimeUs, baseUptimeUs, baseRealtimeUs);
                this.mUpdateTimeUs = baseRealtimeUs;
            }
        }

        @Override // com.android.server.power.stats.BatteryStatsImpl.Timer
        public void logState(android.util.Printer pw, java.lang.String prefix) {
            super.logState(pw, prefix);
            pw.println(prefix + "mNesting=" + this.mNesting + " mUpdateTime=" + this.mUpdateTimeUs + " mAcquireTime=" + this.mAcquireTimeUs);
        }

        public void startRunningLocked(long elapsedRealtimeMs) {
            int i = this.mNesting;
            this.mNesting = i + 1;
            if (i == 0) {
                long batteryRealtimeUs = this.mTimeBase.getRealtime(1000 * elapsedRealtimeMs);
                this.mUpdateTimeUs = batteryRealtimeUs;
                if (this.mTimerPool != null) {
                    refreshTimersLocked(batteryRealtimeUs, this.mTimerPool, null);
                    this.mTimerPool.add(this);
                }
                if (this.mTimeBase.isRunning()) {
                    this.mCount++;
                    this.mAcquireTimeUs = this.mTotalTimeUs;
                } else {
                    this.mAcquireTimeUs = -1L;
                }
            }
        }

        public boolean isRunningLocked() {
            return this.mNesting > 0;
        }

        public void stopRunningLocked(long elapsedRealtimeMs) {
            if (this.mNesting == 0) {
                return;
            }
            int i = this.mNesting - 1;
            this.mNesting = i;
            if (i == 0) {
                long batteryRealtimeUs = this.mTimeBase.getRealtime(elapsedRealtimeMs * 1000);
                if (this.mTimerPool != null) {
                    refreshTimersLocked(batteryRealtimeUs, this.mTimerPool, null);
                    this.mTimerPool.remove(this);
                } else {
                    this.mNesting = 1;
                    this.mTotalTimeUs = computeRunTimeLocked(batteryRealtimeUs, 1000 * elapsedRealtimeMs);
                    this.mNesting = 0;
                }
                if (this.mAcquireTimeUs >= 0 && this.mTotalTimeUs == this.mAcquireTimeUs) {
                    this.mCount--;
                }
            }
        }

        public void stopAllRunningLocked(long elapsedRealtimeMs) {
            if (this.mNesting > 0) {
                this.mNesting = 1;
                stopRunningLocked(elapsedRealtimeMs);
            }
        }

        private static long refreshTimersLocked(long batteryRealtimeUs, java.util.ArrayList<com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer> pool, com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer self) {
            long selfTimeUs = 0;
            int N = pool.size();
            for (int i = N - 1; i >= 0; i--) {
                com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer t = pool.get(i);
                long heldTimeUs = batteryRealtimeUs - t.mUpdateTimeUs;
                if (heldTimeUs > 0) {
                    long myTimeUs = heldTimeUs / ((long) N);
                    if (t == self) {
                        selfTimeUs = myTimeUs;
                    }
                    t.mTotalTimeUs += myTimeUs;
                }
                t.mUpdateTimeUs = batteryRealtimeUs;
            }
            return selfTimeUs;
        }

        @Override // com.android.server.power.stats.BatteryStatsImpl.Timer
        protected long computeRunTimeLocked(long curBatteryRealtimeUs, long elapsedRealtimeUs) {
            long size = 0;
            if (this.mTimeoutUs > 0 && curBatteryRealtimeUs > this.mUpdateTimeUs + this.mTimeoutUs) {
                curBatteryRealtimeUs = this.mUpdateTimeUs + this.mTimeoutUs;
            }
            long j = this.mTotalTimeUs;
            if (this.mNesting > 0) {
                size = (curBatteryRealtimeUs - this.mUpdateTimeUs) / ((long) ((this.mTimerPool == null || this.mTimerPool.size() <= 0) ? 1 : this.mTimerPool.size()));
            }
            return j + size;
        }

        @Override // com.android.server.power.stats.BatteryStatsImpl.Timer
        protected int computeCurrentCountLocked() {
            return this.mCount;
        }

        @Override // com.android.server.power.stats.BatteryStatsImpl.Timer, com.android.server.power.stats.BatteryStatsImpl.TimeBaseObs
        public boolean reset(boolean detachIfReset, long elapsedRealtimeUs) {
            boolean canDetach = this.mNesting <= 0;
            super.reset(canDetach && detachIfReset, elapsedRealtimeUs);
            if (this.mNesting > 0) {
                this.mUpdateTimeUs = this.mTimeBase.getRealtime(elapsedRealtimeUs);
            }
            this.mAcquireTimeUs = -1L;
            return canDetach;
        }

        @Override // com.android.server.power.stats.BatteryStatsImpl.Timer, com.android.server.power.stats.BatteryStatsImpl.TimeBaseObs
        public void detach() {
            super.detach();
            if (this.mTimerPool != null) {
                this.mTimerPool.remove(this);
            }
        }

        @Override // com.android.server.power.stats.BatteryStatsImpl.Timer
        public void readSummaryFromParcelLocked(android.os.Parcel in) {
            super.readSummaryFromParcelLocked(in);
            this.mNesting = 0;
        }

        public void setMark(long elapsedRealtimeMs) {
            long batteryRealtimeUs = this.mTimeBase.getRealtime(1000 * elapsedRealtimeMs);
            if (this.mNesting > 0) {
                if (this.mTimerPool != null) {
                    refreshTimersLocked(batteryRealtimeUs, this.mTimerPool, this);
                } else {
                    this.mTotalTimeUs += batteryRealtimeUs - this.mUpdateTimeUs;
                    this.mUpdateTimeUs = batteryRealtimeUs;
                }
            }
            this.mTimeBeforeMarkUs = this.mTotalTimeUs;
        }
    }

    public static class DualTimer extends com.android.server.power.stats.BatteryStatsImpl.DurationTimer {
        private final com.android.server.power.stats.BatteryStatsImpl.DurationTimer mSubTimer;

        public DualTimer(com.android.internal.os.Clock clock, com.android.server.power.stats.BatteryStatsImpl.Uid uid, int type, java.util.ArrayList<com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer> timerPool, com.android.server.power.stats.BatteryStatsImpl.TimeBase timeBase, com.android.server.power.stats.BatteryStatsImpl.TimeBase subTimeBase, android.os.Parcel in) {
            super(clock, uid, type, timerPool, timeBase, in);
            this.mSubTimer = new com.android.server.power.stats.BatteryStatsImpl.DurationTimer(clock, uid, type, null, subTimeBase, in);
        }

        public DualTimer(com.android.internal.os.Clock clock, com.android.server.power.stats.BatteryStatsImpl.Uid uid, int type, java.util.ArrayList<com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer> timerPool, com.android.server.power.stats.BatteryStatsImpl.TimeBase timeBase, com.android.server.power.stats.BatteryStatsImpl.TimeBase subTimeBase) {
            super(clock, uid, type, timerPool, timeBase);
            this.mSubTimer = new com.android.server.power.stats.BatteryStatsImpl.DurationTimer(clock, uid, type, null, subTimeBase);
        }

        public com.android.server.power.stats.BatteryStatsImpl.DurationTimer getSubTimer() {
            return this.mSubTimer;
        }

        @Override // com.android.server.power.stats.BatteryStatsImpl.DurationTimer, com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer
        public void startRunningLocked(long elapsedRealtimeMs) {
            super.startRunningLocked(elapsedRealtimeMs);
            this.mSubTimer.startRunningLocked(elapsedRealtimeMs);
        }

        @Override // com.android.server.power.stats.BatteryStatsImpl.DurationTimer, com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer
        public void stopRunningLocked(long elapsedRealtimeMs) {
            super.stopRunningLocked(elapsedRealtimeMs);
            this.mSubTimer.stopRunningLocked(elapsedRealtimeMs);
        }

        @Override // com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer
        public void stopAllRunningLocked(long elapsedRealtimeMs) {
            super.stopAllRunningLocked(elapsedRealtimeMs);
            this.mSubTimer.stopAllRunningLocked(elapsedRealtimeMs);
        }

        @Override // com.android.server.power.stats.BatteryStatsImpl.DurationTimer, com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer, com.android.server.power.stats.BatteryStatsImpl.Timer, com.android.server.power.stats.BatteryStatsImpl.TimeBaseObs
        public boolean reset(boolean detachIfReset, long elapsedRealtimeUs) {
            boolean active = false | (!this.mSubTimer.reset(false, elapsedRealtimeUs));
            return !(active | (!super.reset(detachIfReset, elapsedRealtimeUs)));
        }

        @Override // com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer, com.android.server.power.stats.BatteryStatsImpl.Timer, com.android.server.power.stats.BatteryStatsImpl.TimeBaseObs
        public void detach() {
            this.mSubTimer.detach();
            super.detach();
        }

        @Override // com.android.server.power.stats.BatteryStatsImpl.DurationTimer, com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer, com.android.server.power.stats.BatteryStatsImpl.Timer
        public void writeToParcel(android.os.Parcel out, long elapsedRealtimeUs) {
            super.writeToParcel(out, elapsedRealtimeUs);
            this.mSubTimer.writeToParcel(out, elapsedRealtimeUs);
        }

        @Override // com.android.server.power.stats.BatteryStatsImpl.DurationTimer, com.android.server.power.stats.BatteryStatsImpl.Timer
        public void writeSummaryFromParcelLocked(android.os.Parcel out, long elapsedRealtimeUs) {
            super.writeSummaryFromParcelLocked(out, elapsedRealtimeUs);
            this.mSubTimer.writeSummaryFromParcelLocked(out, elapsedRealtimeUs);
        }

        @Override // com.android.server.power.stats.BatteryStatsImpl.DurationTimer, com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer, com.android.server.power.stats.BatteryStatsImpl.Timer
        public void readSummaryFromParcelLocked(android.os.Parcel in) {
            super.readSummaryFromParcelLocked(in);
            this.mSubTimer.readSummaryFromParcelLocked(in);
        }
    }

    public abstract class OverflowArrayMap<T> {
        private static final java.lang.String OVERFLOW_NAME = "*overflow*";
        android.util.ArrayMap<java.lang.String, android.util.MutableInt> mActiveOverflow;
        T mCurOverflow;
        long mLastCleanupTimeMs;
        long mLastClearTimeMs;
        long mLastOverflowFinishTimeMs;
        long mLastOverflowTimeMs;
        final android.util.ArrayMap<java.lang.String, T> mMap = new android.util.ArrayMap<>();
        final int mUid;

        public abstract T instantiateObject();

        public OverflowArrayMap(int uid) {
            this.mUid = uid;
        }

        public android.util.ArrayMap<java.lang.String, T> getMap() {
            return this.mMap;
        }

        public void clear() {
            this.mLastClearTimeMs = android.os.SystemClock.elapsedRealtime();
            this.mMap.clear();
            this.mCurOverflow = null;
            this.mActiveOverflow = null;
        }

        public void add(java.lang.String name, T obj) {
            if (name == null) {
                name = "";
            }
            this.mMap.put(name, obj);
            if (OVERFLOW_NAME.equals(name)) {
                this.mCurOverflow = obj;
            }
        }

        public void cleanup(long elapsedRealtimeMs) {
            this.mLastCleanupTimeMs = elapsedRealtimeMs;
            if (this.mActiveOverflow != null && this.mActiveOverflow.size() == 0) {
                this.mActiveOverflow = null;
            }
            if (this.mActiveOverflow == null) {
                if (this.mMap.containsKey(OVERFLOW_NAME)) {
                    android.util.Slog.wtf(com.android.server.power.stats.BatteryStatsImpl.TAG, "Cleaning up with no active overflow, but have overflow entry " + this.mMap.get(OVERFLOW_NAME));
                    this.mMap.remove(OVERFLOW_NAME);
                }
                this.mCurOverflow = null;
                return;
            }
            if (this.mCurOverflow == null || !this.mMap.containsKey(OVERFLOW_NAME)) {
                android.util.Slog.wtf(com.android.server.power.stats.BatteryStatsImpl.TAG, "Cleaning up with active overflow, but no overflow entry: cur=" + this.mCurOverflow + " map=" + this.mMap.get(OVERFLOW_NAME));
            }
        }

        public T startObject(java.lang.String name, long elapsedRealtimeMs) {
            android.util.MutableInt over;
            if (name == null) {
                name = "";
            }
            T obj = this.mMap.get(name);
            if (obj != null) {
                return obj;
            }
            if (this.mActiveOverflow != null && (over = this.mActiveOverflow.get(name)) != null) {
                T obj2 = this.mCurOverflow;
                if (obj2 == null) {
                    android.util.Slog.wtf(com.android.server.power.stats.BatteryStatsImpl.TAG, "Have active overflow " + name + " but null overflow");
                    T tInstantiateObject = instantiateObject();
                    this.mCurOverflow = tInstantiateObject;
                    obj2 = tInstantiateObject;
                    this.mMap.put(OVERFLOW_NAME, obj2);
                }
                over.value++;
                return obj2;
            }
            int N = this.mMap.size();
            if (N >= com.android.server.power.stats.BatteryStatsImpl.MAX_WAKELOCKS_PER_UID) {
                T obj3 = this.mCurOverflow;
                if (obj3 == null) {
                    T tInstantiateObject2 = instantiateObject();
                    this.mCurOverflow = tInstantiateObject2;
                    obj3 = tInstantiateObject2;
                    this.mMap.put(OVERFLOW_NAME, obj3);
                }
                if (this.mActiveOverflow == null) {
                    this.mActiveOverflow = new android.util.ArrayMap<>();
                }
                this.mActiveOverflow.put(name, new android.util.MutableInt(1));
                this.mLastOverflowTimeMs = elapsedRealtimeMs;
                return obj3;
            }
            T obj4 = instantiateObject();
            this.mMap.put(name, obj4);
            return obj4;
        }

        public T stopObject(java.lang.String name, long elapsedRealtimeMs) {
            android.util.MutableInt over;
            T obj;
            if (name == null) {
                name = "";
            }
            T obj2 = this.mMap.get(name);
            if (obj2 != null) {
                return obj2;
            }
            if (this.mActiveOverflow != null && (over = this.mActiveOverflow.get(name)) != null && (obj = this.mCurOverflow) != null) {
                over.value--;
                if (over.value <= 0) {
                    this.mActiveOverflow.remove(name);
                    this.mLastOverflowFinishTimeMs = elapsedRealtimeMs;
                }
                return obj;
            }
            java.lang.StringBuilder sb = new java.lang.StringBuilder();
            sb.append("Unable to find object for ");
            sb.append(name);
            sb.append(" in uid ");
            sb.append(this.mUid);
            sb.append(" mapsize=");
            sb.append(this.mMap.size());
            sb.append(" activeoverflow=");
            sb.append(this.mActiveOverflow);
            sb.append(" curoverflow=");
            sb.append(this.mCurOverflow);
            if (this.mLastOverflowTimeMs != 0) {
                sb.append(" lastOverflowTime=");
                android.util.TimeUtils.formatDuration(this.mLastOverflowTimeMs - elapsedRealtimeMs, sb);
            }
            if (this.mLastOverflowFinishTimeMs != 0) {
                sb.append(" lastOverflowFinishTime=");
                android.util.TimeUtils.formatDuration(this.mLastOverflowFinishTimeMs - elapsedRealtimeMs, sb);
            }
            if (this.mLastClearTimeMs != 0) {
                sb.append(" lastClearTime=");
                android.util.TimeUtils.formatDuration(this.mLastClearTimeMs - elapsedRealtimeMs, sb);
            }
            if (this.mLastCleanupTimeMs != 0) {
                sb.append(" lastCleanupTime=");
                android.util.TimeUtils.formatDuration(this.mLastCleanupTimeMs - elapsedRealtimeMs, sb);
            }
            android.util.Slog.wtf(com.android.server.power.stats.BatteryStatsImpl.TAG, sb.toString());
            return null;
        }
    }

    public static class ControllerActivityCounterImpl extends android.os.BatteryStats.ControllerActivityCounter implements android.os.Parcelable {
        private final com.android.internal.os.Clock mClock;
        private com.android.server.power.stats.BatteryStatsImpl.TimeMultiStateCounter mIdleTimeMillis;
        private final com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounter mMonitoredRailChargeConsumedMaMs;
        private int mNumTxStates;
        private final com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounter mPowerDrainMaMs;
        private int mProcessState;
        private com.android.server.power.stats.BatteryStatsImpl.TimeMultiStateCounter mRxTimeMillis;
        private final com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounter mScanTimeMillis;
        private final com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounter mSleepTimeMillis;
        private final com.android.server.power.stats.BatteryStatsImpl.TimeBase mTimeBase;
        private com.android.server.power.stats.BatteryStatsImpl.TimeMultiStateCounter[] mTxTimeMillis;

        public ControllerActivityCounterImpl(com.android.internal.os.Clock clock, com.android.server.power.stats.BatteryStatsImpl.TimeBase timeBase, int numTxStates) {
            this.mClock = clock;
            this.mTimeBase = timeBase;
            this.mNumTxStates = numTxStates;
            this.mScanTimeMillis = new com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounter(timeBase);
            this.mSleepTimeMillis = new com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounter(timeBase);
            this.mPowerDrainMaMs = new com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounter(timeBase);
            this.mMonitoredRailChargeConsumedMaMs = new com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounter(timeBase);
        }

        public void readSummaryFromParcel(android.os.Parcel in) {
            this.mIdleTimeMillis = readTimeMultiStateCounter(in, this.mTimeBase);
            this.mScanTimeMillis.readSummaryFromParcelLocked(in);
            this.mSleepTimeMillis.readSummaryFromParcelLocked(in);
            this.mRxTimeMillis = readTimeMultiStateCounter(in, this.mTimeBase);
            this.mTxTimeMillis = readTimeMultiStateCounters(in, this.mTimeBase, this.mNumTxStates);
            this.mPowerDrainMaMs.readSummaryFromParcelLocked(in);
            this.mMonitoredRailChargeConsumedMaMs.readSummaryFromParcelLocked(in);
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        public void writeSummaryToParcel(android.os.Parcel dest) {
            writeTimeMultiStateCounter(dest, this.mIdleTimeMillis);
            this.mScanTimeMillis.writeSummaryFromParcelLocked(dest);
            this.mSleepTimeMillis.writeSummaryFromParcelLocked(dest);
            writeTimeMultiStateCounter(dest, this.mRxTimeMillis);
            writeTimeMultiStateCounters(dest, this.mTxTimeMillis);
            this.mPowerDrainMaMs.writeSummaryFromParcelLocked(dest);
            this.mMonitoredRailChargeConsumedMaMs.writeSummaryFromParcelLocked(dest);
        }

        @Override // android.os.Parcelable
        public void writeToParcel(android.os.Parcel dest, int flags) {
            writeTimeMultiStateCounter(dest, this.mIdleTimeMillis);
            this.mScanTimeMillis.writeToParcel(dest);
            this.mSleepTimeMillis.writeToParcel(dest);
            writeTimeMultiStateCounter(dest, this.mRxTimeMillis);
            writeTimeMultiStateCounters(dest, this.mTxTimeMillis);
            this.mPowerDrainMaMs.writeToParcel(dest);
            this.mMonitoredRailChargeConsumedMaMs.writeToParcel(dest);
        }

        private com.android.server.power.stats.BatteryStatsImpl.TimeMultiStateCounter readTimeMultiStateCounter(android.os.Parcel in, com.android.server.power.stats.BatteryStatsImpl.TimeBase timeBase) {
            if (in.readBoolean()) {
                return com.android.server.power.stats.BatteryStatsImpl.TimeMultiStateCounter.readFromParcel(in, timeBase, 5, this.mClock.elapsedRealtime());
            }
            return null;
        }

        private void writeTimeMultiStateCounter(android.os.Parcel dest, com.android.server.power.stats.BatteryStatsImpl.TimeMultiStateCounter counter) {
            if (counter != null) {
                dest.writeBoolean(true);
                counter.writeToParcel(dest);
            } else {
                dest.writeBoolean(false);
            }
        }

        private com.android.server.power.stats.BatteryStatsImpl.TimeMultiStateCounter[] readTimeMultiStateCounters(android.os.Parcel in, com.android.server.power.stats.BatteryStatsImpl.TimeBase timeBase, int expectedNumCounters) {
            if (in.readBoolean()) {
                int numCounters = in.readInt();
                boolean valid = numCounters == expectedNumCounters;
                com.android.server.power.stats.BatteryStatsImpl.TimeMultiStateCounter[] counters = new com.android.server.power.stats.BatteryStatsImpl.TimeMultiStateCounter[numCounters];
                for (int i = 0; i < numCounters; i++) {
                    com.android.server.power.stats.BatteryStatsImpl.TimeMultiStateCounter counter = com.android.server.power.stats.BatteryStatsImpl.TimeMultiStateCounter.readFromParcel(in, timeBase, 5, this.mClock.elapsedRealtime());
                    if (counter != null) {
                        counters[i] = counter;
                    } else {
                        valid = false;
                    }
                }
                if (valid) {
                    return counters;
                }
                return null;
            }
            return null;
        }

        private void writeTimeMultiStateCounters(android.os.Parcel dest, com.android.server.power.stats.BatteryStatsImpl.TimeMultiStateCounter[] counters) {
            if (counters != null) {
                dest.writeBoolean(true);
                dest.writeInt(counters.length);
                for (com.android.server.power.stats.BatteryStatsImpl.TimeMultiStateCounter counter : counters) {
                    counter.writeToParcel(dest);
                }
                return;
            }
            dest.writeBoolean(false);
        }

        public void reset(boolean detachIfReset, long elapsedRealtimeUs) {
            com.android.server.power.stats.BatteryStatsImpl.resetIfNotNull(this.mIdleTimeMillis, detachIfReset, elapsedRealtimeUs);
            this.mScanTimeMillis.reset(detachIfReset, elapsedRealtimeUs);
            this.mSleepTimeMillis.reset(detachIfReset, elapsedRealtimeUs);
            com.android.server.power.stats.BatteryStatsImpl.resetIfNotNull(this.mRxTimeMillis, detachIfReset, elapsedRealtimeUs);
            com.android.server.power.stats.BatteryStatsImpl.resetIfNotNull(this.mTxTimeMillis, detachIfReset, elapsedRealtimeUs);
            this.mPowerDrainMaMs.reset(detachIfReset, elapsedRealtimeUs);
            this.mMonitoredRailChargeConsumedMaMs.reset(detachIfReset, elapsedRealtimeUs);
        }

        public void detach() {
            com.android.server.power.stats.BatteryStatsImpl.detachIfNotNull(this.mIdleTimeMillis);
            this.mScanTimeMillis.detach();
            this.mSleepTimeMillis.detach();
            com.android.server.power.stats.BatteryStatsImpl.detachIfNotNull(this.mRxTimeMillis);
            com.android.server.power.stats.BatteryStatsImpl.detachIfNotNull(this.mTxTimeMillis);
            this.mPowerDrainMaMs.detach();
            this.mMonitoredRailChargeConsumedMaMs.detach();
        }

        public android.os.BatteryStats.LongCounter getIdleTimeCounter() {
            if (this.mIdleTimeMillis == null) {
                return com.android.server.power.stats.BatteryStatsImpl.ZERO_LONG_COUNTER;
            }
            return this.mIdleTimeMillis;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public com.android.server.power.stats.BatteryStatsImpl.TimeMultiStateCounter getOrCreateIdleTimeCounter() {
            if (this.mIdleTimeMillis == null) {
                this.mIdleTimeMillis = createTimeMultiStateCounter();
            }
            return this.mIdleTimeMillis;
        }

        public com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounter getScanTimeCounter() {
            return this.mScanTimeMillis;
        }

        public com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounter getSleepTimeCounter() {
            return this.mSleepTimeMillis;
        }

        public android.os.BatteryStats.LongCounter getRxTimeCounter() {
            if (this.mRxTimeMillis == null) {
                return com.android.server.power.stats.BatteryStatsImpl.ZERO_LONG_COUNTER;
            }
            return this.mRxTimeMillis;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public com.android.server.power.stats.BatteryStatsImpl.TimeMultiStateCounter getOrCreateRxTimeCounter() {
            if (this.mRxTimeMillis == null) {
                this.mRxTimeMillis = createTimeMultiStateCounter();
            }
            return this.mRxTimeMillis;
        }

        public android.os.BatteryStats.LongCounter[] getTxTimeCounters() {
            if (this.mTxTimeMillis == null) {
                return com.android.server.power.stats.BatteryStatsImpl.ZERO_LONG_COUNTER_ARRAY;
            }
            return this.mTxTimeMillis;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public com.android.server.power.stats.BatteryStatsImpl.TimeMultiStateCounter[] getOrCreateTxTimeCounters() {
            if (this.mTxTimeMillis == null) {
                this.mTxTimeMillis = new com.android.server.power.stats.BatteryStatsImpl.TimeMultiStateCounter[this.mNumTxStates];
                for (int i = 0; i < this.mNumTxStates; i++) {
                    this.mTxTimeMillis[i] = createTimeMultiStateCounter();
                }
            }
            return this.mTxTimeMillis;
        }

        private com.android.server.power.stats.BatteryStatsImpl.TimeMultiStateCounter createTimeMultiStateCounter() {
            long timestampMs = this.mClock.elapsedRealtime();
            com.android.server.power.stats.BatteryStatsImpl.TimeMultiStateCounter counter = new com.android.server.power.stats.BatteryStatsImpl.TimeMultiStateCounter(this.mTimeBase, 5, timestampMs);
            counter.setState(android.os.BatteryStats.mapUidProcessStateToBatteryConsumerProcessState(this.mProcessState), timestampMs);
            counter.update(0L, timestampMs);
            return counter;
        }

        public com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounter getPowerCounter() {
            return this.mPowerDrainMaMs;
        }

        public com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounter getMonitoredRailChargeConsumedMaMs() {
            return this.mMonitoredRailChargeConsumedMaMs;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setState(int processState, long elapsedTimeMs) {
            this.mProcessState = processState;
            if (this.mIdleTimeMillis != null) {
                this.mIdleTimeMillis.setState(processState, elapsedTimeMs);
            }
            if (this.mRxTimeMillis != null) {
                this.mRxTimeMillis.setState(processState, elapsedTimeMs);
            }
            if (this.mTxTimeMillis != null) {
                for (int i = 0; i < this.mTxTimeMillis.length; i++) {
                    this.mTxTimeMillis[i].setState(processState, elapsedTimeMs);
                }
            }
        }
    }

    public com.android.server.power.stats.BatteryStatsImpl.SamplingTimer getRpmTimerLocked(java.lang.String name) {
        com.android.server.power.stats.BatteryStatsImpl.SamplingTimer rpmt = this.mRpmStats.get(name);
        if (rpmt == null) {
            com.android.server.power.stats.BatteryStatsImpl.SamplingTimer rpmt2 = new com.android.server.power.stats.BatteryStatsImpl.SamplingTimer(this.mClock, this.mOnBatteryTimeBase);
            this.mRpmStats.put(name, rpmt2);
            return rpmt2;
        }
        return rpmt;
    }

    public com.android.server.power.stats.BatteryStatsImpl.SamplingTimer getScreenOffRpmTimerLocked(java.lang.String name) {
        com.android.server.power.stats.BatteryStatsImpl.SamplingTimer rpmt = this.mScreenOffRpmStats.get(name);
        if (rpmt == null) {
            com.android.server.power.stats.BatteryStatsImpl.SamplingTimer rpmt2 = new com.android.server.power.stats.BatteryStatsImpl.SamplingTimer(this.mClock, this.mOnBatteryScreenOffTimeBase);
            this.mScreenOffRpmStats.put(name, rpmt2);
            return rpmt2;
        }
        return rpmt;
    }

    public com.android.server.power.stats.BatteryStatsImpl.SamplingTimer getWakeupReasonTimerLocked(java.lang.String name) {
        com.android.server.power.stats.BatteryStatsImpl.SamplingTimer timer = this.mWakeupReasonStats.get(name);
        if (timer == null) {
            com.android.server.power.stats.BatteryStatsImpl.SamplingTimer timer2 = new com.android.server.power.stats.BatteryStatsImpl.SamplingTimer(this.mClock, this.mOnBatteryTimeBase);
            this.mWakeupReasonStats.put(name, timer2);
            return timer2;
        }
        return timer;
    }

    public com.android.server.power.stats.BatteryStatsImpl.SamplingTimer getKernelWakelockTimerLocked(java.lang.String name) {
        com.android.server.power.stats.BatteryStatsImpl.SamplingTimer kwlt = this.mKernelWakelockStats.get(name);
        if (kwlt == null) {
            com.android.server.power.stats.BatteryStatsImpl.SamplingTimer kwlt2 = new com.android.server.power.stats.BatteryStatsImpl.SamplingTimer(this.mClock, this.mOnBatteryScreenOffTimeBase);
            this.mKernelWakelockStats.put(name, kwlt2);
            return kwlt2;
        }
        return kwlt;
    }

    public com.android.server.power.stats.BatteryStatsImpl.SamplingTimer getKernelMemoryTimerLocked(long bucket) {
        com.android.server.power.stats.BatteryStatsImpl.SamplingTimer kmt = this.mKernelMemoryStats.get(bucket);
        if (kmt == null) {
            com.android.server.power.stats.BatteryStatsImpl.SamplingTimer kmt2 = new com.android.server.power.stats.BatteryStatsImpl.SamplingTimer(this.mClock, this.mOnBatteryTimeBase);
            this.mKernelMemoryStats.put(bucket, kmt2);
            return kmt2;
        }
        return kmt;
    }

    private class HistoryStepDetailsCalculatorImpl implements com.android.internal.os.BatteryStatsHistory.HistoryStepDetailsCalculator {
        private long mCurStepCpuSystemTimeMs;
        private long mCurStepCpuUserTimeMs;
        private long mCurStepStatIOWaitTimeMs;
        private long mCurStepStatIdleTimeMs;
        private long mCurStepStatIrqTimeMs;
        private long mCurStepStatSoftIrqTimeMs;
        private long mCurStepStatSystemTimeMs;
        private long mCurStepStatUserTimeMs;
        private final android.os.BatteryStats.HistoryStepDetails mDetails;
        private boolean mHasHistoryStepDetails;
        private long mLastStepCpuSystemTimeMs;
        private long mLastStepCpuUserTimeMs;
        private long mLastStepStatIOWaitTimeMs;
        private long mLastStepStatIdleTimeMs;
        private long mLastStepStatIrqTimeMs;
        private long mLastStepStatSoftIrqTimeMs;
        private long mLastStepStatSystemTimeMs;
        private long mLastStepStatUserTimeMs;
        private boolean mUpdateRequested;

        private HistoryStepDetailsCalculatorImpl() {
            this.mDetails = new android.os.BatteryStats.HistoryStepDetails();
        }

        public android.os.BatteryStats.HistoryStepDetails getHistoryStepDetails() {
            if (!this.mUpdateRequested) {
                this.mUpdateRequested = true;
                com.android.server.power.stats.BatteryStatsImpl.this.requestImmediateCpuUpdate();
                if (com.android.server.power.stats.BatteryStatsImpl.this.mPlatformIdleStateCallback != null) {
                    this.mDetails.statSubsystemPowerState = com.android.server.power.stats.BatteryStatsImpl.this.mPlatformIdleStateCallback.getSubsystemLowPowerStats();
                }
            }
            this.mDetails.statSubsystemPowerState = com.android.server.power.stats.BatteryStatsImpl.this.mBatteryStatsImplExt.addDevicePowerStatsDeltaString(this.mDetails.statSubsystemPowerState);
            if (!this.mHasHistoryStepDetails) {
                int uidCount = com.android.server.power.stats.BatteryStatsImpl.this.mUidStats.size();
                for (int i = 0; i < uidCount; i++) {
                    com.android.server.power.stats.BatteryStatsImpl.Uid uid = (com.android.server.power.stats.BatteryStatsImpl.Uid) com.android.server.power.stats.BatteryStatsImpl.this.mUidStats.valueAt(i);
                    uid.mLastStepUserTimeMs = uid.mCurStepUserTimeMs;
                    uid.mLastStepSystemTimeMs = uid.mCurStepSystemTimeMs;
                }
                this.mLastStepCpuUserTimeMs = this.mCurStepCpuUserTimeMs;
                this.mLastStepCpuSystemTimeMs = this.mCurStepCpuSystemTimeMs;
                this.mLastStepStatUserTimeMs = this.mCurStepStatUserTimeMs;
                this.mLastStepStatSystemTimeMs = this.mCurStepStatSystemTimeMs;
                this.mLastStepStatIOWaitTimeMs = this.mCurStepStatIOWaitTimeMs;
                this.mLastStepStatIrqTimeMs = this.mCurStepStatIrqTimeMs;
                this.mLastStepStatSoftIrqTimeMs = this.mCurStepStatSoftIrqTimeMs;
                this.mLastStepStatIdleTimeMs = this.mCurStepStatIdleTimeMs;
                return null;
            }
            this.mDetails.userTime = (int) (this.mCurStepCpuUserTimeMs - this.mLastStepCpuUserTimeMs);
            this.mDetails.systemTime = (int) (this.mCurStepCpuSystemTimeMs - this.mLastStepCpuSystemTimeMs);
            this.mDetails.statUserTime = (int) (this.mCurStepStatUserTimeMs - this.mLastStepStatUserTimeMs);
            this.mDetails.statSystemTime = (int) (this.mCurStepStatSystemTimeMs - this.mLastStepStatSystemTimeMs);
            this.mDetails.statIOWaitTime = (int) (this.mCurStepStatIOWaitTimeMs - this.mLastStepStatIOWaitTimeMs);
            this.mDetails.statIrqTime = (int) (this.mCurStepStatIrqTimeMs - this.mLastStepStatIrqTimeMs);
            this.mDetails.statSoftIrqTime = (int) (this.mCurStepStatSoftIrqTimeMs - this.mLastStepStatSoftIrqTimeMs);
            this.mDetails.statIdlTime = (int) (this.mCurStepStatIdleTimeMs - this.mLastStepStatIdleTimeMs);
            android.os.BatteryStats.HistoryStepDetails historyStepDetails = this.mDetails;
            android.os.BatteryStats.HistoryStepDetails historyStepDetails2 = this.mDetails;
            this.mDetails.appCpuUid3 = -1;
            historyStepDetails2.appCpuUid2 = -1;
            historyStepDetails.appCpuUid1 = -1;
            android.os.BatteryStats.HistoryStepDetails historyStepDetails3 = this.mDetails;
            android.os.BatteryStats.HistoryStepDetails historyStepDetails4 = this.mDetails;
            this.mDetails.appCpuUTime3 = 0;
            historyStepDetails4.appCpuUTime2 = 0;
            historyStepDetails3.appCpuUTime1 = 0;
            android.os.BatteryStats.HistoryStepDetails historyStepDetails5 = this.mDetails;
            android.os.BatteryStats.HistoryStepDetails historyStepDetails6 = this.mDetails;
            this.mDetails.appCpuSTime3 = 0;
            historyStepDetails6.appCpuSTime2 = 0;
            historyStepDetails5.appCpuSTime1 = 0;
            int uidCount2 = com.android.server.power.stats.BatteryStatsImpl.this.mUidStats.size();
            for (int i2 = 0; i2 < uidCount2; i2++) {
                com.android.server.power.stats.BatteryStatsImpl.Uid uid2 = (com.android.server.power.stats.BatteryStatsImpl.Uid) com.android.server.power.stats.BatteryStatsImpl.this.mUidStats.valueAt(i2);
                int totalUTimeMs = (int) (uid2.mCurStepUserTimeMs - uid2.mLastStepUserTimeMs);
                int totalSTimeMs = (int) (uid2.mCurStepSystemTimeMs - uid2.mLastStepSystemTimeMs);
                int totalTimeMs = totalUTimeMs + totalSTimeMs;
                uid2.mLastStepUserTimeMs = uid2.mCurStepUserTimeMs;
                uid2.mLastStepSystemTimeMs = uid2.mCurStepSystemTimeMs;
                if (totalTimeMs > this.mDetails.appCpuUTime3 + this.mDetails.appCpuSTime3) {
                    if (totalTimeMs <= this.mDetails.appCpuUTime2 + this.mDetails.appCpuSTime2) {
                        this.mDetails.appCpuUid3 = uid2.mUid;
                        this.mDetails.appCpuUTime3 = totalUTimeMs;
                        this.mDetails.appCpuSTime3 = totalSTimeMs;
                    } else {
                        this.mDetails.appCpuUid3 = this.mDetails.appCpuUid2;
                        this.mDetails.appCpuUTime3 = this.mDetails.appCpuUTime2;
                        this.mDetails.appCpuSTime3 = this.mDetails.appCpuSTime2;
                        if (totalTimeMs <= this.mDetails.appCpuUTime1 + this.mDetails.appCpuSTime1) {
                            this.mDetails.appCpuUid2 = uid2.mUid;
                            this.mDetails.appCpuUTime2 = totalUTimeMs;
                            this.mDetails.appCpuSTime2 = totalSTimeMs;
                        } else {
                            this.mDetails.appCpuUid2 = this.mDetails.appCpuUid1;
                            this.mDetails.appCpuUTime2 = this.mDetails.appCpuUTime1;
                            this.mDetails.appCpuSTime2 = this.mDetails.appCpuSTime1;
                            this.mDetails.appCpuUid1 = uid2.mUid;
                            this.mDetails.appCpuUTime1 = totalUTimeMs;
                            this.mDetails.appCpuSTime1 = totalSTimeMs;
                        }
                    }
                }
            }
            this.mLastStepCpuUserTimeMs = this.mCurStepCpuUserTimeMs;
            this.mLastStepCpuSystemTimeMs = this.mCurStepCpuSystemTimeMs;
            this.mLastStepStatUserTimeMs = this.mCurStepStatUserTimeMs;
            this.mLastStepStatSystemTimeMs = this.mCurStepStatSystemTimeMs;
            this.mLastStepStatIOWaitTimeMs = this.mCurStepStatIOWaitTimeMs;
            this.mLastStepStatIrqTimeMs = this.mCurStepStatIrqTimeMs;
            this.mLastStepStatSoftIrqTimeMs = this.mCurStepStatSoftIrqTimeMs;
            this.mLastStepStatIdleTimeMs = this.mCurStepStatIdleTimeMs;
            return this.mDetails;
        }

        public void addCpuStats(int totalUTimeMs, int totalSTimeMs, int statUserTimeMs, int statSystemTimeMs, int statIOWaitTimeMs, int statIrqTimeMs, int statSoftIrqTimeMs, int statIdleTimeMs) {
            this.mCurStepCpuUserTimeMs += (long) totalUTimeMs;
            this.mCurStepCpuSystemTimeMs += (long) totalSTimeMs;
            this.mCurStepStatUserTimeMs += (long) statUserTimeMs;
            this.mCurStepStatSystemTimeMs += (long) statSystemTimeMs;
            this.mCurStepStatIOWaitTimeMs += (long) statIOWaitTimeMs;
            this.mCurStepStatIrqTimeMs += (long) statIrqTimeMs;
            this.mCurStepStatSoftIrqTimeMs += (long) statSoftIrqTimeMs;
            this.mCurStepStatIdleTimeMs += (long) statIdleTimeMs;
        }

        public void finishAddingCpuLocked() {
            this.mHasHistoryStepDetails = true;
            this.mUpdateRequested = false;
        }

        public void clear() {
            this.mHasHistoryStepDetails = false;
            this.mCurStepCpuUserTimeMs = 0L;
            this.mLastStepCpuUserTimeMs = 0L;
            this.mCurStepCpuSystemTimeMs = 0L;
            this.mLastStepCpuSystemTimeMs = 0L;
            this.mCurStepStatUserTimeMs = 0L;
            this.mLastStepStatUserTimeMs = 0L;
            this.mCurStepStatSystemTimeMs = 0L;
            this.mLastStepStatSystemTimeMs = 0L;
            this.mCurStepStatIOWaitTimeMs = 0L;
            this.mLastStepStatIOWaitTimeMs = 0L;
            this.mCurStepStatIrqTimeMs = 0L;
            this.mLastStepStatIrqTimeMs = 0L;
            this.mCurStepStatSoftIrqTimeMs = 0L;
            this.mLastStepStatSoftIrqTimeMs = 0L;
            this.mCurStepStatIdleTimeMs = 0L;
            this.mLastStepStatIdleTimeMs = 0L;
        }
    }

    public void commitCurrentHistoryBatchLocked() {
        this.mHistory.commitCurrentHistoryBatchLocked();
    }

    public void createFakeHistoryEvents(long numEvents) {
        long elapsedRealtimeMs = this.mClock.elapsedRealtime();
        long uptimeMs = this.mClock.uptimeMillis();
        for (long i = 0; i < numEvents; i++) {
            noteLongPartialWakelockStart("name1", "historyName1", 1000, elapsedRealtimeMs, uptimeMs);
            noteLongPartialWakelockFinish("name1", "historyName1", 1000, elapsedRealtimeMs, uptimeMs);
        }
    }

    public void recordHistoryEventLocked(long elapsedRealtimeMs, long uptimeMs, int code, java.lang.String name, int uid) {
        this.mHistory.recordEvent(elapsedRealtimeMs, uptimeMs, code, name, uid);
    }

    public void updateTimeBasesLocked(boolean unplugged, int screenState, long uptimeUs, long realtimeUs) {
        boolean screenOff = !android.view.Display.isOnState(screenState);
        boolean updateOnBatteryTimeBase = unplugged != this.mOnBatteryTimeBase.isRunning();
        boolean updateOnBatteryScreenOffTimeBase = (unplugged && screenOff) != this.mOnBatteryScreenOffTimeBase.isRunning();
        if (updateOnBatteryScreenOffTimeBase || updateOnBatteryTimeBase) {
            if (updateOnBatteryScreenOffTimeBase) {
                updateKernelWakelocksLocked(realtimeUs);
                updateBatteryPropertiesLocked();
            }
            if (updateOnBatteryTimeBase) {
                updateRpmStatsLocked(realtimeUs);
            }
            this.mOnBatteryTimeBase.setRunning(unplugged, uptimeUs, realtimeUs);
            if (updateOnBatteryTimeBase) {
                for (int i = this.mUidStats.size() - 1; i >= 0; i--) {
                    this.mUidStats.valueAt(i).updateOnBatteryBgTimeBase(uptimeUs, realtimeUs);
                }
            }
            if (updateOnBatteryScreenOffTimeBase) {
                this.mOnBatteryScreenOffTimeBase.setRunning(unplugged && screenOff, uptimeUs, realtimeUs);
                for (int i2 = this.mUidStats.size() - 1; i2 >= 0; i2--) {
                    this.mUidStats.valueAt(i2).updateOnBatteryScreenOffBgTimeBase(uptimeUs, realtimeUs);
                }
            }
        }
    }

    protected void updateBatteryPropertiesLocked() {
        try {
            android.os.IBatteryPropertiesRegistrar registrar = android.os.IBatteryPropertiesRegistrar.Stub.asInterface(android.os.ServiceManager.getService("batteryproperties"));
            if (registrar != null) {
                registrar.scheduleUpdate();
            }
        } catch (android.os.RemoteException e) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onIsolatedUidAdded(int isolatedUid, int parentUid) {
        long realtime = this.mClock.elapsedRealtime();
        long uptime = this.mClock.uptimeMillis();
        synchronized (this) {
            getUidStatsLocked(parentUid, realtime, uptime).addIsolatedUid(isolatedUid);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onBeforeIsolatedUidRemoved(int isolatedUid, int parentUid) {
        long realtime = this.mClock.elapsedRealtime();
        this.mPowerStatsUidResolver.retainIsolatedUid(isolatedUid);
        synchronized (this) {
            this.mPendingRemovedUids.add(new com.android.server.power.stats.BatteryStatsImpl.UidToRemove(this, isolatedUid, realtime));
        }
        if (this.mExternalSync != null) {
            this.mExternalSync.scheduleCpuSyncDueToRemovedUid(isolatedUid);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onAfterIsolatedUidRemoved(int isolatedUid, int parentUid) {
        long realtime = this.mClock.elapsedRealtime();
        long uptime = this.mClock.uptimeMillis();
        synchronized (this) {
            getUidStatsLocked(parentUid, realtime, uptime).removeIsolatedUid(isolatedUid);
        }
    }

    public void releaseIsolatedUidLocked(int isolatedUid, long elapsedRealtimeMs, long uptimeMs) {
        this.mPowerStatsUidResolver.releaseIsolatedUid(isolatedUid);
    }

    public int mapUid(int uid) {
        if (android.os.Process.isSdkSandboxUid(uid)) {
            return android.os.Process.getAppUidForSdkSandboxUid(uid);
        }
        return this.mPowerStatsUidResolver.mapUid(uid);
    }

    private int mapIsolatedUid(int uid) {
        return this.mPowerStatsUidResolver.mapUid(uid);
    }

    public void noteEventLocked(int code, java.lang.String name, int uid, long elapsedRealtimeMs, long uptimeMs) {
        int uid2 = mapUid(uid);
        if (!this.mActiveEvents.updateState(code, name, uid2, 0)) {
            return;
        }
        this.mHistory.recordEvent(elapsedRealtimeMs, uptimeMs, code, name, uid2);
        this.mBatteryStatsImplExt.addThermalForegroundApp(elapsedRealtimeMs, uptimeMs, name, uid2, code);
    }

    public void noteCurrentTimeChangedLocked(long currentTimeMs, long elapsedRealtimeMs, long uptimeMs) {
        this.mHistory.recordCurrentTimeChange(elapsedRealtimeMs, uptimeMs, currentTimeMs);
        adjustStartClockTime(currentTimeMs);
    }

    private void adjustStartClockTime(long currentTimeMs) {
        this.mStartClockTimeMs = currentTimeMs - (this.mClock.elapsedRealtime() - (this.mRealtimeStartUs / 1000));
    }

    public void noteProcessStartLocked(java.lang.String name, int uid, long elapsedRealtimeMs, long uptimeMs) {
        int uid2 = mapUid(uid);
        if (isOnBattery()) {
            com.android.server.power.stats.BatteryStatsImpl.Uid u = getUidStatsLocked(uid2, elapsedRealtimeMs, uptimeMs);
            u.getProcessStatsLocked(name).incStartsLocked();
        }
        if (!this.mActiveEvents.updateState(com.android.server.display.IOplusDisplayPowerControllerExt.ADJUSTMENT_GALLERY_OUT, name, uid2, 0) || !this.mRecordAllHistory) {
            return;
        }
        this.mHistory.recordEvent(elapsedRealtimeMs, uptimeMs, com.android.server.display.IOplusDisplayPowerControllerExt.ADJUSTMENT_GALLERY_OUT, name, uid2);
        this.mBatteryStatsImplExt.addThermalForegroundApp(elapsedRealtimeMs, uptimeMs, name, uid2, -1);
    }

    public void noteProcessCrashLocked(java.lang.String name, int uid, long elapsedRealtimeMs, long uptimeMs) {
        int uid2 = mapUid(uid);
        if (isOnBattery()) {
            com.android.server.power.stats.BatteryStatsImpl.Uid u = getUidStatsLocked(uid2, elapsedRealtimeMs, uptimeMs);
            u.getProcessStatsLocked(name).incNumCrashesLocked();
        }
    }

    public void noteProcessAnrLocked(java.lang.String name, int uid, long elapsedRealtimeMs, long uptimeMs) {
        int uid2 = mapUid(uid);
        if (isOnBattery()) {
            com.android.server.power.stats.BatteryStatsImpl.Uid u = getUidStatsLocked(uid2, elapsedRealtimeMs, uptimeMs);
            u.getProcessStatsLocked(name).incNumAnrsLocked();
        }
    }

    public void noteUidProcessStateLocked(int uid, int state) {
        noteUidProcessStateLocked(uid, state, this.mClock.elapsedRealtime(), this.mClock.uptimeMillis());
    }

    public void noteUidProcessStateLocked(int uid, int state, long elapsedRealtimeMs, long uptimeMs) {
        int parentUid = mapUid(uid);
        if (uid == parentUid || !android.os.Process.isIsolated(uid)) {
            this.mFrameworkStatsLogger.uidProcessStateChanged(uid, state);
            getUidStatsLocked(parentUid, elapsedRealtimeMs, uptimeMs).updateUidProcessStateLocked(state, elapsedRealtimeMs, uptimeMs);
        }
    }

    public void noteProcessFinishLocked(java.lang.String name, int uid, long elapsedRealtimeMs, long uptimeMs) {
        int uid2 = mapUid(uid);
        if (!this.mActiveEvents.updateState(com.android.server.display.IOplusDisplayPowerControllerExt.ADJUSTMENT_GALLERY_IN, name, uid2, 0) || !this.mRecordAllHistory) {
            return;
        }
        this.mHistory.recordEvent(elapsedRealtimeMs, uptimeMs, com.android.server.display.IOplusDisplayPowerControllerExt.ADJUSTMENT_GALLERY_IN, name, uid2);
    }

    public void noteSyncStartLocked(java.lang.String name, int uid) {
        noteSyncStartLocked(name, uid, this.mClock.elapsedRealtime(), this.mClock.uptimeMillis());
    }

    public void noteSyncStartLocked(java.lang.String name, int uid, long elapsedRealtimeMs, long uptimeMs) {
        int uid2 = mapUid(uid);
        getUidStatsLocked(uid2, elapsedRealtimeMs, uptimeMs).noteStartSyncLocked(name, elapsedRealtimeMs);
        if (!this.mActiveEvents.updateState(32772, name, uid2, 0)) {
            return;
        }
        this.mHistory.recordEvent(elapsedRealtimeMs, uptimeMs, 32772, name, uid2);
        this.mBatteryStatsImplExt.addThermalnetSyncProc(elapsedRealtimeMs, uptimeMs, name);
    }

    public void noteSyncFinishLocked(java.lang.String name, int uid) {
        noteSyncFinishLocked(name, uid, this.mClock.elapsedRealtime(), this.mClock.uptimeMillis());
    }

    public void noteSyncFinishLocked(java.lang.String name, int uid, long elapsedRealtimeMs, long uptimeMs) {
        int uid2 = mapUid(uid);
        getUidStatsLocked(uid2, elapsedRealtimeMs, uptimeMs).noteStopSyncLocked(name, elapsedRealtimeMs);
        if (!this.mActiveEvents.updateState(16388, name, uid2, 0)) {
            return;
        }
        this.mHistory.recordEvent(elapsedRealtimeMs, uptimeMs, 16388, name, uid2);
        this.mBatteryStatsImplExt.addThermalnetSyncProc(elapsedRealtimeMs, uptimeMs, "null");
    }

    public void noteJobStartLocked(java.lang.String name, int uid) {
        noteJobStartLocked(name, uid, this.mClock.elapsedRealtime(), this.mClock.uptimeMillis());
    }

    public void noteJobStartLocked(java.lang.String name, int uid, long elapsedRealtimeMs, long uptimeMs) {
        int uid2 = mapUid(uid);
        getUidStatsLocked(uid2, elapsedRealtimeMs, uptimeMs).noteStartJobLocked(name, elapsedRealtimeMs);
        if (!this.mActiveEvents.updateState(32774, name, uid2, 0)) {
            return;
        }
        this.mHistory.recordEvent(elapsedRealtimeMs, uptimeMs, 32774, name, uid2);
        this.mBatteryStatsImplExt.addThermalJobProc(elapsedRealtimeMs, uptimeMs, name);
    }

    public void noteJobFinishLocked(java.lang.String name, int uid, int stopReason) {
        noteJobFinishLocked(name, uid, stopReason, this.mClock.elapsedRealtime(), this.mClock.uptimeMillis());
    }

    public void noteJobFinishLocked(java.lang.String name, int uid, int stopReason, long elapsedRealtimeMs, long uptimeMs) {
        int uid2 = mapUid(uid);
        getUidStatsLocked(uid2, elapsedRealtimeMs, uptimeMs).noteStopJobLocked(name, elapsedRealtimeMs, stopReason);
        if (!this.mActiveEvents.updateState(16390, name, uid2, 0)) {
            return;
        }
        this.mHistory.recordEvent(elapsedRealtimeMs, uptimeMs, 16390, name, uid2);
        this.mBatteryStatsImplExt.addThermalJobProc(elapsedRealtimeMs, uptimeMs, "null");
    }

    public void noteJobsDeferredLocked(int uid, int numDeferred, long sinceLast, long elapsedRealtimeMs, long uptimeMs) {
        getUidStatsLocked(mapUid(uid), elapsedRealtimeMs, uptimeMs).noteJobsDeferredLocked(numDeferred, sinceLast);
    }

    public void noteAlarmStartLocked(java.lang.String name, android.os.WorkSource workSource, int uid) {
        noteAlarmStartLocked(name, workSource, uid, this.mClock.elapsedRealtime(), this.mClock.uptimeMillis());
    }

    public void noteAlarmStartLocked(java.lang.String name, android.os.WorkSource workSource, int uid, long elapsedRealtimeMs, long uptimeMs) {
        noteAlarmStartOrFinishLocked(32781, name, workSource, uid, elapsedRealtimeMs, uptimeMs);
    }

    public void noteAlarmFinishLocked(java.lang.String name, android.os.WorkSource workSource, int uid) {
        noteAlarmFinishLocked(name, workSource, uid, this.mClock.elapsedRealtime(), this.mClock.uptimeMillis());
    }

    public void noteAlarmFinishLocked(java.lang.String name, android.os.WorkSource workSource, int uid, long elapsedRealtimeMs, long uptimeMs) {
        noteAlarmStartOrFinishLocked(16397, name, workSource, uid, elapsedRealtimeMs, uptimeMs);
    }

    private void noteAlarmStartOrFinishLocked(int historyItem, java.lang.String name, android.os.WorkSource workSource, int uid, long elapsedRealtimeMs, long uptimeMs) {
        if (this.mRecordAllHistory) {
            if (workSource == null) {
                int uid2 = mapUid(uid);
                if (this.mActiveEvents.updateState(historyItem, name, uid2, 0)) {
                    this.mHistory.recordEvent(elapsedRealtimeMs, uptimeMs, historyItem, name, uid2);
                }
                return;
            }
            for (int i = 0; i < workSource.size(); i++) {
                int uid3 = mapUid(workSource.getUid(i));
                if (this.mActiveEvents.updateState(historyItem, name, uid3, 0)) {
                    this.mHistory.recordEvent(elapsedRealtimeMs, uptimeMs, historyItem, name, uid3);
                }
            }
            java.util.List<android.os.WorkSource.WorkChain> workChains = workSource.getWorkChains();
            if (workChains != null) {
                for (int i2 = 0; i2 < workChains.size(); i2++) {
                    int uid4 = mapUid(workChains.get(i2).getAttributionUid());
                    if (this.mActiveEvents.updateState(historyItem, name, uid4, 0)) {
                        this.mHistory.recordEvent(elapsedRealtimeMs, uptimeMs, historyItem, name, uid4);
                    }
                }
            }
        }
    }

    public void noteWakupAlarmLocked(java.lang.String packageName, int uid, android.os.WorkSource workSource, java.lang.String tag) {
        noteWakupAlarmLocked(packageName, uid, workSource, tag, this.mClock.elapsedRealtime(), this.mClock.uptimeMillis());
    }

    public void noteWakupAlarmLocked(java.lang.String packageName, int uid, android.os.WorkSource workSource, java.lang.String tag, long elapsedRealtimeMs, long uptimeMs) {
        if (workSource != null) {
            for (int i = 0; i < workSource.size(); i++) {
                int uid2 = workSource.getUid(i);
                java.lang.String workSourceName = workSource.getPackageName(i);
                if (isOnBattery()) {
                    com.android.server.power.stats.BatteryStatsImpl.Uid.Pkg pkg = getPackageStatsLocked(uid2, workSourceName != null ? workSourceName : packageName, elapsedRealtimeMs, uptimeMs);
                    pkg.noteWakeupAlarmLocked(tag);
                }
            }
            java.util.List<android.os.WorkSource.WorkChain> workChains = workSource.getWorkChains();
            if (workChains != null) {
                for (int i2 = 0; i2 < workChains.size(); i2++) {
                    android.os.WorkSource.WorkChain wc = workChains.get(i2);
                    int uid3 = wc.getAttributionUid();
                    if (isOnBattery()) {
                        com.android.server.power.stats.BatteryStatsImpl.Uid.Pkg pkg2 = getPackageStatsLocked(uid3, packageName, elapsedRealtimeMs, uptimeMs);
                        pkg2.noteWakeupAlarmLocked(tag);
                    }
                }
                return;
            }
            return;
        }
        if (isOnBattery()) {
            com.android.server.power.stats.BatteryStatsImpl.Uid.Pkg pkg3 = getPackageStatsLocked(uid, packageName, elapsedRealtimeMs, uptimeMs);
            pkg3.noteWakeupAlarmLocked(tag);
        }
    }

    private void requestWakelockCpuUpdate() {
        this.mExternalSync.scheduleCpuSyncDueToWakelockChange(60000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void requestImmediateCpuUpdate() {
        this.mExternalSync.scheduleCpuSyncDueToWakelockChange(0L);
    }

    public void setRecordAllHistoryLocked(boolean enabled) {
        this.mRecordAllHistory = enabled;
        if (enabled) {
            java.util.HashMap<java.lang.String, android.util.SparseIntArray> active = this.mActiveEvents.getStateForEvent(1);
            if (active != null) {
                long mSecRealtime = this.mClock.elapsedRealtime();
                long mSecUptime = this.mClock.uptimeMillis();
                for (java.util.Map.Entry<java.lang.String, android.util.SparseIntArray> ent : active.entrySet()) {
                    int j = 0;
                    for (android.util.SparseIntArray uids = ent.getValue(); j < uids.size(); uids = uids) {
                        this.mHistory.recordEvent(mSecRealtime, mSecUptime, com.android.server.display.IOplusDisplayPowerControllerExt.ADJUSTMENT_GALLERY_OUT, ent.getKey(), uids.keyAt(j));
                        j++;
                    }
                }
                return;
            }
            return;
        }
        this.mActiveEvents.removeEvents(5);
        this.mActiveEvents.removeEvents(13);
        java.util.HashMap<java.lang.String, android.util.SparseIntArray> active2 = this.mActiveEvents.getStateForEvent(1);
        if (active2 != null) {
            long mSecRealtime2 = this.mClock.elapsedRealtime();
            long mSecUptime2 = this.mClock.uptimeMillis();
            for (java.util.Map.Entry<java.lang.String, android.util.SparseIntArray> ent2 : active2.entrySet()) {
                int j2 = 0;
                for (android.util.SparseIntArray uids2 = ent2.getValue(); j2 < uids2.size(); uids2 = uids2) {
                    this.mHistory.recordEvent(mSecRealtime2, mSecUptime2, com.android.server.display.IOplusDisplayPowerControllerExt.ADJUSTMENT_GALLERY_IN, ent2.getKey(), uids2.keyAt(j2));
                    j2++;
                }
            }
        }
    }

    public void setNoAutoReset(boolean enabled) {
        this.mNoAutoReset = enabled;
    }

    public void setPretendScreenOff(boolean pretendScreenOff) {
        if (this.mPretendScreenOff != pretendScreenOff) {
            this.mPretendScreenOff = pretendScreenOff;
            int primaryScreenState = this.mPerDisplayBatteryStats[0].screenState;
            noteScreenStateLocked(0, primaryScreenState, this.mClock.elapsedRealtime(), this.mClock.uptimeMillis(), this.mClock.currentTimeMillis());
        }
    }

    public void noteStartWakeLocked(int uid, int pid, android.os.WorkSource.WorkChain wc, java.lang.String name, java.lang.String historyName, int type, boolean unimportantForLogging) {
        noteStartWakeLocked(uid, pid, wc, name, historyName, type, unimportantForLogging, this.mClock.elapsedRealtime(), this.mClock.uptimeMillis());
    }

    public void noteStartWakeLocked(int uid, int pid, android.os.WorkSource.WorkChain wc, java.lang.String name, java.lang.String historyName, int type, boolean unimportantForLogging, long elapsedRealtimeMs, long uptimeMs) {
        java.lang.String historyName2;
        int mappedUid = mapUid(uid);
        if (type == 0) {
            if (historyName != null) {
                historyName2 = historyName;
            } else {
                historyName2 = name;
            }
            if (this.mRecordAllHistory && this.mActiveEvents.updateState(32773, historyName2, mappedUid, 0)) {
                this.mHistory.recordEvent(elapsedRealtimeMs, uptimeMs, 32773, historyName2, mappedUid);
            }
            if (this.mWakeLockNesting == 0) {
                this.mWakeLockImportant = !unimportantForLogging;
                this.mHistory.recordWakelockStartEvent(elapsedRealtimeMs, uptimeMs, historyName2, mappedUid);
            } else if (!this.mWakeLockImportant && !unimportantForLogging && this.mHistory.maybeUpdateWakelockTag(elapsedRealtimeMs, uptimeMs, historyName2, mappedUid)) {
                this.mWakeLockImportant = true;
            }
            this.mWakeLockNesting++;
        }
        if (mappedUid >= 0) {
            if (mappedUid != uid) {
                this.mPowerStatsUidResolver.retainIsolatedUid(uid);
            }
            if (this.mOnBatteryScreenOffTimeBase.isRunning()) {
                requestWakelockCpuUpdate();
            }
            com.android.server.power.stats.BatteryStatsImpl.Uid uidStats = getUidStatsLocked(mappedUid, elapsedRealtimeMs, uptimeMs);
            uidStats.noteStartWakeLocked(pid, name, type, elapsedRealtimeMs);
            this.mFrameworkStatsLogger.wakelockStateChanged(mapIsolatedUid(uid), wc, name, uidStats.mProcessState, true, getPowerManagerWakeLockLevel(type));
        }
    }

    public void noteStopWakeLocked(int uid, int pid, android.os.WorkSource.WorkChain wc, java.lang.String name, java.lang.String historyName, int type) {
        noteStopWakeLocked(uid, pid, wc, name, historyName, type, this.mClock.elapsedRealtime(), this.mClock.uptimeMillis());
    }

    public void noteStopWakeLocked(int uid, int pid, android.os.WorkSource.WorkChain wc, java.lang.String name, java.lang.String historyName, int type, long elapsedRealtimeMs, long uptimeMs) {
        java.lang.String historyName2;
        int mappedUid = mapUid(uid);
        if (type == 0) {
            this.mWakeLockNesting--;
            if (historyName != null) {
                historyName2 = historyName;
            } else {
                historyName2 = name;
            }
            if (this.mRecordAllHistory && this.mActiveEvents.updateState(16389, historyName2, mappedUid, 0)) {
                this.mHistory.recordEvent(elapsedRealtimeMs, uptimeMs, 16389, historyName2, mappedUid);
            }
            if (this.mWakeLockNesting == 0) {
                this.mHistory.recordWakelockStopEvent(elapsedRealtimeMs, uptimeMs, historyName2, mappedUid);
            }
        }
        if (mappedUid >= 0) {
            if (this.mOnBatteryScreenOffTimeBase.isRunning()) {
                requestWakelockCpuUpdate();
            }
            com.android.server.power.stats.BatteryStatsImpl.Uid uidStats = getUidStatsLocked(mappedUid, elapsedRealtimeMs, uptimeMs);
            uidStats.noteStopWakeLocked(pid, name, type, elapsedRealtimeMs);
            this.mFrameworkStatsLogger.wakelockStateChanged(mapIsolatedUid(uid), wc, name, uidStats.mProcessState, false, getPowerManagerWakeLockLevel(type));
            if (mappedUid != uid) {
                releaseIsolatedUidLocked(uid, elapsedRealtimeMs, uptimeMs);
            }
        }
    }

    private int getPowerManagerWakeLockLevel(int batteryStatsWakelockType) {
        switch (batteryStatsWakelockType) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                android.util.Slog.e(TAG, "Illegal window wakelock type observed in batterystats.");
                break;
            case 18:
                break;
            default:
                android.util.Slog.e(TAG, "Illegal wakelock type in batterystats: " + batteryStatsWakelockType);
                break;
        }
        return -1;
    }

    public void noteStartWakeFromSourceLocked(android.os.WorkSource ws, int pid, java.lang.String name, java.lang.String historyName, int type, boolean unimportantForLogging, long elapsedRealtimeMs, long uptimeMs) {
        int N = ws.size();
        for (int i = 0; i < N; i++) {
            noteStartWakeLocked(ws.getUid(i), pid, null, name, historyName, type, unimportantForLogging, elapsedRealtimeMs, uptimeMs);
        }
        java.util.List<android.os.WorkSource.WorkChain> wcs = ws.getWorkChains();
        if (wcs != null) {
            for (int i2 = 0; i2 < wcs.size(); i2++) {
                android.os.WorkSource.WorkChain wc = wcs.get(i2);
                noteStartWakeLocked(wc.getAttributionUid(), pid, wc, name, historyName, type, unimportantForLogging, elapsedRealtimeMs, uptimeMs);
            }
        }
    }

    public void noteChangeWakelockFromSourceLocked(android.os.WorkSource ws, int pid, java.lang.String name, java.lang.String historyName, int type, android.os.WorkSource newWs, int newPid, java.lang.String newName, java.lang.String newHistoryName, int newType, boolean newUnimportantForLogging, long elapsedRealtimeMs, long uptimeMs) {
        java.util.List<android.os.WorkSource.WorkChain> goneChains;
        java.util.List<android.os.WorkSource.WorkChain> newChains;
        java.util.List<android.os.WorkSource.WorkChain>[] wcs = android.os.WorkSource.diffChains(ws, newWs);
        int NN = newWs.size();
        for (int i = 0; i < NN; i++) {
            noteStartWakeLocked(newWs.getUid(i), newPid, null, newName, newHistoryName, newType, newUnimportantForLogging, elapsedRealtimeMs, uptimeMs);
        }
        if (wcs != null && (newChains = wcs[0]) != null) {
            for (int i2 = 0; i2 < newChains.size(); i2++) {
                android.os.WorkSource.WorkChain newChain = newChains.get(i2);
                noteStartWakeLocked(newChain.getAttributionUid(), newPid, newChain, newName, newHistoryName, newType, newUnimportantForLogging, elapsedRealtimeMs, uptimeMs);
            }
        }
        int NO = ws.size();
        for (int i3 = 0; i3 < NO; i3++) {
            noteStopWakeLocked(ws.getUid(i3), pid, null, name, historyName, type, elapsedRealtimeMs, uptimeMs);
        }
        if (wcs != null && (goneChains = wcs[1]) != null) {
            for (int i4 = 0; i4 < goneChains.size(); i4++) {
                android.os.WorkSource.WorkChain goneChain = goneChains.get(i4);
                noteStopWakeLocked(goneChain.getAttributionUid(), pid, goneChain, name, historyName, type, elapsedRealtimeMs, uptimeMs);
            }
        }
    }

    public void noteStopWakeFromSourceLocked(android.os.WorkSource ws, int pid, java.lang.String name, java.lang.String historyName, int type, long elapsedRealtimeMs, long uptimeMs) {
        int N = ws.size();
        for (int i = 0; i < N; i++) {
            noteStopWakeLocked(ws.getUid(i), pid, null, name, historyName, type, elapsedRealtimeMs, uptimeMs);
        }
        java.util.List<android.os.WorkSource.WorkChain> wcs = ws.getWorkChains();
        if (wcs != null) {
            for (int i2 = 0; i2 < wcs.size(); i2++) {
                android.os.WorkSource.WorkChain wc = wcs.get(i2);
                noteStopWakeLocked(wc.getAttributionUid(), pid, wc, name, historyName, type, elapsedRealtimeMs, uptimeMs);
            }
        }
    }

    public void noteLongPartialWakelockStart(java.lang.String name, java.lang.String historyName, int uid) {
        noteLongPartialWakelockStart(name, historyName, uid, this.mClock.elapsedRealtime(), this.mClock.uptimeMillis());
    }

    public void noteLongPartialWakelockStart(java.lang.String name, java.lang.String historyName, int uid, long elapsedRealtimeMs, long uptimeMs) {
        noteLongPartialWakeLockStartInternal(name, historyName, uid, elapsedRealtimeMs, uptimeMs);
    }

    public void noteLongPartialWakelockStartFromSource(java.lang.String name, java.lang.String historyName, android.os.WorkSource workSource, long elapsedRealtimeMs, long uptimeMs) {
        int N = workSource.size();
        for (int i = 0; i < N; i++) {
            int uid = mapUid(workSource.getUid(i));
            noteLongPartialWakeLockStartInternal(name, historyName, uid, elapsedRealtimeMs, uptimeMs);
        }
        java.util.List<android.os.WorkSource.WorkChain> workChains = workSource.getWorkChains();
        if (workChains != null) {
            for (int i2 = 0; i2 < workChains.size(); i2++) {
                android.os.WorkSource.WorkChain workChain = workChains.get(i2);
                int uid2 = workChain.getAttributionUid();
                noteLongPartialWakeLockStartInternal(name, historyName, uid2, elapsedRealtimeMs, uptimeMs);
            }
        }
    }

    private void noteLongPartialWakeLockStartInternal(java.lang.String name, java.lang.String historyName, int uid, long elapsedRealtimeMs, long uptimeMs) {
        java.lang.String historyName2;
        int mappedUid = mapUid(uid);
        if (historyName != null) {
            historyName2 = historyName;
        } else {
            historyName2 = name;
        }
        if (!this.mActiveEvents.updateState(32788, historyName2, mappedUid, 0)) {
            return;
        }
        this.mHistory.recordEvent(elapsedRealtimeMs, uptimeMs, 32788, historyName2, mappedUid);
        if (mappedUid != uid) {
            this.mPowerStatsUidResolver.retainIsolatedUid(uid);
        }
    }

    public void noteLongPartialWakelockFinish(java.lang.String name, java.lang.String historyName, int uid) {
        noteLongPartialWakelockFinish(name, historyName, uid, this.mClock.elapsedRealtime(), this.mClock.uptimeMillis());
    }

    public void noteLongPartialWakelockFinish(java.lang.String name, java.lang.String historyName, int uid, long elapsedRealtimeMs, long uptimeMs) {
        noteLongPartialWakeLockFinishInternal(name, historyName, uid, elapsedRealtimeMs, uptimeMs);
    }

    public void noteLongPartialWakelockFinishFromSource(java.lang.String name, java.lang.String historyName, android.os.WorkSource workSource, long elapsedRealtimeMs, long uptimeMs) {
        int N = workSource.size();
        for (int i = 0; i < N; i++) {
            int uid = mapUid(workSource.getUid(i));
            noteLongPartialWakeLockFinishInternal(name, historyName, uid, elapsedRealtimeMs, uptimeMs);
        }
        java.util.List<android.os.WorkSource.WorkChain> workChains = workSource.getWorkChains();
        if (workChains != null) {
            for (int i2 = 0; i2 < workChains.size(); i2++) {
                android.os.WorkSource.WorkChain workChain = workChains.get(i2);
                int uid2 = workChain.getAttributionUid();
                noteLongPartialWakeLockFinishInternal(name, historyName, uid2, elapsedRealtimeMs, uptimeMs);
            }
        }
    }

    private void noteLongPartialWakeLockFinishInternal(java.lang.String name, java.lang.String historyName, int uid, long elapsedRealtimeMs, long uptimeMs) {
        java.lang.String historyName2;
        int mappedUid = mapUid(uid);
        if (historyName != null) {
            historyName2 = historyName;
        } else {
            historyName2 = name;
        }
        if (!this.mActiveEvents.updateState(16404, historyName2, mappedUid, 0)) {
            return;
        }
        this.mHistory.recordEvent(elapsedRealtimeMs, uptimeMs, 16404, historyName2, mappedUid);
        if (mappedUid != uid) {
            releaseIsolatedUidLocked(uid, elapsedRealtimeMs, uptimeMs);
        }
    }

    public void noteWakeupReasonLocked(java.lang.String reason, long elapsedRealtimeMs, long uptimeMs) {
        if (this.mLastWakeupReason != null) {
            long deltaUptimeMs = uptimeMs - this.mLastWakeupUptimeMs;
            com.android.server.power.stats.BatteryStatsImpl.SamplingTimer timer = getWakeupReasonTimerLocked(this.mLastWakeupReason);
            timer.add(deltaUptimeMs * 1000, 1, elapsedRealtimeMs);
            this.mFrameworkStatsLogger.kernelWakeupReported(deltaUptimeMs * 1000, this.mLastWakeupReason, this.mLastWakeupElapsedTimeMs);
        }
        this.mHistory.recordWakeupEvent(elapsedRealtimeMs, uptimeMs, reason);
        this.mLastWakeupReason = reason;
        this.mLastWakeupUptimeMs = uptimeMs;
        this.mLastWakeupElapsedTimeMs = elapsedRealtimeMs;
    }

    public boolean startAddingCpuStatsLocked() {
        this.mExternalSync.cancelCpuSyncDueToWakelockChange();
        return this.mOnBatteryInternal;
    }

    public void addCpuStatsLocked(int totalUTimeMs, int totalSTimeMs, int statUserTimeMs, int statSystemTimeMs, int statIOWaitTimeMs, int statIrqTimeMs, int statSoftIrqTimeMs, int statIdleTimeMs) {
        this.mStepDetailsCalculator.addCpuStats(totalUTimeMs, totalSTimeMs, statUserTimeMs, statSystemTimeMs, statIOWaitTimeMs, statIrqTimeMs, statSoftIrqTimeMs, statIdleTimeMs);
    }

    public void finishAddingCpuStatsLocked() {
        this.mStepDetailsCalculator.finishAddingCpuLocked();
    }

    public void noteProcessDiedLocked(int uid, int pid) {
        com.android.server.power.stats.BatteryStatsImpl.Uid u = this.mUidStats.get(mapUid(uid));
        if (u != null) {
            u.mPids.remove(pid);
        }
    }

    public void reportExcessiveCpuLocked(int uid, java.lang.String proc, long overTimeMs, long usedTimeMs) {
        com.android.server.power.stats.BatteryStatsImpl.Uid u = this.mUidStats.get(mapUid(uid));
        if (u != null) {
            u.reportExcessiveCpuLocked(proc, overTimeMs, usedTimeMs);
        }
    }

    public void noteStartSensorLocked(int uid, int sensor) {
        noteStartSensorLocked(uid, sensor, this.mClock.elapsedRealtime(), this.mClock.uptimeMillis());
    }

    public void noteStartSensorLocked(int uid, int sensor, long elapsedRealtimeMs, long uptimeMs) {
        int uid2 = mapUid(uid);
        if (this.mSensorNesting == 0) {
            this.mHistory.recordStateStartEvent(elapsedRealtimeMs, uptimeMs, 8388608);
        }
        this.mSensorNesting++;
        getUidStatsLocked(uid2, elapsedRealtimeMs, uptimeMs).noteStartSensor(sensor, elapsedRealtimeMs);
    }

    public void noteStopSensorLocked(int uid, int sensor) {
        noteStopSensorLocked(uid, sensor, this.mClock.elapsedRealtime(), this.mClock.uptimeMillis());
    }

    public void noteStopSensorLocked(int uid, int sensor, long elapsedRealtimeMs, long uptimeMs) {
        int uid2 = mapUid(uid);
        this.mSensorNesting--;
        if (this.mSensorNesting == 0) {
            this.mHistory.recordStateStopEvent(elapsedRealtimeMs, uptimeMs, 8388608);
        }
        getUidStatsLocked(uid2, elapsedRealtimeMs, uptimeMs).noteStopSensor(sensor, elapsedRealtimeMs);
    }

    public void noteGpsChangedLocked(android.os.WorkSource oldWs, android.os.WorkSource newWs) {
        noteGpsChangedLocked(oldWs, newWs, this.mClock.elapsedRealtime(), this.mClock.uptimeMillis());
    }

    public void noteGpsChangedLocked(android.os.WorkSource oldWs, android.os.WorkSource newWs, long elapsedRealtimeMs, long uptimeMs) {
        for (int i = 0; i < newWs.size(); i++) {
            noteStartGpsLocked(newWs.getUid(i), null, elapsedRealtimeMs, uptimeMs);
        }
        for (int i2 = 0; i2 < oldWs.size(); i2++) {
            noteStopGpsLocked(oldWs.getUid(i2), null, elapsedRealtimeMs, uptimeMs);
        }
        java.util.List<android.os.WorkSource.WorkChain>[] wcs = android.os.WorkSource.diffChains(oldWs, newWs);
        if (wcs != null) {
            if (wcs[0] != null) {
                java.util.List<android.os.WorkSource.WorkChain> newChains = wcs[0];
                for (int i3 = 0; i3 < newChains.size(); i3++) {
                    noteStartGpsLocked(-1, newChains.get(i3), elapsedRealtimeMs, uptimeMs);
                }
            }
            if (wcs[1] != null) {
                java.util.List<android.os.WorkSource.WorkChain> goneChains = wcs[1];
                for (int i4 = 0; i4 < goneChains.size(); i4++) {
                    noteStopGpsLocked(-1, goneChains.get(i4), elapsedRealtimeMs, uptimeMs);
                }
            }
        }
    }

    private void noteStartGpsLocked(int uid, android.os.WorkSource.WorkChain workChain, long elapsedRealtimeMs, long uptimeMs) {
        if (workChain != null) {
            uid = workChain.getAttributionUid();
        }
        int mappedUid = mapUid(uid);
        if (this.mGpsNesting == 0) {
            this.mHistory.recordStateStartEvent(elapsedRealtimeMs, uptimeMs, 536870912, uid, "gnss");
            if (this.mPowerStatsCollectorEnabled.get(10)) {
                this.mGnssPowerStatsCollector.schedule();
            }
            this.mBatteryStatsImplExt.addThermalOnOffEvent(5, elapsedRealtimeMs, uptimeMs, false);
        }
        int FLAG_GPS = this.mGpsNesting;
        this.mGpsNesting = FLAG_GPS + 1;
        this.mFrameworkStatsLogger.gpsScanStateChanged(mapIsolatedUid(uid), workChain, true);
        getUidStatsLocked(mappedUid, elapsedRealtimeMs, uptimeMs).noteStartGps(elapsedRealtimeMs);
    }

    private void noteStopGpsLocked(int uid, android.os.WorkSource.WorkChain workChain, long elapsedRealtimeMs, long uptimeMs) {
        int uid2;
        long j;
        int uid3;
        if (workChain == null) {
            uid2 = uid;
        } else {
            uid2 = workChain.getAttributionUid();
        }
        int mappedUid = mapUid(uid2);
        this.mGpsNesting--;
        if (this.mGpsNesting != 0) {
            j = elapsedRealtimeMs;
            uid3 = uid2;
        } else {
            this.mHistory.recordStateStopEvent(elapsedRealtimeMs, uptimeMs, 536870912, uid2, "gnss");
            this.mHistory.recordGpsSignalQualityEvent(elapsedRealtimeMs, uptimeMs, 2);
            stopAllGpsSignalQualityTimersLocked(-1, elapsedRealtimeMs);
            this.mGpsSignalQualityBin = -1;
            uid3 = uid2;
            j = elapsedRealtimeMs;
            this.mBatteryStatsImplExt.addThermalOnOffEvent(5, elapsedRealtimeMs, uptimeMs, false);
            if (this.mPowerStatsCollectorEnabled.get(10)) {
                this.mGnssPowerStatsCollector.schedule();
            }
        }
        this.mFrameworkStatsLogger.gpsScanStateChanged(mapIsolatedUid(uid3), workChain, false);
        getUidStatsLocked(mappedUid, elapsedRealtimeMs, uptimeMs).noteStopGps(j);
    }

    public void noteGpsSignalQualityLocked(int signalLevel, long elapsedRealtimeMs, long uptimeMs) {
        if (this.mGpsNesting == 0) {
            return;
        }
        if (signalLevel < 0 || signalLevel >= this.mGpsSignalQualityTimer.length) {
            stopAllGpsSignalQualityTimersLocked(-1, elapsedRealtimeMs);
            return;
        }
        if (this.mGpsSignalQualityBin != signalLevel) {
            if (this.mGpsSignalQualityBin >= 0) {
                this.mGpsSignalQualityTimer[this.mGpsSignalQualityBin].stopRunningLocked(elapsedRealtimeMs);
            }
            if (!this.mGpsSignalQualityTimer[signalLevel].isRunningLocked()) {
                this.mGpsSignalQualityTimer[signalLevel].startRunningLocked(elapsedRealtimeMs);
            }
            this.mHistory.recordGpsSignalQualityEvent(elapsedRealtimeMs, uptimeMs, signalLevel);
            this.mGpsSignalQualityBin = signalLevel;
        }
    }

    public void noteScreenStateLocked(int display, int state) {
        noteScreenStateLocked(display, state, this.mClock.elapsedRealtime(), this.mClock.uptimeMillis(), this.mClock.currentTimeMillis());
    }

    /* JADX WARN: Removed duplicated region for block: B:121:0x021f  */
    /* JADX WARN: Removed duplicated region for block: B:124:0x023c  */
    /* JADX WARN: Removed duplicated region for block: B:125:0x0266  */
    /* JADX WARN: Removed duplicated region for block: B:130:0x029c  */
    /* JADX WARN: Removed duplicated region for block: B:131:0x02a4  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void noteScreenStateLocked(int r33, int r34, long r35, long r37, long r39) {
        /*
            Method dump skipped, instruction units count: 816
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.power.stats.BatteryStatsImpl.noteScreenStateLocked(int, int, long, long, long):void");
    }

    public void noteScreenBrightnessLocked(int display, int brightness) {
        noteScreenBrightnessLocked(display, brightness, this.mClock.elapsedRealtime(), this.mClock.uptimeMillis());
    }

    public void noteScreenBrightnessLocked(int display, int brightness, long elapsedRealtimeMs, long uptimeMs) {
        int bin;
        int overallBin;
        int MAX_BRIGHTNESS = android.os.SystemProperties.getInt("sys.oplus.multibrightness", 255);
        int bin2 = brightness / ((MAX_BRIGHTNESS + 1) / 5);
        if (bin2 < 0) {
            bin = 0;
        } else {
            if (bin2 >= 5) {
                bin2 = 4;
            }
            bin = bin2;
        }
        int numDisplays = this.mPerDisplayBatteryStats.length;
        if (display < 0 || display >= numDisplays) {
            android.util.Slog.wtf(TAG, "Unexpected note screen brightness for display " + display + " (only " + this.mPerDisplayBatteryStats.length + " displays exist...)");
            return;
        }
        com.android.server.power.stats.BatteryStatsImpl.DisplayBatteryStats displayStats = this.mPerDisplayBatteryStats[display];
        int oldBin = displayStats.screenBrightnessBin;
        if (oldBin == bin) {
            overallBin = this.mScreenBrightnessBin;
        } else {
            displayStats.screenBrightnessBin = bin;
            if (displayStats.screenState == 2) {
                if (oldBin >= 0) {
                    displayStats.screenBrightnessTimers[oldBin].stopRunningLocked(elapsedRealtimeMs);
                }
                displayStats.screenBrightnessTimers[bin].startRunningLocked(elapsedRealtimeMs);
            }
            overallBin = evaluateOverallScreenBrightnessBinLocked();
        }
        maybeUpdateOverallScreenBrightness(overallBin, elapsedRealtimeMs, uptimeMs);
    }

    private int evaluateOverallScreenBrightnessBinLocked() {
        int displayBrightnessBin;
        int overallBin = -1;
        int numDisplays = getDisplayCount();
        for (int display = 0; display < numDisplays; display++) {
            if (this.mPerDisplayBatteryStats[display].screenState == 2) {
                displayBrightnessBin = this.mPerDisplayBatteryStats[display].screenBrightnessBin;
            } else {
                displayBrightnessBin = -1;
            }
            if (displayBrightnessBin > overallBin) {
                overallBin = displayBrightnessBin;
            }
        }
        return overallBin;
    }

    private void maybeUpdateOverallScreenBrightness(int overallBin, long elapsedRealtimeMs, long uptimeMs) {
        if (this.mScreenBrightnessBin != overallBin) {
            if (overallBin >= 0) {
                this.mHistory.recordScreenBrightnessEvent(elapsedRealtimeMs, uptimeMs, overallBin);
            }
            this.mBatteryStatsImplExt.addThermalScreenBrightnessEvent(elapsedRealtimeMs, uptimeMs, overallBin, 7000);
            if (this.mScreenState == 2) {
                if (this.mScreenBrightnessBin >= 0) {
                    this.mScreenBrightnessTimer[this.mScreenBrightnessBin].stopRunningLocked(elapsedRealtimeMs);
                }
                if (overallBin >= 0) {
                    this.mScreenBrightnessTimer[overallBin].startRunningLocked(elapsedRealtimeMs);
                }
            }
            this.mScreenBrightnessBin = overallBin;
        }
    }

    public void noteUserActivityLocked(int uid, int event, long elapsedRealtimeMs, long uptimeMs) {
        if (this.mOnBatteryInternal) {
            getUidStatsLocked(mapUid(uid), elapsedRealtimeMs, uptimeMs).noteUserActivityLocked(event);
        }
    }

    public void noteWakeUpLocked(java.lang.String reason, int reasonUid, long elapsedRealtimeMs, long uptimeMs) {
        this.mHistory.recordEvent(elapsedRealtimeMs, uptimeMs, 18, reason, reasonUid);
    }

    public void noteInteractiveLocked(boolean interactive, long elapsedRealtimeMs) {
        if (this.mInteractive != interactive) {
            this.mInteractive = interactive;
            if (interactive) {
                this.mInteractiveTimer.startRunningLocked(elapsedRealtimeMs);
            } else {
                this.mInteractiveTimer.stopRunningLocked(elapsedRealtimeMs);
            }
        }
    }

    public void noteConnectivityChangedLocked(int type, java.lang.String extra, long elapsedRealtimeMs, long uptimeMs) {
        int[] result = this.mBatteryStatsExt.noteConnectivityChangedLocked(type, extra, this.mPhoneDataConnectionType, this.mModStepMode, this.mCurStepMode);
        this.mModStepMode |= result[0];
        this.mCurStepMode = result[1];
        this.mHistory.recordEvent(elapsedRealtimeMs, uptimeMs, 9, extra, type);
        this.mNumConnectivityChange++;
        this.mBatteryStatsImplExt.noteConnectivityChangedLocked(type, extra, elapsedRealtimeMs, uptimeMs);
    }

    private void noteMobileRadioApWakeupLocked(long elapsedRealtimeMillis, long uptimeMillis, int uid) {
        int uid2 = mapUid(uid);
        this.mHistory.recordEvent(elapsedRealtimeMillis, uptimeMillis, 19, "", uid2);
        getUidStatsLocked(uid2, elapsedRealtimeMillis, uptimeMillis).noteMobileRadioApWakeupLocked();
    }

    public boolean noteMobileRadioPowerStateLocked(int powerState, long timestampNs, int uid) {
        return noteMobileRadioPowerStateLocked(powerState, timestampNs, uid, this.mClock.elapsedRealtime(), this.mClock.uptimeMillis());
    }

    public boolean noteMobileRadioPowerStateLocked(int powerState, long timestampNs, int uid, long elapsedRealtimeMs, long uptimeMs) {
        long realElapsedRealtimeMs;
        long lastUpdateTimeMs;
        if (this.mMobileRadioPowerState != powerState) {
            boolean active = isActiveRadioPowerState(powerState);
            if (active) {
                if (uid > 0) {
                    noteMobileRadioApWakeupLocked(elapsedRealtimeMs, uptimeMs, uid);
                }
                long j = timestampNs / 1000000;
                lastUpdateTimeMs = j;
                this.mMobileRadioActiveStartTimeMs = j;
                this.mHistory.recordStateStartEvent(elapsedRealtimeMs, uptimeMs, 33554432);
                this.mBatteryStatsImplExt.addThermalNetState(elapsedRealtimeMs, uptimeMs, true);
            } else {
                long realElapsedRealtimeMs2 = timestampNs / 1000000;
                long lastUpdateTimeMs2 = this.mMobileRadioActiveStartTimeMs;
                if (realElapsedRealtimeMs2 < lastUpdateTimeMs2) {
                    android.util.Slog.wtf(TAG, "Data connection inactive timestamp " + realElapsedRealtimeMs2 + " is before start time " + lastUpdateTimeMs2);
                    realElapsedRealtimeMs = elapsedRealtimeMs;
                } else {
                    if (realElapsedRealtimeMs2 < elapsedRealtimeMs) {
                        this.mMobileRadioActiveAdjustedTime.addCountLocked(elapsedRealtimeMs - realElapsedRealtimeMs2);
                    }
                    realElapsedRealtimeMs = realElapsedRealtimeMs2;
                }
                this.mHistory.recordStateStopEvent(elapsedRealtimeMs, uptimeMs, 33554432);
                this.mBatteryStatsImplExt.addThermalNetState(elapsedRealtimeMs, uptimeMs, false);
                lastUpdateTimeMs = realElapsedRealtimeMs;
            }
            this.mMobileRadioPowerState = powerState;
            getRatBatteryStatsLocked(this.mActiveRat).noteActive(active, elapsedRealtimeMs);
            if (active) {
                this.mMobileRadioActiveTimer.startRunningLocked(elapsedRealtimeMs);
                this.mMobileRadioActivePerAppTimer.startRunningLocked(elapsedRealtimeMs);
                return false;
            }
            this.mMobileRadioActiveTimer.stopRunningLocked(lastUpdateTimeMs);
            this.mMobileRadioActivePerAppTimer.stopRunningLocked(lastUpdateTimeMs);
            if (this.mMobileRadioPowerStatsCollector.isEnabled()) {
                this.mMobileRadioPowerStatsCollector.schedule();
                return false;
            }
            if (this.mLastModemActivityInfo == null || elapsedRealtimeMs >= this.mLastModemActivityInfo.getTimestampMillis() + 600000) {
                this.mExternalSync.scheduleSync("modem-data", 4);
                return true;
            }
            return false;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isActiveRadioPowerState(int powerState) {
        return powerState == 2 || powerState == 3;
    }

    public void notePowerSaveModeLockedInit(boolean enabled, long elapsedRealtimeMs, long uptimeMs) {
        if (this.mPowerSaveModeEnabled != enabled) {
            notePowerSaveModeLocked(enabled, elapsedRealtimeMs, uptimeMs);
        } else {
            this.mFrameworkStatsLogger.batterySaverModeChanged(enabled);
        }
    }

    public void notePowerSaveModeLocked(boolean enabled, long elapsedRealtimeMs, long uptimeMs) {
        if (this.mPowerSaveModeEnabled != enabled) {
            int stepState = enabled ? 4 : 0;
            this.mModStepMode = ((4 & this.mCurStepMode) ^ stepState) | this.mModStepMode;
            this.mCurStepMode = (this.mCurStepMode & (-5)) | stepState;
            this.mPowerSaveModeEnabled = enabled;
            if (enabled) {
                this.mHistory.recordState2StartEvent(elapsedRealtimeMs, uptimeMs, Integer.MIN_VALUE);
                this.mPowerSaveModeEnabledTimer.startRunningLocked(elapsedRealtimeMs);
            } else {
                this.mHistory.recordState2StopEvent(elapsedRealtimeMs, uptimeMs, Integer.MIN_VALUE);
                this.mPowerSaveModeEnabledTimer.stopRunningLocked(elapsedRealtimeMs);
            }
            this.mFrameworkStatsLogger.batterySaverModeChanged(enabled);
        }
    }

    public void noteDeviceIdleModeLocked(int mode, java.lang.String activeReason, int activeUid, long elapsedRealtimeMs, long uptimeMs) {
        boolean nowLightIdling;
        boolean nowLightIdling2;
        int statsmode;
        boolean nowIdling = mode == 2;
        if (this.mDeviceIdling && !nowIdling && activeReason == null) {
            nowIdling = true;
        }
        boolean nowLightIdling3 = mode == 1;
        if (this.mDeviceLightIdling && !nowLightIdling3 && !nowIdling && activeReason == null) {
            nowLightIdling = true;
        } else {
            nowLightIdling = nowLightIdling3;
        }
        if (activeReason != null) {
            if (this.mDeviceIdling || this.mDeviceLightIdling) {
                nowLightIdling2 = nowLightIdling;
                this.mHistory.recordEvent(elapsedRealtimeMs, uptimeMs, 10, activeReason, activeUid);
            } else {
                nowLightIdling2 = nowLightIdling;
            }
        } else {
            nowLightIdling2 = nowLightIdling;
        }
        if (this.mDeviceIdling != nowIdling || this.mDeviceLightIdling != nowLightIdling2) {
            if (nowIdling) {
                statsmode = 2;
            } else {
                statsmode = nowLightIdling2 ? 1 : 0;
            }
            this.mFrameworkStatsLogger.deviceIdlingModeStateChanged(statsmode);
        }
        if (this.mDeviceIdling != nowIdling) {
            this.mDeviceIdling = nowIdling;
            int stepState = nowIdling ? 8 : 0;
            this.mModStepMode = ((8 & this.mCurStepMode) ^ stepState) | this.mModStepMode;
            this.mCurStepMode = (this.mCurStepMode & (-9)) | stepState;
            if (nowIdling) {
                this.mDeviceIdlingTimer.startRunningLocked(elapsedRealtimeMs);
            } else {
                this.mDeviceIdlingTimer.stopRunningLocked(elapsedRealtimeMs);
            }
        }
        if (this.mDeviceLightIdling != nowLightIdling2) {
            this.mDeviceLightIdling = nowLightIdling2;
            if (nowLightIdling2) {
                this.mDeviceLightIdlingTimer.startRunningLocked(elapsedRealtimeMs);
            } else {
                this.mDeviceLightIdlingTimer.stopRunningLocked(elapsedRealtimeMs);
            }
        }
        if (this.mDeviceIdleMode != mode) {
            this.mHistory.recordDeviceIdleEvent(elapsedRealtimeMs, uptimeMs, mode);
            long lastDuration = elapsedRealtimeMs - this.mLastIdleTimeStartMs;
            this.mLastIdleTimeStartMs = elapsedRealtimeMs;
            if (this.mDeviceIdleMode == 1) {
                if (lastDuration > this.mLongestLightIdleTimeMs) {
                    this.mLongestLightIdleTimeMs = lastDuration;
                }
                this.mDeviceIdleModeLightTimer.stopRunningLocked(elapsedRealtimeMs);
            } else if (this.mDeviceIdleMode == 2) {
                if (lastDuration > this.mLongestFullIdleTimeMs) {
                    this.mLongestFullIdleTimeMs = lastDuration;
                }
                this.mDeviceIdleModeFullTimer.stopRunningLocked(elapsedRealtimeMs);
            }
            if (mode == 1) {
                this.mDeviceIdleModeLightTimer.startRunningLocked(elapsedRealtimeMs);
            } else if (mode == 2) {
                this.mDeviceIdleModeFullTimer.startRunningLocked(elapsedRealtimeMs);
            }
            this.mDeviceIdleMode = mode;
            this.mFrameworkStatsLogger.deviceIdleModeStateChanged(mode);
        }
    }

    public void notePackageInstalledLocked(java.lang.String pkgName, long versionCode, long elapsedRealtimeMs, long uptimeMs) {
        this.mHistory.recordEvent(elapsedRealtimeMs, uptimeMs, 11, pkgName, (int) versionCode);
        android.os.BatteryStats.PackageChange pc = new android.os.BatteryStats.PackageChange();
        pc.mPackageName = pkgName;
        pc.mUpdate = true;
        pc.mVersionCode = versionCode;
        addPackageChange(pc);
    }

    public void notePackageUninstalledLocked(java.lang.String pkgName, long elapsedRealtimeMs, long uptimeMs) {
        this.mHistory.recordEvent(elapsedRealtimeMs, uptimeMs, 12, pkgName, 0);
        android.os.BatteryStats.PackageChange pc = new android.os.BatteryStats.PackageChange();
        pc.mPackageName = pkgName;
        pc.mUpdate = true;
        addPackageChange(pc);
    }

    private void addPackageChange(android.os.BatteryStats.PackageChange pc) {
        if (this.mDailyPackageChanges == null) {
            this.mDailyPackageChanges = new java.util.ArrayList<>();
        }
        this.mDailyPackageChanges.add(pc);
    }

    void stopAllGpsSignalQualityTimersLocked(int except, long elapsedRealtimeMs) {
        for (int i = 0; i < this.mGpsSignalQualityTimer.length; i++) {
            if (i != except) {
                while (this.mGpsSignalQualityTimer[i].isRunningLocked()) {
                    this.mGpsSignalQualityTimer[i].stopRunningLocked(elapsedRealtimeMs);
                }
            }
        }
    }

    public void notePhoneOnLocked(long elapsedRealtimeMs, long uptimeMs) {
        if (!this.mPhoneOn) {
            this.mHistory.recordState2StartEvent(elapsedRealtimeMs, uptimeMs, 8388608);
            this.mPhoneOn = true;
            this.mPhoneOnTimer.startRunningLocked(elapsedRealtimeMs);
            if (this.mConstants.PHONE_ON_EXTERNAL_STATS_COLLECTION) {
                scheduleSyncExternalStatsLocked("phone-on", 4);
                this.mMobileRadioPowerStatsCollector.schedule();
            }
            this.mBatteryStatsImplExt.addThermalPhoneOnOff(elapsedRealtimeMs, uptimeMs, true);
        }
    }

    public void notePhoneOffLocked(long elapsedRealtimeMs, long uptimeMs) {
        if (this.mPhoneOn) {
            this.mHistory.recordState2StopEvent(elapsedRealtimeMs, uptimeMs, 8388608);
            this.mPhoneOn = false;
            this.mPhoneOnTimer.stopRunningLocked(elapsedRealtimeMs);
            scheduleSyncExternalStatsLocked("phone-off", 4);
            this.mMobileRadioPowerStatsCollector.schedule();
            this.mBatteryStatsImplExt.addThermalPhoneOnOff(elapsedRealtimeMs, uptimeMs, false);
        }
    }

    private void registerUsbStateReceiver(android.content.Context context) {
        android.content.IntentFilter usbStateFilter = new android.content.IntentFilter();
        usbStateFilter.addAction("android.hardware.usb.action.USB_STATE");
        context.registerReceiver(new android.content.BroadcastReceiver() { // from class: com.android.server.power.stats.BatteryStatsImpl.4
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context2, android.content.Intent intent) {
                boolean state = intent.getBooleanExtra("connected", false);
                synchronized (com.android.server.power.stats.BatteryStatsImpl.this) {
                    com.android.server.power.stats.BatteryStatsImpl.this.noteUsbConnectionStateLocked(state, com.android.server.power.stats.BatteryStatsImpl.this.mClock.elapsedRealtime(), com.android.server.power.stats.BatteryStatsImpl.this.mClock.uptimeMillis());
                }
            }
        }, usbStateFilter);
        synchronized (this) {
            if (this.mUsbDataState == 0) {
                android.content.Intent usbState = context.registerReceiver(null, usbStateFilter);
                boolean z = false;
                if (usbState != null && usbState.getBooleanExtra("connected", false)) {
                    z = true;
                }
                boolean initState = z;
                noteUsbConnectionStateLocked(initState, this.mClock.elapsedRealtime(), this.mClock.uptimeMillis());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void noteUsbConnectionStateLocked(boolean connected, long elapsedRealtimeMs, long uptimeMs) {
        int newState = connected ? 2 : 1;
        if (this.mUsbDataState != newState) {
            this.mUsbDataState = newState;
            if (connected) {
                this.mHistory.recordState2StartEvent(elapsedRealtimeMs, uptimeMs, 262144);
            } else {
                this.mHistory.recordState2StopEvent(elapsedRealtimeMs, uptimeMs, 262144);
            }
        }
    }

    void stopAllPhoneSignalStrengthTimersLocked(int except, long elapsedRealtimeMs) {
        for (int i = 0; i < CELL_SIGNAL_STRENGTH_LEVEL_COUNT; i++) {
            if (i != except) {
                while (this.mPhoneSignalStrengthsTimer[i].isRunningLocked()) {
                    this.mPhoneSignalStrengthsTimer[i].stopRunningLocked(elapsedRealtimeMs);
                }
            }
        }
    }

    private void updateAllPhoneStateLocked(int state, int simState, int strengthBin, long elapsedRealtimeMs, long uptimeMs) {
        int strengthBin2;
        boolean scanning;
        int addStateFlag;
        int removeStateFlag;
        int newState;
        boolean newHistory;
        int newSignalStrength;
        int newSignalStrength2;
        boolean newHistory2;
        int state2 = state;
        boolean newHistory3 = false;
        this.mPhoneServiceStateRaw = state2;
        this.mPhoneSimStateRaw = simState;
        this.mPhoneSignalStrengthBinRaw = strengthBin;
        if (simState == 1 && state2 == 1 && strengthBin > 0) {
            state2 = 0;
        }
        if (state2 == 3) {
            strengthBin2 = -1;
            scanning = false;
            addStateFlag = 0;
        } else if (state2 != 0 && state2 == 1) {
            if (this.mPhoneSignalScanningTimer.isRunningLocked()) {
                strengthBin2 = 0;
                scanning = true;
                addStateFlag = 0;
            } else {
                newHistory3 = true;
                this.mPhoneSignalScanningTimer.startRunningLocked(elapsedRealtimeMs);
                this.mFrameworkStatsLogger.phoneServiceStateChanged(state2, simState, 0);
                strengthBin2 = 0;
                scanning = true;
                addStateFlag = 2097152;
            }
        } else {
            strengthBin2 = strengthBin;
            scanning = false;
            addStateFlag = 0;
        }
        if (!scanning && this.mPhoneSignalScanningTimer.isRunningLocked()) {
            newHistory3 = true;
            this.mPhoneSignalScanningTimer.stopRunningLocked(elapsedRealtimeMs);
            this.mFrameworkStatsLogger.phoneServiceStateChanged(state2, simState, strengthBin2);
            removeStateFlag = 2097152;
        } else {
            removeStateFlag = 0;
        }
        if (this.mPhoneServiceState == state2) {
            newState = -1;
        } else {
            int newState2 = state2;
            newHistory3 = true;
            this.mPhoneServiceState = state2;
            newState = newState2;
        }
        if (this.mPhoneSignalStrengthBin == strengthBin2) {
            newHistory = newHistory3;
            newSignalStrength = -1;
        } else {
            if (this.mPhoneSignalStrengthBin >= 0) {
                this.mPhoneSignalStrengthsTimer[this.mPhoneSignalStrengthBin].stopRunningLocked(elapsedRealtimeMs);
            }
            if (strengthBin2 < 0) {
                stopAllPhoneSignalStrengthTimersLocked(-1, elapsedRealtimeMs);
                newSignalStrength2 = -1;
                newHistory2 = newHistory3;
            } else {
                if (!this.mPhoneSignalStrengthsTimer[strengthBin2].isRunningLocked()) {
                    this.mPhoneSignalStrengthsTimer[strengthBin2].startRunningLocked(elapsedRealtimeMs);
                }
                int newSignalStrength3 = strengthBin2;
                this.mFrameworkStatsLogger.phoneSignalStrengthChanged(strengthBin2);
                newSignalStrength2 = newSignalStrength3;
                newHistory2 = true;
            }
            this.mPhoneSignalStrengthBin = strengthBin2;
            this.mBatteryStatsImplExt.addThermalPhoneSignal(elapsedRealtimeMs, uptimeMs, (byte) strengthBin2);
            newHistory = newHistory2;
            newSignalStrength = newSignalStrength2;
        }
        byte phoneState = (byte) ((simState << 4) | (state2 & 15));
        this.mBatteryStatsImplExt.addThermalPhoneState(elapsedRealtimeMs, uptimeMs, phoneState);
        if (newHistory) {
            this.mHistory.recordPhoneStateChangeEvent(elapsedRealtimeMs, uptimeMs, addStateFlag, removeStateFlag, newState, newSignalStrength);
        }
    }

    public void notePhoneStateLocked(int state, int simState, long elapsedRealtimeMs, long uptimeMs) {
        updateAllPhoneStateLocked(state, simState, this.mPhoneSignalStrengthBinRaw, elapsedRealtimeMs, uptimeMs);
    }

    public void notePhoneSignalStrengthLocked(android.telephony.SignalStrength signalStrength, long elapsedRealtimeMs, long uptimeMs) {
        int ratType;
        int level;
        int overallSignalStrength = signalStrength.getLevel();
        android.util.SparseIntArray perRatSignalStrength = new android.util.SparseIntArray(3);
        java.util.List<android.telephony.CellSignalStrength> cellSignalStrengths = signalStrength.getCellSignalStrengths();
        int size = cellSignalStrengths.size();
        for (int i = 0; i < size; i++) {
            android.telephony.CellSignalStrength cellSignalStrength = cellSignalStrengths.get(i);
            if (cellSignalStrength instanceof android.telephony.CellSignalStrengthNr) {
                ratType = 2;
                level = cellSignalStrength.getLevel();
            } else if (cellSignalStrength instanceof android.telephony.CellSignalStrengthLte) {
                ratType = 1;
                level = cellSignalStrength.getLevel();
            } else {
                ratType = 0;
                level = cellSignalStrength.getLevel();
            }
            if (perRatSignalStrength.get(ratType, -1) < level) {
                perRatSignalStrength.put(ratType, level);
            }
        }
        notePhoneSignalStrengthLocked(overallSignalStrength, perRatSignalStrength, elapsedRealtimeMs, uptimeMs);
    }

    public void notePhoneSignalStrengthLocked(int signalStrength, android.util.SparseIntArray perRatSignalStrength) {
        notePhoneSignalStrengthLocked(signalStrength, perRatSignalStrength, this.mClock.elapsedRealtime(), this.mClock.uptimeMillis());
    }

    public void notePhoneSignalStrengthLocked(int signalStrength, android.util.SparseIntArray perRatSignalStrength, long elapsedRealtimeMs, long uptimeMs) {
        int size = perRatSignalStrength.size();
        for (int i = 0; i < size; i++) {
            int rat = perRatSignalStrength.keyAt(i);
            int ratSignalStrength = perRatSignalStrength.valueAt(i);
            getRatBatteryStatsLocked(rat).noteSignalStrength(ratSignalStrength, elapsedRealtimeMs);
        }
        updateAllPhoneStateLocked(this.mPhoneServiceStateRaw, this.mPhoneSimStateRaw, signalStrength, elapsedRealtimeMs, uptimeMs);
    }

    public void notePhoneDataConnectionStateLocked(int dataType, boolean hasData, int serviceType, int nrState, int nrFrequency) {
        notePhoneDataConnectionStateLocked(dataType, hasData, serviceType, nrState, nrFrequency, this.mClock.elapsedRealtime(), this.mClock.uptimeMillis());
    }

    public void notePhoneDataConnectionStateLocked(int dataType, boolean hasData, int serviceType, int nrState, int nrFrequency, long elapsedRealtimeMs, long uptimeMs) {
        int bin;
        if (!hasData) {
            bin = 0;
        } else if (dataType > 0 && dataType <= NUM_ALL_NETWORK_TYPES) {
            bin = dataType;
        } else {
            switch (serviceType) {
                case 1:
                    bin = 0;
                    break;
                case 2:
                    int bin2 = DATA_CONNECTION_EMERGENCY_SERVICE;
                    bin = bin2;
                    break;
                default:
                    int bin3 = DATA_CONNECTION_OTHER;
                    bin = bin3;
                    break;
            }
        }
        int bin4 = this.mPhoneDataConnectionType;
        if (bin4 != bin) {
            int[] result = this.mBatteryStatsExt.notePhoneDataConnectionStateLocked(dataType, hasData, bin, this.mModStepMode, this.mCurStepMode);
            this.mModStepMode |= result[0];
            this.mCurStepMode = result[1];
            this.mHistory.recordDataConnectionTypeChangeEvent(elapsedRealtimeMs, uptimeMs, bin);
            if (this.mPhoneDataConnectionType >= 0) {
                this.mPhoneDataConnectionsTimer[this.mPhoneDataConnectionType].stopRunningLocked(elapsedRealtimeMs);
            }
            this.mPhoneDataConnectionType = bin;
            this.mPhoneDataConnectionsTimer[bin].startRunningLocked(elapsedRealtimeMs);
            this.mBatteryStatsImplExt.notePhoneDataConnectionStateLocked(elapsedRealtimeMs, uptimeMs, dataType);
        }
        if (this.mNrState != nrState) {
            this.mHistory.recordNrStateChangeEvent(elapsedRealtimeMs, uptimeMs, nrState);
            this.mNrState = nrState;
        }
        boolean newNrNsaActive = isNrNsa(bin, nrState);
        boolean nrNsaActive = this.mNrNsaTimer.isRunningLocked();
        if (newNrNsaActive != nrNsaActive) {
            if (newNrNsaActive) {
                this.mNrNsaTimer.startRunningLocked(elapsedRealtimeMs);
            } else {
                this.mNrNsaTimer.stopRunningLocked(elapsedRealtimeMs);
            }
        }
        int newRat = mapNetworkTypeToRadioAccessTechnology(bin, nrState);
        if (newRat == 2) {
            getRatBatteryStatsLocked(newRat).noteFrequencyRange(nrFrequency, elapsedRealtimeMs);
        }
        if (this.mActiveRat != newRat) {
            getRatBatteryStatsLocked(this.mActiveRat).noteActive(false, elapsedRealtimeMs);
            this.mActiveRat = newRat;
        }
        boolean modemActive = this.mMobileRadioActiveTimer.isRunningLocked();
        getRatBatteryStatsLocked(newRat).noteActive(modemActive, elapsedRealtimeMs);
    }

    private static boolean isNrNsa(int dataType, int nrState) {
        return dataType == 13 && nrState == 3;
    }

    private static int mapNetworkTypeToRadioAccessTechnology(int dataType, int nrState) {
        if (isNrNsa(dataType, nrState)) {
            return 2;
        }
        switch (dataType) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
                return 0;
            case 13:
                return 1;
            case 19:
            default:
                android.util.Slog.w(TAG, "Unhandled NetworkType (" + dataType + "), mapping to OTHER");
                return 0;
            case 20:
                return 2;
        }
    }

    public void noteWifiOnLocked(long elapsedRealtimeMs, long uptimeMs) {
        if (!this.mWifiOn) {
            this.mHistory.recordState2StartEvent(elapsedRealtimeMs, uptimeMs, 268435456);
            this.mWifiOn = true;
            this.mWifiOnTimer.startRunningLocked(elapsedRealtimeMs);
            this.mBatteryStatsImplExt.addThermalWifiStatus(elapsedRealtimeMs, uptimeMs, 1);
            scheduleSyncExternalStatsLocked("wifi-off", 2);
            if (this.mWifiPowerStatsCollector.isEnabled()) {
                this.mWifiPowerStatsCollector.schedule();
            } else {
                scheduleSyncExternalStatsLocked("wifi-off", 2);
            }
        }
    }

    public void noteWifiOffLocked(long elapsedRealtimeMs, long uptimeMs) {
        if (this.mWifiOn) {
            this.mHistory.recordState2StopEvent(elapsedRealtimeMs, uptimeMs, 268435456);
            this.mWifiOn = false;
            this.mWifiOnTimer.stopRunningLocked(elapsedRealtimeMs);
            this.mBatteryStatsImplExt.addThermalWifiStatus(elapsedRealtimeMs, uptimeMs, 0);
            scheduleSyncExternalStatsLocked("wifi-on", 2);
            if (this.mWifiPowerStatsCollector.isEnabled()) {
                this.mWifiPowerStatsCollector.schedule();
            } else {
                scheduleSyncExternalStatsLocked("wifi-on", 2);
            }
        }
    }

    public void noteAudioOnLocked(int uid, long elapsedRealtimeMs, long uptimeMs) {
        int uid2 = mapUid(uid);
        if (this.mAudioOnNesting == 0) {
            this.mHistory.recordStateStartEvent(elapsedRealtimeMs, uptimeMs, 4194304, uid2, "audio");
            this.mAudioOnTimer.startRunningLocked(elapsedRealtimeMs);
            this.mBatteryStatsImplExt.addThermalOnOffEvent(3, elapsedRealtimeMs, uptimeMs, true);
        }
        int THERMAL_EVENT_AUDIO = this.mAudioOnNesting;
        this.mAudioOnNesting = THERMAL_EVENT_AUDIO + 1;
        if (!this.mPowerStatsCollectorEnabled.get(4)) {
            getUidStatsLocked(uid2, elapsedRealtimeMs, uptimeMs).noteAudioTurnedOnLocked(elapsedRealtimeMs);
        }
    }

    public void noteAudioOffLocked(int uid, long elapsedRealtimeMs, long uptimeMs) {
        if (this.mAudioOnNesting == 0) {
            return;
        }
        int uid2 = mapUid(uid);
        int i = this.mAudioOnNesting - 1;
        this.mAudioOnNesting = i;
        if (i == 0) {
            this.mHistory.recordStateStopEvent(elapsedRealtimeMs, uptimeMs, 4194304, uid2, "audio");
            this.mAudioOnTimer.stopRunningLocked(elapsedRealtimeMs);
            this.mBatteryStatsImplExt.addThermalOnOffEvent(3, elapsedRealtimeMs, uptimeMs, false);
        }
        if (!this.mPowerStatsCollectorEnabled.get(4)) {
            getUidStatsLocked(uid2, elapsedRealtimeMs, uptimeMs).noteAudioTurnedOffLocked(elapsedRealtimeMs);
        }
    }

    public void noteVideoOnLocked(int uid, long elapsedRealtimeMs, long uptimeMs) {
        int uid2 = mapUid(uid);
        if (this.mVideoOnNesting == 0) {
            this.mHistory.recordState2StartEvent(elapsedRealtimeMs, uptimeMs, 1073741824, uid2, com.android.server.am.IOplusSceneManager.APP_SCENE_VIDEO);
            this.mBatteryStatsImplExt.addThermalOnOffEvent(4, elapsedRealtimeMs, uptimeMs, true);
            this.mVideoOnTimer.startRunningLocked(elapsedRealtimeMs);
        }
        int THERMAL_EVENT_VIDEO = this.mVideoOnNesting;
        this.mVideoOnNesting = THERMAL_EVENT_VIDEO + 1;
        if (!this.mPowerStatsCollectorEnabled.get(5)) {
            getUidStatsLocked(uid2, elapsedRealtimeMs, uptimeMs).noteVideoTurnedOnLocked(elapsedRealtimeMs);
        }
    }

    public void noteVideoOffLocked(int uid, long elapsedRealtimeMs, long uptimeMs) {
        if (this.mVideoOnNesting == 0) {
            return;
        }
        int uid2 = mapUid(uid);
        int i = this.mVideoOnNesting - 1;
        this.mVideoOnNesting = i;
        if (i == 0) {
            this.mHistory.recordState2StopEvent(elapsedRealtimeMs, uptimeMs, 1073741824, uid2, com.android.server.am.IOplusSceneManager.APP_SCENE_VIDEO);
            this.mBatteryStatsImplExt.addThermalOnOffEvent(4, elapsedRealtimeMs, uptimeMs, false);
            this.mVideoOnTimer.stopRunningLocked(elapsedRealtimeMs);
        }
        if (!this.mPowerStatsCollectorEnabled.get(5)) {
            getUidStatsLocked(uid2, elapsedRealtimeMs, uptimeMs).noteVideoTurnedOffLocked(elapsedRealtimeMs);
        }
    }

    public void noteResetAudioLocked(long elapsedRealtimeMs, long uptimeMs) {
        if (this.mAudioOnNesting > 0) {
            this.mAudioOnNesting = 0;
            this.mHistory.recordStateStopEvent(elapsedRealtimeMs, uptimeMs, 4194304);
            this.mBatteryStatsImplExt.addThermalOnOffEvent(3, elapsedRealtimeMs, uptimeMs, false);
            this.mAudioOnTimer.stopAllRunningLocked(elapsedRealtimeMs);
            for (int i = 0; i < this.mUidStats.size(); i++) {
                com.android.server.power.stats.BatteryStatsImpl.Uid uid = this.mUidStats.valueAt(i);
                uid.noteResetAudioLocked(elapsedRealtimeMs);
            }
        }
    }

    public void noteResetVideoLocked(long elapsedRealtimeMs, long uptimeMs) {
        if (this.mVideoOnNesting > 0) {
            this.mVideoOnNesting = 0;
            this.mHistory.recordState2StopEvent(elapsedRealtimeMs, uptimeMs, 1073741824);
            this.mBatteryStatsImplExt.addThermalOnOffEvent(4, elapsedRealtimeMs, uptimeMs, false);
            this.mVideoOnTimer.stopAllRunningLocked(elapsedRealtimeMs);
            for (int i = 0; i < this.mUidStats.size(); i++) {
                com.android.server.power.stats.BatteryStatsImpl.Uid uid = this.mUidStats.valueAt(i);
                uid.noteResetVideoLocked(elapsedRealtimeMs);
            }
        }
    }

    public void noteActivityResumedLocked(int uid) {
        noteActivityResumedLocked(uid, this.mClock.elapsedRealtime(), this.mClock.uptimeMillis());
    }

    public void noteActivityResumedLocked(int uid, long elapsedRealtimeMs, long uptimeMs) {
        getUidStatsLocked(mapUid(uid), elapsedRealtimeMs, uptimeMs).noteActivityResumedLocked(elapsedRealtimeMs);
    }

    public void noteActivityPausedLocked(int uid) {
        noteActivityPausedLocked(uid, this.mClock.elapsedRealtime(), this.mClock.uptimeMillis());
    }

    public void noteActivityPausedLocked(int uid, long elapsedRealtimeMs, long uptimeMs) {
        getUidStatsLocked(mapUid(uid), elapsedRealtimeMs, uptimeMs).noteActivityPausedLocked(elapsedRealtimeMs);
    }

    public void noteVibratorOnLocked(int uid, long durationMillis, long elapsedRealtimeMs, long uptimeMs) {
        getUidStatsLocked(mapUid(uid), elapsedRealtimeMs, uptimeMs).noteVibratorOnLocked(durationMillis, elapsedRealtimeMs);
    }

    public void noteVibratorOffLocked(int uid, long elapsedRealtimeMs, long uptimeMs) {
        getUidStatsLocked(mapUid(uid), elapsedRealtimeMs, uptimeMs).noteVibratorOffLocked(elapsedRealtimeMs);
    }

    public void noteFlashlightOnLocked(int uid, long elapsedRealtimeMs, long uptimeMs) {
        int uid2 = mapUid(uid);
        int i = this.mFlashlightOnNesting;
        this.mFlashlightOnNesting = i + 1;
        if (i == 0) {
            this.mHistory.recordState2StartEvent(elapsedRealtimeMs, uptimeMs, 134217728, uid2, "flashlight");
            this.mBatteryStatsImplExt.addThermalOnOffEvent(6, elapsedRealtimeMs, uptimeMs, true);
            this.mFlashlightOnTimer.startRunningLocked(elapsedRealtimeMs);
        }
        if (!this.mPowerStatsCollectorEnabled.get(6)) {
            getUidStatsLocked(uid2, elapsedRealtimeMs, uptimeMs).noteFlashlightTurnedOnLocked(elapsedRealtimeMs);
        }
    }

    public void noteFlashlightOffLocked(int uid, long elapsedRealtimeMs, long uptimeMs) {
        if (this.mFlashlightOnNesting == 0) {
            return;
        }
        int uid2 = mapUid(uid);
        int i = this.mFlashlightOnNesting - 1;
        this.mFlashlightOnNesting = i;
        if (i == 0) {
            this.mHistory.recordState2StopEvent(elapsedRealtimeMs, uptimeMs, 134217728, uid2, "flashlight");
            this.mBatteryStatsImplExt.addThermalOnOffEvent(6, elapsedRealtimeMs, uptimeMs, false);
            this.mFlashlightOnTimer.stopRunningLocked(elapsedRealtimeMs);
        }
        if (!this.mPowerStatsCollectorEnabled.get(6)) {
            getUidStatsLocked(uid2, elapsedRealtimeMs, uptimeMs).noteFlashlightTurnedOffLocked(elapsedRealtimeMs);
        }
    }

    public void noteCameraOnLocked(int uid, long elapsedRealtimeMs, long uptimeMs) {
        int uid2 = mapUid(uid);
        int i = this.mCameraOnNesting;
        this.mCameraOnNesting = i + 1;
        if (i == 0) {
            this.mHistory.recordState2StartEvent(elapsedRealtimeMs, uptimeMs, 2097152, uid2, "camera");
            this.mBatteryStatsImplExt.addThermalOnOffEvent(2, elapsedRealtimeMs, uptimeMs, true);
            this.mCameraOnTimer.startRunningLocked(elapsedRealtimeMs);
        }
        getUidStatsLocked(uid2, elapsedRealtimeMs, uptimeMs).noteCameraTurnedOnLocked(elapsedRealtimeMs);
        if (!this.mPowerStatsCollectorEnabled.get(3)) {
            scheduleSyncExternalStatsLocked("camera-on", 64);
        } else {
            this.mCameraPowerStatsCollector.schedule();
        }
    }

    public void noteCameraOffLocked(int uid, long elapsedRealtimeMs, long uptimeMs) {
        if (this.mCameraOnNesting == 0) {
            return;
        }
        int uid2 = mapUid(uid);
        int i = this.mCameraOnNesting - 1;
        this.mCameraOnNesting = i;
        if (i == 0) {
            this.mHistory.recordState2StopEvent(elapsedRealtimeMs, uptimeMs, 2097152, uid2, "camera");
            this.mBatteryStatsImplExt.addThermalOnOffEvent(2, elapsedRealtimeMs, uptimeMs, false);
            this.mCameraOnTimer.stopRunningLocked(elapsedRealtimeMs);
        }
        getUidStatsLocked(uid2, elapsedRealtimeMs, uptimeMs).noteCameraTurnedOffLocked(elapsedRealtimeMs);
        if (!this.mPowerStatsCollectorEnabled.get(3)) {
            scheduleSyncExternalStatsLocked("camera-off", 64);
        } else {
            this.mCameraPowerStatsCollector.schedule();
        }
    }

    public void noteResetCameraLocked(long elapsedRealtimeMs, long uptimeMs) {
        if (this.mCameraOnNesting > 0) {
            this.mCameraOnNesting = 0;
            this.mHistory.recordState2StopEvent(elapsedRealtimeMs, uptimeMs, 2097152);
            this.mBatteryStatsImplExt.addThermalOnOffEvent(2, elapsedRealtimeMs, uptimeMs, false);
            this.mCameraOnTimer.stopAllRunningLocked(elapsedRealtimeMs);
            for (int i = 0; i < this.mUidStats.size(); i++) {
                com.android.server.power.stats.BatteryStatsImpl.Uid uid = this.mUidStats.valueAt(i);
                uid.noteResetCameraLocked(elapsedRealtimeMs);
            }
        }
        scheduleSyncExternalStatsLocked("camera-reset", 64);
    }

    public void noteResetFlashlightLocked(long elapsedRealtimeMs, long uptimeMs) {
        if (this.mFlashlightOnNesting > 0) {
            this.mFlashlightOnNesting = 0;
            this.mHistory.recordState2StopEvent(elapsedRealtimeMs, uptimeMs, 134217728);
            this.mBatteryStatsImplExt.addThermalOnOffEvent(6, elapsedRealtimeMs, uptimeMs, false);
            this.mFlashlightOnTimer.stopAllRunningLocked(elapsedRealtimeMs);
            for (int i = 0; i < this.mUidStats.size(); i++) {
                com.android.server.power.stats.BatteryStatsImpl.Uid uid = this.mUidStats.valueAt(i);
                uid.noteResetFlashlightLocked(elapsedRealtimeMs);
            }
        }
    }

    private void noteBluetoothScanStartedLocked(android.os.WorkSource.WorkChain workChain, int uid, boolean isUnoptimized, long elapsedRealtimeMs, long uptimeMs) {
        if (workChain != null) {
            uid = workChain.getAttributionUid();
        }
        int uid2 = mapUid(uid);
        if (this.mBluetoothScanNesting == 0) {
            this.mHistory.recordState2StartEvent(elapsedRealtimeMs, uptimeMs, 1048576);
            this.mBluetoothScanTimer.startRunningLocked(elapsedRealtimeMs);
        }
        this.mBluetoothScanNesting++;
        getUidStatsLocked(uid2, elapsedRealtimeMs, uptimeMs).noteBluetoothScanStartedLocked(elapsedRealtimeMs, isUnoptimized);
    }

    public void noteBluetoothScanStartedFromSourceLocked(android.os.WorkSource ws, boolean isUnoptimized) {
        noteBluetoothScanStartedFromSourceLocked(ws, isUnoptimized, this.mClock.elapsedRealtime(), this.mClock.uptimeMillis());
    }

    public void noteBluetoothScanStartedFromSourceLocked(android.os.WorkSource ws, boolean isUnoptimized, long elapsedRealtimeMs, long uptimeMs) {
        int N = ws.size();
        for (int i = 0; i < N; i++) {
            noteBluetoothScanStartedLocked(null, ws.getUid(i), isUnoptimized, elapsedRealtimeMs, uptimeMs);
        }
        java.util.List<android.os.WorkSource.WorkChain> workChains = ws.getWorkChains();
        if (workChains != null) {
            for (int i2 = 0; i2 < workChains.size(); i2++) {
                noteBluetoothScanStartedLocked(workChains.get(i2), -1, isUnoptimized, elapsedRealtimeMs, uptimeMs);
            }
        }
    }

    private void noteBluetoothScanStoppedLocked(android.os.WorkSource.WorkChain workChain, int uid, boolean isUnoptimized, long elapsedRealtimeMs, long uptimeMs) {
        if (workChain != null) {
            uid = workChain.getAttributionUid();
        }
        int uid2 = mapUid(uid);
        this.mBluetoothScanNesting--;
        if (this.mBluetoothScanNesting == 0) {
            this.mHistory.recordState2StopEvent(elapsedRealtimeMs, uptimeMs, 1048576);
            this.mBluetoothScanTimer.stopRunningLocked(elapsedRealtimeMs);
        }
        getUidStatsLocked(uid2, elapsedRealtimeMs, uptimeMs).noteBluetoothScanStoppedLocked(elapsedRealtimeMs, isUnoptimized);
    }

    public void noteBluetoothScanStoppedFromSourceLocked(android.os.WorkSource ws, boolean isUnoptimized) {
        noteBluetoothScanStoppedFromSourceLocked(ws, isUnoptimized, this.mClock.elapsedRealtime(), this.mClock.uptimeMillis());
    }

    public void noteBluetoothScanStoppedFromSourceLocked(android.os.WorkSource ws, boolean isUnoptimized, long elapsedRealtimeMs, long uptimeMs) {
        int N = ws.size();
        for (int i = 0; i < N; i++) {
            noteBluetoothScanStoppedLocked(null, ws.getUid(i), isUnoptimized, elapsedRealtimeMs, uptimeMs);
        }
        java.util.List<android.os.WorkSource.WorkChain> workChains = ws.getWorkChains();
        if (workChains != null) {
            for (int i2 = 0; i2 < workChains.size(); i2++) {
                noteBluetoothScanStoppedLocked(workChains.get(i2), -1, isUnoptimized, elapsedRealtimeMs, uptimeMs);
            }
        }
    }

    public void noteResetBluetoothScanLocked(long elapsedRealtimeMs, long uptimeMs) {
        if (this.mBluetoothScanNesting > 0) {
            this.mBluetoothScanNesting = 0;
            this.mHistory.recordState2StopEvent(elapsedRealtimeMs, uptimeMs, 1048576);
            this.mBluetoothScanTimer.stopAllRunningLocked(elapsedRealtimeMs);
            for (int i = 0; i < this.mUidStats.size(); i++) {
                com.android.server.power.stats.BatteryStatsImpl.Uid uid = this.mUidStats.valueAt(i);
                uid.noteResetBluetoothScanLocked(elapsedRealtimeMs);
            }
        }
    }

    public void noteBluetoothScanResultsFromSourceLocked(android.os.WorkSource ws, int numNewResults) {
        noteBluetoothScanResultsFromSourceLocked(ws, numNewResults, this.mClock.elapsedRealtime(), this.mClock.uptimeMillis());
    }

    public void noteBluetoothScanResultsFromSourceLocked(android.os.WorkSource ws, int numNewResults, long elapsedRealtimeMs, long uptimeMs) {
        int N = ws.size();
        for (int i = 0; i < N; i++) {
            int uid = mapUid(ws.getUid(i));
            getUidStatsLocked(uid, elapsedRealtimeMs, uptimeMs).noteBluetoothScanResultsLocked(numNewResults);
        }
        java.util.List<android.os.WorkSource.WorkChain> workChains = ws.getWorkChains();
        if (workChains != null) {
            for (int i2 = 0; i2 < workChains.size(); i2++) {
                android.os.WorkSource.WorkChain wc = workChains.get(i2);
                int uid2 = mapUid(wc.getAttributionUid());
                getUidStatsLocked(uid2, elapsedRealtimeMs, uptimeMs).noteBluetoothScanResultsLocked(numNewResults);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void retrieveBluetoothScanTimesLocked(com.android.server.power.stats.BluetoothPowerStatsCollector.BluetoothStatsRetriever.Callback callback) {
        long elapsedTimeUs = this.mClock.elapsedRealtime() * 1000;
        for (int i = this.mUidStats.size() - 1; i >= 0; i--) {
            com.android.server.power.stats.BatteryStatsImpl.Uid uidStats = this.mUidStats.valueAt(i);
            if (uidStats.mBluetoothScanTimer != null) {
                long scanTimeUs = this.mBluetoothScanTimer.getTotalTimeLocked(elapsedTimeUs, 0);
                if (scanTimeUs != 0) {
                    int uid = this.mUidStats.keyAt(i);
                    callback.onBluetoothScanTime(uid, (500 + scanTimeUs) / 1000);
                }
            }
        }
    }

    private void noteWifiRadioApWakeupLocked(long elapsedRealtimeMillis, long uptimeMillis, int uid) {
        int uid2 = mapUid(uid);
        this.mHistory.recordEvent(elapsedRealtimeMillis, uptimeMillis, 19, "", uid2);
        getUidStatsLocked(uid2, elapsedRealtimeMillis, uptimeMillis).noteWifiRadioApWakeupLocked();
    }

    public void noteWifiRadioPowerState(int powerState, long timestampNs, int uid, long elapsedRealtimeMs, long uptimeMs) {
        if (this.mWifiRadioPowerState != powerState) {
            boolean active = powerState == 2 || powerState == 3;
            if (!active) {
                this.mHistory.recordStateStopEvent(elapsedRealtimeMs, uptimeMs, 67108864);
                this.mWifiActiveTimer.stopRunningLocked(timestampNs / 1000000);
            } else {
                if (uid > 0) {
                    noteWifiRadioApWakeupLocked(elapsedRealtimeMs, uptimeMs, uid);
                }
                this.mHistory.recordStateStartEvent(elapsedRealtimeMs, uptimeMs, 67108864);
                this.mWifiActiveTimer.startRunningLocked(elapsedRealtimeMs);
            }
            this.mWifiRadioPowerState = powerState;
        }
    }

    public void noteWifiRunningLocked(android.os.WorkSource ws, long elapsedRealtimeMs, long uptimeMs) {
        if (!this.mGlobalWifiRunning) {
            this.mHistory.recordState2StartEvent(elapsedRealtimeMs, uptimeMs, 536870912);
            this.mGlobalWifiRunning = true;
            this.mGlobalWifiRunningTimer.startRunningLocked(elapsedRealtimeMs);
            int N = ws.size();
            for (int i = 0; i < N; i++) {
                int uid = mapUid(ws.getUid(i));
                getUidStatsLocked(uid, elapsedRealtimeMs, uptimeMs).noteWifiRunningLocked(elapsedRealtimeMs);
            }
            java.util.List<android.os.WorkSource.WorkChain> workChains = ws.getWorkChains();
            if (workChains != null) {
                for (int i2 = 0; i2 < workChains.size(); i2++) {
                    int uid2 = mapUid(workChains.get(i2).getAttributionUid());
                    getUidStatsLocked(uid2, elapsedRealtimeMs, uptimeMs).noteWifiRunningLocked(elapsedRealtimeMs);
                }
            }
            this.mBatteryStatsImplExt.addThermalWifiStatus(elapsedRealtimeMs, uptimeMs, 2);
            scheduleSyncExternalStatsLocked("wifi-running", 2);
            if (this.mWifiPowerStatsCollector.isEnabled()) {
                this.mWifiPowerStatsCollector.schedule();
                return;
            } else {
                scheduleSyncExternalStatsLocked("wifi-running", 2);
                return;
            }
        }
        android.util.Log.w(TAG, "noteWifiRunningLocked -- called while WIFI running");
    }

    public void noteWifiRunningChangedLocked(android.os.WorkSource oldWs, android.os.WorkSource newWs, long elapsedRealtimeMs, long uptimeMs) {
        if (this.mGlobalWifiRunning) {
            int N = oldWs.size();
            for (int i = 0; i < N; i++) {
                int uid = mapUid(oldWs.getUid(i));
                getUidStatsLocked(uid, elapsedRealtimeMs, uptimeMs).noteWifiStoppedLocked(elapsedRealtimeMs);
            }
            java.util.List<android.os.WorkSource.WorkChain> workChains = oldWs.getWorkChains();
            if (workChains != null) {
                for (int i2 = 0; i2 < workChains.size(); i2++) {
                    int uid2 = mapUid(workChains.get(i2).getAttributionUid());
                    getUidStatsLocked(uid2, elapsedRealtimeMs, uptimeMs).noteWifiStoppedLocked(elapsedRealtimeMs);
                }
            }
            int N2 = newWs.size();
            for (int i3 = 0; i3 < N2; i3++) {
                int uid3 = mapUid(newWs.getUid(i3));
                getUidStatsLocked(uid3, elapsedRealtimeMs, uptimeMs).noteWifiRunningLocked(elapsedRealtimeMs);
            }
            java.util.List<android.os.WorkSource.WorkChain> workChains2 = newWs.getWorkChains();
            if (workChains2 != null) {
                for (int i4 = 0; i4 < workChains2.size(); i4++) {
                    int uid4 = mapUid(workChains2.get(i4).getAttributionUid());
                    getUidStatsLocked(uid4, elapsedRealtimeMs, uptimeMs).noteWifiRunningLocked(elapsedRealtimeMs);
                }
                return;
            }
            return;
        }
        android.util.Log.w(TAG, "noteWifiRunningChangedLocked -- called while WIFI not running");
    }

    public void noteWifiStoppedLocked(android.os.WorkSource ws, long elapsedRealtimeMs, long uptimeMs) {
        if (this.mGlobalWifiRunning) {
            this.mHistory.recordState2StopEvent(elapsedRealtimeMs, uptimeMs, 536870912);
            this.mGlobalWifiRunning = false;
            this.mGlobalWifiRunningTimer.stopRunningLocked(elapsedRealtimeMs);
            int N = ws.size();
            for (int i = 0; i < N; i++) {
                int uid = mapUid(ws.getUid(i));
                getUidStatsLocked(uid, elapsedRealtimeMs, uptimeMs).noteWifiStoppedLocked(elapsedRealtimeMs);
            }
            java.util.List<android.os.WorkSource.WorkChain> workChains = ws.getWorkChains();
            if (workChains != null) {
                for (int i2 = 0; i2 < workChains.size(); i2++) {
                    int uid2 = mapUid(workChains.get(i2).getAttributionUid());
                    getUidStatsLocked(uid2, elapsedRealtimeMs, uptimeMs).noteWifiStoppedLocked(elapsedRealtimeMs);
                }
            }
            this.mBatteryStatsImplExt.addThermalWifiStatus(elapsedRealtimeMs, uptimeMs, 3);
            scheduleSyncExternalStatsLocked("wifi-stopped", 2);
            if (this.mWifiPowerStatsCollector.isEnabled()) {
                this.mWifiPowerStatsCollector.schedule();
                return;
            } else {
                scheduleSyncExternalStatsLocked("wifi-stopped", 2);
                return;
            }
        }
        android.util.Log.w(TAG, "noteWifiStoppedLocked -- called while WIFI not running");
    }

    public void noteWifiStateLocked(int wifiState, java.lang.String accessPoint, long elapsedRealtimeMs) {
        if (this.mWifiState != wifiState) {
            if (this.mWifiState >= 0) {
                this.mWifiStateTimer[this.mWifiState].stopRunningLocked(elapsedRealtimeMs);
            }
            this.mWifiState = wifiState;
            this.mWifiStateTimer[wifiState].startRunningLocked(elapsedRealtimeMs);
            if (this.mWifiPowerStatsCollector.isEnabled()) {
                this.mWifiPowerStatsCollector.schedule();
            } else {
                scheduleSyncExternalStatsLocked("wifi-state", 2);
            }
        }
    }

    public void noteWifiSupplicantStateChangedLocked(int supplState, boolean failedAuth, long elapsedRealtimeMs, long uptimeMs) {
        if (this.mWifiSupplState != supplState) {
            if (this.mWifiSupplState >= 0) {
                this.mWifiSupplStateTimer[this.mWifiSupplState].stopRunningLocked(elapsedRealtimeMs);
            }
            this.mWifiSupplState = supplState;
            this.mWifiSupplStateTimer[supplState].startRunningLocked(elapsedRealtimeMs);
            this.mHistory.recordWifiSupplicantStateChangeEvent(elapsedRealtimeMs, uptimeMs, supplState);
        }
    }

    void stopAllWifiSignalStrengthTimersLocked(int except, long elapsedRealtimeMs) {
        for (int i = 0; i < 5; i++) {
            if (i != except) {
                while (this.mWifiSignalStrengthsTimer[i].isRunningLocked()) {
                    this.mWifiSignalStrengthsTimer[i].stopRunningLocked(elapsedRealtimeMs);
                }
            }
        }
    }

    public void noteWifiRssiChangedLocked(int newRssi, long elapsedRealtimeMs, long uptimeMs) {
        int strengthBin = android.net.wifi.WifiManager.calculateSignalLevel(newRssi, 5);
        if (this.mWifiSignalStrengthBin != strengthBin) {
            if (this.mWifiSignalStrengthBin >= 0) {
                this.mWifiSignalStrengthsTimer[this.mWifiSignalStrengthBin].stopRunningLocked(elapsedRealtimeMs);
            }
            if (strengthBin >= 0) {
                if (!this.mWifiSignalStrengthsTimer[strengthBin].isRunningLocked()) {
                    this.mWifiSignalStrengthsTimer[strengthBin].startRunningLocked(elapsedRealtimeMs);
                }
                this.mHistory.recordWifiSignalStrengthChangeEvent(elapsedRealtimeMs, uptimeMs, strengthBin);
                this.mBatteryStatsImplExt.addThermalWifiRssi(elapsedRealtimeMs, uptimeMs, newRssi);
            } else {
                stopAllWifiSignalStrengthTimersLocked(-1, elapsedRealtimeMs);
                this.mBatteryStatsImplExt.addThermalWifiRssi(elapsedRealtimeMs, uptimeMs, 0);
            }
            this.mWifiSignalStrengthBin = strengthBin;
        }
    }

    public void noteFullWifiLockAcquiredLocked(int uid, long elapsedRealtimeMs, long uptimeMs) {
        if (this.mWifiFullLockNesting == 0) {
            this.mHistory.recordStateStartEvent(elapsedRealtimeMs, uptimeMs, 268435456);
        }
        this.mWifiFullLockNesting++;
        getUidStatsLocked(uid, elapsedRealtimeMs, uptimeMs).noteFullWifiLockAcquiredLocked(elapsedRealtimeMs);
    }

    public void noteFullWifiLockReleasedLocked(int uid, long elapsedRealtimeMs, long uptimeMs) {
        this.mWifiFullLockNesting--;
        if (this.mWifiFullLockNesting == 0) {
            this.mHistory.recordStateStopEvent(elapsedRealtimeMs, uptimeMs, 268435456);
        }
        getUidStatsLocked(uid, elapsedRealtimeMs, uptimeMs).noteFullWifiLockReleasedLocked(elapsedRealtimeMs);
    }

    public void noteWifiScanStartedLocked(int uid) {
        noteWifiScanStartedLocked(uid, this.mClock.elapsedRealtime(), this.mClock.uptimeMillis());
    }

    public void noteWifiScanStartedLocked(int uid, long elapsedRealtimeMs, long uptimeMs) {
        if (this.mWifiScanNesting == 0) {
            this.mHistory.recordStateStartEvent(elapsedRealtimeMs, uptimeMs, 134217728);
        }
        this.mWifiScanNesting++;
        getUidStatsLocked(uid, elapsedRealtimeMs, uptimeMs).noteWifiScanStartedLocked(elapsedRealtimeMs);
    }

    public void noteWifiScanStoppedLocked(int uid) {
        noteWifiScanStoppedLocked(uid, this.mClock.elapsedRealtime(), this.mClock.uptimeMillis());
    }

    public void noteWifiScanStoppedLocked(int uid, long elapsedRealtimeMs, long uptimeMs) {
        this.mWifiScanNesting--;
        if (this.mWifiScanNesting == 0) {
            this.mHistory.recordStateStopEvent(elapsedRealtimeMs, uptimeMs, 134217728);
        }
        getUidStatsLocked(uid, elapsedRealtimeMs, uptimeMs).noteWifiScanStoppedLocked(elapsedRealtimeMs);
    }

    public void noteWifiBatchedScanStartedLocked(int uid, int csph, long elapsedRealtimeMs, long uptimeMs) {
        getUidStatsLocked(mapUid(uid), elapsedRealtimeMs, uptimeMs).noteWifiBatchedScanStartedLocked(csph, elapsedRealtimeMs);
    }

    public void noteWifiBatchedScanStoppedLocked(int uid, long elapsedRealtimeMs, long uptimeMs) {
        getUidStatsLocked(mapUid(uid), elapsedRealtimeMs, uptimeMs).noteWifiBatchedScanStoppedLocked(elapsedRealtimeMs);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void retrieveWifiScanTimesLocked(com.android.server.power.stats.WifiPowerStatsCollector.WifiStatsRetriever.Callback callback) {
        long elapsedTimeUs = this.mClock.elapsedRealtime() * 1000;
        for (int i = this.mUidStats.size() - 1; i >= 0; i--) {
            int uid = this.mUidStats.keyAt(i);
            com.android.server.power.stats.BatteryStatsImpl.Uid uidStats = this.mUidStats.valueAt(i);
            long scanTimeUs = uidStats.getWifiScanTime(elapsedTimeUs, 0);
            long batchScanTimeUs = 0;
            for (int bucket = 0; bucket < 5; bucket++) {
                batchScanTimeUs += uidStats.getWifiBatchedScanTime(bucket, elapsedTimeUs, 0);
            }
            if (scanTimeUs != 0 || batchScanTimeUs != 0) {
                callback.onWifiScanTime(uid, (scanTimeUs + 500) / 1000, (batchScanTimeUs + 500) / 1000);
            }
        }
    }

    public void noteWifiMulticastEnabledLocked(int uid, long elapsedRealtimeMs, long uptimeMs) {
        int uid2 = mapUid(uid);
        if (this.mWifiMulticastNesting == 0) {
            this.mHistory.recordStateStartEvent(elapsedRealtimeMs, uptimeMs, 65536);
            if (!this.mWifiMulticastWakelockTimer.isRunningLocked()) {
                this.mWifiMulticastWakelockTimer.startRunningLocked(elapsedRealtimeMs);
            }
        }
        this.mWifiMulticastNesting++;
        getUidStatsLocked(uid2, elapsedRealtimeMs, uptimeMs).noteWifiMulticastEnabledLocked(elapsedRealtimeMs);
    }

    public void noteWifiMulticastDisabledLocked(int uid, long elapsedRealtimeMs, long uptimeMs) {
        int uid2 = mapUid(uid);
        this.mWifiMulticastNesting--;
        if (this.mWifiMulticastNesting == 0) {
            this.mHistory.recordStateStopEvent(elapsedRealtimeMs, uptimeMs, 65536);
            if (this.mWifiMulticastWakelockTimer.isRunningLocked()) {
                this.mWifiMulticastWakelockTimer.stopRunningLocked(elapsedRealtimeMs);
            }
        }
        getUidStatsLocked(uid2, elapsedRealtimeMs, uptimeMs).noteWifiMulticastDisabledLocked(elapsedRealtimeMs);
    }

    public void noteFullWifiLockAcquiredFromSourceLocked(android.os.WorkSource ws, long elapsedRealtimeMs, long uptimeMs) {
        int N = ws.size();
        for (int i = 0; i < N; i++) {
            int uid = mapUid(ws.getUid(i));
            noteFullWifiLockAcquiredLocked(uid, elapsedRealtimeMs, uptimeMs);
        }
        java.util.List<android.os.WorkSource.WorkChain> workChains = ws.getWorkChains();
        if (workChains != null) {
            for (int i2 = 0; i2 < workChains.size(); i2++) {
                android.os.WorkSource.WorkChain workChain = workChains.get(i2);
                int uid2 = mapUid(workChain.getAttributionUid());
                noteFullWifiLockAcquiredLocked(uid2, elapsedRealtimeMs, uptimeMs);
            }
        }
    }

    public void noteFullWifiLockReleasedFromSourceLocked(android.os.WorkSource ws, long elapsedRealtimeMs, long uptimeMs) {
        int N = ws.size();
        for (int i = 0; i < N; i++) {
            int uid = mapUid(ws.getUid(i));
            noteFullWifiLockReleasedLocked(uid, elapsedRealtimeMs, uptimeMs);
        }
        java.util.List<android.os.WorkSource.WorkChain> workChains = ws.getWorkChains();
        if (workChains != null) {
            for (int i2 = 0; i2 < workChains.size(); i2++) {
                android.os.WorkSource.WorkChain workChain = workChains.get(i2);
                int uid2 = mapUid(workChain.getAttributionUid());
                noteFullWifiLockReleasedLocked(uid2, elapsedRealtimeMs, uptimeMs);
            }
        }
    }

    public void noteWifiScanStartedFromSourceLocked(android.os.WorkSource ws, long elapsedRealtimeMs, long uptimeMs) {
        int N = ws.size();
        for (int i = 0; i < N; i++) {
            int uid = mapUid(ws.getUid(i));
            noteWifiScanStartedLocked(uid, elapsedRealtimeMs, uptimeMs);
        }
        java.util.List<android.os.WorkSource.WorkChain> workChains = ws.getWorkChains();
        if (workChains != null) {
            for (int i2 = 0; i2 < workChains.size(); i2++) {
                android.os.WorkSource.WorkChain workChain = workChains.get(i2);
                int uid2 = mapUid(workChain.getAttributionUid());
                noteWifiScanStartedLocked(uid2, elapsedRealtimeMs, uptimeMs);
            }
        }
    }

    public void noteWifiScanStoppedFromSourceLocked(android.os.WorkSource ws, long elapsedRealtimeMs, long uptimeMs) {
        int N = ws.size();
        for (int i = 0; i < N; i++) {
            int uid = mapUid(ws.getUid(i));
            noteWifiScanStoppedLocked(uid, elapsedRealtimeMs, uptimeMs);
        }
        java.util.List<android.os.WorkSource.WorkChain> workChains = ws.getWorkChains();
        if (workChains != null) {
            for (int i2 = 0; i2 < workChains.size(); i2++) {
                android.os.WorkSource.WorkChain workChain = workChains.get(i2);
                int uid2 = mapUid(workChain.getAttributionUid());
                noteWifiScanStoppedLocked(uid2, elapsedRealtimeMs, uptimeMs);
            }
        }
    }

    public void noteWifiBatchedScanStartedFromSourceLocked(android.os.WorkSource ws, int csph, long elapsedRealtimeMs, long uptimeMs) {
        int N = ws.size();
        for (int i = 0; i < N; i++) {
            noteWifiBatchedScanStartedLocked(ws.getUid(i), csph, elapsedRealtimeMs, uptimeMs);
        }
        java.util.List<android.os.WorkSource.WorkChain> workChains = ws.getWorkChains();
        if (workChains != null) {
            for (int i2 = 0; i2 < workChains.size(); i2++) {
                noteWifiBatchedScanStartedLocked(workChains.get(i2).getAttributionUid(), csph, elapsedRealtimeMs, uptimeMs);
            }
        }
    }

    public void noteWifiBatchedScanStoppedFromSourceLocked(android.os.WorkSource ws, long elapsedRealtimeMs, long uptimeMs) {
        int N = ws.size();
        for (int i = 0; i < N; i++) {
            noteWifiBatchedScanStoppedLocked(ws.getUid(i), elapsedRealtimeMs, uptimeMs);
        }
        java.util.List<android.os.WorkSource.WorkChain> workChains = ws.getWorkChains();
        if (workChains != null) {
            for (int i2 = 0; i2 < workChains.size(); i2++) {
                noteWifiBatchedScanStoppedLocked(workChains.get(i2).getAttributionUid(), elapsedRealtimeMs, uptimeMs);
            }
        }
    }

    private static java.lang.String[] includeInStringArray(java.lang.String[] array, java.lang.String str) {
        if (com.android.internal.util.ArrayUtils.indexOf(array, str) >= 0) {
            return array;
        }
        java.lang.String[] newArray = new java.lang.String[array.length + 1];
        java.lang.System.arraycopy(array, 0, newArray, 0, array.length);
        newArray[array.length] = str;
        return newArray;
    }

    private static java.lang.String[] excludeFromStringArray(java.lang.String[] array, java.lang.String str) {
        int index = com.android.internal.util.ArrayUtils.indexOf(array, str);
        if (index >= 0) {
            java.lang.String[] newArray = new java.lang.String[array.length - 1];
            if (index > 0) {
                java.lang.System.arraycopy(array, 0, newArray, 0, index);
            }
            if (index < array.length - 1) {
                java.lang.System.arraycopy(array, index + 1, newArray, index, (array.length - index) - 1);
            }
            return newArray;
        }
        return array;
    }

    public void noteNetworkInterfaceForTransports(java.lang.String iface, int[] transportTypes) {
        if (android.text.TextUtils.isEmpty(iface)) {
            return;
        }
        int displayTransport = getDisplayTransport(transportTypes);
        synchronized (this.mModemNetworkLock) {
            if (displayTransport == 0) {
                this.mModemIfaces = includeInStringArray(this.mModemIfaces, iface);
            } else {
                this.mModemIfaces = excludeFromStringArray(this.mModemIfaces, iface);
            }
        }
        synchronized (this.mWifiNetworkLock) {
            if (displayTransport == 1) {
                this.mWifiIfaces = includeInStringArray(this.mWifiIfaces, iface);
            } else {
                this.mWifiIfaces = excludeFromStringArray(this.mWifiIfaces, iface);
            }
        }
    }

    public void noteBinderCallStats(int workSourceUid, long incrementalCallCount, java.util.Collection<com.android.internal.os.BinderCallsStats.CallStat> callStats) {
        noteBinderCallStats(workSourceUid, incrementalCallCount, callStats, this.mClock.elapsedRealtime(), this.mClock.uptimeMillis());
    }

    public void noteBinderCallStats(int workSourceUid, long incrementalCallCount, java.util.Collection<com.android.internal.os.BinderCallsStats.CallStat> callStats, long elapsedRealtimeMs, long uptimeMs) {
        synchronized (this) {
            getUidStatsLocked(workSourceUid, elapsedRealtimeMs, uptimeMs).noteBinderCallStatsLocked(incrementalCallCount, callStats);
        }
    }

    public void noteBinderThreadNativeIds(int[] binderThreadNativeTids) {
        this.mSystemServerCpuThreadReader.setBinderThreadNativeTids(binderThreadNativeTids);
    }

    public void updateSystemServiceCallStats() {
        long totalSystemServiceTimeMicros;
        int totalRecordedCallCount = 0;
        long totalRecordedCallTimeMicros = 0;
        for (int i = 0; i < this.mUidStats.size(); i++) {
            android.util.ArraySet<com.android.server.power.stats.BatteryStatsImpl.BinderCallStats> binderCallStats = this.mUidStats.valueAt(i).mBinderCallStats;
            for (int j = binderCallStats.size() - 1; j >= 0; j--) {
                com.android.server.power.stats.BatteryStatsImpl.BinderCallStats stats = binderCallStats.valueAt(j);
                totalRecordedCallCount = (int) (((long) totalRecordedCallCount) + stats.recordedCallCount);
                totalRecordedCallTimeMicros += stats.recordedCpuTimeMicros;
            }
        }
        long totalSystemServiceTimeMicros2 = 0;
        for (int i2 = 0; i2 < this.mUidStats.size(); i2++) {
            com.android.server.power.stats.BatteryStatsImpl.Uid uid = this.mUidStats.valueAt(i2);
            long totalTimeForUidUs = 0;
            int totalCallCountForUid = 0;
            android.util.ArraySet<com.android.server.power.stats.BatteryStatsImpl.BinderCallStats> binderCallStats2 = uid.mBinderCallStats;
            int j2 = binderCallStats2.size() - 1;
            while (j2 >= 0) {
                com.android.server.power.stats.BatteryStatsImpl.BinderCallStats stats2 = binderCallStats2.valueAt(j2);
                long totalSystemServiceTimeMicros3 = totalSystemServiceTimeMicros2;
                long totalSystemServiceTimeMicros4 = stats2.callCount;
                totalCallCountForUid = (int) (((long) totalCallCountForUid) + totalSystemServiceTimeMicros4);
                if (stats2.recordedCallCount > 0) {
                    totalTimeForUidUs += (stats2.callCount * stats2.recordedCpuTimeMicros) / stats2.recordedCallCount;
                } else if (totalRecordedCallCount > 0) {
                    totalTimeForUidUs += (stats2.callCount * totalRecordedCallTimeMicros) / ((long) totalRecordedCallCount);
                }
                j2--;
                totalSystemServiceTimeMicros2 = totalSystemServiceTimeMicros3;
            }
            long totalSystemServiceTimeMicros5 = totalSystemServiceTimeMicros2;
            long totalSystemServiceTimeMicros6 = totalCallCountForUid;
            if (totalSystemServiceTimeMicros6 < uid.mBinderCallCount && totalRecordedCallCount > 0) {
                totalTimeForUidUs += ((uid.mBinderCallCount - ((long) totalCallCountForUid)) * totalRecordedCallTimeMicros) / ((long) totalRecordedCallCount);
            }
            uid.mSystemServiceTimeUs = totalTimeForUidUs;
            totalSystemServiceTimeMicros2 = totalSystemServiceTimeMicros5 + totalTimeForUidUs;
        }
        long totalSystemServiceTimeMicros7 = totalSystemServiceTimeMicros2;
        int i3 = 0;
        while (i3 < this.mUidStats.size()) {
            com.android.server.power.stats.BatteryStatsImpl.Uid uid2 = this.mUidStats.valueAt(i3);
            if (totalSystemServiceTimeMicros7 > 0) {
                totalSystemServiceTimeMicros = totalSystemServiceTimeMicros7;
                uid2.mProportionalSystemServiceUsage = uid2.mSystemServiceTimeUs / totalSystemServiceTimeMicros;
            } else {
                totalSystemServiceTimeMicros = totalSystemServiceTimeMicros7;
                uid2.mProportionalSystemServiceUsage = 0.0d;
            }
            i3++;
            totalSystemServiceTimeMicros7 = totalSystemServiceTimeMicros;
        }
    }

    public java.lang.String[] getWifiIfaces() {
        java.lang.String[] strArr;
        synchronized (this.mWifiNetworkLock) {
            strArr = this.mWifiIfaces;
        }
        return strArr;
    }

    public java.lang.String[] getMobileIfaces() {
        java.lang.String[] strArr;
        synchronized (this.mModemNetworkLock) {
            strArr = this.mModemIfaces;
        }
        return strArr;
    }

    public long getScreenOnTime(long elapsedRealtimeUs, int which) {
        return this.mScreenOnTimer.getTotalTimeLocked(elapsedRealtimeUs, which);
    }

    public int getScreenOnCount(int which) {
        return this.mScreenOnTimer.getCountLocked(which);
    }

    public long getScreenDozeTime(long elapsedRealtimeUs, int which) {
        return this.mScreenDozeTimer.getTotalTimeLocked(elapsedRealtimeUs, which);
    }

    public int getScreenDozeCount(int which) {
        return this.mScreenDozeTimer.getCountLocked(which);
    }

    public long getScreenBrightnessTime(int brightnessBin, long elapsedRealtimeUs, int which) {
        return this.mScreenBrightnessTimer[brightnessBin].getTotalTimeLocked(elapsedRealtimeUs, which);
    }

    public com.android.server.power.stats.BatteryStatsImpl.Timer getScreenBrightnessTimer(int brightnessBin) {
        return this.mScreenBrightnessTimer[brightnessBin];
    }

    public int getDisplayCount() {
        return this.mPerDisplayBatteryStats.length;
    }

    public long getDisplayScreenOnTime(int display, long elapsedRealtimeUs) {
        return this.mPerDisplayBatteryStats[display].screenOnTimer.getTotalTimeLocked(elapsedRealtimeUs, 0);
    }

    public long getDisplayScreenDozeTime(int display, long elapsedRealtimeUs) {
        return this.mPerDisplayBatteryStats[display].screenDozeTimer.getTotalTimeLocked(elapsedRealtimeUs, 0);
    }

    public long getDisplayScreenBrightnessTime(int display, int brightnessBin, long elapsedRealtimeUs) {
        com.android.server.power.stats.BatteryStatsImpl.DisplayBatteryStats displayStats = this.mPerDisplayBatteryStats[display];
        return displayStats.screenBrightnessTimers[brightnessBin].getTotalTimeLocked(elapsedRealtimeUs, 0);
    }

    public long getInteractiveTime(long elapsedRealtimeUs, int which) {
        return this.mInteractiveTimer.getTotalTimeLocked(elapsedRealtimeUs, which);
    }

    public long getPowerSaveModeEnabledTime(long elapsedRealtimeUs, int which) {
        return this.mPowerSaveModeEnabledTimer.getTotalTimeLocked(elapsedRealtimeUs, which);
    }

    public int getPowerSaveModeEnabledCount(int which) {
        return this.mPowerSaveModeEnabledTimer.getCountLocked(which);
    }

    public long getDeviceIdleModeTime(int mode, long elapsedRealtimeUs, int which) {
        switch (mode) {
            case 1:
                return this.mDeviceIdleModeLightTimer.getTotalTimeLocked(elapsedRealtimeUs, which);
            case 2:
                return this.mDeviceIdleModeFullTimer.getTotalTimeLocked(elapsedRealtimeUs, which);
            default:
                return 0L;
        }
    }

    public int getDeviceIdleModeCount(int mode, int which) {
        switch (mode) {
            case 1:
                return this.mDeviceIdleModeLightTimer.getCountLocked(which);
            case 2:
                return this.mDeviceIdleModeFullTimer.getCountLocked(which);
            default:
                return 0;
        }
    }

    public long getLongestDeviceIdleModeTime(int mode) {
        switch (mode) {
            case 1:
                return this.mLongestLightIdleTimeMs;
            case 2:
                return this.mLongestFullIdleTimeMs;
            default:
                return 0L;
        }
    }

    public long getDeviceIdlingTime(int mode, long elapsedRealtimeUs, int which) {
        switch (mode) {
            case 1:
                return this.mDeviceLightIdlingTimer.getTotalTimeLocked(elapsedRealtimeUs, which);
            case 2:
                return this.mDeviceIdlingTimer.getTotalTimeLocked(elapsedRealtimeUs, which);
            default:
                return 0L;
        }
    }

    public int getDeviceIdlingCount(int mode, int which) {
        switch (mode) {
            case 1:
                return this.mDeviceLightIdlingTimer.getCountLocked(which);
            case 2:
                return this.mDeviceIdlingTimer.getCountLocked(which);
            default:
                return 0;
        }
    }

    public int getNumConnectivityChange(int which) {
        return this.mNumConnectivityChange;
    }

    public long getGpsSignalQualityTime(int strengthBin, long elapsedRealtimeUs, int which) {
        if (strengthBin < 0 || strengthBin >= this.mGpsSignalQualityTimer.length) {
            return 0L;
        }
        return this.mGpsSignalQualityTimer[strengthBin].getTotalTimeLocked(elapsedRealtimeUs, which);
    }

    public long getGpsBatteryDrainMaMs() {
        if (this.mPowerProfile == null) {
            return 0L;
        }
        double opVolt = this.mPowerProfile.getAveragePower("gps.voltage") / 1000.0d;
        if (opVolt == 0.0d) {
            return 0L;
        }
        double energyUsedMaMs = 0.0d;
        long rawRealtimeUs = android.os.SystemClock.elapsedRealtime() * 1000;
        for (int i = 0; i < this.mGpsSignalQualityTimer.length; i++) {
            energyUsedMaMs += this.mPowerProfile.getAveragePower("gps.signalqualitybased", i) * (getGpsSignalQualityTime(i, rawRealtimeUs, 0) / 1000);
        }
        this.mBatteryStatsImplExt.recordGpsPowerDrainMaMs((long) energyUsedMaMs);
        return (long) energyUsedMaMs;
    }

    public long getPhoneOnTime(long elapsedRealtimeUs, int which) {
        return this.mPhoneOnTimer.getTotalTimeLocked(elapsedRealtimeUs, which);
    }

    public int getPhoneOnCount(int which) {
        return this.mPhoneOnTimer.getCountLocked(which);
    }

    public long getPhoneSignalStrengthTime(int strengthBin, long elapsedRealtimeUs, int which) {
        return this.mPhoneSignalStrengthsTimer[strengthBin].getTotalTimeLocked(elapsedRealtimeUs, which);
    }

    public long getPhoneSignalScanningTime(long elapsedRealtimeUs, int which) {
        return this.mPhoneSignalScanningTimer.getTotalTimeLocked(elapsedRealtimeUs, which);
    }

    public com.android.server.power.stats.BatteryStatsImpl.Timer getPhoneSignalScanningTimer() {
        return this.mPhoneSignalScanningTimer;
    }

    public int getPhoneSignalStrengthCount(int strengthBin, int which) {
        return this.mPhoneSignalStrengthsTimer[strengthBin].getCountLocked(which);
    }

    public com.android.server.power.stats.BatteryStatsImpl.Timer getPhoneSignalStrengthTimer(int strengthBin) {
        return this.mPhoneSignalStrengthsTimer[strengthBin];
    }

    public long getPhoneDataConnectionTime(int dataType, long elapsedRealtimeUs, int which) {
        return this.mPhoneDataConnectionsTimer[dataType].getTotalTimeLocked(elapsedRealtimeUs, which);
    }

    public int getPhoneDataConnectionCount(int dataType, int which) {
        return this.mPhoneDataConnectionsTimer[dataType].getCountLocked(which);
    }

    public com.android.server.power.stats.BatteryStatsImpl.Timer getPhoneDataConnectionTimer(int dataType) {
        return this.mPhoneDataConnectionsTimer[dataType];
    }

    public long getNrNsaTime(long elapsedRealtimeUs) {
        return this.mNrNsaTimer.getTotalTimeLocked(elapsedRealtimeUs, 0);
    }

    public long getActiveRadioDurationMs(int rat, int frequencyRange, int signalStrength, long elapsedRealtimeMs) {
        com.android.server.power.stats.BatteryStatsImpl.RadioAccessTechnologyBatteryStats stats = this.mPerRatBatteryStats[rat];
        if (stats == null) {
            return 0L;
        }
        int freqCount = stats.perStateTimers.length;
        if (frequencyRange < 0 || frequencyRange >= freqCount) {
            return 0L;
        }
        com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer[] strengthTimers = stats.perStateTimers[frequencyRange];
        int strengthCount = strengthTimers.length;
        if (signalStrength < 0 || signalStrength >= strengthCount) {
            return 0L;
        }
        return stats.perStateTimers[frequencyRange][signalStrength].getTotalTimeLocked(elapsedRealtimeMs * 1000, 0) / 1000;
    }

    public long getActiveTxRadioDurationMs(int rat, int frequencyRange, int signalStrength, long elapsedRealtimeMs) {
        com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounter counter;
        com.android.server.power.stats.BatteryStatsImpl.RadioAccessTechnologyBatteryStats stats = this.mPerRatBatteryStats[rat];
        if (stats == null || (counter = stats.getTxDurationCounter(frequencyRange, signalStrength, false)) == null) {
            return -1L;
        }
        return counter.getCountLocked(0);
    }

    public long getActiveRxRadioDurationMs(int rat, int frequencyRange, long elapsedRealtimeMs) {
        com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounter counter;
        com.android.server.power.stats.BatteryStatsImpl.RadioAccessTechnologyBatteryStats stats = this.mPerRatBatteryStats[rat];
        if (stats == null || (counter = stats.getRxDurationCounter(frequencyRange, false)) == null) {
            return -1L;
        }
        return counter.getCountLocked(0);
    }

    public long getMobileRadioActiveTime(long elapsedRealtimeUs, int which) {
        return this.mMobileRadioActiveTimer.getTotalTimeLocked(elapsedRealtimeUs, which);
    }

    public int getMobileRadioActiveCount(int which) {
        return this.mMobileRadioActiveTimer.getCountLocked(which);
    }

    public long getMobileRadioActiveAdjustedTime(int which) {
        return this.mMobileRadioActiveAdjustedTime.getCountLocked(which);
    }

    public long getMobileRadioActiveUnknownTime(int which) {
        return this.mMobileRadioActiveUnknownTime.getCountLocked(which);
    }

    public int getMobileRadioActiveUnknownCount(int which) {
        return (int) this.mMobileRadioActiveUnknownCount.getCountLocked(which);
    }

    public long getWifiMulticastWakelockTime(long elapsedRealtimeUs, int which) {
        return this.mWifiMulticastWakelockTimer.getTotalTimeLocked(elapsedRealtimeUs, which);
    }

    public int getWifiMulticastWakelockCount(int which) {
        return this.mWifiMulticastWakelockTimer.getCountLocked(which);
    }

    public long getWifiOnTime(long elapsedRealtimeUs, int which) {
        return this.mWifiOnTimer.getTotalTimeLocked(elapsedRealtimeUs, which);
    }

    public long getWifiActiveTime(long elapsedRealtimeUs, int which) {
        return this.mWifiActiveTimer.getTotalTimeLocked(elapsedRealtimeUs, which);
    }

    public long getGlobalWifiRunningTime(long elapsedRealtimeUs, int which) {
        return this.mGlobalWifiRunningTimer.getTotalTimeLocked(elapsedRealtimeUs, which);
    }

    public long getWifiStateTime(int wifiState, long elapsedRealtimeUs, int which) {
        return this.mWifiStateTimer[wifiState].getTotalTimeLocked(elapsedRealtimeUs, which);
    }

    public int getWifiStateCount(int wifiState, int which) {
        return this.mWifiStateTimer[wifiState].getCountLocked(which);
    }

    public com.android.server.power.stats.BatteryStatsImpl.Timer getWifiStateTimer(int wifiState) {
        return this.mWifiStateTimer[wifiState];
    }

    public long getWifiSupplStateTime(int state, long elapsedRealtimeUs, int which) {
        return this.mWifiSupplStateTimer[state].getTotalTimeLocked(elapsedRealtimeUs, which);
    }

    public int getWifiSupplStateCount(int state, int which) {
        return this.mWifiSupplStateTimer[state].getCountLocked(which);
    }

    public com.android.server.power.stats.BatteryStatsImpl.Timer getWifiSupplStateTimer(int state) {
        return this.mWifiSupplStateTimer[state];
    }

    public long getWifiSignalStrengthTime(int strengthBin, long elapsedRealtimeUs, int which) {
        return this.mWifiSignalStrengthsTimer[strengthBin].getTotalTimeLocked(elapsedRealtimeUs, which);
    }

    public int getWifiSignalStrengthCount(int strengthBin, int which) {
        return this.mWifiSignalStrengthsTimer[strengthBin].getCountLocked(which);
    }

    public com.android.server.power.stats.BatteryStatsImpl.Timer getWifiSignalStrengthTimer(int strengthBin) {
        return this.mWifiSignalStrengthsTimer[strengthBin];
    }

    public android.os.BatteryStats.ControllerActivityCounter getBluetoothControllerActivity() {
        return this.mBluetoothActivity;
    }

    public android.os.BatteryStats.ControllerActivityCounter getWifiControllerActivity() {
        return this.mWifiActivity;
    }

    public android.os.BatteryStats.ControllerActivityCounter getModemControllerActivity() {
        return this.mModemActivity;
    }

    public boolean hasBluetoothActivityReporting() {
        return this.mHasBluetoothReporting;
    }

    public boolean hasWifiActivityReporting() {
        return this.mHasWifiReporting;
    }

    public boolean hasModemActivityReporting() {
        return this.mHasModemReporting;
    }

    public long getFlashlightOnTime(long elapsedRealtimeUs, int which) {
        return this.mFlashlightOnTimer.getTotalTimeLocked(elapsedRealtimeUs, which);
    }

    public long getFlashlightOnCount(int which) {
        return this.mFlashlightOnTimer.getCountLocked(which);
    }

    public long getCameraOnTime(long elapsedRealtimeUs, int which) {
        return this.mCameraOnTimer.getTotalTimeLocked(elapsedRealtimeUs, which);
    }

    public long getBluetoothScanTime(long elapsedRealtimeUs, int which) {
        return this.mBluetoothScanTimer.getTotalTimeLocked(elapsedRealtimeUs, which);
    }

    public long getNetworkActivityBytes(int type, int which) {
        if (type >= 0 && type < this.mNetworkByteActivityCounters.length) {
            return this.mNetworkByteActivityCounters[type].getCountLocked(which);
        }
        return 0L;
    }

    public long getNetworkActivityPackets(int type, int which) {
        if (type >= 0 && type < this.mNetworkPacketActivityCounters.length) {
            return this.mNetworkPacketActivityCounters[type].getCountLocked(which);
        }
        return 0L;
    }

    public long getBluetoothEnergyConsumptionUC() {
        return getPowerBucketConsumptionUC(5);
    }

    public long getCpuEnergyConsumptionUC() {
        return getPowerBucketConsumptionUC(3);
    }

    public long getGnssEnergyConsumptionUC() {
        return getPowerBucketConsumptionUC(6);
    }

    public long getMobileRadioEnergyConsumptionUC() {
        return getPowerBucketConsumptionUC(7);
    }

    public long getPhoneEnergyConsumptionUC() {
        return getPowerBucketConsumptionUC(9);
    }

    public long getScreenOnEnergyConsumptionUC() {
        return getPowerBucketConsumptionUC(0);
    }

    public long getScreenDozeEnergyConsumptionUC() {
        return getPowerBucketConsumptionUC(1);
    }

    public long getWifiEnergyConsumptionUC() {
        return getPowerBucketConsumptionUC(4);
    }

    public long getCameraEnergyConsumptionUC() {
        return getPowerBucketConsumptionUC(8);
    }

    private long getPowerBucketConsumptionUC(int bucket) {
        if (this.mGlobalEnergyConsumerStats == null) {
            return -1L;
        }
        return this.mGlobalEnergyConsumerStats.getAccumulatedStandardBucketCharge(bucket);
    }

    public long[] getCustomEnergyConsumerBatteryConsumptionUC() {
        if (this.mGlobalEnergyConsumerStats == null) {
            return null;
        }
        return this.mGlobalEnergyConsumerStats.getAccumulatedCustomBucketCharges();
    }

    public java.lang.String[] getCustomEnergyConsumerNames() {
        synchronized (this) {
            if (this.mEnergyConsumerStatsConfig == null) {
                return new java.lang.String[0];
            }
            java.lang.String[] names = this.mEnergyConsumerStatsConfig.getCustomBucketNames();
            for (int i = 0; i < names.length; i++) {
                if (android.text.TextUtils.isEmpty(names[i])) {
                    names[i] = "CUSTOM_1000" + i;
                }
            }
            return names;
        }
    }

    public long getStartClockTime() {
        long j;
        synchronized (this) {
            long currentTimeMs = this.mClock.currentTimeMillis();
            if ((currentTimeMs > 31536000000L && this.mStartClockTimeMs < currentTimeMs - 31536000000L) || this.mStartClockTimeMs > currentTimeMs) {
                this.mHistory.recordCurrentTimeChange(this.mClock.elapsedRealtime(), this.mClock.uptimeMillis(), currentTimeMs);
                adjustStartClockTime(currentTimeMs);
            }
            j = this.mStartClockTimeMs;
        }
        return j;
    }

    public long getMonotonicStartTime() {
        return this.mMonotonicStartTime;
    }

    public long getMonotonicEndTime() {
        return this.mMonotonicEndTime;
    }

    public java.lang.String getStartPlatformVersion() {
        return this.mStartPlatformVersion;
    }

    public java.lang.String getEndPlatformVersion() {
        return this.mEndPlatformVersion;
    }

    public int getParcelVersion() {
        return VERSION;
    }

    public boolean getIsOnBattery() {
        return this.mOnBattery;
    }

    public long getStatsStartRealtime() {
        return this.mRealtimeStartUs;
    }

    public android.util.SparseArray<? extends android.os.BatteryStats.Uid> getUidStats() {
        return this.mUidStats;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <T extends com.android.server.power.stats.BatteryStatsImpl.TimeBaseObs> boolean resetIfNotNull(T t, boolean detachIfReset, long elapsedRealtimeUs) {
        if (t != null) {
            return t.reset(detachIfReset, elapsedRealtimeUs);
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <T extends com.android.server.power.stats.BatteryStatsImpl.TimeBaseObs> boolean resetIfNotNull(T[] t, boolean detachIfReset, long elapsedRealtimeUs) {
        if (t != null) {
            boolean ret = true;
            for (T t2 : t) {
                ret &= resetIfNotNull(t2, detachIfReset, elapsedRealtimeUs);
            }
            return ret;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <T extends com.android.server.power.stats.BatteryStatsImpl.TimeBaseObs> boolean resetIfNotNull(T[][] t, boolean detachIfReset, long elapsedRealtimeUs) {
        if (t != null) {
            boolean ret = true;
            for (T[] tArr : t) {
                ret &= resetIfNotNull(tArr, detachIfReset, elapsedRealtimeUs);
            }
            return ret;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean resetIfNotNull(com.android.server.power.stats.BatteryStatsImpl.ControllerActivityCounterImpl counter, boolean detachIfReset, long elapsedRealtimeUs) {
        if (counter != null) {
            counter.reset(detachIfReset, elapsedRealtimeUs);
            return true;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <T extends com.android.server.power.stats.BatteryStatsImpl.TimeBaseObs> void detachIfNotNull(T t) {
        if (t != null) {
            t.detach();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <T extends com.android.server.power.stats.BatteryStatsImpl.TimeBaseObs> void detachIfNotNull(T[] t) {
        if (t != null) {
            for (T t2 : t) {
                detachIfNotNull(t2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <T extends com.android.server.power.stats.BatteryStatsImpl.TimeBaseObs> void detachIfNotNull(T[][] t) {
        if (t != null) {
            for (T[] tArr : t) {
                detachIfNotNull(tArr);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void detachIfNotNull(com.android.server.power.stats.BatteryStatsImpl.ControllerActivityCounterImpl counter) {
        if (counter != null) {
            counter.detach();
        }
    }

    protected static class BinderCallStats {
        public java.lang.Class<? extends android.os.Binder> binderClass;
        public long callCount;
        public java.lang.String methodName;
        public long recordedCallCount;
        public long recordedCpuTimeMicros;
        public int transactionCode;

        protected BinderCallStats() {
        }

        public int hashCode() {
            return (this.binderClass.hashCode() * 31) + this.transactionCode;
        }

        public boolean equals(java.lang.Object obj) {
            if (!(obj instanceof com.android.server.power.stats.BatteryStatsImpl.BinderCallStats)) {
                return false;
            }
            com.android.server.power.stats.BatteryStatsImpl.BinderCallStats bcsk = (com.android.server.power.stats.BatteryStatsImpl.BinderCallStats) obj;
            return this.binderClass.equals(bcsk.binderClass) && this.transactionCode == bcsk.transactionCode;
        }

        public java.lang.String getClassName() {
            return this.binderClass.getName();
        }

        public java.lang.String getMethodName() {
            return this.methodName;
        }

        public void ensureMethodName(com.android.internal.os.BinderTransactionNameResolver resolver) {
            if (this.methodName == null) {
                this.methodName = resolver.getMethodName(this.binderClass, this.transactionCode);
            }
        }

        public java.lang.String toString() {
            return "BinderCallStats{" + this.binderClass + " transaction=" + this.transactionCode + " callCount=" + this.callCount + " recordedCallCount=" + this.recordedCallCount + " recorderCpuTimeMicros=" + this.recordedCpuTimeMicros + "}";
        }
    }

    public static class Uid extends android.os.BatteryStats.Uid {
        static final int NO_BATCHED_SCAN_STARTED = -1;
        private static com.android.server.power.stats.BatteryStatsImpl.BinderCallStats sTempBinderCallStats = new com.android.server.power.stats.BatteryStatsImpl.BinderCallStats();
        com.android.server.power.stats.BatteryStatsImpl.DualTimer mAggregatedPartialWakelockTimer;
        com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer mAudioTurnedOnTimer;
        private long mBinderCallCount;
        private com.android.server.power.stats.BatteryStatsImpl.ControllerActivityCounterImpl mBluetoothControllerActivity;
        com.android.server.power.stats.BatteryStatsImpl.Counter mBluetoothScanResultBgCounter;
        com.android.server.power.stats.BatteryStatsImpl.Counter mBluetoothScanResultCounter;
        com.android.server.power.stats.BatteryStatsImpl.DualTimer mBluetoothScanTimer;
        com.android.server.power.stats.BatteryStatsImpl.DualTimer mBluetoothUnoptimizedScanTimer;
        protected com.android.server.power.stats.BatteryStatsImpl mBsi;
        com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer mCameraTurnedOnTimer;
        android.util.SparseArray<com.android.server.power.stats.BatteryStatsImpl.Uid.ChildUid> mChildUids;
        com.android.server.power.stats.BatteryStatsImpl.TimeMultiStateCounter mCpuActiveTimeMs;
        com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounter[][] mCpuClusterSpeedTimesUs;
        com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounterArray mCpuClusterTimesMs;
        com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounterArray mCpuFreqTimeMs;
        long mCurStepSystemTimeMs;
        long mCurStepUserTimeMs;
        com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer mFlashlightTurnedOnTimer;
        com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer mForegroundActivityTimer;
        com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer mForegroundServiceTimer;
        boolean mFullWifiLockOut;
        com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer mFullWifiLockTimer;
        final com.android.server.power.stats.BatteryStatsImpl.OverflowArrayMap<com.android.server.power.stats.BatteryStatsImpl.DualTimer> mJobStats;
        com.android.server.power.stats.BatteryStatsImpl.Counter mJobsDeferredCount;
        com.android.server.power.stats.BatteryStatsImpl.Counter mJobsDeferredEventCount;
        final com.android.server.power.stats.BatteryStatsImpl.Counter[] mJobsFreshnessBuckets;
        com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounter mJobsFreshnessTimeMs;
        long mLastStepSystemTimeMs;
        long mLastStepUserTimeMs;
        com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounter mMobileRadioActiveCount;
        com.android.server.power.stats.BatteryStatsImpl.TimeMultiStateCounter mMobileRadioActiveTime;
        private com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounter mMobileRadioApWakeupCount;
        private com.android.server.power.stats.BatteryStatsImpl.ControllerActivityCounterImpl mModemControllerActivity;
        com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounter[] mNetworkByteActivityCounters;
        com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounter[] mNetworkPacketActivityCounters;
        public final com.android.server.power.stats.BatteryStatsImpl.TimeBase mOnBatteryScreenOffBackgroundTimeBase;
        com.android.server.power.stats.BatteryStatsImpl.TimeInFreqMultiStateCounter mProcStateScreenOffTimeMs;
        com.android.server.power.stats.BatteryStatsImpl.TimeInFreqMultiStateCounter mProcStateTimeMs;
        com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer[] mProcessStateTimer;
        private double mProportionalSystemServiceUsage;
        com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounterArray mScreenOffCpuFreqTimeMs;
        final com.android.server.power.stats.BatteryStatsImpl.OverflowArrayMap<com.android.server.power.stats.BatteryStatsImpl.DualTimer> mSyncStats;
        com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounter mSystemCpuTime;
        private long mSystemServiceTimeUs;
        final int mUid;
        private com.android.internal.power.EnergyConsumerStats mUidEnergyConsumerStats;
        com.android.server.power.stats.BatteryStatsImpl.Counter[] mUserActivityCounters;
        com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounter mUserCpuTime;
        com.android.server.power.stats.BatteryStatsImpl.BatchTimer mVibratorOnTimer;
        com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer mVideoTurnedOnTimer;
        final com.android.server.power.stats.BatteryStatsImpl.OverflowArrayMap<com.android.server.power.stats.BatteryStatsImpl.Uid.Wakelock> mWakelockStats;
        com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer[] mWifiBatchedScanTimer;
        private com.android.server.power.stats.BatteryStatsImpl.ControllerActivityCounterImpl mWifiControllerActivity;
        com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer mWifiMulticastTimer;
        int mWifiMulticastWakelockCount;
        private com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounter mWifiRadioApWakeupCount;
        boolean mWifiRunning;
        com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer mWifiRunningTimer;
        boolean mWifiScanStarted;
        com.android.server.power.stats.BatteryStatsImpl.DualTimer mWifiScanTimer;
        int mWifiBatchedScanBinStarted = -1;
        int mProcessState = 7;
        boolean mInForegroundService = false;
        final android.util.ArrayMap<java.lang.String, android.util.SparseIntArray> mJobCompletions = new android.util.ArrayMap<>();
        final android.util.SparseArray<com.android.server.power.stats.BatteryStatsImpl.Uid.Sensor> mSensorStats = new android.util.SparseArray<>();
        final android.util.ArrayMap<java.lang.String, com.android.server.power.stats.BatteryStatsImpl.Uid.Proc> mProcessStats = new android.util.ArrayMap<>();
        final android.util.ArrayMap<java.lang.String, com.android.server.power.stats.BatteryStatsImpl.Uid.Pkg> mPackageStats = new android.util.ArrayMap<>();
        final android.util.SparseArray<android.os.BatteryStats.Uid.Pid> mPids = new android.util.SparseArray<>();
        private final android.util.ArraySet<com.android.server.power.stats.BatteryStatsImpl.BinderCallStats> mBinderCallStats = new android.util.ArraySet<>();
        public final com.android.server.power.stats.BatteryStatsImpl.TimeBase mOnBatteryBackgroundTimeBase = new com.android.server.power.stats.BatteryStatsImpl.TimeBase(false);

        public Uid(com.android.server.power.stats.BatteryStatsImpl bsi, int uid, long elapsedRealtimeMs, long uptimeMs) {
            this.mBsi = bsi;
            this.mUid = uid;
            this.mOnBatteryBackgroundTimeBase.init(uptimeMs * 1000, elapsedRealtimeMs * 1000);
            this.mOnBatteryScreenOffBackgroundTimeBase = new com.android.server.power.stats.BatteryStatsImpl.TimeBase(false);
            this.mOnBatteryScreenOffBackgroundTimeBase.init(uptimeMs * 1000, elapsedRealtimeMs * 1000);
            this.mUserCpuTime = new com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounter(this.mBsi.mOnBatteryTimeBase);
            this.mSystemCpuTime = new com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounter(this.mBsi.mOnBatteryTimeBase);
            this.mCpuClusterTimesMs = new com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounterArray(this.mBsi.mOnBatteryTimeBase);
            com.android.server.power.stats.BatteryStatsImpl batteryStatsImpl = this.mBsi;
            java.util.Objects.requireNonNull(batteryStatsImpl);
            this.mWakelockStats = new com.android.server.power.stats.BatteryStatsImpl.OverflowArrayMap<com.android.server.power.stats.BatteryStatsImpl.Uid.Wakelock>(batteryStatsImpl, uid) { // from class: com.android.server.power.stats.BatteryStatsImpl.Uid.1
                /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                {
                    super(uid);
                    java.util.Objects.requireNonNull(batteryStatsImpl);
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // com.android.server.power.stats.BatteryStatsImpl.OverflowArrayMap
                public com.android.server.power.stats.BatteryStatsImpl.Uid.Wakelock instantiateObject() {
                    return new com.android.server.power.stats.BatteryStatsImpl.Uid.Wakelock(com.android.server.power.stats.BatteryStatsImpl.Uid.this.mBsi, com.android.server.power.stats.BatteryStatsImpl.Uid.this);
                }
            };
            com.android.server.power.stats.BatteryStatsImpl batteryStatsImpl2 = this.mBsi;
            java.util.Objects.requireNonNull(batteryStatsImpl2);
            this.mSyncStats = new com.android.server.power.stats.BatteryStatsImpl.OverflowArrayMap<com.android.server.power.stats.BatteryStatsImpl.DualTimer>(batteryStatsImpl2, uid) { // from class: com.android.server.power.stats.BatteryStatsImpl.Uid.2
                /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                {
                    super(uid);
                    java.util.Objects.requireNonNull(batteryStatsImpl2);
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // com.android.server.power.stats.BatteryStatsImpl.OverflowArrayMap
                public com.android.server.power.stats.BatteryStatsImpl.DualTimer instantiateObject() {
                    return new com.android.server.power.stats.BatteryStatsImpl.DualTimer(com.android.server.power.stats.BatteryStatsImpl.Uid.this.mBsi.mClock, com.android.server.power.stats.BatteryStatsImpl.Uid.this, 13, null, com.android.server.power.stats.BatteryStatsImpl.Uid.this.mBsi.mOnBatteryTimeBase, com.android.server.power.stats.BatteryStatsImpl.Uid.this.mOnBatteryBackgroundTimeBase);
                }
            };
            com.android.server.power.stats.BatteryStatsImpl batteryStatsImpl3 = this.mBsi;
            java.util.Objects.requireNonNull(batteryStatsImpl3);
            this.mJobStats = new com.android.server.power.stats.BatteryStatsImpl.OverflowArrayMap<com.android.server.power.stats.BatteryStatsImpl.DualTimer>(batteryStatsImpl3, uid) { // from class: com.android.server.power.stats.BatteryStatsImpl.Uid.3
                /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                {
                    super(uid);
                    java.util.Objects.requireNonNull(batteryStatsImpl3);
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // com.android.server.power.stats.BatteryStatsImpl.OverflowArrayMap
                public com.android.server.power.stats.BatteryStatsImpl.DualTimer instantiateObject() {
                    return new com.android.server.power.stats.BatteryStatsImpl.DualTimer(com.android.server.power.stats.BatteryStatsImpl.Uid.this.mBsi.mClock, com.android.server.power.stats.BatteryStatsImpl.Uid.this, 14, null, com.android.server.power.stats.BatteryStatsImpl.Uid.this.mBsi.mOnBatteryTimeBase, com.android.server.power.stats.BatteryStatsImpl.Uid.this.mOnBatteryBackgroundTimeBase);
                }
            };
            this.mWifiRunningTimer = new com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer(this.mBsi.mClock, this, 4, this.mBsi.mWifiRunningTimers, this.mBsi.mOnBatteryTimeBase);
            this.mFullWifiLockTimer = new com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer(this.mBsi.mClock, this, 5, this.mBsi.mFullWifiLockTimers, this.mBsi.mOnBatteryTimeBase);
            this.mWifiScanTimer = new com.android.server.power.stats.BatteryStatsImpl.DualTimer(this.mBsi.mClock, this, 6, this.mBsi.mWifiScanTimers, this.mBsi.mOnBatteryTimeBase, this.mOnBatteryBackgroundTimeBase);
            this.mWifiBatchedScanTimer = new com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer[5];
            this.mWifiMulticastTimer = new com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer(this.mBsi.mClock, this, 7, this.mBsi.mWifiMulticastTimers, this.mBsi.mOnBatteryTimeBase);
            this.mProcessStateTimer = new com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer[7];
            this.mJobsDeferredEventCount = new com.android.server.power.stats.BatteryStatsImpl.Counter(this.mBsi.mOnBatteryTimeBase);
            this.mJobsDeferredCount = new com.android.server.power.stats.BatteryStatsImpl.Counter(this.mBsi.mOnBatteryTimeBase);
            this.mJobsFreshnessTimeMs = new com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounter(this.mBsi.mOnBatteryTimeBase);
            this.mJobsFreshnessBuckets = new com.android.server.power.stats.BatteryStatsImpl.Counter[android.os.BatteryStats.JOB_FRESHNESS_BUCKETS.length];
        }

        public void setProcessStateForTest(int procState, long elapsedTimeMs) {
            this.mProcessState = procState;
            getProcStateTimeCounter(elapsedTimeMs).setState(procState, elapsedTimeMs);
            getProcStateScreenOffTimeCounter(elapsedTimeMs).setState(procState, elapsedTimeMs);
            int batteryConsumerProcessState = android.os.BatteryStats.mapUidProcessStateToBatteryConsumerProcessState(procState);
            getCpuActiveTimeCounter().setState(batteryConsumerProcessState, elapsedTimeMs);
            getMobileRadioActiveTimeCounter().setState(batteryConsumerProcessState, elapsedTimeMs);
            com.android.server.power.stats.BatteryStatsImpl.ControllerActivityCounterImpl wifiControllerActivity = getWifiControllerActivity();
            if (wifiControllerActivity != null) {
                wifiControllerActivity.setState(batteryConsumerProcessState, elapsedTimeMs);
            }
            com.android.server.power.stats.BatteryStatsImpl.ControllerActivityCounterImpl bluetoothControllerActivity = getBluetoothControllerActivity();
            if (bluetoothControllerActivity != null) {
                bluetoothControllerActivity.setState(batteryConsumerProcessState, elapsedTimeMs);
            }
            com.android.internal.power.EnergyConsumerStats energyStats = getOrCreateEnergyConsumerStatsIfSupportedLocked();
            if (energyStats != null) {
                energyStats.setState(batteryConsumerProcessState, elapsedTimeMs);
            }
        }

        public long[] getCpuFreqTimes(int which) {
            return nullIfAllZeros(this.mCpuFreqTimeMs, which);
        }

        public long[] getScreenOffCpuFreqTimes(int which) {
            return nullIfAllZeros(this.mScreenOffCpuFreqTimeMs, which);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public com.android.server.power.stats.BatteryStatsImpl.TimeMultiStateCounter getCpuActiveTimeCounter() {
            if (this.mCpuActiveTimeMs == null) {
                long timestampMs = this.mBsi.mClock.elapsedRealtime();
                this.mCpuActiveTimeMs = new com.android.server.power.stats.BatteryStatsImpl.TimeMultiStateCounter(this.mBsi.mOnBatteryTimeBase, 5, timestampMs);
                this.mCpuActiveTimeMs.setState(android.os.BatteryStats.mapUidProcessStateToBatteryConsumerProcessState(this.mProcessState), timestampMs);
            }
            return this.mCpuActiveTimeMs;
        }

        public long getCpuActiveTime() {
            if (this.mCpuActiveTimeMs == null) {
                return 0L;
            }
            long activeTime = 0;
            for (int procState = 0; procState < 5; procState++) {
                activeTime += this.mCpuActiveTimeMs.getCountForProcessState(procState);
            }
            return activeTime;
        }

        public long getCpuActiveTime(int procState) {
            if (this.mCpuActiveTimeMs == null || procState < 0 || procState >= 5) {
                return 0L;
            }
            return this.mCpuActiveTimeMs.getCountForProcessState(procState);
        }

        public long[] getCpuClusterTimes() {
            return nullIfAllZeros(this.mCpuClusterTimesMs, 0);
        }

        public boolean getCpuFreqTimes(long[] timesInFreqMs, int procState) {
            if (procState < 0 || procState >= 7 || this.mProcStateTimeMs == null) {
                return false;
            }
            if (!this.mBsi.mPerProcStateCpuTimesAvailable) {
                this.mProcStateTimeMs = null;
                return false;
            }
            return this.mProcStateTimeMs.getCountsLocked(timesInFreqMs, procState);
        }

        public boolean getScreenOffCpuFreqTimes(long[] timesInFreqMs, int procState) {
            if (procState < 0 || procState >= 7 || this.mProcStateScreenOffTimeMs == null) {
                return false;
            }
            if (!this.mBsi.mPerProcStateCpuTimesAvailable) {
                this.mProcStateScreenOffTimeMs = null;
                return false;
            }
            return this.mProcStateScreenOffTimeMs.getCountsLocked(timesInFreqMs, procState);
        }

        public long getBinderCallCount() {
            return this.mBinderCallCount;
        }

        public android.util.ArraySet<com.android.server.power.stats.BatteryStatsImpl.BinderCallStats> getBinderCallStats() {
            return this.mBinderCallStats;
        }

        public double getProportionalSystemServiceUsage() {
            return this.mProportionalSystemServiceUsage;
        }

        public void addIsolatedUid(int isolatedUid) {
            if (this.mChildUids == null) {
                this.mChildUids = new android.util.SparseArray<>();
            } else if (this.mChildUids.indexOfKey(isolatedUid) >= 0) {
                return;
            }
            this.mChildUids.put(isolatedUid, new com.android.server.power.stats.BatteryStatsImpl.Uid.ChildUid());
        }

        public void removeIsolatedUid(int isolatedUid) {
            int idx = this.mChildUids == null ? -1 : this.mChildUids.indexOfKey(isolatedUid);
            if (idx < 0) {
                return;
            }
            this.mChildUids.remove(idx);
        }

        com.android.server.power.stats.BatteryStatsImpl.Uid.ChildUid getChildUid(int childUid) {
            if (this.mChildUids == null) {
                return null;
            }
            return this.mChildUids.get(childUid);
        }

        private long[] nullIfAllZeros(com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounterArray cpuTimesMs, int which) {
            long[] counts;
            if (cpuTimesMs == null || (counts = cpuTimesMs.getCountsLocked(which)) == null) {
                return null;
            }
            for (int i = counts.length - 1; i >= 0; i--) {
                if (counts[i] != 0) {
                    return counts;
                }
            }
            return null;
        }

        private void ensureMultiStateCounters(long timestampMs) {
            if (this.mBsi.mPowerStatsCollectorEnabled.get(1)) {
                throw new java.lang.IllegalStateException("Multi-state counters used in streamlined mode");
            }
            if (this.mProcStateTimeMs == null) {
                this.mProcStateTimeMs = new com.android.server.power.stats.BatteryStatsImpl.TimeInFreqMultiStateCounter(this.mBsi.mOnBatteryTimeBase, 8, this.mBsi.mCpuScalingPolicies.getScalingStepCount(), timestampMs);
            }
            if (this.mProcStateScreenOffTimeMs == null) {
                this.mProcStateScreenOffTimeMs = new com.android.server.power.stats.BatteryStatsImpl.TimeInFreqMultiStateCounter(this.mBsi.mOnBatteryScreenOffTimeBase, 8, this.mBsi.mCpuScalingPolicies.getScalingStepCount(), timestampMs);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public com.android.server.power.stats.BatteryStatsImpl.TimeInFreqMultiStateCounter getProcStateTimeCounter(long timestampMs) {
            ensureMultiStateCounters(timestampMs);
            return this.mProcStateTimeMs;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public com.android.server.power.stats.BatteryStatsImpl.TimeInFreqMultiStateCounter getProcStateScreenOffTimeCounter(long timestampMs) {
            ensureMultiStateCounters(timestampMs);
            return this.mProcStateScreenOffTimeMs;
        }

        public com.android.server.power.stats.BatteryStatsImpl.Timer getAggregatedPartialWakelockTimer() {
            return this.mAggregatedPartialWakelockTimer;
        }

        public android.util.ArrayMap<java.lang.String, ? extends android.os.BatteryStats.Uid.Wakelock> getWakelockStats() {
            return this.mWakelockStats.getMap();
        }

        public com.android.server.power.stats.BatteryStatsImpl.Timer getMulticastWakelockStats() {
            return this.mWifiMulticastTimer;
        }

        public android.util.ArrayMap<java.lang.String, ? extends android.os.BatteryStats.Timer> getSyncStats() {
            return this.mSyncStats.getMap();
        }

        public android.util.ArrayMap<java.lang.String, ? extends android.os.BatteryStats.Timer> getJobStats() {
            return this.mJobStats.getMap();
        }

        public android.util.ArrayMap<java.lang.String, android.util.SparseIntArray> getJobCompletionStats() {
            return this.mJobCompletions;
        }

        public android.util.SparseArray<? extends android.os.BatteryStats.Uid.Sensor> getSensorStats() {
            return this.mSensorStats;
        }

        public android.util.ArrayMap<java.lang.String, ? extends android.os.BatteryStats.Uid.Proc> getProcessStats() {
            return this.mProcessStats;
        }

        public android.util.ArrayMap<java.lang.String, ? extends android.os.BatteryStats.Uid.Pkg> getPackageStats() {
            return this.mPackageStats;
        }

        public int getUid() {
            return this.mUid;
        }

        public void noteWifiRunningLocked(long elapsedRealtimeMs) {
            if (!this.mWifiRunning) {
                this.mWifiRunning = true;
                if (this.mWifiRunningTimer == null) {
                    this.mWifiRunningTimer = new com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer(this.mBsi.mClock, this, 4, this.mBsi.mWifiRunningTimers, this.mBsi.mOnBatteryTimeBase);
                }
                this.mWifiRunningTimer.startRunningLocked(elapsedRealtimeMs);
            }
        }

        public void noteWifiStoppedLocked(long elapsedRealtimeMs) {
            if (this.mWifiRunning) {
                this.mWifiRunning = false;
                this.mWifiRunningTimer.stopRunningLocked(elapsedRealtimeMs);
            }
        }

        public void noteFullWifiLockAcquiredLocked(long elapsedRealtimeMs) {
            if (!this.mFullWifiLockOut) {
                this.mFullWifiLockOut = true;
                if (this.mFullWifiLockTimer == null) {
                    this.mFullWifiLockTimer = new com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer(this.mBsi.mClock, this, 5, this.mBsi.mFullWifiLockTimers, this.mBsi.mOnBatteryTimeBase);
                }
                this.mFullWifiLockTimer.startRunningLocked(elapsedRealtimeMs);
            }
        }

        public void noteFullWifiLockReleasedLocked(long elapsedRealtimeMs) {
            if (this.mFullWifiLockOut) {
                this.mFullWifiLockOut = false;
                this.mFullWifiLockTimer.stopRunningLocked(elapsedRealtimeMs);
            }
        }

        public void noteWifiScanStartedLocked(long elapsedRealtimeMs) {
            if (!this.mWifiScanStarted) {
                this.mWifiScanStarted = true;
                if (this.mWifiScanTimer == null) {
                    this.mWifiScanTimer = new com.android.server.power.stats.BatteryStatsImpl.DualTimer(this.mBsi.mClock, this, 6, this.mBsi.mWifiScanTimers, this.mBsi.mOnBatteryTimeBase, this.mOnBatteryBackgroundTimeBase);
                }
                this.mWifiScanTimer.startRunningLocked(elapsedRealtimeMs);
            }
        }

        public void noteWifiScanStoppedLocked(long elapsedRealtimeMs) {
            if (this.mWifiScanStarted) {
                this.mWifiScanStarted = false;
                this.mWifiScanTimer.stopRunningLocked(elapsedRealtimeMs);
            }
        }

        public void noteWifiBatchedScanStartedLocked(int csph, long elapsedRealtimeMs) {
            int bin = 0;
            while (csph > 8 && bin < 4) {
                csph >>= 3;
                bin++;
            }
            if (this.mWifiBatchedScanBinStarted == bin) {
                return;
            }
            if (this.mWifiBatchedScanBinStarted != -1) {
                this.mWifiBatchedScanTimer[this.mWifiBatchedScanBinStarted].stopRunningLocked(elapsedRealtimeMs);
            }
            this.mWifiBatchedScanBinStarted = bin;
            if (this.mWifiBatchedScanTimer[bin] == null) {
                makeWifiBatchedScanBin(bin, null);
            }
            this.mWifiBatchedScanTimer[bin].startRunningLocked(elapsedRealtimeMs);
        }

        public void noteWifiBatchedScanStoppedLocked(long elapsedRealtimeMs) {
            if (this.mWifiBatchedScanBinStarted != -1) {
                this.mWifiBatchedScanTimer[this.mWifiBatchedScanBinStarted].stopRunningLocked(elapsedRealtimeMs);
                this.mWifiBatchedScanBinStarted = -1;
            }
        }

        public void noteWifiMulticastEnabledLocked(long elapsedRealtimeMs) {
            if (this.mWifiMulticastWakelockCount == 0) {
                if (this.mWifiMulticastTimer == null) {
                    this.mWifiMulticastTimer = new com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer(this.mBsi.mClock, this, 7, this.mBsi.mWifiMulticastTimers, this.mBsi.mOnBatteryTimeBase);
                }
                this.mWifiMulticastTimer.startRunningLocked(elapsedRealtimeMs);
            }
            this.mWifiMulticastWakelockCount++;
        }

        public void noteWifiMulticastDisabledLocked(long elapsedRealtimeMs) {
            if (this.mWifiMulticastWakelockCount == 0) {
                return;
            }
            this.mWifiMulticastWakelockCount--;
            if (this.mWifiMulticastWakelockCount == 0) {
                this.mWifiMulticastTimer.stopRunningLocked(elapsedRealtimeMs);
            }
        }

        public com.android.server.power.stats.BatteryStatsImpl.ControllerActivityCounterImpl getWifiControllerActivity() {
            return this.mWifiControllerActivity;
        }

        public com.android.server.power.stats.BatteryStatsImpl.ControllerActivityCounterImpl getBluetoothControllerActivity() {
            return this.mBluetoothControllerActivity;
        }

        public android.os.BatteryStats.ControllerActivityCounter getModemControllerActivity() {
            return this.mModemControllerActivity;
        }

        public com.android.server.power.stats.BatteryStatsImpl.ControllerActivityCounterImpl getOrCreateWifiControllerActivityLocked() {
            if (this.mWifiControllerActivity == null) {
                this.mWifiControllerActivity = new com.android.server.power.stats.BatteryStatsImpl.ControllerActivityCounterImpl(this.mBsi.mClock, this.mBsi.mOnBatteryTimeBase, 1);
            }
            return this.mWifiControllerActivity;
        }

        public com.android.server.power.stats.BatteryStatsImpl.ControllerActivityCounterImpl getOrCreateBluetoothControllerActivityLocked() {
            if (this.mBluetoothControllerActivity == null) {
                this.mBluetoothControllerActivity = new com.android.server.power.stats.BatteryStatsImpl.ControllerActivityCounterImpl(this.mBsi.mClock, this.mBsi.mOnBatteryTimeBase, 1);
            }
            return this.mBluetoothControllerActivity;
        }

        public com.android.server.power.stats.BatteryStatsImpl.ControllerActivityCounterImpl getOrCreateModemControllerActivityLocked() {
            if (this.mModemControllerActivity == null) {
                this.mModemControllerActivity = new com.android.server.power.stats.BatteryStatsImpl.ControllerActivityCounterImpl(this.mBsi.mClock, this.mBsi.mOnBatteryTimeBase, com.android.server.power.stats.BatteryStatsImpl.MODEM_TX_POWER_LEVEL_COUNT);
            }
            return this.mModemControllerActivity;
        }

        private com.android.internal.power.EnergyConsumerStats getOrCreateEnergyConsumerStatsLocked() {
            if (this.mUidEnergyConsumerStats == null) {
                this.mUidEnergyConsumerStats = new com.android.internal.power.EnergyConsumerStats(this.mBsi.mEnergyConsumerStatsConfig);
            }
            return this.mUidEnergyConsumerStats;
        }

        private com.android.internal.power.EnergyConsumerStats getOrCreateEnergyConsumerStatsIfSupportedLocked() {
            if (this.mUidEnergyConsumerStats == null && this.mBsi.mEnergyConsumerStatsConfig != null) {
                this.mUidEnergyConsumerStats = new com.android.internal.power.EnergyConsumerStats(this.mBsi.mEnergyConsumerStatsConfig);
            }
            return this.mUidEnergyConsumerStats;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addChargeToStandardBucketLocked(long chargeDeltaUC, int powerBucket, long timestampMs) {
            com.android.internal.power.EnergyConsumerStats energyConsumerStats = getOrCreateEnergyConsumerStatsLocked();
            energyConsumerStats.updateStandardBucket(powerBucket, chargeDeltaUC, timestampMs);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addChargeToCustomBucketLocked(long chargeDeltaUC, int powerBucket) {
            getOrCreateEnergyConsumerStatsLocked().updateCustomBucket(powerBucket, chargeDeltaUC, this.mBsi.mClock.elapsedRealtime());
        }

        public long getEnergyConsumptionUC(int bucket) {
            if (this.mBsi.mGlobalEnergyConsumerStats == null || !this.mBsi.mGlobalEnergyConsumerStats.isStandardBucketSupported(bucket)) {
                return -1L;
            }
            if (this.mUidEnergyConsumerStats == null) {
                return 0L;
            }
            return this.mUidEnergyConsumerStats.getAccumulatedStandardBucketCharge(bucket);
        }

        public long getEnergyConsumptionUC(int bucket, int processState) {
            if (this.mBsi.mGlobalEnergyConsumerStats == null || !this.mBsi.mGlobalEnergyConsumerStats.isStandardBucketSupported(bucket)) {
                return -1L;
            }
            if (this.mUidEnergyConsumerStats == null) {
                return 0L;
            }
            return this.mUidEnergyConsumerStats.getAccumulatedStandardBucketCharge(bucket, processState);
        }

        public long[] getCustomEnergyConsumerBatteryConsumptionUC() {
            if (this.mBsi.mGlobalEnergyConsumerStats == null) {
                return null;
            }
            if (this.mUidEnergyConsumerStats == null) {
                return new long[this.mBsi.mGlobalEnergyConsumerStats.getNumberCustomPowerBuckets()];
            }
            return this.mUidEnergyConsumerStats.getAccumulatedCustomBucketCharges();
        }

        public long getBluetoothEnergyConsumptionUC() {
            return getEnergyConsumptionUC(5);
        }

        public long getBluetoothEnergyConsumptionUC(int processState) {
            return getEnergyConsumptionUC(5, processState);
        }

        public long getCpuEnergyConsumptionUC() {
            return getEnergyConsumptionUC(3);
        }

        public long getCpuEnergyConsumptionUC(int processState) {
            return getEnergyConsumptionUC(3, processState);
        }

        public long getGnssEnergyConsumptionUC() {
            return getEnergyConsumptionUC(6);
        }

        public long getMobileRadioEnergyConsumptionUC() {
            return getEnergyConsumptionUC(7);
        }

        public long getMobileRadioEnergyConsumptionUC(int processState) {
            return getEnergyConsumptionUC(7, processState);
        }

        public long getScreenOnEnergyConsumptionUC() {
            return getEnergyConsumptionUC(0);
        }

        public long getWifiEnergyConsumptionUC() {
            return getEnergyConsumptionUC(4);
        }

        public long getWifiEnergyConsumptionUC(int processState) {
            return getEnergyConsumptionUC(4, processState);
        }

        public long getCameraEnergyConsumptionUC() {
            return getEnergyConsumptionUC(8);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public long markProcessForegroundTimeUs(long elapsedRealtimeMs, boolean doCalc) {
            long fgTimeUs = 0;
            com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer fgTimer = this.mForegroundActivityTimer;
            if (fgTimer != null) {
                if (doCalc) {
                    fgTimeUs = fgTimer.getTimeSinceMarkLocked(elapsedRealtimeMs * 1000);
                }
                fgTimer.setMark(elapsedRealtimeMs);
            }
            long topTimeUs = 0;
            com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer topTimer = this.mProcessStateTimer[0];
            if (topTimer != null) {
                if (doCalc) {
                    topTimeUs = topTimer.getTimeSinceMarkLocked(1000 * elapsedRealtimeMs);
                }
                topTimer.setMark(elapsedRealtimeMs);
            }
            return topTimeUs < fgTimeUs ? topTimeUs : fgTimeUs;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public long markGnssTimeUs(long elapsedRealtimeMs) {
            com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer timer;
            com.android.server.power.stats.BatteryStatsImpl.Uid.Sensor sensor = this.mSensorStats.get(-10000);
            if (sensor == null || (timer = sensor.mTimer) == null) {
                return 0L;
            }
            long gnssTimeUs = timer.getTimeSinceMarkLocked(1000 * elapsedRealtimeMs);
            timer.setMark(elapsedRealtimeMs);
            return gnssTimeUs;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public long markCameraTimeUs(long elapsedRealtimeMs) {
            com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer timer = this.mCameraTurnedOnTimer;
            if (timer == null) {
                return 0L;
            }
            long cameraTimeUs = timer.getTimeSinceMarkLocked(1000 * elapsedRealtimeMs);
            timer.setMark(elapsedRealtimeMs);
            return cameraTimeUs;
        }

        public com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer createAudioTurnedOnTimerLocked() {
            if (this.mAudioTurnedOnTimer == null) {
                this.mAudioTurnedOnTimer = new com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer(this.mBsi.mClock, this, 15, this.mBsi.mAudioTurnedOnTimers, this.mBsi.mOnBatteryTimeBase);
            }
            return this.mAudioTurnedOnTimer;
        }

        public void noteAudioTurnedOnLocked(long elapsedRealtimeMs) {
            createAudioTurnedOnTimerLocked().startRunningLocked(elapsedRealtimeMs);
        }

        public void noteAudioTurnedOffLocked(long elapsedRealtimeMs) {
            if (this.mAudioTurnedOnTimer != null) {
                this.mAudioTurnedOnTimer.stopRunningLocked(elapsedRealtimeMs);
            }
        }

        public void noteResetAudioLocked(long elapsedRealtimeMs) {
            if (this.mAudioTurnedOnTimer != null) {
                this.mAudioTurnedOnTimer.stopAllRunningLocked(elapsedRealtimeMs);
            }
        }

        public com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer createVideoTurnedOnTimerLocked() {
            if (this.mVideoTurnedOnTimer == null) {
                this.mVideoTurnedOnTimer = new com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer(this.mBsi.mClock, this, 8, this.mBsi.mVideoTurnedOnTimers, this.mBsi.mOnBatteryTimeBase);
            }
            return this.mVideoTurnedOnTimer;
        }

        public void noteVideoTurnedOnLocked(long elapsedRealtimeMs) {
            createVideoTurnedOnTimerLocked().startRunningLocked(elapsedRealtimeMs);
        }

        public void noteVideoTurnedOffLocked(long elapsedRealtimeMs) {
            if (this.mVideoTurnedOnTimer != null) {
                this.mVideoTurnedOnTimer.stopRunningLocked(elapsedRealtimeMs);
            }
        }

        public void noteResetVideoLocked(long elapsedRealtimeMs) {
            if (this.mVideoTurnedOnTimer != null) {
                this.mVideoTurnedOnTimer.stopAllRunningLocked(elapsedRealtimeMs);
            }
        }

        public com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer createFlashlightTurnedOnTimerLocked() {
            if (this.mFlashlightTurnedOnTimer == null) {
                this.mFlashlightTurnedOnTimer = new com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer(this.mBsi.mClock, this, 16, this.mBsi.mFlashlightTurnedOnTimers, this.mBsi.mOnBatteryTimeBase);
            }
            return this.mFlashlightTurnedOnTimer;
        }

        public void noteFlashlightTurnedOnLocked(long elapsedRealtimeMs) {
            createFlashlightTurnedOnTimerLocked().startRunningLocked(elapsedRealtimeMs);
        }

        public void noteFlashlightTurnedOffLocked(long elapsedRealtimeMs) {
            if (this.mFlashlightTurnedOnTimer != null) {
                this.mFlashlightTurnedOnTimer.stopRunningLocked(elapsedRealtimeMs);
            }
        }

        public void noteResetFlashlightLocked(long elapsedRealtimeMs) {
            if (this.mFlashlightTurnedOnTimer != null) {
                this.mFlashlightTurnedOnTimer.stopAllRunningLocked(elapsedRealtimeMs);
            }
        }

        public com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer createCameraTurnedOnTimerLocked() {
            if (this.mCameraTurnedOnTimer == null) {
                this.mCameraTurnedOnTimer = new com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer(this.mBsi.mClock, this, 17, this.mBsi.mCameraTurnedOnTimers, this.mBsi.mOnBatteryTimeBase);
            }
            return this.mCameraTurnedOnTimer;
        }

        public void noteCameraTurnedOnLocked(long elapsedRealtimeMs) {
            createCameraTurnedOnTimerLocked().startRunningLocked(elapsedRealtimeMs);
        }

        public void noteCameraTurnedOffLocked(long elapsedRealtimeMs) {
            if (this.mCameraTurnedOnTimer != null) {
                this.mCameraTurnedOnTimer.stopRunningLocked(elapsedRealtimeMs);
            }
        }

        public void noteResetCameraLocked(long elapsedRealtimeMs) {
            if (this.mCameraTurnedOnTimer != null) {
                this.mCameraTurnedOnTimer.stopAllRunningLocked(elapsedRealtimeMs);
            }
        }

        public com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer createForegroundActivityTimerLocked() {
            if (this.mForegroundActivityTimer == null) {
                this.mForegroundActivityTimer = new com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer(this.mBsi.mClock, this, 10, null, this.mBsi.mOnBatteryTimeBase);
            }
            return this.mForegroundActivityTimer;
        }

        public com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer createForegroundServiceTimerLocked() {
            if (this.mForegroundServiceTimer == null) {
                this.mForegroundServiceTimer = new com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer(this.mBsi.mClock, this, 22, null, this.mBsi.mOnBatteryTimeBase);
            }
            return this.mForegroundServiceTimer;
        }

        public com.android.server.power.stats.BatteryStatsImpl.DualTimer createAggregatedPartialWakelockTimerLocked() {
            if (this.mAggregatedPartialWakelockTimer == null) {
                this.mAggregatedPartialWakelockTimer = new com.android.server.power.stats.BatteryStatsImpl.DualTimer(this.mBsi.mClock, this, 20, null, this.mBsi.mOnBatteryScreenOffTimeBase, this.mOnBatteryScreenOffBackgroundTimeBase);
            }
            return this.mAggregatedPartialWakelockTimer;
        }

        public com.android.server.power.stats.BatteryStatsImpl.DualTimer createBluetoothScanTimerLocked() {
            if (this.mBluetoothScanTimer == null) {
                this.mBluetoothScanTimer = new com.android.server.power.stats.BatteryStatsImpl.DualTimer(this.mBsi.mClock, this, 19, this.mBsi.mBluetoothScanOnTimers, this.mBsi.mOnBatteryTimeBase, this.mOnBatteryBackgroundTimeBase);
            }
            return this.mBluetoothScanTimer;
        }

        public com.android.server.power.stats.BatteryStatsImpl.DualTimer createBluetoothUnoptimizedScanTimerLocked() {
            if (this.mBluetoothUnoptimizedScanTimer == null) {
                this.mBluetoothUnoptimizedScanTimer = new com.android.server.power.stats.BatteryStatsImpl.DualTimer(this.mBsi.mClock, this, 21, null, this.mBsi.mOnBatteryTimeBase, this.mOnBatteryBackgroundTimeBase);
            }
            return this.mBluetoothUnoptimizedScanTimer;
        }

        public void noteBluetoothScanStartedLocked(long elapsedRealtimeMs, boolean isUnoptimized) {
            createBluetoothScanTimerLocked().startRunningLocked(elapsedRealtimeMs);
            if (isUnoptimized) {
                createBluetoothUnoptimizedScanTimerLocked().startRunningLocked(elapsedRealtimeMs);
            }
        }

        public void noteBluetoothScanStoppedLocked(long elapsedRealtimeMs, boolean isUnoptimized) {
            if (this.mBluetoothScanTimer != null) {
                this.mBluetoothScanTimer.stopRunningLocked(elapsedRealtimeMs);
            }
            if (isUnoptimized && this.mBluetoothUnoptimizedScanTimer != null) {
                this.mBluetoothUnoptimizedScanTimer.stopRunningLocked(elapsedRealtimeMs);
            }
        }

        public void noteResetBluetoothScanLocked(long elapsedRealtimeMs) {
            if (this.mBluetoothScanTimer != null) {
                this.mBluetoothScanTimer.stopAllRunningLocked(elapsedRealtimeMs);
            }
            if (this.mBluetoothUnoptimizedScanTimer != null) {
                this.mBluetoothUnoptimizedScanTimer.stopAllRunningLocked(elapsedRealtimeMs);
            }
        }

        public com.android.server.power.stats.BatteryStatsImpl.Counter createBluetoothScanResultCounterLocked() {
            if (this.mBluetoothScanResultCounter == null) {
                this.mBluetoothScanResultCounter = new com.android.server.power.stats.BatteryStatsImpl.Counter(this.mBsi.mOnBatteryTimeBase);
            }
            return this.mBluetoothScanResultCounter;
        }

        public com.android.server.power.stats.BatteryStatsImpl.Counter createBluetoothScanResultBgCounterLocked() {
            if (this.mBluetoothScanResultBgCounter == null) {
                this.mBluetoothScanResultBgCounter = new com.android.server.power.stats.BatteryStatsImpl.Counter(this.mOnBatteryBackgroundTimeBase);
            }
            return this.mBluetoothScanResultBgCounter;
        }

        public void noteBluetoothScanResultsLocked(int numNewResults) {
            createBluetoothScanResultCounterLocked().addAtomic(numNewResults);
            createBluetoothScanResultBgCounterLocked().addAtomic(numNewResults);
        }

        public void noteActivityResumedLocked(long elapsedRealtimeMs) {
            createForegroundActivityTimerLocked().startRunningLocked(elapsedRealtimeMs);
        }

        public void noteActivityPausedLocked(long elapsedRealtimeMs) {
            if (this.mForegroundActivityTimer != null) {
                this.mForegroundActivityTimer.stopRunningLocked(elapsedRealtimeMs);
            }
        }

        public void noteForegroundServiceResumedLocked(long elapsedRealtimeMs) {
            createForegroundServiceTimerLocked().startRunningLocked(elapsedRealtimeMs);
        }

        public void noteForegroundServicePausedLocked(long elapsedRealtimeMs) {
            if (this.mForegroundServiceTimer != null) {
                this.mForegroundServiceTimer.stopRunningLocked(elapsedRealtimeMs);
            }
        }

        public com.android.server.power.stats.BatteryStatsImpl.BatchTimer createVibratorOnTimerLocked() {
            if (this.mVibratorOnTimer == null) {
                this.mVibratorOnTimer = new com.android.server.power.stats.BatteryStatsImpl.BatchTimer(this.mBsi.mClock, this, 9, this.mBsi.mOnBatteryTimeBase);
            }
            return this.mVibratorOnTimer;
        }

        public void noteVibratorOnLocked(long durationMillis, long elapsedRealtimeMs) {
            createVibratorOnTimerLocked().addDuration(durationMillis, elapsedRealtimeMs);
        }

        public void noteVibratorOffLocked(long elapsedRealtimeMs) {
            if (this.mVibratorOnTimer != null) {
                this.mVibratorOnTimer.abortLastDuration(elapsedRealtimeMs);
            }
        }

        public long getWifiRunningTime(long elapsedRealtimeUs, int which) {
            if (this.mWifiRunningTimer == null) {
                return 0L;
            }
            return this.mWifiRunningTimer.getTotalTimeLocked(elapsedRealtimeUs, which);
        }

        public long getFullWifiLockTime(long elapsedRealtimeUs, int which) {
            if (this.mFullWifiLockTimer == null) {
                return 0L;
            }
            return this.mFullWifiLockTimer.getTotalTimeLocked(elapsedRealtimeUs, which);
        }

        public long getWifiScanTime(long elapsedRealtimeUs, int which) {
            if (this.mWifiScanTimer == null) {
                return 0L;
            }
            return this.mWifiScanTimer.getTotalTimeLocked(elapsedRealtimeUs, which);
        }

        public int getWifiScanCount(int which) {
            if (this.mWifiScanTimer == null) {
                return 0;
            }
            return this.mWifiScanTimer.getCountLocked(which);
        }

        public com.android.server.power.stats.BatteryStatsImpl.Timer getWifiScanTimer() {
            return this.mWifiScanTimer;
        }

        public int getWifiScanBackgroundCount(int which) {
            if (this.mWifiScanTimer == null || this.mWifiScanTimer.getSubTimer() == null) {
                return 0;
            }
            return this.mWifiScanTimer.getSubTimer().getCountLocked(which);
        }

        public long getWifiScanActualTime(long elapsedRealtimeUs) {
            if (this.mWifiScanTimer == null) {
                return 0L;
            }
            long elapsedRealtimeMs = (500 + elapsedRealtimeUs) / 1000;
            return this.mWifiScanTimer.getTotalDurationMsLocked(elapsedRealtimeMs) * 1000;
        }

        public long getWifiScanBackgroundTime(long elapsedRealtimeUs) {
            if (this.mWifiScanTimer == null || this.mWifiScanTimer.getSubTimer() == null) {
                return 0L;
            }
            long elapsedRealtimeMs = (500 + elapsedRealtimeUs) / 1000;
            return this.mWifiScanTimer.getSubTimer().getTotalDurationMsLocked(elapsedRealtimeMs) * 1000;
        }

        public com.android.server.power.stats.BatteryStatsImpl.Timer getWifiScanBackgroundTimer() {
            if (this.mWifiScanTimer == null) {
                return null;
            }
            return this.mWifiScanTimer.getSubTimer();
        }

        public long getWifiBatchedScanTime(int csphBin, long elapsedRealtimeUs, int which) {
            if (csphBin < 0 || csphBin >= 5 || this.mWifiBatchedScanTimer[csphBin] == null) {
                return 0L;
            }
            return this.mWifiBatchedScanTimer[csphBin].getTotalTimeLocked(elapsedRealtimeUs, which);
        }

        public int getWifiBatchedScanCount(int csphBin, int which) {
            if (csphBin < 0 || csphBin >= 5 || this.mWifiBatchedScanTimer[csphBin] == null) {
                return 0;
            }
            return this.mWifiBatchedScanTimer[csphBin].getCountLocked(which);
        }

        public long getWifiMulticastTime(long elapsedRealtimeUs, int which) {
            if (this.mWifiMulticastTimer == null) {
                return 0L;
            }
            return this.mWifiMulticastTimer.getTotalTimeLocked(elapsedRealtimeUs, which);
        }

        public com.android.server.power.stats.BatteryStatsImpl.Timer getAudioTurnedOnTimer() {
            return this.mAudioTurnedOnTimer;
        }

        public com.android.server.power.stats.BatteryStatsImpl.Timer getVideoTurnedOnTimer() {
            return this.mVideoTurnedOnTimer;
        }

        public com.android.server.power.stats.BatteryStatsImpl.Timer getFlashlightTurnedOnTimer() {
            return this.mFlashlightTurnedOnTimer;
        }

        public com.android.server.power.stats.BatteryStatsImpl.Timer getCameraTurnedOnTimer() {
            return this.mCameraTurnedOnTimer;
        }

        public com.android.server.power.stats.BatteryStatsImpl.Timer getForegroundActivityTimer() {
            return this.mForegroundActivityTimer;
        }

        public com.android.server.power.stats.BatteryStatsImpl.Timer getForegroundServiceTimer() {
            return this.mForegroundServiceTimer;
        }

        public com.android.server.power.stats.BatteryStatsImpl.Timer getBluetoothScanTimer() {
            return this.mBluetoothScanTimer;
        }

        public com.android.server.power.stats.BatteryStatsImpl.Timer getBluetoothScanBackgroundTimer() {
            if (this.mBluetoothScanTimer == null) {
                return null;
            }
            return this.mBluetoothScanTimer.getSubTimer();
        }

        public com.android.server.power.stats.BatteryStatsImpl.Timer getBluetoothUnoptimizedScanTimer() {
            return this.mBluetoothUnoptimizedScanTimer;
        }

        public com.android.server.power.stats.BatteryStatsImpl.Timer getBluetoothUnoptimizedScanBackgroundTimer() {
            if (this.mBluetoothUnoptimizedScanTimer == null) {
                return null;
            }
            return this.mBluetoothUnoptimizedScanTimer.getSubTimer();
        }

        public com.android.server.power.stats.BatteryStatsImpl.Counter getBluetoothScanResultCounter() {
            return this.mBluetoothScanResultCounter;
        }

        public com.android.server.power.stats.BatteryStatsImpl.Counter getBluetoothScanResultBgCounter() {
            return this.mBluetoothScanResultBgCounter;
        }

        void makeProcessState(int i, android.os.Parcel in) {
            if (i < 0 || i >= 7) {
                return;
            }
            com.android.server.power.stats.BatteryStatsImpl.detachIfNotNull(this.mProcessStateTimer[i]);
            if (in == null) {
                this.mProcessStateTimer[i] = new com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer(this.mBsi.mClock, this, 12, null, this.mBsi.mOnBatteryTimeBase);
            } else {
                this.mProcessStateTimer[i] = new com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer(this.mBsi.mClock, this, 12, null, this.mBsi.mOnBatteryTimeBase, in);
            }
        }

        public long getProcessStateTime(int state, long elapsedRealtimeUs, int which) {
            if (state < 0 || state >= 7 || this.mProcessStateTimer[state] == null) {
                return 0L;
            }
            return this.mProcessStateTimer[state].getTotalTimeLocked(elapsedRealtimeUs, which);
        }

        public com.android.server.power.stats.BatteryStatsImpl.Timer getProcessStateTimer(int state) {
            if (state < 0 || state >= 7) {
                return null;
            }
            return this.mProcessStateTimer[state];
        }

        public com.android.server.power.stats.BatteryStatsImpl.Timer getVibratorOnTimer() {
            return this.mVibratorOnTimer;
        }

        public void noteUserActivityLocked(int event) {
            if (this.mUserActivityCounters == null) {
                initUserActivityLocked();
            }
            if (event >= 0 && event < NUM_USER_ACTIVITY_TYPES) {
                this.mUserActivityCounters[event].stepAtomic();
            } else {
                android.util.Slog.w(com.android.server.power.stats.BatteryStatsImpl.TAG, "Unknown user activity type " + event + " was specified.", new java.lang.Throwable());
            }
        }

        public boolean hasUserActivity() {
            return this.mUserActivityCounters != null;
        }

        public int getUserActivityCount(int type, int which) {
            if (this.mUserActivityCounters == null) {
                return 0;
            }
            return this.mUserActivityCounters[type].getCountLocked(which);
        }

        void makeWifiBatchedScanBin(int i, android.os.Parcel in) {
            if (i < 0 || i >= 5) {
                return;
            }
            java.util.ArrayList<com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer> collected = (java.util.ArrayList) this.mBsi.mWifiBatchedScanTimers.get(i);
            if (collected == null) {
                collected = new java.util.ArrayList<>();
                this.mBsi.mWifiBatchedScanTimers.put(i, collected);
            }
            com.android.server.power.stats.BatteryStatsImpl.detachIfNotNull(this.mWifiBatchedScanTimer[i]);
            if (in == null) {
                this.mWifiBatchedScanTimer[i] = new com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer(this.mBsi.mClock, this, 11, collected, this.mBsi.mOnBatteryTimeBase);
            } else {
                this.mWifiBatchedScanTimer[i] = new com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer(this.mBsi.mClock, this, 11, collected, this.mBsi.mOnBatteryTimeBase, in);
            }
        }

        void initUserActivityLocked() {
            com.android.server.power.stats.BatteryStatsImpl.detachIfNotNull(this.mUserActivityCounters);
            this.mUserActivityCounters = new com.android.server.power.stats.BatteryStatsImpl.Counter[NUM_USER_ACTIVITY_TYPES];
            for (int i = 0; i < NUM_USER_ACTIVITY_TYPES; i++) {
                this.mUserActivityCounters[i] = new com.android.server.power.stats.BatteryStatsImpl.Counter(this.mBsi.mOnBatteryTimeBase);
            }
        }

        void noteNetworkActivityLocked(int type, long deltaBytes, long deltaPackets) {
            ensureNetworkActivityLocked();
            if (type >= 0 && type < 10) {
                this.mNetworkByteActivityCounters[type].addCountLocked(deltaBytes);
                this.mNetworkPacketActivityCounters[type].addCountLocked(deltaPackets);
            } else {
                android.util.Slog.w(com.android.server.power.stats.BatteryStatsImpl.TAG, "Unknown network activity type " + type + " was specified.", new java.lang.Throwable());
            }
        }

        void noteMobileRadioActiveTimeLocked(long batteryUptimeDeltaUs, long elapsedTimeMs) {
            ensureNetworkActivityLocked();
            getMobileRadioActiveTimeCounter().increment(batteryUptimeDeltaUs, elapsedTimeMs);
            this.mMobileRadioActiveCount.addCountLocked(1L);
        }

        private com.android.server.power.stats.BatteryStatsImpl.TimeMultiStateCounter getMobileRadioActiveTimeCounter() {
            if (this.mMobileRadioActiveTime == null) {
                long timestampMs = this.mBsi.mClock.elapsedRealtime();
                this.mMobileRadioActiveTime = new com.android.server.power.stats.BatteryStatsImpl.TimeMultiStateCounter(this.mBsi.mOnBatteryTimeBase, 5, timestampMs);
                this.mMobileRadioActiveTime.setState(android.os.BatteryStats.mapUidProcessStateToBatteryConsumerProcessState(this.mProcessState), timestampMs);
                this.mMobileRadioActiveTime.update(0L, timestampMs);
            }
            return this.mMobileRadioActiveTime;
        }

        public boolean hasNetworkActivity() {
            return this.mNetworkByteActivityCounters != null;
        }

        public long getNetworkActivityBytes(int type, int which) {
            if (this.mNetworkByteActivityCounters != null && type >= 0 && type < this.mNetworkByteActivityCounters.length) {
                return this.mNetworkByteActivityCounters[type].getCountLocked(which);
            }
            return 0L;
        }

        public long getNetworkActivityPackets(int type, int which) {
            if (this.mNetworkPacketActivityCounters != null && type >= 0 && type < this.mNetworkPacketActivityCounters.length) {
                return this.mNetworkPacketActivityCounters[type].getCountLocked(which);
            }
            return 0L;
        }

        public long getMobileRadioActiveTime(int which) {
            return getMobileRadioActiveTimeInProcessState(0);
        }

        public long getMobileRadioActiveTimeInProcessState(int processState) {
            if (this.mMobileRadioActiveTime == null) {
                return 0L;
            }
            if (processState == 0) {
                return this.mMobileRadioActiveTime.getTotalCountLocked();
            }
            return this.mMobileRadioActiveTime.getCountForProcessState(processState);
        }

        public int getMobileRadioActiveCount(int which) {
            if (this.mMobileRadioActiveCount != null) {
                return (int) this.mMobileRadioActiveCount.getCountLocked(which);
            }
            return 0;
        }

        public long getUserCpuTimeUs(int which) {
            return this.mUserCpuTime.getCountLocked(which);
        }

        public long getSystemCpuTimeUs(int which) {
            return this.mSystemCpuTime.getCountLocked(which);
        }

        @java.lang.Deprecated
        public long getTimeAtCpuSpeed(int cluster, int step, int which) {
            com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounter[] cpuSpeedTimesUs;
            com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounter c;
            if (this.mCpuClusterSpeedTimesUs != null && cluster >= 0 && cluster < this.mCpuClusterSpeedTimesUs.length && (cpuSpeedTimesUs = this.mCpuClusterSpeedTimesUs[cluster]) != null && step >= 0 && step < cpuSpeedTimesUs.length && (c = cpuSpeedTimesUs[step]) != null) {
                return c.getCountLocked(which);
            }
            return 0L;
        }

        public void noteMobileRadioApWakeupLocked() {
            if (this.mMobileRadioApWakeupCount == null) {
                this.mMobileRadioApWakeupCount = new com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounter(this.mBsi.mOnBatteryTimeBase);
            }
            this.mMobileRadioApWakeupCount.addCountLocked(1L);
        }

        public long getMobileRadioApWakeupCount(int which) {
            if (this.mMobileRadioApWakeupCount != null) {
                return this.mMobileRadioApWakeupCount.getCountLocked(which);
            }
            return 0L;
        }

        public void noteWifiRadioApWakeupLocked() {
            if (this.mWifiRadioApWakeupCount == null) {
                this.mWifiRadioApWakeupCount = new com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounter(this.mBsi.mOnBatteryTimeBase);
            }
            this.mWifiRadioApWakeupCount.addCountLocked(1L);
        }

        public long getWifiRadioApWakeupCount(int which) {
            if (this.mWifiRadioApWakeupCount != null) {
                return this.mWifiRadioApWakeupCount.getCountLocked(which);
            }
            return 0L;
        }

        public void getDeferredJobsCheckinLineLocked(java.lang.StringBuilder sb, int which) {
            sb.setLength(0);
            int deferredEventCount = this.mJobsDeferredEventCount.getCountLocked(which);
            if (deferredEventCount == 0) {
                return;
            }
            int deferredCount = this.mJobsDeferredCount.getCountLocked(which);
            long totalLatency = this.mJobsFreshnessTimeMs.getCountLocked(which);
            sb.append(deferredEventCount);
            sb.append(',');
            sb.append(deferredCount);
            sb.append(',');
            sb.append(totalLatency);
            for (int i = 0; i < android.os.BatteryStats.JOB_FRESHNESS_BUCKETS.length; i++) {
                if (this.mJobsFreshnessBuckets[i] == null) {
                    sb.append(",0");
                } else {
                    sb.append(",");
                    sb.append(this.mJobsFreshnessBuckets[i].getCountLocked(which));
                }
            }
        }

        public void getDeferredJobsLineLocked(java.lang.StringBuilder sb, int which) {
            sb.setLength(0);
            int deferredEventCount = this.mJobsDeferredEventCount.getCountLocked(which);
            if (deferredEventCount == 0) {
                return;
            }
            int deferredCount = this.mJobsDeferredCount.getCountLocked(which);
            long totalLatency = this.mJobsFreshnessTimeMs.getCountLocked(which);
            sb.append("times=");
            sb.append(deferredEventCount);
            sb.append(", ");
            sb.append("count=");
            sb.append(deferredCount);
            sb.append(", ");
            sb.append("totalLatencyMs=");
            sb.append(totalLatency);
            sb.append(", ");
            for (int i = 0; i < android.os.BatteryStats.JOB_FRESHNESS_BUCKETS.length; i++) {
                sb.append("<");
                sb.append(android.os.BatteryStats.JOB_FRESHNESS_BUCKETS[i]);
                sb.append("ms=");
                if (this.mJobsFreshnessBuckets[i] == null) {
                    sb.append("0");
                } else {
                    sb.append(this.mJobsFreshnessBuckets[i].getCountLocked(which));
                }
                sb.append(" ");
            }
        }

        void ensureNetworkActivityLocked() {
            if (this.mNetworkByteActivityCounters != null) {
                return;
            }
            this.mNetworkByteActivityCounters = new com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounter[10];
            this.mNetworkPacketActivityCounters = new com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounter[10];
            for (int i = 0; i < 10; i++) {
                this.mNetworkByteActivityCounters[i] = new com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounter(this.mBsi.mOnBatteryTimeBase);
                this.mNetworkPacketActivityCounters[i] = new com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounter(this.mBsi.mOnBatteryTimeBase);
            }
            this.mMobileRadioActiveCount = new com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounter(this.mBsi.mOnBatteryTimeBase);
        }

        public boolean reset(long uptimeUs, long realtimeUs, int resetReason) {
            boolean active = false;
            this.mOnBatteryBackgroundTimeBase.init(uptimeUs, realtimeUs);
            this.mOnBatteryScreenOffBackgroundTimeBase.init(uptimeUs, realtimeUs);
            if (this.mWifiRunningTimer != null) {
                boolean active2 = false | (!this.mWifiRunningTimer.reset(false, realtimeUs));
                active = active2 | this.mWifiRunning;
            }
            if (this.mFullWifiLockTimer != null) {
                active = active | (!this.mFullWifiLockTimer.reset(false, realtimeUs)) | this.mFullWifiLockOut;
            }
            if (this.mWifiScanTimer != null) {
                active = active | (!this.mWifiScanTimer.reset(false, realtimeUs)) | this.mWifiScanStarted;
            }
            if (this.mWifiBatchedScanTimer != null) {
                for (int i = 0; i < 5; i++) {
                    if (this.mWifiBatchedScanTimer[i] != null) {
                        active |= !this.mWifiBatchedScanTimer[i].reset(false, realtimeUs);
                    }
                }
                int i2 = this.mWifiBatchedScanBinStarted;
                active |= i2 != -1;
            }
            if (this.mWifiMulticastTimer != null) {
                active = active | (!this.mWifiMulticastTimer.reset(false, realtimeUs)) | (this.mWifiMulticastWakelockCount > 0);
            }
            boolean active3 = active | (!com.android.server.power.stats.BatteryStatsImpl.resetIfNotNull(this.mAudioTurnedOnTimer, false, realtimeUs)) | (!com.android.server.power.stats.BatteryStatsImpl.resetIfNotNull(this.mVideoTurnedOnTimer, false, realtimeUs)) | (!com.android.server.power.stats.BatteryStatsImpl.resetIfNotNull(this.mFlashlightTurnedOnTimer, false, realtimeUs)) | (!com.android.server.power.stats.BatteryStatsImpl.resetIfNotNull(this.mCameraTurnedOnTimer, false, realtimeUs)) | (!com.android.server.power.stats.BatteryStatsImpl.resetIfNotNull(this.mForegroundActivityTimer, false, realtimeUs)) | (!com.android.server.power.stats.BatteryStatsImpl.resetIfNotNull(this.mForegroundServiceTimer, false, realtimeUs)) | (!com.android.server.power.stats.BatteryStatsImpl.resetIfNotNull(this.mAggregatedPartialWakelockTimer, false, realtimeUs)) | (!com.android.server.power.stats.BatteryStatsImpl.resetIfNotNull(this.mBluetoothScanTimer, false, realtimeUs)) | (!com.android.server.power.stats.BatteryStatsImpl.resetIfNotNull(this.mBluetoothUnoptimizedScanTimer, false, realtimeUs));
            com.android.server.power.stats.BatteryStatsImpl.resetIfNotNull(this.mBluetoothScanResultCounter, false, realtimeUs);
            com.android.server.power.stats.BatteryStatsImpl.resetIfNotNull(this.mBluetoothScanResultBgCounter, false, realtimeUs);
            if (this.mProcessStateTimer != null) {
                for (int i3 = 0; i3 < 7; i3++) {
                    active3 |= !com.android.server.power.stats.BatteryStatsImpl.resetIfNotNull(this.mProcessStateTimer[i3], false, realtimeUs);
                }
                int i4 = this.mProcessState;
                active3 |= i4 != 7;
            }
            if (this.mVibratorOnTimer != null) {
                if (this.mVibratorOnTimer.reset(false, realtimeUs)) {
                    this.mVibratorOnTimer.detach();
                    this.mVibratorOnTimer = null;
                } else {
                    active3 = true;
                }
            }
            com.android.server.power.stats.BatteryStatsImpl.resetIfNotNull((com.android.server.power.stats.BatteryStatsImpl.TimeBaseObs[]) this.mUserActivityCounters, false, realtimeUs);
            com.android.server.power.stats.BatteryStatsImpl.resetIfNotNull((com.android.server.power.stats.BatteryStatsImpl.TimeBaseObs[]) this.mNetworkByteActivityCounters, false, realtimeUs);
            com.android.server.power.stats.BatteryStatsImpl.resetIfNotNull((com.android.server.power.stats.BatteryStatsImpl.TimeBaseObs[]) this.mNetworkPacketActivityCounters, false, realtimeUs);
            com.android.server.power.stats.BatteryStatsImpl.resetIfNotNull(this.mMobileRadioActiveTime, false, realtimeUs);
            com.android.server.power.stats.BatteryStatsImpl.resetIfNotNull(this.mMobileRadioActiveCount, false, realtimeUs);
            com.android.server.power.stats.BatteryStatsImpl.resetIfNotNull(this.mWifiControllerActivity, false, realtimeUs);
            com.android.server.power.stats.BatteryStatsImpl.resetIfNotNull(this.mBluetoothControllerActivity, false, realtimeUs);
            com.android.server.power.stats.BatteryStatsImpl.resetIfNotNull(this.mModemControllerActivity, false, realtimeUs);
            if (resetReason == 4) {
                this.mUidEnergyConsumerStats = null;
            } else {
                com.android.internal.power.EnergyConsumerStats.resetIfNotNull(this.mUidEnergyConsumerStats);
            }
            com.android.server.power.stats.BatteryStatsImpl.resetIfNotNull(this.mUserCpuTime, false, realtimeUs);
            com.android.server.power.stats.BatteryStatsImpl.resetIfNotNull(this.mSystemCpuTime, false, realtimeUs);
            com.android.server.power.stats.BatteryStatsImpl.resetIfNotNull((com.android.server.power.stats.BatteryStatsImpl.TimeBaseObs[][]) this.mCpuClusterSpeedTimesUs, false, realtimeUs);
            com.android.server.power.stats.BatteryStatsImpl.resetIfNotNull(this.mCpuFreqTimeMs, false, realtimeUs);
            com.android.server.power.stats.BatteryStatsImpl.resetIfNotNull(this.mScreenOffCpuFreqTimeMs, false, realtimeUs);
            com.android.server.power.stats.BatteryStatsImpl.resetIfNotNull(this.mCpuActiveTimeMs, false, realtimeUs);
            com.android.server.power.stats.BatteryStatsImpl.resetIfNotNull(this.mCpuClusterTimesMs, false, realtimeUs);
            com.android.server.power.stats.BatteryStatsImpl.resetIfNotNull(this.mProcStateTimeMs, false, realtimeUs);
            com.android.server.power.stats.BatteryStatsImpl.resetIfNotNull(this.mProcStateScreenOffTimeMs, false, realtimeUs);
            com.android.server.power.stats.BatteryStatsImpl.resetIfNotNull(this.mMobileRadioApWakeupCount, false, realtimeUs);
            com.android.server.power.stats.BatteryStatsImpl.resetIfNotNull(this.mWifiRadioApWakeupCount, false, realtimeUs);
            android.util.ArrayMap<java.lang.String, com.android.server.power.stats.BatteryStatsImpl.Uid.Wakelock> wakeStats = this.mWakelockStats.getMap();
            for (int iw = wakeStats.size() - 1; iw >= 0; iw--) {
                com.android.server.power.stats.BatteryStatsImpl.Uid.Wakelock wl = wakeStats.valueAt(iw);
                if (wl.reset(realtimeUs)) {
                    wakeStats.removeAt(iw);
                } else {
                    active3 = true;
                }
            }
            long realtimeMs = realtimeUs / 1000;
            this.mWakelockStats.cleanup(realtimeMs);
            android.util.ArrayMap<java.lang.String, com.android.server.power.stats.BatteryStatsImpl.DualTimer> syncStats = this.mSyncStats.getMap();
            for (int is = syncStats.size() - 1; is >= 0; is--) {
                com.android.server.power.stats.BatteryStatsImpl.DualTimer timer = syncStats.valueAt(is);
                if (timer.reset(false, realtimeUs)) {
                    syncStats.removeAt(is);
                    timer.detach();
                } else {
                    active3 = true;
                }
            }
            this.mSyncStats.cleanup(realtimeMs);
            android.util.ArrayMap<java.lang.String, com.android.server.power.stats.BatteryStatsImpl.DualTimer> jobStats = this.mJobStats.getMap();
            for (int ij = jobStats.size() - 1; ij >= 0; ij--) {
                com.android.server.power.stats.BatteryStatsImpl.DualTimer timer2 = jobStats.valueAt(ij);
                if (timer2.reset(false, realtimeUs)) {
                    jobStats.removeAt(ij);
                    timer2.detach();
                } else {
                    active3 = true;
                }
            }
            this.mJobStats.cleanup(realtimeMs);
            this.mJobCompletions.clear();
            com.android.server.power.stats.BatteryStatsImpl.resetIfNotNull(this.mJobsDeferredEventCount, false, realtimeUs);
            com.android.server.power.stats.BatteryStatsImpl.resetIfNotNull(this.mJobsDeferredCount, false, realtimeUs);
            com.android.server.power.stats.BatteryStatsImpl.resetIfNotNull(this.mJobsFreshnessTimeMs, false, realtimeUs);
            com.android.server.power.stats.BatteryStatsImpl.resetIfNotNull((com.android.server.power.stats.BatteryStatsImpl.TimeBaseObs[]) this.mJobsFreshnessBuckets, false, realtimeUs);
            for (int ise = this.mSensorStats.size() - 1; ise >= 0; ise--) {
                com.android.server.power.stats.BatteryStatsImpl.Uid.Sensor s = this.mSensorStats.valueAt(ise);
                if (s.reset(realtimeUs)) {
                    this.mSensorStats.removeAt(ise);
                } else {
                    active3 = true;
                }
            }
            for (int ip = this.mProcessStats.size() - 1; ip >= 0; ip--) {
                com.android.server.power.stats.BatteryStatsImpl.Uid.Proc proc = this.mProcessStats.valueAt(ip);
                proc.detach();
            }
            this.mProcessStats.clear();
            for (int i5 = this.mPids.size() - 1; i5 >= 0; i5--) {
                android.os.BatteryStats.Uid.Pid pid = this.mPids.valueAt(i5);
                if (pid.mWakeNesting > 0) {
                    active3 = true;
                } else {
                    this.mPids.removeAt(i5);
                }
            }
            for (int i6 = this.mPackageStats.size() - 1; i6 >= 0; i6--) {
                com.android.server.power.stats.BatteryStatsImpl.Uid.Pkg p = this.mPackageStats.valueAt(i6);
                p.detach();
            }
            this.mPackageStats.clear();
            this.mBinderCallCount = 0L;
            this.mBinderCallStats.clear();
            this.mProportionalSystemServiceUsage = 0.0d;
            this.mLastStepSystemTimeMs = 0L;
            this.mLastStepUserTimeMs = 0L;
            this.mCurStepSystemTimeMs = 0L;
            this.mCurStepUserTimeMs = 0L;
            return !active3;
        }

        void detachFromTimeBase() {
            com.android.server.power.stats.BatteryStatsImpl.detachIfNotNull(this.mWifiRunningTimer);
            com.android.server.power.stats.BatteryStatsImpl.detachIfNotNull(this.mFullWifiLockTimer);
            com.android.server.power.stats.BatteryStatsImpl.detachIfNotNull(this.mWifiScanTimer);
            com.android.server.power.stats.BatteryStatsImpl.detachIfNotNull(this.mWifiBatchedScanTimer);
            com.android.server.power.stats.BatteryStatsImpl.detachIfNotNull(this.mWifiMulticastTimer);
            com.android.server.power.stats.BatteryStatsImpl.detachIfNotNull(this.mAudioTurnedOnTimer);
            com.android.server.power.stats.BatteryStatsImpl.detachIfNotNull(this.mVideoTurnedOnTimer);
            com.android.server.power.stats.BatteryStatsImpl.detachIfNotNull(this.mFlashlightTurnedOnTimer);
            com.android.server.power.stats.BatteryStatsImpl.detachIfNotNull(this.mCameraTurnedOnTimer);
            com.android.server.power.stats.BatteryStatsImpl.detachIfNotNull(this.mForegroundActivityTimer);
            com.android.server.power.stats.BatteryStatsImpl.detachIfNotNull(this.mForegroundServiceTimer);
            com.android.server.power.stats.BatteryStatsImpl.detachIfNotNull(this.mAggregatedPartialWakelockTimer);
            com.android.server.power.stats.BatteryStatsImpl.detachIfNotNull(this.mBluetoothScanTimer);
            com.android.server.power.stats.BatteryStatsImpl.detachIfNotNull(this.mBluetoothUnoptimizedScanTimer);
            com.android.server.power.stats.BatteryStatsImpl.detachIfNotNull(this.mBluetoothScanResultCounter);
            com.android.server.power.stats.BatteryStatsImpl.detachIfNotNull(this.mBluetoothScanResultBgCounter);
            com.android.server.power.stats.BatteryStatsImpl.detachIfNotNull(this.mProcessStateTimer);
            com.android.server.power.stats.BatteryStatsImpl.detachIfNotNull(this.mVibratorOnTimer);
            com.android.server.power.stats.BatteryStatsImpl.detachIfNotNull(this.mUserActivityCounters);
            com.android.server.power.stats.BatteryStatsImpl.detachIfNotNull(this.mNetworkByteActivityCounters);
            com.android.server.power.stats.BatteryStatsImpl.detachIfNotNull(this.mNetworkPacketActivityCounters);
            com.android.server.power.stats.BatteryStatsImpl.detachIfNotNull(this.mMobileRadioActiveTime);
            com.android.server.power.stats.BatteryStatsImpl.detachIfNotNull(this.mMobileRadioActiveCount);
            com.android.server.power.stats.BatteryStatsImpl.detachIfNotNull(this.mMobileRadioApWakeupCount);
            com.android.server.power.stats.BatteryStatsImpl.detachIfNotNull(this.mWifiRadioApWakeupCount);
            com.android.server.power.stats.BatteryStatsImpl.detachIfNotNull(this.mWifiControllerActivity);
            com.android.server.power.stats.BatteryStatsImpl.detachIfNotNull(this.mBluetoothControllerActivity);
            com.android.server.power.stats.BatteryStatsImpl.detachIfNotNull(this.mModemControllerActivity);
            this.mPids.clear();
            com.android.server.power.stats.BatteryStatsImpl.detachIfNotNull(this.mUserCpuTime);
            com.android.server.power.stats.BatteryStatsImpl.detachIfNotNull(this.mSystemCpuTime);
            com.android.server.power.stats.BatteryStatsImpl.detachIfNotNull(this.mCpuClusterSpeedTimesUs);
            com.android.server.power.stats.BatteryStatsImpl.detachIfNotNull(this.mCpuActiveTimeMs);
            com.android.server.power.stats.BatteryStatsImpl.detachIfNotNull(this.mCpuFreqTimeMs);
            com.android.server.power.stats.BatteryStatsImpl.detachIfNotNull(this.mScreenOffCpuFreqTimeMs);
            com.android.server.power.stats.BatteryStatsImpl.detachIfNotNull(this.mCpuClusterTimesMs);
            com.android.server.power.stats.BatteryStatsImpl.detachIfNotNull(this.mProcStateTimeMs);
            com.android.server.power.stats.BatteryStatsImpl.detachIfNotNull(this.mProcStateScreenOffTimeMs);
            android.util.ArrayMap<java.lang.String, com.android.server.power.stats.BatteryStatsImpl.Uid.Wakelock> wakeStats = this.mWakelockStats.getMap();
            for (int iw = wakeStats.size() - 1; iw >= 0; iw--) {
                com.android.server.power.stats.BatteryStatsImpl.Uid.Wakelock wl = wakeStats.valueAt(iw);
                wl.detachFromTimeBase();
            }
            android.util.ArrayMap<java.lang.String, com.android.server.power.stats.BatteryStatsImpl.DualTimer> syncStats = this.mSyncStats.getMap();
            for (int is = syncStats.size() - 1; is >= 0; is--) {
                com.android.server.power.stats.BatteryStatsImpl.DualTimer timer = syncStats.valueAt(is);
                com.android.server.power.stats.BatteryStatsImpl.detachIfNotNull(timer);
            }
            android.util.ArrayMap<java.lang.String, com.android.server.power.stats.BatteryStatsImpl.DualTimer> jobStats = this.mJobStats.getMap();
            for (int ij = jobStats.size() - 1; ij >= 0; ij--) {
                com.android.server.power.stats.BatteryStatsImpl.DualTimer timer2 = jobStats.valueAt(ij);
                com.android.server.power.stats.BatteryStatsImpl.detachIfNotNull(timer2);
            }
            com.android.server.power.stats.BatteryStatsImpl.detachIfNotNull(this.mJobsDeferredEventCount);
            com.android.server.power.stats.BatteryStatsImpl.detachIfNotNull(this.mJobsDeferredCount);
            com.android.server.power.stats.BatteryStatsImpl.detachIfNotNull(this.mJobsFreshnessTimeMs);
            com.android.server.power.stats.BatteryStatsImpl.detachIfNotNull(this.mJobsFreshnessBuckets);
            for (int ise = this.mSensorStats.size() - 1; ise >= 0; ise--) {
                com.android.server.power.stats.BatteryStatsImpl.Uid.Sensor s = this.mSensorStats.valueAt(ise);
                s.detachFromTimeBase();
            }
            for (int ip = this.mProcessStats.size() - 1; ip >= 0; ip--) {
                com.android.server.power.stats.BatteryStatsImpl.Uid.Proc proc = this.mProcessStats.valueAt(ip);
                proc.detach();
            }
            this.mProcessStats.clear();
            for (int i = this.mPackageStats.size() - 1; i >= 0; i--) {
                com.android.server.power.stats.BatteryStatsImpl.Uid.Pkg p = this.mPackageStats.valueAt(i);
                p.detach();
            }
            this.mPackageStats.clear();
        }

        void writeJobCompletionsToParcelLocked(android.os.Parcel out) {
            int NJC = this.mJobCompletions.size();
            out.writeInt(NJC);
            for (int ijc = 0; ijc < NJC; ijc++) {
                out.writeString(this.mJobCompletions.keyAt(ijc));
                android.util.SparseIntArray types = this.mJobCompletions.valueAt(ijc);
                int NT = types.size();
                out.writeInt(NT);
                for (int it = 0; it < NT; it++) {
                    out.writeInt(types.keyAt(it));
                    out.writeInt(types.valueAt(it));
                }
            }
        }

        void readJobCompletionsFromParcelLocked(android.os.Parcel in) {
            int numJobCompletions = in.readInt();
            this.mJobCompletions.clear();
            for (int j = 0; j < numJobCompletions; j++) {
                java.lang.String jobName = in.readString();
                int numTypes = in.readInt();
                if (numTypes > 0) {
                    android.util.SparseIntArray types = new android.util.SparseIntArray();
                    for (int k = 0; k < numTypes; k++) {
                        int type = in.readInt();
                        int count = in.readInt();
                        types.put(type, count);
                    }
                    this.mJobCompletions.put(jobName, types);
                }
            }
        }

        public void noteJobsDeferredLocked(int numDeferred, long sinceLast) {
            this.mJobsDeferredEventCount.addAtomic(1);
            this.mJobsDeferredCount.addAtomic(numDeferred);
            if (sinceLast != 0) {
                this.mJobsFreshnessTimeMs.addCountLocked(sinceLast);
                for (int i = 0; i < android.os.BatteryStats.JOB_FRESHNESS_BUCKETS.length; i++) {
                    if (sinceLast < android.os.BatteryStats.JOB_FRESHNESS_BUCKETS[i]) {
                        if (this.mJobsFreshnessBuckets[i] == null) {
                            this.mJobsFreshnessBuckets[i] = new com.android.server.power.stats.BatteryStatsImpl.Counter(this.mBsi.mOnBatteryTimeBase);
                        }
                        this.mJobsFreshnessBuckets[i].addAtomic(1);
                        return;
                    }
                }
            }
        }

        public void noteBinderCallStatsLocked(long incrementalCallCount, java.util.Collection<com.android.internal.os.BinderCallsStats.CallStat> callStats) {
            com.android.server.power.stats.BatteryStatsImpl.BinderCallStats bcs;
            this.mBinderCallCount += incrementalCallCount;
            for (com.android.internal.os.BinderCallsStats.CallStat stat : callStats) {
                sTempBinderCallStats.binderClass = stat.binderClass;
                sTempBinderCallStats.transactionCode = stat.transactionCode;
                int index = this.mBinderCallStats.indexOf(sTempBinderCallStats);
                if (index >= 0) {
                    bcs = this.mBinderCallStats.valueAt(index);
                } else {
                    bcs = new com.android.server.power.stats.BatteryStatsImpl.BinderCallStats();
                    bcs.binderClass = stat.binderClass;
                    bcs.transactionCode = stat.transactionCode;
                    this.mBinderCallStats.add(bcs);
                }
                bcs.callCount += stat.incrementalCallCount;
                bcs.recordedCallCount = stat.recordedCallCount;
                bcs.recordedCpuTimeMicros = stat.cpuTimeMicros;
            }
        }

        public static class Wakelock extends android.os.BatteryStats.Uid.Wakelock {
            protected com.android.server.power.stats.BatteryStatsImpl mBsi;
            com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer mTimerDraw;
            com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer mTimerFull;
            com.android.server.power.stats.BatteryStatsImpl.DualTimer mTimerPartial;
            com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer mTimerWindow;
            protected com.android.server.power.stats.BatteryStatsImpl.Uid mUid;

            public Wakelock(com.android.server.power.stats.BatteryStatsImpl bsi, com.android.server.power.stats.BatteryStatsImpl.Uid uid) {
                this.mBsi = bsi;
                this.mUid = uid;
            }

            private com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer readStopwatchTimerFromParcel(int type, java.util.ArrayList<com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer> pool, com.android.server.power.stats.BatteryStatsImpl.TimeBase timeBase, android.os.Parcel in) {
                if (in.readInt() == 0) {
                    return null;
                }
                return new com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer(this.mBsi.mClock, this.mUid, type, pool, timeBase, in);
            }

            private com.android.server.power.stats.BatteryStatsImpl.DualTimer readDualTimerFromParcel(int type, java.util.ArrayList<com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer> pool, com.android.server.power.stats.BatteryStatsImpl.TimeBase timeBase, com.android.server.power.stats.BatteryStatsImpl.TimeBase bgTimeBase, android.os.Parcel in) {
                if (in.readInt() == 0) {
                    return null;
                }
                return new com.android.server.power.stats.BatteryStatsImpl.DualTimer(this.mBsi.mClock, this.mUid, type, pool, timeBase, bgTimeBase, in);
            }

            boolean reset(long elapsedRealtimeUs) {
                boolean wlactive = false | (!com.android.server.power.stats.BatteryStatsImpl.resetIfNotNull(this.mTimerFull, false, elapsedRealtimeUs)) | (!com.android.server.power.stats.BatteryStatsImpl.resetIfNotNull(this.mTimerPartial, false, elapsedRealtimeUs)) | (!com.android.server.power.stats.BatteryStatsImpl.resetIfNotNull(this.mTimerWindow, false, elapsedRealtimeUs)) | (!com.android.server.power.stats.BatteryStatsImpl.resetIfNotNull(this.mTimerDraw, false, elapsedRealtimeUs));
                if (!wlactive) {
                    com.android.server.power.stats.BatteryStatsImpl.detachIfNotNull(this.mTimerFull);
                    this.mTimerFull = null;
                    com.android.server.power.stats.BatteryStatsImpl.detachIfNotNull(this.mTimerPartial);
                    this.mTimerPartial = null;
                    com.android.server.power.stats.BatteryStatsImpl.detachIfNotNull(this.mTimerWindow);
                    this.mTimerWindow = null;
                    com.android.server.power.stats.BatteryStatsImpl.detachIfNotNull(this.mTimerDraw);
                    this.mTimerDraw = null;
                }
                return !wlactive;
            }

            void readFromParcelLocked(com.android.server.power.stats.BatteryStatsImpl.TimeBase timeBase, com.android.server.power.stats.BatteryStatsImpl.TimeBase screenOffTimeBase, com.android.server.power.stats.BatteryStatsImpl.TimeBase screenOffBgTimeBase, android.os.Parcel in) {
                this.mTimerPartial = readDualTimerFromParcel(0, this.mBsi.mPartialTimers, screenOffTimeBase, screenOffBgTimeBase, in);
                this.mTimerFull = readStopwatchTimerFromParcel(1, this.mBsi.mFullTimers, timeBase, in);
                this.mTimerWindow = readStopwatchTimerFromParcel(2, this.mBsi.mWindowTimers, timeBase, in);
                this.mTimerDraw = readStopwatchTimerFromParcel(18, this.mBsi.mDrawTimers, timeBase, in);
            }

            void writeToParcelLocked(android.os.Parcel out, long elapsedRealtimeUs) {
                com.android.server.power.stats.BatteryStatsImpl.Timer.writeTimerToParcel(out, this.mTimerPartial, elapsedRealtimeUs);
                com.android.server.power.stats.BatteryStatsImpl.Timer.writeTimerToParcel(out, this.mTimerFull, elapsedRealtimeUs);
                com.android.server.power.stats.BatteryStatsImpl.Timer.writeTimerToParcel(out, this.mTimerWindow, elapsedRealtimeUs);
                com.android.server.power.stats.BatteryStatsImpl.Timer.writeTimerToParcel(out, this.mTimerDraw, elapsedRealtimeUs);
            }

            public com.android.server.power.stats.BatteryStatsImpl.Timer getWakeTime(int type) {
                switch (type) {
                    case 0:
                        return this.mTimerPartial;
                    case 1:
                        return this.mTimerFull;
                    case 2:
                        return this.mTimerWindow;
                    case 18:
                        return this.mTimerDraw;
                    default:
                        throw new java.lang.IllegalArgumentException("type = " + type);
                }
            }

            public void detachFromTimeBase() {
                com.android.server.power.stats.BatteryStatsImpl.detachIfNotNull(this.mTimerPartial);
                com.android.server.power.stats.BatteryStatsImpl.detachIfNotNull(this.mTimerFull);
                com.android.server.power.stats.BatteryStatsImpl.detachIfNotNull(this.mTimerWindow);
                com.android.server.power.stats.BatteryStatsImpl.detachIfNotNull(this.mTimerDraw);
            }
        }

        public static class Sensor extends android.os.BatteryStats.Uid.Sensor {
            protected com.android.server.power.stats.BatteryStatsImpl mBsi;
            final int mHandle;
            com.android.server.power.stats.BatteryStatsImpl.DualTimer mTimer;
            protected com.android.server.power.stats.BatteryStatsImpl.Uid mUid;

            public Sensor(com.android.server.power.stats.BatteryStatsImpl bsi, com.android.server.power.stats.BatteryStatsImpl.Uid uid, int handle) {
                this.mBsi = bsi;
                this.mUid = uid;
                this.mHandle = handle;
            }

            private com.android.server.power.stats.BatteryStatsImpl.DualTimer readTimersFromParcel(com.android.server.power.stats.BatteryStatsImpl.TimeBase timeBase, com.android.server.power.stats.BatteryStatsImpl.TimeBase bgTimeBase, android.os.Parcel in) {
                if (in.readInt() == 0) {
                    return null;
                }
                java.util.ArrayList<com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer> pool = (java.util.ArrayList) this.mBsi.mSensorTimers.get(this.mHandle);
                if (pool == null) {
                    pool = new java.util.ArrayList<>();
                    this.mBsi.mSensorTimers.put(this.mHandle, pool);
                }
                return new com.android.server.power.stats.BatteryStatsImpl.DualTimer(this.mBsi.mClock, this.mUid, 0, pool, timeBase, bgTimeBase, in);
            }

            boolean reset(long elapsedRealtimeUs) {
                if (this.mTimer.reset(true, elapsedRealtimeUs)) {
                    this.mTimer = null;
                    return true;
                }
                return false;
            }

            void readFromParcelLocked(com.android.server.power.stats.BatteryStatsImpl.TimeBase timeBase, com.android.server.power.stats.BatteryStatsImpl.TimeBase bgTimeBase, android.os.Parcel in) {
                this.mTimer = readTimersFromParcel(timeBase, bgTimeBase, in);
            }

            void writeToParcelLocked(android.os.Parcel out, long elapsedRealtimeUs) {
                com.android.server.power.stats.BatteryStatsImpl.Timer.writeTimerToParcel(out, this.mTimer, elapsedRealtimeUs);
            }

            public com.android.server.power.stats.BatteryStatsImpl.Timer getSensorTime() {
                return this.mTimer;
            }

            public com.android.server.power.stats.BatteryStatsImpl.Timer getSensorBackgroundTime() {
                if (this.mTimer == null) {
                    return null;
                }
                return this.mTimer.getSubTimer();
            }

            public int getHandle() {
                return this.mHandle;
            }

            public void detachFromTimeBase() {
                com.android.server.power.stats.BatteryStatsImpl.detachIfNotNull(this.mTimer);
            }
        }

        public static class Proc extends android.os.BatteryStats.Uid.Proc implements com.android.server.power.stats.BatteryStatsImpl.TimeBaseObs {
            boolean mActive = true;
            protected com.android.server.power.stats.BatteryStatsImpl mBsi;
            java.util.ArrayList<android.os.BatteryStats.Uid.Proc.ExcessivePower> mExcessivePower;
            long mForegroundTimeMs;
            final java.lang.String mName;
            int mNumAnrs;
            int mNumCrashes;
            int mStarts;
            long mSystemTimeMs;
            long mUserTimeMs;

            public Proc(com.android.server.power.stats.BatteryStatsImpl bsi, java.lang.String name) {
                this.mBsi = bsi;
                this.mName = name;
                this.mBsi.mOnBatteryTimeBase.add(this);
            }

            @Override // com.android.server.power.stats.BatteryStatsImpl.TimeBaseObs
            public void onTimeStarted(long elapsedRealtimeUs, long baseUptimeUs, long baseRealtimeUs) {
            }

            @Override // com.android.server.power.stats.BatteryStatsImpl.TimeBaseObs
            public void onTimeStopped(long elapsedRealtimeUs, long baseUptimeUs, long baseRealtimeUs) {
            }

            @Override // com.android.server.power.stats.BatteryStatsImpl.TimeBaseObs
            public boolean reset(boolean detachIfReset, long elapsedRealtimeUs) {
                if (detachIfReset) {
                    detach();
                    return true;
                }
                return true;
            }

            @Override // com.android.server.power.stats.BatteryStatsImpl.TimeBaseObs
            public void detach() {
                this.mActive = false;
                this.mBsi.mOnBatteryTimeBase.remove(this);
            }

            public int countExcessivePowers() {
                if (this.mExcessivePower != null) {
                    return this.mExcessivePower.size();
                }
                return 0;
            }

            public android.os.BatteryStats.Uid.Proc.ExcessivePower getExcessivePower(int i) {
                if (this.mExcessivePower != null) {
                    return this.mExcessivePower.get(i);
                }
                return null;
            }

            public void addExcessiveCpu(long overTimeMs, long usedTimeMs) {
                if (this.mExcessivePower == null) {
                    this.mExcessivePower = new java.util.ArrayList<>();
                }
                android.os.BatteryStats.Uid.Proc.ExcessivePower ew = new android.os.BatteryStats.Uid.Proc.ExcessivePower();
                ew.type = 2;
                ew.overTime = overTimeMs;
                ew.usedTime = usedTimeMs;
                this.mExcessivePower.add(ew);
            }

            void writeExcessivePowerToParcelLocked(android.os.Parcel out) {
                if (this.mExcessivePower == null) {
                    out.writeInt(0);
                    return;
                }
                int N = this.mExcessivePower.size();
                out.writeInt(N);
                for (int i = 0; i < N; i++) {
                    android.os.BatteryStats.Uid.Proc.ExcessivePower ew = this.mExcessivePower.get(i);
                    out.writeInt(ew.type);
                    out.writeLong(ew.overTime);
                    out.writeLong(ew.usedTime);
                }
            }

            void readExcessivePowerFromParcelLocked(android.os.Parcel in) {
                int N = in.readInt();
                if (N == 0) {
                    this.mExcessivePower = null;
                    return;
                }
                if (N > 10000) {
                    throw new android.os.ParcelFormatException("File corrupt: too many excessive power entries " + N);
                }
                this.mExcessivePower = new java.util.ArrayList<>();
                for (int i = 0; i < N; i++) {
                    android.os.BatteryStats.Uid.Proc.ExcessivePower ew = new android.os.BatteryStats.Uid.Proc.ExcessivePower();
                    ew.type = in.readInt();
                    ew.overTime = in.readLong();
                    ew.usedTime = in.readLong();
                    this.mExcessivePower.add(ew);
                }
            }

            void writeToParcelLocked(android.os.Parcel out) {
                out.writeLong(this.mUserTimeMs);
                out.writeLong(this.mSystemTimeMs);
                out.writeLong(this.mForegroundTimeMs);
                out.writeInt(this.mStarts);
                out.writeInt(this.mNumCrashes);
                out.writeInt(this.mNumAnrs);
                writeExcessivePowerToParcelLocked(out);
            }

            void readFromParcelLocked(android.os.Parcel in) {
                this.mUserTimeMs = in.readLong();
                this.mSystemTimeMs = in.readLong();
                this.mForegroundTimeMs = in.readLong();
                this.mStarts = in.readInt();
                this.mNumCrashes = in.readInt();
                this.mNumAnrs = in.readInt();
                readExcessivePowerFromParcelLocked(in);
            }

            public void addCpuTimeLocked(int utimeMs, int stimeMs) {
                addCpuTimeLocked(utimeMs, stimeMs, this.mBsi.mOnBatteryTimeBase.isRunning());
            }

            public void addCpuTimeLocked(int utimeMs, int stimeMs, boolean isRunning) {
                if (isRunning) {
                    this.mUserTimeMs += (long) utimeMs;
                    this.mSystemTimeMs += (long) stimeMs;
                }
            }

            public void addForegroundTimeLocked(long ttimeMs) {
                this.mForegroundTimeMs += ttimeMs;
            }

            public void incStartsLocked() {
                this.mStarts++;
            }

            public void incNumCrashesLocked() {
                this.mNumCrashes++;
            }

            public void incNumAnrsLocked() {
                this.mNumAnrs++;
            }

            public boolean isActive() {
                return this.mActive;
            }

            public long getUserTime(int which) {
                return this.mUserTimeMs;
            }

            public long getSystemTime(int which) {
                return this.mSystemTimeMs;
            }

            public long getForegroundTime(int which) {
                return this.mForegroundTimeMs;
            }

            public int getStarts(int which) {
                return this.mStarts;
            }

            public int getNumCrashes(int which) {
                return this.mNumCrashes;
            }

            public int getNumAnrs(int which) {
                return this.mNumAnrs;
            }
        }

        public static class Pkg extends android.os.BatteryStats.Uid.Pkg implements com.android.server.power.stats.BatteryStatsImpl.TimeBaseObs {
            protected com.android.server.power.stats.BatteryStatsImpl mBsi;
            android.util.ArrayMap<java.lang.String, com.android.server.power.stats.BatteryStatsImpl.Counter> mWakeupAlarms = new android.util.ArrayMap<>();
            final android.util.ArrayMap<java.lang.String, com.android.server.power.stats.BatteryStatsImpl.Uid.Pkg.Serv> mServiceStats = new android.util.ArrayMap<>();

            public Pkg(com.android.server.power.stats.BatteryStatsImpl bsi) {
                this.mBsi = bsi;
                this.mBsi.mOnBatteryScreenOffTimeBase.add(this);
            }

            @Override // com.android.server.power.stats.BatteryStatsImpl.TimeBaseObs
            public void onTimeStarted(long elapsedRealtimeUs, long baseUptimeUs, long baseRealtimeUs) {
            }

            @Override // com.android.server.power.stats.BatteryStatsImpl.TimeBaseObs
            public void onTimeStopped(long elapsedRealtimeUs, long baseUptimeUs, long baseRealtimeUs) {
            }

            @Override // com.android.server.power.stats.BatteryStatsImpl.TimeBaseObs
            public boolean reset(boolean detachIfReset, long elapsedRealtimeUs) {
                if (detachIfReset) {
                    detach();
                    return true;
                }
                return true;
            }

            @Override // com.android.server.power.stats.BatteryStatsImpl.TimeBaseObs
            public void detach() {
                this.mBsi.mOnBatteryScreenOffTimeBase.remove(this);
                for (int j = this.mWakeupAlarms.size() - 1; j >= 0; j--) {
                    com.android.server.power.stats.BatteryStatsImpl.detachIfNotNull(this.mWakeupAlarms.valueAt(j));
                }
                for (int j2 = this.mServiceStats.size() - 1; j2 >= 0; j2--) {
                    com.android.server.power.stats.BatteryStatsImpl.detachIfNotNull(this.mServiceStats.valueAt(j2));
                }
            }

            void readFromParcelLocked(android.os.Parcel in) {
                int numWA = in.readInt();
                this.mWakeupAlarms.clear();
                for (int i = 0; i < numWA; i++) {
                    java.lang.String tag = in.readString();
                    this.mWakeupAlarms.put(tag, new com.android.server.power.stats.BatteryStatsImpl.Counter(this.mBsi.mOnBatteryScreenOffTimeBase, in));
                }
                int numServs = in.readInt();
                this.mServiceStats.clear();
                for (int m = 0; m < numServs; m++) {
                    java.lang.String serviceName = in.readString();
                    com.android.server.power.stats.BatteryStatsImpl.Uid.Pkg.Serv serv = new com.android.server.power.stats.BatteryStatsImpl.Uid.Pkg.Serv(this.mBsi);
                    this.mServiceStats.put(serviceName, serv);
                    serv.readFromParcelLocked(in);
                }
            }

            void writeToParcelLocked(android.os.Parcel out) {
                int numWA = this.mWakeupAlarms.size();
                out.writeInt(numWA);
                for (int i = 0; i < numWA; i++) {
                    out.writeString(this.mWakeupAlarms.keyAt(i));
                    this.mWakeupAlarms.valueAt(i).writeToParcel(out);
                }
                int NS = this.mServiceStats.size();
                out.writeInt(NS);
                for (int i2 = 0; i2 < NS; i2++) {
                    out.writeString(this.mServiceStats.keyAt(i2));
                    com.android.server.power.stats.BatteryStatsImpl.Uid.Pkg.Serv serv = this.mServiceStats.valueAt(i2);
                    serv.writeToParcelLocked(out);
                }
            }

            public android.util.ArrayMap<java.lang.String, ? extends android.os.BatteryStats.Counter> getWakeupAlarmStats() {
                return this.mWakeupAlarms;
            }

            public void noteWakeupAlarmLocked(java.lang.String tag) {
                com.android.server.power.stats.BatteryStatsImpl.Counter c = this.mWakeupAlarms.get(tag);
                if (c == null) {
                    c = new com.android.server.power.stats.BatteryStatsImpl.Counter(this.mBsi.mOnBatteryScreenOffTimeBase);
                    this.mWakeupAlarms.put(tag, c);
                }
                c.stepAtomic();
            }

            public android.util.ArrayMap<java.lang.String, ? extends android.os.BatteryStats.Uid.Pkg.Serv> getServiceStats() {
                return this.mServiceStats;
            }

            public static class Serv extends android.os.BatteryStats.Uid.Pkg.Serv implements com.android.server.power.stats.BatteryStatsImpl.TimeBaseObs {
                protected com.android.server.power.stats.BatteryStatsImpl mBsi;
                protected boolean mLaunched;
                protected long mLaunchedSinceMs;
                protected long mLaunchedTimeMs;
                protected int mLaunches;
                protected com.android.server.power.stats.BatteryStatsImpl.Uid.Pkg mPkg;
                protected boolean mRunning;
                protected long mRunningSinceMs;
                protected long mStartTimeMs;
                protected int mStarts;

                public Serv(com.android.server.power.stats.BatteryStatsImpl bsi) {
                    this.mBsi = bsi;
                    this.mBsi.mOnBatteryTimeBase.add(this);
                }

                @Override // com.android.server.power.stats.BatteryStatsImpl.TimeBaseObs
                public void onTimeStarted(long elapsedRealtimeUs, long baseUptimeUs, long baseRealtimeUs) {
                }

                @Override // com.android.server.power.stats.BatteryStatsImpl.TimeBaseObs
                public void onTimeStopped(long elapsedRealtimeUs, long baseUptimeUs, long baseRealtimeUs) {
                }

                @Override // com.android.server.power.stats.BatteryStatsImpl.TimeBaseObs
                public boolean reset(boolean detachIfReset, long elapsedRealtimeUs) {
                    if (detachIfReset) {
                        detach();
                        return true;
                    }
                    return true;
                }

                @Override // com.android.server.power.stats.BatteryStatsImpl.TimeBaseObs
                public void detach() {
                    this.mBsi.mOnBatteryTimeBase.remove(this);
                }

                public void readFromParcelLocked(android.os.Parcel in) {
                    this.mStartTimeMs = in.readLong();
                    this.mRunningSinceMs = in.readLong();
                    this.mRunning = in.readInt() != 0;
                    this.mStarts = in.readInt();
                    this.mLaunchedTimeMs = in.readLong();
                    this.mLaunchedSinceMs = in.readLong();
                    this.mLaunched = in.readInt() != 0;
                    this.mLaunches = in.readInt();
                }

                public void writeToParcelLocked(android.os.Parcel parcel) {
                    parcel.writeLong(this.mStartTimeMs);
                    parcel.writeLong(this.mRunningSinceMs);
                    parcel.writeInt(this.mRunning ? 1 : 0);
                    parcel.writeInt(this.mStarts);
                    parcel.writeLong(this.mLaunchedTimeMs);
                    parcel.writeLong(this.mLaunchedSinceMs);
                    parcel.writeInt(this.mLaunched ? 1 : 0);
                    parcel.writeInt(this.mLaunches);
                }

                public long getLaunchTimeToNowLocked(long batteryUptimeMs) {
                    return !this.mLaunched ? this.mLaunchedTimeMs : (this.mLaunchedTimeMs + batteryUptimeMs) - this.mLaunchedSinceMs;
                }

                public long getStartTimeToNowLocked(long batteryUptimeMs) {
                    return !this.mRunning ? this.mStartTimeMs : (this.mStartTimeMs + batteryUptimeMs) - this.mRunningSinceMs;
                }

                public void startLaunchedLocked() {
                    startLaunchedLocked(this.mBsi.mClock.uptimeMillis());
                }

                public void startLaunchedLocked(long uptimeMs) {
                    if (!this.mLaunched) {
                        this.mLaunches++;
                        this.mLaunchedSinceMs = this.mBsi.getBatteryUptimeLocked(uptimeMs) / 1000;
                        this.mLaunched = true;
                    }
                }

                public void stopLaunchedLocked() {
                    stopLaunchedLocked(this.mBsi.mClock.uptimeMillis());
                }

                public void stopLaunchedLocked(long uptimeMs) {
                    if (this.mLaunched) {
                        long timeMs = (this.mBsi.getBatteryUptimeLocked(uptimeMs) / 1000) - this.mLaunchedSinceMs;
                        if (timeMs > 0) {
                            this.mLaunchedTimeMs += timeMs;
                        } else {
                            this.mLaunches--;
                        }
                        this.mLaunched = false;
                    }
                }

                public void startRunningLocked() {
                    startRunningLocked(this.mBsi.mClock.uptimeMillis());
                }

                public void startRunningLocked(long uptimeMs) {
                    if (!this.mRunning) {
                        this.mStarts++;
                        this.mRunningSinceMs = this.mBsi.getBatteryUptimeLocked(uptimeMs) / 1000;
                        this.mRunning = true;
                    }
                }

                public void stopRunningLocked() {
                    stopRunningLocked(this.mBsi.mClock.uptimeMillis());
                }

                public void stopRunningLocked(long uptimeMs) {
                    if (this.mRunning) {
                        long timeMs = (this.mBsi.getBatteryUptimeLocked(uptimeMs) / 1000) - this.mRunningSinceMs;
                        if (timeMs > 0) {
                            this.mStartTimeMs += timeMs;
                        } else {
                            this.mStarts--;
                        }
                        this.mRunning = false;
                    }
                }

                public com.android.server.power.stats.BatteryStatsImpl getBatteryStats() {
                    return this.mBsi;
                }

                public int getLaunches(int which) {
                    return this.mLaunches;
                }

                public long getStartTime(long now, int which) {
                    return getStartTimeToNowLocked(now);
                }

                public int getStarts(int which) {
                    return this.mStarts;
                }
            }

            final com.android.server.power.stats.BatteryStatsImpl.Uid.Pkg.Serv newServiceStatsLocked() {
                return new com.android.server.power.stats.BatteryStatsImpl.Uid.Pkg.Serv(this.mBsi);
            }
        }

        private class ChildUid {
            public final com.android.server.power.stats.BatteryStatsImpl.TimeMultiStateCounter cpuActiveCounter;
            public final com.android.internal.os.LongArrayMultiStateCounter cpuTimeInFreqCounter;

            ChildUid() {
                long timestampMs = com.android.server.power.stats.BatteryStatsImpl.Uid.this.mBsi.mClock.elapsedRealtime();
                this.cpuActiveCounter = new com.android.server.power.stats.BatteryStatsImpl.TimeMultiStateCounter(com.android.server.power.stats.BatteryStatsImpl.Uid.this.mBsi.mOnBatteryTimeBase, 1, timestampMs);
                this.cpuActiveCounter.setState(0, timestampMs);
                if (com.android.server.power.stats.BatteryStatsImpl.Uid.this.mBsi.trackPerProcStateCpuTimes()) {
                    int cpuFreqCount = com.android.server.power.stats.BatteryStatsImpl.Uid.this.mBsi.mCpuScalingPolicies.getScalingStepCount();
                    this.cpuTimeInFreqCounter = new com.android.internal.os.LongArrayMultiStateCounter(1, cpuFreqCount);
                    this.cpuTimeInFreqCounter.updateValues(new com.android.internal.os.LongArrayMultiStateCounter.LongArrayContainer(cpuFreqCount), timestampMs);
                    return;
                }
                this.cpuTimeInFreqCounter = null;
            }
        }

        public com.android.server.power.stats.BatteryStatsImpl.Uid.Proc getProcessStatsLocked(java.lang.String name) {
            com.android.server.power.stats.BatteryStatsImpl.Uid.Proc ps = this.mProcessStats.get(name);
            if (ps == null) {
                com.android.server.power.stats.BatteryStatsImpl.Uid.Proc ps2 = new com.android.server.power.stats.BatteryStatsImpl.Uid.Proc(this.mBsi, name);
                this.mProcessStats.put(name, ps2);
                return ps2;
            }
            return ps;
        }

        public void updateUidProcessStateLocked(int procState, long elapsedRealtimeMs, long uptimeMs) {
            boolean userAwareService = android.app.ActivityManager.isForegroundService(procState);
            int uidRunningState = android.os.BatteryStats.mapToInternalProcessState(procState);
            if (this.mProcessState == uidRunningState && userAwareService == this.mInForegroundService) {
                return;
            }
            if (this.mProcessState != uidRunningState) {
                if (this.mProcessState != 7) {
                    this.mProcessStateTimer[this.mProcessState].stopRunningLocked(elapsedRealtimeMs);
                }
                if (uidRunningState != 7) {
                    if (this.mProcessStateTimer[uidRunningState] == null) {
                        makeProcessState(uidRunningState, null);
                    }
                    this.mProcessStateTimer[uidRunningState].startRunningLocked(elapsedRealtimeMs);
                }
                if (!this.mBsi.mPowerStatsCollectorEnabled.get(1) && this.mBsi.trackPerProcStateCpuTimes()) {
                    this.mBsi.updateProcStateCpuTimesLocked(this.mUid, elapsedRealtimeMs, uptimeMs);
                    com.android.internal.os.LongArrayMultiStateCounter onBatteryCounter = getProcStateTimeCounter(elapsedRealtimeMs).getCounter();
                    com.android.internal.os.LongArrayMultiStateCounter onBatteryScreenOffCounter = getProcStateScreenOffTimeCounter(elapsedRealtimeMs).getCounter();
                    onBatteryCounter.setState(uidRunningState, elapsedRealtimeMs);
                    onBatteryScreenOffCounter.setState(uidRunningState, elapsedRealtimeMs);
                }
                int prevBatteryConsumerProcessState = android.os.BatteryStats.mapUidProcessStateToBatteryConsumerProcessState(this.mProcessState);
                this.mProcessState = uidRunningState;
                updateOnBatteryBgTimeBase(uptimeMs * 1000, elapsedRealtimeMs * 1000);
                updateOnBatteryScreenOffBgTimeBase(uptimeMs * 1000, 1000 * elapsedRealtimeMs);
                int batteryConsumerProcessState = android.os.BatteryStats.mapUidProcessStateToBatteryConsumerProcessState(uidRunningState);
                if (this.mBsi.mSystemReady && this.mBsi.mPowerStatsCollectorEnabled.get(1)) {
                    this.mBsi.mHistory.recordProcessStateChange(elapsedRealtimeMs, uptimeMs, this.mUid, batteryConsumerProcessState);
                }
                getCpuActiveTimeCounter().setState(batteryConsumerProcessState, elapsedRealtimeMs);
                getMobileRadioActiveTimeCounter().setState(batteryConsumerProcessState, elapsedRealtimeMs);
                com.android.server.power.stats.BatteryStatsImpl.ControllerActivityCounterImpl wifiControllerActivity = getWifiControllerActivity();
                if (wifiControllerActivity != null) {
                    wifiControllerActivity.setState(batteryConsumerProcessState, elapsedRealtimeMs);
                }
                com.android.server.power.stats.BatteryStatsImpl.ControllerActivityCounterImpl bluetoothControllerActivity = getBluetoothControllerActivity();
                if (bluetoothControllerActivity != null) {
                    bluetoothControllerActivity.setState(batteryConsumerProcessState, elapsedRealtimeMs);
                }
                com.android.internal.power.EnergyConsumerStats energyStats = getOrCreateEnergyConsumerStatsIfSupportedLocked();
                if (energyStats != null) {
                    energyStats.setState(batteryConsumerProcessState, elapsedRealtimeMs);
                }
                maybeScheduleExternalStatsSync(prevBatteryConsumerProcessState, batteryConsumerProcessState);
            }
            if (userAwareService != this.mInForegroundService) {
                if (userAwareService) {
                    noteForegroundServiceResumedLocked(elapsedRealtimeMs);
                } else {
                    noteForegroundServicePausedLocked(elapsedRealtimeMs);
                }
                this.mInForegroundService = userAwareService;
            }
        }

        private void maybeScheduleExternalStatsSync(int oldProcessState, int newProcessState) {
            if (oldProcessState == newProcessState) {
                return;
            }
            if (oldProcessState != 0 || newProcessState != 2) {
                if (oldProcessState == 2 && newProcessState == 0) {
                    return;
                }
                int flags = 14;
                if (!com.android.server.power.stats.BatteryStatsImpl.isActiveRadioPowerState(this.mBsi.mMobileRadioPowerState)) {
                    flags = 14 & (-5);
                }
                this.mBsi.mExternalSync.scheduleSyncDueToProcessStateChange(flags, this.mBsi.mConstants.PROC_STATE_CHANGE_COLLECTION_DELAY_MS);
            }
        }

        public boolean isInBackground() {
            return this.mProcessState >= 3;
        }

        public boolean updateOnBatteryBgTimeBase(long uptimeUs, long realtimeUs) {
            boolean on = this.mBsi.mOnBatteryTimeBase.isRunning() && isInBackground();
            return this.mOnBatteryBackgroundTimeBase.setRunning(on, uptimeUs, realtimeUs);
        }

        public boolean updateOnBatteryScreenOffBgTimeBase(long uptimeUs, long realtimeUs) {
            boolean on = this.mBsi.mOnBatteryScreenOffTimeBase.isRunning() && isInBackground();
            return this.mOnBatteryScreenOffBackgroundTimeBase.setRunning(on, uptimeUs, realtimeUs);
        }

        public android.util.SparseArray<? extends android.os.BatteryStats.Uid.Pid> getPidStats() {
            return this.mPids;
        }

        public android.os.BatteryStats.Uid.Pid getPidStatsLocked(int pid) {
            android.os.BatteryStats.Uid.Pid p = this.mPids.get(pid);
            if (p == null) {
                android.os.BatteryStats.Uid.Pid p2 = new android.os.BatteryStats.Uid.Pid(this);
                this.mPids.put(pid, p2);
                return p2;
            }
            return p;
        }

        public com.android.server.power.stats.BatteryStatsImpl.Uid.Pkg getPackageStatsLocked(java.lang.String name) {
            com.android.server.power.stats.BatteryStatsImpl.Uid.Pkg ps = this.mPackageStats.get(name);
            if (ps == null) {
                com.android.server.power.stats.BatteryStatsImpl.Uid.Pkg ps2 = new com.android.server.power.stats.BatteryStatsImpl.Uid.Pkg(this.mBsi);
                this.mPackageStats.put(name, ps2);
                return ps2;
            }
            return ps;
        }

        public com.android.server.power.stats.BatteryStatsImpl.Uid.Pkg.Serv getServiceStatsLocked(java.lang.String pkg, java.lang.String serv) {
            com.android.server.power.stats.BatteryStatsImpl.Uid.Pkg ps = getPackageStatsLocked(pkg);
            com.android.server.power.stats.BatteryStatsImpl.Uid.Pkg.Serv ss = ps.mServiceStats.get(serv);
            if (ss == null) {
                com.android.server.power.stats.BatteryStatsImpl.Uid.Pkg.Serv ss2 = ps.newServiceStatsLocked();
                ps.mServiceStats.put(serv, ss2);
                return ss2;
            }
            return ss;
        }

        public void readSyncSummaryFromParcelLocked(java.lang.String name, android.os.Parcel in) {
            com.android.server.power.stats.BatteryStatsImpl.DualTimer timer = this.mSyncStats.instantiateObject();
            timer.readSummaryFromParcelLocked(in);
            this.mSyncStats.add(name, timer);
        }

        public void readJobSummaryFromParcelLocked(java.lang.String name, android.os.Parcel in) {
            com.android.server.power.stats.BatteryStatsImpl.DualTimer timer = this.mJobStats.instantiateObject();
            timer.readSummaryFromParcelLocked(in);
            this.mJobStats.add(name, timer);
        }

        public void readWakeSummaryFromParcelLocked(java.lang.String wlName, android.os.Parcel in) {
            com.android.server.power.stats.BatteryStatsImpl.Uid.Wakelock wl = new com.android.server.power.stats.BatteryStatsImpl.Uid.Wakelock(this.mBsi, this);
            this.mWakelockStats.add(wlName, wl);
            if (in.readInt() != 0) {
                getWakelockTimerLocked(wl, 1).readSummaryFromParcelLocked(in);
            }
            if (in.readInt() != 0) {
                getWakelockTimerLocked(wl, 0).readSummaryFromParcelLocked(in);
            }
            if (in.readInt() != 0) {
                getWakelockTimerLocked(wl, 2).readSummaryFromParcelLocked(in);
            }
            if (in.readInt() != 0) {
                getWakelockTimerLocked(wl, 18).readSummaryFromParcelLocked(in);
            }
        }

        public com.android.server.power.stats.BatteryStatsImpl.DualTimer getSensorTimerLocked(int sensor, boolean create) {
            com.android.server.power.stats.BatteryStatsImpl.Uid.Sensor se = this.mSensorStats.get(sensor);
            if (se == null) {
                if (!create) {
                    return null;
                }
                se = new com.android.server.power.stats.BatteryStatsImpl.Uid.Sensor(this.mBsi, this, sensor);
                this.mSensorStats.put(sensor, se);
            }
            com.android.server.power.stats.BatteryStatsImpl.DualTimer t = se.mTimer;
            if (t != null) {
                return t;
            }
            java.util.ArrayList<com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer> timers = (java.util.ArrayList) this.mBsi.mSensorTimers.get(sensor);
            if (timers == null) {
                timers = new java.util.ArrayList<>();
                this.mBsi.mSensorTimers.put(sensor, timers);
            }
            com.android.server.power.stats.BatteryStatsImpl.DualTimer t2 = new com.android.server.power.stats.BatteryStatsImpl.DualTimer(this.mBsi.mClock, this, 3, timers, this.mBsi.mOnBatteryTimeBase, this.mOnBatteryBackgroundTimeBase);
            se.mTimer = t2;
            return t2;
        }

        public void noteStartSyncLocked(java.lang.String name, long elapsedRealtimeMs) {
            com.android.server.power.stats.BatteryStatsImpl.DualTimer t = this.mSyncStats.startObject(name, elapsedRealtimeMs);
            if (t != null) {
                t.startRunningLocked(elapsedRealtimeMs);
            }
        }

        public void noteStopSyncLocked(java.lang.String name, long elapsedRealtimeMs) {
            com.android.server.power.stats.BatteryStatsImpl.DualTimer t = this.mSyncStats.stopObject(name, elapsedRealtimeMs);
            if (t != null) {
                t.stopRunningLocked(elapsedRealtimeMs);
            }
        }

        public void noteStartJobLocked(java.lang.String name, long elapsedRealtimeMs) {
            com.android.server.power.stats.BatteryStatsImpl.DualTimer t = this.mJobStats.startObject(name, elapsedRealtimeMs);
            if (t != null) {
                t.startRunningLocked(elapsedRealtimeMs);
            }
        }

        public void noteStopJobLocked(java.lang.String name, long elapsedRealtimeMs, int stopReason) {
            com.android.server.power.stats.BatteryStatsImpl.DualTimer t = this.mJobStats.stopObject(name, elapsedRealtimeMs);
            if (t != null) {
                t.stopRunningLocked(elapsedRealtimeMs);
            }
            if (this.mBsi.mOnBatteryTimeBase.isRunning()) {
                android.util.SparseIntArray types = this.mJobCompletions.get(name);
                if (types == null) {
                    types = new android.util.SparseIntArray();
                    this.mJobCompletions.put(name, types);
                }
                int last = types.get(stopReason, 0);
                types.put(stopReason, last + 1);
            }
        }

        public com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer getWakelockTimerLocked(com.android.server.power.stats.BatteryStatsImpl.Uid.Wakelock wl, int type) {
            if (wl == null) {
                return null;
            }
            switch (type) {
                case 0:
                    com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer t = wl.mTimerPartial;
                    if (t == null) {
                        com.android.server.power.stats.BatteryStatsImpl.DualTimer t2 = new com.android.server.power.stats.BatteryStatsImpl.DualTimer(this.mBsi.mClock, this, 0, this.mBsi.mPartialTimers, this.mBsi.mOnBatteryScreenOffTimeBase, this.mOnBatteryScreenOffBackgroundTimeBase);
                        wl.mTimerPartial = t2;
                        return t2;
                    }
                    return t;
                case 1:
                    com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer t3 = wl.mTimerFull;
                    if (t3 == null) {
                        com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer t4 = new com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer(this.mBsi.mClock, this, 1, this.mBsi.mFullTimers, this.mBsi.mOnBatteryTimeBase);
                        wl.mTimerFull = t4;
                        return t4;
                    }
                    return t3;
                case 2:
                    com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer t5 = wl.mTimerWindow;
                    if (t5 == null) {
                        com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer t6 = new com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer(this.mBsi.mClock, this, 2, this.mBsi.mWindowTimers, this.mBsi.mOnBatteryTimeBase);
                        wl.mTimerWindow = t6;
                        return t6;
                    }
                    return t5;
                case 18:
                    com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer t7 = wl.mTimerDraw;
                    if (t7 == null) {
                        com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer t8 = new com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer(this.mBsi.mClock, this, 18, this.mBsi.mDrawTimers, this.mBsi.mOnBatteryTimeBase);
                        wl.mTimerDraw = t8;
                        return t8;
                    }
                    return t7;
                default:
                    throw new java.lang.IllegalArgumentException("type=" + type);
            }
        }

        public void noteStartWakeLocked(int pid, java.lang.String name, int type, long elapsedRealtimeMs) {
            com.android.server.power.stats.BatteryStatsImpl.Uid.Wakelock wl = this.mWakelockStats.startObject(name, elapsedRealtimeMs);
            if (wl != null) {
                getWakelockTimerLocked(wl, type).startRunningLocked(elapsedRealtimeMs);
            }
            if (type == 0) {
                createAggregatedPartialWakelockTimerLocked().startRunningLocked(elapsedRealtimeMs);
                if (pid >= 0) {
                    android.os.BatteryStats.Uid.Pid p = getPidStatsLocked(pid);
                    int i = p.mWakeNesting;
                    p.mWakeNesting = i + 1;
                    if (i == 0) {
                        p.mWakeStartMs = elapsedRealtimeMs;
                    }
                }
            }
        }

        public void noteStopWakeLocked(int pid, java.lang.String name, int type, long elapsedRealtimeMs) {
            android.os.BatteryStats.Uid.Pid p;
            com.android.server.power.stats.BatteryStatsImpl.Uid.Wakelock wl = this.mWakelockStats.stopObject(name, elapsedRealtimeMs);
            if (wl != null) {
                com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer wlt = getWakelockTimerLocked(wl, type);
                wlt.stopRunningLocked(elapsedRealtimeMs);
            }
            if (type == 0) {
                if (this.mAggregatedPartialWakelockTimer != null) {
                    this.mAggregatedPartialWakelockTimer.stopRunningLocked(elapsedRealtimeMs);
                }
                if (pid >= 0 && (p = this.mPids.get(pid)) != null && p.mWakeNesting > 0) {
                    int i = p.mWakeNesting;
                    p.mWakeNesting = i - 1;
                    if (i == 1) {
                        p.mWakeSumMs += elapsedRealtimeMs - p.mWakeStartMs;
                        p.mWakeStartMs = 0L;
                    }
                }
            }
        }

        public void reportExcessiveCpuLocked(java.lang.String proc, long overTimeMs, long usedTimeMs) {
            com.android.server.power.stats.BatteryStatsImpl.Uid.Proc p = getProcessStatsLocked(proc);
            if (p != null) {
                p.addExcessiveCpu(overTimeMs, usedTimeMs);
            }
        }

        public void noteStartSensor(int sensor, long elapsedRealtimeMs) {
            com.android.server.power.stats.BatteryStatsImpl.DualTimer t = getSensorTimerLocked(sensor, true);
            t.startRunningLocked(elapsedRealtimeMs);
        }

        public void noteStopSensor(int sensor, long elapsedRealtimeMs) {
            com.android.server.power.stats.BatteryStatsImpl.DualTimer t = getSensorTimerLocked(sensor, false);
            if (t != null) {
                t.stopRunningLocked(elapsedRealtimeMs);
            }
        }

        public void noteStartGps(long elapsedRealtimeMs) {
            noteStartSensor(-10000, elapsedRealtimeMs);
        }

        public void noteStopGps(long elapsedRealtimeMs) {
            noteStopSensor(-10000, elapsedRealtimeMs);
        }
    }

    public com.android.internal.os.CpuScalingPolicies getCpuScalingPolicies() {
        return this.mCpuScalingPolicies;
    }

    private com.android.internal.os.LongArrayMultiStateCounter.LongArrayContainer getCpuTimeInFreqContainer() {
        if (this.mTmpCpuTimeInFreq == null) {
            this.mTmpCpuTimeInFreq = new com.android.internal.os.LongArrayMultiStateCounter.LongArrayContainer(this.mCpuScalingPolicies.getScalingStepCount());
        }
        return this.mTmpCpuTimeInFreq;
    }

    public BatteryStatsImpl(com.android.server.power.stats.BatteryStatsImpl.BatteryStatsConfig config, com.android.internal.os.Clock clock, com.android.internal.os.MonotonicClock monotonicClock, java.io.File systemDir, android.os.Handler handler, com.android.server.power.stats.BatteryStatsImpl.PlatformIdleStateCallback platformIdleStateCallback, com.android.server.power.stats.BatteryStatsImpl.EnergyStatsRetriever energyStatsRetriever, com.android.server.power.stats.BatteryStatsImpl.UserInfoProvider userInfoProvider, com.android.internal.os.PowerProfile powerProfile, com.android.internal.os.CpuScalingPolicies cpuScalingPolicies, com.android.server.power.stats.PowerStatsUidResolver powerStatsUidResolver) {
        this(config, clock, monotonicClock, systemDir, handler, platformIdleStateCallback, energyStatsRetriever, userInfoProvider, powerProfile, cpuScalingPolicies, powerStatsUidResolver, new com.android.server.power.stats.BatteryStatsImpl.FrameworkStatsLogger(), new com.android.internal.os.BatteryStatsHistory.TraceDelegate(), new com.android.internal.os.BatteryStatsHistory.EventLogger());
        this.mBatteryStatsImplExt.initBatteryStatsImplExtImpl(this, systemDir, handler);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public BatteryStatsImpl(com.android.server.power.stats.BatteryStatsImpl.BatteryStatsConfig batteryStatsConfig, com.android.internal.os.Clock clock, com.android.internal.os.MonotonicClock monotonicClock, java.io.File file, android.os.Handler handler, com.android.server.power.stats.BatteryStatsImpl.PlatformIdleStateCallback platformIdleStateCallback, com.android.server.power.stats.BatteryStatsImpl.EnergyStatsRetriever energyStatsRetriever, com.android.server.power.stats.BatteryStatsImpl.UserInfoProvider userInfoProvider, com.android.internal.os.PowerProfile powerProfile, com.android.internal.os.CpuScalingPolicies cpuScalingPolicies, com.android.server.power.stats.PowerStatsUidResolver powerStatsUidResolver, com.android.server.power.stats.BatteryStatsImpl.FrameworkStatsLogger frameworkStatsLogger, com.android.internal.os.BatteryStatsHistory.TraceDelegate traceDelegate, com.android.internal.os.BatteryStatsHistory.EventLogger eventLogger) {
        this.mTmpWakelockStats = new com.android.server.power.stats.KernelWakelockStats();
        this.mKernelMemoryStats = new android.util.LongSparseArray<>();
        this.mPowerStatsCollectorEnabled = new android.util.SparseBooleanArray();
        this.mWifiStatsRetriever = new com.android.server.power.stats.WifiPowerStatsCollector.WifiStatsRetriever() { // from class: com.android.server.power.stats.BatteryStatsImpl.2
            @Override // com.android.server.power.stats.WifiPowerStatsCollector.WifiStatsRetriever
            public void retrieveWifiScanTimes(com.android.server.power.stats.WifiPowerStatsCollector.WifiStatsRetriever.Callback callback) {
                synchronized (com.android.server.power.stats.BatteryStatsImpl.this) {
                    com.android.server.power.stats.BatteryStatsImpl.this.retrieveWifiScanTimesLocked(callback);
                }
            }

            @Override // com.android.server.power.stats.WifiPowerStatsCollector.WifiStatsRetriever
            public long getWifiActiveDuration() {
                long globalWifiRunningTime;
                synchronized (com.android.server.power.stats.BatteryStatsImpl.this) {
                    globalWifiRunningTime = com.android.server.power.stats.BatteryStatsImpl.this.getGlobalWifiRunningTime(com.android.server.power.stats.BatteryStatsImpl.this.mClock.elapsedRealtime() * 1000, 0) / 1000;
                }
                return globalWifiRunningTime;
            }
        };
        this.mPerProcStateCpuTimesAvailable = true;
        this.mCpuTimeReadsTrackingStartTimeMs = android.os.SystemClock.uptimeMillis();
        this.mTmpRpmStats = null;
        this.mLastRpmStatsUpdateTimeMs = -1000L;
        this.mPendingRemovedUids = new java.util.LinkedList();
        this.mDeferSetCharging = new java.lang.Runnable() { // from class: com.android.server.power.stats.BatteryStatsImpl.3
            @Override // java.lang.Runnable
            public void run() {
                synchronized (com.android.server.power.stats.BatteryStatsImpl.this) {
                    if (com.android.server.power.stats.BatteryStatsImpl.this.mOnBattery) {
                        return;
                    }
                    boolean changed = com.android.server.power.stats.BatteryStatsImpl.this.setChargingLocked(true);
                    if (changed) {
                        long uptimeMs = com.android.server.power.stats.BatteryStatsImpl.this.mClock.uptimeMillis();
                        long elapsedRealtimeMs = com.android.server.power.stats.BatteryStatsImpl.this.mClock.elapsedRealtime();
                        com.android.server.power.stats.BatteryStatsImpl.this.mHistory.writeHistoryItem(elapsedRealtimeMs, uptimeMs);
                    }
                }
            }
        };
        this.mExternalSync = null;
        this.mUserInfoProvider = null;
        this.mUidStats = new android.util.SparseArray<>();
        this.mPartialTimers = new java.util.ArrayList<>();
        this.mFullTimers = new java.util.ArrayList<>();
        this.mWindowTimers = new java.util.ArrayList<>();
        this.mDrawTimers = new java.util.ArrayList<>();
        this.mSensorTimers = new android.util.SparseArray<>();
        this.mWifiRunningTimers = new java.util.ArrayList<>();
        this.mFullWifiLockTimers = new java.util.ArrayList<>();
        this.mWifiMulticastTimers = new java.util.ArrayList<>();
        this.mWifiScanTimers = new java.util.ArrayList<>();
        this.mWifiBatchedScanTimers = new android.util.SparseArray<>();
        this.mAudioTurnedOnTimers = new java.util.ArrayList<>();
        this.mVideoTurnedOnTimers = new java.util.ArrayList<>();
        this.mFlashlightTurnedOnTimers = new java.util.ArrayList<>();
        this.mCameraTurnedOnTimers = new java.util.ArrayList<>();
        this.mBluetoothScanOnTimers = new java.util.ArrayList<>();
        this.mLastPartialTimers = new java.util.ArrayList<>();
        this.mOnBatteryTimeBase = new com.android.server.power.stats.BatteryStatsImpl.TimeBase(true);
        this.mOnBatteryScreenOffTimeBase = new com.android.server.power.stats.BatteryStatsImpl.TimeBase(true);
        this.mActiveEvents = new android.os.BatteryStats.HistoryEventTracker();
        this.mStepDetailsCalculator = new com.android.server.power.stats.BatteryStatsImpl.HistoryStepDetailsCalculatorImpl();
        this.mHaveBatteryLevel = false;
        this.mBatteryPluggedInRealTimeMs = 0L;
        this.mIgnoreNextExternalStats = false;
        this.mMonotonicEndTime = -1L;
        this.mScreenState = 0;
        this.mScreenBrightnessBin = -1;
        this.mScreenBrightnessTimer = new com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer[5];
        this.mDisplayMismatchWtfCount = 0;
        this.mUsbDataState = 0;
        this.mGpsSignalQualityBin = -1;
        this.mGpsSignalQualityTimer = new com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer[2];
        this.mPhoneSignalStrengthBin = -1;
        this.mPhoneSignalStrengthBinRaw = -1;
        this.mPhoneSignalStrengthsTimer = new com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer[CELL_SIGNAL_STRENGTH_LEVEL_COUNT];
        this.mPhoneDataConnectionType = -1;
        this.mPhoneDataConnectionsTimer = new com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer[NUM_DATA_CONNECTION_TYPES];
        this.mNrState = -1;
        this.mActiveRat = 0;
        this.mPerRatBatteryStats = new com.android.server.power.stats.BatteryStatsImpl.RadioAccessTechnologyBatteryStats[3];
        this.mNetworkByteActivityCounters = new com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounter[10];
        this.mNetworkPacketActivityCounters = new com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounter[10];
        this.mHasWifiReporting = false;
        this.mHasBluetoothReporting = false;
        this.mHasModemReporting = false;
        this.mWifiState = -1;
        this.mWifiStateTimer = new com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer[8];
        this.mWifiSupplState = -1;
        this.mWifiSupplStateTimer = new com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer[13];
        this.mWifiSignalStrengthBin = -1;
        this.mWifiSignalStrengthsTimer = new com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer[5];
        this.mMobileRadioPowerState = 1;
        this.mWifiRadioPowerState = 1;
        this.mBluetoothPowerCalculator = null;
        this.mCpuPowerCalculator = null;
        this.mMobileRadioPowerCalculator = null;
        this.mWifiPowerCalculator = null;
        this.mCharging = true;
        this.mInitStepMode = 0;
        this.mCurStepMode = 0;
        this.mModStepMode = 0;
        this.mDischargeStepTracker = new android.os.BatteryStats.LevelStepTracker(200);
        this.mDailyDischargeStepTracker = new android.os.BatteryStats.LevelStepTracker(400);
        this.mChargeStepTracker = new android.os.BatteryStats.LevelStepTracker(200);
        this.mDailyChargeStepTracker = new android.os.BatteryStats.LevelStepTracker(400);
        this.mDailyStartTimeMs = 0L;
        this.mNextMinDailyDeadlineMs = 0L;
        this.mNextMaxDailyDeadlineMs = 0L;
        this.mDailyItems = new java.util.ArrayList<>();
        this.mLastWriteTimeMs = 0L;
        this.mPhoneServiceState = -1;
        this.mPhoneServiceStateRaw = -1;
        this.mPhoneSimStateRaw = -1;
        this.mEstimatedBatteryCapacityMah = -1;
        this.mLastLearnedBatteryCapacityUah = -1;
        this.mMinLearnedBatteryCapacityUah = -1;
        this.mMaxLearnedBatteryCapacityUah = -1;
        this.mBatteryTimeToFullSeconds = -1L;
        this.mAlarmManager = null;
        this.mLongPlugInAlarmHandler = new android.app.AlarmManager.OnAlarmListener() { // from class: com.android.server.power.stats.BatteryStatsImpl$$ExternalSyntheticLambda3
            @Override // android.app.AlarmManager.OnAlarmListener
            public final void onAlarm() {
                this.f$0.lambda$new$1();
            }
        };
        this.mRpmStats = new java.util.HashMap<>();
        this.mScreenOffRpmStats = new java.util.HashMap<>();
        this.mBatteryStatsImplExt = (com.android.server.power.stats.IBatteryStatsImplExt) system.ext.loader.core.ExtLoader.type(com.android.server.power.stats.IBatteryStatsImplExt.class).base(this).create();
        this.mKernelWakelockStats = new java.util.HashMap<>();
        this.mLastWakeupReason = null;
        this.mLastWakeupUptimeMs = 0L;
        this.mLastWakeupElapsedTimeMs = 0L;
        this.mWakeupReasonStats = new java.util.HashMap<>();
        this.mPowerStatsCollectorInjector = new com.android.server.power.stats.BatteryStatsImpl.PowerStatsCollectorInjector();
        this.mWifiFullLockNesting = 0;
        this.mWifiScanNesting = 0;
        this.mWifiMulticastNesting = 0;
        this.mWifiNetworkLock = new java.lang.Object();
        this.mWifiIfaces = libcore.util.EmptyArray.STRING;
        this.mModemNetworkLock = new java.lang.Object();
        this.mModemIfaces = libcore.util.EmptyArray.STRING;
        this.mLastModemActivityInfo = null;
        this.mLastBluetoothActivityInfo = new com.android.server.power.stats.BatteryStatsImpl.BluetoothActivityInfoCache();
        this.mWriteAsyncRunnable = new java.lang.Runnable() { // from class: com.android.server.power.stats.BatteryStatsImpl$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$new$9();
            }
        };
        this.mWriteLock = new java.util.concurrent.locks.ReentrantLock();
        this.mBsiWrapper = new com.android.server.power.stats.BatteryStatsImpl.BatteryStatsImplWrapper();
        this.mClock = clock;
        initKernelStatsReaders();
        this.mBatteryStatsConfig = batteryStatsConfig;
        this.mMonotonicClock = monotonicClock;
        this.mHandler = new com.android.server.power.stats.BatteryStatsImpl.MyHandler(handler.getLooper());
        this.mConstants = new com.android.server.power.stats.BatteryStatsImpl.Constants(this.mHandler);
        this.mPowerProfile = powerProfile;
        this.mCpuScalingPolicies = cpuScalingPolicies;
        this.mPowerStatsUidResolver = powerStatsUidResolver;
        this.mFrameworkStatsLogger = frameworkStatsLogger;
        initPowerProfile();
        if (file != null) {
            this.mStatsFile = new android.util.AtomicFile(new java.io.File(file, "batterystats.bin"));
            this.mCheckinFile = new android.util.AtomicFile(new java.io.File(file, "batterystats-checkin.bin"));
            this.mDailyFile = new android.util.AtomicFile(new java.io.File(file, "batterystats-daily.xml"));
        } else {
            this.mStatsFile = null;
            this.mCheckinFile = null;
            this.mDailyFile = null;
        }
        this.mHistory = new com.android.internal.os.BatteryStatsHistory((android.os.Parcel) null, file, this.mConstants.MAX_HISTORY_FILES, this.mConstants.MAX_HISTORY_BUFFER, this.mStepDetailsCalculator, this.mClock, this.mMonotonicClock, traceDelegate, eventLogger);
        this.mCpuPowerStatsCollector = new com.android.server.power.stats.CpuPowerStatsCollector(this.mPowerStatsCollectorInjector);
        this.mCpuPowerStatsCollector.addConsumer(new java.util.function.Consumer() { // from class: com.android.server.power.stats.BatteryStatsImpl$$ExternalSyntheticLambda5
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.recordPowerStats((com.android.internal.os.PowerStats) obj);
            }
        });
        this.mMobileRadioPowerStatsCollector = new com.android.server.power.stats.MobileRadioPowerStatsCollector(this.mPowerStatsCollectorInjector);
        this.mMobileRadioPowerStatsCollector.addConsumer(new java.util.function.Consumer() { // from class: com.android.server.power.stats.BatteryStatsImpl$$ExternalSyntheticLambda5
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.recordPowerStats((com.android.internal.os.PowerStats) obj);
            }
        });
        this.mWifiPowerStatsCollector = new com.android.server.power.stats.WifiPowerStatsCollector(this.mPowerStatsCollectorInjector);
        this.mWifiPowerStatsCollector.addConsumer(new java.util.function.Consumer() { // from class: com.android.server.power.stats.BatteryStatsImpl$$ExternalSyntheticLambda5
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.recordPowerStats((com.android.internal.os.PowerStats) obj);
            }
        });
        this.mBluetoothPowerStatsCollector = new com.android.server.power.stats.BluetoothPowerStatsCollector(this.mPowerStatsCollectorInjector);
        this.mBluetoothPowerStatsCollector.addConsumer(new java.util.function.Consumer() { // from class: com.android.server.power.stats.BatteryStatsImpl$$ExternalSyntheticLambda5
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.recordPowerStats((com.android.internal.os.PowerStats) obj);
            }
        });
        this.mCameraPowerStatsCollector = new com.android.server.power.stats.CameraPowerStatsCollector(this.mPowerStatsCollectorInjector);
        this.mCameraPowerStatsCollector.addConsumer(new java.util.function.Consumer() { // from class: com.android.server.power.stats.BatteryStatsImpl$$ExternalSyntheticLambda5
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.recordPowerStats((com.android.internal.os.PowerStats) obj);
            }
        });
        this.mGnssPowerStatsCollector = new com.android.server.power.stats.GnssPowerStatsCollector(this.mPowerStatsCollectorInjector);
        this.mGnssPowerStatsCollector.addConsumer(new java.util.function.Consumer() { // from class: com.android.server.power.stats.BatteryStatsImpl$$ExternalSyntheticLambda5
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.recordPowerStats((com.android.internal.os.PowerStats) obj);
            }
        });
        this.mStartCount++;
        initTimersAndCounters();
        this.mOnBatteryInternal = false;
        this.mOnBattery = false;
        long jUptimeMillis = this.mClock.uptimeMillis() * 1000;
        long jElapsedRealtime = this.mClock.elapsedRealtime() * 1000;
        initTimes(jUptimeMillis, jElapsedRealtime);
        java.lang.String str = android.os.Build.ID;
        this.mEndPlatformVersion = str;
        this.mStartPlatformVersion = str;
        initDischarge(jElapsedRealtime);
        updateDailyDeadlineLocked();
        this.mPlatformIdleStateCallback = platformIdleStateCallback;
        this.mEnergyConsumerRetriever = energyStatsRetriever;
        this.mUserInfoProvider = userInfoProvider;
        this.mPowerStatsUidResolver.addListener(new com.android.server.power.stats.PowerStatsUidResolver.Listener() { // from class: com.android.server.power.stats.BatteryStatsImpl.5
            @Override // com.android.server.power.stats.PowerStatsUidResolver.Listener
            public void onIsolatedUidAdded(int isolatedUid, int parentUid) {
                com.android.server.power.stats.BatteryStatsImpl.this.onIsolatedUidAdded(isolatedUid, parentUid);
            }

            @Override // com.android.server.power.stats.PowerStatsUidResolver.Listener
            public void onBeforeIsolatedUidRemoved(int isolatedUid, int parentUid) {
                com.android.server.power.stats.BatteryStatsImpl.this.onBeforeIsolatedUidRemoved(isolatedUid, parentUid);
            }

            @Override // com.android.server.power.stats.PowerStatsUidResolver.Listener
            public void onAfterIsolatedUidRemoved(int isolatedUid, int parentUid) {
                com.android.server.power.stats.BatteryStatsImpl.this.onAfterIsolatedUidRemoved(isolatedUid, parentUid);
            }
        });
        this.mDeviceIdleMode = 0;
        this.mFrameworkStatsLogger.deviceIdleModeStateChanged(this.mDeviceIdleMode);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void recordPowerStats(com.android.internal.os.PowerStats stats) {
        if (stats.durationMs > 0) {
            synchronized (this) {
                this.mHistory.recordPowerStats(this.mClock.elapsedRealtime(), this.mClock.uptimeMillis(), stats);
            }
        }
    }

    protected void initTimersAndCounters() {
        this.mScreenOnTimer = new com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer(this.mClock, null, -1, null, this.mOnBatteryTimeBase);
        this.mScreenDozeTimer = new com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer(this.mClock, null, -1, null, this.mOnBatteryTimeBase);
        for (int i = 0; i < 5; i++) {
            this.mScreenBrightnessTimer[i] = new com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer(this.mClock, null, (-100) - i, null, this.mOnBatteryTimeBase);
        }
        this.mPerDisplayBatteryStats = new com.android.server.power.stats.BatteryStatsImpl.DisplayBatteryStats[1];
        this.mPerDisplayBatteryStats[0] = new com.android.server.power.stats.BatteryStatsImpl.DisplayBatteryStats(this.mClock, this.mOnBatteryTimeBase);
        this.mInteractiveTimer = new com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer(this.mClock, null, -10, null, this.mOnBatteryTimeBase);
        this.mPowerSaveModeEnabledTimer = new com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer(this.mClock, null, -2, null, this.mOnBatteryTimeBase);
        this.mDeviceIdleModeLightTimer = new com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer(this.mClock, null, -11, null, this.mOnBatteryTimeBase);
        this.mDeviceIdleModeFullTimer = new com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer(this.mClock, null, -14, null, this.mOnBatteryTimeBase);
        this.mDeviceLightIdlingTimer = new com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer(this.mClock, null, -15, null, this.mOnBatteryTimeBase);
        this.mDeviceIdlingTimer = new com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer(this.mClock, null, -12, null, this.mOnBatteryTimeBase);
        this.mPhoneOnTimer = new com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer(this.mClock, null, -3, null, this.mOnBatteryTimeBase);
        for (int i2 = 0; i2 < CELL_SIGNAL_STRENGTH_LEVEL_COUNT; i2++) {
            this.mPhoneSignalStrengthsTimer[i2] = new com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer(this.mClock, null, (-200) - i2, null, this.mOnBatteryTimeBase);
        }
        this.mPhoneSignalScanningTimer = new com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer(this.mClock, null, -199, null, this.mOnBatteryTimeBase);
        for (int i3 = 0; i3 < NUM_DATA_CONNECTION_TYPES; i3++) {
            this.mPhoneDataConnectionsTimer[i3] = new com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer(this.mClock, null, (-300) - i3, null, this.mOnBatteryTimeBase);
        }
        this.mNrNsaTimer = new com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer(this.mClock, null, -198, null, this.mOnBatteryTimeBase);
        for (int i4 = 0; i4 < 10; i4++) {
            this.mNetworkByteActivityCounters[i4] = new com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounter(this.mOnBatteryTimeBase);
            this.mNetworkPacketActivityCounters[i4] = new com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounter(this.mOnBatteryTimeBase);
        }
        this.mWifiActivity = new com.android.server.power.stats.BatteryStatsImpl.ControllerActivityCounterImpl(this.mClock, this.mOnBatteryTimeBase, 1);
        this.mBluetoothActivity = new com.android.server.power.stats.BatteryStatsImpl.ControllerActivityCounterImpl(this.mClock, this.mOnBatteryTimeBase, 1);
        this.mModemActivity = new com.android.server.power.stats.BatteryStatsImpl.ControllerActivityCounterImpl(this.mClock, this.mOnBatteryTimeBase, MODEM_TX_POWER_LEVEL_COUNT);
        this.mMobileRadioActiveTimer = new com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer(this.mClock, null, -400, null, this.mOnBatteryTimeBase);
        this.mMobileRadioActivePerAppTimer = new com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer(this.mClock, null, -401, null, this.mOnBatteryTimeBase);
        this.mMobileRadioActiveAdjustedTime = new com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounter(this.mOnBatteryTimeBase);
        this.mMobileRadioActiveUnknownTime = new com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounter(this.mOnBatteryTimeBase);
        this.mMobileRadioActiveUnknownCount = new com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounter(this.mOnBatteryTimeBase);
        this.mWifiMulticastWakelockTimer = new com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer(this.mClock, null, 23, null, this.mOnBatteryTimeBase);
        this.mWifiOnTimer = new com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer(this.mClock, null, -4, null, this.mOnBatteryTimeBase);
        this.mGlobalWifiRunningTimer = new com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer(this.mClock, null, -5, null, this.mOnBatteryTimeBase);
        for (int i5 = 0; i5 < 8; i5++) {
            this.mWifiStateTimer[i5] = new com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer(this.mClock, null, (-600) - i5, null, this.mOnBatteryTimeBase);
        }
        for (int i6 = 0; i6 < 13; i6++) {
            this.mWifiSupplStateTimer[i6] = new com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer(this.mClock, null, (-700) - i6, null, this.mOnBatteryTimeBase);
        }
        for (int i7 = 0; i7 < 5; i7++) {
            this.mWifiSignalStrengthsTimer[i7] = new com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer(this.mClock, null, (-800) - i7, null, this.mOnBatteryTimeBase);
        }
        this.mWifiActiveTimer = new com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer(this.mClock, null, com.android.server.am.ProcessList.SYSTEM_ADJ, null, this.mOnBatteryTimeBase);
        for (int i8 = 0; i8 < this.mGpsSignalQualityTimer.length; i8++) {
            this.mGpsSignalQualityTimer[i8] = new com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer(this.mClock, null, (-1000) - i8, null, this.mOnBatteryTimeBase);
        }
        this.mAudioOnTimer = new com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer(this.mClock, null, -7, null, this.mOnBatteryTimeBase);
        this.mVideoOnTimer = new com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer(this.mClock, null, -8, null, this.mOnBatteryTimeBase);
        this.mFlashlightOnTimer = new com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer(this.mClock, null, -9, null, this.mOnBatteryTimeBase);
        this.mCameraOnTimer = new com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer(this.mClock, null, -13, null, this.mOnBatteryTimeBase);
        this.mBluetoothScanTimer = new com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer(this.mClock, null, -14, null, this.mOnBatteryTimeBase);
        this.mDischargeScreenOffCounter = new com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounter(this.mOnBatteryScreenOffTimeBase);
        this.mDischargeScreenDozeCounter = new com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounter(this.mOnBatteryTimeBase);
        this.mDischargeLightDozeCounter = new com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounter(this.mOnBatteryTimeBase);
        this.mDischargeDeepDozeCounter = new com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounter(this.mOnBatteryTimeBase);
        this.mDischargeCounter = new com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounter(this.mOnBatteryTimeBase);
        this.mDischargeUnplugLevel = 0;
        this.mDischargePlugLevel = -1;
        this.mDischargeCurrentLevel = 0;
        this.mBatteryLevel = 0;
    }

    private void initPowerProfile() {
        int i;
        int[] policies = this.mCpuScalingPolicies.getPolicies();
        this.mKernelCpuSpeedReaders = new com.android.internal.os.KernelCpuSpeedReader[policies.length];
        int i2 = 0;
        while (true) {
            if (i2 >= policies.length) {
                break;
            }
            int[] cpus = this.mCpuScalingPolicies.getRelatedCpus(policies[i2]);
            int[] freqs = this.mCpuScalingPolicies.getFrequencies(policies[i2]);
            this.mKernelCpuSpeedReaders[i2] = new com.android.internal.os.KernelCpuSpeedReader(cpus[0], freqs.length);
            i2++;
        }
        this.mCpuPowerBracketMap = new int[this.mCpuScalingPolicies.getScalingStepCount()];
        int index = 0;
        for (int policy : policies) {
            int steps = this.mCpuScalingPolicies.getFrequencies(policy).length;
            int step = 0;
            while (step < steps) {
                this.mCpuPowerBracketMap[index] = this.mPowerProfile.getCpuPowerBracketForScalingStep(policy, step);
                step++;
                index++;
            }
        }
        if (this.mEstimatedBatteryCapacityMah == -1) {
            this.mEstimatedBatteryCapacityMah = (int) this.mPowerProfile.getBatteryCapacity();
        }
        setDisplayCountLocked(this.mPowerProfile.getNumDisplays());
    }

    com.android.internal.os.PowerProfile getPowerProfile() {
        return this.mPowerProfile;
    }

    public void startTrackingSystemServerCpuTime() {
        this.mSystemServerCpuThreadReader.startTrackingThreadCpuTime();
    }

    public com.android.server.power.stats.SystemServerCpuThreadReader.SystemServiceCpuThreadTimes getSystemServiceCpuThreadTimes() {
        return this.mSystemServerCpuThreadReader.readAbsolute();
    }

    public void setCallback(com.android.server.power.stats.BatteryStatsImpl.BatteryCallback cb) {
        this.mCallback = cb;
    }

    public void setRadioScanningTimeoutLocked(long timeoutUs) {
        if (this.mPhoneSignalScanningTimer != null) {
            this.mPhoneSignalScanningTimer.setTimeout(timeoutUs);
        }
    }

    public void setExternalStatsSyncLocked(com.android.server.power.stats.BatteryStatsImpl.ExternalStatsSync sync) {
        this.mExternalSync = sync;
    }

    public void setDisplayCountLocked(int numDisplays) {
        this.mPerDisplayBatteryStats = new com.android.server.power.stats.BatteryStatsImpl.DisplayBatteryStats[numDisplays];
        for (int i = 0; i < numDisplays; i++) {
            this.mPerDisplayBatteryStats[i] = new com.android.server.power.stats.BatteryStatsImpl.DisplayBatteryStats(this.mClock, this.mOnBatteryTimeBase);
        }
    }

    public void updateDailyDeadlineLocked() {
        long currentTimeMs = this.mClock.currentTimeMillis();
        this.mDailyStartTimeMs = currentTimeMs;
        java.util.Calendar calDeadline = java.util.Calendar.getInstance();
        calDeadline.setTimeInMillis(currentTimeMs);
        calDeadline.set(6, calDeadline.get(6) + 1);
        calDeadline.set(14, 0);
        calDeadline.set(13, 0);
        calDeadline.set(12, 0);
        calDeadline.set(11, 1);
        this.mNextMinDailyDeadlineMs = calDeadline.getTimeInMillis();
        calDeadline.set(11, 3);
        this.mNextMaxDailyDeadlineMs = calDeadline.getTimeInMillis();
    }

    public void recordDailyStatsIfNeededLocked(boolean settled, long currentTimeMs) {
        if (currentTimeMs >= this.mNextMaxDailyDeadlineMs) {
            recordDailyStatsLocked();
            return;
        }
        if (settled && currentTimeMs >= this.mNextMinDailyDeadlineMs) {
            recordDailyStatsLocked();
        } else if (currentTimeMs < this.mDailyStartTimeMs - 86400000) {
            recordDailyStatsLocked();
        }
    }

    public void recordDailyStatsLocked() {
        android.os.BatteryStats.DailyItem item = new android.os.BatteryStats.DailyItem();
        item.mStartTime = this.mDailyStartTimeMs;
        item.mEndTime = this.mClock.currentTimeMillis();
        boolean hasData = false;
        if (this.mDailyDischargeStepTracker.mNumStepDurations > 0) {
            hasData = true;
            item.mDischargeSteps = new android.os.BatteryStats.LevelStepTracker(this.mDailyDischargeStepTracker.mNumStepDurations, this.mDailyDischargeStepTracker.mStepDurations);
        }
        if (this.mDailyChargeStepTracker.mNumStepDurations > 0) {
            hasData = true;
            item.mChargeSteps = new android.os.BatteryStats.LevelStepTracker(this.mDailyChargeStepTracker.mNumStepDurations, this.mDailyChargeStepTracker.mStepDurations);
        }
        if (this.mDailyPackageChanges != null) {
            hasData = true;
            item.mPackageChanges = this.mDailyPackageChanges;
            this.mDailyPackageChanges = null;
        }
        this.mDailyDischargeStepTracker.init();
        this.mDailyChargeStepTracker.init();
        updateDailyDeadlineLocked();
        if (hasData) {
            long startTimeMs = android.os.SystemClock.uptimeMillis();
            this.mDailyItems.add(item);
            this.mBatteryStatsExt.reportDailyProto();
            while (this.mDailyItems.size() > 10) {
                this.mDailyItems.remove(0);
            }
            final java.io.ByteArrayOutputStream memStream = new java.io.ByteArrayOutputStream();
            try {
                com.android.modules.utils.TypedXmlSerializer out = android.util.Xml.resolveSerializer(memStream);
                writeDailyItemsLocked(out);
                final long initialTimeMs = android.os.SystemClock.uptimeMillis() - startTimeMs;
                com.android.internal.os.BackgroundThread.getHandler().post(new java.lang.Runnable() { // from class: com.android.server.power.stats.BatteryStatsImpl.6
                    @Override // java.lang.Runnable
                    public void run() {
                        synchronized (com.android.server.power.stats.BatteryStatsImpl.this.mCheckinFile) {
                            long startTimeMs2 = android.os.SystemClock.uptimeMillis();
                            java.io.FileOutputStream stream = null;
                            try {
                                stream = com.android.server.power.stats.BatteryStatsImpl.this.mDailyFile.startWrite();
                                memStream.writeTo(stream);
                                stream.flush();
                                com.android.server.power.stats.BatteryStatsImpl.this.mDailyFile.finishWrite(stream);
                                com.android.server.power.stats.BatteryStatsImpl.this.mFrameworkStatsLogger.writeCommitSysConfigFile("batterystats-daily", (initialTimeMs + android.os.SystemClock.uptimeMillis()) - startTimeMs2);
                            } catch (java.io.IOException e) {
                                android.util.Slog.w("BatteryStats", "Error writing battery daily items", e);
                                com.android.server.power.stats.BatteryStatsImpl.this.mDailyFile.failWrite(stream);
                            }
                        }
                    }
                });
            } catch (java.io.IOException e) {
            }
        }
    }

    private void writeDailyItemsLocked(com.android.modules.utils.TypedXmlSerializer out) throws java.io.IOException {
        java.lang.StringBuilder sb = new java.lang.StringBuilder(64);
        out.startDocument((java.lang.String) null, true);
        out.startTag((java.lang.String) null, "daily-items");
        for (int i = 0; i < this.mDailyItems.size(); i++) {
            android.os.BatteryStats.DailyItem dit = this.mDailyItems.get(i);
            out.startTag((java.lang.String) null, com.android.server.pm.Settings.TAG_ITEM);
            out.attributeLong((java.lang.String) null, "start", dit.mStartTime);
            out.attributeLong((java.lang.String) null, "end", dit.mEndTime);
            writeDailyLevelSteps(out, "dis", dit.mDischargeSteps, sb);
            writeDailyLevelSteps(out, "chg", dit.mChargeSteps, sb);
            if (dit.mPackageChanges != null) {
                for (int j = 0; j < dit.mPackageChanges.size(); j++) {
                    android.os.BatteryStats.PackageChange pc = (android.os.BatteryStats.PackageChange) dit.mPackageChanges.get(j);
                    if (pc.mUpdate) {
                        out.startTag((java.lang.String) null, "upd");
                        out.attribute((java.lang.String) null, "pkg", pc.mPackageName);
                        out.attributeLong((java.lang.String) null, "ver", pc.mVersionCode);
                        out.endTag((java.lang.String) null, "upd");
                    } else {
                        out.startTag((java.lang.String) null, "rem");
                        out.attribute((java.lang.String) null, "pkg", pc.mPackageName);
                        out.endTag((java.lang.String) null, "rem");
                    }
                }
            }
            out.endTag((java.lang.String) null, com.android.server.pm.Settings.TAG_ITEM);
        }
        out.endTag((java.lang.String) null, "daily-items");
        out.endDocument();
    }

    private void writeDailyLevelSteps(com.android.modules.utils.TypedXmlSerializer out, java.lang.String tag, android.os.BatteryStats.LevelStepTracker steps, java.lang.StringBuilder tmpBuilder) throws java.io.IOException {
        if (steps != null) {
            out.startTag((java.lang.String) null, tag);
            out.attributeInt((java.lang.String) null, "n", steps.mNumStepDurations);
            for (int i = 0; i < steps.mNumStepDurations; i++) {
                out.startTag((java.lang.String) null, "s");
                tmpBuilder.setLength(0);
                steps.encodeEntryAt(i, tmpBuilder);
                out.attribute((java.lang.String) null, "v", tmpBuilder.toString());
                out.endTag((java.lang.String) null, "s");
            }
            out.endTag((java.lang.String) null, tag);
        }
    }

    public void readDailyStatsLocked() {
        android.util.Slog.d(TAG, "Reading daily items from " + this.mDailyFile.getBaseFile());
        this.mDailyItems.clear();
        try {
            java.io.FileInputStream stream = this.mDailyFile.openRead();
            try {
                try {
                    com.android.modules.utils.TypedXmlPullParser parser = android.util.Xml.resolvePullParser(stream);
                    readDailyItemsLocked(parser);
                    stream.close();
                } catch (java.io.IOException e) {
                }
            } catch (java.io.IOException e2) {
                stream.close();
            } catch (java.lang.Throwable th) {
                try {
                    stream.close();
                } catch (java.io.IOException e3) {
                }
                throw th;
            }
        } catch (java.io.FileNotFoundException e4) {
        }
    }

    private void readDailyItemsLocked(com.android.modules.utils.TypedXmlPullParser parser) {
        int type;
        do {
            try {
                type = parser.next();
                if (type == 2) {
                    break;
                }
            } catch (java.io.IOException e) {
                android.util.Slog.w(TAG, "Failed parsing daily " + e);
                return;
            } catch (java.lang.IllegalStateException e2) {
                android.util.Slog.w(TAG, "Failed parsing daily " + e2);
                return;
            } catch (java.lang.IndexOutOfBoundsException e3) {
                android.util.Slog.w(TAG, "Failed parsing daily " + e3);
                return;
            } catch (java.lang.NullPointerException e4) {
                android.util.Slog.w(TAG, "Failed parsing daily " + e4);
                return;
            } catch (java.lang.NumberFormatException e5) {
                android.util.Slog.w(TAG, "Failed parsing daily " + e5);
                return;
            } catch (org.xmlpull.v1.XmlPullParserException e6) {
                android.util.Slog.w(TAG, "Failed parsing daily " + e6);
                return;
            }
        } while (type != 1);
        if (type != 2) {
            throw new java.lang.IllegalStateException("no start tag found");
        }
        int outerDepth = parser.getDepth();
        while (true) {
            int type2 = parser.next();
            if (type2 != 1) {
                if (type2 != 3 || parser.getDepth() > outerDepth) {
                    if (type2 != 3 && type2 != 4) {
                        java.lang.String tagName = parser.getName();
                        if (tagName.equals(com.android.server.pm.Settings.TAG_ITEM)) {
                            readDailyItemTagLocked(parser);
                        } else {
                            android.util.Slog.w(TAG, "Unknown element under <daily-items>: " + parser.getName());
                            com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                        }
                    }
                } else {
                    return;
                }
            } else {
                return;
            }
        }
    }

    void readDailyItemTagLocked(com.android.modules.utils.TypedXmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException, java.lang.NumberFormatException {
        android.os.BatteryStats.DailyItem dit = new android.os.BatteryStats.DailyItem();
        dit.mStartTime = parser.getAttributeLong((java.lang.String) null, "start", 0L);
        dit.mEndTime = parser.getAttributeLong((java.lang.String) null, "end", 0L);
        int outerDepth = parser.getDepth();
        while (true) {
            int type = parser.next();
            if (type == 1 || (type == 3 && parser.getDepth() <= outerDepth)) {
                break;
            }
            if (type != 3 && type != 4) {
                java.lang.String tagName = parser.getName();
                if (tagName.equals("dis")) {
                    readDailyItemTagDetailsLocked(parser, dit, false, "dis");
                } else if (tagName.equals("chg")) {
                    readDailyItemTagDetailsLocked(parser, dit, true, "chg");
                } else if (tagName.equals("upd")) {
                    if (dit.mPackageChanges == null) {
                        dit.mPackageChanges = new java.util.ArrayList();
                    }
                    android.os.BatteryStats.PackageChange pc = new android.os.BatteryStats.PackageChange();
                    pc.mUpdate = true;
                    pc.mPackageName = parser.getAttributeValue((java.lang.String) null, "pkg");
                    pc.mVersionCode = parser.getAttributeLong((java.lang.String) null, "ver", 0L);
                    dit.mPackageChanges.add(pc);
                    com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                } else if (tagName.equals("rem")) {
                    if (dit.mPackageChanges == null) {
                        dit.mPackageChanges = new java.util.ArrayList();
                    }
                    android.os.BatteryStats.PackageChange pc2 = new android.os.BatteryStats.PackageChange();
                    pc2.mUpdate = false;
                    pc2.mPackageName = parser.getAttributeValue((java.lang.String) null, "pkg");
                    dit.mPackageChanges.add(pc2);
                    com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                } else {
                    android.util.Slog.w(TAG, "Unknown element under <item>: " + parser.getName());
                    com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                }
            }
        }
        this.mDailyItems.add(dit);
    }

    void readDailyItemTagDetailsLocked(com.android.modules.utils.TypedXmlPullParser parser, android.os.BatteryStats.DailyItem dit, boolean isCharge, java.lang.String tag) throws org.xmlpull.v1.XmlPullParserException, java.lang.NumberFormatException, java.io.IOException {
        java.lang.String valueAttr;
        int num = parser.getAttributeInt((java.lang.String) null, "n", -1);
        if (num == -1) {
            android.util.Slog.w(TAG, "Missing 'n' attribute at " + parser.getPositionDescription());
            com.android.internal.util.XmlUtils.skipCurrentTag(parser);
            return;
        }
        android.os.BatteryStats.LevelStepTracker steps = new android.os.BatteryStats.LevelStepTracker(num);
        if (isCharge) {
            dit.mChargeSteps = steps;
        } else {
            dit.mDischargeSteps = steps;
        }
        int i = 0;
        int outerDepth = parser.getDepth();
        while (true) {
            int type = parser.next();
            if (type == 1 || (type == 3 && parser.getDepth() <= outerDepth)) {
                break;
            }
            if (type != 3 && type != 4) {
                java.lang.String tagName = parser.getName();
                if ("s".equals(tagName)) {
                    if (i < num && (valueAttr = parser.getAttributeValue((java.lang.String) null, "v")) != null) {
                        steps.decodeEntryAt(i, valueAttr);
                        i++;
                    }
                } else {
                    android.util.Slog.w(TAG, "Unknown element under <" + tag + ">: " + parser.getName());
                    com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                }
            }
        }
        steps.mNumStepDurations = i;
    }

    public android.os.BatteryStats.DailyItem getDailyItemLocked(int daysAgo) {
        int index = (this.mDailyItems.size() - 1) - daysAgo;
        if (index >= 0) {
            return this.mDailyItems.get(index);
        }
        return null;
    }

    public long getCurrentDailyStartTime() {
        return this.mDailyStartTimeMs;
    }

    public long getNextMinDailyDeadline() {
        return this.mNextMinDailyDeadlineMs;
    }

    public long getNextMaxDailyDeadline() {
        return this.mNextMaxDailyDeadlineMs;
    }

    public int getHistoryTotalSize() {
        return this.mConstants.MAX_HISTORY_BUFFER * this.mConstants.MAX_HISTORY_FILES;
    }

    public int getHistoryUsedSize() {
        return this.mHistory.getHistoryUsedSize();
    }

    public com.android.internal.os.BatteryStatsHistoryIterator iterateBatteryStatsHistory(long startTimeMs, long endTimeMs) {
        return this.mHistory.iterate(startTimeMs, endTimeMs);
    }

    public int getHistoryStringPoolSize() {
        return this.mHistory.getHistoryStringPoolSize();
    }

    public int getHistoryStringPoolBytes() {
        return this.mHistory.getHistoryStringPoolBytes();
    }

    public java.lang.String getHistoryTagPoolString(int index) {
        return this.mHistory.getHistoryTagPoolString(index);
    }

    public int getHistoryTagPoolUid(int index) {
        return this.mHistory.getHistoryTagPoolUid(index);
    }

    public int getStartCount() {
        return this.mStartCount;
    }

    public boolean isOnBattery() {
        return this.mOnBattery;
    }

    public boolean isCharging() {
        return this.mCharging;
    }

    void initTimes(long uptimeUs, long realtimeUs) {
        this.mStartClockTimeMs = this.mClock.currentTimeMillis();
        this.mOnBatteryTimeBase.init(uptimeUs, realtimeUs);
        this.mOnBatteryScreenOffTimeBase.init(uptimeUs, realtimeUs);
        this.mRealtimeUs = 0L;
        this.mUptimeUs = 0L;
        this.mRealtimeStartUs = realtimeUs;
        this.mUptimeStartUs = uptimeUs;
        this.mMonotonicStartTime = this.mMonotonicClock.monotonicTime();
    }

    void initDischarge(long elapsedRealtimeUs) {
        this.mLowDischargeAmountSinceCharge = 0;
        this.mHighDischargeAmountSinceCharge = 0;
        this.mDischargeAmountScreenOn = 0;
        this.mDischargeAmountScreenOnSinceCharge = 0;
        this.mDischargeAmountScreenOff = 0;
        this.mDischargeAmountScreenOffSinceCharge = 0;
        this.mDischargeAmountScreenDoze = 0;
        this.mDischargeAmountScreenDozeSinceCharge = 0;
        this.mDischargeStepTracker.init();
        this.mChargeStepTracker.init();
        this.mDischargeScreenOffCounter.reset(false, elapsedRealtimeUs);
        this.mDischargeScreenDozeCounter.reset(false, elapsedRealtimeUs);
        this.mDischargeLightDozeCounter.reset(false, elapsedRealtimeUs);
        this.mDischargeDeepDozeCounter.reset(false, elapsedRealtimeUs);
        this.mDischargeCounter.reset(false, elapsedRealtimeUs);
    }

    public void saveBatteryUsageStatsOnReset(com.android.server.power.stats.BatteryUsageStatsProvider batteryUsageStatsProvider, com.android.server.power.stats.PowerStatsStore powerStatsStore) {
        this.mSaveBatteryUsageStatsOnReset = true;
        this.mBatteryUsageStatsProvider = batteryUsageStatsProvider;
        this.mPowerStatsStore = powerStatsStore;
    }

    public void resetAllStatsAndHistoryLocked(int reason) {
        this.mBatteryStatsImplExt.collectCheckinFile(getLowDischargeAmountSinceCharge(), this.mCheckinFile, this.mCallback);
        long mSecUptime = this.mClock.uptimeMillis();
        long uptimeUs = mSecUptime * 1000;
        long mSecRealtime = this.mClock.elapsedRealtime();
        long realtimeUs = mSecRealtime * 1000;
        resetAllStatsLocked(mSecUptime, mSecRealtime, reason);
        pullPendingStateUpdatesLocked();
        this.mHistory.writeHistoryItem(mSecRealtime, mSecUptime);
        int i = this.mBatteryLevel;
        this.mDischargePlugLevel = i;
        this.mDischargeUnplugLevel = i;
        this.mDischargeCurrentLevel = i;
        this.mOnBatteryTimeBase.reset(uptimeUs, realtimeUs);
        this.mOnBatteryScreenOffTimeBase.reset(uptimeUs, realtimeUs);
        if (!this.mBatteryPluggedIn) {
            if (android.view.Display.isOnState(this.mScreenState)) {
                this.mDischargeScreenOnUnplugLevel = this.mBatteryLevel;
                this.mDischargeScreenDozeUnplugLevel = 0;
                this.mDischargeScreenOffUnplugLevel = 0;
            } else if (android.view.Display.isDozeState(this.mScreenState)) {
                this.mDischargeScreenOnUnplugLevel = 0;
                this.mDischargeScreenDozeUnplugLevel = this.mBatteryLevel;
                this.mDischargeScreenOffUnplugLevel = 0;
            } else {
                this.mDischargeScreenOnUnplugLevel = 0;
                this.mDischargeScreenDozeUnplugLevel = 0;
                this.mDischargeScreenOffUnplugLevel = this.mBatteryLevel;
            }
            this.mDischargeAmountScreenOn = 0;
            this.mDischargeAmountScreenOff = 0;
            this.mDischargeAmountScreenDoze = 0;
        }
        initActiveHistoryEventsLocked(mSecRealtime, mSecUptime);
    }

    private void resetAllStatsLocked(long uptimeMillis, long elapsedRealtimeMillis, int resetReason) {
        int i;
        int i2;
        saveBatteryUsageStatsOnReset(resetReason);
        long uptimeUs = uptimeMillis * 1000;
        long elapsedRealtimeUs = elapsedRealtimeMillis * 1000;
        this.mStartCount = 0;
        initTimes(uptimeUs, elapsedRealtimeUs);
        this.mScreenOnTimer.reset(false, elapsedRealtimeUs);
        this.mScreenDozeTimer.reset(false, elapsedRealtimeUs);
        for (int i3 = 0; i3 < 5; i3++) {
            this.mScreenBrightnessTimer[i3].reset(false, elapsedRealtimeUs);
        }
        int numDisplays = this.mPerDisplayBatteryStats.length;
        for (int i4 = 0; i4 < numDisplays; i4++) {
            this.mPerDisplayBatteryStats[i4].reset(elapsedRealtimeUs);
        }
        if (this.mPowerProfile != null) {
            this.mEstimatedBatteryCapacityMah = (int) this.mPowerProfile.getBatteryCapacity();
        } else {
            this.mEstimatedBatteryCapacityMah = -1;
        }
        this.mLastLearnedBatteryCapacityUah = -1;
        this.mMinLearnedBatteryCapacityUah = -1;
        this.mMaxLearnedBatteryCapacityUah = -1;
        this.mInteractiveTimer.reset(false, elapsedRealtimeUs);
        this.mPowerSaveModeEnabledTimer.reset(false, elapsedRealtimeUs);
        this.mLastIdleTimeStartMs = elapsedRealtimeMillis;
        this.mLongestLightIdleTimeMs = 0L;
        this.mLongestFullIdleTimeMs = 0L;
        this.mDeviceIdleModeLightTimer.reset(false, elapsedRealtimeUs);
        this.mDeviceIdleModeFullTimer.reset(false, elapsedRealtimeUs);
        this.mDeviceLightIdlingTimer.reset(false, elapsedRealtimeUs);
        this.mDeviceIdlingTimer.reset(false, elapsedRealtimeUs);
        this.mPhoneOnTimer.reset(false, elapsedRealtimeUs);
        this.mAudioOnTimer.reset(false, elapsedRealtimeUs);
        this.mVideoOnTimer.reset(false, elapsedRealtimeUs);
        this.mFlashlightOnTimer.reset(false, elapsedRealtimeUs);
        this.mCameraOnTimer.reset(false, elapsedRealtimeUs);
        this.mBluetoothScanTimer.reset(false, elapsedRealtimeUs);
        for (int i5 = 0; i5 < CELL_SIGNAL_STRENGTH_LEVEL_COUNT; i5++) {
            this.mPhoneSignalStrengthsTimer[i5].reset(false, elapsedRealtimeUs);
        }
        this.mPhoneSignalScanningTimer.reset(false, elapsedRealtimeUs);
        for (int i6 = 0; i6 < NUM_DATA_CONNECTION_TYPES; i6++) {
            this.mPhoneDataConnectionsTimer[i6].reset(false, elapsedRealtimeUs);
        }
        this.mNrNsaTimer.reset(false, elapsedRealtimeUs);
        for (int i7 = 0; i7 < 10; i7++) {
            this.mNetworkByteActivityCounters[i7].reset(false, elapsedRealtimeUs);
            this.mNetworkPacketActivityCounters[i7].reset(false, elapsedRealtimeUs);
        }
        for (int i8 = 0; i8 < 3; i8++) {
            com.android.server.power.stats.BatteryStatsImpl.RadioAccessTechnologyBatteryStats stats = this.mPerRatBatteryStats[i8];
            if (stats != null) {
                stats.reset(elapsedRealtimeUs);
            }
        }
        this.mMobileRadioActiveTimer.reset(false, elapsedRealtimeUs);
        this.mMobileRadioActivePerAppTimer.reset(false, elapsedRealtimeUs);
        this.mMobileRadioActiveAdjustedTime.reset(false, elapsedRealtimeUs);
        this.mMobileRadioActiveUnknownTime.reset(false, elapsedRealtimeUs);
        this.mMobileRadioActiveUnknownCount.reset(false, elapsedRealtimeUs);
        this.mWifiOnTimer.reset(false, elapsedRealtimeUs);
        this.mGlobalWifiRunningTimer.reset(false, elapsedRealtimeUs);
        for (int i9 = 0; i9 < 8; i9++) {
            this.mWifiStateTimer[i9].reset(false, elapsedRealtimeUs);
        }
        for (int i10 = 0; i10 < 13; i10++) {
            this.mWifiSupplStateTimer[i10].reset(false, elapsedRealtimeUs);
        }
        for (int i11 = 0; i11 < 5; i11++) {
            this.mWifiSignalStrengthsTimer[i11].reset(false, elapsedRealtimeUs);
        }
        this.mWifiMulticastWakelockTimer.reset(false, elapsedRealtimeUs);
        this.mWifiActiveTimer.reset(false, elapsedRealtimeUs);
        this.mWifiActivity.reset(false, elapsedRealtimeUs);
        for (int i12 = 0; i12 < this.mGpsSignalQualityTimer.length; i12++) {
            this.mGpsSignalQualityTimer[i12].reset(false, elapsedRealtimeUs);
        }
        this.mBluetoothActivity.reset(false, elapsedRealtimeUs);
        this.mModemActivity.reset(false, elapsedRealtimeUs);
        this.mNumConnectivityChange = 0;
        int i13 = 0;
        while (i13 < this.mUidStats.size()) {
            int i14 = i13;
            if (!this.mUidStats.valueAt(i13).reset(uptimeUs, elapsedRealtimeUs, resetReason)) {
                i2 = i14;
            } else {
                this.mUidStats.valueAt(i14).detachFromTimeBase();
                this.mUidStats.remove(this.mUidStats.keyAt(i14));
                i2 = i14 - 1;
            }
            i13 = i2 + 1;
        }
        if (this.mRpmStats.size() > 0) {
            for (com.android.server.power.stats.BatteryStatsImpl.SamplingTimer timer : this.mRpmStats.values()) {
                this.mOnBatteryTimeBase.remove(timer);
            }
            this.mRpmStats.clear();
        }
        if (this.mScreenOffRpmStats.size() > 0) {
            for (com.android.server.power.stats.BatteryStatsImpl.SamplingTimer timer2 : this.mScreenOffRpmStats.values()) {
                this.mOnBatteryScreenOffTimeBase.remove(timer2);
            }
            this.mScreenOffRpmStats.clear();
        }
        if (this.mKernelWakelockStats.size() > 0) {
            for (com.android.server.power.stats.BatteryStatsImpl.SamplingTimer timer3 : this.mKernelWakelockStats.values()) {
                this.mOnBatteryScreenOffTimeBase.remove(timer3);
            }
            this.mKernelWakelockStats.clear();
        }
        if (this.mKernelMemoryStats.size() > 0) {
            for (int i15 = 0; i15 < this.mKernelMemoryStats.size(); i15++) {
                this.mOnBatteryTimeBase.remove(this.mKernelMemoryStats.valueAt(i15));
            }
            this.mKernelMemoryStats.clear();
        }
        if (this.mWakeupReasonStats.size() > 0) {
            for (com.android.server.power.stats.BatteryStatsImpl.SamplingTimer timer4 : this.mWakeupReasonStats.values()) {
                this.mOnBatteryTimeBase.remove(timer4);
            }
            this.mWakeupReasonStats.clear();
        }
        if (this.mTmpRailStats != null) {
            this.mTmpRailStats.reset();
        }
        com.android.internal.power.EnergyConsumerStats.resetIfNotNull(this.mGlobalEnergyConsumerStats);
        if (com.android.internal.hidden_from_bootclasspath.com.android.server.power.optimization.Flags.disableSystemServicePowerAttr()) {
            i = 0;
        } else {
            i = 0;
            resetIfNotNull(this.mBinderThreadCpuTimesUs, false, elapsedRealtimeUs);
        }
        this.mNumAllUidCpuTimeReads = i;
        this.mNumUidsRemoved = i;
        initDischarge(elapsedRealtimeUs);
        this.mHistory.reset();
        writeSyncLocked();
        if (this.mPowerStatsCollectorEnabled.get(1)) {
            schedulePowerStatsSampleCollection();
        }
        this.mIgnoreNextExternalStats = true;
        this.mExternalSync.scheduleSync("reset", 255);
        this.mHandler.sendEmptyMessage(4);
    }

    private void saveBatteryUsageStatsOnReset(int resetReason) {
        final android.os.BatteryUsageStats batteryUsageStats;
        if (!this.mSaveBatteryUsageStatsOnReset || resetReason == 1) {
            return;
        }
        synchronized (this) {
            batteryUsageStats = this.mBatteryUsageStatsProvider.getBatteryUsageStats(this, new android.os.BatteryUsageStatsQuery.Builder().setMaxStatsAgeMs(0L).includePowerModels().includeProcessStateData().build());
        }
        final long monotonicStartTime = this.mMonotonicClock.monotonicTime() - batteryUsageStats.getStatsDuration();
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.power.stats.BatteryStatsImpl$$ExternalSyntheticLambda11
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$saveBatteryUsageStatsOnReset$2(monotonicStartTime, batteryUsageStats);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$saveBatteryUsageStatsOnReset$2(long monotonicStartTime, android.os.BatteryUsageStats batteryUsageStats) {
        this.mPowerStatsStore.storeBatteryUsageStats(monotonicStartTime, batteryUsageStats);
        try {
            batteryUsageStats.close();
        } catch (java.io.IOException e) {
            android.util.Log.e(TAG, "Cannot close BatteryUsageStats", e);
        }
    }

    private void initActiveHistoryEventsLocked(long elapsedRealtimeMs, long uptimeMs) {
        java.util.HashMap<java.lang.String, android.util.SparseIntArray> active;
        for (int i = 0; i < 22; i++) {
            if ((this.mRecordAllHistory || i != 1) && (active = this.mActiveEvents.getStateForEvent(i)) != null) {
                for (java.util.Map.Entry<java.lang.String, android.util.SparseIntArray> ent : active.entrySet()) {
                    android.util.SparseIntArray uids = ent.getValue();
                    for (int j = 0; j < uids.size(); j++) {
                        this.mHistory.recordEvent(elapsedRealtimeMs, uptimeMs, i, ent.getKey(), uids.keyAt(j));
                    }
                }
            }
        }
    }

    void updateDischargeScreenLevelsLocked(int oldState, int newState) {
        updateOldDischargeScreenLevelLocked(oldState);
        updateNewDischargeScreenLevelLocked(newState);
    }

    private void updateOldDischargeScreenLevelLocked(int state) {
        int diff;
        if (android.view.Display.isOnState(state)) {
            int diff2 = this.mDischargeScreenOnUnplugLevel - this.mDischargeCurrentLevel;
            if (diff2 > 0) {
                this.mDischargeAmountScreenOn += diff2;
                this.mDischargeAmountScreenOnSinceCharge += diff2;
                return;
            }
            return;
        }
        if (android.view.Display.isDozeState(state)) {
            int diff3 = this.mDischargeScreenDozeUnplugLevel - this.mDischargeCurrentLevel;
            if (diff3 > 0) {
                this.mDischargeAmountScreenDoze += diff3;
                this.mDischargeAmountScreenDozeSinceCharge += diff3;
                return;
            }
            return;
        }
        if (android.view.Display.isOffState(state) && (diff = this.mDischargeScreenOffUnplugLevel - this.mDischargeCurrentLevel) > 0) {
            this.mDischargeAmountScreenOff += diff;
            this.mDischargeAmountScreenOffSinceCharge += diff;
        }
    }

    private void updateNewDischargeScreenLevelLocked(int state) {
        if (android.view.Display.isOnState(state)) {
            this.mDischargeScreenOnUnplugLevel = this.mDischargeCurrentLevel;
            this.mDischargeScreenOffUnplugLevel = 0;
            this.mDischargeScreenDozeUnplugLevel = 0;
        } else if (android.view.Display.isDozeState(state)) {
            this.mDischargeScreenOnUnplugLevel = 0;
            this.mDischargeScreenDozeUnplugLevel = this.mDischargeCurrentLevel;
            this.mDischargeScreenOffUnplugLevel = 0;
        } else if (android.view.Display.isOffState(state)) {
            this.mDischargeScreenOnUnplugLevel = 0;
            this.mDischargeScreenDozeUnplugLevel = 0;
            this.mDischargeScreenOffUnplugLevel = this.mDischargeCurrentLevel;
        }
    }

    public void pullPendingStateUpdatesLocked() {
        if (this.mOnBatteryInternal) {
            updateDischargeScreenLevelsLocked(this.mScreenState, this.mScreenState);
        }
    }

    protected android.net.NetworkStats readMobileNetworkStatsLocked(android.app.usage.NetworkStatsManager networkStatsManager) {
        return networkStatsManager.getMobileUidStats();
    }

    protected android.net.NetworkStats readWifiNetworkStatsLocked(android.app.usage.NetworkStatsManager networkStatsManager) {
        return networkStatsManager.getWifiUidStats();
    }

    static class NetworkStatsDelta {
        long mRxBytes;
        long mRxPackets;
        int mSet;
        long mTxBytes;
        long mTxPackets;
        int mUid;

        NetworkStatsDelta() {
        }

        public int getUid() {
            return this.mUid;
        }

        public int getSet() {
            return this.mSet;
        }

        public long getRxBytes() {
            return this.mRxBytes;
        }

        public long getRxPackets() {
            return this.mRxPackets;
        }

        public long getTxBytes() {
            return this.mTxBytes;
        }

        public long getTxPackets() {
            return this.mTxPackets;
        }

        public java.lang.String toString() {
            return "NetworkStatsDelta{mUid=" + this.mUid + ", mSet=" + this.mSet + ", mRxBytes=" + this.mRxBytes + ", mRxPackets=" + this.mRxPackets + ", mTxBytes=" + this.mTxBytes + ", mTxPackets=" + this.mTxPackets + '}';
        }
    }

    static java.util.List<com.android.server.power.stats.BatteryStatsImpl.NetworkStatsDelta> computeDelta(android.net.NetworkStats currentStats, android.net.NetworkStats lastStats) {
        java.util.List<com.android.server.power.stats.BatteryStatsImpl.NetworkStatsDelta> deltaList = new java.util.ArrayList<>();
        java.util.Iterator it = currentStats.iterator();
        while (it.hasNext()) {
            android.net.NetworkStats.Entry entry = (android.net.NetworkStats.Entry) it.next();
            com.android.server.power.stats.BatteryStatsImpl.NetworkStatsDelta delta = new com.android.server.power.stats.BatteryStatsImpl.NetworkStatsDelta();
            delta.mUid = entry.getUid();
            delta.mSet = entry.getSet();
            android.net.NetworkStats.Entry lastEntry = null;
            if (lastStats != null) {
                java.util.Iterator it2 = lastStats.iterator();
                while (true) {
                    if (!it2.hasNext()) {
                        break;
                    }
                    android.net.NetworkStats.Entry e = (android.net.NetworkStats.Entry) it2.next();
                    if (e.getUid() == entry.getUid() && e.getSet() == entry.getSet() && e.getTag() == entry.getTag() && e.getMetered() == entry.getMetered() && e.getRoaming() == entry.getRoaming() && e.getDefaultNetwork() == entry.getDefaultNetwork()) {
                        lastEntry = e;
                        break;
                    }
                }
            }
            if (lastEntry != null) {
                delta.mRxBytes = java.lang.Math.max(0L, entry.getRxBytes() - lastEntry.getRxBytes());
                delta.mRxPackets = java.lang.Math.max(0L, entry.getRxPackets() - lastEntry.getRxPackets());
                delta.mTxBytes = java.lang.Math.max(0L, entry.getTxBytes() - lastEntry.getTxBytes());
                delta.mTxPackets = java.lang.Math.max(0L, entry.getTxPackets() - lastEntry.getTxPackets());
            } else {
                delta.mRxBytes = entry.getRxBytes();
                delta.mRxPackets = entry.getRxPackets();
                delta.mTxBytes = entry.getTxBytes();
                delta.mTxPackets = entry.getTxPackets();
            }
            deltaList.add(delta);
        }
        return deltaList;
    }

    /* JADX WARN: Removed duplicated region for block: B:28:0x0048  */
    /* JADX WARN: Removed duplicated region for block: B:94:0x01ff  */
    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find top splitter block for handler:B:187:0x0569
        	at jadx.core.utils.BlockUtils.getTopSplitterForHandler(BlockUtils.java:1182)
        	at jadx.core.dex.visitors.regions.maker.ExcHandlersRegionMaker.collectHandlerRegions(ExcHandlersRegionMaker.java:53)
        	at jadx.core.dex.visitors.regions.maker.ExcHandlersRegionMaker.process(ExcHandlersRegionMaker.java:38)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:27)
        */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void updateWifiState(android.os.connectivity.WifiActivityEnergyInfo r64, long r65, long r67, long r69, android.app.usage.NetworkStatsManager r71) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 1390
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.power.stats.BatteryStatsImpl.updateWifiState(android.os.connectivity.WifiActivityEnergyInfo, long, long, long, android.app.usage.NetworkStatsManager):void");
    }

    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find top splitter block for handler:B:127:0x044b
        	at jadx.core.utils.BlockUtils.getTopSplitterForHandler(BlockUtils.java:1182)
        	at jadx.core.dex.visitors.regions.maker.ExcHandlersRegionMaker.collectHandlerRegions(ExcHandlersRegionMaker.java:53)
        	at jadx.core.dex.visitors.regions.maker.ExcHandlersRegionMaker.process(ExcHandlersRegionMaker.java:38)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:27)
        */
    public void noteModemControllerActivity(android.telephony.ModemActivityInfo r46, long r47, long r49, long r51, android.app.usage.NetworkStatsManager r53) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 1102
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.power.stats.BatteryStatsImpl.noteModemControllerActivity(android.telephony.ModemActivityInfo, long, long, long, android.app.usage.NetworkStatsManager):void");
    }

    private static class RxTxConsumption {
        public final double rxConsumptionMah;
        public final long rxDurationMs;
        public final double txConsumptionMah;
        public final long txDurationMs;
        public final double txToTotalRatio;

        RxTxConsumption(double rxMah, long rxMs, double txMah, long txMs) {
            this.rxConsumptionMah = rxMah;
            this.rxDurationMs = rxMs;
            this.txConsumptionMah = txMah;
            this.txDurationMs = txMs;
            long activeDurationMs = this.txDurationMs + this.rxDurationMs;
            if (activeDurationMs == 0) {
                this.txToTotalRatio = 0.0d;
            } else {
                this.txToTotalRatio = this.txDurationMs / activeDurationMs;
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:69:0x01d5  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x01ee  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x01fe A[RETURN] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private com.android.server.power.stats.BatteryStatsImpl.RxTxConsumption incrementPerRatDataLocked(android.telephony.ModemActivityInfo r41, long r42) {
        /*
            Method dump skipped, instruction units count: 512
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.power.stats.BatteryStatsImpl.incrementPerRatDataLocked(android.telephony.ModemActivityInfo, long):com.android.server.power.stats.BatteryStatsImpl$RxTxConsumption");
    }

    private double smearModemActivityInfoRxTxConsumptionMah(com.android.server.power.stats.BatteryStatsImpl.RxTxConsumption rxTxConsumption, long rxPackets, long txPackets, long totalRxPackets, long totalTxPackets) {
        double consumptionMah = totalRxPackets != 0 ? 0.0d + ((rxTxConsumption.rxConsumptionMah * rxPackets) / totalRxPackets) : 0.0d;
        if (totalTxPackets != 0 || (totalRxPackets != 0 && rxTxConsumption.txToTotalRatio != 0.0d)) {
            double totalPacketsDuringTxTime = totalTxPackets + (rxTxConsumption.txToTotalRatio * totalRxPackets);
            double packetsDuringTxTime = txPackets + (rxTxConsumption.txToTotalRatio * rxPackets);
            return consumptionMah + ((rxTxConsumption.txConsumptionMah * packetsDuringTxTime) / totalPacketsDuringTxTime);
        }
        return consumptionMah;
    }

    private synchronized void addModemTxPowerToHistory(android.telephony.ModemActivityInfo activityInfo, long elapsedRealtimeMs, long uptimeMs) {
        if (activityInfo == null) {
            return;
        }
        int levelMaxTimeSpent = 0;
        for (int i = 1; i < MODEM_TX_POWER_LEVEL_COUNT; i++) {
            if (activityInfo.getTransmitDurationMillisAtPowerLevel(i) > activityInfo.getTransmitDurationMillisAtPowerLevel(levelMaxTimeSpent)) {
                levelMaxTimeSpent = i;
            }
        }
        int i2 = MODEM_TX_POWER_LEVEL_COUNT;
        if (levelMaxTimeSpent == i2 - 1) {
            this.mHistory.recordState2StartEvent(elapsedRealtimeMs, uptimeMs, 524288);
        }
    }

    private final class BluetoothActivityInfoCache {
        long energy;
        long idleTimeMs;
        long rxTimeMs;
        long txTimeMs;
        android.util.SparseLongArray uidRxBytes;
        android.util.SparseLongArray uidTxBytes;

        private BluetoothActivityInfoCache() {
            this.uidRxBytes = new android.util.SparseLongArray();
            this.uidTxBytes = new android.util.SparseLongArray();
        }

        void set(android.bluetooth.BluetoothActivityEnergyInfo info) {
            this.idleTimeMs = info.getControllerIdleTimeMillis();
            this.rxTimeMs = info.getControllerRxTimeMillis();
            this.txTimeMs = info.getControllerTxTimeMillis();
            this.energy = info.getControllerEnergyUsed();
            if (!info.getUidTraffic().isEmpty()) {
                for (android.bluetooth.UidTraffic traffic : info.getUidTraffic()) {
                    this.uidRxBytes.put(traffic.getUid(), traffic.getRxBytes());
                    this.uidTxBytes.put(traffic.getUid(), traffic.getTxBytes());
                }
            }
        }

        void reset() {
            this.idleTimeMs = 0L;
            this.rxTimeMs = 0L;
            this.txTimeMs = 0L;
            this.energy = 0L;
            this.uidRxBytes.clear();
            this.uidTxBytes.clear();
        }
    }

    public void updateBluetoothStateLocked(android.bluetooth.BluetoothActivityEnergyInfo info, long consumedChargeUC, long elapsedRealtimeMs, long uptimeMs) {
        double controllerMaMs;
        android.util.SparseLongArray txTimesMs;
        android.util.SparseLongArray rxTimesMs;
        long rxTimeMs;
        long scanTimeSinceMarkMs;
        long rxTimeMs2;
        long idleTimeMs;
        if (this.mBluetoothPowerStatsCollector.isEnabled() || info == null) {
            return;
        }
        if (this.mOnBatteryInternal && !this.mIgnoreNextExternalStats) {
            this.mHasBluetoothReporting = true;
            if (info.getControllerRxTimeMillis() < this.mLastBluetoothActivityInfo.rxTimeMs || info.getControllerTxTimeMillis() < this.mLastBluetoothActivityInfo.txTimeMs || info.getControllerIdleTimeMillis() < this.mLastBluetoothActivityInfo.idleTimeMs || info.getControllerEnergyUsed() < this.mLastBluetoothActivityInfo.energy) {
                this.mLastBluetoothActivityInfo.reset();
            }
            long rxTimeMs3 = info.getControllerRxTimeMillis() - this.mLastBluetoothActivityInfo.rxTimeMs;
            long txTimeMs = info.getControllerTxTimeMillis() - this.mLastBluetoothActivityInfo.txTimeMs;
            long idleTimeMs2 = info.getControllerIdleTimeMillis() - this.mLastBluetoothActivityInfo.idleTimeMs;
            android.util.SparseDoubleArray uidEstimatedConsumptionMah = (this.mGlobalEnergyConsumerStats == null || this.mBluetoothPowerCalculator == null || consumedChargeUC <= 0) ? null : new android.util.SparseDoubleArray();
            int uidCount = this.mUidStats.size();
            long totalScanTimeMs = 0;
            int i = 0;
            while (i < uidCount) {
                com.android.server.power.stats.BatteryStatsImpl.Uid u = this.mUidStats.valueAt(i);
                if (u.mBluetoothScanTimer == null) {
                    idleTimeMs = idleTimeMs2;
                } else {
                    idleTimeMs = idleTimeMs2;
                    long idleTimeMs3 = elapsedRealtimeMs * 1000;
                    totalScanTimeMs += u.mBluetoothScanTimer.getTimeSinceMarkLocked(idleTimeMs3) / 1000;
                }
                i++;
                idleTimeMs2 = idleTimeMs;
            }
            long idleTimeMs4 = idleTimeMs2;
            boolean normalizeScanRxTime = totalScanTimeMs > rxTimeMs3;
            boolean normalizeScanTxTime = totalScanTimeMs > txTimeMs;
            android.util.SparseLongArray rxTimesMs2 = new android.util.SparseLongArray(uidCount);
            long leftOverRxTimeMs = rxTimeMs3;
            android.util.SparseLongArray txTimesMs2 = new android.util.SparseLongArray(uidCount);
            int i2 = 0;
            long leftOverTxTimeMs = txTimeMs;
            while (i2 < uidCount) {
                com.android.server.power.stats.BatteryStatsImpl.Uid u2 = this.mUidStats.valueAt(i2);
                if (u2.mBluetoothScanTimer == null) {
                    txTimesMs = txTimesMs2;
                    rxTimesMs = rxTimesMs2;
                    rxTimeMs = rxTimeMs3;
                } else {
                    long scanTimeSinceMarkMs2 = u2.mBluetoothScanTimer.getTimeSinceMarkLocked(elapsedRealtimeMs * 1000) / 1000;
                    if (scanTimeSinceMarkMs2 <= 0) {
                        txTimesMs = txTimesMs2;
                        rxTimesMs = rxTimesMs2;
                        rxTimeMs = rxTimeMs3;
                    } else {
                        u2.mBluetoothScanTimer.setMark(elapsedRealtimeMs);
                        if (!normalizeScanRxTime) {
                            scanTimeSinceMarkMs = scanTimeSinceMarkMs2;
                        } else {
                            long scanTimeRxSinceMarkMs = (rxTimeMs3 * scanTimeSinceMarkMs2) / totalScanTimeMs;
                            scanTimeSinceMarkMs = scanTimeRxSinceMarkMs;
                        }
                        if (!normalizeScanTxTime) {
                            rxTimeMs = rxTimeMs3;
                            rxTimeMs2 = scanTimeSinceMarkMs2;
                        } else {
                            long scanTimeTxSinceMarkMs = (txTimeMs * scanTimeSinceMarkMs2) / totalScanTimeMs;
                            rxTimeMs = rxTimeMs3;
                            rxTimeMs2 = scanTimeTxSinceMarkMs;
                        }
                        rxTimesMs2.incrementValue(u2.getUid(), scanTimeSinceMarkMs);
                        txTimesMs2.incrementValue(u2.getUid(), rxTimeMs2);
                        if (uidEstimatedConsumptionMah == null) {
                            txTimesMs = txTimesMs2;
                            rxTimesMs = rxTimesMs2;
                        } else {
                            txTimesMs = txTimesMs2;
                            rxTimesMs = rxTimesMs2;
                            uidEstimatedConsumptionMah.incrementValue(u2.getUid(), this.mBluetoothPowerCalculator.calculatePowerMah(scanTimeSinceMarkMs, rxTimeMs2, 0L));
                        }
                        leftOverRxTimeMs -= scanTimeSinceMarkMs;
                        leftOverTxTimeMs -= rxTimeMs2;
                    }
                }
                i2++;
                rxTimesMs2 = rxTimesMs;
                txTimesMs2 = txTimesMs;
                rxTimeMs3 = rxTimeMs;
            }
            android.util.SparseLongArray txTimesMs3 = txTimesMs2;
            android.util.SparseLongArray txTimesMs4 = rxTimesMs2;
            long rxTimeMs4 = rxTimeMs3;
            java.util.List<android.bluetooth.UidTraffic> uidTraffic = info.getUidTraffic();
            int numUids = uidTraffic.size();
            long totalTxBytes = 0;
            int i3 = 0;
            long totalRxBytes = 0;
            while (i3 < numUids) {
                android.bluetooth.UidTraffic traffic = uidTraffic.get(i3);
                long rxBytes = traffic.getRxBytes() - this.mLastBluetoothActivityInfo.uidRxBytes.get(traffic.getUid());
                long txBytes = traffic.getTxBytes() - this.mLastBluetoothActivityInfo.uidTxBytes.get(traffic.getUid());
                this.mNetworkByteActivityCounters[4].addCountLocked(rxBytes);
                this.mNetworkByteActivityCounters[5].addCountLocked(txBytes);
                android.util.SparseLongArray rxTimesMs3 = txTimesMs4;
                com.android.server.power.stats.BatteryStatsImpl.Uid u3 = getUidStatsLocked(mapUid(traffic.getUid()), elapsedRealtimeMs, uptimeMs);
                u3.noteNetworkActivityLocked(4, rxBytes, 0L);
                u3.noteNetworkActivityLocked(5, txBytes, 0L);
                totalRxBytes += rxBytes;
                totalTxBytes += txBytes;
                i3++;
                txTimesMs3 = txTimesMs3;
                uidCount = uidCount;
                txTimeMs = txTimeMs;
                txTimesMs4 = rxTimesMs3;
            }
            android.util.SparseLongArray rxTimesMs4 = txTimesMs4;
            long txTimeMs2 = txTimeMs;
            android.util.SparseLongArray txTimesMs5 = txTimesMs3;
            if ((totalTxBytes != 0 || totalRxBytes != 0) && (leftOverRxTimeMs != 0 || leftOverTxTimeMs != 0)) {
                int i4 = 0;
                while (i4 < numUids) {
                    android.bluetooth.UidTraffic traffic2 = uidTraffic.get(i4);
                    int uid = traffic2.getUid();
                    long rxBytes2 = traffic2.getRxBytes() - this.mLastBluetoothActivityInfo.uidRxBytes.get(uid);
                    long txBytes2 = traffic2.getTxBytes() - this.mLastBluetoothActivityInfo.uidTxBytes.get(uid);
                    java.util.List<android.bluetooth.UidTraffic> uidTraffic2 = uidTraffic;
                    getUidStatsLocked(mapUid(uid), elapsedRealtimeMs, uptimeMs).getOrCreateBluetoothControllerActivityLocked();
                    if (totalRxBytes > 0 && rxBytes2 > 0) {
                        long timeRxMs = (leftOverRxTimeMs * rxBytes2) / totalRxBytes;
                        rxTimesMs4.incrementValue(uid, timeRxMs);
                    }
                    if (totalTxBytes > 0 && txBytes2 > 0) {
                        long timeTxMs = (leftOverTxTimeMs * txBytes2) / totalTxBytes;
                        txTimesMs5.incrementValue(uid, timeTxMs);
                    }
                    i4++;
                    uidTraffic = uidTraffic2;
                }
                int i5 = 0;
                while (i5 < txTimesMs5.size()) {
                    int uid2 = txTimesMs5.keyAt(i5);
                    long myTxTimeMs = txTimesMs5.valueAt(i5);
                    int numUids2 = numUids;
                    android.util.SparseLongArray txTimesMs6 = txTimesMs5;
                    getUidStatsLocked(uid2, elapsedRealtimeMs, uptimeMs).getOrCreateBluetoothControllerActivityLocked().getOrCreateTxTimeCounters()[0].increment(myTxTimeMs, elapsedRealtimeMs);
                    if (uidEstimatedConsumptionMah != null) {
                        uidEstimatedConsumptionMah.incrementValue(uid2, this.mBluetoothPowerCalculator.calculatePowerMah(0L, myTxTimeMs, 0L));
                    }
                    i5++;
                    numUids = numUids2;
                    txTimesMs5 = txTimesMs6;
                }
                for (int i6 = 0; i6 < rxTimesMs4.size(); i6++) {
                    int uid3 = rxTimesMs4.keyAt(i6);
                    long myRxTimeMs = rxTimesMs4.valueAt(i6);
                    getUidStatsLocked(rxTimesMs4.keyAt(i6), elapsedRealtimeMs, uptimeMs).getOrCreateBluetoothControllerActivityLocked().getOrCreateRxTimeCounter().increment(myRxTimeMs, elapsedRealtimeMs);
                    if (uidEstimatedConsumptionMah != null) {
                        uidEstimatedConsumptionMah.incrementValue(uid3, this.mBluetoothPowerCalculator.calculatePowerMah(myRxTimeMs, 0L, 0L));
                    }
                }
            }
            this.mBluetoothActivity.getOrCreateRxTimeCounter().increment(rxTimeMs4, elapsedRealtimeMs);
            this.mBluetoothActivity.getOrCreateTxTimeCounters()[0].increment(txTimeMs2, elapsedRealtimeMs);
            this.mBluetoothActivity.getOrCreateIdleTimeCounter().increment(idleTimeMs4, elapsedRealtimeMs);
            double opVolt = this.mPowerProfile.getAveragePower("bluetooth.controller.voltage") / 1000.0d;
            if (opVolt != 0.0d) {
                double controllerMaMs2 = (info.getControllerEnergyUsed() - this.mLastBluetoothActivityInfo.energy) / opVolt;
                this.mBluetoothActivity.getPowerCounter().addCountLocked((long) controllerMaMs2);
                this.mBatteryStatsImplExt.recordBluetoothPowerDrainMaMs((long) ((info.getControllerEnergyUsed() - this.mLastBluetoothActivityInfo.energy) / opVolt));
                controllerMaMs = controllerMaMs2;
            } else {
                controllerMaMs = 0.0d;
            }
            if (uidEstimatedConsumptionMah != null) {
                this.mGlobalEnergyConsumerStats.updateStandardBucket(5, consumedChargeUC);
                double totalEstimatedMah = this.mBluetoothPowerCalculator.calculatePowerMah(rxTimeMs4, txTimeMs2, idleTimeMs4);
                distributeEnergyToUidsLocked(5, consumedChargeUC, uidEstimatedConsumptionMah, java.lang.Math.max(totalEstimatedMah, controllerMaMs / MILLISECONDS_IN_HOUR), elapsedRealtimeMs);
            }
            this.mLastBluetoothActivityInfo.set(info);
            return;
        }
        android.bluetooth.BluetoothActivityEnergyInfo bluetoothActivityEnergyInfo = info;
        this.mLastBluetoothActivityInfo.set(bluetoothActivityEnergyInfo);
    }

    public void fillLowPowerStats() {
        if (this.mPlatformIdleStateCallback == null) {
            return;
        }
        com.android.internal.os.RpmStats rpmStats = new com.android.internal.os.RpmStats();
        long now = android.os.SystemClock.elapsedRealtime();
        if (now - this.mLastRpmStatsUpdateTimeMs >= 1000) {
            this.mPlatformIdleStateCallback.fillLowPowerStats(rpmStats);
            synchronized (this) {
                this.mTmpRpmStats = rpmStats;
                this.mLastRpmStatsUpdateTimeMs = now;
            }
        }
    }

    public void updateRpmStatsLocked(long elapsedRealtimeUs) {
        if (this.mTmpRpmStats == null) {
            return;
        }
        for (java.util.Map.Entry<java.lang.String, com.android.internal.os.RpmStats.PowerStatePlatformSleepState> pstate : this.mTmpRpmStats.mPlatformLowPowerStats.entrySet()) {
            java.lang.String pName = pstate.getKey();
            long pTimeUs = pstate.getValue().mTimeMs * 1000;
            int pCount = pstate.getValue().mCount;
            getRpmTimerLocked(pName).update(pTimeUs, pCount, elapsedRealtimeUs);
            for (java.util.Map.Entry<java.lang.String, com.android.internal.os.RpmStats.PowerStateElement> voter : pstate.getValue().mVoters.entrySet()) {
                java.lang.String vName = pName + "." + voter.getKey();
                long vTimeUs = voter.getValue().mTimeMs * 1000;
                int vCount = voter.getValue().mCount;
                getRpmTimerLocked(vName).update(vTimeUs, vCount, elapsedRealtimeUs);
            }
        }
        for (java.util.Map.Entry<java.lang.String, com.android.internal.os.RpmStats.PowerStateSubsystem> subsys : this.mTmpRpmStats.mSubsystemLowPowerStats.entrySet()) {
            java.lang.String subsysName = subsys.getKey();
            for (java.util.Map.Entry<java.lang.String, com.android.internal.os.RpmStats.PowerStateElement> sstate : subsys.getValue().mStates.entrySet()) {
                java.lang.String name = subsysName + "." + sstate.getKey();
                long timeUs = sstate.getValue().mTimeMs * 1000;
                getRpmTimerLocked(name).update(timeUs, sstate.getValue().mCount, elapsedRealtimeUs);
            }
        }
    }

    private void updateCpuEnergyConsumerStatsLocked(long[] clusterChargeUC, com.android.server.power.stats.BatteryStatsImpl.CpuDeltaPowerAccumulator accumulator) {
        int i;
        if (this.mGlobalEnergyConsumerStats == null) {
            return;
        }
        int numClusters = clusterChargeUC.length;
        long totalCpuChargeUC = 0;
        for (long j : clusterChargeUC) {
            totalCpuChargeUC += j;
        }
        if (totalCpuChargeUC <= 0) {
            return;
        }
        long timestampMs = this.mClock.elapsedRealtime();
        this.mGlobalEnergyConsumerStats.updateStandardBucket(3, totalCpuChargeUC, timestampMs);
        double[] clusterChargeRatio = new double[numClusters];
        for (int cluster = 0; cluster < numClusters; cluster++) {
            double totalClusterChargeMah = accumulator.totalClusterChargesMah[cluster];
            if (totalClusterChargeMah <= 0.0d) {
                clusterChargeRatio[cluster] = 0.0d;
            } else {
                clusterChargeRatio[cluster] = clusterChargeUC[cluster] / accumulator.totalClusterChargesMah[cluster];
            }
        }
        long uidChargeArraySize = accumulator.perUidCpuClusterChargesMah.size();
        int i2 = 0;
        while (i2 < uidChargeArraySize) {
            com.android.server.power.stats.BatteryStatsImpl.Uid uid = accumulator.perUidCpuClusterChargesMah.keyAt(i2);
            double[] uidClusterChargesMah = accumulator.perUidCpuClusterChargesMah.valueAt(i2);
            long uidChargeArraySize2 = uidChargeArraySize;
            int i3 = i2;
            long uidCpuChargeUC = 0;
            for (int cluster2 = 0; cluster2 < numClusters; cluster2++) {
                double uidClusterChargeMah = uidClusterChargesMah[cluster2];
                long uidClusterChargeUC = (long) ((clusterChargeRatio[cluster2] * uidClusterChargeMah) + 0.5d);
                uidCpuChargeUC += uidClusterChargeUC;
            }
            if (uidCpuChargeUC < 0) {
                android.util.Slog.wtf(TAG, "Unexpected proportional EnergyConsumer charge (" + uidCpuChargeUC + ") for uid " + uid.mUid);
                i = i3;
            } else {
                i = i3;
                uid.addChargeToStandardBucketLocked(uidCpuChargeUC, 3, timestampMs);
            }
            i2 = i + 1;
            uidChargeArraySize = uidChargeArraySize2;
        }
    }

    public void updateDisplayEnergyConsumerStatsLocked(long[] chargesUC, int[] screenStates, long elapsedRealtimeMs) {
        int numDisplays;
        long totalScreenOnChargeUC;
        if (this.mGlobalEnergyConsumerStats == null) {
            return;
        }
        if (this.mPerDisplayBatteryStats.length == screenStates.length) {
            numDisplays = screenStates.length;
        } else {
            int numDisplays2 = this.mDisplayMismatchWtfCount;
            this.mDisplayMismatchWtfCount = numDisplays2 + 1;
            if (numDisplays2 % 100 == 0) {
                android.util.Slog.wtf(TAG, "Mismatch between PowerProfile reported display count (" + this.mPerDisplayBatteryStats.length + ") and PowerStatsHal reported display count (" + screenStates.length + ")");
            }
            numDisplays = this.mPerDisplayBatteryStats.length < screenStates.length ? this.mPerDisplayBatteryStats.length : screenStates.length;
        }
        int[] oldScreenStates = new int[numDisplays];
        for (int i = 0; i < numDisplays; i++) {
            int screenState = screenStates[i];
            oldScreenStates[i] = this.mPerDisplayBatteryStats[i].screenStateAtLastEnergyMeasurement;
            this.mPerDisplayBatteryStats[i].screenStateAtLastEnergyMeasurement = screenState;
        }
        if (!this.mOnBatteryInternal) {
            return;
        }
        if (this.mIgnoreNextExternalStats) {
            int uidStatsSize = this.mUidStats.size();
            for (int i2 = 0; i2 < uidStatsSize; i2++) {
                this.mUidStats.valueAt(i2).markProcessForegroundTimeUs(elapsedRealtimeMs, false);
            }
            return;
        }
        int i3 = 0;
        long totalScreenOnChargeUC2 = 0;
        while (true) {
            totalScreenOnChargeUC = 0;
            if (i3 >= numDisplays) {
                break;
            }
            long chargeUC = chargesUC[i3];
            if (chargeUC > 0) {
                int powerBucket = com.android.internal.power.EnergyConsumerStats.getDisplayPowerBucket(oldScreenStates[i3]);
                this.mGlobalEnergyConsumerStats.updateStandardBucket(powerBucket, chargeUC);
                if (powerBucket == 0) {
                    totalScreenOnChargeUC2 += chargeUC;
                }
            }
            i3++;
        }
        if (totalScreenOnChargeUC2 <= 0) {
            return;
        }
        android.util.SparseDoubleArray fgTimeUsArray = new android.util.SparseDoubleArray();
        long j = elapsedRealtimeMs * 1000;
        int uidStatsSize2 = this.mUidStats.size();
        int i4 = 0;
        while (i4 < uidStatsSize2) {
            com.android.server.power.stats.BatteryStatsImpl.Uid uid = this.mUidStats.valueAt(i4);
            long fgTimeUs = uid.markProcessForegroundTimeUs(elapsedRealtimeMs, true);
            if (fgTimeUs != totalScreenOnChargeUC) {
                fgTimeUsArray.put(uid.getUid(), fgTimeUs);
            }
            i4++;
            totalScreenOnChargeUC = 0;
        }
        distributeEnergyToUidsLocked(0, totalScreenOnChargeUC2, fgTimeUsArray, 0.0d, elapsedRealtimeMs);
    }

    public void updateGnssEnergyConsumerStatsLocked(long chargeUC, long elapsedRealtimeMs) {
        if (this.mGlobalEnergyConsumerStats == null || !this.mOnBatteryInternal || chargeUC <= 0) {
            return;
        }
        if (this.mIgnoreNextExternalStats) {
            int uidStatsSize = this.mUidStats.size();
            for (int i = 0; i < uidStatsSize; i++) {
                this.mUidStats.valueAt(i).markGnssTimeUs(elapsedRealtimeMs);
            }
            return;
        }
        this.mGlobalEnergyConsumerStats.updateStandardBucket(6, chargeUC);
        android.util.SparseDoubleArray gnssTimeUsArray = new android.util.SparseDoubleArray();
        int uidStatsSize2 = this.mUidStats.size();
        for (int i2 = 0; i2 < uidStatsSize2; i2++) {
            com.android.server.power.stats.BatteryStatsImpl.Uid uid = this.mUidStats.valueAt(i2);
            long gnssTimeUs = uid.markGnssTimeUs(elapsedRealtimeMs);
            if (gnssTimeUs != 0) {
                gnssTimeUsArray.put(uid.getUid(), gnssTimeUs);
            }
        }
        distributeEnergyToUidsLocked(6, chargeUC, gnssTimeUsArray, 0.0d, elapsedRealtimeMs);
    }

    public void updateCameraEnergyConsumerStatsLocked(long chargeUC, long elapsedRealtimeMs) {
        if (this.mGlobalEnergyConsumerStats == null || !this.mOnBatteryInternal || chargeUC <= 0) {
            return;
        }
        if (this.mIgnoreNextExternalStats) {
            int uidStatsSize = this.mUidStats.size();
            for (int i = 0; i < uidStatsSize; i++) {
                this.mUidStats.valueAt(i).markCameraTimeUs(elapsedRealtimeMs);
            }
            return;
        }
        this.mGlobalEnergyConsumerStats.updateStandardBucket(8, chargeUC);
        android.util.SparseDoubleArray cameraTimeUsArray = new android.util.SparseDoubleArray();
        int uidStatsSize2 = this.mUidStats.size();
        for (int i2 = 0; i2 < uidStatsSize2; i2++) {
            com.android.server.power.stats.BatteryStatsImpl.Uid uid = this.mUidStats.valueAt(i2);
            long cameraTimeUs = uid.markCameraTimeUs(elapsedRealtimeMs);
            if (cameraTimeUs != 0) {
                cameraTimeUsArray.put(uid.getUid(), cameraTimeUs);
            }
        }
        distributeEnergyToUidsLocked(8, chargeUC, cameraTimeUsArray, 0.0d, elapsedRealtimeMs);
    }

    public void updateCustomEnergyConsumerStatsLocked(int customPowerBucket, long totalChargeUC, android.util.SparseLongArray uidCharges) {
        if (this.mGlobalEnergyConsumerStats == null || !this.mOnBatteryInternal || this.mIgnoreNextExternalStats || totalChargeUC <= 0) {
            return;
        }
        this.mGlobalEnergyConsumerStats.updateCustomBucket(customPowerBucket, totalChargeUC, this.mClock.elapsedRealtime());
        if (uidCharges == null) {
            return;
        }
        int numUids = uidCharges.size();
        for (int i = 0; i < numUids; i++) {
            int uidInt = mapUid(uidCharges.keyAt(i));
            long uidChargeUC = uidCharges.valueAt(i);
            if (uidChargeUC != 0) {
                com.android.server.power.stats.BatteryStatsImpl.Uid uidObj = getAvailableUidStatsLocked(uidInt);
                if (uidObj != null) {
                    uidObj.addChargeToCustomBucketLocked(uidChargeUC, customPowerBucket);
                } else if (!android.os.Process.isIsolated(uidInt)) {
                    android.util.Slog.w(TAG, "Received EnergyConsumer charge " + totalChargeUC + " for custom bucket " + customPowerBucket + " for non-existent uid " + uidInt);
                }
            }
        }
    }

    private void distributeEnergyToUidsLocked(int bucket, long totalConsumedChargeUC, android.util.SparseDoubleArray ratioNumerators, double minRatioDenominator, long timestampMs) {
        double sumRatioNumerators = 0.0d;
        for (int i = ratioNumerators.size() - 1; i >= 0; i--) {
            sumRatioNumerators += ratioNumerators.valueAt(i);
        }
        double ratioDenominator = java.lang.Math.max(sumRatioNumerators, minRatioDenominator);
        if (ratioDenominator <= 0.0d) {
            return;
        }
        for (int i2 = ratioNumerators.size() - 1; i2 >= 0; i2--) {
            com.android.server.power.stats.BatteryStatsImpl.Uid uid = getAvailableUidStatsLocked(ratioNumerators.keyAt(i2));
            double ratioNumerator = ratioNumerators.valueAt(i2);
            uid.addChargeToStandardBucketLocked((long) (((totalConsumedChargeUC * ratioNumerator) / ratioDenominator) + 0.5d), bucket, timestampMs);
        }
    }

    public void updateRailStatsLocked() {
        if (this.mEnergyConsumerRetriever == null || !this.mTmpRailStats.isRailStatsAvailable()) {
            return;
        }
        this.mEnergyConsumerRetriever.fillRailDataStats(this.mTmpRailStats);
    }

    public void informThatAllExternalStatsAreFlushed() {
        synchronized (this) {
            this.mIgnoreNextExternalStats = false;
        }
    }

    public void updateKernelWakelocksLocked(long elapsedRealtimeUs) {
        if (this.mKernelWakelockReader != null) {
            com.android.server.power.stats.KernelWakelockStats wakelockStats = this.mKernelWakelockReader.readKernelWakelockStats(this.mTmpWakelockStats);
            if (wakelockStats == null) {
                android.util.Slog.w(TAG, "Couldn't get kernel wake lock stats");
                return;
            }
            for (java.util.Map.Entry<java.lang.String, com.android.server.power.stats.KernelWakelockStats.Entry> ent : wakelockStats.entrySet()) {
                java.lang.String name = ent.getKey();
                com.android.server.power.stats.KernelWakelockStats.Entry kws = ent.getValue();
                com.android.server.power.stats.BatteryStatsImpl.SamplingTimer kwlt = this.mKernelWakelockStats.get(name);
                if (kwlt == null) {
                    kwlt = new com.android.server.power.stats.BatteryStatsImpl.SamplingTimer(this.mClock, this.mOnBatteryScreenOffTimeBase);
                    this.mKernelWakelockStats.put(name, kwlt);
                }
                kwlt.update(kws.totalTimeUs, kws.activeTimeUs, kws.count, elapsedRealtimeUs);
                kwlt.setUpdateVersion(kws.version);
            }
            int numWakelocksSetStale = 0;
            java.util.Iterator<java.util.Map.Entry<java.lang.String, com.android.server.power.stats.BatteryStatsImpl.SamplingTimer>> it = this.mKernelWakelockStats.entrySet().iterator();
            while (it.hasNext()) {
                com.android.server.power.stats.BatteryStatsImpl.SamplingTimer st = it.next().getValue();
                if (st.getUpdateVersion() != wakelockStats.kernelWakelockVersion) {
                    st.endSample(elapsedRealtimeUs);
                    numWakelocksSetStale++;
                }
            }
        }
    }

    public void updateKernelMemoryBandwidthLocked(long elapsedRealtimeUs) {
        com.android.server.power.stats.BatteryStatsImpl.SamplingTimer timer;
        this.mKernelMemoryBandwidthStats.updateStats();
        android.util.LongSparseLongArray bandwidthEntries = this.mKernelMemoryBandwidthStats.getBandwidthEntries();
        int bandwidthEntryCount = bandwidthEntries.size();
        for (int i = 0; i < bandwidthEntryCount; i++) {
            int index = this.mKernelMemoryStats.indexOfKey(bandwidthEntries.keyAt(i));
            if (index >= 0) {
                timer = this.mKernelMemoryStats.valueAt(index);
            } else {
                timer = new com.android.server.power.stats.BatteryStatsImpl.SamplingTimer(this.mClock, this.mOnBatteryTimeBase);
                this.mKernelMemoryStats.put(bandwidthEntries.keyAt(i), timer);
            }
            timer.update(bandwidthEntries.valueAt(i), 1, elapsedRealtimeUs);
        }
    }

    public boolean isOnBatteryLocked() {
        return this.mOnBatteryTimeBase.isRunning();
    }

    public boolean isOnBatteryScreenOffLocked() {
        return this.mOnBatteryScreenOffTimeBase.isRunning();
    }

    public static class CpuDeltaPowerAccumulator {
        private final com.android.server.power.stats.CpuPowerCalculator mCalculator;
        public final double[] totalClusterChargesMah;
        private com.android.server.power.stats.BatteryStatsImpl.Uid mCachedUid = null;
        private double[] mUidClusterCache = null;
        public final android.util.ArrayMap<com.android.server.power.stats.BatteryStatsImpl.Uid, double[]> perUidCpuClusterChargesMah = new android.util.ArrayMap<>();

        CpuDeltaPowerAccumulator(com.android.server.power.stats.CpuPowerCalculator calculator, int nClusters) {
            this.mCalculator = calculator;
            this.totalClusterChargesMah = new double[nClusters];
        }

        public void addCpuClusterDurationsMs(com.android.server.power.stats.BatteryStatsImpl.Uid uid, long[] durationsMs) {
            double[] uidChargesMah = getOrCreateUidCpuClusterCharges(uid);
            for (int cluster = 0; cluster < durationsMs.length; cluster++) {
                double estimatedDeltaMah = this.mCalculator.calculatePerCpuClusterPowerMah(cluster, durationsMs[cluster]);
                uidChargesMah[cluster] = uidChargesMah[cluster] + estimatedDeltaMah;
                double[] dArr = this.totalClusterChargesMah;
                dArr[cluster] = dArr[cluster] + estimatedDeltaMah;
            }
        }

        public void addCpuClusterSpeedDurationsMs(com.android.server.power.stats.BatteryStatsImpl.Uid uid, int cluster, int speed, long durationsMs) {
            double[] uidChargesMah = getOrCreateUidCpuClusterCharges(uid);
            double estimatedDeltaMah = this.mCalculator.calculatePerCpuFreqPowerMah(cluster, speed, durationsMs);
            uidChargesMah[cluster] = uidChargesMah[cluster] + estimatedDeltaMah;
            double[] dArr = this.totalClusterChargesMah;
            dArr[cluster] = dArr[cluster] + estimatedDeltaMah;
        }

        private double[] getOrCreateUidCpuClusterCharges(com.android.server.power.stats.BatteryStatsImpl.Uid uid) {
            if (uid == this.mCachedUid) {
                return this.mUidClusterCache;
            }
            double[] uidChargesMah = this.perUidCpuClusterChargesMah.get(uid);
            if (uidChargesMah == null) {
                uidChargesMah = new double[this.totalClusterChargesMah.length];
                this.perUidCpuClusterChargesMah.put(uid, uidChargesMah);
            }
            this.mCachedUid = uid;
            this.mUidClusterCache = uidChargesMah;
            return uidChargesMah;
        }
    }

    public void updateCpuTimeLocked(boolean onBattery, boolean onBatteryScreenOff, long[] cpuClusterChargeUC) {
        com.android.server.power.stats.BatteryStatsImpl.CpuDeltaPowerAccumulator powerAccumulator;
        java.util.ArrayList<com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer> partialTimersToConsider = null;
        if (onBatteryScreenOff) {
            partialTimersToConsider = new java.util.ArrayList<>();
            for (int i = this.mPartialTimers.size() - 1; i >= 0; i--) {
                com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer timer = this.mPartialTimers.get(i);
                if (timer.mInList && timer.mUid != null && timer.mUid.mUid != 1000) {
                    partialTimersToConsider.add(timer);
                }
            }
        }
        markPartialTimersAsEligible();
        if (!onBattery) {
            this.mCpuUidUserSysTimeReader.readDelta(false, (com.android.internal.os.KernelCpuUidTimeReader.Callback) null);
            this.mCpuUidFreqTimeReader.readDelta(false, (com.android.internal.os.KernelCpuUidTimeReader.Callback) null);
            this.mNumAllUidCpuTimeReads += 2;
            if (this.mConstants.TRACK_CPU_ACTIVE_CLUSTER_TIME) {
                this.mCpuUidActiveTimeReader.readDelta(false, (com.android.internal.os.KernelCpuUidTimeReader.Callback) null);
                this.mCpuUidClusterTimeReader.readDelta(false, (com.android.internal.os.KernelCpuUidTimeReader.Callback) null);
                this.mNumAllUidCpuTimeReads += 2;
            }
            for (int i2 = this.mKernelCpuSpeedReaders.length - 1; i2 >= 0; i2--) {
                if (this.mKernelCpuSpeedReaders[i2] != null) {
                    this.mKernelCpuSpeedReaders[i2].readDelta();
                }
            }
            if (!com.android.internal.hidden_from_bootclasspath.com.android.server.power.optimization.Flags.disableSystemServicePowerAttr()) {
                this.mSystemServerCpuThreadReader.readDelta();
                return;
            }
            return;
        }
        this.mUserInfoProvider.refreshUserIds();
        android.util.SparseLongArray updatedUids = this.mCpuUidFreqTimeReader.allUidTimesAvailable() ? null : new android.util.SparseLongArray();
        if (this.mGlobalEnergyConsumerStats != null && this.mGlobalEnergyConsumerStats.isStandardBucketSupported(3)) {
            if (cpuClusterChargeUC == null) {
                android.util.Slog.wtf(TAG, "POWER_BUCKET_CPU supported but no EnergyConsumer Cpu Cluster charge reported on updateCpuTimeLocked!");
                powerAccumulator = null;
            } else {
                if (this.mCpuPowerCalculator == null) {
                    this.mCpuPowerCalculator = new com.android.server.power.stats.CpuPowerCalculator(this.mCpuScalingPolicies, this.mPowerProfile);
                }
                powerAccumulator = new com.android.server.power.stats.BatteryStatsImpl.CpuDeltaPowerAccumulator(this.mCpuPowerCalculator, this.mCpuScalingPolicies.getPolicies().length);
            }
        } else {
            powerAccumulator = null;
        }
        readKernelUidCpuTimesLocked(partialTimersToConsider, updatedUids, onBattery);
        if (updatedUids != null) {
            updateClusterSpeedTimes(updatedUids, onBattery, powerAccumulator);
        }
        readKernelUidCpuFreqTimesLocked(partialTimersToConsider, onBattery, onBatteryScreenOff, powerAccumulator);
        this.mNumAllUidCpuTimeReads += 2;
        if (this.mConstants.TRACK_CPU_ACTIVE_CLUSTER_TIME) {
            readKernelUidCpuActiveTimesLocked(onBattery);
            readKernelUidCpuClusterTimesLocked(onBattery, powerAccumulator);
            this.mNumAllUidCpuTimeReads += 2;
        }
        if (!com.android.internal.hidden_from_bootclasspath.com.android.server.power.optimization.Flags.disableSystemServicePowerAttr()) {
            updateSystemServerThreadStats();
        }
        if (powerAccumulator != null) {
            updateCpuEnergyConsumerStatsLocked(cpuClusterChargeUC, powerAccumulator);
        }
    }

    public void updateSystemServerThreadStats() {
        com.android.server.power.stats.SystemServerCpuThreadReader.SystemServiceCpuThreadTimes systemServiceCpuThreadTimes = this.mSystemServerCpuThreadReader.readDelta();
        if (systemServiceCpuThreadTimes == null) {
            return;
        }
        if (this.mBinderThreadCpuTimesUs == null) {
            this.mBinderThreadCpuTimesUs = new com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounterArray(this.mOnBatteryTimeBase);
        }
        this.mBinderThreadCpuTimesUs.addCountLocked(systemServiceCpuThreadTimes.binderThreadCpuTimesUs);
    }

    public void markPartialTimersAsEligible() {
        if (com.android.internal.util.ArrayUtils.referenceEquals(this.mPartialTimers, this.mLastPartialTimers)) {
            for (int i = this.mPartialTimers.size() - 1; i >= 0; i--) {
                this.mPartialTimers.get(i).mInList = true;
            }
            return;
        }
        for (int i2 = this.mLastPartialTimers.size() - 1; i2 >= 0; i2--) {
            this.mLastPartialTimers.get(i2).mInList = false;
        }
        this.mLastPartialTimers.clear();
        int numPartialTimers = this.mPartialTimers.size();
        for (int i3 = 0; i3 < numPartialTimers; i3++) {
            com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer timer = this.mPartialTimers.get(i3);
            timer.mInList = true;
            this.mLastPartialTimers.add(timer);
        }
    }

    public void updateClusterSpeedTimes(android.util.SparseLongArray updatedUids, boolean onBattery, com.android.server.power.stats.BatteryStatsImpl.CpuDeltaPowerAccumulator powerAccumulator) {
        int speedsInCluster;
        long elapsedRealtimeMs;
        android.util.SparseLongArray sparseLongArray = updatedUids;
        long[][] clusterSpeedTimesMs = new long[this.mKernelCpuSpeedReaders.length][];
        long totalCpuClustersTimeMs = 0;
        for (int cluster = 0; cluster < this.mKernelCpuSpeedReaders.length; cluster++) {
            if (this.mKernelCpuSpeedReaders[cluster] != null) {
                clusterSpeedTimesMs[cluster] = this.mKernelCpuSpeedReaders[cluster].readDelta();
                if (clusterSpeedTimesMs[cluster] != null) {
                    for (int speed = clusterSpeedTimesMs[cluster].length - 1; speed >= 0; speed--) {
                        totalCpuClustersTimeMs += clusterSpeedTimesMs[cluster][speed];
                    }
                }
            }
        }
        if (totalCpuClustersTimeMs != 0) {
            int i = updatedUids.size();
            long elapsedRealtimeMs2 = this.mClock.elapsedRealtime();
            long uptimeMs = this.mClock.uptimeMillis();
            int cluster2 = 0;
            while (cluster2 < i) {
                int updatedUidsCount = i;
                int updatedUidsCount2 = cluster2;
                com.android.server.power.stats.BatteryStatsImpl.Uid u = getUidStatsLocked(sparseLongArray.keyAt(cluster2), elapsedRealtimeMs2, uptimeMs);
                long appCpuTimeUs = sparseLongArray.valueAt(updatedUidsCount2);
                int[] policies = this.mCpuScalingPolicies.getPolicies();
                if (u.mCpuClusterSpeedTimesUs == null || u.mCpuClusterSpeedTimesUs.length != policies.length) {
                    u.mCpuClusterSpeedTimesUs = new com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounter[policies.length][];
                }
                int cluster3 = 0;
                while (cluster3 < policies.length) {
                    int speedsInCluster2 = clusterSpeedTimesMs[cluster3].length;
                    int[] policies2 = policies;
                    if (u.mCpuClusterSpeedTimesUs[cluster3] == null || speedsInCluster2 != u.mCpuClusterSpeedTimesUs[cluster3].length) {
                        u.mCpuClusterSpeedTimesUs[cluster3] = new com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounter[speedsInCluster2];
                    }
                    com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounter[] cpuSpeeds = u.mCpuClusterSpeedTimesUs[cluster3];
                    int speed2 = 0;
                    while (speed2 < speedsInCluster2) {
                        if (cpuSpeeds[speed2] != null) {
                            speedsInCluster = speedsInCluster2;
                            elapsedRealtimeMs = elapsedRealtimeMs2;
                        } else {
                            speedsInCluster = speedsInCluster2;
                            elapsedRealtimeMs = elapsedRealtimeMs2;
                            cpuSpeeds[speed2] = new com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounter(this.mOnBatteryTimeBase);
                        }
                        long deltaSpeedCount = (clusterSpeedTimesMs[cluster3][speed2] * appCpuTimeUs) / totalCpuClustersTimeMs;
                        long appCpuTimeUs2 = appCpuTimeUs;
                        cpuSpeeds[speed2].addCountLocked(deltaSpeedCount, onBattery);
                        if (powerAccumulator != null) {
                            powerAccumulator.addCpuClusterSpeedDurationsMs(u, cluster3, speed2, deltaSpeedCount);
                        }
                        speed2++;
                        speedsInCluster2 = speedsInCluster;
                        elapsedRealtimeMs2 = elapsedRealtimeMs;
                        appCpuTimeUs = appCpuTimeUs2;
                    }
                    cluster3++;
                    policies = policies2;
                    appCpuTimeUs = appCpuTimeUs;
                }
                cluster2 = updatedUidsCount2 + 1;
                sparseLongArray = updatedUids;
                i = updatedUidsCount;
            }
        }
    }

    public void readKernelUidCpuTimesLocked(java.util.ArrayList<com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer> partialTimers, final android.util.SparseLongArray updatedUids, final boolean onBattery) {
        this.mTempTotalCpuSystemTimeUs = 0L;
        this.mTempTotalCpuUserTimeUs = 0L;
        final int numWakelocks = partialTimers == null ? 0 : partialTimers.size();
        final long startTimeMs = this.mClock.uptimeMillis();
        final long elapsedRealtimeMs = this.mClock.elapsedRealtime();
        this.mCpuUidUserSysTimeReader.readDelta(false, new com.android.internal.os.KernelCpuUidTimeReader.Callback() { // from class: com.android.server.power.stats.BatteryStatsImpl$$ExternalSyntheticLambda8
            public final void onUidCpuTime(int i, java.lang.Object obj) {
                this.f$0.lambda$readKernelUidCpuTimesLocked$3(elapsedRealtimeMs, startTimeMs, numWakelocks, onBattery, updatedUids, i, (long[]) obj);
            }
        });
        long elapsedTimeMs = this.mClock.uptimeMillis() - startTimeMs;
        if (elapsedTimeMs >= 100) {
            android.util.Slog.d(TAG, "Reading cpu stats took " + elapsedTimeMs + "ms");
        }
        if (numWakelocks > 0) {
            this.mTempTotalCpuUserTimeUs = (this.mTempTotalCpuUserTimeUs * 50) / 100;
            this.mTempTotalCpuSystemTimeUs = (this.mTempTotalCpuSystemTimeUs * 50) / 100;
            int i = 0;
            while (i < numWakelocks) {
                com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer timer = partialTimers.get(i);
                int userTimeUs = (int) (this.mTempTotalCpuUserTimeUs / ((long) (numWakelocks - i)));
                int systemTimeUs = (int) (this.mTempTotalCpuSystemTimeUs / ((long) (numWakelocks - i)));
                timer.mUid.mUserCpuTime.addCountLocked(userTimeUs, onBattery);
                timer.mUid.mSystemCpuTime.addCountLocked(systemTimeUs, onBattery);
                if (updatedUids != null) {
                    int uid = timer.mUid.getUid();
                    updatedUids.put(uid, updatedUids.get(uid, 0L) + ((long) userTimeUs) + ((long) systemTimeUs));
                }
                com.android.server.power.stats.BatteryStatsImpl.Uid.Proc proc = timer.mUid.getProcessStatsLocked("*wakelock*");
                proc.addCpuTimeLocked(userTimeUs / 1000, systemTimeUs / 1000, onBattery);
                this.mTempTotalCpuUserTimeUs -= (long) userTimeUs;
                this.mTempTotalCpuSystemTimeUs -= (long) systemTimeUs;
                i++;
                elapsedTimeMs = elapsedTimeMs;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$readKernelUidCpuTimesLocked$3(long elapsedRealtimeMs, long startTimeMs, int numWakelocks, boolean onBattery, android.util.SparseLongArray updatedUids, int uid, long[] timesUs) {
        long userTimeUs = timesUs[0];
        long systemTimeUs = timesUs[1];
        int uid2 = mapUid(uid);
        if (android.os.Process.isIsolated(uid2) || !this.mUserInfoProvider.exists(android.os.UserHandle.getUserId(uid2))) {
            return;
        }
        com.android.server.power.stats.BatteryStatsImpl.Uid u = getUidStatsLocked(uid2, elapsedRealtimeMs, startTimeMs);
        this.mTempTotalCpuUserTimeUs += userTimeUs;
        this.mTempTotalCpuSystemTimeUs += systemTimeUs;
        java.lang.StringBuilder sb = null;
        if (numWakelocks > 0) {
            userTimeUs = (userTimeUs * 50) / 100;
            systemTimeUs = (50 * systemTimeUs) / 100;
        }
        if (0 != 0) {
            sb.append("  adding to uid=").append(u.mUid).append(": u=");
            android.util.TimeUtils.formatDuration(userTimeUs / 1000, null);
            sb.append(" s=");
            android.util.TimeUtils.formatDuration(systemTimeUs / 1000, null);
            android.util.Slog.d(TAG, sb.toString());
        }
        u.mUserCpuTime.addCountLocked(userTimeUs, onBattery);
        u.mSystemCpuTime.addCountLocked(systemTimeUs, onBattery);
        if (updatedUids != null) {
            updatedUids.put(u.getUid(), userTimeUs + systemTimeUs);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:26:0x00b1  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void readKernelUidCpuFreqTimesLocked(java.util.ArrayList<com.android.server.power.stats.BatteryStatsImpl.StopwatchTimer> r30, final boolean r31, final boolean r32, final com.android.server.power.stats.BatteryStatsImpl.CpuDeltaPowerAccumulator r33) {
        /*
            Method dump skipped, instruction units count: 322
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.power.stats.BatteryStatsImpl.readKernelUidCpuFreqTimesLocked(java.util.ArrayList, boolean, boolean, com.android.server.power.stats.BatteryStatsImpl$CpuDeltaPowerAccumulator):void");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$readKernelUidCpuFreqTimesLocked$4(long elapsedRealtimeMs, long startTimeMs, boolean onBattery, boolean onBatteryScreenOff, boolean perClusterTimesAvailable, int numClusters, int numWakelocks, int[] policies, com.android.server.power.stats.BatteryStatsImpl.CpuDeltaPowerAccumulator powerAccumulator, int uid, long[] cpuFreqTimeMs) {
        long appAllocationUs;
        int speed;
        int uid2 = mapUid(uid);
        if (android.os.Process.isIsolated(uid2) || !this.mUserInfoProvider.exists(android.os.UserHandle.getUserId(uid2))) {
            return;
        }
        com.android.server.power.stats.BatteryStatsImpl.Uid u = getUidStatsLocked(uid2, elapsedRealtimeMs, startTimeMs);
        if (u.mCpuFreqTimeMs == null || u.mCpuFreqTimeMs.getSize() != cpuFreqTimeMs.length) {
            detachIfNotNull(u.mCpuFreqTimeMs);
            u.mCpuFreqTimeMs = new com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounterArray(this.mOnBatteryTimeBase);
        }
        u.mCpuFreqTimeMs.addCountLocked(cpuFreqTimeMs, onBattery);
        if (u.mScreenOffCpuFreqTimeMs == null || u.mScreenOffCpuFreqTimeMs.getSize() != cpuFreqTimeMs.length) {
            detachIfNotNull(u.mScreenOffCpuFreqTimeMs);
            u.mScreenOffCpuFreqTimeMs = new com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounterArray(this.mOnBatteryScreenOffTimeBase);
        }
        u.mScreenOffCpuFreqTimeMs.addCountLocked(cpuFreqTimeMs, onBatteryScreenOff);
        if (perClusterTimesAvailable) {
            if (u.mCpuClusterSpeedTimesUs == null || u.mCpuClusterSpeedTimesUs.length != numClusters) {
                detachIfNotNull(u.mCpuClusterSpeedTimesUs);
                u.mCpuClusterSpeedTimesUs = new com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounter[numClusters][];
            }
            if (numWakelocks > 0 && this.mWakeLockAllocationsUs == null) {
                this.mWakeLockAllocationsUs = new long[numClusters][];
            }
            int freqIndex = 0;
            for (int cluster = 0; cluster < numClusters; cluster++) {
                int[] freqs = this.mCpuScalingPolicies.getFrequencies(policies[cluster]);
                if (u.mCpuClusterSpeedTimesUs[cluster] == null || u.mCpuClusterSpeedTimesUs[cluster].length != freqs.length) {
                    detachIfNotNull(u.mCpuClusterSpeedTimesUs[cluster]);
                    u.mCpuClusterSpeedTimesUs[cluster] = new com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounter[freqs.length];
                }
                if (numWakelocks > 0 && this.mWakeLockAllocationsUs[cluster] == null) {
                    this.mWakeLockAllocationsUs[cluster] = new long[freqs.length];
                }
                com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounter[] cpuTimesUs = u.mCpuClusterSpeedTimesUs[cluster];
                int speed2 = 0;
                while (speed2 < freqs.length) {
                    if (cpuTimesUs[speed2] == null) {
                        cpuTimesUs[speed2] = new com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounter(this.mOnBatteryTimeBase);
                    }
                    if (this.mWakeLockAllocationsUs != null) {
                        long appAllocationUs2 = ((cpuFreqTimeMs[freqIndex] * 1000) * 50) / 100;
                        long[] jArr = this.mWakeLockAllocationsUs[cluster];
                        jArr[speed2] = jArr[speed2] + ((cpuFreqTimeMs[freqIndex] * 1000) - appAllocationUs2);
                        appAllocationUs = appAllocationUs2;
                    } else {
                        long appAllocationUs3 = cpuFreqTimeMs[freqIndex];
                        appAllocationUs = appAllocationUs3 * 1000;
                    }
                    cpuTimesUs[speed2].addCountLocked(appAllocationUs, onBattery);
                    if (powerAccumulator != null) {
                        speed = speed2;
                        powerAccumulator.addCpuClusterSpeedDurationsMs(u, cluster, speed2, appAllocationUs / 1000);
                    } else {
                        speed = speed2;
                    }
                    freqIndex++;
                    speed2 = speed + 1;
                }
            }
        }
    }

    public void readKernelUidCpuActiveTimesLocked(boolean onBattery) {
        final long startTimeMs = this.mClock.uptimeMillis();
        final long elapsedRealtimeMs = this.mClock.elapsedRealtime();
        this.mCpuUidActiveTimeReader.readAbsolute(new com.android.internal.os.KernelCpuUidTimeReader.Callback() { // from class: com.android.server.power.stats.BatteryStatsImpl$$ExternalSyntheticLambda1
            public final void onUidCpuTime(int i, java.lang.Object obj) {
                this.f$0.lambda$readKernelUidCpuActiveTimesLocked$5(elapsedRealtimeMs, startTimeMs, i, (java.lang.Long) obj);
            }
        });
        long elapsedTimeMs = this.mClock.uptimeMillis() - startTimeMs;
        if (elapsedTimeMs >= 100) {
            android.util.Slog.d(TAG, "Reading cpu active times took " + elapsedTimeMs + "ms");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$readKernelUidCpuActiveTimesLocked$5(long elapsedRealtimeMs, long startTimeMs, int uid, java.lang.Long cpuActiveTimesMs) {
        com.android.server.power.stats.BatteryStatsImpl.Uid.ChildUid childUid;
        int parentUid = mapUid(uid);
        if (android.os.Process.isIsolated(parentUid) || !this.mUserInfoProvider.exists(android.os.UserHandle.getUserId(uid))) {
            return;
        }
        com.android.server.power.stats.BatteryStatsImpl.Uid u = getUidStatsLocked(parentUid, elapsedRealtimeMs, startTimeMs);
        if (parentUid == uid) {
            u.getCpuActiveTimeCounter().update(cpuActiveTimesMs.longValue(), elapsedRealtimeMs);
            return;
        }
        android.util.SparseArray<com.android.server.power.stats.BatteryStatsImpl.Uid.ChildUid> childUids = u.mChildUids;
        if (childUids != null && (childUid = childUids.get(uid)) != null) {
            long delta = childUid.cpuActiveCounter.update(cpuActiveTimesMs.longValue(), elapsedRealtimeMs);
            u.getCpuActiveTimeCounter().increment(delta, elapsedRealtimeMs);
        }
    }

    public void readKernelUidCpuClusterTimesLocked(final boolean onBattery, final com.android.server.power.stats.BatteryStatsImpl.CpuDeltaPowerAccumulator powerAccumulator) {
        final long startTimeMs = this.mClock.uptimeMillis();
        final long elapsedRealtimeMs = this.mClock.elapsedRealtime();
        boolean forceRead = powerAccumulator != null;
        this.mCpuUidClusterTimeReader.readDelta(forceRead, new com.android.internal.os.KernelCpuUidTimeReader.Callback() { // from class: com.android.server.power.stats.BatteryStatsImpl$$ExternalSyntheticLambda7
            public final void onUidCpuTime(int i, java.lang.Object obj) {
                this.f$0.lambda$readKernelUidCpuClusterTimesLocked$6(elapsedRealtimeMs, startTimeMs, onBattery, powerAccumulator, i, (long[]) obj);
            }
        });
        long elapsedTimeMs = this.mClock.uptimeMillis() - startTimeMs;
        if (elapsedTimeMs >= 100) {
            android.util.Slog.d(TAG, "Reading cpu cluster times took " + elapsedTimeMs + "ms");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$readKernelUidCpuClusterTimesLocked$6(long elapsedRealtimeMs, long startTimeMs, boolean onBattery, com.android.server.power.stats.BatteryStatsImpl.CpuDeltaPowerAccumulator powerAccumulator, int uid, long[] cpuClusterTimesMs) {
        int uid2 = mapUid(uid);
        if (android.os.Process.isIsolated(uid2) || !this.mUserInfoProvider.exists(android.os.UserHandle.getUserId(uid2))) {
            return;
        }
        com.android.server.power.stats.BatteryStatsImpl.Uid u = getUidStatsLocked(uid2, elapsedRealtimeMs, startTimeMs);
        u.mCpuClusterTimesMs.addCountLocked(cpuClusterTimesMs, onBattery);
        if (powerAccumulator != null) {
            powerAccumulator.addCpuClusterDurationsMs(u, cpuClusterTimesMs);
        }
    }

    boolean setChargingLocked(boolean charging) {
        this.mHandler.removeCallbacks(this.mDeferSetCharging);
        if (this.mCharging != charging) {
            this.mCharging = charging;
            this.mHistory.setChargingState(charging);
            this.mHandler.sendEmptyMessage(3);
            return true;
        }
        return false;
    }

    public void onSystemReady(android.content.Context context) {
        if (this.mCpuUidFreqTimeReader != null) {
            this.mCpuUidFreqTimeReader.onSystemReady();
        }
        this.mPowerStatsCollectorInjector.setContext(context);
        this.mCpuPowerStatsCollector.setEnabled(this.mPowerStatsCollectorEnabled.get(1));
        this.mCpuPowerStatsCollector.schedule();
        this.mMobileRadioPowerStatsCollector.setEnabled(this.mPowerStatsCollectorEnabled.get(8));
        this.mMobileRadioPowerStatsCollector.schedule();
        this.mWifiPowerStatsCollector.setEnabled(this.mPowerStatsCollectorEnabled.get(11));
        this.mWifiPowerStatsCollector.schedule();
        this.mBluetoothPowerStatsCollector.setEnabled(this.mPowerStatsCollectorEnabled.get(2));
        this.mBluetoothPowerStatsCollector.schedule();
        this.mCameraPowerStatsCollector.setEnabled(this.mPowerStatsCollectorEnabled.get(3));
        this.mCameraPowerStatsCollector.schedule();
        this.mGnssPowerStatsCollector.setEnabled(this.mPowerStatsCollectorEnabled.get(10));
        this.mGnssPowerStatsCollector.schedule();
        this.mSystemReady = true;
    }

    com.android.server.power.stats.PowerStatsCollector getPowerStatsCollector(int powerComponent) {
        switch (powerComponent) {
            case 1:
                return this.mCpuPowerStatsCollector;
            case 2:
                return this.mBluetoothPowerStatsCollector;
            case 3:
                return this.mCameraPowerStatsCollector;
            case 4:
            case 5:
            case 6:
            case 7:
            case 9:
            default:
                return null;
            case 8:
                return this.mMobileRadioPowerStatsCollector;
            case 10:
                return this.mGnssPowerStatsCollector;
            case 11:
                return this.mWifiPowerStatsCollector;
        }
    }

    public void forceRecordAllHistory() {
        this.mHistory.forceRecordAllHistory();
        this.mRecordAllHistory = true;
    }

    public void maybeResetWhilePluggedInLocked() {
        long elapsedRealtimeMs = this.mClock.elapsedRealtime();
        if (shouldResetWhilePluggedInLocked(elapsedRealtimeMs)) {
            android.util.Slog.i(TAG, "Resetting due to long plug in duration. elapsed time = " + elapsedRealtimeMs + " ms, last plug in time = " + this.mBatteryPluggedInRealTimeMs + " ms, last reset time = " + (this.mRealtimeStartUs / 1000));
            resetAllStatsAndHistoryLocked(5);
        }
        scheduleNextResetWhilePluggedInCheck();
    }

    private void scheduleNextResetWhilePluggedInCheck() {
        if (this.mAlarmManager == null) {
            return;
        }
        long timeoutMs = this.mClock.currentTimeMillis() + (((long) this.mConstants.RESET_WHILE_PLUGGED_IN_MINIMUM_DURATION_HOURS) * 3600000);
        java.util.Calendar nextAlarm = java.util.Calendar.getInstance();
        nextAlarm.setTimeInMillis(timeoutMs);
        nextAlarm.set(14, 0);
        nextAlarm.set(13, 0);
        nextAlarm.set(12, 0);
        nextAlarm.set(11, 2);
        long possibleNextTimeMs = nextAlarm.getTimeInMillis();
        if (possibleNextTimeMs < timeoutMs) {
            possibleNextTimeMs += 86400000;
        }
        final long nextTimeMs = possibleNextTimeMs;
        final android.app.AlarmManager am = this.mAlarmManager;
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.power.stats.BatteryStatsImpl$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$scheduleNextResetWhilePluggedInCheck$7(am, nextTimeMs);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleNextResetWhilePluggedInCheck$7(android.app.AlarmManager am, long nextTimeMs) {
        am.setWindow(1, nextTimeMs, 3600000L, TAG, this.mLongPlugInAlarmHandler, this.mHandler);
    }

    private boolean shouldResetWhilePluggedInLocked(long elapsedRealtimeMs) {
        if (this.mNoAutoReset || !this.mSystemReady || !this.mHistory.isResetEnabled()) {
            return false;
        }
        long pluggedInThresholdMs = this.mBatteryPluggedInRealTimeMs + (((long) this.mConstants.RESET_WHILE_PLUGGED_IN_MINIMUM_DURATION_HOURS) * 3600000);
        if (elapsedRealtimeMs >= pluggedInThresholdMs) {
            long resetThresholdMs = (this.mRealtimeStartUs / 1000) + (((long) this.mConstants.RESET_WHILE_PLUGGED_IN_MINIMUM_DURATION_HOURS) * 3600000);
            if (elapsedRealtimeMs >= resetThresholdMs) {
                return true;
            }
        }
        return false;
    }

    private boolean shouldResetOnUnplugLocked(int batteryStatus, int batteryLevel) {
        if (this.mNoAutoReset || !this.mSystemReady || !this.mHistory.isResetEnabled()) {
            return false;
        }
        if (!this.mBatteryStatsConfig.shouldResetOnUnplugHighBatteryLevel() || (batteryStatus != 5 && batteryLevel < 90)) {
            return (this.mBatteryStatsConfig.shouldResetOnUnplugAfterSignificantCharge() && this.mDischargePlugLevel < 20 && batteryLevel >= 80) || getHighDischargeAmountSinceCharge() >= 200;
        }
        return true;
    }

    /* JADX WARN: Type inference failed for: r10v2 */
    /* JADX WARN: Type inference failed for: r10v3, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r10v4 */
    protected void setOnBatteryLocked(long j, long j2, boolean z, int i, int i2, int i3) {
        ?? r10;
        android.os.Message messageObtainMessage = this.mHandler.obtainMessage(2);
        messageObtainMessage.arg1 = z ? 1 : 0;
        this.mHandler.sendMessage(messageObtainMessage);
        long j3 = j2 * 1000;
        long j4 = j * 1000;
        int i4 = this.mScreenState;
        if (!z) {
            this.mOnBatteryInternal = false;
            this.mOnBattery = false;
            pullPendingStateUpdatesLocked();
            this.mBatteryPluggedIn = true;
            this.mBatteryPluggedInRealTimeMs = j;
            this.mHistory.recordBatteryState(j, j2, i2, this.mBatteryPluggedIn);
            this.mDischargePlugLevel = i2;
            this.mDischargeCurrentLevel = i2;
            if (i2 < this.mDischargeUnplugLevel) {
                this.mLowDischargeAmountSinceCharge += (this.mDischargeUnplugLevel - i2) - 1;
                this.mHighDischargeAmountSinceCharge += this.mDischargeUnplugLevel - i2;
            }
            updateDischargeScreenLevelsLocked(i4, i4);
            updateTimeBasesLocked(false, i4, j3, j4);
            this.mChargeStepTracker.init();
            this.mLastChargeStepLevel = i2;
            this.mMaxChargeStepLevel = i2;
            this.mInitStepMode = this.mCurStepMode;
            this.mModStepMode = 0;
            scheduleNextResetWhilePluggedInCheck();
        } else {
            if (this.mCharging) {
                setChargingLocked(false);
            }
            this.mOnBatteryInternal = true;
            this.mOnBattery = true;
            this.mLastDischargeStepLevel = i2;
            this.mMinDischargeStepLevel = i2;
            this.mDischargeStepTracker.clearTime();
            this.mDailyDischargeStepTracker.clearTime();
            this.mInitStepMode = this.mCurStepMode;
            this.mModStepMode = 0;
            pullPendingStateUpdatesLocked();
            if (0 != 0) {
                r10 = 0;
                this.mHistory.startRecordingHistory(j, j2, false);
                initActiveHistoryEventsLocked(j, j2);
            } else {
                r10 = 0;
            }
            this.mBatteryPluggedIn = r10;
            if (this.mAlarmManager != null) {
                final android.app.AlarmManager alarmManager = this.mAlarmManager;
                this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.power.stats.BatteryStatsImpl$$ExternalSyntheticLambda9
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$setOnBatteryLocked$8(alarmManager);
                    }
                });
            }
            this.mHistory.recordBatteryState(j, j2, i2, this.mBatteryPluggedIn);
            this.mDischargeUnplugLevel = i2;
            this.mDischargeCurrentLevel = i2;
            if (android.view.Display.isOnState(i4)) {
                this.mDischargeScreenOnUnplugLevel = i2;
                this.mDischargeScreenDozeUnplugLevel = r10;
                this.mDischargeScreenOffUnplugLevel = r10;
            } else if (android.view.Display.isDozeState(i4)) {
                this.mDischargeScreenOnUnplugLevel = r10;
                this.mDischargeScreenDozeUnplugLevel = i2;
                this.mDischargeScreenOffUnplugLevel = r10;
            } else {
                this.mDischargeScreenOnUnplugLevel = r10;
                this.mDischargeScreenDozeUnplugLevel = r10;
                this.mDischargeScreenOffUnplugLevel = i2;
            }
            this.mDischargeAmountScreenOn = r10;
            this.mDischargeAmountScreenDoze = r10;
            this.mDischargeAmountScreenOff = r10;
            updateTimeBasesLocked(true, i4, j3, j4);
        }
        if ((0 != 0 || this.mLastWriteTimeMs + 60000 < j) && this.mStatsFile != null && !this.mHistory.isReadOnly()) {
            writeAsyncLocked();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setOnBatteryLocked$8(android.app.AlarmManager am) {
        am.cancel(this.mLongPlugInAlarmHandler);
    }

    private void scheduleSyncExternalStatsLocked(java.lang.String reason, int updateFlags) {
        if (this.mExternalSync != null) {
            this.mExternalSync.scheduleSync(reason, updateFlags);
        }
    }

    public void setBatteryStateLocked(int status, int health, int plugType, int level, int temp, int voltageMv, int chargeUah, int chargeFullUah, long chargeTimeToFullSeconds, long elapsedRealtimeMs, long uptimeMs, long currentTimeMs) {
        boolean onBattery;
        boolean z;
        int temp2 = java.lang.Math.max(0, temp);
        reportChangesToStatsLog(status, plugType, level);
        boolean onBattery2 = isOnBattery(plugType, status);
        if (!this.mHaveBatteryLevel) {
            this.mHaveBatteryLevel = true;
            if (onBattery2 == this.mOnBattery) {
                this.mHistory.setPluggedInState(!onBattery2);
            }
            this.mBatteryStatus = status;
            this.mBatteryLevel = level;
            this.mBatteryChargeUah = chargeUah;
            this.mHistory.setBatteryState(true, status, level, chargeUah);
            this.mLastDischargeStepLevel = level;
            this.mLastChargeStepLevel = level;
            this.mMinDischargeStepLevel = level;
            this.mMaxChargeStepLevel = level;
        } else if (this.mBatteryLevel != level || this.mOnBattery != onBattery2) {
            recordDailyStatsIfNeededLocked(level >= 100 && onBattery2, currentTimeMs);
        }
        int oldStatus = this.mBatteryStatus;
        if (onBattery2) {
            this.mDischargeCurrentLevel = level;
            if (!this.mHistory.isRecordingHistory()) {
                this.mHistory.startRecordingHistory(elapsedRealtimeMs, uptimeMs, true);
            }
        } else if (level < 96 && status != 1 && !this.mHistory.isRecordingHistory()) {
            this.mHistory.startRecordingHistory(elapsedRealtimeMs, uptimeMs, true);
        }
        if (this.mDischargePlugLevel < 0) {
            this.mDischargePlugLevel = level;
        }
        if (onBattery2 != this.mOnBattery) {
            this.mBatteryLevel = level;
            this.mBatteryStatus = status;
            this.mBatteryHealth = health;
            this.mBatteryPlugType = plugType;
            this.mBatteryTemperature = temp2;
            this.mBatteryVoltageMv = voltageMv;
            onBattery = onBattery2;
            this.mHistory.setBatteryState(status, level, health, plugType, temp2, voltageMv, chargeUah);
            if (chargeUah < this.mBatteryChargeUah) {
                long chargeDiff = ((long) this.mBatteryChargeUah) - ((long) chargeUah);
                this.mDischargeCounter.addCountLocked(chargeDiff);
                this.mDischargeScreenOffCounter.addCountLocked(chargeDiff);
                if (android.view.Display.isDozeState(this.mScreenState)) {
                    this.mDischargeScreenDozeCounter.addCountLocked(chargeDiff);
                }
                if (this.mDeviceIdleMode == 1) {
                    this.mDischargeLightDozeCounter.addCountLocked(chargeDiff);
                } else if (this.mDeviceIdleMode == 2) {
                    this.mDischargeDeepDozeCounter.addCountLocked(chargeDiff);
                }
            }
            this.mBatteryChargeUah = chargeUah;
            setOnBatteryLocked(elapsedRealtimeMs, uptimeMs, onBattery, oldStatus, level, chargeUah);
        } else {
            onBattery = onBattery2;
            boolean changed = false;
            if (this.mBatteryLevel != level) {
                this.mBatteryLevel = level;
                changed = true;
                this.mExternalSync.scheduleSyncDueToBatteryLevelChange(this.mConstants.BATTERY_LEVEL_COLLECTION_DELAY_MS);
            }
            if (this.mBatteryStatus != status) {
                this.mBatteryStatus = status;
                changed = true;
            }
            if (this.mBatteryHealth != health) {
                this.mBatteryHealth = health;
                changed = true;
            }
            if (this.mBatteryPlugType != plugType) {
                this.mBatteryPlugType = plugType;
                changed = true;
            }
            if (temp2 >= this.mBatteryTemperature + 10 || temp2 <= this.mBatteryTemperature - 10) {
                this.mBatteryTemperature = temp2;
                changed = true;
            }
            if (voltageMv > this.mBatteryVoltageMv + 20 || voltageMv < this.mBatteryVoltageMv - 20) {
                this.mBatteryVoltageMv = voltageMv;
                changed = true;
            }
            if (chargeUah >= this.mBatteryChargeUah + 10 || chargeUah <= this.mBatteryChargeUah - 10) {
                if (chargeUah >= this.mBatteryChargeUah) {
                    z = true;
                } else {
                    long chargeDiff2 = ((long) this.mBatteryChargeUah) - ((long) chargeUah);
                    this.mDischargeCounter.addCountLocked(chargeDiff2);
                    this.mDischargeScreenOffCounter.addCountLocked(chargeDiff2);
                    if (android.view.Display.isDozeState(this.mScreenState)) {
                        this.mDischargeScreenDozeCounter.addCountLocked(chargeDiff2);
                    }
                    z = true;
                    if (this.mDeviceIdleMode == 1) {
                        this.mDischargeLightDozeCounter.addCountLocked(chargeDiff2);
                    } else if (this.mDeviceIdleMode == 2) {
                        this.mDischargeDeepDozeCounter.addCountLocked(chargeDiff2);
                    }
                }
                this.mBatteryChargeUah = chargeUah;
                changed = true;
            } else {
                z = true;
            }
            long modeBits = (((long) this.mInitStepMode) << 48) | (((long) this.mModStepMode) << 56) | (((long) (level & 255)) << 40);
            if (onBattery) {
                changed |= setChargingLocked(false);
                if (this.mLastDischargeStepLevel != level && this.mMinDischargeStepLevel > level) {
                    this.mDischargeStepTracker.addLevelSteps(this.mLastDischargeStepLevel - level, modeBits, elapsedRealtimeMs);
                    this.mDailyDischargeStepTracker.addLevelSteps(this.mLastDischargeStepLevel - level, modeBits, elapsedRealtimeMs);
                    this.mLastDischargeStepLevel = level;
                    this.mMinDischargeStepLevel = level;
                    this.mInitStepMode = this.mCurStepMode;
                    this.mModStepMode = 0;
                }
            } else {
                if (level >= this.mConstants.BATTERY_CHARGING_ENFORCE_LEVEL) {
                    changed |= setChargingLocked(z);
                } else if (this.mCharging) {
                    if (this.mLastChargeStepLevel > level) {
                        changed |= setChargingLocked(false);
                    }
                } else if (this.mLastChargeStepLevel < level) {
                    if (!this.mHandler.hasCallbacks(this.mDeferSetCharging)) {
                        this.mHandler.postDelayed(this.mDeferSetCharging, this.mConstants.BATTERY_CHARGED_DELAY_MS);
                    }
                } else if (this.mLastChargeStepLevel > level) {
                    this.mHandler.removeCallbacks(this.mDeferSetCharging);
                }
                if (this.mLastChargeStepLevel != level && this.mMaxChargeStepLevel < level) {
                    this.mChargeStepTracker.addLevelSteps(level - this.mLastChargeStepLevel, modeBits, elapsedRealtimeMs);
                    this.mDailyChargeStepTracker.addLevelSteps(level - this.mLastChargeStepLevel, modeBits, elapsedRealtimeMs);
                    this.mMaxChargeStepLevel = level;
                    this.mInitStepMode = this.mCurStepMode;
                    this.mModStepMode = 0;
                }
                this.mLastChargeStepLevel = level;
            }
            if (changed) {
                this.mHistory.setBatteryState(this.mBatteryStatus, this.mBatteryLevel, this.mBatteryHealth, this.mBatteryPlugType, this.mBatteryTemperature, this.mBatteryVoltageMv, this.mBatteryChargeUah);
                this.mHistory.writeHistoryItem(elapsedRealtimeMs, uptimeMs);
            }
        }
        if (!onBattery && (status == 5 || status == 1)) {
            this.mHistory.setHistoryRecordingEnabled(false);
        }
        this.mLastLearnedBatteryCapacityUah = chargeFullUah;
        if (this.mMinLearnedBatteryCapacityUah != -1) {
            this.mMinLearnedBatteryCapacityUah = java.lang.Math.min(this.mMinLearnedBatteryCapacityUah, chargeFullUah);
        } else {
            this.mMinLearnedBatteryCapacityUah = chargeFullUah;
        }
        this.mMaxLearnedBatteryCapacityUah = java.lang.Math.max(this.mMaxLearnedBatteryCapacityUah, chargeFullUah);
        this.mBatteryTimeToFullSeconds = chargeTimeToFullSeconds;
    }

    public static boolean isOnBattery(int plugType, int status) {
        return plugType == 0 && status != 1;
    }

    private void reportChangesToStatsLog(int status, int plugType, int level) {
        if (!this.mHaveBatteryLevel || this.mBatteryStatus != status) {
            this.mFrameworkStatsLogger.chargingStateChanged(status);
        }
        if (!this.mHaveBatteryLevel || this.mBatteryPlugType != plugType) {
            this.mFrameworkStatsLogger.pluggedStateChanged(plugType);
        }
        if (!this.mHaveBatteryLevel || this.mBatteryLevel != level) {
            this.mFrameworkStatsLogger.batteryLevelChanged(level);
        }
    }

    public long getAwakeTimeBattery() {
        return getBatteryUptimeLocked(this.mClock.uptimeMillis());
    }

    public long getAwakeTimePlugged() {
        return (this.mClock.uptimeMillis() * 1000) - getAwakeTimeBattery();
    }

    public long computeUptime(long curTimeUs, int which) {
        return this.mUptimeUs + (curTimeUs - this.mUptimeStartUs);
    }

    public long computeRealtime(long curTimeUs, int which) {
        return this.mRealtimeUs + (curTimeUs - this.mRealtimeStartUs);
    }

    public long computeBatteryUptime(long curTimeUs, int which) {
        return this.mOnBatteryTimeBase.computeUptime(curTimeUs, which);
    }

    public long computeBatteryRealtime(long curTimeUs, int which) {
        return this.mOnBatteryTimeBase.computeRealtime(curTimeUs, which);
    }

    public long computeBatteryScreenOffUptime(long curTimeUs, int which) {
        return this.mOnBatteryScreenOffTimeBase.computeUptime(curTimeUs, which);
    }

    public long computeBatteryScreenOffRealtime(long curTimeUs, int which) {
        return this.mOnBatteryScreenOffTimeBase.computeRealtime(curTimeUs, which);
    }

    public long computeBatteryTimeRemaining(long curTime) {
        if (!this.mOnBattery || this.mDischargeStepTracker.mNumStepDurations < 1) {
            return -1L;
        }
        long msPerLevel = this.mDischargeStepTracker.computeTimePerLevel();
        if (msPerLevel <= 0) {
            return -1L;
        }
        return ((long) this.mBatteryLevel) * msPerLevel * 1000;
    }

    public android.os.BatteryStats.LevelStepTracker getDischargeLevelStepTracker() {
        return this.mDischargeStepTracker;
    }

    public android.os.BatteryStats.LevelStepTracker getDailyDischargeLevelStepTracker() {
        return this.mDailyDischargeStepTracker;
    }

    public long computeChargeTimeRemaining(long curTime) {
        if (this.mOnBattery) {
            return -1L;
        }
        if (this.mBatteryTimeToFullSeconds >= 0) {
            return this.mBatteryTimeToFullSeconds * 1000000;
        }
        if (this.mChargeStepTracker.mNumStepDurations < 1) {
            return -1L;
        }
        long msPerLevel = this.mChargeStepTracker.computeTimePerLevel();
        if (msPerLevel <= 0) {
            return -1L;
        }
        return ((long) (100 - this.mBatteryLevel)) * msPerLevel * 1000;
    }

    public android.os.connectivity.CellularBatteryStats getCellularBatteryStats() {
        long rawRealTimeUs = android.os.SystemClock.elapsedRealtime() * 1000;
        android.os.BatteryStats.ControllerActivityCounter counter = getModemControllerActivity();
        long sleepTimeMs = counter.getSleepTimeCounter().getCountLocked(0);
        long idleTimeMs = counter.getIdleTimeCounter().getCountLocked(0);
        long rxTimeMs = counter.getRxTimeCounter().getCountLocked(0);
        long energyConsumedMaMs = counter.getPowerCounter().getCountLocked(0);
        long monitoredRailChargeConsumedMaMs = counter.getMonitoredRailChargeConsumedMaMs().getCountLocked(0);
        long[] timeInRatMs = new long[android.os.BatteryStats.NUM_DATA_CONNECTION_TYPES];
        for (int i = 0; i < timeInRatMs.length; i++) {
            timeInRatMs[i] = getPhoneDataConnectionTime(i, rawRealTimeUs, 0) / 1000;
        }
        int i2 = CELL_SIGNAL_STRENGTH_LEVEL_COUNT;
        long[] timeInRxSignalStrengthLevelMs = new long[i2];
        for (int i3 = 0; i3 < timeInRxSignalStrengthLevelMs.length; i3++) {
            timeInRxSignalStrengthLevelMs[i3] = getPhoneSignalStrengthTime(i3, rawRealTimeUs, 0) / 1000;
        }
        int i4 = MODEM_TX_POWER_LEVEL_COUNT;
        long[] txTimeMs = new long[java.lang.Math.min(i4, counter.getTxTimeCounters().length)];
        long totalTxTimeMs = 0;
        for (int i5 = 0; i5 < txTimeMs.length; i5++) {
            txTimeMs[i5] = counter.getTxTimeCounters()[i5].getCountLocked(0);
            totalTxTimeMs += txTimeMs[i5];
        }
        return new android.os.connectivity.CellularBatteryStats(computeBatteryRealtime(rawRealTimeUs, 0) / 1000, getMobileRadioActiveTime(rawRealTimeUs, 0) / 1000, getNetworkActivityPackets(1, 0), getNetworkActivityBytes(1, 0), getNetworkActivityPackets(0, 0), getNetworkActivityBytes(0, 0), sleepTimeMs, idleTimeMs, rxTimeMs, java.lang.Long.valueOf(energyConsumedMaMs), timeInRatMs, timeInRxSignalStrengthLevelMs, txTimeMs, monitoredRailChargeConsumedMaMs);
    }

    public android.os.connectivity.WifiBatteryStats getWifiBatteryStats() {
        long rawRealTimeUs = android.os.SystemClock.elapsedRealtime() * 1000;
        android.os.BatteryStats.ControllerActivityCounter counter = getWifiControllerActivity();
        long idleTimeMs = counter.getIdleTimeCounter().getCountLocked(0);
        long scanTimeMs = counter.getScanTimeCounter().getCountLocked(0);
        long rxTimeMs = counter.getRxTimeCounter().getCountLocked(0);
        long txTimeMs = counter.getTxTimeCounters()[0].getCountLocked(0);
        long totalControllerActivityTimeMs = computeBatteryRealtime(android.os.SystemClock.elapsedRealtime() * 1000, 0) / 1000;
        long sleepTimeMs = totalControllerActivityTimeMs - ((idleTimeMs + rxTimeMs) + txTimeMs);
        long energyConsumedMaMs = counter.getPowerCounter().getCountLocked(0);
        long monitoredRailChargeConsumedMaMs = counter.getMonitoredRailChargeConsumedMaMs().getCountLocked(0);
        long numAppScanRequest = 0;
        for (int i = 0; i < this.mUidStats.size(); i++) {
            numAppScanRequest += (long) this.mUidStats.valueAt(i).mWifiScanTimer.getCountLocked(0);
        }
        long[] timeInStateMs = new long[8];
        for (int i2 = 0; i2 < 8; i2++) {
            timeInStateMs[i2] = getWifiStateTime(i2, rawRealTimeUs, 0) / 1000;
        }
        long[] timeInSupplStateMs = new long[13];
        for (int i3 = 0; i3 < 13; i3++) {
            timeInSupplStateMs[i3] = getWifiSupplStateTime(i3, rawRealTimeUs, 0) / 1000;
        }
        long[] timeSignalStrengthTimeMs = new long[5];
        for (int i4 = 0; i4 < 5; i4++) {
            timeSignalStrengthTimeMs[i4] = getWifiSignalStrengthTime(i4, rawRealTimeUs, 0) / 1000;
        }
        return new android.os.connectivity.WifiBatteryStats(computeBatteryRealtime(rawRealTimeUs, 0) / 1000, getWifiActiveTime(rawRealTimeUs, 0) / 1000, getNetworkActivityPackets(3, 0), getNetworkActivityBytes(3, 0), getNetworkActivityPackets(2, 0), getNetworkActivityBytes(2, 0), sleepTimeMs, scanTimeMs, idleTimeMs, rxTimeMs, txTimeMs, energyConsumedMaMs, numAppScanRequest, timeInStateMs, timeSignalStrengthTimeMs, timeInSupplStateMs, monitoredRailChargeConsumedMaMs);
    }

    public android.os.connectivity.GpsBatteryStats getGpsBatteryStats() {
        android.os.connectivity.GpsBatteryStats s = new android.os.connectivity.GpsBatteryStats();
        long rawRealTimeUs = android.os.SystemClock.elapsedRealtime() * 1000;
        s.setLoggingDurationMs(computeBatteryRealtime(rawRealTimeUs, 0) / 1000);
        s.setEnergyConsumedMaMs(getGpsBatteryDrainMaMs());
        long[] time = new long[this.mGpsSignalQualityTimer.length];
        for (int i = 0; i < time.length; i++) {
            time[i] = getGpsSignalQualityTime(i, rawRealTimeUs, 0) / 1000;
        }
        s.setTimeInGpsSignalQualityLevel(time);
        return s;
    }

    public android.os.BatteryStats.LevelStepTracker getChargeLevelStepTracker() {
        return this.mChargeStepTracker;
    }

    public android.os.BatteryStats.LevelStepTracker getDailyChargeLevelStepTracker() {
        return this.mDailyChargeStepTracker;
    }

    public java.util.ArrayList<android.os.BatteryStats.PackageChange> getDailyPackageChanges() {
        return this.mDailyPackageChanges;
    }

    protected long getBatteryUptimeLocked(long uptimeMs) {
        return this.mOnBatteryTimeBase.getUptime(1000 * uptimeMs);
    }

    public long getBatteryUptime(long curTimeUs) {
        return this.mOnBatteryTimeBase.getUptime(curTimeUs);
    }

    public long getBatteryRealtime(long curTimeUs) {
        return this.mOnBatteryTimeBase.getRealtime(curTimeUs);
    }

    public int getDischargeStartLevel() {
        int dischargeStartLevelLocked;
        synchronized (this) {
            dischargeStartLevelLocked = getDischargeStartLevelLocked();
        }
        return dischargeStartLevelLocked;
    }

    public int getDischargeStartLevelLocked() {
        return this.mDischargeUnplugLevel;
    }

    public int getDischargeCurrentLevel() {
        int dischargeCurrentLevelLocked;
        synchronized (this) {
            dischargeCurrentLevelLocked = getDischargeCurrentLevelLocked();
        }
        return dischargeCurrentLevelLocked;
    }

    public int getDischargeCurrentLevelLocked() {
        return this.mDischargeCurrentLevel;
    }

    public int getLowDischargeAmountSinceCharge() {
        int val;
        synchronized (this) {
            val = this.mLowDischargeAmountSinceCharge;
            if (this.mOnBattery && this.mDischargeCurrentLevel < this.mDischargeUnplugLevel) {
                val += (this.mDischargeUnplugLevel - this.mDischargeCurrentLevel) - 1;
            }
        }
        return val;
    }

    public int getHighDischargeAmountSinceCharge() {
        int val;
        synchronized (this) {
            val = this.mHighDischargeAmountSinceCharge;
            if (this.mOnBattery && this.mDischargeCurrentLevel < this.mDischargeUnplugLevel) {
                val += this.mDischargeUnplugLevel - this.mDischargeCurrentLevel;
            }
        }
        return val;
    }

    public int getDischargeAmount(int which) {
        int dischargeAmount;
        if (which == 0) {
            dischargeAmount = getHighDischargeAmountSinceCharge();
        } else {
            dischargeAmount = getDischargeStartLevel() - getDischargeCurrentLevel();
        }
        if (dischargeAmount < 0) {
            return 0;
        }
        return dischargeAmount;
    }

    public int getDischargeAmountScreenOn() {
        int val;
        synchronized (this) {
            val = this.mDischargeAmountScreenOn;
            if (this.mOnBattery && android.view.Display.isOnState(this.mScreenState) && this.mDischargeCurrentLevel < this.mDischargeScreenOnUnplugLevel) {
                val += this.mDischargeScreenOnUnplugLevel - this.mDischargeCurrentLevel;
            }
        }
        return val;
    }

    public int getDischargeAmountScreenOnSinceCharge() {
        int val;
        synchronized (this) {
            val = this.mDischargeAmountScreenOnSinceCharge;
            if (this.mOnBattery && android.view.Display.isOnState(this.mScreenState) && this.mDischargeCurrentLevel < this.mDischargeScreenOnUnplugLevel) {
                val += this.mDischargeScreenOnUnplugLevel - this.mDischargeCurrentLevel;
            }
        }
        return val;
    }

    public int getDischargeAmountScreenOff() {
        int dischargeAmountScreenDoze;
        synchronized (this) {
            int val = this.mDischargeAmountScreenOff;
            if (this.mOnBattery && android.view.Display.isOffState(this.mScreenState) && this.mDischargeCurrentLevel < this.mDischargeScreenOffUnplugLevel) {
                val += this.mDischargeScreenOffUnplugLevel - this.mDischargeCurrentLevel;
            }
            dischargeAmountScreenDoze = getDischargeAmountScreenDoze() + val;
        }
        return dischargeAmountScreenDoze;
    }

    public int getDischargeAmountScreenOffSinceCharge() {
        int dischargeAmountScreenDozeSinceCharge;
        synchronized (this) {
            int val = this.mDischargeAmountScreenOffSinceCharge;
            if (this.mOnBattery && android.view.Display.isOffState(this.mScreenState) && this.mDischargeCurrentLevel < this.mDischargeScreenOffUnplugLevel) {
                val += this.mDischargeScreenOffUnplugLevel - this.mDischargeCurrentLevel;
            }
            dischargeAmountScreenDozeSinceCharge = getDischargeAmountScreenDozeSinceCharge() + val;
        }
        return dischargeAmountScreenDozeSinceCharge;
    }

    public int getDischargeAmountScreenDoze() {
        int val;
        synchronized (this) {
            val = this.mDischargeAmountScreenDoze;
            if (this.mOnBattery && android.view.Display.isDozeState(this.mScreenState) && this.mDischargeCurrentLevel < this.mDischargeScreenDozeUnplugLevel) {
                val += this.mDischargeScreenDozeUnplugLevel - this.mDischargeCurrentLevel;
            }
        }
        return val;
    }

    public int getDischargeAmountScreenDozeSinceCharge() {
        int val;
        synchronized (this) {
            val = this.mDischargeAmountScreenDozeSinceCharge;
            if (this.mOnBattery && android.view.Display.isDozeState(this.mScreenState) && this.mDischargeCurrentLevel < this.mDischargeScreenDozeUnplugLevel) {
                val += this.mDischargeScreenDozeUnplugLevel - this.mDischargeCurrentLevel;
            }
        }
        return val;
    }

    public long[] getSystemServiceTimeAtCpuSpeeds() {
        if (this.mBinderThreadCpuTimesUs == null) {
            return null;
        }
        return this.mBinderThreadCpuTimesUs.getCountsLocked(0);
    }

    public com.android.server.power.stats.BatteryStatsImpl.Uid getUidStatsLocked(int uid) {
        return getUidStatsLocked(uid, this.mClock.elapsedRealtime(), this.mClock.uptimeMillis());
    }

    public com.android.server.power.stats.BatteryStatsImpl.Uid getUidStatsLocked(int uid, long elapsedRealtimeMs, long uptimeMs) {
        com.android.server.power.stats.BatteryStatsImpl.Uid u = this.mUidStats.get(uid);
        if (u == null) {
            if (android.os.Process.isSdkSandboxUid(uid)) {
                android.util.Log.wtf(TAG, "Tracking an SDK Sandbox UID");
            }
            com.android.server.power.stats.BatteryStatsImpl.Uid u2 = new com.android.server.power.stats.BatteryStatsImpl.Uid(this, uid, elapsedRealtimeMs, uptimeMs);
            this.mUidStats.put(uid, u2);
            return u2;
        }
        return u;
    }

    public com.android.server.power.stats.BatteryStatsImpl.Uid getAvailableUidStatsLocked(int uid) {
        com.android.server.power.stats.BatteryStatsImpl.Uid u = this.mUidStats.get(uid);
        return u;
    }

    public void onCleanupUserLocked(int userId, long elapsedRealtimeMs) {
        int firstUidForUser = android.os.UserHandle.getUid(userId, 0);
        int lastUidForUser = android.os.UserHandle.getUid(userId, 99999);
        this.mPendingRemovedUids.add(new com.android.server.power.stats.BatteryStatsImpl.UidToRemove(firstUidForUser, lastUidForUser, elapsedRealtimeMs));
    }

    public void onUserRemovedLocked(int userId) {
        if (this.mExternalSync != null) {
            this.mExternalSync.scheduleCleanupDueToRemovedUser(userId);
        }
    }

    public void clearRemovedUserUidsLocked(int userId) {
        int firstUidForUser = android.os.UserHandle.getUid(userId, 0);
        int lastUidForUser = android.os.UserHandle.getUid(userId, 99999);
        this.mUidStats.put(firstUidForUser, null);
        this.mUidStats.put(lastUidForUser, null);
        int firstIndex = this.mUidStats.indexOfKey(firstUidForUser);
        int lastIndex = this.mUidStats.indexOfKey(lastUidForUser);
        for (int i = firstIndex; i <= lastIndex; i++) {
            com.android.server.power.stats.BatteryStatsImpl.Uid uid = this.mUidStats.valueAt(i);
            if (uid != null) {
                uid.detachFromTimeBase();
            }
        }
        this.mUidStats.removeAtRange(firstIndex, (lastIndex - firstIndex) + 1);
        removeCpuStatsForUidRangeLocked(firstUidForUser, lastUidForUser);
    }

    public void removeUidStatsLocked(int uid, long elapsedRealtimeMs) {
        com.android.server.power.stats.BatteryStatsImpl.Uid u = this.mUidStats.get(uid);
        if (u != null) {
            u.detachFromTimeBase();
        }
        this.mUidStats.remove(uid);
        this.mPendingRemovedUids.add(new com.android.server.power.stats.BatteryStatsImpl.UidToRemove(this, uid, elapsedRealtimeMs));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeCpuStatsForUidRangeLocked(int startUid, int endUid) {
        if (startUid == endUid) {
            this.mCpuUidUserSysTimeReader.removeUid(startUid);
            this.mCpuUidFreqTimeReader.removeUid(startUid);
            if (this.mConstants.TRACK_CPU_ACTIVE_CLUSTER_TIME) {
                this.mCpuUidActiveTimeReader.removeUid(startUid);
                this.mCpuUidClusterTimeReader.removeUid(startUid);
            }
            if (this.mKernelSingleUidTimeReader != null) {
                this.mKernelSingleUidTimeReader.removeUid(startUid);
            }
            this.mNumUidsRemoved++;
            return;
        }
        if (startUid < endUid) {
            this.mCpuUidFreqTimeReader.removeUidsInRange(startUid, endUid);
            this.mCpuUidUserSysTimeReader.removeUidsInRange(startUid, endUid);
            if (this.mConstants.TRACK_CPU_ACTIVE_CLUSTER_TIME) {
                this.mCpuUidActiveTimeReader.removeUidsInRange(startUid, endUid);
                this.mCpuUidClusterTimeReader.removeUidsInRange(startUid, endUid);
            }
            if (this.mKernelSingleUidTimeReader != null) {
                this.mKernelSingleUidTimeReader.removeUidsInRange(startUid, endUid);
            }
            this.mPowerStatsUidResolver.releaseUidsInRange(startUid, endUid);
            this.mNumUidsRemoved++;
            return;
        }
        android.util.Slog.w(TAG, "End UID " + endUid + " is smaller than start UID " + startUid);
    }

    public com.android.server.power.stats.BatteryStatsImpl.Uid.Proc getProcessStatsLocked(int uid, java.lang.String name, long elapsedRealtimeMs, long uptimeMs) {
        com.android.server.power.stats.BatteryStatsImpl.Uid u = getUidStatsLocked(mapUid(uid), elapsedRealtimeMs, uptimeMs);
        return u.getProcessStatsLocked(name);
    }

    public com.android.server.power.stats.BatteryStatsImpl.Uid.Pkg getPackageStatsLocked(int uid, java.lang.String pkg) {
        return getPackageStatsLocked(uid, pkg, this.mClock.elapsedRealtime(), this.mClock.uptimeMillis());
    }

    public com.android.server.power.stats.BatteryStatsImpl.Uid.Pkg getPackageStatsLocked(int uid, java.lang.String pkg, long elapsedRealtimeMs, long uptimeMs) {
        com.android.server.power.stats.BatteryStatsImpl.Uid u = getUidStatsLocked(mapUid(uid), elapsedRealtimeMs, uptimeMs);
        return u.getPackageStatsLocked(pkg);
    }

    public com.android.server.power.stats.BatteryStatsImpl.Uid.Pkg.Serv getServiceStatsLocked(int uid, java.lang.String pkg, java.lang.String name, long elapsedRealtimeMs, long uptimeMs) {
        com.android.server.power.stats.BatteryStatsImpl.Uid u = getUidStatsLocked(mapUid(uid), elapsedRealtimeMs, uptimeMs);
        return u.getServiceStatsLocked(pkg, name);
    }

    public void shutdownLocked() {
        this.mHistory.recordShutdownEvent(this.mClock.elapsedRealtime(), this.mClock.uptimeMillis(), this.mClock.currentTimeMillis());
        writeSyncLocked();
        this.mShuttingDown = true;
    }

    public boolean isProcessStateDataAvailable() {
        boolean zTrackPerProcStateCpuTimes;
        synchronized (this) {
            zTrackPerProcStateCpuTimes = trackPerProcStateCpuTimes();
        }
        return zTrackPerProcStateCpuTimes;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean trackPerProcStateCpuTimes() {
        return this.mCpuUidFreqTimeReader.isFastCpuTimesReader();
    }

    public void setPowerStatsCollectorEnabled(int powerComponent, boolean enabled) {
        synchronized (this) {
            this.mPowerStatsCollectorEnabled.put(powerComponent, enabled);
        }
    }

    public void systemServicesReady(android.content.Context context) {
        this.mConstants.startObserving(context.getContentResolver());
        registerUsbStateReceiver(context);
        synchronized (this) {
            this.mAlarmManager = (android.app.AlarmManager) context.getSystemService(android.app.AlarmManager.class);
            if (this.mBatteryPluggedIn) {
                scheduleNextResetWhilePluggedInCheck();
            }
        }
        this.mBatteryStatsImplExt.onSystemServicesReady(context);
    }

    public void initEnergyConsumerStatsLocked(boolean[] supportedStandardBuckets, java.lang.String[] customBucketNames) {
        int numDisplays = this.mPerDisplayBatteryStats.length;
        for (int i = 0; i < numDisplays; i++) {
            int screenState = this.mPerDisplayBatteryStats[i].screenState;
            this.mPerDisplayBatteryStats[i].screenStateAtLastEnergyMeasurement = screenState;
        }
        if (supportedStandardBuckets != null) {
            com.android.internal.power.EnergyConsumerStats.Config config = new com.android.internal.power.EnergyConsumerStats.Config(supportedStandardBuckets, customBucketNames, SUPPORTED_PER_PROCESS_STATE_STANDARD_ENERGY_BUCKETS, getBatteryConsumerProcessStateNames());
            if (this.mEnergyConsumerStatsConfig != null && !this.mEnergyConsumerStatsConfig.isCompatible(config)) {
                resetAllStatsLocked(android.os.SystemClock.uptimeMillis(), android.os.SystemClock.elapsedRealtime(), 4);
            }
            this.mEnergyConsumerStatsConfig = config;
            this.mGlobalEnergyConsumerStats = new com.android.internal.power.EnergyConsumerStats(config);
            if (supportedStandardBuckets[5]) {
                this.mBluetoothPowerCalculator = new com.android.server.power.stats.BluetoothPowerCalculator(this.mPowerProfile);
            }
            if (supportedStandardBuckets[7]) {
                this.mMobileRadioPowerCalculator = new com.android.server.power.stats.MobileRadioPowerCalculator(this.mPowerProfile);
            }
            if (supportedStandardBuckets[4]) {
                this.mWifiPowerCalculator = new com.android.server.power.stats.WifiPowerCalculator(this.mPowerProfile);
                return;
            }
            return;
        }
        if (this.mEnergyConsumerStatsConfig != null) {
            resetAllStatsLocked(android.os.SystemClock.uptimeMillis(), android.os.SystemClock.elapsedRealtime(), 4);
        }
        this.mEnergyConsumerStatsConfig = null;
        this.mGlobalEnergyConsumerStats = null;
    }

    private boolean isMobileRadioEnergyConsumerSupportedLocked() {
        if (this.mGlobalEnergyConsumerStats == null) {
            return false;
        }
        return this.mGlobalEnergyConsumerStats.isStandardBucketSupported(7);
    }

    private static java.lang.String[] getBatteryConsumerProcessStateNames() {
        java.lang.String[] procStateNames = new java.lang.String[5];
        for (int procState = 0; procState < 5; procState++) {
            procStateNames[procState] = android.os.BatteryConsumer.processStateToString(procState);
        }
        return procStateNames;
    }

    public int getBatteryVoltageMvLocked() {
        return this.mBatteryVoltageMv;
    }

    public final class Constants extends android.database.ContentObserver {
        private static final int DEFAULT_BATTERY_CHARGED_DELAY_MS = 900000;
        private static final int DEFAULT_BATTERY_CHARGING_ENFORCE_LEVEL = 90;
        private static final long DEFAULT_BATTERY_LEVEL_COLLECTION_DELAY_MS = 300000;
        private static final long DEFAULT_EXTERNAL_STATS_COLLECTION_RATE_LIMIT_MS = 600000;
        private static final long DEFAULT_KERNEL_UID_READERS_THROTTLE_TIME = 1000;
        private static final int DEFAULT_MAX_HISTORY_BUFFER_KB = 128;
        private static final int DEFAULT_MAX_HISTORY_BUFFER_LOW_RAM_DEVICE_KB = 64;
        private static final int DEFAULT_MAX_HISTORY_FILES = 32;
        private static final int DEFAULT_MAX_HISTORY_FILES_LOW_RAM_DEVICE = 64;
        private static final int DEFAULT_PER_UID_MODEM_MODEL = 2;
        private static final boolean DEFAULT_PHONE_ON_EXTERNAL_STATS_COLLECTION = true;
        private static final long DEFAULT_PROC_STATE_CHANGE_COLLECTION_DELAY_MS = 60000;
        private static final int DEFAULT_RESET_WHILE_PLUGGED_IN_MINIMUM_DURATION_HOURS = 47;
        private static final boolean DEFAULT_TRACK_CPU_ACTIVE_CLUSTER_TIME = true;
        private static final long DEFAULT_UID_REMOVE_DELAY_MS = 300000;
        public static final java.lang.String KEY_BATTERY_CHARGED_DELAY_MS = "battery_charged_delay_ms";
        public static final java.lang.String KEY_BATTERY_CHARGING_ENFORCE_LEVEL = "battery_charging_enforce_level";
        public static final java.lang.String KEY_BATTERY_LEVEL_COLLECTION_DELAY_MS = "battery_level_collection_delay_ms";
        public static final java.lang.String KEY_EXTERNAL_STATS_COLLECTION_RATE_LIMIT_MS = "external_stats_collection_rate_limit_ms";
        public static final java.lang.String KEY_KERNEL_UID_READERS_THROTTLE_TIME = "kernel_uid_readers_throttle_time";
        public static final java.lang.String KEY_MAX_HISTORY_BUFFER_KB = "max_history_buffer_kb";
        public static final java.lang.String KEY_MAX_HISTORY_FILES = "max_history_files";
        public static final java.lang.String KEY_PER_UID_MODEM_POWER_MODEL = "per_uid_modem_power_model";
        public static final java.lang.String KEY_PHONE_ON_EXTERNAL_STATS_COLLECTION = "phone_on_external_stats_collection";
        public static final java.lang.String KEY_PROC_STATE_CHANGE_COLLECTION_DELAY_MS = "procstate_change_collection_delay_ms";
        public static final java.lang.String KEY_RESET_WHILE_PLUGGED_IN_MINIMUM_DURATION_HOURS = "reset_while_plugged_in_minimum_duration_hours";
        public static final java.lang.String KEY_TRACK_CPU_ACTIVE_CLUSTER_TIME = "track_cpu_active_cluster_time";
        public static final java.lang.String KEY_UID_REMOVE_DELAY_MS = "uid_remove_delay_ms";
        public static final java.lang.String PER_UID_MODEM_POWER_MODEL_MOBILE_RADIO_ACTIVE_TIME_NAME = "mobile_radio_active_time";
        public static final java.lang.String PER_UID_MODEM_POWER_MODEL_MODEM_ACTIVITY_INFO_RX_TX_NAME = "modem_activity_info_rx_tx";
        public int BATTERY_CHARGED_DELAY_MS;
        public int BATTERY_CHARGING_ENFORCE_LEVEL;
        public long BATTERY_LEVEL_COLLECTION_DELAY_MS;
        public long EXTERNAL_STATS_COLLECTION_RATE_LIMIT_MS;
        public long KERNEL_UID_READERS_THROTTLE_TIME;
        public int MAX_HISTORY_BUFFER;
        public int MAX_HISTORY_FILES;
        public int PER_UID_MODEM_MODEL;
        public boolean PHONE_ON_EXTERNAL_STATS_COLLECTION;
        public long PROC_STATE_CHANGE_COLLECTION_DELAY_MS;
        public int RESET_WHILE_PLUGGED_IN_MINIMUM_DURATION_HOURS;
        public boolean TRACK_CPU_ACTIVE_CLUSTER_TIME;
        public long UID_REMOVE_DELAY_MS;
        private final android.util.KeyValueListParser mParser;
        private android.content.ContentResolver mResolver;

        public java.lang.String getPerUidModemModelName(int model) {
            switch (model) {
                case 1:
                    return PER_UID_MODEM_POWER_MODEL_MOBILE_RADIO_ACTIVE_TIME_NAME;
                case 2:
                    return PER_UID_MODEM_POWER_MODEL_MODEM_ACTIVITY_INFO_RX_TX_NAME;
                default:
                    android.util.Slog.w(com.android.server.power.stats.BatteryStatsImpl.TAG, "Unexpected per uid modem model (" + model + ")");
                    return "unknown_" + model;
            }
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Removed duplicated region for block: B:11:0x001f  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public int getPerUidModemModel(java.lang.String r4) {
            /*
                r3 = this;
                int r0 = r4.hashCode()
                r1 = 1
                switch(r0) {
                    case -615381273: goto L14;
                    case 426026949: goto L9;
                    default: goto L8;
                }
            L8:
                goto L1f
            L9:
                java.lang.String r0 = "mobile_radio_active_time"
                boolean r0 = r4.equals(r0)
                if (r0 == 0) goto L8
                r0 = 0
                goto L20
            L14:
                java.lang.String r0 = "modem_activity_info_rx_tx"
                boolean r0 = r4.equals(r0)
                if (r0 == 0) goto L8
                r0 = r1
                goto L20
            L1f:
                r0 = -1
            L20:
                r2 = 2
                switch(r0) {
                    case 0: goto L44;
                    case 1: goto L43;
                    default: goto L24;
                }
            L24:
                java.lang.StringBuilder r0 = new java.lang.StringBuilder
                r0.<init>()
                java.lang.String r1 = "Unexpected per uid modem model name ("
                java.lang.StringBuilder r0 = r0.append(r1)
                java.lang.StringBuilder r0 = r0.append(r4)
                java.lang.String r1 = ")"
                java.lang.StringBuilder r0 = r0.append(r1)
                java.lang.String r0 = r0.toString()
                java.lang.String r1 = "BatteryStatsImpl"
                android.util.Slog.w(r1, r0)
                return r2
            L43:
                return r2
            L44:
                return r1
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.power.stats.BatteryStatsImpl.Constants.getPerUidModemModel(java.lang.String):int");
        }

        public Constants(android.os.Handler handler) {
            super(handler);
            this.TRACK_CPU_ACTIVE_CLUSTER_TIME = true;
            this.UID_REMOVE_DELAY_MS = 300000L;
            this.EXTERNAL_STATS_COLLECTION_RATE_LIMIT_MS = 600000L;
            this.BATTERY_LEVEL_COLLECTION_DELAY_MS = 300000L;
            this.PROC_STATE_CHANGE_COLLECTION_DELAY_MS = 60000L;
            this.BATTERY_CHARGED_DELAY_MS = DEFAULT_BATTERY_CHARGED_DELAY_MS;
            this.BATTERY_CHARGING_ENFORCE_LEVEL = 90;
            this.PER_UID_MODEM_MODEL = 2;
            this.PHONE_ON_EXTERNAL_STATS_COLLECTION = true;
            this.RESET_WHILE_PLUGGED_IN_MINIMUM_DURATION_HOURS = 47;
            this.mParser = new android.util.KeyValueListParser(',');
            if (com.android.server.power.stats.BatteryStatsImpl.isLowRamDevice()) {
                this.MAX_HISTORY_FILES = 64;
                this.MAX_HISTORY_BUFFER = 65536;
            } else {
                this.MAX_HISTORY_FILES = 32;
                this.MAX_HISTORY_BUFFER = 131072;
            }
        }

        public void startObserving(android.content.ContentResolver resolver) {
            this.mResolver = resolver;
            this.mResolver.registerContentObserver(android.provider.Settings.Global.getUriFor("battery_stats_constants"), false, this);
            this.mResolver.registerContentObserver(android.provider.Settings.Global.getUriFor("battery_charging_state_update_delay"), false, this);
            this.mResolver.registerContentObserver(android.provider.Settings.Global.getUriFor("battery_charging_state_enforce_level"), false, this);
            updateConstants();
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange, android.net.Uri uri) {
            if (uri.equals(android.provider.Settings.Global.getUriFor("battery_charging_state_update_delay"))) {
                synchronized (com.android.server.power.stats.BatteryStatsImpl.this) {
                    updateBatteryChargedDelayMsLocked();
                }
            } else {
                if (uri.equals(android.provider.Settings.Global.getUriFor("battery_charging_state_enforce_level"))) {
                    synchronized (com.android.server.power.stats.BatteryStatsImpl.this) {
                        updateBatteryChargingEnforceLevelLocked();
                    }
                    return;
                }
                updateConstants();
            }
        }

        private void updateConstants() {
            synchronized (com.android.server.power.stats.BatteryStatsImpl.this) {
                try {
                    this.mParser.setString(android.provider.Settings.Global.getString(this.mResolver, "battery_stats_constants"));
                } catch (java.lang.IllegalArgumentException e) {
                    android.util.Slog.e(com.android.server.power.stats.BatteryStatsImpl.TAG, "Bad batterystats settings", e);
                }
                this.TRACK_CPU_ACTIVE_CLUSTER_TIME = this.mParser.getBoolean(KEY_TRACK_CPU_ACTIVE_CLUSTER_TIME, true);
                updateKernelUidReadersThrottleTime(this.KERNEL_UID_READERS_THROTTLE_TIME, this.mParser.getLong(KEY_KERNEL_UID_READERS_THROTTLE_TIME, 1000L));
                updateUidRemoveDelay(this.mParser.getLong(KEY_UID_REMOVE_DELAY_MS, 300000L));
                this.EXTERNAL_STATS_COLLECTION_RATE_LIMIT_MS = this.mParser.getLong(KEY_EXTERNAL_STATS_COLLECTION_RATE_LIMIT_MS, 600000L);
                this.BATTERY_LEVEL_COLLECTION_DELAY_MS = this.mParser.getLong(KEY_BATTERY_LEVEL_COLLECTION_DELAY_MS, 300000L);
                this.PROC_STATE_CHANGE_COLLECTION_DELAY_MS = this.mParser.getLong(KEY_PROC_STATE_CHANGE_COLLECTION_DELAY_MS, 60000L);
                int i = 64;
                this.MAX_HISTORY_FILES = this.mParser.getInt(KEY_MAX_HISTORY_FILES, com.android.server.power.stats.BatteryStatsImpl.isLowRamDevice() ? 64 : 32);
                android.util.KeyValueListParser keyValueListParser = this.mParser;
                if (!com.android.server.power.stats.BatteryStatsImpl.isLowRamDevice()) {
                    i = 128;
                }
                this.MAX_HISTORY_BUFFER = keyValueListParser.getInt(KEY_MAX_HISTORY_BUFFER_KB, i) * 1024;
                java.lang.String perUidModemModel = this.mParser.getString(KEY_PER_UID_MODEM_POWER_MODEL, "");
                this.PER_UID_MODEM_MODEL = getPerUidModemModel(perUidModemModel);
                this.PHONE_ON_EXTERNAL_STATS_COLLECTION = this.mParser.getBoolean(KEY_PHONE_ON_EXTERNAL_STATS_COLLECTION, true);
                this.RESET_WHILE_PLUGGED_IN_MINIMUM_DURATION_HOURS = this.mParser.getInt(KEY_RESET_WHILE_PLUGGED_IN_MINIMUM_DURATION_HOURS, 47);
                updateBatteryChargedDelayMsLocked();
                updateBatteryChargingEnforceLevelLocked();
                onChange();
            }
        }

        public void onChange() {
            com.android.server.power.stats.BatteryStatsImpl.this.mHistory.setMaxHistoryFiles(this.MAX_HISTORY_FILES);
            com.android.server.power.stats.BatteryStatsImpl.this.mHistory.setMaxHistoryBufferSize(this.MAX_HISTORY_BUFFER);
        }

        private void updateBatteryChargedDelayMsLocked() {
            int delay = android.provider.Settings.Global.getInt(this.mResolver, "battery_charging_state_update_delay", -1);
            this.BATTERY_CHARGED_DELAY_MS = delay >= 0 ? delay : this.mParser.getInt(KEY_BATTERY_CHARGED_DELAY_MS, DEFAULT_BATTERY_CHARGED_DELAY_MS);
            if (com.android.server.power.stats.BatteryStatsImpl.this.mHandler.hasCallbacks(com.android.server.power.stats.BatteryStatsImpl.this.mDeferSetCharging)) {
                com.android.server.power.stats.BatteryStatsImpl.this.mHandler.removeCallbacks(com.android.server.power.stats.BatteryStatsImpl.this.mDeferSetCharging);
                com.android.server.power.stats.BatteryStatsImpl.this.mHandler.postDelayed(com.android.server.power.stats.BatteryStatsImpl.this.mDeferSetCharging, this.BATTERY_CHARGED_DELAY_MS);
            }
        }

        private void updateBatteryChargingEnforceLevelLocked() {
            int lastChargingEnforceLevel = this.BATTERY_CHARGING_ENFORCE_LEVEL;
            int level = android.provider.Settings.Global.getInt(this.mResolver, "battery_charging_state_enforce_level", -1);
            this.BATTERY_CHARGING_ENFORCE_LEVEL = level >= 0 ? level : this.mParser.getInt(KEY_BATTERY_CHARGING_ENFORCE_LEVEL, 90);
            if (this.BATTERY_CHARGING_ENFORCE_LEVEL <= com.android.server.power.stats.BatteryStatsImpl.this.mLastChargeStepLevel && com.android.server.power.stats.BatteryStatsImpl.this.mLastChargeStepLevel < lastChargingEnforceLevel) {
                com.android.server.power.stats.BatteryStatsImpl.this.setChargingLocked(true);
            }
        }

        private void updateKernelUidReadersThrottleTime(long oldTimeMs, long newTimeMs) {
            this.KERNEL_UID_READERS_THROTTLE_TIME = newTimeMs;
            if (oldTimeMs != newTimeMs) {
                com.android.server.power.stats.BatteryStatsImpl.this.mCpuUidUserSysTimeReader.setThrottle(this.KERNEL_UID_READERS_THROTTLE_TIME);
                com.android.server.power.stats.BatteryStatsImpl.this.mCpuUidFreqTimeReader.setThrottle(this.KERNEL_UID_READERS_THROTTLE_TIME);
                com.android.server.power.stats.BatteryStatsImpl.this.mCpuUidActiveTimeReader.setThrottle(this.KERNEL_UID_READERS_THROTTLE_TIME);
                com.android.server.power.stats.BatteryStatsImpl.this.mCpuUidClusterTimeReader.setThrottle(this.KERNEL_UID_READERS_THROTTLE_TIME);
            }
        }

        private void updateUidRemoveDelay(long newTimeMs) {
            this.UID_REMOVE_DELAY_MS = newTimeMs;
            com.android.server.power.stats.BatteryStatsImpl.this.clearPendingRemovedUidsLocked();
        }

        public void dumpLocked(java.io.PrintWriter pw) {
            pw.print(KEY_TRACK_CPU_ACTIVE_CLUSTER_TIME);
            pw.print("=");
            pw.println(this.TRACK_CPU_ACTIVE_CLUSTER_TIME);
            pw.print(KEY_KERNEL_UID_READERS_THROTTLE_TIME);
            pw.print("=");
            pw.println(this.KERNEL_UID_READERS_THROTTLE_TIME);
            pw.print(KEY_EXTERNAL_STATS_COLLECTION_RATE_LIMIT_MS);
            pw.print("=");
            pw.println(this.EXTERNAL_STATS_COLLECTION_RATE_LIMIT_MS);
            pw.print(KEY_BATTERY_LEVEL_COLLECTION_DELAY_MS);
            pw.print("=");
            pw.println(this.BATTERY_LEVEL_COLLECTION_DELAY_MS);
            pw.print(KEY_PROC_STATE_CHANGE_COLLECTION_DELAY_MS);
            pw.print("=");
            pw.println(this.PROC_STATE_CHANGE_COLLECTION_DELAY_MS);
            pw.print(KEY_MAX_HISTORY_FILES);
            pw.print("=");
            pw.println(this.MAX_HISTORY_FILES);
            pw.print(KEY_MAX_HISTORY_BUFFER_KB);
            pw.print("=");
            pw.println(this.MAX_HISTORY_BUFFER / 1024);
            pw.print(KEY_BATTERY_CHARGED_DELAY_MS);
            pw.print("=");
            pw.println(this.BATTERY_CHARGED_DELAY_MS);
            pw.print(KEY_BATTERY_CHARGING_ENFORCE_LEVEL);
            pw.print("=");
            pw.println(this.BATTERY_CHARGING_ENFORCE_LEVEL);
            pw.print(KEY_PER_UID_MODEM_POWER_MODEL);
            pw.print("=");
            pw.println(getPerUidModemModelName(this.PER_UID_MODEM_MODEL));
            pw.print(KEY_PHONE_ON_EXTERNAL_STATS_COLLECTION);
            pw.print("=");
            pw.println(this.PHONE_ON_EXTERNAL_STATS_COLLECTION);
            pw.print(KEY_RESET_WHILE_PLUGGED_IN_MINIMUM_DURATION_HOURS);
            pw.print("=");
            pw.println(this.RESET_WHILE_PLUGGED_IN_MINIMUM_DURATION_HOURS);
        }
    }

    public long getExternalStatsCollectionRateLimitMs() {
        long j;
        synchronized (this) {
            j = this.mConstants.EXTERNAL_STATS_COLLECTION_RATE_LIMIT_MS;
        }
        return j;
    }

    public void dumpConstantsLocked(java.io.PrintWriter pw) {
        java.io.PrintWriter indentingPrintWriter = new android.util.IndentingPrintWriter(pw, "    ");
        indentingPrintWriter.println("BatteryStats constants:");
        indentingPrintWriter.increaseIndent();
        this.mConstants.dumpLocked(indentingPrintWriter);
        indentingPrintWriter.decreaseIndent();
    }

    public void dumpCpuStatsLocked(java.io.PrintWriter pw) {
        int size = this.mUidStats.size();
        pw.println("Per UID CPU user & system time in ms:");
        for (int i = 0; i < size; i++) {
            int u = this.mUidStats.keyAt(i);
            com.android.server.power.stats.BatteryStatsImpl.Uid uid = this.mUidStats.get(u);
            pw.print("  ");
            pw.print(u);
            pw.print(": ");
            pw.print(uid.getUserCpuTimeUs(0) / 1000);
            pw.print(" ");
            pw.println(uid.getSystemCpuTimeUs(0) / 1000);
        }
        pw.println("Per UID CPU active time in ms:");
        for (int i2 = 0; i2 < size; i2++) {
            int u2 = this.mUidStats.keyAt(i2);
            com.android.server.power.stats.BatteryStatsImpl.Uid uid2 = this.mUidStats.get(u2);
            if (uid2.getCpuActiveTime() > 0) {
                pw.print("  ");
                pw.print(u2);
                pw.print(": ");
                pw.println(uid2.getCpuActiveTime());
            }
        }
        pw.println("Per UID CPU cluster time in ms:");
        for (int i3 = 0; i3 < size; i3++) {
            int u3 = this.mUidStats.keyAt(i3);
            long[] times = this.mUidStats.get(u3).getCpuClusterTimes();
            if (times != null) {
                pw.print("  ");
                pw.print(u3);
                pw.print(": ");
                pw.println(java.util.Arrays.toString(times));
            }
        }
        pw.println("Per UID CPU frequency time in ms:");
        for (int i4 = 0; i4 < size; i4++) {
            int u4 = this.mUidStats.keyAt(i4);
            long[] times2 = this.mUidStats.get(u4).getCpuFreqTimes(0);
            if (times2 != null) {
                pw.print("  ");
                pw.print(u4);
                pw.print(": ");
                pw.println(java.util.Arrays.toString(times2));
            }
        }
        if (!com.android.internal.hidden_from_bootclasspath.com.android.server.power.optimization.Flags.disableSystemServicePowerAttr()) {
            updateSystemServiceCallStats();
            if (this.mBinderThreadCpuTimesUs != null) {
                pw.println("Per UID System server binder time in ms:");
                long[] systemServiceTimeAtCpuSpeeds = getSystemServiceTimeAtCpuSpeeds();
                for (int i5 = 0; i5 < size; i5++) {
                    int u5 = this.mUidStats.keyAt(i5);
                    double proportionalSystemServiceUsage = this.mUidStats.get(u5).getProportionalSystemServiceUsage();
                    long timeUs = 0;
                    for (int j = systemServiceTimeAtCpuSpeeds.length - 1; j >= 0; j--) {
                        double d = timeUs;
                        long timeUs2 = systemServiceTimeAtCpuSpeeds[j];
                        timeUs = (long) (d + (timeUs2 * proportionalSystemServiceUsage));
                    }
                    pw.print("  ");
                    pw.print(u5);
                    pw.print(": ");
                    pw.println(timeUs / 1000);
                }
            }
        }
    }

    public void dumpEnergyConsumerStatsLocked(java.io.PrintWriter pw) {
        pw.printf("On-battery energy consumer stats (microcoulombs) \n", new java.lang.Object[0]);
        if (this.mGlobalEnergyConsumerStats == null) {
            pw.printf("    Not supported on this device.\n", new java.lang.Object[0]);
            return;
        }
        dumpEnergyConsumerStatsLocked(pw, "global usage", this.mGlobalEnergyConsumerStats);
        int size = this.mUidStats.size();
        for (int i = 0; i < size; i++) {
            int u = this.mUidStats.keyAt(i);
            com.android.server.power.stats.BatteryStatsImpl.Uid uid = this.mUidStats.get(u);
            java.lang.String name = "uid " + uid.mUid;
            dumpEnergyConsumerStatsLocked(pw, name, uid.mUidEnergyConsumerStats);
        }
    }

    private void dumpEnergyConsumerStatsLocked(java.io.PrintWriter pw, java.lang.String name, com.android.internal.power.EnergyConsumerStats stats) {
        if (stats == null) {
            return;
        }
        android.util.IndentingPrintWriter iPw = new android.util.IndentingPrintWriter(pw, "    ");
        iPw.increaseIndent();
        iPw.printf("%s:\n", new java.lang.Object[]{name});
        iPw.increaseIndent();
        stats.dump(iPw);
        iPw.decreaseIndent();
    }

    public void dumpPowerProfileLocked(java.io.PrintWriter pw) {
        android.util.IndentingPrintWriter iPw = new android.util.IndentingPrintWriter(pw, "    ");
        iPw.printf("Power Profile: \n", new java.lang.Object[0]);
        iPw.increaseIndent();
        this.mPowerProfile.dump(iPw);
        iPw.decreaseIndent();
    }

    public void schedulePowerStatsSampleCollection() {
        this.mCpuPowerStatsCollector.forceSchedule();
        this.mMobileRadioPowerStatsCollector.forceSchedule();
        this.mWifiPowerStatsCollector.forceSchedule();
        this.mBluetoothPowerStatsCollector.forceSchedule();
        this.mCameraPowerStatsCollector.forceSchedule();
        this.mGnssPowerStatsCollector.forceSchedule();
    }

    public void collectPowerStatsSamples() {
        schedulePowerStatsSampleCollection();
        android.os.ConditionVariable done = new android.os.ConditionVariable();
        android.os.Handler handler = this.mHandler;
        java.util.Objects.requireNonNull(done);
        handler.post(new com.android.server.power.stats.BatteryStatsImpl$$ExternalSyntheticLambda10(done));
        done.block();
    }

    public void dumpStatsSample(java.io.PrintWriter pw) {
        this.mCpuPowerStatsCollector.collectAndDump(pw);
        this.mMobileRadioPowerStatsCollector.collectAndDump(pw);
        this.mWifiPowerStatsCollector.collectAndDump(pw);
        this.mBluetoothPowerStatsCollector.collectAndDump(pw);
        this.mCameraPowerStatsCollector.collectAndDump(pw);
        this.mGnssPowerStatsCollector.collectAndDump(pw);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$9() {
        synchronized (this) {
            writeSyncLocked();
        }
    }

    public void writeAsyncLocked() {
        com.android.internal.os.BackgroundThread.getHandler().removeCallbacks(this.mWriteAsyncRunnable);
        com.android.internal.os.BackgroundThread.getHandler().post(this.mWriteAsyncRunnable);
    }

    public void writeSyncLocked() {
        com.android.internal.os.BackgroundThread.getHandler().removeCallbacks(this.mWriteAsyncRunnable);
        writeStatsLocked();
        writeHistoryLocked();
    }

    private void writeStatsLocked() {
        if (this.mStatsFile == null) {
            android.util.Slog.w(TAG, "writeStatsLocked: no file associated with this instance");
            return;
        }
        if (this.mShuttingDown) {
            return;
        }
        android.os.Parcel p = android.os.Parcel.obtain();
        try {
            android.os.SystemClock.uptimeMillis();
            writeSummaryToParcel(p, false);
            this.mLastWriteTimeMs = this.mClock.elapsedRealtime();
            writeParcelToFileLocked(p, this.mStatsFile);
        } finally {
            p.recycle();
        }
    }

    private void writeHistoryLocked() {
        if (this.mShuttingDown) {
            return;
        }
        this.mHistory.writeHistory();
    }

    private void writeParcelToFileLocked(android.os.Parcel p, android.util.AtomicFile file) {
        this.mWriteLock.lock();
        java.io.FileOutputStream fos = null;
        try {
            try {
                long startTimeMs = android.os.SystemClock.uptimeMillis();
                fos = file.startWrite();
                fos.write(p.marshall());
                fos.flush();
                file.finishWrite(fos);
                this.mFrameworkStatsLogger.writeCommitSysConfigFile("batterystats", android.os.SystemClock.uptimeMillis() - startTimeMs);
            } catch (java.io.IOException e) {
                android.util.Slog.w(TAG, "Error writing battery statistics", e);
                file.failWrite(fos);
            }
        } finally {
            this.mWriteLock.unlock();
        }
    }

    public void readLocked() {
        if (this.mDailyFile != null) {
            readDailyStatsLocked();
        }
        if (this.mStatsFile == null) {
            android.util.Slog.w(TAG, "readLocked: no file associated with this instance");
            return;
        }
        this.mUidStats.clear();
        android.os.Parcel stats = android.os.Parcel.obtain();
        try {
            try {
                android.os.SystemClock.uptimeMillis();
                if (this.mStatsFile.exists()) {
                    byte[] raw = this.mStatsFile.readFully();
                    stats.unmarshall(raw, 0, raw.length);
                    stats.setDataPosition(0);
                    readSummaryFromParcel(stats);
                }
            } catch (java.lang.Exception e) {
                android.util.Slog.e(TAG, "Error reading battery statistics", e);
                resetAllStatsLocked(android.os.SystemClock.uptimeMillis(), android.os.SystemClock.elapsedRealtime(), 1);
            }
            if (!this.mHistory.readSummary()) {
                resetAllStatsLocked(android.os.SystemClock.uptimeMillis(), android.os.SystemClock.elapsedRealtime(), 1);
            }
            this.mEndPlatformVersion = android.os.Build.ID;
            this.mMonotonicEndTime = -1L;
            this.mHistory.continueRecordingHistory();
            recordDailyStatsIfNeededLocked(false, this.mClock.currentTimeMillis());
        } finally {
            stats.recycle();
        }
    }

    public void readSummaryFromParcel(android.os.Parcel in) throws android.os.ParcelFormatException {
        int NPKG;
        int NMS;
        int numClusters;
        int[] policies;
        int[] policies2;
        int NRPMS;
        int version = in.readInt();
        if (version != VERSION) {
            android.util.Slog.w("BatteryStats", "readFromParcel: version got " + version + ", expected " + VERSION + "; erasing old stats");
            return;
        }
        this.mHistory.readSummaryFromParcel(in);
        this.mStartCount = in.readInt();
        this.mUptimeUs = in.readLong();
        this.mRealtimeUs = in.readLong();
        this.mStartClockTimeMs = in.readLong();
        this.mMonotonicStartTime = in.readLong();
        this.mMonotonicEndTime = in.readLong();
        this.mStartPlatformVersion = in.readString();
        this.mEndPlatformVersion = in.readString();
        this.mOnBatteryTimeBase.readSummaryFromParcel(in);
        this.mOnBatteryScreenOffTimeBase.readSummaryFromParcel(in);
        this.mDischargeUnplugLevel = in.readInt();
        this.mDischargePlugLevel = in.readInt();
        this.mDischargeCurrentLevel = in.readInt();
        this.mBatteryLevel = in.readInt();
        this.mEstimatedBatteryCapacityMah = in.readInt();
        this.mLastLearnedBatteryCapacityUah = in.readInt();
        this.mMinLearnedBatteryCapacityUah = in.readInt();
        this.mMaxLearnedBatteryCapacityUah = in.readInt();
        this.mLowDischargeAmountSinceCharge = in.readInt();
        this.mHighDischargeAmountSinceCharge = in.readInt();
        this.mDischargeAmountScreenOnSinceCharge = in.readInt();
        this.mDischargeAmountScreenOffSinceCharge = in.readInt();
        this.mDischargeAmountScreenDozeSinceCharge = in.readInt();
        this.mDischargeStepTracker.readFromParcel(in);
        this.mChargeStepTracker.readFromParcel(in);
        this.mDailyDischargeStepTracker.readFromParcel(in);
        this.mDailyChargeStepTracker.readFromParcel(in);
        this.mDischargeCounter.readSummaryFromParcelLocked(in);
        this.mDischargeScreenOffCounter.readSummaryFromParcelLocked(in);
        this.mDischargeScreenDozeCounter.readSummaryFromParcelLocked(in);
        this.mDischargeLightDozeCounter.readSummaryFromParcelLocked(in);
        this.mDischargeDeepDozeCounter.readSummaryFromParcelLocked(in);
        int NPKG2 = in.readInt();
        if (NPKG2 > 0) {
            this.mDailyPackageChanges = new java.util.ArrayList<>(NPKG2);
            while (NPKG2 > 0) {
                NPKG2--;
                android.os.BatteryStats.PackageChange pc = new android.os.BatteryStats.PackageChange();
                pc.mPackageName = in.readString();
                pc.mUpdate = in.readInt() != 0;
                pc.mVersionCode = in.readLong();
                this.mDailyPackageChanges.add(pc);
            }
            NPKG = NPKG2;
        } else {
            this.mDailyPackageChanges = null;
            NPKG = NPKG2;
        }
        this.mDailyStartTimeMs = in.readLong();
        this.mNextMinDailyDeadlineMs = in.readLong();
        this.mNextMaxDailyDeadlineMs = in.readLong();
        this.mBatteryTimeToFullSeconds = in.readLong();
        com.android.internal.power.EnergyConsumerStats.Config config = com.android.internal.power.EnergyConsumerStats.Config.createFromParcel(in);
        com.android.internal.power.EnergyConsumerStats energyConsumerStats = com.android.internal.power.EnergyConsumerStats.createAndReadSummaryFromParcel(this.mEnergyConsumerStatsConfig, in);
        if (config != null && java.util.Arrays.equals(config.getStateNames(), getBatteryConsumerProcessStateNames())) {
            this.mEnergyConsumerStatsConfig = config;
            this.mGlobalEnergyConsumerStats = energyConsumerStats;
        }
        this.mStartCount++;
        this.mScreenState = 0;
        this.mScreenOnTimer.readSummaryFromParcelLocked(in);
        this.mScreenDozeTimer.readSummaryFromParcelLocked(in);
        for (int i = 0; i < 5; i++) {
            this.mScreenBrightnessTimer[i].readSummaryFromParcelLocked(in);
        }
        int numDisplays = in.readInt();
        for (int i2 = 0; i2 < numDisplays; i2++) {
            this.mPerDisplayBatteryStats[i2].readSummaryFromParcel(in);
        }
        this.mInteractive = false;
        this.mInteractiveTimer.readSummaryFromParcelLocked(in);
        this.mPhoneOn = false;
        this.mPowerSaveModeEnabledTimer.readSummaryFromParcelLocked(in);
        this.mLongestLightIdleTimeMs = in.readLong();
        this.mLongestFullIdleTimeMs = in.readLong();
        this.mDeviceIdleModeLightTimer.readSummaryFromParcelLocked(in);
        this.mDeviceIdleModeFullTimer.readSummaryFromParcelLocked(in);
        this.mDeviceLightIdlingTimer.readSummaryFromParcelLocked(in);
        this.mDeviceIdlingTimer.readSummaryFromParcelLocked(in);
        this.mPhoneOnTimer.readSummaryFromParcelLocked(in);
        for (int i3 = 0; i3 < CELL_SIGNAL_STRENGTH_LEVEL_COUNT; i3++) {
            this.mPhoneSignalStrengthsTimer[i3].readSummaryFromParcelLocked(in);
        }
        this.mPhoneSignalScanningTimer.readSummaryFromParcelLocked(in);
        for (int i4 = 0; i4 < NUM_DATA_CONNECTION_TYPES; i4++) {
            this.mPhoneDataConnectionsTimer[i4].readSummaryFromParcelLocked(in);
        }
        this.mNrNsaTimer.readSummaryFromParcelLocked(in);
        for (int i5 = 0; i5 < 10; i5++) {
            this.mNetworkByteActivityCounters[i5].readSummaryFromParcelLocked(in);
            this.mNetworkPacketActivityCounters[i5].readSummaryFromParcelLocked(in);
        }
        int numRat = in.readInt();
        for (int i6 = 0; i6 < numRat; i6++) {
            if (in.readInt() != 0) {
                getRatBatteryStatsLocked(i6).readSummaryFromParcel(in);
            }
        }
        this.mMobileRadioPowerState = 1;
        this.mMobileRadioActiveTimer.readSummaryFromParcelLocked(in);
        this.mMobileRadioActivePerAppTimer.readSummaryFromParcelLocked(in);
        this.mMobileRadioActiveAdjustedTime.readSummaryFromParcelLocked(in);
        this.mMobileRadioActiveUnknownTime.readSummaryFromParcelLocked(in);
        this.mMobileRadioActiveUnknownCount.readSummaryFromParcelLocked(in);
        this.mWifiMulticastWakelockTimer.readSummaryFromParcelLocked(in);
        this.mWifiRadioPowerState = 1;
        this.mWifiOn = false;
        this.mWifiOnTimer.readSummaryFromParcelLocked(in);
        this.mGlobalWifiRunning = false;
        this.mGlobalWifiRunningTimer.readSummaryFromParcelLocked(in);
        for (int i7 = 0; i7 < 8; i7++) {
            this.mWifiStateTimer[i7].readSummaryFromParcelLocked(in);
        }
        for (int i8 = 0; i8 < 13; i8++) {
            this.mWifiSupplStateTimer[i8].readSummaryFromParcelLocked(in);
        }
        for (int i9 = 0; i9 < 5; i9++) {
            this.mWifiSignalStrengthsTimer[i9].readSummaryFromParcelLocked(in);
        }
        this.mWifiActiveTimer.readSummaryFromParcelLocked(in);
        this.mWifiActivity.readSummaryFromParcel(in);
        for (int i10 = 0; i10 < this.mGpsSignalQualityTimer.length; i10++) {
            this.mGpsSignalQualityTimer[i10].readSummaryFromParcelLocked(in);
        }
        this.mBluetoothActivity.readSummaryFromParcel(in);
        this.mModemActivity.readSummaryFromParcel(in);
        this.mHasWifiReporting = in.readInt() != 0;
        this.mHasBluetoothReporting = in.readInt() != 0;
        this.mHasModemReporting = in.readInt() != 0;
        this.mNumConnectivityChange = in.readInt();
        this.mFlashlightOnNesting = 0;
        this.mFlashlightOnTimer.readSummaryFromParcelLocked(in);
        this.mCameraOnNesting = 0;
        this.mCameraOnTimer.readSummaryFromParcelLocked(in);
        this.mBluetoothScanNesting = 0;
        this.mBluetoothScanTimer.readSummaryFromParcelLocked(in);
        int NRPMS2 = in.readInt();
        if (NRPMS2 > 10000) {
            throw new android.os.ParcelFormatException("File corrupt: too many rpm stats " + NRPMS2);
        }
        for (int irpm = 0; irpm < NRPMS2; irpm++) {
            if (in.readInt() != 0) {
                java.lang.String rpmName = in.readString();
                getRpmTimerLocked(rpmName).readSummaryFromParcelLocked(in);
            }
        }
        int NSORPMS = in.readInt();
        if (NSORPMS > 10000) {
            throw new android.os.ParcelFormatException("File corrupt: too many screen-off rpm stats " + NSORPMS);
        }
        for (int irpm2 = 0; irpm2 < NSORPMS; irpm2++) {
            if (in.readInt() != 0) {
                java.lang.String rpmName2 = in.readString();
                getScreenOffRpmTimerLocked(rpmName2).readSummaryFromParcelLocked(in);
            }
        }
        int NKW = in.readInt();
        if (NKW > 10000) {
            throw new android.os.ParcelFormatException("File corrupt: too many kernel wake locks " + NKW);
        }
        for (int ikw = 0; ikw < NKW; ikw++) {
            if (in.readInt() != 0) {
                java.lang.String kwltName = in.readString();
                getKernelWakelockTimerLocked(kwltName).readSummaryFromParcelLocked(in);
            }
        }
        int NWR = in.readInt();
        if (NWR > 10000) {
            throw new android.os.ParcelFormatException("File corrupt: too many wakeup reasons " + NWR);
        }
        for (int iwr = 0; iwr < NWR; iwr++) {
            if (in.readInt() != 0) {
                java.lang.String reasonName = in.readString();
                getWakeupReasonTimerLocked(reasonName).readSummaryFromParcelLocked(in);
            }
        }
        int NMS2 = in.readInt();
        int ims = 0;
        while (ims < NMS2) {
            if (in.readInt() == 0) {
                NRPMS = NRPMS2;
            } else {
                long kmstName = in.readLong();
                NRPMS = NRPMS2;
                getKernelMemoryTimerLocked(kmstName).readSummaryFromParcelLocked(in);
            }
            ims++;
            NRPMS2 = NRPMS;
        }
        int iu = NRPMS2;
        int NU = in.readInt();
        int i11 = 10000;
        if (NU > 10000) {
            throw new android.os.ParcelFormatException("File corrupt: too many uids " + NU);
        }
        long elapsedRealtimeMs = this.mClock.elapsedRealtime();
        long uptimeMs = this.mClock.uptimeMillis();
        int NS = 0;
        while (NS < NU) {
            int uid = in.readInt();
            int version2 = version;
            int NRPMS3 = iu;
            int NRPMS4 = NS;
            int NU2 = NU;
            int numRat2 = numRat;
            int NSORPMS2 = NSORPMS;
            int numDisplays2 = numDisplays;
            com.android.server.power.stats.BatteryStatsImpl.Uid u = new com.android.server.power.stats.BatteryStatsImpl.Uid(this, uid, elapsedRealtimeMs, uptimeMs);
            this.mUidStats.put(uid, u);
            u.mOnBatteryBackgroundTimeBase.readSummaryFromParcel(in);
            u.mOnBatteryScreenOffBackgroundTimeBase.readSummaryFromParcel(in);
            u.mWifiRunning = false;
            if (in.readInt() != 0) {
                u.mWifiRunningTimer.readSummaryFromParcelLocked(in);
            }
            u.mFullWifiLockOut = false;
            if (in.readInt() != 0) {
                u.mFullWifiLockTimer.readSummaryFromParcelLocked(in);
            }
            u.mWifiScanStarted = false;
            if (in.readInt() != 0) {
                u.mWifiScanTimer.readSummaryFromParcelLocked(in);
            }
            u.mWifiBatchedScanBinStarted = -1;
            for (int i12 = 0; i12 < 5; i12++) {
                if (in.readInt() != 0) {
                    u.makeWifiBatchedScanBin(i12, null);
                    u.mWifiBatchedScanTimer[i12].readSummaryFromParcelLocked(in);
                }
            }
            u.mWifiMulticastWakelockCount = 0;
            if (in.readInt() != 0) {
                u.mWifiMulticastTimer.readSummaryFromParcelLocked(in);
            }
            if (in.readInt() != 0) {
                u.createAudioTurnedOnTimerLocked().readSummaryFromParcelLocked(in);
            }
            if (in.readInt() != 0) {
                u.createVideoTurnedOnTimerLocked().readSummaryFromParcelLocked(in);
            }
            if (in.readInt() != 0) {
                u.createFlashlightTurnedOnTimerLocked().readSummaryFromParcelLocked(in);
            }
            if (in.readInt() != 0) {
                u.createCameraTurnedOnTimerLocked().readSummaryFromParcelLocked(in);
            }
            if (in.readInt() != 0) {
                u.createForegroundActivityTimerLocked().readSummaryFromParcelLocked(in);
            }
            if (in.readInt() != 0) {
                u.createForegroundServiceTimerLocked().readSummaryFromParcelLocked(in);
            }
            if (in.readInt() != 0) {
                u.createAggregatedPartialWakelockTimerLocked().readSummaryFromParcelLocked(in);
            }
            if (in.readInt() != 0) {
                u.createBluetoothScanTimerLocked().readSummaryFromParcelLocked(in);
            }
            if (in.readInt() != 0) {
                u.createBluetoothUnoptimizedScanTimerLocked().readSummaryFromParcelLocked(in);
            }
            if (in.readInt() != 0) {
                u.createBluetoothScanResultCounterLocked().readSummaryFromParcelLocked(in);
            }
            if (in.readInt() != 0) {
                u.createBluetoothScanResultBgCounterLocked().readSummaryFromParcelLocked(in);
            }
            u.mProcessState = 7;
            for (int i13 = 0; i13 < 7; i13++) {
                if (in.readInt() != 0) {
                    u.makeProcessState(i13, null);
                    u.mProcessStateTimer[i13].readSummaryFromParcelLocked(in);
                }
            }
            if (in.readInt() != 0) {
                u.createVibratorOnTimerLocked().readSummaryFromParcelLocked(in);
            }
            if (in.readInt() != 0) {
                if (u.mUserActivityCounters == null) {
                    u.initUserActivityLocked();
                }
                for (int i14 = 0; i14 < com.android.server.power.stats.BatteryStatsImpl.Uid.NUM_USER_ACTIVITY_TYPES; i14++) {
                    u.mUserActivityCounters[i14].readSummaryFromParcelLocked(in);
                }
            }
            int i15 = in.readInt();
            if (i15 != 0) {
                u.ensureNetworkActivityLocked();
                for (int i16 = 0; i16 < 10; i16++) {
                    u.mNetworkByteActivityCounters[i16].readSummaryFromParcelLocked(in);
                    u.mNetworkPacketActivityCounters[i16].readSummaryFromParcelLocked(in);
                }
                if (in.readBoolean()) {
                    u.mMobileRadioActiveTime = com.android.server.power.stats.BatteryStatsImpl.TimeMultiStateCounter.readFromParcel(in, this.mOnBatteryTimeBase, 5, elapsedRealtimeMs);
                }
                u.mMobileRadioActiveCount.readSummaryFromParcelLocked(in);
            }
            u.mUserCpuTime.readSummaryFromParcelLocked(in);
            u.mSystemCpuTime.readSummaryFromParcelLocked(in);
            if (in.readInt() != 0) {
                int numClusters2 = in.readInt();
                int[] policies3 = this.mCpuScalingPolicies != null ? this.mCpuScalingPolicies.getPolicies() : null;
                if (policies3 != null && policies3.length != numClusters2) {
                    throw new android.os.ParcelFormatException("Incompatible cpu cluster arrangement");
                }
                detachIfNotNull(u.mCpuClusterSpeedTimesUs);
                u.mCpuClusterSpeedTimesUs = new com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounter[numClusters2][];
                int cluster = 0;
                while (cluster < numClusters2) {
                    if (in.readInt() != 0) {
                        int NSB = in.readInt();
                        if (policies3 != null && this.mCpuScalingPolicies.getFrequencies(policies3[cluster]).length != NSB) {
                            throw new android.os.ParcelFormatException("File corrupt: too many speed bins " + NSB);
                        }
                        numClusters = numClusters2;
                        u.mCpuClusterSpeedTimesUs[cluster] = new com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounter[NSB];
                        int speed = 0;
                        while (speed < NSB) {
                            if (in.readInt() != 0) {
                                policies2 = policies3;
                                u.mCpuClusterSpeedTimesUs[cluster][speed] = new com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounter(this.mOnBatteryTimeBase);
                                u.mCpuClusterSpeedTimesUs[cluster][speed].readSummaryFromParcelLocked(in);
                            } else {
                                policies2 = policies3;
                            }
                            speed++;
                            policies3 = policies2;
                        }
                        policies = policies3;
                    } else {
                        numClusters = numClusters2;
                        policies = policies3;
                        u.mCpuClusterSpeedTimesUs[cluster] = null;
                    }
                    cluster++;
                    numClusters2 = numClusters;
                    policies3 = policies;
                }
            } else {
                detachIfNotNull(u.mCpuClusterSpeedTimesUs);
                u.mCpuClusterSpeedTimesUs = null;
            }
            detachIfNotNull(u.mCpuFreqTimeMs);
            u.mCpuFreqTimeMs = com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounterArray.readSummaryFromParcelLocked(in, this.mOnBatteryTimeBase);
            detachIfNotNull(u.mScreenOffCpuFreqTimeMs);
            u.mScreenOffCpuFreqTimeMs = com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounterArray.readSummaryFromParcelLocked(in, this.mOnBatteryScreenOffTimeBase);
            int stateCount = in.readInt();
            if (stateCount != 0) {
                u.mCpuActiveTimeMs = com.android.server.power.stats.BatteryStatsImpl.TimeMultiStateCounter.readFromParcel(in, this.mOnBatteryTimeBase, 5, this.mClock.elapsedRealtime());
            }
            u.mCpuClusterTimesMs.readSummaryFromParcelLocked(in);
            detachIfNotNull(u.mProcStateTimeMs);
            u.mProcStateTimeMs = null;
            int stateCount2 = in.readInt();
            if (stateCount2 != 0) {
                detachIfNotNull(u.mProcStateTimeMs);
                u.mProcStateTimeMs = com.android.server.power.stats.BatteryStatsImpl.TimeInFreqMultiStateCounter.readFromParcel(in, this.mOnBatteryTimeBase, 8, this.mCpuScalingPolicies.getScalingStepCount(), this.mClock.elapsedRealtime());
            }
            detachIfNotNull(u.mProcStateScreenOffTimeMs);
            u.mProcStateScreenOffTimeMs = null;
            int stateCount3 = in.readInt();
            if (stateCount3 != 0) {
                detachIfNotNull(u.mProcStateScreenOffTimeMs);
                u.mProcStateScreenOffTimeMs = com.android.server.power.stats.BatteryStatsImpl.TimeInFreqMultiStateCounter.readFromParcel(in, this.mOnBatteryScreenOffTimeBase, 8, this.mCpuScalingPolicies.getScalingStepCount(), this.mClock.elapsedRealtime());
            }
            if (in.readInt() != 0) {
                detachIfNotNull(u.mMobileRadioApWakeupCount);
                u.mMobileRadioApWakeupCount = new com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounter(this.mOnBatteryTimeBase);
                u.mMobileRadioApWakeupCount.readSummaryFromParcelLocked(in);
            } else {
                detachIfNotNull(u.mMobileRadioApWakeupCount);
                u.mMobileRadioApWakeupCount = null;
            }
            if (in.readInt() != 0) {
                detachIfNotNull(u.mWifiRadioApWakeupCount);
                u.mWifiRadioApWakeupCount = new com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounter(this.mOnBatteryTimeBase);
                u.mWifiRadioApWakeupCount.readSummaryFromParcelLocked(in);
            } else {
                detachIfNotNull(u.mWifiRadioApWakeupCount);
                u.mWifiRadioApWakeupCount = null;
            }
            u.mUidEnergyConsumerStats = com.android.internal.power.EnergyConsumerStats.createAndReadSummaryFromParcel(this.mEnergyConsumerStatsConfig, in);
            int NW = in.readInt();
            if (NW > MAX_WAKELOCKS_PER_UID + 1) {
                throw new android.os.ParcelFormatException("File corrupt: too many wake locks " + NW);
            }
            for (int iw = 0; iw < NW; iw++) {
                java.lang.String wlName = in.readString();
                u.readWakeSummaryFromParcelLocked(wlName, in);
            }
            int NS2 = in.readInt();
            if (NS2 > MAX_WAKELOCKS_PER_UID + 1) {
                throw new android.os.ParcelFormatException("File corrupt: too many syncs " + NS2);
            }
            for (int is = 0; is < NS2; is++) {
                java.lang.String name = in.readString();
                u.readSyncSummaryFromParcelLocked(name, in);
            }
            int NJ = in.readInt();
            if (NJ > MAX_WAKELOCKS_PER_UID + 1) {
                throw new android.os.ParcelFormatException("File corrupt: too many job timers " + NJ);
            }
            for (int ij = 0; ij < NJ; ij++) {
                java.lang.String name2 = in.readString();
                u.readJobSummaryFromParcelLocked(name2, in);
            }
            u.readJobCompletionsFromParcelLocked(in);
            u.mJobsDeferredEventCount.readSummaryFromParcelLocked(in);
            u.mJobsDeferredCount.readSummaryFromParcelLocked(in);
            u.mJobsFreshnessTimeMs.readSummaryFromParcelLocked(in);
            detachIfNotNull(u.mJobsFreshnessBuckets);
            for (int i17 = 0; i17 < JOB_FRESHNESS_BUCKETS.length; i17++) {
                if (in.readInt() != 0) {
                    u.mJobsFreshnessBuckets[i17] = new com.android.server.power.stats.BatteryStatsImpl.Counter(u.mBsi.mOnBatteryTimeBase);
                    u.mJobsFreshnessBuckets[i17].readSummaryFromParcelLocked(in);
                }
            }
            int NP = in.readInt();
            if (NP > 1000) {
                throw new android.os.ParcelFormatException("File corrupt: too many sensors " + NP);
            }
            int is2 = 0;
            while (is2 < NP) {
                int seNumber = in.readInt();
                if (in.readInt() == 0) {
                    NMS = NMS2;
                } else {
                    NMS = NMS2;
                    u.getSensorTimerLocked(seNumber, true).readSummaryFromParcelLocked(in);
                }
                is2++;
                NMS2 = NMS;
            }
            int NMS3 = NMS2;
            int NP2 = in.readInt();
            if (NP2 > 10000) {
                throw new android.os.ParcelFormatException("File corrupt: too many processes " + NP2);
            }
            int ip = 0;
            while (ip < NP2) {
                java.lang.String procName = in.readString();
                com.android.server.power.stats.BatteryStatsImpl.Uid.Proc p = u.getProcessStatsLocked(procName);
                p.mUserTimeMs = in.readLong();
                p.mSystemTimeMs = in.readLong();
                p.mForegroundTimeMs = in.readLong();
                p.mStarts = in.readInt();
                p.mNumCrashes = in.readInt();
                p.mNumAnrs = in.readInt();
                p.readExcessivePowerFromParcelLocked(in);
                ip++;
                NWR = NWR;
            }
            int NWR2 = NWR;
            int NP3 = in.readInt();
            if (NP3 > 10000) {
                throw new android.os.ParcelFormatException("File corrupt: too many packages " + NP3);
            }
            int ip2 = 0;
            while (ip2 < NP3) {
                java.lang.String pkgName = in.readString();
                detachIfNotNull(u.mPackageStats.get(pkgName));
                com.android.server.power.stats.BatteryStatsImpl.Uid.Pkg p2 = u.getPackageStatsLocked(pkgName);
                int NWA = in.readInt();
                if (NWA > 10000) {
                    throw new android.os.ParcelFormatException("File corrupt: too many wakeup alarms " + NWA);
                }
                p2.mWakeupAlarms.clear();
                int iwa = 0;
                while (iwa < NWA) {
                    int NS3 = NS2;
                    java.lang.String tag = in.readString();
                    long elapsedRealtimeMs2 = elapsedRealtimeMs;
                    com.android.server.power.stats.BatteryStatsImpl.Counter c = new com.android.server.power.stats.BatteryStatsImpl.Counter(this.mOnBatteryScreenOffTimeBase);
                    c.readSummaryFromParcelLocked(in);
                    p2.mWakeupAlarms.put(tag, c);
                    iwa++;
                    NS2 = NS3;
                    elapsedRealtimeMs = elapsedRealtimeMs2;
                }
                long elapsedRealtimeMs3 = elapsedRealtimeMs;
                NS2 = in.readInt();
                if (NS2 > 10000) {
                    throw new android.os.ParcelFormatException("File corrupt: too many services " + NS2);
                }
                int is3 = 0;
                while (is3 < NS2) {
                    java.lang.String servName = in.readString();
                    com.android.server.power.stats.BatteryStatsImpl.Uid.Pkg.Serv s = u.getServiceStatsLocked(pkgName, servName);
                    s.mStartTimeMs = in.readLong();
                    s.mStarts = in.readInt();
                    s.mLaunches = in.readInt();
                    is3++;
                    u = u;
                    pkgName = pkgName;
                }
                ip2++;
                elapsedRealtimeMs = elapsedRealtimeMs3;
            }
            NS = NRPMS4 + 1;
            numRat = numRat2;
            numDisplays = numDisplays2;
            NMS2 = NMS3;
            version = version2;
            iu = NRPMS3;
            NU = NU2;
            NSORPMS = NSORPMS2;
            NWR = NWR2;
            i11 = 10000;
        }
        if (!com.android.internal.hidden_from_bootclasspath.com.android.server.power.optimization.Flags.disableSystemServicePowerAttr()) {
            this.mBinderThreadCpuTimesUs = com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounterArray.readSummaryFromParcelLocked(in, this.mOnBatteryTimeBase);
        }
    }

    public void writeSummaryToParcel(android.os.Parcel parcel, boolean z) {
        boolean z2;
        int i;
        com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounter[][] longSamplingCounterArr;
        com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounter[][] longSamplingCounterArr2;
        pullPendingStateUpdatesLocked();
        getStartClockTime();
        long jUptimeMillis = this.mClock.uptimeMillis() * 1000;
        long jElapsedRealtime = this.mClock.elapsedRealtime() * 1000;
        parcel.writeInt(VERSION);
        this.mHistory.writeSummaryToParcel(parcel, z);
        parcel.writeInt(this.mStartCount);
        parcel.writeLong(computeUptime(jUptimeMillis, 0));
        parcel.writeLong(computeRealtime(jElapsedRealtime, 0));
        parcel.writeLong(this.mStartClockTimeMs);
        parcel.writeLong(this.mMonotonicStartTime);
        parcel.writeLong(this.mMonotonicClock.monotonicTime());
        parcel.writeString(this.mStartPlatformVersion);
        parcel.writeString(this.mEndPlatformVersion);
        this.mOnBatteryTimeBase.writeSummaryToParcel(parcel, jUptimeMillis, jElapsedRealtime);
        this.mOnBatteryScreenOffTimeBase.writeSummaryToParcel(parcel, jUptimeMillis, jElapsedRealtime);
        parcel.writeInt(this.mDischargeUnplugLevel);
        parcel.writeInt(this.mDischargePlugLevel);
        parcel.writeInt(this.mDischargeCurrentLevel);
        parcel.writeInt(this.mBatteryLevel);
        parcel.writeInt(this.mEstimatedBatteryCapacityMah);
        parcel.writeInt(this.mLastLearnedBatteryCapacityUah);
        parcel.writeInt(this.mMinLearnedBatteryCapacityUah);
        parcel.writeInt(this.mMaxLearnedBatteryCapacityUah);
        parcel.writeInt(getLowDischargeAmountSinceCharge());
        parcel.writeInt(getHighDischargeAmountSinceCharge());
        parcel.writeInt(getDischargeAmountScreenOnSinceCharge());
        parcel.writeInt(getDischargeAmountScreenOffSinceCharge());
        parcel.writeInt(getDischargeAmountScreenDozeSinceCharge());
        this.mDischargeStepTracker.writeToParcel(parcel);
        this.mChargeStepTracker.writeToParcel(parcel);
        this.mDailyDischargeStepTracker.writeToParcel(parcel);
        this.mDailyChargeStepTracker.writeToParcel(parcel);
        this.mDischargeCounter.writeSummaryFromParcelLocked(parcel);
        this.mDischargeScreenOffCounter.writeSummaryFromParcelLocked(parcel);
        this.mDischargeScreenDozeCounter.writeSummaryFromParcelLocked(parcel);
        this.mDischargeLightDozeCounter.writeSummaryFromParcelLocked(parcel);
        this.mDischargeDeepDozeCounter.writeSummaryFromParcelLocked(parcel);
        if (this.mDailyPackageChanges != null) {
            int size = this.mDailyPackageChanges.size();
            parcel.writeInt(size);
            for (int i2 = 0; i2 < size; i2++) {
                android.os.BatteryStats.PackageChange packageChange = this.mDailyPackageChanges.get(i2);
                parcel.writeString(packageChange.mPackageName);
                parcel.writeInt(packageChange.mUpdate ? 1 : 0);
                parcel.writeLong(packageChange.mVersionCode);
            }
        } else {
            parcel.writeInt(0);
        }
        parcel.writeLong(this.mDailyStartTimeMs);
        parcel.writeLong(this.mNextMinDailyDeadlineMs);
        parcel.writeLong(this.mNextMaxDailyDeadlineMs);
        parcel.writeLong(this.mBatteryTimeToFullSeconds);
        com.android.internal.power.EnergyConsumerStats.Config.writeToParcel(this.mEnergyConsumerStatsConfig, parcel);
        com.android.internal.power.EnergyConsumerStats.writeSummaryToParcel(this.mGlobalEnergyConsumerStats, parcel);
        this.mScreenOnTimer.writeSummaryFromParcelLocked(parcel, jElapsedRealtime);
        this.mScreenDozeTimer.writeSummaryFromParcelLocked(parcel, jElapsedRealtime);
        for (int i3 = 0; i3 < 5; i3++) {
            this.mScreenBrightnessTimer[i3].writeSummaryFromParcelLocked(parcel, jElapsedRealtime);
        }
        int length = this.mPerDisplayBatteryStats.length;
        parcel.writeInt(length);
        for (int i4 = 0; i4 < length; i4++) {
            this.mPerDisplayBatteryStats[i4].writeSummaryToParcel(parcel, jElapsedRealtime);
        }
        this.mInteractiveTimer.writeSummaryFromParcelLocked(parcel, jElapsedRealtime);
        this.mPowerSaveModeEnabledTimer.writeSummaryFromParcelLocked(parcel, jElapsedRealtime);
        parcel.writeLong(this.mLongestLightIdleTimeMs);
        parcel.writeLong(this.mLongestFullIdleTimeMs);
        this.mDeviceIdleModeLightTimer.writeSummaryFromParcelLocked(parcel, jElapsedRealtime);
        this.mDeviceIdleModeFullTimer.writeSummaryFromParcelLocked(parcel, jElapsedRealtime);
        this.mDeviceLightIdlingTimer.writeSummaryFromParcelLocked(parcel, jElapsedRealtime);
        this.mDeviceIdlingTimer.writeSummaryFromParcelLocked(parcel, jElapsedRealtime);
        this.mPhoneOnTimer.writeSummaryFromParcelLocked(parcel, jElapsedRealtime);
        for (int i5 = 0; i5 < CELL_SIGNAL_STRENGTH_LEVEL_COUNT; i5++) {
            this.mPhoneSignalStrengthsTimer[i5].writeSummaryFromParcelLocked(parcel, jElapsedRealtime);
        }
        this.mPhoneSignalScanningTimer.writeSummaryFromParcelLocked(parcel, jElapsedRealtime);
        for (int i6 = 0; i6 < NUM_DATA_CONNECTION_TYPES; i6++) {
            this.mPhoneDataConnectionsTimer[i6].writeSummaryFromParcelLocked(parcel, jElapsedRealtime);
        }
        this.mNrNsaTimer.writeSummaryFromParcelLocked(parcel, jElapsedRealtime);
        for (int i7 = 0; i7 < 10; i7++) {
            this.mNetworkByteActivityCounters[i7].writeSummaryFromParcelLocked(parcel);
            this.mNetworkPacketActivityCounters[i7].writeSummaryFromParcelLocked(parcel);
        }
        int length2 = this.mPerRatBatteryStats.length;
        parcel.writeInt(length2);
        int i8 = 0;
        while (true) {
            z2 = true;
            if (i8 >= length2) {
                break;
            }
            com.android.server.power.stats.BatteryStatsImpl.RadioAccessTechnologyBatteryStats radioAccessTechnologyBatteryStats = this.mPerRatBatteryStats[i8];
            if (radioAccessTechnologyBatteryStats == null) {
                parcel.writeInt(0);
            } else {
                parcel.writeInt(1);
                radioAccessTechnologyBatteryStats.writeSummaryToParcel(parcel, jElapsedRealtime);
            }
            i8++;
        }
        this.mMobileRadioActiveTimer.writeSummaryFromParcelLocked(parcel, jElapsedRealtime);
        this.mMobileRadioActivePerAppTimer.writeSummaryFromParcelLocked(parcel, jElapsedRealtime);
        this.mMobileRadioActiveAdjustedTime.writeSummaryFromParcelLocked(parcel);
        this.mMobileRadioActiveUnknownTime.writeSummaryFromParcelLocked(parcel);
        this.mMobileRadioActiveUnknownCount.writeSummaryFromParcelLocked(parcel);
        this.mWifiMulticastWakelockTimer.writeSummaryFromParcelLocked(parcel, jElapsedRealtime);
        this.mWifiOnTimer.writeSummaryFromParcelLocked(parcel, jElapsedRealtime);
        this.mGlobalWifiRunningTimer.writeSummaryFromParcelLocked(parcel, jElapsedRealtime);
        for (int i9 = 0; i9 < 8; i9++) {
            this.mWifiStateTimer[i9].writeSummaryFromParcelLocked(parcel, jElapsedRealtime);
        }
        for (int i10 = 0; i10 < 13; i10++) {
            this.mWifiSupplStateTimer[i10].writeSummaryFromParcelLocked(parcel, jElapsedRealtime);
        }
        for (int i11 = 0; i11 < 5; i11++) {
            this.mWifiSignalStrengthsTimer[i11].writeSummaryFromParcelLocked(parcel, jElapsedRealtime);
        }
        this.mWifiActiveTimer.writeSummaryFromParcelLocked(parcel, jElapsedRealtime);
        this.mWifiActivity.writeSummaryToParcel(parcel);
        for (int i12 = 0; i12 < this.mGpsSignalQualityTimer.length; i12++) {
            this.mGpsSignalQualityTimer[i12].writeSummaryFromParcelLocked(parcel, jElapsedRealtime);
        }
        this.mBluetoothActivity.writeSummaryToParcel(parcel);
        this.mModemActivity.writeSummaryToParcel(parcel);
        parcel.writeInt(this.mHasWifiReporting ? 1 : 0);
        parcel.writeInt(this.mHasBluetoothReporting ? 1 : 0);
        parcel.writeInt(this.mHasModemReporting ? 1 : 0);
        parcel.writeInt(this.mNumConnectivityChange);
        this.mFlashlightOnTimer.writeSummaryFromParcelLocked(parcel, jElapsedRealtime);
        this.mCameraOnTimer.writeSummaryFromParcelLocked(parcel, jElapsedRealtime);
        this.mBluetoothScanTimer.writeSummaryFromParcelLocked(parcel, jElapsedRealtime);
        parcel.writeInt(this.mRpmStats.size());
        for (java.util.Map.Entry<java.lang.String, com.android.server.power.stats.BatteryStatsImpl.SamplingTimer> entry : this.mRpmStats.entrySet()) {
            com.android.server.power.stats.BatteryStatsImpl.SamplingTimer value = entry.getValue();
            if (value != null) {
                parcel.writeInt(1);
                parcel.writeString(entry.getKey());
                value.writeSummaryFromParcelLocked(parcel, jElapsedRealtime);
            } else {
                parcel.writeInt(0);
            }
        }
        parcel.writeInt(this.mScreenOffRpmStats.size());
        for (java.util.Map.Entry<java.lang.String, com.android.server.power.stats.BatteryStatsImpl.SamplingTimer> entry2 : this.mScreenOffRpmStats.entrySet()) {
            com.android.server.power.stats.BatteryStatsImpl.SamplingTimer value2 = entry2.getValue();
            if (value2 != null) {
                parcel.writeInt(1);
                parcel.writeString(entry2.getKey());
                value2.writeSummaryFromParcelLocked(parcel, jElapsedRealtime);
            } else {
                parcel.writeInt(0);
            }
        }
        parcel.writeInt(this.mKernelWakelockStats.size());
        for (java.util.Map.Entry<java.lang.String, com.android.server.power.stats.BatteryStatsImpl.SamplingTimer> entry3 : this.mKernelWakelockStats.entrySet()) {
            com.android.server.power.stats.BatteryStatsImpl.SamplingTimer value3 = entry3.getValue();
            if (value3 != null) {
                parcel.writeInt(1);
                parcel.writeString(entry3.getKey());
                value3.writeSummaryFromParcelLocked(parcel, jElapsedRealtime);
            } else {
                parcel.writeInt(0);
            }
        }
        parcel.writeInt(this.mWakeupReasonStats.size());
        for (java.util.Map.Entry<java.lang.String, com.android.server.power.stats.BatteryStatsImpl.SamplingTimer> entry4 : this.mWakeupReasonStats.entrySet()) {
            com.android.server.power.stats.BatteryStatsImpl.SamplingTimer value4 = entry4.getValue();
            if (value4 != null) {
                parcel.writeInt(1);
                parcel.writeString(entry4.getKey());
                value4.writeSummaryFromParcelLocked(parcel, jElapsedRealtime);
            } else {
                parcel.writeInt(0);
            }
        }
        parcel.writeInt(this.mKernelMemoryStats.size());
        for (int i13 = 0; i13 < this.mKernelMemoryStats.size(); i13++) {
            com.android.server.power.stats.BatteryStatsImpl.SamplingTimer samplingTimerValueAt = this.mKernelMemoryStats.valueAt(i13);
            if (samplingTimerValueAt != null) {
                parcel.writeInt(1);
                parcel.writeLong(this.mKernelMemoryStats.keyAt(i13));
                samplingTimerValueAt.writeSummaryFromParcelLocked(parcel, jElapsedRealtime);
            } else {
                parcel.writeInt(0);
            }
        }
        int size2 = this.mUidStats.size();
        parcel.writeInt(size2);
        int i14 = 0;
        while (i14 < size2) {
            parcel.writeInt(this.mUidStats.keyAt(i14));
            com.android.server.power.stats.BatteryStatsImpl.Uid uidValueAt = this.mUidStats.valueAt(i14);
            int i15 = length2;
            int i16 = length;
            int i17 = size2;
            uidValueAt.mOnBatteryBackgroundTimeBase.writeSummaryToParcel(parcel, jUptimeMillis, jElapsedRealtime);
            uidValueAt.mOnBatteryScreenOffBackgroundTimeBase.writeSummaryToParcel(parcel, jUptimeMillis, jElapsedRealtime);
            if (uidValueAt.mWifiRunningTimer != null) {
                parcel.writeInt(1);
                uidValueAt.mWifiRunningTimer.writeSummaryFromParcelLocked(parcel, jElapsedRealtime);
            } else {
                parcel.writeInt(0);
            }
            if (uidValueAt.mFullWifiLockTimer != null) {
                parcel.writeInt(1);
                uidValueAt.mFullWifiLockTimer.writeSummaryFromParcelLocked(parcel, jElapsedRealtime);
            } else {
                parcel.writeInt(0);
            }
            if (uidValueAt.mWifiScanTimer != null) {
                parcel.writeInt(1);
                uidValueAt.mWifiScanTimer.writeSummaryFromParcelLocked(parcel, jElapsedRealtime);
            } else {
                parcel.writeInt(0);
            }
            for (int i18 = 0; i18 < 5; i18++) {
                if (uidValueAt.mWifiBatchedScanTimer[i18] != null) {
                    parcel.writeInt(1);
                    uidValueAt.mWifiBatchedScanTimer[i18].writeSummaryFromParcelLocked(parcel, jElapsedRealtime);
                } else {
                    parcel.writeInt(0);
                }
            }
            if (uidValueAt.mWifiMulticastTimer != null) {
                parcel.writeInt(1);
                uidValueAt.mWifiMulticastTimer.writeSummaryFromParcelLocked(parcel, jElapsedRealtime);
            } else {
                parcel.writeInt(0);
            }
            if (uidValueAt.mAudioTurnedOnTimer != null) {
                parcel.writeInt(1);
                uidValueAt.mAudioTurnedOnTimer.writeSummaryFromParcelLocked(parcel, jElapsedRealtime);
            } else {
                parcel.writeInt(0);
            }
            if (uidValueAt.mVideoTurnedOnTimer != null) {
                parcel.writeInt(1);
                uidValueAt.mVideoTurnedOnTimer.writeSummaryFromParcelLocked(parcel, jElapsedRealtime);
            } else {
                parcel.writeInt(0);
            }
            if (uidValueAt.mFlashlightTurnedOnTimer != null) {
                parcel.writeInt(1);
                uidValueAt.mFlashlightTurnedOnTimer.writeSummaryFromParcelLocked(parcel, jElapsedRealtime);
            } else {
                parcel.writeInt(0);
            }
            if (uidValueAt.mCameraTurnedOnTimer != null) {
                parcel.writeInt(1);
                uidValueAt.mCameraTurnedOnTimer.writeSummaryFromParcelLocked(parcel, jElapsedRealtime);
            } else {
                parcel.writeInt(0);
            }
            if (uidValueAt.mForegroundActivityTimer != null) {
                parcel.writeInt(1);
                uidValueAt.mForegroundActivityTimer.writeSummaryFromParcelLocked(parcel, jElapsedRealtime);
            } else {
                parcel.writeInt(0);
            }
            if (uidValueAt.mForegroundServiceTimer != null) {
                parcel.writeInt(1);
                uidValueAt.mForegroundServiceTimer.writeSummaryFromParcelLocked(parcel, jElapsedRealtime);
            } else {
                parcel.writeInt(0);
            }
            if (uidValueAt.mAggregatedPartialWakelockTimer != null) {
                parcel.writeInt(1);
                uidValueAt.mAggregatedPartialWakelockTimer.writeSummaryFromParcelLocked(parcel, jElapsedRealtime);
            } else {
                parcel.writeInt(0);
            }
            if (uidValueAt.mBluetoothScanTimer != null) {
                parcel.writeInt(1);
                uidValueAt.mBluetoothScanTimer.writeSummaryFromParcelLocked(parcel, jElapsedRealtime);
            } else {
                parcel.writeInt(0);
            }
            if (uidValueAt.mBluetoothUnoptimizedScanTimer != null) {
                parcel.writeInt(1);
                uidValueAt.mBluetoothUnoptimizedScanTimer.writeSummaryFromParcelLocked(parcel, jElapsedRealtime);
            } else {
                parcel.writeInt(0);
            }
            if (uidValueAt.mBluetoothScanResultCounter != null) {
                parcel.writeInt(1);
                uidValueAt.mBluetoothScanResultCounter.writeSummaryFromParcelLocked(parcel);
            } else {
                parcel.writeInt(0);
            }
            if (uidValueAt.mBluetoothScanResultBgCounter != null) {
                parcel.writeInt(1);
                uidValueAt.mBluetoothScanResultBgCounter.writeSummaryFromParcelLocked(parcel);
            } else {
                parcel.writeInt(0);
            }
            for (int i19 = 0; i19 < 7; i19++) {
                if (uidValueAt.mProcessStateTimer[i19] != null) {
                    parcel.writeInt(1);
                    uidValueAt.mProcessStateTimer[i19].writeSummaryFromParcelLocked(parcel, jElapsedRealtime);
                } else {
                    parcel.writeInt(0);
                }
            }
            if (uidValueAt.mVibratorOnTimer != null) {
                parcel.writeInt(1);
                uidValueAt.mVibratorOnTimer.writeSummaryFromParcelLocked(parcel, jElapsedRealtime);
                i = 0;
            } else {
                i = 0;
                parcel.writeInt(0);
            }
            if (uidValueAt.mUserActivityCounters == null) {
                parcel.writeInt(i);
            } else {
                parcel.writeInt(1);
                for (int i20 = 0; i20 < com.android.server.power.stats.BatteryStatsImpl.Uid.NUM_USER_ACTIVITY_TYPES; i20++) {
                    uidValueAt.mUserActivityCounters[i20].writeSummaryFromParcelLocked(parcel);
                }
            }
            if (uidValueAt.mNetworkByteActivityCounters == null) {
                parcel.writeInt(0);
            } else {
                parcel.writeInt(1);
                for (int i21 = 0; i21 < 10; i21++) {
                    uidValueAt.mNetworkByteActivityCounters[i21].writeSummaryFromParcelLocked(parcel);
                    uidValueAt.mNetworkPacketActivityCounters[i21].writeSummaryFromParcelLocked(parcel);
                }
                if (uidValueAt.mMobileRadioActiveTime != null) {
                    parcel.writeBoolean(true);
                    uidValueAt.mMobileRadioActiveTime.writeToParcel(parcel);
                } else {
                    parcel.writeBoolean(false);
                }
                uidValueAt.mMobileRadioActiveCount.writeSummaryFromParcelLocked(parcel);
            }
            uidValueAt.mUserCpuTime.writeSummaryFromParcelLocked(parcel);
            uidValueAt.mSystemCpuTime.writeSummaryFromParcelLocked(parcel);
            if (uidValueAt.mCpuClusterSpeedTimesUs != null) {
                parcel.writeInt(1);
                parcel.writeInt(uidValueAt.mCpuClusterSpeedTimesUs.length);
                com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounter[][] longSamplingCounterArr3 = uidValueAt.mCpuClusterSpeedTimesUs;
                int length3 = longSamplingCounterArr3.length;
                int i22 = 0;
                while (i22 < length3) {
                    com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounter[] longSamplingCounterArr4 = longSamplingCounterArr3[i22];
                    if (longSamplingCounterArr4 != null) {
                        parcel.writeInt(1);
                        parcel.writeInt(longSamplingCounterArr4.length);
                        int length4 = longSamplingCounterArr4.length;
                        int i23 = 0;
                        while (i23 < length4) {
                            com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounter longSamplingCounter = longSamplingCounterArr4[i23];
                            if (longSamplingCounter != null) {
                                longSamplingCounterArr2 = longSamplingCounterArr3;
                                parcel.writeInt(1);
                                longSamplingCounter.writeSummaryFromParcelLocked(parcel);
                            } else {
                                longSamplingCounterArr2 = longSamplingCounterArr3;
                                parcel.writeInt(0);
                            }
                            i23++;
                            longSamplingCounterArr3 = longSamplingCounterArr2;
                        }
                        longSamplingCounterArr = longSamplingCounterArr3;
                    } else {
                        longSamplingCounterArr = longSamplingCounterArr3;
                        parcel.writeInt(0);
                    }
                    i22++;
                    longSamplingCounterArr3 = longSamplingCounterArr;
                }
            } else {
                parcel.writeInt(0);
            }
            com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounterArray.writeSummaryToParcelLocked(parcel, uidValueAt.mCpuFreqTimeMs);
            com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounterArray.writeSummaryToParcelLocked(parcel, uidValueAt.mScreenOffCpuFreqTimeMs);
            if (uidValueAt.mCpuActiveTimeMs != null) {
                parcel.writeInt(uidValueAt.mCpuActiveTimeMs.getStateCount());
                uidValueAt.mCpuActiveTimeMs.writeToParcel(parcel);
            } else {
                parcel.writeInt(0);
            }
            uidValueAt.mCpuClusterTimesMs.writeSummaryToParcelLocked(parcel);
            if (uidValueAt.mProcStateTimeMs != null) {
                parcel.writeInt(uidValueAt.mProcStateTimeMs.getStateCount());
                uidValueAt.mProcStateTimeMs.writeToParcel(parcel);
            } else {
                parcel.writeInt(0);
            }
            if (uidValueAt.mProcStateScreenOffTimeMs != null) {
                parcel.writeInt(uidValueAt.mProcStateScreenOffTimeMs.getStateCount());
                uidValueAt.mProcStateScreenOffTimeMs.writeToParcel(parcel);
            } else {
                parcel.writeInt(0);
            }
            if (uidValueAt.mMobileRadioApWakeupCount != null) {
                parcel.writeInt(1);
                uidValueAt.mMobileRadioApWakeupCount.writeSummaryFromParcelLocked(parcel);
            } else {
                parcel.writeInt(0);
            }
            if (uidValueAt.mWifiRadioApWakeupCount != null) {
                parcel.writeInt(1);
                uidValueAt.mWifiRadioApWakeupCount.writeSummaryFromParcelLocked(parcel);
            } else {
                parcel.writeInt(0);
            }
            com.android.internal.power.EnergyConsumerStats.writeSummaryToParcel(uidValueAt.mUidEnergyConsumerStats, parcel);
            android.util.ArrayMap<java.lang.String, com.android.server.power.stats.BatteryStatsImpl.Uid.Wakelock> map = uidValueAt.mWakelockStats.getMap();
            int size3 = map.size();
            parcel.writeInt(size3);
            for (int i24 = 0; i24 < size3; i24++) {
                parcel.writeString(map.keyAt(i24));
                com.android.server.power.stats.BatteryStatsImpl.Uid.Wakelock wakelockValueAt = map.valueAt(i24);
                if (wakelockValueAt.mTimerFull != null) {
                    parcel.writeInt(1);
                    wakelockValueAt.mTimerFull.writeSummaryFromParcelLocked(parcel, jElapsedRealtime);
                } else {
                    parcel.writeInt(0);
                }
                if (wakelockValueAt.mTimerPartial != null) {
                    parcel.writeInt(1);
                    wakelockValueAt.mTimerPartial.writeSummaryFromParcelLocked(parcel, jElapsedRealtime);
                } else {
                    parcel.writeInt(0);
                }
                if (wakelockValueAt.mTimerWindow != null) {
                    parcel.writeInt(1);
                    wakelockValueAt.mTimerWindow.writeSummaryFromParcelLocked(parcel, jElapsedRealtime);
                } else {
                    parcel.writeInt(0);
                }
                if (wakelockValueAt.mTimerDraw != null) {
                    parcel.writeInt(1);
                    wakelockValueAt.mTimerDraw.writeSummaryFromParcelLocked(parcel, jElapsedRealtime);
                } else {
                    parcel.writeInt(0);
                }
            }
            android.util.ArrayMap<java.lang.String, com.android.server.power.stats.BatteryStatsImpl.DualTimer> map2 = uidValueAt.mSyncStats.getMap();
            int size4 = map2.size();
            parcel.writeInt(size4);
            for (int i25 = 0; i25 < size4; i25++) {
                parcel.writeString(map2.keyAt(i25));
                map2.valueAt(i25).writeSummaryFromParcelLocked(parcel, jElapsedRealtime);
            }
            android.util.ArrayMap<java.lang.String, com.android.server.power.stats.BatteryStatsImpl.DualTimer> map3 = uidValueAt.mJobStats.getMap();
            int size5 = map3.size();
            parcel.writeInt(size5);
            int i26 = 0;
            while (i26 < size5) {
                parcel.writeString(map3.keyAt(i26));
                map3.valueAt(i26).writeSummaryFromParcelLocked(parcel, jElapsedRealtime);
                i26++;
                map = map;
            }
            uidValueAt.writeJobCompletionsToParcelLocked(parcel);
            uidValueAt.mJobsDeferredEventCount.writeSummaryFromParcelLocked(parcel);
            uidValueAt.mJobsDeferredCount.writeSummaryFromParcelLocked(parcel);
            uidValueAt.mJobsFreshnessTimeMs.writeSummaryFromParcelLocked(parcel);
            for (int i27 = 0; i27 < JOB_FRESHNESS_BUCKETS.length; i27++) {
                if (uidValueAt.mJobsFreshnessBuckets[i27] != null) {
                    parcel.writeInt(1);
                    uidValueAt.mJobsFreshnessBuckets[i27].writeSummaryFromParcelLocked(parcel);
                } else {
                    parcel.writeInt(0);
                }
            }
            int size6 = uidValueAt.mSensorStats.size();
            parcel.writeInt(size6);
            int i28 = 0;
            while (i28 < size6) {
                int i29 = size6;
                parcel.writeInt(uidValueAt.mSensorStats.keyAt(i28));
                com.android.server.power.stats.BatteryStatsImpl.Uid.Sensor sensorValueAt = uidValueAt.mSensorStats.valueAt(i28);
                int i30 = size3;
                if (sensorValueAt.mTimer != null) {
                    parcel.writeInt(1);
                    sensorValueAt.mTimer.writeSummaryFromParcelLocked(parcel, jElapsedRealtime);
                } else {
                    parcel.writeInt(0);
                }
                i28++;
                size6 = i29;
                size3 = i30;
            }
            int size7 = uidValueAt.mProcessStats.size();
            parcel.writeInt(size7);
            int i31 = 0;
            while (i31 < size7) {
                parcel.writeString(uidValueAt.mProcessStats.keyAt(i31));
                com.android.server.power.stats.BatteryStatsImpl.Uid.Proc procValueAt = uidValueAt.mProcessStats.valueAt(i31);
                parcel.writeLong(procValueAt.mUserTimeMs);
                parcel.writeLong(procValueAt.mSystemTimeMs);
                parcel.writeLong(procValueAt.mForegroundTimeMs);
                parcel.writeInt(procValueAt.mStarts);
                parcel.writeInt(procValueAt.mNumCrashes);
                parcel.writeInt(procValueAt.mNumAnrs);
                procValueAt.writeExcessivePowerToParcelLocked(parcel);
                i31++;
                map2 = map2;
                size4 = size4;
            }
            int size8 = uidValueAt.mPackageStats.size();
            parcel.writeInt(size8);
            if (size8 > 0) {
                java.util.Iterator<java.util.Map.Entry<java.lang.String, com.android.server.power.stats.BatteryStatsImpl.Uid.Pkg>> it = uidValueAt.mPackageStats.entrySet().iterator();
                while (it.hasNext()) {
                    java.util.Map.Entry<java.lang.String, com.android.server.power.stats.BatteryStatsImpl.Uid.Pkg> next = it.next();
                    parcel.writeString(next.getKey());
                    com.android.server.power.stats.BatteryStatsImpl.Uid.Pkg value5 = next.getValue();
                    int i32 = size8;
                    int size9 = value5.mWakeupAlarms.size();
                    parcel.writeInt(size9);
                    java.util.Iterator<java.util.Map.Entry<java.lang.String, com.android.server.power.stats.BatteryStatsImpl.Uid.Pkg>> it2 = it;
                    int i33 = 0;
                    while (i33 < size9) {
                        parcel.writeString(value5.mWakeupAlarms.keyAt(i33));
                        value5.mWakeupAlarms.valueAt(i33).writeSummaryFromParcelLocked(parcel);
                        i33++;
                        size9 = size9;
                    }
                    int size10 = value5.mServiceStats.size();
                    parcel.writeInt(size10);
                    int i34 = 0;
                    while (i34 < size10) {
                        parcel.writeString(value5.mServiceStats.keyAt(i34));
                        com.android.server.power.stats.BatteryStatsImpl.Uid.Pkg.Serv servValueAt = value5.mServiceStats.valueAt(i34);
                        parcel.writeLong(servValueAt.getStartTimeToNowLocked(this.mOnBatteryTimeBase.getUptime(jUptimeMillis) / 1000));
                        parcel.writeInt(servValueAt.mStarts);
                        parcel.writeInt(servValueAt.mLaunches);
                        i34++;
                        next = next;
                        size10 = size10;
                    }
                    size8 = i32;
                    it = it2;
                }
            }
            i14++;
            length2 = i15;
            length = i16;
            size2 = i17;
            z2 = true;
        }
        if (!com.android.internal.hidden_from_bootclasspath.com.android.server.power.optimization.Flags.disableSystemServicePowerAttr()) {
            com.android.server.power.stats.BatteryStatsImpl.LongSamplingCounterArray.writeSummaryToParcelLocked(parcel, this.mBinderThreadCpuTimesUs);
        }
    }

    public void prepareForDumpLocked() {
        pullPendingStateUpdatesLocked();
        getStartClockTime();
        if (!com.android.internal.hidden_from_bootclasspath.com.android.server.power.optimization.Flags.disableSystemServicePowerAttr()) {
            updateSystemServiceCallStats();
        }
    }

    public void dump(android.content.Context context, java.io.PrintWriter pw, int flags, int reqUid, long histStart, android.os.BatteryStats.BatteryStatsDumpHelper dumpHelper) {
        super.dump(context, pw, flags, reqUid, histStart, dumpHelper);
        synchronized (this) {
            pw.print("Per process state tracking available: ");
            pw.println(trackPerProcStateCpuTimes());
            pw.print("Total cpu time reads: ");
            pw.println(this.mNumSingleUidCpuTimeReads);
            pw.print("Batching Duration (min): ");
            pw.println((this.mClock.uptimeMillis() - this.mCpuTimeReadsTrackingStartTimeMs) / 60000);
            pw.print("All UID cpu time reads since the later of device start or stats reset: ");
            pw.println(this.mNumAllUidCpuTimeReads);
            pw.print("UIDs removed since the later of device start or stats reset: ");
            pw.println(this.mNumUidsRemoved);
            this.mPowerStatsUidResolver.dump(pw);
            pw.println();
            dumpConstantsLocked(pw);
            pw.println();
            this.mCpuPowerStatsCollector.dumpCpuPowerBracketsLocked(pw);
            pw.println();
            dumpEnergyConsumerStatsLocked(pw);
        }
    }

    public com.android.server.power.stats.IBatteryStatsImplWrapper getWrapper() {
        return this.mBsiWrapper;
    }

    private class BatteryStatsImplWrapper implements com.android.server.power.stats.IBatteryStatsImplWrapper {
        private BatteryStatsImplWrapper() {
        }

        @Override // com.android.server.power.stats.IBatteryStatsImplWrapper
        public com.android.server.power.stats.BatteryStatsImpl.BatteryCallback getBatteryCallback() {
            return com.android.server.power.stats.BatteryStatsImpl.this.mCallback;
        }
    }
}
