package org.apache.commons.math.stat.descriptive;

/* JADX INFO: loaded from: classes4.dex */
public class SummaryStatistics implements org.apache.commons.math.stat.descriptive.StatisticalSummary, java.io.Serializable {
    private static final long serialVersionUID = -2021321786743555871L;
    protected long n = 0;
    protected org.apache.commons.math.stat.descriptive.moment.SecondMoment secondMoment = new org.apache.commons.math.stat.descriptive.moment.SecondMoment();
    protected org.apache.commons.math.stat.descriptive.summary.Sum sum = new org.apache.commons.math.stat.descriptive.summary.Sum();
    protected org.apache.commons.math.stat.descriptive.summary.SumOfSquares sumsq = new org.apache.commons.math.stat.descriptive.summary.SumOfSquares();
    protected org.apache.commons.math.stat.descriptive.rank.Min min = new org.apache.commons.math.stat.descriptive.rank.Min();
    protected org.apache.commons.math.stat.descriptive.rank.Max max = new org.apache.commons.math.stat.descriptive.rank.Max();
    protected org.apache.commons.math.stat.descriptive.summary.SumOfLogs sumLog = new org.apache.commons.math.stat.descriptive.summary.SumOfLogs();
    protected org.apache.commons.math.stat.descriptive.moment.GeometricMean geoMean = new org.apache.commons.math.stat.descriptive.moment.GeometricMean(this.sumLog);
    protected org.apache.commons.math.stat.descriptive.moment.Mean mean = new org.apache.commons.math.stat.descriptive.moment.Mean();
    protected org.apache.commons.math.stat.descriptive.moment.Variance variance = new org.apache.commons.math.stat.descriptive.moment.Variance();
    private org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic sumImpl = this.sum;
    private org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic sumsqImpl = this.sumsq;
    private org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic minImpl = this.min;
    private org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic maxImpl = this.max;
    private org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic sumLogImpl = this.sumLog;
    private org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic geoMeanImpl = this.geoMean;
    private org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic meanImpl = this.mean;
    private org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic varianceImpl = this.variance;

    public SummaryStatistics() {
    }

    public SummaryStatistics(org.apache.commons.math.stat.descriptive.SummaryStatistics original) {
        copy(original, this);
    }

    public org.apache.commons.math.stat.descriptive.StatisticalSummary getSummary() {
        return new org.apache.commons.math.stat.descriptive.StatisticalSummaryValues(getMean(), getVariance(), getN(), getMax(), getMin(), getSum());
    }

    public void addValue(double value) {
        this.sumImpl.increment(value);
        this.sumsqImpl.increment(value);
        this.minImpl.increment(value);
        this.maxImpl.increment(value);
        this.sumLogImpl.increment(value);
        this.secondMoment.increment(value);
        if (!(this.meanImpl instanceof org.apache.commons.math.stat.descriptive.moment.Mean)) {
            this.meanImpl.increment(value);
        }
        if (!(this.varianceImpl instanceof org.apache.commons.math.stat.descriptive.moment.Variance)) {
            this.varianceImpl.increment(value);
        }
        if (!(this.geoMeanImpl instanceof org.apache.commons.math.stat.descriptive.moment.GeometricMean)) {
            this.geoMeanImpl.increment(value);
        }
        this.n++;
    }

    @Override // org.apache.commons.math.stat.descriptive.StatisticalSummary
    public long getN() {
        return this.n;
    }

    @Override // org.apache.commons.math.stat.descriptive.StatisticalSummary
    public double getSum() {
        return this.sumImpl.getResult();
    }

    public double getSumsq() {
        return this.sumsqImpl.getResult();
    }

    @Override // org.apache.commons.math.stat.descriptive.StatisticalSummary
    public double getMean() {
        if (this.mean == this.meanImpl) {
            return new org.apache.commons.math.stat.descriptive.moment.Mean(this.secondMoment).getResult();
        }
        return this.meanImpl.getResult();
    }

    @Override // org.apache.commons.math.stat.descriptive.StatisticalSummary
    public double getStandardDeviation() {
        if (getN() <= 0) {
            return Double.NaN;
        }
        if (getN() > 1) {
            double stdDev = org.apache.commons.math.util.FastMath.sqrt(getVariance());
            return stdDev;
        }
        return 0.0d;
    }

    @Override // org.apache.commons.math.stat.descriptive.StatisticalSummary
    public double getVariance() {
        if (this.varianceImpl == this.variance) {
            return new org.apache.commons.math.stat.descriptive.moment.Variance(this.secondMoment).getResult();
        }
        return this.varianceImpl.getResult();
    }

    @Override // org.apache.commons.math.stat.descriptive.StatisticalSummary
    public double getMax() {
        return this.maxImpl.getResult();
    }

