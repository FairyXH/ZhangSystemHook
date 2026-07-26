package org.apache.commons.math.stat.inference;

/* JADX INFO: loaded from: classes4.dex */
public class ChiSquareTestImpl implements org.apache.commons.math.stat.inference.UnknownDistributionChiSquareTest {
    private org.apache.commons.math.distribution.ChiSquaredDistribution distribution;

    public ChiSquareTestImpl() {
        this(new org.apache.commons.math.distribution.ChiSquaredDistributionImpl(1.0d));
    }

    public ChiSquareTestImpl(org.apache.commons.math.distribution.ChiSquaredDistribution x) {
        setDistribution(x);
    }

    @Override // org.apache.commons.math.stat.inference.ChiSquareTest
    public double chiSquare(double[] expected, long[] observed) throws java.lang.IllegalArgumentException {
        double d;
        double d2;
        if (expected.length < 2) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.INSUFFICIENT_DIMENSION, java.lang.Integer.valueOf(expected.length), 2);
        }
        if (expected.length != observed.length) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.DIMENSIONS_MISMATCH_SIMPLE, java.lang.Integer.valueOf(expected.length), java.lang.Integer.valueOf(observed.length));
        }
        checkPositive(expected);
        checkNonNegative(observed);
        double sumExpected = 0.0d;
        double sumObserved = 0.0d;
        for (int i = 0; i < observed.length; i++) {
            sumExpected += expected[i];
            sumObserved += observed[i];
        }
        double ratio = 1.0d;
        boolean rescale = false;
        if (org.apache.commons.math.util.FastMath.abs(sumExpected - sumObserved) > 1.0E-5d) {
            ratio = sumObserved / sumExpected;
            rescale = true;
        }
        double sumSq = 0.0d;
        for (int i2 = 0; i2 < observed.length; i2++) {
            if (rescale) {
                double dev = observed[i2] - (expected[i2] * ratio);
                d = dev * dev;
                d2 = expected[i2] * ratio;
            } else {
                double dev2 = observed[i2] - expected[i2];
                d = dev2 * dev2;
                d2 = expected[i2];
            }
            sumSq += d / d2;
        }
        return sumSq;
    }

    @Override // org.apache.commons.math.stat.inference.ChiSquareTest
    public double chiSquareTest(double[] expected, long[] observed) throws org.apache.commons.math.MathException, java.lang.IllegalArgumentException {
        this.distribution.setDegreesOfFreedom(((double) expected.length) - 1.0d);
        return 1.0d - this.distribution.cumulativeProbability(chiSquare(expected, observed));
    }

    @Override // org.apache.commons.math.stat.inference.ChiSquareTest
    public boolean chiSquareTest(double[] expected, long[] observed, double alpha) throws org.apache.commons.math.MathException, java.lang.IllegalArgumentException {
        if (alpha <= 0.0d || alpha > 0.5d) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.OUT_OF_BOUND_SIGNIFICANCE_LEVEL, java.lang.Double.valueOf(alpha), 0, java.lang.Double.valueOf(0.5d));
        }
        return chiSquareTest(expected, observed) < alpha;
    }

    @Override // org.apache.commons.math.stat.inference.ChiSquareTest
    public double chiSquare(long[][] counts) throws java.lang.IllegalArgumentException {
        long[][] jArr = counts;
        checkArray(counts);
        int nRows = jArr.length;
        int nCols = jArr[0].length;
        double[] rowSum = new double[nRows];
        double[] colSum = new double[nCols];
        double total = 0.0d;
        for (int row = 0; row < nRows; row++) {
            for (int col = 0; col < nCols; col++) {
                rowSum[row] = rowSum[row] + jArr[row][col];
                colSum[col] = colSum[col] + jArr[row][col];
                total += jArr[row][col];
            }
        }
        double sumSq = 0.0d;
        int row2 = 0;
        while (row2 < nRows) {
            int col2 = 0;
            while (col2 < nCols) {
                double expected = (rowSum[row2] * colSum[col2]) / total;
                sumSq += ((jArr[row2][col2] - expected) * (jArr[row2][col2] - expected)) / expected;
                col2++;
                jArr = counts;
                nRows = nRows;
            }
            row2++;
            jArr = counts;
        }
        return sumSq;
    }

    @Override // org.apache.commons.math.stat.inference.ChiSquareTest
    public double chiSquareTest(long[][] counts) throws org.apache.commons.math.MathException, java.lang.IllegalArgumentException {
        checkArray(counts);
        double df = (((double) counts.length) - 1.0d) * (((double) counts[0].length) - 1.0d);
        this.distribution.setDegreesOfFreedom(df);
        return 1.0d - this.distribution.cumulativeProbability(chiSquare(counts));
    }

    @Override // org.apache.commons.math.stat.inference.ChiSquareTest
    public boolean chiSquareTest(long[][] counts, double alpha) throws org.apache.commons.math.MathException, java.lang.IllegalArgumentException {
        if (alpha <= 0.0d || alpha > 0.5d) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.OUT_OF_BOUND_SIGNIFICANCE_LEVEL, java.lang.Double.valueOf(alpha), java.lang.Double.valueOf(0.0d), java.lang.Double.valueOf(0.5d));
        }
        return chiSquareTest(counts) < alpha;
    }

    @Override // org.apache.commons.math.stat.inference.UnknownDistributionChiSquareTest
    public double chiSquareDataSetsComparison(long[] observed1, long[] observed2) throws java.lang.IllegalArgumentException {
        double dev;
        if (observed1.length < 2) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.INSUFFICIENT_DIMENSION, java.lang.Integer.valueOf(observed1.length), 2);
        }
        if (observed1.length != observed2.length) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.DIMENSIONS_MISMATCH_SIMPLE, java.lang.Integer.valueOf(observed1.length), java.lang.Integer.valueOf(observed2.length));
        }
        checkNonNegative(observed1);
        checkNonNegative(observed2);
        long countSum1 = 0;
        long countSum2 = 0;
        double weight = 0.0d;
        for (int i = 0; i < observed1.length; i++) {
            countSum1 += observed1[i];
            countSum2 += observed2[i];
        }
        if (countSum1 == 0) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.OBSERVED_COUNTS_ALL_ZERO, 1);
        }
        if (countSum2 == 0) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.OBSERVED_COUNTS_ALL_ZERO, 2);
        }
        boolean unequalCounts = countSum1 != countSum2;
        if (unequalCounts) {
            weight = org.apache.commons.math.util.FastMath.sqrt(countSum1 / countSum2);
        }
        double sumSq = 0.0d;
        int i2 = 0;
        while (i2 < observed1.length) {
            if (observed1[i2] == 0 && observed2[i2] == 0) {
                throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.OBSERVED_COUNTS_BOTTH_ZERO_FOR_ENTRY, java.lang.Integer.valueOf(i2));
            }
            long countSum12 = countSum1;
            long countSum13 = observed1[i2];
            double obs1 = countSum13;
            long countSum22 = countSum2;
            long countSum23 = observed2[i2];
            double obs2 = countSum23;
            if (unequalCounts) {
                dev = (obs1 / weight) - (obs2 * weight);
            } else {
                dev = obs1 - obs2;
            }
            sumSq += (dev * dev) / (obs1 + obs2);
            i2++;
            countSum1 = countSum12;
            countSum2 = countSum22;
        }
        return sumSq;
    }

    @Override // org.apache.commons.math.stat.inference.UnknownDistributionChiSquareTest
    public double chiSquareTestDataSetsComparison(long[] observed1, long[] observed2) throws org.apache.commons.math.MathException, java.lang.IllegalArgumentException {
        this.distribution.setDegreesOfFreedom(((double) observed1.length) - 1.0d);
        return 1.0d - this.distribution.cumulativeProbability(chiSquareDataSetsComparison(observed1, observed2));
    }

    @Override // org.apache.commons.math.stat.inference.UnknownDistributionChiSquareTest
    public boolean chiSquareTestDataSetsComparison(long[] observed1, long[] observed2, double alpha) throws org.apache.commons.math.MathException, java.lang.IllegalArgumentException {
        if (alpha <= 0.0d || alpha > 0.5d) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.OUT_OF_BOUND_SIGNIFICANCE_LEVEL, java.lang.Double.valueOf(alpha), java.lang.Double.valueOf(0.0d), java.lang.Double.valueOf(0.5d));
        }
        return chiSquareTestDataSetsComparison(observed1, observed2) < alpha;
    }

    private void checkArray(long[][] in) throws java.lang.IllegalArgumentException {
        if (in.length < 2) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.INSUFFICIENT_DIMENSION, java.lang.Integer.valueOf(in.length), 2);
        }
        if (in[0].length < 2) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.INSUFFICIENT_DIMENSION, java.lang.Integer.valueOf(in[0].length), 2);
        }
        checkRectangular(in);
        checkNonNegative(in);
    }

    private void checkRectangular(long[][] in) {
        for (int i = 1; i < in.length; i++) {
            if (in[i].length != in[0].length) {
                throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.DIFFERENT_ROWS_LENGTHS, java.lang.Integer.valueOf(in[i].length), java.lang.Integer.valueOf(in[0].length));
            }
        }
    }

    private void checkPositive(double[] in) throws java.lang.IllegalArgumentException {
        for (int i = 0; i < in.length; i++) {
            if (in[i] <= 0.0d) {
                throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.NOT_POSITIVE_ELEMENT_AT_INDEX, java.lang.Integer.valueOf(i), java.lang.Double.valueOf(in[i]));
            }
        }
    }

    private void checkNonNegative(long[] in) throws java.lang.IllegalArgumentException {
        for (int i = 0; i < in.length; i++) {
            if (in[i] < 0) {
                throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.NEGATIVE_ELEMENT_AT_INDEX, java.lang.Integer.valueOf(i), java.lang.Long.valueOf(in[i]));
            }
        }
    }

    private void checkNonNegative(long[][] in) throws java.lang.IllegalArgumentException {
        for (int i = 0; i < in.length; i++) {
            for (int j = 0; j < in[i].length; j++) {
                if (in[i][j] < 0) {
                    throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.NEGATIVE_ELEMENT_AT_2D_INDEX, java.lang.Integer.valueOf(i), java.lang.Integer.valueOf(j), java.lang.Long.valueOf(in[i][j]));
                }
            }
        }
    }

    public void setDistribution(org.apache.commons.math.distribution.ChiSquaredDistribution value) {
        this.distribution = value;
    }
}
