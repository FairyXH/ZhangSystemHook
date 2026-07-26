package org.apache.commons.math.analysis.interpolation;

/* JADX INFO: compiled from: BicubicSplineInterpolatingFunction.java */
/* JADX INFO: loaded from: classes4.dex */
class BicubicSplineFunction implements org.apache.commons.math.analysis.BivariateRealFunction {
    private static final short N = 4;
    private final double[][] a = (double[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.Double.TYPE, 4, 4);
    private org.apache.commons.math.analysis.BivariateRealFunction partialDerivativeX;
    private org.apache.commons.math.analysis.BivariateRealFunction partialDerivativeXX;
    private org.apache.commons.math.analysis.BivariateRealFunction partialDerivativeXY;
    private org.apache.commons.math.analysis.BivariateRealFunction partialDerivativeY;
    private org.apache.commons.math.analysis.BivariateRealFunction partialDerivativeYY;

    public BicubicSplineFunction(double[] a) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                this.a[i][j] = a[(j * 4) + i];
            }
        }
    }

    @Override // org.apache.commons.math.analysis.BivariateRealFunction
    public double value(double x, double y) {
        if (x < 0.0d || x > 1.0d) {
            throw new org.apache.commons.math.exception.OutOfRangeException(java.lang.Double.valueOf(x), 0, 1);
        }
        if (y < 0.0d || y > 1.0d) {
            throw new org.apache.commons.math.exception.OutOfRangeException(java.lang.Double.valueOf(y), 0, 1);
        }
        double x2 = x * x;
        double x3 = x2 * x;
        double[] pX = {1.0d, x, x2, x3};
        double y2 = y * y;
        double y3 = y2 * y;
        double[] pY = {1.0d, y, y2, y3};
        return apply(pX, pY, this.a);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public double apply(double[] pX, double[] pY, double[][] coeff) {
        double result = 0.0d;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                result += coeff[i][j] * pX[i] * pY[j];
            }
        }
        return result;
    }

    public org.apache.commons.math.analysis.BivariateRealFunction partialDerivativeX() {
        if (this.partialDerivativeX == null) {
            computePartialDerivatives();
        }
        return this.partialDerivativeX;
    }

    public org.apache.commons.math.analysis.BivariateRealFunction partialDerivativeY() {
        if (this.partialDerivativeY == null) {
            computePartialDerivatives();
        }
        return this.partialDerivativeY;
    }

    public org.apache.commons.math.analysis.BivariateRealFunction partialDerivativeXX() {
        if (this.partialDerivativeXX == null) {
            computePartialDerivatives();
        }
        return this.partialDerivativeXX;
    }

    public org.apache.commons.math.analysis.BivariateRealFunction partialDerivativeYY() {
        if (this.partialDerivativeYY == null) {
            computePartialDerivatives();
        }
        return this.partialDerivativeYY;
    }

    public org.apache.commons.math.analysis.BivariateRealFunction partialDerivativeXY() {
        if (this.partialDerivativeXY == null) {
            computePartialDerivatives();
        }
        return this.partialDerivativeXY;
    }

    private void computePartialDerivatives() {
        final double[][] aX = (double[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.Double.TYPE, 4, 4);
        final double[][] aY = (double[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.Double.TYPE, 4, 4);
        final double[][] aXX = (double[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.Double.TYPE, 4, 4);
        final double[][] aYY = (double[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.Double.TYPE, 4, 4);
        final double[][] aXY = (double[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.Double.TYPE, 4, 4);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                double c = this.a[i][j];
                aX[i][j] = ((double) i) * c;
                aY[i][j] = ((double) j) * c;
                aXX[i][j] = ((double) (i - 1)) * aX[i][j];
                aYY[i][j] = ((double) (j - 1)) * aY[i][j];
                aXY[i][j] = ((double) j) * aX[i][j];
            }
        }
        this.partialDerivativeX = new org.apache.commons.math.analysis.BivariateRealFunction() { // from class: org.apache.commons.math.analysis.interpolation.BicubicSplineFunction.1
            @Override // org.apache.commons.math.analysis.BivariateRealFunction
            public double value(double x, double y) {
                double x2 = x * x;
                double[] pX = {0.0d, 1.0d, x, x2};
                double y2 = y * y;
                double y3 = y2 * y;
                double[] pY = {1.0d, y, y2, y3};
                return org.apache.commons.math.analysis.interpolation.BicubicSplineFunction.this.apply(pX, pY, aX);
            }
        };
        this.partialDerivativeY = new org.apache.commons.math.analysis.BivariateRealFunction() { // from class: org.apache.commons.math.analysis.interpolation.BicubicSplineFunction.2
            @Override // org.apache.commons.math.analysis.BivariateRealFunction
            public double value(double x, double y) {
                double x2 = x * x;
                double x3 = x2 * x;
                double[] pX = {1.0d, x, x2, x3};
                double y2 = y * y;
                double[] pY = {0.0d, 1.0d, y, y2};
                return org.apache.commons.math.analysis.interpolation.BicubicSplineFunction.this.apply(pX, pY, aY);
            }
        };
        this.partialDerivativeXX = new org.apache.commons.math.analysis.BivariateRealFunction() { // from class: org.apache.commons.math.analysis.interpolation.BicubicSplineFunction.3
            @Override // org.apache.commons.math.analysis.BivariateRealFunction
            public double value(double x, double y) {
                double[] pX = {0.0d, 0.0d, 1.0d, x};
                double y2 = y * y;
                double y3 = y2 * y;
                double[] pY = {1.0d, y, y2, y3};
                return org.apache.commons.math.analysis.interpolation.BicubicSplineFunction.this.apply(pX, pY, aXX);
            }
        };
        this.partialDerivativeYY = new org.apache.commons.math.analysis.BivariateRealFunction() { // from class: org.apache.commons.math.analysis.interpolation.BicubicSplineFunction.4
            @Override // org.apache.commons.math.analysis.BivariateRealFunction
            public double value(double x, double y) {
                double x2 = x * x;
                double x3 = x2 * x;
                double[] pX = {1.0d, x, x2, x3};
                double[] pY = {0.0d, 0.0d, 1.0d, y};
                return org.apache.commons.math.analysis.interpolation.BicubicSplineFunction.this.apply(pX, pY, aYY);
            }
        };
        this.partialDerivativeXY = new org.apache.commons.math.analysis.BivariateRealFunction() { // from class: org.apache.commons.math.analysis.interpolation.BicubicSplineFunction.5
            @Override // org.apache.commons.math.analysis.BivariateRealFunction
            public double value(double x, double y) {
                double x2 = x * x;
                double[] pX = {0.0d, 1.0d, x, x2};
                double y2 = y * y;
                double[] pY = {0.0d, 1.0d, y, y2};
                return org.apache.commons.math.analysis.interpolation.BicubicSplineFunction.this.apply(pX, pY, aXY);
            }
        };
    }
}