    @Override // org.apache.commons.math.stat.descriptive.StatisticalSummary
    public double getMin() {
        return this.minImpl.getResult();
    }

    public double getGeometricMean() {
        return this.geoMeanImpl.getResult();
    }

    public double getSumOfLogs() {
        return this.sumLogImpl.getResult();
    }

    public double getSecondMoment() {
        return this.secondMoment.getResult();
    }

    public java.lang.String toString() {
        java.lang.StringBuilder outBuffer = new java.lang.StringBuilder();
        outBuffer.append("SummaryStatistics:").append("\n");
        outBuffer.append("n: ").append(getN()).append("\n");
        outBuffer.append("min: ").append(getMin()).append("\n");
        outBuffer.append("max: ").append(getMax()).append("\n");
        outBuffer.append("mean: ").append(getMean()).append("\n");
        outBuffer.append("geometric mean: ").append(getGeometricMean()).append("\n");
        outBuffer.append("variance: ").append(getVariance()).append("\n");
        outBuffer.append("sum of squares: ").append(getSumsq()).append("\n");
        outBuffer.append("standard deviation: ").append(getStandardDeviation()).append("\n");
        outBuffer.append("sum of logs: ").append(getSumOfLogs()).append("\n");
        return outBuffer.toString();
    }

    public void clear() {
        this.n = 0L;
        this.minImpl.clear();
        this.maxImpl.clear();
        this.sumImpl.clear();
        this.sumLogImpl.clear();
        this.sumsqImpl.clear();
        this.geoMeanImpl.clear();
        this.secondMoment.clear();
        if (this.meanImpl != this.mean) {
            this.meanImpl.clear();
        }
        if (this.varianceImpl != this.variance) {
            this.varianceImpl.clear();
        }
    }

