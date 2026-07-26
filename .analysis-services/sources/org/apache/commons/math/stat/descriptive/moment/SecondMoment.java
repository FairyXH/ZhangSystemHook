package org.apache.commons.math.stat.descriptive.moment;

/* JADX INFO: loaded from: classes4.dex */
public class SecondMoment extends org.apache.commons.math.stat.descriptive.moment.FirstMoment implements java.io.Serializable {
    private static final long serialVersionUID = 3942403127395076445L;
    protected double m2;

    public SecondMoment() {
        this.m2 = Double.NaN;
    }

    public SecondMoment(org.apache.commons.math.stat.descriptive.moment.SecondMoment original) {
        super(original);
        this.m2 = original.m2;
    }

    @Override // org.apache.commons.math.stat.descriptive.moment.FirstMoment, org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic
    public void increment(double d) {
        if (this.n < 1) {
            this.m2 = 0.0d;
            this.m1 = 0.0d;
        }
        super.increment(d);
        this.m2 += (this.n - 1.0d) * this.dev * this.nDev;
    }

    @Override // org.apache.commons.math.stat.descriptive.moment.FirstMoment, org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic
    public void clear() {
        super.clear();
        this.m2 = Double.NaN;
    }

    @Override // org.apache.commons.math.stat.descriptive.moment.FirstMoment, org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic
    public double getResult() {
        return this.m2;
    }

    @Override // org.apache.commons.math.stat.descriptive.moment.FirstMoment, org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math.stat.descriptive.AbstractUnivariateStatistic, org.apache.commons.math.stat.descriptive.UnivariateStatistic, org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic
    public org.apache.commons.math.stat.descriptive.moment.SecondMoment copy() {
        org.apache.commons.math.stat.descriptive.moment.SecondMoment result = new org.apache.commons.math.stat.descriptive.moment.SecondMoment();
        copy(this, result);
        return result;
    }

    public static void copy(org.apache.commons.math.stat.descriptive.moment.SecondMoment source, org.apache.commons.math.stat.descriptive.moment.SecondMoment dest) {
        org.apache.commons.math.stat.descriptive.moment.FirstMoment.copy(source, dest);
        dest.m2 = source.m2;
    }
}
