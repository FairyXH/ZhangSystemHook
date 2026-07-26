package com.android.server.powerstats;

/* JADX INFO: loaded from: classes3.dex */
public class PowerStatsService extends com.android.server.SystemService {
    private static final java.lang.String DATA_STORAGE_SUBDIR = "powerstats";
    private static final int DATA_STORAGE_VERSION = 0;
    private static final boolean DEBUG = false;
    private static final double INTERVAL_RANDOM_NOISE_GENERATION_ALPHA = 50.0d;
    static final java.lang.String KEY_POWER_MONITOR_API_ENABLED = "power_monitor_api_enabled";
    private static final long MAX_POWER_MONITOR_AGE_MILLIS = 30000;
    private static final long MAX_RANDOM_NOISE_UWS = 10000000;
    private static final java.lang.String METER_CACHE_FILENAME = "meterCache";
    private static final java.lang.String METER_FILENAME = "log.powerstats.meter.0";
    private static final java.lang.String MODEL_CACHE_FILENAME = "modelCache";
    private static final java.lang.String MODEL_FILENAME = "log.powerstats.model.0";
    private static final java.lang.String RESIDENCY_CACHE_FILENAME = "residencyCache";
    private static final java.lang.String RESIDENCY_FILENAME = "log.powerstats.residency.0";
    private static final java.lang.String TAG = com.android.server.powerstats.PowerStatsService.class.getSimpleName();
    private com.android.server.powerstats.BatteryTrigger mBatteryTrigger;
    private final com.android.internal.os.Clock mClock;
    private android.content.Context mContext;
    private java.io.File mDataStoragePath;
    private final android.provider.DeviceConfigInterface mDeviceConfig;
    private final com.android.server.powerstats.PowerStatsService.DeviceConfigListener mDeviceConfigListener;
    private android.hardware.power.stats.EnergyConsumer[] mEnergyConsumers;
    private android.hardware.power.stats.Channel[] mEnergyMeters;
    private android.os.Handler mHandler;
    private final com.android.server.powerstats.PowerStatsService.Injector mInjector;
    private com.android.server.powerstats.IntervalRandomNoiseGenerator mIntervalRandomNoiseGenerator;
    private android.os.Looper mLooper;
    private boolean mPowerMonitorApiEnabled;
    private com.android.server.powerstats.PowerStatsService.PowerMonitorState[] mPowerMonitorStates;
    private volatile android.os.PowerMonitor[] mPowerMonitors;
    private android.power.PowerStatsInternal mPowerStatsInternal;
    private com.android.server.powerstats.PowerStatsLogger mPowerStatsLogger;
    private com.android.server.powerstats.StatsPullAtomCallbackImpl mPullAtomCallback;
    private final android.os.IBinder mService;
    private com.android.server.powerstats.TimerTrigger mTimerTrigger;

    static class Injector {
        private com.android.server.powerstats.PowerStatsHALWrapper.IPowerStatsHALWrapper mPowerStatsHALWrapper;

        Injector() {
        }

        com.android.internal.os.Clock getClock() {
            return com.android.internal.os.Clock.SYSTEM_CLOCK;
        }

        java.io.File createDataStoragePath() {
            return new java.io.File(android.os.Environment.getDataSystemDeDirectory(0), com.android.server.powerstats.PowerStatsService.DATA_STORAGE_SUBDIR);
        }

        java.lang.String createMeterFilename() {
            return com.android.server.powerstats.PowerStatsService.METER_FILENAME;
        }

        java.lang.String createModelFilename() {
            return com.android.server.powerstats.PowerStatsService.MODEL_FILENAME;
        }

        java.lang.String createResidencyFilename() {
            return com.android.server.powerstats.PowerStatsService.RESIDENCY_FILENAME;
        }

        java.lang.String createMeterCacheFilename() {
            return com.android.server.powerstats.PowerStatsService.METER_CACHE_FILENAME;
        }

        java.lang.String createModelCacheFilename() {
            return com.android.server.powerstats.PowerStatsService.MODEL_CACHE_FILENAME;
        }

        java.lang.String createResidencyCacheFilename() {
            return com.android.server.powerstats.PowerStatsService.RESIDENCY_CACHE_FILENAME;
        }

