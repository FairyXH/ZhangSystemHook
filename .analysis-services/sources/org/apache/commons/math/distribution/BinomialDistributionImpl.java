package org.apache.commons.math.distribution;

/* JADX INFO: loaded from: classes4.dex */
public class BinomialDistributionImpl extends org.apache.commons.math.distribution.AbstractIntegerDistribution implements org.apache.commons.math.distribution.BinomialDistribution, java.io.Serializable {
    private static final long serialVersionUID = 6751309484392813623L;
    private int numberOfTrials;
    private double probabilityOfSuccess;

    public BinomialDistributionImpl(int trials, double p) {
        setNumberOfTrialsInternal(trials);
        setProbabilityOfSuccessInternal(p);
    }

    @Override // org.apache.commons.math.distribution.BinomialDistribution
    public int getNumberOfTrials() {
        return this.numberOfTrials;
    }

    @Override // org.apache.commons.math.distribution.BinomialDistribution
    public double getProbabilityOfSuccess() {
        return this.probabilityOfSuccess;
    }

    @Override // org.apache.commons.math.distribution.BinomialDistribution
    @java.lang.Deprecated
    public void setNumberOfTrials(int trials) {
        setNumberOfTrialsInternal(trials);
    }

    private void setNumberOfTrialsInternal(int trials) {
        if (trials < 0) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.NEGATIVE_NUMBER_OF_TRIALS, java.lang.Integer.valueOf(trials));
        }
        this.numberOfTrials = trials;
    }

    @Override // org.apache.commons.math.distribution.BinomialDistribution
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
        return this.numberOfTrials;
    }

    @Override // org.apache.commons.math.distribution.AbstractIntegerDistribution, org.apache.commons.math.distribution.IntegerDistribution
    public double cumulativeProbability(int x) throws org.apache.commons.math.MathException {
        if (x < 0) {
            return 0.0d;
        }
        if (x >= this.numberOfTrials) {
            return 1.0d;
        }
        double ret = 1.0d - org.apache.commons.math.special.Beta.regularizedBeta(getProbabilityOfSuccess(), ((double) x) + 1.0d, this.numberOfTrials - x);
        return ret;
    }

    @Override // org.apache.commons.math.distribution.IntegerDistribution
    public double probability(int x) {
        if (x < 0 || x > this.numberOfTrials) {
            return 0.0d;
        }
        double ret = org.apache.commons.math.util.FastMath.exp(org.apache.commons.math.distribution.SaddlePointExpansion.logBinomialProbability(x, this.numberOfTrials, this.probabilityOfSuccess, 1.0d - this.probabilityOfSuccess));
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
        return super.inverseCumulativeProbability(p);
    }

    public int getSupportLowerBound() {
        return 0;
    }

    public int getSupportUpperBound() {
        return getNumberOfTrials();
    }

    public double getNumericalMean() {
        return ((double) getNumberOfTrials()) * getProbabilityOfSuccess();
    }

    public double getNumericalVariance() {
        double p = getProbabilityOfSuccess();
        return ((double) getNumberOfTrials()) * p * (1.0d - p);
    }
}
