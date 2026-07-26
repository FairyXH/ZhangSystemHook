package org.apache.commons.math.stat.descriptive;

/* JADX INFO: loaded from: classes4.dex */
public class SynchronizedSummaryStatistics extends org.apache.commons.math.stat.descriptive.SummaryStatistics {
    private static final long serialVersionUID = 1909861009042253704L;

    public SynchronizedSummaryStatistics() {
    }

    public SynchronizedSummaryStatistics(org.apache.commons.math.stat.descriptive.SynchronizedSummaryStatistics original) {
        copy(original, this);
    }

    @Override // org.apache.commons.math.stat.descriptive.SummaryStatistics
    public synchronized org.apache.commons.math.stat.descriptive.StatisticalSummary getSummary() {
        return super.getSummary();
    }

    @Override // org.apache.commons.math.stat.descriptive.SummaryStatistics
    public synchronized void addValue(double value) {
        super.addValue(value);
    }

    @Override // org.apache.commons.math.stat.descriptive.SummaryStatistics, org.apache.commons.math.stat.descriptive.StatisticalSummary
    public synchronized long getN() {
        return super.getN();
    }

    @Override // org.apache.commons.math.stat.descriptive.SummaryStatistics, org.apache.commons.math.stat.descriptive.StatisticalSummary
    public synchronized double getSum() {
        return super.getSum();
    }

    @Override // org.apache.commons.math.stat.descriptive.SummaryStatistics
    public synchronized double getSumsq() {
        return super.getSumsq();
    }

    @Override // org.apache.commons.math.stat.descriptive.SummaryStatistics, org.apache.commons.math.stat.descriptive.StatisticalSummary
    public synchronized double getMean() {
        return super.getMean();
    }

    @Override // org.apache.commons.math.stat.descriptive.SummaryStatistics, org.apache.commons.math.stat.descriptive.StatisticalSummary
    public synchronized double getStandardDeviation() {
        return super.getStandardDeviation();
    }

    @Override // org.apache.commons.math.stat.descriptive.SummaryStatistics, org.apache.commons.math.stat.descriptive.StatisticalSummary
    public synchronized double getVariance() {
        return super.getVariance();
    }

    @Override // org.apache.commons.math.stat.descriptive.SummaryStatistics, org.apache.commons.math.stat.descriptive.StatisticalSummary
    public synchronized double getMax() {
        return super.getMax();
    }

    @Override // org.apache.commons.math.stat.descriptive.SummaryStatistics, org.apache.commons.math.stat.descriptive.StatisticalSummary
    public synchronized double getMin() {
        return super.getMin();
    }

    @Override // org.apache.commons.math.stat.descriptive.SummaryStatistics
    public synchronized double getGeometricMean() {
        return super.getGeometricMean();
    }

    @Override // org.apache.commons.math.stat.descriptive.SummaryStatistics
    public synchronized java.lang.String toString() {
        return super.toString();
    }

    @Override // org.apache.commons.math.stat.descriptive.SummaryStatistics
    public synchronized void clear() {
        super.clear();
    }

    @Override // org.apache.commons.math.stat.descriptive.SummaryStatistics
    public synchronized boolean equals(java.lang.Object object) {
        return super.equals(object);
    }

    @Override // org.apache.commons.math.stat.descriptive.SummaryStatistics
    public synchronized int hashCode() {
        return super.hashCode();
    }

    @Override // org.apache.commons.math.stat.descriptive.SummaryStatistics
    public synchronized org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic getSumImpl() {
        return super.getSumImpl();
    }

    @Override // org.apache.commons.math.stat.descriptive.SummaryStatistics
    public synchronized void setSumImpl(org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic sumImpl) {
        super.setSumImpl(sumImpl);
    }

    @Override // org.apache.commons.math.stat.descriptive.SummaryStatistics
    public synchronized org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic getSumsqImpl() {
        return super.getSumsqImpl();
    }

    @Override // org.apache.commons.math.stat.descriptive.SummaryStatistics
    public synchronized void setSumsqImpl(org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic sumsqImpl) {
        super.setSumsqImpl(sumsqImpl);
    }

    @Override // org.apache.commons.math.stat.descriptive.SummaryStatistics
    public synchronized org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic getMinImpl() {
        return super.getMinImpl();
    }

    @Override // org.apache.commons.math.stat.descriptive.SummaryStatistics
    public synchronized void setMinImpl(org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic minImpl) {
        super.setMinImpl(minImpl);
    }

    @Override // org.apache.commons.math.stat.descriptive.SummaryStatistics
    public synchronized org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic getMaxImpl() {
        return super.getMaxImpl();
    }

    @Override // org.apache.commons.math.stat.descriptive.SummaryStatistics
    public synchronized void setMaxImpl(org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic maxImpl) {
        super.setMaxImpl(maxImpl);
    }

    @Override // org.apache.commons.math.stat.descriptive.SummaryStatistics
    public synchronized org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic getSumLogImpl() {
        return super.getSumLogImpl();
    }

    @Override // org.apache.commons.math.stat.descriptive.SummaryStatistics
    public synchronized void setSumLogImpl(org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic sumLogImpl) {
        super.setSumLogImpl(sumLogImpl);
    }

    @Override // org.apache.commons.math.stat.descriptive.SummaryStatistics
    public synchronized org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic getGeoMeanImpl() {
        return super.getGeoMeanImpl();
    }

    @Override // org.apache.commons.math.stat.descriptive.SummaryStatistics
    public synchronized void setGeoMeanImpl(org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic geoMeanImpl) {
        super.setGeoMeanImpl(geoMeanImpl);
    }

    @Override // org.apache.commons.math.stat.descriptive.SummaryStatistics
    public synchronized org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic getMeanImpl() {
        return super.getMeanImpl();
    }

    @Override // org.apache.commons.math.stat.descriptive.SummaryStatistics
    public synchronized void setMeanImpl(org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic meanImpl) {
        super.setMeanImpl(meanImpl);
    }

    @Override // org.apache.commons.math.stat.descriptive.SummaryStatistics
    public synchronized org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic getVarianceImpl() {
        return super.getVarianceImpl();
    }

    @Override // org.apache.commons.math.stat.descriptive.SummaryStatistics
    public synchronized void setVarianceImpl(org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic varianceImpl) {
        super.setVarianceImpl(varianceImpl);
    }

    @Override // org.apache.commons.math.stat.descriptive.SummaryStatistics
    public synchronized org.apache.commons.math.stat.descriptive.SynchronizedSummaryStatistics copy() {
        org.apache.commons.math.stat.descriptive.SynchronizedSummaryStatistics result;
        result = new org.apache.commons.math.stat.descriptive.SynchronizedSummaryStatistics();
        copy(this, result);
        return result;
    }

    public static void copy(org.apache.commons.math.stat.descriptive.SynchronizedSummaryStatistics source, org.apache.commons.math.stat.descriptive.SynchronizedSummaryStatistics dest) {
        synchronized (source) {
            synchronized (dest) {
                org.apache.commons.math.stat.descriptive.SummaryStatistics.copy(source, dest);
            }
        }
    }
}
