package org.apache.commons.math.stat.descriptive.moment;

/* JADX INFO: loaded from: classes4.dex */
public class Kurtosis extends org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic implements java.io.Serializable {
    private static final long serialVersionUID = 2784465764798260919L;
    protected boolean incMoment;
    protected org.apache.commons.math.stat.descriptive.moment.FourthMoment moment;

    public Kurtosis() {
        this.incMoment = true;
        this.moment = new org.apache.commons.math.stat.descriptive.moment.FourthMoment();
    }

    public Kurtosis(org.apache.commons.math.stat.descriptive.moment.FourthMoment m4) {
        this.incMoment = false;
        this.moment = m4;
    }

    public Kurtosis(org.apache.commons.math.stat.descriptive.moment.Kurtosis original) {
        copy(original, this);
    }

    @Override // org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic
    public void increment(double d) {
        if (this.incMoment) {
            this.moment.increment(d);
            return;
        }
        throw org.apache.commons.math.MathRuntimeException.createIllegalStateException(org.apache.commons.math.exception.util.LocalizedFormats.CANNOT_INCREMENT_STATISTIC_CONSTRUCTED_FROM_EXTERNAL_MOMENTS, new java.lang.Object[0]);
    }

    @Override // org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic
    public double getResult() {
        if (this.moment.getN() <= 3) {
            return Double.NaN;
        }
        double variance = this.moment.m2 / (this.moment.n - 1);
        if (this.moment.n <= 3 || variance < 1.0E-19d) {
            return 0.0d;
        }
        double n = this.moment.n;
        double kurtosis = ((((n + 1.0d) * n) * this.moment.m4) - (((this.moment.m2 * 3.0d) * this.moment.m2) * (n - 1.0d))) / (((((n - 1.0d) * (n - 2.0d)) * (n - 3.0d)) * variance) * variance);
        return kurtosis;
    }

    @Override // org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic
    public void clear() {
        if (this.incMoment) {
            this.moment.clear();
            return;
        }
        throw org.apache.commons.math.MathRuntimeException.createIllegalStateException(org.apache.commons.math.exception.util.LocalizedFormats.CANNOT_CLEAR_STATISTIC_CONSTRUCTED_FROM_EXTERNAL_MOMENTS, new java.lang.Object[0]);
    }

    @Override // org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic
    public long getN() {
        return this.moment.getN();
    }

    @Override // org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math.stat.descriptive.AbstractUnivariateStatistic, org.apache.commons.math.stat.descriptive.UnivariateStatistic
    public double evaluate(double[] values, int begin, int length) {
        double[] dArr = values;
        int i = begin;
        if (!test(values, begin, length) || length <= 3) {
            return Double.NaN;
        }
        org.apache.commons.math.stat.descriptive.moment.Variance variance = new org.apache.commons.math.stat.descriptive.moment.Variance();
        variance.incrementAll(dArr, i, length);
        double mean = variance.moment.m1;
        double stdDev = org.apache.commons.math.util.FastMath.sqrt(variance.getResult());
        double accum3 = 0.0d;
        int i2 = begin;
        while (i2 < i + length) {
            accum3 += org.apache.commons.math.util.FastMath.pow(dArr[i2] - mean, 4.0d);
            i2++;
            dArr = values;
            i = begin;
        }
        double accum32 = accum3 / org.apache.commons.math.util.FastMath.pow(stdDev, 4.0d);
        double n0 = length;
        double coefficientOne = ((n0 + 1.0d) * n0) / (((n0 - 1.0d) * (n0 - 2.0d)) * (n0 - 3.0d));
        double termTwo = (org.apache.commons.math.util.FastMath.pow(n0 - 1.0d, 2.0d) * 3.0d) / ((n0 - 2.0d) * (n0 - 3.0d));
        double kurt = (coefficientOne * accum32) - termTwo;
        return kurt;
    }

    @Override // org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math.stat.descriptive.AbstractUnivariateStatistic, org.apache.commons.math.stat.descriptive.UnivariateStatistic, org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic
    public org.apache.commons.math.stat.descriptive.moment.Kurtosis copy() {
        org.apache.commons.math.stat.descriptive.moment.Kurtosis result = new org.apache.commons.math.stat.descriptive.moment.Kurtosis();
        copy(this, result);
        return result;
    }

    public static void copy(org.apache.commons.math.stat.descriptive.moment.Kurtosis source, org.apache.commons.math.stat.descriptive.moment.Kurtosis dest) {
        dest.setData(source.getDataRef());
        dest.moment = source.moment.copy();
        dest.incMoment = source.incMoment;
    }
}
