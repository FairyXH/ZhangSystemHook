package com.android.server.power.stats;

/* JADX INFO: loaded from: classes3.dex */
public class CpuPowerStatsCollector extends com.android.server.power.stats.PowerStatsCollector {
    private static final int DEFAULT_CPU_POWER_BRACKETS = 3;
    private static final int DEFAULT_CPU_POWER_BRACKETS_PER_ENERGY_CONSUMER = 2;
    private static final long ENERGY_UNSPECIFIED = -1;
    private static final long NANOS_PER_MILLIS = 1000000;
    private static final java.lang.String TAG = "CpuPowerStatsCollector";
    private com.android.server.power.stats.PowerStatsCollector.ConsumedEnergyRetriever mConsumedEnergyRetriever;
    private int[] mCpuEnergyConsumerIds;
    private com.android.internal.os.PowerStats mCpuPowerStats;
    private com.android.internal.os.CpuScalingPolicies mCpuScalingPolicies;
    private long[] mCpuTimeByScalingStep;
    private int mDefaultCpuPowerBrackets;
    private int mDefaultCpuPowerBracketsPerEnergyConsumer;
    private final com.android.server.power.stats.CpuPowerStatsCollector.Injector mInjector;
    private boolean mIsInitialized;
    private boolean mIsPerUidTimeInStateSupported;
    private com.android.server.power.stats.CpuPowerStatsCollector.KernelCpuStatsReader mKernelCpuStatsReader;
    private long[] mLastConsumedEnergyUws;
    private long mLastUpdateTimestampNanos;
    private long mLastUpdateUptimeMillis;
    private int mLastVoltageMv;
    private com.android.server.power.stats.CpuPowerStatsLayout mLayout;
    private com.android.internal.os.PowerProfile mPowerProfile;
    private com.android.internal.os.PowerStats.Descriptor mPowerStatsDescriptor;
    private long[] mTempCpuTimeByScalingStep;
    private long[] mTempUidStats;
    private final android.util.SparseArray<com.android.server.power.stats.CpuPowerStatsCollector.UidStats> mUidStats;
    private java.util.function.IntSupplier mVoltageSupplier;

    interface KernelCpuStatsCallback {
        void processUidStats(int i, long[] jArr);
    }

    interface Injector {
        com.android.internal.os.Clock getClock();

        com.android.server.power.stats.PowerStatsCollector.ConsumedEnergyRetriever getConsumedEnergyRetriever();

        com.android.internal.os.CpuScalingPolicies getCpuScalingPolicies();

        android.os.Handler getHandler();

        com.android.server.power.stats.CpuPowerStatsCollector.KernelCpuStatsReader getKernelCpuStatsReader();

        com.android.internal.os.PowerProfile getPowerProfile();

        long getPowerStatsCollectionThrottlePeriod(java.lang.String str);

        com.android.server.power.stats.PowerStatsUidResolver getUidResolver();

        java.util.function.IntSupplier getVoltageSupplier();

        default int getDefaultCpuPowerBrackets() {
            return 3;
        }

        default int getDefaultCpuPowerBracketsPerEnergyConsumer() {
            return 2;
        }
    }

    CpuPowerStatsCollector(com.android.server.power.stats.CpuPowerStatsCollector.Injector injector) {
        super(injector.getHandler(), injector.getPowerStatsCollectionThrottlePeriod(android.os.BatteryConsumer.powerComponentIdToString(1)), injector.getUidResolver(), injector.getClock());
        this.mUidStats = new android.util.SparseArray<>();
        this.mCpuEnergyConsumerIds = new int[0];
        this.mInjector = injector;
    }

