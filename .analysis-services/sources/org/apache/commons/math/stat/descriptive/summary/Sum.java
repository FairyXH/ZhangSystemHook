package org.apache.commons.math.stat.descriptive.summary;

/* JADX INFO: loaded from: classes4.dex */
public class Sum extends org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic implements java.io.Serializable {
    private static final long serialVersionUID = -8231831954703408316L;
    private long n;
    private double value;

    public Sum() {
        this.n = 0L;
        this.value = Double.NaN;
    }

    public Sum(org.apache.commons.math.stat.descriptive.summary.Sum original) {
        copy(original, this);
    }

    @Override // org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic
    public void increment(double d) {
        if (this.n == 0) {
            this.value = d;
        } else {
            this.value += d;
        }
        this.n++;
    }

    @Override // org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic
    public double getResult() {
        return this.value;
    }

    @Override // org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic
    public long getN() {
        return this.n;
    }

    @Override // org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic
    public void clear() {
        this.value = Double.NaN;
        this.n = 0L;
    }

    @Override // org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math.stat.descriptive.AbstractUnivariateStatistic, org.apache.commons.math.stat.descriptive.UnivariateStatistic
    public double evaluate(double[] values, int begin, int length) {
        double sum = Double.NaN;
        if (test(values, begin, length)) {
            sum = 0.0d;
            for (int i = begin; i < begin + length; i++) {
                sum += values[i];
            }
        }
        return sum;
    }

    public double evaluate(double[] values, double[] weights, int begin, int length) {
        double sum = Double.NaN;
        if (test(values, weights, begin, length)) {
            sum = 0.0d;
            for (int i = begin; i < begin + length; i++) {
                sum += values[i] * weights[i];
            }
        }
        return sum;
    }

    public double evaluate(double[] values, double[] weights) {
        return evaluate(values, weights, 0, values.length);
    }

    @Override // org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math.stat.descriptive.AbstractUnivariateStatistic, org.apache.commons.math.stat.descriptive.UnivariateStatistic, org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic
    public org.apache.commons.math.stat.descriptive.summary.Sum copy() {
        org.apache.commons.math.stat.descriptive.summary.Sum result = new org.apache.commons.math.stat.descriptive.summary.Sum();
        copy(this, result);
        return result;
    }

    public static void copy(org.apache.commons.math.stat.descriptive.summary.Sum source, org.apache.commons.math.stat.descriptive.summary.Sum dest) {
        dest.setData(source.getDataRef());
        dest.n = source.n;
        dest.value = source.value;
    }
}
