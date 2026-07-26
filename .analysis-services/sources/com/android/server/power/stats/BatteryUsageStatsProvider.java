package com.android.server.power.stats;

/* JADX INFO: loaded from: classes3.dex */
public class BatteryUsageStatsProvider {
    private static final java.lang.String TAG = "BatteryUsageStatsProv";
    private static boolean sErrorReported;
    private final com.android.internal.os.Clock mClock;
    private final android.content.Context mContext;
    private final com.android.internal.os.CpuScalingPolicies mCpuScalingPolicies;
    private java.util.List<com.android.server.power.stats.PowerCalculator> mPowerCalculators;
    private final com.android.internal.os.PowerProfile mPowerProfile;
    private final com.android.server.power.stats.PowerStatsExporter mPowerStatsExporter;
    private final com.android.server.power.stats.PowerStatsStore mPowerStatsStore;
    private final android.util.SparseBooleanArray mPowerStatsExporterEnabled = new android.util.SparseBooleanArray();
    private final java.lang.Object mLock = new java.lang.Object();

    public BatteryUsageStatsProvider(android.content.Context context, com.android.server.power.stats.PowerStatsExporter powerStatsExporter, com.android.internal.os.PowerProfile powerProfile, com.android.internal.os.CpuScalingPolicies cpuScalingPolicies, com.android.server.power.stats.PowerStatsStore powerStatsStore, com.android.internal.os.Clock clock) {
        this.mContext = context;
        this.mPowerStatsExporter = powerStatsExporter;
        this.mPowerStatsStore = powerStatsStore;
        this.mPowerProfile = powerProfile;
        this.mCpuScalingPolicies = cpuScalingPolicies;
        this.mClock = clock;
    }

    private java.util.List<com.android.server.power.stats.PowerCalculator> getPowerCalculators() {
        synchronized (this.mLock) {
            if (this.mPowerCalculators == null) {
                this.mPowerCalculators = new java.util.ArrayList();
                this.mPowerCalculators.add(new com.android.server.power.stats.BatteryChargeCalculator());
                if (!this.mPowerStatsExporterEnabled.get(1)) {
                    this.mPowerCalculators.add(new com.android.server.power.stats.CpuPowerCalculator(this.mCpuScalingPolicies, this.mPowerProfile));
                }
                this.mPowerCalculators.add(new com.android.server.power.stats.MemoryPowerCalculator(this.mPowerProfile));
                this.mPowerCalculators.add(new com.android.server.power.stats.WakelockPowerCalculator(this.mPowerProfile));
                if (!android.os.BatteryStats.checkWifiOnly(this.mContext)) {
                    if (!this.mPowerStatsExporterEnabled.get(8)) {
                        this.mPowerCalculators.add(new com.android.server.power.stats.MobileRadioPowerCalculator(this.mPowerProfile));
                    }
                    if (!this.mPowerStatsExporterEnabled.get(14)) {
                        this.mPowerCalculators.add(new com.android.server.power.stats.PhonePowerCalculator(this.mPowerProfile));
                    }
                }
                if (!this.mPowerStatsExporterEnabled.get(11)) {
                    this.mPowerCalculators.add(new com.android.server.power.stats.WifiPowerCalculator(this.mPowerProfile));
                }
                if (!this.mPowerStatsExporterEnabled.get(2)) {
                    this.mPowerCalculators.add(new com.android.server.power.stats.BluetoothPowerCalculator(this.mPowerProfile));
                }
                this.mPowerCalculators.add(new com.android.server.power.stats.SensorPowerCalculator((android.hardware.SensorManager) this.mContext.getSystemService(android.hardware.SensorManager.class)));
                if (!this.mPowerStatsExporterEnabled.get(10)) {
                    this.mPowerCalculators.add(new com.android.server.power.stats.GnssPowerCalculator(this.mPowerProfile));
                }
                if (!this.mPowerStatsExporterEnabled.get(3)) {
                    this.mPowerCalculators.add(new com.android.server.power.stats.CameraPowerCalculator(this.mPowerProfile));
                }
                if (!this.mPowerStatsExporterEnabled.get(6)) {
                    this.mPowerCalculators.add(new com.android.server.power.stats.FlashlightPowerCalculator(this.mPowerProfile));
                }
                if (!this.mPowerStatsExporterEnabled.get(4)) {
                    this.mPowerCalculators.add(new com.android.server.power.stats.AudioPowerCalculator(this.mPowerProfile));
                }
                if (!this.mPowerStatsExporterEnabled.get(5)) {
                    this.mPowerCalculators.add(new com.android.server.power.stats.VideoPowerCalculator(this.mPowerProfile));
                }
                this.mPowerCalculators.add(new com.android.server.power.stats.ScreenPowerCalculator(this.mPowerProfile));
                this.mPowerCalculators.add(new com.android.server.power.stats.AmbientDisplayPowerCalculator(this.mPowerProfile));
                this.mPowerCalculators.add(new com.android.server.power.stats.IdlePowerCalculator(this.mPowerProfile));
                this.mPowerCalculators.add(new com.android.server.power.stats.CustomEnergyConsumerPowerCalculator(this.mPowerProfile));
                this.mPowerCalculators.add(new com.android.server.power.stats.UserPowerCalculator());
                if (!com.android.internal.hidden_from_bootclasspath.com.android.server.power.optimization.Flags.disableSystemServicePowerAttr()) {
                    this.mPowerCalculators.add(new com.android.server.power.stats.SystemServicePowerCalculator(this.mCpuScalingPolicies, this.mPowerProfile));
                }
            }
        }
        return this.mPowerCalculators;
    }

