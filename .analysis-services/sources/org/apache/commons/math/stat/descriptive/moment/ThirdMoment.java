package org.apache.commons.math.stat.descriptive.moment;

/* JADX INFO: loaded from: classes4.dex */
public class ThirdMoment extends org.apache.commons.math.stat.descriptive.moment.SecondMoment implements java.io.Serializable {
    private static final long serialVersionUID = -7818711964045118679L;
    protected double m3;
    protected double nDevSq;

    public ThirdMoment() {
        this.m3 = Double.NaN;
        this.nDevSq = Double.NaN;
    }

    public ThirdMoment(org.apache.commons.math.stat.descriptive.moment.ThirdMoment original) {
        copy(original, this);
    }

    @Override // org.apache.commons.math.stat.descriptive.moment.SecondMoment, org.apache.commons.math.stat.descriptive.moment.FirstMoment, org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic
    public void increment(double d) {
        if (this.n < 1) {
            this.m1 = 0.0d;
            this.m2 = 0.0d;
            this.m3 = 0.0d;
        }
        double prevM2 = this.m2;
        super.increment(d);
        this.nDevSq = this.nDev * this.nDev;
        double n0 = this.n;
        this.m3 = (this.m3 - ((this.nDev * 3.0d) * prevM2)) + ((n0 - 1.0d) * (n0 - 2.0d) * this.nDevSq * this.dev);
    }

    @Override // org.apache.commons.math.stat.descriptive.moment.SecondMoment, org.apache.commons.math.stat.descriptive.moment.FirstMoment, org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic
    public double getResult() {
        return this.m3;
    }

    @Override // org.apache.commons.math.stat.descriptive.moment.SecondMoment, org.apache.commons.math.stat.descriptive.moment.FirstMoment, org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic
    public void clear() {
        super.clear();
        this.m3 = Double.NaN;
        this.nDevSq = Double.NaN;
    }

    @Override // org.apache.commons.math.stat.descriptive.moment.SecondMoment, org.apache.commons.math.stat.descriptive.moment.FirstMoment, org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math.stat.descriptive.AbstractUnivariateStatistic, org.apache.commons.math.stat.descriptive.UnivariateStatistic, org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic
    public org.apache.commons.math.stat.descriptive.moment.ThirdMoment copy() {
        org.apache.commons.math.stat.descriptive.moment.ThirdMoment result = new org.apache.commons.math.stat.descriptive.moment.ThirdMoment();
        copy(this, result);
        return result;
    }

    public static void copy(org.apache.commons.math.stat.descriptive.moment.ThirdMoment source, org.apache.commons.math.stat.descriptive.moment.ThirdMoment dest) {
        org.apache.commons.math.stat.descriptive.moment.SecondMoment.copy((org.apache.commons.math.stat.descriptive.moment.SecondMoment) source, (org.apache.commons.math.stat.descriptive.moment.SecondMoment) dest);
        dest.m3 = source.m3;
        dest.nDevSq = source.nDevSq;
    }
}
