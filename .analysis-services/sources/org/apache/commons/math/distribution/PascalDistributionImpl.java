package org.apache.commons.math.distribution;

/* JADX INFO: loaded from: classes4.dex */
public class PascalDistributionImpl extends org.apache.commons.math.distribution.AbstractIntegerDistribution implements org.apache.commons.math.distribution.PascalDistribution, java.io.Serializable {
    private static final long serialVersionUID = 6751309484392813623L;
    private int numberOfSuccesses;
    private double probabilityOfSuccess;

    public PascalDistributionImpl(int r, double p) {
        setNumberOfSuccessesInternal(r);
        setProbabilityOfSuccessInternal(p);
    }

    @Override // org.apache.commons.math.distribution.PascalDistribution
    public int getNumberOfSuccesses() {
        return this.numberOfSuccesses;
    }

    @Override // org.apache.commons.math.distribution.PascalDistribution
    public double getProbabilityOfSuccess() {
        return this.probabilityOfSuccess;
    }

    @Override // org.apache.commons.math.distribution.PascalDistribution
    @java.lang.Deprecated
    public void setNumberOfSuccesses(int successes) {
        setNumberOfSuccessesInternal(successes);
    }

    private void setNumberOfSuccessesInternal(int successes) {
        if (successes < 0) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.NEGATIVE_NUMBER_OF_SUCCESSES, java.lang.Integer.valueOf(successes));
        }
        this.numberOfSuccesses = successes;
    }

    @Override // org.apache.commons.math.distribution.PascalDistribution
    @java.lang.Deprecated
    public void setProbabilityOfSuccess(double p) {
        setProbabilityOfSuccessInternal(p);
    }

    private void setProbabilityOfSuccessInternal(double p) {
        if (p < 0.0d || p > 1.0d) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.OUT_OF_RANGE_SIMPLE, java.lang.Double.valueOf(p), java.lang.Double.valueOf(0.0d), java.lang.Double.valueOf(1.0d));
        }
        this.probabilityOfSuccess = p;
    }

    @Override // org.apache.commons.math.distribution.AbstractIntegerDistribution
    protected int getDomainLowerBound(double p) {
        return -1;
    }

    @Override // org.apache.commons.math.distribution.AbstractIntegerDistribution
    protected int getDomainUpperBound(double p) {
        return 2147483646;
    }

    @Override // org.apache.commons.math.distribution.AbstractIntegerDistribution, org.apache.commons.math.distribution.IntegerDistribution
    public double cumulativeProbability(int x) throws org.apache.commons.math.MathException {
        if (x < 0) {
            return 0.0d;
        }
        double ret = org.apache.commons.math.special.Beta.regularizedBeta(this.probabilityOfSuccess, this.numberOfSuccesses, x + 1);
        return ret;
    }

    @Override // org.apache.commons.math.distribution.IntegerDistribution
    public double probability(int x) {
        if (x < 0) {
            return 0.0d;
        }
        double ret = org.apache.commons.math.util.MathUtils.binomialCoefficientDouble((this.numberOfSuccesses + x) - 1, this.numberOfSuccesses - 1) * org.apache.commons.math.util.FastMath.pow(this.probabilityOfSuccess, this.numberOfSuccesses) * org.apache.commons.math.util.FastMath.pow(1.0d - this.probabilityOfSuccess, x);
        return ret;
    }

    @Override // org.apache.commons.math.distribution.AbstractIntegerDistribution, org.apache.commons.math.distribution.IntegerDistribution
    public int inverseCumulativeProbability(double p) throws org.apache.commons.math.MathException {
        if (p == 0.0d) {
            return -1;
        }
        if (p == 1.0d) {
            return Integer.MAX_VALUE;
        }
        int ret = super.inverseCumulativeProbability(p);
        return ret;
    }

    public int getSupportLowerBound() {
        return 0;
    }

    public int getSupportUpperBound() {
        return Integer.MAX_VALUE;
    }

    public double getNumericalMean() {
        double p = getProbabilityOfSuccess();
        double r = getNumberOfSuccesses();
        return (r * p) / (1.0d - p);
    }

    public double getNumericalVariance() {
        double p = getProbabilityOfSuccess();
        double r = getNumberOfSuccesses();
        double pInv = 1.0d - p;
        return (r * p) / (pInv * pInv);
    }
}
