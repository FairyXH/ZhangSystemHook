package org.apache.commons.math.analysis.interpolation;

/* JADX INFO: loaded from: classes4.dex */
public class BicubicSplineInterpolatingFunction implements org.apache.commons.math.analysis.BivariateRealFunction {
    private static final double[][] AINV = {new double[]{1.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d}, new double[]{0.0d, 0.0d, 0.0d, 0.0d, 1.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d}, new double[]{-3.0d, 3.0d, 0.0d, 0.0d, -2.0d, -1.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d}, new double[]{2.0d, -2.0d, 0.0d, 0.0d, 1.0d, 1.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d}, new double[]{0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 1.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d}, new double[]{0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 1.0d, 0.0d, 0.0d, 0.0d}, new double[]{0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, -3.0d, 3.0d, 0.0d, 0.0d, -2.0d, -1.0d, 0.0d, 0.0d}, new double[]{0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 2.0d, -2.0d, 0.0d, 0.0d, 1.0d, 1.0d, 0.0d, 0.0d}, new double[]{-3.0d, 0.0d, 3.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, -2.0d, 0.0d, -1.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d}, new double[]{0.0d, 0.0d, 0.0d, 0.0d, -3.0d, 0.0d, 3.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, -2.0d, 0.0d, -1.0d, 0.0d}, new double[]{9.0d, -9.0d, -9.0d, 9.0d, 6.0d, 3.0d, -6.0d, -3.0d, 6.0d, -6.0d, 3.0d, -3.0d, 4.0d, 2.0d, 2.0d, 1.0d}, new double[]{-6.0d, 6.0d, 6.0d, -6.0d, -3.0d, -3.0d, 3.0d, 3.0d, -4.0d, 4.0d, -2.0d, 2.0d, -2.0d, -2.0d, -1.0d, -1.0d}, new double[]{2.0d, 0.0d, -2.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 1.0d, 0.0d, 1.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d}, new double[]{0.0d, 0.0d, 0.0d, 0.0d, 2.0d, 0.0d, -2.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 1.0d, 0.0d, 1.0d, 0.0d}, new double[]{-6.0d, 6.0d, 6.0d, -6.0d, -4.0d, -2.0d, 4.0d, 2.0d, -3.0d, 3.0d, -3.0d, 3.0d, -2.0d, -1.0d, -2.0d, -1.0d}, new double[]{4.0d, -4.0d, -4.0d, 4.0d, 2.0d, 2.0d, -2.0d, -2.0d, 2.0d, -2.0d, 2.0d, -2.0d, 1.0d, 1.0d, 1.0d, 1.0d}};
    private org.apache.commons.math.analysis.BivariateRealFunction[][][] partialDerivatives = null;
    private final org.apache.commons.math.analysis.interpolation.BicubicSplineFunction[][] splines;
    private final double[] xval;
    private final double[] yval;

    public BicubicSplineInterpolatingFunction(double[] x, double[] y, double[][] f, double[][] dFdX, double[][] dFdY, double[][] d2FdXdY) throws org.apache.commons.math.DimensionMismatchException {
        int xLen = x.length;
        int yLen = y.length;
        if (xLen != 0 && yLen != 0 && f.length != 0) {
            char c = 0;
            if (f[0].length != 0) {
                if (xLen != f.length) {
                    throw new org.apache.commons.math.DimensionMismatchException(xLen, f.length);
                }
                if (xLen != dFdX.length) {
                    throw new org.apache.commons.math.DimensionMismatchException(xLen, dFdX.length);
                }
                if (xLen != dFdY.length) {
                    throw new org.apache.commons.math.DimensionMismatchException(xLen, dFdY.length);
                }
                if (xLen != d2FdXdY.length) {
                    throw new org.apache.commons.math.DimensionMismatchException(xLen, d2FdXdY.length);
                }
                org.apache.commons.math.util.MathUtils.checkOrder(x);
                org.apache.commons.math.util.MathUtils.checkOrder(y);
                this.xval = (double[]) x.clone();
                this.yval = (double[]) y.clone();
                int lastI = xLen - 1;
                int lastJ = yLen - 1;
                boolean z = true;
                this.splines = (org.apache.commons.math.analysis.interpolation.BicubicSplineFunction[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) org.apache.commons.math.analysis.interpolation.BicubicSplineFunction.class, lastI, lastJ);
                int i = 0;
                while (i < lastI) {
                    if (f[i].length != yLen) {
                        throw new org.apache.commons.math.DimensionMismatchException(f[i].length, yLen);
                    }
                    if (dFdX[i].length != yLen) {
                        throw new org.apache.commons.math.DimensionMismatchException(dFdX[i].length, yLen);
                    }
                    if (dFdY[i].length != yLen) {
                        throw new org.apache.commons.math.DimensionMismatchException(dFdY[i].length, yLen);
                    }
                    if (d2FdXdY[i].length != yLen) {
                        throw new org.apache.commons.math.DimensionMismatchException(d2FdXdY[i].length, yLen);
                    }
                    int ip1 = i + 1;
                    int j = 0;
                    while (j < lastJ) {
                        int jp1 = j + 1;
                        double d = f[i][j];
                        double d2 = f[ip1][j];
                        double d3 = f[i][jp1];
                        double d4 = f[ip1][jp1];
                        double d5 = dFdX[i][j];
                        double d6 = dFdX[ip1][j];
                        double d7 = dFdX[i][jp1];
                        double d8 = dFdX[ip1][jp1];
                        double d9 = dFdY[i][j];
                        double d10 = dFdY[ip1][j];
                        double d11 = dFdY[i][jp1];
                        double d12 = dFdY[ip1][jp1];
                        double d13 = d2FdXdY[i][j];
                        double d14 = d2FdXdY[ip1][j];
                        double d15 = d2FdXdY[i][jp1];
                        double d16 = d2FdXdY[ip1][jp1];
                        double[] beta = new double[16];
                        beta[c] = d;
                        beta[1] = d2;
                        beta[2] = d3;
                        beta[3] = d4;
                        beta[4] = d5;
                        beta[5] = d6;
                        beta[6] = d7;
                        beta[7] = d8;
                        beta[8] = d9;
                        beta[9] = d10;
                        beta[10] = d11;
                        beta[11] = d12;
                        beta[12] = d13;
                        beta[13] = d14;
                        beta[14] = d15;
                        beta[15] = d16;
                        this.splines[i][j] = new org.apache.commons.math.analysis.interpolation.BicubicSplineFunction(computeSplineCoefficients(beta));
                        j++;
                        z = true;
                        c = 0;
                    }
                    i++;
                    c = 0;
                }
                return;
            }
        }
        throw new org.apache.commons.math.exception.NoDataException();
    }

