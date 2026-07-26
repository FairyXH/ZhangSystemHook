package org.apache.commons.math.distribution;

/* JADX INFO: loaded from: classes4.dex */
public class ChiSquaredDistributionImpl extends org.apache.commons.math.distribution.AbstractContinuousDistribution implements org.apache.commons.math.distribution.ChiSquaredDistribution, java.io.Serializable {
    public static final double DEFAULT_INVERSE_ABSOLUTE_ACCURACY = 1.0E-9d;
    private static final long serialVersionUID = -8352658048349159782L;
    private org.apache.commons.math.distribution.GammaDistribution gamma;
    private final double solverAbsoluteAccuracy;

    public ChiSquaredDistributionImpl(double df) {
        this(df, new org.apache.commons.math.distribution.GammaDistributionImpl(df / 2.0d, 2.0d));
    }

    @java.lang.Deprecated
    public ChiSquaredDistributionImpl(double df, org.apache.commons.math.distribution.GammaDistribution g) {
        setGammaInternal(g);
        setDegreesOfFreedomInternal(df);
        this.solverAbsoluteAccuracy = 1.0E-9d;
    }

    public ChiSquaredDistributionImpl(double df, double inverseCumAccuracy) {
        this.gamma = new org.apache.commons.math.distribution.GammaDistributionImpl(df / 2.0d, 2.0d);
        setDegreesOfFreedomInternal(df);
        this.solverAbsoluteAccuracy = inverseCumAccuracy;
    }

    @Override // org.apache.commons.math.distribution.ChiSquaredDistribution
    @java.lang.Deprecated
    public void setDegreesOfFreedom(double degreesOfFreedom) {
        setDegreesOfFreedomInternal(degreesOfFreedom);
    }

    private void setDegreesOfFreedomInternal(double degreesOfFreedom) {
        this.gamma.setAlpha(degreesOfFreedom / 2.0d);
    }

    @Override // org.apache.commons.math.distribution.ChiSquaredDistribution
    public double getDegreesOfFreedom() {
        return this.gamma.getAlpha() * 2.0d;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math.distribution.ChiSquaredDistribution, org.apache.commons.math.distribution.HasDensity
    @java.lang.Deprecated
    public double density(java.lang.Double x) {
        return density(x.doubleValue());
    }

    @Override // org.apache.commons.math.distribution.AbstractContinuousDistribution
    public double density(double x) {
        return this.gamma.density(java.lang.Double.valueOf(x));
    }

    @Override // org.apache.commons.math.distribution.Distribution
    public double cumulativeProbability(double x) throws org.apache.commons.math.MathException {
        return this.gamma.cumulativeProbability(x);
    }

    @Override // org.apache.commons.math.distribution.AbstractContinuousDistribution, org.apache.commons.math.distribution.ContinuousDistribution
    public double inverseCumulativeProbability(double p) throws org.apache.commons.math.MathException {
        if (p == 0.0d) {
            return 0.0d;
        }
        if (p == 1.0d) {
            return Double.POSITIVE_INFINITY;
        }
        return super.inverseCumulativeProbability(p);
    }

    @Override // org.apache.commons.math.distribution.AbstractContinuousDistribution
    protected double getDomainLowerBound(double p) {
        return this.gamma.getBeta() * Double.MIN_VALUE;
    }

    @Override // org.apache.commons.math.distribution.AbstractContinuousDistribution
    protected double getDomainUpperBound(double p) {
        if (p < 0.5d) {
            double ret = getDegreesOfFreedom();
            return ret;
        }
        return Double.MAX_VALUE;
    }

    @Override // org.apache.commons.math.distribution.AbstractContinuousDistribution
    protected double getInitialDomain(double p) {
        if (p < 0.5d) {
            double ret = getDegreesOfFreedom() * 0.5d;
            return ret;
        }
        double ret2 = getDegreesOfFreedom();
        return ret2;
    }

    @java.lang.Deprecated
    public void setGamma(org.apache.commons.math.distribution.GammaDistribution g) {
        setGammaInternal(g);
    }

    private void setGammaInternal(org.apache.commons.math.distribution.GammaDistribution g) {
        this.gamma = g;
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
        return getDegreesOfFreedom();
    }

    public double getNumericalVariance() {
        return getDegreesOfFreedom() * 2.0d;
    }
}
