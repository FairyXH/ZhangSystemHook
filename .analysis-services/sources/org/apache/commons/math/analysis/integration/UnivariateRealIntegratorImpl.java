package org.apache.commons.math.analysis.integration;

/* JADX INFO: loaded from: classes4.dex */
public abstract class UnivariateRealIntegratorImpl extends org.apache.commons.math.ConvergingAlgorithmImpl implements org.apache.commons.math.analysis.integration.UnivariateRealIntegrator {
    private static final long serialVersionUID = 6248808456637441533L;
    protected int defaultMinimalIterationCount;

    @java.lang.Deprecated
    protected org.apache.commons.math.analysis.UnivariateRealFunction f;
    protected int minimalIterationCount;
    protected double result;
    protected boolean resultComputed;

    @java.lang.Deprecated
    protected UnivariateRealIntegratorImpl(org.apache.commons.math.analysis.UnivariateRealFunction f, int defaultMaximalIterationCount) throws java.lang.IllegalArgumentException {
        super(defaultMaximalIterationCount, 1.0E-15d);
        this.resultComputed = false;
        if (f == null) {
            throw new org.apache.commons.math.exception.NullArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.FUNCTION);
        }
        this.f = f;
        setRelativeAccuracy(1.0E-6d);
        this.defaultMinimalIterationCount = 3;
        this.minimalIterationCount = this.defaultMinimalIterationCount;
        verifyIterationCount();
    }

    protected UnivariateRealIntegratorImpl(int defaultMaximalIterationCount) throws java.lang.IllegalArgumentException {
        super(defaultMaximalIterationCount, 1.0E-15d);
        this.resultComputed = false;
        setRelativeAccuracy(1.0E-6d);
        this.defaultMinimalIterationCount = 3;
        this.minimalIterationCount = this.defaultMinimalIterationCount;
        verifyIterationCount();
    }

    @Override // org.apache.commons.math.analysis.integration.UnivariateRealIntegrator
    public double getResult() throws java.lang.IllegalStateException {
        if (this.resultComputed) {
            return this.result;
        }
        throw org.apache.commons.math.MathRuntimeException.createIllegalStateException(org.apache.commons.math.exception.util.LocalizedFormats.NO_RESULT_AVAILABLE, new java.lang.Object[0]);
    }

    protected final void setResult(double newResult, int iterationCount) {
        this.result = newResult;
        this.iterationCount = iterationCount;
        this.resultComputed = true;
    }

    protected final void clearResult() {
        this.iterationCount = 0;
        this.resultComputed = false;
    }

    @Override // org.apache.commons.math.analysis.integration.UnivariateRealIntegrator
    public void setMinimalIterationCount(int count) {
        this.minimalIterationCount = count;
    }

    @Override // org.apache.commons.math.analysis.integration.UnivariateRealIntegrator
    public int getMinimalIterationCount() {
        return this.minimalIterationCount;
    }

    @Override // org.apache.commons.math.analysis.integration.UnivariateRealIntegrator
    public void resetMinimalIterationCount() {
        this.minimalIterationCount = this.defaultMinimalIterationCount;
    }

    protected void verifyInterval(double lower, double upper) throws java.lang.IllegalArgumentException {
        if (lower >= upper) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.ENDPOINTS_NOT_AN_INTERVAL, java.lang.Double.valueOf(lower), java.lang.Double.valueOf(upper));
        }
    }

    protected void verifyIterationCount() throws java.lang.IllegalArgumentException {
        if (this.minimalIterationCount <= 0 || this.maximalIterationCount <= this.minimalIterationCount) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.INVALID_ITERATIONS_LIMITS, java.lang.Integer.valueOf(this.minimalIterationCount), java.lang.Integer.valueOf(this.maximalIterationCount));
        }
    }
}
