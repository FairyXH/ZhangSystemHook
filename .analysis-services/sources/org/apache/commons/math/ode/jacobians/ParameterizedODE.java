package org.apache.commons.math.ode.jacobians;

/* JADX INFO: loaded from: classes4.dex */
@java.lang.Deprecated
public interface ParameterizedODE extends org.apache.commons.math.ode.FirstOrderDifferentialEquations {
    int getParametersDimension();

    void setParameter(int i, double d);
}
