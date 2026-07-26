package org.apache.commons.math.stat.descriptive.summary;

/* JADX INFO: loaded from: classes4.dex */
public class SumOfSquares extends org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic implements java.io.Serializable {
    private static final long serialVersionUID = 1460986908574398008L;
    private long n;
    private double value;

    public SumOfSquares() {
        this.n = 0L;
        this.value = Double.NaN;
    }

    public SumOfSquares(org.apache.commons.math.stat.descriptive.summary.SumOfSquares original) {
        copy(original, this);
    }

    @Override // org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic
    public void increment(double d) {
        if (this.n == 0) {
            this.value = d * d;
        } else {
            this.value += d * d;
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
        double sumSq = Double.NaN;
        if (test(values, begin, length)) {
            sumSq = 0.0d;
            for (int i = begin; i < begin + length; i++) {
                sumSq += values[i] * values[i];
            }
        }
        return sumSq;
    }

    @Override // org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math.stat.descriptive.AbstractUnivariateStatistic, org.apache.commons.math.stat.descriptive.UnivariateStatistic, org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic
    public org.apache.commons.math.stat.descriptive.summary.SumOfSquares copy() {
        org.apache.commons.math.stat.descriptive.summary.SumOfSquares result = new org.apache.commons.math.stat.descriptive.summary.SumOfSquares();
        copy(this, result);
        return result;
    }

    public static void copy(org.apache.commons.math.stat.descriptive.summary.SumOfSquares source, org.apache.commons.math.stat.descriptive.summary.SumOfSquares dest) {
        dest.setData(source.getDataRef());
        dest.n = source.n;
        dest.value = source.value;
    }
}
