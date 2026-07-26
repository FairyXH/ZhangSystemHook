package com.android.server.power.stats;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: loaded from: classes3.dex */
public abstract class PowerStatsProcessor {
    private static final double MILLIAMPHOUR_PER_MICROCOULOMB = 2.777777777777778E-7d;
    private static final java.lang.String TAG = "PowerStatsProcessor";

    abstract void finish(com.android.server.power.stats.PowerComponentAggregatedPowerStats powerComponentAggregatedPowerStats, long j);

    PowerStatsProcessor() {
    }

    void start(com.android.server.power.stats.PowerComponentAggregatedPowerStats stats, long timestampMs) {
    }

    void noteStateChange(com.android.server.power.stats.PowerComponentAggregatedPowerStats stats, android.os.BatteryStats.HistoryItem item) {
    }

    void addPowerStats(com.android.server.power.stats.PowerComponentAggregatedPowerStats stats, com.android.internal.os.PowerStats powerStats, long timestampMs) {
        stats.addPowerStats(powerStats, timestampMs);
    }

    protected static class PowerEstimationPlan {
        private final com.android.server.power.stats.AggregatedPowerStatsConfig.PowerComponent mConfig;
        public java.util.List<com.android.server.power.stats.PowerStatsProcessor.DeviceStateEstimation> deviceStateEstimations = new java.util.ArrayList();
        public java.util.List<com.android.server.power.stats.PowerStatsProcessor.CombinedDeviceStateEstimate> combinedDeviceStateEstimations = new java.util.ArrayList();
        public java.util.List<com.android.server.power.stats.PowerStatsProcessor.UidStateEstimate> uidStateEstimates = new java.util.ArrayList();

        public PowerEstimationPlan(com.android.server.power.stats.AggregatedPowerStatsConfig.PowerComponent config) {
            this.mConfig = config;
            addDeviceStateEstimations();
            combineDeviceStateEstimations();
            addUidStateEstimations();
        }

        private void addDeviceStateEstimations() {
            com.android.server.power.stats.MultiStateStats.States[] config = this.mConfig.getDeviceStateConfig();
            int[][] deviceStateCombinations = com.android.server.power.stats.PowerStatsProcessor.getAllTrackedStateCombinations(config);
            for (int[] deviceStateCombination : deviceStateCombinations) {
                this.deviceStateEstimations.add(new com.android.server.power.stats.PowerStatsProcessor.DeviceStateEstimation(config, deviceStateCombination));
            }
        }

        private void combineDeviceStateEstimations() {
            int index;
            com.android.server.power.stats.MultiStateStats.States[] deviceStateConfig = this.mConfig.getDeviceStateConfig();
            com.android.server.power.stats.MultiStateStats.States[] uidStateConfig = this.mConfig.getUidStateConfig();
            com.android.server.power.stats.MultiStateStats.States[] deviceStatesTrackedPerUid = new com.android.server.power.stats.MultiStateStats.States[deviceStateConfig.length];
            for (int i = 0; i < deviceStateConfig.length; i++) {
                if (deviceStateConfig[i].isTracked() && (index = com.android.server.power.stats.MultiStateStats.States.findTrackedStateByName(uidStateConfig, deviceStateConfig[i].getName())) != -1 && uidStateConfig[index].isTracked()) {
                    deviceStatesTrackedPerUid[i] = deviceStateConfig[i];
                }
            }
            int i2 = deviceStateConfig.length;
            combineDeviceStateEstimationsRecursively(deviceStateConfig, deviceStatesTrackedPerUid, new int[i2], 0);
        }

