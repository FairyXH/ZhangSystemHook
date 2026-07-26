package com.android.server.power.stats;

/* JADX INFO: loaded from: classes3.dex */
public class PowerStatsExporter {
    private static final long BATTERY_SESSION_TIME_SPAN_SLACK_MILLIS = java.util.concurrent.TimeUnit.MINUTES.toMillis(2);
    private static final java.lang.String TAG = "PowerStatsExporter";
    private final long mBatterySessionTimeSpanSlackMillis;
    private final com.android.server.power.stats.PowerStatsAggregator mPowerStatsAggregator;
    private final com.android.server.power.stats.PowerStatsStore mPowerStatsStore;

    public PowerStatsExporter(com.android.server.power.stats.PowerStatsStore powerStatsStore, com.android.server.power.stats.PowerStatsAggregator powerStatsAggregator) {
        this(powerStatsStore, powerStatsAggregator, BATTERY_SESSION_TIME_SPAN_SLACK_MILLIS);
    }

    public PowerStatsExporter(com.android.server.power.stats.PowerStatsStore powerStatsStore, com.android.server.power.stats.PowerStatsAggregator powerStatsAggregator, long batterySessionTimeSpanSlackMillis) {
        this.mPowerStatsStore = powerStatsStore;
        this.mPowerStatsAggregator = powerStatsAggregator;
        this.mBatterySessionTimeSpanSlackMillis = batterySessionTimeSpanSlackMillis;
    }

    public void exportAggregatedPowerStats(final android.os.BatteryUsageStats.Builder batteryUsageStatsBuilder, long monotonicStartTime, long monotonicEndTime) {
        java.util.List<com.android.server.power.stats.PowerStatsSpan.Metadata> spans;
        boolean hasStoredSpans;
        java.util.List<com.android.server.power.stats.PowerStatsSpan.Metadata> spans2 = this.mPowerStatsStore.getTableOfContents();
        int i = spans2.size() - 1;
        boolean hasStoredSpans2 = false;
        long maxEndTime = monotonicStartTime;
        while (i >= 0) {
            com.android.server.power.stats.PowerStatsSpan.Metadata metadata = spans2.get(i);
            java.util.List<java.lang.String> sections = metadata.getSections();
            java.lang.String str = com.android.server.power.stats.AggregatedPowerStatsSection.TYPE;
            if (!sections.contains(com.android.server.power.stats.AggregatedPowerStatsSection.TYPE)) {
                spans = spans2;
                hasStoredSpans = hasStoredSpans2;
            } else {
                java.util.List<com.android.server.power.stats.PowerStatsSpan.TimeFrame> timeFrames = metadata.getTimeFrames();
                long spanMinTime = Long.MAX_VALUE;
                long spanMaxTime = Long.MIN_VALUE;
                int j = 0;
                while (j < timeFrames.size()) {
                    com.android.server.power.stats.PowerStatsSpan.TimeFrame timeFrame = timeFrames.get(j);
                    java.util.List<com.android.server.power.stats.PowerStatsSpan.Metadata> spans3 = spans2;
                    boolean hasStoredSpans3 = hasStoredSpans2;
                    long startMonotonicTime = timeFrame.startMonotonicTime;
                    java.util.List<com.android.server.power.stats.PowerStatsSpan.TimeFrame> timeFrames2 = timeFrames;
                    java.lang.String str2 = str;
                    long endMonotonicTime = timeFrame.duration + startMonotonicTime;
                    if (startMonotonicTime < spanMinTime) {
                        spanMinTime = startMonotonicTime;
                    }
                    if (endMonotonicTime > spanMaxTime) {
                        spanMaxTime = endMonotonicTime;
                    }
                    j++;
                    spans2 = spans3;
                    hasStoredSpans2 = hasStoredSpans3;
                    str = str2;
                    timeFrames = timeFrames2;
                }
                java.lang.String str3 = str;
                spans = spans2;
                hasStoredSpans = hasStoredSpans2;
                if (spanMinTime >= monotonicStartTime && spanMaxTime < monotonicEndTime) {
                    if (spanMaxTime > maxEndTime) {
                        maxEndTime = spanMaxTime;
                    }
                    com.android.server.power.stats.PowerStatsSpan span = this.mPowerStatsStore.loadPowerStatsSpan(metadata.getId(), str3);
                    if (span == null) {
                        android.util.Slog.e(TAG, "Could not read PowerStatsStore section " + metadata);
                        hasStoredSpans2 = hasStoredSpans;
                    } else {
                        java.util.List<com.android.server.power.stats.PowerStatsSpan.Section> sections2 = span.getSections();
                        hasStoredSpans2 = hasStoredSpans;
                        for (int k = 0; k < sections2.size(); k++) {
                            hasStoredSpans2 = true;
                            com.android.server.power.stats.PowerStatsSpan.Section section = sections2.get(k);
                            lambda$exportAggregatedPowerStats$0(batteryUsageStatsBuilder, ((com.android.server.power.stats.AggregatedPowerStatsSection) section).getAggregatedPowerStats());
                        }
                    }
                }
                i--;
                spans2 = spans;
            }
            hasStoredSpans2 = hasStoredSpans;
            i--;
            spans2 = spans;
        }
        if (!hasStoredSpans2 || maxEndTime < monotonicEndTime - this.mBatterySessionTimeSpanSlackMillis) {
            this.mPowerStatsAggregator.aggregatePowerStats(maxEndTime, monotonicEndTime, new java.util.function.Consumer() { // from class: com.android.server.power.stats.PowerStatsExporter$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.lambda$exportAggregatedPowerStats$0(batteryUsageStatsBuilder, (com.android.server.power.stats.AggregatedPowerStats) obj);
                }
            });
        }
        this.mPowerStatsAggregator.reset();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: populateBatteryUsageStatsBuilder, reason: merged with bridge method [inline-methods] */
    public void lambda$exportAggregatedPowerStats$0(android.os.BatteryUsageStats.Builder batteryUsageStatsBuilder, com.android.server.power.stats.AggregatedPowerStats stats) {
        com.android.server.power.stats.AggregatedPowerStatsConfig config = this.mPowerStatsAggregator.getConfig();
        java.util.List<com.android.server.power.stats.AggregatedPowerStatsConfig.PowerComponent> powerComponents = config.getPowerComponentsAggregatedStatsConfigs();
        for (int i = powerComponents.size() - 1; i >= 0; i--) {
            populateBatteryUsageStatsBuilder(batteryUsageStatsBuilder, stats, powerComponents.get(i));
        }
    }

