package org.apache.commons.math.ode.nonstiff;

/* JADX INFO: loaded from: classes4.dex */
public abstract class AdaptiveStepsizeIntegrator extends org.apache.commons.math.ode.AbstractIntegrator {
    private double initialStep;
    protected int mainSetDimension;
    private final double maxStep;
    private final double minStep;
    protected final double scalAbsoluteTolerance;
    protected final double scalRelativeTolerance;
    protected final double[] vecAbsoluteTolerance;
    protected final double[] vecRelativeTolerance;

    @Override // org.apache.commons.math.ode.FirstOrderIntegrator
    public abstract double integrate(org.apache.commons.math.ode.FirstOrderDifferentialEquations firstOrderDifferentialEquations, double d, double[] dArr, double d2, double[] dArr2) throws org.apache.commons.math.ode.DerivativeException, org.apache.commons.math.ode.IntegratorException;

    public AdaptiveStepsizeIntegrator(java.lang.String name, double minStep, double maxStep, double scalAbsoluteTolerance, double scalRelativeTolerance) {
        super(name);
        this.minStep = org.apache.commons.math.util.FastMath.abs(minStep);
        this.maxStep = org.apache.commons.math.util.FastMath.abs(maxStep);
        this.initialStep = -1.0d;
        this.scalAbsoluteTolerance = scalAbsoluteTolerance;
        this.scalRelativeTolerance = scalRelativeTolerance;
        this.vecAbsoluteTolerance = null;
        this.vecRelativeTolerance = null;
        resetInternalState();
    }

    public AdaptiveStepsizeIntegrator(java.lang.String name, double minStep, double maxStep, double[] vecAbsoluteTolerance, double[] vecRelativeTolerance) {
        super(name);
        this.minStep = minStep;
        this.maxStep = maxStep;
        this.initialStep = -1.0d;
        this.scalAbsoluteTolerance = 0.0d;
        this.scalRelativeTolerance = 0.0d;
        this.vecAbsoluteTolerance = (double[]) vecAbsoluteTolerance.clone();
        this.vecRelativeTolerance = (double[]) vecRelativeTolerance.clone();
        resetInternalState();
    }

    public void setInitialStepSize(double initialStepSize) {
        if (initialStepSize < this.minStep || initialStepSize > this.maxStep) {
            this.initialStep = -1.0d;
        } else {
            this.initialStep = initialStepSize;
        }
    }

    @Override // org.apache.commons.math.ode.AbstractIntegrator
    protected void sanityChecks(org.apache.commons.math.ode.FirstOrderDifferentialEquations equations, double t0, double[] y0, double t, double[] y) throws org.apache.commons.math.ode.IntegratorException {
        super.sanityChecks(equations, t0, y0, t, y);
        if (equations instanceof org.apache.commons.math.ode.ExtendedFirstOrderDifferentialEquations) {
            this.mainSetDimension = ((org.apache.commons.math.ode.ExtendedFirstOrderDifferentialEquations) equations).getMainSetDimension();
        } else {
            this.mainSetDimension = equations.getDimension();
        }
        if (this.vecAbsoluteTolerance != null && this.vecAbsoluteTolerance.length != this.mainSetDimension) {
            throw new org.apache.commons.math.ode.IntegratorException(org.apache.commons.math.exception.util.LocalizedFormats.DIMENSIONS_MISMATCH_SIMPLE, java.lang.Integer.valueOf(this.mainSetDimension), java.lang.Integer.valueOf(this.vecAbsoluteTolerance.length));
        }
        if (this.vecRelativeTolerance != null && this.vecRelativeTolerance.length != this.mainSetDimension) {
            throw new org.apache.commons.math.ode.IntegratorException(org.apache.commons.math.exception.util.LocalizedFormats.DIMENSIONS_MISMATCH_SIMPLE, java.lang.Integer.valueOf(this.mainSetDimension), java.lang.Integer.valueOf(this.vecRelativeTolerance.length));
        }
    }

