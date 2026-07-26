package org.apache.commons.math.analysis.integration;

/* JADX INFO: loaded from: classes4.dex */
public interface UnivariateRealIntegrator extends org.apache.commons.math.ConvergingAlgorithm {
    int getMinimalIterationCount();

    double getResult() throws java.lang.IllegalStateException;

    @java.lang.Deprecated
    double integrate(double d, double d2) throws org.apache.commons.math.FunctionEvaluationException, java.lang.IllegalArgumentException, org.apache.commons.math.ConvergenceException;

    double integrate(org.apache.commons.math.analysis.UnivariateRealFunction univariateRealFunction, double d, double d2) throws org.apache.commons.math.FunctionEvaluationException, java.lang.IllegalArgumentException, org.apache.commons.math.ConvergenceException;

    void resetMinimalIterationCount();

    void setMinimalIterationCount(int i);
}
