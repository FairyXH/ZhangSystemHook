package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public final class BatteryService extends com.android.server.SystemService {
    private static final long BATTERY_LEVEL_CHANGE_THROTTLE_MS = 60000;
    private static final int BATTERY_PLUGGED_NONE = 0;
    private static final int BATTERY_SCALE = 100;
    private static final java.lang.String DUMPSYS_DATA_PATH = "/data/system/";
    private static final long HEALTH_HAL_WAIT_MS = 1000;
    private static final int MAX_BATTERY_LEVELS_QUEUE_SIZE = 100;
    static final int OPTION_FORCE_UPDATE = 1;
    private static java.lang.String sSystemUiPackage;
    private android.app.ActivityManagerInternal mActivityManagerInternal;
    private android.os.Bundle mBatteryChangedOptions;
    private boolean mBatteryInputSuspended;
    private boolean mBatteryLevelCritical;
    private boolean mBatteryLevelLow;
    private java.util.ArrayDeque<android.os.Bundle> mBatteryLevelsEventQueue;
    private int mBatteryNearlyFullLevel;
    private android.os.Bundle mBatteryOptions;
    private com.android.server.BatteryService.BatteryPropertiesRegistrar mBatteryPropertiesRegistrar;
    public com.android.server.IBatteryServiceExt mBatteryServiceExt;
    private final com.android.internal.app.IBatteryStats mBatteryStats;
    com.android.server.BatteryService.BinderService mBinderService;
    private int mChargeStartLevel;
    private long mChargeStartTime;
    private final java.util.concurrent.CopyOnWriteArraySet<android.os.BatteryManagerInternal.ChargingPolicyChangeListener> mChargingPolicyChangeListeners;
    private final android.content.Context mContext;
    private int mCriticalBatteryLevel;
    private int mDischargeStartLevel;
    private long mDischargeStartTime;
    private final android.os.Handler mHandler;
    private android.hardware.health.HealthInfo mHealthInfo;
    private com.android.server.health.HealthServiceWrapper mHealthServiceWrapper;
    private int mInvalidCharger;
    private int mLastBatteryCycleCount;
    private int mLastBatteryHealth;
    private int mLastBatteryLevel;
    private long mLastBatteryLevelChangedSentMs;
    private boolean mLastBatteryLevelCritical;
    private boolean mLastBatteryPresent;
    private int mLastBatteryStatus;
    private int mLastBatteryTemperature;
    private int mLastBatteryVoltage;
    private int mLastChargeCounter;
    private int mLastChargingPolicy;
    private int mLastCharingState;
    private final android.hardware.health.HealthInfo mLastHealthInfo;
    private int mLastInvalidCharger;
    private int mLastLowBatteryWarningLevel;
    private int mLastMaxChargingCurrent;
    private int mLastMaxChargingVoltage;
    private int mLastPlugType;
    private com.android.server.BatteryService.Led mLed;
    private final java.lang.Object mLock;
    private int mLowBatteryCloseWarningLevel;
    private int mLowBatteryWarningLevel;
    private com.android.internal.logging.MetricsLogger mMetricsLogger;
    private int mPlugType;
    private android.os.Bundle mPowerOptions;
    private boolean mSentLowBatteryBroadcast;
    private int mSequence;
    private int mShutdownBatteryTemperature;
    private boolean mShutdownIfNoPower;
    private boolean mUpdatesStopped;
    private static final java.lang.String TAG = com.android.server.BatteryService.class.getSimpleName();
    static boolean DEBUG = false;
    private static final java.lang.String[] DUMPSYS_ARGS = {"--checkin", "--unplugged"};

    public BatteryService(android.content.Context context) {
        super(context);
        this.mLock = new java.lang.Object();
        this.mLastHealthInfo = new android.hardware.health.HealthInfo();
        this.mSequence = 1;
        this.mLastPlugType = -1;
        this.mSentLowBatteryBroadcast = false;
        this.mChargingPolicyChangeListeners = new java.util.concurrent.CopyOnWriteArraySet<>();
        this.mBatteryChangedOptions = android.app.BroadcastOptions.makeBasic().setDeliveryGroupPolicy(1).setDeferralPolicy(2).toBundle();
        this.mPowerOptions = android.app.BroadcastOptions.makeBasic().setDeliveryGroupPolicy(1).setDeliveryGroupMatchingKey(com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME, "android.intent.action.ACTION_POWER_CONNECTED").setDeferralPolicy(2).toBundle();
        this.mBatteryOptions = android.app.BroadcastOptions.makeBasic().setDeliveryGroupPolicy(1).setDeliveryGroupMatchingKey(com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME, "android.intent.action.BATTERY_OKAY").setDeferralPolicy(2).toBundle();
        this.mBatteryServiceExt = (com.android.server.IBatteryServiceExt) system.ext.loader.core.ExtLoader.type(com.android.server.IBatteryServiceExt.class).base(this).create();
        this.mContext = context;
        this.mHandler = new android.os.Handler(true);
        this.mLed = new com.android.server.BatteryService.Led(context, (com.android.server.lights.LightsManager) getLocalService(com.android.server.lights.LightsManager.class));
        this.mBatteryStats = com.android.server.am.BatteryStatsService.getService();
        this.mActivityManagerInternal = (android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class);
        this.mCriticalBatteryLevel = this.mContext.getResources().getInteger(android.R.integer.config_carDockKeepsScreenOn);
        this.mLowBatteryWarningLevel = this.mContext.getResources().getInteger(android.R.integer.config_letterboxDefaultPositionForBookModeReachability);
        this.mLowBatteryCloseWarningLevel = this.mLowBatteryWarningLevel + this.mContext.getResources().getInteger(android.R.integer.config_letterboxBackgroundType);
        this.mShutdownBatteryTemperature = this.mContext.getResources().getInteger(android.R.integer.config_satellite_stay_at_listening_from_receiving_millis);
        this.mShutdownIfNoPower = this.mContext.getResources().getBoolean(android.R.bool.config_safe_media_disable_on_volume_up);
        sSystemUiPackage = this.mContext.getResources().getString(android.R.string.config_systemUi);
        this.mBatteryLevelsEventQueue = new java.util.ArrayDeque<>();
        this.mMetricsLogger = new com.android.internal.logging.MetricsLogger();
        this.mBatteryServiceExt.initBatteryServiceExtImpl(context, this, this.mLed);
        if (new java.io.File("/sys/devices/virtual/switch/invalid_charger/state").exists()) {
            android.os.UEventObserver invalidChargerObserver = new android.os.UEventObserver() { // from class: com.android.server.BatteryService.1
                public void onUEvent(android.os.UEventObserver.UEvent uEvent) {
                    boolean zEquals = "1".equals(uEvent.get("SWITCH_STATE"));
                    synchronized (com.android.server.BatteryService.this.mLock) {
                        if (com.android.server.BatteryService.this.mInvalidCharger != zEquals) {
                            com.android.server.BatteryService.this.mInvalidCharger = zEquals ? 1 : 0;
                        }
                    }
                }
            };
            invalidChargerObserver.startObserving("DEVPATH=/devices/virtual/switch/invalid_charger");
        }
        this.mBatteryInputSuspended = ((java.lang.Boolean) android.sysprop.PowerProperties.battery_input_suspended().orElse(false)).booleanValue();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.android.server.SystemService
    public void onStart() {
        this.mBatteryServiceExt.onStart();
        registerHealthCallback();
        this.mBinderService = new com.android.server.BatteryService.BinderService();
        publishBinderService("battery", this.mBinderService);
        this.mBatteryPropertiesRegistrar = new com.android.server.BatteryService.BatteryPropertiesRegistrar();
        publishBinderService("batteryproperties", this.mBatteryPropertiesRegistrar);
        publishLocalService(android.os.BatteryManagerInternal.class, new com.android.server.BatteryService.LocalService());
    }

    @Override // com.android.server.SystemService
    public void onBootPhase(int phase) {
        synchronized (this.mLock) {
            this.mBatteryServiceExt.onBootPhase(phase);
        }
        if (phase == 550) {
            synchronized (this.mLock) {
                android.database.ContentObserver obs = new android.database.ContentObserver(this.mHandler) { // from class: com.android.server.BatteryService.2
                    @Override // android.database.ContentObserver
                    public void onChange(boolean selfChange) {
                        synchronized (com.android.server.BatteryService.this.mLock) {
                            com.android.server.BatteryService.this.updateBatteryWarningLevelLocked();
                        }
                    }
                };
                android.content.ContentResolver resolver = this.mContext.getContentResolver();
                resolver.registerContentObserver(android.provider.Settings.Global.getUriFor("low_power_trigger_level"), false, obs, -1);
                updateBatteryWarningLevelLocked();
            }
        }
    }

    private void registerHealthCallback() {
        traceBegin("HealthInitWrapper");
        try {
            try {
                this.mHealthServiceWrapper = com.android.server.health.HealthServiceWrapper.create(new com.android.server.health.HealthInfoCallback() { // from class: com.android.server.BatteryService$$ExternalSyntheticLambda6
                    @Override // com.android.server.health.HealthInfoCallback
                    public final void update(android.hardware.health.HealthInfo healthInfo) {
                        this.f$0.update(healthInfo);
                    }
                });
                traceEnd();
                traceBegin("HealthInitWaitUpdate");
                long beforeWait = android.os.SystemClock.uptimeMillis();
                synchronized (this.mLock) {
                    while (this.mHealthInfo == null) {
                        android.util.Slog.i(TAG, "health: Waited " + (android.os.SystemClock.uptimeMillis() - beforeWait) + "ms for callbacks. Waiting another 1000 ms...");
                        try {
                            this.mLock.wait(1000L);
                        } catch (java.lang.InterruptedException e) {
                            android.util.Slog.i(TAG, "health: InterruptedException when waiting for update.  Continuing...");
                        }
                    }
                }
                android.util.Slog.i(TAG, "health: Waited " + (android.os.SystemClock.uptimeMillis() - beforeWait) + "ms and received the update.");
            } finally {
                traceEnd();
            }
        } catch (android.os.RemoteException ex) {
            android.util.Slog.e(TAG, "health: cannot register callback. (RemoteException)");
            throw ex.rethrowFromSystemServer();
        } catch (java.util.NoSuchElementException ex2) {
            android.util.Slog.e(TAG, "health: cannot register callback. (no supported health HAL service)");
            throw ex2;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateBatteryWarningLevelLocked() {
        android.content.ContentResolver resolver = this.mContext.getContentResolver();
        int defWarnLevel = this.mContext.getResources().getInteger(android.R.integer.config_letterboxDefaultPositionForBookModeReachability);
        this.mLastLowBatteryWarningLevel = this.mLowBatteryWarningLevel;
        this.mLowBatteryWarningLevel = android.provider.Settings.Global.getInt(resolver, "low_power_trigger_level", defWarnLevel);
        if (this.mLowBatteryWarningLevel == 0) {
            this.mLowBatteryWarningLevel = defWarnLevel;
        }
        if (this.mLowBatteryWarningLevel < this.mCriticalBatteryLevel) {
            this.mLowBatteryWarningLevel = this.mCriticalBatteryLevel;
        }
        this.mLowBatteryCloseWarningLevel = this.mLowBatteryWarningLevel + this.mContext.getResources().getInteger(android.R.integer.config_letterboxBackgroundType);
        lambda$setChargerAcOnline$1(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isPoweredLocked(int plugTypeSet) {
        if (this.mHealthInfo.batteryStatus == 1) {
            return true;
        }
        if ((plugTypeSet & 1) != 0 && this.mHealthInfo.chargerAcOnline) {
            return true;
        }
        if ((plugTypeSet & 2) != 0 && this.mHealthInfo.chargerUsbOnline) {
            return true;
        }
        if ((plugTypeSet & 4) == 0 || !this.mHealthInfo.chargerWirelessOnline) {
            return (plugTypeSet & 8) != 0 && this.mHealthInfo.chargerDockOnline;
        }
        return true;
    }

    private boolean shouldSendBatteryLowLocked() {
        boolean plugged = this.mPlugType != 0;
        boolean oldPlugged = this.mLastPlugType != 0;
        if (plugged || this.mHealthInfo.batteryStatus == 1 || this.mHealthInfo.batteryLevel > this.mLowBatteryWarningLevel) {
            return false;
        }
        return oldPlugged || this.mLastBatteryLevel > this.mLowBatteryWarningLevel || this.mHealthInfo.batteryLevel > this.mLastLowBatteryWarningLevel;
    }

    private boolean shouldShutdownLocked() {
        return this.mHealthInfo.batteryCapacityLevel != -1 ? this.mHealthInfo.batteryCapacityLevel == 1 : this.mShutdownIfNoPower && this.mHealthInfo.batteryLevel <= 0 && this.mHealthInfo.batteryPresent && this.mHealthInfo.batteryStatus != 2;
    }

    private void shutdownIfNoPowerLocked() {
        if (shouldShutdownLocked()) {
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.BatteryService.3
                @Override // java.lang.Runnable
                public void run() {
                    if (com.android.server.BatteryService.this.mActivityManagerInternal.isSystemReady()) {
                        android.util.Slog.v(com.android.server.BatteryService.TAG, "mHealthInfo.batteryLevel = " + com.android.server.BatteryService.this.mHealthInfo.batteryLevel + "shutdown because of low power");
                        com.android.server.BatteryService.this.mBatteryServiceExt.writeEventLowBatteryPowerOff();
                        android.content.Intent intent = new android.content.Intent("com.android.internal.intent.action.REQUEST_SHUTDOWN");
                        intent.putExtra("android.intent.extra.KEY_CONFIRM", false);
                        intent.putExtra("android.intent.extra.REASON", "battery");
                        intent.setFlags(268435456);
                        com.android.server.BatteryService.this.mContext.startActivityAsUser(intent, android.os.UserHandle.CURRENT);
                        com.android.server.BatteryService.this.mLed.mLedExt.turnOffBatteryLights();
                    }
                }
            });
        }
    }

    private void shutdownIfOverTempLocked() {
        if (!this.mBatteryServiceExt.ignoreShutdownIfOverTempByOplusLocked() && this.mHealthInfo.batteryTemperatureTenthsCelsius > this.mShutdownBatteryTemperature) {
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.BatteryService.4
                @Override // java.lang.Runnable
                public void run() {
                    if (com.android.server.BatteryService.this.mActivityManagerInternal.isSystemReady()) {
                        android.content.Intent intent = new android.content.Intent("com.android.internal.intent.action.REQUEST_SHUTDOWN");
                        intent.putExtra("android.intent.extra.KEY_CONFIRM", false);
                        intent.putExtra("android.intent.extra.REASON", "thermal,battery");
                        intent.setFlags(268435456);
                        com.android.server.BatteryService.this.mContext.startActivityAsUser(intent, android.os.UserHandle.CURRENT);
                    }
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void update(android.hardware.health.HealthInfo info) {
        traceBegin("HealthInfoUpdate");
        android.os.Trace.traceCounter(131072L, "BatteryChargeCounter", info.batteryChargeCounterUah);
        android.os.Trace.traceCounter(131072L, "BatteryCurrent", info.batteryCurrentMicroamps);
        android.os.Trace.traceCounter(131072L, "PlugType", plugType(info));
        android.os.Trace.traceCounter(131072L, "BatteryStatus", info.batteryStatus);
        if (!this.mUpdatesStopped) {
            this.mBatteryServiceExt.updateBatteryService();
        }
        synchronized (this.mLock) {
            if (this.mBatteryServiceExt.getDebugCommand()) {
                android.util.Slog.v(TAG, "update mUpdatesStopped = " + this.mUpdatesStopped);
            }
            if (!this.mUpdatesStopped) {
                this.mHealthInfo = info;
                lambda$setChargerAcOnline$1(false);
                this.mLock.notifyAll();
            } else {
                com.android.server.health.Utils.copyV1Battery(this.mLastHealthInfo, info);
            }
        }
        this.mBatteryServiceExt.notifyTempChanged();
        traceEnd();
    }

    private static int plugType(android.hardware.health.HealthInfo healthInfo) {
        if (healthInfo.chargerAcOnline) {
            return 1;
        }
        if (healthInfo.chargerUsbOnline) {
            return 2;
        }
        if (healthInfo.chargerWirelessOnline) {
            return 4;
        }
        if (healthInfo.chargerDockOnline) {
            return 8;
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: processValuesLocked, reason: merged with bridge method [inline-methods] and merged with bridge method [inline-methods] */
    public void lambda$setChargerAcOnline$1(boolean z) {
        boolean z2 = false;
        long jElapsedRealtime = 0;
        this.mBatteryLevelCritical = this.mHealthInfo.batteryStatus != 1 && this.mHealthInfo.batteryLevel <= this.mCriticalBatteryLevel;
        this.mPlugType = plugType(this.mHealthInfo);
        this.mBatteryServiceExt.processValuesForOplusLocked(z, this.mPlugType, this.mHealthInfo);
        if (DEBUG) {
            android.util.Slog.d(TAG, "Processing new values: info=" + this.mHealthInfo + ", mBatteryLevelCritical=" + this.mBatteryLevelCritical + ", mPlugType=" + this.mPlugType);
        }
        try {
            this.mBatteryStats.setBatteryState(this.mHealthInfo.batteryStatus, this.mHealthInfo.batteryHealth, this.mPlugType, this.mHealthInfo.batteryLevel, this.mHealthInfo.batteryTemperatureTenthsCelsius, this.mHealthInfo.batteryVoltageMillivolts, this.mHealthInfo.batteryChargeCounterUah, this.mHealthInfo.batteryFullChargeUah, this.mHealthInfo.batteryChargeTimeToFullNowSeconds);
        } catch (android.os.RemoteException e) {
        }
        shutdownIfNoPowerLocked();
        shutdownIfOverTempLocked();
        boolean zShouldUpdateChargingState = z ? false : this.mBatteryServiceExt.shouldUpdateChargingState(this.mHealthInfo.batteryTemperatureTenthsCelsius, this.mLastBatteryTemperature);
        if (z || this.mHealthInfo.chargingPolicy != this.mLastChargingPolicy) {
            this.mLastChargingPolicy = this.mHealthInfo.chargingPolicy;
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.BatteryService$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.notifyChargingPolicyChanged();
                }
            });
        }
        if (z || this.mHealthInfo.batteryStatus != this.mLastBatteryStatus || this.mHealthInfo.batteryHealth != this.mLastBatteryHealth || this.mHealthInfo.batteryPresent != this.mLastBatteryPresent || this.mHealthInfo.batteryLevel != this.mLastBatteryLevel || this.mPlugType != this.mLastPlugType || this.mHealthInfo.batteryVoltageMillivolts != this.mLastBatteryVoltage || zShouldUpdateChargingState || this.mHealthInfo.maxChargingCurrentMicroamps != this.mLastMaxChargingCurrent || this.mHealthInfo.maxChargingVoltageMicrovolts != this.mLastMaxChargingVoltage || this.mHealthInfo.batteryChargeCounterUah != this.mLastChargeCounter || this.mInvalidCharger != this.mLastInvalidCharger || this.mHealthInfo.batteryCycleCount != this.mLastBatteryCycleCount || this.mHealthInfo.chargingState != this.mLastCharingState) {
            if (this.mPlugType != this.mLastPlugType) {
                if (this.mLastPlugType == 0) {
                    this.mChargeStartLevel = this.mHealthInfo.batteryLevel;
                    this.mChargeStartTime = android.os.SystemClock.elapsedRealtime();
                    android.metrics.LogMaker logMaker = new android.metrics.LogMaker(1417);
                    logMaker.setType(4);
                    logMaker.addTaggedData(1421, java.lang.Integer.valueOf(this.mPlugType));
                    logMaker.addTaggedData(1418, java.lang.Integer.valueOf(this.mHealthInfo.batteryLevel));
                    this.mMetricsLogger.write(logMaker);
                    if (this.mDischargeStartTime != 0 && this.mDischargeStartLevel != this.mHealthInfo.batteryLevel) {
                        jElapsedRealtime = android.os.SystemClock.elapsedRealtime() - this.mDischargeStartTime;
                        z2 = true;
                        android.util.EventLog.writeEvent(com.android.server.EventLogTags.BATTERY_DISCHARGE, java.lang.Long.valueOf(jElapsedRealtime), java.lang.Integer.valueOf(this.mDischargeStartLevel), java.lang.Integer.valueOf(this.mHealthInfo.batteryLevel));
                        this.mDischargeStartTime = 0L;
                    }
                } else if (this.mPlugType == 0) {
                    this.mDischargeStartTime = android.os.SystemClock.elapsedRealtime();
                    this.mDischargeStartLevel = this.mHealthInfo.batteryLevel;
                    long jElapsedRealtime2 = android.os.SystemClock.elapsedRealtime() - this.mChargeStartTime;
                    if (this.mChargeStartTime != 0 && jElapsedRealtime2 != 0) {
                        android.metrics.LogMaker logMaker2 = new android.metrics.LogMaker(1417);
                        logMaker2.setType(5);
                        logMaker2.addTaggedData(1421, java.lang.Integer.valueOf(this.mLastPlugType));
                        logMaker2.addTaggedData(1420, java.lang.Long.valueOf(jElapsedRealtime2));
                        logMaker2.addTaggedData(1418, java.lang.Integer.valueOf(this.mChargeStartLevel));
                        logMaker2.addTaggedData(1419, java.lang.Integer.valueOf(this.mHealthInfo.batteryLevel));
                        this.mMetricsLogger.write(logMaker2);
                    }
                    this.mChargeStartTime = 0L;
                }
                this.mBatteryServiceExt.onPlugChangedForOplusSysStateManager(this.mPlugType);
            }
            if (this.mHealthInfo.batteryStatus != this.mLastBatteryStatus || this.mHealthInfo.batteryHealth != this.mLastBatteryHealth || this.mHealthInfo.batteryPresent != this.mLastBatteryPresent || this.mPlugType != this.mLastPlugType) {
                android.util.EventLog.writeEvent(com.android.server.EventLogTags.BATTERY_STATUS, java.lang.Integer.valueOf(this.mHealthInfo.batteryStatus), java.lang.Integer.valueOf(this.mHealthInfo.batteryHealth), java.lang.Integer.valueOf(this.mHealthInfo.batteryPresent ? 1 : 0), java.lang.Integer.valueOf(this.mPlugType), this.mHealthInfo.batteryTechnology);
                android.os.SystemProperties.set("debug.tracing.battery_status", java.lang.Integer.toString(this.mHealthInfo.batteryStatus));
                android.os.SystemProperties.set("debug.tracing.plug_type", java.lang.Integer.toString(this.mPlugType));
            }
            if (this.mHealthInfo.batteryLevel != this.mLastBatteryLevel) {
                android.util.EventLog.writeEvent(com.android.server.EventLogTags.BATTERY_LEVEL, java.lang.Integer.valueOf(this.mHealthInfo.batteryLevel), java.lang.Integer.valueOf(this.mHealthInfo.batteryVoltageMillivolts), java.lang.Integer.valueOf(this.mHealthInfo.batteryTemperatureTenthsCelsius));
            }
            if (this.mBatteryLevelCritical && !this.mLastBatteryLevelCritical && this.mPlugType == 0) {
                z2 = true;
                jElapsedRealtime = android.os.SystemClock.elapsedRealtime() - this.mDischargeStartTime;
            }
            if (!this.mBatteryLevelLow) {
                if (this.mPlugType == 0 && this.mHealthInfo.batteryStatus != 1 && this.mHealthInfo.batteryLevel <= this.mLowBatteryWarningLevel) {
                    this.mBatteryLevelLow = true;
                }
            } else if (this.mPlugType != 0 || this.mHealthInfo.batteryLevel >= this.mLowBatteryCloseWarningLevel) {
                this.mBatteryLevelLow = false;
            } else if (z && this.mHealthInfo.batteryLevel >= this.mLowBatteryWarningLevel) {
                this.mBatteryLevelLow = false;
            }
            this.mSequence++;
            if (this.mPlugType != 0 && this.mLastPlugType == 0) {
                final android.content.Intent intent = new android.content.Intent("android.intent.action.ACTION_POWER_CONNECTED");
                intent.setFlags(67108864);
                intent.putExtra(com.android.server.storage.DeviceStorageMonitorService.EXTRA_SEQUENCE, this.mSequence);
                this.mBatteryServiceExt.appendFlagToStatusIntent(intent, android.R.raw.loaderror);
                this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.BatteryService.5
                    @Override // java.lang.Runnable
                    public void run() {
                        com.android.server.BatteryService.this.mContext.sendBroadcastAsUser(intent, android.os.UserHandle.ALL, null, com.android.server.BatteryService.this.mPowerOptions);
                    }
                });
            } else if (this.mPlugType == 0 && this.mLastPlugType != 0) {
                final android.content.Intent intent2 = new android.content.Intent("android.intent.action.ACTION_POWER_DISCONNECTED");
                intent2.setFlags(67108864);
                intent2.putExtra(com.android.server.storage.DeviceStorageMonitorService.EXTRA_SEQUENCE, this.mSequence);
                this.mBatteryServiceExt.appendFlagToStatusIntent(intent2, 1048576);
                this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.BatteryService.6
                    @Override // java.lang.Runnable
                    public void run() {
                        com.android.server.BatteryService.this.mContext.sendBroadcastAsUser(intent2, android.os.UserHandle.ALL, null, com.android.server.BatteryService.this.mPowerOptions);
                    }
                });
            }
            if (shouldSendBatteryLowLocked()) {
                this.mSentLowBatteryBroadcast = true;
                final android.content.Intent intent3 = new android.content.Intent("android.intent.action.BATTERY_LOW");
                intent3.setFlags(67108864);
                intent3.putExtra(com.android.server.storage.DeviceStorageMonitorService.EXTRA_SEQUENCE, this.mSequence);
                this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.BatteryService.7
                    @Override // java.lang.Runnable
                    public void run() {
                        com.android.server.BatteryService.this.mContext.sendBroadcastAsUser(intent3, android.os.UserHandle.ALL, null, com.android.server.BatteryService.this.mBatteryOptions);
                    }
                });
            } else if (this.mSentLowBatteryBroadcast && this.mHealthInfo.batteryLevel >= this.mLowBatteryCloseWarningLevel) {
                this.mSentLowBatteryBroadcast = false;
                final android.content.Intent intent4 = new android.content.Intent("android.intent.action.BATTERY_OKAY");
                intent4.setFlags(67108864);
                intent4.putExtra(com.android.server.storage.DeviceStorageMonitorService.EXTRA_SEQUENCE, this.mSequence);
                this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.BatteryService.8
                    @Override // java.lang.Runnable
                    public void run() {
                        com.android.server.BatteryService.this.mContext.sendBroadcastAsUser(intent4, android.os.UserHandle.ALL, null, com.android.server.BatteryService.this.mBatteryOptions);
                    }
                });
            }
            if (z || !this.mBatteryServiceExt.isNeedSkipBatteryChangedBroadcast(this.mHealthInfo, this.mPlugType, this.mInvalidCharger, zShouldUpdateChargingState)) {
                sendBatteryChangedIntentLocked();
                if (this.mLastBatteryLevel != this.mHealthInfo.batteryLevel || this.mLastPlugType != this.mPlugType) {
                    sendBatteryLevelChangedIntentLocked();
                }
                this.mLed.updateLightsLocked();
                if (z2 && jElapsedRealtime != 0) {
                    logOutlierLocked(jElapsedRealtime);
                }
                this.mLastBatteryStatus = this.mHealthInfo.batteryStatus;
                this.mLastBatteryHealth = this.mHealthInfo.batteryHealth;
                this.mLastBatteryPresent = this.mHealthInfo.batteryPresent;
                this.mLastBatteryLevel = this.mHealthInfo.batteryLevel;
                this.mLastPlugType = this.mPlugType;
                this.mLastBatteryVoltage = this.mHealthInfo.batteryVoltageMillivolts;
                this.mLastBatteryTemperature = this.mHealthInfo.batteryTemperatureTenthsCelsius;
                this.mLastMaxChargingCurrent = this.mHealthInfo.maxChargingCurrentMicroamps;
                this.mLastMaxChargingVoltage = this.mHealthInfo.maxChargingVoltageMicrovolts;
                this.mLastChargeCounter = this.mHealthInfo.batteryChargeCounterUah;
                this.mLastBatteryLevelCritical = this.mBatteryLevelCritical;
                this.mLastInvalidCharger = this.mInvalidCharger;
            }
            this.mBatteryServiceExt.saveLastStatsAfterValuesChanged();
            this.mLastBatteryStatus = this.mHealthInfo.batteryStatus;
            this.mLastBatteryHealth = this.mHealthInfo.batteryHealth;
            this.mLastBatteryPresent = this.mHealthInfo.batteryPresent;
            this.mLastBatteryLevel = this.mHealthInfo.batteryLevel;
            this.mLastPlugType = this.mPlugType;
            this.mLastBatteryVoltage = this.mHealthInfo.batteryVoltageMillivolts;
            this.mLastBatteryTemperature = this.mHealthInfo.batteryTemperatureTenthsCelsius;
            this.mLastMaxChargingCurrent = this.mHealthInfo.maxChargingCurrentMicroamps;
            this.mLastMaxChargingVoltage = this.mHealthInfo.maxChargingVoltageMicrovolts;
            this.mLastChargeCounter = this.mHealthInfo.batteryChargeCounterUah;
            this.mLastBatteryLevelCritical = this.mBatteryLevelCritical;
            this.mLastInvalidCharger = this.mInvalidCharger;
            this.mLastBatteryCycleCount = this.mHealthInfo.batteryCycleCount;
            this.mLastCharingState = this.mHealthInfo.chargingState;
        }
    }

    private void sendBatteryChangedIntentLocked() {
        final android.content.Intent intent = new android.content.Intent("android.intent.action.BATTERY_CHANGED");
        intent.addFlags(1610612736);
        int icon = getIconLocked(this.mHealthInfo.batteryLevel);
        intent.putExtra(com.android.server.storage.DeviceStorageMonitorService.EXTRA_SEQUENCE, this.mSequence);
        intent.putExtra("status", this.mHealthInfo.batteryStatus);
        intent.putExtra("health", this.mHealthInfo.batteryHealth);
        intent.putExtra("present", this.mHealthInfo.batteryPresent);
        intent.putExtra("level", this.mHealthInfo.batteryLevel);
        intent.putExtra("battery_low", this.mSentLowBatteryBroadcast);
        intent.putExtra("scale", 100);
        intent.putExtra("icon-small", icon);
        intent.putExtra("plugged", this.mPlugType);
        intent.putExtra("voltage", this.mHealthInfo.batteryVoltageMillivolts);
        intent.putExtra("temperature", this.mHealthInfo.batteryTemperatureTenthsCelsius);
        intent.putExtra("technology", this.mHealthInfo.batteryTechnology);
        intent.putExtra("invalid_charger", this.mInvalidCharger);
        intent.putExtra("max_charging_current", this.mHealthInfo.maxChargingCurrentMicroamps);
        intent.putExtra("max_charging_voltage", this.mHealthInfo.maxChargingVoltageMicrovolts);
        intent.putExtra("charge_counter", this.mHealthInfo.batteryChargeCounterUah);
        intent.putExtra("android.os.extra.CYCLE_COUNT", this.mHealthInfo.batteryCycleCount);
        intent.putExtra("android.os.extra.CHARGING_STATUS", this.mHealthInfo.chargingState);
        this.mBatteryServiceExt.appendExtraToBatteryStatusChangedIntend(intent);
        if (DEBUG) {
            android.util.Slog.d(TAG, "Sending ACTION_BATTERY_CHANGED. scale:100, info:" + this.mHealthInfo.toString() + this.mBatteryServiceExt.getBatteryStatusStrForDebug());
        }
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.BatteryService$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$sendBatteryChangedIntentLocked$0(intent);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendBatteryChangedIntentLocked$0(android.content.Intent intent) {
        broadcastBatteryChangedIntent(this.mContext, intent, this.mBatteryChangedOptions);
    }

    private static void broadcastBatteryChangedIntent(android.content.Context context, android.content.Intent intent, android.os.Bundle options) {
        android.content.Intent fgIntent = new android.content.Intent(intent);
        fgIntent.addFlags(268435456);
        fgIntent.setPackage(sSystemUiPackage);
        if (com.android.server.flags.Flags.pkgTargetedBatteryChangedNotSticky()) {
            context.sendBroadcastAsUser(fgIntent, android.os.UserHandle.ALL, null, options);
        } else {
            android.app.ActivityManager.broadcastStickyIntent(fgIntent, -1, options, -1);
        }
        android.app.ActivityManager.broadcastStickyIntent(intent, new java.lang.String[]{sSystemUiPackage}, -1, options, -1);
    }

    private void sendBatteryLevelChangedIntentLocked() {
        android.os.Bundle event = new android.os.Bundle();
        long now = android.os.SystemClock.elapsedRealtime();
        event.putInt(com.android.server.storage.DeviceStorageMonitorService.EXTRA_SEQUENCE, this.mSequence);
        event.putInt("status", this.mHealthInfo.batteryStatus);
        event.putInt("health", this.mHealthInfo.batteryHealth);
        event.putBoolean("present", this.mHealthInfo.batteryPresent);
        event.putInt("level", this.mHealthInfo.batteryLevel);
        event.putBoolean("battery_low", this.mSentLowBatteryBroadcast);
        event.putInt("scale", 100);
        event.putInt("plugged", this.mPlugType);
        event.putInt("voltage", this.mHealthInfo.batteryVoltageMillivolts);
        event.putInt("temperature", this.mHealthInfo.batteryTemperatureTenthsCelsius);
        event.putInt("charge_counter", this.mHealthInfo.batteryChargeCounterUah);
        event.putLong("android.os.extra.EVENT_TIMESTAMP", now);
        event.putInt("android.os.extra.CYCLE_COUNT", this.mHealthInfo.batteryCycleCount);
        event.putInt("android.os.extra.CHARGING_STATUS", this.mHealthInfo.chargingState);
        boolean queueWasEmpty = this.mBatteryLevelsEventQueue.isEmpty();
        this.mBatteryLevelsEventQueue.add(event);
        if (this.mBatteryLevelsEventQueue.size() > 100) {
            this.mBatteryLevelsEventQueue.removeFirst();
        }
        if (queueWasEmpty) {
            long delay = now - this.mLastBatteryLevelChangedSentMs > 60000 ? 0L : (this.mLastBatteryLevelChangedSentMs + 60000) - now;
            this.mHandler.postDelayed(new java.lang.Runnable() { // from class: com.android.server.BatteryService$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.sendEnqueuedBatteryLevelChangedEvents();
                }
            }, delay);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendEnqueuedBatteryLevelChangedEvents() {
        java.util.ArrayList<android.os.Bundle> events;
        synchronized (this.mLock) {
            events = new java.util.ArrayList<>(this.mBatteryLevelsEventQueue);
            this.mBatteryLevelsEventQueue.clear();
        }
        android.content.Intent intent = new android.content.Intent("android.intent.action.BATTERY_LEVEL_CHANGED");
        intent.addFlags(16777216);
        intent.putParcelableArrayListExtra("android.os.extra.EVENTS", events);
        this.mContext.sendBroadcastAsUser(intent, android.os.UserHandle.ALL, "android.permission.BATTERY_STATS");
        this.mLastBatteryLevelChangedSentMs = android.os.SystemClock.elapsedRealtime();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyChargingPolicyChanged() {
        int newPolicy;
        synchronized (this.mLock) {
            newPolicy = this.mLastChargingPolicy;
        }
        for (android.os.BatteryManagerInternal.ChargingPolicyChangeListener listener : this.mChargingPolicyChangeListeners) {
            listener.onChargingPolicyChanged(newPolicy);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v0, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r0v1, types: [boolean] */
    /* JADX WARN: Type inference failed for: r0v10, types: [boolean] */
    /* JADX WARN: Type inference failed for: r0v12 */
    /* JADX WARN: Type inference failed for: r0v13 */
    /* JADX WARN: Type inference failed for: r0v14 */
    /* JADX WARN: Type inference failed for: r0v3, types: [boolean] */
    /* JADX WARN: Type inference failed for: r0v8 */
    /* JADX WARN: Type inference failed for: r0v9, types: [java.lang.String] */
    private void logBatteryStatsLocked() {
        android.os.DropBoxManager dropBoxManager;
        java.lang.StringBuilder sb;
        java.lang.String strDelete = "failed to close dumpsys output stream";
        java.lang.String string = "failed to delete temporary dumpsys file: ";
        android.os.IBinder service = android.os.ServiceManager.getService("batterystats");
        if (service == null || (dropBoxManager = (android.os.DropBoxManager) this.mContext.getSystemService("dropbox")) == null || !dropBoxManager.isTagEnabled("BATTERY_DISCHARGE_INFO")) {
            return;
        }
        java.io.File file = null;
        java.io.FileOutputStream fileOutputStream = null;
        try {
            try {
                file = new java.io.File("/data/system/batterystats.dump");
                fileOutputStream = new java.io.FileOutputStream(file);
                service.dump(fileOutputStream.getFD(), DUMPSYS_ARGS);
                android.os.FileUtils.sync(fileOutputStream);
                dropBoxManager.addFile("BATTERY_DISCHARGE_INFO", file, 2);
                try {
                    fileOutputStream.close();
                } catch (java.io.IOException e) {
                    android.util.Slog.e(TAG, "failed to close dumpsys output stream");
                }
                strDelete = file.delete();
            } catch (android.os.RemoteException e2) {
                android.util.Slog.e(TAG, "failed to dump battery service", e2);
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (java.io.IOException e3) {
                        android.util.Slog.e(TAG, "failed to close dumpsys output stream");
                    }
                }
                if (file != null && (strDelete = file.delete()) == 0) {
                    java.lang.String str = TAG;
                    sb = new java.lang.StringBuilder();
                    strDelete = str;
                }
            } catch (java.io.IOException e4) {
                android.util.Slog.e(TAG, "failed to write dumpsys file", e4);
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (java.io.IOException e5) {
                        android.util.Slog.e(TAG, "failed to close dumpsys output stream");
                    }
                }
                if (file != null && (strDelete = file.delete()) == 0) {
                    java.lang.String str2 = TAG;
                    sb = new java.lang.StringBuilder();
                    strDelete = str2;
                }
            }
            if (strDelete == 0) {
                java.lang.String str3 = TAG;
                sb = new java.lang.StringBuilder();
                strDelete = str3;
                string = sb.append("failed to delete temporary dumpsys file: ").append(file.getAbsolutePath()).toString();
                android.util.Slog.e((java.lang.String) strDelete, string);
            }
        } finally {
        }
    }

    private void logOutlierLocked(long duration) {
        android.content.ContentResolver cr = this.mContext.getContentResolver();
        java.lang.String dischargeThresholdString = android.provider.Settings.Global.getString(cr, "battery_discharge_threshold");
        java.lang.String durationThresholdString = android.provider.Settings.Global.getString(cr, "battery_discharge_duration_threshold");
        if (dischargeThresholdString != null && durationThresholdString != null) {
            try {
                long durationThreshold = java.lang.Long.parseLong(durationThresholdString);
                int dischargeThreshold = java.lang.Integer.parseInt(dischargeThresholdString);
                if (duration <= durationThreshold && this.mDischargeStartLevel - this.mHealthInfo.batteryLevel >= dischargeThreshold) {
                    logBatteryStatsLocked();
                }
                if (DEBUG) {
                    android.util.Slog.v(TAG, "duration threshold: " + durationThreshold + " discharge threshold: " + dischargeThreshold);
                }
                if (DEBUG) {
                    android.util.Slog.v(TAG, "duration: " + duration + " discharge: " + (this.mDischargeStartLevel - this.mHealthInfo.batteryLevel));
                }
            } catch (java.lang.NumberFormatException e) {
                android.util.Slog.e(TAG, "Invalid DischargeThresholds GService string: " + durationThresholdString + " or " + dischargeThresholdString);
            }
        }
    }

    private int getIconLocked(int level) {
        if (this.mHealthInfo.batteryStatus == 2) {
            return android.R.drawable.sim_light_orange;
        }
        if (this.mHealthInfo.batteryStatus == 3) {
            return android.R.drawable.seekbar_thumb_unpressed_to_pressed;
        }
        if (this.mHealthInfo.batteryStatus == 4 || this.mHealthInfo.batteryStatus == 5) {
            return (!isPoweredLocked(15) || this.mHealthInfo.batteryLevel < 100) ? android.R.drawable.seekbar_thumb_unpressed_to_pressed : android.R.drawable.sim_light_orange;
        }
        return android.R.drawable.spinner_ab_default_holo_light;
    }

    class Shell extends android.os.ShellCommand {
        Shell() {
        }

        public int onCommand(java.lang.String cmd) {
            return com.android.server.BatteryService.this.onShellCommand(this, cmd);
        }

        public void onHelp() {
            java.io.PrintWriter pw = getOutPrintWriter();
            com.android.server.BatteryService.dumpHelp(pw);
        }
    }

    static void dumpHelp(java.io.PrintWriter pw) {
        pw.println("Battery service (battery) commands:");
        pw.println("  help");
        pw.println("    Print this help text.");
        java.lang.String getSetOptions = com.android.internal.hidden_from_bootclasspath.android.os.Flags.batteryServiceSupportCurrentAdbCommand() ? "ac|usb|wireless|dock|status|level|temp|present|counter|invalid|current_now|current_average" : "ac|usb|wireless|dock|status|level|temp|present|counter|invalid";
        pw.println("  get [-f] [" + getSetOptions + "]");
        pw.println("  set [-f] [" + getSetOptions + "] <value>");
        pw.println("    Force a battery property value, freezing battery state.");
        pw.println("    -f: force a battery change broadcast be sent, prints new sequence.");
        pw.println("  unplug [-f]");
        pw.println("    Force battery unplugged, freezing battery state.");
        pw.println("    -f: force a battery change broadcast be sent, prints new sequence.");
        pw.println("  reset [-f]");
        pw.println("    Unfreeze battery state, returning to current hardware values.");
        pw.println("    -f: force a battery change broadcast be sent, prints new sequence.");
        if (android.os.Build.IS_DEBUGGABLE) {
            pw.println("  suspend_input");
            pw.println("    Suspend charging even if plugged in. ");
        }
    }

    int parseOptions(com.android.server.BatteryService.Shell shell) {
        int opts = 0;
        while (true) {
            java.lang.String opt = shell.getNextOption();
            if (opt != null) {
                if ("-f".equals(opt)) {
                    opts |= 1;
                }
            } else {
                return opts;
            }
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:24:0x004e  */
    /* JADX WARN: Removed duplicated region for block: B:48:0x00da  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    int onShellCommand(com.android.server.BatteryService.Shell r22, java.lang.String r23) {
        /*
            Method dump skipped, instruction units count: 1066
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.BatteryService.onShellCommand(com.android.server.BatteryService$Shell, java.lang.String):int");
    }

    private void updateHealthInfo() {
        try {
            this.mHealthServiceWrapper.scheduleUpdate();
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Unable to update health service data.", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setChargerAcOnline(boolean online, final boolean forceUpdate) {
        if (!this.mUpdatesStopped) {
            com.android.server.health.Utils.copyV1Battery(this.mLastHealthInfo, this.mHealthInfo);
        }
        this.mHealthInfo.chargerAcOnline = online;
        this.mUpdatesStopped = true;
        android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.BatteryService$$ExternalSyntheticLambda1
            public final void runOrThrow() throws java.lang.Exception {
                this.f$0.lambda$setChargerAcOnline$1(forceUpdate);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setBatteryLevel(int level, final boolean forceUpdate) {
        if (!this.mUpdatesStopped) {
            com.android.server.health.Utils.copyV1Battery(this.mLastHealthInfo, this.mHealthInfo);
        }
        this.mHealthInfo.batteryLevel = level;
        this.mUpdatesStopped = true;
        android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.BatteryService$$ExternalSyntheticLambda4
            public final void runOrThrow() throws java.lang.Exception {
                this.f$0.lambda$setBatteryLevel$2(forceUpdate);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unplugBattery(final boolean forceUpdate, final java.io.PrintWriter pw) {
        if (!this.mUpdatesStopped) {
            com.android.server.health.Utils.copyV1Battery(this.mLastHealthInfo, this.mHealthInfo);
        }
        this.mHealthInfo.chargerAcOnline = false;
        this.mHealthInfo.chargerUsbOnline = false;
        this.mHealthInfo.chargerWirelessOnline = false;
        this.mHealthInfo.chargerDockOnline = false;
        this.mUpdatesStopped = true;
        android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.BatteryService$$ExternalSyntheticLambda2
            public final void runOrThrow() throws java.lang.Exception {
                this.f$0.lambda$unplugBattery$3(forceUpdate, pw);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetBattery(final boolean forceUpdate, final java.io.PrintWriter pw) {
        if (this.mUpdatesStopped) {
            this.mUpdatesStopped = false;
            com.android.server.health.Utils.copyV1Battery(this.mHealthInfo, this.mLastHealthInfo);
            android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.BatteryService$$ExternalSyntheticLambda5
                public final void runOrThrow() throws java.lang.Exception {
                    this.f$0.lambda$resetBattery$4(forceUpdate, pw);
                }
            });
        }
        if (this.mBatteryInputSuspended) {
            android.sysprop.PowerProperties.battery_input_suspended(false);
            this.mBatteryInputSuspended = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void suspendBatteryInput() {
        if (!android.os.Build.IS_DEBUGGABLE) {
            throw new java.lang.SecurityException("battery suspend_input is only supported on debuggable builds");
        }
        android.sysprop.PowerProperties.battery_input_suspended(true);
        this.mBatteryInputSuspended = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: processValuesLocked, reason: merged with bridge method [inline-methods] and merged with bridge method [inline-methods] */
    public void lambda$unplugBattery$3(boolean forceUpdate, java.io.PrintWriter pw) {
        lambda$setChargerAcOnline$1(forceUpdate);
        if (pw != null && forceUpdate) {
            pw.println(this.mSequence);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dumpInternal(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
        synchronized (this.mLock) {
            if (this.mBatteryServiceExt.dumpInternalBase(fd, pw, args)) {
                if (args == null || args.length == 0 || "-a".equals(args[0])) {
                    pw.println("Current Battery Service state:");
                    if (this.mUpdatesStopped) {
                        pw.println("  (UPDATES STOPPED -- use 'reset' to restart)");
                    }
                    pw.println("  AC powered: " + this.mHealthInfo.chargerAcOnline);
                    pw.println("  USB powered: " + this.mHealthInfo.chargerUsbOnline);
                    pw.println("  Wireless powered: " + this.mHealthInfo.chargerWirelessOnline);
                    pw.println("  Dock powered: " + this.mHealthInfo.chargerDockOnline);
                    pw.println("  Max charging current: " + this.mHealthInfo.maxChargingCurrentMicroamps);
                    pw.println("  Max charging voltage: " + this.mHealthInfo.maxChargingVoltageMicrovolts);
                    pw.println("  Charge counter: " + this.mHealthInfo.batteryChargeCounterUah);
                    pw.println("  status: " + this.mHealthInfo.batteryStatus);
                    pw.println("  health: " + this.mHealthInfo.batteryHealth);
                    pw.println("  present: " + this.mHealthInfo.batteryPresent);
                    pw.println("  level: " + this.mHealthInfo.batteryLevel);
                    pw.println("  scale: 100");
                    pw.println("  voltage: " + this.mHealthInfo.batteryVoltageMillivolts);
                    pw.println("  temperature: " + this.mHealthInfo.batteryTemperatureTenthsCelsius);
                    pw.println("  technology: " + this.mHealthInfo.batteryTechnology);
                    pw.println("  Charging state: " + this.mHealthInfo.chargingState);
                    pw.println("  Charging policy: " + this.mHealthInfo.chargingPolicy);
                } else {
                    com.android.server.BatteryService.Shell shell = new com.android.server.BatteryService.Shell();
                    shell.exec(this.mBinderService, null, fd, null, args, null, new android.os.ResultReceiver(null));
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dumpProto(java.io.FileDescriptor fd) {
        android.util.proto.ProtoOutputStream proto = new android.util.proto.ProtoOutputStream(fd);
        synchronized (this.mLock) {
            proto.write(1133871366145L, this.mUpdatesStopped);
            int batteryPluggedValue = 0;
            if (this.mHealthInfo.chargerAcOnline) {
                batteryPluggedValue = 1;
            } else if (this.mHealthInfo.chargerUsbOnline) {
                batteryPluggedValue = 2;
            } else if (this.mHealthInfo.chargerWirelessOnline) {
                batteryPluggedValue = 4;
            } else if (this.mHealthInfo.chargerDockOnline) {
                batteryPluggedValue = 8;
            }
            proto.write(1159641169922L, batteryPluggedValue);
            proto.write(1120986464259L, this.mHealthInfo.maxChargingCurrentMicroamps);
            proto.write(1120986464260L, this.mHealthInfo.maxChargingVoltageMicrovolts);
            proto.write(1120986464261L, this.mHealthInfo.batteryChargeCounterUah);
            proto.write(1159641169926L, this.mHealthInfo.batteryStatus);
            proto.write(1159641169927L, this.mHealthInfo.batteryHealth);
            proto.write(1133871366152L, this.mHealthInfo.batteryPresent);
            proto.write(1120986464265L, this.mHealthInfo.batteryLevel);
            proto.write(1120986464266L, 100);
            proto.write(1120986464267L, this.mHealthInfo.batteryVoltageMillivolts);
            proto.write(1120986464268L, this.mHealthInfo.batteryTemperatureTenthsCelsius);
            proto.write(1138166333453L, this.mHealthInfo.batteryTechnology);
        }
        proto.flush();
    }

    private static void traceBegin(java.lang.String name) {
        android.os.Trace.traceBegin(524288L, name);
    }

    private static void traceEnd() {
        android.os.Trace.traceEnd(524288L);
    }

    public final class Led {
        static final int LOW_BATTERY_BEHAVIOR_DEFAULT = 0;
        static final int LOW_BATTERY_BEHAVIOR_FLASHING = 2;
        static final int LOW_BATTERY_BEHAVIOR_SOLID = 1;
        private final int mBatteryFullARGB;
        private final int mBatteryLedOff;
        private final int mBatteryLedOn;
        private final com.android.server.lights.LogicalLight mBatteryLight;
        private final int mBatteryLowARGB;
        private final int mBatteryLowBehavior;
        private final int mBatteryMediumARGB;
        private com.android.server.BatteryService.Led.LedWrapper mLedWrapper = new com.android.server.BatteryService.Led.LedWrapper();
        private com.android.server.ILedExt mLedExt = (com.android.server.ILedExt) system.ext.loader.core.ExtLoader.type(com.android.server.ILedExt.class).base(this).create();

        public Led(android.content.Context context, com.android.server.lights.LightsManager lights) {
            this.mLedExt.initLedExtImpl(context, lights, this);
            this.mBatteryLight = lights.getLight(3);
            this.mBatteryLowARGB = context.getResources().getInteger(android.R.integer.config_networkAvoidBadWifi);
            this.mBatteryMediumARGB = context.getResources().getInteger(android.R.integer.config_networkMeteredMultipathPreference);
            this.mBatteryFullARGB = context.getResources().getInteger(android.R.integer.config_multiuserMaximumUsers);
            this.mBatteryLedOn = context.getResources().getInteger(android.R.integer.config_navBarOpacityMode);
            this.mBatteryLedOff = context.getResources().getInteger(android.R.integer.config_navBarInteractionMode);
            com.android.server.BatteryService.this.mBatteryNearlyFullLevel = context.getResources().getInteger(android.R.integer.config_networkNotifySwitchType);
            this.mBatteryLowBehavior = context.getResources().getInteger(android.R.integer.config_networkDefaultDailyMultipathQuotaBytes);
        }

        public void updateLightsLocked() {
            if (this.mLedExt.isIgnoreUpdateLights(com.android.server.BatteryService.this.mHealthInfo) || this.mBatteryLight == null) {
                return;
            }
            int level = com.android.server.BatteryService.this.mHealthInfo.batteryLevel;
            int status = com.android.server.BatteryService.this.mHealthInfo.batteryStatus;
            if (level < com.android.server.BatteryService.this.mLowBatteryWarningLevel) {
                switch (this.mBatteryLowBehavior) {
                    case 1:
                        this.mBatteryLight.setColor(this.mBatteryLowARGB);
                        break;
                    case 2:
                        this.mBatteryLight.setFlashing(this.mBatteryLowARGB, 1, this.mBatteryLedOn, this.mBatteryLedOff);
                        break;
                    default:
                        if (status == 2) {
                            this.mBatteryLight.setColor(this.mBatteryLowARGB);
                        } else {
                            this.mBatteryLight.setFlashing(this.mBatteryLowARGB, 1, this.mBatteryLedOn, this.mBatteryLedOff);
                        }
                        break;
                }
                return;
            }
            if (status == 2 || status == 5) {
                if (status == 5 || level >= com.android.server.BatteryService.this.mBatteryNearlyFullLevel) {
                    this.mBatteryLight.setColor(this.mBatteryFullARGB);
                    return;
                } else {
                    this.mBatteryLight.setColor(this.mBatteryMediumARGB);
                    return;
                }
            }
            this.mBatteryLight.turnOff();
        }

        public void onUpdateLights() {
            synchronized (com.android.server.BatteryService.this.mLock) {
                updateLightsLocked();
            }
        }

        public com.android.server.ILedWrapper getWrapper() {
            return this.mLedWrapper;
        }

        private class LedWrapper implements com.android.server.ILedWrapper {
            private LedWrapper() {
            }

            @Override // com.android.server.ILedWrapper
            public com.android.server.ILedExt getExtImpl() {
                return com.android.server.BatteryService.Led.this.mLedExt;
            }

            @Override // com.android.server.ILedWrapper
            public com.android.server.lights.LogicalLight getBatteryLight() {
                return com.android.server.BatteryService.Led.this.mBatteryLight;
            }
        }
    }

    private final class BinderService extends android.os.Binder {
        private BinderService() {
        }

        @Override // android.os.Binder
        protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
            if (com.android.internal.util.DumpUtils.checkDumpPermission(com.android.server.BatteryService.this.mContext, com.android.server.BatteryService.TAG, pw)) {
                if (args.length > 0 && "--proto".equals(args[0])) {
                    com.android.server.BatteryService.this.dumpProto(fd);
                } else {
                    com.android.server.BatteryService.this.dumpInternal(fd, pw, args);
                }
            }
        }

        public void onShellCommand(java.io.FileDescriptor in, java.io.FileDescriptor out, java.io.FileDescriptor err, java.lang.String[] args, android.os.ShellCallback callback, android.os.ResultReceiver resultReceiver) {
            com.android.server.BatteryService.this.new Shell().exec(this, in, out, err, args, callback, resultReceiver);
        }
    }

    private final class BatteryPropertiesRegistrar extends android.os.IBatteryPropertiesRegistrar.Stub {
        private BatteryPropertiesRegistrar() {
        }

        public int getProperty(int id, android.os.BatteryProperty prop) throws android.os.RemoteException {
            switch (id) {
                case 10:
                    if (!com.android.internal.hidden_from_bootclasspath.android.os.Flags.stateOfHealthPublic()) {
                    }
                case 7:
                case 8:
                case 9:
                case 11:
                case 12:
                    com.android.server.BatteryService.this.mContext.enforceCallingPermission("android.permission.BATTERY_STATS", null);
                    break;
            }
            return com.android.server.BatteryService.this.mHealthServiceWrapper.getProperty(id, prop);
        }

        public void scheduleUpdate() throws android.os.RemoteException {
            com.android.server.BatteryService.this.mHealthServiceWrapper.scheduleUpdate();
        }
    }

    private final class LocalService extends android.os.BatteryManagerInternal {
        private LocalService() {
        }

        public boolean isPowered(int plugTypeSet) {
            boolean zIsPoweredLocked;
            synchronized (com.android.server.BatteryService.this.mLock) {
                zIsPoweredLocked = com.android.server.BatteryService.this.isPoweredLocked(plugTypeSet);
            }
            return zIsPoweredLocked;
        }

        public int getPlugType() {
            int i;
            synchronized (com.android.server.BatteryService.this.mLock) {
                i = com.android.server.BatteryService.this.mPlugType;
            }
            return i;
        }

        public int getBatteryLevel() {
            int i;
            synchronized (com.android.server.BatteryService.this.mLock) {
                i = com.android.server.BatteryService.this.mHealthInfo.batteryLevel;
            }
            return i;
        }

        public int getBatteryChargeCounter() {
            int i;
            synchronized (com.android.server.BatteryService.this.mLock) {
                i = com.android.server.BatteryService.this.mHealthInfo.batteryChargeCounterUah;
            }
            return i;
        }

        public int getBatteryFullCharge() {
            int i;
            synchronized (com.android.server.BatteryService.this.mLock) {
                i = com.android.server.BatteryService.this.mHealthInfo.batteryFullChargeUah;
            }
            return i;
        }

        public int getBatteryHealth() {
            int i;
            synchronized (com.android.server.BatteryService.this.mLock) {
                i = com.android.server.BatteryService.this.mHealthInfo.batteryHealth;
            }
            return i;
        }

        public boolean getBatteryLevelLow() {
            boolean z;
            synchronized (com.android.server.BatteryService.this.mLock) {
                z = com.android.server.BatteryService.this.mBatteryLevelLow;
            }
            return z;
        }

        public void registerChargingPolicyChangeListener(android.os.BatteryManagerInternal.ChargingPolicyChangeListener listener) {
            com.android.server.BatteryService.this.mChargingPolicyChangeListeners.add(listener);
        }

        public int getChargingPolicy() {
            int i;
            synchronized (com.android.server.BatteryService.this.mLock) {
                i = com.android.server.BatteryService.this.mLastChargingPolicy;
            }
            return i;
        }

        public int getInvalidCharger() {
            int i;
            synchronized (com.android.server.BatteryService.this.mLock) {
                i = com.android.server.BatteryService.this.mInvalidCharger;
            }
            return i;
        }

        public void setChargerAcOnline(boolean online, boolean forceUpdate) {
            com.android.server.BatteryService.this.setChargerAcOnline(online, forceUpdate);
        }

        public void setBatteryLevel(int level, boolean forceUpdate) {
            com.android.server.BatteryService.this.setBatteryLevel(level, forceUpdate);
        }

        public void unplugBattery(boolean forceUpdate) {
            com.android.server.BatteryService.this.unplugBattery(forceUpdate, null);
        }

        public void resetBattery(boolean forceUpdate) {
            com.android.server.BatteryService.this.resetBattery(forceUpdate, null);
        }

        public void suspendBatteryInput() {
            com.android.server.BatteryService.this.suspendBatteryInput();
        }
    }
}
