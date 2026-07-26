package org.apache.commons.math.distribution;

/* JADX INFO: loaded from: classes4.dex */
public class FDistributionImpl extends org.apache.commons.math.distribution.AbstractContinuousDistribution implements org.apache.commons.math.distribution.FDistribution, java.io.Serializable {
    public static final double DEFAULT_INVERSE_ABSOLUTE_ACCURACY = 1.0E-9d;
    private static final long serialVersionUID = -8516354193418641566L;
    private double denominatorDegreesOfFreedom;
    private double numeratorDegreesOfFreedom;
    private final double solverAbsoluteAccuracy;

    public FDistributionImpl(double numeratorDegreesOfFreedom, double denominatorDegreesOfFreedom) {
        this(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom, 1.0E-9d);
    }

    public FDistributionImpl(double numeratorDegreesOfFreedom, double denominatorDegreesOfFreedom, double inverseCumAccuracy) {
        setNumeratorDegreesOfFreedomInternal(numeratorDegreesOfFreedom);
        setDenominatorDegreesOfFreedomInternal(denominatorDegreesOfFreedom);
        this.solverAbsoluteAccuracy = inverseCumAccuracy;
    }

    @Override // org.apache.commons.math.distribution.AbstractContinuousDistribution
    public double density(double x) {
        double nhalf = this.numeratorDegreesOfFreedom / 2.0d;
        double mhalf = this.denominatorDegreesOfFreedom / 2.0d;
        double logx = org.apache.commons.math.util.FastMath.log(x);
        double logn = org.apache.commons.math.util.FastMath.log(this.numeratorDegreesOfFreedom);
        double logm = org.apache.commons.math.util.FastMath.log(this.denominatorDegreesOfFreedom);
        double lognxm = org.apache.commons.math.util.FastMath.log((this.numeratorDegreesOfFreedom * x) + this.denominatorDegreesOfFreedom);
        return org.apache.commons.math.util.FastMath.exp(((((((nhalf * logn) + (nhalf * logx)) - logx) + (mhalf * logm)) - (nhalf * lognxm)) - (mhalf * lognxm)) - org.apache.commons.math.special.Beta.logBeta(nhalf, mhalf));
    }

    @Override // org.apache.commons.math.distribution.Distribution
    public double cumulativeProbability(double x) throws org.apache.commons.math.MathException {
        if (x <= 0.0d) {
            return 0.0d;
        }
        double ret = this.numeratorDegreesOfFreedom;
        double m = this.denominatorDegreesOfFreedom;
        return org.apache.commons.math.special.Beta.regularizedBeta((ret * x) / ((ret * x) + m), ret * 0.5d, m * 0.5d);
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
        return 0.0d;
    }

    @Override // org.apache.commons.math.distribution.AbstractContinuousDistribution
    protected double getDomainUpperBound(double p) {
        return Double.MAX_VALUE;
    }

    @Override // org.apache.commons.math.distribution.AbstractContinuousDistribution
    protected double getInitialDomain(double p) {
        double d = this.denominatorDegreesOfFreedom;
        if (d <= 2.0d) {
            return 1.0d;
        }
        double ret = d / (d - 2.0d);
        return ret;
    }

    @Override // org.apache.commons.math.distribution.FDistribution
    @java.lang.Deprecated
    public void setNumeratorDegreesOfFreedom(double degreesOfFreedom) {
        setNumeratorDegreesOfFreedomInternal(degreesOfFreedom);
    }

    private void setNumeratorDegreesOfFreedomInternal(double degreesOfFreedom) {
        if (degreesOfFreedom <= 0.0d) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.NOT_POSITIVE_DEGREES_OF_FREEDOM, java.lang.Double.valueOf(degreesOfFreedom));
        }
        this.numeratorDegreesOfFreedom = degreesOfFreedom;
    }

    @Override // org.apache.commons.math.distribution.FDistribution
    public double getNumeratorDegreesOfFreedom() {
        return this.numeratorDegreesOfFreedom;
    }

    @Override // org.apache.commons.math.distribution.FDistribution
    @java.lang.Deprecated
    public void setDenominatorDegreesOfFreedom(double degreesOfFreedom) {
        setDenominatorDegreesOfFreedomInternal(degreesOfFreedom);
    }

    private void setDenominatorDegreesOfFreedomInternal(double degreesOfFreedom) {
        if (degreesOfFreedom <= 0.0d) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.NOT_POSITIVE_DEGREES_OF_FREEDOM, java.lang.Double.valueOf(degreesOfFreedom));
        }
        this.denominatorDegreesOfFreedom = degreesOfFreedom;
    }

    @Override // org.apache.commons.math.distribution.FDistribution
    public double getDenominatorDegreesOfFreedom() {
        return this.denominatorDegreesOfFreedom;
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
        double denominatorDF = getDenominatorDegreesOfFreedom();
        if (denominatorDF > 2.0d) {
            return denominatorDF / (denominatorDF - 2.0d);
        }
        return Double.NaN;
    }

    public double getNumericalVariance() {
        double denominatorDF = getDenominatorDegreesOfFreedom();
        if (denominatorDF > 4.0d) {
            double numeratorDF = getNumeratorDegreesOfFreedom();
            double denomDFMinusTwo = denominatorDF - 2.0d;
            return (((denominatorDF * denominatorDF) * 2.0d) * ((numeratorDF + denominatorDF) - 2.0d)) / (((denomDFMinusTwo * denomDFMinusTwo) * numeratorDF) * (denominatorDF - 4.0d));
        }
        return Double.NaN;
    }
}
