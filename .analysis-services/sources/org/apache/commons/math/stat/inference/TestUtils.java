package org.apache.commons.math.stat.inference;

/* JADX INFO: loaded from: classes4.dex */
public class TestUtils {
    private static org.apache.commons.math.stat.inference.TTest tTest = new org.apache.commons.math.stat.inference.TTestImpl();
    private static org.apache.commons.math.stat.inference.ChiSquareTest chiSquareTest = new org.apache.commons.math.stat.inference.ChiSquareTestImpl();
    private static org.apache.commons.math.stat.inference.UnknownDistributionChiSquareTest unknownDistributionChiSquareTest = new org.apache.commons.math.stat.inference.ChiSquareTestImpl();
    private static org.apache.commons.math.stat.inference.OneWayAnova oneWayAnova = new org.apache.commons.math.stat.inference.OneWayAnovaImpl();

    protected TestUtils() {
    }

    @java.lang.Deprecated
    public static void setChiSquareTest(org.apache.commons.math.stat.inference.TTest chiSquareTest2) {
        tTest = chiSquareTest2;
    }

    @java.lang.Deprecated
    public static org.apache.commons.math.stat.inference.TTest getTTest() {
        return tTest;
    }

    @java.lang.Deprecated
    public static void setChiSquareTest(org.apache.commons.math.stat.inference.ChiSquareTest chiSquareTest2) {
        chiSquareTest = chiSquareTest2;
    }

    @java.lang.Deprecated
    public static org.apache.commons.math.stat.inference.ChiSquareTest getChiSquareTest() {
        return chiSquareTest;
    }

    @java.lang.Deprecated
    public static void setUnknownDistributionChiSquareTest(org.apache.commons.math.stat.inference.UnknownDistributionChiSquareTest unknownDistributionChiSquareTest2) {
        unknownDistributionChiSquareTest = unknownDistributionChiSquareTest2;
    }

    @java.lang.Deprecated
    public static org.apache.commons.math.stat.inference.UnknownDistributionChiSquareTest getUnknownDistributionChiSquareTest() {
        return unknownDistributionChiSquareTest;
    }

    @java.lang.Deprecated
    public static void setOneWayAnova(org.apache.commons.math.stat.inference.OneWayAnova oneWayAnova2) {
        oneWayAnova = oneWayAnova2;
    }

    @java.lang.Deprecated
    public static org.apache.commons.math.stat.inference.OneWayAnova getOneWayAnova() {
        return oneWayAnova;
    }

    public static double homoscedasticT(double[] sample1, double[] sample2) throws java.lang.IllegalArgumentException {
        return tTest.homoscedasticT(sample1, sample2);
    }

    public static double homoscedasticT(org.apache.commons.math.stat.descriptive.StatisticalSummary sampleStats1, org.apache.commons.math.stat.descriptive.StatisticalSummary sampleStats2) throws java.lang.IllegalArgumentException {
        return tTest.homoscedasticT(sampleStats1, sampleStats2);
    }

    public static boolean homoscedasticTTest(double[] sample1, double[] sample2, double alpha) throws org.apache.commons.math.MathException, java.lang.IllegalArgumentException {
        return tTest.homoscedasticTTest(sample1, sample2, alpha);
    }

    public static double homoscedasticTTest(double[] sample1, double[] sample2) throws org.apache.commons.math.MathException, java.lang.IllegalArgumentException {
        return tTest.homoscedasticTTest(sample1, sample2);
    }

    public static double homoscedasticTTest(org.apache.commons.math.stat.descriptive.StatisticalSummary sampleStats1, org.apache.commons.math.stat.descriptive.StatisticalSummary sampleStats2) throws org.apache.commons.math.MathException, java.lang.IllegalArgumentException {
        return tTest.homoscedasticTTest(sampleStats1, sampleStats2);
    }

    public static double pairedT(double[] sample1, double[] sample2) throws org.apache.commons.math.MathException, java.lang.IllegalArgumentException {
        return tTest.pairedT(sample1, sample2);
    }

    public static boolean pairedTTest(double[] sample1, double[] sample2, double alpha) throws org.apache.commons.math.MathException, java.lang.IllegalArgumentException {
        return tTest.pairedTTest(sample1, sample2, alpha);
    }

    public static double pairedTTest(double[] sample1, double[] sample2) throws org.apache.commons.math.MathException, java.lang.IllegalArgumentException {
        return tTest.pairedTTest(sample1, sample2);
    }

    public static double t(double mu, double[] observed) throws java.lang.IllegalArgumentException {
        return tTest.t(mu, observed);
    }

    public static double t(double mu, org.apache.commons.math.stat.descriptive.StatisticalSummary sampleStats) throws java.lang.IllegalArgumentException {
        return tTest.t(mu, sampleStats);
    }

    public static double t(double[] sample1, double[] sample2) throws java.lang.IllegalArgumentException {
        return tTest.t(sample1, sample2);
    }

