package org.apache.commons.math.stat;

/* JADX INFO: loaded from: classes4.dex */
public final class StatUtils {
    private static final org.apache.commons.math.stat.descriptive.UnivariateStatistic SUM = new org.apache.commons.math.stat.descriptive.summary.Sum();
    private static final org.apache.commons.math.stat.descriptive.UnivariateStatistic SUM_OF_SQUARES = new org.apache.commons.math.stat.descriptive.summary.SumOfSquares();
    private static final org.apache.commons.math.stat.descriptive.UnivariateStatistic PRODUCT = new org.apache.commons.math.stat.descriptive.summary.Product();
    private static final org.apache.commons.math.stat.descriptive.UnivariateStatistic SUM_OF_LOGS = new org.apache.commons.math.stat.descriptive.summary.SumOfLogs();
    private static final org.apache.commons.math.stat.descriptive.UnivariateStatistic MIN = new org.apache.commons.math.stat.descriptive.rank.Min();
    private static final org.apache.commons.math.stat.descriptive.UnivariateStatistic MAX = new org.apache.commons.math.stat.descriptive.rank.Max();
    private static final org.apache.commons.math.stat.descriptive.UnivariateStatistic MEAN = new org.apache.commons.math.stat.descriptive.moment.Mean();
    private static final org.apache.commons.math.stat.descriptive.moment.Variance VARIANCE = new org.apache.commons.math.stat.descriptive.moment.Variance();
    private static final org.apache.commons.math.stat.descriptive.rank.Percentile PERCENTILE = new org.apache.commons.math.stat.descriptive.rank.Percentile();
    private static final org.apache.commons.math.stat.descriptive.moment.GeometricMean GEOMETRIC_MEAN = new org.apache.commons.math.stat.descriptive.moment.GeometricMean();

    private StatUtils() {
    }

    public static double sum(double[] values) {
        return SUM.evaluate(values);
    }

    public static double sum(double[] values, int begin, int length) {
        return SUM.evaluate(values, begin, length);
    }

    public static double sumSq(double[] values) {
        return SUM_OF_SQUARES.evaluate(values);
    }

    public static double sumSq(double[] values, int begin, int length) {
        return SUM_OF_SQUARES.evaluate(values, begin, length);
    }

    public static double product(double[] values) {
        return PRODUCT.evaluate(values);
    }

    public static double product(double[] values, int begin, int length) {
        return PRODUCT.evaluate(values, begin, length);
    }

    public static double sumLog(double[] values) {
        return SUM_OF_LOGS.evaluate(values);
    }

    public static double sumLog(double[] values, int begin, int length) {
        return SUM_OF_LOGS.evaluate(values, begin, length);
    }

    public static double mean(double[] values) {
        return MEAN.evaluate(values);
    }

    public static double mean(double[] values, int begin, int length) {
        return MEAN.evaluate(values, begin, length);
    }

    public static double geometricMean(double[] values) {
        return GEOMETRIC_MEAN.evaluate(values);
    }

    public static double geometricMean(double[] values, int begin, int length) {
        return GEOMETRIC_MEAN.evaluate(values, begin, length);
    }

    public static double variance(double[] values) {
        return VARIANCE.evaluate(values);
    }

    public static double variance(double[] values, int begin, int length) {
        return VARIANCE.evaluate(values, begin, length);
    }

    public static double variance(double[] values, double mean, int begin, int length) {
        return VARIANCE.evaluate(values, mean, begin, length);
    }

    public static double variance(double[] values, double mean) {
        return VARIANCE.evaluate(values, mean);
    }

    public static double max(double[] values) {
        return MAX.evaluate(values);
    }

    public static double max(double[] values, int begin, int length) {
        return MAX.evaluate(values, begin, length);
    }

    public static double min(double[] values) {
        return MIN.evaluate(values);
    }

    public static double min(double[] values, int begin, int length) {
        return MIN.evaluate(values, begin, length);
    }

    public static double percentile(double[] values, double p) {
        return PERCENTILE.evaluate(values, p);
    }

    public static double percentile(double[] values, int begin, int length, double p) {
        return PERCENTILE.evaluate(values, begin, length, p);
    }

    public static double sumDifference(double[] sample1, double[] sample2) throws java.lang.IllegalArgumentException {
        int n = sample1.length;
        if (n != sample2.length) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.DIMENSIONS_MISMATCH_SIMPLE, java.lang.Integer.valueOf(n), java.lang.Integer.valueOf(sample2.length));
        }
        if (n < 1) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.INSUFFICIENT_DIMENSION, java.lang.Integer.valueOf(sample2.length), 1);
        }
        double result = 0.0d;
        for (int i = 0; i < n; i++) {
            result += sample1[i] - sample2[i];
        }
        return result;
    }

    public static double meanDifference(double[] sample1, double[] sample2) throws java.lang.IllegalArgumentException {
        return sumDifference(sample1, sample2) / ((double) sample1.length);
    }

    public static double varianceDifference(double[] sample1, double[] sample2, double meanDifference) throws java.lang.IllegalArgumentException {
        double sum1 = 0.0d;
        double sum2 = 0.0d;
        int n = sample1.length;
        if (n != sample2.length) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.DIMENSIONS_MISMATCH_SIMPLE, java.lang.Integer.valueOf(n), java.lang.Integer.valueOf(sample2.length));
        }
        if (n < 2) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.INSUFFICIENT_DIMENSION, java.lang.Integer.valueOf(n), 2);
        }
        for (int i = 0; i < n; i++) {
            double diff = sample1[i] - sample2[i];
            sum1 += (diff - meanDifference) * (diff - meanDifference);
            sum2 += diff - meanDifference;
        }
        return (sum1 - ((sum2 * sum2) / ((double) n))) / ((double) (n - 1));
    }

    public static double[] normalize(double[] sample) {
        org.apache.commons.math.stat.descriptive.DescriptiveStatistics stats = new org.apache.commons.math.stat.descriptive.DescriptiveStatistics();
        for (double d : sample) {
            stats.addValue(d);
        }
        double mean = stats.getMean();
        double standardDeviation = stats.getStandardDeviation();
        double[] standardizedSample = new double[sample.length];
        for (int i = 0; i < sample.length; i++) {
            standardizedSample[i] = (sample[i] - mean) / standardDeviation;
        }
        return standardizedSample;
    }
}
