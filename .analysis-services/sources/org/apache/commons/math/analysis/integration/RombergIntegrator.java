package org.apache.commons.math.analysis.integration;

/* JADX INFO: loaded from: classes4.dex */
public class RombergIntegrator extends org.apache.commons.math.analysis.integration.UnivariateRealIntegratorImpl {
    @java.lang.Deprecated
    public RombergIntegrator(org.apache.commons.math.analysis.UnivariateRealFunction f) {
        super(f, 32);
    }

    public RombergIntegrator() {
        super(32);
    }

    @Override // org.apache.commons.math.analysis.integration.UnivariateRealIntegrator
    @java.lang.Deprecated
    public double integrate(double min, double max) throws org.apache.commons.math.FunctionEvaluationException, org.apache.commons.math.MaxIterationsExceededException, java.lang.IllegalArgumentException {
        return integrate(this.f, min, max);
    }

    @Override // org.apache.commons.math.analysis.integration.UnivariateRealIntegrator
    public double integrate(org.apache.commons.math.analysis.UnivariateRealFunction f, double min, double max) throws org.apache.commons.math.FunctionEvaluationException, org.apache.commons.math.MaxIterationsExceededException, java.lang.IllegalArgumentException {
        int i;
        int m;
        double[] previousRow;
        int m2 = this.maximalIterationCount + 1;
        double[] previousRow2 = new double[m2];
        double[] currentRow = new double[m2];
        clearResult();
        verifyInterval(min, max);
        verifyIterationCount();
        org.apache.commons.math.analysis.integration.TrapezoidIntegrator qtrap = new org.apache.commons.math.analysis.integration.TrapezoidIntegrator();
        currentRow[0] = qtrap.stage(f, min, max, 0);
        double olds = currentRow[0];
        double olds2 = olds;
        int i2 = 1;
        while (i2 <= this.maximalIterationCount) {
            double[] tmpRow = previousRow2;
            double[] previousRow3 = currentRow;
            currentRow = tmpRow;
            int i3 = i2;
            currentRow[0] = qtrap.stage(f, min, max, i2);
            int j = 1;
            while (true) {
                i = i3;
                if (j > i) {
                    break;
                }
                double r = (1 << (j * 2)) - 1;
                double tIJm1 = currentRow[j - 1];
                currentRow[j] = tIJm1 + ((tIJm1 - previousRow3[j - 1]) / r);
                j++;
                i3 = i;
            }
            double s = currentRow[i];
            if (i < this.minimalIterationCount) {
                m = m2;
                previousRow = previousRow3;
            } else {
                double delta = org.apache.commons.math.util.FastMath.abs(s - olds2);
                double rLimit = this.relativeAccuracy * (org.apache.commons.math.util.FastMath.abs(olds2) + org.apache.commons.math.util.FastMath.abs(s)) * 0.5d;
                if (delta > rLimit) {
                    m = m2;
                    previousRow = previousRow3;
                    if (delta <= this.absoluteAccuracy) {
                    }
                }
                setResult(s, i);
                return this.result;
            }
            olds2 = s;
            i2 = i + 1;
            m2 = m;
            previousRow2 = previousRow;
        }
        throw new org.apache.commons.math.MaxIterationsExceededException(this.maximalIterationCount);
    }

    @Override // org.apache.commons.math.analysis.integration.UnivariateRealIntegratorImpl
    protected void verifyIterationCount() throws java.lang.IllegalArgumentException {
        super.verifyIterationCount();
        if (this.maximalIterationCount > 32) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.INVALID_ITERATIONS_LIMITS, 0, 32);
        }
    }
}