    private boolean ensureInitialized() {
        if (this.mIsInitialized) {
            return true;
        }
        if (!isEnabled()) {
            return false;
        }
        this.mCpuScalingPolicies = this.mInjector.getCpuScalingPolicies();
        this.mPowerProfile = this.mInjector.getPowerProfile();
        this.mKernelCpuStatsReader = this.mInjector.getKernelCpuStatsReader();
        this.mConsumedEnergyRetriever = this.mInjector.getConsumedEnergyRetriever();
        this.mVoltageSupplier = this.mInjector.getVoltageSupplier();
        this.mDefaultCpuPowerBrackets = this.mInjector.getDefaultCpuPowerBrackets();
        this.mDefaultCpuPowerBracketsPerEnergyConsumer = this.mInjector.getDefaultCpuPowerBracketsPerEnergyConsumer();
        this.mIsPerUidTimeInStateSupported = this.mKernelCpuStatsReader.isSupportedFeature();
        this.mCpuEnergyConsumerIds = this.mConsumedEnergyRetriever.getEnergyConsumerIds(2);
        this.mLastConsumedEnergyUws = new long[this.mCpuEnergyConsumerIds.length];
        java.util.Arrays.fill(this.mLastConsumedEnergyUws, -1L);
        int cpuScalingStepCount = this.mCpuScalingPolicies.getScalingStepCount();
        this.mCpuTimeByScalingStep = new long[cpuScalingStepCount];
        this.mTempCpuTimeByScalingStep = new long[cpuScalingStepCount];
        int[] scalingStepToPowerBracketMap = initPowerBrackets();
        this.mLayout = new com.android.server.power.stats.CpuPowerStatsLayout();
        this.mLayout.addDeviceSectionCpuTimeByScalingStep(cpuScalingStepCount);
        this.mLayout.addDeviceSectionCpuTimeByCluster(this.mCpuScalingPolicies.getPolicies().length);
        this.mLayout.addDeviceSectionUsageDuration();
        this.mLayout.addDeviceSectionEnergyConsumers(this.mCpuEnergyConsumerIds.length);
        this.mLayout.addDeviceSectionPowerEstimate();
        this.mLayout.addUidSectionCpuTimeByPowerBracket(scalingStepToPowerBracketMap);
        this.mLayout.addUidSectionPowerEstimate();
        android.os.PersistableBundle extras = new android.os.PersistableBundle();
        this.mLayout.toExtras(extras);
        this.mPowerStatsDescriptor = new com.android.internal.os.PowerStats.Descriptor(1, this.mLayout.getDeviceStatsArrayLength(), (android.util.SparseArray) null, 0, this.mLayout.getUidStatsArrayLength(), extras);
        this.mCpuPowerStats = new com.android.internal.os.PowerStats(this.mPowerStatsDescriptor);
        this.mTempUidStats = new long[this.mLayout.getCpuPowerBracketCount()];
        this.mIsInitialized = true;
        return true;
    }

    private int[] initPowerBrackets() {
        if (this.mPowerProfile.getCpuPowerBracketCount() != -1) {
            return initPowerBracketsFromPowerProfile();
        }
        if (this.mCpuEnergyConsumerIds.length == 0 || this.mCpuEnergyConsumerIds.length == 1) {
            return initDefaultPowerBrackets(this.mDefaultCpuPowerBrackets);
        }
        if (this.mCpuScalingPolicies.getPolicies().length == this.mCpuEnergyConsumerIds.length) {
            return initPowerBracketsByCluster(this.mDefaultCpuPowerBracketsPerEnergyConsumer);
        }
        android.util.Slog.i(TAG, "Assigning a single power brackets to each CPU_CLUSTER energy consumer. Number of CPU clusters (" + this.mCpuScalingPolicies.getPolicies().length + ") does not match the number of energy consumers (" + this.mCpuEnergyConsumerIds.length + ").  Using default power bucket assignment.");
        return initDefaultPowerBrackets(this.mDefaultCpuPowerBrackets);
    }

    private int[] initPowerBracketsFromPowerProfile() {
        int[] stepToBracketMap = new int[this.mCpuScalingPolicies.getScalingStepCount()];
        int index = 0;
        for (int policy : this.mCpuScalingPolicies.getPolicies()) {
            int[] frequencies = this.mCpuScalingPolicies.getFrequencies(policy);
            int step = 0;
            while (step < frequencies.length) {
                int bracket = this.mPowerProfile.getCpuPowerBracketForScalingStep(policy, step);
                stepToBracketMap[index] = bracket;
                step++;
                index++;
            }
        }
        return stepToBracketMap;
    }

