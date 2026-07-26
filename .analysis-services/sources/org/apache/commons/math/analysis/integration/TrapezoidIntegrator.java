package org.apache.commons.math.analysis.integration;

/* JADX INFO: loaded from: classes4.dex */
public class TrapezoidIntegrator extends org.apache.commons.math.analysis.integration.UnivariateRealIntegratorImpl {
    private double s;

    @java.lang.Deprecated
    public TrapezoidIntegrator(org.apache.commons.math.analysis.UnivariateRealFunction f) {
        super(f, 64);
    }

    public TrapezoidIntegrator() {
        super(64);
    }

    double stage(org.apache.commons.math.analysis.UnivariateRealFunction f, double min, double max, int n) throws org.apache.commons.math.FunctionEvaluationException {
        if (n == 0) {
            this.s = (max - min) * 0.5d * (f.value(min) + f.value(max));
            return this.s;
        }
        long np = 1 << (n - 1);
        double sum = 0.0d;
        double spacing = (max - min) / np;
        double x = min + (spacing * 0.5d);
        for (long i = 0; i < np; i++) {
            sum += f.value(x);
            x += spacing;
        }
        this.s = (this.s + (sum * spacing)) * 0.5d;
        return this.s;
    }

    @Override // org.apache.commons.math.analysis.integration.UnivariateRealIntegrator
    @java.lang.Deprecated
    public double integrate(double min, double max) throws org.apache.commons.math.FunctionEvaluationException, org.apache.commons.math.MaxIterationsExceededException, java.lang.IllegalArgumentException {
        return integrate(this.f, min, max);
    }

    @Override // org.apache.commons.math.analysis.integration.UnivariateRealIntegrator
    public double integrate(org.apache.commons.math.analysis.UnivariateRealFunction f, double min, double max) throws org.apache.commons.math.FunctionEvaluationException, org.apache.commons.math.MaxIterationsExceededException, java.lang.IllegalArgumentException {
        clearResult();
        verifyInterval(min, max);
        verifyIterationCount();
        double oldt = stage(f, min, max, 0);
        double oldt2 = oldt;
        for (int i = 1; i <= this.maximalIterationCount; i++) {
            double t = stage(f, min, max, i);
            if (i >= this.minimalIterationCount) {
                double delta = org.apache.commons.math.util.FastMath.abs(t - oldt2);
                double rLimit = this.relativeAccuracy * (org.apache.commons.math.util.FastMath.abs(oldt2) + org.apache.commons.math.util.FastMath.abs(t)) * 0.5d;
                if (delta > rLimit) {
                    double rLimit2 = this.absoluteAccuracy;
                    if (delta <= rLimit2) {
                    }
                }
                setResult(t, i);
                return this.result;
            }
            oldt2 = t;
        }
        throw new org.apache.commons.math.MaxIterationsExceededException(this.maximalIterationCount);
    }

    @Override // org.apache.commons.math.analysis.integration.UnivariateRealIntegratorImpl
    protected void verifyIterationCount() throws java.lang.IllegalArgumentException {
        super.verifyIterationCount();
        if (this.maximalIterationCount > 64) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.INVALID_ITERATIONS_LIMITS, 0, 64);
        }
    }
}