    public static double t(org.apache.commons.math.stat.descriptive.StatisticalSummary sampleStats1, org.apache.commons.math.stat.descriptive.StatisticalSummary sampleStats2) throws java.lang.IllegalArgumentException {
        return tTest.t(sampleStats1, sampleStats2);
    }

    public static boolean tTest(double mu, double[] sample, double alpha) throws org.apache.commons.math.MathException, java.lang.IllegalArgumentException {
        return tTest.tTest(mu, sample, alpha);
    }

    public static double tTest(double mu, double[] sample) throws org.apache.commons.math.MathException, java.lang.IllegalArgumentException {
        return tTest.tTest(mu, sample);
    }

    public static boolean tTest(double mu, org.apache.commons.math.stat.descriptive.StatisticalSummary sampleStats, double alpha) throws org.apache.commons.math.MathException, java.lang.IllegalArgumentException {
        return tTest.tTest(mu, sampleStats, alpha);
    }

    public static double tTest(double mu, org.apache.commons.math.stat.descriptive.StatisticalSummary sampleStats) throws org.apache.commons.math.MathException, java.lang.IllegalArgumentException {
        return tTest.tTest(mu, sampleStats);
    }

    public static boolean tTest(double[] sample1, double[] sample2, double alpha) throws org.apache.commons.math.MathException, java.lang.IllegalArgumentException {
        return tTest.tTest(sample1, sample2, alpha);
    }

    public static double tTest(double[] sample1, double[] sample2) throws org.apache.commons.math.MathException, java.lang.IllegalArgumentException {
        return tTest.tTest(sample1, sample2);
    }

    public static boolean tTest(org.apache.commons.math.stat.descriptive.StatisticalSummary sampleStats1, org.apache.commons.math.stat.descriptive.StatisticalSummary sampleStats2, double alpha) throws org.apache.commons.math.MathException, java.lang.IllegalArgumentException {
        return tTest.tTest(sampleStats1, sampleStats2, alpha);
    }

    public static double tTest(org.apache.commons.math.stat.descriptive.StatisticalSummary sampleStats1, org.apache.commons.math.stat.descriptive.StatisticalSummary sampleStats2) throws org.apache.commons.math.MathException, java.lang.IllegalArgumentException {
        return tTest.tTest(sampleStats1, sampleStats2);
    }

    public static double chiSquare(double[] expected, long[] observed) throws java.lang.IllegalArgumentException {
        return chiSquareTest.chiSquare(expected, observed);
    }

    public static double chiSquare(long[][] counts) throws java.lang.IllegalArgumentException {
        return chiSquareTest.chiSquare(counts);
    }

    public static boolean chiSquareTest(double[] expected, long[] observed, double alpha) throws org.apache.commons.math.MathException, java.lang.IllegalArgumentException {
        return chiSquareTest.chiSquareTest(expected, observed, alpha);
    }

    public static double chiSquareTest(double[] expected, long[] observed) throws org.apache.commons.math.MathException, java.lang.IllegalArgumentException {
        return chiSquareTest.chiSquareTest(expected, observed);
    }

    public static boolean chiSquareTest(long[][] counts, double alpha) throws org.apache.commons.math.MathException, java.lang.IllegalArgumentException {
        return chiSquareTest.chiSquareTest(counts, alpha);
    }

    public static double chiSquareTest(long[][] counts) throws org.apache.commons.math.MathException, java.lang.IllegalArgumentException {
        return chiSquareTest.chiSquareTest(counts);
    }

    public static double chiSquareDataSetsComparison(long[] observed1, long[] observed2) throws java.lang.IllegalArgumentException {
        return unknownDistributionChiSquareTest.chiSquareDataSetsComparison(observed1, observed2);
    }

    public static double chiSquareTestDataSetsComparison(long[] observed1, long[] observed2) throws org.apache.commons.math.MathException, java.lang.IllegalArgumentException {
        return unknownDistributionChiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2);
    }

    public static boolean chiSquareTestDataSetsComparison(long[] observed1, long[] observed2, double alpha) throws org.apache.commons.math.MathException, java.lang.IllegalArgumentException {
        return unknownDistributionChiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2, alpha);
    }

    public static double oneWayAnovaFValue(java.util.Collection<double[]> categoryData) throws org.apache.commons.math.MathException, java.lang.IllegalArgumentException {
        return oneWayAnova.anovaFValue(categoryData);
    }

    public static double oneWayAnovaPValue(java.util.Collection<double[]> categoryData) throws org.apache.commons.math.MathException, java.lang.IllegalArgumentException {
        return oneWayAnova.anovaPValue(categoryData);
    }

    public static boolean oneWayAnovaTest(java.util.Collection<double[]> categoryData, double alpha) throws org.apache.commons.math.MathException, java.lang.IllegalArgumentException {
        return oneWayAnova.anovaTest(categoryData, alpha);
    }
}
