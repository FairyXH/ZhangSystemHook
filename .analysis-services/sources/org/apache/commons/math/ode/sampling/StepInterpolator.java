package org.apache.commons.math.ode.sampling;

/* JADX INFO: loaded from: classes4.dex */
public interface StepInterpolator extends java.io.Externalizable {
    org.apache.commons.math.ode.sampling.StepInterpolator copy() throws org.apache.commons.math.ode.DerivativeException;

    double getCurrentTime();

    double[] getInterpolatedDerivatives() throws org.apache.commons.math.ode.DerivativeException;

    double[] getInterpolatedState() throws org.apache.commons.math.ode.DerivativeException;

    double getInterpolatedTime();

    double getPreviousTime();

    boolean isForward();

    void setInterpolatedTime(double d);
}
