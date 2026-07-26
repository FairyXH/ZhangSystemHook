package org.apache.commons.math.analysis.solvers;

/* JADX INFO: loaded from: classes4.dex */
public interface UnivariateRealSolver extends org.apache.commons.math.ConvergingAlgorithm {
    double getFunctionValue();

    double getFunctionValueAccuracy();

    double getResult();

    void resetFunctionValueAccuracy();

    void setFunctionValueAccuracy(double d);

    @java.lang.Deprecated
    double solve(double d, double d2) throws org.apache.commons.math.FunctionEvaluationException, org.apache.commons.math.ConvergenceException;

    @java.lang.Deprecated
    double solve(double d, double d2, double d3) throws org.apache.commons.math.FunctionEvaluationException, java.lang.IllegalArgumentException, org.apache.commons.math.ConvergenceException;

    @java.lang.Deprecated
    double solve(org.apache.commons.math.analysis.UnivariateRealFunction univariateRealFunction, double d, double d2) throws org.apache.commons.math.FunctionEvaluationException, org.apache.commons.math.ConvergenceException;

    @java.lang.Deprecated
    double solve(org.apache.commons.math.analysis.UnivariateRealFunction univariateRealFunction, double d, double d2, double d3) throws org.apache.commons.math.FunctionEvaluationException, java.lang.IllegalArgumentException, org.apache.commons.math.ConvergenceException;
}
