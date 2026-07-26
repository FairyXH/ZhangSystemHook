package org.apache.commons.math.analysis.interpolation;

/* JADX INFO: loaded from: classes4.dex */
public class BicubicSplineInterpolator implements org.apache.commons.math.analysis.interpolation.BivariateRealGridInterpolator {
    @Override // org.apache.commons.math.analysis.interpolation.BivariateRealGridInterpolator
    public org.apache.commons.math.analysis.interpolation.BicubicSplineInterpolatingFunction interpolate(double[] xval, double[] yval, double[][] fval) throws org.apache.commons.math.MathException, java.lang.IllegalArgumentException {
        if (xval.length == 0 || yval.length == 0 || fval.length == 0) {
            throw new org.apache.commons.math.exception.NoDataException();
        }
        if (xval.length != fval.length) {
            throw new org.apache.commons.math.DimensionMismatchException(xval.length, fval.length);
        }
        org.apache.commons.math.util.MathUtils.checkOrder(xval);
        org.apache.commons.math.util.MathUtils.checkOrder(yval);
        int xLen = xval.length;
        int yLen = yval.length;
        double[][] fX = (double[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.Double.TYPE, yLen, xLen);
        for (int i = 0; i < xLen; i++) {
            if (fval[i].length != yLen) {
                throw new org.apache.commons.math.DimensionMismatchException(fval[i].length, yLen);
            }
            for (int j = 0; j < yLen; j++) {
                fX[j][i] = fval[i][j];
            }
        }
        org.apache.commons.math.analysis.interpolation.SplineInterpolator spInterpolator = new org.apache.commons.math.analysis.interpolation.SplineInterpolator();
        org.apache.commons.math.analysis.polynomials.PolynomialSplineFunction[] ySplineX = new org.apache.commons.math.analysis.polynomials.PolynomialSplineFunction[yLen];
        for (int j2 = 0; j2 < yLen; j2++) {
            ySplineX[j2] = spInterpolator.interpolate(xval, fX[j2]);
        }
        org.apache.commons.math.analysis.polynomials.PolynomialSplineFunction[] xSplineY = new org.apache.commons.math.analysis.polynomials.PolynomialSplineFunction[xLen];
        for (int i2 = 0; i2 < xLen; i2++) {
            xSplineY[i2] = spInterpolator.interpolate(yval, fval[i2]);
        }
        double[][] dFdX = (double[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.Double.TYPE, xLen, yLen);
        for (int j3 = 0; j3 < yLen; j3++) {
            org.apache.commons.math.analysis.UnivariateRealFunction f = ySplineX[j3].derivative();
            for (int i3 = 0; i3 < xLen; i3++) {
                dFdX[i3][j3] = f.value(xval[i3]);
            }
        }
        double[][] dFdY = (double[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.Double.TYPE, xLen, yLen);
        int i4 = 0;
        while (i4 < xLen) {
            org.apache.commons.math.analysis.UnivariateRealFunction f2 = xSplineY[i4].derivative();
            int j4 = 0;
            while (j4 < yLen) {
                dFdY[i4][j4] = f2.value(yval[j4]);
                j4++;
                i4 = i4;
            }
            i4++;
        }
        double[][] d2FdXdY = (double[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.Double.TYPE, xLen, yLen);
        for (int i5 = 0; i5 < xLen; i5++) {
            int nI = nextIndex(i5, xLen);
            int pI = previousIndex(i5);
            for (int j5 = 0; j5 < yLen; j5++) {
                int nJ = nextIndex(j5, yLen);
                int pJ = previousIndex(j5);
                d2FdXdY[i5][j5] = (((fval[nI][nJ] - fval[nI][pJ]) - fval[pI][nJ]) + fval[pI][pJ]) / ((xval[nI] - xval[pI]) * (yval[nJ] - yval[pJ]));
            }
        }
        return new org.apache.commons.math.analysis.interpolation.BicubicSplineInterpolatingFunction(xval, yval, fval, dFdX, dFdY, d2FdXdY);
    }

    private int nextIndex(int i, int max) {
        int index = i + 1;
        return index < max ? index : index - 1;
    }

    private int previousIndex(int i) {
        int index = i - 1;
        if (index >= 0) {
            return index;
        }
        return 0;
    }
}