    private int[] initPowerBracketsByCluster(int defaultBracketCountPerCluster) {
        int[] stepToBracketMap = new int[this.mCpuScalingPolicies.getScalingStepCount()];
        int index = 0;
        int bracketBase = 0;
        int[] policies = this.mCpuScalingPolicies.getPolicies();
        for (int policy : policies) {
            int[] frequencies = this.mCpuScalingPolicies.getFrequencies(policy);
            double[] powerByStep = new double[frequencies.length];
            for (int step = 0; step < frequencies.length; step++) {
                powerByStep[step] = this.mPowerProfile.getAveragePowerForCpuScalingStep(policy, step);
            }
            int step2 = frequencies.length;
            int[] policyStepToBracketMap = new int[step2];
            mapScalingStepsToDefaultBrackets(policyStepToBracketMap, powerByStep, defaultBracketCountPerCluster);
            int maxBracket = 0;
            int step3 = 0;
            while (step3 < frequencies.length) {
                int bracket = policyStepToBracketMap[step3] + bracketBase;
                int index2 = index + 1;
                stepToBracketMap[index] = bracket;
                if (bracket > maxBracket) {
                    maxBracket = bracket;
                }
                step3++;
                index = index2;
            }
            bracketBase = maxBracket + 1;
        }
        return stepToBracketMap;
    }

    private int[] initDefaultPowerBrackets(int defaultCpuPowerBracketCount) {
        int[] stepToBracketMap = new int[this.mCpuScalingPolicies.getScalingStepCount()];
        double[] powerByStep = new double[this.mCpuScalingPolicies.getScalingStepCount()];
        int index = 0;
        int[] policies = this.mCpuScalingPolicies.getPolicies();
        for (int policy : policies) {
            int[] frequencies = this.mCpuScalingPolicies.getFrequencies(policy);
            int step = 0;
            while (step < frequencies.length) {
                powerByStep[index] = this.mPowerProfile.getAveragePowerForCpuScalingStep(policy, step);
                step++;
                index++;
            }
        }
        mapScalingStepsToDefaultBrackets(stepToBracketMap, powerByStep, defaultCpuPowerBracketCount);
        return stepToBracketMap;
    }

    private static void mapScalingStepsToDefaultBrackets(int[] stepToBracketMap, double[] powerByStep, int defaultCpuPowerBracketCount) {
        double minPower = Double.MAX_VALUE;
        double maxPower = Double.MIN_VALUE;
        for (double power : powerByStep) {
            if (power < minPower) {
                minPower = power;
            }
            if (power > maxPower) {
                maxPower = power;
            }
        }
        if (powerByStep.length <= defaultCpuPowerBracketCount) {
            for (int index = 0; index < stepToBracketMap.length; index++) {
                stepToBracketMap[index] = index;
            }
            return;
        }
        double minLogPower = java.lang.Math.log(minPower);
        double logBracket = (java.lang.Math.log(maxPower) - minLogPower) / ((double) defaultCpuPowerBracketCount);
        for (int step = 0; step < powerByStep.length; step++) {
            int bracket = (int) ((java.lang.Math.log(powerByStep[step]) - minLogPower) / logBracket);
            if (bracket >= defaultCpuPowerBracketCount) {
                bracket = defaultCpuPowerBracketCount - 1;
            }
            stepToBracketMap[step] = bracket;
        }
    }

    public void dumpCpuPowerBracketsLocked(java.io.PrintWriter pw) {
        if (!ensureInitialized() || this.mLayout == null) {
            return;
        }
        pw.println("CPU power brackets; cluster/freq in MHz(avg current in mA):");
        for (int bracket = 0; bracket < this.mLayout.getCpuPowerBracketCount(); bracket++) {
            pw.print("    ");
            pw.print(bracket);
            pw.print(": ");
            pw.println(getCpuPowerBracketDescription(bracket));
        }
    }

