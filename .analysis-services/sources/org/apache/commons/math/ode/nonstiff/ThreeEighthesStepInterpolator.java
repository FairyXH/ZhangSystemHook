package org.apache.commons.math.ode.nonstiff;

/* JADX INFO: loaded from: classes4.dex */
class ThreeEighthesStepInterpolator extends org.apache.commons.math.ode.nonstiff.RungeKuttaStepInterpolator {
    private static final long serialVersionUID = -3345024435978721931L;

    public ThreeEighthesStepInterpolator() {
    }

    public ThreeEighthesStepInterpolator(org.apache.commons.math.ode.nonstiff.ThreeEighthesStepInterpolator interpolator) {
        super(interpolator);
    }

    @Override // org.apache.commons.math.ode.sampling.AbstractStepInterpolator
    protected org.apache.commons.math.ode.sampling.StepInterpolator doCopy() {
        return new org.apache.commons.math.ode.nonstiff.ThreeEighthesStepInterpolator(this);
    }

    @Override // org.apache.commons.math.ode.sampling.AbstractStepInterpolator
    protected void computeInterpolatedStateAndDerivatives(double theta, double oneMinusThetaH) throws org.apache.commons.math.ode.DerivativeException {
        double fourTheta2 = theta * 4.0d * theta;
        double s = oneMinusThetaH / 8.0d;
        double coeff1 = ((1.0d - (7.0d * theta)) + (fourTheta2 * 2.0d)) * s;
        double coeff2 = s * 3.0d * ((theta + 1.0d) - fourTheta2);
        double coeff3 = 3.0d * s * (theta + 1.0d);
        double coeff4 = (theta + 1.0d + fourTheta2) * s;
        double coeffDot3 = 0.75d * theta;
        double coeffDot1 = (((4.0d * theta) - 5.0d) * coeffDot3) + 1.0d;
        double coeffDot2 = (5.0d - (6.0d * theta)) * coeffDot3;
        double coeffDot4 = ((2.0d * theta) - 1.0d) * coeffDot3;
        int i = 0;
        while (i < this.interpolatedState.length) {
            double yDot1 = this.yDotK[0][i];
            double yDot2 = this.yDotK[1][i];
            double yDot3 = this.yDotK[2][i];
            double yDot4 = this.yDotK[3][i];
            this.interpolatedState[i] = (((this.currentState[i] - (coeff1 * yDot1)) - (coeff2 * yDot2)) - (coeff3 * yDot3)) - (coeff4 * yDot4);
            this.interpolatedDerivatives[i] = (coeffDot1 * yDot1) + (coeffDot2 * yDot2) + (coeffDot3 * yDot3) + (coeffDot4 * yDot4);
            i++;
            fourTheta2 = fourTheta2;
        }
    }
}
