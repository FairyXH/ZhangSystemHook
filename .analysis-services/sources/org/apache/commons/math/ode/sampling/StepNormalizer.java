package org.apache.commons.math.ode.sampling;

/* JADX INFO: loaded from: classes4.dex */
public class StepNormalizer implements org.apache.commons.math.ode.sampling.StepHandler {
    private boolean forward;
    private double h;
    private final org.apache.commons.math.ode.sampling.FixedStepHandler handler;
    private double[] lastDerivatives;
    private double[] lastState;
    private double lastTime;

    public StepNormalizer(double h, org.apache.commons.math.ode.sampling.FixedStepHandler handler) {
        this.h = org.apache.commons.math.util.FastMath.abs(h);
        this.handler = handler;
        reset();
    }

    @Override // org.apache.commons.math.ode.sampling.StepHandler
    public boolean requiresDenseOutput() {
        return true;
    }

    @Override // org.apache.commons.math.ode.sampling.StepHandler
    public void reset() {
        this.lastTime = Double.NaN;
        this.lastState = null;
        this.lastDerivatives = null;
        this.forward = true;
    }

    @Override // org.apache.commons.math.ode.sampling.StepHandler
    public void handleStep(org.apache.commons.math.ode.sampling.StepInterpolator interpolator, boolean isLast) throws org.apache.commons.math.ode.DerivativeException {
        if (this.lastState == null) {
            this.lastTime = interpolator.getPreviousTime();
            interpolator.setInterpolatedTime(this.lastTime);
            this.lastState = (double[]) interpolator.getInterpolatedState().clone();
            this.lastDerivatives = (double[]) interpolator.getInterpolatedDerivatives().clone();
            this.forward = interpolator.getCurrentTime() >= this.lastTime;
            if (!this.forward) {
                this.h = -this.h;
            }
        }
        double nextTime = this.lastTime + this.h;
        boolean nextInStep = this.forward ^ (nextTime > interpolator.getCurrentTime());
        while (nextInStep) {
            this.handler.handleStep(this.lastTime, this.lastState, this.lastDerivatives, false);
            this.lastTime = nextTime;
            interpolator.setInterpolatedTime(this.lastTime);
            java.lang.System.arraycopy(interpolator.getInterpolatedState(), 0, this.lastState, 0, this.lastState.length);
            java.lang.System.arraycopy(interpolator.getInterpolatedDerivatives(), 0, this.lastDerivatives, 0, this.lastDerivatives.length);
            nextTime += this.h;
            nextInStep = this.forward ^ (nextTime > interpolator.getCurrentTime());
        }
        if (isLast) {
            this.handler.handleStep(this.lastTime, this.lastState, this.lastDerivatives, true);
        }
    }
}
