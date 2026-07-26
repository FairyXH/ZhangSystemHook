package org.apache.commons.math.stat.inference;

/* JADX INFO: loaded from: classes4.dex */
public class TTestImpl implements org.apache.commons.math.stat.inference.TTest {

    @java.lang.Deprecated
    private org.apache.commons.math.distribution.TDistribution distribution;

    public TTestImpl() {
        this(new org.apache.commons.math.distribution.TDistributionImpl(1.0d));
    }

    @java.lang.Deprecated
    public TTestImpl(org.apache.commons.math.distribution.TDistribution t) {
        setDistribution(t);
    }

    @Override // org.apache.commons.math.stat.inference.TTest
    public double pairedT(double[] sample1, double[] sample2) throws org.apache.commons.math.MathException, java.lang.IllegalArgumentException {
        checkSampleData(sample1);
        checkSampleData(sample2);
        double meanDifference = org.apache.commons.math.stat.StatUtils.meanDifference(sample1, sample2);
        return t(meanDifference, 0.0d, org.apache.commons.math.stat.StatUtils.varianceDifference(sample1, sample2, meanDifference), sample1.length);
    }

    @Override // org.apache.commons.math.stat.inference.TTest
    public double pairedTTest(double[] sample1, double[] sample2) throws org.apache.commons.math.MathException, java.lang.IllegalArgumentException {
        double meanDifference = org.apache.commons.math.stat.StatUtils.meanDifference(sample1, sample2);
        return tTest(meanDifference, 0.0d, org.apache.commons.math.stat.StatUtils.varianceDifference(sample1, sample2, meanDifference), sample1.length);
    }

    @Override // org.apache.commons.math.stat.inference.TTest
    public boolean pairedTTest(double[] sample1, double[] sample2, double alpha) throws org.apache.commons.math.MathException, java.lang.IllegalArgumentException {
        checkSignificanceLevel(alpha);
        return pairedTTest(sample1, sample2) < alpha;
    }

    @Override // org.apache.commons.math.stat.inference.TTest
    public double t(double mu, double[] observed) throws java.lang.IllegalArgumentException {
        checkSampleData(observed);
        return t(org.apache.commons.math.stat.StatUtils.mean(observed), mu, org.apache.commons.math.stat.StatUtils.variance(observed), observed.length);
    }

    @Override // org.apache.commons.math.stat.inference.TTest
    public double t(double mu, org.apache.commons.math.stat.descriptive.StatisticalSummary sampleStats) throws java.lang.IllegalArgumentException {
        checkSampleData(sampleStats);
        return t(sampleStats.getMean(), mu, sampleStats.getVariance(), sampleStats.getN());
    }

    @Override // org.apache.commons.math.stat.inference.TTest
    public double homoscedasticT(double[] sample1, double[] sample2) throws java.lang.IllegalArgumentException {
        checkSampleData(sample1);
        checkSampleData(sample2);
        return homoscedasticT(org.apache.commons.math.stat.StatUtils.mean(sample1), org.apache.commons.math.stat.StatUtils.mean(sample2), org.apache.commons.math.stat.StatUtils.variance(sample1), org.apache.commons.math.stat.StatUtils.variance(sample2), sample1.length, sample2.length);
    }

    @Override // org.apache.commons.math.stat.inference.TTest
    public double t(double[] sample1, double[] sample2) throws java.lang.IllegalArgumentException {
        checkSampleData(sample1);
        checkSampleData(sample2);
        return t(org.apache.commons.math.stat.StatUtils.mean(sample1), org.apache.commons.math.stat.StatUtils.mean(sample2), org.apache.commons.math.stat.StatUtils.variance(sample1), org.apache.commons.math.stat.StatUtils.variance(sample2), sample1.length, sample2.length);
    }

    @Override // org.apache.commons.math.stat.inference.TTest
    public double t(org.apache.commons.math.stat.descriptive.StatisticalSummary sampleStats1, org.apache.commons.math.stat.descriptive.StatisticalSummary sampleStats2) throws java.lang.IllegalArgumentException {
        checkSampleData(sampleStats1);
        checkSampleData(sampleStats2);
        return t(sampleStats1.getMean(), sampleStats2.getMean(), sampleStats1.getVariance(), sampleStats2.getVariance(), sampleStats1.getN(), sampleStats2.getN());
    }

