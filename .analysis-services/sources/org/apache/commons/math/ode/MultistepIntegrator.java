package org.apache.commons.math.ode;

/* JADX INFO: loaded from: classes4.dex */
public abstract class MultistepIntegrator extends org.apache.commons.math.ode.nonstiff.AdaptiveStepsizeIntegrator {
    private double exp;
    private double maxGrowth;
    private double minReduction;
    private final int nSteps;
    protected org.apache.commons.math.linear.Array2DRowRealMatrix nordsieck;
    private double safety;
    protected double[] scaled;
    private org.apache.commons.math.ode.FirstOrderIntegrator starter;

    public interface NordsieckTransformer {
        org.apache.commons.math.linear.RealMatrix initializeHighOrderDerivatives(double[] dArr, double[][] dArr2);
    }

    protected abstract org.apache.commons.math.linear.Array2DRowRealMatrix initializeHighOrderDerivatives(double[] dArr, double[][] dArr2);

    protected MultistepIntegrator(java.lang.String name, int nSteps, int order, double minStep, double maxStep, double scalAbsoluteTolerance, double scalRelativeTolerance) {
        super(name, minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
        if (nSteps <= 0) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.INTEGRATION_METHOD_NEEDS_AT_LEAST_ONE_PREVIOUS_POINT, name);
        }
        this.starter = new org.apache.commons.math.ode.nonstiff.DormandPrince853Integrator(minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
        this.nSteps = nSteps;
        this.exp = (-1.0d) / ((double) order);
        setSafety(0.9d);
        setMinReduction(0.2d);
        setMaxGrowth(org.apache.commons.math.util.FastMath.pow(2.0d, -this.exp));
    }

    protected MultistepIntegrator(java.lang.String name, int nSteps, int order, double minStep, double maxStep, double[] vecAbsoluteTolerance, double[] vecRelativeTolerance) {
        super(name, minStep, maxStep, vecAbsoluteTolerance, vecRelativeTolerance);
        this.starter = new org.apache.commons.math.ode.nonstiff.DormandPrince853Integrator(minStep, maxStep, vecAbsoluteTolerance, vecRelativeTolerance);
        this.nSteps = nSteps;
        this.exp = (-1.0d) / ((double) order);
        setSafety(0.9d);
        setMinReduction(0.2d);
        setMaxGrowth(org.apache.commons.math.util.FastMath.pow(2.0d, -this.exp));
    }

    public org.apache.commons.math.ode.ODEIntegrator getStarterIntegrator() {
        return this.starter;
    }

    public void setStarterIntegrator(org.apache.commons.math.ode.FirstOrderIntegrator starterIntegrator) {
        this.starter = starterIntegrator;
    }

    protected void start(double t0, double[] y0, double t) throws org.apache.commons.math.ode.DerivativeException, org.apache.commons.math.ode.IntegratorException {
        this.starter.clearEventHandlers();
        this.starter.clearStepHandlers();
        this.starter.addStepHandler(new org.apache.commons.math.ode.MultistepIntegrator.NordsieckInitializer(y0.length));
        try {
            this.starter.integrate(new org.apache.commons.math.ode.MultistepIntegrator.CountingDifferentialEquations(y0.length), t0, y0, t, new double[y0.length]);
        } catch (org.apache.commons.math.ode.DerivativeException mue) {
            if (!(mue instanceof org.apache.commons.math.ode.MultistepIntegrator.InitializationCompletedMarkerException)) {
                throw mue;
            }
        }
        this.starter.clearStepHandlers();
    }

    public double getMinReduction() {
        return this.minReduction;
    }

    public void setMinReduction(double minReduction) {
        this.minReduction = minReduction;
    }

    public double getMaxGrowth() {
        return this.maxGrowth;
    }

    public void setMaxGrowth(double maxGrowth) {
        this.maxGrowth = maxGrowth;
    }

    public double getSafety() {
        return this.safety;
    }

    public void setSafety(double safety) {
        this.safety = safety;
    }

