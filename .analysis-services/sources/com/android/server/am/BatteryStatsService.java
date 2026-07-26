package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public final class BatteryStatsService extends com.android.internal.app.IBatteryStats.Stub implements android.os.PowerManagerInternal.LowPowerModeListener, com.android.server.power.stats.BatteryStatsImpl.PlatformIdleStateCallback, com.android.server.power.stats.BatteryStatsImpl.EnergyStatsRetriever, com.android.server.Watchdog.Monitor {
    private static final java.lang.String BATTERY_USAGE_STATS_BEFORE_RESET_TIMESTAMP_PROPERTY = "BATTERY_USAGE_STATS_BEFORE_RESET_TIMESTAMP";
    static final boolean DBG = false;
    private static final java.lang.String DEVICE_CONFIG_NAMESPACE = "backstage_power";
    private static final java.lang.String EMPTY = "Empty";
    private static final int MAX_LOW_POWER_STATS_SIZE = 32768;
    private static final java.lang.String MIN_CONSUMED_POWER_THRESHOLD_KEY = "min_consumed_power_threshold";
    private static final int POWER_STATS_QUERY_TIMEOUT_MILLIS = 2000;
    static final java.lang.String TAG = "BatteryStatsService";
    static final java.lang.String TRACE_TRACK_WAKEUP_REASON = "wakeup_reason";
    private static com.android.internal.app.IBatteryStats sService;
    private final com.android.server.power.stats.AggregatedPowerStatsConfig mAggregatedPowerStatsConfig;
    private android.os.BatteryManagerInternal mBatteryManagerInternal;
    private final com.android.server.power.stats.BatteryStatsImpl.BatteryStatsConfig mBatteryStatsConfig;
    private final com.android.server.power.stats.BatteryUsageStatsProvider mBatteryUsageStatsProvider;
    private final android.util.AtomicFile mConfigFile;
    private final android.content.Context mContext;
    private final com.android.internal.os.CpuScalingPolicies mCpuScalingPolicies;
    final com.android.server.power.stats.wakeups.CpuWakeupStats mCpuWakeupStats;
    private final android.os.BatteryStats.BatteryStatsDumpHelper mDumpHelper;
    private final android.os.Handler mHandler;
    private final com.android.internal.os.MonotonicClock mMonotonicClock;
    private final com.android.internal.os.PowerProfile mPowerProfile;
    private final com.android.server.power.stats.PowerStatsScheduler mPowerStatsScheduler;
    private final com.android.server.power.stats.PowerStatsStore mPowerStatsStore;
    final com.android.server.power.stats.BatteryStatsImpl mStats;
    private final com.android.server.power.stats.BatteryExternalStatsWorker mWorker;
    private final com.android.server.power.stats.PowerStatsUidResolver mPowerStatsUidResolver = new com.android.server.power.stats.PowerStatsUidResolver();
    private volatile boolean mMonitorEnabled = true;
    private java.nio.charset.CharsetDecoder mDecoderStat = java.nio.charset.StandardCharsets.UTF_8.newDecoder().onMalformedInput(java.nio.charset.CodingErrorAction.REPLACE).onUnmappableCharacter(java.nio.charset.CodingErrorAction.REPLACE).replaceWith("?");
    private final java.lang.Object mLock = new java.lang.Object();
    private final java.lang.Object mPowerStatsLock = new java.lang.Object();
    private android.power.PowerStatsInternal mPowerStatsInternal = null;
    private java.util.Map<java.lang.Integer, java.lang.String> mEntityNames = new java.util.HashMap();
    private java.util.Map<java.lang.Integer, java.util.Map<java.lang.Integer, java.lang.String>> mStateNames = new java.util.HashMap();
    private int mLastPowerStateFromRadio = 1;
    private int mLastPowerStateFromWifi = 1;
    private final android.net.INetworkManagementEventObserver mActivityChangeObserver = new com.android.server.net.BaseNetworkObserver() { // from class: com.android.server.am.BatteryStatsService.1
        public void interfaceClassDataActivityChanged(int transportType, boolean active, long tsNanos, int uid) throws java.lang.Throwable {
            int powerState;
            long timestampNanos;
            if (active) {
                powerState = 3;
            } else {
                powerState = 1;
            }
            if (tsNanos <= 0) {
                timestampNanos = android.os.SystemClock.elapsedRealtimeNanos();
            } else {
                timestampNanos = tsNanos;
            }
            switch (transportType) {
                case 0:
                    com.android.server.am.BatteryStatsService.this.noteMobileRadioPowerState(powerState, timestampNanos, uid);
                    break;
                case 1:
                    com.android.server.am.BatteryStatsService.this.noteWifiRadioPowerState(powerState, timestampNanos, uid);
                    break;
                default:
                    android.util.Slog.d(com.android.server.am.BatteryStatsService.TAG, "Received unexpected transport in interfaceClassDataActivityChanged unexpected type: " + transportType);
                    break;
            }
        }
    };
    private android.net.ConnectivityManager.NetworkCallback mNetworkCallback = new android.net.ConnectivityManager.NetworkCallback() { // from class: com.android.server.am.BatteryStatsService.2
        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onCapabilitiesChanged(android.net.Network network, android.net.NetworkCapabilities networkCapabilities) {
            java.lang.String state = networkCapabilities.hasCapability(21) ? "CONNECTED" : kotlinx.coroutines.debug.internal.DebugCoroutineInfoImplKt.SUSPENDED;
            com.android.server.am.BatteryStatsService.this.noteConnectivityChanged(com.android.net.module.util.NetworkCapabilitiesUtils.getDisplayTransport(networkCapabilities.getTransportTypes()), state);
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onLost(android.net.Network network) {
            com.android.server.am.BatteryStatsService.this.noteConnectivityChanged(-1, "DISCONNECTED");
        }
    };
    private com.android.server.am.BatteryStatsService.BatteryStatsServiceWrapper mBssWrapper = new com.android.server.am.BatteryStatsService.BatteryStatsServiceWrapper();
    public com.android.server.am.IBatteryStatsServiceExt mBssExt = (com.android.server.am.IBatteryStatsServiceExt) system.ext.loader.core.ExtLoader.type(com.android.server.am.IBatteryStatsServiceExt.class).base(this).create();
    private final com.android.server.power.stats.BatteryStatsImpl.UserInfoProvider mUserManagerUserInfoProvider = new com.android.server.power.stats.BatteryStatsImpl.UserInfoProvider() { // from class: com.android.server.am.BatteryStatsService.3
        private com.android.server.pm.UserManagerInternal umi;

        @Override // com.android.server.power.stats.BatteryStatsImpl.UserInfoProvider
        public int[] getUserIds() {
            if (this.umi == null) {
                this.umi = (com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class);
            }
            if (this.umi != null) {
                return this.umi.getUserIds();
            }
            return null;
        }
    };
    private final android.os.HandlerThread mHandlerThread = new android.os.HandlerThread("batterystats-handler");

    private native void getRailEnergyPowerStats(com.android.internal.os.RailStats railStats);

    /* JADX INFO: Access modifiers changed from: private */
    public static native int nativeWaitWakeup(java.nio.ByteBuffer byteBuffer);

    private void populatePowerEntityMaps() {
        android.hardware.power.stats.PowerEntity[] entities = this.mPowerStatsInternal.getPowerEntityInfo();
        if (entities == null) {
            return;
        }
        for (android.hardware.power.stats.PowerEntity entity : entities) {
            java.util.Map<java.lang.Integer, java.lang.String> states = new java.util.HashMap<>();
            for (int j = 0; j < entity.states.length; j++) {
                android.hardware.power.stats.State state = entity.states[j];
                states.put(java.lang.Integer.valueOf(state.id), state.name);
            }
            this.mEntityNames.put(java.lang.Integer.valueOf(entity.id), entity.name);
            this.mStateNames.put(java.lang.Integer.valueOf(entity.id), states);
        }
    }

    @Override // com.android.server.power.stats.BatteryStatsImpl.PlatformIdleStateCallback
    public void fillLowPowerStats(com.android.internal.os.RpmStats rpmStats) {
        synchronized (this.mPowerStatsLock) {
            if (this.mPowerStatsInternal != null && !this.mEntityNames.isEmpty() && !this.mStateNames.isEmpty()) {
                try {
                    android.hardware.power.stats.StateResidencyResult[] results = this.mPowerStatsInternal.getStateResidencyAsync(new int[0]).get(2000L, java.util.concurrent.TimeUnit.MILLISECONDS);
                    if (results == null) {
                        return;
                    }
                    for (android.hardware.power.stats.StateResidencyResult result : results) {
                        com.android.internal.os.RpmStats.PowerStateSubsystem subsystem = rpmStats.getSubsystem(this.mEntityNames.get(java.lang.Integer.valueOf(result.id)));
                        for (int j = 0; j < result.stateResidencyData.length; j++) {
                            android.hardware.power.stats.StateResidency stateResidency = result.stateResidencyData[j];
                            subsystem.putState(this.mStateNames.get(java.lang.Integer.valueOf(result.id)).get(java.lang.Integer.valueOf(stateResidency.id)), stateResidency.totalTimeInStateMs, (int) stateResidency.totalStateEntryCount);
                        }
                    }
                } catch (java.lang.Exception e) {
                    android.util.Slog.e(TAG, "Failed to getStateResidencyAsync", e);
                }
            }
        }
    }

    @Override // com.android.server.power.stats.BatteryStatsImpl.EnergyStatsRetriever
    public void fillRailDataStats(com.android.internal.os.RailStats railStats) {
        getRailEnergyPowerStats(railStats);
    }

    @Override // com.android.server.power.stats.BatteryStatsImpl.PlatformIdleStateCallback
    public java.lang.String getSubsystemLowPowerStats() {
        synchronized (this.mPowerStatsLock) {
            if (this.mPowerStatsInternal != null && !this.mEntityNames.isEmpty() && !this.mStateNames.isEmpty()) {
                try {
                    android.hardware.power.stats.StateResidencyResult[] results = this.mPowerStatsInternal.getStateResidencyAsync(new int[0]).get(2000L, java.util.concurrent.TimeUnit.MILLISECONDS);
                    if (results == null || results.length == 0) {
                        return EMPTY;
                    }
                    int charsLeft = 32768;
                    java.lang.StringBuilder builder = new java.lang.StringBuilder("SubsystemPowerState");
                    int i = 0;
                    while (true) {
                        if (i >= results.length) {
                            break;
                        }
                        android.hardware.power.stats.StateResidencyResult result = results[i];
                        java.lang.StringBuilder subsystemBuilder = new java.lang.StringBuilder();
                        subsystemBuilder.append(" subsystem_" + i);
                        subsystemBuilder.append(" name=" + this.mEntityNames.get(java.lang.Integer.valueOf(result.id)));
                        for (int j = 0; j < result.stateResidencyData.length; j++) {
                            android.hardware.power.stats.StateResidency stateResidency = result.stateResidencyData[j];
                            subsystemBuilder.append(" state_" + j);
                            subsystemBuilder.append(" name=" + this.mStateNames.get(java.lang.Integer.valueOf(result.id)).get(java.lang.Integer.valueOf(stateResidency.id)));
                            subsystemBuilder.append(" time=" + stateResidency.totalTimeInStateMs);
                            subsystemBuilder.append(" count=" + stateResidency.totalStateEntryCount);
                            subsystemBuilder.append(" last entry=" + stateResidency.lastEntryTimestampMs);
                        }
                        int j2 = subsystemBuilder.length();
                        if (j2 <= charsLeft) {
                            charsLeft -= subsystemBuilder.length();
                            builder.append((java.lang.CharSequence) subsystemBuilder);
                            i++;
                        } else {
                            android.util.Slog.e(TAG, "getSubsystemLowPowerStats: buffer not enough");
                            break;
                        }
                    }
                    return builder.toString();
                } catch (java.lang.Exception e) {
                    android.util.Slog.e(TAG, "Failed to getStateResidencyAsync", e);
                    return EMPTY;
                }
            }
            return EMPTY;
        }
    }

    BatteryStatsService(android.content.Context context, java.io.File systemDir) {
        this.mContext = context;
        this.mHandlerThread.start();
        this.mHandler = new android.os.Handler(this.mHandlerThread.getLooper());
        this.mMonotonicClock = new com.android.internal.os.MonotonicClock(new java.io.File(systemDir, "monotonic_clock.xml"));
        this.mPowerProfile = new com.android.internal.os.PowerProfile(context);
        this.mCpuScalingPolicies = new com.android.internal.os.CpuScalingPolicyReader().read();
        boolean resetOnUnplugHighBatteryLevel = context.getResources().getBoolean(android.R.bool.config_batterySaver_full_forceBackgroundCheck);
        boolean resetOnUnplugAfterSignificantCharge = context.getResources().getBoolean(android.R.bool.config_batterySaver_full_forceAllAppsStandby);
        com.android.server.power.stats.BatteryStatsImpl.BatteryStatsConfig.Builder batteryStatsConfigBuilder = new com.android.server.power.stats.BatteryStatsImpl.BatteryStatsConfig.Builder().setResetOnUnplugHighBatteryLevel(resetOnUnplugHighBatteryLevel).setResetOnUnplugAfterSignificantCharge(resetOnUnplugAfterSignificantCharge);
        setPowerStatsThrottlePeriods(batteryStatsConfigBuilder, context.getResources().getString(android.R.string.config_rawContactsLocalAccountType));
        this.mBatteryStatsConfig = batteryStatsConfigBuilder.build();
        this.mStats = new com.android.server.power.stats.BatteryStatsImpl(this.mBatteryStatsConfig, com.android.internal.os.Clock.SYSTEM_CLOCK, this.mMonotonicClock, systemDir, this.mHandler, this, this, this.mUserManagerUserInfoProvider, this.mPowerProfile, this.mCpuScalingPolicies, this.mPowerStatsUidResolver);
        this.mWorker = new com.android.server.power.stats.BatteryExternalStatsWorker(context, this.mStats);
        this.mStats.setExternalStatsSyncLocked(this.mWorker);
        this.mStats.setRadioScanningTimeoutLocked(((long) this.mContext.getResources().getInteger(android.R.integer.config_ntpRetry)) * 1000);
        if (!com.android.internal.hidden_from_bootclasspath.com.android.server.power.optimization.Flags.disableSystemServicePowerAttr()) {
            this.mStats.startTrackingSystemServerCpuTime();
        }
        this.mAggregatedPowerStatsConfig = createAggregatedPowerStatsConfig();
        this.mPowerStatsStore = new com.android.server.power.stats.PowerStatsStore(systemDir, this.mHandler, this.mAggregatedPowerStatsConfig);
        this.mPowerStatsScheduler = createPowerStatsScheduler(this.mContext);
        com.android.server.power.stats.PowerStatsExporter powerStatsExporter = new com.android.server.power.stats.PowerStatsExporter(this.mPowerStatsStore, new com.android.server.power.stats.PowerStatsAggregator(this.mAggregatedPowerStatsConfig, this.mStats.getHistory()));
        this.mBatteryUsageStatsProvider = new com.android.server.power.stats.BatteryUsageStatsProvider(context, powerStatsExporter, this.mPowerProfile, this.mCpuScalingPolicies, this.mPowerStatsStore, com.android.internal.os.Clock.SYSTEM_CLOCK);
        this.mStats.saveBatteryUsageStatsOnReset(this.mBatteryUsageStatsProvider, this.mPowerStatsStore);
        this.mDumpHelper = new com.android.server.power.stats.BatteryStatsDumpHelperImpl(this.mBatteryUsageStatsProvider);
        this.mCpuWakeupStats = new com.android.server.power.stats.wakeups.CpuWakeupStats(context, android.R.xml.haptic_feedback_customization, this.mHandler);
        this.mConfigFile = new android.util.AtomicFile(new java.io.File(systemDir, "battery_usage_stats_config"));
        this.mBssExt.init(context);
    }

    private com.android.server.power.stats.PowerStatsScheduler createPowerStatsScheduler(android.content.Context context) {
        long aggregatedPowerStatsSpanDuration = context.getResources().getInteger(android.R.integer.config_accessibilityColorMode);
        long powerStatsAggregationPeriod = context.getResources().getInteger(android.R.integer.config_notificationsBatteryMediumARGB);
        com.android.server.power.stats.PowerStatsScheduler.AlarmScheduler alarmScheduler = new com.android.server.power.stats.PowerStatsScheduler.AlarmScheduler() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda14
            @Override // com.android.server.power.stats.PowerStatsScheduler.AlarmScheduler
            public final void scheduleAlarm(long j, java.lang.String str, android.app.AlarmManager.OnAlarmListener onAlarmListener, android.os.Handler handler) {
                this.f$0.lambda$createPowerStatsScheduler$0(j, str, onAlarmListener, handler);
            }
        };
        final com.android.server.power.stats.BatteryStatsImpl batteryStatsImpl = this.mStats;
        java.util.Objects.requireNonNull(batteryStatsImpl);
        return new com.android.server.power.stats.PowerStatsScheduler(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda15
            @Override // java.lang.Runnable
            public final void run() {
                batteryStatsImpl.schedulePowerStatsSampleCollection();
            }
        }, new com.android.server.power.stats.PowerStatsAggregator(this.mAggregatedPowerStatsConfig, this.mStats.getHistory()), aggregatedPowerStatsSpanDuration, powerStatsAggregationPeriod, this.mPowerStatsStore, alarmScheduler, com.android.internal.os.Clock.SYSTEM_CLOCK, this.mMonotonicClock, new java.util.function.Supplier() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda16
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return this.f$0.lambda$createPowerStatsScheduler$1();
            }
        }, this.mHandler);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createPowerStatsScheduler$0(long triggerAtMillis, java.lang.String tag, android.app.AlarmManager.OnAlarmListener onAlarmListener, android.os.Handler aHandler) {
        android.app.AlarmManager alarmManager = (android.app.AlarmManager) this.mContext.getSystemService(android.app.AlarmManager.class);
        alarmManager.set(3, triggerAtMillis, tag, onAlarmListener, aHandler);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.lang.Long lambda$createPowerStatsScheduler$1() {
        return java.lang.Long.valueOf(this.mStats.getHistory().getStartTime());
    }

    private com.android.server.power.stats.AggregatedPowerStatsConfig createAggregatedPowerStatsConfig() {
        com.android.server.power.stats.AggregatedPowerStatsConfig config = new com.android.server.power.stats.AggregatedPowerStatsConfig();
        config.trackPowerComponent(1).trackDeviceStates(0, 1).trackUidStates(0, 1, 2).setProcessor(new com.android.server.power.stats.CpuPowerStatsProcessor(this.mPowerProfile, this.mCpuScalingPolicies));
        config.trackPowerComponent(8).trackDeviceStates(0, 1).trackUidStates(0, 1, 2).setProcessor(new com.android.server.power.stats.MobileRadioPowerStatsProcessor(this.mPowerProfile));
        config.trackPowerComponent(14, 8).setProcessor(new com.android.server.power.stats.PhoneCallPowerStatsProcessor());
        config.trackPowerComponent(11).trackDeviceStates(0, 1).trackUidStates(0, 1, 2).setProcessor(new com.android.server.power.stats.WifiPowerStatsProcessor(this.mPowerProfile));
        config.trackPowerComponent(2).trackDeviceStates(0, 1).trackUidStates(0, 1, 2).setProcessor(new com.android.server.power.stats.BluetoothPowerStatsProcessor(this.mPowerProfile));
        config.trackPowerComponent(4).trackDeviceStates(0, 1).trackUidStates(0, 1, 2).setProcessor(new com.android.server.power.stats.AudioPowerStatsProcessor(this.mPowerProfile, this.mPowerStatsUidResolver));
        config.trackPowerComponent(5).trackDeviceStates(0, 1).trackUidStates(0, 1, 2).setProcessor(new com.android.server.power.stats.VideoPowerStatsProcessor(this.mPowerProfile, this.mPowerStatsUidResolver));
        config.trackPowerComponent(6).trackDeviceStates(0, 1).trackUidStates(0, 1, 2).setProcessor(new com.android.server.power.stats.FlashlightPowerStatsProcessor(this.mPowerProfile, this.mPowerStatsUidResolver));
        config.trackPowerComponent(3).trackDeviceStates(0, 1).trackUidStates(0, 1, 2).setProcessor(new com.android.server.power.stats.CameraPowerStatsProcessor(this.mPowerProfile, this.mPowerStatsUidResolver));
        config.trackPowerComponent(10).trackDeviceStates(0, 1).trackUidStates(0, 1, 2).setProcessor(new com.android.server.power.stats.GnssPowerStatsProcessor(this.mPowerProfile, this.mPowerStatsUidResolver));
        return config;
    }

    private void setPowerStatsThrottlePeriods(com.android.server.power.stats.BatteryStatsImpl.BatteryStatsConfig.Builder builder, java.lang.String configString) {
        java.util.regex.Matcher matcher = java.util.regex.Pattern.compile("([^:]+):(\\d+)\\s*").matcher(configString);
        while (matcher.find()) {
            java.lang.String powerComponentName = matcher.group(1);
            try {
                long throttlePeriod = java.lang.Long.parseLong(matcher.group(2));
                if (powerComponentName.equals(com.android.server.am.SettingsToPropertiesMapper.NAMESPACE_REBOOT_STAGING_DELIMITER)) {
                    builder.setDefaultPowerStatsThrottlePeriodMillis(throttlePeriod);
                } else {
                    builder.setPowerStatsThrottlePeriodMillis(powerComponentName, throttlePeriod);
                }
            } catch (java.lang.NumberFormatException e) {
                throw new java.lang.IllegalArgumentException("Invalid config_powerStatsThrottlePeriods format: " + configString);
            }
        }
    }

    public static com.android.server.am.BatteryStatsService create(android.content.Context context, java.io.File systemDir, android.os.Handler handler, com.android.server.power.stats.BatteryStatsImpl.BatteryCallback callback) {
        com.android.server.am.BatteryStatsService service = new com.android.server.am.BatteryStatsService(context, systemDir);
        service.mStats.setCallback(callback);
        synchronized (service.mStats) {
            service.mStats.readLocked();
        }
        service.scheduleWriteToDisk();
        return service;
    }

    public void publish() {
        com.android.server.LocalServices.addService(android.os.BatteryStatsInternal.class, new com.android.server.am.BatteryStatsService.LocalService());
        android.os.ServiceManager.addService("batterystats", asBinder());
    }

    public void systemServicesReady() {
        this.mStats.setPowerStatsCollectorEnabled(1, com.android.internal.hidden_from_bootclasspath.com.android.server.power.optimization.Flags.streamlinedBatteryStats());
        this.mBatteryUsageStatsProvider.setPowerStatsExporterEnabled(1, com.android.internal.hidden_from_bootclasspath.com.android.server.power.optimization.Flags.streamlinedBatteryStats());
        this.mStats.setPowerStatsCollectorEnabled(8, com.android.internal.hidden_from_bootclasspath.com.android.server.power.optimization.Flags.streamlinedConnectivityBatteryStats());
        this.mBatteryUsageStatsProvider.setPowerStatsExporterEnabled(8, com.android.internal.hidden_from_bootclasspath.com.android.server.power.optimization.Flags.streamlinedConnectivityBatteryStats());
        this.mStats.setPowerStatsCollectorEnabled(11, com.android.internal.hidden_from_bootclasspath.com.android.server.power.optimization.Flags.streamlinedConnectivityBatteryStats());
        this.mBatteryUsageStatsProvider.setPowerStatsExporterEnabled(11, com.android.internal.hidden_from_bootclasspath.com.android.server.power.optimization.Flags.streamlinedConnectivityBatteryStats());
        this.mStats.setPowerStatsCollectorEnabled(2, com.android.internal.hidden_from_bootclasspath.com.android.server.power.optimization.Flags.streamlinedConnectivityBatteryStats());
        this.mBatteryUsageStatsProvider.setPowerStatsExporterEnabled(2, com.android.internal.hidden_from_bootclasspath.com.android.server.power.optimization.Flags.streamlinedConnectivityBatteryStats());
        this.mStats.setPowerStatsCollectorEnabled(4, com.android.internal.hidden_from_bootclasspath.com.android.server.power.optimization.Flags.streamlinedMiscBatteryStats());
        this.mBatteryUsageStatsProvider.setPowerStatsExporterEnabled(4, com.android.internal.hidden_from_bootclasspath.com.android.server.power.optimization.Flags.streamlinedMiscBatteryStats());
        this.mStats.setPowerStatsCollectorEnabled(5, com.android.internal.hidden_from_bootclasspath.com.android.server.power.optimization.Flags.streamlinedMiscBatteryStats());
        this.mBatteryUsageStatsProvider.setPowerStatsExporterEnabled(5, com.android.internal.hidden_from_bootclasspath.com.android.server.power.optimization.Flags.streamlinedMiscBatteryStats());
        this.mStats.setPowerStatsCollectorEnabled(6, com.android.internal.hidden_from_bootclasspath.com.android.server.power.optimization.Flags.streamlinedMiscBatteryStats());
        this.mBatteryUsageStatsProvider.setPowerStatsExporterEnabled(6, com.android.internal.hidden_from_bootclasspath.com.android.server.power.optimization.Flags.streamlinedMiscBatteryStats());
        this.mStats.setPowerStatsCollectorEnabled(3, com.android.internal.hidden_from_bootclasspath.com.android.server.power.optimization.Flags.streamlinedMiscBatteryStats());
        this.mBatteryUsageStatsProvider.setPowerStatsExporterEnabled(3, com.android.internal.hidden_from_bootclasspath.com.android.server.power.optimization.Flags.streamlinedMiscBatteryStats());
        this.mWorker.systemServicesReady();
        this.mStats.systemServicesReady(this.mContext);
        this.mCpuWakeupStats.systemServicesReady();
        android.os.INetworkManagementService nms = android.os.INetworkManagementService.Stub.asInterface(android.os.ServiceManager.getService("network_management"));
        android.net.ConnectivityManager cm = (android.net.ConnectivityManager) this.mContext.getSystemService(android.net.ConnectivityManager.class);
        try {
            if (!com.android.modules.utils.build.SdkLevel.isAtLeastV()) {
                nms.registerObserver(this.mActivityChangeObserver);
            }
            cm.registerDefaultNetworkCallback(this.mNetworkCallback);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Could not register INetworkManagement event observer " + e);
        }
        synchronized (this.mPowerStatsLock) {
            this.mPowerStatsInternal = (android.power.PowerStatsInternal) com.android.server.LocalServices.getService(android.power.PowerStatsInternal.class);
            if (this.mPowerStatsInternal != null) {
                populatePowerEntityMaps();
            } else {
                android.util.Slog.e(TAG, "Could not register PowerStatsInternal");
            }
        }
        this.mBatteryManagerInternal = (android.os.BatteryManagerInternal) com.android.server.LocalServices.getService(android.os.BatteryManagerInternal.class);
        com.android.server.Watchdog.getInstance().addMonitor(this);
        com.android.server.am.DataConnectionStats dataConnectionStats = new com.android.server.am.DataConnectionStats(this.mContext, this.mHandler);
        dataConnectionStats.startMonitoring();
        this.mBssExt.systemServicesReady();
        registerStatsCallbacks();
    }

    public void onSystemReady() {
        this.mStats.onSystemReady(this.mContext);
        this.mPowerStatsScheduler.start(com.android.internal.hidden_from_bootclasspath.com.android.server.power.optimization.Flags.streamlinedBatteryStats());
    }

    private final class LocalService extends android.os.BatteryStatsInternal {
        private LocalService() {
        }

        @Override // android.os.BatteryStatsInternal
        public java.lang.String[] getWifiIfaces() {
            return (java.lang.String[]) com.android.server.am.BatteryStatsService.this.mStats.getWifiIfaces().clone();
        }

        @Override // android.os.BatteryStatsInternal
        public java.lang.String[] getMobileIfaces() {
            return (java.lang.String[]) com.android.server.am.BatteryStatsService.this.mStats.getMobileIfaces().clone();
        }

        @Override // android.os.BatteryStatsInternal
        public com.android.server.power.stats.SystemServerCpuThreadReader.SystemServiceCpuThreadTimes getSystemServiceCpuThreadTimes() {
            return com.android.server.am.BatteryStatsService.this.mStats.getSystemServiceCpuThreadTimes();
        }

        @Override // android.os.BatteryStatsInternal
        public java.util.List<android.os.BatteryUsageStats> getBatteryUsageStats(java.util.List<android.os.BatteryUsageStatsQuery> queries) {
            return com.android.server.am.BatteryStatsService.this.getBatteryUsageStats(queries);
        }

        @Override // android.os.BatteryStatsInternal
        public void noteJobsDeferred(int uid, int numDeferred, long sinceLast) {
            com.android.server.am.BatteryStatsService.this.noteJobsDeferred(uid, numDeferred, sinceLast);
        }

        private int transportToSubsystem(android.net.NetworkCapabilities nc) {
            if (nc.hasTransport(1)) {
                return 2;
            }
            if (nc.hasTransport(0)) {
                return 5;
            }
            return -1;
        }

        @Override // android.os.BatteryStatsInternal
        public void noteCpuWakingNetworkPacket(android.net.Network network, long elapsedMillis, int uid) {
            if (uid < 0) {
                android.util.Slog.e(com.android.server.am.BatteryStatsService.TAG, "Invalid uid for waking network packet: " + uid);
                return;
            }
            android.net.ConnectivityManager cm = (android.net.ConnectivityManager) com.android.server.am.BatteryStatsService.this.mContext.getSystemService(android.net.ConnectivityManager.class);
            android.net.NetworkCapabilities nc = cm.getNetworkCapabilities(network);
            int subsystem = transportToSubsystem(nc);
            if (subsystem == -1) {
                android.util.Slog.wtf(com.android.server.am.BatteryStatsService.TAG, "Could not map transport for network: " + network + " while attributing wakeup by packet sent to uid: " + uid);
            } else {
                com.android.server.am.BatteryStatsService.this.noteCpuWakingActivity(subsystem, elapsedMillis, uid);
            }
        }

        @Override // android.os.BatteryStatsInternal
        public void noteBinderCallStats(int workSourceUid, long incrementatCallCount, java.util.Collection<com.android.internal.os.BinderCallsStats.CallStat> callStats) {
            synchronized (com.android.server.am.BatteryStatsService.this.mLock) {
                android.os.Handler handler = com.android.server.am.BatteryStatsService.this.mHandler;
                final com.android.server.power.stats.BatteryStatsImpl batteryStatsImpl = com.android.server.am.BatteryStatsService.this.mStats;
                java.util.Objects.requireNonNull(batteryStatsImpl);
                handler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.internal.util.function.QuintConsumer() { // from class: com.android.server.am.BatteryStatsService$LocalService$$ExternalSyntheticLambda0
                    public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3, java.lang.Object obj4, java.lang.Object obj5) {
                        batteryStatsImpl.noteBinderCallStats(((java.lang.Integer) obj).intValue(), ((java.lang.Long) obj2).longValue(), (java.util.Collection) obj3, ((java.lang.Long) obj4).longValue(), ((java.lang.Long) obj5).longValue());
                    }
                }, java.lang.Integer.valueOf(workSourceUid), java.lang.Long.valueOf(incrementatCallCount), callStats, java.lang.Long.valueOf(android.os.SystemClock.elapsedRealtime()), java.lang.Long.valueOf(android.os.SystemClock.uptimeMillis())));
            }
        }

        @Override // android.os.BatteryStatsInternal
        public void noteBinderThreadNativeIds(int[] binderThreadNativeTids) {
            synchronized (com.android.server.am.BatteryStatsService.this.mLock) {
                com.android.server.am.BatteryStatsService.this.mStats.noteBinderThreadNativeIds(binderThreadNativeTids);
            }
        }

        @Override // android.os.BatteryStatsInternal
        public void noteWakingSoundTrigger(long elapsedMillis, int uid) {
            com.android.server.am.BatteryStatsService.this.noteCpuWakingActivity(3, elapsedMillis, uid);
        }

        @Override // android.os.BatteryStatsInternal
        public void noteWakingAlarmBatch(long elapsedMillis, int... uids) {
            com.android.server.am.BatteryStatsService.this.noteCpuWakingActivity(1, elapsedMillis, uids);
        }
    }

    void noteCpuWakingActivity(final int subsystem, final long elapsedMillis, final int... uids) {
        java.util.Objects.requireNonNull(uids);
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda17
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$noteCpuWakingActivity$2(subsystem, elapsedMillis, uids);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteCpuWakingActivity$2(int subsystem, long elapsedMillis, int[] uids) {
        this.mCpuWakeupStats.noteWakingActivity(subsystem, elapsedMillis, uids);
    }

    @Override // com.android.server.Watchdog.Monitor
    public void monitor() {
        if (!this.mMonitorEnabled) {
            return;
        }
        synchronized (this.mLock) {
        }
        synchronized (this.mStats) {
        }
    }

    private static void awaitUninterruptibly(java.util.concurrent.Future<?> future) {
        while (true) {
            try {
                future.get();
                return;
            } catch (java.lang.InterruptedException e) {
            } catch (java.util.concurrent.CancellationException | java.util.concurrent.ExecutionException e2) {
                return;
            }
        }
    }

    private void syncStats(java.lang.String reason, int flags) {
        awaitUninterruptibly(this.mWorker.scheduleSync(reason, flags));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void awaitCompletion() {
        final java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(1);
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda87
            @Override // java.lang.Runnable
            public final void run() {
                latch.countDown();
            }
        });
        try {
            latch.await();
        } catch (java.lang.InterruptedException e) {
        }
    }

    public void initPowerManagement() {
        android.os.PowerManagerInternal powerMgr = (android.os.PowerManagerInternal) com.android.server.LocalServices.getService(android.os.PowerManagerInternal.class);
        powerMgr.registerLowPowerModeObserver(this);
        synchronized (this.mStats) {
            this.mStats.notePowerSaveModeLockedInit(powerMgr.getLowPowerState(9).batterySaverEnabled, android.os.SystemClock.elapsedRealtime(), android.os.SystemClock.uptimeMillis());
        }
        new com.android.server.am.BatteryStatsService.WakeupReasonThread().start();
    }

    public void shutdown() {
        android.util.Slog.w("BatteryStats", "Writing battery stats before shutdown...");
        awaitCompletion();
        syncStats("shutdown", 127);
        synchronized (this.mStats) {
            this.mStats.shutdownLocked();
        }
        this.mWorker.shutdown();
        this.mMonotonicClock.write();
    }

    public static com.android.internal.app.IBatteryStats getService() {
        if (sService != null) {
            return sService;
        }
        android.os.IBinder b = android.os.ServiceManager.getService("batterystats");
        sService = asInterface(b);
        return sService;
    }

    public int getServiceType() {
        return 9;
    }

    public void onLowPowerModeChanged(final android.os.PowerSaveState result) {
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            final long uptime = android.os.SystemClock.uptimeMillis();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda98
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onLowPowerModeChanged$4(result, elapsedRealtime, uptime);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onLowPowerModeChanged$4(android.os.PowerSaveState result, long elapsedRealtime, long uptime) {
        synchronized (this.mStats) {
            this.mStats.notePowerSaveModeLocked(result.batterySaverEnabled, elapsedRealtime, uptime);
        }
    }

    public com.android.server.power.stats.BatteryStatsImpl getActiveStatistics() {
        return this.mStats;
    }

    public void scheduleWriteToDisk() {
        synchronized (this.mLock) {
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda34
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$scheduleWriteToDisk$5();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleWriteToDisk$5() {
        this.mWorker.scheduleWrite();
    }

    void removeUid(final int uid) {
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda43
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$removeUid$6(uid, elapsedRealtime);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$removeUid$6(int uid, long elapsedRealtime) {
        this.mCpuWakeupStats.onUidRemoved(uid);
        synchronized (this.mStats) {
            this.mStats.removeUidStatsLocked(uid, elapsedRealtime);
        }
    }

    void onCleanupUser(final int userId) {
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda66
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onCleanupUser$7(userId, elapsedRealtime);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCleanupUser$7(int userId, long elapsedRealtime) {
        synchronized (this.mStats) {
            this.mStats.onCleanupUserLocked(userId, elapsedRealtime);
        }
    }

    void onUserRemoved(final int userId) {
        synchronized (this.mLock) {
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda101
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onUserRemoved$8(userId);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onUserRemoved$8(int userId) {
        synchronized (this.mStats) {
            this.mStats.onUserRemovedLocked(userId);
        }
    }

    void addIsolatedUid(int isolatedUid, int appUid) {
        this.mPowerStatsUidResolver.noteIsolatedUidAdded(isolatedUid, appUid);
        com.android.internal.util.FrameworkStatsLog.write(43, appUid, isolatedUid, 1);
    }

    void removeIsolatedUid(int isolatedUid, int appUid) {
        this.mPowerStatsUidResolver.noteIsolatedUidRemoved(isolatedUid, appUid);
        com.android.internal.util.FrameworkStatsLog.write(43, -1, isolatedUid, 0);
    }

    void noteProcessStart(final java.lang.String name, final int uid) {
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            final long uptime = android.os.SystemClock.uptimeMillis();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda55
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$noteProcessStart$9(name, uid, elapsedRealtime, uptime);
                }
            });
        }
        com.android.internal.util.FrameworkStatsLog.write(28, uid, name, 1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteProcessStart$9(java.lang.String name, int uid, long elapsedRealtime, long uptime) {
        synchronized (this.mStats) {
            this.mStats.noteProcessStartLocked(name, uid, elapsedRealtime, uptime);
        }
    }

    void noteProcessCrash(final java.lang.String name, final int uid) {
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            final long uptime = android.os.SystemClock.uptimeMillis();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda94
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$noteProcessCrash$10(name, uid, elapsedRealtime, uptime);
                }
            });
        }
        com.android.internal.util.FrameworkStatsLog.write(28, uid, name, 2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteProcessCrash$10(java.lang.String name, int uid, long elapsedRealtime, long uptime) {
        synchronized (this.mStats) {
            this.mStats.noteProcessCrashLocked(name, uid, elapsedRealtime, uptime);
        }
    }

    public void noteProcessAnr(final java.lang.String name, final int uid) {
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            final long uptime = android.os.SystemClock.uptimeMillis();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda20
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$noteProcessAnr$11(name, uid, elapsedRealtime, uptime);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteProcessAnr$11(java.lang.String name, int uid, long elapsedRealtime, long uptime) {
        synchronized (this.mStats) {
            this.mStats.noteProcessAnrLocked(name, uid, elapsedRealtime, uptime);
        }
    }

    void noteProcessFinish(final java.lang.String name, final int uid) {
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            final long uptime = android.os.SystemClock.uptimeMillis();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda7
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$noteProcessFinish$12(name, uid, elapsedRealtime, uptime);
                }
            });
        }
        com.android.internal.util.FrameworkStatsLog.write(28, uid, name, 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteProcessFinish$12(java.lang.String name, int uid, long elapsedRealtime, long uptime) {
        synchronized (this.mStats) {
            this.mStats.noteProcessFinishLocked(name, uid, elapsedRealtime, uptime);
        }
    }

    void noteUidProcessState(final int uid, final int state) {
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            final long uptime = android.os.SystemClock.uptimeMillis();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda83
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$noteUidProcessState$13(uid, state, elapsedRealtime, uptime);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteUidProcessState$13(int uid, int state, long elapsedRealtime, long uptime) {
        this.mCpuWakeupStats.noteUidProcessState(uid, state);
        synchronized (this.mStats) {
            this.mStats.noteUidProcessStateLocked(uid, state, elapsedRealtime, uptime);
        }
    }

    public java.util.List<android.os.BatteryUsageStats> getBatteryUsageStats(java.util.List<android.os.BatteryUsageStatsQuery> queries) {
        super.getBatteryUsageStats_enforcePermission();
        awaitCompletion();
        if (com.android.server.power.stats.BatteryUsageStatsProvider.shouldUpdateStats(queries, android.os.SystemClock.elapsedRealtime(), this.mWorker.getLastCollectionTimeStamp())) {
            syncStats("get-stats", 127);
            if (com.android.internal.hidden_from_bootclasspath.com.android.server.power.optimization.Flags.streamlinedBatteryStats()) {
                this.mStats.collectPowerStatsSamples();
            }
        }
        return this.mBatteryUsageStatsProvider.getBatteryUsageStats(this.mStats, queries);
    }

    private void registerStatsCallbacks() {
        android.app.StatsManager statsManager = (android.app.StatsManager) this.mContext.getSystemService(android.app.StatsManager.class);
        com.android.server.am.BatteryStatsService.StatsPullAtomCallbackImpl pullAtomCallback = new com.android.server.am.BatteryStatsService.StatsPullAtomCallbackImpl();
        statsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.BATTERY_USAGE_STATS_SINCE_RESET, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, pullAtomCallback);
        statsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.BATTERY_USAGE_STATS_SINCE_RESET_USING_POWER_PROFILE_MODEL, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, pullAtomCallback);
        statsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.BATTERY_USAGE_STATS_BEFORE_RESET, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, pullAtomCallback);
    }

    private class StatsPullAtomCallbackImpl implements android.app.StatsManager.StatsPullAtomCallback {
        private StatsPullAtomCallbackImpl() {
        }

        public int onPullAtom(int atomTag, java.util.List<android.util.StatsEvent> data) {
            long sessionEnd;
            android.os.BatteryUsageStats bus;
            switch (atomTag) {
                case com.android.internal.util.FrameworkStatsLog.BATTERY_USAGE_STATS_BEFORE_RESET /* 10111 */:
                    long sessionStart = com.android.server.am.BatteryStatsService.this.getLastBatteryUsageStatsBeforeResetAtomPullTimestamp();
                    synchronized (com.android.server.am.BatteryStatsService.this.mStats) {
                        sessionEnd = com.android.server.am.BatteryStatsService.this.mStats.getStartClockTime();
                        break;
                    }
                    android.os.BatteryUsageStatsQuery queryBeforeReset = new android.os.BatteryUsageStatsQuery.Builder().setMaxStatsAgeMs(0L).includeProcessStateData().includeVirtualUids().aggregateSnapshots(sessionStart, sessionEnd).build();
                    bus = com.android.server.am.BatteryStatsService.this.getBatteryUsageStats(java.util.List.of(queryBeforeReset)).get(0);
                    com.android.server.am.BatteryStatsService.this.setLastBatteryUsageStatsBeforeResetAtomPullTimestamp(sessionEnd);
                    break;
                case com.android.internal.util.FrameworkStatsLog.BATTERY_USAGE_STATS_SINCE_RESET /* 10112 */:
                    double minConsumedPowerThreshold = android.provider.DeviceConfig.getFloat(com.android.server.am.BatteryStatsService.DEVICE_CONFIG_NAMESPACE, com.android.server.am.BatteryStatsService.MIN_CONSUMED_POWER_THRESHOLD_KEY, 0.0f);
                    android.os.BatteryUsageStatsQuery querySinceReset = new android.os.BatteryUsageStatsQuery.Builder().setMaxStatsAgeMs(0L).includeProcessStateData().includeVirtualUids().includePowerModels().setMinConsumedPowerThreshold(minConsumedPowerThreshold).build();
                    bus = com.android.server.am.BatteryStatsService.this.getBatteryUsageStats(java.util.List.of(querySinceReset)).get(0);
                    break;
                case com.android.internal.util.FrameworkStatsLog.BATTERY_USAGE_STATS_SINCE_RESET_USING_POWER_PROFILE_MODEL /* 10113 */:
                    android.os.BatteryUsageStatsQuery queryPowerProfile = new android.os.BatteryUsageStatsQuery.Builder().setMaxStatsAgeMs(0L).includeProcessStateData().includeVirtualUids().powerProfileModeledOnly().includePowerModels().build();
                    bus = com.android.server.am.BatteryStatsService.this.getBatteryUsageStats(java.util.List.of(queryPowerProfile)).get(0);
                    break;
                default:
                    throw new java.lang.UnsupportedOperationException("Unknown tagId=" + atomTag);
            }
            byte[] statsProto = bus.getStatsProto();
            data.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, statsProto));
            return 0;
        }
    }

    public boolean isCharging() {
        boolean zIsCharging;
        synchronized (this.mStats) {
            zIsCharging = this.mStats.isCharging();
        }
        return zIsCharging;
    }

    public long computeBatteryTimeRemaining() {
        long j;
        synchronized (this.mStats) {
            long time = this.mStats.computeBatteryTimeRemaining(android.os.SystemClock.elapsedRealtime());
            j = time >= 0 ? time / 1000 : time;
        }
        return j;
    }

    public long computeChargeTimeRemaining() {
        long j;
        synchronized (this.mStats) {
            long time = this.mStats.computeChargeTimeRemaining(android.os.SystemClock.elapsedRealtime());
            j = time >= 0 ? time / 1000 : time;
        }
        return j;
    }

    public long computeBatteryScreenOffRealtimeMs() {
        long j;
        super.computeBatteryScreenOffRealtimeMs_enforcePermission();
        synchronized (this.mStats) {
            long curTimeUs = android.os.SystemClock.elapsedRealtimeNanos() / 1000;
            long timeUs = this.mStats.computeBatteryScreenOffRealtime(curTimeUs, 0);
            j = timeUs / 1000;
        }
        return j;
    }

    public long getScreenOffDischargeMah() {
        long j;
        super.getScreenOffDischargeMah_enforcePermission();
        synchronized (this.mStats) {
            long dischargeUah = this.mStats.getUahDischargeScreenOff(0);
            j = dischargeUah / 1000;
        }
        return j;
    }

    public void noteEvent(final int code, final java.lang.String name, final int uid) {
        super.noteEvent_enforcePermission();
        if (name == null) {
            android.util.Slog.wtfStack(TAG, "noteEvent called with null name. code = " + code);
            return;
        }
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            final long uptime = android.os.SystemClock.uptimeMillis();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda80
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$noteEvent$14(code, name, uid, elapsedRealtime, uptime);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteEvent$14(int code, java.lang.String name, int uid, long elapsedRealtime, long uptime) {
        synchronized (this.mStats) {
            this.mStats.noteEventLocked(code, name, uid, elapsedRealtime, uptime);
        }
    }

    public void noteSyncStart(final java.lang.String name, final int uid) {
        super.noteSyncStart_enforcePermission();
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            final long uptime = android.os.SystemClock.uptimeMillis();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda13
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$noteSyncStart$15(name, uid, elapsedRealtime, uptime);
                }
            });
        }
        com.android.internal.util.FrameworkStatsLog.write_non_chained(7, uid, (java.lang.String) null, name, 1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteSyncStart$15(java.lang.String name, int uid, long elapsedRealtime, long uptime) {
        synchronized (this.mStats) {
            this.mStats.noteSyncStartLocked(name, uid, elapsedRealtime, uptime);
        }
    }

    public void noteSyncFinish(final java.lang.String name, final int uid) {
        super.noteSyncFinish_enforcePermission();
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            final long uptime = android.os.SystemClock.uptimeMillis();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda23
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$noteSyncFinish$16(name, uid, elapsedRealtime, uptime);
                }
            });
        }
        com.android.internal.util.FrameworkStatsLog.write_non_chained(7, uid, (java.lang.String) null, name, 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteSyncFinish$16(java.lang.String name, int uid, long elapsedRealtime, long uptime) {
        synchronized (this.mStats) {
            this.mStats.noteSyncFinishLocked(name, uid, elapsedRealtime, uptime);
        }
    }

    public void noteJobStart(final java.lang.String name, final int uid) {
        super.noteJobStart_enforcePermission();
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            final long uptime = android.os.SystemClock.uptimeMillis();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda36
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$noteJobStart$17(name, uid, elapsedRealtime, uptime);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteJobStart$17(java.lang.String name, int uid, long elapsedRealtime, long uptime) {
        synchronized (this.mStats) {
            this.mStats.noteJobStartLocked(name, uid, elapsedRealtime, uptime);
        }
    }

    public void noteJobFinish(final java.lang.String name, final int uid, final int stopReason) {
        super.noteJobFinish_enforcePermission();
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            final long uptime = android.os.SystemClock.uptimeMillis();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda70
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$noteJobFinish$18(name, uid, stopReason, elapsedRealtime, uptime);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteJobFinish$18(java.lang.String name, int uid, int stopReason, long elapsedRealtime, long uptime) {
        synchronized (this.mStats) {
            this.mStats.noteJobFinishLocked(name, uid, stopReason, elapsedRealtime, uptime);
        }
    }

    void noteJobsDeferred(final int uid, final int numDeferred, final long sinceLast) {
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            final long uptime = android.os.SystemClock.uptimeMillis();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda64
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$noteJobsDeferred$19(uid, numDeferred, sinceLast, elapsedRealtime, uptime);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteJobsDeferred$19(int uid, int numDeferred, long sinceLast, long elapsedRealtime, long uptime) {
        synchronized (this.mStats) {
            this.mStats.noteJobsDeferredLocked(uid, numDeferred, sinceLast, elapsedRealtime, uptime);
        }
    }

    public void noteWakupAlarm(final java.lang.String name, final int uid, android.os.WorkSource workSource, final java.lang.String tag) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.UPDATE_DEVICE_STATS", "noteWakupAlarm");
        final android.os.WorkSource localWs = workSource != null ? new android.os.WorkSource(workSource) : null;
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            final long uptime = android.os.SystemClock.uptimeMillis();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda89
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$noteWakupAlarm$20(name, uid, localWs, tag, elapsedRealtime, uptime);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteWakupAlarm$20(java.lang.String name, int uid, android.os.WorkSource localWs, java.lang.String tag, long elapsedRealtime, long uptime) {
        synchronized (this.mStats) {
            this.mStats.noteWakupAlarmLocked(name, uid, localWs, tag, elapsedRealtime, uptime);
        }
    }

    public void noteAlarmStart(final java.lang.String name, android.os.WorkSource workSource, final int uid) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.UPDATE_DEVICE_STATS", "noteAlarmStart");
        final android.os.WorkSource localWs = workSource != null ? new android.os.WorkSource(workSource) : null;
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            final long uptime = android.os.SystemClock.uptimeMillis();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda29
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$noteAlarmStart$21(name, localWs, uid, elapsedRealtime, uptime);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteAlarmStart$21(java.lang.String name, android.os.WorkSource localWs, int uid, long elapsedRealtime, long uptime) {
        synchronized (this.mStats) {
            this.mStats.noteAlarmStartLocked(name, localWs, uid, elapsedRealtime, uptime);
        }
    }

    public void noteAlarmFinish(final java.lang.String name, android.os.WorkSource workSource, final int uid) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.UPDATE_DEVICE_STATS", "noteAlarmFinish");
        final android.os.WorkSource localWs = workSource != null ? new android.os.WorkSource(workSource) : null;
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            final long uptime = android.os.SystemClock.uptimeMillis();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda62
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$noteAlarmFinish$22(name, localWs, uid, elapsedRealtime, uptime);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteAlarmFinish$22(java.lang.String name, android.os.WorkSource localWs, int uid, long elapsedRealtime, long uptime) {
        synchronized (this.mStats) {
            this.mStats.noteAlarmFinishLocked(name, localWs, uid, elapsedRealtime, uptime);
        }
    }

    public void noteStartWakelock(final int uid, final int pid, final java.lang.String name, final java.lang.String historyName, final int type, final boolean unimportantForLogging) {
        super.noteStartWakelock_enforcePermission();
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            final long uptime = android.os.SystemClock.uptimeMillis();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$noteStartWakelock$23(uid, pid, name, historyName, type, unimportantForLogging, elapsedRealtime, uptime);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteStartWakelock$23(int uid, int pid, java.lang.String name, java.lang.String historyName, int type, boolean unimportantForLogging, long elapsedRealtime, long uptime) {
        synchronized (this.mStats) {
            this.mStats.noteStartWakeLocked(uid, pid, null, name, historyName, type, unimportantForLogging, elapsedRealtime, uptime);
        }
    }

    public void noteStopWakelock(final int uid, final int pid, final java.lang.String name, final java.lang.String historyName, final int type) {
        super.noteStopWakelock_enforcePermission();
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            final long uptime = android.os.SystemClock.uptimeMillis();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda9
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$noteStopWakelock$24(uid, pid, name, historyName, type, elapsedRealtime, uptime);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteStopWakelock$24(int uid, int pid, java.lang.String name, java.lang.String historyName, int type, long elapsedRealtime, long uptime) {
        synchronized (this.mStats) {
            this.mStats.noteStopWakeLocked(uid, pid, null, name, historyName, type, elapsedRealtime, uptime);
        }
    }

    public void noteStartWakelockFromSource(android.os.WorkSource ws, final int pid, final java.lang.String name, final java.lang.String historyName, final int type, final boolean unimportantForLogging) {
        super.noteStartWakelockFromSource_enforcePermission();
        final android.os.WorkSource localWs = ws != null ? new android.os.WorkSource(ws) : null;
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            final long uptime = android.os.SystemClock.uptimeMillis();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda21
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$noteStartWakelockFromSource$25(localWs, pid, name, historyName, type, unimportantForLogging, elapsedRealtime, uptime);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteStartWakelockFromSource$25(android.os.WorkSource localWs, int pid, java.lang.String name, java.lang.String historyName, int type, boolean unimportantForLogging, long elapsedRealtime, long uptime) {
        synchronized (this.mStats) {
            this.mStats.noteStartWakeFromSourceLocked(localWs, pid, name, historyName, type, unimportantForLogging, elapsedRealtime, uptime);
        }
    }

    public void noteChangeWakelockFromSource(android.os.WorkSource ws, final int pid, final java.lang.String name, final java.lang.String historyName, final int type, android.os.WorkSource newWs, final int newPid, final java.lang.String newName, final java.lang.String newHistoryName, final int newType, final boolean newUnimportantForLogging) throws java.lang.Throwable {
        super.noteChangeWakelockFromSource_enforcePermission();
        final android.os.WorkSource localWs = ws != null ? new android.os.WorkSource(ws) : null;
        final android.os.WorkSource localNewWs = newWs != null ? new android.os.WorkSource(newWs) : null;
        synchronized (this.mLock) {
            try {
                try {
                    final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
                    final long uptime = android.os.SystemClock.uptimeMillis();
                    this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda33
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$noteChangeWakelockFromSource$26(localWs, pid, name, historyName, type, localNewWs, newPid, newName, newHistoryName, newType, newUnimportantForLogging, elapsedRealtime, uptime);
                        }
                    });
                } catch (java.lang.Throwable th) {
                    th = th;
                    throw th;
                }
            } catch (java.lang.Throwable th2) {
                th = th2;
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteChangeWakelockFromSource$26(android.os.WorkSource localWs, int pid, java.lang.String name, java.lang.String historyName, int type, android.os.WorkSource localNewWs, int newPid, java.lang.String newName, java.lang.String newHistoryName, int newType, boolean newUnimportantForLogging, long elapsedRealtime, long uptime) {
        synchronized (this.mStats) {
            this.mStats.noteChangeWakelockFromSourceLocked(localWs, pid, name, historyName, type, localNewWs, newPid, newName, newHistoryName, newType, newUnimportantForLogging, elapsedRealtime, uptime);
        }
    }

    public void noteStopWakelockFromSource(android.os.WorkSource ws, final int pid, final java.lang.String name, final java.lang.String historyName, final int type) {
        super.noteStopWakelockFromSource_enforcePermission();
        final android.os.WorkSource localWs = ws != null ? new android.os.WorkSource(ws) : null;
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            final long uptime = android.os.SystemClock.uptimeMillis();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda39
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$noteStopWakelockFromSource$27(localWs, pid, name, historyName, type, elapsedRealtime, uptime);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteStopWakelockFromSource$27(android.os.WorkSource localWs, int pid, java.lang.String name, java.lang.String historyName, int type, long elapsedRealtime, long uptime) {
        synchronized (this.mStats) {
            this.mStats.noteStopWakeFromSourceLocked(localWs, pid, name, historyName, type, elapsedRealtime, uptime);
        }
    }

    public void noteLongPartialWakelockStart(final java.lang.String name, final java.lang.String historyName, final int uid) {
        super.noteLongPartialWakelockStart_enforcePermission();
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            final long uptime = android.os.SystemClock.uptimeMillis();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda108
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$noteLongPartialWakelockStart$28(name, historyName, uid, elapsedRealtime, uptime);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteLongPartialWakelockStart$28(java.lang.String name, java.lang.String historyName, int uid, long elapsedRealtime, long uptime) {
        synchronized (this.mStats) {
            this.mStats.noteLongPartialWakelockStart(name, historyName, uid, elapsedRealtime, uptime);
        }
    }

    public void noteLongPartialWakelockStartFromSource(final java.lang.String name, final java.lang.String historyName, android.os.WorkSource workSource) {
        super.noteLongPartialWakelockStartFromSource_enforcePermission();
        final android.os.WorkSource localWs = workSource != null ? new android.os.WorkSource(workSource) : null;
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            final long uptime = android.os.SystemClock.uptimeMillis();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda46
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$noteLongPartialWakelockStartFromSource$29(name, historyName, localWs, elapsedRealtime, uptime);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteLongPartialWakelockStartFromSource$29(java.lang.String name, java.lang.String historyName, android.os.WorkSource localWs, long elapsedRealtime, long uptime) {
        synchronized (this.mStats) {
            this.mStats.noteLongPartialWakelockStartFromSource(name, historyName, localWs, elapsedRealtime, uptime);
        }
    }

    public void noteLongPartialWakelockFinish(final java.lang.String name, final java.lang.String historyName, final int uid) {
        super.noteLongPartialWakelockFinish_enforcePermission();
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            final long uptime = android.os.SystemClock.uptimeMillis();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda38
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$noteLongPartialWakelockFinish$30(name, historyName, uid, elapsedRealtime, uptime);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteLongPartialWakelockFinish$30(java.lang.String name, java.lang.String historyName, int uid, long elapsedRealtime, long uptime) {
        synchronized (this.mStats) {
            this.mStats.noteLongPartialWakelockFinish(name, historyName, uid, elapsedRealtime, uptime);
        }
    }

    public void noteLongPartialWakelockFinishFromSource(final java.lang.String name, final java.lang.String historyName, android.os.WorkSource workSource) {
        super.noteLongPartialWakelockFinishFromSource_enforcePermission();
        final android.os.WorkSource localWs = workSource != null ? new android.os.WorkSource(workSource) : null;
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            final long uptime = android.os.SystemClock.uptimeMillis();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda52
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$noteLongPartialWakelockFinishFromSource$31(name, historyName, localWs, elapsedRealtime, uptime);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteLongPartialWakelockFinishFromSource$31(java.lang.String name, java.lang.String historyName, android.os.WorkSource localWs, long elapsedRealtime, long uptime) {
        synchronized (this.mStats) {
            this.mStats.noteLongPartialWakelockFinishFromSource(name, historyName, localWs, elapsedRealtime, uptime);
        }
    }

    public void noteStartSensor(final int uid, final int sensor) {
        super.noteStartSensor_enforcePermission();
        this.mBssExt.noteStartSensor(uid, sensor);
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            final long uptime = android.os.SystemClock.uptimeMillis();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda32
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$noteStartSensor$32(uid, sensor, elapsedRealtime, uptime);
                }
            });
        }
        com.android.internal.util.FrameworkStatsLog.write_non_chained(5, uid, (java.lang.String) null, sensor, 1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteStartSensor$32(int uid, int sensor, long elapsedRealtime, long uptime) {
        synchronized (this.mStats) {
            this.mStats.noteStartSensorLocked(uid, sensor, elapsedRealtime, uptime);
        }
    }

    public void noteWakeupSensorEvent(long elapsedNanos, int uid, int sensorHandle) {
        int callingUid = android.os.Binder.getCallingUid();
        if (callingUid != 1000) {
            throw new java.lang.SecurityException("Calling uid " + callingUid + " is not system uid");
        }
        long elapsedMillis = java.util.concurrent.TimeUnit.NANOSECONDS.toMillis(elapsedNanos);
        android.hardware.SensorManager sm = (android.hardware.SensorManager) this.mContext.getSystemService(android.hardware.SensorManager.class);
        android.hardware.Sensor sensor = sm.getSensorByHandle(sensorHandle);
        if (sensor == null) {
            android.util.Slog.w(TAG, "Unknown sensor handle " + sensorHandle + " received in noteWakeupSensorEvent");
        } else if (uid < 0) {
            android.util.Slog.wtf(TAG, "Invalid uid " + uid + " for sensor event with sensor: " + sensor);
        } else {
            noteCpuWakingActivity(4, elapsedMillis, uid);
        }
    }

    public void noteStopSensor(final int uid, final int sensor) {
        super.noteStopSensor_enforcePermission();
        this.mBssExt.noteStopSensor(uid, sensor);
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            final long uptime = android.os.SystemClock.uptimeMillis();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda85
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$noteStopSensor$33(uid, sensor, elapsedRealtime, uptime);
                }
            });
        }
        com.android.internal.util.FrameworkStatsLog.write_non_chained(5, uid, (java.lang.String) null, sensor, 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteStopSensor$33(int uid, int sensor, long elapsedRealtime, long uptime) {
        synchronized (this.mStats) {
            this.mStats.noteStopSensorLocked(uid, sensor, elapsedRealtime, uptime);
        }
    }

    public void noteVibratorOn(final int uid, final long durationMillis) {
        super.noteVibratorOn_enforcePermission();
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            final long uptime = android.os.SystemClock.uptimeMillis();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda99
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$noteVibratorOn$34(uid, durationMillis, elapsedRealtime, uptime);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteVibratorOn$34(int uid, long durationMillis, long elapsedRealtime, long uptime) {
        synchronized (this.mStats) {
            this.mStats.noteVibratorOnLocked(uid, durationMillis, elapsedRealtime, uptime);
        }
    }

    public void noteVibratorOff(final int uid) {
        super.noteVibratorOff_enforcePermission();
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            final long uptime = android.os.SystemClock.uptimeMillis();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda24
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$noteVibratorOff$35(uid, elapsedRealtime, uptime);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteVibratorOff$35(int uid, long elapsedRealtime, long uptime) {
        synchronized (this.mStats) {
            this.mStats.noteVibratorOffLocked(uid, elapsedRealtime, uptime);
        }
    }

    public void noteGpsChanged(android.os.WorkSource oldWs, android.os.WorkSource newWs) {
        super.noteGpsChanged_enforcePermission();
        final android.os.WorkSource localOldWs = oldWs != null ? new android.os.WorkSource(oldWs) : null;
        final android.os.WorkSource localNewWs = newWs != null ? new android.os.WorkSource(newWs) : null;
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            final long uptime = android.os.SystemClock.uptimeMillis();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda90
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$noteGpsChanged$36(localOldWs, localNewWs, elapsedRealtime, uptime);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteGpsChanged$36(android.os.WorkSource localOldWs, android.os.WorkSource localNewWs, long elapsedRealtime, long uptime) {
        synchronized (this.mStats) {
            this.mStats.noteGpsChangedLocked(localOldWs, localNewWs, elapsedRealtime, uptime);
        }
    }

    public void noteGpsSignalQuality(final int signalLevel) {
        super.noteGpsSignalQuality_enforcePermission();
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            final long uptime = android.os.SystemClock.uptimeMillis();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda103
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$noteGpsSignalQuality$37(signalLevel, elapsedRealtime, uptime);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteGpsSignalQuality$37(int signalLevel, long elapsedRealtime, long uptime) {
        synchronized (this.mStats) {
            this.mStats.noteGpsSignalQualityLocked(signalLevel, elapsedRealtime, uptime);
        }
    }

    public void noteScreenState(final int state) {
        super.noteScreenState_enforcePermission();
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            final long uptime = android.os.SystemClock.uptimeMillis();
            final long currentTime = java.lang.System.currentTimeMillis();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda79
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$noteScreenState$38(state, elapsedRealtime, uptime, currentTime);
                }
            });
        }
        com.android.internal.util.FrameworkStatsLog.write(29, state);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteScreenState$38(int state, long elapsedRealtime, long uptime, long currentTime) {
        synchronized (this.mStats) {
            this.mStats.noteScreenStateLocked(0, state, elapsedRealtime, uptime, currentTime);
        }
    }

    public void noteScreenBrightness(final int brightness) {
        super.noteScreenBrightness_enforcePermission();
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            final long uptime = android.os.SystemClock.uptimeMillis();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda35
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$noteScreenBrightness$39(brightness, elapsedRealtime, uptime);
                }
            });
        }
        com.android.internal.util.FrameworkStatsLog.write(9, brightness);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteScreenBrightness$39(int brightness, long elapsedRealtime, long uptime) {
        synchronized (this.mStats) {
            this.mStats.noteScreenBrightnessLocked(0, brightness, elapsedRealtime, uptime);
        }
    }

    public void noteUserActivity(final int uid, final int event) {
        super.noteUserActivity_enforcePermission();
        final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
        final long uptime = android.os.SystemClock.uptimeMillis();
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda56
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$noteUserActivity$40(uid, event, elapsedRealtime, uptime);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteUserActivity$40(int uid, int event, long elapsedRealtime, long uptime) {
        synchronized (this.mStats) {
            this.mStats.noteUserActivityLocked(uid, event, elapsedRealtime, uptime);
        }
    }

    public void noteWakeUp(final java.lang.String reason, final int reasonUid) {
        super.noteWakeUp_enforcePermission();
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            final long uptime = android.os.SystemClock.uptimeMillis();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda81
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$noteWakeUp$41(reason, reasonUid, elapsedRealtime, uptime);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteWakeUp$41(java.lang.String reason, int reasonUid, long elapsedRealtime, long uptime) {
        synchronized (this.mStats) {
            this.mStats.noteWakeUpLocked(reason, reasonUid, elapsedRealtime, uptime);
        }
    }

    public void noteInteractive(final boolean interactive) {
        super.noteInteractive_enforcePermission();
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda107
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$noteInteractive$42(interactive, elapsedRealtime);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteInteractive$42(boolean interactive, long elapsedRealtime) {
        synchronized (this.mStats) {
            this.mStats.noteInteractiveLocked(interactive, elapsedRealtime);
        }
    }

    public void noteConnectivityChanged(final int type, final java.lang.String extra) {
        super.noteConnectivityChanged_enforcePermission();
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            final long uptime = android.os.SystemClock.uptimeMillis();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda61
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$noteConnectivityChanged$43(type, extra, elapsedRealtime, uptime);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteConnectivityChanged$43(int type, java.lang.String extra, long elapsedRealtime, long uptime) {
        synchronized (this.mStats) {
            this.mStats.noteConnectivityChangedLocked(type, extra, elapsedRealtime, uptime);
        }
    }

    public void noteMobileRadioPowerState(final int powerState, final long timestampNs, final int uid) throws java.lang.Throwable {
        super.noteMobileRadioPowerState_enforcePermission();
        synchronized (this.mLock) {
            try {
                final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
                final long uptime = android.os.SystemClock.uptimeMillis();
                this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda102
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$noteMobileRadioPowerState$44(powerState, timestampNs, uid, elapsedRealtime, uptime);
                    }
                });
            } catch (java.lang.Throwable th) {
                th = th;
                while (true) {
                    try {
                        throw th;
                    } catch (java.lang.Throwable th2) {
                        th = th2;
                    }
                }
            }
        }
        com.android.internal.util.FrameworkStatsLog.write_non_chained(12, uid, null, powerState);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteMobileRadioPowerState$44(int powerState, long timestampNs, int uid, long elapsedRealtime, long uptime) {
        synchronized (this.mStats) {
            if (this.mLastPowerStateFromRadio == powerState) {
                return;
            }
            this.mLastPowerStateFromRadio = powerState;
            this.mStats.noteMobileRadioPowerStateLocked(powerState, timestampNs, uid, elapsedRealtime, uptime);
        }
    }

    public void notePhoneOn() {
        super.notePhoneOn_enforcePermission();
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            final long uptime = android.os.SystemClock.uptimeMillis();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda51
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$notePhoneOn$45(elapsedRealtime, uptime);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$notePhoneOn$45(long elapsedRealtime, long uptime) {
        synchronized (this.mStats) {
            this.mStats.notePhoneOnLocked(elapsedRealtime, uptime);
        }
    }

    public void notePhoneOff() {
        super.notePhoneOff_enforcePermission();
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            final long uptime = android.os.SystemClock.uptimeMillis();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda63
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$notePhoneOff$46(elapsedRealtime, uptime);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$notePhoneOff$46(long elapsedRealtime, long uptime) {
        synchronized (this.mStats) {
            this.mStats.notePhoneOffLocked(elapsedRealtime, uptime);
        }
    }

    public void notePhoneSignalStrength(final android.telephony.SignalStrength signalStrength) {
        super.notePhoneSignalStrength_enforcePermission();
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            final long uptime = android.os.SystemClock.uptimeMillis();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda73
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$notePhoneSignalStrength$47(signalStrength, elapsedRealtime, uptime);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$notePhoneSignalStrength$47(android.telephony.SignalStrength signalStrength, long elapsedRealtime, long uptime) {
        synchronized (this.mStats) {
            this.mStats.notePhoneSignalStrengthLocked(signalStrength, elapsedRealtime, uptime);
        }
    }

    public void notePhoneDataConnectionState(final int dataType, final boolean hasData, final int serviceType, final int nrState, final int nrFrequency) {
        super.notePhoneDataConnectionState_enforcePermission();
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            final long uptime = android.os.SystemClock.uptimeMillis();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda77
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$notePhoneDataConnectionState$48(dataType, hasData, serviceType, nrState, nrFrequency, elapsedRealtime, uptime);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$notePhoneDataConnectionState$48(int dataType, boolean hasData, int serviceType, int nrState, int nrFrequency, long elapsedRealtime, long uptime) {
        synchronized (this.mStats) {
            this.mStats.notePhoneDataConnectionStateLocked(dataType, hasData, serviceType, nrState, nrFrequency, elapsedRealtime, uptime);
        }
    }

    public void notePhoneState(final int state) {
        super.notePhoneState_enforcePermission();
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            final long uptime = android.os.SystemClock.uptimeMillis();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda53
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$notePhoneState$49(state, elapsedRealtime, uptime);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$notePhoneState$49(int state, long elapsedRealtime, long uptime) {
        int simState = ((android.telephony.TelephonyManager) this.mContext.getSystemService(android.telephony.TelephonyManager.class)).getSimState();
        synchronized (this.mStats) {
            this.mStats.notePhoneStateLocked(state, simState, elapsedRealtime, uptime);
        }
    }

    public void noteWifiOn() {
        super.noteWifiOn_enforcePermission();
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            final long uptime = android.os.SystemClock.uptimeMillis();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda40
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$noteWifiOn$50(elapsedRealtime, uptime);
                }
            });
        }
        com.android.internal.util.FrameworkStatsLog.write(113, 1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteWifiOn$50(long elapsedRealtime, long uptime) {
        synchronized (this.mStats) {
            this.mStats.noteWifiOnLocked(elapsedRealtime, uptime);
        }
    }

    public void noteWifiOff() {
        super.noteWifiOff_enforcePermission();
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            final long uptime = android.os.SystemClock.uptimeMillis();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda31
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$noteWifiOff$51(elapsedRealtime, uptime);
                }
            });
        }
        com.android.internal.util.FrameworkStatsLog.write(113, 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteWifiOff$51(long elapsedRealtime, long uptime) {
        synchronized (this.mStats) {
            this.mStats.noteWifiOffLocked(elapsedRealtime, uptime);
        }
    }

    public void noteStartAudio(final int uid) {
        super.noteStartAudio_enforcePermission();
        this.mBssExt.noteStartAudio(uid);
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            final long uptime = android.os.SystemClock.uptimeMillis();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda78
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$noteStartAudio$52(uid, elapsedRealtime, uptime);
                }
            });
        }
        com.android.internal.util.FrameworkStatsLog.write_non_chained(23, uid, null, 1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteStartAudio$52(int uid, long elapsedRealtime, long uptime) {
        synchronized (this.mStats) {
            this.mStats.noteAudioOnLocked(uid, elapsedRealtime, uptime);
        }
    }

    public void noteStopAudio(final int uid) {
        super.noteStopAudio_enforcePermission();
        this.mBssExt.noteStopAudio(uid);
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            final long uptime = android.os.SystemClock.uptimeMillis();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda100
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$noteStopAudio$53(uid, elapsedRealtime, uptime);
                }
            });
        }
        com.android.internal.util.FrameworkStatsLog.write_non_chained(23, uid, null, 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteStopAudio$53(int uid, long elapsedRealtime, long uptime) {
        synchronized (this.mStats) {
            this.mStats.noteAudioOffLocked(uid, elapsedRealtime, uptime);
        }
    }

    public void noteStartVideo(final int uid) {
        super.noteStartVideo_enforcePermission();
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            final long uptime = android.os.SystemClock.uptimeMillis();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda74
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$noteStartVideo$54(uid, elapsedRealtime, uptime);
                }
            });
        }
        com.android.internal.util.FrameworkStatsLog.write_non_chained(24, uid, null, 1);
        this.mBssExt.noteStartVideo(uid);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteStartVideo$54(int uid, long elapsedRealtime, long uptime) {
        synchronized (this.mStats) {
            this.mStats.noteVideoOnLocked(uid, elapsedRealtime, uptime);
        }
    }

    public void noteStopVideo(final int uid) {
        super.noteStopVideo_enforcePermission();
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            final long uptime = android.os.SystemClock.uptimeMillis();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda28
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$noteStopVideo$55(uid, elapsedRealtime, uptime);
                }
            });
        }
        com.android.internal.util.FrameworkStatsLog.write_non_chained(24, uid, null, 0);
        this.mBssExt.noteStopVideo(uid);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteStopVideo$55(int uid, long elapsedRealtime, long uptime) {
        synchronized (this.mStats) {
            this.mStats.noteVideoOffLocked(uid, elapsedRealtime, uptime);
        }
    }

    public void noteResetAudio() {
        super.noteResetAudio_enforcePermission();
        this.mBssExt.noteResetAudio();
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            final long uptime = android.os.SystemClock.uptimeMillis();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda27
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$noteResetAudio$56(elapsedRealtime, uptime);
                }
            });
        }
        com.android.internal.util.FrameworkStatsLog.write_non_chained(23, -1, null, 2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteResetAudio$56(long elapsedRealtime, long uptime) {
        synchronized (this.mStats) {
            this.mStats.noteResetAudioLocked(elapsedRealtime, uptime);
        }
    }

    public void noteResetVideo() {
        super.noteResetVideo_enforcePermission();
        this.mBssExt.noteResetVideo();
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            final long uptime = android.os.SystemClock.uptimeMillis();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda12
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$noteResetVideo$57(elapsedRealtime, uptime);
                }
            });
        }
        com.android.internal.util.FrameworkStatsLog.write_non_chained(24, -1, null, 2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteResetVideo$57(long elapsedRealtime, long uptime) {
        synchronized (this.mStats) {
            this.mStats.noteResetVideoLocked(elapsedRealtime, uptime);
        }
    }

    public void noteFlashlightOn(final int uid) {
        super.noteFlashlightOn_enforcePermission();
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            final long uptime = android.os.SystemClock.uptimeMillis();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda50
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$noteFlashlightOn$58(uid, elapsedRealtime, uptime);
                }
            });
        }
        com.android.internal.util.FrameworkStatsLog.write_non_chained(26, uid, null, 1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteFlashlightOn$58(int uid, long elapsedRealtime, long uptime) {
        synchronized (this.mStats) {
            this.mStats.noteFlashlightOnLocked(uid, elapsedRealtime, uptime);
        }
    }

    public void noteFlashlightOff(final int uid) {
        super.noteFlashlightOff_enforcePermission();
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            final long uptime = android.os.SystemClock.uptimeMillis();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda54
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$noteFlashlightOff$59(uid, elapsedRealtime, uptime);
                }
            });
        }
        com.android.internal.util.FrameworkStatsLog.write_non_chained(26, uid, null, 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteFlashlightOff$59(int uid, long elapsedRealtime, long uptime) {
        synchronized (this.mStats) {
            this.mStats.noteFlashlightOffLocked(uid, elapsedRealtime, uptime);
        }
    }

    public void noteStartCamera(final int uid) {
        super.noteStartCamera_enforcePermission();
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            final long uptime = android.os.SystemClock.uptimeMillis();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda93
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$noteStartCamera$60(uid, elapsedRealtime, uptime);
                }
            });
        }
        com.android.internal.util.FrameworkStatsLog.write_non_chained(25, uid, null, 1);
        this.mBssExt.noteStartCamera(uid);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteStartCamera$60(int uid, long elapsedRealtime, long uptime) {
        synchronized (this.mStats) {
            this.mStats.noteCameraOnLocked(uid, elapsedRealtime, uptime);
        }
    }

    public void noteStopCamera(final int uid) {
        super.noteStopCamera_enforcePermission();
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            final long uptime = android.os.SystemClock.uptimeMillis();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda49
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$noteStopCamera$61(uid, elapsedRealtime, uptime);
                }
            });
        }
        com.android.internal.util.FrameworkStatsLog.write_non_chained(25, uid, null, 0);
        this.mBssExt.noteStopCamera(uid);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteStopCamera$61(int uid, long elapsedRealtime, long uptime) {
        synchronized (this.mStats) {
            this.mStats.noteCameraOffLocked(uid, elapsedRealtime, uptime);
        }
    }

    public void noteResetCamera() {
        super.noteResetCamera_enforcePermission();
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            final long uptime = android.os.SystemClock.uptimeMillis();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$noteResetCamera$62(elapsedRealtime, uptime);
                }
            });
        }
        com.android.internal.util.FrameworkStatsLog.write_non_chained(25, -1, null, 2);
        this.mBssExt.noteResetCamera();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteResetCamera$62(long elapsedRealtime, long uptime) {
        synchronized (this.mStats) {
            this.mStats.noteResetCameraLocked(elapsedRealtime, uptime);
        }
    }

    public void noteResetFlashlight() {
        super.noteResetFlashlight_enforcePermission();
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            final long uptime = android.os.SystemClock.uptimeMillis();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda92
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$noteResetFlashlight$63(elapsedRealtime, uptime);
                }
            });
        }
        com.android.internal.util.FrameworkStatsLog.write_non_chained(26, -1, null, 2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteResetFlashlight$63(long elapsedRealtime, long uptime) {
        synchronized (this.mStats) {
            this.mStats.noteResetFlashlightLocked(elapsedRealtime, uptime);
        }
    }

    public void noteWifiRadioPowerState(final int powerState, final long tsNanos, final int uid) throws java.lang.Throwable {
        super.noteWifiRadioPowerState_enforcePermission();
        synchronized (this.mLock) {
            try {
                final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
                final long uptime = android.os.SystemClock.uptimeMillis();
                this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda22
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$noteWifiRadioPowerState$64(powerState, tsNanos, uid, elapsedRealtime, uptime);
                    }
                });
            } catch (java.lang.Throwable th) {
                th = th;
                while (true) {
                    try {
                        throw th;
                    } catch (java.lang.Throwable th2) {
                        th = th2;
                    }
                }
            }
        }
        com.android.internal.util.FrameworkStatsLog.write_non_chained(13, uid, null, powerState);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteWifiRadioPowerState$64(int powerState, long tsNanos, int uid, long elapsedRealtime, long uptime) {
        synchronized (this.mStats) {
            if (this.mLastPowerStateFromWifi == powerState) {
                return;
            }
            this.mLastPowerStateFromWifi = powerState;
            if (this.mStats.isOnBattery()) {
                java.lang.String type = (powerState == 3 || powerState == 2) ? com.android.server.pm.verify.domain.DomainVerificationPersistence.TAG_ACTIVE : "inactive";
                this.mWorker.scheduleSync("wifi-data: " + type, 2);
            }
            this.mStats.noteWifiRadioPowerState(powerState, tsNanos, uid, elapsedRealtime, uptime);
        }
    }

    public void noteWifiRunning(android.os.WorkSource ws) {
        super.noteWifiRunning_enforcePermission();
        final android.os.WorkSource localWs = ws != null ? new android.os.WorkSource(ws) : null;
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            final long uptime = android.os.SystemClock.uptimeMillis();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda8
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$noteWifiRunning$65(localWs, elapsedRealtime, uptime);
                }
            });
        }
        com.android.internal.util.FrameworkStatsLog.write(114, ws, 1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteWifiRunning$65(android.os.WorkSource localWs, long elapsedRealtime, long uptime) {
        synchronized (this.mStats) {
            this.mStats.noteWifiRunningLocked(localWs, elapsedRealtime, uptime);
        }
    }

    public void noteWifiRunningChanged(android.os.WorkSource oldWs, android.os.WorkSource newWs) {
        super.noteWifiRunningChanged_enforcePermission();
        final android.os.WorkSource localOldWs = oldWs != null ? new android.os.WorkSource(oldWs) : null;
        final android.os.WorkSource localNewWs = newWs != null ? new android.os.WorkSource(newWs) : null;
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            final long uptime = android.os.SystemClock.uptimeMillis();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda58
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$noteWifiRunningChanged$66(localOldWs, localNewWs, elapsedRealtime, uptime);
                }
            });
        }
        com.android.internal.util.FrameworkStatsLog.write(114, newWs, 1);
        com.android.internal.util.FrameworkStatsLog.write(114, oldWs, 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteWifiRunningChanged$66(android.os.WorkSource localOldWs, android.os.WorkSource localNewWs, long elapsedRealtime, long uptime) {
        synchronized (this.mStats) {
            this.mStats.noteWifiRunningChangedLocked(localOldWs, localNewWs, elapsedRealtime, uptime);
        }
    }

    public void noteWifiStopped(android.os.WorkSource ws) {
        super.noteWifiStopped_enforcePermission();
        final android.os.WorkSource localWs = ws != null ? new android.os.WorkSource(ws) : ws;
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            final long uptime = android.os.SystemClock.uptimeMillis();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda26
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$noteWifiStopped$67(localWs, elapsedRealtime, uptime);
                }
            });
        }
        com.android.internal.util.FrameworkStatsLog.write(114, ws, 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteWifiStopped$67(android.os.WorkSource localWs, long elapsedRealtime, long uptime) {
        synchronized (this.mStats) {
            this.mStats.noteWifiStoppedLocked(localWs, elapsedRealtime, uptime);
        }
    }

    public void noteWifiState(final int wifiState, final java.lang.String accessPoint) {
        super.noteWifiState_enforcePermission();
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda47
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$noteWifiState$68(wifiState, accessPoint, elapsedRealtime);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteWifiState$68(int wifiState, java.lang.String accessPoint, long elapsedRealtime) {
        synchronized (this.mStats) {
            this.mStats.noteWifiStateLocked(wifiState, accessPoint, elapsedRealtime);
        }
    }

    public void noteWifiSupplicantStateChanged(final int supplState, final boolean failedAuth) {
        super.noteWifiSupplicantStateChanged_enforcePermission();
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            final long uptime = android.os.SystemClock.uptimeMillis();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda42
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$noteWifiSupplicantStateChanged$69(supplState, failedAuth, elapsedRealtime, uptime);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteWifiSupplicantStateChanged$69(int supplState, boolean failedAuth, long elapsedRealtime, long uptime) {
        synchronized (this.mStats) {
            this.mStats.noteWifiSupplicantStateChangedLocked(supplState, failedAuth, elapsedRealtime, uptime);
        }
    }

    public void noteWifiRssiChanged(final int newRssi) {
        super.noteWifiRssiChanged_enforcePermission();
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            final long uptime = android.os.SystemClock.uptimeMillis();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda45
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$noteWifiRssiChanged$70(newRssi, elapsedRealtime, uptime);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteWifiRssiChanged$70(int newRssi, long elapsedRealtime, long uptime) {
        synchronized (this.mStats) {
            this.mStats.noteWifiRssiChangedLocked(newRssi, elapsedRealtime, uptime);
        }
    }

    public void noteFullWifiLockAcquired(final int uid) {
        super.noteFullWifiLockAcquired_enforcePermission();
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            final long uptime = android.os.SystemClock.uptimeMillis();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$noteFullWifiLockAcquired$71(uid, elapsedRealtime, uptime);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteFullWifiLockAcquired$71(int uid, long elapsedRealtime, long uptime) {
        synchronized (this.mStats) {
            this.mStats.noteFullWifiLockAcquiredLocked(uid, elapsedRealtime, uptime);
        }
    }

    public void noteFullWifiLockReleased(final int uid) {
        super.noteFullWifiLockReleased_enforcePermission();
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            final long uptime = android.os.SystemClock.uptimeMillis();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda59
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$noteFullWifiLockReleased$72(uid, elapsedRealtime, uptime);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteFullWifiLockReleased$72(int uid, long elapsedRealtime, long uptime) {
        synchronized (this.mStats) {
            this.mStats.noteFullWifiLockReleasedLocked(uid, elapsedRealtime, uptime);
        }
    }

    public void noteWifiScanStarted(final int uid) {
        super.noteWifiScanStarted_enforcePermission();
        this.mBssExt.noteWifiScanStarted(uid);
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            final long uptime = android.os.SystemClock.uptimeMillis();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda82
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$noteWifiScanStarted$73(uid, elapsedRealtime, uptime);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteWifiScanStarted$73(int uid, long elapsedRealtime, long uptime) {
        synchronized (this.mStats) {
            this.mStats.noteWifiScanStartedLocked(uid, elapsedRealtime, uptime);
        }
    }

    public void noteWifiScanStopped(final int uid) {
        super.noteWifiScanStopped_enforcePermission();
        this.mBssExt.noteWifiScanStopped(uid);
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            final long uptime = android.os.SystemClock.uptimeMillis();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda57
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$noteWifiScanStopped$74(uid, elapsedRealtime, uptime);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteWifiScanStopped$74(int uid, long elapsedRealtime, long uptime) {
        synchronized (this.mStats) {
            this.mStats.noteWifiScanStoppedLocked(uid, elapsedRealtime, uptime);
        }
    }

    public void noteWifiMulticastEnabled(final int uid) {
        super.noteWifiMulticastEnabled_enforcePermission();
        this.mBssExt.noteWifiMulticastEnabled(uid);
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            final long uptime = android.os.SystemClock.uptimeMillis();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$noteWifiMulticastEnabled$75(uid, elapsedRealtime, uptime);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteWifiMulticastEnabled$75(int uid, long elapsedRealtime, long uptime) {
        synchronized (this.mStats) {
            this.mStats.noteWifiMulticastEnabledLocked(uid, elapsedRealtime, uptime);
        }
    }

    public void noteWifiMulticastDisabled(final int uid) {
        super.noteWifiMulticastDisabled_enforcePermission();
        this.mBssExt.noteWifiMulticastDisabled(uid);
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            final long uptime = android.os.SystemClock.uptimeMillis();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda25
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$noteWifiMulticastDisabled$76(uid, elapsedRealtime, uptime);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteWifiMulticastDisabled$76(int uid, long elapsedRealtime, long uptime) {
        synchronized (this.mStats) {
            this.mStats.noteWifiMulticastDisabledLocked(uid, elapsedRealtime, uptime);
        }
    }

    public void noteFullWifiLockAcquiredFromSource(android.os.WorkSource ws) {
        super.noteFullWifiLockAcquiredFromSource_enforcePermission();
        final android.os.WorkSource localWs = ws != null ? new android.os.WorkSource(ws) : null;
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            final long uptime = android.os.SystemClock.uptimeMillis();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda97
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$noteFullWifiLockAcquiredFromSource$77(localWs, elapsedRealtime, uptime);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteFullWifiLockAcquiredFromSource$77(android.os.WorkSource localWs, long elapsedRealtime, long uptime) {
        synchronized (this.mStats) {
            this.mStats.noteFullWifiLockAcquiredFromSourceLocked(localWs, elapsedRealtime, uptime);
        }
    }

    public void noteFullWifiLockReleasedFromSource(android.os.WorkSource ws) {
        super.noteFullWifiLockReleasedFromSource_enforcePermission();
        final android.os.WorkSource localWs = ws != null ? new android.os.WorkSource(ws) : null;
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            final long uptime = android.os.SystemClock.uptimeMillis();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda37
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$noteFullWifiLockReleasedFromSource$78(localWs, elapsedRealtime, uptime);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteFullWifiLockReleasedFromSource$78(android.os.WorkSource localWs, long elapsedRealtime, long uptime) {
        synchronized (this.mStats) {
            this.mStats.noteFullWifiLockReleasedFromSourceLocked(localWs, elapsedRealtime, uptime);
        }
    }

    public void noteWifiScanStartedFromSource(android.os.WorkSource ws) {
        super.noteWifiScanStartedFromSource_enforcePermission();
        android.os.WorkSource localWs = ws != null ? new android.os.WorkSource(ws) : null;
        this.mBssExt.noteWifiScanStartedFromSource(localWs);
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            final long uptime = android.os.SystemClock.uptimeMillis();
            final android.os.WorkSource workSource = localWs;
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda60
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$noteWifiScanStartedFromSource$79(workSource, elapsedRealtime, uptime);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteWifiScanStartedFromSource$79(android.os.WorkSource localWs, long elapsedRealtime, long uptime) {
        synchronized (this.mStats) {
            this.mStats.noteWifiScanStartedFromSourceLocked(localWs, elapsedRealtime, uptime);
        }
    }

    public void noteWifiScanStoppedFromSource(android.os.WorkSource ws) {
        super.noteWifiScanStoppedFromSource_enforcePermission();
        android.os.WorkSource localWs = ws != null ? new android.os.WorkSource(ws) : null;
        this.mBssExt.noteWifiScanStoppedFromSource(localWs);
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            final long uptime = android.os.SystemClock.uptimeMillis();
            final android.os.WorkSource workSource = localWs;
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda68
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$noteWifiScanStoppedFromSource$80(workSource, elapsedRealtime, uptime);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteWifiScanStoppedFromSource$80(android.os.WorkSource localWs, long elapsedRealtime, long uptime) {
        synchronized (this.mStats) {
            this.mStats.noteWifiScanStoppedFromSourceLocked(localWs, elapsedRealtime, uptime);
        }
    }

    public void noteWifiBatchedScanStartedFromSource(android.os.WorkSource ws, final int csph) {
        super.noteWifiBatchedScanStartedFromSource_enforcePermission();
        final android.os.WorkSource localWs = ws != null ? new android.os.WorkSource(ws) : null;
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            final long uptime = android.os.SystemClock.uptimeMillis();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda105
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$noteWifiBatchedScanStartedFromSource$81(localWs, csph, elapsedRealtime, uptime);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteWifiBatchedScanStartedFromSource$81(android.os.WorkSource localWs, int csph, long elapsedRealtime, long uptime) {
        synchronized (this.mStats) {
            this.mStats.noteWifiBatchedScanStartedFromSourceLocked(localWs, csph, elapsedRealtime, uptime);
        }
    }

    public void noteWifiBatchedScanStoppedFromSource(android.os.WorkSource ws) {
        super.noteWifiBatchedScanStoppedFromSource_enforcePermission();
        final android.os.WorkSource localWs = ws != null ? new android.os.WorkSource(ws) : null;
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            final long uptime = android.os.SystemClock.uptimeMillis();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$noteWifiBatchedScanStoppedFromSource$82(localWs, elapsedRealtime, uptime);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteWifiBatchedScanStoppedFromSource$82(android.os.WorkSource localWs, long elapsedRealtime, long uptime) {
        synchronized (this.mStats) {
            this.mStats.noteWifiBatchedScanStoppedFromSourceLocked(localWs, elapsedRealtime, uptime);
        }
    }

    public void noteNetworkInterfaceForTransports(final java.lang.String iface, final int[] transportTypes) {
        super.noteNetworkInterfaceForTransports_enforcePermission();
        synchronized (this.mLock) {
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda96
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$noteNetworkInterfaceForTransports$83(iface, transportTypes);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteNetworkInterfaceForTransports$83(java.lang.String iface, int[] transportTypes) {
        this.mStats.noteNetworkInterfaceForTransports(iface, transportTypes);
    }

    public void noteNetworkStatsEnabled() {
        super.noteNetworkStatsEnabled_enforcePermission();
        synchronized (this.mLock) {
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda69
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$noteNetworkStatsEnabled$84();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteNetworkStatsEnabled$84() {
        this.mWorker.scheduleSync("network-stats-enabled", 6);
    }

    public void noteDeviceIdleMode(final int mode, final java.lang.String activeReason, final int activeUid) {
        super.noteDeviceIdleMode_enforcePermission();
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            final long uptime = android.os.SystemClock.uptimeMillis();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda71
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$noteDeviceIdleMode$85(mode, activeReason, activeUid, elapsedRealtime, uptime);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteDeviceIdleMode$85(int mode, java.lang.String activeReason, int activeUid, long elapsedRealtime, long uptime) {
        synchronized (this.mStats) {
            this.mStats.noteDeviceIdleModeLocked(mode, activeReason, activeUid, elapsedRealtime, uptime);
        }
    }

    public void notePackageInstalled(final java.lang.String pkgName, final long versionCode) {
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            final long uptime = android.os.SystemClock.uptimeMillis();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda41
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$notePackageInstalled$86(pkgName, versionCode, elapsedRealtime, uptime);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$notePackageInstalled$86(java.lang.String pkgName, long versionCode, long elapsedRealtime, long uptime) {
        synchronized (this.mStats) {
            this.mStats.notePackageInstalledLocked(pkgName, versionCode, elapsedRealtime, uptime);
        }
    }

    public void notePackageUninstalled(final java.lang.String pkgName) {
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            final long uptime = android.os.SystemClock.uptimeMillis();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda84
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$notePackageUninstalled$87(pkgName, elapsedRealtime, uptime);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$notePackageUninstalled$87(java.lang.String pkgName, long elapsedRealtime, long uptime) {
        synchronized (this.mStats) {
            this.mStats.notePackageUninstalledLocked(pkgName, elapsedRealtime, uptime);
        }
    }

    public void noteBleScanStarted(android.os.WorkSource ws, final boolean isUnoptimized) {
        super.noteBleScanStarted_enforcePermission();
        android.os.WorkSource localWs = ws != null ? new android.os.WorkSource(ws) : null;
        this.mBssExt.noteBleScanStarted(localWs, isUnoptimized);
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            final long uptime = android.os.SystemClock.uptimeMillis();
            final android.os.WorkSource workSource = localWs;
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$noteBleScanStarted$88(workSource, isUnoptimized, elapsedRealtime, uptime);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteBleScanStarted$88(android.os.WorkSource localWs, boolean isUnoptimized, long elapsedRealtime, long uptime) {
        synchronized (this.mStats) {
            this.mStats.noteBluetoothScanStartedFromSourceLocked(localWs, isUnoptimized, elapsedRealtime, uptime);
        }
    }

    public void noteBleScanStopped(android.os.WorkSource ws, final boolean isUnoptimized) {
        super.noteBleScanStopped_enforcePermission();
        android.os.WorkSource localWs = ws != null ? new android.os.WorkSource(ws) : null;
        this.mBssExt.noteBleScanStopped(localWs, isUnoptimized);
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            final long uptime = android.os.SystemClock.uptimeMillis();
            final android.os.WorkSource workSource = localWs;
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda95
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$noteBleScanStopped$89(workSource, isUnoptimized, elapsedRealtime, uptime);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteBleScanStopped$89(android.os.WorkSource localWs, boolean isUnoptimized, long elapsedRealtime, long uptime) {
        synchronized (this.mStats) {
            this.mStats.noteBluetoothScanStoppedFromSourceLocked(localWs, isUnoptimized, elapsedRealtime, uptime);
        }
    }

    public void noteBleScanReset() {
        super.noteBleScanReset_enforcePermission();
        this.mBssExt.noteBleScanReset();
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            final long uptime = android.os.SystemClock.uptimeMillis();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda44
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$noteBleScanReset$90(elapsedRealtime, uptime);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteBleScanReset$90(long elapsedRealtime, long uptime) {
        synchronized (this.mStats) {
            this.mStats.noteResetBluetoothScanLocked(elapsedRealtime, uptime);
        }
    }

    public void noteBleScanResults(android.os.WorkSource ws, final int numNewResults) {
        super.noteBleScanResults_enforcePermission();
        final android.os.WorkSource localWs = ws != null ? new android.os.WorkSource(ws) : null;
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            final long uptime = android.os.SystemClock.uptimeMillis();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda72
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$noteBleScanResults$91(localWs, numNewResults, elapsedRealtime, uptime);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteBleScanResults$91(android.os.WorkSource localWs, int numNewResults, long elapsedRealtime, long uptime) {
        synchronized (this.mStats) {
            this.mStats.noteBluetoothScanResultsFromSourceLocked(localWs, numNewResults, elapsedRealtime, uptime);
        }
    }

    public void noteWifiControllerActivity(final android.os.connectivity.WifiActivityEnergyInfo info) {
        super.noteWifiControllerActivity_enforcePermission();
        if (info == null || !info.isValid()) {
            android.util.Slog.e(TAG, "invalid wifi data given: " + info);
            return;
        }
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            final long uptime = android.os.SystemClock.uptimeMillis();
            final android.app.usage.NetworkStatsManager networkStatsManager = (android.app.usage.NetworkStatsManager) this.mContext.getSystemService(android.app.usage.NetworkStatsManager.class);
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda48
                @Override // java.lang.Runnable
                public final void run() throws java.lang.Throwable {
                    this.f$0.lambda$noteWifiControllerActivity$92(info, elapsedRealtime, uptime, networkStatsManager);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteWifiControllerActivity$92(android.os.connectivity.WifiActivityEnergyInfo info, long elapsedRealtime, long uptime, android.app.usage.NetworkStatsManager networkStatsManager) throws java.lang.Throwable {
        this.mStats.updateWifiState(info, -1L, elapsedRealtime, uptime, networkStatsManager);
    }

    public void noteBluetoothControllerActivity(final android.bluetooth.BluetoothActivityEnergyInfo info) {
        super.noteBluetoothControllerActivity_enforcePermission();
        if (info == null || !info.isValid()) {
            android.util.Slog.e(TAG, "invalid bluetooth data given: " + info);
            return;
        }
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            final long uptime = android.os.SystemClock.uptimeMillis();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda30
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$noteBluetoothControllerActivity$93(info, elapsedRealtime, uptime);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteBluetoothControllerActivity$93(android.bluetooth.BluetoothActivityEnergyInfo info, long elapsedRealtime, long uptime) {
        synchronized (this.mStats) {
            this.mStats.updateBluetoothStateLocked(info, -1L, elapsedRealtime, uptime);
        }
    }

    public void noteModemControllerActivity(final android.telephony.ModemActivityInfo info) {
        super.noteModemControllerActivity_enforcePermission();
        if (info == null) {
            android.util.Slog.e(TAG, "invalid modem data given: " + info);
            return;
        }
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            final long uptime = android.os.SystemClock.uptimeMillis();
            final android.app.usage.NetworkStatsManager networkStatsManager = (android.app.usage.NetworkStatsManager) this.mContext.getSystemService(android.app.usage.NetworkStatsManager.class);
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda65
                @Override // java.lang.Runnable
                public final void run() throws java.lang.Throwable {
                    this.f$0.lambda$noteModemControllerActivity$94(info, elapsedRealtime, uptime, networkStatsManager);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteModemControllerActivity$94(android.telephony.ModemActivityInfo info, long elapsedRealtime, long uptime, android.app.usage.NetworkStatsManager networkStatsManager) throws java.lang.Throwable {
        this.mStats.noteModemControllerActivity(info, -1L, elapsedRealtime, uptime, networkStatsManager);
    }

    public boolean isOnBattery() {
        return this.mStats.isOnBattery();
    }

    public void setBatteryState(final int status, final int health, final int plugType, final int level, final int temp, final int volt, final int chargeUAh, final int chargeFullUAh, final long chargeTimeToFullSeconds) throws java.lang.Throwable {
        super.setBatteryState_enforcePermission();
        synchronized (this.mLock) {
            try {
                try {
                    final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
                    final long uptime = android.os.SystemClock.uptimeMillis();
                    final long currentTime = java.lang.System.currentTimeMillis();
                    this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda11
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$setBatteryState$97(plugType, status, health, level, temp, volt, chargeUAh, chargeFullUAh, chargeTimeToFullSeconds, elapsedRealtime, uptime, currentTime);
                        }
                    });
                } catch (java.lang.Throwable th) {
                    th = th;
                    throw th;
                }
            } catch (java.lang.Throwable th2) {
                th = th2;
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setBatteryState$97(final int plugType, final int status, final int health, final int level, final int temp, final int volt, final int chargeUAh, final int chargeFullUAh, final long chargeTimeToFullSeconds, final long elapsedRealtime, final long uptime, final long currentTime) {
        this.mWorker.scheduleRunnable(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda88
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$setBatteryState$96(plugType, status, health, level, temp, volt, chargeUAh, chargeFullUAh, chargeTimeToFullSeconds, elapsedRealtime, uptime, currentTime);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setBatteryState$96(final int plugType, final int status, final int health, final int level, final int temp, final int volt, final int chargeUAh, final int chargeFullUAh, final long chargeTimeToFullSeconds, final long elapsedRealtime, final long uptime, final long currentTime) {
        synchronized (this.mStats) {
            boolean onBattery = com.android.server.power.stats.BatteryStatsImpl.isOnBattery(plugType, status);
            if (this.mStats.isOnBattery() == onBattery) {
                this.mStats.setBatteryStateLocked(status, health, plugType, level, temp, volt, chargeUAh, chargeFullUAh, chargeTimeToFullSeconds, elapsedRealtime, uptime, currentTime);
            } else {
                this.mWorker.scheduleSync("battery-state", 127);
                this.mWorker.scheduleRunnable(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda91
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$setBatteryState$95(status, health, plugType, level, temp, volt, chargeUAh, chargeFullUAh, chargeTimeToFullSeconds, elapsedRealtime, uptime, currentTime);
                    }
                });
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setBatteryState$95(int status, int health, int plugType, int level, int temp, int volt, int chargeUAh, int chargeFullUAh, long chargeTimeToFullSeconds, long elapsedRealtime, long uptime, long currentTime) {
        synchronized (this.mStats) {
            this.mStats.setBatteryStateLocked(status, health, plugType, level, temp, volt, chargeUAh, chargeFullUAh, chargeTimeToFullSeconds, elapsedRealtime, uptime, currentTime);
        }
    }

    public long getAwakeTimeBattery() {
        super.getAwakeTimeBattery_enforcePermission();
        return this.mStats.getAwakeTimeBattery();
    }

    public long getAwakeTimePlugged() {
        super.getAwakeTimePlugged_enforcePermission();
        return this.mStats.getAwakeTimePlugged();
    }

    final class WakeupReasonThread extends java.lang.Thread {
        private static final int MAX_REASON_SIZE = 512;
        private java.nio.charset.CharsetDecoder mDecoder;
        private java.nio.CharBuffer mUtf16Buffer;
        private java.nio.ByteBuffer mUtf8Buffer;

        WakeupReasonThread() {
            super("BatteryStats_wakeupReason");
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            android.os.Process.setThreadPriority(-2);
            this.mDecoder = java.nio.charset.StandardCharsets.UTF_8.newDecoder().onMalformedInput(java.nio.charset.CodingErrorAction.REPLACE).onUnmappableCharacter(java.nio.charset.CodingErrorAction.REPLACE).replaceWith("?");
            this.mUtf8Buffer = java.nio.ByteBuffer.allocateDirect(512);
            this.mUtf16Buffer = java.nio.CharBuffer.allocate(512);
            while (true) {
                try {
                    java.lang.String reason = waitWakeup();
                    if (reason != null) {
                        long nowElapsed = android.os.SystemClock.elapsedRealtime();
                        long nowUptime = android.os.SystemClock.uptimeMillis();
                        android.os.Trace.instantForTrack(131072L, com.android.server.am.BatteryStatsService.TRACE_TRACK_WAKEUP_REASON, nowElapsed + " " + reason);
                        com.android.server.am.BatteryStatsService.this.awaitCompletion();
                        com.android.server.am.BatteryStatsService.this.mCpuWakeupStats.noteWakeupTimeAndReason(nowElapsed, nowUptime, reason);
                        synchronized (com.android.server.am.BatteryStatsService.this.mStats) {
                            com.android.server.am.BatteryStatsService.this.mStats.noteWakeupReasonLocked(reason, nowElapsed, nowUptime);
                        }
                    } else {
                        return;
                    }
                } catch (java.lang.RuntimeException e) {
                    android.util.Slog.e(com.android.server.am.BatteryStatsService.TAG, "Failure reading wakeup reasons", e);
                    return;
                }
            }
        }

        private java.lang.String waitWakeup() {
            this.mUtf8Buffer.clear();
            this.mUtf16Buffer.clear();
            this.mDecoder.reset();
            int bytesWritten = com.android.server.am.BatteryStatsService.nativeWaitWakeup(this.mUtf8Buffer);
            if (bytesWritten < 0) {
                return null;
            }
            if (bytesWritten == 0) {
                return "unknown";
            }
            this.mUtf8Buffer.limit(bytesWritten);
            this.mDecoder.decode(this.mUtf8Buffer, this.mUtf16Buffer, true);
            this.mUtf16Buffer.flip();
            return this.mUtf16Buffer.toString();
        }
    }

    private void dumpHelp(java.io.PrintWriter pw) {
        pw.println("Battery stats (batterystats) dump options:");
        pw.println("  [--checkin] [--proto] [--history] [--history-start] [--charged] [-c]");
        pw.println("  [--daily] [--reset] [--reset-all] [--write] [--new-daily] [--read-daily]");
        pw.println("  [-h] [<package.name>]");
        pw.println("  --checkin: generate output for a checkin report; will write (and clear) the");
        pw.println("             last old completed stats when they had been reset.");
        pw.println("  -c: write the current stats in checkin format.");
        pw.println("  --proto: write the current aggregate stats (without history) in proto format.");
        pw.println("  --history: show only history data.");
        pw.println("  --history-start <num>: show only history data starting at given time offset.");
        pw.println("  --history-create-events <num>: create <num> of battery history events.");
        pw.println("  --charged: only output data since last charged.");
        pw.println("  --daily: only output full daily data.");
        pw.println("  --reset: reset the stats, clearing all current data.");
        pw.println("  --reset-all: reset the stats, clearing all current and past data.");
        pw.println("  --write: force write current collected stats to disk.");
        pw.println("  --new-daily: immediately create and write new daily stats record.");
        pw.println("  --read-daily: read-load last written daily stats.");
        pw.println("  --settings: dump the settings key/values related to batterystats");
        pw.println("  --cpu: dump cpu stats for debugging purpose");
        pw.println("  --wakeups: dump CPU wakeup history and attribution.");
        pw.println("  --power-profile: dump the power profile constants");
        pw.println("  --usage: write battery usage stats. Optional arguments:");
        pw.println("     --proto: output as a binary protobuffer");
        pw.println("     --model power-profile: use the power profile model even if measured energy is available");
        if (com.android.internal.hidden_from_bootclasspath.com.android.server.power.optimization.Flags.streamlinedBatteryStats()) {
            pw.println("  --sample: collect and dump a sample of stats for debugging purpose");
        }
        pw.println("  <package.name>: optional name of package to filter output by.");
        pw.println("  -h: print this help text.");
        pw.println("Battery stats (batterystats) commands:");
        pw.println("  enable|disable <option>");
        pw.println("    Enable or disable a running option.  Option state is not saved across boots.");
        pw.println("    Options are:");
        pw.println("      full-history: include additional detailed events in battery history:");
        pw.println("          wake_lock_in, alarms and proc events");
        pw.println("      no-auto-reset: don't automatically reset stats when unplugged");
        pw.println("      pretend-screen-off: pretend the screen is off, even if screen state changes");
    }

    private void dumpSettings(java.io.PrintWriter pw) {
        awaitCompletion();
        synchronized (this.mStats) {
            this.mStats.dumpConstantsLocked(pw);
            pw.println("Flags:");
            pw.println("    com.android.server.power.optimization.streamlined_battery_stats: " + com.android.internal.hidden_from_bootclasspath.com.android.server.power.optimization.Flags.streamlinedBatteryStats());
        }
    }

    private void dumpCpuStats(java.io.PrintWriter pw) {
        awaitCompletion();
        synchronized (this.mStats) {
            this.mStats.dumpCpuStatsLocked(pw);
        }
    }

    private void dumpStatsSample(java.io.PrintWriter pw) {
        this.mStats.dumpStatsSample(pw);
    }

    private void dumpAggregatedStats(java.io.PrintWriter pw) {
        this.mPowerStatsScheduler.aggregateAndDumpPowerStats(pw);
    }

    private void dumpPowerStatsStore(java.io.PrintWriter pw) {
        this.mPowerStatsStore.dump(new android.util.IndentingPrintWriter(pw, "  "));
    }

    private void dumpPowerStatsStoreTableOfContents(java.io.PrintWriter pw) {
        this.mPowerStatsStore.dumpTableOfContents(new android.util.IndentingPrintWriter(pw, "  "));
    }

    private void dumpMeasuredEnergyStats(java.io.PrintWriter pw) {
        awaitCompletion();
        syncStats("dump", 127);
        synchronized (this.mStats) {
            this.mStats.dumpEnergyConsumerStatsLocked(pw);
        }
    }

    private void dumpPowerProfile(java.io.PrintWriter pw) {
        synchronized (this.mStats) {
            this.mStats.dumpPowerProfileLocked(pw);
        }
    }

    private void dumpUsageStats(java.io.FileDescriptor fd, java.io.PrintWriter pw, int model, boolean proto) {
        awaitCompletion();
        syncStats("dump", 127);
        android.os.BatteryUsageStatsQuery.Builder builder = new android.os.BatteryUsageStatsQuery.Builder().setMaxStatsAgeMs(0L).includeProcessStateData().includePowerModels();
        if (model == 1) {
            builder.powerProfileModeledOnly();
        }
        android.os.BatteryUsageStatsQuery query = builder.build();
        synchronized (this.mStats) {
            this.mStats.prepareForDumpLocked();
        }
        if (com.android.internal.hidden_from_bootclasspath.com.android.server.power.optimization.Flags.streamlinedBatteryStats()) {
            this.mStats.collectPowerStatsSamples();
        }
        android.os.BatteryUsageStats batteryUsageStats = this.mBatteryUsageStatsProvider.getBatteryUsageStats(this.mStats, query);
        if (proto) {
            batteryUsageStats.dumpToProto(fd);
        } else {
            batteryUsageStats.dump(pw, "");
        }
    }

    private int doEnableOrDisable(java.io.PrintWriter pw, int i, java.lang.String[] args, boolean enable) {
        int i2 = i + 1;
        if (i2 >= args.length) {
            pw.println("Missing option argument for " + (enable ? "--enable" : "--disable"));
            dumpHelp(pw);
            return -1;
        }
        if ("full-wake-history".equals(args[i2]) || "full-history".equals(args[i2])) {
            awaitCompletion();
            synchronized (this.mStats) {
                this.mStats.setRecordAllHistoryLocked(enable);
            }
        } else if ("no-auto-reset".equals(args[i2])) {
            awaitCompletion();
            synchronized (this.mStats) {
                this.mStats.setNoAutoReset(enable);
            }
        } else if ("pretend-screen-off".equals(args[i2])) {
            awaitCompletion();
            synchronized (this.mStats) {
                this.mStats.setPretendScreenOff(enable);
            }
        } else {
            pw.println("Unknown enable/disable option: " + args[i2]);
            dumpHelp(pw);
            return -1;
        }
        return i2;
    }

    protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
        this.mMonitorEnabled = false;
        try {
            dumpUnmonitored(fd, pw, args);
        } finally {
            this.mMonitorEnabled = true;
        }
    }

    /* JADX WARN: Incorrect condition in loop: B:12:0x0032 */
    /* JADX WARN: Removed duplicated region for block: B:146:0x025a A[PHI: r22
  0x025a: PHI (r22v10 'noOutput' boolean) = (r22v7 'noOutput' boolean), (r22v8 'noOutput' boolean), (r22v11 'noOutput' boolean) binds: [B:144:0x0256, B:141:0x024a, B:139:0x023f] A[DONT_GENERATE, DONT_INLINE]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void dumpUnmonitored(java.io.FileDescriptor r41, java.io.PrintWriter r42, java.lang.String[] r43) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 1558
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.BatteryStatsService.dumpUnmonitored(java.io.FileDescriptor, java.io.PrintWriter, java.lang.String[]):void");
    }

    public android.os.connectivity.CellularBatteryStats getCellularBatteryStats() {
        android.os.connectivity.CellularBatteryStats cellularBatteryStats;
        super.getCellularBatteryStats_enforcePermission();
        awaitCompletion();
        synchronized (this.mStats) {
            cellularBatteryStats = this.mStats.getCellularBatteryStats();
        }
        return cellularBatteryStats;
    }

    public android.os.connectivity.WifiBatteryStats getWifiBatteryStats() {
        android.os.connectivity.WifiBatteryStats wifiBatteryStats;
        super.getWifiBatteryStats_enforcePermission();
        awaitCompletion();
        synchronized (this.mStats) {
            wifiBatteryStats = this.mStats.getWifiBatteryStats();
        }
        return wifiBatteryStats;
    }

    public android.os.connectivity.GpsBatteryStats getGpsBatteryStats() {
        android.os.connectivity.GpsBatteryStats gpsBatteryStats;
        super.getGpsBatteryStats_enforcePermission();
        awaitCompletion();
        synchronized (this.mStats) {
            gpsBatteryStats = this.mStats.getGpsBatteryStats();
        }
        return gpsBatteryStats;
    }

    public android.os.WakeLockStats getWakeLockStats() {
        android.os.WakeLockStats wakeLockStats;
        super.getWakeLockStats_enforcePermission();
        awaitCompletion();
        synchronized (this.mStats) {
            wakeLockStats = this.mStats.getWakeLockStats();
        }
        return wakeLockStats;
    }

    public android.os.BluetoothBatteryStats getBluetoothBatteryStats() {
        android.os.BluetoothBatteryStats bluetoothBatteryStats;
        super.getBluetoothBatteryStats_enforcePermission();
        awaitCompletion();
        synchronized (this.mStats) {
            bluetoothBatteryStats = this.mStats.getBluetoothBatteryStats();
        }
        return bluetoothBatteryStats;
    }

    public android.os.health.HealthStatsParceler takeUidSnapshot(int requestUid) {
        android.os.health.HealthStatsParceler healthStatsForUidLocked;
        if (requestUid != android.os.Binder.getCallingUid()) {
            this.mContext.enforceCallingOrSelfPermission("android.permission.BATTERY_STATS", null);
        }
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            try {
                awaitCompletion();
                if (shouldCollectExternalStats()) {
                    syncStats("get-health-stats-for-uids", 127);
                }
                synchronized (this.mStats) {
                    healthStatsForUidLocked = getHealthStatsForUidLocked(requestUid);
                }
                return healthStatsForUidLocked;
            } catch (java.lang.Exception ex) {
                android.util.Slog.w(TAG, "Crashed while writing for takeUidSnapshot(" + requestUid + ")", ex);
                throw ex;
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    public android.os.health.HealthStatsParceler[] takeUidSnapshots(int[] requestUids) {
        android.os.health.HealthStatsParceler[] results;
        if (!onlyCaller(requestUids)) {
            this.mContext.enforceCallingOrSelfPermission("android.permission.BATTERY_STATS", null);
        }
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            try {
                awaitCompletion();
                if (shouldCollectExternalStats()) {
                    syncStats("get-health-stats-for-uids", 127);
                }
                synchronized (this.mStats) {
                    int N = requestUids.length;
                    results = new android.os.health.HealthStatsParceler[N];
                    for (int i = 0; i < N; i++) {
                        results[i] = getHealthStatsForUidLocked(requestUids[i]);
                    }
                }
                return results;
            } catch (java.lang.Exception ex) {
                throw ex;
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    public void takeUidSnapshotsAsync(final int[] requestUids, final android.os.ResultReceiver resultReceiver) {
        final java.util.concurrent.Future<?> futureScheduleSync;
        if (!onlyCaller(requestUids)) {
            this.mContext.enforceCallingOrSelfPermission("android.permission.BATTERY_STATS", null);
        }
        if (shouldCollectExternalStats()) {
            futureScheduleSync = this.mWorker.scheduleSync("get-health-stats-for-uids", 127);
        } else {
            futureScheduleSync = null;
        }
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda86
            @Override // java.lang.Runnable
            public final void run() throws java.lang.Exception {
                this.f$0.lambda$takeUidSnapshotsAsync$98(futureScheduleSync, requestUids, resultReceiver);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$takeUidSnapshotsAsync$98(java.util.concurrent.Future future, int[] requestUids, android.os.ResultReceiver resultReceiver) throws java.lang.Exception {
        java.lang.Exception ex;
        if (future != null) {
            try {
                future.get();
            } catch (java.lang.InterruptedException | java.util.concurrent.ExecutionException e) {
                android.util.Slog.e(TAG, "Sync failed", e);
            }
        }
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            try {
                int count = requestUids.length;
                android.os.health.HealthStatsParceler[] results = new android.os.health.HealthStatsParceler[count];
                synchronized (this.mStats) {
                    for (int i = 0; i < count; i++) {
                        try {
                            results[i] = getHealthStatsForUidLocked(requestUids[i]);
                        } catch (java.lang.Exception ex2) {
                            throw ex2;
                        }
                    }
                }
                android.os.Bundle resultData = new android.os.Bundle(1);
                resultData.putParcelableArray("uid_snapshots", results);
                resultReceiver.send(0, resultData);
                android.os.Binder.restoreCallingIdentity(ident);
            } catch (java.lang.Throwable th) {
                ex = th;
                android.os.Binder.restoreCallingIdentity(ident);
                throw ex;
            }
        } catch (java.lang.Exception ex3) {
            throw ex3;
        } catch (java.lang.Throwable th2) {
            ex = th2;
            android.os.Binder.restoreCallingIdentity(ident);
            throw ex;
        }
    }

    private boolean shouldCollectExternalStats() {
        return android.os.SystemClock.elapsedRealtime() - this.mWorker.getLastCollectionTimeStamp() > this.mStats.getExternalStatsCollectionRateLimitMs();
    }

    private static boolean onlyCaller(int[] requestUids) {
        int caller = android.os.Binder.getCallingUid();
        for (int i : requestUids) {
            if (i != caller) {
                return false;
            }
        }
        return true;
    }

    android.os.health.HealthStatsParceler getHealthStatsForUidLocked(int requestUid) {
        com.android.server.am.HealthStatsBatteryStatsWriter writer = new com.android.server.am.HealthStatsBatteryStatsWriter();
        android.os.health.HealthStatsWriter uidWriter = new android.os.health.HealthStatsWriter(android.os.health.UidHealthStats.CONSTANTS);
        android.os.BatteryStats.Uid uid = this.mStats.getUidStats().get(requestUid);
        if (uid != null) {
            writer.writeUid(uidWriter, this.mStats, uid);
        }
        return new android.os.health.HealthStatsParceler(uidWriter);
    }

    public boolean setChargingStateUpdateDelayMillis(int delayMillis) {
        super.setChargingStateUpdateDelayMillis_enforcePermission();
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            android.content.ContentResolver contentResolver = this.mContext.getContentResolver();
            return android.provider.Settings.Global.putLong(contentResolver, "battery_charging_state_update_delay", delayMillis);
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    void updateForegroundTimeIfOnBattery(final java.lang.String packageName, final int uid, final long cpuTimeDiff) {
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            final long uptime = android.os.SystemClock.uptimeMillis();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda67
                @Override // java.lang.Runnable
                public final void run() throws java.lang.Throwable {
                    this.f$0.lambda$updateForegroundTimeIfOnBattery$99(uid, packageName, elapsedRealtime, uptime, cpuTimeDiff);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateForegroundTimeIfOnBattery$99(int uid, java.lang.String packageName, long elapsedRealtime, long uptime, long cpuTimeDiff) throws java.lang.Throwable {
        if (!isOnBattery()) {
            return;
        }
        synchronized (this.mStats) {
            try {
                try {
                    com.android.server.power.stats.BatteryStatsImpl.Uid.Proc ps = this.mStats.getProcessStatsLocked(uid, packageName, elapsedRealtime, uptime);
                    if (ps != null) {
                        ps.addForegroundTimeLocked(cpuTimeDiff);
                    }
                } catch (java.lang.Throwable th) {
                    th = th;
                    throw th;
                }
            } catch (java.lang.Throwable th2) {
                th = th2;
            }
        }
    }

    void noteCurrentTimeChanged() {
        synchronized (this.mLock) {
            final long currentTime = java.lang.System.currentTimeMillis();
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            final long uptime = android.os.SystemClock.uptimeMillis();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda10
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$noteCurrentTimeChanged$100(currentTime, elapsedRealtime, uptime);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteCurrentTimeChanged$100(long currentTime, long elapsedRealtime, long uptime) {
        synchronized (this.mStats) {
            this.mStats.noteCurrentTimeChangedLocked(currentTime, elapsedRealtime, uptime);
        }
    }

    void updateBatteryStatsOnActivityUsage(java.lang.String packageName, java.lang.String className, final int uid, int userId, final boolean resumed) {
        int i;
        synchronized (this.mLock) {
            try {
                final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
                final long uptime = android.os.SystemClock.uptimeMillis();
                this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda18
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$updateBatteryStatsOnActivityUsage$101(resumed, uid, elapsedRealtime, uptime);
                    }
                });
            } catch (java.lang.Throwable th) {
                th = th;
                while (true) {
                    try {
                        throw th;
                    } catch (java.lang.Throwable th2) {
                        th = th2;
                    }
                }
            }
        }
        if (resumed) {
            i = 1;
        } else {
            i = 0;
        }
        com.android.internal.util.FrameworkStatsLog.write(42, uid, packageName, className, i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateBatteryStatsOnActivityUsage$101(boolean resumed, int uid, long elapsedRealtime, long uptime) {
        synchronized (this.mStats) {
            if (resumed) {
                this.mStats.noteActivityResumedLocked(uid, elapsedRealtime, uptime);
            } else {
                this.mStats.noteActivityPausedLocked(uid, elapsedRealtime, uptime);
            }
        }
    }

    void noteProcessDied(final int uid, final int pid) {
        synchronized (this.mLock) {
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda76
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$noteProcessDied$102(uid, pid);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteProcessDied$102(int uid, int pid) {
        synchronized (this.mStats) {
            this.mStats.noteProcessDiedLocked(uid, pid);
        }
    }

    void reportExcessiveCpu(final int uid, final java.lang.String processName, final long uptimeSince, final long cputimeUsed) {
        synchronized (this.mLock) {
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda75
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$reportExcessiveCpu$103(uid, processName, uptimeSince, cputimeUsed);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$reportExcessiveCpu$103(int uid, java.lang.String processName, long uptimeSince, long cputimeUsed) {
        synchronized (this.mStats) {
            this.mStats.reportExcessiveCpuLocked(uid, processName, uptimeSince, cputimeUsed);
        }
    }

    void noteServiceStartRunning(final int uid, final java.lang.String pkg, final java.lang.String name) {
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            final long uptime = android.os.SystemClock.uptimeMillis();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda104
                @Override // java.lang.Runnable
                public final void run() throws java.lang.Throwable {
                    this.f$0.lambda$noteServiceStartRunning$104(uid, pkg, name, elapsedRealtime, uptime);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteServiceStartRunning$104(int uid, java.lang.String pkg, java.lang.String name, long elapsedRealtime, long uptime) throws java.lang.Throwable {
        synchronized (this.mStats) {
            try {
                try {
                    com.android.server.power.stats.BatteryStatsImpl.Uid.Pkg.Serv stats = this.mStats.getServiceStatsLocked(uid, pkg, name, elapsedRealtime, uptime);
                    stats.startRunningLocked(uptime);
                } catch (java.lang.Throwable th) {
                    th = th;
                    throw th;
                }
            } catch (java.lang.Throwable th2) {
                th = th2;
            }
        }
    }

    void noteServiceStopRunning(final int uid, final java.lang.String pkg, final java.lang.String name) {
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            final long uptime = android.os.SystemClock.uptimeMillis();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda19
                @Override // java.lang.Runnable
                public final void run() throws java.lang.Throwable {
                    this.f$0.lambda$noteServiceStopRunning$105(uid, pkg, name, elapsedRealtime, uptime);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteServiceStopRunning$105(int uid, java.lang.String pkg, java.lang.String name, long elapsedRealtime, long uptime) throws java.lang.Throwable {
        synchronized (this.mStats) {
            try {
                try {
                    com.android.server.power.stats.BatteryStatsImpl.Uid.Pkg.Serv stats = this.mStats.getServiceStatsLocked(uid, pkg, name, elapsedRealtime, uptime);
                    stats.stopRunningLocked(uptime);
                } catch (java.lang.Throwable th) {
                    th = th;
                    throw th;
                }
            } catch (java.lang.Throwable th2) {
                th = th2;
            }
        }
    }

    void noteServiceStartLaunch(final int uid, final java.lang.String pkg, final java.lang.String name) {
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            final long uptime = android.os.SystemClock.uptimeMillis();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda106
                @Override // java.lang.Runnable
                public final void run() throws java.lang.Throwable {
                    this.f$0.lambda$noteServiceStartLaunch$106(uid, pkg, name, elapsedRealtime, uptime);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteServiceStartLaunch$106(int uid, java.lang.String pkg, java.lang.String name, long elapsedRealtime, long uptime) throws java.lang.Throwable {
        synchronized (this.mStats) {
            try {
                try {
                    com.android.server.power.stats.BatteryStatsImpl.Uid.Pkg.Serv stats = this.mStats.getServiceStatsLocked(uid, pkg, name, elapsedRealtime, uptime);
                    stats.startLaunchedLocked(uptime);
                } catch (java.lang.Throwable th) {
                    th = th;
                    throw th;
                }
            } catch (java.lang.Throwable th2) {
                th = th2;
            }
        }
    }

    void noteServiceStopLaunch(final int uid, final java.lang.String pkg, final java.lang.String name) {
        synchronized (this.mLock) {
            final long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
            final long uptime = android.os.SystemClock.uptimeMillis();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.BatteryStatsService$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() throws java.lang.Throwable {
                    this.f$0.lambda$noteServiceStopLaunch$107(uid, pkg, name, elapsedRealtime, uptime);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$noteServiceStopLaunch$107(int uid, java.lang.String pkg, java.lang.String name, long elapsedRealtime, long uptime) throws java.lang.Throwable {
        synchronized (this.mStats) {
            try {
                try {
                    com.android.server.power.stats.BatteryStatsImpl.Uid.Pkg.Serv stats = this.mStats.getServiceStatsLocked(uid, pkg, name, elapsedRealtime, uptime);
                    stats.stopLaunchedLocked(uptime);
                } catch (java.lang.Throwable th) {
                    th = th;
                    throw th;
                }
            } catch (java.lang.Throwable th2) {
                th = th2;
            }
        }
    }

    public void setLastBatteryUsageStatsBeforeResetAtomPullTimestamp(long timestamp) {
        java.io.InputStream in;
        synchronized (this.mConfigFile) {
            java.util.Properties props = new java.util.Properties();
            try {
                in = this.mConfigFile.openRead();
            } catch (java.io.IOException e) {
                android.util.Slog.e(TAG, "Cannot load config file " + this.mConfigFile, e);
            }
            try {
                props.load(in);
                if (in != null) {
                    in.close();
                }
                props.put(BATTERY_USAGE_STATS_BEFORE_RESET_TIMESTAMP_PROPERTY, java.lang.String.valueOf(timestamp));
                java.io.FileOutputStream out = null;
                try {
                    out = this.mConfigFile.startWrite();
                    props.store(out, "Statsd atom pull timestamps");
                    this.mConfigFile.finishWrite(out);
                } catch (java.io.IOException e2) {
                    this.mConfigFile.failWrite(out);
                    android.util.Slog.e(TAG, "Cannot save config file " + this.mConfigFile, e2);
                }
            } catch (java.lang.Throwable th) {
                if (in != null) {
                    try {
                        in.close();
                    } catch (java.lang.Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }
    }

    public long getLastBatteryUsageStatsBeforeResetAtomPullTimestamp() {
        long j;
        java.io.InputStream in;
        synchronized (this.mConfigFile) {
            java.util.Properties props = new java.util.Properties();
            try {
                in = this.mConfigFile.openRead();
            } catch (java.io.IOException e) {
                android.util.Slog.e(TAG, "Cannot load config file " + this.mConfigFile, e);
            }
            try {
                props.load(in);
                if (in != null) {
                    in.close();
                }
                j = java.lang.Long.parseLong(props.getProperty(BATTERY_USAGE_STATS_BEFORE_RESET_TIMESTAMP_PROPERTY, "0"));
            } catch (java.lang.Throwable th) {
                if (in != null) {
                    try {
                        in.close();
                    } catch (java.lang.Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }
        return j;
    }

    public void setChargerAcOnline(boolean online, boolean forceUpdate) {
        super.setChargerAcOnline_enforcePermission();
        this.mBatteryManagerInternal.setChargerAcOnline(online, forceUpdate);
    }

    public void setBatteryLevel(int level, boolean forceUpdate) {
        super.setBatteryLevel_enforcePermission();
        this.mBatteryManagerInternal.setBatteryLevel(level, forceUpdate);
    }

    public void unplugBattery(boolean forceUpdate) {
        super.unplugBattery_enforcePermission();
        this.mBatteryManagerInternal.unplugBattery(forceUpdate);
    }

    public void resetBattery(boolean forceUpdate) {
        super.resetBattery_enforcePermission();
        this.mBatteryManagerInternal.resetBattery(forceUpdate);
    }

    public void suspendBatteryInput() {
        super.suspendBatteryInput_enforcePermission();
        this.mBatteryManagerInternal.suspendBatteryInput();
    }

    public com.android.server.am.IBatteryStatsServiceWrapper getWrapper() {
        return this.mBssWrapper;
    }

    private class BatteryStatsServiceWrapper implements com.android.server.am.IBatteryStatsServiceWrapper {
        private BatteryStatsServiceWrapper() {
        }

        @Override // com.android.server.am.IBatteryStatsServiceWrapper
        public com.android.server.power.stats.BatteryExternalStatsWorker getWorker() {
            return com.android.server.am.BatteryStatsService.this.mWorker;
        }

        @Override // com.android.server.am.IBatteryStatsServiceWrapper
        public com.android.server.power.stats.BatteryStatsImpl.UserInfoProvider getUserManagerUserInfoProvider() {
            return com.android.server.am.BatteryStatsService.this.mUserManagerUserInfoProvider;
        }

        @Override // com.android.server.am.IBatteryStatsServiceWrapper
        public com.android.server.power.stats.BatteryStatsImpl.BatteryStatsConfig getBatteryStatsConfig() {
            return com.android.server.am.BatteryStatsService.this.mBatteryStatsConfig;
        }

        @Override // com.android.server.am.IBatteryStatsServiceWrapper
        public com.android.internal.os.MonotonicClock getMonotonicClock() {
            return com.android.server.am.BatteryStatsService.this.mMonotonicClock;
        }

        @Override // com.android.server.am.IBatteryStatsServiceWrapper
        public android.os.BatteryStats.BatteryStatsDumpHelper getBatteryStatsDumpHelper() {
            return com.android.server.am.BatteryStatsService.this.mDumpHelper;
        }

        @Override // com.android.server.am.IBatteryStatsServiceWrapper
        public void awaitCompletion() {
            com.android.server.am.BatteryStatsService.this.awaitCompletion();
        }
    }
}
