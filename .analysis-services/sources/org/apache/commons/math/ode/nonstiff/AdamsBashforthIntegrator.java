package org.apache.commons.math.ode.nonstiff;

/* JADX INFO: loaded from: classes4.dex */
public class AdamsBashforthIntegrator extends org.apache.commons.math.ode.nonstiff.AdamsIntegrator {
    private static final java.lang.String METHOD_NAME = "Adams-Bashforth";

    public AdamsBashforthIntegrator(int nSteps, double minStep, double maxStep, double scalAbsoluteTolerance, double scalRelativeTolerance) throws java.lang.IllegalArgumentException {
        super(METHOD_NAME, nSteps, nSteps, minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
    }

    public AdamsBashforthIntegrator(int nSteps, double minStep, double maxStep, double[] vecAbsoluteTolerance, double[] vecRelativeTolerance) throws java.lang.IllegalArgumentException {
        super(METHOD_NAME, nSteps, nSteps, minStep, maxStep, vecAbsoluteTolerance, vecRelativeTolerance);
    }

    @Override // org.apache.commons.math.ode.nonstiff.AdamsIntegrator, org.apache.commons.math.ode.nonstiff.AdaptiveStepsizeIntegrator, org.apache.commons.math.ode.FirstOrderIntegrator
    public double integrate(org.apache.commons.math.ode.FirstOrderDifferentialEquations equations, double t0, double[] y0, double t, double[] y) throws org.apache.commons.math.ode.DerivativeException, org.apache.commons.math.ode.IntegratorException {
        double error;
        double hNew;
        double d;
        double d2;
        double[] dArr = y0;
        double[] dArr2 = y;
        int n = dArr.length;
        sanityChecks(equations, t0, y0, t, y);
        setEquations(equations);
        resetEvaluations();
        boolean forward = t > t0;
        if (dArr2 != dArr) {
            java.lang.System.arraycopy(dArr, 0, dArr2, 0, n);
        }
        double[] yDot = new double[n];
        org.apache.commons.math.ode.sampling.NordsieckStepInterpolator interpolator = new org.apache.commons.math.ode.sampling.NordsieckStepInterpolator();
        interpolator.reinitialize(dArr2, forward);
        for (org.apache.commons.math.ode.sampling.StepHandler handler : this.stepHandlers) {
            handler.reset();
        }
        setStateInitialized(false);
        start(t0, y, t);
        interpolator.reinitialize(this.stepStart, this.stepSize, this.scaled, this.nordsieck);
        interpolator.storeTime(this.stepStart);
        int lastRow = this.nordsieck.getRowDimension() - 1;
        double hNew2 = this.stepSize;
        interpolator.rescale(hNew2);
        this.isLastStep = false;
        while (true) {
            double d3 = hNew2;
            double error2 = 10.0d;
            double hNew3 = d3;
            while (error2 >= 1.0d) {
                this.stepSize = hNew3;
                double error3 = 0.0d;
                int i = 0;
                while (i < this.mainSetDimension) {
                    double yScale = org.apache.commons.math.util.FastMath.abs(dArr2[i]);
                    if (this.vecAbsoluteTolerance == null) {
                        d2 = this.scalAbsoluteTolerance;
                        hNew = hNew3;
                        d = this.scalRelativeTolerance * yScale;
                    } else {
                        hNew = hNew3;
                        d = this.vecAbsoluteTolerance[i];
                        d2 = this.vecRelativeTolerance[i] * yScale;
                    }
                    double tol = d2 + d;
                    double ratio = this.nordsieck.getEntry(lastRow, i) / tol;
                    error3 += ratio * ratio;
                    i++;
                    hNew3 = hNew;
                }
                double hNew4 = hNew3;
                error2 = org.apache.commons.math.util.FastMath.sqrt(error3 / ((double) this.mainSetDimension));
                if (error2 < 1.0d) {
                    hNew3 = hNew4;
                } else {
                    double factor = computeStepGrowShrinkFactor(error2);
                    double hNew5 = filterStep(this.stepSize * factor, forward, false);
                    interpolator.rescale(hNew5);
                    hNew3 = hNew5;
                }
            }
            double hNew6 = hNew3;
            double stepEnd = this.stepStart + this.stepSize;
            interpolator.shift();
            interpolator.setInterpolatedTime(stepEnd);
            java.lang.System.arraycopy(interpolator.getInterpolatedState(), 0, dArr2, 0, dArr.length);
            computeDerivatives(stepEnd, dArr2, yDot);
            double[] predictedScaled = new double[dArr.length];
            int j = 0;
            while (j < dArr.length) {
                double error4 = error2;
                double error5 = this.stepSize;
                predictedScaled[j] = error5 * yDot[j];
                j++;
                error2 = error4;
            }
            double error6 = error2;
            org.apache.commons.math.linear.Array2DRowRealMatrix nordsieckTmp = updateHighOrderDerivativesPhase1(this.nordsieck);
            updateHighOrderDerivativesPhase2(this.scaled, predictedScaled, nordsieckTmp);
            interpolator.reinitialize(stepEnd, this.stepSize, predictedScaled, nordsieckTmp);
            interpolator.storeTime(stepEnd);
            int lastRow2 = lastRow;
            this.stepStart = acceptStep(interpolator, y, yDot, t);
            this.scaled = predictedScaled;
            this.nordsieck = nordsieckTmp;
            interpolator.reinitialize(stepEnd, this.stepSize, this.scaled, this.nordsieck);
            if (this.isLastStep) {
                hNew2 = hNew6;
            } else {
                interpolator.storeTime(this.stepStart);
                if (!this.resetOccurred) {
                    error = error6;
                } else {
                    start(this.stepStart, y, t);
                    error = error6;
                    interpolator.reinitialize(this.stepStart, this.stepSize, this.scaled, this.nordsieck);
                }
                double factor2 = computeStepGrowShrinkFactor(error);
                double scaledH = this.stepSize * factor2;
                double nextT = this.stepStart + scaledH;
                boolean nextIsLast = !forward ? nextT > t : nextT < t;
                double hNew7 = filterStep(scaledH, forward, nextIsLast);
                double factor3 = this.stepStart;
                double filteredNextT = factor3 + hNew7;
                boolean filteredNextIsLast = !forward ? filteredNextT > t : filteredNextT < t;
                hNew2 = filteredNextIsLast ? t - this.stepStart : hNew7;
                interpolator.rescale(hNew2);
            }
            if (!this.isLastStep) {
                dArr = y0;
                dArr2 = y;
                lastRow = lastRow2;
            } else {
                double stopTime = this.stepStart;
                resetInternalState();
                return stopTime;
            }
        }
    }
}
