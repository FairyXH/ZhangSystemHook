package org.apache.commons.math.stat.descriptive;

/* JADX INFO: loaded from: classes4.dex */
public abstract class AbstractStorelessUnivariateStatistic extends org.apache.commons.math.stat.descriptive.AbstractUnivariateStatistic implements org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic {
    @Override // org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic
    public abstract void clear();

    @Override // org.apache.commons.math.stat.descriptive.AbstractUnivariateStatistic, org.apache.commons.math.stat.descriptive.UnivariateStatistic, org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic
    public abstract org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic copy();

    @Override // org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic
    public abstract double getResult();

    @Override // org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic
    public abstract void increment(double d);

    @Override // org.apache.commons.math.stat.descriptive.AbstractUnivariateStatistic, org.apache.commons.math.stat.descriptive.UnivariateStatistic
    public double evaluate(double[] values) {
        if (values == null) {
            throw new org.apache.commons.math.exception.NullArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.INPUT_ARRAY);
        }
        return evaluate(values, 0, values.length);
    }

    @Override // org.apache.commons.math.stat.descriptive.AbstractUnivariateStatistic, org.apache.commons.math.stat.descriptive.UnivariateStatistic
    public double evaluate(double[] values, int begin, int length) {
        if (test(values, begin, length)) {
            clear();
            incrementAll(values, begin, length);
        }
        return getResult();
    }

    @Override // org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic
    public void incrementAll(double[] values) {
        if (values == null) {
            throw new org.apache.commons.math.exception.NullArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.INPUT_ARRAY);
        }
        incrementAll(values, 0, values.length);
    }

    @Override // org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic
    public void incrementAll(double[] values, int begin, int length) {
        if (test(values, begin, length)) {
            int k = begin + length;
            for (int i = begin; i < k; i++) {
                increment(values[i]);
            }
        }
    }

    public boolean equals(java.lang.Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic)) {
            return false;
        }
        org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic stat = (org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic) object;
        return org.apache.commons.math.util.MathUtils.equalsIncludingNaN(stat.getResult(), getResult()) && org.apache.commons.math.util.MathUtils.equalsIncludingNaN((float) stat.getN(), (float) getN());
    }

    public int hashCode() {
        return ((org.apache.commons.math.util.MathUtils.hash(getResult()) + 31) * 31) + org.apache.commons.math.util.MathUtils.hash(getN());
    }
}
