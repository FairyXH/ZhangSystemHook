package org.apache.commons.math.stat.descriptive;

/* JADX INFO: loaded from: classes4.dex */
public class MultivariateSummaryStatistics implements org.apache.commons.math.stat.descriptive.StatisticalMultivariateSummary, java.io.Serializable {
    private static final long serialVersionUID = 2271900808994826718L;
    private org.apache.commons.math.stat.descriptive.moment.VectorialCovariance covarianceImpl;
    private org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic[] geoMeanImpl;
    private int k;
    private org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic[] maxImpl;
    private org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic[] meanImpl;
    private org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic[] minImpl;
    private long n = 0;
    private org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic[] sumImpl;
    private org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic[] sumLogImpl;
    private org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic[] sumSqImpl;

    public MultivariateSummaryStatistics(int k, boolean isCovarianceBiasCorrected) {
        this.k = k;
        this.sumImpl = new org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic[k];
        this.sumSqImpl = new org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic[k];
        this.minImpl = new org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic[k];
        this.maxImpl = new org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic[k];
        this.sumLogImpl = new org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic[k];
        this.geoMeanImpl = new org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic[k];
        this.meanImpl = new org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic[k];
        for (int i = 0; i < k; i++) {
            this.sumImpl[i] = new org.apache.commons.math.stat.descriptive.summary.Sum();
            this.sumSqImpl[i] = new org.apache.commons.math.stat.descriptive.summary.SumOfSquares();
            this.minImpl[i] = new org.apache.commons.math.stat.descriptive.rank.Min();
            this.maxImpl[i] = new org.apache.commons.math.stat.descriptive.rank.Max();
            this.sumLogImpl[i] = new org.apache.commons.math.stat.descriptive.summary.SumOfLogs();
            this.geoMeanImpl[i] = new org.apache.commons.math.stat.descriptive.moment.GeometricMean();
            this.meanImpl[i] = new org.apache.commons.math.stat.descriptive.moment.Mean();
        }
        this.covarianceImpl = new org.apache.commons.math.stat.descriptive.moment.VectorialCovariance(k, isCovarianceBiasCorrected);
    }

    public void addValue(double[] value) throws org.apache.commons.math.DimensionMismatchException {
        checkDimension(value.length);
        for (int i = 0; i < this.k; i++) {
            double v = value[i];
            this.sumImpl[i].increment(v);
            this.sumSqImpl[i].increment(v);
            this.minImpl[i].increment(v);
            this.maxImpl[i].increment(v);
            this.sumLogImpl[i].increment(v);
            this.geoMeanImpl[i].increment(v);
            this.meanImpl[i].increment(v);
        }
        this.covarianceImpl.increment(value);
        this.n++;
    }

    @Override // org.apache.commons.math.stat.descriptive.StatisticalMultivariateSummary
    public int getDimension() {
        return this.k;
    }

    @Override // org.apache.commons.math.stat.descriptive.StatisticalMultivariateSummary
    public long getN() {
        return this.n;
    }

    private double[] getResults(org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic[] stats) {
        double[] results = new double[stats.length];
        for (int i = 0; i < results.length; i++) {
            results[i] = stats[i].getResult();
        }
        return results;
    }

    @Override // org.apache.commons.math.stat.descriptive.StatisticalMultivariateSummary
    public double[] getSum() {
        return getResults(this.sumImpl);
    }

    @Override // org.apache.commons.math.stat.descriptive.StatisticalMultivariateSummary
    public double[] getSumSq() {
        return getResults(this.sumSqImpl);
    }

    @Override // org.apache.commons.math.stat.descriptive.StatisticalMultivariateSummary
    public double[] getSumLog() {
        return getResults(this.sumLogImpl);
    }

    @Override // org.apache.commons.math.stat.descriptive.StatisticalMultivariateSummary
    public double[] getMean() {
        return getResults(this.meanImpl);
    }

