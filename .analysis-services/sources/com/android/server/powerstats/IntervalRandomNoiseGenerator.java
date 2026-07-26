package com.android.server.powerstats;

/* JADX INFO: loaded from: classes3.dex */
public class IntervalRandomNoiseGenerator {
    private static final int DISTRIBUTION_SAMPLE_SIZE = 17;
    private static final double UNINITIALIZED = -1.0d;
    private final org.apache.commons.math.distribution.AbstractContinuousDistribution mDistribution;
    private final double[] mSamples = new double[17];

    IntervalRandomNoiseGenerator(double alpha) {
        if (alpha <= 1.0d) {
            throw new java.lang.IllegalArgumentException("alpha should be > 1");
        }
        this.mDistribution = new org.apache.commons.math.distribution.BetaDistributionImpl(alpha, 1.0d);
        refresh();
    }

    void reseed(long seed) {
        this.mDistribution.reseedRandomGenerator(seed);
    }

    long addNoise(long lowProbabilityBound, long highProbabilityBound, int stickyKey) throws org.apache.commons.math.MathException {
        double sample = this.mSamples[stickyKey % 17];
        if (sample < 0.0d) {
            try {
                sample = this.mDistribution.sample();
                this.mSamples[stickyKey % 17] = sample;
            } catch (org.apache.commons.math.MathException e) {
                throw new java.lang.IllegalStateException(e);
            }
        }
        return ((long) ((highProbabilityBound - lowProbabilityBound) * sample)) + lowProbabilityBound;
    }

    void refresh() {
        java.util.Arrays.fill(this.mSamples, UNINITIALIZED);
    }
}
