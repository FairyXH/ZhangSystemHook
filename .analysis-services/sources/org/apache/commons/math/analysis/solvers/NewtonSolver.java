package org.apache.commons.math.analysis.solvers;

/* JADX INFO: loaded from: classes4.dex */
public class NewtonSolver extends org.apache.commons.math.analysis.solvers.UnivariateRealSolverImpl {
    @java.lang.Deprecated
    public NewtonSolver(org.apache.commons.math.analysis.DifferentiableUnivariateRealFunction f) {
        super(f, 100, 1.0E-6d);
    }

    @java.lang.Deprecated
    public NewtonSolver() {
        super(100, 1.0E-6d);
    }

    @Override // org.apache.commons.math.analysis.solvers.UnivariateRealSolver
    @java.lang.Deprecated
    public double solve(double min, double max) throws org.apache.commons.math.FunctionEvaluationException, org.apache.commons.math.MaxIterationsExceededException {
        return solve(this.f, min, max);
    }

    @Override // org.apache.commons.math.analysis.solvers.UnivariateRealSolver
    @java.lang.Deprecated
    public double solve(double min, double max, double startValue) throws org.apache.commons.math.FunctionEvaluationException, org.apache.commons.math.MaxIterationsExceededException {
        return solve(this.f, min, max, startValue);
    }

    @Override // org.apache.commons.math.analysis.solvers.UnivariateRealSolverImpl
    public double solve(int maxEval, org.apache.commons.math.analysis.UnivariateRealFunction f, double min, double max) throws org.apache.commons.math.FunctionEvaluationException, org.apache.commons.math.MaxIterationsExceededException {
        setMaximalIterationCount(maxEval);
        return solve(f, min, max);
    }

    @Override // org.apache.commons.math.analysis.solvers.UnivariateRealSolver
    @java.lang.Deprecated
    public double solve(org.apache.commons.math.analysis.UnivariateRealFunction f, double min, double max) throws org.apache.commons.math.FunctionEvaluationException, org.apache.commons.math.MaxIterationsExceededException {
        return solve(f, min, max, org.apache.commons.math.analysis.solvers.UnivariateRealSolverUtils.midpoint(min, max));
    }

    @Override // org.apache.commons.math.analysis.solvers.UnivariateRealSolverImpl
    public double solve(int maxEval, org.apache.commons.math.analysis.UnivariateRealFunction f, double min, double max, double startValue) throws org.apache.commons.math.FunctionEvaluationException, org.apache.commons.math.MaxIterationsExceededException {
        setMaximalIterationCount(maxEval);
        return solve(f, min, max, startValue);
    }

    @Override // org.apache.commons.math.analysis.solvers.UnivariateRealSolver
    @java.lang.Deprecated
    public double solve(org.apache.commons.math.analysis.UnivariateRealFunction f, double min, double max, double startValue) throws org.apache.commons.math.FunctionEvaluationException, org.apache.commons.math.MaxIterationsExceededException {
        try {
            org.apache.commons.math.analysis.UnivariateRealFunction derivative = ((org.apache.commons.math.analysis.DifferentiableUnivariateRealFunction) f).derivative();
            clearResult();
            verifySequence(min, startValue, max);
            double x0 = startValue;
            for (int i = 0; i < this.maximalIterationCount; i++) {
                double x1 = x0 - (f.value(x0) / derivative.value(x0));
                if (org.apache.commons.math.util.FastMath.abs(x1 - x0) <= this.absoluteAccuracy) {
                    setResult(x1, i);
                    return x1;
                }
                x0 = x1;
            }
            throw new org.apache.commons.math.MaxIterationsExceededException(this.maximalIterationCount);
        } catch (java.lang.ClassCastException e) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.FUNCTION_NOT_DIFFERENTIABLE, new java.lang.Object[0]);
        }
    }
}
