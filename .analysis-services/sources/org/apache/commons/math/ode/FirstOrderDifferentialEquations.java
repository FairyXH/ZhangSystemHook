package org.apache.commons.math.ode;

/* JADX INFO: loaded from: classes4.dex */
public interface FirstOrderDifferentialEquations {
    void computeDerivatives(double d, double[] dArr, double[] dArr2) throws org.apache.commons.math.ode.DerivativeException;

    int getDimension();
}