    public static boolean shouldUpdateStats(java.util.List<android.os.BatteryUsageStatsQuery> queries, long elapsedRealtime, long lastUpdateTimeStampMs) {
        long allowableStatsAge = Long.MAX_VALUE;
        for (int i = queries.size() - 1; i >= 0; i--) {
            android.os.BatteryUsageStatsQuery query = queries.get(i);
            allowableStatsAge = java.lang.Math.min(allowableStatsAge, query.getMaxStatsAge());
        }
        return elapsedRealtime - lastUpdateTimeStampMs > allowableStatsAge;
    }

    public java.util.List<android.os.BatteryUsageStats> getBatteryUsageStats(com.android.server.power.stats.BatteryStatsImpl stats, java.util.List<android.os.BatteryUsageStatsQuery> queries) {
        java.util.ArrayList<android.os.BatteryUsageStats> results = new java.util.ArrayList<>(queries.size());
        synchronized (stats) {
            stats.prepareForDumpLocked();
        }
        long currentTimeMillis = this.mClock.currentTimeMillis();
        for (int i = 0; i < queries.size(); i++) {
            results.add(getBatteryUsageStats(stats, queries.get(i), currentTimeMillis));
        }
        return results;
    }

    public android.os.BatteryUsageStats getBatteryUsageStats(com.android.server.power.stats.BatteryStatsImpl stats, android.os.BatteryUsageStatsQuery query) {
        return getBatteryUsageStats(stats, query, this.mClock.currentTimeMillis());
    }

    private android.os.BatteryUsageStats getBatteryUsageStats(com.android.server.power.stats.BatteryStatsImpl stats, android.os.BatteryUsageStatsQuery query, long currentTimeMs) {
        if (query.getToTimestamp() == 0) {
            return getCurrentBatteryUsageStats(stats, query, currentTimeMs);
        }
        return getAggregatedBatteryUsageStats(stats, query);
    }

