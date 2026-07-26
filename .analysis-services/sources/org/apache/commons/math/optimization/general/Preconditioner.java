package org.apache.commons.math.optimization.general;

/* JADX INFO: loaded from: classes4.dex */
public interface Preconditioner {
    double[] precondition(double[] dArr, double[] dArr2) throws org.apache.commons.math.FunctionEvaluationException, java.lang.IllegalArgumentException;
}
