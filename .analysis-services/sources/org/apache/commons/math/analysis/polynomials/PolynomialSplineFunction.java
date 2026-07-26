package org.apache.commons.math.analysis.polynomials;

/* JADX INFO: loaded from: classes4.dex */
public class PolynomialSplineFunction implements org.apache.commons.math.analysis.DifferentiableUnivariateRealFunction {
    private final double[] knots;
    private final int n;
    private final org.apache.commons.math.analysis.polynomials.PolynomialFunction[] polynomials;

    public PolynomialSplineFunction(double[] knots, org.apache.commons.math.analysis.polynomials.PolynomialFunction[] polynomials) {
        if (knots.length < 2) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.NOT_ENOUGH_POINTS_IN_SPLINE_PARTITION, 2, java.lang.Integer.valueOf(knots.length));
        }
        if (knots.length - 1 != polynomials.length) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.POLYNOMIAL_INTERPOLANTS_MISMATCH_SEGMENTS, java.lang.Integer.valueOf(polynomials.length), java.lang.Integer.valueOf(knots.length));
        }
        if (!isStrictlyIncreasing(knots)) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.NOT_STRICTLY_INCREASING_KNOT_VALUES, new java.lang.Object[0]);
        }
        this.n = knots.length - 1;
        this.knots = new double[this.n + 1];
        java.lang.System.arraycopy(knots, 0, this.knots, 0, this.n + 1);
        this.polynomials = new org.apache.commons.math.analysis.polynomials.PolynomialFunction[this.n];
        java.lang.System.arraycopy(polynomials, 0, this.polynomials, 0, this.n);
    }

    @Override // org.apache.commons.math.analysis.UnivariateRealFunction
    public double value(double v) throws org.apache.commons.math.ArgumentOutsideDomainException {
        if (v < this.knots[0] || v > this.knots[this.n]) {
            throw new org.apache.commons.math.ArgumentOutsideDomainException(v, this.knots[0], this.knots[this.n]);
        }
        int i = java.util.Arrays.binarySearch(this.knots, v);
        if (i < 0) {
            i = (-i) - 2;
        }
        if (i >= this.polynomials.length) {
            i--;
        }
        return this.polynomials[i].value(v - this.knots[i]);
    }

    @Override // org.apache.commons.math.analysis.DifferentiableUnivariateRealFunction
    public org.apache.commons.math.analysis.UnivariateRealFunction derivative() {
        return polynomialSplineDerivative();
    }

    public org.apache.commons.math.analysis.polynomials.PolynomialSplineFunction polynomialSplineDerivative() {
        org.apache.commons.math.analysis.polynomials.PolynomialFunction[] derivativePolynomials = new org.apache.commons.math.analysis.polynomials.PolynomialFunction[this.n];
        for (int i = 0; i < this.n; i++) {
            derivativePolynomials[i] = this.polynomials[i].polynomialDerivative();
        }
        return new org.apache.commons.math.analysis.polynomials.PolynomialSplineFunction(this.knots, derivativePolynomials);
    }

    public int getN() {
        return this.n;
    }

    public org.apache.commons.math.analysis.polynomials.PolynomialFunction[] getPolynomials() {
        org.apache.commons.math.analysis.polynomials.PolynomialFunction[] p = new org.apache.commons.math.analysis.polynomials.PolynomialFunction[this.n];
        java.lang.System.arraycopy(this.polynomials, 0, p, 0, this.n);
        return p;
    }

    public double[] getKnots() {
        double[] out = new double[this.n + 1];
        java.lang.System.arraycopy(this.knots, 0, out, 0, this.n + 1);
        return out;
    }

    private static boolean isStrictlyIncreasing(double[] x) {
        for (int i = 1; i < x.length; i++) {
            if (x[i - 1] >= x[i]) {
                return false;
            }
        }
        return true;
    }
}
