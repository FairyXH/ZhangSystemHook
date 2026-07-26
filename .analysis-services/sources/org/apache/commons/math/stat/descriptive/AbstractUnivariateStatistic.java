package org.apache.commons.math.stat.descriptive;

/* JADX INFO: loaded from: classes4.dex */
public abstract class AbstractUnivariateStatistic implements org.apache.commons.math.stat.descriptive.UnivariateStatistic {
    private double[] storedData;

    @Override // org.apache.commons.math.stat.descriptive.UnivariateStatistic, org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic
    public abstract org.apache.commons.math.stat.descriptive.UnivariateStatistic copy();

    @Override // org.apache.commons.math.stat.descriptive.UnivariateStatistic
    public abstract double evaluate(double[] dArr, int i, int i2);

    public void setData(double[] values) {
        this.storedData = values == null ? null : (double[]) values.clone();
    }

    public double[] getData() {
        if (this.storedData == null) {
            return null;
        }
        return (double[]) this.storedData.clone();
    }

    protected double[] getDataRef() {
        return this.storedData;
    }

    public void setData(double[] values, int begin, int length) {
        this.storedData = new double[length];
        java.lang.System.arraycopy(values, begin, this.storedData, 0, length);
    }

    public double evaluate() {
        return evaluate(this.storedData);
    }

    @Override // org.apache.commons.math.stat.descriptive.UnivariateStatistic
    public double evaluate(double[] values) {
        test(values, 0, 0);
        return evaluate(values, 0, values.length);
    }

    protected boolean test(double[] values, int begin, int length) {
        if (values == null) {
            throw new org.apache.commons.math.exception.NullArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.INPUT_ARRAY);
        }
        if (begin < 0) {
            throw new org.apache.commons.math.exception.NotPositiveException(org.apache.commons.math.exception.util.LocalizedFormats.START_POSITION, java.lang.Integer.valueOf(begin));
        }
        if (length < 0) {
            throw new org.apache.commons.math.exception.NotPositiveException(org.apache.commons.math.exception.util.LocalizedFormats.LENGTH, java.lang.Integer.valueOf(length));
        }
        if (begin + length <= values.length) {
            return length != 0;
        }
        throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.SUBARRAY_ENDS_AFTER_ARRAY_END, new java.lang.Object[0]);
    }

    protected boolean test(double[] values, double[] weights, int begin, int length) {
        if (weights == null) {
            throw new org.apache.commons.math.exception.NullArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.INPUT_ARRAY);
        }
        if (weights.length != values.length) {
            throw new org.apache.commons.math.exception.DimensionMismatchException(weights.length, values.length);
        }
        boolean containsPositiveWeight = false;
        for (int i = begin; i < begin + length; i++) {
            if (java.lang.Double.isNaN(weights[i])) {
                throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.NAN_ELEMENT_AT_INDEX, java.lang.Integer.valueOf(i));
            }
            if (java.lang.Double.isInfinite(weights[i])) {
                throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.INFINITE_ARRAY_ELEMENT, java.lang.Double.valueOf(weights[i]), java.lang.Integer.valueOf(i));
            }
            if (weights[i] < 0.0d) {
                throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.NEGATIVE_ELEMENT_AT_INDEX, java.lang.Integer.valueOf(i), java.lang.Double.valueOf(weights[i]));
            }
            if (!containsPositiveWeight && weights[i] > 0.0d) {
                containsPositiveWeight = true;
            }
        }
        if (!containsPositiveWeight) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.WEIGHT_AT_LEAST_ONE_NON_ZERO, new java.lang.Object[0]);
        }
        return test(values, begin, length);
    }
}