    public java.lang.String getCpuPowerBracketDescription(int powerBracket) {
        if (!ensureInitialized()) {
            return "";
        }
        int[] stepToPowerBracketMap = this.mLayout.getScalingStepToPowerBracketMap();
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        int index = 0;
        int[] policies = this.mCpuScalingPolicies.getPolicies();
        for (int policy : policies) {
            int[] freqs = this.mCpuScalingPolicies.getFrequencies(policy);
            for (int step = 0; step < freqs.length; step++) {
                if (stepToPowerBracketMap[index] == powerBracket) {
                    if (sb.length() != 0) {
                        sb.append(", ");
                    }
                    if (policies.length > 1) {
                        sb.append(policy).append('/');
                    }
                    sb.append(freqs[step] / 1000);
                    sb.append('(');
                    sb.append(java.lang.String.format(java.util.Locale.US, "%.1f", java.lang.Double.valueOf(this.mPowerProfile.getAveragePowerForCpuScalingStep(policy, step))));
                    sb.append(')');
                }
                index++;
            }
        }
        return sb.toString();
    }

    public com.android.internal.os.PowerStats.Descriptor getPowerStatsDescriptor() {
        if (!ensureInitialized()) {
            return null;
        }
        return this.mPowerStatsDescriptor;
    }

    @Override // com.android.server.power.stats.PowerStatsCollector
    protected com.android.internal.os.PowerStats collectStats() {
        if (!ensureInitialized() || !this.mIsPerUidTimeInStateSupported) {
            return null;
        }
        this.mCpuPowerStats.uidStats.clear();
        long newTimestampNanos = this.mKernelCpuStatsReader.readCpuStats(new com.android.server.power.stats.CpuPowerStatsCollector.KernelCpuStatsCallback() { // from class: com.android.server.power.stats.CpuPowerStatsCollector$$ExternalSyntheticLambda0
            @Override // com.android.server.power.stats.CpuPowerStatsCollector.KernelCpuStatsCallback
            public final void processUidStats(int i, long[] jArr) {
                this.f$0.processUidStats(i, jArr);
            }
        }, this.mLayout.getScalingStepToPowerBracketMap(), this.mLastUpdateTimestampNanos, this.mTempCpuTimeByScalingStep, this.mTempUidStats);
        for (int step = this.mLayout.getCpuScalingStepCount() - 1; step >= 0; step--) {
            this.mLayout.setTimeByScalingStep(this.mCpuPowerStats.stats, step, this.mTempCpuTimeByScalingStep[step] - this.mCpuTimeByScalingStep[step]);
            this.mCpuTimeByScalingStep[step] = this.mTempCpuTimeByScalingStep[step];
        }
        this.mCpuPowerStats.durationMs = (newTimestampNanos - this.mLastUpdateTimestampNanos) / NANOS_PER_MILLIS;
        this.mLastUpdateTimestampNanos = newTimestampNanos;
        long uptimeMillis = this.mClock.uptimeMillis();
        long uptimeDelta = uptimeMillis - this.mLastUpdateUptimeMillis;
        this.mLastUpdateUptimeMillis = uptimeMillis;
        if (uptimeDelta > this.mCpuPowerStats.durationMs) {
            uptimeDelta = this.mCpuPowerStats.durationMs;
        }
        this.mLayout.setUsageDuration(this.mCpuPowerStats.stats, uptimeDelta);
        if (this.mCpuEnergyConsumerIds.length != 0) {
            collectEnergyConsumers();
        }
        return this.mCpuPowerStats;
    }

