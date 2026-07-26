package org.apache.commons.math.ode.sampling;

/* JADX INFO: loaded from: classes4.dex */
public class DummyStepInterpolator extends org.apache.commons.math.ode.sampling.AbstractStepInterpolator {
    private static final long serialVersionUID = 1708010296707839488L;
    private double[] currentDerivative;

    public DummyStepInterpolator() {
        this.currentDerivative = null;
    }

    public DummyStepInterpolator(double[] y, double[] yDot, boolean forward) {
        super(y, forward);
        this.currentDerivative = yDot;
    }

    public DummyStepInterpolator(org.apache.commons.math.ode.sampling.DummyStepInterpolator interpolator) {
        super(interpolator);
        this.currentDerivative = (double[]) interpolator.currentDerivative.clone();
    }

    @Override // org.apache.commons.math.ode.sampling.AbstractStepInterpolator
    protected org.apache.commons.math.ode.sampling.StepInterpolator doCopy() {
        return new org.apache.commons.math.ode.sampling.DummyStepInterpolator(this);
    }

    @Override // org.apache.commons.math.ode.sampling.AbstractStepInterpolator
    protected void computeInterpolatedStateAndDerivatives(double theta, double oneMinusThetaH) {
        java.lang.System.arraycopy(this.currentState, 0, this.interpolatedState, 0, this.currentState.length);
        java.lang.System.arraycopy(this.currentDerivative, 0, this.interpolatedDerivatives, 0, this.currentDerivative.length);
    }

    @Override // org.apache.commons.math.ode.sampling.AbstractStepInterpolator, java.io.Externalizable
    public void writeExternal(java.io.ObjectOutput out) throws java.io.IOException {
        writeBaseExternal(out);
        if (this.currentDerivative != null) {
            for (int i = 0; i < this.currentDerivative.length; i++) {
                out.writeDouble(this.currentDerivative[i]);
            }
        }
    }

    @Override // org.apache.commons.math.ode.sampling.AbstractStepInterpolator, java.io.Externalizable
    public void readExternal(java.io.ObjectInput in) throws java.io.IOException {
        double t = readBaseExternal(in);
        if (this.currentState == null) {
            this.currentDerivative = null;
        } else {
            this.currentDerivative = new double[this.currentState.length];
            for (int i = 0; i < this.currentDerivative.length; i++) {
                this.currentDerivative[i] = in.readDouble();
            }
        }
        setInterpolatedTime(t);
    }
}
