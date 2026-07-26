package org.apache.commons.math.distribution;

/* JADX INFO: loaded from: classes4.dex */
public class WeibullDistributionImpl extends org.apache.commons.math.distribution.AbstractContinuousDistribution implements org.apache.commons.math.distribution.WeibullDistribution, java.io.Serializable {
    public static final double DEFAULT_INVERSE_ABSOLUTE_ACCURACY = 1.0E-9d;
    private static final long serialVersionUID = 8589540077390120676L;
    private double numericalMean;
    private boolean numericalMeanIsCalculated;
    private double numericalVariance;
    private boolean numericalVarianceIsCalculated;
    private double scale;
    private double shape;
    private final double solverAbsoluteAccuracy;

    public WeibullDistributionImpl(double alpha, double beta) {
        this(alpha, beta, 1.0E-9d);
    }

    public WeibullDistributionImpl(double alpha, double beta, double inverseCumAccuracy) {
        this.numericalMean = Double.NaN;
        this.numericalMeanIsCalculated = false;
        this.numericalVariance = Double.NaN;
        this.numericalVarianceIsCalculated = false;
        setShapeInternal(alpha);
        setScaleInternal(beta);
        this.solverAbsoluteAccuracy = inverseCumAccuracy;
    }

    @Override // org.apache.commons.math.distribution.Distribution
    public double cumulativeProbability(double x) {
        if (x <= 0.0d) {
            return 0.0d;
        }
        double ret = this.scale;
        return 1.0d - org.apache.commons.math.util.FastMath.exp(-org.apache.commons.math.util.FastMath.pow(x / ret, this.shape));
    }

    @Override // org.apache.commons.math.distribution.WeibullDistribution
    public double getShape() {
        return this.shape;
    }

    @Override // org.apache.commons.math.distribution.WeibullDistribution
    public double getScale() {
        return this.scale;
    }

    @Override // org.apache.commons.math.distribution.AbstractContinuousDistribution
    public double density(double x) {
        if (x < 0.0d) {
            return 0.0d;
        }
        double xscale = x / this.scale;
        double xscalepow = org.apache.commons.math.util.FastMath.pow(xscale, this.shape - 1.0d);
        double xscalepowshape = xscalepow * xscale;
        return (this.shape / this.scale) * xscalepow * org.apache.commons.math.util.FastMath.exp(-xscalepowshape);
    }

    @Override // org.apache.commons.math.distribution.AbstractContinuousDistribution, org.apache.commons.math.distribution.ContinuousDistribution
    public double inverseCumulativeProbability(double p) {
        if (p < 0.0d || p > 1.0d) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.OUT_OF_RANGE_SIMPLE, java.lang.Double.valueOf(p), java.lang.Double.valueOf(0.0d), java.lang.Double.valueOf(1.0d));
        }
        if (p == 0.0d) {
            return 0.0d;
        }
        if (p == 1.0d) {
            return Double.POSITIVE_INFINITY;
        }
        double ret = this.scale;
        return ret * org.apache.commons.math.util.FastMath.pow(-org.apache.commons.math.util.FastMath.log(1.0d - p), 1.0d / this.shape);
    }

    @Override // org.apache.commons.math.distribution.WeibullDistribution
    @java.lang.Deprecated
    public void setShape(double alpha) {
        setShapeInternal(alpha);
        invalidateParameterDependentMoments();
    }

    private void setShapeInternal(double alpha) {
        if (alpha <= 0.0d) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.NOT_POSITIVE_SHAPE, java.lang.Double.valueOf(alpha));
        }
        this.shape = alpha;
    }

    @Override // org.apache.commons.math.distribution.WeibullDistribution
    @java.lang.Deprecated
    public void setScale(double beta) {
        setScaleInternal(beta);
        invalidateParameterDependentMoments();
    }

    private void setScaleInternal(double beta) {
        if (beta <= 0.0d) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.NOT_POSITIVE_SCALE, java.lang.Double.valueOf(beta));
        }
        this.scale = beta;
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
        return org.apache.commons.math.util.FastMath.pow(this.scale * org.apache.commons.math.util.FastMath.log(2.0d), 1.0d / this.shape);
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

    protected double calculateNumericalMean() {
        double sh = getShape();
        double sc = getScale();
        return org.apache.commons.math.util.FastMath.exp(org.apache.commons.math.special.Gamma.logGamma((1.0d / sh) + 1.0d)) * sc;
    }

    private double calculateNumericalVariance() {
        double sh = getShape();
        double sc = getScale();
        double mn = getNumericalMean();
        return ((sc * sc) * org.apache.commons.math.util.FastMath.exp(org.apache.commons.math.special.Gamma.logGamma((2.0d / sh) + 1.0d))) - (mn * mn);
    }

    public double getNumericalMean() {
        if (!this.numericalMeanIsCalculated) {
            this.numericalMean = calculateNumericalMean();
            this.numericalMeanIsCalculated = true;
        }
        return this.numericalMean;
    }

    public double getNumericalVariance() {
        if (!this.numericalVarianceIsCalculated) {
            this.numericalVariance = calculateNumericalVariance();
            this.numericalVarianceIsCalculated = true;
        }
        return this.numericalVariance;
    }

    private void invalidateParameterDependentMoments() {
        this.numericalMeanIsCalculated = false;
        this.numericalVarianceIsCalculated = false;
    }
}
