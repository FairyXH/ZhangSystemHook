package com.android.server.power.stats;

/* JADX INFO: loaded from: classes3.dex */
public class PhoneCallPowerStatsProcessor extends com.android.server.power.stats.PowerStatsProcessor {
    private final com.android.internal.os.PowerStats.Descriptor mDescriptor;
    private com.android.internal.os.PowerStats.Descriptor mMobileRadioStatsDescriptor;
    private com.android.server.power.stats.MobileRadioPowerStatsLayout mMobileRadioStatsLayout;
    private final com.android.server.power.stats.PowerStatsLayout mStatsLayout = new com.android.server.power.stats.PowerStatsLayout();
    private final long[] mTmpDeviceStats;
    private long[] mTmpMobileRadioDeviceStats;

    public PhoneCallPowerStatsProcessor() {
        this.mStatsLayout.addDeviceSectionPowerEstimate();
        android.os.PersistableBundle extras = new android.os.PersistableBundle();
        this.mStatsLayout.toExtras(extras);
        this.mDescriptor = new com.android.internal.os.PowerStats.Descriptor(14, this.mStatsLayout.getDeviceStatsArrayLength(), (android.util.SparseArray) null, 0, 0, extras);
        this.mTmpDeviceStats = new long[this.mDescriptor.statsArrayLength];
    }

    @Override // com.android.server.power.stats.PowerStatsProcessor
    void finish(final com.android.server.power.stats.PowerComponentAggregatedPowerStats stats, long timestampMs) {
        stats.setPowerStatsDescriptor(this.mDescriptor);
        final com.android.server.power.stats.PowerComponentAggregatedPowerStats mobileRadioStats = stats.getAggregatedPowerStats().getPowerComponentStats(8);
        if (mobileRadioStats == null) {
            return;
        }
        if (this.mMobileRadioStatsDescriptor == null) {
            this.mMobileRadioStatsDescriptor = mobileRadioStats.getPowerStatsDescriptor();
            if (this.mMobileRadioStatsDescriptor == null) {
                return;
            }
            this.mMobileRadioStatsLayout = new com.android.server.power.stats.MobileRadioPowerStatsLayout(this.mMobileRadioStatsDescriptor);
            this.mTmpMobileRadioDeviceStats = new long[this.mMobileRadioStatsDescriptor.statsArrayLength];
        }
        com.android.server.power.stats.MultiStateStats.States[] deviceStateConfig = mobileRadioStats.getConfig().getDeviceStateConfig();
        com.android.server.power.stats.MultiStateStats.States.forEachTrackedStateCombination(deviceStateConfig, new java.util.function.Consumer() { // from class: com.android.server.power.stats.PhoneCallPowerStatsProcessor$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$finish$0(mobileRadioStats, stats, (int[]) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$finish$0(com.android.server.power.stats.PowerComponentAggregatedPowerStats mobileRadioStats, com.android.server.power.stats.PowerComponentAggregatedPowerStats stats, int[] states) {
        mobileRadioStats.getDeviceStats(this.mTmpMobileRadioDeviceStats, states);
        double callPowerEstimate = this.mMobileRadioStatsLayout.getDeviceCallPowerEstimate(this.mTmpMobileRadioDeviceStats);
        this.mStatsLayout.setDevicePowerEstimate(this.mTmpDeviceStats, callPowerEstimate);
        stats.setDeviceStats(states, this.mTmpDeviceStats);
    }
}