    private android.os.BatteryUsageStats getCurrentBatteryUsageStats(com.android.server.power.stats.BatteryStatsImpl stats, android.os.BatteryUsageStatsQuery query, long currentTimeMs) {
        long monotonicStartTime;
        long monotonicEndTime;
        android.os.BatteryUsageStats.Builder batteryUsageStatsBuilder;
        char c;
        int i;
        int count;
        char c2;
        android.util.SparseArray<? extends android.os.BatteryStats.Uid> uidStats;
        boolean include;
        long realtimeUs = this.mClock.elapsedRealtime() * 1000;
        long uptimeUs = this.mClock.uptimeMillis() * 1000;
        boolean includePowerModels = (query.getFlags() & 4) != 0;
        boolean includeProcessStateData = (query.getFlags() & 8) != 0 && stats.isProcessStateDataAvailable();
        boolean includeVirtualUids = (query.getFlags() & 16) != 0;
        double minConsumedPowerThreshold = query.getMinConsumedPowerThreshold();
        synchronized (stats) {
            monotonicStartTime = stats.getMonotonicStartTime();
            monotonicEndTime = stats.getMonotonicEndTime();
            batteryUsageStatsBuilder = new android.os.BatteryUsageStats.Builder(stats.getCustomEnergyConsumerNames(), includePowerModels, includeProcessStateData, minConsumedPowerThreshold);
            batteryUsageStatsBuilder.setStatsStartTimestamp(stats.getStartClockTime());
            batteryUsageStatsBuilder.setStatsEndTimestamp(currentTimeMs);
            android.util.SparseArray<? extends android.os.BatteryStats.Uid> uidStats2 = stats.getUidStats();
            int i2 = uidStats2.size() - 1;
            while (true) {
                c = 2;
                if (i2 < 0) {
                    break;
                }
                android.os.BatteryStats.Uid uid = uidStats2.valueAt(i2);
                if (includeVirtualUids || uid.getUid() != 1090) {
                    batteryUsageStatsBuilder.getOrCreateUidBatteryConsumerBuilder(uid).setTimeInProcessStateMs(2, getProcessBackgroundTimeMs(uid, realtimeUs)).setTimeInProcessStateMs(1, getProcessForegroundTimeMs(uid, realtimeUs)).setTimeInProcessStateMs(3, getProcessForegroundServiceTimeMs(uid, realtimeUs));
                }
                i2--;
            }
            int[] powerComponents = query.getPowerComponents();
            java.util.List<com.android.server.power.stats.PowerCalculator> powerCalculators = getPowerCalculators();
            int count2 = powerCalculators.size();
            int i3 = 0;
            while (i3 < count2) {
                com.android.server.power.stats.PowerCalculator powerCalculator = powerCalculators.get(i3);
                if (powerComponents == null) {
                    i = i3;
                } else {
                    int length = powerComponents.length;
                    int i4 = 0;
                    while (true) {
                        if (i4 >= length) {
                            i = i3;
                            include = false;
                            break;
                        }
                        int powerComponent = powerComponents[i4];
                        i = i3;
                        if (!powerCalculator.isPowerComponentSupported(powerComponent)) {
                            i4++;
                            i3 = i;
                        } else {
                            include = true;
                            break;
                        }
                    }
                    if (!include) {
                        count = count2;
                        uidStats = uidStats2;
                        c2 = 2;
                    }
                    i3 = i + 1;
                    count2 = count;
                    c = c2;
                    uidStats2 = uidStats;
                }
                count = count2;
                c2 = 2;
                uidStats = uidStats2;
                powerCalculator.calculate(batteryUsageStatsBuilder, stats, realtimeUs, uptimeUs, query);
                i3 = i + 1;
                count2 = count;
                c = c2;
                uidStats2 = uidStats;
            }
            if ((query.getFlags() & 2) != 0) {
                batteryUsageStatsBuilder.setBatteryHistory(stats.copyHistory());
            }
        }
        if (this.mPowerStatsExporterEnabled.indexOfValue(true) >= 0) {
            this.mPowerStatsExporter.exportAggregatedPowerStats(batteryUsageStatsBuilder, monotonicStartTime, monotonicEndTime);
        }
        android.os.BatteryUsageStats batteryUsageStats = batteryUsageStatsBuilder.build();
        if (includeProcessStateData) {
            verify(batteryUsageStats);
        }
        return batteryUsageStats;
    }