        com.android.server.powerstats.PowerStatsHALWrapper.IPowerStatsHALWrapper createPowerStatsHALWrapperImpl() {
            return com.android.server.powerstats.PowerStatsHALWrapper.getPowerStatsHalImpl();
        }

        com.android.server.powerstats.PowerStatsHALWrapper.IPowerStatsHALWrapper getPowerStatsHALWrapperImpl() {
            com.android.server.powerstats.PowerStatsHALWrapper.IPowerStatsHALWrapper iPowerStatsHALWrapper;
            synchronized (this) {
                if (this.mPowerStatsHALWrapper == null) {
                    this.mPowerStatsHALWrapper = com.android.server.powerstats.PowerStatsHALWrapper.getPowerStatsHalImpl();
                }
                iPowerStatsHALWrapper = this.mPowerStatsHALWrapper;
            }
            return iPowerStatsHALWrapper;
        }

        com.android.server.powerstats.PowerStatsLogger createPowerStatsLogger(android.content.Context context, android.os.Looper looper, java.io.File dataStoragePath, java.lang.String meterFilename, java.lang.String meterCacheFilename, java.lang.String modelFilename, java.lang.String modelCacheFilename, java.lang.String residencyFilename, java.lang.String residencyCacheFilename, com.android.server.powerstats.PowerStatsHALWrapper.IPowerStatsHALWrapper powerStatsHALWrapper) {
            return new com.android.server.powerstats.PowerStatsLogger(context, looper, dataStoragePath, meterFilename, meterCacheFilename, modelFilename, modelCacheFilename, residencyFilename, residencyCacheFilename, powerStatsHALWrapper);
        }

        com.android.server.powerstats.BatteryTrigger createBatteryTrigger(android.content.Context context, com.android.server.powerstats.PowerStatsLogger powerStatsLogger) {
            return new com.android.server.powerstats.BatteryTrigger(context, powerStatsLogger, true);
        }

        com.android.server.powerstats.TimerTrigger createTimerTrigger(android.content.Context context, com.android.server.powerstats.PowerStatsLogger powerStatsLogger) {
            return new com.android.server.powerstats.TimerTrigger(context, powerStatsLogger, true);
        }

        com.android.server.powerstats.StatsPullAtomCallbackImpl createStatsPullerImpl(android.content.Context context, android.power.PowerStatsInternal powerStatsInternal) {
            return new com.android.server.powerstats.StatsPullAtomCallbackImpl(context, powerStatsInternal);
        }

        android.provider.DeviceConfigInterface getDeviceConfig() {
            return android.provider.DeviceConfigInterface.REAL;
        }

        com.android.server.powerstats.IntervalRandomNoiseGenerator createIntervalRandomNoiseGenerator() {
            return new com.android.server.powerstats.IntervalRandomNoiseGenerator(com.android.server.powerstats.PowerStatsService.INTERVAL_RANDOM_NOISE_GENERATION_ALPHA);
        }
    }

    /* JADX INFO: renamed from: com.android.server.powerstats.PowerStatsService$1, reason: invalid class name */
    class AnonymousClass1 extends android.os.IPowerStatsService.Stub {
        AnonymousClass1() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$getSupportedPowerMonitors$0(android.os.ResultReceiver resultReceiver) {
            com.android.server.powerstats.PowerStatsService.this.getSupportedPowerMonitorsImpl(resultReceiver);
        }

        public void getSupportedPowerMonitors(final android.os.ResultReceiver resultReceiver) {
            com.android.server.powerstats.PowerStatsService.this.getHandler().post(new java.lang.Runnable() { // from class: com.android.server.powerstats.PowerStatsService$1$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$getSupportedPowerMonitors$0(resultReceiver);
                }
            });
        }

