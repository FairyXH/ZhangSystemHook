package org.apache.commons.math.optimization.fitting;

/* JADX INFO: loaded from: classes4.dex */
public interface ParametricRealFunction {
    double[] gradient(double d, double[] dArr) throws org.apache.commons.math.FunctionEvaluationException;

    double value(double d, double[] dArr) throws org.apache.commons.math.FunctionEvaluationException;
}
