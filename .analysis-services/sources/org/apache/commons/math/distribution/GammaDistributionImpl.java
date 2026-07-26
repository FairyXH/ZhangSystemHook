package org.apache.commons.math.distribution;

/* JADX INFO: loaded from: classes4.dex */
public class GammaDistributionImpl extends org.apache.commons.math.distribution.AbstractContinuousDistribution implements org.apache.commons.math.distribution.GammaDistribution, java.io.Serializable {
    public static final double DEFAULT_INVERSE_ABSOLUTE_ACCURACY = 1.0E-9d;
    private static final long serialVersionUID = -3239549463135430361L;
    private double alpha;
    private double beta;
    private final double solverAbsoluteAccuracy;

    public GammaDistributionImpl(double alpha, double beta) {
        this(alpha, beta, 1.0E-9d);
    }

    public GammaDistributionImpl(double alpha, double beta, double inverseCumAccuracy) {
        setAlphaInternal(alpha);
        setBetaInternal(beta);
        this.solverAbsoluteAccuracy = inverseCumAccuracy;
    }

    @Override // org.apache.commons.math.distribution.Distribution
    public double cumulativeProbability(double x) throws org.apache.commons.math.MathException {
        if (x <= 0.0d) {
            return 0.0d;
        }
        double ret = this.alpha;
        return org.apache.commons.math.special.Gamma.regularizedGammaP(ret, x / this.beta);
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

    @Override // org.apache.commons.math.distribution.GammaDistribution
    @java.lang.Deprecated
    public void setAlpha(double alpha) {
        setAlphaInternal(alpha);
    }

    private void setAlphaInternal(double newAlpha) {
        if (newAlpha <= 0.0d) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.NOT_POSITIVE_ALPHA, java.lang.Double.valueOf(newAlpha));
        }
        this.alpha = newAlpha;
    }

    @Override // org.apache.commons.math.distribution.GammaDistribution
    public double getAlpha() {
        return this.alpha;
    }

    @Override // org.apache.commons.math.distribution.GammaDistribution
    @java.lang.Deprecated
    public void setBeta(double newBeta) {
        setBetaInternal(newBeta);
    }

    private void setBetaInternal(double newBeta) {
        if (newBeta <= 0.0d) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.NOT_POSITIVE_BETA, java.lang.Double.valueOf(newBeta));
        }
        this.beta = newBeta;
    }

    @Override // org.apache.commons.math.distribution.GammaDistribution
    public double getBeta() {
        return this.beta;
    }

    @Override // org.apache.commons.math.distribution.AbstractContinuousDistribution
    public double density(double x) {
        if (x < 0.0d) {
            return 0.0d;
        }
        return ((org.apache.commons.math.util.FastMath.pow(x / this.beta, this.alpha - 1.0d) / this.beta) * org.apache.commons.math.util.FastMath.exp((-x) / this.beta)) / org.apache.commons.math.util.FastMath.exp(org.apache.commons.math.special.Gamma.logGamma(this.alpha));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math.distribution.GammaDistribution, org.apache.commons.math.distribution.HasDensity
    @java.lang.Deprecated
    public double density(java.lang.Double x) {
        return density(x.doubleValue());
    }

    @Override // org.apache.commons.math.distribution.AbstractContinuousDistribution
    protected double getDomainLowerBound(double p) {
        return Double.MIN_VALUE;
    }

    @Override // org.apache.commons.math.distribution.AbstractContinuousDistribution
    protected double getDomainUpperBound(double p) {
        if (p < 0.5d) {
            double ret = this.alpha * this.beta;
            return ret;
        }
        return Double.MAX_VALUE;
    }

    @Override // org.apache.commons.math.distribution.AbstractContinuousDistribution
    protected double getInitialDomain(double p) {
        if (p < 0.5d) {
            double ret = this.alpha * this.beta * 0.5d;
            return ret;
        }
        double ret2 = this.beta * this.alpha;
        return ret2;
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
        return getAlpha() * getBeta();
    }

    public double getNumericalVariance() {
        double b = getBeta();
        return getAlpha() * b * b;
    }
}