        public void getPowerMonitorReadings(final int[] powerMonitorIds, final android.os.ResultReceiver resultReceiver) {
            final int callingUid = android.os.Binder.getCallingUid();
            com.android.server.powerstats.PowerStatsService.this.getHandler().post(new java.lang.Runnable() { // from class: com.android.server.powerstats.PowerStatsService$1$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$getPowerMonitorReadings$1(powerMonitorIds, resultReceiver, callingUid);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$getPowerMonitorReadings$1(int[] powerMonitorIds, android.os.ResultReceiver resultReceiver, int callingUid) {
            com.android.server.powerstats.PowerStatsService.this.getPowerMonitorReadingsImpl(powerMonitorIds, resultReceiver, callingUid);
        }

        protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
            if (com.android.internal.util.DumpUtils.checkDumpPermission(com.android.server.powerstats.PowerStatsService.this.mContext, com.android.server.powerstats.PowerStatsService.TAG, pw)) {
                if (com.android.server.powerstats.PowerStatsService.this.mPowerStatsLogger == null) {
                    android.util.Slog.e(com.android.server.powerstats.PowerStatsService.TAG, "PowerStats HAL is not initialized.  No data available.");
                    return;
                }
                if (args.length > 0 && "--proto".equals(args[0])) {
                    if ("model".equals(args[1])) {
                        com.android.server.powerstats.PowerStatsService.this.mPowerStatsLogger.writeModelDataToFile(fd);
                        return;
                    } else if ("meter".equals(args[1])) {
                        com.android.server.powerstats.PowerStatsService.this.mPowerStatsLogger.writeMeterDataToFile(fd);
                        return;
                    } else {
                        if ("residency".equals(args[1])) {
                            com.android.server.powerstats.PowerStatsService.this.mPowerStatsLogger.writeResidencyDataToFile(fd);
                            return;
                        }
                        return;
                    }
                }
                android.util.IndentingPrintWriter ipw = new android.util.IndentingPrintWriter(pw);
                ipw.println("PowerStatsService dumpsys: available PowerEntities");
                android.hardware.power.stats.PowerEntity[] powerEntity = com.android.server.powerstats.PowerStatsService.this.getPowerStatsHal().getPowerEntityInfo();
                ipw.increaseIndent();
                com.android.server.powerstats.ProtoStreamUtils.PowerEntityUtils.dumpsys(powerEntity, ipw);
                ipw.decreaseIndent();
                ipw.println("PowerStatsService dumpsys: available Channels");
                android.hardware.power.stats.Channel[] channel = com.android.server.powerstats.PowerStatsService.this.getPowerStatsHal().getEnergyMeterInfo();
                ipw.increaseIndent();
                com.android.server.powerstats.ProtoStreamUtils.ChannelUtils.dumpsys(channel, ipw);
                ipw.decreaseIndent();
                ipw.println("PowerStatsService dumpsys: available EnergyConsumers");
                android.hardware.power.stats.EnergyConsumer[] energyConsumer = com.android.server.powerstats.PowerStatsService.this.getPowerStatsHal().getEnergyConsumerInfo();
                ipw.increaseIndent();
                com.android.server.powerstats.ProtoStreamUtils.EnergyConsumerUtils.dumpsys(energyConsumer, ipw);
                ipw.decreaseIndent();
                ipw.println("PowerStatsService dumpsys: PowerStatsLogger stats");
                ipw.increaseIndent();
                com.android.server.powerstats.PowerStatsService.this.mPowerStatsLogger.dump(ipw);
                ipw.decreaseIndent();
            }
        }
    }

    private class DeviceConfigListener implements android.provider.DeviceConfig.OnPropertiesChangedListener {
        public java.util.concurrent.Executor mExecutor;

        private DeviceConfigListener() {
            this.mExecutor = new android.os.HandlerExecutor(com.android.server.powerstats.PowerStatsService.this.getHandler());
        }

        void startListening() {
            com.android.server.powerstats.PowerStatsService.this.mDeviceConfig.addOnPropertiesChangedListener("battery_stats", this.mExecutor, this);
        }

        public void onPropertiesChanged(android.provider.DeviceConfig.Properties properties) {
            com.android.server.powerstats.PowerStatsService.this.refreshFlags();
        }
    }

    @Override // com.android.server.SystemService
    public void onBootPhase(int phase) {
        if (phase == 500) {
            onSystemServicesReady();
        } else if (phase == 1000) {
            onBootCompleted();
        }
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        if (getPowerStatsHal().isInitialized()) {
            this.mPowerStatsInternal = new com.android.server.powerstats.PowerStatsService.LocalService();
            publishLocalService(android.power.PowerStatsInternal.class, this.mPowerStatsInternal);
        }
        publishBinderService(DATA_STORAGE_SUBDIR, this.mService);
    }

