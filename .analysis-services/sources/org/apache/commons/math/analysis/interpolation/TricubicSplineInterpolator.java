package org.apache.commons.math.analysis.interpolation;

/* JADX INFO: loaded from: classes4.dex */
public class TricubicSplineInterpolator implements org.apache.commons.math.analysis.interpolation.TrivariateRealGridInterpolator {
    @Override // org.apache.commons.math.analysis.interpolation.TrivariateRealGridInterpolator
    public org.apache.commons.math.analysis.interpolation.TricubicSplineInterpolatingFunction interpolate(double[] xval, double[] yval, double[] zval, double[][][] fval) throws org.apache.commons.math.MathException {
        double[] dArr = xval;
        if (dArr.length == 0 || yval.length == 0 || zval.length == 0 || fval.length == 0) {
            throw new org.apache.commons.math.exception.NoDataException();
        }
        if (dArr.length != fval.length) {
            throw new org.apache.commons.math.exception.DimensionMismatchException(dArr.length, fval.length);
        }
        org.apache.commons.math.util.MathUtils.checkOrder(xval);
        org.apache.commons.math.util.MathUtils.checkOrder(yval);
        org.apache.commons.math.util.MathUtils.checkOrder(zval);
        int xLen = dArr.length;
        int yLen = yval.length;
        int zLen = zval.length;
        double[][][] fvalXY = (double[][][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.Double.TYPE, zLen, xLen, yLen);
        double[][][] fvalZX = (double[][][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.Double.TYPE, yLen, zLen, xLen);
        for (int i = 0; i < xLen; i++) {
            if (fval[i].length != yLen) {
                throw new org.apache.commons.math.exception.DimensionMismatchException(fval[i].length, yLen);
            }
            for (int j = 0; j < yLen; j++) {
                if (fval[i][j].length != zLen) {
                    throw new org.apache.commons.math.exception.DimensionMismatchException(fval[i][j].length, zLen);
                }
                for (int k = 0; k < zLen; k++) {
                    double v = fval[i][j][k];
                    fvalXY[k][i][j] = v;
                    fvalZX[j][k][i] = v;
                }
            }
        }
        org.apache.commons.math.analysis.interpolation.BicubicSplineInterpolator bsi = new org.apache.commons.math.analysis.interpolation.BicubicSplineInterpolator();
        org.apache.commons.math.analysis.interpolation.BicubicSplineInterpolatingFunction[] xSplineYZ = new org.apache.commons.math.analysis.interpolation.BicubicSplineInterpolatingFunction[xLen];
        for (int i2 = 0; i2 < xLen; i2++) {
            xSplineYZ[i2] = bsi.interpolate(yval, zval, fval[i2]);
        }
        org.apache.commons.math.analysis.interpolation.BicubicSplineInterpolatingFunction[] ySplineZX = new org.apache.commons.math.analysis.interpolation.BicubicSplineInterpolatingFunction[yLen];
        for (int j2 = 0; j2 < yLen; j2++) {
            ySplineZX[j2] = bsi.interpolate(zval, dArr, fvalZX[j2]);
        }
        org.apache.commons.math.analysis.interpolation.BicubicSplineInterpolatingFunction[] zSplineXY = new org.apache.commons.math.analysis.interpolation.BicubicSplineInterpolatingFunction[zLen];
        for (int k2 = 0; k2 < zLen; k2++) {
            zSplineXY[k2] = bsi.interpolate(dArr, yval, fvalXY[k2]);
        }
        double[][][] dFdX = (double[][][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.Double.TYPE, xLen, yLen, zLen);
        double[][][] dFdY = (double[][][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.Double.TYPE, xLen, yLen, zLen);
        double[][][] d2FdXdY = (double[][][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.Double.TYPE, xLen, yLen, zLen);
        int k3 = 0;
        while (k3 < zLen) {
            org.apache.commons.math.analysis.interpolation.BicubicSplineInterpolatingFunction f = zSplineXY[k3];
            int i3 = 0;
            while (i3 < xLen) {
                int k4 = k3;
                double x = dArr[i3];
                org.apache.commons.math.analysis.interpolation.BicubicSplineInterpolatingFunction[] zSplineXY2 = zSplineXY;
                for (int j3 = 0; j3 < yLen; j3++) {
                    double y = yval[j3];
                    dFdX[i3][j3][k4] = f.partialDerivativeX(x, y);
                    dFdY[i3][j3][k4] = f.partialDerivativeY(x, y);
                    d2FdXdY[i3][j3][k4] = f.partialDerivativeXY(x, y);
                }
                i3++;
                dArr = xval;
                k3 = k4;
                zSplineXY = zSplineXY2;
            }
            k3++;
            dArr = xval;
        }
        double[][][] dFdZ = (double[][][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.Double.TYPE, xLen, yLen, zLen);
        double[][][] d2FdYdZ = (double[][][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.Double.TYPE, xLen, yLen, zLen);
        for (int i4 = 0; i4 < xLen; i4++) {
            org.apache.commons.math.analysis.interpolation.BicubicSplineInterpolatingFunction f2 = xSplineYZ[i4];
            for (int j4 = 0; j4 < yLen; j4++) {
                double y2 = yval[j4];
                int k5 = 0;
                while (k5 < zLen) {
                    org.apache.commons.math.analysis.interpolation.BicubicSplineInterpolatingFunction[] xSplineYZ2 = xSplineYZ;
                    double z = zval[k5];
                    dFdZ[i4][j4][k5] = f2.partialDerivativeY(y2, z);
                    d2FdYdZ[i4][j4][k5] = f2.partialDerivativeXY(y2, z);
                    k5++;
                    bsi = bsi;
                    xSplineYZ = xSplineYZ2;
                }
            }
        }
        double[][][] d2FdZdX = (double[][][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.Double.TYPE, xLen, yLen, zLen);
        for (int j5 = 0; j5 < yLen; j5++) {
            org.apache.commons.math.analysis.interpolation.BicubicSplineInterpolatingFunction f3 = ySplineZX[j5];
            for (int k6 = 0; k6 < zLen; k6++) {
                double z2 = zval[k6];
                int i5 = 0;
                while (i5 < xLen) {
                    d2FdZdX[i5][j5][k6] = f3.partialDerivativeXY(z2, xval[i5]);
                    i5++;
                    dFdZ = dFdZ;
                }
            }
        }
        double[][][] dFdZ2 = dFdZ;
        double[][][] d3FdXdYdZ = (double[][][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.Double.TYPE, xLen, yLen, zLen);
        for (int i6 = 0; i6 < xLen; i6++) {
            int nI = nextIndex(i6, xLen);
            int pI = previousIndex(i6);
            for (int j6 = 0; j6 < yLen; j6++) {
                int nJ = nextIndex(j6, yLen);
                int pJ = previousIndex(j6);
                for (int k7 = 0; k7 < zLen; k7++) {
                    int nK = nextIndex(k7, zLen);
                    int pK = previousIndex(k7);
                    d3FdXdYdZ[i6][j6][k7] = (((((((fval[nI][nJ][nK] - fval[nI][pJ][nK]) - fval[pI][nJ][nK]) + fval[pI][pJ][nK]) - fval[nI][nJ][pK]) + fval[nI][pJ][pK]) + fval[pI][nJ][pK]) - fval[pI][pJ][pK]) / (((xval[nI] - xval[pI]) * (yval[nJ] - yval[pJ])) * (zval[nK] - zval[pK]));
                }
            }
        }
        return new org.apache.commons.math.analysis.interpolation.TricubicSplineInterpolatingFunction(xval, yval, zval, fval, dFdX, dFdY, dFdZ2, d2FdXdY, d2FdZdX, d2FdYdZ, d3FdXdYdZ);
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
