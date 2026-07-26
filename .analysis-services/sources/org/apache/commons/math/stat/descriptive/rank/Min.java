package org.apache.commons.math.stat.descriptive.rank;

/* JADX INFO: loaded from: classes4.dex */
public class Min extends org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic implements java.io.Serializable {
    private static final long serialVersionUID = -2941995784909003131L;
    private long n;
    private double value;

    public Min() {
        this.n = 0L;
        this.value = Double.NaN;
    }

    public Min(org.apache.commons.math.stat.descriptive.rank.Min original) {
        copy(original, this);
    }

    @Override // org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic
    public void increment(double d) {
        if (d < this.value || java.lang.Double.isNaN(this.value)) {
            this.value = d;
        }
        this.n++;
    }

    @Override // org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic
    public void clear() {
        this.value = Double.NaN;
        this.n = 0L;
    }

    @Override // org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic
    public double getResult() {
        return this.value;
    }

    @Override // org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic
    public long getN() {
        return this.n;
    }

    @Override // org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math.stat.descriptive.AbstractUnivariateStatistic, org.apache.commons.math.stat.descriptive.UnivariateStatistic
    public double evaluate(double[] values, int begin, int length) {
        double min = Double.NaN;
        if (test(values, begin, length)) {
            min = values[begin];
            for (int i = begin; i < begin + length; i++) {
                if (!java.lang.Double.isNaN(values[i])) {
                    min = min < values[i] ? min : values[i];
                }
            }
        }
        return min;
    }

    @Override // org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math.stat.descriptive.AbstractUnivariateStatistic, org.apache.commons.math.stat.descriptive.UnivariateStatistic, org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic
    public org.apache.commons.math.stat.descriptive.rank.Min copy() {
        org.apache.commons.math.stat.descriptive.rank.Min result = new org.apache.commons.math.stat.descriptive.rank.Min();
        copy(this, result);
        return result;
    }

    public static void copy(org.apache.commons.math.stat.descriptive.rank.Min source, org.apache.commons.math.stat.descriptive.rank.Min dest) {
        dest.setData(source.getDataRef());
        dest.n = source.n;
        dest.value = source.value;
    }
}