        private void combineDeviceStateEstimationsRecursively(com.android.server.power.stats.MultiStateStats.States[] deviceStateConfig, com.android.server.power.stats.MultiStateStats.States[] deviceStatesTrackedPerUid, int[] stateValues, int state) {
            if (state >= deviceStateConfig.length) {
                com.android.server.power.stats.PowerStatsProcessor.DeviceStateEstimation dse = getDeviceStateEstimate(stateValues);
                com.android.server.power.stats.PowerStatsProcessor.CombinedDeviceStateEstimate cdse = getCombinedDeviceStateEstimate(deviceStatesTrackedPerUid, stateValues);
                if (cdse == null) {
                    cdse = new com.android.server.power.stats.PowerStatsProcessor.CombinedDeviceStateEstimate(deviceStatesTrackedPerUid, stateValues);
                    this.combinedDeviceStateEstimations.add(cdse);
                }
                cdse.deviceStateEstimations.add(dse);
                return;
            }
            if (deviceStateConfig[state].isTracked()) {
                for (int stateValue = 0; stateValue < deviceStateConfig[state].getLabels().length; stateValue++) {
                    stateValues[state] = stateValue;
                    combineDeviceStateEstimationsRecursively(deviceStateConfig, deviceStatesTrackedPerUid, stateValues, state + 1);
                }
                return;
            }
            combineDeviceStateEstimationsRecursively(deviceStateConfig, deviceStatesTrackedPerUid, stateValues, state + 1);
        }

        private void addUidStateEstimations() {
            com.android.server.power.stats.MultiStateStats.States[] deviceStateConfig = this.mConfig.getDeviceStateConfig();
            com.android.server.power.stats.MultiStateStats.States[] uidStateConfig = this.mConfig.getUidStateConfig();
            com.android.server.power.stats.MultiStateStats.States[] uidStatesTrackedForDevice = new com.android.server.power.stats.MultiStateStats.States[uidStateConfig.length];
            com.android.server.power.stats.MultiStateStats.States[] uidStatesNotTrackedForDevice = new com.android.server.power.stats.MultiStateStats.States[uidStateConfig.length];
            for (int i = 0; i < uidStateConfig.length; i++) {
                if (uidStateConfig[i].isTracked()) {
                    int index = com.android.server.power.stats.MultiStateStats.States.findTrackedStateByName(deviceStateConfig, uidStateConfig[i].getName());
                    if (index != -1 && deviceStateConfig[index].isTracked()) {
                        uidStatesTrackedForDevice[i] = uidStateConfig[i];
                    } else {
                        uidStatesNotTrackedForDevice[i] = uidStateConfig[i];
                    }
                }
            }
            int[][] uidStateCombinations = com.android.server.power.stats.PowerStatsProcessor.getAllTrackedStateCombinations(uidStateConfig);
            for (int[] stateValues : uidStateCombinations) {
                com.android.server.power.stats.PowerStatsProcessor.CombinedDeviceStateEstimate combined = getCombinedDeviceStateEstimate(uidStatesTrackedForDevice, stateValues);
                if (combined == null) {
                    android.util.Log.wtf(com.android.server.power.stats.PowerStatsProcessor.TAG, "Mismatch in UID and combined device states: " + com.android.server.power.stats.PowerStatsProcessor.concatLabels(uidStatesTrackedForDevice, stateValues));
                } else {
                    com.android.server.power.stats.PowerStatsProcessor.UidStateEstimate uidStateEstimate = getUidStateEstimate(combined);
                    if (uidStateEstimate == null) {
                        uidStateEstimate = new com.android.server.power.stats.PowerStatsProcessor.UidStateEstimate(combined, uidStatesNotTrackedForDevice);
                        this.uidStateEstimates.add(uidStateEstimate);
                    }
                    uidStateEstimate.proportionalEstimates.add(new com.android.server.power.stats.PowerStatsProcessor.UidStateProportionalEstimate(stateValues));
                }
            }
        }