    @Override // org.apache.commons.math.stat.descriptive.StatisticalMultivariateSummary
    public double[] getStandardDeviation() {
        double[] stdDev = new double[this.k];
        if (getN() < 1) {
            java.util.Arrays.fill(stdDev, Double.NaN);
        } else if (getN() < 2) {
            java.util.Arrays.fill(stdDev, 0.0d);
        } else {
            org.apache.commons.math.linear.RealMatrix matrix = this.covarianceImpl.getResult();
            for (int i = 0; i < this.k; i++) {
                stdDev[i] = org.apache.commons.math.util.FastMath.sqrt(matrix.getEntry(i, i));
            }
        }
        return stdDev;
    }

    @Override // org.apache.commons.math.stat.descriptive.StatisticalMultivariateSummary
    public org.apache.commons.math.linear.RealMatrix getCovariance() {
        return this.covarianceImpl.getResult();
    }

    @Override // org.apache.commons.math.stat.descriptive.StatisticalMultivariateSummary
    public double[] getMax() {
        return getResults(this.maxImpl);
    }

    @Override // org.apache.commons.math.stat.descriptive.StatisticalMultivariateSummary
    public double[] getMin() {
        return getResults(this.minImpl);
    }

    @Override // org.apache.commons.math.stat.descriptive.StatisticalMultivariateSummary
    public double[] getGeometricMean() {
        return getResults(this.geoMeanImpl);
    }

    public java.lang.String toString() {
        java.lang.String suffix = java.lang.System.getProperty("line.separator");
        java.lang.StringBuilder outBuffer = new java.lang.StringBuilder();
        outBuffer.append("MultivariateSummaryStatistics:" + suffix);
        outBuffer.append("n: " + getN() + suffix);
        append(outBuffer, getMin(), "min: ", ", ", suffix);
        append(outBuffer, getMax(), "max: ", ", ", suffix);
        append(outBuffer, getMean(), "mean: ", ", ", suffix);
        append(outBuffer, getGeometricMean(), "geometric mean: ", ", ", suffix);
        append(outBuffer, getSumSq(), "sum of squares: ", ", ", suffix);
        append(outBuffer, getSumLog(), "sum of logarithms: ", ", ", suffix);
        append(outBuffer, getStandardDeviation(), "standard deviation: ", ", ", suffix);
        outBuffer.append("covariance: " + getCovariance().toString() + suffix);
        return outBuffer.toString();
    }

    private void append(java.lang.StringBuilder buffer, double[] data, java.lang.String prefix, java.lang.String separator, java.lang.String suffix) {
        buffer.append(prefix);
        for (int i = 0; i < data.length; i++) {
            if (i > 0) {
                buffer.append(separator);
            }
            buffer.append(data[i]);
        }
        buffer.append(suffix);
    }

    public void clear() {
        this.n = 0L;
        for (int i = 0; i < this.k; i++) {
            this.minImpl[i].clear();
            this.maxImpl[i].clear();
            this.sumImpl[i].clear();
            this.sumLogImpl[i].clear();
            this.sumSqImpl[i].clear();
            this.geoMeanImpl[i].clear();
            this.meanImpl[i].clear();
        }
        this.covarianceImpl.clear();
    }

