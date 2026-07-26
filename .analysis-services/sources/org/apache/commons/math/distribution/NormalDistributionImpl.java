package org.apache.commons.math.distribution;

/* JADX INFO: loaded from: classes4.dex */
public class NormalDistributionImpl extends org.apache.commons.math.distribution.AbstractContinuousDistribution implements org.apache.commons.math.distribution.NormalDistribution, java.io.Serializable {
    public static final double DEFAULT_INVERSE_ABSOLUTE_ACCURACY = 1.0E-9d;
    private static final double SQRT2PI = org.apache.commons.math.util.FastMath.sqrt(6.283185307179586d);
    private static final long serialVersionUID = 8589540077390120676L;
    private double mean;
    private final double solverAbsoluteAccuracy;
    private double standardDeviation;

    public NormalDistributionImpl(double mean, double sd) {
        this(mean, sd, 1.0E-9d);
    }

    public NormalDistributionImpl(double mean, double sd, double inverseCumAccuracy) {
        this.mean = 0.0d;
        this.standardDeviation = 1.0d;
        setMeanInternal(mean);
        setStandardDeviationInternal(sd);
        this.solverAbsoluteAccuracy = inverseCumAccuracy;
    }

    public NormalDistributionImpl() {
        this(0.0d, 1.0d);
    }

    @Override // org.apache.commons.math.distribution.NormalDistribution
    public double getMean() {
        return this.mean;
    }

    @Override // org.apache.commons.math.distribution.NormalDistribution
    @java.lang.Deprecated
    public void setMean(double mean) {
        setMeanInternal(mean);
    }

    private void setMeanInternal(double newMean) {
        this.mean = newMean;
    }

    @Override // org.apache.commons.math.distribution.NormalDistribution
    public double getStandardDeviation() {
        return this.standardDeviation;
    }

    @Override // org.apache.commons.math.distribution.NormalDistribution
    @java.lang.Deprecated
    public void setStandardDeviation(double sd) {
        setStandardDeviationInternal(sd);
    }

    private void setStandardDeviationInternal(double sd) {
        if (sd <= 0.0d) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.NOT_POSITIVE_STANDARD_DEVIATION, java.lang.Double.valueOf(sd));
        }
        this.standardDeviation = sd;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math.distribution.NormalDistribution, org.apache.commons.math.distribution.HasDensity
    @java.lang.Deprecated
    public double density(java.lang.Double x) {
        return density(x.doubleValue());
    }

    @Override // org.apache.commons.math.distribution.AbstractContinuousDistribution
    public double density(double x) {
        double x0 = x - this.mean;
        return org.apache.commons.math.util.FastMath.exp(((-x0) * x0) / ((this.standardDeviation * 2.0d) * this.standardDeviation)) / (this.standardDeviation * SQRT2PI);
    }

    @Override // org.apache.commons.math.distribution.Distribution
    public double cumulativeProbability(double x) throws org.apache.commons.math.MathException {
        double dev = x - this.mean;
        if (org.apache.commons.math.util.FastMath.abs(dev) > this.standardDeviation * 40.0d) {
            return dev < 0.0d ? 0.0d : 1.0d;
        }
        return (org.apache.commons.math.special.Erf.erf(dev / (this.standardDeviation * org.apache.commons.math.util.FastMath.sqrt(2.0d))) + 1.0d) * 0.5d;
    }

    @Override // org.apache.commons.math.distribution.AbstractContinuousDistribution
    protected double getSolverAbsoluteAccuracy() {
        return this.solverAbsoluteAccuracy;
    }

    @Override // org.apache.commons.math.distribution.AbstractContinuousDistribution, org.apache.commons.math.distribution.ContinuousDistribution
    public double inverseCumulativeProbability(double p) throws org.apache.commons.math.MathException {
        if (p == 0.0d) {
            return Double.NEGATIVE_INFINITY;
        }
        if (p == 1.0d) {
            return Double.POSITIVE_INFINITY;
        }
        return super.inverseCumulativeProbability(p);
    }

    @Override // org.apache.commons.math.distribution.AbstractContinuousDistribution
    public double sample() throws org.apache.commons.math.MathException {
        return this.randomData.nextGaussian(this.mean, this.standardDeviation);
    }

    @Override // org.apache.commons.math.distribution.AbstractContinuousDistribution
    protected double getDomainLowerBound(double p) {
        if (p < 0.5d) {
            return -1.7976931348623157E308d;
        }
        double ret = this.mean;
        return ret;
    }

    @Override // org.apache.commons.math.distribution.AbstractContinuousDistribution
    protected double getDomainUpperBound(double p) {
        if (p < 0.5d) {
            double ret = this.mean;
            return ret;
        }
        return Double.MAX_VALUE;
    }

    @Override // org.apache.commons.math.distribution.AbstractContinuousDistribution
    protected double getInitialDomain(double p) {
        if (p < 0.5d) {
            double ret = this.mean - this.standardDeviation;
            return ret;
        }
        if (p > 0.5d) {
            double ret2 = this.mean + this.standardDeviation;
            return ret2;
        }
        double ret3 = this.mean;
        return ret3;
    }

    public double getSupportLowerBound() {
        return Double.NEGATIVE_INFINITY;
    }

    public double getSupportUpperBound() {
        return Double.POSITIVE_INFINITY;
    }

    public double getNumericalVariance() {
        double s = getStandardDeviation();
        return s * s;
    }
}
