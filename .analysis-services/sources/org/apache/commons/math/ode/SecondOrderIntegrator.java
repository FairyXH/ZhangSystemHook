package org.apache.commons.math.ode;

/* JADX INFO: loaded from: classes4.dex */
public interface SecondOrderIntegrator extends org.apache.commons.math.ode.ODEIntegrator {
    void integrate(org.apache.commons.math.ode.SecondOrderDifferentialEquations secondOrderDifferentialEquations, double d, double[] dArr, double[] dArr2, double d2, double[] dArr3, double[] dArr4) throws org.apache.commons.math.ode.DerivativeException, org.apache.commons.math.ode.IntegratorException;
}