    private void populateBatteryUsageStatsBuilder(android.os.BatteryUsageStats.Builder batteryUsageStatsBuilder, com.android.server.power.stats.AggregatedPowerStats stats, com.android.server.power.stats.AggregatedPowerStatsConfig.PowerComponent powerComponent) {
        com.android.internal.os.PowerStats.Descriptor descriptor;
        int powerComponentId = powerComponent.getPowerComponentId();
        final com.android.server.power.stats.PowerComponentAggregatedPowerStats powerComponentStats = stats.getPowerComponentStats(powerComponentId);
        if (powerComponentStats == null || (descriptor = powerComponentStats.getPowerStatsDescriptor()) == null) {
            return;
        }
        final com.android.server.power.stats.PowerStatsLayout layout = new com.android.server.power.stats.PowerStatsLayout();
        layout.fromExtras(descriptor.extras);
        final long[] deviceStats = new long[descriptor.statsArrayLength];
        final double[] totalPower = new double[1];
        com.android.server.power.stats.MultiStateStats.States.forEachTrackedStateCombination(powerComponent.getDeviceStateConfig(), new java.util.function.Consumer() { // from class: com.android.server.power.stats.PowerStatsExporter$$ExternalSyntheticLambda2
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.power.stats.PowerStatsExporter.lambda$populateBatteryUsageStatsBuilder$1(powerComponentStats, deviceStats, totalPower, layout, (int[]) obj);
            }
        });
        android.os.AggregateBatteryConsumer.Builder deviceScope = batteryUsageStatsBuilder.getAggregateBatteryConsumerBuilder(0);
        deviceScope.addConsumedPower(powerComponentId, totalPower[0], 0);
        if (layout.isUidPowerAttributionSupported()) {
            populateUidBatteryConsumers(batteryUsageStatsBuilder, powerComponent, powerComponentStats, layout);
        }
    }

    static /* synthetic */ void lambda$populateBatteryUsageStatsBuilder$1(com.android.server.power.stats.PowerComponentAggregatedPowerStats powerComponentStats, long[] deviceStats, double[] totalPower, com.android.server.power.stats.PowerStatsLayout layout, int[] states) {
        if (states[0] == 0 && powerComponentStats.getDeviceStats(deviceStats, states)) {
            totalPower[0] = totalPower[0] + layout.getDevicePowerEstimate(deviceStats);
        }
    }

    private static void populateUidBatteryConsumers(android.os.BatteryUsageStats.Builder batteryUsageStatsBuilder, com.android.server.power.stats.AggregatedPowerStatsConfig.PowerComponent powerComponent, final com.android.server.power.stats.PowerComponentAggregatedPowerStats powerComponentStats, final com.android.server.power.stats.PowerStatsLayout layout) {
        int powerComponentId = powerComponent.getPowerComponentId();
        com.android.internal.os.PowerStats.Descriptor descriptor = powerComponentStats.getPowerStatsDescriptor();
        final long[] uidStats = new long[descriptor.uidStatsArrayLength];
        final boolean breakDownByProcState = batteryUsageStatsBuilder.isProcessStateDataNeeded() && powerComponent.getUidStateConfig()[2].isTracked();
        final double[] powerByProcState = new double[breakDownByProcState ? 5 : 1];
        java.util.ArrayList<java.lang.Integer> uids = new java.util.ArrayList<>();
        powerComponentStats.collectUids(uids);
        java.util.Iterator<java.lang.Integer> it = uids.iterator();
        double powerAllApps = 0.0d;
        while (it.hasNext()) {
            final int uid = it.next().intValue();
            android.os.UidBatteryConsumer.Builder builder = batteryUsageStatsBuilder.getOrCreateUidBatteryConsumerBuilder(uid);
            java.util.Arrays.fill(powerByProcState, 0.0d);
            com.android.internal.os.PowerStats.Descriptor descriptor2 = descriptor;
            double powerAllApps2 = powerAllApps;
            com.android.server.power.stats.MultiStateStats.States.forEachTrackedStateCombination(powerComponent.getUidStateConfig(), new java.util.function.Consumer() { // from class: com.android.server.power.stats.PowerStatsExporter$$ExternalSyntheticLambda1
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    com.android.server.power.stats.PowerStatsExporter.lambda$populateUidBatteryConsumers$2(powerComponentStats, uidStats, uid, layout, breakDownByProcState, powerByProcState, (int[]) obj);
                }
            });
            double powerAllProcStates = 0.0d;
            for (int procState = 0; procState < powerByProcState.length; procState++) {
                double power = powerByProcState[procState];
                if (power != 0.0d) {
                    powerAllProcStates += power;
                    if (breakDownByProcState && procState != 0) {
                        builder.addConsumedPower(builder.getKey(powerComponentId, procState), power, 0);
                    }
                }
            }
            builder.addConsumedPower(powerComponentId, powerAllProcStates, 0);
            powerAllApps = powerAllApps2 + powerAllProcStates;
            descriptor = descriptor2;
        }
        android.os.AggregateBatteryConsumer.Builder allAppsScope = batteryUsageStatsBuilder.getAggregateBatteryConsumerBuilder(1);
        allAppsScope.addConsumedPower(powerComponentId, powerAllApps, 0);
    }

    static /* synthetic */ void lambda$populateUidBatteryConsumers$2(com.android.server.power.stats.PowerComponentAggregatedPowerStats powerComponentStats, long[] uidStats, int uid, com.android.server.power.stats.PowerStatsLayout layout, boolean breakDownByProcState, double[] powerByProcState, int[] states) {
        int procState = 0;
        if (states[0] != 0 || !powerComponentStats.getUidStats(uidStats, uid, states)) {
            return;
        }
        double power = layout.getUidPowerEstimate(uidStats);
        if (breakDownByProcState) {
            procState = states[2];
        }
        powerByProcState[procState] = powerByProcState[procState] + power;
    }
}
