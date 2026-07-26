package org.apache.commons.math.stat.descriptive.moment;

/* JADX INFO: loaded from: classes4.dex */
public class GeometricMean extends org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic implements java.io.Serializable {
    private static final long serialVersionUID = -8178734905303459453L;
    private org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic sumOfLogs;

    public GeometricMean() {
        this.sumOfLogs = new org.apache.commons.math.stat.descriptive.summary.SumOfLogs();
    }

    public GeometricMean(org.apache.commons.math.stat.descriptive.moment.GeometricMean original) {
        copy(original, this);
    }

    public GeometricMean(org.apache.commons.math.stat.descriptive.summary.SumOfLogs sumOfLogs) {
        this.sumOfLogs = sumOfLogs;
    }

    @Override // org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math.stat.descriptive.AbstractUnivariateStatistic, org.apache.commons.math.stat.descriptive.UnivariateStatistic, org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic
    public org.apache.commons.math.stat.descriptive.moment.GeometricMean copy() {
        org.apache.commons.math.stat.descriptive.moment.GeometricMean result = new org.apache.commons.math.stat.descriptive.moment.GeometricMean();
        copy(this, result);
        return result;
    }

    @Override // org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic
    public void increment(double d) {
        this.sumOfLogs.increment(d);
    }

    @Override // org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic
    public double getResult() {
        if (this.sumOfLogs.getN() > 0) {
            return org.apache.commons.math.util.FastMath.exp(this.sumOfLogs.getResult() / this.sumOfLogs.getN());
        }
        return Double.NaN;
    }

    @Override // org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic
    public void clear() {
        this.sumOfLogs.clear();
    }

    @Override // org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math.stat.descriptive.AbstractUnivariateStatistic, org.apache.commons.math.stat.descriptive.UnivariateStatistic
    public double evaluate(double[] values, int begin, int length) {
        return org.apache.commons.math.util.FastMath.exp(this.sumOfLogs.evaluate(values, begin, length) / ((double) length));
    }

    @Override // org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic
    public long getN() {
        return this.sumOfLogs.getN();
    }

    public void setSumLogImpl(org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic sumLogImpl) {
        checkEmpty();
        this.sumOfLogs = sumLogImpl;
    }

    public org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic getSumLogImpl() {
        return this.sumOfLogs;
    }

    public static void copy(org.apache.commons.math.stat.descriptive.moment.GeometricMean source, org.apache.commons.math.stat.descriptive.moment.GeometricMean dest) {
        dest.setData(source.getDataRef());
        dest.sumOfLogs = source.sumOfLogs.copy();
    }

    private void checkEmpty() {
        if (getN() > 0) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalStateException(org.apache.commons.math.exception.util.LocalizedFormats.VALUES_ADDED_BEFORE_CONFIGURING_STATISTIC, java.lang.Long.valueOf(getN()));
        }
    }
}
