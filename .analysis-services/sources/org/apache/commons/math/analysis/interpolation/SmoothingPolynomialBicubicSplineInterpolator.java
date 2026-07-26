package org.apache.commons.math.analysis.interpolation;

/* JADX INFO: loaded from: classes4.dex */
public class SmoothingPolynomialBicubicSplineInterpolator extends org.apache.commons.math.analysis.interpolation.BicubicSplineInterpolator {
    private final org.apache.commons.math.optimization.fitting.PolynomialFitter xFitter;
    private final org.apache.commons.math.optimization.fitting.PolynomialFitter yFitter;

    public SmoothingPolynomialBicubicSplineInterpolator() {
        this(3);
    }

    public SmoothingPolynomialBicubicSplineInterpolator(int degree) {
        this(degree, degree);
    }

    public SmoothingPolynomialBicubicSplineInterpolator(int xDegree, int yDegree) {
        this.xFitter = new org.apache.commons.math.optimization.fitting.PolynomialFitter(xDegree, new org.apache.commons.math.optimization.general.GaussNewtonOptimizer(false));
        this.yFitter = new org.apache.commons.math.optimization.fitting.PolynomialFitter(yDegree, new org.apache.commons.math.optimization.general.GaussNewtonOptimizer(false));
    }

    @Override // org.apache.commons.math.analysis.interpolation.BicubicSplineInterpolator, org.apache.commons.math.analysis.interpolation.BivariateRealGridInterpolator
    public org.apache.commons.math.analysis.interpolation.BicubicSplineInterpolatingFunction interpolate(double[] xval, double[] yval, double[][] fval) throws org.apache.commons.math.MathException {
        if (xval.length == 0 || yval.length == 0 || fval.length == 0) {
            throw new org.apache.commons.math.exception.NoDataException();
        }
        if (xval.length != fval.length) {
            throw new org.apache.commons.math.exception.DimensionMismatchException(xval.length, fval.length);
        }
        int xLen = xval.length;
        int yLen = yval.length;
        for (int i = 0; i < xLen; i++) {
            if (fval[i].length != yLen) {
                throw new org.apache.commons.math.exception.DimensionMismatchException(fval[i].length, yLen);
            }
        }
        org.apache.commons.math.util.MathUtils.checkOrder(xval);
        org.apache.commons.math.util.MathUtils.checkOrder(yval);
        org.apache.commons.math.analysis.polynomials.PolynomialFunction[] yPolyX = new org.apache.commons.math.analysis.polynomials.PolynomialFunction[yLen];
        for (int j = 0; j < yLen; j++) {
            this.xFitter.clearObservations();
            for (int i2 = 0; i2 < xLen; i2++) {
                this.xFitter.addObservedPoint(1.0d, xval[i2], fval[i2][j]);
            }
            yPolyX[j] = this.xFitter.fit();
        }
        double[][] fval_1 = (double[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.Double.TYPE, xLen, yLen);
        for (int j2 = 0; j2 < yLen; j2++) {
            org.apache.commons.math.analysis.polynomials.PolynomialFunction f = yPolyX[j2];
            for (int i3 = 0; i3 < xLen; i3++) {
                fval_1[i3][j2] = f.value(xval[i3]);
            }
        }
        org.apache.commons.math.analysis.polynomials.PolynomialFunction[] xPolyY = new org.apache.commons.math.analysis.polynomials.PolynomialFunction[xLen];
        for (int i4 = 0; i4 < xLen; i4++) {
            this.yFitter.clearObservations();
            for (int j3 = 0; j3 < yLen; j3++) {
                this.yFitter.addObservedPoint(1.0d, yval[j3], fval_1[i4][j3]);
            }
            xPolyY[i4] = this.yFitter.fit();
        }
        double[][] fval_2 = (double[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.Double.TYPE, xLen, yLen);
        for (int i5 = 0; i5 < xLen; i5++) {
            org.apache.commons.math.analysis.polynomials.PolynomialFunction f2 = xPolyY[i5];
            for (int j4 = 0; j4 < yLen; j4++) {
                fval_2[i5][j4] = f2.value(yval[j4]);
            }
        }
        return super.interpolate(xval, yval, fval_2);
    }
}
