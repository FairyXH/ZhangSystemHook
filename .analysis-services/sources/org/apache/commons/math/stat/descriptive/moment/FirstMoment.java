package org.apache.commons.math.stat.descriptive.moment;

/* JADX INFO: loaded from: classes4.dex */
public class FirstMoment extends org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic implements java.io.Serializable {
    private static final long serialVersionUID = 6112755307178490473L;
    protected double dev;
    protected double m1;
    protected long n;
    protected double nDev;

    public FirstMoment() {
        this.n = 0L;
        this.m1 = Double.NaN;
        this.dev = Double.NaN;
        this.nDev = Double.NaN;
    }

    public FirstMoment(org.apache.commons.math.stat.descriptive.moment.FirstMoment original) {
        copy(original, this);
    }

    @Override // org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic
    public void increment(double d) {
        if (this.n == 0) {
            this.m1 = 0.0d;
        }
        this.n++;
        double n0 = this.n;
        this.dev = d - this.m1;
        this.nDev = this.dev / n0;
        this.m1 += this.nDev;
    }

    @Override // org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic
    public void clear() {
        this.m1 = Double.NaN;
        this.n = 0L;
        this.dev = Double.NaN;
        this.nDev = Double.NaN;
    }

    @Override // org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic
    public double getResult() {
        return this.m1;
    }

    @Override // org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic
    public long getN() {
        return this.n;
    }

    @Override // org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math.stat.descriptive.AbstractUnivariateStatistic, org.apache.commons.math.stat.descriptive.UnivariateStatistic, org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic
    public org.apache.commons.math.stat.descriptive.moment.FirstMoment copy() {
        org.apache.commons.math.stat.descriptive.moment.FirstMoment result = new org.apache.commons.math.stat.descriptive.moment.FirstMoment();
        copy(this, result);
        return result;
    }

    public static void copy(org.apache.commons.math.stat.descriptive.moment.FirstMoment source, org.apache.commons.math.stat.descriptive.moment.FirstMoment dest) {
        dest.setData(source.getDataRef());
        dest.n = source.n;
        dest.m1 = source.m1;
        dest.dev = source.dev;
        dest.nDev = source.nDev;
    }
}
