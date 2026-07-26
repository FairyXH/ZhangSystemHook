package org.apache.commons.math.special;

/* JADX INFO: loaded from: classes4.dex */
public class Gamma {
    private static final double C_LIMIT = 49.0d;
    private static final double DEFAULT_EPSILON = 1.0E-14d;
    public static final double GAMMA = 0.5772156649015329d;
    private static final double S_LIMIT = 1.0E-5d;
    private static final double[] LANCZOS = {0.9999999999999971d, 57.15623566586292d, -59.59796035547549d, 14.136097974741746d, -0.4919138160976202d, 3.399464998481189E-5d, 4.652362892704858E-5d, -9.837447530487956E-5d, 1.580887032249125E-4d, -2.1026444172410488E-4d, 2.1743961811521265E-4d, -1.643181065367639E-4d, 8.441822398385275E-5d, -2.6190838401581408E-5d, 3.6899182659531625E-6d};
    private static final double HALF_LOG_2_PI = org.apache.commons.math.util.FastMath.log(6.283185307179586d) * 0.5d;

    private Gamma() {
    }

    public static double logGamma(double x) {
        if (java.lang.Double.isNaN(x) || x <= 0.0d) {
            return Double.NaN;
        }
        double sum = 0.0d;
        for (int i = LANCZOS.length - 1; i > 0; i--) {
            sum += LANCZOS[i] / (((double) i) + x);
        }
        double tmp = x + 4.7421875d + 0.5d;
        double ret = (((0.5d + x) * org.apache.commons.math.util.FastMath.log(tmp)) - tmp) + HALF_LOG_2_PI + org.apache.commons.math.util.FastMath.log((sum + LANCZOS[0]) / x);
        return ret;
    }

    public static double regularizedGammaP(double a, double x) throws org.apache.commons.math.MathException {
        return regularizedGammaP(a, x, DEFAULT_EPSILON, Integer.MAX_VALUE);
    }

    public static double regularizedGammaP(double a, double x, double epsilon, int maxIterations) throws org.apache.commons.math.MathException {
        if (java.lang.Double.isNaN(a) || java.lang.Double.isNaN(x) || a <= 0.0d || x < 0.0d) {
            return Double.NaN;
        }
        if (x == 0.0d) {
            return 0.0d;
        }
        if (x >= a + 1.0d) {
            double ret = 1.0d - regularizedGammaQ(a, x, epsilon, maxIterations);
            return ret;
        }
        double n = 0.0d;
        double an = 1.0d / a;
        double sum = an;
        while (org.apache.commons.math.util.FastMath.abs(an / sum) > epsilon && n < maxIterations && sum < Double.POSITIVE_INFINITY) {
            n += 1.0d;
            an *= x / (a + n);
            sum += an;
        }
        if (n >= maxIterations) {
            throw new org.apache.commons.math.MaxIterationsExceededException(maxIterations);
        }
        if (java.lang.Double.isInfinite(sum)) {
            return 1.0d;
        }
        double ret2 = -x;
        return org.apache.commons.math.util.FastMath.exp((ret2 + (org.apache.commons.math.util.FastMath.log(x) * a)) - logGamma(a)) * sum;
    }

    public static double regularizedGammaQ(double a, double x) throws org.apache.commons.math.MathException {
        return regularizedGammaQ(a, x, DEFAULT_EPSILON, Integer.MAX_VALUE);
    }

    public static double regularizedGammaQ(final double a, double x, double epsilon, int maxIterations) throws org.apache.commons.math.MathException {
        if (java.lang.Double.isNaN(a) || java.lang.Double.isNaN(x) || a <= 0.0d || x < 0.0d) {
            return Double.NaN;
        }
        if (x == 0.0d) {
            return 1.0d;
        }
        if (x < a + 1.0d) {
            double ret = 1.0d - regularizedGammaP(a, x, epsilon, maxIterations);
            return ret;
        }
        org.apache.commons.math.util.ContinuedFraction cf = new org.apache.commons.math.util.ContinuedFraction() { // from class: org.apache.commons.math.special.Gamma.1
            @Override // org.apache.commons.math.util.ContinuedFraction
            protected double getA(int n, double x2) {
                return (((((double) n) * 2.0d) + 1.0d) - a) + x2;
            }

            @Override // org.apache.commons.math.util.ContinuedFraction
            protected double getB(int n, double x2) {
                return ((double) n) * (a - ((double) n));
            }
        };
        double ret2 = 1.0d / cf.evaluate(x, epsilon, maxIterations);
        return ret2 * org.apache.commons.math.util.FastMath.exp(((-x) + (org.apache.commons.math.util.FastMath.log(x) * a)) - logGamma(a));
    }

    public static double digamma(double x) {
        if (x > 0.0d && x <= S_LIMIT) {
            return (-0.5772156649015329d) - (1.0d / x);
        }
        if (x >= C_LIMIT) {
            double inv = 1.0d / (x * x);
            return (org.apache.commons.math.util.FastMath.log(x) - (0.5d / x)) - ((((0.008333333333333333d - (inv / 252.0d)) * inv) + 0.08333333333333333d) * inv);
        }
        return digamma(x + 1.0d) - (1.0d / x);
    }

    public static double trigamma(double x) {
        if (x > 0.0d && x <= S_LIMIT) {
            return 1.0d / (x * x);
        }
        if (x >= C_LIMIT) {
            double inv = 1.0d / (x * x);
            return (1.0d / x) + (inv / 2.0d) + ((inv / x) * (0.16666666666666666d - (((inv / 42.0d) + 0.03333333333333333d) * inv)));
        }
        return trigamma(x + 1.0d) + (1.0d / (x * x));
    }
}
