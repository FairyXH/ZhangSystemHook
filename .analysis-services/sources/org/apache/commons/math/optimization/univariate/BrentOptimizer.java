package org.apache.commons.math.optimization.univariate;

/* JADX INFO: loaded from: classes4.dex */
public class BrentOptimizer extends org.apache.commons.math.optimization.univariate.AbstractUnivariateRealOptimizer {
    private static final double GOLDEN_SECTION = (3.0d - org.apache.commons.math.util.FastMath.sqrt(5.0d)) * 0.5d;

    public BrentOptimizer() {
        setMaxEvaluations(1000);
        setMaximalIterationCount(100);
        setAbsoluteAccuracy(1.0E-11d);
        setRelativeAccuracy(1.0E-9d);
    }

    @Override // org.apache.commons.math.optimization.univariate.AbstractUnivariateRealOptimizer
    protected double doOptimize() throws org.apache.commons.math.FunctionEvaluationException, org.apache.commons.math.MaxIterationsExceededException {
        return localMin(getGoalType() == org.apache.commons.math.optimization.GoalType.MINIMIZE, getMin(), getStartValue(), getMax(), getRelativeAccuracy(), getAbsoluteAccuracy());
    }

    private double localMin(boolean isMinim, double lo, double mid, double hi, double eps, double t) throws org.apache.commons.math.FunctionEvaluationException, org.apache.commons.math.MaxIterationsExceededException {
        double a;
        double b;
        double v;
        double w;
        double e;
        double d;
        double p;
        double q;
        double e2;
        if (eps <= 0.0d) {
            throw new org.apache.commons.math.exception.NotStrictlyPositiveException(java.lang.Double.valueOf(eps));
        }
        if (t <= 0.0d) {
            throw new org.apache.commons.math.exception.NotStrictlyPositiveException(java.lang.Double.valueOf(t));
        }
        if (lo < hi) {
            a = lo;
            b = hi;
        } else {
            a = hi;
            b = lo;
        }
        double x = mid;
        double u = x;
        double d2 = x;
        double tol1 = 0.0d;
        double e3 = 0.0d;
        double fx = computeObjectiveValue(x);
        if (!isMinim) {
            fx = -fx;
        }
        double fv = fx;
        double fw = fx;
        while (true) {
            double m = (a + b) * 0.5d;
            double d3 = tol1;
            double d4 = (org.apache.commons.math.util.FastMath.abs(x) * eps) + t;
            double tol2 = d4 * 2.0d;
            if (org.apache.commons.math.util.FastMath.abs(x - m) <= tol2 - ((b - a) * 0.5d)) {
                break;
            }
            if (org.apache.commons.math.util.FastMath.abs(e3) > d4) {
                double r = (x - d2) * (fx - fv);
                double q2 = (x - u) * (fx - fw);
                v = u;
                double v2 = ((x - u) * q2) - ((x - d2) * r);
                double p2 = q2 - r;
                w = d2;
                double w2 = p2 * 2.0d;
                if (w2 > 0.0d) {
                    double p3 = -v2;
                    p = p3;
                    q = w2;
                } else {
                    p = v2;
                    q = -w2;
                }
                double r2 = e3;
                if (p > (a - x) * q && p < (b - x) * q && org.apache.commons.math.util.FastMath.abs(p) < org.apache.commons.math.util.FastMath.abs(0.5d * q * r2)) {
                    d = p / q;
                    double u2 = x + d;
                    if (u2 - a >= tol2 && b - u2 >= tol2) {
                        e3 = d3;
                    } else if (x <= m) {
                        d = d4;
                        e3 = d3;
                    } else {
                        d = -d4;
                        e3 = d3;
                    }
                } else {
                    if (x < m) {
                        e2 = b - x;
                    } else {
                        e2 = a - x;
                    }
                    d = GOLDEN_SECTION * e2;
                    e3 = e2;
                }
            } else {
                v = u;
                w = d2;
                if (x < m) {
                    e = b - x;
                } else {
                    e = a - x;
                }
                d = GOLDEN_SECTION * e;
                e3 = e;
            }
            double e4 = org.apache.commons.math.util.FastMath.abs(d);
            if (e4 >= d4) {
                u = x + d;
            } else if (d >= 0.0d) {
                u = x + d4;
            } else {
                double u3 = x - d4;
                u = u3;
            }
            double a2 = a;
            double fu = computeObjectiveValue(u);
            if (!isMinim) {
                fu = -fu;
            }
            if (fu <= fx) {
                if (u < x) {
                    b = x;
                } else {
                    a2 = x;
                }
                double v3 = w;
                fv = fw;
                double w3 = x;
                fw = fx;
                x = u;
                fx = fu;
                a = a2;
                u = v3;
                w = w3;
            } else {
                if (u < x) {
                    a2 = u;
                } else {
                    b = u;
                }
                if (fu <= fw || w == x) {
                    double v4 = w;
                    fv = fw;
                    double w4 = u;
                    fw = fu;
                    a = a2;
                    u = v4;
                    w = w4;
                } else if (fu <= fv || v == x || v == w) {
                    fv = fu;
                    a = a2;
                } else {
                    a = a2;
                    u = v;
                }
            }
            incrementIterationsCounter();
            tol1 = d;
            d2 = w;
        }
        setFunctionValue(isMinim ? fx : -fx);
        return x;
    }
}
