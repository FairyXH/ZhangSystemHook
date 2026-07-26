package org.apache.commons.math.ode.nonstiff;

/* JADX INFO: loaded from: classes4.dex */
class HighamHall54StepInterpolator extends org.apache.commons.math.ode.nonstiff.RungeKuttaStepInterpolator {
    private static final long serialVersionUID = -3583240427587318654L;

    public HighamHall54StepInterpolator() {
    }

    public HighamHall54StepInterpolator(org.apache.commons.math.ode.nonstiff.HighamHall54StepInterpolator interpolator) {
        super(interpolator);
    }

    @Override // org.apache.commons.math.ode.sampling.AbstractStepInterpolator
    protected org.apache.commons.math.ode.sampling.StepInterpolator doCopy() {
        return new org.apache.commons.math.ode.nonstiff.HighamHall54StepInterpolator(this);
    }

    @Override // org.apache.commons.math.ode.sampling.AbstractStepInterpolator
    protected void computeInterpolatedStateAndDerivatives(double theta, double oneMinusThetaH) throws org.apache.commons.math.ode.DerivativeException {
        double theta2 = theta * theta;
        double b0 = this.h * ((((((((((-5.0d) * theta) / 2.0d) + 5.333333333333333d) * theta) - 3.75d) * theta) + 1.0d) * theta) - 0.08333333333333333d);
        double b2 = this.h * (((((((135.0d * theta) / 8.0d) - 30.375d) * theta) + 14.34375d) * theta2) - 0.84375d);
        double b3 = this.h * (((((((-30.0d) * theta) + 50.666666666666664d) * theta) - 22.0d) * theta2) + 1.3333333333333333d);
        double b4 = this.h * (((((((125.0d * theta) / 8.0d) - 26.041666666666668d) * theta) + 11.71875d) * theta2) - 1.3020833333333333d);
        double b5 = this.h * (((((theta * 5.0d) / 12.0d) - 0.3125d) * theta2) - 0.10416666666666667d);
        double bDot0 = (((theta * (16.0d - (10.0d * theta))) - 7.5d) * theta) + 1.0d;
        double bDot2 = ((((67.5d * theta) - 91.125d) * theta) + 28.6875d) * theta;
        double bDot3 = ((theta * (152.0d - (120.0d * theta))) - 44.0d) * theta;
        double bDot4 = ((((62.5d * theta) - 78.125d) * theta) + 23.4375d) * theta;
        double bDot5 = ((5.0d * theta) / 8.0d) * ((2.0d * theta) - 1.0d);
        int i = 0;
        while (i < this.interpolatedState.length) {
            double yDot0 = this.yDotK[0][i];
            double yDot2 = this.yDotK[2][i];
            double yDot3 = this.yDotK[3][i];
            double yDot4 = this.yDotK[4][i];
            double yDot5 = this.yDotK[5][i];
            this.interpolatedState[i] = this.currentState[i] + (b0 * yDot0) + (b2 * yDot2) + (b3 * yDot3) + (b4 * yDot4) + (b5 * yDot5);
            this.interpolatedDerivatives[i] = (bDot0 * yDot0) + (bDot2 * yDot2) + (bDot3 * yDot3) + (bDot4 * yDot4) + (bDot5 * yDot5);
            i++;
            theta2 = theta2;
        }
    }
}
