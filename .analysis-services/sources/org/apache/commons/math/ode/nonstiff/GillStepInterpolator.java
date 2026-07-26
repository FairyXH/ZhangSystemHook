package org.apache.commons.math.ode.nonstiff;

/* JADX INFO: loaded from: classes4.dex */
class GillStepInterpolator extends org.apache.commons.math.ode.nonstiff.RungeKuttaStepInterpolator {
    private static final double TWO_MINUS_SQRT_2 = 2.0d - org.apache.commons.math.util.FastMath.sqrt(2.0d);
    private static final double TWO_PLUS_SQRT_2 = org.apache.commons.math.util.FastMath.sqrt(2.0d) + 2.0d;
    private static final long serialVersionUID = -107804074496313322L;

    public GillStepInterpolator() {
    }

    public GillStepInterpolator(org.apache.commons.math.ode.nonstiff.GillStepInterpolator interpolator) {
        super(interpolator);
    }

    @Override // org.apache.commons.math.ode.sampling.AbstractStepInterpolator
    protected org.apache.commons.math.ode.sampling.StepInterpolator doCopy() {
        return new org.apache.commons.math.ode.nonstiff.GillStepInterpolator(this);
    }

    @Override // org.apache.commons.math.ode.sampling.AbstractStepInterpolator
    protected void computeInterpolatedStateAndDerivatives(double theta, double oneMinusThetaH) throws org.apache.commons.math.ode.DerivativeException {
        double twoTheta = 2.0d * theta;
        double fourTheta = 4.0d * theta;
        double s = oneMinusThetaH / 6.0d;
        double oMt = 1.0d - theta;
        double soMt = s * oMt;
        double c23 = (twoTheta + 1.0d) * soMt;
        double coeff1 = (1.0d - fourTheta) * soMt;
        double coeff2 = TWO_MINUS_SQRT_2 * c23;
        double coeff3 = TWO_PLUS_SQRT_2 * c23;
        double coeff4 = (((fourTheta + 1.0d) * theta) + 1.0d) * s;
        double coeffDot1 = ((twoTheta - 3.0d) * theta) + 1.0d;
        double cDot23 = theta * oMt;
        double coeffDot2 = TWO_MINUS_SQRT_2 * cDot23;
        double coeffDot3 = TWO_PLUS_SQRT_2 * cDot23;
        double coeffDot4 = (twoTheta - 1.0d) * theta;
        int i = 0;
        while (i < this.interpolatedState.length) {
            double yDot1 = this.yDotK[0][i];
            double yDot2 = this.yDotK[1][i];
            double yDot3 = this.yDotK[2][i];
            double yDot4 = this.yDotK[3][i];
            this.interpolatedState[i] = (((this.currentState[i] - (coeff1 * yDot1)) - (coeff2 * yDot2)) - (coeff3 * yDot3)) - (coeff4 * yDot4);
            this.interpolatedDerivatives[i] = (coeffDot1 * yDot1) + (coeffDot2 * yDot2) + (coeffDot3 * yDot3) + (coeffDot4 * yDot4);
            i++;
            fourTheta = fourTheta;
        }
    }
}
