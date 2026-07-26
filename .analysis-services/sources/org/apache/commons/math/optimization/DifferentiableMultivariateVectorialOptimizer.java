package org.apache.commons.math.optimization;

/* JADX INFO: loaded from: classes4.dex */
public interface DifferentiableMultivariateVectorialOptimizer {
    org.apache.commons.math.optimization.VectorialConvergenceChecker getConvergenceChecker();

    int getEvaluations();

    int getIterations();

    int getJacobianEvaluations();

    int getMaxEvaluations();

    int getMaxIterations();

    org.apache.commons.math.optimization.VectorialPointValuePair optimize(org.apache.commons.math.analysis.DifferentiableMultivariateVectorialFunction differentiableMultivariateVectorialFunction, double[] dArr, double[] dArr2, double[] dArr3) throws org.apache.commons.math.FunctionEvaluationException, org.apache.commons.math.optimization.OptimizationException, java.lang.IllegalArgumentException;

    void setConvergenceChecker(org.apache.commons.math.optimization.VectorialConvergenceChecker vectorialConvergenceChecker);

    void setMaxEvaluations(int i);

    void setMaxIterations(int i);
}
