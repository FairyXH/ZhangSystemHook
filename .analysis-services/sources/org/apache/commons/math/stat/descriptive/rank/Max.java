package org.apache.commons.math.stat.descriptive.rank;

/* JADX INFO: loaded from: classes4.dex */
public class Max extends org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic implements java.io.Serializable {
    private static final long serialVersionUID = -5593383832225844641L;
    private long n;
    private double value;

    public Max() {
        this.n = 0L;
        this.value = Double.NaN;
    }

    public Max(org.apache.commons.math.stat.descriptive.rank.Max original) {
        copy(original, this);
    }

    @Override // org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic
    public void increment(double d) {
        if (d > this.value || java.lang.Double.isNaN(this.value)) {
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
        double max = Double.NaN;
        if (test(values, begin, length)) {
            max = values[begin];
            for (int i = begin; i < begin + length; i++) {
                if (!java.lang.Double.isNaN(values[i])) {
                    max = max > values[i] ? max : values[i];
                }
            }
        }
        return max;
    }

    @Override // org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math.stat.descriptive.AbstractUnivariateStatistic, org.apache.commons.math.stat.descriptive.UnivariateStatistic, org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic
    public org.apache.commons.math.stat.descriptive.rank.Max copy() {
        org.apache.commons.math.stat.descriptive.rank.Max result = new org.apache.commons.math.stat.descriptive.rank.Max();
        copy(this, result);
        return result;
    }

    public static void copy(org.apache.commons.math.stat.descriptive.rank.Max source, org.apache.commons.math.stat.descriptive.rank.Max dest) {
        dest.setData(source.getDataRef());
        dest.n = source.n;
        dest.value = source.value;
    }
}