    public boolean equals(java.lang.Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof org.apache.commons.math.stat.descriptive.MultivariateSummaryStatistics)) {
            return false;
        }
        org.apache.commons.math.stat.descriptive.MultivariateSummaryStatistics stat = (org.apache.commons.math.stat.descriptive.MultivariateSummaryStatistics) object;
        return org.apache.commons.math.util.MathUtils.equalsIncludingNaN(stat.getGeometricMean(), getGeometricMean()) && org.apache.commons.math.util.MathUtils.equalsIncludingNaN(stat.getMax(), getMax()) && org.apache.commons.math.util.MathUtils.equalsIncludingNaN(stat.getMean(), getMean()) && org.apache.commons.math.util.MathUtils.equalsIncludingNaN(stat.getMin(), getMin()) && org.apache.commons.math.util.MathUtils.equalsIncludingNaN((float) stat.getN(), (float) getN()) && org.apache.commons.math.util.MathUtils.equalsIncludingNaN(stat.getSum(), getSum()) && org.apache.commons.math.util.MathUtils.equalsIncludingNaN(stat.getSumSq(), getSumSq()) && org.apache.commons.math.util.MathUtils.equalsIncludingNaN(stat.getSumLog(), getSumLog()) && stat.getCovariance().equals(getCovariance());
    }

    public int hashCode() {
        int result = org.apache.commons.math.util.MathUtils.hash(getGeometricMean()) + 31;
        return (((((((((((((((((result * 31) + org.apache.commons.math.util.MathUtils.hash(getGeometricMean())) * 31) + org.apache.commons.math.util.MathUtils.hash(getMax())) * 31) + org.apache.commons.math.util.MathUtils.hash(getMean())) * 31) + org.apache.commons.math.util.MathUtils.hash(getMin())) * 31) + org.apache.commons.math.util.MathUtils.hash(getN())) * 31) + org.apache.commons.math.util.MathUtils.hash(getSum())) * 31) + org.apache.commons.math.util.MathUtils.hash(getSumSq())) * 31) + org.apache.commons.math.util.MathUtils.hash(getSumLog())) * 31) + getCovariance().hashCode();
    }

    private void setImpl(org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic[] newImpl, org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic[] oldImpl) throws java.lang.IllegalStateException, org.apache.commons.math.DimensionMismatchException {
        checkEmpty();
        checkDimension(newImpl.length);
        java.lang.System.arraycopy(newImpl, 0, oldImpl, 0, newImpl.length);
    }

    public org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic[] getSumImpl() {
        return (org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic[]) this.sumImpl.clone();
    }

    public void setSumImpl(org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic[] sumImpl) throws org.apache.commons.math.DimensionMismatchException {
        setImpl(sumImpl, this.sumImpl);
    }

    public org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic[] getSumsqImpl() {
        return (org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic[]) this.sumSqImpl.clone();
    }

    public void setSumsqImpl(org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic[] sumsqImpl) throws org.apache.commons.math.DimensionMismatchException {
        setImpl(sumsqImpl, this.sumSqImpl);
    }

    public org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic[] getMinImpl() {
        return (org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic[]) this.minImpl.clone();
    }

    public void setMinImpl(org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic[] minImpl) throws org.apache.commons.math.DimensionMismatchException {
        setImpl(minImpl, this.minImpl);
    }

    public org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic[] getMaxImpl() {
        return (org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic[]) this.maxImpl.clone();
    }

    public void setMaxImpl(org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic[] maxImpl) throws org.apache.commons.math.DimensionMismatchException {
        setImpl(maxImpl, this.maxImpl);
    }

    public org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic[] getSumLogImpl() {
        return (org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic[]) this.sumLogImpl.clone();
    }

    public void setSumLogImpl(org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic[] sumLogImpl) throws org.apache.commons.math.DimensionMismatchException {
        setImpl(sumLogImpl, this.sumLogImpl);
    }

    public org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic[] getGeoMeanImpl() {
        return (org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic[]) this.geoMeanImpl.clone();
    }

    public void setGeoMeanImpl(org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic[] geoMeanImpl) throws org.apache.commons.math.DimensionMismatchException {
        setImpl(geoMeanImpl, this.geoMeanImpl);
    }

    public org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic[] getMeanImpl() {
        return (org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic[]) this.meanImpl.clone();
    }

    public void setMeanImpl(org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic[] meanImpl) throws org.apache.commons.math.DimensionMismatchException {
        setImpl(meanImpl, this.meanImpl);
    }

    private void checkEmpty() {
        if (this.n > 0) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalStateException(org.apache.commons.math.exception.util.LocalizedFormats.VALUES_ADDED_BEFORE_CONFIGURING_STATISTIC, java.lang.Long.valueOf(this.n));
        }
    }

    private void checkDimension(int dimension) throws org.apache.commons.math.DimensionMismatchException {
        if (dimension != this.k) {
            throw new org.apache.commons.math.DimensionMismatchException(dimension, this.k);
        }
    }
}
