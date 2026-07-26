package org.apache.commons.math.ode.nonstiff;

/* JADX INFO: loaded from: classes4.dex */
class MidpointStepInterpolator extends org.apache.commons.math.ode.nonstiff.RungeKuttaStepInterpolator {
    private static final long serialVersionUID = -865524111506042509L;

    public MidpointStepInterpolator() {
    }

    public MidpointStepInterpolator(org.apache.commons.math.ode.nonstiff.MidpointStepInterpolator interpolator) {
        super(interpolator);
    }

    @Override // org.apache.commons.math.ode.sampling.AbstractStepInterpolator
    protected org.apache.commons.math.ode.sampling.StepInterpolator doCopy() {
        return new org.apache.commons.math.ode.nonstiff.MidpointStepInterpolator(this);
    }

    @Override // org.apache.commons.math.ode.sampling.AbstractStepInterpolator
    protected void computeInterpolatedStateAndDerivatives(double theta, double oneMinusThetaH) throws org.apache.commons.math.ode.DerivativeException {
        double coeff1 = oneMinusThetaH * theta;
        double coeff2 = (theta + 1.0d) * oneMinusThetaH;
        double coeffDot2 = 2.0d * theta;
        double coeffDot1 = 1.0d - coeffDot2;
        for (int i = 0; i < this.interpolatedState.length; i++) {
            double yDot1 = this.yDotK[0][i];
            double yDot2 = this.yDotK[1][i];
            this.interpolatedState[i] = (this.currentState[i] + (coeff1 * yDot1)) - (coeff2 * yDot2);
            this.interpolatedDerivatives[i] = (coeffDot1 * yDot1) + (coeffDot2 * yDot2);
        }
    }
}
