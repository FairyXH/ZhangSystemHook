package org.apache.commons.math.analysis.solvers;

/* JADX INFO: loaded from: classes4.dex */
public class MullerSolver extends org.apache.commons.math.analysis.solvers.UnivariateRealSolverImpl {
    @java.lang.Deprecated
    public MullerSolver(org.apache.commons.math.analysis.UnivariateRealFunction f) {
        super(f, 100, 1.0E-6d);
    }

    @java.lang.Deprecated
    public MullerSolver() {
        super(100, 1.0E-6d);
    }

    @Override // org.apache.commons.math.analysis.solvers.UnivariateRealSolver
    @java.lang.Deprecated
    public double solve(double min, double max) throws org.apache.commons.math.FunctionEvaluationException, org.apache.commons.math.ConvergenceException {
        return solve(this.f, min, max);
    }

    @Override // org.apache.commons.math.analysis.solvers.UnivariateRealSolver
    @java.lang.Deprecated
    public double solve(double min, double max, double initial) throws org.apache.commons.math.FunctionEvaluationException, org.apache.commons.math.ConvergenceException {
        return solve(this.f, min, max, initial);
    }

    @Override // org.apache.commons.math.analysis.solvers.UnivariateRealSolverImpl
    public double solve(int maxEval, org.apache.commons.math.analysis.UnivariateRealFunction f, double min, double max, double initial) throws org.apache.commons.math.FunctionEvaluationException, org.apache.commons.math.MaxIterationsExceededException {
        setMaximalIterationCount(maxEval);
        return solve(f, min, max, initial);
    }

    @Override // org.apache.commons.math.analysis.solvers.UnivariateRealSolver
    @java.lang.Deprecated
    public double solve(org.apache.commons.math.analysis.UnivariateRealFunction f, double min, double max, double initial) throws org.apache.commons.math.FunctionEvaluationException, org.apache.commons.math.MaxIterationsExceededException {
        if (f.value(min) == 0.0d) {
            return min;
        }
        if (f.value(max) == 0.0d) {
            return max;
        }
        if (f.value(initial) == 0.0d) {
            return initial;
        }
        verifyBracketing(min, max, f);
        verifySequence(min, initial, max);
        if (isBracketing(min, initial, f)) {
            return solve(f, min, initial);
        }
        return solve(f, initial, max);
    }

    @Override // org.apache.commons.math.analysis.solvers.UnivariateRealSolverImpl
    public double solve(int maxEval, org.apache.commons.math.analysis.UnivariateRealFunction f, double min, double max) throws org.apache.commons.math.FunctionEvaluationException, org.apache.commons.math.MaxIterationsExceededException {
        setMaximalIterationCount(maxEval);
        return solve(f, min, max);
    }

    @Override // org.apache.commons.math.analysis.solvers.UnivariateRealSolver
    @java.lang.Deprecated
    public double solve(org.apache.commons.math.analysis.UnivariateRealFunction f, double min, double max) throws org.apache.commons.math.FunctionEvaluationException, org.apache.commons.math.MaxIterationsExceededException {
        double y0 = f.value(min);
        double y2 = f.value(max);
        double x1 = (min + max) * 0.5d;
        double y1 = f.value(x1);
        if (y0 == 0.0d) {
            return min;
        }
        if (y2 == 0.0d) {
            return max;
        }
        double x12 = x1;
        verifyBracketing(min, max, f);
        double y12 = y2;
        double y13 = y1;
        double y22 = max;
        double x2 = y0;
        double y02 = min;
        double oldx = Double.POSITIVE_INFINITY;
        for (int i = 1; i <= this.maximalIterationCount; i++) {
            double d01 = (y13 - x2) / (x12 - y02);
            double d12 = (y12 - y13) / (y22 - x12);
            double d012 = (d12 - d01) / (y22 - y02);
            double c1 = d01 + ((x12 - y02) * d012);
            double delta = (c1 * c1) - ((4.0d * y13) * d012);
            double xplus = x12 + ((y13 * (-2.0d)) / (c1 + org.apache.commons.math.util.FastMath.sqrt(delta)));
            double xminus = x12 + (((-2.0d) * y13) / (c1 - org.apache.commons.math.util.FastMath.sqrt(delta)));
            double x = isSequence(y02, xplus, y22) ? xplus : xminus;
            double y = f.value(x);
            double y03 = x2;
            double tolerance = org.apache.commons.math.util.FastMath.max(this.relativeAccuracy * org.apache.commons.math.util.FastMath.abs(x), this.absoluteAccuracy);
            if (org.apache.commons.math.util.FastMath.abs(x - oldx) <= tolerance) {
                setResult(x, i);
                return this.result;
            }
            double dAbs = org.apache.commons.math.util.FastMath.abs(y);
            double tolerance2 = this.functionValueAccuracy;
            if (dAbs <= tolerance2) {
                setResult(x, i);
                return this.result;
            }
            boolean bisect = (x < x12 && x12 - y02 > (y22 - y02) * 0.95d) || (x > x12 && y22 - x12 > (y22 - y02) * 0.95d) || x == x12;
            if (!bisect) {
                double x0 = x < x12 ? y02 : x12;
                double y04 = x < x12 ? y03 : y13;
                double x22 = x > x12 ? y22 : x12;
                double y23 = x > x12 ? y12 : y13;
                x12 = x;
                oldx = x;
                y13 = y;
                y12 = y23;
                y22 = x22;
                x2 = y04;
                y02 = x0;
            } else {
                double x02 = y02 + y22;
                double xm = x02 * 0.5d;
                double ym = f.value(xm);
                if (org.apache.commons.math.util.MathUtils.sign(y03) + org.apache.commons.math.util.MathUtils.sign(ym) == 0.0d) {
                    y22 = xm;
                    y12 = ym;
                } else {
                    y02 = xm;
                    y03 = ym;
                }
                double x3 = (y02 + y22) * 0.5d;
                double y14 = f.value(x3);
                oldx = Double.POSITIVE_INFINITY;
                y13 = y14;
                x2 = y03;
                x12 = x3;
            }
        }
        throw new org.apache.commons.math.MaxIterationsExceededException(this.maximalIterationCount);
    }

