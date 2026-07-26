package org.apache.commons.math.analysis.interpolation;

/* JADX INFO: loaded from: classes4.dex */
@java.lang.Deprecated
public class SmoothingBicubicSplineInterpolator implements org.apache.commons.math.analysis.interpolation.BivariateRealGridInterpolator {
    @Override // org.apache.commons.math.analysis.interpolation.BivariateRealGridInterpolator
    public org.apache.commons.math.analysis.BivariateRealFunction interpolate(double[] xval, double[] yval, double[][] zval) throws org.apache.commons.math.MathException, java.lang.IllegalArgumentException {
        if (xval.length == 0 || yval.length == 0 || zval.length == 0) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.NO_DATA, new java.lang.Object[0]);
        }
        if (xval.length != zval.length) {
            throw new org.apache.commons.math.DimensionMismatchException(xval.length, zval.length);
        }
        org.apache.commons.math.util.MathUtils.checkOrder(xval, org.apache.commons.math.util.MathUtils.OrderDirection.INCREASING, true);
        org.apache.commons.math.util.MathUtils.checkOrder(yval, org.apache.commons.math.util.MathUtils.OrderDirection.INCREASING, true);
        int xLen = xval.length;
        int yLen = yval.length;
        double[][] zX = (double[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.Double.TYPE, yLen, xLen);
        for (int i = 0; i < xLen; i++) {
            if (zval[i].length != yLen) {
                throw new org.apache.commons.math.DimensionMismatchException(zval[i].length, yLen);
            }
            for (int j = 0; j < yLen; j++) {
                zX[j][i] = zval[i][j];
            }
        }
        org.apache.commons.math.analysis.interpolation.SplineInterpolator spInterpolator = new org.apache.commons.math.analysis.interpolation.SplineInterpolator();
        org.apache.commons.math.analysis.polynomials.PolynomialSplineFunction[] ySplineX = new org.apache.commons.math.analysis.polynomials.PolynomialSplineFunction[yLen];
        for (int j2 = 0; j2 < yLen; j2++) {
            ySplineX[j2] = spInterpolator.interpolate(xval, zX[j2]);
        }
        double[][] zY_1 = (double[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.Double.TYPE, xLen, yLen);
        for (int j3 = 0; j3 < yLen; j3++) {
            org.apache.commons.math.analysis.polynomials.PolynomialSplineFunction f = ySplineX[j3];
            for (int i2 = 0; i2 < xLen; i2++) {
                zY_1[i2][j3] = f.value(xval[i2]);
            }
        }
        org.apache.commons.math.analysis.polynomials.PolynomialSplineFunction[] xSplineY = new org.apache.commons.math.analysis.polynomials.PolynomialSplineFunction[xLen];
        for (int i3 = 0; i3 < xLen; i3++) {
            xSplineY[i3] = spInterpolator.interpolate(yval, zY_1[i3]);
        }
        double[][] zY_2 = (double[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.Double.TYPE, xLen, yLen);
        int i4 = 0;
        while (i4 < xLen) {
            org.apache.commons.math.analysis.polynomials.PolynomialSplineFunction f2 = xSplineY[i4];
            int j4 = 0;
            while (j4 < yLen) {
                zY_2[i4][j4] = f2.value(yval[j4]);
                j4++;
                i4 = i4;
            }
            i4++;
        }
        double[][] dZdX = (double[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.Double.TYPE, xLen, yLen);
        for (int j5 = 0; j5 < yLen; j5++) {
            org.apache.commons.math.analysis.UnivariateRealFunction f3 = ySplineX[j5].derivative();
            int i5 = 0;
            while (i5 < xLen) {
                dZdX[i5][j5] = f3.value(xval[i5]);
                i5++;
                xSplineY = xSplineY;
            }
        }
        org.apache.commons.math.analysis.polynomials.PolynomialSplineFunction[] xSplineY2 = xSplineY;
        double[][] dZdY = (double[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.Double.TYPE, xLen, yLen);
        for (int i6 = 0; i6 < xLen; i6++) {
            org.apache.commons.math.analysis.UnivariateRealFunction f4 = xSplineY2[i6].derivative();
            for (int j6 = 0; j6 < yLen; j6++) {
                dZdY[i6][j6] = f4.value(yval[j6]);
            }
        }
        double[][] dZdXdY = (double[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.Double.TYPE, xLen, yLen);
        for (int i7 = 0; i7 < xLen; i7++) {
            int nI = nextIndex(i7, xLen);
            int pI = previousIndex(i7);
            for (int j7 = 0; j7 < yLen; j7++) {
                int nJ = nextIndex(j7, yLen);
                int pJ = previousIndex(j7);
                dZdXdY[i7][j7] = (((zY_2[nI][nJ] - zY_2[nI][pJ]) - zY_2[pI][nJ]) + zY_2[pI][pJ]) / ((xval[nI] - xval[pI]) * (yval[nJ] - yval[pJ]));
            }
        }
        return new org.apache.commons.math.analysis.interpolation.BicubicSplineInterpolatingFunction(xval, yval, zY_2, dZdX, dZdY, dZdXdY);
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
