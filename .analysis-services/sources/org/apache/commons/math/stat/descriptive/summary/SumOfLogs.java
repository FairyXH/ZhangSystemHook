package org.apache.commons.math.stat.descriptive.summary;

/* JADX INFO: loaded from: classes4.dex */
public class SumOfLogs extends org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic implements java.io.Serializable {
    private static final long serialVersionUID = -370076995648386763L;
    private int n;
    private double value;

    public SumOfLogs() {
        this.value = 0.0d;
        this.n = 0;
    }

    public SumOfLogs(org.apache.commons.math.stat.descriptive.summary.SumOfLogs original) {
        copy(original, this);
    }

    @Override // org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic
    public void increment(double d) {
        this.value += org.apache.commons.math.util.FastMath.log(d);
        this.n++;
    }

    @Override // org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic
    public double getResult() {
        if (this.n > 0) {
            return this.value;
        }
        return Double.NaN;
    }

    @Override // org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic
    public long getN() {
        return this.n;
    }

    @Override // org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic
    public void clear() {
        this.value = 0.0d;
        this.n = 0;
    }

    @Override // org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math.stat.descriptive.AbstractUnivariateStatistic, org.apache.commons.math.stat.descriptive.UnivariateStatistic
    public double evaluate(double[] values, int begin, int length) {
        double sumLog = Double.NaN;
        if (test(values, begin, length)) {
            sumLog = 0.0d;
            for (int i = begin; i < begin + length; i++) {
                sumLog += org.apache.commons.math.util.FastMath.log(values[i]);
            }
        }
        return sumLog;
    }

    @Override // org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math.stat.descriptive.AbstractUnivariateStatistic, org.apache.commons.math.stat.descriptive.UnivariateStatistic, org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic
    public org.apache.commons.math.stat.descriptive.summary.SumOfLogs copy() {
        org.apache.commons.math.stat.descriptive.summary.SumOfLogs result = new org.apache.commons.math.stat.descriptive.summary.SumOfLogs();
        copy(this, result);
        return result;
    }

    public static void copy(org.apache.commons.math.stat.descriptive.summary.SumOfLogs source, org.apache.commons.math.stat.descriptive.summary.SumOfLogs dest) {
        dest.setData(source.getDataRef());
        dest.n = source.n;
        dest.value = source.value;
    }
}
