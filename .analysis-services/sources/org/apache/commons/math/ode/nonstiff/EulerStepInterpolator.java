package org.apache.commons.math.ode.nonstiff;

/* JADX INFO: loaded from: classes4.dex */
class EulerStepInterpolator extends org.apache.commons.math.ode.nonstiff.RungeKuttaStepInterpolator {
    private static final long serialVersionUID = -7179861704951334960L;

    public EulerStepInterpolator() {
    }

    public EulerStepInterpolator(org.apache.commons.math.ode.nonstiff.EulerStepInterpolator interpolator) {
        super(interpolator);
    }

    @Override // org.apache.commons.math.ode.sampling.AbstractStepInterpolator
    protected org.apache.commons.math.ode.sampling.StepInterpolator doCopy() {
        return new org.apache.commons.math.ode.nonstiff.EulerStepInterpolator(this);
    }

    @Override // org.apache.commons.math.ode.sampling.AbstractStepInterpolator
    protected void computeInterpolatedStateAndDerivatives(double theta, double oneMinusThetaH) throws org.apache.commons.math.ode.DerivativeException {
        for (int i = 0; i < this.interpolatedState.length; i++) {
            this.interpolatedState[i] = this.currentState[i] - (this.yDotK[0][i] * oneMinusThetaH);
        }
        java.lang.System.arraycopy(this.yDotK[0], 0, this.interpolatedDerivatives, 0, this.interpolatedDerivatives.length);
    }
}
