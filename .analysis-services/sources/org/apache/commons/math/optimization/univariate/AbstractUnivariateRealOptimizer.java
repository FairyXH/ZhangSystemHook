package org.apache.commons.math.optimization.univariate;

/* JADX INFO: loaded from: classes4.dex */
public abstract class AbstractUnivariateRealOptimizer extends org.apache.commons.math.ConvergingAlgorithmImpl implements org.apache.commons.math.optimization.UnivariateRealOptimizer {
    private int evaluations;
    private org.apache.commons.math.analysis.UnivariateRealFunction function;
    protected double functionValue;
    private int maxEvaluations;
    private org.apache.commons.math.optimization.GoalType optimizationGoal;
    protected double result;
    protected boolean resultComputed;
    private double searchMax;
    private double searchMin;
    private double searchStart;

    @java.lang.Deprecated
    protected AbstractUnivariateRealOptimizer(int defaultMaximalIterationCount, double defaultAbsoluteAccuracy) {
        super(defaultMaximalIterationCount, defaultAbsoluteAccuracy);
        this.resultComputed = false;
        setMaxEvaluations(Integer.MAX_VALUE);
    }

    protected AbstractUnivariateRealOptimizer() {
    }

    @java.lang.Deprecated
    protected void checkResultComputed() {
        if (!this.resultComputed) {
            throw new org.apache.commons.math.exception.NoDataException();
        }
    }

    @Override // org.apache.commons.math.optimization.UnivariateRealOptimizer
    public double getResult() {
        if (!this.resultComputed) {
            throw new org.apache.commons.math.exception.NoDataException();
        }
        return this.result;
    }

    @Override // org.apache.commons.math.optimization.UnivariateRealOptimizer
    public double getFunctionValue() throws org.apache.commons.math.FunctionEvaluationException {
        if (java.lang.Double.isNaN(this.functionValue)) {
            double opt = getResult();
            this.functionValue = this.function.value(opt);
        }
        double opt2 = this.functionValue;
        return opt2;
    }

    @java.lang.Deprecated
    protected final void setResult(double x, double fx, int iterationCount) {
        this.result = x;
        this.functionValue = fx;
        this.iterationCount = iterationCount;
        this.resultComputed = true;
    }

    @java.lang.Deprecated
    protected final void clearResult() {
        this.resultComputed = false;
    }

    @Override // org.apache.commons.math.optimization.UnivariateRealOptimizer
    public void setMaxEvaluations(int maxEvaluations) {
        this.maxEvaluations = maxEvaluations;
    }

    @Override // org.apache.commons.math.optimization.UnivariateRealOptimizer
    public int getMaxEvaluations() {
        return this.maxEvaluations;
    }

    @Override // org.apache.commons.math.optimization.UnivariateRealOptimizer
    public int getEvaluations() {
        return this.evaluations;
    }

    public org.apache.commons.math.optimization.GoalType getGoalType() {
        return this.optimizationGoal;
    }

    public double getMin() {
        return this.searchMin;
    }

    public double getMax() {
        return this.searchMax;
    }

    public double getStartValue() {
        return this.searchStart;
    }

    @java.lang.Deprecated
    protected double computeObjectiveValue(org.apache.commons.math.analysis.UnivariateRealFunction f, double point) throws org.apache.commons.math.FunctionEvaluationException {
        int i = this.evaluations + 1;
        this.evaluations = i;
        if (i > this.maxEvaluations) {
            throw new org.apache.commons.math.FunctionEvaluationException(new org.apache.commons.math.MaxEvaluationsExceededException(this.maxEvaluations), point);
        }
        return f.value(point);
    }

    protected double computeObjectiveValue(double point) throws org.apache.commons.math.FunctionEvaluationException {
        int i = this.evaluations + 1;
        this.evaluations = i;
        if (i > this.maxEvaluations) {
            this.resultComputed = false;
            throw new org.apache.commons.math.FunctionEvaluationException(new org.apache.commons.math.MaxEvaluationsExceededException(this.maxEvaluations), point);
        }
        return this.function.value(point);
    }

    @Override // org.apache.commons.math.optimization.UnivariateRealOptimizer
    public double optimize(org.apache.commons.math.analysis.UnivariateRealFunction f, org.apache.commons.math.optimization.GoalType goal, double min, double max, double startValue) throws org.apache.commons.math.FunctionEvaluationException, org.apache.commons.math.MaxIterationsExceededException {
        this.searchMin = min;
        this.searchMax = max;
        this.searchStart = startValue;
        this.optimizationGoal = goal;
        this.function = f;
        this.functionValue = Double.NaN;
        this.evaluations = 0;
        resetIterationsCounter();
        this.result = doOptimize();
        this.resultComputed = true;
        return this.result;
    }

    protected void setFunctionValue(double functionValue) {
        this.functionValue = functionValue;
    }

    @Override // org.apache.commons.math.optimization.UnivariateRealOptimizer
    public double optimize(org.apache.commons.math.analysis.UnivariateRealFunction f, org.apache.commons.math.optimization.GoalType goal, double min, double max) throws org.apache.commons.math.FunctionEvaluationException, org.apache.commons.math.MaxIterationsExceededException {
        return optimize(f, goal, min, max, min + ((max - min) * 0.5d));
    }

    protected double doOptimize() throws org.apache.commons.math.FunctionEvaluationException, org.apache.commons.math.MaxIterationsExceededException {
        throw new org.apache.commons.math.exception.MathUnsupportedOperationException(org.apache.commons.math.exception.util.LocalizedFormats.NOT_OVERRIDEN, new java.lang.Object[0]);
    }
}
