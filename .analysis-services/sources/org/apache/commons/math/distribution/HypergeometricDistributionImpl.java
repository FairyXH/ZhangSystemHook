package org.apache.commons.math.distribution;

/* JADX INFO: loaded from: classes4.dex */
public class HypergeometricDistributionImpl extends org.apache.commons.math.distribution.AbstractIntegerDistribution implements org.apache.commons.math.distribution.HypergeometricDistribution, java.io.Serializable {
    private static final long serialVersionUID = -436928820673516179L;
    private int numberOfSuccesses;
    private int populationSize;
    private int sampleSize;

    public HypergeometricDistributionImpl(int populationSize, int numberOfSuccesses, int sampleSize) {
        if (numberOfSuccesses > populationSize) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.NUMBER_OF_SUCCESS_LARGER_THAN_POPULATION_SIZE, java.lang.Integer.valueOf(numberOfSuccesses), java.lang.Integer.valueOf(populationSize));
        }
        if (sampleSize > populationSize) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.SAMPLE_SIZE_LARGER_THAN_POPULATION_SIZE, java.lang.Integer.valueOf(sampleSize), java.lang.Integer.valueOf(populationSize));
        }
        setPopulationSizeInternal(populationSize);
        setSampleSizeInternal(sampleSize);
        setNumberOfSuccessesInternal(numberOfSuccesses);
    }

    @Override // org.apache.commons.math.distribution.AbstractIntegerDistribution, org.apache.commons.math.distribution.IntegerDistribution
    public double cumulativeProbability(int x) {
        int[] domain = getDomain(this.populationSize, this.numberOfSuccesses, this.sampleSize);
        if (x < domain[0]) {
            return 0.0d;
        }
        if (x >= domain[1]) {
            return 1.0d;
        }
        double ret = innerCumulativeProbability(domain[0], x, 1, this.populationSize, this.numberOfSuccesses, this.sampleSize);
        return ret;
    }

    private int[] getDomain(int n, int m, int k) {
        return new int[]{getLowerDomain(n, m, k), getUpperDomain(m, k)};
    }

    @Override // org.apache.commons.math.distribution.AbstractIntegerDistribution
    protected int getDomainLowerBound(double p) {
        return getLowerDomain(this.populationSize, this.numberOfSuccesses, this.sampleSize);
    }

    @Override // org.apache.commons.math.distribution.AbstractIntegerDistribution
    protected int getDomainUpperBound(double p) {
        return getUpperDomain(this.sampleSize, this.numberOfSuccesses);
    }

    private int getLowerDomain(int n, int m, int k) {
        return org.apache.commons.math.util.FastMath.max(0, m - (n - k));
    }

    @Override // org.apache.commons.math.distribution.HypergeometricDistribution
    public int getNumberOfSuccesses() {
        return this.numberOfSuccesses;
    }

    @Override // org.apache.commons.math.distribution.HypergeometricDistribution
    public int getPopulationSize() {
        return this.populationSize;
    }

    @Override // org.apache.commons.math.distribution.HypergeometricDistribution
    public int getSampleSize() {
        return this.sampleSize;
    }

    private int getUpperDomain(int m, int k) {
        return org.apache.commons.math.util.FastMath.min(k, m);
    }

    @Override // org.apache.commons.math.distribution.IntegerDistribution
    public double probability(int x) {
        int[] domain = getDomain(this.populationSize, this.numberOfSuccesses, this.sampleSize);
        if (x >= domain[0] && x <= domain[1]) {
            double p = ((double) this.sampleSize) / ((double) this.populationSize);
            double q = ((double) (this.populationSize - this.sampleSize)) / ((double) this.populationSize);
            double p1 = org.apache.commons.math.distribution.SaddlePointExpansion.logBinomialProbability(x, this.numberOfSuccesses, p, q);
            double p2 = org.apache.commons.math.distribution.SaddlePointExpansion.logBinomialProbability(this.sampleSize - x, this.populationSize - this.numberOfSuccesses, p, q);
            double p3 = org.apache.commons.math.distribution.SaddlePointExpansion.logBinomialProbability(this.sampleSize, this.populationSize, p, q);
            double ret = org.apache.commons.math.util.FastMath.exp((p1 + p2) - p3);
            return ret;
        }
        return 0.0d;
    }

    private double probability(int n, int m, int k, int x) {
        return org.apache.commons.math.util.FastMath.exp((org.apache.commons.math.util.MathUtils.binomialCoefficientLog(m, x) + org.apache.commons.math.util.MathUtils.binomialCoefficientLog(n - m, k - x)) - org.apache.commons.math.util.MathUtils.binomialCoefficientLog(n, k));
    }

    @Override // org.apache.commons.math.distribution.HypergeometricDistribution
    @java.lang.Deprecated
    public void setNumberOfSuccesses(int num) {
        setNumberOfSuccessesInternal(num);
    }

    private void setNumberOfSuccessesInternal(int num) {
        if (num < 0) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.NEGATIVE_NUMBER_OF_SUCCESSES, java.lang.Integer.valueOf(num));
        }
        this.numberOfSuccesses = num;
    }

    @Override // org.apache.commons.math.distribution.HypergeometricDistribution
    @java.lang.Deprecated
    public void setPopulationSize(int size) {
        setPopulationSizeInternal(size);
    }

    private void setPopulationSizeInternal(int size) {
        if (size <= 0) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.NOT_POSITIVE_POPULATION_SIZE, java.lang.Integer.valueOf(size));
        }
        this.populationSize = size;
    }

    @Override // org.apache.commons.math.distribution.HypergeometricDistribution
    @java.lang.Deprecated
    public void setSampleSize(int size) {
        setSampleSizeInternal(size);
    }

    private void setSampleSizeInternal(int size) {
        if (size < 0) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.NOT_POSITIVE_SAMPLE_SIZE, java.lang.Integer.valueOf(size));
        }
        this.sampleSize = size;
    }

    public double upperCumulativeProbability(int x) {
        int[] domain = getDomain(this.populationSize, this.numberOfSuccesses, this.sampleSize);
        if (x < domain[0]) {
            return 1.0d;
        }
        if (x > domain[1]) {
            return 0.0d;
        }
        double ret = innerCumulativeProbability(domain[1], x, -1, this.populationSize, this.numberOfSuccesses, this.sampleSize);
        return ret;
    }

    private double innerCumulativeProbability(int x0, int x1, int dx, int n, int m, int k) {
        double ret = probability(n, m, k, x0);
        while (x0 != x1) {
            x0 += dx;
            ret += probability(n, m, k, x0);
        }
        return ret;
    }

    public int getSupportLowerBound() {
        return org.apache.commons.math.util.FastMath.max(0, (getSampleSize() + getNumberOfSuccesses()) - getPopulationSize());
    }

    public int getSupportUpperBound() {
        return org.apache.commons.math.util.FastMath.min(getNumberOfSuccesses(), getSampleSize());
    }

    protected double getNumericalMean() {
        return ((double) (getSampleSize() * getNumberOfSuccesses())) / ((double) getPopulationSize());
    }

    public double getNumericalVariance() {
        double N = getPopulationSize();
        double m = getNumberOfSuccesses();
        double n = getSampleSize();
        return (((n * m) * (N - n)) * (N - m)) / ((N * N) * (N - 1.0d));
    }
}
