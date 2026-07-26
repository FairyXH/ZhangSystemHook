package org.apache.commons.math.distribution;

/* JADX INFO: loaded from: classes4.dex */
public class PoissonDistributionImpl extends org.apache.commons.math.distribution.AbstractIntegerDistribution implements org.apache.commons.math.distribution.PoissonDistribution, java.io.Serializable {
    public static final double DEFAULT_EPSILON = 1.0E-12d;
    public static final int DEFAULT_MAX_ITERATIONS = 10000000;
    private static final long serialVersionUID = -3349935121172596109L;
    private double epsilon;
    private int maxIterations;
    private double mean;
    private org.apache.commons.math.distribution.NormalDistribution normal;

    public PoissonDistributionImpl(double p) {
        this(p, new org.apache.commons.math.distribution.NormalDistributionImpl());
    }

    public PoissonDistributionImpl(double p, double epsilon, int maxIterations) {
        this.maxIterations = DEFAULT_MAX_ITERATIONS;
        this.epsilon = 1.0E-12d;
        setMean(p);
        this.epsilon = epsilon;
        this.maxIterations = maxIterations;
    }

    public PoissonDistributionImpl(double p, double epsilon) {
        this.maxIterations = DEFAULT_MAX_ITERATIONS;
        this.epsilon = 1.0E-12d;
        setMean(p);
        this.epsilon = epsilon;
    }

    public PoissonDistributionImpl(double p, int maxIterations) {
        this.maxIterations = DEFAULT_MAX_ITERATIONS;
        this.epsilon = 1.0E-12d;
        setMean(p);
        this.maxIterations = maxIterations;
    }

    @java.lang.Deprecated
    public PoissonDistributionImpl(double p, org.apache.commons.math.distribution.NormalDistribution z) {
        this.maxIterations = DEFAULT_MAX_ITERATIONS;
        this.epsilon = 1.0E-12d;
        setNormalAndMeanInternal(z, p);
    }

    @Override // org.apache.commons.math.distribution.PoissonDistribution
    public double getMean() {
        return this.mean;
    }

    @Override // org.apache.commons.math.distribution.PoissonDistribution
    @java.lang.Deprecated
    public void setMean(double p) {
        setNormalAndMeanInternal(this.normal, p);
    }

    private void setNormalAndMeanInternal(org.apache.commons.math.distribution.NormalDistribution z, double p) {
        if (p <= 0.0d) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.NOT_POSITIVE_POISSON_MEAN, java.lang.Double.valueOf(p));
        }
        this.mean = p;
        this.normal = z;
        this.normal.setMean(p);
        this.normal.setStandardDeviation(org.apache.commons.math.util.FastMath.sqrt(p));
    }

    @Override // org.apache.commons.math.distribution.IntegerDistribution
    public double probability(int x) {
        if (x < 0 || x == Integer.MAX_VALUE) {
            return 0.0d;
        }
        if (x == 0) {
            double ret = org.apache.commons.math.util.FastMath.exp(-this.mean);
            return ret;
        }
        double ret2 = x;
        return org.apache.commons.math.util.FastMath.exp((-org.apache.commons.math.distribution.SaddlePointExpansion.getStirlingError(ret2)) - org.apache.commons.math.distribution.SaddlePointExpansion.getDeviancePart(x, this.mean)) / org.apache.commons.math.util.FastMath.sqrt(((double) x) * 6.283185307179586d);
    }

    @Override // org.apache.commons.math.distribution.AbstractIntegerDistribution, org.apache.commons.math.distribution.IntegerDistribution
    public double cumulativeProbability(int x) throws org.apache.commons.math.MathException {
        if (x < 0) {
            return 0.0d;
        }
        if (x == Integer.MAX_VALUE) {
            return 1.0d;
        }
        return org.apache.commons.math.special.Gamma.regularizedGammaQ(((double) x) + 1.0d, this.mean, this.epsilon, this.maxIterations);
    }

    @Override // org.apache.commons.math.distribution.PoissonDistribution
    public double normalApproximateProbability(int x) throws org.apache.commons.math.MathException {
        return this.normal.cumulativeProbability(((double) x) + 0.5d);
    }

    @Override // org.apache.commons.math.distribution.AbstractIntegerDistribution
    public int sample() throws org.apache.commons.math.MathException {
        return (int) org.apache.commons.math.util.FastMath.min(this.randomData.nextPoisson(this.mean), 2147483647L);
    }

    @Override // org.apache.commons.math.distribution.AbstractIntegerDistribution
    protected int getDomainLowerBound(double p) {
        return 0;
    }

    @Override // org.apache.commons.math.distribution.AbstractIntegerDistribution
    protected int getDomainUpperBound(double p) {
        return Integer.MAX_VALUE;
    }

    @java.lang.Deprecated
    public void setNormal(org.apache.commons.math.distribution.NormalDistribution value) {
        setNormalAndMeanInternal(value, this.mean);
    }

    public int getSupportLowerBound() {
        return 0;
    }

    public int getSupportUpperBound() {
        return Integer.MAX_VALUE;
    }

    public double getNumericalVariance() {
        return getMean();
    }
}