    private void verify(android.os.BatteryUsageStats stats) {
        if (sErrorReported) {
            return;
        }
        double precision = 2.0d;
        boolean z = true;
        int[] components = {1, 8, 11, 2};
        int[] states = {1, 2, 3, 4};
        for (android.os.UidBatteryConsumer ubc : stats.getUidBatteryConsumers()) {
            int length = components.length;
            int i = 0;
            while (i < length) {
                int component = components[i];
                double consumedPower = ubc.getConsumedPower(ubc.getKey(component));
                double sumStates = 0.0d;
                int length2 = states.length;
                int i2 = 0;
                while (i2 < length2) {
                    int state = states[i2];
                    sumStates += ubc.getConsumedPower(ubc.getKey(component, state));
                    i2++;
                    precision = precision;
                }
                double precision2 = precision;
                if (sumStates <= 2.0d + consumedPower) {
                    i++;
                    z = true;
                    precision = precision2;
                } else {
                    java.lang.String error = "Sum of states exceeds total. UID = " + ubc.getUid() + " " + android.os.BatteryConsumer.powerComponentIdToString(component) + " total = " + consumedPower + " states = " + sumStates;
                    if (!sErrorReported) {
                        android.util.Slog.wtf(TAG, error);
                        sErrorReported = true;
                        return;
                    } else {
                        android.util.Slog.e(TAG, error);
                        return;
                    }
                }
            }
            precision = precision;
        }
    }

    private long getProcessForegroundTimeMs(android.os.BatteryStats.Uid uid, long realtimeUs) {
        long topStateDurationUs = uid.getProcessStateTime(0, realtimeUs, 0);
        long foregroundActivityDurationUs = 0;
        android.os.BatteryStats.Timer foregroundActivityTimer = uid.getForegroundActivityTimer();
        if (foregroundActivityTimer != null) {
            foregroundActivityDurationUs = foregroundActivityTimer.getTotalTimeLocked(realtimeUs, 0);
        }
        long totalForegroundDurationUs = java.lang.Math.min(topStateDurationUs, foregroundActivityDurationUs);
        return (totalForegroundDurationUs + uid.getProcessStateTime(2, realtimeUs, 0)) / 1000;
    }

    private long getProcessBackgroundTimeMs(android.os.BatteryStats.Uid uid, long realtimeUs) {
        return uid.getProcessStateTime(3, realtimeUs, 0) / 1000;
    }

    private long getProcessForegroundServiceTimeMs(android.os.BatteryStats.Uid uid, long realtimeUs) {
        return uid.getProcessStateTime(1, realtimeUs, 0) / 1000;
    }

