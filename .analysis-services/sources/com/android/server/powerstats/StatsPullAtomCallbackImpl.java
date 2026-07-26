package com.android.server.powerstats;

/* JADX INFO: loaded from: classes3.dex */
public class StatsPullAtomCallbackImpl implements android.app.StatsManager.StatsPullAtomCallback {
    private static final boolean DEBUG = false;
    private static final int STATS_PULL_TIMEOUT_MILLIS = 2000;
    private static final java.lang.String TAG = com.android.server.powerstats.StatsPullAtomCallbackImpl.class.getSimpleName();
    private android.content.Context mContext;
    private android.power.PowerStatsInternal mPowerStatsInternal;
    private java.util.Map<java.lang.Integer, android.hardware.power.stats.Channel> mChannels = new java.util.HashMap();
    private java.util.Map<java.lang.Integer, java.lang.String> mEntityNames = new java.util.HashMap();
    private java.util.Map<java.lang.Integer, java.util.Map<java.lang.Integer, java.lang.String>> mStateNames = new java.util.HashMap();

    public int onPullAtom(int atomTag, java.util.List<android.util.StatsEvent> data) {
        switch (atomTag) {
            case 10005:
                return pullSubsystemSleepState(atomTag, data);
            case com.android.internal.util.FrameworkStatsLog.ON_DEVICE_POWER_MEASUREMENT /* 10038 */:
                return pullOnDevicePowerMeasurement(atomTag, data);
            default:
                throw new java.lang.UnsupportedOperationException("Unknown tagId=" + atomTag);
        }
    }

    private boolean initPullOnDevicePowerMeasurement() {
        android.hardware.power.stats.Channel[] channels = this.mPowerStatsInternal.getEnergyMeterInfo();
        if (channels == null || channels.length == 0) {
            android.util.Slog.e(TAG, "Failed to init OnDevicePowerMeasurement puller");
            return false;
        }
        for (android.hardware.power.stats.Channel channel : channels) {
            this.mChannels.put(java.lang.Integer.valueOf(channel.id), channel);
        }
        return true;
    }

    private int pullOnDevicePowerMeasurement(int atomTag, java.util.List<android.util.StatsEvent> events) {
        try {
            android.hardware.power.stats.EnergyMeasurement[] energyMeasurements = this.mPowerStatsInternal.readEnergyMeterAsync(new int[0]).get(2000L, java.util.concurrent.TimeUnit.MILLISECONDS);
            if (energyMeasurements == null) {
                return 1;
            }
            for (android.hardware.power.stats.EnergyMeasurement energyMeasurement : energyMeasurements) {
                if (energyMeasurement.durationMs == energyMeasurement.timestampMs) {
                    events.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, this.mChannels.get(java.lang.Integer.valueOf(energyMeasurement.id)).subsystem, this.mChannels.get(java.lang.Integer.valueOf(energyMeasurement.id)).name, energyMeasurement.durationMs, energyMeasurement.energyUWs));
                }
            }
            return 0;
        } catch (java.lang.Exception e) {
            android.util.Slog.e(TAG, "Failed to readEnergyMeterAsync", e);
            return 1;
        }
    }

    private boolean initSubsystemSleepState() {
        android.hardware.power.stats.PowerEntity[] entities = this.mPowerStatsInternal.getPowerEntityInfo();
        if (entities == null || entities.length == 0) {
            android.util.Slog.e(TAG, "Failed to init SubsystemSleepState puller");
            return false;
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
        return true;
    }

    private int pullSubsystemSleepState(int atomTag, java.util.List<android.util.StatsEvent> events) {
        try {
            android.hardware.power.stats.StateResidencyResult[] results = this.mPowerStatsInternal.getStateResidencyAsync(new int[0]).get(2000L, java.util.concurrent.TimeUnit.MILLISECONDS);
            if (results == null) {
                return 1;
            }
            for (int i = 0; i < results.length; i++) {
                int j = 0;
                for (android.hardware.power.stats.StateResidencyResult result = results[i]; j < result.stateResidencyData.length; result = result) {
                    android.hardware.power.stats.StateResidency stateResidency = result.stateResidencyData[j];
                    events.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, this.mEntityNames.get(java.lang.Integer.valueOf(result.id)), this.mStateNames.get(java.lang.Integer.valueOf(result.id)).get(java.lang.Integer.valueOf(stateResidency.id)), stateResidency.totalStateEntryCount, stateResidency.totalTimeInStateMs, android.os.SystemClock.elapsedRealtime() - stateResidency.totalTimeInStateMs));
                    j++;
                }
            }
            return 0;
        } catch (java.lang.Exception e) {
            android.util.Slog.e(TAG, "Failed to getStateResidencyAsync", e);
            return 1;
        }
    }

    public StatsPullAtomCallbackImpl(android.content.Context context, android.power.PowerStatsInternal powerStatsInternal) {
        this.mContext = context;
        this.mPowerStatsInternal = powerStatsInternal;
        if (powerStatsInternal == null) {
            android.util.Slog.e(TAG, "Failed to start PowerStatsService statsd pullers");
            return;
        }
        android.app.StatsManager manager = (android.app.StatsManager) this.mContext.getSystemService(android.app.StatsManager.class);
        if (initPullOnDevicePowerMeasurement()) {
            manager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.ON_DEVICE_POWER_MEASUREMENT, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this);
        }
        if (initSubsystemSleepState()) {
            manager.setPullAtomCallback(10005, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this);
        }
    }
}