    public boolean equals(java.lang.Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof org.apache.commons.math.stat.descriptive.SummaryStatistics)) {
            return false;
        }
        org.apache.commons.math.stat.descriptive.SummaryStatistics stat = (org.apache.commons.math.stat.descriptive.SummaryStatistics) object;
        return org.apache.commons.math.util.MathUtils.equalsIncludingNaN(stat.getGeometricMean(), getGeometricMean()) && org.apache.commons.math.util.MathUtils.equalsIncludingNaN(stat.getMax(), getMax()) && org.apache.commons.math.util.MathUtils.equalsIncludingNaN(stat.getMean(), getMean()) && org.apache.commons.math.util.MathUtils.equalsIncludingNaN(stat.getMin(), getMin()) && org.apache.commons.math.util.MathUtils.equalsIncludingNaN((float) stat.getN(), (float) getN()) && org.apache.commons.math.util.MathUtils.equalsIncludingNaN(stat.getSum(), getSum()) && org.apache.commons.math.util.MathUtils.equalsIncludingNaN(stat.getSumsq(), getSumsq()) && org.apache.commons.math.util.MathUtils.equalsIncludingNaN(stat.getVariance(), getVariance());
    }

    public int hashCode() {
        int result = org.apache.commons.math.util.MathUtils.hash(getGeometricMean()) + 31;
        return (((((((((((((((result * 31) + org.apache.commons.math.util.MathUtils.hash(getGeometricMean())) * 31) + org.apache.commons.math.util.MathUtils.hash(getMax())) * 31) + org.apache.commons.math.util.MathUtils.hash(getMean())) * 31) + org.apache.commons.math.util.MathUtils.hash(getMin())) * 31) + org.apache.commons.math.util.MathUtils.hash(getN())) * 31) + org.apache.commons.math.util.MathUtils.hash(getSum())) * 31) + org.apache.commons.math.util.MathUtils.hash(getSumsq())) * 31) + org.apache.commons.math.util.MathUtils.hash(getVariance());
    }

    public org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic getSumImpl() {
        return this.sumImpl;
    }

    public void setSumImpl(org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic sumImpl) {
        checkEmpty();
        this.sumImpl = sumImpl;
    }

    public org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic getSumsqImpl() {
        return this.sumsqImpl;
    }

    public void setSumsqImpl(org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic sumsqImpl) {
        checkEmpty();
        this.sumsqImpl = sumsqImpl;
    }

    public org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic getMinImpl() {
        return this.minImpl;
    }

    public void setMinImpl(org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic minImpl) {
        checkEmpty();
        this.minImpl = minImpl;
    }

    public org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic getMaxImpl() {
        return this.maxImpl;
    }

    public void setMaxImpl(org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic maxImpl) {
        checkEmpty();
        this.maxImpl = maxImpl;
    }

    public org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic getSumLogImpl() {
        return this.sumLogImpl;
    }

    public void setSumLogImpl(org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic sumLogImpl) {
        checkEmpty();
        this.sumLogImpl = sumLogImpl;
        this.geoMean.setSumLogImpl(sumLogImpl);
    }

    public org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic getGeoMeanImpl() {
        return this.geoMeanImpl;
    }

    public void setGeoMeanImpl(org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic geoMeanImpl) {
        checkEmpty();
        this.geoMeanImpl = geoMeanImpl;
    }

    public org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic getMeanImpl() {
        return this.meanImpl;
    }

    public void setMeanImpl(org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic meanImpl) {
        checkEmpty();
        this.meanImpl = meanImpl;
    }

    public org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic getVarianceImpl() {
        return this.varianceImpl;
    }

    public void setVarianceImpl(org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic varianceImpl) {
        checkEmpty();
        this.varianceImpl = varianceImpl;
    }

    private void checkEmpty() {
        if (this.n > 0) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalStateException(org.apache.commons.math.exception.util.LocalizedFormats.VALUES_ADDED_BEFORE_CONFIGURING_STATISTIC, java.lang.Long.valueOf(this.n));
        }
    }

    public org.apache.commons.math.stat.descriptive.SummaryStatistics copy() {
        org.apache.commons.math.stat.descriptive.SummaryStatistics result = new org.apache.commons.math.stat.descriptive.SummaryStatistics();
        copy(this, result);
        return result;
    }

    public static void copy(org.apache.commons.math.stat.descriptive.SummaryStatistics source, org.apache.commons.math.stat.descriptive.SummaryStatistics dest) {
        dest.maxImpl = source.maxImpl.copy();
        dest.meanImpl = source.meanImpl.copy();
        dest.minImpl = source.minImpl.copy();
        dest.sumImpl = source.sumImpl.copy();
        dest.varianceImpl = source.varianceImpl.copy();
        dest.sumLogImpl = source.sumLogImpl.copy();
        dest.sumsqImpl = source.sumsqImpl.copy();
        if (source.getGeoMeanImpl() instanceof org.apache.commons.math.stat.descriptive.moment.GeometricMean) {
            dest.geoMeanImpl = new org.apache.commons.math.stat.descriptive.moment.GeometricMean((org.apache.commons.math.stat.descriptive.summary.SumOfLogs) dest.sumLogImpl);
        } else {
            dest.geoMeanImpl = source.geoMeanImpl.copy();
        }
        org.apache.commons.math.stat.descriptive.moment.SecondMoment.copy(source.secondMoment, dest.secondMoment);
        dest.n = source.n;
        if (source.geoMean == source.geoMeanImpl) {
            dest.geoMean = (org.apache.commons.math.stat.descriptive.moment.GeometricMean) dest.geoMeanImpl;
        } else {
            org.apache.commons.math.stat.descriptive.moment.GeometricMean.copy(source.geoMean, dest.geoMean);
        }
        if (source.max == source.maxImpl) {
            dest.max = (org.apache.commons.math.stat.descriptive.rank.Max) dest.maxImpl;
        } else {
            org.apache.commons.math.stat.descriptive.rank.Max.copy(source.max, dest.max);
        }
        if (source.mean == source.meanImpl) {
            dest.mean = (org.apache.commons.math.stat.descriptive.moment.Mean) dest.meanImpl;
        } else {
            org.apache.commons.math.stat.descriptive.moment.Mean.copy(source.mean, dest.mean);
        }
        if (source.min == source.minImpl) {
            dest.min = (org.apache.commons.math.stat.descriptive.rank.Min) dest.minImpl;
        } else {
            org.apache.commons.math.stat.descriptive.rank.Min.copy(source.min, dest.min);
        }
        if (source.sum == source.sumImpl) {
            dest.sum = (org.apache.commons.math.stat.descriptive.summary.Sum) dest.sumImpl;
        } else {
            org.apache.commons.math.stat.descriptive.summary.Sum.copy(source.sum, dest.sum);
        }
        if (source.variance == source.varianceImpl) {
            dest.variance = (org.apache.commons.math.stat.descriptive.moment.Variance) dest.varianceImpl;
        } else {
            org.apache.commons.math.stat.descriptive.moment.Variance.copy(source.variance, dest.variance);
        }
        if (source.sumLog == source.sumLogImpl) {
            dest.sumLog = (org.apache.commons.math.stat.descriptive.summary.SumOfLogs) dest.sumLogImpl;
        } else {
            org.apache.commons.math.stat.descriptive.summary.SumOfLogs.copy(source.sumLog, dest.sumLog);
        }
        if (source.sumsq == source.sumsqImpl) {
            dest.sumsq = (org.apache.commons.math.stat.descriptive.summary.SumOfSquares) dest.sumsqImpl;
        } else {
            org.apache.commons.math.stat.descriptive.summary.SumOfSquares.copy(source.sumsq, dest.sumsq);
        }
    }
}