    @java.lang.Deprecated
    public double solve2(double min, double max) throws org.apache.commons.math.FunctionEvaluationException, org.apache.commons.math.MaxIterationsExceededException {
        return solve2(this.f, min, max);
    }

    @java.lang.Deprecated
    public double solve2(org.apache.commons.math.analysis.UnivariateRealFunction f, double min, double max) throws org.apache.commons.math.FunctionEvaluationException, org.apache.commons.math.MaxIterationsExceededException {
        double dplus;
        double x0;
        double x02 = min;
        double y0 = f.value(x02);
        double x1 = max;
        double y1 = f.value(x1);
        double x2 = (x02 + x1) * 0.5d;
        double y2 = f.value(x2);
        if (y0 == 0.0d) {
            return min;
        }
        if (y1 == 0.0d) {
            return max;
        }
        verifyBracketing(min, max, f);
        double oldx = Double.POSITIVE_INFINITY;
        int i = 1;
        double oldx2 = x2;
        while (i <= this.maximalIterationCount) {
            double q = (oldx2 - x1) / (x1 - x02);
            double a = ((y2 - ((q + 1.0d) * y1)) + (q * y0)) * q;
            double b = ((((q * 2.0d) + 1.0d) * y2) - (((q + 1.0d) * (q + 1.0d)) * y1)) + (q * q * y0);
            double c = (q + 1.0d) * y2;
            double delta = (b * b) - ((4.0d * a) * c);
            if (delta >= 0.0d) {
                double dplus2 = b + org.apache.commons.math.util.FastMath.sqrt(delta);
                double dminus = b - org.apache.commons.math.util.FastMath.sqrt(delta);
                dplus = org.apache.commons.math.util.FastMath.abs(dplus2) > org.apache.commons.math.util.FastMath.abs(dminus) ? dplus2 : dminus;
            } else {
                double denominator = b * b;
                dplus = org.apache.commons.math.util.FastMath.sqrt(denominator - delta);
            }
            if (dplus != 0.0d) {
                double x = oldx2 - (((2.0d * c) * (oldx2 - x1)) / dplus);
                while (true) {
                    if (x != x1 && x != oldx2) {
                        break;
                    }
                    double oldx3 = oldx;
                    double oldx4 = this.absoluteAccuracy;
                    x += oldx4;
                    oldx = oldx3;
                }
                x0 = x;
            } else {
                double oldx5 = org.apache.commons.math.util.FastMath.random();
                double x3 = min + (oldx5 * (max - min));
                oldx = Double.POSITIVE_INFINITY;
                x0 = x3;
            }
            double y = f.value(x0);
            double x22 = oldx2;
            double tolerance = org.apache.commons.math.util.FastMath.max(this.relativeAccuracy * org.apache.commons.math.util.FastMath.abs(x0), this.absoluteAccuracy);
            if (org.apache.commons.math.util.FastMath.abs(x0 - oldx) <= tolerance) {
                setResult(x0, i);
                return this.result;
            }
            double dAbs = org.apache.commons.math.util.FastMath.abs(y);
            double oldx6 = this.functionValueAccuracy;
            if (dAbs <= oldx6) {
                setResult(x0, i);
                return this.result;
            }
            double x03 = x1;
            y0 = y1;
            x1 = x22;
            y1 = y2;
            double x23 = x0;
            y2 = y;
            double tolerance2 = x0;
            i++;
            x02 = x03;
            oldx = tolerance2;
            oldx2 = x23;
        }
        throw new org.apache.commons.math.MaxIterationsExceededException(this.maximalIterationCount);
    }
}
