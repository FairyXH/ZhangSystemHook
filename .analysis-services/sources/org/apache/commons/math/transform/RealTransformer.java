package org.apache.commons.math.transform;

/* JADX INFO: loaded from: classes4.dex */
public interface RealTransformer {
    double[] inversetransform(org.apache.commons.math.analysis.UnivariateRealFunction univariateRealFunction, double d, double d2, int i) throws org.apache.commons.math.FunctionEvaluationException, java.lang.IllegalArgumentException;

    double[] inversetransform(double[] dArr) throws java.lang.IllegalArgumentException;

    double[] transform(org.apache.commons.math.analysis.UnivariateRealFunction univariateRealFunction, double d, double d2, int i) throws org.apache.commons.math.FunctionEvaluationException, java.lang.IllegalArgumentException;

    double[] transform(double[] dArr) throws java.lang.IllegalArgumentException;
}