        public java.lang.String toString() {
            java.lang.StringBuilder sb = new java.lang.StringBuilder();
            sb.append("Step 1. Compute device-wide power estimates for state combinations:\n");
            for (com.android.server.power.stats.PowerStatsProcessor.DeviceStateEstimation deviceStateEstimation : this.deviceStateEstimations) {
                sb.append("    ").append(deviceStateEstimation.id).append("\n");
            }
            sb.append("Step 2. Combine device-wide estimates that are untracked per UID:\n");
            boolean any = false;
            for (com.android.server.power.stats.PowerStatsProcessor.CombinedDeviceStateEstimate cdse : this.combinedDeviceStateEstimations) {
                if (cdse.deviceStateEstimations.size() > 1) {
                    any = true;
                    sb.append("    ").append(cdse.id).append(": ");
                    for (int i = 0; i < cdse.deviceStateEstimations.size(); i++) {
                        if (i != 0) {
                            sb.append(" + ");
                        }
                        sb.append(cdse.deviceStateEstimations.get(i).id);
                    }
                    sb.append("\n");
                }
            }
            if (!any) {
                sb.append("    N/A\n");
            }
            sb.append("Step 3. Proportionally distribute power estimates to UIDs:\n");
            for (com.android.server.power.stats.PowerStatsProcessor.UidStateEstimate uidStateEstimate : this.uidStateEstimates) {
                sb.append("    ").append(uidStateEstimate.combinedDeviceStateEstimate.id).append("\n        among: ");
                for (int i2 = 0; i2 < uidStateEstimate.proportionalEstimates.size(); i2++) {
                    com.android.server.power.stats.PowerStatsProcessor.UidStateProportionalEstimate uspe = uidStateEstimate.proportionalEstimates.get(i2);
                    if (i2 != 0) {
                        sb.append(", ");
                    }
                    sb.append(com.android.server.power.stats.PowerStatsProcessor.concatLabels(uidStateEstimate.states, uspe.stateValues));
                }
                sb.append("\n");
            }
            return sb.toString();
        }

        public com.android.server.power.stats.PowerStatsProcessor.DeviceStateEstimation getDeviceStateEstimate(int[] stateValues) {
            java.lang.String label = com.android.server.power.stats.PowerStatsProcessor.concatLabels(this.mConfig.getDeviceStateConfig(), stateValues);
            for (int i = 0; i < this.deviceStateEstimations.size(); i++) {
                com.android.server.power.stats.PowerStatsProcessor.DeviceStateEstimation deviceStateEstimation = this.deviceStateEstimations.get(i);
                if (deviceStateEstimation.id.equals(label)) {
                    return deviceStateEstimation;
                }
            }
            return null;
        }

        public com.android.server.power.stats.PowerStatsProcessor.CombinedDeviceStateEstimate getCombinedDeviceStateEstimate(com.android.server.power.stats.MultiStateStats.States[] deviceStates, int[] stateValues) {
            java.lang.String label = com.android.server.power.stats.PowerStatsProcessor.concatLabels(deviceStates, stateValues);
            for (int i = 0; i < this.combinedDeviceStateEstimations.size(); i++) {
                com.android.server.power.stats.PowerStatsProcessor.CombinedDeviceStateEstimate cdse = this.combinedDeviceStateEstimations.get(i);
                if (cdse.id.equals(label)) {
                    return cdse;
                }
            }
            return null;
        }

        public com.android.server.power.stats.PowerStatsProcessor.UidStateEstimate getUidStateEstimate(com.android.server.power.stats.PowerStatsProcessor.CombinedDeviceStateEstimate combined) {
            for (int i = 0; i < this.uidStateEstimates.size(); i++) {
                com.android.server.power.stats.PowerStatsProcessor.UidStateEstimate uidStateEstimate = this.uidStateEstimates.get(i);
                if (uidStateEstimate.combinedDeviceStateEstimate == combined) {
                    return uidStateEstimate;
                }
            }
            return null;
        }