    @Override // org.apache.commons.math.analysis.BivariateRealFunction
    public double value(double x, double y) {
        int i = searchIndex(x, this.xval);
        if (i == -1) {
            throw new org.apache.commons.math.exception.OutOfRangeException(java.lang.Double.valueOf(x), java.lang.Double.valueOf(this.xval[0]), java.lang.Double.valueOf(this.xval[this.xval.length - 1]));
        }
        int j = searchIndex(y, this.yval);
        if (j == -1) {
            throw new org.apache.commons.math.exception.OutOfRangeException(java.lang.Double.valueOf(y), java.lang.Double.valueOf(this.yval[0]), java.lang.Double.valueOf(this.yval[this.yval.length - 1]));
        }
        double xN = (x - this.xval[i]) / (this.xval[i + 1] - this.xval[i]);
        double yN = (y - this.yval[j]) / (this.yval[j + 1] - this.yval[j]);
        return this.splines[i][j].value(xN, yN);
    }

    public double partialDerivativeX(double x, double y) {
        return partialDerivative(0, x, y);
    }

    public double partialDerivativeY(double x, double y) {
        return partialDerivative(1, x, y);
    }

    public double partialDerivativeXX(double x, double y) {
        return partialDerivative(2, x, y);
    }

    public double partialDerivativeYY(double x, double y) {
        return partialDerivative(3, x, y);
    }

    public double partialDerivativeXY(double x, double y) {
        return partialDerivative(4, x, y);
    }

    private double partialDerivative(int which, double x, double y) {
        if (this.partialDerivatives == null) {
            computePartialDerivatives();
        }
        int i = searchIndex(x, this.xval);
        if (i == -1) {
            throw new org.apache.commons.math.exception.OutOfRangeException(java.lang.Double.valueOf(x), java.lang.Double.valueOf(this.xval[0]), java.lang.Double.valueOf(this.xval[this.xval.length - 1]));
        }
        int j = searchIndex(y, this.yval);
        if (j == -1) {
            throw new org.apache.commons.math.exception.OutOfRangeException(java.lang.Double.valueOf(y), java.lang.Double.valueOf(this.yval[0]), java.lang.Double.valueOf(this.yval[this.yval.length - 1]));
        }
        double xN = (x - this.xval[i]) / (this.xval[i + 1] - this.xval[i]);
        double yN = (y - this.yval[j]) / (this.yval[j + 1] - this.yval[j]);
        try {
            return this.partialDerivatives[which][i][j].value(xN, yN);
        } catch (org.apache.commons.math.FunctionEvaluationException fee) {
            throw new java.lang.RuntimeException(fee);
        }
    }

    private void computePartialDerivatives() {
        int lastI = this.xval.length - 1;
        int lastJ = this.yval.length - 1;
        this.partialDerivatives = (org.apache.commons.math.analysis.BivariateRealFunction[][][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) org.apache.commons.math.analysis.BivariateRealFunction.class, 5, lastI, lastJ);
        for (int i = 0; i < lastI; i++) {
            for (int j = 0; j < lastJ; j++) {
                org.apache.commons.math.analysis.interpolation.BicubicSplineFunction f = this.splines[i][j];
                this.partialDerivatives[0][i][j] = f.partialDerivativeX();
                this.partialDerivatives[1][i][j] = f.partialDerivativeY();
                this.partialDerivatives[2][i][j] = f.partialDerivativeXX();
                this.partialDerivatives[3][i][j] = f.partialDerivativeYY();
                this.partialDerivatives[4][i][j] = f.partialDerivativeXY();
            }
        }
    }

    private int searchIndex(double c, double[] val) {
        if (c < val[0]) {
            return -1;
        }
        int max = val.length;
        for (int i = 1; i < max; i++) {
            if (c <= val[i]) {
                return i - 1;
            }
        }
        return -1;
    }

    private double[] computeSplineCoefficients(double[] beta) {
        double[] a = new double[16];
        for (int i = 0; i < 16; i++) {
            double result = 0.0d;
            double[] row = AINV[i];
            for (int j = 0; j < 16; j++) {
                result += row[j] * beta[j];
            }
            a[i] = result;
        }
        return a;
    }
}
