package org.apache.commons.math.util;

/* JADX INFO: loaded from: classes4.dex */
public abstract class ContinuedFraction {
    private static final double DEFAULT_EPSILON = 1.0E-8d;

    protected abstract double getA(int i, double d);

    protected abstract double getB(int i, double d);

    protected ContinuedFraction() {
    }

    public double evaluate(double x) throws org.apache.commons.math.MathException {
        return evaluate(x, DEFAULT_EPSILON, Integer.MAX_VALUE);
    }

    public double evaluate(double x, double epsilon) throws org.apache.commons.math.MathException {
        return evaluate(x, epsilon, Integer.MAX_VALUE);
    }

    public double evaluate(double x, int maxIterations) throws org.apache.commons.math.MathException {
        return evaluate(x, DEFAULT_EPSILON, maxIterations);
    }

    public double evaluate(double x, double epsilon, int maxIterations) throws org.apache.commons.math.MathException {
        org.apache.commons.math.util.ContinuedFraction continuedFraction = this;
        double d = x;
        double p0 = 1.0d;
        double p1 = continuedFraction.getA(0, d);
        double q0 = 0.0d;
        double q1 = 1.0d;
        double c = p1 / 1.0d;
        int n = 0;
        double relativeError = Double.MAX_VALUE;
        while (n < maxIterations && relativeError > epsilon) {
            n++;
            double p12 = p1;
            double a = continuedFraction.getA(n, d);
            double c2 = c;
            double b = continuedFraction.getB(n, d);
            double p2 = (a * p12) + (b * p0);
            double q2 = (a * q1) + (b * q0);
            boolean infinite = false;
            if (java.lang.Double.isInfinite(p2) || java.lang.Double.isInfinite(q2)) {
                double scaleFactor = 1.0d;
                double scale = org.apache.commons.math.util.FastMath.max(a, b);
                if (scale <= 0.0d) {
                    throw new org.apache.commons.math.ConvergenceException(org.apache.commons.math.exception.util.LocalizedFormats.CONTINUED_FRACTION_INFINITY_DIVERGENCE, java.lang.Double.valueOf(x));
                }
                infinite = true;
                for (int i = 0; i < 5; i++) {
                    double lastScaleFactor = scaleFactor;
                    scaleFactor *= scale;
                    if (a != 0.0d && a > b) {
                        q2 = (q1 / lastScaleFactor) + ((b / scaleFactor) * q0);
                        p2 = (p12 / lastScaleFactor) + ((b / scaleFactor) * p0);
                    } else if (b != 0.0d) {
                        q2 = ((a / scaleFactor) * q1) + (q0 / lastScaleFactor);
                        p2 = ((a / scaleFactor) * p12) + (p0 / lastScaleFactor);
                    }
                    infinite = java.lang.Double.isInfinite(p2) || java.lang.Double.isInfinite(q2);
                    if (!infinite) {
                        break;
                    }
                }
            }
            if (infinite) {
                throw new org.apache.commons.math.ConvergenceException(org.apache.commons.math.exception.util.LocalizedFormats.CONTINUED_FRACTION_INFINITY_DIVERGENCE, java.lang.Double.valueOf(x));
            }
            double r = p2 / q2;
            if (java.lang.Double.isNaN(r)) {
                throw new org.apache.commons.math.ConvergenceException(org.apache.commons.math.exception.util.LocalizedFormats.CONTINUED_FRACTION_NAN_DIVERGENCE, java.lang.Double.valueOf(x));
            }
            relativeError = org.apache.commons.math.util.FastMath.abs((r / c2) - 1.0d);
            double c3 = p2 / q2;
            p0 = p12;
            q0 = q1;
            q1 = q2;
            continuedFraction = this;
            d = x;
            p1 = p2;
            c = c3;
        }
        double c4 = c;
        if (n >= maxIterations) {
            throw new org.apache.commons.math.MaxIterationsExceededException(maxIterations, org.apache.commons.math.exception.util.LocalizedFormats.NON_CONVERGENT_CONTINUED_FRACTION, java.lang.Double.valueOf(x));
        }
        return c4;
    }
}