        public void resetIntermediates() {
            int i = this.deviceStateEstimations.size();
            while (true) {
                i--;
                if (i < 0) {
                    break;
                } else {
                    this.deviceStateEstimations.get(i).intermediates = null;
                }
            }
            for (int i2 = this.deviceStateEstimations.size() - 1; i2 >= 0; i2--) {
                this.deviceStateEstimations.get(i2).intermediates = null;
            }
            for (int i3 = this.uidStateEstimates.size() - 1; i3 >= 0; i3--) {
                com.android.server.power.stats.PowerStatsProcessor.UidStateEstimate uidStateEstimate = this.uidStateEstimates.get(i3);
                java.util.List<com.android.server.power.stats.PowerStatsProcessor.UidStateProportionalEstimate> proportionalEstimates = uidStateEstimate.proportionalEstimates;
                for (int j = proportionalEstimates.size() - 1; j >= 0; j--) {
                    proportionalEstimates.get(j).intermediates = null;
                }
            }
        }
    }

    protected static class DeviceStateEstimation {
        public final java.lang.String id;
        public java.lang.Object intermediates;
        public final int[] stateValues;

        public DeviceStateEstimation(com.android.server.power.stats.MultiStateStats.States[] config, int[] stateValues) {
            this.id = com.android.server.power.stats.PowerStatsProcessor.concatLabels(config, stateValues);
            this.stateValues = stateValues;
        }
    }

    protected static class CombinedDeviceStateEstimate {
        public java.util.List<com.android.server.power.stats.PowerStatsProcessor.DeviceStateEstimation> deviceStateEstimations = new java.util.ArrayList();
        public final java.lang.String id;
        public java.lang.Object intermediates;

        public CombinedDeviceStateEstimate(com.android.server.power.stats.MultiStateStats.States[] config, int[] stateValues) {
            this.id = com.android.server.power.stats.PowerStatsProcessor.concatLabels(config, stateValues);
        }
    }

    protected static class UidStateEstimate {
        public com.android.server.power.stats.PowerStatsProcessor.CombinedDeviceStateEstimate combinedDeviceStateEstimate;
        public java.util.List<com.android.server.power.stats.PowerStatsProcessor.UidStateProportionalEstimate> proportionalEstimates = new java.util.ArrayList();
        public final com.android.server.power.stats.MultiStateStats.States[] states;

        public UidStateEstimate(com.android.server.power.stats.PowerStatsProcessor.CombinedDeviceStateEstimate combined, com.android.server.power.stats.MultiStateStats.States[] states) {
            this.combinedDeviceStateEstimate = combined;
            this.states = states;
        }
    }

    protected static class UidStateProportionalEstimate {
        public java.lang.Object intermediates;
        public final int[] stateValues;

        protected UidStateProportionalEstimate(int[] stateValues) {
            this.stateValues = stateValues;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static java.lang.String concatLabels(com.android.server.power.stats.MultiStateStats.States[] config, int[] stateValues) {
        java.util.List<java.lang.String> labels = new java.util.ArrayList<>();
        for (int state = 0; state < config.length; state++) {
            if (config[state] != null && config[state].isTracked()) {
                labels.add(config[state].getName() + "=" + config[state].getLabels()[stateValues[state]]);
            }
        }
        java.util.Collections.sort(labels);
        return labels.toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int[][] getAllTrackedStateCombinations(com.android.server.power.stats.MultiStateStats.States[] states) {
        final java.util.List<int[]> combinations = new java.util.ArrayList<>();
        com.android.server.power.stats.MultiStateStats.States.forEachTrackedStateCombination(states, new java.util.function.Consumer() { // from class: com.android.server.power.stats.PowerStatsProcessor$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                int[] iArr = (int[]) obj;
                combinations.add(java.util.Arrays.copyOf(iArr, iArr.length));
            }
        });
        return (int[][]) combinations.toArray((int[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.Integer.TYPE, combinations.size(), 0));
    }

    public static double uCtoMah(long chargeUC) {
        return chargeUC * MILLIAMPHOUR_PER_MICROCOULOMB;
    }
}
