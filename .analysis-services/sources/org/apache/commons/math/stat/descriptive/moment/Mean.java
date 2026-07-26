package org.apache.commons.math.stat.descriptive.moment;

/* JADX INFO: loaded from: classes4.dex */
public class Mean extends org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic implements java.io.Serializable, org.apache.commons.math.stat.descriptive.WeightedEvaluation {
    private static final long serialVersionUID = -1296043746617791564L;
    protected boolean incMoment;
    protected org.apache.commons.math.stat.descriptive.moment.FirstMoment moment;

    public Mean() {
        this.incMoment = true;
        this.moment = new org.apache.commons.math.stat.descriptive.moment.FirstMoment();
    }

    public Mean(org.apache.commons.math.stat.descriptive.moment.FirstMoment m1) {
        this.moment = m1;
        this.incMoment = false;
    }

    public Mean(org.apache.commons.math.stat.descriptive.moment.Mean original) {
        copy(original, this);
    }

    @Override // org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic
    public void increment(double d) {
        if (this.incMoment) {
            this.moment.increment(d);
        }
    }

    @Override // org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic
    public void clear() {
        if (this.incMoment) {
            this.moment.clear();
        }
    }

    @Override // org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic
    public double getResult() {
        return this.moment.m1;
    }

    @Override // org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic
    public long getN() {
        return this.moment.getN();
    }

    @Override // org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math.stat.descriptive.AbstractUnivariateStatistic, org.apache.commons.math.stat.descriptive.UnivariateStatistic
    public double evaluate(double[] values, int begin, int length) {
        if (test(values, begin, length)) {
            org.apache.commons.math.stat.descriptive.summary.Sum sum = new org.apache.commons.math.stat.descriptive.summary.Sum();
            double sampleSize = length;
            double xbar = sum.evaluate(values, begin, length) / sampleSize;
            double correction = 0.0d;
            for (int i = begin; i < begin + length; i++) {
                correction += values[i] - xbar;
            }
            return (correction / sampleSize) + xbar;
        }
        return Double.NaN;
    }

    @Override // org.apache.commons.math.stat.descriptive.WeightedEvaluation
    public double evaluate(double[] values, double[] weights, int begin, int length) {
        if (test(values, weights, begin, length)) {
            org.apache.commons.math.stat.descriptive.summary.Sum sum = new org.apache.commons.math.stat.descriptive.summary.Sum();
            double sumw = sum.evaluate(weights, begin, length);
            double xbarw = sum.evaluate(values, weights, begin, length) / sumw;
            double correction = 0.0d;
            for (int i = begin; i < begin + length; i++) {
                correction += weights[i] * (values[i] - xbarw);
            }
            return (correction / sumw) + xbarw;
        }
        return Double.NaN;
    }

    @Override // org.apache.commons.math.stat.descriptive.WeightedEvaluation
    public double evaluate(double[] values, double[] weights) {
        return evaluate(values, weights, 0, values.length);
    }

    @Override // org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math.stat.descriptive.AbstractUnivariateStatistic, org.apache.commons.math.stat.descriptive.UnivariateStatistic, org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic
    public org.apache.commons.math.stat.descriptive.moment.Mean copy() {
        org.apache.commons.math.stat.descriptive.moment.Mean result = new org.apache.commons.math.stat.descriptive.moment.Mean();
        copy(this, result);
        return result;
    }

    public static void copy(org.apache.commons.math.stat.descriptive.moment.Mean source, org.apache.commons.math.stat.descriptive.moment.Mean dest) {
        dest.setData(source.getDataRef());
        dest.incMoment = source.incMoment;
        dest.moment = source.moment.copy();
    }
}
