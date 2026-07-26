package org.apache.commons.math.distribution;

/* JADX INFO: loaded from: classes4.dex */
public class ExponentialDistributionImpl extends org.apache.commons.math.distribution.AbstractContinuousDistribution implements org.apache.commons.math.distribution.ExponentialDistribution, java.io.Serializable {
    public static final double DEFAULT_INVERSE_ABSOLUTE_ACCURACY = 1.0E-9d;
    private static final long serialVersionUID = 2401296428283614780L;
    private double mean;
    private final double solverAbsoluteAccuracy;

    public ExponentialDistributionImpl(double mean) {
        this(mean, 1.0E-9d);
    }

    public ExponentialDistributionImpl(double mean, double inverseCumAccuracy) {
        setMeanInternal(mean);
        this.solverAbsoluteAccuracy = inverseCumAccuracy;
    }

    @Override // org.apache.commons.math.distribution.ExponentialDistribution
    @java.lang.Deprecated
    public void setMean(double mean) {
        setMeanInternal(mean);
    }

    private void setMeanInternal(double newMean) {
        if (newMean <= 0.0d) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.NOT_POSITIVE_MEAN, java.lang.Double.valueOf(newMean));
        }
        this.mean = newMean;
    }

    @Override // org.apache.commons.math.distribution.ExponentialDistribution
    public double getMean() {
        return this.mean;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math.distribution.ExponentialDistribution, org.apache.commons.math.distribution.HasDensity
    @java.lang.Deprecated
    public double density(java.lang.Double x) {
        return density(x.doubleValue());
    }

    @Override // org.apache.commons.math.distribution.AbstractContinuousDistribution
    public double density(double x) {
        if (x < 0.0d) {
            return 0.0d;
        }
        return org.apache.commons.math.util.FastMath.exp((-x) / this.mean) / this.mean;
    }

    @Override // org.apache.commons.math.distribution.Distribution
    public double cumulativeProbability(double x) throws org.apache.commons.math.MathException {
        if (x <= 0.0d) {
            return 0.0d;
        }
        double ret = -x;
        return 1.0d - org.apache.commons.math.util.FastMath.exp(ret / this.mean);
    }

    @Override // org.apache.commons.math.distribution.AbstractContinuousDistribution, org.apache.commons.math.distribution.ContinuousDistribution
    public double inverseCumulativeProbability(double p) throws org.apache.commons.math.MathException {
        if (p < 0.0d || p > 1.0d) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.OUT_OF_RANGE_SIMPLE, java.lang.Double.valueOf(p), java.lang.Double.valueOf(0.0d), java.lang.Double.valueOf(1.0d));
        }
        if (p == 1.0d) {
            return Double.POSITIVE_INFINITY;
        }
        double ret = this.mean;
        return (-ret) * org.apache.commons.math.util.FastMath.log(1.0d - p);
    }

    @Override // org.apache.commons.math.distribution.AbstractContinuousDistribution
    public double sample() throws org.apache.commons.math.MathException {
        return this.randomData.nextExponential(this.mean);
    }

    @Override // org.apache.commons.math.distribution.AbstractContinuousDistribution
    protected double getDomainLowerBound(double p) {
        return 0.0d;
    }

    @Override // org.apache.commons.math.distribution.AbstractContinuousDistribution
    protected double getDomainUpperBound(double p) {
        if (p < 0.5d) {
            return this.mean;
        }
        return Double.MAX_VALUE;
    }

    @Override // org.apache.commons.math.distribution.AbstractContinuousDistribution
    protected double getInitialDomain(double p) {
        if (p < 0.5d) {
            return this.mean * 0.5d;
        }
        return this.mean;
    }

    @Override // org.apache.commons.math.distribution.AbstractContinuousDistribution
    protected double getSolverAbsoluteAccuracy() {
        return this.solverAbsoluteAccuracy;
    }

    public double getSupportLowerBound() {
        return 0.0d;
    }

    public double getSupportUpperBound() {
        return Double.POSITIVE_INFINITY;
    }

    public double getNumericalMean() {
        return getMean();
    }

    public double getNumericalVariance() {
        double m = getMean();
        return m * m;
    }
}