    private android.os.BatteryUsageStats getAggregatedBatteryUsageStats(com.android.server.power.stats.BatteryStatsImpl stats, android.os.BatteryUsageStatsQuery query) {
        com.android.server.power.stats.BatteryUsageStatsProvider batteryUsageStatsProvider = this;
        boolean includePowerModels = (query.getFlags() & 4) != 0;
        boolean includeProcessStateData = (query.getFlags() & 8) != 0 && stats.isProcessStateDataAvailable();
        double minConsumedPowerThreshold = query.getMinConsumedPowerThreshold();
        java.lang.String[] customEnergyConsumerNames = stats.getCustomEnergyConsumerNames();
        android.os.BatteryUsageStats.Builder builder = new android.os.BatteryUsageStats.Builder(customEnergyConsumerNames, includePowerModels, includeProcessStateData, minConsumedPowerThreshold);
        if (batteryUsageStatsProvider.mPowerStatsStore == null) {
            android.util.Log.e(TAG, "PowerStatsStore is unavailable");
            return builder.build();
        }
        java.util.List<com.android.server.power.stats.PowerStatsSpan.Metadata> toc = batteryUsageStatsProvider.mPowerStatsStore.getTableOfContents();
        java.util.Iterator<com.android.server.power.stats.PowerStatsSpan.Metadata> it = toc.iterator();
        while (it.hasNext()) {
            com.android.server.power.stats.PowerStatsSpan.Metadata spanMetadata = it.next();
            if (spanMetadata.getSections().contains(com.android.server.power.stats.BatteryUsageStatsSection.TYPE)) {
                long minTime = Long.MAX_VALUE;
                java.util.List<com.android.server.power.stats.PowerStatsSpan.Metadata> toc2 = toc;
                boolean includePowerModels2 = includePowerModels;
                long maxTime = 0;
                for (java.util.Iterator<com.android.server.power.stats.PowerStatsSpan.TimeFrame> it2 = spanMetadata.getTimeFrames().iterator(); it2.hasNext(); it2 = it2) {
                    java.util.Iterator<com.android.server.power.stats.PowerStatsSpan.Metadata> it3 = it;
                    com.android.server.power.stats.PowerStatsSpan.TimeFrame timeFrame = it2.next();
                    double minConsumedPowerThreshold2 = minConsumedPowerThreshold;
                    long spanEndTime = timeFrame.startTime + timeFrame.duration;
                    minTime = java.lang.Math.min(minTime, spanEndTime);
                    maxTime = java.lang.Math.max(maxTime, spanEndTime);
                    customEnergyConsumerNames = customEnergyConsumerNames;
                    it = it3;
                    minConsumedPowerThreshold = minConsumedPowerThreshold2;
                }
                java.util.Iterator<com.android.server.power.stats.PowerStatsSpan.Metadata> it4 = it;
                double minConsumedPowerThreshold3 = minConsumedPowerThreshold;
                java.lang.String[] customEnergyConsumerNames2 = customEnergyConsumerNames;
                boolean isInRange = (query.getFromTimestamp() == 0 || minTime > query.getFromTimestamp()) && (query.getToTimestamp() == 0 || maxTime <= query.getToTimestamp());
                if (!isInRange) {
                    includePowerModels = includePowerModels2;
                    toc = toc2;
                    customEnergyConsumerNames = customEnergyConsumerNames2;
                    it = it4;
                    minConsumedPowerThreshold = minConsumedPowerThreshold3;
                } else {
                    com.android.server.power.stats.PowerStatsSpan powerStatsSpan = batteryUsageStatsProvider.mPowerStatsStore.loadPowerStatsSpan(spanMetadata.getId(), com.android.server.power.stats.BatteryUsageStatsSection.TYPE);
                    if (powerStatsSpan == null) {
                        includePowerModels = includePowerModels2;
                        toc = toc2;
                        customEnergyConsumerNames = customEnergyConsumerNames2;
                        it = it4;
                        minConsumedPowerThreshold = minConsumedPowerThreshold3;
                    } else {
                        for (com.android.server.power.stats.PowerStatsSpan.Section section : powerStatsSpan.getSections()) {
                            android.os.BatteryUsageStats snapshot = ((com.android.server.power.stats.BatteryUsageStatsSection) section).getBatteryUsageStats();
                            java.lang.String[] customEnergyConsumerNames3 = customEnergyConsumerNames2;
                            if (!java.util.Arrays.equals(snapshot.getCustomPowerComponentNames(), customEnergyConsumerNames3)) {
                                customEnergyConsumerNames2 = customEnergyConsumerNames3;
                                android.util.Log.w(TAG, "Ignoring older BatteryUsageStats snapshot, which has different custom power components: " + java.util.Arrays.toString(snapshot.getCustomPowerComponentNames()));
                            } else {
                                customEnergyConsumerNames2 = customEnergyConsumerNames3;
                                if (includeProcessStateData && !snapshot.isProcessStateDataIncluded()) {
                                    android.util.Log.w(TAG, "Ignoring older BatteryUsageStats snapshot, which  does not include process state data");
                                } else {
                                    builder.add(snapshot);
                                }
                            }
                        }
                        batteryUsageStatsProvider = this;
                        includePowerModels = includePowerModels2;
                        toc = toc2;
                        customEnergyConsumerNames = customEnergyConsumerNames2;
                        it = it4;
                        minConsumedPowerThreshold = minConsumedPowerThreshold3;
                    }
                }
            }
        }
        return builder.build();
    }

    public void setPowerStatsExporterEnabled(int powerComponentId, boolean enabled) {
        this.mPowerStatsExporterEnabled.put(powerComponentId, enabled);
    }
}
