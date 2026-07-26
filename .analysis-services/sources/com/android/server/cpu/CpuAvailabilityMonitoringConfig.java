package com.android.server.cpu;

/* JADX INFO: loaded from: classes.dex */
public final class CpuAvailabilityMonitoringConfig {
    public static final int CPUSET_ALL = 1;
    public static final int CPUSET_BACKGROUND = 2;
    public final int cpuset;
    private final android.util.IntArray mThresholds;

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface Cpuset {
    }

    public android.util.IntArray getThresholds() {
        return this.mThresholds;
    }

    public static final class Builder {
        private final int mCpuset;
        private final android.util.IntArray mThresholds = new android.util.IntArray();

        public Builder(int cpuset, int... thresholds) {
            this.mCpuset = cpuset;
            for (int threshold : thresholds) {
                addThreshold(threshold);
            }
        }

        public com.android.server.cpu.CpuAvailabilityMonitoringConfig.Builder addThreshold(int threshold) {
            if (this.mThresholds.indexOf(threshold) == -1) {
                this.mThresholds.add(threshold);
            }
            return this;
        }

        public com.android.server.cpu.CpuAvailabilityMonitoringConfig build() {
            return new com.android.server.cpu.CpuAvailabilityMonitoringConfig(this);
        }
    }

    public java.lang.String toString() {
        return "CpuAvailabilityMonitoringConfig{cpuset=" + toCpusetString(this.cpuset) + ", mThresholds=" + this.mThresholds + ')';
    }

    public static java.lang.String toCpusetString(int cpuset) {
        switch (cpuset) {
            case 1:
                return "CPUSET_ALL";
            case 2:
                return "CPUSET_BACKGROUND";
            default:
                return "Invalid cpuset: " + cpuset;
        }
    }

    private CpuAvailabilityMonitoringConfig(com.android.server.cpu.CpuAvailabilityMonitoringConfig.Builder builder) {
        if (builder.mCpuset != 1 && builder.mCpuset != 2) {
            throw new java.lang.IllegalStateException("Cpuset must be either CPUSET_ALL (1) or CPUSET_BACKGROUND (2). Builder contains " + builder.mCpuset);
        }
        if (builder.mThresholds.size() == 0) {
            throw new java.lang.IllegalStateException("Must provide at least one threshold");
        }
        this.cpuset = builder.mCpuset;
        this.mThresholds = builder.mThresholds.clone();
    }
}
