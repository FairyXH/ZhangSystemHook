package org.apache.commons.math.stat.descriptive.moment;

/* JADX INFO: loaded from: classes4.dex */
public class Skewness extends org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic implements java.io.Serializable {
    private static final long serialVersionUID = 7101857578996691352L;
    protected boolean incMoment;
    protected org.apache.commons.math.stat.descriptive.moment.ThirdMoment moment;

    public Skewness() {
        this.moment = null;
        this.incMoment = true;
        this.moment = new org.apache.commons.math.stat.descriptive.moment.ThirdMoment();
    }

    public Skewness(org.apache.commons.math.stat.descriptive.moment.ThirdMoment m3) {
        this.moment = null;
        this.incMoment = false;
        this.moment = m3;
    }

    public Skewness(org.apache.commons.math.stat.descriptive.moment.Skewness original) {
        this.moment = null;
        copy(original, this);
    }

    @Override // org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic
    public void increment(double d) {
        if (this.incMoment) {
            this.moment.increment(d);
        }
    }

    @Override // org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic
    public double getResult() {
        if (this.moment.n < 3) {
            return Double.NaN;
        }
        double variance = this.moment.m2 / (this.moment.n - 1);
        if (variance < 1.0E-19d) {
            return 0.0d;
        }
        double n0 = this.moment.getN();
        return (this.moment.m3 * n0) / ((((n0 - 1.0d) * (n0 - 2.0d)) * org.apache.commons.math.util.FastMath.sqrt(variance)) * variance);
    }

    @Override // org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic
    public long getN() {
        return this.moment.getN();
    }

    @Override // org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic
    public void clear() {
        if (this.incMoment) {
            this.moment.clear();
        }
    }

    @Override // org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math.stat.descriptive.AbstractUnivariateStatistic, org.apache.commons.math.stat.descriptive.UnivariateStatistic
    public double evaluate(double[] values, int begin, int length) {
        if (!test(values, begin, length) || length <= 2) {
            return Double.NaN;
        }
        org.apache.commons.math.stat.descriptive.moment.Mean mean = new org.apache.commons.math.stat.descriptive.moment.Mean();
        double m = mean.evaluate(values, begin, length);
        double accum = 0.0d;
        double accum2 = 0.0d;
        for (int i = begin; i < begin + length; i++) {
            double d = values[i] - m;
            accum += d * d;
            accum2 += d;
        }
        double variance = (accum - ((accum2 * accum2) / ((double) length))) / ((double) (length - 1));
        double accum3 = 0.0d;
        for (int i2 = begin; i2 < begin + length; i2++) {
            double d2 = values[i2] - m;
            accum3 += d2 * d2 * d2;
        }
        double n0 = length;
        double skew = (n0 / ((n0 - 1.0d) * (n0 - 2.0d))) * (accum3 / (org.apache.commons.math.util.FastMath.sqrt(variance) * variance));
        return skew;
    }

    @Override // org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math.stat.descriptive.AbstractUnivariateStatistic, org.apache.commons.math.stat.descriptive.UnivariateStatistic, org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic
    public org.apache.commons.math.stat.descriptive.moment.Skewness copy() {
        org.apache.commons.math.stat.descriptive.moment.Skewness result = new org.apache.commons.math.stat.descriptive.moment.Skewness();
        copy(this, result);
        return result;
    }

    public static void copy(org.apache.commons.math.stat.descriptive.moment.Skewness source, org.apache.commons.math.stat.descriptive.moment.Skewness dest) {
        dest.setData(source.getDataRef());
        dest.moment = new org.apache.commons.math.stat.descriptive.moment.ThirdMoment(source.moment.copy());
        dest.incMoment = source.incMoment;
    }
}
