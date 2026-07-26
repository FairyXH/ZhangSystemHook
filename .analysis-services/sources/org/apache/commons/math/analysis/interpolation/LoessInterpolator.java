package org.apache.commons.math.analysis.interpolation;

/* JADX INFO: loaded from: classes4.dex */
public class LoessInterpolator implements org.apache.commons.math.analysis.interpolation.UnivariateRealInterpolator, java.io.Serializable {
    public static final double DEFAULT_ACCURACY = 1.0E-12d;
    public static final double DEFAULT_BANDWIDTH = 0.3d;
    public static final int DEFAULT_ROBUSTNESS_ITERS = 2;
    private static final long serialVersionUID = 5204927143605193821L;
    private final double accuracy;
    private final double bandwidth;
    private final int robustnessIters;

    public LoessInterpolator() {
        this.bandwidth = 0.3d;
        this.robustnessIters = 2;
        this.accuracy = 1.0E-12d;
    }

    public LoessInterpolator(double bandwidth, int robustnessIters) throws org.apache.commons.math.MathException {
        this(bandwidth, robustnessIters, 1.0E-12d);
    }

    public LoessInterpolator(double bandwidth, int robustnessIters, double accuracy) throws org.apache.commons.math.MathException {
        if (bandwidth < 0.0d || bandwidth > 1.0d) {
            throw new org.apache.commons.math.MathException(org.apache.commons.math.exception.util.LocalizedFormats.BANDWIDTH_OUT_OF_INTERVAL, java.lang.Double.valueOf(bandwidth));
        }
        this.bandwidth = bandwidth;
        if (robustnessIters < 0) {
            throw new org.apache.commons.math.MathException(org.apache.commons.math.exception.util.LocalizedFormats.NEGATIVE_ROBUSTNESS_ITERATIONS, java.lang.Integer.valueOf(robustnessIters));
        }
        this.robustnessIters = robustnessIters;
        this.accuracy = accuracy;
    }

    @Override // org.apache.commons.math.analysis.interpolation.UnivariateRealInterpolator
    public final org.apache.commons.math.analysis.polynomials.PolynomialSplineFunction interpolate(double[] xval, double[] yval) throws org.apache.commons.math.MathException {
        return new org.apache.commons.math.analysis.interpolation.SplineInterpolator().interpolate(xval, smooth(xval, yval));
    }

    public final double[] smooth(double[] xval, double[] yval, double[] weights) throws org.apache.commons.math.MathException {
        int edge;
        double beta;
        double[] dArr = yval;
        double[] dArr2 = weights;
        if (xval.length != dArr.length) {
            throw new org.apache.commons.math.MathException(org.apache.commons.math.exception.util.LocalizedFormats.MISMATCHED_LOESS_ABSCISSA_ORDINATE_ARRAYS, java.lang.Integer.valueOf(xval.length), java.lang.Integer.valueOf(yval.length));
        }
        int n = xval.length;
        int i = 0;
        if (n == 0) {
            throw new org.apache.commons.math.MathException(org.apache.commons.math.exception.util.LocalizedFormats.LOESS_EXPECTS_AT_LEAST_ONE_POINT, new java.lang.Object[0]);
        }
        checkAllFiniteReal(xval, org.apache.commons.math.exception.util.LocalizedFormats.NON_REAL_FINITE_ABSCISSA);
        checkAllFiniteReal(dArr, org.apache.commons.math.exception.util.LocalizedFormats.NON_REAL_FINITE_ORDINATE);
        checkAllFiniteReal(dArr2, org.apache.commons.math.exception.util.LocalizedFormats.NON_REAL_FINITE_WEIGHT);
        checkStrictlyIncreasing(xval);
        char c = 1;
        if (n == 1) {
            return new double[]{dArr[0]};
        }
        if (n == 2) {
            return new double[]{dArr[0], dArr[1]};
        }
        int bandwidthInPoints = (int) (this.bandwidth * ((double) n));
        if (bandwidthInPoints < 2) {
            throw new org.apache.commons.math.MathException(org.apache.commons.math.exception.util.LocalizedFormats.TOO_SMALL_BANDWIDTH, java.lang.Integer.valueOf(n), java.lang.Double.valueOf(2.0d / ((double) n)), java.lang.Double.valueOf(this.bandwidth));
        }
        double[] res = new double[n];
        double[] residuals = new double[n];
        double[] sortedResiduals = new double[n];
        double[] robustnessWeights = new double[n];
        java.util.Arrays.fill(robustnessWeights, 1.0d);
        int iter = 0;
        while (iter <= this.robustnessIters) {
            int[] bandwidthInterval = {i, bandwidthInPoints - 1};
            int i2 = 0;
            while (i2 < n) {
                double x = xval[i2];
                if (i2 > 0) {
                    updateBandwidthInterval(xval, dArr2, i2, bandwidthInterval);
                }
                int ileft = bandwidthInterval[i];
                int iright = bandwidthInterval[c];
                if (xval[i2] - xval[ileft] > xval[iright] - xval[i2]) {
                    edge = ileft;
                } else {
                    edge = iright;
                }
                double sumWeights = 0.0d;
                double sumX = 0.0d;
                double sumXSquared = 0.0d;
                double sumY = 0.0d;
                double sumXY = 0.0d;
                double denom = org.apache.commons.math.util.FastMath.abs(1.0d / (xval[edge] - x));
                int k = ileft;
                while (k <= iright) {
                    double xk = xval[k];
                    double yk = dArr[k];
                    double dist = k < i2 ? x - xk : xk - x;
                    double w = tricube(dist * denom) * robustnessWeights[k] * dArr2[k];
                    double xkw = xk * w;
                    sumWeights += w;
                    sumX += xkw;
                    sumXSquared += xk * xkw;
                    sumY += yk * w;
                    sumXY += yk * xkw;
                    k++;
                }
                double meanX = sumX / sumWeights;
                double meanY = sumY / sumWeights;
                double meanXY = sumXY / sumWeights;
                double meanXSquared = sumXSquared / sumWeights;
                if (org.apache.commons.math.util.FastMath.sqrt(org.apache.commons.math.util.FastMath.abs(meanXSquared - (meanX * meanX))) < this.accuracy) {
                    beta = 0.0d;
                } else {
                    double beta2 = meanX * meanY;
                    beta = (meanXY - beta2) / (meanXSquared - (meanX * meanX));
                }
                double alpha = meanY - (beta * meanX);
                res[i2] = (beta * x) + alpha;
                residuals[i2] = org.apache.commons.math.util.FastMath.abs(dArr[i2] - res[i2]);
                i2++;
                i = 0;
                c = 1;
            }
            if (iter == this.robustnessIters) {
                break;
            }
            java.lang.System.arraycopy(residuals, 0, sortedResiduals, 0, n);
            java.util.Arrays.sort(sortedResiduals);
            double medianResidual = sortedResiduals[n / 2];
            if (org.apache.commons.math.util.FastMath.abs(medianResidual) < this.accuracy) {
                break;
            }
            for (int i3 = 0; i3 < n; i3++) {
                double arg = residuals[i3] / (6.0d * medianResidual);
                if (arg < 1.0d) {
                    double w2 = 1.0d - (arg * arg);
                    robustnessWeights[i3] = w2 * w2;
                } else {
                    robustnessWeights[i3] = 0.0d;
                }
            }
            iter++;
            dArr = yval;
            dArr2 = weights;
            i = 0;
            c = 1;
        }
        return res;
    }