    public double initializeStep(org.apache.commons.math.ode.FirstOrderDifferentialEquations equations, boolean forward, int order, double[] scale, double t0, double[] y0, double[] yDot0, double[] y1, double[] yDot1) throws org.apache.commons.math.ode.DerivativeException {
        double h1;
        if (this.initialStep > 0.0d) {
            double d = this.initialStep;
            return forward ? d : -d;
        }
        double yOnScale2 = 0.0d;
        double yDotOnScale2 = 0.0d;
        for (int j = 0; j < scale.length; j++) {
            double ratio = y0[j] / scale[j];
            yOnScale2 += ratio * ratio;
            double ratio2 = yDot0[j] / scale[j];
            yDotOnScale2 += ratio2 * ratio2;
        }
        double h = (yOnScale2 < 1.0E-10d || yDotOnScale2 < 1.0E-10d) ? 1.0E-6d : org.apache.commons.math.util.FastMath.sqrt(yOnScale2 / yDotOnScale2) * 0.01d;
        if (!forward) {
            h = -h;
        }
        for (int j2 = 0; j2 < y0.length; j2++) {
            y1[j2] = y0[j2] + (yDot0[j2] * h);
        }
        computeDerivatives(t0 + h, y1, yDot1);
        double yDDotOnScale = 0.0d;
        for (int j3 = 0; j3 < scale.length; j3++) {
            double ratio3 = (yDot1[j3] - yDot0[j3]) / scale[j3];
            yDDotOnScale += ratio3 * ratio3;
        }
        double yDDotOnScale2 = org.apache.commons.math.util.FastMath.sqrt(yDDotOnScale) / h;
        double yDDotOnScale3 = org.apache.commons.math.util.FastMath.sqrt(yDotOnScale2);
        double maxInv2 = org.apache.commons.math.util.FastMath.max(yDDotOnScale3, yDDotOnScale2);
        if (maxInv2 < 1.0E-15d) {
            h1 = org.apache.commons.math.util.FastMath.max(1.0E-6d, org.apache.commons.math.util.FastMath.abs(h) * 0.001d);
        } else {
            h1 = org.apache.commons.math.util.FastMath.pow(0.01d / maxInv2, 1.0d / ((double) order));
        }
        double h2 = org.apache.commons.math.util.FastMath.max(org.apache.commons.math.util.FastMath.min(org.apache.commons.math.util.FastMath.abs(h) * 100.0d, h1), org.apache.commons.math.util.FastMath.abs(t0) * 1.0E-12d);
        if (h2 < getMinStep()) {
            h2 = getMinStep();
        }
        if (h2 > getMaxStep()) {
            h2 = getMaxStep();
        }
        if (!forward) {
            return -h2;
        }
        return h2;
    }

    protected double filterStep(double h, boolean forward, boolean acceptSmall) throws org.apache.commons.math.ode.IntegratorException {
        double filteredH = h;
        if (org.apache.commons.math.util.FastMath.abs(h) < this.minStep) {
            if (acceptSmall) {
                double d = this.minStep;
                if (!forward) {
                    d = -d;
                }
                filteredH = d;
            } else {
                throw new org.apache.commons.math.ode.IntegratorException(org.apache.commons.math.exception.util.LocalizedFormats.MINIMAL_STEPSIZE_REACHED_DURING_INTEGRATION, java.lang.Double.valueOf(this.minStep), java.lang.Double.valueOf(org.apache.commons.math.util.FastMath.abs(h)));
            }
        }
        if (filteredH > this.maxStep) {
            double filteredH2 = this.maxStep;
            return filteredH2;
        }
        if (filteredH < (-this.maxStep)) {
            double filteredH3 = -this.maxStep;
            return filteredH3;
        }
        return filteredH;
    }

    @Override // org.apache.commons.math.ode.AbstractIntegrator, org.apache.commons.math.ode.ODEIntegrator
    public double getCurrentStepStart() {
        return this.stepStart;
    }

    protected void resetInternalState() {
        this.stepStart = Double.NaN;
        this.stepSize = org.apache.commons.math.util.FastMath.sqrt(this.minStep * this.maxStep);
    }

    public double getMinStep() {
        return this.minStep;
    }

    public double getMaxStep() {
        return this.maxStep;
    }
}