    protected double computeStepGrowShrinkFactor(double error) {
        return org.apache.commons.math.util.FastMath.min(this.maxGrowth, org.apache.commons.math.util.FastMath.max(this.minReduction, this.safety * org.apache.commons.math.util.FastMath.pow(error, this.exp)));
    }

    private class NordsieckInitializer implements org.apache.commons.math.ode.sampling.StepHandler {
        private final int n;

        public NordsieckInitializer(int n) {
            this.n = n;
        }

        @Override // org.apache.commons.math.ode.sampling.StepHandler
        public void handleStep(org.apache.commons.math.ode.sampling.StepInterpolator interpolator, boolean isLast) throws org.apache.commons.math.ode.DerivativeException {
            double prev = interpolator.getPreviousTime();
            double curr = interpolator.getCurrentTime();
            org.apache.commons.math.ode.MultistepIntegrator.this.stepStart = prev;
            org.apache.commons.math.ode.MultistepIntegrator.this.stepSize = (curr - prev) / ((double) (org.apache.commons.math.ode.MultistepIntegrator.this.nSteps + 1));
            interpolator.setInterpolatedTime(prev);
            org.apache.commons.math.ode.MultistepIntegrator.this.scaled = (double[]) interpolator.getInterpolatedDerivatives().clone();
            for (int j = 0; j < this.n; j++) {
                double[] dArr = org.apache.commons.math.ode.MultistepIntegrator.this.scaled;
                dArr[j] = dArr[j] * org.apache.commons.math.ode.MultistepIntegrator.this.stepSize;
            }
            double[][] multistep = new double[org.apache.commons.math.ode.MultistepIntegrator.this.nSteps][];
            for (int i = 1; i <= org.apache.commons.math.ode.MultistepIntegrator.this.nSteps; i++) {
                interpolator.setInterpolatedTime((org.apache.commons.math.ode.MultistepIntegrator.this.stepSize * ((double) i)) + prev);
                double[] msI = (double[]) interpolator.getInterpolatedDerivatives().clone();
                for (int j2 = 0; j2 < this.n; j2++) {
                    msI[j2] = msI[j2] * org.apache.commons.math.ode.MultistepIntegrator.this.stepSize;
                }
                int j3 = i - 1;
                multistep[j3] = msI;
            }
            org.apache.commons.math.ode.MultistepIntegrator.this.nordsieck = org.apache.commons.math.ode.MultistepIntegrator.this.initializeHighOrderDerivatives(org.apache.commons.math.ode.MultistepIntegrator.this.scaled, multistep);
            throw new org.apache.commons.math.ode.MultistepIntegrator.InitializationCompletedMarkerException();
        }

        @Override // org.apache.commons.math.ode.sampling.StepHandler
        public boolean requiresDenseOutput() {
            return true;
        }

        @Override // org.apache.commons.math.ode.sampling.StepHandler
        public void reset() {
        }
    }

    private static class InitializationCompletedMarkerException extends org.apache.commons.math.ode.DerivativeException {
        private static final long serialVersionUID = -4105805787353488365L;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public InitializationCompletedMarkerException() {
            super(null);
        }
    }

    private class CountingDifferentialEquations implements org.apache.commons.math.ode.ExtendedFirstOrderDifferentialEquations {
        private final int dimension;

        public CountingDifferentialEquations(int dimension) {
            this.dimension = dimension;
        }

        @Override // org.apache.commons.math.ode.FirstOrderDifferentialEquations
        public void computeDerivatives(double t, double[] y, double[] dot) throws org.apache.commons.math.ode.DerivativeException {
            org.apache.commons.math.ode.MultistepIntegrator.this.computeDerivatives(t, y, dot);
        }

        @Override // org.apache.commons.math.ode.FirstOrderDifferentialEquations
        public int getDimension() {
            return this.dimension;
        }

        @Override // org.apache.commons.math.ode.ExtendedFirstOrderDifferentialEquations
        public int getMainSetDimension() {
            return org.apache.commons.math.ode.MultistepIntegrator.this.mainSetDimension;
        }
    }
}