    private void collectEnergyConsumers() {
        int voltageMv = this.mVoltageSupplier.getAsInt();
        if (voltageMv <= 0) {
            android.util.Slog.wtf(TAG, "Unexpected battery voltage (" + voltageMv + " mV) when querying energy consumers");
            return;
        }
        int averageVoltage = this.mLastVoltageMv != 0 ? (this.mLastVoltageMv + voltageMv) / 2 : voltageMv;
        this.mLastVoltageMv = voltageMv;
        long[] energyUws = this.mConsumedEnergyRetriever.getConsumedEnergyUws(this.mCpuEnergyConsumerIds);
        if (energyUws == null) {
            return;
        }
        for (int i = energyUws.length - 1; i >= 0; i--) {
            long energyDelta = this.mLastConsumedEnergyUws[i] != -1 ? energyUws[i] - this.mLastConsumedEnergyUws[i] : 0L;
            if (energyDelta < 0) {
                energyDelta = 0;
            }
            this.mLayout.setConsumedEnergy(this.mCpuPowerStats.stats, i, uJtoUc(energyDelta, averageVoltage));
            this.mLastConsumedEnergyUws[i] = energyUws[i];
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processUidStats(int uid, long[] timeByPowerBracket) {
        int ownerUid;
        int powerBracketCount = this.mLayout.getCpuPowerBracketCount();
        com.android.server.power.stats.CpuPowerStatsCollector.UidStats uidStats = this.mUidStats.get(uid);
        if (uidStats == null) {
            uidStats = new com.android.server.power.stats.CpuPowerStatsCollector.UidStats();
            uidStats.timeByPowerBracket = new long[powerBracketCount];
            uidStats.stats = new long[this.mLayout.getUidStatsArrayLength()];
            this.mUidStats.put(uid, uidStats);
        }
        boolean nonzero = false;
        for (int bracket = powerBracketCount - 1; bracket >= 0; bracket--) {
            long delta = java.lang.Math.max(0L, timeByPowerBracket[bracket] - uidStats.timeByPowerBracket[bracket]);
            if (delta != 0) {
                nonzero = true;
            }
            this.mLayout.setUidTimeByPowerBracket(uidStats.stats, bracket, delta);
            uidStats.timeByPowerBracket[bracket] = timeByPowerBracket[bracket];
        }
        if (nonzero) {
            if (android.os.Process.isSdkSandboxUid(uid)) {
                ownerUid = android.os.Process.getAppUidForSdkSandboxUid(uid);
            } else {
                ownerUid = this.mUidResolver.mapUid(uid);
            }
            long[] ownerStats = (long[]) this.mCpuPowerStats.uidStats.get(ownerUid);
            if (ownerStats == null) {
                this.mCpuPowerStats.uidStats.put(ownerUid, uidStats.stats);
                return;
            }
            for (int i = 0; i < ownerStats.length; i++) {
                ownerStats[i] = ownerStats[i] + uidStats.stats[i];
            }
        }
    }

    @Override // com.android.server.power.stats.PowerStatsCollector
    protected void onUidRemoved(int uid) {
        super.onUidRemoved(uid);
        this.mUidStats.remove(uid);
    }

    public static class KernelCpuStatsReader {
        protected native boolean nativeIsSupportedFeature();

        protected native long nativeReadCpuStats(com.android.server.power.stats.CpuPowerStatsCollector.KernelCpuStatsCallback kernelCpuStatsCallback, int[] iArr, long j, long[] jArr, long[] jArr2);

        protected boolean isSupportedFeature() {
            return nativeIsSupportedFeature();
        }

        protected long readCpuStats(com.android.server.power.stats.CpuPowerStatsCollector.KernelCpuStatsCallback callback, int[] scalingStepToPowerBracketMap, long lastUpdateTimestampNanos, long[] outCpuTimeByScalingStep, long[] tempForUidStats) {
            return nativeReadCpuStats(callback, scalingStepToPowerBracketMap, lastUpdateTimestampNanos, outCpuTimeByScalingStep, tempForUidStats);
        }
    }

    private static class UidStats {
        public long[] stats;
        public long[] timeByPowerBracket;

        private UidStats() {
        }
    }
}
