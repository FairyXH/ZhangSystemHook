package org.apache.commons.math.optimization;

/* JADX INFO: loaded from: classes4.dex */
public interface UnivariateRealOptimizer extends org.apache.commons.math.ConvergingAlgorithm {
    int getEvaluations();

    double getFunctionValue() throws org.apache.commons.math.FunctionEvaluationException;

    int getMaxEvaluations();

    double getResult();

    double optimize(org.apache.commons.math.analysis.UnivariateRealFunction univariateRealFunction, org.apache.commons.math.optimization.GoalType goalType, double d, double d2) throws org.apache.commons.math.FunctionEvaluationException, org.apache.commons.math.ConvergenceException;

    double optimize(org.apache.commons.math.analysis.UnivariateRealFunction univariateRealFunction, org.apache.commons.math.optimization.GoalType goalType, double d, double d2, double d3) throws org.apache.commons.math.FunctionEvaluationException, org.apache.commons.math.ConvergenceException;

    void setMaxEvaluations(int i);
}
