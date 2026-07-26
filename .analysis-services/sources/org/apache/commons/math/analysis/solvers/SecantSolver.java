package org.apache.commons.math.analysis.solvers;

/* JADX INFO: loaded from: classes4.dex */
public class SecantSolver extends org.apache.commons.math.analysis.solvers.UnivariateRealSolverImpl {
    @java.lang.Deprecated
    public SecantSolver(org.apache.commons.math.analysis.UnivariateRealFunction f) {
        super(f, 100, 1.0E-6d);
    }

    @java.lang.Deprecated
    public SecantSolver() {
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
        return solve(f, min, max);
    }

    @Override // org.apache.commons.math.analysis.solvers.UnivariateRealSolverImpl
    public double solve(int maxEval, org.apache.commons.math.analysis.UnivariateRealFunction f, double min, double max) throws org.apache.commons.math.FunctionEvaluationException, org.apache.commons.math.MaxIterationsExceededException {
        setMaximalIterationCount(maxEval);
        return solve(f, min, max);
    }

    @Override // org.apache.commons.math.analysis.solvers.UnivariateRealSolver
    @java.lang.Deprecated
    public double solve(org.apache.commons.math.analysis.UnivariateRealFunction f, double min, double max) throws org.apache.commons.math.FunctionEvaluationException, org.apache.commons.math.MaxIterationsExceededException {
        double delta;
        clearResult();
        verifyInterval(min, max);
        double x0 = min;
        double x1 = max;
        double y0 = f.value(x0);
        double y1 = f.value(x1);
        if (y0 * y1 >= 0.0d) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.SAME_SIGN_AT_ENDPOINTS, java.lang.Double.valueOf(min), java.lang.Double.valueOf(max), java.lang.Double.valueOf(y0), java.lang.Double.valueOf(y1));
        }
        double x2 = x0;
        double y2 = y0;
        double oldDelta = x2 - x1;
        for (int i = 0; i < this.maximalIterationCount; i++) {
            if (org.apache.commons.math.util.FastMath.abs(y2) < org.apache.commons.math.util.FastMath.abs(y1)) {
                x0 = x1;
                x1 = x2;
                x2 = x0;
                y0 = y1;
                y1 = y2;
                y2 = y0;
            }
            if (org.apache.commons.math.util.FastMath.abs(y1) <= this.functionValueAccuracy) {
                setResult(x1, i);
                return this.result;
            }
            double x22 = x2;
            double x02 = x0;
            if (org.apache.commons.math.util.FastMath.abs(oldDelta) < org.apache.commons.math.util.FastMath.max(this.relativeAccuracy * org.apache.commons.math.util.FastMath.abs(x1), this.absoluteAccuracy)) {
                setResult(x1, i);
                return this.result;
            }
            if (org.apache.commons.math.util.FastMath.abs(y1) > org.apache.commons.math.util.FastMath.abs(y0)) {
                delta = 0.5d * oldDelta;
            } else {
                double delta2 = (x02 - x1) / (1.0d - (y0 / y1));
                if (delta2 / oldDelta <= 1.0d) {
                    delta = delta2;
                } else {
                    delta = 0.5d * oldDelta;
                }
            }
            x0 = x1;
            y0 = y1;
            x1 += delta;
            y1 = f.value(x1);
            if ((y1 > 0.0d) != (y2 > 0.0d)) {
                x2 = x22;
            } else {
                x2 = x0;
                y2 = y0;
            }
            oldDelta = x2 - x1;
        }
        throw new org.apache.commons.math.MaxIterationsExceededException(this.maximalIterationCount);
    }
}
