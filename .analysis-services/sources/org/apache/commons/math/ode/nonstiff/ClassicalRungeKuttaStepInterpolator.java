package org.apache.commons.math.ode.nonstiff;

/* JADX INFO: loaded from: classes4.dex */
class ClassicalRungeKuttaStepInterpolator extends org.apache.commons.math.ode.nonstiff.RungeKuttaStepInterpolator {
    private static final long serialVersionUID = -6576285612589783992L;

    public ClassicalRungeKuttaStepInterpolator() {
    }

    public ClassicalRungeKuttaStepInterpolator(org.apache.commons.math.ode.nonstiff.ClassicalRungeKuttaStepInterpolator interpolator) {
        super(interpolator);
    }

    @Override // org.apache.commons.math.ode.sampling.AbstractStepInterpolator
    protected org.apache.commons.math.ode.sampling.StepInterpolator doCopy() {
        return new org.apache.commons.math.ode.nonstiff.ClassicalRungeKuttaStepInterpolator(this);
    }

    @Override // org.apache.commons.math.ode.sampling.AbstractStepInterpolator
    protected void computeInterpolatedStateAndDerivatives(double theta, double oneMinusThetaH) throws org.apache.commons.math.ode.DerivativeException {
        double fourTheta = 4.0d * theta;
        double oneMinusTheta = 1.0d - theta;
        double oneMinus2Theta = 1.0d - (theta * 2.0d);
        double s = oneMinusThetaH / 6.0d;
        double coeff1 = ((((-fourTheta) + 5.0d) * theta) - 1.0d) * s;
        double coeff23 = (((fourTheta - 2.0d) * theta) - 2.0d) * s;
        double coeff12 = -fourTheta;
        double coeff4 = (((coeff12 - 1.0d) * theta) - 1.0d) * s;
        double coeffDot1 = oneMinusTheta * oneMinus2Theta;
        double coeffDot23 = 2.0d * theta * oneMinusTheta;
        double coeffDot4 = (-theta) * oneMinus2Theta;
        int i = 0;
        while (i < this.interpolatedState.length) {
            double yDot1 = this.yDotK[0][i];
            double yDot23 = this.yDotK[1][i] + this.yDotK[2][i];
            double yDot4 = this.yDotK[3][i];
            this.interpolatedState[i] = this.currentState[i] + (coeff1 * yDot1) + (coeff23 * yDot23) + (coeff4 * yDot4);
            this.interpolatedDerivatives[i] = (coeffDot1 * yDot1) + (coeffDot23 * yDot23) + (coeffDot4 * yDot4);
            i++;
            oneMinusTheta = oneMinusTheta;
        }
    }
}