    @Override // org.apache.commons.math.stat.inference.TTest
    public double homoscedasticT(org.apache.commons.math.stat.descriptive.StatisticalSummary sampleStats1, org.apache.commons.math.stat.descriptive.StatisticalSummary sampleStats2) throws java.lang.IllegalArgumentException {
        checkSampleData(sampleStats1);
        checkSampleData(sampleStats2);
        return homoscedasticT(sampleStats1.getMean(), sampleStats2.getMean(), sampleStats1.getVariance(), sampleStats2.getVariance(), sampleStats1.getN(), sampleStats2.getN());
    }

    @Override // org.apache.commons.math.stat.inference.TTest
    public double tTest(double mu, double[] sample) throws org.apache.commons.math.MathException, java.lang.IllegalArgumentException {
        checkSampleData(sample);
        return tTest(org.apache.commons.math.stat.StatUtils.mean(sample), mu, org.apache.commons.math.stat.StatUtils.variance(sample), sample.length);
    }

    @Override // org.apache.commons.math.stat.inference.TTest
    public boolean tTest(double mu, double[] sample, double alpha) throws org.apache.commons.math.MathException, java.lang.IllegalArgumentException {
        checkSignificanceLevel(alpha);
        return tTest(mu, sample) < alpha;
    }

    @Override // org.apache.commons.math.stat.inference.TTest
    public double tTest(double mu, org.apache.commons.math.stat.descriptive.StatisticalSummary sampleStats) throws org.apache.commons.math.MathException, java.lang.IllegalArgumentException {
        checkSampleData(sampleStats);
        return tTest(sampleStats.getMean(), mu, sampleStats.getVariance(), sampleStats.getN());
    }

    @Override // org.apache.commons.math.stat.inference.TTest
    public boolean tTest(double mu, org.apache.commons.math.stat.descriptive.StatisticalSummary sampleStats, double alpha) throws org.apache.commons.math.MathException, java.lang.IllegalArgumentException {
        checkSignificanceLevel(alpha);
        return tTest(mu, sampleStats) < alpha;
    }

    @Override // org.apache.commons.math.stat.inference.TTest
    public double tTest(double[] sample1, double[] sample2) throws org.apache.commons.math.MathException, java.lang.IllegalArgumentException {
        checkSampleData(sample1);
        checkSampleData(sample2);
        return tTest(org.apache.commons.math.stat.StatUtils.mean(sample1), org.apache.commons.math.stat.StatUtils.mean(sample2), org.apache.commons.math.stat.StatUtils.variance(sample1), org.apache.commons.math.stat.StatUtils.variance(sample2), sample1.length, sample2.length);
    }

    @Override // org.apache.commons.math.stat.inference.TTest
    public double homoscedasticTTest(double[] sample1, double[] sample2) throws org.apache.commons.math.MathException, java.lang.IllegalArgumentException {
        checkSampleData(sample1);
        checkSampleData(sample2);
        return homoscedasticTTest(org.apache.commons.math.stat.StatUtils.mean(sample1), org.apache.commons.math.stat.StatUtils.mean(sample2), org.apache.commons.math.stat.StatUtils.variance(sample1), org.apache.commons.math.stat.StatUtils.variance(sample2), sample1.length, sample2.length);
    }

    @Override // org.apache.commons.math.stat.inference.TTest
    public boolean tTest(double[] sample1, double[] sample2, double alpha) throws org.apache.commons.math.MathException, java.lang.IllegalArgumentException {
        checkSignificanceLevel(alpha);
        return tTest(sample1, sample2) < alpha;
    }

    @Override // org.apache.commons.math.stat.inference.TTest
    public boolean homoscedasticTTest(double[] sample1, double[] sample2, double alpha) throws org.apache.commons.math.MathException, java.lang.IllegalArgumentException {
        checkSignificanceLevel(alpha);
        return homoscedasticTTest(sample1, sample2) < alpha;
    }

    @Override // org.apache.commons.math.stat.inference.TTest
    public double tTest(org.apache.commons.math.stat.descriptive.StatisticalSummary sampleStats1, org.apache.commons.math.stat.descriptive.StatisticalSummary sampleStats2) throws org.apache.commons.math.MathException, java.lang.IllegalArgumentException {
        checkSampleData(sampleStats1);
        checkSampleData(sampleStats2);
        return tTest(sampleStats1.getMean(), sampleStats2.getMean(), sampleStats1.getVariance(), sampleStats2.getVariance(), sampleStats1.getN(), sampleStats2.getN());
    }

