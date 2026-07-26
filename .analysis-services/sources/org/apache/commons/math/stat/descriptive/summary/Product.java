package org.apache.commons.math.stat.descriptive.summary;

/* JADX INFO: loaded from: classes4.dex */
public class Product extends org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic implements java.io.Serializable, org.apache.commons.math.stat.descriptive.WeightedEvaluation {
    private static final long serialVersionUID = 2824226005990582538L;
    private long n;
    private double value;

    public Product() {
        this.n = 0L;
        this.value = Double.NaN;
    }

    public Product(org.apache.commons.math.stat.descriptive.summary.Product original) {
        copy(original, this);
    }

    @Override // org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic
    public void increment(double d) {
        if (this.n == 0) {
            this.value = d;
        } else {
            this.value *= d;
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
        double product = Double.NaN;
        if (test(values, begin, length)) {
            product = 1.0d;
            for (int i = begin; i < begin + length; i++) {
                product *= values[i];
            }
        }
        return product;
    }

    @Override // org.apache.commons.math.stat.descriptive.WeightedEvaluation
    public double evaluate(double[] values, double[] weights, int begin, int length) {
        double product = Double.NaN;
        if (test(values, weights, begin, length)) {
            product = 1.0d;
            for (int i = begin; i < begin + length; i++) {
                product *= org.apache.commons.math.util.FastMath.pow(values[i], weights[i]);
            }
        }
        return product;
    }

    @Override // org.apache.commons.math.stat.descriptive.WeightedEvaluation
    public double evaluate(double[] values, double[] weights) {
        return evaluate(values, weights, 0, values.length);
    }

    @Override // org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math.stat.descriptive.AbstractUnivariateStatistic, org.apache.commons.math.stat.descriptive.UnivariateStatistic, org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic
    public org.apache.commons.math.stat.descriptive.summary.Product copy() {
        org.apache.commons.math.stat.descriptive.summary.Product result = new org.apache.commons.math.stat.descriptive.summary.Product();
        copy(this, result);
        return result;
    }

    public static void copy(org.apache.commons.math.stat.descriptive.summary.Product source, org.apache.commons.math.stat.descriptive.summary.Product dest) {
        dest.setData(source.getDataRef());
        dest.n = source.n;
        dest.value = source.value;
    }
}
