package org.apache.commons.math.analysis.solvers;

/* JADX INFO: loaded from: classes4.dex */
public class BisectionSolver extends org.apache.commons.math.analysis.solvers.UnivariateRealSolverImpl {
    @java.lang.Deprecated
    public BisectionSolver(org.apache.commons.math.analysis.UnivariateRealFunction f) {
        super(f, 100, 1.0E-6d);
    }

    public BisectionSolver() {
        super(100, 1.0E-6d);
    }

    @Override // org.apache.commons.math.analysis.solvers.UnivariateRealSolver
    @java.lang.Deprecated
    public double solve(double min, double max, double initial) throws org.apache.commons.math.FunctionEvaluationException, org.apache.commons.math.MaxIterationsExceededException {
        return solve(this.f, min, max);
    }

    @Override // org.apache.commons.math.analysis.solvers.UnivariateRealSolver
    @java.lang.Deprecated
    public double solve(double min, double max) throws org.apache.commons.math.FunctionEvaluationException, org.apache.commons.math.MaxIterationsExceededException {
        return solve(this.f, min, max);
    }

    @Override // org.apache.commons.math.analysis.solvers.UnivariateRealSolver
    @java.lang.Deprecated
    public double solve(org.apache.commons.math.analysis.UnivariateRealFunction f, double min, double max, double initial) throws org.apache.commons.math.FunctionEvaluationException, org.apache.commons.math.MaxIterationsExceededException {
        return solve(f, min, max);
    }

    @Override // org.apache.commons.math.analysis.solvers.UnivariateRealSolverImpl
    public double solve(int maxEval, org.apache.commons.math.analysis.UnivariateRealFunction f, double min, double max, double initial) throws org.apache.commons.math.FunctionEvaluationException, org.apache.commons.math.MaxIterationsExceededException {
        return solve(maxEval, f, min, max);
    }

    @Override // org.apache.commons.math.analysis.solvers.UnivariateRealSolverImpl
    public double solve(int maxEval, org.apache.commons.math.analysis.UnivariateRealFunction f, double min, double max) throws org.apache.commons.math.FunctionEvaluationException, org.apache.commons.math.MaxIterationsExceededException {
        setMaximalIterationCount(maxEval);
        return solve(f, min, max);
    }

    @Override // org.apache.commons.math.analysis.solvers.UnivariateRealSolver
    @java.lang.Deprecated
    public double solve(org.apache.commons.math.analysis.UnivariateRealFunction f, double min, double max) throws org.apache.commons.math.FunctionEvaluationException, org.apache.commons.math.MaxIterationsExceededException {
        clearResult();
        double min2 = min;
        double max2 = max;
        verifyInterval(min2, max2);
        for (int i = 0; i < this.maximalIterationCount; i++) {
            double m = org.apache.commons.math.analysis.solvers.UnivariateRealSolverUtils.midpoint(min2, max2);
            double fmin = f.value(min2);
            double fm = f.value(m);
            if (fm * fmin > 0.0d) {
                min2 = m;
            } else {
                max2 = m;
            }
            if (org.apache.commons.math.util.FastMath.abs(max2 - min2) <= this.absoluteAccuracy) {
                double m2 = org.apache.commons.math.analysis.solvers.UnivariateRealSolverUtils.midpoint(min2, max2);
                setResult(m2, i);
                return m2;
            }
        }
        throw new org.apache.commons.math.MaxIterationsExceededException(this.maximalIterationCount);
    }
}
