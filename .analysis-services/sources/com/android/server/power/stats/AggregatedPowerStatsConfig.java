package com.android.server.power.stats;

/* JADX INFO: loaded from: classes3.dex */
public class AggregatedPowerStatsConfig {
    private static final com.android.server.power.stats.PowerStatsProcessor NO_OP_PROCESSOR;
    static final int POWER_STATE_BATTERY = 0;
    static final int POWER_STATE_OTHER = 1;
    static final int SCREEN_STATE_ON = 0;
    static final int SCREEN_STATE_OTHER = 1;
    static final java.lang.String[] STATE_LABELS_PROCESS_STATE;
    static final java.lang.String STATE_NAME_POWER = "pwr";
    static final java.lang.String STATE_NAME_PROCESS_STATE = "ps";
    static final java.lang.String STATE_NAME_SCREEN = "scr";
    public static final int STATE_POWER = 0;
    public static final int STATE_PROCESS_STATE = 2;
    public static final int STATE_SCREEN = 1;
    private final java.util.List<com.android.server.power.stats.AggregatedPowerStatsConfig.PowerComponent> mPowerComponents = new java.util.ArrayList();
    static final java.lang.String[] STATE_LABELS_POWER = {"pwr-battery", "pwr-other"};
    static final java.lang.String[] STATE_LABELS_SCREEN = {"scr-on", "scr-other"};

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface TrackedState {
    }

    static {
        java.lang.String[] procStateLabels = new java.lang.String[5];
        for (int i = 0; i < 5; i++) {
            procStateLabels[i] = android.os.BatteryConsumer.processStateToString(i);
        }
        STATE_LABELS_PROCESS_STATE = procStateLabels;
        NO_OP_PROCESSOR = new com.android.server.power.stats.PowerStatsProcessor() { // from class: com.android.server.power.stats.AggregatedPowerStatsConfig.1
            @Override // com.android.server.power.stats.PowerStatsProcessor
            void finish(com.android.server.power.stats.PowerComponentAggregatedPowerStats stats, long timestampMs) {
            }
        };
    }

    public static class PowerComponent {
        private final int mPowerComponentId;
        private com.android.server.power.stats.PowerStatsProcessor mProcessor = com.android.server.power.stats.AggregatedPowerStatsConfig.NO_OP_PROCESSOR;
        private int[] mTrackedDeviceStates;
        private int[] mTrackedUidStates;

        PowerComponent(int powerComponentId) {
            this.mPowerComponentId = powerComponentId;
        }

        public com.android.server.power.stats.AggregatedPowerStatsConfig.PowerComponent trackDeviceStates(int... states) {
            if (this.mTrackedDeviceStates != null) {
                throw new java.lang.IllegalStateException("Component is already configured");
            }
            this.mTrackedDeviceStates = states;
            return this;
        }

        public com.android.server.power.stats.AggregatedPowerStatsConfig.PowerComponent trackUidStates(int... states) {
            if (this.mTrackedUidStates != null) {
                throw new java.lang.IllegalStateException("Component is already configured");
            }
            this.mTrackedUidStates = states;
            return this;
        }

        public com.android.server.power.stats.AggregatedPowerStatsConfig.PowerComponent setProcessor(com.android.server.power.stats.PowerStatsProcessor processor) {
            this.mProcessor = processor;
            return this;
        }

        public int getPowerComponentId() {
            return this.mPowerComponentId;
        }

        public com.android.server.power.stats.MultiStateStats.States[] getDeviceStateConfig() {
            return new com.android.server.power.stats.MultiStateStats.States[]{new com.android.server.power.stats.MultiStateStats.States(com.android.server.power.stats.AggregatedPowerStatsConfig.STATE_NAME_POWER, isTracked(this.mTrackedDeviceStates, 0), com.android.server.power.stats.AggregatedPowerStatsConfig.STATE_LABELS_POWER), new com.android.server.power.stats.MultiStateStats.States(com.android.server.power.stats.AggregatedPowerStatsConfig.STATE_NAME_SCREEN, isTracked(this.mTrackedDeviceStates, 1), com.android.server.power.stats.AggregatedPowerStatsConfig.STATE_LABELS_SCREEN)};
        }

        public com.android.server.power.stats.MultiStateStats.States[] getUidStateConfig() {
            return new com.android.server.power.stats.MultiStateStats.States[]{new com.android.server.power.stats.MultiStateStats.States(com.android.server.power.stats.AggregatedPowerStatsConfig.STATE_NAME_POWER, isTracked(this.mTrackedUidStates, 0), com.android.server.power.stats.AggregatedPowerStatsConfig.STATE_LABELS_POWER), new com.android.server.power.stats.MultiStateStats.States(com.android.server.power.stats.AggregatedPowerStatsConfig.STATE_NAME_SCREEN, isTracked(this.mTrackedUidStates, 1), com.android.server.power.stats.AggregatedPowerStatsConfig.STATE_LABELS_SCREEN), new com.android.server.power.stats.MultiStateStats.States(com.android.server.power.stats.AggregatedPowerStatsConfig.STATE_NAME_PROCESS_STATE, isTracked(this.mTrackedUidStates, 2), com.android.server.power.stats.AggregatedPowerStatsConfig.STATE_LABELS_PROCESS_STATE)};
        }

        public com.android.server.power.stats.PowerStatsProcessor getProcessor() {
            return this.mProcessor;
        }

        private boolean isTracked(int[] trackedStates, int state) {
            if (trackedStates == null) {
                return false;
            }
            for (int trackedState : trackedStates) {
                if (trackedState == state) {
                    return true;
                }
            }
            return false;
        }
    }

    public com.android.server.power.stats.AggregatedPowerStatsConfig.PowerComponent trackPowerComponent(int powerComponentId) {
        com.android.server.power.stats.AggregatedPowerStatsConfig.PowerComponent builder = new com.android.server.power.stats.AggregatedPowerStatsConfig.PowerComponent(powerComponentId);
        this.mPowerComponents.add(builder);
        return builder;
    }

    public com.android.server.power.stats.AggregatedPowerStatsConfig.PowerComponent trackPowerComponent(int powerComponentId, int parentPowerComponentId) {
        com.android.server.power.stats.AggregatedPowerStatsConfig.PowerComponent parent = null;
        int i = 0;
        while (true) {
            if (i >= this.mPowerComponents.size()) {
                break;
            }
            com.android.server.power.stats.AggregatedPowerStatsConfig.PowerComponent powerComponent = this.mPowerComponents.get(i);
            if (powerComponent.getPowerComponentId() != parentPowerComponentId) {
                i++;
            } else {
                parent = powerComponent;
                break;
            }
        }
        if (parent == null) {
            throw new java.lang.IllegalArgumentException("Parent component " + parentPowerComponentId + " is not configured");
        }
        com.android.server.power.stats.AggregatedPowerStatsConfig.PowerComponent powerComponent2 = trackPowerComponent(powerComponentId);
        powerComponent2.mTrackedDeviceStates = parent.mTrackedDeviceStates;
        powerComponent2.mTrackedUidStates = parent.mTrackedUidStates;
        return powerComponent2;
    }

    public java.util.List<com.android.server.power.stats.AggregatedPowerStatsConfig.PowerComponent> getPowerComponentsAggregatedStatsConfigs() {
        return this.mPowerComponents;
    }
}
