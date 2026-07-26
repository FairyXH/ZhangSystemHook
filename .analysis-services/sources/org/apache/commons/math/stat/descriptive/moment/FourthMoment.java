package org.apache.commons.math.stat.descriptive.moment;

/* JADX INFO: loaded from: classes4.dex */
public class FourthMoment extends org.apache.commons.math.stat.descriptive.moment.ThirdMoment implements java.io.Serializable {
    private static final long serialVersionUID = 4763990447117157611L;
    protected double m4;

    public FourthMoment() {
        this.m4 = Double.NaN;
    }

    public FourthMoment(org.apache.commons.math.stat.descriptive.moment.FourthMoment original) {
        copy(original, this);
    }

    @Override // org.apache.commons.math.stat.descriptive.moment.ThirdMoment, org.apache.commons.math.stat.descriptive.moment.SecondMoment, org.apache.commons.math.stat.descriptive.moment.FirstMoment, org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic
    public void increment(double d) {
        if (this.n < 1) {
            this.m4 = 0.0d;
            this.m3 = 0.0d;
            this.m2 = 0.0d;
            this.m1 = 0.0d;
        }
        double prevM3 = this.m3;
        double prevM2 = this.m2;
        super.increment(d);
        double n0 = this.n;
        double d2 = (this.m4 - ((this.nDev * 4.0d) * prevM3)) + (this.nDevSq * 6.0d * prevM2);
        double d3 = this.nDevSq;
        double prevM32 = this.nDevSq;
        this.m4 = d2 + (((n0 * n0) - ((n0 - 1.0d) * 3.0d)) * d3 * prevM32 * (n0 - 1.0d) * n0);
    }

    @Override // org.apache.commons.math.stat.descriptive.moment.ThirdMoment, org.apache.commons.math.stat.descriptive.moment.SecondMoment, org.apache.commons.math.stat.descriptive.moment.FirstMoment, org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic
    public double getResult() {
        return this.m4;
    }

    @Override // org.apache.commons.math.stat.descriptive.moment.ThirdMoment, org.apache.commons.math.stat.descriptive.moment.SecondMoment, org.apache.commons.math.stat.descriptive.moment.FirstMoment, org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic
    public void clear() {
        super.clear();
        this.m4 = Double.NaN;
    }

    @Override // org.apache.commons.math.stat.descriptive.moment.ThirdMoment, org.apache.commons.math.stat.descriptive.moment.SecondMoment, org.apache.commons.math.stat.descriptive.moment.FirstMoment, org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math.stat.descriptive.AbstractUnivariateStatistic, org.apache.commons.math.stat.descriptive.UnivariateStatistic, org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic
    public org.apache.commons.math.stat.descriptive.moment.FourthMoment copy() {
        org.apache.commons.math.stat.descriptive.moment.FourthMoment result = new org.apache.commons.math.stat.descriptive.moment.FourthMoment();
        copy(this, result);
        return result;
    }

    public static void copy(org.apache.commons.math.stat.descriptive.moment.FourthMoment source, org.apache.commons.math.stat.descriptive.moment.FourthMoment dest) {
        org.apache.commons.math.stat.descriptive.moment.ThirdMoment.copy((org.apache.commons.math.stat.descriptive.moment.ThirdMoment) source, (org.apache.commons.math.stat.descriptive.moment.ThirdMoment) dest);
        dest.m4 = source.m4;
    }
}
