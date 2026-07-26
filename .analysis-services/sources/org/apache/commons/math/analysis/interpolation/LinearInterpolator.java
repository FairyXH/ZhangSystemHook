package org.apache.commons.math.analysis.interpolation;

/* JADX INFO: loaded from: classes4.dex */
public class LinearInterpolator implements org.apache.commons.math.analysis.interpolation.UnivariateRealInterpolator {
    @Override // org.apache.commons.math.analysis.interpolation.UnivariateRealInterpolator
    public org.apache.commons.math.analysis.polynomials.PolynomialSplineFunction interpolate(double[] x, double[] y) {
        if (x.length != y.length) {
            throw new org.apache.commons.math.exception.DimensionMismatchException(x.length, y.length);
        }
        if (x.length < 2) {
            throw new org.apache.commons.math.exception.NumberIsTooSmallException(org.apache.commons.math.exception.util.LocalizedFormats.NUMBER_OF_POINTS, java.lang.Integer.valueOf(x.length), 2, true);
        }
        int n = x.length - 1;
        org.apache.commons.math.util.MathUtils.checkOrder(x);
        double[] m = new double[n];
        for (int i = 0; i < n; i++) {
            m[i] = (y[i + 1] - y[i]) / (x[i + 1] - x[i]);
        }
        org.apache.commons.math.analysis.polynomials.PolynomialFunction[] polynomials = new org.apache.commons.math.analysis.polynomials.PolynomialFunction[n];
        double[] coefficients = new double[2];
        for (int i2 = 0; i2 < n; i2++) {
            coefficients[0] = y[i2];
            coefficients[1] = m[i2];
            polynomials[i2] = new org.apache.commons.math.analysis.polynomials.PolynomialFunction(coefficients);
        }
        return new org.apache.commons.math.analysis.polynomials.PolynomialSplineFunction(x, polynomials);
    }
}
