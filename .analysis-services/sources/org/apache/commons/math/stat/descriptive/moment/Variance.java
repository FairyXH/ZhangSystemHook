package org.apache.commons.math.stat.descriptive.moment;

/* JADX INFO: loaded from: classes4.dex */
public class Variance extends org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic implements java.io.Serializable, org.apache.commons.math.stat.descriptive.WeightedEvaluation {
    private static final long serialVersionUID = -9111962718267217978L;
    protected boolean incMoment;
    private boolean isBiasCorrected;
    protected org.apache.commons.math.stat.descriptive.moment.SecondMoment moment;

    public Variance() {
        this.moment = null;
        this.incMoment = true;
        this.isBiasCorrected = true;
        this.moment = new org.apache.commons.math.stat.descriptive.moment.SecondMoment();
    }

    public Variance(org.apache.commons.math.stat.descriptive.moment.SecondMoment m2) {
        this.moment = null;
        this.incMoment = true;
        this.isBiasCorrected = true;
        this.incMoment = false;
        this.moment = m2;
    }

    public Variance(boolean isBiasCorrected) {
        this.moment = null;
        this.incMoment = true;
        this.isBiasCorrected = true;
        this.moment = new org.apache.commons.math.stat.descriptive.moment.SecondMoment();
        this.isBiasCorrected = isBiasCorrected;
    }

    public Variance(boolean isBiasCorrected, org.apache.commons.math.stat.descriptive.moment.SecondMoment m2) {
        this.moment = null;
        this.incMoment = true;
        this.isBiasCorrected = true;
        this.incMoment = false;
        this.moment = m2;
        this.isBiasCorrected = isBiasCorrected;
    }

    public Variance(org.apache.commons.math.stat.descriptive.moment.Variance original) {
        this.moment = null;
        this.incMoment = true;
        this.isBiasCorrected = true;
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
        if (this.moment.n == 0) {
            return Double.NaN;
        }
        if (this.moment.n == 1) {
            return 0.0d;
        }
        if (this.isBiasCorrected) {
            return this.moment.m2 / (this.moment.n - 1.0d);
        }
        return this.moment.m2 / this.moment.n;
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
    public double evaluate(double[] values) {
        if (values == null) {
            throw new org.apache.commons.math.exception.NullArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.INPUT_ARRAY);
        }
        return evaluate(values, 0, values.length);
    }

    @Override // org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math.stat.descriptive.AbstractUnivariateStatistic, org.apache.commons.math.stat.descriptive.UnivariateStatistic
    public double evaluate(double[] values, int begin, int length) {
        if (!test(values, begin, length)) {
            return Double.NaN;
        }
        clear();
        if (length == 1) {
            return 0.0d;
        }
        if (length <= 1) {
            return Double.NaN;
        }
        org.apache.commons.math.stat.descriptive.moment.Mean mean = new org.apache.commons.math.stat.descriptive.moment.Mean();
        double m = mean.evaluate(values, begin, length);
        double var = evaluate(values, m, begin, length);
        return var;
    }

    @Override // org.apache.commons.math.stat.descriptive.WeightedEvaluation
    public double evaluate(double[] values, double[] weights, int begin, int length) {
        if (!test(values, weights, begin, length)) {
            return Double.NaN;
        }
        clear();
        if (length == 1) {
            return 0.0d;
        }
        if (length <= 1) {
            return Double.NaN;
        }
        org.apache.commons.math.stat.descriptive.moment.Mean mean = new org.apache.commons.math.stat.descriptive.moment.Mean();
        double m = mean.evaluate(values, weights, begin, length);
        double var = evaluate(values, weights, m, begin, length);
        return var;
    }

    @Override // org.apache.commons.math.stat.descriptive.WeightedEvaluation
    public double evaluate(double[] values, double[] weights) {
        return evaluate(values, weights, 0, values.length);
    }

    public double evaluate(double[] values, double mean, int begin, int length) {
        if (!test(values, begin, length)) {
            return Double.NaN;
        }
        if (length == 1) {
            return 0.0d;
        }
        if (length <= 1) {
            return Double.NaN;
        }
        double accum = 0.0d;
        double accum2 = 0.0d;
        for (int i = begin; i < begin + length; i++) {
            double dev = values[i] - mean;
            accum += dev * dev;
            accum2 += dev;
        }
        double len = length;
        if (this.isBiasCorrected) {
            double var = (accum - ((accum2 * accum2) / len)) / (len - 1.0d);
            return var;
        }
        double var2 = (accum - ((accum2 * accum2) / len)) / len;
        return var2;
    }

    public double evaluate(double[] values, double mean) {
        return evaluate(values, mean, 0, values.length);
    }

    public double evaluate(double[] values, double[] weights, double mean, int begin, int length) {
        if (!test(values, weights, begin, length)) {
            return Double.NaN;
        }
        if (length == 1) {
            return 0.0d;
        }
        if (length <= 1) {
            return Double.NaN;
        }
        double accum = 0.0d;
        double accum2 = 0.0d;
        for (int i = begin; i < begin + length; i++) {
            double dev = values[i] - mean;
            accum += weights[i] * dev * dev;
            accum2 += weights[i] * dev;
        }
        double sumWts = 0.0d;
        for (double d : weights) {
            sumWts += d;
        }
        if (this.isBiasCorrected) {
            double var = (accum - ((accum2 * accum2) / sumWts)) / (sumWts - 1.0d);
            return var;
        }
        double var2 = (accum - ((accum2 * accum2) / sumWts)) / sumWts;
        return var2;
    }

    public double evaluate(double[] values, double[] weights, double mean) {
        return evaluate(values, weights, mean, 0, values.length);
    }

    public boolean isBiasCorrected() {
        return this.isBiasCorrected;
    }

    public void setBiasCorrected(boolean biasCorrected) {
        this.isBiasCorrected = biasCorrected;
    }

    @Override // org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math.stat.descriptive.AbstractUnivariateStatistic, org.apache.commons.math.stat.descriptive.UnivariateStatistic, org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic
    public org.apache.commons.math.stat.descriptive.moment.Variance copy() {
        org.apache.commons.math.stat.descriptive.moment.Variance result = new org.apache.commons.math.stat.descriptive.moment.Variance();
        copy(this, result);
        return result;
    }

    public static void copy(org.apache.commons.math.stat.descriptive.moment.Variance source, org.apache.commons.math.stat.descriptive.moment.Variance dest) {
        if (source == null || dest == null) {
            throw new org.apache.commons.math.exception.NullArgumentException();
        }
        dest.setData(source.getDataRef());
        dest.moment = source.moment.copy();
        dest.isBiasCorrected = source.isBiasCorrected;
        dest.incMoment = source.incMoment;
    }
}
