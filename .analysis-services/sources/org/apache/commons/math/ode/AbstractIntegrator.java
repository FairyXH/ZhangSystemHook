package org.apache.commons.math.ode;

/* JADX INFO: loaded from: classes4.dex */
public abstract class AbstractIntegrator implements org.apache.commons.math.ode.FirstOrderIntegrator {
    private transient org.apache.commons.math.ode.FirstOrderDifferentialEquations equations;
    private int evaluations;
    private java.util.Collection<org.apache.commons.math.ode.events.EventState> eventsStates;
    protected boolean isLastStep;
    private int maxEvaluations;
    private final java.lang.String name;
    protected boolean resetOccurred;
    private boolean statesInitialized;
    protected java.util.Collection<org.apache.commons.math.ode.sampling.StepHandler> stepHandlers;
    protected double stepSize;
    protected double stepStart;

    public AbstractIntegrator(java.lang.String name) {
        this.name = name;
        this.stepHandlers = new java.util.ArrayList();
        this.stepStart = Double.NaN;
        this.stepSize = Double.NaN;
        this.eventsStates = new java.util.ArrayList();
        this.statesInitialized = false;
        setMaxEvaluations(-1);
        resetEvaluations();
    }

    protected AbstractIntegrator() {
        this(null);
    }

    @Override // org.apache.commons.math.ode.ODEIntegrator
    public java.lang.String getName() {
        return this.name;
    }

    @Override // org.apache.commons.math.ode.ODEIntegrator
    public void addStepHandler(org.apache.commons.math.ode.sampling.StepHandler handler) {
        this.stepHandlers.add(handler);
    }

    @Override // org.apache.commons.math.ode.ODEIntegrator
    public java.util.Collection<org.apache.commons.math.ode.sampling.StepHandler> getStepHandlers() {
        return java.util.Collections.unmodifiableCollection(this.stepHandlers);
    }

    @Override // org.apache.commons.math.ode.ODEIntegrator
    public void clearStepHandlers() {
        this.stepHandlers.clear();
    }

    @Override // org.apache.commons.math.ode.ODEIntegrator
    public void addEventHandler(org.apache.commons.math.ode.events.EventHandler handler, double maxCheckInterval, double convergence, int maxIterationCount) {
        this.eventsStates.add(new org.apache.commons.math.ode.events.EventState(handler, maxCheckInterval, convergence, maxIterationCount));
    }

    @Override // org.apache.commons.math.ode.ODEIntegrator
    public java.util.Collection<org.apache.commons.math.ode.events.EventHandler> getEventHandlers() {
        java.util.List<org.apache.commons.math.ode.events.EventHandler> list = new java.util.ArrayList<>();
        for (org.apache.commons.math.ode.events.EventState state : this.eventsStates) {
            list.add(state.getEventHandler());
        }
        return java.util.Collections.unmodifiableCollection(list);
    }

    @Override // org.apache.commons.math.ode.ODEIntegrator
    public void clearEventHandlers() {
        this.eventsStates.clear();
    }

    protected boolean requiresDenseOutput() {
        if (!this.eventsStates.isEmpty()) {
            return true;
        }
        for (org.apache.commons.math.ode.sampling.StepHandler handler : this.stepHandlers) {
            if (handler.requiresDenseOutput()) {
                return true;
            }
        }
        return false;
    }

    @Override // org.apache.commons.math.ode.ODEIntegrator
    public double getCurrentStepStart() {
        return this.stepStart;
    }

    @Override // org.apache.commons.math.ode.ODEIntegrator
    public double getCurrentSignedStepsize() {
        return this.stepSize;
    }

    @Override // org.apache.commons.math.ode.ODEIntegrator
    public void setMaxEvaluations(int maxEvaluations) {
        this.maxEvaluations = maxEvaluations < 0 ? Integer.MAX_VALUE : maxEvaluations;
    }

    @Override // org.apache.commons.math.ode.ODEIntegrator
    public int getMaxEvaluations() {
        return this.maxEvaluations;
    }

    @Override // org.apache.commons.math.ode.ODEIntegrator
    public int getEvaluations() {
        return this.evaluations;
    }

    protected void resetEvaluations() {
        this.evaluations = 0;
    }

    protected void setEquations(org.apache.commons.math.ode.FirstOrderDifferentialEquations equations) {
        this.equations = equations;
    }

