package com.android.server.location.gnss;

/* JADX INFO: loaded from: classes2.dex */
public class GnssPowerStats {
    private final int mElapsedRealtimeFlags;
    private final long mElapsedRealtimeNanos;
    private final double mElapsedRealtimeUncertaintyNanos;
    private final double mMultibandAcquisitionModeEnergyMilliJoule;
    private final double mMultibandTrackingModeEnergyMilliJoule;
    private final double[] mOtherModesEnergyMilliJoule;
    private final double mSinglebandAcquisitionModeEnergyMilliJoule;
    private final double mSinglebandTrackingModeEnergyMilliJoule;
    private final double mTotalEnergyMilliJoule;

    public GnssPowerStats(int elapsedRealtimeFlags, long elapsedRealtimeNanos, double elapsedRealtimeUncertaintyNanos, double totalEnergyMilliJoule, double singlebandTrackingModeEnergyMilliJoule, double multibandTrackingModeEnergyMilliJoule, double singlebandAcquisitionModeEnergyMilliJoule, double multibandAcquisitionModeEnergyMilliJoule, double[] otherModesEnergyMilliJoule) {
        this.mElapsedRealtimeFlags = elapsedRealtimeFlags;
        this.mElapsedRealtimeNanos = elapsedRealtimeNanos;
        this.mElapsedRealtimeUncertaintyNanos = elapsedRealtimeUncertaintyNanos;
        this.mTotalEnergyMilliJoule = totalEnergyMilliJoule;
        this.mSinglebandTrackingModeEnergyMilliJoule = singlebandTrackingModeEnergyMilliJoule;
        this.mMultibandTrackingModeEnergyMilliJoule = multibandTrackingModeEnergyMilliJoule;
        this.mSinglebandAcquisitionModeEnergyMilliJoule = singlebandAcquisitionModeEnergyMilliJoule;
        this.mMultibandAcquisitionModeEnergyMilliJoule = multibandAcquisitionModeEnergyMilliJoule;
        this.mOtherModesEnergyMilliJoule = otherModesEnergyMilliJoule;
    }

    public boolean hasElapsedRealtimeNanos() {
        return (this.mElapsedRealtimeFlags & 1) != 0;
    }

    public boolean hasElapsedRealtimeUncertaintyNanos() {
        return (this.mElapsedRealtimeFlags & 2) != 0;
    }

    public long getElapsedRealtimeNanos() {
        return this.mElapsedRealtimeNanos;
    }

    public double getElapsedRealtimeUncertaintyNanos() {
        return this.mElapsedRealtimeUncertaintyNanos;
    }

    public double getTotalEnergyMilliJoule() {
        return this.mTotalEnergyMilliJoule;
    }

    public double getSinglebandTrackingModeEnergyMilliJoule() {
        return this.mSinglebandTrackingModeEnergyMilliJoule;
    }

    public double getMultibandTrackingModeEnergyMilliJoule() {
        return this.mMultibandTrackingModeEnergyMilliJoule;
    }

    public double getSinglebandAcquisitionModeEnergyMilliJoule() {
        return this.mSinglebandAcquisitionModeEnergyMilliJoule;
    }

    public double getMultibandAcquisitionModeEnergyMilliJoule() {
        return this.mMultibandAcquisitionModeEnergyMilliJoule;
    }

    public double[] getOtherModesEnergyMilliJoule() {
        return this.mOtherModesEnergyMilliJoule;
    }

    public void validate() {
        com.android.internal.util.Preconditions.checkArgument(hasElapsedRealtimeNanos());
    }

    public void dump(java.io.FileDescriptor fd, android.util.IndentingPrintWriter ipw, java.lang.String[] args, android.location.GnssCapabilities capabilities) {
        if (hasElapsedRealtimeNanos()) {
            ipw.print("time: ");
            ipw.print(android.util.TimeUtils.formatRealtime(java.util.concurrent.TimeUnit.NANOSECONDS.toMillis(this.mElapsedRealtimeNanos)));
            if (hasElapsedRealtimeUncertaintyNanos() && this.mElapsedRealtimeUncertaintyNanos != 0.0d) {
                ipw.print(" +/- ");
                ipw.print(java.util.concurrent.TimeUnit.NANOSECONDS.toMillis((long) this.mElapsedRealtimeUncertaintyNanos));
            }
        }
        if (capabilities.hasPowerTotal()) {
            ipw.print("total power: ");
            ipw.print(this.mTotalEnergyMilliJoule);
            ipw.println("mJ");
        }
        if (capabilities.hasPowerSinglebandTracking()) {
            ipw.print("single-band tracking power: ");
            ipw.print(this.mSinglebandTrackingModeEnergyMilliJoule);
            ipw.println("mJ");
        }
        if (capabilities.hasPowerMultibandTracking()) {
            ipw.print("multi-band tracking power: ");
            ipw.print(this.mMultibandTrackingModeEnergyMilliJoule);
            ipw.println("mJ");
        }
        if (capabilities.hasPowerSinglebandAcquisition()) {
            ipw.print("single-band acquisition power: ");
            ipw.print(this.mSinglebandAcquisitionModeEnergyMilliJoule);
            ipw.println("mJ");
        }
        if (capabilities.hasPowerMultibandAcquisition()) {
            ipw.print("multi-band acquisition power: ");
            ipw.print(this.mMultibandAcquisitionModeEnergyMilliJoule);
            ipw.println("mJ");
        }
        if (capabilities.hasPowerOtherModes()) {
            for (int i = 0; i < this.mOtherModesEnergyMilliJoule.length; i++) {
                ipw.print("other mode [" + i + "] power: ");
                ipw.print(this.mOtherModesEnergyMilliJoule[i]);
                ipw.println("mJ");
            }
        }
    }
}
