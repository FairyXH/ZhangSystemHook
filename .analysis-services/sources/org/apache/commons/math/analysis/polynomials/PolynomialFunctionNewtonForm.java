package org.apache.commons.math.analysis.polynomials;

/* JADX INFO: loaded from: classes4.dex */
public class PolynomialFunctionNewtonForm implements org.apache.commons.math.analysis.UnivariateRealFunction {
    private final double[] a;
    private final double[] c;
    private double[] coefficients;
    private boolean coefficientsComputed;

    public PolynomialFunctionNewtonForm(double[] a, double[] c) throws java.lang.IllegalArgumentException {
        verifyInputArray(a, c);
        this.a = new double[a.length];
        this.c = new double[c.length];
        java.lang.System.arraycopy(a, 0, this.a, 0, a.length);
        java.lang.System.arraycopy(c, 0, this.c, 0, c.length);
        this.coefficientsComputed = false;
    }

    @Override // org.apache.commons.math.analysis.UnivariateRealFunction
    public double value(double z) throws org.apache.commons.math.FunctionEvaluationException {
        return evaluate(this.a, this.c, z);
    }

    public int degree() {
        return this.c.length;
    }

    public double[] getNewtonCoefficients() {
        double[] out = new double[this.a.length];
        java.lang.System.arraycopy(this.a, 0, out, 0, this.a.length);
        return out;
    }

    public double[] getCenters() {
        double[] out = new double[this.c.length];
        java.lang.System.arraycopy(this.c, 0, out, 0, this.c.length);
        return out;
    }

    public double[] getCoefficients() {
        if (!this.coefficientsComputed) {
            computeCoefficients();
        }
        double[] out = new double[this.coefficients.length];
        java.lang.System.arraycopy(this.coefficients, 0, out, 0, this.coefficients.length);
        return out;
    }

    public static double evaluate(double[] a, double[] c, double z) throws org.apache.commons.math.FunctionEvaluationException, java.lang.IllegalArgumentException {
        verifyInputArray(a, c);
        int n = c.length;
        double value = a[n];
        for (int i = n - 1; i >= 0; i--) {
            value = a[i] + ((z - c[i]) * value);
        }
        return value;
    }

    protected void computeCoefficients() {
        int n = degree();
        this.coefficients = new double[n + 1];
        for (int i = 0; i <= n; i++) {
            this.coefficients[i] = 0.0d;
        }
        this.coefficients[0] = this.a[n];
        for (int i2 = n - 1; i2 >= 0; i2--) {
            for (int j = n - i2; j > 0; j--) {
                this.coefficients[j] = this.coefficients[j - 1] - (this.c[i2] * this.coefficients[j]);
            }
            this.coefficients[0] = this.a[i2] - (this.c[i2] * this.coefficients[0]);
        }
        this.coefficientsComputed = true;
    }

    protected static void verifyInputArray(double[] a, double[] c) throws java.lang.IllegalArgumentException {
        if (a.length < 1 || c.length < 1) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.EMPTY_POLYNOMIALS_COEFFICIENTS_ARRAY, new java.lang.Object[0]);
        }
        if (a.length != c.length + 1) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.ARRAY_SIZES_SHOULD_HAVE_DIFFERENCE_1, java.lang.Integer.valueOf(a.length), java.lang.Integer.valueOf(c.length));
        }
    }
}
