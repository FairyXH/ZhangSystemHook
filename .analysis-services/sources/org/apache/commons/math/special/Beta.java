package org.apache.commons.math.special;

/* JADX INFO: loaded from: classes4.dex */
public class Beta {
    private static final double DEFAULT_EPSILON = 1.0E-14d;

    private Beta() {
    }

    public static double regularizedBeta(double x, double a, double b) throws org.apache.commons.math.MathException {
        return regularizedBeta(x, a, b, DEFAULT_EPSILON, Integer.MAX_VALUE);
    }

    public static double regularizedBeta(double x, double a, double b, double epsilon) throws org.apache.commons.math.MathException {
        return regularizedBeta(x, a, b, epsilon, Integer.MAX_VALUE);
    }

    public static double regularizedBeta(double x, double a, double b, int maxIterations) throws org.apache.commons.math.MathException {
        return regularizedBeta(x, a, b, DEFAULT_EPSILON, maxIterations);
    }

    public static double regularizedBeta(double x, final double a, final double b, double epsilon, int maxIterations) throws org.apache.commons.math.MathException {
        if (java.lang.Double.isNaN(x) || java.lang.Double.isNaN(a) || java.lang.Double.isNaN(b) || x < 0.0d || x > 1.0d || a <= 0.0d || b <= 0.0d) {
            return Double.NaN;
        }
        if (x <= (a + 1.0d) / ((a + b) + 2.0d)) {
            org.apache.commons.math.util.ContinuedFraction fraction = new org.apache.commons.math.util.ContinuedFraction() { // from class: org.apache.commons.math.special.Beta.1
                @Override // org.apache.commons.math.util.ContinuedFraction
                protected double getB(int n, double x2) {
                    if (n % 2 == 0) {
                        double m = ((double) n) / 2.0d;
                        double ret = (((b - m) * m) * x2) / (((a + (m * 2.0d)) - 1.0d) * (a + (2.0d * m)));
                        return ret;
                    }
                    double m2 = (((double) n) - 1.0d) / 2.0d;
                    double ret2 = (-(((a + m2) * ((a + b) + m2)) * x2)) / ((a + (m2 * 2.0d)) * ((a + (2.0d * m2)) + 1.0d));
                    return ret2;
                }

                @Override // org.apache.commons.math.util.ContinuedFraction
                protected double getA(int n, double x2) {
                    return 1.0d;
                }
            };
            double ret = (org.apache.commons.math.util.FastMath.exp((((org.apache.commons.math.util.FastMath.log(x) * a) + (org.apache.commons.math.util.FastMath.log(1.0d - x) * b)) - org.apache.commons.math.util.FastMath.log(a)) - logBeta(a, b, epsilon, maxIterations)) * 1.0d) / fraction.evaluate(x, epsilon, maxIterations);
            return ret;
        }
        double ret2 = 1.0d - regularizedBeta(1.0d - x, b, a, epsilon, maxIterations);
        return ret2;
    }

    public static double logBeta(double a, double b) {
        return logBeta(a, b, DEFAULT_EPSILON, Integer.MAX_VALUE);
    }

    public static double logBeta(double a, double b, double epsilon, int maxIterations) {
        if (java.lang.Double.isNaN(a) || java.lang.Double.isNaN(b) || a <= 0.0d || b <= 0.0d) {
            return Double.NaN;
        }
        double ret = (org.apache.commons.math.special.Gamma.logGamma(a) + org.apache.commons.math.special.Gamma.logGamma(b)) - org.apache.commons.math.special.Gamma.logGamma(a + b);
        return ret;
    }
}