    public final double[] smooth(double[] xval, double[] yval) throws org.apache.commons.math.MathException {
        if (xval.length != yval.length) {
            throw new org.apache.commons.math.MathException(org.apache.commons.math.exception.util.LocalizedFormats.MISMATCHED_LOESS_ABSCISSA_ORDINATE_ARRAYS, java.lang.Integer.valueOf(xval.length), java.lang.Integer.valueOf(yval.length));
        }
        double[] unitWeights = new double[xval.length];
        java.util.Arrays.fill(unitWeights, 1.0d);
        return smooth(xval, yval, unitWeights);
    }

    private static void updateBandwidthInterval(double[] xval, double[] weights, int i, int[] bandwidthInterval) {
        int left = bandwidthInterval[0];
        int right = bandwidthInterval[1];
        int nextRight = nextNonzero(weights, right);
        if (nextRight < xval.length && xval[nextRight] - xval[i] < xval[i] - xval[left]) {
            int nextLeft = nextNonzero(weights, bandwidthInterval[0]);
            bandwidthInterval[0] = nextLeft;
            bandwidthInterval[1] = nextRight;
        }
    }

    private static int nextNonzero(double[] weights, int i) {
        int j = i + 1;
        while (j < weights.length && weights[j] == 0.0d) {
            j++;
        }
        return j;
    }

    private static double tricube(double x) {
        double tmp = 1.0d - ((x * x) * x);
        return tmp * tmp * tmp;
    }

    private static void checkAllFiniteReal(double[] values, org.apache.commons.math.exception.util.Localizable pattern) throws org.apache.commons.math.MathException {
        for (int i = 0; i < values.length; i++) {
            double x = values[i];
            if (java.lang.Double.isInfinite(x) || java.lang.Double.isNaN(x)) {
                throw new org.apache.commons.math.MathException(pattern, java.lang.Integer.valueOf(i), java.lang.Double.valueOf(x));
            }
        }
    }

    private static void checkStrictlyIncreasing(double[] xval) throws org.apache.commons.math.MathException {
        for (int i = 0; i < xval.length; i++) {
            if (i >= 1 && xval[i - 1] >= xval[i]) {
                throw new org.apache.commons.math.MathException(org.apache.commons.math.exception.util.LocalizedFormats.OUT_OF_ORDER_ABSCISSA_ARRAY, java.lang.Integer.valueOf(i - 1), java.lang.Double.valueOf(xval[i - 1]), java.lang.Integer.valueOf(i), java.lang.Double.valueOf(xval[i]));
            }
        }
    }
}
