package org.apache.commons.math.ode.jacobians;

/* JADX INFO: loaded from: classes4.dex */
@java.lang.Deprecated
public interface ODEWithJacobians extends org.apache.commons.math.ode.FirstOrderDifferentialEquations {
    void computeJacobians(double d, double[] dArr, double[] dArr2, double[][] dArr3, double[][] dArr4) throws org.apache.commons.math.ode.DerivativeException;

    int getParametersDimension();
}
