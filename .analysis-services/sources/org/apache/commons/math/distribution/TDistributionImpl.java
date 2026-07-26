package org.apache.commons.math.distribution;

/* JADX INFO: loaded from: classes4.dex */
public class TDistributionImpl extends org.apache.commons.math.distribution.AbstractContinuousDistribution implements org.apache.commons.math.distribution.TDistribution, java.io.Serializable {
    public static final double DEFAULT_INVERSE_ABSOLUTE_ACCURACY = 1.0E-9d;
    private static final long serialVersionUID = -5852615386664158222L;
    private double degreesOfFreedom;
    private final double solverAbsoluteAccuracy;

    public TDistributionImpl(double degreesOfFreedom, double inverseCumAccuracy) {
        setDegreesOfFreedomInternal(degreesOfFreedom);
        this.solverAbsoluteAccuracy = inverseCumAccuracy;
    }

    public TDistributionImpl(double degreesOfFreedom) {
        this(degreesOfFreedom, 1.0E-9d);
    }

    @Override // org.apache.commons.math.distribution.TDistribution
    @java.lang.Deprecated
    public void setDegreesOfFreedom(double degreesOfFreedom) {
        setDegreesOfFreedomInternal(degreesOfFreedom);
    }

    private void setDegreesOfFreedomInternal(double newDegreesOfFreedom) {
        if (newDegreesOfFreedom <= 0.0d) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.NOT_POSITIVE_DEGREES_OF_FREEDOM, java.lang.Double.valueOf(newDegreesOfFreedom));
        }
        this.degreesOfFreedom = newDegreesOfFreedom;
    }

    @Override // org.apache.commons.math.distribution.TDistribution
    public double getDegreesOfFreedom() {
        return this.degreesOfFreedom;
    }

    @Override // org.apache.commons.math.distribution.AbstractContinuousDistribution
    public double density(double x) {
        double n = this.degreesOfFreedom;
        double nPlus1Over2 = (n + 1.0d) / 2.0d;
        return org.apache.commons.math.util.FastMath.exp(((org.apache.commons.math.special.Gamma.logGamma(nPlus1Over2) - ((org.apache.commons.math.util.FastMath.log(3.141592653589793d) + org.apache.commons.math.util.FastMath.log(n)) * 0.5d)) - org.apache.commons.math.special.Gamma.logGamma(n / 2.0d)) - (org.apache.commons.math.util.FastMath.log(((x * x) / n) + 1.0d) * nPlus1Over2));
    }

    @Override // org.apache.commons.math.distribution.Distribution
    public double cumulativeProbability(double x) throws org.apache.commons.math.MathException {
        if (x == 0.0d) {
            return 0.5d;
        }
        double t = org.apache.commons.math.special.Beta.regularizedBeta(this.degreesOfFreedom / (this.degreesOfFreedom + (x * x)), this.degreesOfFreedom * 0.5d, 0.5d);
        if (x < 0.0d) {
            double ret = t * 0.5d;
            return ret;
        }
        double ret2 = 1.0d - (0.5d * t);
        return ret2;
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
    protected double getDomainLowerBound(double p) {
        return -1.7976931348623157E308d;
    }

    @Override // org.apache.commons.math.distribution.AbstractContinuousDistribution
    protected double getDomainUpperBound(double p) {
        return Double.MAX_VALUE;
    }

    @Override // org.apache.commons.math.distribution.AbstractContinuousDistribution
    protected double getInitialDomain(double p) {
        return 0.0d;
    }

    @Override // org.apache.commons.math.distribution.AbstractContinuousDistribution
    protected double getSolverAbsoluteAccuracy() {
        return this.solverAbsoluteAccuracy;
    }

    public double getSupportLowerBound() {
        return Double.NEGATIVE_INFINITY;
    }

    public double getSupportUpperBound() {
        return Double.POSITIVE_INFINITY;
    }

    public double getNumericalMean() {
        double df = getDegreesOfFreedom();
        if (df > 1.0d) {
            return 0.0d;
        }
        return Double.NaN;
    }

    public double getNumericalVariance() {
        double df = getDegreesOfFreedom();
        if (df > 2.0d) {
            return df / (df - 2.0d);
        }
        if (df > 1.0d && df <= 2.0d) {
            return Double.POSITIVE_INFINITY;
        }
        return Double.NaN;
    }
}
