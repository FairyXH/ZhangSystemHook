package org.apache.commons.math.ode.nonstiff;

/* JADX INFO: loaded from: classes4.dex */
public class AdamsMoultonIntegrator extends org.apache.commons.math.ode.nonstiff.AdamsIntegrator {
    private static final java.lang.String METHOD_NAME = "Adams-Moulton";

    public AdamsMoultonIntegrator(int nSteps, double minStep, double maxStep, double scalAbsoluteTolerance, double scalRelativeTolerance) throws java.lang.IllegalArgumentException {
        super(METHOD_NAME, nSteps, nSteps + 1, minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
    }

    public AdamsMoultonIntegrator(int nSteps, double minStep, double maxStep, double[] vecAbsoluteTolerance, double[] vecRelativeTolerance) throws java.lang.IllegalArgumentException {
        super(METHOD_NAME, nSteps, nSteps + 1, minStep, maxStep, vecAbsoluteTolerance, vecRelativeTolerance);
    }

    @Override // org.apache.commons.math.ode.nonstiff.AdamsIntegrator, org.apache.commons.math.ode.nonstiff.AdaptiveStepsizeIntegrator, org.apache.commons.math.ode.FirstOrderIntegrator
    public double integrate(org.apache.commons.math.ode.FirstOrderDifferentialEquations equations, double t0, double[] y0, double t, double[] y) throws org.apache.commons.math.ode.DerivativeException, org.apache.commons.math.ode.IntegratorException {
        double error;
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
        double[] yDot = new double[dArr.length];
        double[] yTmp = new double[dArr.length];
        double[] predictedScaled = new double[dArr.length];
        org.apache.commons.math.linear.Array2DRowRealMatrix nordsieckTmp = null;
        org.apache.commons.math.ode.sampling.NordsieckStepInterpolator interpolator = new org.apache.commons.math.ode.sampling.NordsieckStepInterpolator();
        interpolator.reinitialize(dArr2, forward);
        for (org.apache.commons.math.ode.sampling.StepHandler handler : this.stepHandlers) {
            handler.reset();
        }
        setStateInitialized(false);
        start(t0, y, t);
        interpolator.reinitialize(this.stepStart, this.stepSize, this.scaled, this.nordsieck);
        org.apache.commons.math.ode.sampling.NordsieckStepInterpolator interpolator2 = interpolator;
        interpolator2.storeTime(this.stepStart);
        double hNew = this.stepSize;
        interpolator2.rescale(hNew);
        this.isLastStep = false;
        while (true) {
            org.apache.commons.math.linear.Array2DRowRealMatrix nordsieckTmp2 = nordsieckTmp;
            double d = hNew;
            double hNew2 = 10.0d;
            double hNew3 = d;
            while (hNew2 >= 1.0d) {
                this.stepSize = hNew3;
                double d2 = this.stepStart;
                double error2 = this.stepSize;
                double stepEnd = d2 + error2;
                interpolator2.setInterpolatedTime(stepEnd);
                double hNew4 = hNew3;
                java.lang.System.arraycopy(interpolator2.getInterpolatedState(), 0, yTmp, 0, dArr.length);
                computeDerivatives(stepEnd, yTmp, yDot);
                for (int j = 0; j < dArr.length; j++) {
                    predictedScaled[j] = this.stepSize * yDot[j];
                }
                nordsieckTmp2 = updateHighOrderDerivativesPhase1(this.nordsieck);
                updateHighOrderDerivativesPhase2(this.scaled, predictedScaled, nordsieckTmp2);
                double error3 = nordsieckTmp2.walkInOptimizedOrder(new org.apache.commons.math.ode.nonstiff.AdamsMoultonIntegrator.Corrector(dArr2, predictedScaled, yTmp));
                if (error3 < 1.0d) {
                    error = error3;
                    hNew3 = hNew4;
                } else {
                    double factor = computeStepGrowShrinkFactor(error3);
                    error = error3;
                    double error4 = this.stepSize;
                    double hNew5 = filterStep(error4 * factor, forward, false);
                    interpolator2.rescale(hNew5);
                    hNew3 = hNew5;
                }
                hNew2 = error;
            }
            double error5 = hNew2;
            double hNew6 = hNew3;
            double error6 = this.stepStart;
            double stepEnd2 = error6 + this.stepSize;
            computeDerivatives(stepEnd2, yTmp, yDot);
            double[] correctedScaled = new double[dArr.length];
            for (int j2 = 0; j2 < dArr.length; j2++) {
                correctedScaled[j2] = this.stepSize * yDot[j2];
            }
            updateHighOrderDerivativesPhase2(predictedScaled, correctedScaled, nordsieckTmp2);
            java.lang.System.arraycopy(yTmp, 0, dArr2, 0, n);
            interpolator2.reinitialize(stepEnd2, this.stepSize, correctedScaled, nordsieckTmp2);
            interpolator2.storeTime(this.stepStart);
            interpolator2.shift();
            interpolator2.storeTime(stepEnd2);
            int n2 = n;
            org.apache.commons.math.ode.sampling.NordsieckStepInterpolator interpolator3 = interpolator2;
            org.apache.commons.math.linear.Array2DRowRealMatrix nordsieckTmp3 = nordsieckTmp2;
            this.stepStart = acceptStep(interpolator2, y, yDot, t);
            this.scaled = correctedScaled;
            this.nordsieck = nordsieckTmp3;
            if (this.isLastStep) {
                hNew = hNew6;
            } else {
                interpolator3.storeTime(this.stepStart);
                if (this.resetOccurred) {
                    start(this.stepStart, y, t);
                    interpolator3.reinitialize(this.stepStart, this.stepSize, this.scaled, this.nordsieck);
                }
                double factor2 = computeStepGrowShrinkFactor(error5);
                double scaledH = this.stepSize * factor2;
                double nextT = this.stepStart + scaledH;
                boolean nextIsLast = !forward ? nextT > t : nextT < t;
                double hNew7 = filterStep(scaledH, forward, nextIsLast);
                double filteredNextT = this.stepStart + hNew7;
                boolean filteredNextIsLast = !forward ? filteredNextT > t : filteredNextT < t;
                hNew = filteredNextIsLast ? t - this.stepStart : hNew7;
                interpolator3.rescale(hNew);
            }
            if (!this.isLastStep) {
                dArr = y0;
                dArr2 = y;
                interpolator2 = interpolator3;
                n = n2;
                nordsieckTmp = nordsieckTmp3;
            } else {
                double stopTime = this.stepStart;
                this.stepStart = Double.NaN;
                this.stepSize = Double.NaN;
                return stopTime;
            }
        }
    }

    private class Corrector implements org.apache.commons.math.linear.RealMatrixPreservingVisitor {
        private final double[] after;
        private final double[] before;
        private final double[] previous;
        private final double[] scaled;

        public Corrector(double[] previous, double[] scaled, double[] state) {
            this.previous = previous;
            this.scaled = scaled;
            this.after = state;
            this.before = (double[]) state.clone();
        }

        @Override // org.apache.commons.math.linear.RealMatrixPreservingVisitor
        public void start(int rows, int columns, int startRow, int endRow, int startColumn, int endColumn) {
            java.util.Arrays.fill(this.after, 0.0d);
        }

        @Override // org.apache.commons.math.linear.RealMatrixPreservingVisitor
        public void visit(int row, int column, double value) {
            if ((row & 1) == 0) {
                double[] dArr = this.after;
                dArr[column] = dArr[column] - value;
            } else {
                double[] dArr2 = this.after;
                dArr2[column] = dArr2[column] + value;
            }
        }

        @Override // org.apache.commons.math.linear.RealMatrixPreservingVisitor
        public double end() {
            double tol;
            double error = 0.0d;
            for (int i = 0; i < this.after.length; i++) {
                double[] dArr = this.after;
                dArr[i] = dArr[i] + this.previous[i] + this.scaled[i];
                if (i < org.apache.commons.math.ode.nonstiff.AdamsMoultonIntegrator.this.mainSetDimension) {
                    double yScale = org.apache.commons.math.util.FastMath.max(org.apache.commons.math.util.FastMath.abs(this.previous[i]), org.apache.commons.math.util.FastMath.abs(this.after[i]));
                    if (org.apache.commons.math.ode.nonstiff.AdamsMoultonIntegrator.this.vecAbsoluteTolerance == null) {
                        tol = org.apache.commons.math.ode.nonstiff.AdamsMoultonIntegrator.this.scalAbsoluteTolerance + (org.apache.commons.math.ode.nonstiff.AdamsMoultonIntegrator.this.scalRelativeTolerance * yScale);
                    } else {
                        tol = org.apache.commons.math.ode.nonstiff.AdamsMoultonIntegrator.this.vecAbsoluteTolerance[i] + (org.apache.commons.math.ode.nonstiff.AdamsMoultonIntegrator.this.vecRelativeTolerance[i] * yScale);
                    }
                    double ratio = (this.after[i] - this.before[i]) / tol;
                    error += ratio * ratio;
                }
            }
            return org.apache.commons.math.util.FastMath.sqrt(error / ((double) org.apache.commons.math.ode.nonstiff.AdamsMoultonIntegrator.this.mainSetDimension));
        }
    }
}
