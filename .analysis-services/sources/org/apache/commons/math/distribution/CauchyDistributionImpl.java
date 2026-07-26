package org.apache.commons.math.distribution;

/* JADX INFO: loaded from: classes4.dex */
public class CauchyDistributionImpl extends org.apache.commons.math.distribution.AbstractContinuousDistribution implements org.apache.commons.math.distribution.CauchyDistribution, java.io.Serializable {
    public static final double DEFAULT_INVERSE_ABSOLUTE_ACCURACY = 1.0E-9d;
    private static final long serialVersionUID = 8589540077390120676L;
    private double median;
    private double scale;
    private final double solverAbsoluteAccuracy;

    public CauchyDistributionImpl() {
        this(0.0d, 1.0d);
    }

    public CauchyDistributionImpl(double median, double s) {
        this(median, s, 1.0E-9d);
    }

    public CauchyDistributionImpl(double median, double s, double inverseCumAccuracy) {
        this.median = 0.0d;
        this.scale = 1.0d;
        setMedianInternal(median);
        setScaleInternal(s);
        this.solverAbsoluteAccuracy = inverseCumAccuracy;
    }

    @Override // org.apache.commons.math.distribution.Distribution
    public double cumulativeProbability(double x) {
        return (org.apache.commons.math.util.FastMath.atan((x - this.median) / this.scale) / 3.141592653589793d) + 0.5d;
    }

    @Override // org.apache.commons.math.distribution.CauchyDistribution
    public double getMedian() {
        return this.median;
    }

    @Override // org.apache.commons.math.distribution.CauchyDistribution
    public double getScale() {
        return this.scale;
    }

    @Override // org.apache.commons.math.distribution.AbstractContinuousDistribution
    public double density(double x) {
        double dev = x - this.median;
        return (this.scale / ((dev * dev) + (this.scale * this.scale))) * 0.3183098861837907d;
    }

    @Override // org.apache.commons.math.distribution.AbstractContinuousDistribution, org.apache.commons.math.distribution.ContinuousDistribution
    public double inverseCumulativeProbability(double p) {
        if (p < 0.0d || p > 1.0d) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.OUT_OF_RANGE_SIMPLE, java.lang.Double.valueOf(p), java.lang.Double.valueOf(0.0d), java.lang.Double.valueOf(1.0d));
        }
        if (p == 0.0d) {
            return Double.NEGATIVE_INFINITY;
        }
        if (p == 1.0d) {
            return Double.POSITIVE_INFINITY;
        }
        double ret = this.median;
        return ret + (this.scale * org.apache.commons.math.util.FastMath.tan((p - 0.5d) * 3.141592653589793d));
    }

    @Override // org.apache.commons.math.distribution.CauchyDistribution
    @java.lang.Deprecated
    public void setMedian(double median) {
        setMedianInternal(median);
    }

    private void setMedianInternal(double newMedian) {
        this.median = newMedian;
    }

    @Override // org.apache.commons.math.distribution.CauchyDistribution
    @java.lang.Deprecated
    public void setScale(double s) {
        setScaleInternal(s);
    }

    private void setScaleInternal(double s) {
        if (s <= 0.0d) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.NOT_POSITIVE_SCALE, java.lang.Double.valueOf(s));
        }
        this.scale = s;
    }

    @Override // org.apache.commons.math.distribution.AbstractContinuousDistribution
    protected double getDomainLowerBound(double p) {
        if (p < 0.5d) {
            return -1.7976931348623157E308d;
        }
        double ret = this.median;
        return ret;
    }

    @Override // org.apache.commons.math.distribution.AbstractContinuousDistribution
    protected double getDomainUpperBound(double p) {
        if (p < 0.5d) {
            double ret = this.median;
            return ret;
        }
        return Double.MAX_VALUE;
    }

    @Override // org.apache.commons.math.distribution.AbstractContinuousDistribution
    protected double getInitialDomain(double p) {
        if (p < 0.5d) {
            double ret = this.median - this.scale;
            return ret;
        }
        if (p > 0.5d) {
            double ret2 = this.median + this.scale;
            return ret2;
        }
        double ret3 = this.median;
        return ret3;
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
        return Double.NaN;
    }

    public double getNumericalVariance() {
        return Double.NaN;
    }
}
