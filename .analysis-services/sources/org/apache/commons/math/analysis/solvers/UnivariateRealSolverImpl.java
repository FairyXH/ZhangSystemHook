package org.apache.commons.math.analysis.solvers;

/* JADX INFO: loaded from: classes4.dex */
@java.lang.Deprecated
public abstract class UnivariateRealSolverImpl extends org.apache.commons.math.ConvergingAlgorithmImpl implements org.apache.commons.math.analysis.solvers.UnivariateRealSolver {
    protected double defaultFunctionValueAccuracy;

    @java.lang.Deprecated
    protected org.apache.commons.math.analysis.UnivariateRealFunction f;
    protected double functionValue;
    protected double functionValueAccuracy;
    protected double result;
    protected boolean resultComputed;

    @java.lang.Deprecated
    protected UnivariateRealSolverImpl(org.apache.commons.math.analysis.UnivariateRealFunction f, int defaultMaximalIterationCount, double defaultAbsoluteAccuracy) {
        super(defaultMaximalIterationCount, defaultAbsoluteAccuracy);
        this.resultComputed = false;
        if (f == null) {
            throw new org.apache.commons.math.exception.NullArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.FUNCTION);
        }
        this.f = f;
        this.defaultFunctionValueAccuracy = 1.0E-15d;
        this.functionValueAccuracy = this.defaultFunctionValueAccuracy;
    }

    protected UnivariateRealSolverImpl(int defaultMaximalIterationCount, double defaultAbsoluteAccuracy) {
        super(defaultMaximalIterationCount, defaultAbsoluteAccuracy);
        this.resultComputed = false;
        this.defaultFunctionValueAccuracy = 1.0E-15d;
        this.functionValueAccuracy = this.defaultFunctionValueAccuracy;
    }

    protected void checkResultComputed() throws java.lang.IllegalStateException {
        if (!this.resultComputed) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalStateException(org.apache.commons.math.exception.util.LocalizedFormats.NO_RESULT_AVAILABLE, new java.lang.Object[0]);
        }
    }

    @Override // org.apache.commons.math.analysis.solvers.UnivariateRealSolver
    public double getResult() {
        checkResultComputed();
        return this.result;
    }

    @Override // org.apache.commons.math.analysis.solvers.UnivariateRealSolver
    public double getFunctionValue() {
        checkResultComputed();
        return this.functionValue;
    }

    @Override // org.apache.commons.math.analysis.solvers.UnivariateRealSolver
    public void setFunctionValueAccuracy(double accuracy) {
        this.functionValueAccuracy = accuracy;
    }

    @Override // org.apache.commons.math.analysis.solvers.UnivariateRealSolver
    public double getFunctionValueAccuracy() {
        return this.functionValueAccuracy;
    }

    @Override // org.apache.commons.math.analysis.solvers.UnivariateRealSolver
    public void resetFunctionValueAccuracy() {
        this.functionValueAccuracy = this.defaultFunctionValueAccuracy;
    }

    public double solve(int maxEval, org.apache.commons.math.analysis.UnivariateRealFunction function, double min, double max) throws org.apache.commons.math.FunctionEvaluationException, org.apache.commons.math.ConvergenceException {
        throw org.apache.commons.math.MathRuntimeException.createUnsupportedOperationException(org.apache.commons.math.exception.util.LocalizedFormats.NOT_OVERRIDEN, new java.lang.Object[0]);
    }

    public double solve(int maxEval, org.apache.commons.math.analysis.UnivariateRealFunction function, double min, double max, double startValue) throws org.apache.commons.math.FunctionEvaluationException, java.lang.IllegalArgumentException, org.apache.commons.math.ConvergenceException {
        throw org.apache.commons.math.MathRuntimeException.createUnsupportedOperationException(org.apache.commons.math.exception.util.LocalizedFormats.NOT_OVERRIDEN, new java.lang.Object[0]);
    }

    protected final void setResult(double newResult, int iterationCount) {
        this.result = newResult;
        this.iterationCount = iterationCount;
        this.resultComputed = true;
    }

    protected final void setResult(double x, double fx, int iterationCount) {
        this.result = x;
        this.functionValue = fx;
        this.iterationCount = iterationCount;
        this.resultComputed = true;
    }

    protected final void clearResult() {
        this.iterationCount = 0;
        this.resultComputed = false;
    }

    protected boolean isBracketing(double lower, double upper, org.apache.commons.math.analysis.UnivariateRealFunction function) throws org.apache.commons.math.FunctionEvaluationException {
        double f1 = function.value(lower);
        double f2 = function.value(upper);
        return (f1 > 0.0d && f2 < 0.0d) || (f1 < 0.0d && f2 > 0.0d);
    }

    protected boolean isSequence(double start, double mid, double end) {
        return start < mid && mid < end;
    }

    protected void verifyInterval(double lower, double upper) {
        if (lower >= upper) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.ENDPOINTS_NOT_AN_INTERVAL, java.lang.Double.valueOf(lower), java.lang.Double.valueOf(upper));
        }
    }

    protected void verifySequence(double lower, double initial, double upper) {
        if (!isSequence(lower, initial, upper)) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.INVALID_INTERVAL_INITIAL_VALUE_PARAMETERS, java.lang.Double.valueOf(lower), java.lang.Double.valueOf(initial), java.lang.Double.valueOf(upper));
        }
    }

    protected void verifyBracketing(double lower, double upper, org.apache.commons.math.analysis.UnivariateRealFunction function) throws org.apache.commons.math.FunctionEvaluationException {
        verifyInterval(lower, upper);
        if (!isBracketing(lower, upper, function)) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.SAME_SIGN_AT_ENDPOINTS, java.lang.Double.valueOf(lower), java.lang.Double.valueOf(upper), java.lang.Double.valueOf(function.value(lower)), java.lang.Double.valueOf(function.value(upper)));
        }
    }
}
