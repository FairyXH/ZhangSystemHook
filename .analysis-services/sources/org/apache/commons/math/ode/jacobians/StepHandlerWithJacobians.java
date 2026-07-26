package org.apache.commons.math.ode.jacobians;

/* JADX INFO: loaded from: classes4.dex */
@java.lang.Deprecated
public interface StepHandlerWithJacobians {
    void handleStep(org.apache.commons.math.ode.jacobians.StepInterpolatorWithJacobians stepInterpolatorWithJacobians, boolean z) throws org.apache.commons.math.ode.DerivativeException;

    boolean requiresDenseOutput();

    void reset();
}
