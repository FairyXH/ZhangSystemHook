package org.apache.commons.math.stat.inference;

/* JADX INFO: loaded from: classes4.dex */
public class OneWayAnovaImpl implements org.apache.commons.math.stat.inference.OneWayAnova {
    @Override // org.apache.commons.math.stat.inference.OneWayAnova
    public double anovaFValue(java.util.Collection<double[]> categoryData) throws org.apache.commons.math.MathException, java.lang.IllegalArgumentException {
        org.apache.commons.math.stat.inference.OneWayAnovaImpl.AnovaStats a = anovaStats(categoryData);
        return a.F;
    }

    @Override // org.apache.commons.math.stat.inference.OneWayAnova
    public double anovaPValue(java.util.Collection<double[]> categoryData) throws org.apache.commons.math.MathException, java.lang.IllegalArgumentException {
        org.apache.commons.math.stat.inference.OneWayAnovaImpl.AnovaStats a = anovaStats(categoryData);
        org.apache.commons.math.distribution.FDistribution fdist = new org.apache.commons.math.distribution.FDistributionImpl(a.dfbg, a.dfwg);
        return 1.0d - fdist.cumulativeProbability(a.F);
    }

    @Override // org.apache.commons.math.stat.inference.OneWayAnova
    public boolean anovaTest(java.util.Collection<double[]> categoryData, double alpha) throws org.apache.commons.math.MathException, java.lang.IllegalArgumentException {
        if (alpha <= 0.0d || alpha > 0.5d) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.OUT_OF_BOUND_SIGNIFICANCE_LEVEL, java.lang.Double.valueOf(alpha), 0, java.lang.Double.valueOf(0.5d));
        }
        return anovaPValue(categoryData) < alpha;
    }

    private org.apache.commons.math.stat.inference.OneWayAnovaImpl.AnovaStats anovaStats(java.util.Collection<double[]> categoryData) throws org.apache.commons.math.MathException, java.lang.IllegalArgumentException {
        if (categoryData.size() < 2) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.TWO_OR_MORE_CATEGORIES_REQUIRED, java.lang.Integer.valueOf(categoryData.size()));
        }
        for (double[] array : categoryData) {
            if (array.length <= 1) {
                throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.TWO_OR_MORE_VALUES_IN_CATEGORY_REQUIRED, java.lang.Integer.valueOf(array.length));
            }
        }
        int dfwg = 0;
        double sswg = 0.0d;
        org.apache.commons.math.stat.descriptive.summary.Sum totsum = new org.apache.commons.math.stat.descriptive.summary.Sum();
        org.apache.commons.math.stat.descriptive.summary.SumOfSquares totsumsq = new org.apache.commons.math.stat.descriptive.summary.SumOfSquares();
        int totnum = 0;
        for (double[] data : categoryData) {
            org.apache.commons.math.stat.descriptive.summary.Sum sum = new org.apache.commons.math.stat.descriptive.summary.Sum();
            org.apache.commons.math.stat.descriptive.summary.SumOfSquares sumsq = new org.apache.commons.math.stat.descriptive.summary.SumOfSquares();
            int num = 0;
            for (double val : data) {
                num++;
                sum.increment(val);
                sumsq.increment(val);
                totnum++;
                totsum.increment(val);
                totsumsq.increment(val);
            }
            int i = num - 1;
            dfwg += i;
            double ss = sumsq.getResult() - ((sum.getResult() * sum.getResult()) / ((double) num));
            sswg += ss;
        }
        double sswg2 = sswg;
        double sst = totsumsq.getResult() - ((totsum.getResult() * totsum.getResult()) / ((double) totnum));
        double ssbg = sst - sswg2;
        int dfbg = categoryData.size() - 1;
        double msbg = ssbg / ((double) dfbg);
        double mswg = sswg2 / ((double) dfwg);
        double F = msbg / mswg;
        return new org.apache.commons.math.stat.inference.OneWayAnovaImpl.AnovaStats(dfbg, dfwg, F);
    }

    private static class AnovaStats {
        private double F;
        private int dfbg;
        private int dfwg;

        private AnovaStats(int dfbg, int dfwg, double F) {
            this.dfbg = dfbg;
            this.dfwg = dfwg;
            this.F = F;
        }
    }
}