    public void computeDerivatives(double t, double[] y, double[] yDot) throws org.apache.commons.math.ode.DerivativeException {
        int i = this.evaluations + 1;
        this.evaluations = i;
        if (i > this.maxEvaluations) {
            throw new org.apache.commons.math.ode.DerivativeException(new org.apache.commons.math.MaxEvaluationsExceededException(this.maxEvaluations));
        }
        this.equations.computeDerivatives(t, y, yDot);
    }

    protected void setStateInitialized(boolean stateInitialized) {
        this.statesInitialized = stateInitialized;
    }

    /* JADX WARN: Removed duplicated region for block: B:67:0x013b A[Catch: ConvergenceException -> 0x0125, EventException -> 0x0127, LOOP:5: B:65:0x0135->B:67:0x013b, LOOP_END, TRY_LEAVE, TryCatch #5 {ConvergenceException -> 0x0125, EventException -> 0x0127, blocks: (B:55:0x011e, B:64:0x012d, B:65:0x0135, B:67:0x013b), top: B:91:0x011e }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected double acceptStep(org.apache.commons.math.ode.sampling.AbstractStepInterpolator r21, double[] r22, double[] r23, double r24) throws org.apache.commons.math.ode.DerivativeException, org.apache.commons.math.ode.IntegratorException {
        /*
            Method dump skipped, instruction units count: 368
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.math.ode.AbstractIntegrator.acceptStep(org.apache.commons.math.ode.sampling.AbstractStepInterpolator, double[], double[], double):double");
    }

    protected void sanityChecks(org.apache.commons.math.ode.FirstOrderDifferentialEquations ode, double t0, double[] y0, double t, double[] y) throws org.apache.commons.math.ode.IntegratorException {
        if (ode.getDimension() != y0.length) {
            throw new org.apache.commons.math.ode.IntegratorException(org.apache.commons.math.exception.util.LocalizedFormats.DIMENSIONS_MISMATCH_SIMPLE, java.lang.Integer.valueOf(ode.getDimension()), java.lang.Integer.valueOf(y0.length));
        }
        if (ode.getDimension() != y.length) {
            throw new org.apache.commons.math.ode.IntegratorException(org.apache.commons.math.exception.util.LocalizedFormats.DIMENSIONS_MISMATCH_SIMPLE, java.lang.Integer.valueOf(ode.getDimension()), java.lang.Integer.valueOf(y.length));
        }
        if (org.apache.commons.math.util.FastMath.abs(t - t0) <= org.apache.commons.math.util.FastMath.max(org.apache.commons.math.util.FastMath.abs(t0), org.apache.commons.math.util.FastMath.abs(t)) * 1.0E-12d) {
            throw new org.apache.commons.math.ode.IntegratorException(org.apache.commons.math.exception.util.LocalizedFormats.TOO_SMALL_INTEGRATION_INTERVAL, java.lang.Double.valueOf(org.apache.commons.math.util.FastMath.abs(t - t0)));
        }
    }

    @java.lang.Deprecated
    protected org.apache.commons.math.ode.events.CombinedEventsManager addEndTimeChecker(double startTime, double endTime, org.apache.commons.math.ode.events.CombinedEventsManager manager) {
        org.apache.commons.math.ode.events.CombinedEventsManager newManager = new org.apache.commons.math.ode.events.CombinedEventsManager();
        for (org.apache.commons.math.ode.events.EventState state : manager.getEventsStates()) {
            newManager.addEventHandler(state.getEventHandler(), state.getMaxCheckInterval(), state.getConvergence(), state.getMaxIterationCount());
        }
        newManager.addEventHandler(new org.apache.commons.math.ode.AbstractIntegrator.EndTimeChecker(endTime), Double.POSITIVE_INFINITY, org.apache.commons.math.util.FastMath.ulp(org.apache.commons.math.util.FastMath.max(org.apache.commons.math.util.FastMath.abs(startTime), org.apache.commons.math.util.FastMath.abs(endTime))), 100);
        return newManager;
    }

    @java.lang.Deprecated
    private static class EndTimeChecker implements org.apache.commons.math.ode.events.EventHandler {
        private final double endTime;

        public EndTimeChecker(double endTime) {
            this.endTime = endTime;
        }

        @Override // org.apache.commons.math.ode.events.EventHandler
        public int eventOccurred(double t, double[] y, boolean increasing) {
            return 0;
        }

        @Override // org.apache.commons.math.ode.events.EventHandler
        public double g(double t, double[] y) {
            return t - this.endTime;
        }

        @Override // org.apache.commons.math.ode.events.EventHandler
        public void resetState(double t, double[] y) {
        }
    }
}
