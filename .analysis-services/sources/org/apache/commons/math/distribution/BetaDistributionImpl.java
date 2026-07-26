package org.apache.commons.math.distribution;

/* JADX INFO: loaded from: classes4.dex */
public class BetaDistributionImpl extends org.apache.commons.math.distribution.AbstractContinuousDistribution implements org.apache.commons.math.distribution.BetaDistribution {
    public static final double DEFAULT_INVERSE_ABSOLUTE_ACCURACY = 1.0E-9d;
    private static final long serialVersionUID = -1221965979403477668L;
    private double alpha;
    private double beta;
    private final double solverAbsoluteAccuracy;
    private double z;

    public BetaDistributionImpl(double alpha, double beta, double inverseCumAccuracy) {
        this.alpha = alpha;
        this.beta = beta;
        this.z = Double.NaN;
        this.solverAbsoluteAccuracy = inverseCumAccuracy;
    }

    public BetaDistributionImpl(double alpha, double beta) {
        this(alpha, beta, 1.0E-9d);
    }

    @Override // org.apache.commons.math.distribution.BetaDistribution
    @java.lang.Deprecated
    public void setAlpha(double alpha) {
        this.alpha = alpha;
        this.z = Double.NaN;
    }

    @Override // org.apache.commons.math.distribution.BetaDistribution
    public double getAlpha() {
        return this.alpha;
    }

    @Override // org.apache.commons.math.distribution.BetaDistribution
    @java.lang.Deprecated
    public void setBeta(double beta) {
        this.beta = beta;
        this.z = Double.NaN;
    }

    @Override // org.apache.commons.math.distribution.BetaDistribution
    public double getBeta() {
        return this.beta;
    }

    private void recomputeZ() {
        if (java.lang.Double.isNaN(this.z)) {
            this.z = (org.apache.commons.math.special.Gamma.logGamma(this.alpha) + org.apache.commons.math.special.Gamma.logGamma(this.beta)) - org.apache.commons.math.special.Gamma.logGamma(this.alpha + this.beta);
        }
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math.distribution.BetaDistribution, org.apache.commons.math.distribution.HasDensity
    @java.lang.Deprecated
    public double density(java.lang.Double x) {
        return density(x.doubleValue());
    }

    @Override // org.apache.commons.math.distribution.AbstractContinuousDistribution
    public double density(double x) {
        recomputeZ();
        if (x < 0.0d || x > 1.0d) {
            return 0.0d;
        }
        if (x == 0.0d) {
            if (this.alpha >= 1.0d) {
                return 0.0d;
            }
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.CANNOT_COMPUTE_BETA_DENSITY_AT_0_FOR_SOME_ALPHA, java.lang.Double.valueOf(this.alpha));
        }
        if (x == 1.0d) {
            if (this.beta >= 1.0d) {
                return 0.0d;
            }
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.CANNOT_COMPUTE_BETA_DENSITY_AT_1_FOR_SOME_BETA, java.lang.Double.valueOf(this.beta));
        }
        double logX = org.apache.commons.math.util.FastMath.log(x);
        double log1mX = org.apache.commons.math.util.FastMath.log1p(-x);
        return org.apache.commons.math.util.FastMath.exp((((this.alpha - 1.0d) * logX) + ((this.beta - 1.0d) * log1mX)) - this.z);
    }

    @Override // org.apache.commons.math.distribution.AbstractContinuousDistribution, org.apache.commons.math.distribution.ContinuousDistribution
    public double inverseCumulativeProbability(double p) throws org.apache.commons.math.MathException {
        if (p == 0.0d) {
            return 0.0d;
        }
        if (p == 1.0d) {
            return 1.0d;
        }
        return super.inverseCumulativeProbability(p);
    }

    @Override // org.apache.commons.math.distribution.AbstractContinuousDistribution
    protected double getInitialDomain(double p) {
        return p;
    }

    @Override // org.apache.commons.math.distribution.AbstractContinuousDistribution
    protected double getDomainLowerBound(double p) {
        return 0.0d;
    }

    @Override // org.apache.commons.math.distribution.AbstractContinuousDistribution
    protected double getDomainUpperBound(double p) {
        return 1.0d;
    }

    @Override // org.apache.commons.math.distribution.Distribution
    public double cumulativeProbability(double x) throws org.apache.commons.math.MathException {
        if (x <= 0.0d) {
            return 0.0d;
        }
        if (x >= 1.0d) {
            return 1.0d;
        }
        return org.apache.commons.math.special.Beta.regularizedBeta(x, this.alpha, this.beta);
    }

    @Override // org.apache.commons.math.distribution.AbstractDistribution, org.apache.commons.math.distribution.Distribution
    public double cumulativeProbability(double x0, double x1) throws org.apache.commons.math.MathException {
        return cumulativeProbability(x1) - cumulativeProbability(x0);
    }

    @Override // org.apache.commons.math.distribution.AbstractContinuousDistribution
    protected double getSolverAbsoluteAccuracy() {
        return this.solverAbsoluteAccuracy;
    }

    public double getSupportLowerBound() {
        return 0.0d;
    }

    public double getSupportUpperBound() {
        return 1.0d;
    }

    public double getNumericalMean() {
        double a = getAlpha();
        return a / (getBeta() + a);
    }

    public double getNumericalVariance() {
        double a = getAlpha();
        double b = getBeta();
        double alphabetasum = a + b;
        return (a * b) / ((alphabetasum * alphabetasum) * (1.0d + alphabetasum));
    }
}
