package org.apache.commons.math.ode.nonstiff;

/* JADX INFO: loaded from: classes4.dex */
public abstract class AdamsIntegrator extends org.apache.commons.math.ode.MultistepIntegrator {
    private final org.apache.commons.math.ode.nonstiff.AdamsNordsieckTransformer transformer;

    @Override // org.apache.commons.math.ode.nonstiff.AdaptiveStepsizeIntegrator, org.apache.commons.math.ode.FirstOrderIntegrator
    public abstract double integrate(org.apache.commons.math.ode.FirstOrderDifferentialEquations firstOrderDifferentialEquations, double d, double[] dArr, double d2, double[] dArr2) throws org.apache.commons.math.ode.DerivativeException, org.apache.commons.math.ode.IntegratorException;

    public AdamsIntegrator(java.lang.String name, int nSteps, int order, double minStep, double maxStep, double scalAbsoluteTolerance, double scalRelativeTolerance) throws java.lang.IllegalArgumentException {
        super(name, nSteps, order, minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
        this.transformer = org.apache.commons.math.ode.nonstiff.AdamsNordsieckTransformer.getInstance(nSteps);
    }

    public AdamsIntegrator(java.lang.String name, int nSteps, int order, double minStep, double maxStep, double[] vecAbsoluteTolerance, double[] vecRelativeTolerance) throws java.lang.IllegalArgumentException {
        super(name, nSteps, order, minStep, maxStep, vecAbsoluteTolerance, vecRelativeTolerance);
        this.transformer = org.apache.commons.math.ode.nonstiff.AdamsNordsieckTransformer.getInstance(nSteps);
    }

    @Override // org.apache.commons.math.ode.MultistepIntegrator
    protected org.apache.commons.math.linear.Array2DRowRealMatrix initializeHighOrderDerivatives(double[] first, double[][] multistep) {
        return this.transformer.initializeHighOrderDerivatives(first, multistep);
    }

    public org.apache.commons.math.linear.Array2DRowRealMatrix updateHighOrderDerivativesPhase1(org.apache.commons.math.linear.Array2DRowRealMatrix highOrder) {
        return this.transformer.updateHighOrderDerivativesPhase1(highOrder);
    }

    public void updateHighOrderDerivativesPhase2(double[] start, double[] end, org.apache.commons.math.linear.Array2DRowRealMatrix highOrder) {
        this.transformer.updateHighOrderDerivativesPhase2(start, end, highOrder);
    }
}
