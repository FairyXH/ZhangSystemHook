package org.apache.commons.math.analysis.integration;

/* JADX INFO: loaded from: classes4.dex */
public class SimpsonIntegrator extends org.apache.commons.math.analysis.integration.UnivariateRealIntegratorImpl {
    @java.lang.Deprecated
    public SimpsonIntegrator(org.apache.commons.math.analysis.UnivariateRealFunction f) {
        super(f, 64);
    }

    public SimpsonIntegrator() {
        super(64);
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
        org.apache.commons.math.analysis.integration.TrapezoidIntegrator qtrap = new org.apache.commons.math.analysis.integration.TrapezoidIntegrator();
        double d = 3.0d;
        if (this.minimalIterationCount == 1) {
            setResult(((4.0d * qtrap.stage(f, min, max, 1)) - qtrap.stage(f, min, max, 0)) / 3.0d, 1);
            return this.result;
        }
        double oldt = qtrap.stage(f, min, max, 0);
        int i = 1;
        double olds = 0.0d;
        double oldt2 = oldt;
        while (i <= this.maximalIterationCount) {
            int i2 = i;
            double t = qtrap.stage(f, min, max, i);
            double s = ((t * 4.0d) - oldt2) / d;
            if (i2 >= this.minimalIterationCount) {
                double delta = org.apache.commons.math.util.FastMath.abs(s - olds);
                double rLimit = this.relativeAccuracy * (org.apache.commons.math.util.FastMath.abs(olds) + org.apache.commons.math.util.FastMath.abs(s)) * 0.5d;
                if (delta <= rLimit || delta <= this.absoluteAccuracy) {
                    setResult(s, i2);
                    return this.result;
                }
            }
            olds = s;
            oldt2 = t;
            i = i2 + 1;
            d = 3.0d;
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