    private void onSystemServicesReady() {
        this.mPullAtomCallback = this.mInjector.createStatsPullerImpl(this.mContext, this.mPowerStatsInternal);
        this.mDeviceConfigListener.startListening();
        refreshFlags();
    }

    public boolean getDeleteMeterDataOnBoot() {
        return this.mPowerStatsLogger.getDeleteMeterDataOnBoot();
    }

    public boolean getDeleteModelDataOnBoot() {
        return this.mPowerStatsLogger.getDeleteModelDataOnBoot();
    }

    public boolean getDeleteResidencyDataOnBoot() {
        return this.mPowerStatsLogger.getDeleteResidencyDataOnBoot();
    }

    private void onBootCompleted() {
        if (getPowerStatsHal().isInitialized()) {
            this.mDataStoragePath = this.mInjector.createDataStoragePath();
            this.mPowerStatsLogger = this.mInjector.createPowerStatsLogger(this.mContext, getLooper(), this.mDataStoragePath, this.mInjector.createMeterFilename(), this.mInjector.createMeterCacheFilename(), this.mInjector.createModelFilename(), this.mInjector.createModelCacheFilename(), this.mInjector.createResidencyFilename(), this.mInjector.createResidencyCacheFilename(), getPowerStatsHal());
            this.mBatteryTrigger = this.mInjector.createBatteryTrigger(this.mContext, this.mPowerStatsLogger);
            this.mTimerTrigger = this.mInjector.createTimerTrigger(this.mContext, this.mPowerStatsLogger);
            return;
        }
        android.util.Slog.e(TAG, "Failed to start PowerStatsService loggers");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.powerstats.PowerStatsHALWrapper.IPowerStatsHALWrapper getPowerStatsHal() {
        return this.mInjector.getPowerStatsHALWrapperImpl();
    }

    private android.os.Looper getLooper() {
        synchronized (this) {
            if (this.mLooper == null) {
                android.os.HandlerThread thread = new android.os.HandlerThread(TAG);
                thread.start();
                return thread.getLooper();
            }
            return this.mLooper;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.os.Handler getHandler() {
        android.os.Handler handler;
        synchronized (this) {
            if (this.mHandler == null) {
                this.mHandler = new android.os.Handler(getLooper());
            }
            handler = this.mHandler;
        }
        return handler;
    }

    private android.hardware.power.stats.EnergyConsumer[] getEnergyConsumerInfo() {
        android.hardware.power.stats.EnergyConsumer[] energyConsumerArr;
        synchronized (this) {
            if (this.mEnergyConsumers == null) {
                this.mEnergyConsumers = getPowerStatsHal().getEnergyConsumerInfo();
            }
            energyConsumerArr = this.mEnergyConsumers;
        }
        return energyConsumerArr;
    }

    private android.hardware.power.stats.Channel[] getEnergyMeterInfo() {
        android.hardware.power.stats.Channel[] channelArr;
        synchronized (this) {
            if (this.mEnergyMeters == null) {
                this.mEnergyMeters = getPowerStatsHal().getEnergyMeterInfo();
            }
            channelArr = this.mEnergyMeters;
        }
        return channelArr;
    }

    public PowerStatsService(android.content.Context context) {
        this(context, new com.android.server.powerstats.PowerStatsService.Injector());
    }

    public PowerStatsService(android.content.Context context, com.android.server.powerstats.PowerStatsService.Injector injector) {
        super(context);
        this.mDeviceConfigListener = new com.android.server.powerstats.PowerStatsService.DeviceConfigListener();
        this.mEnergyConsumers = null;
        this.mEnergyMeters = null;
        this.mService = new com.android.server.powerstats.PowerStatsService.AnonymousClass1();
        this.mPowerMonitorApiEnabled = true;
        this.mContext = context;
        this.mInjector = injector;
        this.mClock = injector.getClock();
        this.mDeviceConfig = injector.getDeviceConfig();
    }

    void refreshFlags() {
        setPowerMonitorApiEnabled(this.mDeviceConfig.getBoolean("battery_stats", KEY_POWER_MONITOR_API_ENABLED, true));
    }

    /* JADX INFO: Access modifiers changed from: private */
    final class LocalService extends android.power.PowerStatsInternal {
        private LocalService() {
        }

        @Override // android.power.PowerStatsInternal
        public android.hardware.power.stats.EnergyConsumer[] getEnergyConsumerInfo() {
            return com.android.server.powerstats.PowerStatsService.this.getPowerStatsHal().getEnergyConsumerInfo();
        }

        @Override // android.power.PowerStatsInternal
        public java.util.concurrent.CompletableFuture<android.hardware.power.stats.EnergyConsumerResult[]> getEnergyConsumedAsync(final int[] energyConsumerIds) {
            final java.util.concurrent.CompletableFuture<android.hardware.power.stats.EnergyConsumerResult[]> future = new java.util.concurrent.CompletableFuture<>();
            com.android.server.powerstats.PowerStatsService.this.getHandler().post(new java.lang.Runnable() { // from class: com.android.server.powerstats.PowerStatsService$LocalService$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$getEnergyConsumedAsync$0(future, energyConsumerIds);
                }
            });
            return future;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$getEnergyConsumedAsync$0(java.util.concurrent.CompletableFuture future, int[] energyConsumerIds) {
            com.android.server.powerstats.PowerStatsService.this.getEnergyConsumedAsync(future, energyConsumerIds);
        }

        @Override // android.power.PowerStatsInternal
        public android.hardware.power.stats.PowerEntity[] getPowerEntityInfo() {
            return com.android.server.powerstats.PowerStatsService.this.getPowerStatsHal().getPowerEntityInfo();
        }

        @Override // android.power.PowerStatsInternal
        public java.util.concurrent.CompletableFuture<android.hardware.power.stats.StateResidencyResult[]> getStateResidencyAsync(final int[] powerEntityIds) {
            final java.util.concurrent.CompletableFuture<android.hardware.power.stats.StateResidencyResult[]> future = new java.util.concurrent.CompletableFuture<>();
            com.android.server.powerstats.PowerStatsService.this.getHandler().post(new java.lang.Runnable() { // from class: com.android.server.powerstats.PowerStatsService$LocalService$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$getStateResidencyAsync$1(future, powerEntityIds);
                }
            });
            return future;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$getStateResidencyAsync$1(java.util.concurrent.CompletableFuture future, int[] powerEntityIds) {
            com.android.server.powerstats.PowerStatsService.this.getStateResidencyAsync(future, powerEntityIds);
        }

        @Override // android.power.PowerStatsInternal
        public android.hardware.power.stats.Channel[] getEnergyMeterInfo() {
            return com.android.server.powerstats.PowerStatsService.this.getPowerStatsHal().getEnergyMeterInfo();
        }

        @Override // android.power.PowerStatsInternal
        public java.util.concurrent.CompletableFuture<android.hardware.power.stats.EnergyMeasurement[]> readEnergyMeterAsync(final int[] channelIds) {
            final java.util.concurrent.CompletableFuture<android.hardware.power.stats.EnergyMeasurement[]> future = new java.util.concurrent.CompletableFuture<>();
            com.android.server.powerstats.PowerStatsService.this.getHandler().post(new java.lang.Runnable() { // from class: com.android.server.powerstats.PowerStatsService$LocalService$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$readEnergyMeterAsync$2(future, channelIds);
                }
            });
            return future;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$readEnergyMeterAsync$2(java.util.concurrent.CompletableFuture future, int[] channelIds) {
            com.android.server.powerstats.PowerStatsService.this.readEnergyMeterAsync(future, channelIds);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getEnergyConsumedAsync(java.util.concurrent.CompletableFuture<android.hardware.power.stats.EnergyConsumerResult[]> future, int[] energyConsumerIds) {
        int expectedLength;
        android.hardware.power.stats.EnergyConsumerResult[] results = getPowerStatsHal().getEnergyConsumed(energyConsumerIds);
        android.hardware.power.stats.EnergyConsumer[] energyConsumers = getEnergyConsumerInfo();
        if (energyConsumers != null) {
            if (energyConsumerIds.length == 0) {
                expectedLength = energyConsumers.length;
            } else {
                expectedLength = energyConsumerIds.length;
            }
            if (results == null || expectedLength != results.length) {
                java.lang.StringBuilder sb = new java.lang.StringBuilder();
                sb.append("Requested ids:");
                if (energyConsumerIds.length == 0) {
                    sb.append("ALL");
                }
                sb.append("[");
                for (int i = 0; i < energyConsumerIds.length; i++) {
                    int id = energyConsumerIds[i];
                    sb.append(id);
                    sb.append("(type:");
                    sb.append((int) energyConsumers[id].type);
                    sb.append(",ord:");
                    sb.append(energyConsumers[id].ordinal);
                    sb.append(",name:");
                    sb.append(energyConsumers[id].name);
                    sb.append(")");
                    if (i != expectedLength - 1) {
                        sb.append(", ");
                    }
                }
                sb.append("]");
                sb.append(", Received result ids:");
                if (results == null) {
                    sb.append("null");
                } else {
                    sb.append("[");
                    int resultLength = results.length;
                    for (int i2 = 0; i2 < resultLength; i2++) {
                        int id2 = results[i2].id;
                        sb.append(id2);
                        sb.append("(type:");
                        sb.append((int) energyConsumers[id2].type);
                        sb.append(",ord:");
                        sb.append(energyConsumers[id2].ordinal);
                        sb.append(",name:");
                        sb.append(energyConsumers[id2].name);
                        sb.append(")");
                        if (i2 != resultLength - 1) {
                            sb.append(", ");
                        }
                    }
                    sb.append("]");
                }
                android.util.Slog.wtf(TAG, "Missing result from getEnergyConsumedAsync call. " + ((java.lang.Object) sb));
            }
        }
        future.complete(results);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getStateResidencyAsync(java.util.concurrent.CompletableFuture<android.hardware.power.stats.StateResidencyResult[]> future, int[] powerEntityIds) {
        future.complete(getPowerStatsHal().getStateResidency(powerEntityIds));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void readEnergyMeterAsync(java.util.concurrent.CompletableFuture<android.hardware.power.stats.EnergyMeasurement[]> future, int[] channelIds) {
        future.complete(getPowerStatsHal().readEnergyMeter(channelIds));
    }

    private static class PowerMonitorState {
        public long energyUws;
        public final int id;
        public final android.os.PowerMonitor powerMonitor;
        public long prevEnergyUws;
        public long timestampMs;

        private PowerMonitorState(android.os.PowerMonitor powerMonitor, int id) {
            this.energyUws = -1L;
            this.powerMonitor = powerMonitor;
            this.id = id;
        }
    }

    private void setPowerMonitorApiEnabled(boolean powerMonitorApiEnabled) {
        if (powerMonitorApiEnabled != this.mPowerMonitorApiEnabled) {
            this.mPowerMonitorApiEnabled = powerMonitorApiEnabled;
            this.mPowerMonitors = null;
            this.mPowerMonitorStates = null;
        }
    }

    private void ensurePowerMonitors() {
        if (this.mPowerMonitors != null) {
            return;
        }
        synchronized (this) {
            if (this.mPowerMonitors != null) {
                return;
            }
            if (this.mIntervalRandomNoiseGenerator == null) {
                this.mIntervalRandomNoiseGenerator = this.mInjector.createIntervalRandomNoiseGenerator();
            }
            if (!this.mPowerMonitorApiEnabled) {
                this.mPowerMonitors = new android.os.PowerMonitor[0];
                this.mPowerMonitorStates = new com.android.server.powerstats.PowerStatsService.PowerMonitorState[0];
                return;
            }
            java.util.List<android.os.PowerMonitor> monitors = new java.util.ArrayList<>();
            java.util.List<com.android.server.powerstats.PowerStatsService.PowerMonitorState> states = new java.util.ArrayList<>();
            int index = 0;
            android.hardware.power.stats.Channel[] channels = getEnergyMeterInfo();
            if (channels != null) {
                int length = channels.length;
                int i = 0;
                while (i < length) {
                    android.hardware.power.stats.Channel channel = channels[i];
                    android.os.PowerMonitor monitor = new android.os.PowerMonitor(index, 1, getChannelName(channel));
                    monitors.add(monitor);
                    states.add(new com.android.server.powerstats.PowerStatsService.PowerMonitorState(monitor, channel.id));
                    i++;
                    index++;
                }
            }
            android.hardware.power.stats.EnergyConsumer[] energyConsumers = getEnergyConsumerInfo();
            if (energyConsumers != null) {
                int length2 = energyConsumers.length;
                int i2 = 0;
                while (i2 < length2) {
                    android.hardware.power.stats.EnergyConsumer consumer = energyConsumers[i2];
                    android.os.PowerMonitor monitor2 = new android.os.PowerMonitor(index, 0, getEnergyConsumerName(consumer, energyConsumers));
                    monitors.add(monitor2);
                    states.add(new com.android.server.powerstats.PowerStatsService.PowerMonitorState(monitor2, consumer.id));
                    i2++;
                    index++;
                }
            }
            this.mPowerMonitors = (android.os.PowerMonitor[]) monitors.toArray(new android.os.PowerMonitor[monitors.size()]);
            this.mPowerMonitorStates = (com.android.server.powerstats.PowerStatsService.PowerMonitorState[]) states.toArray(new com.android.server.powerstats.PowerStatsService.PowerMonitorState[monitors.size()]);
        }
    }

    private java.lang.String getChannelName(android.hardware.power.stats.Channel c) {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        sb.append('[').append(c.name).append("]:");
        if (c.subsystem != null) {
            sb.append(c.subsystem);
        }
        return sb.toString();
    }

    private java.lang.String getEnergyConsumerName(android.hardware.power.stats.EnergyConsumer consumer, android.hardware.power.stats.EnergyConsumer[] energyConsumers) {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        switch (consumer.type) {
            case 1:
                sb.append("BLUETOOTH");
                break;
            case 2:
                sb.append("CPU");
                break;
            case 3:
                sb.append(com.android.server.display.config.SensorData.TEMPERATURE_TYPE_DISPLAY);
                break;
            case 4:
                sb.append("GNSS");
                break;
            case 5:
                sb.append("MOBILE_RADIO");
                break;
            case 6:
                sb.append("WIFI");
                break;
            case 7:
                sb.append("CAMERA");
                break;
            default:
                if (consumer.name != null && !consumer.name.isBlank()) {
                    sb.append(consumer.name.toUpperCase(java.util.Locale.ENGLISH));
                } else {
                    sb.append("CONSUMER_").append((int) consumer.type);
                }
                break;
        }
        int i = 0;
        boolean hasOrdinal = consumer.ordinal != 0;
        if (!hasOrdinal) {
            int length = energyConsumers.length;
            while (true) {
                if (i < length) {
                    android.hardware.power.stats.EnergyConsumer aConsumer = energyConsumers[i];
                    if (aConsumer.type != consumer.type || aConsumer.ordinal == 0) {
                        i++;
                    } else {
                        hasOrdinal = true;
                    }
                }
            }
        }
        if (hasOrdinal) {
            sb.append('/').append(consumer.ordinal);
        }
        return sb.toString();
    }

    public void getSupportedPowerMonitorsImpl(android.os.ResultReceiver resultReceiver) {
        ensurePowerMonitors();
        android.os.Bundle result = new android.os.Bundle();
        result.putParcelableArray("monitors", this.mPowerMonitors);
        resultReceiver.send(0, result);
    }

    public void getPowerMonitorReadingsImpl(int[] powerMonitorIndices, android.os.ResultReceiver resultReceiver, int callingUid) {
        ensurePowerMonitors();
        long earliestTimestamp = Long.MAX_VALUE;
        com.android.server.powerstats.PowerStatsService.PowerMonitorState[] powerMonitorStates = new com.android.server.powerstats.PowerStatsService.PowerMonitorState[powerMonitorIndices.length];
        for (int i = 0; i < powerMonitorIndices.length; i++) {
            int index = powerMonitorIndices[i];
            if (index < 0 || index >= this.mPowerMonitorStates.length) {
                resultReceiver.send(1, null);
                return;
            }
            powerMonitorStates[i] = this.mPowerMonitorStates[index];
            if (this.mPowerMonitorStates[index] != null && this.mPowerMonitorStates[index].timestampMs < earliestTimestamp) {
                earliestTimestamp = this.mPowerMonitorStates[index].timestampMs;
            }
        }
        if (earliestTimestamp == 0 || this.mClock.elapsedRealtime() - earliestTimestamp > 30000) {
            updateEnergyConsumers(powerMonitorStates);
            updateEnergyMeasurements(powerMonitorStates);
            this.mIntervalRandomNoiseGenerator.refresh();
        }
        long[] energy = new long[powerMonitorStates.length];
        long[] timestamps = new long[powerMonitorStates.length];
        for (int i2 = 0; i2 < powerMonitorStates.length; i2++) {
            com.android.server.powerstats.PowerStatsService.PowerMonitorState state = powerMonitorStates[i2];
            if (state.energyUws != -1 && state.prevEnergyUws != -1) {
                energy[i2] = this.mIntervalRandomNoiseGenerator.addNoise(java.lang.Math.max(state.prevEnergyUws, state.energyUws - MAX_RANDOM_NOISE_UWS), state.energyUws, callingUid);
            } else {
                energy[i2] = state.energyUws;
            }
            timestamps[i2] = state.timestampMs;
        }
        android.os.Bundle result = new android.os.Bundle();
        result.putLongArray("energy", energy);
        result.putLongArray("timestamps", timestamps);
        resultReceiver.send(0, result);
    }

    private void updateEnergyConsumers(com.android.server.powerstats.PowerStatsService.PowerMonitorState[] powerMonitorStates) {
        android.hardware.power.stats.EnergyConsumerResult[] energyConsumerResults;
        int[] ids = collectIds(powerMonitorStates, 0);
        if (ids == null || (energyConsumerResults = getPowerStatsHal().getEnergyConsumed(ids)) == null) {
            return;
        }
        for (com.android.server.powerstats.PowerStatsService.PowerMonitorState powerMonitorState : powerMonitorStates) {
            if (powerMonitorState.powerMonitor.getType() == 0) {
                int length = energyConsumerResults.length;
                int i = 0;
                while (true) {
                    if (i < length) {
                        android.hardware.power.stats.EnergyConsumerResult energyConsumerResult = energyConsumerResults[i];
                        if (energyConsumerResult.id != powerMonitorState.id) {
                            i++;
                        } else {
                            powerMonitorState.prevEnergyUws = powerMonitorState.energyUws;
                            powerMonitorState.energyUws = energyConsumerResult.energyUWs;
                            powerMonitorState.timestampMs = energyConsumerResult.timestampMs;
                            break;
                        }
                    }
                }
            }
        }
    }

    private void updateEnergyMeasurements(com.android.server.powerstats.PowerStatsService.PowerMonitorState[] powerMonitorStates) {
        android.hardware.power.stats.EnergyMeasurement[] energyMeasurements;
        int[] ids = collectIds(powerMonitorStates, 1);
        if (ids == null || (energyMeasurements = getPowerStatsHal().readEnergyMeter(ids)) == null) {
            return;
        }
        for (com.android.server.powerstats.PowerStatsService.PowerMonitorState powerMonitorState : powerMonitorStates) {
            if (powerMonitorState.powerMonitor.getType() == 1) {
                int length = energyMeasurements.length;
                int i = 0;
                while (true) {
                    if (i < length) {
                        android.hardware.power.stats.EnergyMeasurement energyMeasurement = energyMeasurements[i];
                        if (energyMeasurement.id != powerMonitorState.id) {
                            i++;
                        } else {
                            powerMonitorState.prevEnergyUws = powerMonitorState.energyUws;
                            powerMonitorState.energyUws = energyMeasurement.energyUWs;
                            powerMonitorState.timestampMs = energyMeasurement.timestampMs;
                            break;
                        }
                    }
                }
            }
        }
    }

    private int[] collectIds(com.android.server.powerstats.PowerStatsService.PowerMonitorState[] powerMonitorStates, int type) {
        int count = 0;
        for (com.android.server.powerstats.PowerStatsService.PowerMonitorState powerMonitorState : powerMonitorStates) {
            if (powerMonitorState.powerMonitor.getType() == type) {
                count++;
            }
        }
        if (count == 0) {
            return null;
        }
        int[] ids = new int[count];
        int index = 0;
        for (com.android.server.powerstats.PowerStatsService.PowerMonitorState monitorState : powerMonitorStates) {
            if (monitorState.powerMonitor.getType() == type) {
                ids[index] = monitorState.id;
                index++;
            }
        }
        return ids;
    }
}
