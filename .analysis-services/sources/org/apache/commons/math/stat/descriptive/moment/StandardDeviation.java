package org.apache.commons.math.stat.descriptive.moment;

/* JADX INFO: loaded from: classes4.dex */
public class StandardDeviation extends org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic implements java.io.Serializable {
    private static final long serialVersionUID = 5728716329662425188L;
    private org.apache.commons.math.stat.descriptive.moment.Variance variance;

    public StandardDeviation() {
        this.variance = null;
        this.variance = new org.apache.commons.math.stat.descriptive.moment.Variance();
    }

    public StandardDeviation(org.apache.commons.math.stat.descriptive.moment.SecondMoment m2) {
        this.variance = null;
        this.variance = new org.apache.commons.math.stat.descriptive.moment.Variance(m2);
    }

    public StandardDeviation(org.apache.commons.math.stat.descriptive.moment.StandardDeviation original) {
        this.variance = null;
        copy(original, this);
    }

    public StandardDeviation(boolean isBiasCorrected) {
        this.variance = null;
        this.variance = new org.apache.commons.math.stat.descriptive.moment.Variance(isBiasCorrected);
    }

    public StandardDeviation(boolean isBiasCorrected, org.apache.commons.math.stat.descriptive.moment.SecondMoment m2) {
        this.variance = null;
        this.variance = new org.apache.commons.math.stat.descriptive.moment.Variance(isBiasCorrected, m2);
    }

    @Override // org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic
    public void increment(double d) {
        this.variance.increment(d);
    }

    @Override // org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic
    public long getN() {
        return this.variance.getN();
    }

    @Override // org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic
    public double getResult() {
        return org.apache.commons.math.util.FastMath.sqrt(this.variance.getResult());
    }

    @Override // org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic
    public void clear() {
        this.variance.clear();
    }

    @Override // org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math.stat.descriptive.AbstractUnivariateStatistic, org.apache.commons.math.stat.descriptive.UnivariateStatistic
    public double evaluate(double[] values) {
        return org.apache.commons.math.util.FastMath.sqrt(this.variance.evaluate(values));
    }

    @Override // org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math.stat.descriptive.AbstractUnivariateStatistic, org.apache.commons.math.stat.descriptive.UnivariateStatistic
    public double evaluate(double[] values, int begin, int length) {
        return org.apache.commons.math.util.FastMath.sqrt(this.variance.evaluate(values, begin, length));
    }

    public double evaluate(double[] values, double mean, int begin, int length) {
        return org.apache.commons.math.util.FastMath.sqrt(this.variance.evaluate(values, mean, begin, length));
    }

    public double evaluate(double[] values, double mean) {
        return org.apache.commons.math.util.FastMath.sqrt(this.variance.evaluate(values, mean));
    }

    public boolean isBiasCorrected() {
        return this.variance.isBiasCorrected();
    }

    public void setBiasCorrected(boolean isBiasCorrected) {
        this.variance.setBiasCorrected(isBiasCorrected);
    }

    @Override // org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math.stat.descriptive.AbstractUnivariateStatistic, org.apache.commons.math.stat.descriptive.UnivariateStatistic, org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic
    public org.apache.commons.math.stat.descriptive.moment.StandardDeviation copy() {
        org.apache.commons.math.stat.descriptive.moment.StandardDeviation result = new org.apache.commons.math.stat.descriptive.moment.StandardDeviation();
        copy(this, result);
        return result;
    }

    public static void copy(org.apache.commons.math.stat.descriptive.moment.StandardDeviation source, org.apache.commons.math.stat.descriptive.moment.StandardDeviation dest) {
        dest.setData(source.getDataRef());
        dest.variance = source.variance.copy();
    }
}
