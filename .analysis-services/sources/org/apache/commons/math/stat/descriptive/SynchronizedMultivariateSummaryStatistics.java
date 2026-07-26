package org.apache.commons.math.stat.descriptive;

/* JADX INFO: loaded from: classes4.dex */
public class SynchronizedMultivariateSummaryStatistics extends org.apache.commons.math.stat.descriptive.MultivariateSummaryStatistics {
    private static final long serialVersionUID = 7099834153347155363L;

    public SynchronizedMultivariateSummaryStatistics(int k, boolean isCovarianceBiasCorrected) {
        super(k, isCovarianceBiasCorrected);
    }

    @Override // org.apache.commons.math.stat.descriptive.MultivariateSummaryStatistics
    public synchronized void addValue(double[] value) throws org.apache.commons.math.DimensionMismatchException {
        super.addValue(value);
    }

    @Override // org.apache.commons.math.stat.descriptive.MultivariateSummaryStatistics, org.apache.commons.math.stat.descriptive.StatisticalMultivariateSummary
    public synchronized int getDimension() {
        return super.getDimension();
    }

    @Override // org.apache.commons.math.stat.descriptive.MultivariateSummaryStatistics, org.apache.commons.math.stat.descriptive.StatisticalMultivariateSummary
    public synchronized long getN() {
        return super.getN();
    }

    @Override // org.apache.commons.math.stat.descriptive.MultivariateSummaryStatistics, org.apache.commons.math.stat.descriptive.StatisticalMultivariateSummary
    public synchronized double[] getSum() {
        return super.getSum();
    }

    @Override // org.apache.commons.math.stat.descriptive.MultivariateSummaryStatistics, org.apache.commons.math.stat.descriptive.StatisticalMultivariateSummary
    public synchronized double[] getSumSq() {
        return super.getSumSq();
    }

    @Override // org.apache.commons.math.stat.descriptive.MultivariateSummaryStatistics, org.apache.commons.math.stat.descriptive.StatisticalMultivariateSummary
    public synchronized double[] getSumLog() {
        return super.getSumLog();
    }

    @Override // org.apache.commons.math.stat.descriptive.MultivariateSummaryStatistics, org.apache.commons.math.stat.descriptive.StatisticalMultivariateSummary
    public synchronized double[] getMean() {
        return super.getMean();
    }

    @Override // org.apache.commons.math.stat.descriptive.MultivariateSummaryStatistics, org.apache.commons.math.stat.descriptive.StatisticalMultivariateSummary
    public synchronized double[] getStandardDeviation() {
        return super.getStandardDeviation();
    }

    @Override // org.apache.commons.math.stat.descriptive.MultivariateSummaryStatistics, org.apache.commons.math.stat.descriptive.StatisticalMultivariateSummary
    public synchronized org.apache.commons.math.linear.RealMatrix getCovariance() {
        return super.getCovariance();
    }

    @Override // org.apache.commons.math.stat.descriptive.MultivariateSummaryStatistics, org.apache.commons.math.stat.descriptive.StatisticalMultivariateSummary
    public synchronized double[] getMax() {
        return super.getMax();
    }

    @Override // org.apache.commons.math.stat.descriptive.MultivariateSummaryStatistics, org.apache.commons.math.stat.descriptive.StatisticalMultivariateSummary
    public synchronized double[] getMin() {
        return super.getMin();
    }

    @Override // org.apache.commons.math.stat.descriptive.MultivariateSummaryStatistics, org.apache.commons.math.stat.descriptive.StatisticalMultivariateSummary
    public synchronized double[] getGeometricMean() {
        return super.getGeometricMean();
    }

    @Override // org.apache.commons.math.stat.descriptive.MultivariateSummaryStatistics
    public synchronized java.lang.String toString() {
        return super.toString();
    }

    @Override // org.apache.commons.math.stat.descriptive.MultivariateSummaryStatistics
    public synchronized void clear() {
        super.clear();
    }

    @Override // org.apache.commons.math.stat.descriptive.MultivariateSummaryStatistics
    public synchronized boolean equals(java.lang.Object object) {
        return super.equals(object);
    }

    @Override // org.apache.commons.math.stat.descriptive.MultivariateSummaryStatistics
    public synchronized int hashCode() {
        return super.hashCode();
    }

    @Override // org.apache.commons.math.stat.descriptive.MultivariateSummaryStatistics
    public synchronized org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic[] getSumImpl() {
        return super.getSumImpl();
    }

    @Override // org.apache.commons.math.stat.descriptive.MultivariateSummaryStatistics
    public synchronized void setSumImpl(org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic[] sumImpl) throws org.apache.commons.math.DimensionMismatchException {
        super.setSumImpl(sumImpl);
    }

    @Override // org.apache.commons.math.stat.descriptive.MultivariateSummaryStatistics
    public synchronized org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic[] getSumsqImpl() {
        return super.getSumsqImpl();
    }

    @Override // org.apache.commons.math.stat.descriptive.MultivariateSummaryStatistics
    public synchronized void setSumsqImpl(org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic[] sumsqImpl) throws org.apache.commons.math.DimensionMismatchException {
        super.setSumsqImpl(sumsqImpl);
    }

    @Override // org.apache.commons.math.stat.descriptive.MultivariateSummaryStatistics
    public synchronized org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic[] getMinImpl() {
        return super.getMinImpl();
    }

    @Override // org.apache.commons.math.stat.descriptive.MultivariateSummaryStatistics
    public synchronized void setMinImpl(org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic[] minImpl) throws org.apache.commons.math.DimensionMismatchException {
        super.setMinImpl(minImpl);
    }

    @Override // org.apache.commons.math.stat.descriptive.MultivariateSummaryStatistics
    public synchronized org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic[] getMaxImpl() {
        return super.getMaxImpl();
    }

    @Override // org.apache.commons.math.stat.descriptive.MultivariateSummaryStatistics
    public synchronized void setMaxImpl(org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic[] maxImpl) throws org.apache.commons.math.DimensionMismatchException {
        super.setMaxImpl(maxImpl);
    }

    @Override // org.apache.commons.math.stat.descriptive.MultivariateSummaryStatistics
    public synchronized org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic[] getSumLogImpl() {
        return super.getSumLogImpl();
    }

    @Override // org.apache.commons.math.stat.descriptive.MultivariateSummaryStatistics
    public synchronized void setSumLogImpl(org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic[] sumLogImpl) throws org.apache.commons.math.DimensionMismatchException {
        super.setSumLogImpl(sumLogImpl);
    }

    @Override // org.apache.commons.math.stat.descriptive.MultivariateSummaryStatistics
    public synchronized org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic[] getGeoMeanImpl() {
        return super.getGeoMeanImpl();
    }

    @Override // org.apache.commons.math.stat.descriptive.MultivariateSummaryStatistics
    public synchronized void setGeoMeanImpl(org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic[] geoMeanImpl) throws org.apache.commons.math.DimensionMismatchException {
        super.setGeoMeanImpl(geoMeanImpl);
    }

    @Override // org.apache.commons.math.stat.descriptive.MultivariateSummaryStatistics
    public synchronized org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic[] getMeanImpl() {
        return super.getMeanImpl();
    }

    @Override // org.apache.commons.math.stat.descriptive.MultivariateSummaryStatistics
    public synchronized void setMeanImpl(org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic[] meanImpl) throws org.apache.commons.math.DimensionMismatchException {
        super.setMeanImpl(meanImpl);
    }
}
