package org.apache.commons.math.analysis.polynomials;

/* JADX INFO: loaded from: classes4.dex */
public class PolynomialFunctionLagrangeForm implements org.apache.commons.math.analysis.UnivariateRealFunction {
    private double[] coefficients;
    private boolean coefficientsComputed;
    private final double[] x;
    private final double[] y;

    public PolynomialFunctionLagrangeForm(double[] x, double[] y) throws java.lang.IllegalArgumentException {
        verifyInterpolationArray(x, y);
        this.x = new double[x.length];
        this.y = new double[y.length];
        java.lang.System.arraycopy(x, 0, this.x, 0, x.length);
        java.lang.System.arraycopy(y, 0, this.y, 0, y.length);
        this.coefficientsComputed = false;
    }

    @Override // org.apache.commons.math.analysis.UnivariateRealFunction
    public double value(double z) throws org.apache.commons.math.FunctionEvaluationException {
        try {
            return evaluate(this.x, this.y, z);
        } catch (org.apache.commons.math.DuplicateSampleAbscissaException e) {
            throw new org.apache.commons.math.FunctionEvaluationException(z, e.getSpecificPattern(), e.getGeneralPattern(), e.getArguments());
        }
    }

    public int degree() {
        return this.x.length - 1;
    }

    public double[] getInterpolatingPoints() {
        double[] out = new double[this.x.length];
        java.lang.System.arraycopy(this.x, 0, out, 0, this.x.length);
        return out;
    }

    public double[] getInterpolatingValues() {
        double[] out = new double[this.y.length];
        java.lang.System.arraycopy(this.y, 0, out, 0, this.y.length);
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

    public static double evaluate(double[] x, double[] y, double z) throws org.apache.commons.math.DuplicateSampleAbscissaException, java.lang.IllegalArgumentException {
        double d;
        verifyInterpolationArray(x, y);
        int nearest = 0;
        int n = x.length;
        double[] c = new double[n];
        double[] d2 = new double[n];
        double min_dist = Double.POSITIVE_INFINITY;
        for (int i = 0; i < n; i++) {
            c[i] = y[i];
            d2[i] = y[i];
            double dist = org.apache.commons.math.util.FastMath.abs(z - x[i]);
            if (dist < min_dist) {
                nearest = i;
                min_dist = dist;
            }
        }
        double value = y[nearest];
        int i2 = 1;
        while (i2 < n) {
            for (int j = 0; j < n - i2; j++) {
                double tc = x[j] - z;
                double td = x[i2 + j] - z;
                double divider = x[j] - x[i2 + j];
                if (divider == 0.0d) {
                    double tc2 = x[i2];
                    throw new org.apache.commons.math.DuplicateSampleAbscissaException(tc2, i2, i2 + j);
                }
                double w = (c[j + 1] - d2[j]) / divider;
                c[j] = tc * w;
                d2[j] = td * w;
            }
            double min_dist2 = min_dist;
            double min_dist3 = nearest;
            if (min_dist3 < ((double) ((n - i2) + 1)) * 0.5d) {
                d = c[nearest];
            } else {
                nearest--;
                d = d2[nearest];
            }
            value += d;
            i2++;
            min_dist = min_dist2;
        }
        return value;
    }

    protected void computeCoefficients() throws java.lang.ArithmeticException {
        double d;
        int n = degree() + 1;
        this.coefficients = new double[n];
        int i = 0;
        while (true) {
            d = 0.0d;
            if (i >= n) {
                break;
            }
            this.coefficients[i] = 0.0d;
            i++;
        }
        int i2 = n + 1;
        double[] c = new double[i2];
        c[0] = 1.0d;
        for (int i3 = 0; i3 < n; i3++) {
            for (int j = i3; j > 0; j--) {
                c[j] = c[j - 1] - (c[j] * this.x[i3]);
            }
            c[0] = c[0] * (-this.x[i3]);
            c[i3 + 1] = 1.0d;
        }
        double[] tc = new double[n];
        int i4 = 0;
        while (i4 < n) {
            double d2 = 1.0d;
            for (int j2 = 0; j2 < n; j2++) {
                if (i4 != j2) {
                    d2 *= this.x[i4] - this.x[j2];
                }
            }
            if (d2 == d) {
                for (int k = 0; k < n; k++) {
                    if (i4 != k && this.x[i4] == this.x[k]) {
                        throw org.apache.commons.math.MathRuntimeException.createArithmeticException(org.apache.commons.math.exception.util.LocalizedFormats.IDENTICAL_ABSCISSAS_DIVISION_BY_ZERO, java.lang.Integer.valueOf(i4), java.lang.Integer.valueOf(k), java.lang.Double.valueOf(this.x[i4]));
                    }
                }
            }
            double t = this.y[i4] / d2;
            tc[n - 1] = c[n];
            double[] dArr = this.coefficients;
            int i5 = n - 1;
            dArr[i5] = dArr[i5] + (tc[n - 1] * t);
            for (int j3 = n - 2; j3 >= 0; j3--) {
                tc[j3] = c[j3 + 1] + (tc[j3 + 1] * this.x[i4]);
                double[] dArr2 = this.coefficients;
                dArr2[j3] = dArr2[j3] + (tc[j3] * t);
            }
            i4++;
            d = 0.0d;
        }
        this.coefficientsComputed = true;
    }

    public static void verifyInterpolationArray(double[] x, double[] y) throws java.lang.IllegalArgumentException {
        if (x.length != y.length) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.DIMENSIONS_MISMATCH_SIMPLE, java.lang.Integer.valueOf(x.length), java.lang.Integer.valueOf(y.length));
        }
        if (x.length < 2) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.WRONG_NUMBER_OF_POINTS, 2, java.lang.Integer.valueOf(x.length));
        }
    }
}
