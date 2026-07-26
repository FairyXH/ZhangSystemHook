package org.apache.commons.math.analysis.solvers;

/* JADX INFO: loaded from: classes4.dex */
public class RiddersSolver extends org.apache.commons.math.analysis.solvers.UnivariateRealSolverImpl {
    @java.lang.Deprecated
    public RiddersSolver(org.apache.commons.math.analysis.UnivariateRealFunction f) {
        super(f, 100, 1.0E-6d);
    }

    @java.lang.Deprecated
    public RiddersSolver() {
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
        double x2;
        double x22;
        double y2;
        double x1 = min;
        double y1 = f.value(x1);
        double x23 = max;
        double x24 = f.value(x23);
        if (y1 == 0.0d) {
            return min;
        }
        if (x24 == 0.0d) {
            return max;
        }
        verifyBracketing(min, max, f);
        int i = 1;
        double x12 = Double.POSITIVE_INFINITY;
        while (i <= this.maximalIterationCount) {
            double x3 = 0.5d * (x1 + x23);
            double y3 = f.value(x3);
            double x25 = x23;
            if (org.apache.commons.math.util.FastMath.abs(y3) <= this.functionValueAccuracy) {
                setResult(x3, i);
                return this.result;
            }
            double delta = 1.0d - ((y1 * x24) / (y3 * y3));
            double correction = ((org.apache.commons.math.util.MathUtils.sign(x24) * org.apache.commons.math.util.MathUtils.sign(y3)) * (x3 - x1)) / org.apache.commons.math.util.FastMath.sqrt(delta);
            double x13 = x1;
            double x14 = x3 - correction;
            double y = f.value(x14);
            double y22 = x24;
            double tolerance = org.apache.commons.math.util.FastMath.max(this.relativeAccuracy * org.apache.commons.math.util.FastMath.abs(x14), this.absoluteAccuracy);
            if (org.apache.commons.math.util.FastMath.abs(x14 - x12) <= tolerance) {
                setResult(x14, i);
                return this.result;
            }
            double dAbs = org.apache.commons.math.util.FastMath.abs(y);
            double oldx = this.functionValueAccuracy;
            if (dAbs <= oldx) {
                setResult(x14, i);
                return this.result;
            }
            if (correction > 0.0d) {
                if (org.apache.commons.math.util.MathUtils.sign(y1) + org.apache.commons.math.util.MathUtils.sign(y) == 0.0d) {
                    y2 = y;
                    x22 = x14;
                    x2 = x13;
                } else {
                    x2 = x14;
                    x22 = x3;
                    y1 = y;
                    y2 = y3;
                }
            } else if (org.apache.commons.math.util.MathUtils.sign(y22) + org.apache.commons.math.util.MathUtils.sign(y) == 0.0d) {
                x2 = x14;
                y1 = y;
                x22 = x25;
                y2 = y22;
            } else {
                x2 = x3;
                x22 = x14;
                y1 = y3;
                y2 = y;
            }
            i++;
            x1 = x2;
            x23 = x22;
            x24 = y2;
            x12 = x14;
        }
        throw new org.apache.commons.math.MaxIterationsExceededException(this.maximalIterationCount);
    }
}