    @Override // org.apache.commons.math.stat.inference.TTest
    public double homoscedasticTTest(org.apache.commons.math.stat.descriptive.StatisticalSummary sampleStats1, org.apache.commons.math.stat.descriptive.StatisticalSummary sampleStats2) throws org.apache.commons.math.MathException, java.lang.IllegalArgumentException {
        checkSampleData(sampleStats1);
        checkSampleData(sampleStats2);
        return homoscedasticTTest(sampleStats1.getMean(), sampleStats2.getMean(), sampleStats1.getVariance(), sampleStats2.getVariance(), sampleStats1.getN(), sampleStats2.getN());
    }

    @Override // org.apache.commons.math.stat.inference.TTest
    public boolean tTest(org.apache.commons.math.stat.descriptive.StatisticalSummary sampleStats1, org.apache.commons.math.stat.descriptive.StatisticalSummary sampleStats2, double alpha) throws org.apache.commons.math.MathException, java.lang.IllegalArgumentException {
        checkSignificanceLevel(alpha);
        return tTest(sampleStats1, sampleStats2) < alpha;
    }

    protected double df(double v1, double v2, double n1, double n2) {
        return (((v1 / n1) + (v2 / n2)) * ((v1 / n1) + (v2 / n2))) / (((v1 * v1) / ((n1 * n1) * (n1 - 1.0d))) + ((v2 * v2) / ((n2 * n2) * (n2 - 1.0d))));
    }

    protected double t(double m, double mu, double v, double n) {
        return (m - mu) / org.apache.commons.math.util.FastMath.sqrt(v / n);
    }

    protected double t(double m1, double m2, double v1, double v2, double n1, double n2) {
        return (m1 - m2) / org.apache.commons.math.util.FastMath.sqrt((v1 / n1) + (v2 / n2));
    }

    protected double homoscedasticT(double m1, double m2, double v1, double v2, double n1, double n2) {
        double pooledVariance = (((n1 - 1.0d) * v1) + ((n2 - 1.0d) * v2)) / ((n1 + n2) - 2.0d);
        return (m1 - m2) / org.apache.commons.math.util.FastMath.sqrt(((1.0d / n1) + (1.0d / n2)) * pooledVariance);
    }

    protected double tTest(double m, double mu, double v, double n) throws org.apache.commons.math.MathException {
        double t = org.apache.commons.math.util.FastMath.abs(t(m, mu, v, n));
        this.distribution.setDegreesOfFreedom(n - 1.0d);
        return this.distribution.cumulativeProbability(-t) * 2.0d;
    }

    protected double tTest(double m1, double m2, double v1, double v2, double n1, double n2) throws org.apache.commons.math.MathException {
        double t = org.apache.commons.math.util.FastMath.abs(t(m1, m2, v1, v2, n1, n2));
        double degreesOfFreedom = df(v1, v2, n1, n2);
        this.distribution.setDegreesOfFreedom(degreesOfFreedom);
        return this.distribution.cumulativeProbability(-t) * 2.0d;
    }

    protected double homoscedasticTTest(double m1, double m2, double v1, double v2, double n1, double n2) throws org.apache.commons.math.MathException {
        double t = org.apache.commons.math.util.FastMath.abs(homoscedasticT(m1, m2, v1, v2, n1, n2));
        double degreesOfFreedom = (n1 + n2) - 2.0d;
        this.distribution.setDegreesOfFreedom(degreesOfFreedom);
        return this.distribution.cumulativeProbability(-t) * 2.0d;
    }

    @java.lang.Deprecated
    public void setDistribution(org.apache.commons.math.distribution.TDistribution value) {
        this.distribution = value;
    }

    private void checkSignificanceLevel(double alpha) throws java.lang.IllegalArgumentException {
        if (alpha <= 0.0d || alpha > 0.5d) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.OUT_OF_BOUND_SIGNIFICANCE_LEVEL, java.lang.Double.valueOf(alpha), java.lang.Double.valueOf(0.0d), java.lang.Double.valueOf(0.5d));
        }
    }

    private void checkSampleData(double[] data) throws java.lang.IllegalArgumentException {
        if (data == null || data.length < 2) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.INSUFFICIENT_DATA_FOR_T_STATISTIC, java.lang.Integer.valueOf(data == null ? 0 : data.length));
        }
    }

    private void checkSampleData(org.apache.commons.math.stat.descriptive.StatisticalSummary stat) throws java.lang.IllegalArgumentException {
        if (stat == null || stat.getN() < 2) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.INSUFFICIENT_DATA_FOR_T_STATISTIC, java.lang.Long.valueOf(stat == null ? 0L : stat.getN()));
        }
    }
}
